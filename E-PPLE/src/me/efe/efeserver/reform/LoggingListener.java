package me.efe.efeserver.reform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;

public class LoggingListener implements Listener {
	public EfeServer plugin;
	public Random rand = new Random();
	public List<RegenTask> regen = new ArrayList<RegenTask>();
	
	public LoggingListener(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void blockBreak(BlockBreakEvent e) {
		if (IslandUtils.isTreeIsland(e.getBlock().getLocation())) {
			if (e.getPlayer().isOp() || e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			
			Material type = e.getBlock().getType();
			
			if (type == Material.LOG || type == Material.LOG_2 || type == Material.LEAVES || type == Material.LEAVES_2) {
				boolean hasSnow = e.getBlock().getRelative(BlockFace.UP).getType() == Material.SNOW;
				
				new RegenTask(e.getBlock().getState(), hasSnow).runTaskLater(plugin, 20 * 60);
			} else {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void decay(LeavesDecayEvent e) {
		if (IslandUtils.isTreeIsland(e.getBlock().getLocation())) {
			e.setCancelled(true);
		}
	}
	
	private class RegenTask extends BukkitRunnable {
		private final BlockState state;
		private final boolean hasSnow;
		
		public RegenTask(BlockState state, boolean hasSnow) {
			this.state = state;
			this.hasSnow = hasSnow;
			regen.add(this);
		}
		
		public void run() {
			state.update(true);
			state.getWorld().playEffect(state.getLocation(), Effect.STEP_SOUND, state.getBlock().getType());
			
			if (hasSnow) {
				Block snow = state.getBlock().getRelative(BlockFace.UP);
				
				snow.setType(Material.SNOW);
				snow.getWorld().playEffect(snow.getLocation(), Effect.STEP_SOUND, Material.SNOW);
			}
		}
	}
	
//  public BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};
//
//	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
//	public void blockBreak(BlockBreakEvent e) {
//		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
//		
//		if (e.getBlock().getType().equals(Material.LOG) || e.getBlock().getType().equals(Material.LOG_2)) {
//			
//			for (int i = 1; i < 15; i ++) {
//				Block block = e.getBlock().getRelative(BlockFace.UP, i);
//				
//				if (block.getType().equals(Material.LOG) || block.getType().equals(Material.LOG_2) || block.getType().equals(Material.AIR)) continue;
//				
//				if (block.getType().equals(Material.LEAVES) || block.getType().equals(Material.LEAVES_2))
//					break;
//				else
//					return;
//			}
//			
//			for (int i = 1; i < 30; i ++) {
//				Block block = e.getBlock().getRelative(BlockFace.UP, i);
//				
//				if (block.getType().equals(Material.LOG)) {
//					block.setType(Material.AIR);
//					
//					@SuppressWarnings("deprecation")
//					FallingBlock falling = e.getBlock().getWorld().spawnFallingBlock(block.getLocation(), Material.LOG, (byte) block.getData());
//					
//					falling.setDropItem(false);
//				} else if (block.getType().equals(Material.LOG_2)) {
//					block.setType(Material.AIR);
//					
//					@SuppressWarnings("deprecation")
//					FallingBlock falling = e.getBlock().getWorld().spawnFallingBlock(block.getLocation(), Material.LOG_2, (byte) block.getData());
//					
//					falling.setDropItem(false);
//				} else {
//					return;
//				}
//			}
//		}
//	}
}