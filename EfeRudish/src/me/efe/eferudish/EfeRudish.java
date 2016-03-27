package me.efe.eferudish;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeitems.ItemStorage;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EfeRudish extends JavaPlugin {
	private static EfeRudish instance;
	private Random random = new Random();
	private List<PotionRecipe> brewingRecipes = new ArrayList<PotionRecipe>();
	private List<AlchemyRecipe> alchemyRecipes = new ArrayList<AlchemyRecipe>();
	private PotionRecipe brewingRecipeOfDay;
	
	@Override
	public void onEnable() {
		instance = this;
		
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 8195))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.NETHER_STALK))
				.addIngredient(new ItemStack(Material.MAGMA_CREAM))
				.build());
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 8194))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.NETHER_STALK))
				.addIngredient(new ItemStack(Material.SUGAR))
				.build());
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 8197))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.NETHER_STALK))
				.addIngredient(new ItemStack(Material.SPECKLED_MELON))
				.build());
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 8229))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.NETHER_STALK))
				.addIngredient(new ItemStack(Material.SPECKLED_MELON, 3))
				.build());
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 8193))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.NETHER_STALK))
				.addIngredient(new ItemStack(Material.GHAST_TEAR))
				.build());
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 8225))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.NETHER_STALK))
				.addIngredient(new ItemStack(Material.GHAST_TEAR, 3))
				.build());
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 8202))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.NETHER_STALK))
				.addIngredient(new ItemStack(Material.SUGAR))
				.addIngredient(new ItemStack(Material.FERMENTED_SPIDER_EYE))
				.build());
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 8196))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.NETHER_STALK))
				.addIngredient(new ItemStack(Material.SPIDER_EYE))
				.build());
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 8200))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.FERMENTED_SPIDER_EYE))
				.build());
		this.brewingRecipes.add(PotionRecipe.builder()
				.setResult(new ItemStack(Material.POTION, 1, (short) 16393))
				.addIngredient(new ItemStack(Material.GLASS_BOTTLE))
				.addIngredient(new ItemStack(Material.BLAZE_POWDER))
				.addIngredient(new ItemStack(Material.SULPHUR))
				.build());
		
		this.alchemyRecipes.add(AlchemyRecipe.builder()
				.setDisplayResult(EfeUtils.item.createDisplayItem("§b모루", new ItemStack(Material.ANVIL),
						new String[]{"아이템을 1회 수리할 수 있습니다.", "세 가지 등급이 있으며, 하급 모루는", "아이템을 파괴시키기도 합니다."}))
				.addResult(ItemStorage.ANVIL_ABANDONED.clone(), 0.5)
				.addResult(ItemStorage.ANVIL_OLD.clone(), 0.45)
				.addResult(ItemStorage.ANVIL_ENCHANTED, 0.05)
				.addIngredient(new ItemStack(Material.IRON_BLOCK, 5))
				.addIngredient(new ItemStack(Material.ENCHANTED_BOOK))
				.build());
		this.alchemyRecipes.add(AlchemyRecipe.builder()
				.setDisplayResult(EfeUtils.item.createDisplayItem("§b동물 질병 치료제", new ItemStack(Material.POTION, 1, (short) 16389),
						new String[]{"동물의 유전병을 치료할 수 있습니다.", "투척형으로, 다수 동물의 치료가 가능합니다."}))
				.addResult(ItemStorage.ANIMAL_MEDICINE.clone(), 1)
				.addIngredient(new ItemStack(Material.GOLDEN_APPLE))
				.addIngredient(new ItemStack(Material.POTION, 1, (short) 8200))
				.addIngredient(new ItemStack(Material.SULPHUR))
				.addIngredient(new ItemStack(Material.ENCHANTED_BOOK))
				.build());
		this.alchemyRecipes.add(AlchemyRecipe.builder()
				.setDisplayResult(EfeUtils.item.createDisplayItem("§b마법의 화로", EfeUtils.item.enchant(new ItemStack(Material.FURNACE), Enchantment.SILK_TOUCH, 100),
						new String[]{"다양한 기능을 가진 화로입니다.", "단, 사용시 1% 확률로 화로가 파괴됩니다."}))
				.addResult(ItemStorage.FURNACE_SPEED_1.clone(), 0.15)
				.addResult(ItemStorage.FURNACE_SPEED_2.clone(), 0.1)
				.addResult(ItemStorage.FURNACE_SPEED_3.clone(), 0.05)
				.addResult(ItemStorage.FURNACE_FUEL_1.clone(), 0.15)
				.addResult(ItemStorage.FURNACE_FUEL_2.clone(), 0.1)
				.addResult(ItemStorage.FURNACE_FUEL_3.clone(), 0.05)
				.addResult(ItemStorage.FURNACE_FORTUNE_1.clone(), 0.15)
				.addResult(ItemStorage.FURNACE_FORTUNE_2.clone(), 0.1)
				.addResult(ItemStorage.FURNACE_FORTUNE_3.clone(), 0.05)
				.addResult(ItemStorage.FURNACE_REMAIN.clone(), 0.1)
				.addIngredient(new ItemStack(Material.FURNACE))
				.addIngredient(new ItemStack(Material.DIAMOND))
				.addIngredient(new ItemStack(Material.ENCHANTED_BOOK))
				.build());
		
		setBrewingRecipeOfDay();
		
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(new BrewingGUI(this), this);
		//manager.registerEvents(new AlchemyGUI(this), this);
		
		getLogger().info(getDescription().getFullName()+" has been enabled!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info(getDescription().getFullName()+" has been disabled.");
	}
	
	public static EfeRudish getInstance() {
		return instance;
	}
	
	public void setBrewingRecipeOfDay() {
		int index = random.nextInt(brewingRecipes.size());
		this.brewingRecipeOfDay = this.brewingRecipes.get(index);
	}
	
	public PotionRecipe getBrewingRecipeOfDay() {
		return brewingRecipeOfDay;
	}
	
	public List<AlchemyRecipe> getAlchemyRecipes() {
		return alchemyRecipes;
	}
}
