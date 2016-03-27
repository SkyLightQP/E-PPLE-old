package me.efe.efeserver;

import java.util.ArrayList;
import java.util.List;

import me.efe.efecommunity.Post;
import me.efe.efeisland.IslandUtils;
import me.efe.efeitems.ItemStorage;
import me.efe.efemobs.EfeMobs;
import me.efe.efeserver.util.UserUtils;
import me.efe.efetutorial.TutorialState;
import me.efe.fishkg.Fishkg;
import mkremins.fanciful.FancyMessage;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class ExtraListener implements Listener {
	public EfeServer plugin;
	
	public ExtraListener(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		sendTabTitle(e.getPlayer());
		
		e.setJoinMessage("§d▒§r "+e.getPlayer().getName()+"님께서 접속하셨습니다.");
		
		if (!e.getPlayer().hasPlayedBefore()) {
			e.setJoinMessage("§d▒§r "+e.getPlayer().getName()+"님께서 새로 접속하셨습니다!");
		}
		
		
		PlayerData data = PlayerData.get(e.getPlayer());
		
		e.getPlayer().getInventory().remove(Material.BOAT);
		e.getPlayer().getInventory().remove(Material.COMPASS);
		
		if (data.getOptionBoat()) {
			
			if (data.getOptionMenu())
				e.getPlayer().getInventory().setItem(7, plugin.myboat.getBoatItem(e.getPlayer()));
			else
				e.getPlayer().getInventory().setItem(8, plugin.myboat.getBoatItem(e.getPlayer()));
		}
		
		if (data.getOptionMenu()) {
			e.getPlayer().getInventory().setItem(8, plugin.util.createDisplayItem("§e/메뉴", new ItemStack(Material.NETHER_STAR), 
					new String[]{"클릭하면 메인 메뉴를 엽니다."}));
		}
		
		
		giveGift(e.getPlayer());
		
		
		if (!e.getPlayer().hasPotionEffect(PotionEffectType.BLINDNESS)) {
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
		}
		
		
		for (Player all : plugin.util.getOnlinePlayers()) {
			me.efe.efecommunity.UserData communityData = new me.efe.efecommunity.UserData(all);
			
			if (communityData.getFriends().contains(e.getPlayer().getUniqueId())) {
				all.playSound(all.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
			}
		}
		
		
		me.efe.efecommunity.UserData cData = new me.efe.efecommunity.UserData(e.getPlayer());
		if (!cData.getPosts().isEmpty()) {
			new FancyMessage("§a▒§r "+cData.getPosts().size()+"개의 우편물이 ")
			.then("§b§n우편함§r")
				.command("/우편함")
				.tooltip("§b/우편함")
			.then("에 보관되어 있습니다!")
			.send(e.getPlayer());
		}
		
		
		final Player p = e.getPlayer();
		final String subtitle = "§aWelcome To New-Style Survival!";
		
		TitleAPI.sendTitle(p, 30, 60, 20, "§4§l* E-PPLE *", subtitle);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				List<String> titles = new ArrayList<String>();
				titles.add("§4§l* E-PPLE *");
				titles.add("§4§l* E-PPLE *");
				titles.add("§4§l* §c§lE§4§l-PPLE *");
				titles.add("§4§l* §c§lE-§4§lPPLE *");
				titles.add("§4§l* E§c§l-P§4§lPLE *");
				titles.add("§4§l* E-§c§lPP§4§lLE *");
				titles.add("§4§l* E-P§c§lPL§4§lE *");
				titles.add("§4§l* E-PP§c§lLE§4§l *");
				titles.add("§4§l* E-PPL§c§lE§4§l *");
				titles.add("§4§l* E-PPLE *");
				titles.add("§4§l* E-PPLE *");
				titles.add("§4§l§c§l*§4§l E-PPLE §c§l*");
				titles.add("§4§l* E-PPLE *");
				titles.add("§4§l* E-PPLE *");
				
				repeat(p, titles, subtitle);
			}
		}, 30L);
	}
	
	public void giveGift(Player p) {
		if (!p.hasPlayedBefore()) {
			if (UserUtils.isCBTTester(p)) {
				Post post = Post.getBuilder()
						.setSender("§cE-PPLE")
						.setMessage("CBT 참가 보상", "오랜만이네요, 반가워요!|CBT에 참가해주셔서 감사합니다.")
						.setItems(new ItemStack[]{ItemStorage.RANDOM_TITLE_BOOK.clone(), ItemStorage.ANVIL_ENCHANTED.clone()})
						.build();
				Post.sendPost(p, post);
			}
			
			if (UserUtils.isOBTTester(p)) {
				Post post = Post.getBuilder()
						.setSender("§cE-PPLE")
						.setMessage("OBT 참가 보상", "오랜만이네요, 반가워요!|OBT에 참가해주셔서 감사합니다.")
						.setItems(new ItemStack[]{ItemStorage.BLOOD_SKIN_BOX.clone()})
						.build();
				Post.sendPost(p, post);
			}
			
			if (UserUtils.isOldUser(p)) {
				PlayerData.get(p).setPlayedIntro();
				
				ItemStack item = ItemStorage.EYE_OF_TELEPORTATION_GIFT.clone();
				item.setAmount(UserUtils.getOldLevel(p));
				
				Post post = Post.getBuilder()
						.setSender("§cE-PPLE")
						.setMessage("리뉴얼 복구 보상", "기다려주셔서 감사합니다!|더욱 발전하는 E-PPLE이 되겠습니다.")
						.setItems(new ItemStack[]{item})
						.build();
				Post.sendPost(p, post);
			}
		}
	}
	
	public void sendTabTitle(Player p) {
		TitleAPI.sendTabTitle(p, 
				"          §l* E-PPLE *          ", 
				"§aEphe: "+plugin.vault.getBalance(p)+"E\n" +
				"§7·· e-pple.kr ··");
	}
	
	private void repeat(final Player p, final List<String> titles, final String subtitle) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				TitleAPI.sendTitle(p, 0, 20, 30, titles.get(0), subtitle);
				
				titles.remove(0);
				
				if (titles.isEmpty()) return;
				
				repeat(p, titles, subtitle);
			}
		}, 5L);
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		e.setQuitMessage("§d▒§r "+e.getPlayer().getName()+"님께서 종료하셨습니다.");
		
		e.getPlayer().removeMetadata("fishing_rod", plugin);
		
		if (plugin.chairManager.chairs.containsKey(e.getPlayer())) {
			plugin.chairManager.removeChair(e.getPlayer());
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if ((plugin.util.isRightClick(e) || plugin.util.isLeftClick(e)) && e.getItem() != null && e.getItem().getType().equals(Material.NETHER_STAR)) {
			e.setCancelled(true);
			
			plugin.mainGui.openGUI(e.getPlayer());
		}
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && 
				(e.getClickedBlock().getType() == Material.BEACON || 
				e.getClickedBlock().getType() == Material.ANVIL || 
				e.getClickedBlock().getType() == Material.BREWING_STAND)) {
			e.setCancelled(true);
		}
		
		if (plugin.util.isRightClick(e) && e.getItem() != null && e.getItem().getType().equals(Material.FISHING_ROD)) {
			if (e.getPlayer().hasMetadata("fishing_rod")) return;
			
			final Player p = e.getPlayer();
			
			p.setMetadata("fishing_rod", new FixedMetadataValue(plugin, ""));
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					if (p == null || !p.isOnline()) return;
					
					p.removeMetadata("fishing_rod", plugin);
				}
			}, 5L);
		}
		
		if (plugin.util.isLeftClick(e) && e.getItem() != null && e.getItem().getType().equals(Material.FISHING_ROD)) {
			if (e.getPlayer().hasMetadata("fishing_rod")) return;
			
			e.setCancelled(true);
			plugin.util.updateInv(e.getPlayer());
			
			plugin.getServer().dispatchCommand(e.getPlayer(), "미끼");
		}
	}
	
	@EventHandler
	public void drop(PlayerDropItemEvent e) {
		ItemStack item = e.getItemDrop().getItemStack().clone();
		
		if (item.getType().equals(Material.NETHER_STAR)) {
			e.getItemDrop().remove();
			
			e.getPlayer().getInventory().setItem(8, plugin.util.createDisplayItem("§e/메뉴", new ItemStack(Material.NETHER_STAR), 
					new String[]{"클릭하면 메인 메뉴를 엽니다."}));
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void pickup(PlayerPickupItemEvent e) {
		final Player p = e.getPlayer();
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if (p.getInventory().firstEmpty() == -1) {
					ActionBarAPI.sendActionBar(p, "§c인벤토리가 가득 찼습니다!");
				}
			}
		}, 1L);
	}
	
	@EventHandler
	public void death(PlayerDeathEvent e) {
		for (Player all : plugin.util.getOnlinePlayers()) {
			me.efe.efecommunity.UserData communityData = new me.efe.efecommunity.UserData(all);
			
			if (communityData.getFriends().contains(e.getEntity().getUniqueId())) {
				all.sendMessage("§d▒§r "+e.getEntity().getName()+"님이 사망했습니다.");
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void fish(PlayerFishEvent e) {
		if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH && e.getCaught() instanceof Item) {
			Item item = (Item) e.getCaught();
			ItemStack stack = item.getItemStack();
			
			if (Fishkg.getInstance().isFish(stack) && stack.getItemMeta().getDisplayName().endsWith("오징어")) {
				Location cLoc = e.getCaught().getLocation().clone();
				Location pLoc = e.getPlayer().getLocation().clone();
				e.getCaught().remove();
				
				Squid squid = e.getPlayer().getWorld().spawn(cLoc, Squid.class);
				
				Vector vector = new Vector(pLoc.getX() - cLoc.getX(), pLoc.getY() - cLoc.getY(), pLoc.getZ() - cLoc.getZ());
				double sqrt = Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
				
				vector.setX(vector.getX() * 0.1D);
				vector.setX(vector.getY() * 0.1D + Math.sqrt(sqrt) * 0.08D);
				vector.setX(vector.getZ() * 0.1D);
				
				squid.setVelocity(vector);
			}
		}
	}
	
	@EventHandler
	public void nameTag(PlayerInteractEntityEvent e) {
		if (e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType() == Material.NAME_TAG && e.getPlayer().getWorld().equals(plugin.world)) {
			e.setCancelled(true);
			plugin.util.updateInv(e.getPlayer());
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void bucketFill(final PlayerBucketFillEvent e) {
		if (e.isCancelled()) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					plugin.util.updateInv(e.getPlayer());
				}
			}, 5L);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void bucketEmpty(final PlayerBucketEmptyEvent e) {
		if (e.isCancelled()) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					plugin.util.updateInv(e.getPlayer());
				}
			}, 5L);
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void signColor(SignChangeEvent e) {
		if (e.getPlayer().hasPermission("epple.signcolor")) {
			String[] lines = e.getLines();
			
			for (int i = 0; i < 4; i ++) {
				lines[i] = plugin.util.replaceColors(lines[i]);
				e.setLine(i, lines[i]);
			}
		}
	}
	
	@EventHandler
	public void hunger(FoodLevelChangeEvent e) {
		if (e.getFoodLevel() < ((Player) e.getEntity()).getFoodLevel() && IslandUtils.getIsleName(e.getEntity().getLocation()).equals(IslandUtils.POLARIS)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void noPvP(EntityDamageByEntityEvent e) {
		LivingEntity damager = plugin.util.getDamager(e);
		if (damager == null) return;
		
		if (e.getEntity() instanceof Player && damager instanceof Player) {
			boolean var = false;
			
			for (ProtectedRegion region : WGBukkit.getRegionManager(e.getEntity().getWorld()).getApplicableRegions(e.getEntity().getLocation()).getRegions()) {
				if (region.getFlag(DefaultFlag.PVP) != State.ALLOW) {
					var = true;
				}
			}
			
			for (ProtectedRegion region : WGBukkit.getRegionManager(damager.getWorld()).getApplicableRegions(damager.getLocation()).getRegions()) {
				if (region.getFlag(DefaultFlag.PVP) != State.ALLOW) {
					var = true;
				}
			}
			
			if (var)
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void noClick(InventoryClickEvent e) {
		if (e.getHotbarButton() == 7) {
			PlayerData data = PlayerData.get(e.getWhoClicked().getUniqueId());
			
			if (data.getOptionBoat())
				e.setCancelled(true);
		} else if (e.getHotbarButton() == 8) {
			PlayerData data = PlayerData.get(e.getWhoClicked().getUniqueId());
			
			if (data.getOptionMenu())
				e.setCancelled(true);
		}
		
		if (e.getCurrentItem() != null && 
				(e.getCurrentItem().getType().equals(Material.BOAT) ||
						e.getCurrentItem().getType().equals(Material.COMPASS) ||
						e.getCurrentItem().getType().equals(Material.NETHER_STAR))) {
			
			e.setCancelled(true);
		} else if (e.getCursor() != null && 
				(e.getCursor().getType().equals(Material.BOAT) ||
						e.getCursor().getType().equals(Material.COMPASS) ||
						e.getCursor().getType().equals(Material.NETHER_STAR))) {
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void noInteract(PlayerInteractEvent e) {
		if ((plugin.util.isLeftClick(e) || plugin.util.isRightClick(e)) && e.getItem() != null) {
			if (e.getItem().getType() == Material.ENDER_PEARL || e.getItem().getType() == Material.EYE_OF_ENDER) {
				e.setCancelled(true);
				plugin.util.updateInv(e.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void noSpawn(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.BUILD_IRONGOLEM || 
				e.getSpawnReason() == SpawnReason.BUILD_SNOWMAN || 
				e.getSpawnReason() == SpawnReason.BUILD_WITHER ||
				e.getSpawnReason() == SpawnReason.JOCKEY || 
				e.getSpawnReason() == SpawnReason.MOUNT) {
			e.setCancelled(true);
		}
		
		if (e.getSpawnReason() == SpawnReason.EGG && e.getLocation().getWorld().equals(plugin.world)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void noPlace(BlockPlaceEvent e) {
		if (e.getPlayer().isOp() || e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		
		if (e.getBlock().getWorld().getName().equalsIgnoreCase("world") && TutorialState.get(e.getPlayer()) != TutorialState.ARRIVED_FARM_ISLAND) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void noBreak(BlockBreakEvent e) {
		e.setExpToDrop(0);
		
		if (e.getPlayer().isOp() || e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		if (WGBukkit.getRegionManager(e.getBlock().getWorld()).getApplicableRegions(e.getBlock().getLocation()).size() != 0) return;
		if (e.getBlock().getWorld().getName().equalsIgnoreCase("world")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void noExtinguish(PlayerInteractEvent e) {
		if (plugin.util.isLeftClick(e) && !e.getPlayer().isOp() && getTargetBlock(e.getPlayer(), 5).getType() == Material.FIRE) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onToggleFlight(PlayerToggleFlightEvent e) {
		if (e.isFlying() && e.getPlayer().getWorld().equals(plugin.world) && !e.getPlayer().isOp()) {
			e.setCancelled(true);
			e.getPlayer().setAllowFlight(false);
		}
	}
	
	@EventHandler
	public void noMelt(BlockFadeEvent e) {
		if (e.getBlock().getWorld().equals(plugin.world) && (e.getBlock().getType().equals(Material.SNOW) || e.getBlock().getType().equals(Material.ICE))) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void noIgnite(BlockIgniteEvent e) {
		if (e.getBlock().getWorld().equals(plugin.world)) {
			if (e.getCause() == IgniteCause.LAVA || e.getCause() == IgniteCause.SPREAD) {
				e.setCancelled(true);
			} else if (e.getCause() == IgniteCause.FIREBALL && EfeMobs.getFloor(e.getBlock().getLocation()) != 8) {
				e.setCancelled(true);
			}
			
		}
	}
	
	@EventHandler
	public void noPop(EntityChangeBlockEvent e) {
		if (e.getBlock().getType() == Material.WATER_LILY && e.getEntity().getClass().getName().endsWith("Boat")) {
			e.setCancelled(true);
			
			final Block block = e.getBlock();
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					block.setType(Material.WATER_LILY);
				}
			}, 1200L);
		}
	}
	
	@EventHandler
	public void noWeather(WeatherChangeEvent e) {
		if (e.toWeatherState()) {
			e.setCancelled(true);
		}
	}
	
	public Block getTargetBlock(Player p, int range) {
        BlockIterator it = new BlockIterator(p, range);
        Block lastBlock = it.next();
        
        while (it.hasNext()) {
            lastBlock = it.next();
            
            if (lastBlock.getType() == Material.AIR)
            	continue;
            break;
        }
        
        return lastBlock;
    }
}