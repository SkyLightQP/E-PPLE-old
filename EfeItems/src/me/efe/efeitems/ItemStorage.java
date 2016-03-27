package me.efe.efeitems;

import java.util.HashMap;

import me.efe.bloodskin.BloodSkin;
import me.efe.bloodskin.skins.Skin;
import me.efe.efecore.util.EfeUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
 * §d: 최상급
 * §b: 상급
 * §a: 중급
 * §r: 하급
 */
public class ItemStorage {
	private static BloodSkin bloodSkin;
	private static HashMap<String, Skin> skins;
	
	public static ItemStack ANVIL_ABANDONED;
	public static ItemStack ANVIL_OLD;
	public static ItemStack ANVIL_ENCHANTED;
	public static ItemStack EYE_OF_TELEPORTATION;
	public static ItemStack EYE_OF_TELEPORTATION_GIFT;
	public static ItemStack SPAWN_CHANGER;
	public static ItemStack LUMINOUS_HOE;
	public static ItemStack HEALING_SPAWNER;
	public static ItemStack DEFORMED_ANVIL;
	public static ItemStack DEFORMED_ENCHANTMENT_TABLE;
	public static ItemStack DEFORMED_BEACON;
	public static ItemStack DEFORMED_BREWING_STAND;
	public static ItemStack BIOME_CREEPER_PLAINS;
	public static ItemStack BIOME_CREEPER_ICE_PLAINS;
	public static ItemStack BIOME_CREEPER_SWAMPLAND;
	public static ItemStack BIOME_CREEPER_JUNGLE;
	public static ItemStack BIOME_CREEPER_DESERT;
	public static ItemStack BIOME_CREEPER_OCEAN;
	public static ItemStack RAINBOW_BOX_WOOL;
	public static ItemStack RAINBOW_BOX_CLAY;
	public static ItemStack SPAWN_EGG_VILLAGER;
	public static ItemStack NAME_CHANGER;
	public static ItemStack SP_RESET_SCROLL;
	public static ItemStack PICK_ME_UP_1;
	public static ItemStack PICK_ME_UP_2;
	public static ItemStack ANIMAL_MEDICINE;
	public static ItemStack FURNACE_SPEED_1;
	public static ItemStack FURNACE_SPEED_2;
	public static ItemStack FURNACE_SPEED_3;
	public static ItemStack FURNACE_FUEL_1;
	public static ItemStack FURNACE_FUEL_2;
	public static ItemStack FURNACE_FUEL_3;
	public static ItemStack FURNACE_FORTUNE_1;
	public static ItemStack FURNACE_FORTUNE_2;
	public static ItemStack FURNACE_FORTUNE_3;
	public static ItemStack FURNACE_REMAIN;
	public static ItemStack RANDOM_TITLE_BOOK;
	public static ItemStack BLOOD_SKIN_BOX;
	public static ItemStack BLOOD_SKIN_BOX_POPULAR;
	public static ItemStack BLOOD_SKIN_BOX_PREMIUM;
	public static ItemStack DECORATION_HEAD_BOX;
	public static ItemStack VALUABLE_CARROT;
	public static ItemStack VALUABLE_POTATO;
	public static ItemStack VALUABLE_WHEAT;
	public static ItemStack NETHER_WART_SEEDS;
	public static ItemStack EMELARD_BOX;
	
