package me.efe.efetutorial.listeners;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeisland.IslandUtils;
import me.efe.efetutorial.EfeTutorial;
import me.efe.efetutorial.TutorialState;
import me.efe.fishkg.events.PlayerFishkgEvent;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;
import com.mewin.WGRegionEvents.MovementWay;
import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeftEvent;

public class TutorialListener implements Listener {
	private static EfeTutorial plugin;
	public static int USED_SP_BOAT;
	public static int ENTERED_BOAT;
	public static int CLICKED_NAVIGATION;
	public static int SELECTED_SKILL;
	public static int LEFT_START_REGION;
	public static int ARRIVED_SKILL_ISLAND;
	public static int LEFT_SKILL_REGION;
	public static int ARRIVED_MINE_ISLAND;
	public static int BROKE_STONES;
	public static int ARRIVED_WEED_ISLAND;
	public static int COLLECTED_SEEDS;
	public static int LEFT_WEED_REGION;
	public static int ARRIVED_FARM_ISLAND;
	public static int PLACED_SIGN;
	public static int WATERED_FARM;
	public static int PLANTED_SEEDS;
	public static int HARVESTED_CROPS;
	public static int LEFT_FARM_REGION;
	public static int ARRIVED_FISH_ISLAND;
	public static int FISHED;
	public static int LEFT_FISH_REGION;
	public static int ARRIVED_HUNT_ISLAND;
	public static int BE_KILLED_BY_ZOMBIE;
	
	public TutorialListener(EfeTutorial plugin) {
		TutorialListener.plugin = plugin;
	}
	
	public static void onUseSPBoat(Player player) {
		if (TutorialState.get(player) != TutorialState.LEVEL_UP)
			return;
		
		TutorialState.set(player, TutorialState.SP_USED_BOAT);
		
		plugin.tutorialHandler.proceedTutorial(player, USED_SP_BOAT, 60);
	}
	
	public static void onEnterBoat(Player player) {
		if (TutorialState.get(player) != TutorialState.LETS_ENTER_BOAT)
			return;
		
		TutorialState.set(player, TutorialState.ENTERED_BOAT);
		
		plugin.tutorialHandler.proceedTutorial(player, ENTERED_BOAT, 5);
	}
	
	public static void onClickNavigation(Player player) {
		if (TutorialState.get(player) != TutorialState.LETS_CLICK_NAVIGATION)
			return;
		
		TutorialState.set(player, TutorialState.CLICKED_NAVIGATION);
		
		plugin.tutorialHandler.proceedTutorial(player, CLICKED_NAVIGATION, 5);
	}
	
	public static void onClickSkill(Player player) {
		if (TutorialState.get(player) != TutorialState.LETS_SELECT_SKILL)
			return;
		
		TutorialState.set(player, TutorialState.NAVIGATED_SKILL);
		
		plugin.tutorialHandler.proceedTutorial(player, SELECTED_SKILL, 20);
	}
	
	public static void onBreakBlock(Player player) {
		if (TutorialState.get(player) != TutorialState.LETS_BREAK_STONES)
			return;
		
		if (player.hasMetadata("tutorial_break_stone")) {
			int count = player.getMetadata("tutorial_break_stone").get(0).asInt();
			count ++;
			
			TitleAPI.sendTitle(player, 0, 99999, 0, "", "§5돌을 "+(5 - count)+"번 캐보세요.");
			
			if (count >= 5) {
				player.removeMetadata("tutorial_break_stone", plugin);
				TutorialState.set(player, TutorialState.BROKE_STONE);
				
				plugin.tutorialHandler.proceedTutorial(player, BROKE_STONES, 20);
				return;
			}
			
			player.setMetadata("tutorial_break_stone", new FixedMetadataValue(plugin, count));
		} else {
			TitleAPI.sendTitle(player, 0, 99999, 0, "", "§5돌을 4번 캐보세요.");
			
			player.setMetadata("tutorial_break_stone", new FixedMetadataValue(plugin, 1));
		}
	}
	
	public static void onCreateFarm(Player player) {
		if (TutorialState.get(player) != TutorialState.ARRIVED_FARM_ISLAND)
			return;
		
		TutorialState.set(player, TutorialState.CREATED_FARM);
		
		plugin.tutorialHandler.proceedTutorial(player, PLACED_SIGN, 20);
	}
	
	public static void onWaterFarm(Player player) {
		if (TutorialState.get(player) != TutorialState.CREATED_FARM)
			return;
		
		TutorialState.set(player, TutorialState.WATERD_FARM);
		
		plugin.tutorialHandler.proceedTutorial(player, WATERED_FARM, 20);
	}
	
