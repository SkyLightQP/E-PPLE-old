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
 * ��d: �ֻ��
 * ��b: ���
 * ��a: �߱�
 * ��r: �ϱ�
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
		
		
		ItemStorage.ANVIL_ABANDONED = EfeUtils.item.createItem("��a������ ���", new ItemStack(Material.ANVIL, 1, (short) 2), new String[]{"��7�������� �����ϴ� ���."});
		ItemStorage.ANVIL_OLD = EfeUtils.item.createItem("��b���� ���", new ItemStack(Material.ANVIL, 1, (short) 1), new String[]{"��7�������� �����ϴ� ���."});
		ItemStorage.ANVIL_ENCHANTED = EfeUtils.item.createItem("��d������ ���", new ItemStack(Material.ANVIL), new String[]{"��7�������� �����ϴ� ���."});
		
		ItemStorage.EYE_OF_TELEPORTATION = EfeUtils.item.createItem("��b�����̵��� ��", new ItemStack(Material.EYE_OF_ENDER), new String[]{"��7������ �ʿ��ϴ� ��."});
		ItemStorage.EYE_OF_TELEPORTATION_GIFT = EfeUtils.item.createItem("��b������ �����̵��� ��", new ItemStack(Material.EYE_OF_ENDER), new String[]{"��7������ �ʿ��ϴ� ��."});
		
		ItemStorage.SPAWN_CHANGER = EfeUtils.item.createItem("��b���� ü����", new ItemStack(Material.CARPET, 1, (short) 14), 
				new String[]{"��7�ڽ��� �� ���� ��ǥ��", "��7��ġ�� ������ 1ȸ �ٲ��ִ� ������."});
		ItemStorage.LUMINOUS_HOE = EfeUtils.item.createItem("��a������ ����", new ItemStack(Material.GOLD_HOE), 
				new String[]{"��7�ڽ� ���� ���� �� �� �ִ� ����.", "��7�� �뿡 ������ ���� �� ������", "��7�۹��� ��Ȯ�� �� ���� ����."});
		ItemStorage.HEALING_SPAWNER = EfeUtils.item.createItem("��bȸ���� ������", new ItemStack(Material.MOB_SPAWNER), 
				new String[]{"��7��ó�� ������ ȸ�������ִ�", "��7������ ������."});
		ItemStorage.DEFORMED_ANVIL = EfeUtils.item.createItem("��a��Ŀ� ���", new ItemStack(Material.ANVIL), 
				new String[]{"��7����� �Ұ����� ��Ŀ� ���."});
		ItemStorage.DEFORMED_ENCHANTMENT_TABLE = EfeUtils.item.createItem("��a��Ŀ� ���� �ο���", new ItemStack(Material.ENCHANTMENT_TABLE), 
				new String[]{"��7����� �Ұ����� ��Ŀ� ���� �ο���."});
		ItemStorage.DEFORMED_BEACON = EfeUtils.item.createItem("��a��Ŀ� ��ȣ��", new ItemStack(Material.BEACON), 
				new String[]{"��7����� �Ұ����� ��Ŀ� ��ȣ��."});
		ItemStorage.DEFORMED_BREWING_STAND = EfeUtils.item.createItem("��a��Ŀ� ������", new ItemStack(Material.BREWING_STAND_ITEM), 
				new String[]{"��7����� �Ұ����� ��Ŀ� ������."});
		
		ItemStorage.BIOME_CREEPER_PLAINS = EfeUtils.item.createItem("��b���̿� ũ���� <�ʿ�>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"��7�ڽ� ���� 7x7 �� ���ĸ�", "��7Plains�� �ٲ��ִ� ������ ũ����."});
		ItemStorage.BIOME_CREEPER_ICE_PLAINS = EfeUtils.item.createItem("��b���̿� ũ���� <����>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"��7�ڽ� ���� 7x7 �� ���ĸ�", "��7Ice Plains�� �ٲ��ִ� ������ ũ����."});
		ItemStorage.BIOME_CREEPER_SWAMPLAND = EfeUtils.item.createItem("��b���̿� ũ���� <����>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"��7�ڽ� ���� 7x7 �� ���ĸ�", "��7Swampland�� �ٲ��ִ� ������ ũ����."});
		ItemStorage.BIOME_CREEPER_JUNGLE = EfeUtils.item.createItem("��b���̿� ũ���� <����>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"��7�ڽ� ���� 7x7 �� ���ĸ�", "��7Jungle�� �ٲ��ִ� ������ ũ����."});
		ItemStorage.BIOME_CREEPER_DESERT = EfeUtils.item.createItem("��b���̿� ũ���� <�縷>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"��7�ڽ� ���� 7x7 �� ���ĸ�", "��7Desert�� �ٲ��ִ� ������ ũ����."});
		ItemStorage.BIOME_CREEPER_OCEAN = EfeUtils.item.createItem("��b���̿� ũ���� <�ٴ�>", new ItemStack(Material.MONSTER_EGG, 1, (short) 50), 
				new String[]{"��7�ڽ� ���� 7x7 �� ���ĸ�", "��7Ocean���� �ٲ��ִ� ������ ũ����."});
		
		ItemStorage.RAINBOW_BOX_WOOL = EfeUtils.item.createItem("��a����� ����", new ItemStack(Material.CHEST), 
				new String[]{"��7������ �� ��� 64����", "��7����ִ� ����."});
		ItemStorage.RAINBOW_BOX_CLAY = EfeUtils.item.createItem("��a������ ����", new ItemStack(Material.CHEST), 
				new String[]{"��7������ �� ���� 64����", "��7����ִ� ����."});
		
		ItemStorage.SPAWN_EGG_VILLAGER = EfeUtils.item.createItem("��d�ֹ� ������", new ItemStack(Material.MONSTER_EGG, 1, (short) 120), 
				new String[]{"��7�ڽ� ���� ��ȯ�� �� �ִ� �ֹ�.", "��7�湮���� ������ ���� ������", "��7����� �������� �����ȴ�.", "��7���� �ٲ� �� ������", "��7���� ������ ����� �� �ִ�."});
		
		ItemStorage.NAME_CHANGER = EfeUtils.item.createItem("��a�̸�ǥ", new ItemStack(Material.NAME_TAG), 
				new String[]{"��7�ڽ� ���� ���� �̸���", "��71ȸ ������ �� �ִ� ������. ��������", "��7��Ŭ���ؼ� ����� �� �ִ�.", "��7����� ������ ����ϸ� �����ȴ�."});
		ItemStorage.SP_RESET_SCROLL = EfeUtils.item.createItem("��dSP �ʱ�ȭ ��ũ��", new ItemStack(Material.EMPTY_MAP), 
				new String[]{"��7��ų�� ������ ��� SP��", "��71ȸ �������� �� �ִ� ��ũ��.", "��7��Ŭ���ؼ� ����� �� �ִ�."});
		ItemStorage.PICK_ME_UP_1 = EfeUtils.item.createDisplayItem("��a�Ƿ�ȸ����", new ItemStack(Material.POTION, 1, (short) 8232), 
				new String[]{"�Ƿθ� 50% ȸ�������ִ�", "���� ȿ���� ����."});
		ItemStorage.PICK_ME_UP_2 = EfeUtils.item.createDisplayItem("��b�پ �Ƿ�ȸ����", new ItemStack(Material.POTION, 1, (short) 8232), 
				new String[]{"�Ƿθ� 100% ȸ�������ִ�", "���� ȿ���� ����."});
		ItemStorage.ANIMAL_MEDICINE = EfeUtils.item.createDisplayItem("��b���� ���� ġ����", new ItemStack(Material.POTION, 1, (short) 16389), 
				new String[]{"������ �������� ġ���ϴ� ��.", "��ô������, �ټ��� ������ ġ���� �� �ִ�."});
		
		ItemStorage.FURNACE_SPEED_1 = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7ȭ�� �ӵ��� ��b3.3�ʡ�7 ���ȴ�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_SPEED_1.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
		ItemStorage.FURNACE_SPEED_2 = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7ȭ�� �ӵ��� ��b5�ʡ�7 ���ȴ�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_SPEED_2.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
		ItemStorage.FURNACE_SPEED_3 = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7ȭ�� �ӵ��� ��b3.3�ʡ�7 ���ȴ�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_SPEED_3.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);
		ItemStorage.FURNACE_FUEL_1 = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7���� ȿ���� ��b20%��7 ���ȴ�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_FUEL_1.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		ItemStorage.FURNACE_FUEL_2 = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7���� ȿ���� ��b40%��7 ���ȴ�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_FUEL_2.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
		ItemStorage.FURNACE_FUEL_3 = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7���� ȿ���� ��b60%��7 ���ȴ�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_FUEL_3.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
		ItemStorage.FURNACE_FORTUNE_1 = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7���� �������� ��b33%��7�� Ȯ����", "��7�߰������� �� ��´�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_FORTUNE_1.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
		ItemStorage.FURNACE_FORTUNE_2 = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7���� �������� ��b25%��7�� Ȯ����", "��7�߰������� �� ��´�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_FORTUNE_2.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
		ItemStorage.FURNACE_FORTUNE_3 = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7���� �������� ��b20%��7�� Ȯ����", "��7�߰������� �� ��´�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_FORTUNE_3.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
		ItemStorage.FURNACE_REMAIN = EfeUtils.item.createItem("��b������ ȭ��", new ItemStack(Material.FURNACE), 
				new String[]{"��7������ �ο��� ȭ��.", "��7ȭ�θ� ������� ���� ��", "��7���� ���Ⱑ �״�� �����ȴ�.", "��7��, 1%�� Ȯ���� ȭ�ΰ� �ı��ȴ�."});
		ItemStorage.FURNACE_REMAIN.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
		
		ItemStorage.RANDOM_TITLE_BOOK = EfeUtils.item.createItem("��a������ ���� Īȣ", new ItemStack(Material.ENCHANTED_BOOK), 
				new String[]{"��7������ Īȣ�� �����ִ� ������.", "��7\"(������) + (���)\"�� ������ ����", "��7Īȣ�� 1ȸ �о �� �ִ�."});
		
		ItemStorage.BLOOD_SKIN_BOX = EfeUtils.item.createItem("��b���� ��Ų ����", new ItemStack(Material.ENDER_CHEST), 
				new String[]{"��7������ ���� ��Ų 1����", "��7����ִ� ����� ����.", "��7Polaris ���� ������ ���������", "��7��Ź�ϸ� 50Ephe�� �� �� �ִ�."});
		ItemStorage.BLOOD_SKIN_BOX_POPULAR = EfeUtils.item.createItem("��a������ ���� ��Ų ����", new ItemStack(Material.ENDER_CHEST), 
				new String[]{"��7������ ���� ��Ų 1����", "��7����ִ� ����� ����.", "��7Polaris ���� ������ ���������", "��7��Ź�ϸ� 50Ephe�� �� �� �ִ�."});
		ItemStorage.BLOOD_SKIN_BOX_PREMIUM = EfeUtils.item.createItem("��d�����̾� ���� ��Ų ����", new ItemStack(Material.ENDER_CHEST), 
				new String[]{"��7������ ��� ���� ��Ų 1����", "��7����ִ� ����� ����.", "��7Polaris ���� ������ ���������", "��7��Ź�ϸ� 50Ephe�� �� �� �ִ�."});
		ItemStorage.DECORATION_HEAD_BOX = EfeUtils.item.createItem("��a���ڷ��̼� ��� ����", new ItemStack(Material.CHEST), 
				new String[]{"��7������ ���ڷ��̼� ��� 1����", "��7����ִ� ����� ����.", "��7Polaris ���� ������ ���������", "��7��Ź�ϸ� 100Ephe�� �� �� �ִ�."});
		
		ItemStorage.VALUABLE_CARROT = EfeUtils.item.createItem("��aư���� ���", new ItemStack(Material.CARROT_ITEM), 
				new String[]{"��7ǰ���� ���� ���.", "��7Polaris�� ��ȭ�������� ��� ���ݿ�", "��7�Ǹ��� �� �ִ�."});
		ItemStorage.VALUABLE_POTATO = EfeUtils.item.createItem("��aư���� ����", new ItemStack(Material.POTATO_ITEM), 
				new String[]{"��7ǰ���� ���� ����.", "��7Polaris�� ��ȭ�������� ��� ���ݿ�", "��7�Ǹ��� �� �ִ�."});
		ItemStorage.VALUABLE_WHEAT = EfeUtils.item.createItem("��aư���� ��", new ItemStack(Material.WHEAT), 
				new String[]{"��7ǰ���� ���� ��.", "��7Polaris�� ��ȭ�������� ��� ���ݿ�", "��7�Ǹ��� �� �ִ�."});
		ItemStorage.NETHER_WART_SEEDS = EfeUtils.item.createItem("���� �縶�� ����", new ItemStack(Material.NETHER_BRICK_ITEM), 
				new String[]{});

		ItemStorage.EMELARD_BOX = EfeUtils.item.createItem("��a���� ����", new ItemStack(Material.CHEST), 
				new String[]{"��715~30Ephe�� ����ִ� ����.", "��7��Ŭ���Ͽ� ������ �� �ִ�."});
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
		
		return EfeUtils.item.createItem("��c���� ��Ų <"+display+"��c>", new ItemStack(Material.ENCHANTED_BOOK), 
				new String[]{"��7��Ŭ���� ���� ��Ų�� ȹ���մϴ�:", "��7- \""+(skin.isPremium() ? "<�����̾�> " : "")+display.substring(2)+"\""});
	}
	
	public static Skin getBloodSkin(ItemStack item) {
		String display = getName(item);
		
		if (!display.startsWith("��c���� ��Ų "))
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
		return item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().startsWith("��b���̿� ũ���� ");
	}
	
	public static Biome getCreeperBiome(ItemStack item) {
		String name = item.getItemMeta().getDisplayName();
		String biome = name.substring(11, name.length() - 1);
		
		switch (biome) {
		case "�ʿ�":
			return Biome.PLAINS;
		case "����":
			return Biome.ICE_PLAINS;
		case "����":
			return Biome.SWAMPLAND;
		case "����":
			return Biome.JUNGLE;
		case "�縷":
			return Biome.DESERT;
		case "�ٴ�":
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