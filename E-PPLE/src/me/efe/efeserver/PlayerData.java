package me.efe.efeserver;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerData {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy_mm_dd");
	
	private UUID id;
	private String islName;
	
	private int tutorial;

	private boolean hasPlayedIntro;
	private boolean hasIsland;
	
	private double saveX;
	private double saveY;
	private double saveZ;
	
	private String recommendDate;
	
	private HashMap<String, String> farmDates;
	private List<String> tips;
	
	private int huntExp;
	private int mineExp;
	private int farmExp;
	private int huntLevel;
	private int mineLevel;
	private int farmLevel;
	
	private int skin;
	private List<String> skins;
	
	private int wheelOfFortune;
	
	private boolean optMenu;
	private boolean optBoat;
	private boolean optChat;
	private boolean optWhisper;
	
	public PlayerData(UUID id) {
		File file = new File("plugins/EfeServer/PlayerData/"+id.toString()+".yml");
		File folder = new File("plugins/EfeServer/");
		File folder2 = new File("plugins/EfeServer/PlayerData");
		
		//Data Basic Setting
		this.islName = Bukkit.getOfflinePlayer(id).getName().toLowerCase();
		this.tutorial = 0;
		this.hasPlayedIntro = false;
		this.hasIsland = false;
		this.saveX = 0;
		this.saveY = 0;
		this.saveZ = 0;
		this.recommendDate = "";
		this.farmDates = new HashMap<String, String>();
		this.tips = new ArrayList<String>();
		this.huntExp = 0;
		this.mineExp = 0;
		this.farmExp = 0;
		this.huntLevel = 0;
		this.mineLevel = 0;
		this.farmLevel = 0;
		this.skin = -1;
		this.skins = new ArrayList<String>();
		this.wheelOfFortune = 0;
		this.optMenu = true;
		this.optBoat = true;
		this.optChat = false;
		this.optWhisper = true;
		
		try {
			this.id = id;
			
			if (!file.exists()) {
				folder.mkdir();
				folder2.mkdir();
				file.createNewFile();
				
				this.save();
				
				File uuidf = new File("plugins/EfeServer/uuidlist.yml");
				if (!uuidf.exists()) file.createNewFile();
				
				FileConfiguration uuidc = YamlConfiguration.loadConfiguration(uuidf);
				
				uuidc.set(islName, id.toString());
				uuidc.save(uuidf);
				
				return;
			}
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			if (config.contains("islName")) this.islName = config.getString("islName");
			if (config.contains("tutorial")) this.tutorial = config.getInt("tutorial");
			if (config.contains("hasPlayedIntro")) this.hasPlayedIntro = config.getBoolean("hasPlayedIntro");
			if (config.contains("hasIsland")) this.hasIsland = config.getBoolean("hasIsland");
			if (config.contains("save.x")) this.saveX = config.getDouble("save.x");
			if (config.contains("save.y")) this.saveY = config.getDouble("save.y");
			if (config.contains("save.z")) this.saveZ = config.getDouble("save.z");
			if (config.contains("recommendDate")) this.recommendDate = config.getString("recommendDate");
			if (config.contains("farmDates")) {
				for (String path : config.getKeys(true)) {
					if (!path.startsWith("farmDates.")) continue;
					
					this.farmDates.put(path.substring(10), config.getString(path));
				}
			}
			if (config.contains("tips")) this.tips = config.getStringList("tips");
			if (config.contains("skin")) this.skin = config.getInt("skin");
			if (config.contains("skins")) this.skins = config.getStringList("skins");
			if (config.contains("wheelOfFortune")) this.wheelOfFortune = config.getInt("wheelOfFortune");
			if (config.contains("huntExp")) this.huntExp = config.getInt("huntExp");
			if (config.contains("mineExp")) this.mineExp = config.getInt("mineExp");
			if (config.contains("farmExp")) this.farmExp = config.getInt("farmExp");
			if (config.contains("huntLevel")) this.huntLevel = config.getInt("huntLevel");
			if (config.contains("mineLevel")) this.mineLevel = config.getInt("mineLevel");
			if (config.contains("farmLevel")) this.farmLevel = config.getInt("farmLevel");
			if (config.contains("option.menu")) this.optMenu = config.getBoolean("option.menu");
			if (config.contains("option.boat")) this.optBoat = config.getBoolean("option.boat");
			if (config.contains("option.chat")) this.optChat = config.getBoolean("option.chat");
			if (config.contains("option.whisper")) this.optWhisper = config.getBoolean("option.whisper");
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
	
	public static PlayerData getPlayerData(String islName) {
		UUID id = UUID.fromString(YamlConfiguration.loadConfiguration(new File("plugins/EfeServer/uuidlist.yml")).getString(islName));
		return PlayerData.get(id);
	}
	
	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(id);
	}
	
	public UUID getUniqueId() {
		return this.id;
	}
	
	public String getIslName() {
		return this.islName;
	}
	
	public int getTutorialState() {
		return this.tutorial;
	}
	
	public boolean hasPlayedIntro() {
		return this.hasPlayedIntro;
	}
	
	public boolean hasIsland() {
		return this.hasIsland;
	}
	
	public int getFarmAmount() {
		return this.farmDates.size();
	}
	
	public Location getSaveLocation() {
		return new Location(Bukkit.getWorld("world"), this.saveX, this.saveY, this.saveZ);
	}
	
	public String getFarmDate(Location centerLoc) {
		return this.farmDates.get(convertLoc(centerLoc));
	}
	
	public boolean canRecommend() {
		if (this.recommendDate.isEmpty())
			return true;
		
		try {
			Date today = new Date();
			Date last = format.parse(this.recommendDate);
			
			return today.after(last);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean hasTip(String tip) {
		return tips.contains(tip);
	}
	
	public int getHuntExp() {
		return this.huntExp;
	}
	
	public int getMineExp() {
		return this.mineExp;
	}
	
	public int getFarmExp() {
		return this.farmExp;
	}
	
	public int getHuntLevel() {
		return this.huntLevel;
	}
	
	public int getMineLevel() {
		return this.mineLevel;
	}
	
	public int getFarmLevel() {
		return this.farmLevel;
	}
	
	public int getBloodSkin() {
		return this.skin;
	}
	
	public List<String> getBloodSkins() {
		return this.skins;
	}
	
	public int getWheelOfFortune() {
		return this.wheelOfFortune;
	}
	
	public boolean getOptionMenu() {
		return this.optMenu;
	}
	
	public boolean getOptionBoat() {
		return this.optBoat;
	}
	
	public boolean getOptionChat() {
		return this.optChat;
	}
	
	public boolean getOptionWhisper() {
		return this.optWhisper;
	}
	
	public void setTutorialState(int tutorial) {
		this.tutorial = tutorial;
		
		save();
	}
	
	public void setPlayedIntro() {
		this.hasPlayedIntro = true;
		
		save();
	}
	
	public void setIsland(boolean hasIsland) {
		this.hasIsland = hasIsland;
		
		save();
	}
	
	public void addFarm(Location centerLoc) {
		this.farmDates.put(convertLoc(centerLoc), "");
		
		save();
	}
	
	public void removeFarm(Location centerLoc) {
		this.farmDates.remove(convertLoc(centerLoc));
		
		save();
	}
	
	public void setSaveLocation(Location loc) {
		this.saveX = loc.getX();
		this.saveY = loc.getY();
		this.saveZ = loc.getZ();
		
		save();
	}
	
	public void setFarmDate(Location centerLoc, String farmDate) {
		this.farmDates.put(convertLoc(centerLoc), farmDate);
		
		save();
	}
	
	public void recommend() {
		Date date = new Date();
		this.recommendDate = format.format(date);
		
		save();
	}
	
	public void addTip(String tip) {
		this.tips.add(tip);
		
		save();
	}
	
	public void setHuntExp(int huntExp) {
		this.huntExp = huntExp;
		
		save();
	}
	
	public void setMineExp(int mineExp) {
		this.mineExp = mineExp;
		
		save();
	}
	
	public void setFarmExp(int farmExp) {
		this.farmExp = farmExp;
		
		save();
	}
	
	public void setHuntLevel(int huntLevel) {
		this.huntLevel = huntLevel;
		
		save();
	}
	
	public void setMineLevel(int mineLevel) {
		this.mineLevel = mineLevel;
		
		save();
	}
	
	public void setFarmLevel(int farmLevel) {
		this.farmLevel = farmLevel;
		
		save();
	}
	
	public void setBloodSkin(int index) {
		this.skin = index;
		
		save();
	}
	
	public void addBloodSkin(String skin) {
		if (this.skins.contains(skin)) return;
		
		this.skins.add(skin);
		
		save();
	}
	
	public void removeBloodSkin(String skin) {
		this.skins.remove(skin);
		
		save();
	}
	
	public boolean hasBloodSkin(String skin) {
		
		return this.skins.contains(skin);
	}
	
	public void giveWheelOfFortune() {
		this.wheelOfFortune ++;
		
		save();
	}
	
	public void takeWheelOfFortune() {
		this.wheelOfFortune --;
		
		save();
	}
	
	public void setOptionMenu(boolean on) {
		this.optMenu = on;
		
		save();
	}
	
	public void setOptionBoat(boolean on) {
		this.optBoat = on;
		
		save();
	}
	
	public void setOptionChat(boolean on) {
		this.optChat = on;
		
		save();
	}
	
	public void setOptionWhisper(boolean on) {
		this.optWhisper = on;
		
		save();
	}
	
	private String convertLoc(Location loc) {
		return loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ();
	}
	
	public void save() {
		File file = new File("plugins/EfeServer/PlayerData/"+id.toString()+".yml");
		
		try {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			config.set("islName", this.islName);
			config.set("tutorial", this.tutorial);
			config.set("hasPlayedIntro", this.hasPlayedIntro);
			config.set("hasIsland", this.hasIsland);
			config.set("save.x", this.saveX);
			config.set("save.y", this.saveY);
			config.set("save.z", this.saveZ);
			
			for (String path : config.getKeys(true)) {
				if (!path.startsWith("farmDates.")) continue;
				
				config.set(path, null);
			}
			
			for (String key : farmDates.keySet()) {
				config.set("farmDates."+key, farmDates.get(key));
			}
			
			config.set("recommendDate", this.recommendDate);
			config.set("tips", this.tips);
			config.set("huntExp", this.huntExp);
			config.set("mineExp", this.mineExp);
			config.set("farmExp", this.farmExp);
			config.set("huntLevel", this.huntLevel);
			config.set("mineLevel", this.mineLevel);
			config.set("farmLevel", this.farmLevel);
			config.set("skin", this.skin);
			config.set("skins", this.skins);
			config.set("wheelOfFortune", this.wheelOfFortune);
			config.set("option.menu", this.optMenu);
			config.set("option.boat", this.optBoat);
			config.set("option.chat", this.optChat);
			config.set("option.whisper", this.optWhisper);
			
			config.save(file);
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
	
	private static HashMap<UUID, PlayerData> dataMap = new HashMap<UUID, PlayerData>();
	
	public static PlayerData get(OfflinePlayer player) {
		return get(player.getUniqueId());
	}
	
	public static PlayerData get(UUID id) {
		return dataMap.containsKey(id) ? dataMap.get(id) : new PlayerData(id);
	}
}