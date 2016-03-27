package me.efe.efeserver.reform;

import java.io.File;
import java.io.IOException;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Fatigue {
	
	public static void addFatigue(OfflinePlayer p, int amount) {
		int fatigue = getFatigue(p) + amount;
		
		if (fatigue > 500) fatigue = 500;
		if (fatigue < 0) fatigue = 0;
		
		setFatigue(p, fatigue);
	}
	
	public static void clear() {
		File file = new File("plugins/EfeServer/fatigue.yml");
		
		if (!file.exists()) return;
		if (!file.delete()) {
			System.out.println("[EfeServer] Failed to delete Fatigue File!");
		}
	}
	
	public static void setFatigue(OfflinePlayer p, int amount) {
		File file = new File("plugins/EfeServer/fatigue.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		config.set(p.getUniqueId().toString(), amount);
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getFatigue(OfflinePlayer p) {
		File file = new File("plugins/EfeServer/fatigue.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		return config.contains(p.getUniqueId().toString()) ? config.getInt(p.getUniqueId().toString()) : 0;
	}
}