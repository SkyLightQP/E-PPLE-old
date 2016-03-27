package me.efe.efeserver.reform.farming;

import java.util.Random;

import me.efe.efeitems.ItemStorage;
import me.efe.skilltree.Skill;
import me.efe.skilltree.SkillManager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Crop {
	
	CARROT("´ç±Ù", "farm.carrot", new int[]{20, 20, 20, 15, 15, 15}, new int[]{20, 18, 16, 14, 12, 10}, 1.0D, 0, 0,
			new ItemStack(Material.CARROT_ITEM), 4, "´ç±Ù", Material.CARROT_ITEM, ItemStorage.VALUABLE_CARROT.clone(), Material.CARROT),
	POTATO("°¨ÀÚ", "farm.potato", new int[]{20, 20, 20, 15, 15, 15}, new int[]{20, 18, 16, 14, 12, 10}, 1.0D, 0, 0,
			new ItemStack(Material.POTATO_ITEM), 4, "°¨ÀÚ", Material.POTATO_ITEM, ItemStorage.VALUABLE_POTATO.clone(), Material.POTATO),
	WHEAT("¹Ð", "farm.wheat", new int[]{25, 25, 25, 30, 30, 30}, new int[]{20, 18, 16, 14, 12, 10}, 1.0D, 0, 0,
			new ItemStack(Material.SEEDS), 8, "¹Ð ¾¾¾Ñ", Material.WHEAT, ItemStorage.VALUABLE_WHEAT.clone(), Material.CROPS),
	MELON("¼ö¹Ú", "farm.melon", new int[]{80, 80, 80, 70, 70, 70}, new int[]{30, 28, 26, 24, 22, 20}, 2.0D, 0, 0,
			new ItemStack(Material.MELON_SEEDS), 4, "¼ö¹Ú¾¾", Material.MELON, Material.MELON_STEM),
	PUMPKIN("È£¹Ú", "farm.pumpkin", new int[]{40, 40, 40, 35, 35, 35}, new int[]{30, 28, 26, 24, 22, 20}, 2.0D, 0, 0,
			new ItemStack(Material.PUMPKIN_SEEDS), 4, "È£¹Ú¾¾", Material.PUMPKIN, Material.PUMPKIN_STEM),
	NETHER_WARTS("Áö¿Á »ç¸¶±Í", "farm.nether-wart", new int[]{0, 0, 0, 0, 0, 0}, new int[]{40, 38, 36, 34, 32, 30}, 3.0D, 0, 0,
			ItemStorage.NETHER_WART_SEEDS, 1, "Áö¿Á »ç¸¶±Í ¾¾¾Ñ", Material.NETHER_STALK, Material.NETHER_WARTS);
	
	private static Random random = new Random();
	private final String name;
	private final String skillName;
	private final int[] waterArr;
	private final int[] timeArr;
	private final double expMultiplier;
	private final int maxYield;
	private final int minYield;
	private final ItemStack seedItem;
	private final int seedAmount;
	private final String seedName;
	private final Material itemType;
	private final ItemStack specialItem;
	private final Material blockType;
	
	private Crop(String name, String skillName, int[] waterArr, int[] timeArr, double expMultiplier, int maxYield, int minYield,
			ItemStack seedItem, int seedAmount, String seedName, Material itemType, Material blockType) {
		this(name, skillName, waterArr, timeArr, expMultiplier, maxYield, minYield, seedItem, seedAmount, seedName, itemType, null, blockType);
	}
	
	private Crop(String name, String skillName, int[] waterArr, int[] timeArr, double expMultiplier, int maxYield, int minYield,
			ItemStack seedItem, int seedAmount, String seedName, Material itemType, ItemStack specialItem, Material blockType) {
		this.name = name;
		this.skillName = skillName;
		this.waterArr = waterArr;
		this.timeArr = timeArr;
		this.expMultiplier = expMultiplier;
		this.maxYield = maxYield;
		this.minYield = minYield;
		this.seedItem = seedItem;
		this.seedAmount = seedAmount;
		this.seedName = seedName;
		this.itemType = itemType;
		this.blockType = blockType;
		this.specialItem = specialItem;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Skill getSkill() {
		return SkillManager.getSkill(skillName);
	}
	
	public int getWater(int level) {
		return this.waterArr[level - 1];
	}
	
	public int getTime(int level) {
		return this.timeArr[level - 1];
	}
	
	public double getExpMultiplier() {
		return this.expMultiplier;
	}
	
	public int getYield() {
		return random.nextInt(this.maxYield - this.minYield + 1) + this.minYield;
	}
	
	public ItemStack getSeedItem() {
		return this.seedItem;
	}
	
	public int getSeedAmount() {
		return this.seedAmount;
	}
	
	public String getSeedName() {
		return  this.seedName;
	}
	
	public Material getItemType() {
		return this.itemType;
	}
	
	public ItemStack getSpecialItem() {
		return this.specialItem;
	}
	
	public Material getBlockType() {
		return this.blockType;
	}
	
	public static Crop getCrop(String name) {
		for (Crop crop : values()) {
			if (crop.getName().equals(name)) {
				return crop;
			}
		}
		
		return null;
	}
}
