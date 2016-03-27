package me.efe.unlimitedrpg.unlimitedtag;

import me.efe.unlimitedrpg.UnlimitedRPG;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WGListener implements Listener {
	public UnlimitedRPG plugin;
	
	public WGListener(UnlimitedRPG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void leaveRegion(PlayerMoveEvent e) {
		if (e.isAsynchronous() || e.getPlayer().isOp() || e.getTo().distance(e.getFrom()) == 0.0D) return;
		
		for (ItemStack item : e.getPlayer().getInventory().getContents()) {
			if (item == null) continue;
			
			if (isSpatial(item, e.getTo())) {
				e.getPlayer().getInventory().remove(item);
			}
		}
		
		for (ItemStack item : e.getPlayer().getInventory().getArmorContents()) {
			if (item == null) continue;
			
			if (isSpatial(item, e.getTo())) {
				e.getPlayer().getInventory().remove(item);
			}
		}
	}
	
	public boolean isSpatial(ItemStack item, Location loc) {
		if (UnlimitedTagAPI.hasTag(item, TagType.SPATIAL)) {
			World world = loc.getWorld();
			String name = UnlimitedTagAPI.getData(item, TagType.SPATIAL);
			
			ProtectedRegion region = WGBukkit.getRegionManager(world).getRegion(name);
			
			if (region == null) return false;
			
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			
			return !region.contains(x, y, z);
		}
		return false;
	}
}