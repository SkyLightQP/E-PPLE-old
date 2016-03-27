package me.efe.eferudish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class AlchemyRecipe {
	private final ItemStack displayResult;
	private final Map<ItemStack, Double> resultMap;
	private final List<ItemStack> ingredients;
	
	public AlchemyRecipe(ItemStack displayResult, Map<ItemStack, Double> resultMap, List<ItemStack> ingredients) {
		this.displayResult = displayResult;
		this.resultMap = resultMap;
		this.ingredients = ingredients;
	}
	
	public ItemStack getDisplayResult() {
		return displayResult.clone();
	}
	
	public ItemStack getResult() {
		double random = Math.random();
		double percent = 0.0D;
		
		for (ItemStack result : resultMap.keySet()) {
			percent += resultMap.get(result);
			
			if (percent >= random) {
				return result;
			}
		}
		
		return null;
	}
	
	public List<ItemStack> getIngredients() {
		return ingredients;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private ItemStack displayResult;
		private Map<ItemStack, Double> resultMap;
		private List<ItemStack> ingredients = new ArrayList<ItemStack>();
		
		public Builder() {
			this.resultMap = new HashMap<ItemStack, Double>();
		}
		
		public Builder setDisplayResult(ItemStack item) {
			this.displayResult = item;
			return this;
		}
		
		public Builder addResult(ItemStack item, double percent) {
			this.resultMap.put(item, percent);
			return this;
		}
		
		public Builder addIngredient(ItemStack item) {
			this.ingredients.add(item);
			return this;
		}
		
		public AlchemyRecipe build() {
			return new AlchemyRecipe(displayResult, resultMap, ingredients);
		}
	}
}
