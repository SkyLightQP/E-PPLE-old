package me.efe.efegear.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Token {
	public static String getToken(OfflinePlayer p) {
		return p.getUniqueId().toString();
	}
	
	public static Player getPlayer(String token) {
		return Bukkit.getPlayer(UUID.fromString(token));
	}
	
	public static OfflinePlayer getOfflinePlayer(String token) {
		return Bukkit.getOfflinePlayer(UUID.fromString(token));
	}
	
	public static List<Player> toPlayerList(List<String> players) {
		List<Player> list = new ArrayList<Player>();
		
		for (String id : players) {
			Player p = Bukkit.getPlayer(UUID.fromString(id));
			if (p == null) continue;
			
			list.add(p);
		}
		return list;
	}
	
	public static String getPlayerName(String token) {
		return Bukkit.getOfflinePlayer(UUID.fromString(token)).getName();
	}
}