package me.efe.eferudish;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class PotionRecipe {
	private final ItemStack result;
	private final List<ItemStack> ingredients;
	
	public PotionRecipe(ItemStack result, List<ItemStack> ingredients) {
		this.result = result;
		this.ingredients = ingredients;
	}
	
	public ItemStack getResult() {
		return result.clone();
	}
	
	public List<ItemStack> getIngredients() {
		return ingredients;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private ItemStack result;
		private List<ItemStack> ingredients;
		
		public Builder() {
			this.ingredients = new ArrayList<ItemStack>();
		}
		
		public Builder setResult(ItemStack item) {
			this.result = item;
			return this;
		}
		
		public Builder addIngredient(ItemStack item) {
			this.ingredients.add(item);
			return this;
		}
		
		public PotionRecipe build() {
			return new PotionRecipe(result, ingredients);
		}
	}
}
