package me.efe.efemobs.rudish;

import java.util.ArrayList;
import java.util.List;

import me.efe.efemobs.EfeMobs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiderGUI  implements Listener {
	public EfeMobs plugin;
	public List<String> users = new ArrayList<String>();
	
	public GuiderGUI(EfeMobs plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(null, 9*3, "��6�ơ�r ��� Ÿ�� ���̵�");
		
		inv.setItem(11, plugin.util.createDisplayItem("��b��n��� Ÿ���� ���� �� ȯ���մϴ�.", plugin.util.enchant(new ItemStack(Material.SMOOTH_BRICK), Enchantment.SILK_TOUCH, 100),
				new String[]{"��9�� 10������ ������r�Ǿ��ִ�", "�� ž���� �پ��� ���Ͱ� �����մϴ�.", "ó�� ���� �е��� ���� ��91����", "��9���� ���ɡ�r�մϴ�."}));
		inv.setItem(13, plugin.util.createDisplayItem("��e��n�ҿ��� ��ƺ�����!", plugin.util.enchant(new ItemStack(Material.SOUL_SAND), Enchantment.SILK_TOUCH, 100),
				new String[]{"�ڽ��� �ֻ������� ���͸� ����ϸ�", "�������� ��6�ҿ��� ȹ���r�մϴ�.", "�ҿ��� ��� ���� ���� ������ �� �ֽ��ϴ�.", "����� ��� �ҿ��� �����Ƿ�",
			"��6�ҿ� �����Կ� �ҿ��� ������r�صδ�", "���� �����ϴ�!"}));
		inv.setItem(15, plugin.util.createDisplayItem("��c��n�������� ����", plugin.util.enchant(new ItemStack(Material.EMPTY_MAP), Enchantment.SILK_TOUCH, 100),
				new String[]{"�ҿ��� ���� ��Ƶμ̴ٸ�", "ž �Ա��� ���ο��� ��4��ũ�ѡ�r�� �����Ͽ�", "�������� �����ϼ���!", "������ ����Ʈ���� ��4���� ���� �����r�˴ϴ�.",
			"1~5���� ��Ƽ�� ������ �� ������,", "�Ϸ� �ִ� 7ȸ ������ �� �ֽ��ϴ�.", "���� ���� ����Ǹ� �׾Ƴ��� �ҿ���", "��� �ʱ�ȭ�˴ϴ�."}));
		
		UserData data = new UserData(player);
		if (!data.hasPlayedBefore())
			data.setPlayedBefore();
		
		users.add(player.getName());
		player.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (users.contains(event.getWhoClicked().getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getName())) {
			users.remove(event.getPlayer().getName());
		}
	}
}