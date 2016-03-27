package me.efe.efemobs.rudish;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class UserData {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	private UUID id;
	private int soul;
	private int storedSoul;
	private int maxFloor;
	private int bossCount;
	private String bossLast;
	private boolean hasPlayedBefore;
	private int bookCount;
	
	public UserData(OfflinePlayer p) {
		this(p.getUniqueId());
	}
	
	public UserData(UUID id) {
		this.id = id;
		
		File file = new File("plugins/EfeMobs/UserData/"+id.toString()+".yml");
		
		//Data Basic Setting
		this.soul = 0;
		this.storedSoul = 0;
		this.maxFloor = 1;
		this.hasPlayedBefore = false;
		this.bookCount = 0;
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return;
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		if (config.contains("soul")) this.soul = config.getInt("soul");
		if (config.contains("storedSoul")) this.storedSoul = config.getInt("storedSoul");
		if (config.contains("maxFloor")) this.maxFloor = config.getInt("maxFloor");
		if (config.contains("boss.count")) this.bossCount = config.getInt("boss.count");
		if (config.contains("boss.last")) this.bossLast = config.getString("boss.last");
		if (config.contains("hasPlayedBefore")) this.hasPlayedBefore = config.getBoolean("hasPlayedBefore");
		if (config.contains("bookCount")) this.bossCount = config.getInt("bookCount");
	}
	
	public int getSoul() {
		return this.soul;
	}
	
	public int getStoredSoul() {
		return this.storedSoul;
	}
	
	public int getMaxFloor() {
		return this.maxFloor;
	}
	
	public int getBossCount() {
		refreshBossCount();
		
		return this.bossCount;
	}

	public boolean hasPlayedBefore() {
		return this.hasPlayedBefore;
	}
	
	public int getBookCount() {
		return this.bookCount;
	}
	
	public void setSoul(int amount) {
		this.soul = amount;
		
		save();
	}
	
	public void setStoredSoul(int amount) {
		this.storedSoul = amount;
		
		save();
	}
	
	public void giveSoul(int amount) {
		this.soul += amount;
		
		save();
	}
	
	public void takeSoul(int amount) {
		this.soul -= amount;
		
		save();
	}
	
	public void raiseFloor() {
		if (this.maxFloor == 10) return;
		
		this.maxFloor ++;
		
		save();
	}
	
	public void setMaxFloor(int maxFloor) {
		this.maxFloor = maxFloor;
		
		save();
	}
	
	public void addBossCount() {
		refreshBossCount();
		
		this.bossCount ++;
		
		save();
	}
	
	public void setPlayedBefore() {
		this.hasPlayedBefore = true;
		
		save();
	}
	
	public void addBookCount() {
		this.bookCount ++;
		
		save();
	}
	
	public void setBookCount(int bookCount) {
		this.bookCount = bookCount;
		
		save();
	}
	
	private void refreshBossCount() {
		if (this.bossLast == null) {
			this.bossLast = format.format(Calendar.getInstance().getTime());
			return;
		}
		
		try {
			Calendar now = Calendar.getInstance();
			Calendar last = Calendar.getInstance();
			
			last.setTime(format.parse(this.bossLast));
			
			if (now.get(Calendar.YEAR) != last.get(Calendar.YEAR) || 
					now.get(Calendar.MONTH) != last.get(Calendar.MONTH) || 
					now.get(Calendar.DAY_OF_MONTH) != last.get(Calendar.DAY_OF_MONTH)) {
				this.bossCount = 0;
				this.bossLast = format.format(now.getTime());
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void save() {
		try {
			File file = new File("plugins/EfeMobs/UserData/"+id.toString()+".yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			config.set("soul", this.soul);
			config.set("storedSoul", this.storedSoul);
			config.set("maxFloor", this.maxFloor);
			config.set("boss.count", this.bossCount);
			config.set("boss.last", this.bossLast);
			config.set("hasPlayedBefore", this.hasPlayedBefore);
			config.set("bookCount", this.bookCount);
			
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}