	public static void onUseSPCarrot(Player player) {
		if (TutorialState.get(player) != TutorialState.WATERD_FARM)
			return;
		
		TutorialState.set(player, TutorialState.SP_USED_CARROT);
	}
	
	public static void onPlantSeeds(Player player) {
		if (TutorialState.get(player) != TutorialState.SP_USED_CARROT)
			return;
		
		TutorialState.set(player, TutorialState.PLANTED_SEEDS);
		
		plugin.tutorialHandler.proceedTutorial(player, PLANTED_SEEDS, 20);
	}
	
	public static void onHarvestCrops(Player player) {
		if (TutorialState.get(player) != TutorialState.LETS_HARVEST_CROPS)
			return;
		
		TutorialState.set(player, TutorialState.HARVESTED_CROPS);
		
		plugin.tutorialHandler.proceedTutorial(player, HARVESTED_CROPS, 20);
	}
	
	@EventHandler
	public void onRegionEnter(RegionEnterEvent event) {
		int state = TutorialState.get(event.getPlayer());
		
		if (state <= TutorialState.INTRO_FINISHED || state >= TutorialState.WELCOME_TO_POLARIS)
			return;

		String id = event.getRegion().getId();
		
		if (id.equals("tutorial_skill") && state == TutorialState.LEFT_SKILL_REGION) {
			TutorialState.set(event.getPlayer(), TutorialState.ARRIVED_SKILL_ISLAND);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), ARRIVED_SKILL_ISLAND, 20);
		} else if (id.equals("tutorial_mine") && state == TutorialState.LEFT_START_REGION) {
			TutorialState.set(event.getPlayer(), TutorialState.ARRIVED_MINE_ISLAND);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), ARRIVED_MINE_ISLAND, 20);
		} else if (id.equals("tutorial_weed") && state == TutorialState.GO_TO_WEED) {
			TutorialState.set(event.getPlayer(), TutorialState.ARRIVED_WEED_ISLAND);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), ARRIVED_WEED_ISLAND, 20);
		} else if (id.equals("tutorial_farm") && state == TutorialState.LEFT_WEED_REGION) {
			TutorialState.set(event.getPlayer(), TutorialState.ARRIVED_FARM_ISLAND);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), ARRIVED_FARM_ISLAND, 20);
		} else if (id.equals("tutorial_fish") && state == TutorialState.LEFT_FARM_REGION) {
			TutorialState.set(event.getPlayer(), TutorialState.ARRIVED_FISH_ISLAND);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), ARRIVED_FISH_ISLAND, 20);
		} else if (id.equals("tutorial_hunt") && state == TutorialState.LEFT_FISH_REGION) {
			TutorialState.set(event.getPlayer(), TutorialState.ARRIVED_HUNT_ISLAND);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), ARRIVED_HUNT_ISLAND, 20);
		} else if (event.getMovementWay() == MovementWay.MOVE) {
			if (id.equals("tutorial_start"))
				return;
			if (id.equals("tutorial_skill") && state >= TutorialState.GO_TO_SKILL)
				return;
			if (id.equals("tutorial_mine") && state >= TutorialState.GO_TO_MINE)
				return;
			if (id.equals("tutorial_weed") && state >= TutorialState.GO_TO_WEED)
				return;
			if (id.equals("tutorial_farm") && state >= TutorialState.GO_TO_FARM)
				return;
			if (id.equals("tutorial_hunt") && state >= TutorialState.GO_TO_HUNT)
				return;
			
			teleport(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onRegionLeft(RegionLeftEvent event) {
		int state = TutorialState.get(event.getPlayer());
		
		if (state <= TutorialState.INTRO_FINISHED || state >= TutorialState.WELCOME_TO_POLARIS)
			return;
		
		String id = event.getRegion().getId();
		
		if (id.equals("tutorial_start") && state == TutorialState.GO_TO_SKILL) {
			TutorialState.set(event.getPlayer(), TutorialState.LEFT_START_REGION);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), LEFT_START_REGION, 20);
		} else if (id.equals("tutorial_skill") && state == TutorialState.GO_TO_MINE) {
			TutorialState.set(event.getPlayer(), TutorialState.LEFT_SKILL_REGION);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), LEFT_SKILL_REGION, 20);
		} else if (id.equals("tutorial_mine") && state == TutorialState.GO_TO_WEED) {
			//Do nothing
		} else if (id.equals("tutorial_weed") && state == TutorialState.GO_TO_FARM) {
			TutorialState.set(event.getPlayer(), TutorialState.LEFT_WEED_REGION);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), LEFT_WEED_REGION, 20);
		} else if (id.equals("tutorial_farm") && state == TutorialState.GO_TO_FISH) {
			TutorialState.set(event.getPlayer(), TutorialState.LEFT_FARM_REGION);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), LEFT_FARM_REGION, 20);
		} else if (id.equals("tutorial_fish") && state == TutorialState.GO_TO_HUNT) {
			TutorialState.set(event.getPlayer(), TutorialState.LEFT_FISH_REGION);
			
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), LEFT_FISH_REGION, 20);
		} else if (event.getMovementWay() == MovementWay.MOVE) {
			if (id.equals("tutorial_start") && state >= TutorialState.GO_TO_SKILL)
				return;
			if (id.equals("tutorial_skill") && state >= TutorialState.GO_TO_MINE)
				return;
			if (id.equals("tutorial_mine") && state >= TutorialState.GO_TO_WEED)
				return;
			if (id.equals("tutorial_weed") && state >= TutorialState.GO_TO_FARM)
				return;
			if (id.equals("tutorial_farm") && state >= TutorialState.GO_TO_FISH)
				return;
			if (id.equals("tutorial_hunt"))
				return;
			
			teleport(event.getPlayer());
		}
	}
	
	private void teleport(Player player) {
		if (player.getVehicle() != null) {
			player.eject();
		}
		
		final int state = TutorialState.get(player);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (state <= TutorialState.ARRIVED_SKILL_ISLAND) {
					player.teleport(IslandUtils.getIsleLoc(IslandUtils.TUTORIAL_START));
				} else if (state <= TutorialState.ARRIVED_MINE_ISLAND) {
					player.teleport(IslandUtils.getIsleLoc(IslandUtils.TUTORIAL_SKILL));
				} else if (state <= TutorialState.ARRIVED_WEED_ISLAND) {
					player.teleport(IslandUtils.getIsleLoc(IslandUtils.TUTORIAL_MINE));
				} else if (state <= TutorialState.ARRIVED_FARM_ISLAND) {
					player.teleport(IslandUtils.getIsleLoc(IslandUtils.TUTORIAL_WEED));
				} else if (state <= TutorialState.ARRIVED_FISH_ISLAND) {
					player.teleport(IslandUtils.getIsleLoc(IslandUtils.TUTORIAL_FARM));
				} else if (state <= TutorialState.ARRIVED_HUNT_ISLAND) {
					player.teleport(IslandUtils.getIsleLoc(IslandUtils.TUTORIAL_FISH));
				} else {
					player.teleport(IslandUtils.getIsleLoc(IslandUtils.TUTORIAL_HUNT));
				}
				
				player.sendMessage("§c▒§r 튜토리얼을 차례대로 마쳐주세요!");
				player.sendMessage("§c▒§r 재접속하면 튜토리얼을 다시 체험할 수 있습니다.");
			}
		}, 40L);
	}
	
	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent event) {
		if (event.getItem().getItemStack().getType() == Material.SEEDS && TutorialState.get(event.getPlayer()) == TutorialState.LETS_COLLECT_SEEDS) {
			int amount = EfeUtils.player.getItemAmount(event.getPlayer(), new ItemStack(Material.SEEDS));
			amount += event.getItem().getItemStack().getAmount();
			
			TitleAPI.sendTitle(event.getPlayer(), 0, 99999, 0, "", "§5잔디와 꽃을 캐서 밀씨앗을 "+(2 - amount >= 0 ? 2 - amount : 0)+"개 모아보세요.");
			
			if (amount >= 2) {
				TutorialState.set(event.getPlayer(), TutorialState.COLLECTED_SEEDS);
				
				plugin.tutorialHandler.proceedTutorial(event.getPlayer(), COLLECTED_SEEDS, 20);
			}
		}
	}
	
	@EventHandler
	public void onFish(PlayerFishkgEvent event) {
		if (TutorialState.get(event.getPlayer()) == TutorialState.LETS_FISH) {
			TutorialState.set(event.getPlayer(), TutorialState.FISHED);
			plugin.tutorialHandler.proceedTutorial(event.getPlayer(), FISHED, 20);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			
			if (TutorialState.get(player) == TutorialState.LETS_KILL_ZOMBIES) {
				Vector vector = event.getEntity().getLocation().toVector().subtract(player.getLocation().toVector());
				vector.multiply(0.5D);
				
				player.setVelocity(vector);
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (TutorialState.get(event.getEntity()) == TutorialState.LETS_KILL_ZOMBIES) {
			TutorialState.set(event.getEntity(), TutorialState.WELCOME_TO_POLARIS);
			plugin.tutorialHandler.proceedTutorial(event.getEntity(), BE_KILLED_BY_ZOMBIE, 10);
		}
	}
}