package me.efe.titlemaker.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.efe.titlemaker.TitleManager;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TitleAPI {
	private static List<UUID> invisibleHolograms = new ArrayList<UUID>();
	
	public static List<String> getTitles(OfflinePlayer player) {
		
		return TitleManager.getTitles(player);
	}
	
	public static void addTitle(OfflinePlayer player, String title) {
		List<String> list = TitleManager.getTitles(player);
		list.add(title);
		
		TitleManager.setTitles(player, list);
	}
	
	public static void removeTitle(OfflinePlayer player, String title) {
		List<String> list = TitleManager.getTitles(player);
		
		if (list.contains("title")) return;
		list.remove(title);
		
		TitleManager.setTitles(player, list);
	}
	
	public static boolean hasTitle(OfflinePlayer player, String title) {
		List<String> list = TitleManager.getTitles(player);
		
		return list.contains(title);
	}
	
	public static String getMainTitle(OfflinePlayer player) {
		return TitleManager.getMainTitle(player);
	}
	
	public static boolean hasMainTitle(OfflinePlayer player) {
		return TitleManager.getMainTitle(player) != null;
	}
	
	public static void setInvisibleHologram(Player player, boolean var) {
		if (var)
			invisibleHolograms.add(player.getUniqueId());
		else
			invisibleHolograms.remove(player.getUniqueId());
	}
	
	public static boolean hasInvisibleHologram(Player player) {
		return invisibleHolograms.contains(player.getUniqueId());
	}
}