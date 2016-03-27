package me.efe.efetutorial.listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;
import com.mewin.WGRegionEvents.events.RegionLeftEvent;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efeserver.reform.Fatigue;
import me.efe.efetutorial.EfeTutorial;
import me.efe.efetutorial.TutorialState;
import me.efe.skilltree.UserData;

public class PlayerListener implements Listener {
	public EfeTutorial plugin;
	public HashMap<UUID, Object[]> respawnMap = new HashMap<UUID, Object[]>();
	public Location spawnLoc;
	public Location startLoc;
	public Location villageLoc;
	public Location zombieLoc;
	
	public PlayerListener(EfeTutorial plugin) {
		this.plugin = plugin;
		
		World world = EfeServer.getInstance().world;
		
		this.startLoc = new Location(world, 3000.5, 230, 1527.5, 179.0F, 0.0F);
		this.spawnLoc = new Location(world, 0.5, 70.5, 0.5);
		this.villageLoc = new Location(world, -38, 81, 13, -145.0F, 0.0F);
		this.zombieLoc = new Location(world, 3100, 65, 2200);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (event.getEntity().getWorld().equals(EfeServer.getInstance().efeIsland.world)) {
			Player player = event.getEntity();
			
			respawnMap.put(player.getUniqueId(), new Object[]{player.getFoodLevel(), player.getSaturation()});
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		int state = TutorialState.get(event.getPlayer());
		
		if (state >= TutorialState.QUEST_ACCEPTED) {
			if (event.getPlayer().hasMetadata("death_island")) {
				event.setRespawnLocation((Location) event.getPlayer().getMetadata("death_island").get(0).value());
				event.getPlayer().removeMetadata("death_island", EfeServer.getInstance().efeIsland);
				
				if (respawnMap.containsKey(event.getPlayer().getUniqueId())) {
					Object[] arr = respawnMap.get(event.getPlayer().getUniqueId());
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							event.getPlayer().setFoodLevel((int) arr[0]);
							event.getPlayer().setSaturation((float) arr[1]);
							respawnMap.remove(event.getPlayer().getUniqueId());
						}
					}, 1L);
					
					event.getPlayer().sendMessage("§a▒§r 유저 월드에서 부활하여 경험치와 허기가 유지되었습니다.");
				}
				
				return;
			}
			
			event.setRespawnLocation(spawnLoc);
			
			me.efe.efemobs.rudish.UserData data = new me.efe.efemobs.rudish.UserData(event.getPlayer());
			
			event.getPlayer().sendMessage("§a▒§r 부활하여 30%의 경험치와 "+data.getSoul()+" 소울을 잃었습니다.");
			
			event.getPlayer().setExp((float) (event.getPlayer().getExp() * 0.7));
			data.setSoul(0);
			
