package me.efe.efeitems.listeners;

import java.util.HashMap;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeitems.EfeItems;
import me.efe.efeitems.ItemStorage;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VillagerGUI implements Listener {
	private static String buyShopName;
	private static String sellShopName;
	public EfeItems plugin;
	public HashMap<String, Villager> users = new HashMap<String, Villager>();
	public HashMap<Villager.Profession, DyeColor> profMap = new HashMap<Villager.Profession, DyeColor>();
	
	public VillagerGUI(EfeItems plugin) {
		this.plugin = plugin;
		
		profMap.put(Villager.Profession.BLACKSMITH, DyeColor.GRAY);
		profMap.put(Villager.Profession.BUTCHER, DyeColor.SILVER);
		profMap.put(Villager.Profession.FARMER, DyeColor.BROWN);
		profMap.put(Villager.Profession.LIBRARIAN, DyeColor.WHITE);
		profMap.put(Villager.Profession.PRIEST, DyeColor.PURPLE);
		
		buyShopName = plugin.animalListener.efeShops.buyShop.getItemMeta().getDisplayName();
		sellShopName = plugin.animalListener.efeShops.sellShop.getItemMeta().getDisplayName();
	}
	
	@SuppressWarnings("deprecation")
	public void openGUI(Player player, LivingEntity entity) {
		Inventory inv = plugin.getServer().createInventory(null, 9, "��6�ơ�r �ֹ�");
		String name = entity.getCustomName() != null ? "��b"+entity.getCustomName() : "��b�ֹ�";
		Villager villager = (Villager) entity;
		
		inv.setItem(0, EfeUtils.item.createDisplayItem("��a�� ����", new ItemStack(Material.WOOL, 1, (short) profMap.get(villager.getProfession()).getWoolData()), null));
		inv.setItem(4, EfeUtils.item.createDisplayItem(name, new ItemStack(Material.MONSTER_EGG, 1, (short) 120), null));
		
		if (plugin.animalListener.efeShops.privateShop.isMerchant(entity)) {
			inv.setItem(8, EfeUtils.item.createDisplayItem("��e���� ����", new ItemStack(Material.CHEST),
					new String[]{"����� ������ �����մϴ�."}));
		} else {
			inv.setItem(7, EfeUtils.item.createDisplayItem("��e���� ���� ���", new ItemStack(Material.CHEST),
					new String[]{"���� ���� �������� �Ҹ��Ͽ�", "�� �ֹο��� ���� ���� ������", "����մϴ�."}));
			inv.setItem(8, EfeUtils.item.createDisplayItem("��e�Ǹ� ���� ���", new ItemStack(Material.CHEST),
					new String[]{"���� ���� �������� �Ҹ��Ͽ�", "�� �ֹο��� ���� ���� ������", "����մϴ�."}));
		}
		
		player.openInventory(inv);
		users.put(player.getName(), villager);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (!users.containsKey(event.getWhoClicked().getName())) return;
		event.setCancelled(true);
		
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) || event.getRawSlot() >= 9) return;
		
		Player player = (Player) event.getWhoClicked();
		Villager villager = users.get(player.getName());
		
		if (event.getRawSlot() == 0) {
			villager.setProfession(getNextProfession(villager.getProfession()));
			
			event.getInventory()
			.setItem(0, EfeUtils.item.createDisplayItem("��a�� ����", new ItemStack(Material.WOOL, 1, (short) profMap.get(villager.getProfession()).getWoolData()), null));
			
		} else if (event.getRawSlot() == 7 && !plugin.animalListener.efeShops.privateShop.isMerchant(villager)) {
			if (!ItemStorage.hasItem(player, buyShopName)) {
				player.sendMessage("��c�ơ�r \""+buyShopName+"��r\" �������� �ʿ��մϴ�!");
				player.sendMessage("��c�ơ�r ���󸮽��� ��ȭ�������� �������ּ���.");
				return;
			}
			
			plugin.animalListener.efeShops.privateShop.openSignGUI(player, villager, true);
		} else if (event.getRawSlot() == 8) {
			if (!plugin.animalListener.efeShops.privateShop.isMerchant(villager)) {
				if (!ItemStorage.hasItem(player, sellShopName)) {
					player.sendMessage("��c�ơ�r \""+sellShopName+"��r\" �������� �ʿ��մϴ�!");
					player.sendMessage("��c�ơ�r ���󸮽��� ��ȭ�������� �������ּ���.");
					return;
				}
				
				plugin.animalListener.efeShops.privateShop.openSignGUI(player, villager, false);
			} else {
				plugin.animalListener.efeShops.privateShop.openMerchantGUI(player, villager);
			}
		}
	}
	
	public static void takeBuyShopItem(Player player) {
		ItemStorage.takeItem(player, buyShopName, 1);
	}
	
	public static void takeSellShopItem(Player player) {
		ItemStorage.takeItem(player, sellShopName, 1);
	}
	
	@SuppressWarnings("deprecation")
	private Villager.Profession getNextProfession(Villager.Profession prof) {
		int id = prof.getId() + 1;
		id = Villager.Profession.values().length <= id ? 0 : id;
		
		return Villager.Profession.getProfession(id);
	}

	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.containsKey(event.getPlayer().getName())) {
			users.remove(event.getPlayer().getName());
		}
	}
}