package me.efe.efeserver.reform.farming;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.slikey.effectlib.util.ParticleEffect;

public class GrassListener implements Listener {
	public EfeServer plugin;
	public Random random = new Random();
	public List<Material> weeds = new ArrayList<Material>();
	public List<ItemStack> newWeeds = new ArrayList<ItemStack>();
	public List<RegenTask> regens = new ArrayList<RegenTask>();
	
	public GrassListener(EfeServer plugin) {
		this.plugin = plugin;
		
		weeds.add(Material.SAPLING);
		weeds.add(Material.LONG_GRASS);
		weeds.add(Material.YELLOW_FLOWER);
		weeds.add(Material.RED_ROSE);
		weeds.add(Material.BROWN_MUSHROOM);
		weeds.add(Material.RED_MUSHROOM);
		weeds.add(Material.DOUBLE_PLANT);
		
		newWeeds.add(new ItemStack(Material.LONG_GRASS, 1, (short) 2));
		newWeeds.add(new ItemStack(Material.YELLOW_FLOWER));
		newWeeds.add(new ItemStack(Material.RED_ROSE));
		newWeeds.add(new ItemStack(Material.RED_ROSE, 1, (short) 1));
		newWeeds.add(new ItemStack(Material.RED_ROSE, 1, (short) 2));
		newWeeds.add(new ItemStack(Material.RED_ROSE, 1, (short) 3));
		newWeeds.add(new ItemStack(Material.RED_ROSE, 1, (short) 4));
		newWeeds.add(new ItemStack(Material.RED_ROSE, 1, (short) 5));
		newWeeds.add(new ItemStack(Material.RED_ROSE, 1, (short) 6));
		newWeeds.add(new ItemStack(Material.RED_ROSE, 1, (short) 7));
		newWeeds.add(new ItemStack(Material.RED_ROSE, 1, (short) 8));
		newWeeds.add(new ItemStack(Material.BROWN_MUSHROOM));
		newWeeds.add(new ItemStack(Material.RED_MUSHROOM));
		newWeeds.add(new ItemStack(Material.DOUBLE_PLANT));
		newWeeds.add(new ItemStack(Material.DOUBLE_PLANT, 1, (short) 1));
		newWeeds.add(new ItemStack(Material.DOUBLE_PLANT, 1, (short) 2));
		newWeeds.add(new ItemStack(Material.DOUBLE_PLANT, 1, (short) 3));
		newWeeds.add(new ItemStack(Material.DOUBLE_PLANT, 1, (short) 4));
		newWeeds.add(new ItemStack(Material.DOUBLE_PLANT, 1, (short) 5));
	}
	
	public void clear() {
		for (RegenTask task : regens) {
			task.run();
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void blockBreak(BlockBreakEvent e) {
		if (IslandUtils.isNimbus(e.getBlock().getLocation())) {
			if (!e.getPlayer().isOp())
				e.setCancelled(true);
			
			if (weeds.contains(e.getBlock().getType())) {
				if (e.getBlock().getType().equals(Material.DOUBLE_PLANT) &&
						e.getBlock().getLocation().clone().subtract(0, 1, 0).getBlock().getType().equals(Material.DOUBLE_PLANT)) {
					new RegenTask(e.getBlock().getLocation().clone().subtract(0, 1, 0)).runTaskLater(plugin, 20*30);
				} else {
					new RegenTask(e.getBlock().getLocation()).runTaskLater(plugin, 20*30);
				}
				
				if (e.getBlock().getType().equals(Material.LONG_GRASS)) {
					if (Math.random() <= 0.0005) {
						e.getBlock().setType(Material.AIR);
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
						
						plugin.animalListener.spawn(e.getBlock().getLocation().add(0.5D, 0.5D, 0.5D), e.getPlayer());
						return;
					}
					
					if (Math.random() <= 0.95) {
						e.getBlock().setType(Material.AIR);
						return;
					}
					
					ItemStack item = null;
					int rand = random.nextInt(10);
					
					if (rand <= 2) item = new ItemStack(Material.SEEDS);
					if (rand == 3) item = new ItemStack(Material.PUMPKIN_SEEDS);
					if (rand == 4) item = new ItemStack(Material.MELON_SEEDS);
					if (rand == 5) item = new ItemStack(Material.SUGAR_CANE);
					if (rand == 6) item = new ItemStack(Material.CACTUS);
					if (rand == 7) item = new ItemStack(Material.INK_SACK, 1, (short) 3);
					if (rand == 8) item = new ItemStack(Material.WATER_LILY);
					if (rand == 9) item = new ItemStack(Material.VINE);
					
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
				}
				
				
				e.getBlock().breakNaturally();
				e.getBlock().setType(Material.AIR);
			}
		}
	}
	
	@EventHandler
	public void noGrow(StructureGrowEvent e) {
		if ((IslandUtils.isNimbus(e.getLocation()) || IslandUtils.getIsleName(e.getLocation()).equals(IslandUtils.TUTORIAL_WEED))) {
			e.setCancelled(true);
		}
	}
	
	private class RegenTask extends BukkitRunnable {
		private final Location loc;
		
		public RegenTask(Location loc) {
			this.loc = loc;
			regens.add(this);
		}
		
		@SuppressWarnings("deprecation")
		public void run() {
			ItemStack regen = 
					(Math.random() <= 0.9) ? new ItemStack(Material.LONG_GRASS, 1, (short) 1) : 
					newWeeds.get(random.nextInt(newWeeds.size()));
			
			loc.getBlock().setType(regen.getType());
			loc.getBlock().setData((byte) regen.getDurability());
			loc.getBlock().getState().update(true, false);
			
			if (regen.getType() == Material.DOUBLE_PLANT) {
				loc.clone().add(0, 1, 0).getBlock().setType(Material.DOUBLE_PLANT);
				loc.clone().add(0, 1, 0).getBlock().setData((byte) 8);
				loc.clone().add(0, 1, 0).getBlock().getState().update(true, false);
			}
			
			loc.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType(), 2);
			
			ParticleEffect.VILLAGER_HAPPY.display(0.15F, 0.15F, 0.15F, 0.0F, 15, loc.clone().add(0.5, 0.5, 0.5), 32);
		}
	}
}