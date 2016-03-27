package me.efe.fishkg.listeners.fish;

import java.util.ArrayList;
import java.util.List;

import me.efe.fishkg.Fishkg;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FishkgTrash extends FishkgFish {
	private ItemStack icon;
	
	public FishkgTrash(String name, double length, ItemStack icon) {
		this(name, length, length, icon);
	}
	
	public FishkgTrash(String name, double lengthMin, double lengthMax, ItemStack icon) {
		super(name, 0.0D, lengthMin, lengthMax);
		
		this.icon = icon;
	}
	
	@Override
	public void generateItemStack(Fishkg plugin, Player p, boolean isFirst, boolean mas) {
		item = icon.clone();
		
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
			lore.add("§7등급: "+rank.display);
		
		lore.add("§7"+length+"cm");
		lore.add("§7Fished by §l"+p.getName());
		
		if (isFirst)
			lore.add("§71위 한 물고기");
		
		if (isFirst)
			meta.setDisplayName("§6§l"+this.getName());
		else
			meta.setDisplayName("§9"+this.getName());
		
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
}