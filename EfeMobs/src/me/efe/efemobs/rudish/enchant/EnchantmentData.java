package me.efe.efemobs.rudish.enchant;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantmentData {
	private Random random = new Random();
	private final Map<Enchantment, Map<Integer, Double>> enchMap;
	private final Map<Enchantment, Double> probabilityMap;
	
	public EnchantmentData(Map<Enchantment, Map<Integer, Double>> enchMap, Map<Enchantment, Double> probabilityMap) {
		this.enchMap = enchMap;
		this.probabilityMap = probabilityMap;
	}
	
	public void enchant(ItemStack itemStack) {
		for (Enchantment ench : enchMap.keySet()) {
			if (Math.random() <= probabilityMap.get(ench)) {
				double currentVar = 0.0D;
				double randomVar = Math.random();
				
				for (Integer level : enchMap.get(ench).keySet()) {
					currentVar += enchMap.get(ench).get(level);
					
					if (randomVar <= currentVar) {
						itemStack.addUnsafeEnchantment(ench, level);
					}
				}
			}
		}
		
		if (itemStack.getEnchantments().isEmpty()) {
			int level = random.nextInt(3) + 1;
			itemStack.addEnchantment(Enchantment.DURABILITY, level);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private Map<Enchantment, Map<Integer, Double>> enchMap = new HashMap<Enchantment, Map<Integer, Double>>();
		private Map<Enchantment, Double> probabilityMap = new HashMap<Enchantment, Double>();
		
		public Builder addEnchantment(Enchantment ench, int level, double percent) {
			if (!enchMap.containsKey(ench)) {
				enchMap.put(ench, new HashMap<Integer, Double>());
			}
			
			enchMap.get(ench).put(level, percent);
			
			return this;
		}
		
		public Builder addEnchantment(Enchantment ench, double percent) {
			probabilityMap.put(ench, percent);
			
			return this;
		}
		
		public EnchantmentData build() {
			return new EnchantmentData(enchMap, probabilityMap);
		}
	}
}
