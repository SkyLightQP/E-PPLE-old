package me.efe.skilltree;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class UserData {
	private UUID id;
	private FileConfiguration config;
	
	public UserData(OfflinePlayer player) {
		this(player.getUniqueId());
	}
	
	public UserData(UUID id) {
		this.id = id;
		
		File file = new File("plugins/SkillTree/UserData/" + id.toString() + ".yml");
		File folder = new File("plugins/SkillTree");
		File folder2 = new File("plugins/SkillTree/UserData");
		
		try {
			if (!file.exists()) {
				folder.mkdir();
				folder2.mkdir();
				file.createNewFile();
			}
			
			this.config = YamlConfiguration.loadConfiguration(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getLevel(Skill skill) {
		return config.contains(skill.getName()) ? config.getInt(skill.getName()) : 0;
	}
	
	public int getLevel(String skillName) {
		return getLevel(SkillManager.getSkill(skillName));
	}
	
	public boolean hasLearned(Skill skill) {
		return getLevel(skill) > 0;
	}
	
	public boolean hasLearned(String skillName) {
		return hasLearned(SkillManager.getSkill(skillName));
	}
	
	public int getSP() {
		return config.contains("sp") ? config.getInt("sp") : 0;
	}
	
	public int getUsedSP() {
		return config.contains("used-sp") ? config.getInt("used-sp") : 0;
	}
	
	public void setLevel(Skill skill, int lv) {
		config.set(skill.getName(), lv);
		
		save();
	}
	
	public void giveSP(int amount) {
		config.set("sp", config.getInt("sp") + amount);
		
		save();
	}
	
	public void levelup(Skill skill) {
		config.set("sp", config.getInt("sp") - 1);
		config.set("used-sp", config.getInt("used-sp") + 1);
		config.set(skill.getName(), config.getInt(skill.getName()) + 1);
		
		save();
	}
	
	public void reset() {
		for (String path : config.getKeys(false)) {
			config.set(path, null);
		}
		
		save();
	}
	
	private void save() {
		try {
			File file = new File("plugins/SkillTree/UserData/" + id.toString() + ".yml");
			
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}