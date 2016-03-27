package me.efe.efeshops.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class ShopData {
	private int number;
	private Chest chest;
	private LivingEntity merchant;
	private List<String> lines;
	private UUID id;
	private boolean isBuy;
	private List<ItemStack> items;
	private List<Integer> prices;
	private final static File dataFile = new File("plugins/EfeShops/data.yml");
	
	public ShopData(int number, Chest chest, LivingEntity merchant) {
		this.number = number;
		this.chest = chest;
		this.merchant = merchant;
		
		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
		
		this.lines = config.getStringList(number+".lines");
		this.id = UUID.fromString(config.getString(number+".id"));
		this.isBuy = config.getBoolean(number+".isBuy");
		this.items = new ArrayList<ItemStack>();
		this.prices = config.getIntegerList(number+".prices");
		
		for (Object obj : config.getList(number+".items"))
			this.items.add((ItemStack) obj);
	}
	
	public String[] getLines() {
		return lines.toArray(new String[lines.size()]);
	}
	
	public OfflinePlayer getOwner() {
		return Bukkit.getOfflinePlayer(id);
	}
	
	public boolean isBuying() {
		return this.isBuy;
	}
	
	public List<ItemStack> getItems() {
		return this.items;
	}
	
	public int getPrice(ItemStack item) {
		return prices.get(items.indexOf(item));
	}
	
	public void setPrice(ItemStack item, int price) {
		prices.set(items.indexOf(item), price);
		
		save();
	}
	
	public void replaceItem(ItemStack origin, ItemStack replace) {
		items.set(items.indexOf(origin), replace);
		
		save();
	}
	
	public void updateAmount(ItemStack item, int amount) {
		int value = item.clone().getAmount() - amount;
		
		if (value <= 0) {
			removeItem(item);
		} else {
			ItemStack val = item.clone();
			val.setAmount(value);
			
			items.set(items.indexOf(item), val);
			
			save();
		}
	}
	
	public void addItem(ItemStack item, int price) {
		items.add(item);
		prices.add(price);
		
		save();
	}
	
	public void removeItem(ItemStack item) {
		prices.remove(items.indexOf(item));
		items.remove(item);
		
		save();
	}
	
	public void save() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
		
		config.set(number+".items", items);
		config.set(number+".prices", prices);
		
		try {
			config.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Chest getChest() {
		return this.chest;
	}
	
	public LivingEntity getMerchant() {
		return this.merchant;
	}
	
	public static int generateNumber() {
		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
		
		int num =  config.contains("lastNumber") ? config.getInt("lastNumber") + 1 : 0;
		
		config.set("lastNumber", num);
		
		return num;
	}
	
	public static ShopData getShopData(Chest chest) {
		int num = Integer.parseInt(chest.getInventory().getItem(0).getItemMeta().getLore().get(0));
		
		return new ShopData(num, chest, null);
	}
	
	public static ShopData getShopData(int number, LivingEntity merchant) {
		return new ShopData(number, null, merchant);
	}
	
	public static void createShopData(int number, String[] lines, UUID id, boolean isBuy) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
		
		List<String> list = new ArrayList<String>();
		
		for (String text : lines) {
			if (!text.isEmpty()) {
				list.add(text);
			}
		}
		
		config.set(number+".lines", list);
		config.set(number+".id", id.toString());
		config.set(number+".isBuy", isBuy);
		config.set(number+".items", new ArrayList<ItemStack>());
		config.set(number+".prices", new ArrayList<Integer>());
		
		try {
			config.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}