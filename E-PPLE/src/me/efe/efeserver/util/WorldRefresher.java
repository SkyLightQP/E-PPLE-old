package me.efe.efeserver.util;

import org.bukkit.Location;

public class WorldRefresher {
	
	@SuppressWarnings("deprecation")
	public static void refresh(Location loc) {
		loc.getWorld().refreshChunk(loc.getBlockX(), loc.getBlockZ());
	}
}