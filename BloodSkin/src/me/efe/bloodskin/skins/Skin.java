package me.efe.bloodskin.skins;

import me.efe.bloodskin.BloodSkin;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Skin {
	protected static BloodSkin plugin;
	private String name;
	private boolean isPremium;
	private boolean isGettable;
	private ItemStack icon;
	
	public Skin(String name, boolean isPremium, boolean isGettable, String display, ItemStack item) {
		this.name = name;
		this.isPremium = isPremium;
		this.isGettable = isGettable;
		this.icon = plugin.util.createDisplayItem((isPremium ? "§b" : "§a") + display, item, isPremium ? new String[]{"프리미엄 스킨"} : new String[]{});
	}
	
	public static void init(BloodSkin pl) {
		plugin = pl;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isPremium() {
		return this.isPremium;
	}
	
	public boolean isGettable() {
		return this.isGettable;
	}
	
	public ItemStack getIcon() {
		return this.icon;
	}
	
	public abstract void effect(Player p, LivingEntity entity);
	
	protected Location getBody(LivingEntity entity) {
		double eye = entity.getEyeHeight(false);
		
		return entity.getLocation().add(0.0D, eye * 2/3, 0.0D);
	}
}