	static {
		bloodSkin = (BloodSkin) Bukkit.getPluginManager().getPlugin("BloodSkin");
		skins = new HashMap<String, Skin>();
		
		for (Skin skin : bloodSkin.skins.values()) {
			skins.put(skin.getIcon().getItemMeta().getDisplayName(), skin);
		}
		
		
		ItemStorage.ANVIL_ABANDONED = EfeUtils.item.createItem("§a버려진 모루", new ItemStack(Material.ANVIL, 1, (short) 2), new String[]{"§7아이템을 수리하는 모루."});
		ItemStorage.ANVIL_OLD = EfeUtils.item.createItem("§b낡은 모루", new ItemStack(Material.ANVIL, 1, (short) 1), new String[]{"§7아이템을 수리하는 모루."});
		ItemStorage.ANVIL_ENCHANTED = EfeUtils.item.createItem("§d마법의 모루", new ItemStack(Material.ANVIL), new String[]{"§7아이템을 수리하는 모루."});
		
		ItemStorage.EYE_OF_TELEPORTATION = EfeUtils.item.createItem("§b순간이동의 눈", new ItemStack(Material.EYE_OF_ENDER), new String[]{"§7공간을 초월하는 눈."});
		ItemStorage.EYE_OF_TELEPORTATION_GIFT = EfeUtils.item.createItem("§b증정용 순간이동의 눈", new ItemStack(Material.EYE_OF_ENDER), new String[]{"§7공간을 초월하는 눈."});
		
		ItemStorage.SPAWN_CHANGER = EfeUtils.item.createItem("§b스폰 체인저", new ItemStack(Material.CARPET, 1, (short) 14), 
				new String[]{"§7자신의 섬 스폰 좌표를", "§7설치한 곳으로 1회 바꿔주는 아이템."});
		ItemStorage.LUMINOUS_HOE = EfeUtils.item.createItem("§a빛나는 괭이", new ItemStack(Material.GOLD_HOE), 
				new String[]{"§7자신 섬의 흙을 갈 수 있는 괭이.", "§7간 흙에 씨앗을 심을 수 있으나", "§7작물을 수확을 할 수는 없다."});
		ItemStorage.HEALING_SPAWNER = EfeUtils.item.createItem("§b회복의 스포너", new ItemStack(Material.MOB_SPAWNER), 
				new String[]{"§7근처에 있으면 회복시켜주는", "§7마법의 스포너."});
		ItemStorage.DEFORMED_ANVIL = EfeUtils.item.createItem("§a장식용 모루", new ItemStack(Material.ANVIL), 
				new String[]{"§7사용이 불가능한 장식용 모루."});
		ItemStorage.DEFORMED_ENCHANTMENT_TABLE = EfeUtils.item.createItem("§a장식용 마법 부여대", new ItemStack(Material.ENCHANTMENT_TABLE), 
				new String[]{"§7사용이 불가능한 장식용 마법 부여대."});
		ItemStorage.DEFORMED_BEACON = EfeUtils.item.createItem("§a장식용 신호기", new ItemStack(Material.BEACON), 
				new String[]{"§7사용이 불가능한 장식용 신호기."});
		ItemStorage.DEFORMED_BREWING_STAND = EfeUtils.item.createItem("§a장식용 증류기", new ItemStack(Material.BREWING_STAND_ITEM), 
				new String[]{"§7사용이 불가능한 장식용 증류기."});
		
		ItemStorage.BIOME_CREEPER_PLAINS = EfeUtils.item.createItem("§b바이옴 크리퍼 <초원>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"§7자신 섬의 7x7 땅 기후를", "§7Plains로 바꿔주는 마법의 크리퍼."});
		ItemStorage.BIOME_CREEPER_ICE_PLAINS = EfeUtils.item.createItem("§b바이옴 크리퍼 <설원>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"§7자신 섬의 7x7 땅 기후를", "§7Ice Plains로 바꿔주는 마법의 크리퍼."});
		ItemStorage.BIOME_CREEPER_SWAMPLAND = EfeUtils.item.createItem("§b바이옴 크리퍼 <늪지>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"§7자신 섬의 7x7 땅 기후를", "§7Swampland로 바꿔주는 마법의 크리퍼."});
		ItemStorage.BIOME_CREEPER_JUNGLE = EfeUtils.item.createItem("§b바이옴 크리퍼 <정글>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"§7자신 섬의 7x7 땅 기후를", "§7Jungle로 바꿔주는 마법의 크리퍼."});
		ItemStorage.BIOME_CREEPER_DESERT = EfeUtils.item.createItem("§b바이옴 크리퍼 <사막>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"§7자신 섬의 7x7 땅 기후를", "§7Desert로 바꿔주는 마법의 크리퍼."});
		ItemStorage.BIOME_CREEPER_OCEAN = EfeUtils.item.createItem("§b바이옴 크리퍼 <바다>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"§7자신 섬의 7x7 땅 기후를", "§7Ocean으로 바꿔주는 마법의 크리퍼."});
		
		ItemStorage.RAINBOW_BOX_WOOL = EfeUtils.item.createItem("§a양모의 상자", new ItemStack(Material.CHEST), 
				new String[]{"§7무작위 색 양모 64개가", "§7들어있는 상자."});
		ItemStorage.RAINBOW_BOX_CLAY = EfeUtils.item.createItem("§a점토의 상자", new ItemStack(Material.CHEST), 
				new String[]{"§7무작위 색 점토 64개가", "§7들어있는 상자."});
		
		ItemStorage.SPAWN_EGG_VILLAGER = EfeUtils.item.createItem("§d주민 스폰알", new ItemStack(Material.MONSTER_EGG, 1, (short) 120), 
				new String[]{"§7자신 섬에 소환할 수 있는 주민.", "§7방문객의 공격을 입지 않으며", "§7사망시 스폰알이 복구된다.", "§7옷을 바꿀 수 있으며", "§7개인 상점을 등록할 수 있다."});
		
		ItemStorage.NAME_CHANGER = EfeUtils.item.createItem("§a이름표", new ItemStack(Material.NAME_TAG), 
				new String[]{"§7자신 섬의 동물 이름을", "§71회 변경할 수 있는 아이템. 동물에게", "§7우클릭해서 사용할 수 있다.", "§7흡수한 동물이 사망하면 복구된다."});
		ItemStorage.SP_RESET_SCROLL = EfeUtils.item.createItem("§dSP 초기화 스크롤", new ItemStack(Material.EMPTY_MAP), 
				new String[]{"§7스킬에 투자한 모든 SP를", "§71회 돌려받을 수 있는 스크롤.", "§7우클릭해서 사용할 수 있다."});
		ItemStorage.PICK_ME_UP_1 = EfeUtils.item.createDisplayItem("§a피로회복제", new ItemStack(Material.POTION, 1, (short) 8232), 
				new String[]{"피로를 50% 회복시켜주는", "놀라운 효과의 포션."});
		ItemStorage.PICK_ME_UP_2 = EfeUtils.item.createDisplayItem("§b뛰어난 피로회복제", new ItemStack(Material.POTION, 1, (short) 8232), 
				new String[]{"피로를 100% 회복시켜주는", "놀라운 효과의 포션."});
		ItemStorage.ANIMAL_MEDICINE = EfeUtils.item.createDisplayItem("§b동물 질병 치료제", new ItemStack(Material.POTION, 1, (short) 16389), 
				new String[]{"동물의 유전병을 치료하는 약.", "투척형으로, 다수의 동물을 치료할 수 있다."});
		
		ItemStorage.FURNACE_SPEED_1 = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7화로 속도가 §b3.3초§7 향상된다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_SPEED_1.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
		ItemStorage.FURNACE_SPEED_2 = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7화로 속도가 §b5초§7 향상된다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_SPEED_2.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
		ItemStorage.FURNACE_SPEED_3 = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7화로 속도가 §b3.3초§7 향상된다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_SPEED_3.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);
		ItemStorage.FURNACE_FUEL_1 = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7연료 효율이 §b20%§7 향상된다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_FUEL_1.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		ItemStorage.FURNACE_FUEL_2 = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7연료 효율이 §b40%§7 향상된다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_FUEL_2.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
		ItemStorage.FURNACE_FUEL_3 = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7연료 효율이 §b60%§7 향상된다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_FUEL_3.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
		ItemStorage.FURNACE_FORTUNE_1 = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7구운 아이템을 §b33%§7의 확률로", "§7추가적으로 더 얻는다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_FORTUNE_1.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
		ItemStorage.FURNACE_FORTUNE_2 = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7구운 아이템을 §b25%§7의 확률로", "§7추가적으로 더 얻는다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_FORTUNE_2.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
		ItemStorage.FURNACE_FORTUNE_3 = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7구운 아이템을 §b20%§7의 확률로", "§7추가적으로 더 얻는다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_FORTUNE_3.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
		ItemStorage.FURNACE_REMAIN = EfeUtils.item.createItem("§b마법의 화로", new ItemStack(Material.FURNACE), 
				new String[]{"§7마법이 부여된 화로.", "§7화로를 사용하지 않을 때", "§7연료 열기가 그대로 유지된다.", "§7단, 1%의 확률로 화로가 파괴된다."});
		ItemStorage.FURNACE_REMAIN.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
		
		ItemStorage.RANDOM_TITLE_BOOK = EfeUtils.item.createItem("§a무작위 조합 칭호", new ItemStack(Material.ENCHANTED_BOOK), 
				new String[]{"§7무작위 칭호가 적혀있는 마법서.", "§7\"(관형사) + (명사)\"의 형식을 지닌", "§7칭호를 1회 읽어낼 수 있다."});
		
		ItemStorage.BLOOD_SKIN_BOX = EfeUtils.item.createItem("§b블러드 스킨 상자", new ItemStack(Material.ENDER_CHEST), 
				new String[]{"§7무작위 블러드 스킨 1종이", "§7들어있는 잠겨진 상자.", "§7Polaris 지하 술집의 열쇠공에게", "§7부탁하면 50Ephe에 열 수 있다."});
		ItemStorage.BLOOD_SKIN_BOX_POPULAR = EfeUtils.item.createItem("§a보급형 블러드 스킨 상자", new ItemStack(Material.ENDER_CHEST), 
				new String[]{"§7무작위 블러드 스킨 1종이", "§7들어있는 잠겨진 상자.", "§7Polaris 지하 술집의 열쇠공에게", "§7부탁하면 50Ephe에 열 수 있다."});
		ItemStorage.BLOOD_SKIN_BOX_PREMIUM = EfeUtils.item.createItem("§d프리미엄 블러드 스킨 상자", new ItemStack(Material.ENDER_CHEST), 
				new String[]{"§7무작위 희귀 블러드 스킨 1종이", "§7들어있는 잠겨진 상자.", "§7Polaris 지하 술집의 열쇠공에게", "§7부탁하면 50Ephe에 열 수 있다."});
		ItemStorage.DECORATION_HEAD_BOX = EfeUtils.item.createItem("§a데코레이션 헤드 상자", new ItemStack(Material.CHEST), 
				new String[]{"§7무작위 데코레이션 헤드 1개가", "§7들어있는 잠겨진 상자.", "§7Polaris 지하 술집의 열쇠공에게", "§7부탁하면 100Ephe에 열 수 있다."});
		
		ItemStorage.VALUABLE_CARROT = EfeUtils.item.createItem("§a튼실한 당근", new ItemStack(Material.CARROT_ITEM), 
				new String[]{"§7품질이 좋은 당근.", "§7Polaris의 잡화상점에서 비싼 가격에", "§7판매할 수 있다."});
		ItemStorage.VALUABLE_POTATO = EfeUtils.item.createItem("§a튼실한 감자", new ItemStack(Material.POTATO_ITEM), 
				new String[]{"§7품질이 좋은 감자.", "§7Polaris의 잡화상점에서 비싼 가격에", "§7판매할 수 있다."});
		ItemStorage.VALUABLE_WHEAT = EfeUtils.item.createItem("§a튼실한 밀", new ItemStack(Material.WHEAT), 
				new String[]{"§7품질이 좋은 밀.", "§7Polaris의 잡화상점에서 비싼 가격에", "§7판매할 수 있다."});
		ItemStorage.NETHER_WART_SEEDS = EfeUtils.item.createItem("지옥 사마귀 씨앗", new ItemStack(Material.NETHER_BRICK_ITEM), 
				new String[]{});

		ItemStorage.EMELARD_BOX = EfeUtils.item.createItem("§a에페 상자", new ItemStack(Material.CHEST), 
				new String[]{"§715~30Ephe가 들어있는 상자.", "§7우클릭하여 개봉할 수 있다."});
		glow(ItemStorage.EMELARD_BOX);
	}
	
	private static void glow(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
		
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
	}
	
	public static ItemStack createSkinBook(String name) {
		Skin skin = bloodSkin.skins.get(name);
		String display = skin.getIcon().getItemMeta().getDisplayName();
		
		return EfeUtils.item.createItem("§c블러드 스킨 <"+display+"§c>", new ItemStack(Material.ENCHANTED_BOOK), 
				new String[]{"§7우클릭시 블러드 스킨을 획득합니다:", "§7- \""+(skin.isPremium() ? "<프리미엄> " : "")+display.substring(2)+"\""});
	}
	
	public static Skin getBloodSkin(ItemStack item) {
		String display = getName(item);
		
		if (!display.startsWith("§c블러드 스킨 "))
			return null;
		
		String name = display.substring(10, display.length() - 3);
		return skins.get(name);
	}
	
	public static String getName(ItemStack item) {
		return EfeUtils.item.getItemName(item);
	}
	
	public static int getItemAmount(Player player, String itemName) {
		int amount = 0;
		
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null || item.getType().equals(Material.AIR)) continue;
			if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) continue;
			if (!item.getItemMeta().getDisplayName().equals(itemName)) continue;
			
			amount += item.clone().getAmount();
		}
		
		return amount;
	}
	
	public static boolean hasItem(Player player, String itemName) {
		return getItemAmount(player, itemName) > 0;
	}
	
	public static void takeItem(Player player, String itemName, int amount) {
		ItemStack[] contents = player.getInventory().getContents().clone();
		
		for (int i = 0; i < contents.length; i ++) {
			ItemStack item = contents[i];
			
			if (item == null || item.getType().equals(Material.AIR)) continue;
			if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) continue;
			if (!item.getItemMeta().getDisplayName().equals(itemName)) continue;
			
			if (item.clone().getAmount() > amount) {
				item.setAmount(item.clone().getAmount() - amount);
				amount = 0;
			} else {
				amount -= item.clone().getAmount();
				item.setType(Material.AIR);
			}
			
			if (amount == 0) {
				break;
			}
		}
		
		player.getInventory().setContents(contents);
	}
	
	public static boolean isBiomeCreeper(ItemStack item) {
		return item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().startsWith("§b바이옴 크리퍼 ");
	}
	
	public static Biome getCreeperBiome(ItemStack item) {
		String name = item.getItemMeta().getDisplayName();
		String biome = name.substring(11, name.length() - 1);
		
		switch (biome) {
		case "초원":
			return Biome.PLAINS;
		case "설원":
			return Biome.ICE_PLAINS;
		case "늪지":
			return Biome.SWAMPLAND;
		case "정글":
			return Biome.JUNGLE;
		case "사막":
			return Biome.DESERT;
		case "바다":
			return Biome.OCEAN;
		}
		
		return Biome.PLAINS;
	}
	
	public static ItemStack getBiomeCreeper(Biome biome) {
		switch (biome) {
		case PLAINS:
			return ItemStorage.BIOME_CREEPER_PLAINS.clone();
		case ICE_PLAINS:
			return ItemStorage.BIOME_CREEPER_ICE_PLAINS.clone();
		case SWAMPLAND:
			return ItemStorage.BIOME_CREEPER_SWAMPLAND.clone();
		case JUNGLE:
			return ItemStorage.BIOME_CREEPER_JUNGLE.clone();
		case DESERT:
			return ItemStorage.BIOME_CREEPER_DESERT.clone();
		case OCEAN:
			return ItemStorage.BIOME_CREEPER_OCEAN.clone();
		default:
			return ItemStorage.BIOME_CREEPER_PLAINS.clone();
		}
	}
	
	public static ItemStack getFurnace(Enchantment ench, int level) {
		if (ench == Enchantment.DIG_SPEED) {
			switch (level) {
			case 1:
				return ItemStorage.FURNACE_SPEED_1;
			case 2:
				return ItemStorage.FURNACE_SPEED_2;
			case 3:
				return ItemStorage.FURNACE_SPEED_3;
			}
		} else if (ench == Enchantment.DURABILITY) {
			switch (level) {
			case 1:
				return ItemStorage.FURNACE_FUEL_1;
			case 2:
				return ItemStorage.FURNACE_FUEL_2;
			case 3:
				return ItemStorage.FURNACE_FUEL_3;
			}
		} else if (ench == Enchantment.LOOT_BONUS_BLOCKS) {
			switch (level) {
			case 1:
				return ItemStorage.FURNACE_FORTUNE_1;
			case 2:
				return ItemStorage.FURNACE_FORTUNE_2;
			case 3:
				return ItemStorage.FURNACE_FORTUNE_3;
			}
		} else if (ench == Enchantment.SILK_TOUCH) {
			return ItemStorage.FURNACE_REMAIN;
		}
		
		return null;
	}
}