			TitleAPI.sendTitle(event.getPlayer(), 10, 40, 20, "§c§lRESPAWN", "§c약간의 경험치와 모든 소울을 잃었습니다.");
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
			
		} else if (state == TutorialState.WELCOME_TO_POLARIS) {
			event.setRespawnLocation(villageLoc);
			event.getPlayer().getInventory().clear();
			
			PlayerData data = PlayerData.get(event.getPlayer());
			
			if (data.getOptionBoat()) {
				
				if (data.getOptionMenu())
					event.getPlayer().getInventory().setItem(7, EfeServer.getInstance().myboat.getBoatItem(event.getPlayer()));
				else
					event.getPlayer().getInventory().setItem(8, EfeServer.getInstance().myboat.getBoatItem(event.getPlayer()));
			}
			
			if (data.getOptionMenu()) {
				event.getPlayer().getInventory().setItem(8, EfeUtils.item.createDisplayItem("§e/메뉴", new ItemStack(Material.NETHER_STAR), 
						new String[]{"클릭하면 메인 메뉴를 엽니다."}));
			}
			
			TitleAPI.sendTitle(event.getPlayer(), 10, 40, 20, "§c§lRESPAWN", "§c이런, IQ 두 자리시군요.");
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
		} else {
			event.setRespawnLocation(startLoc);
			event.getPlayer().getInventory().clear();
			
			TutorialState.set(event.getPlayer(), TutorialState.ON_THE_MAGIC_CIRCLE);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (plugin.introHandler.isInIntro(event.getPlayer())) {
			event.getPlayer().teleport(plugin.introHandler.playerLoc);
		}
	}
	
	@EventHandler
	public void onRegionLeft(RegionLeftEvent event) {
		if (event.getRegion().getId().equals("intro") && TutorialState.get(event.getPlayer()) == TutorialState.ON_THE_MAGIC_CIRCLE) {
			event.getPlayer().setFallDistance(0.0F);
			event.getPlayer().teleport(startLoc);
		} else if (event.getRegion().getId().equals("intro_start") && TutorialState.get(event.getPlayer()) == TutorialState.ON_THE_MAGIC_CIRCLE) {
			TutorialState.set(event.getPlayer(), TutorialState.FALLING);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (event.getPlayer() == null) return;
					
					plugin.introHandler.startIntro(event.getPlayer());
				}
			}, 50L);
		} else if (event.getRegion().getId().equals("first_house") &&
				TutorialState.WELCOME_TO_POLARIS <= TutorialState.get(event.getPlayer()) && TutorialState.get(event.getPlayer()) < TutorialState.QUEST_ACCEPTED) {
			event.getPlayer().teleport(villageLoc);
			event.getPlayer().sendMessage("§c▒§r NPC를 우클릭해서 퀘스트를 수행해주세요!");
		} else if (event.getRegion().equals(IslandUtils.getIsleRegion(IslandUtils.POLARIS)) &&
				TutorialState.WELCOME_TO_POLARIS <= TutorialState.get(event.getPlayer()) && TutorialState.get(event.getPlayer()) < TutorialState.ISLAND_CREATED) {
			event.getPlayer().teleport(villageLoc);
			event.getPlayer().sendMessage("§c▒§r 튜토리얼 퀘스트가 아직 끝나지 않았습니다!");
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			
			if (TutorialState.get(player) < TutorialState.LETS_KILL_ZOMBIES) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (TutorialState.get((Player) event.getEntity()) < TutorialState.WELCOME_TO_POLARIS) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Iterator<Player> it = event.getRecipients().iterator();
		
		while (it.hasNext()) {
			Player player = it.next();
			
			if (plugin.introHandler.isInIntro(player)) {
				it.remove();
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (plugin.introHandler.users.contains(event.getPlayer().getUniqueId())) {
			plugin.introHandler.removeItems(event.getPlayer());
			plugin.introHandler.users.remove(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		for (UUID id : plugin.introHandler.users) {
			Player target = plugin.getServer().getPlayer(id);
			
			event.getPlayer().hidePlayer(target);
		}
		
		if (TutorialState.get(event.getPlayer()) < TutorialState.WELCOME_TO_POLARIS) {
			event.getPlayer().setGameMode(GameMode.ADVENTURE);
			event.getPlayer().teleport(startLoc);
			event.getPlayer().setExp(0.0F);
			event.getPlayer().setLevel(0);
			event.getPlayer().getInventory().clear();
			event.getPlayer().removeMetadata("tutorial_break_stone", plugin);
			event.getPlayer().removeMetadata("tutorial_farm", plugin);
			
			UserData data = new UserData(event.getPlayer());
			data.reset();
			
			Fatigue.setFatigue(event.getPlayer(), 0);
			
			TutorialState.set(event.getPlayer(), TutorialState.ON_THE_MAGIC_CIRCLE);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (TutorialState.get(event.getPlayer()) < TutorialState.WELCOME_TO_POLARIS) {
			ItemStack item = event.getItemDrop().getItemStack();
			
			if (item.getType().equals(Material.BOAT) || item.getType().equals(Material.COMPASS))
				return;
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFill(PlayerBucketFillEvent event) {
		if (TutorialState.get(event.getPlayer()) < TutorialState.WELCOME_TO_POLARIS) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEmpty(PlayerBucketEmptyEvent event) {
		if (TutorialState.get(event.getPlayer()) < TutorialState.WELCOME_TO_POLARIS) {
			event.setCancelled(true);
		}
	}
}