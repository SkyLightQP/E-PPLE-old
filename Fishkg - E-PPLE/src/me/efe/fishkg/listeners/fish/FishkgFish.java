package me.efe.fishkg.listeners.fish;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.efe.fishkg.Fishkg;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class FishkgFish implements Cloneable {
	private String name;
	private double percent;
	private double lengthMin;
	private double lengthMax;
	private PotionEffect effect = null;
	private double damage = 0.0D;
	private String message = null;
	
	protected FishRank rank = null;
	protected double length = 0.0D;
	protected ItemStack item = null;
	
	public FishkgFish(String name, double percent, double lengthMin, double lengthMax) {
		this.name = name;
		this.percent = percent;
		this.lengthMin = lengthMin;
		this.lengthMax = lengthMax;
	}
	
	public FishkgFish effect(PotionEffect effect, String message) {
		this.effect = effect;
		this.message = message;
		
		return this;
	}
	
	public FishkgFish damage(double damage, String message) {
		this.damage = damage;
		this.message = message;
		
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getObjectiveName() {
		char c = this.name.charAt(this.name.length() - 1);
		
		return ((c - 44032) % 28 != 0) ? this.name + "聖" : this.name + "研";
	}
	
	public double getPercent() {
		return this.percent;
	}
	
	public double getLength() {
		if (this.length == 0.0D) {
			double rand = new Random().nextDouble();
			
			this.length = this.lengthMin + (rand * (this.lengthMax - this.lengthMin));
			this.length = ((double) ((int) (this.length * 10))) / 10;
		}
		
		return this.length;
	}
	
	public void setLength(double length) {
		this.length = length;
	}
	
	public boolean hasEffect() {
		return this.effect != null;
	}
	
	public boolean hasDamage() {
		return this.damage != 0.0D;
	}
	
	public PotionEffect getEffect() {
		return this.effect;
	}
	
	public double getDamage() {
		return this.damage;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public FishRank getRank() {
		return this.rank;
	}
	
	public ItemStack getItemStack() {
		return this.item;
	}
	
	public void generateItemStack(Fishkg plugin, Player p, boolean isFirst, boolean mas) {
		item = new ItemStack(Material.RAW_FISH);
		
		if (!plugin.getConfig().getBoolean("fish.enable")) return;
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		double rand = Math.random() - (mas ? 0.05D : 0.0D);
		
		if (rand <= 0.05D)
			this.rank = FishRank.S;
		else if (rand <= 0.05D + 0.1D)
			this.rank = FishRank.A;
		else if (rand <= 0.05D + 0.1D + 0.2D)
			this.rank = FishRank.B;
		else if (rand <= 0.05D + 0.1D + 0.2D + 0.3D)
			this.rank = FishRank.C;
		else
			this.rank = FishRank.D;
		
		if (plugin.getConfig().getBoolean("fish.addGrade"))
			lore.add("」7去厭: "+rank.display);
		
		lore.add("」7"+length+"cm");
		lore.add("」7Fished by 」l"+p.getName());
		
		meta.setDisplayName("」9"+this.getName());
		meta.setLore(lore);
		
		item.setItemMeta(meta);
	}
	
	public FishkgFish clone() {
		try {
			return (FishkgFish) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public enum FishRank{
		S("」5」lS"), 
		A("」d」lA"), 
		B("」9」lB"), 
		C("」3C"), 
		D("」bD");
		
		String display;
		
		FishRank(String display) {
			this.display = display;
		}
	}
}