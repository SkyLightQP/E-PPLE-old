package me.efe.efemobs.rudish.enchant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantUtils {
	private static Map<Material, EnchantingData> dataMap = new HashMap<Material, EnchantingData>();
	
	static {
		dataMap.put(Material.WOOD_SWORD, EnchantingData.builder()
				.addPrice(1)
				.addPrice(1)
				.addProbability(0.99D)
				.addProbability(0.99D)
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1, 1.0)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1, 0.5)
						.addEnchantment(Enchantment.DAMAGE_ALL, 2, 0.5)
						.build())
				.build());
		dataMap.put(Material.STONE_SWORD, EnchantingData.builder()
				.addPrice(1)
				.addPrice(1)
				.addProbability(0.99D)
				.addProbability(0.99D)
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1, 1.0)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1, 0.5)
						.addEnchantment(Enchantment.DAMAGE_ALL, 2, 0.5)
						.build())
				.build());
		dataMap.put(Material.IRON_SWORD, EnchantingData.builder()
				.addPrice(1)
				.addPrice(1)
				.addPrice(1)
				.addProbability(0.99D)
				.addProbability(0.99D)
				.addProbability(0.99D)
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1, 0.75)
						.addEnchantment(Enchantment.DAMAGE_ALL, 2, 0.25)
						.addEnchantment(Enchantment.DURABILITY, 1, 0.7)
						.addEnchantment(Enchantment.DURABILITY, 2, 0.3)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1, 0.45)
						.addEnchantment(Enchantment.DAMAGE_ALL, 2, 0.35)
						.addEnchantment(Enchantment.DAMAGE_ALL, 3, 0.2)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1, 0.5)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 2, 0.5)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 1, 0.5)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 2, 0.5)
						.addEnchantment(Enchantment.FIRE_ASPECT, 1, 1.0)
						.addEnchantment(Enchantment.DURABILITY, 1, 0.5)
						.addEnchantment(Enchantment.DURABILITY, 2, 0.49)
						.addEnchantment(Enchantment.DURABILITY, 3, 0.1)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1, 0.1)
						.addEnchantment(Enchantment.DAMAGE_ALL, 2, 0.5)
						.addEnchantment(Enchantment.DAMAGE_ALL, 3, 0.3)
						.addEnchantment(Enchantment.DAMAGE_ALL, 4, 0.1)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1, 0.4)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 2, 0.37)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 3, 0.23)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 1, 0.4)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 2, 0.37)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 3, 0.23)
						.addEnchantment(Enchantment.FIRE_ASPECT, 1, 0.5)
						.addEnchantment(Enchantment.FIRE_ASPECT, 2, 0.39)
						.addEnchantment(Enchantment.FIRE_ASPECT, 3, 0.11)
						.addEnchantment(Enchantment.DURABILITY, 1, 0.11)
						.addEnchantment(Enchantment.DURABILITY, 2, 0.66)
						.addEnchantment(Enchantment.DURABILITY, 3, 0.23)
						.build())
				.build());
		dataMap.put(Material.GOLD_SWORD, EnchantingData.builder()
				.addPrice(1)
				.addPrice(1)
				.addPrice(1)
				.addProbability(0.99D)
				.addProbability(0.99D)
				.addProbability(0.99D)
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1.0)
						.addEnchantment(Enchantment.DAMAGE_ALL, 4, 0.23)
						.addEnchantment(Enchantment.DAMAGE_ALL, 5, 0.44)
						.addEnchantment(Enchantment.DAMAGE_ALL, 6, 0.19)
						.addEnchantment(Enchantment.DAMAGE_ALL, 7, 0.04)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1.0)
						.addEnchantment(Enchantment.DAMAGE_ALL, 5, 0.11)
						.addEnchantment(Enchantment.DAMAGE_ALL, 6, 0.49)
						.addEnchantment(Enchantment.DAMAGE_ALL, 7, 0.26)
						.addEnchantment(Enchantment.DAMAGE_ALL, 8, 0.04)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 0.5)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1, 0.5)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 2, 0.5)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 0.5)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 1, 0.5)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 2, 0.5)
						.addEnchantment(Enchantment.FIRE_ASPECT, 0.1)
						.addEnchantment(Enchantment.FIRE_ASPECT, 1, 0.5)
						.addEnchantment(Enchantment.FIRE_ASPECT, 2, 0.39)
						.addEnchantment(Enchantment.FIRE_ASPECT, 3, 0.11)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DAMAGE_ALL, 1.0)
						.addEnchantment(Enchantment.DAMAGE_ALL, 6, 0.4)
						.addEnchantment(Enchantment.DAMAGE_ALL, 7, 0.46)
						.addEnchantment(Enchantment.DAMAGE_ALL, 8, 0.27)
						.addEnchantment(Enchantment.DAMAGE_ALL, 9, 0.19)
						.addEnchantment(Enchantment.DAMAGE_ALL, 10, 0.04)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 0.5)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1, 0.4)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 2, 0.37)
						.addEnchantment(Enchantment.DAMAGE_UNDEAD, 3, 0.23)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 0.5)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 1, 0.4)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 2, 0.37)
						.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 3, 0.23)
						.addEnchantment(Enchantment.FIRE_ASPECT, 0.1)
						.addEnchantment(Enchantment.FIRE_ASPECT, 1, 0.5)
						.addEnchantment(Enchantment.FIRE_ASPECT, 2, 0.39)
						.addEnchantment(Enchantment.FIRE_ASPECT, 3, 0.11)
						.build())
				.build());
		dataMap.put(Material.IRON_PICKAXE, EnchantingData.builder()
				.addPrice(1)
				.addPrice(1)
				.addProbability(0.99D)
				.addProbability(0.99D)
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DIG_SPEED, 1, 0.67)
						.addEnchantment(Enchantment.DIG_SPEED, 2, 0.19)
						.addEnchantment(Enchantment.DIG_SPEED, 3, 0.04)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1, 1.0)
						.addEnchantment(Enchantment.DURABILITY, 1, 0.7)
						.addEnchantment(Enchantment.DURABILITY, 2, 0.3)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DIG_SPEED, 1, 0.23)
						.addEnchantment(Enchantment.DIG_SPEED, 2, 0.66)
						.addEnchantment(Enchantment.DIG_SPEED, 3, 0.11)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1, 0.96)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2, 0.04)
						.addEnchantment(Enchantment.DURABILITY, 1, 0.5)
						.addEnchantment(Enchantment.DURABILITY, 2, 0.49)
						.addEnchantment(Enchantment.DURABILITY, 3, 0.1)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DIG_SPEED, 2, 0.5)
						.addEnchantment(Enchantment.DIG_SPEED, 3, 0.39)
						.addEnchantment(Enchantment.DIG_SPEED, 4, 0.11)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1, 0.67)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2, 0.19)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3, 0.04)
						.addEnchantment(Enchantment.DURABILITY, 1, 0.11)
						.addEnchantment(Enchantment.DURABILITY, 2, 0.66)
						.addEnchantment(Enchantment.DURABILITY, 3, 0.23)
						.build())
				.build());
		dataMap.put(Material.GOLD_PICKAXE, EnchantingData.builder()
				.addPrice(1)
				.addPrice(1)
				.addProbability(0.99D)
				.addProbability(0.99D)
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DIG_SPEED, 1.0)
						.addEnchantment(Enchantment.DIG_SPEED, 2, 0.67)
						.addEnchantment(Enchantment.DIG_SPEED, 3, 0.19)
						.addEnchantment(Enchantment.DIG_SPEED, 4, 0.04)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 0.4)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1, 0.89)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2, 0.11)
						.addEnchantment(Enchantment.DURABILITY, 1.0)
						.addEnchantment(Enchantment.DURABILITY, 1, 0.04)
						.addEnchantment(Enchantment.DURABILITY, 2, 0.26)
						.addEnchantment(Enchantment.DURABILITY, 3, 0.49)
						.addEnchantment(Enchantment.DURABILITY, 4, 0.11)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DIG_SPEED, 1.0)
						.addEnchantment(Enchantment.DIG_SPEED, 2, 0.4)
						.addEnchantment(Enchantment.DIG_SPEED, 3, 0.49)
						.addEnchantment(Enchantment.DIG_SPEED, 4, 0.7)
						.addEnchantment(Enchantment.DIG_SPEED, 5, 0.4)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 0.4)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1, 0.5)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2, 0.39)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3, 0.11)
						.addEnchantment(Enchantment.DURABILITY, 1.0)
						.addEnchantment(Enchantment.DURABILITY, 1, 0.04)
						.addEnchantment(Enchantment.DURABILITY, 2, 0.26)
						.addEnchantment(Enchantment.DURABILITY, 3, 0.49)
						.addEnchantment(Enchantment.DURABILITY, 4, 0.11)
						.build())
				.addEnchantmentData(EnchantmentData.builder()
						.addEnchantment(Enchantment.DIG_SPEED, 1.0)
						.addEnchantment(Enchantment.DIG_SPEED, 3, 0.5)
						.addEnchantment(Enchantment.DIG_SPEED, 4, 0.27)
						.addEnchantment(Enchantment.DIG_SPEED, 5, 0.19)
						.addEnchantment(Enchantment.DIG_SPEED, 6, 0.4)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 0.4)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1, 0.4)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1, 0.37)
						.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1, 0.23)
						.addEnchantment(Enchantment.DURABILITY, 1.0)
						.addEnchantment(Enchantment.DURABILITY, 2, 0.11)
						.addEnchantment(Enchantment.DURABILITY, 3, 0.29)
						.addEnchantment(Enchantment.DURABILITY, 4, 0.49)
						.addEnchantment(Enchantment.DURABILITY, 5, 0.11)
						.build())
				.build());
	}
	
	public static Location getTableLocation() {
		return new Location(Bukkit.getWorld("world"), 43, 66, -31);
	}
	
	public static boolean isEnchantable(ItemStack itemStack) {
		return itemStack.getEnchantments().isEmpty() || (itemStack.getType() == Material.GOLDEN_APPLE && itemStack.getDurability() == 0);
	}
	
	public static double getProbability(ItemStack itemStack, int grade) {
		return dataMap.get(itemStack.getType()).getProbability(grade);
	}
	
	public static void enchant(ItemStack itemStack, int grade) {
		EnchantmentData data = dataMap.get(itemStack.getType()).getEnchantmentData(grade);
		data.enchant(itemStack);
	}
	
	public static int getMaxGrade(ItemStack itemStack) {
		return dataMap.get(itemStack.getType()).getMaxGrade();
	}
	
	public static int getPrice(ItemStack itemStack, int grade) {
		return dataMap.get(itemStack.getType()).getPrice(grade);
	}
	
	public static class EnchantingData {
		private final List<EnchantmentData> dataList;
		private final List<Integer> priceList;
		private final List<Double> probability;
		
		public EnchantingData(List<EnchantmentData> dataList, List<Integer> priceList, List<Double> probability) {
			this.dataList = dataList;
			this.priceList = priceList;
			this.probability = probability;
		}
		
		public void addEnchantmentData(EnchantmentData data) {
			this.dataList.add(data);
		}
		
		public EnchantmentData getEnchantmentData(int grade) {
			return dataList.get(grade - 1);
		}
		
		public int getMaxGrade() {
			return dataList.size();
		}
		
		public int getPrice(int grade) {
			return this.priceList.get(grade - 1);
		}
		
		public double getProbability(int grade) {
			return this.probability.get(grade - 1);
		}
		
		public static Builder builder() {
			return new Builder();
		}
		
		public static class Builder {
			private List<EnchantmentData> dataList = new ArrayList<EnchantmentData>();
			private List<Integer> priceList = new ArrayList<Integer>();
			private List<Double> probability = new ArrayList<Double>();
			
			public Builder addEnchantmentData(EnchantmentData data) {
				dataList.add(data);
				return this;
			}
			
			public Builder addPrice(int price) {
				this.priceList.add(price);
				return this;
			}
			
			public Builder addProbability(double percent) {
				this.probability.add(percent);
				return this;
			}
			
			public EnchantingData build() {
				return new EnchantingData(dataList, priceList, probability);
			}
		}
	}
}