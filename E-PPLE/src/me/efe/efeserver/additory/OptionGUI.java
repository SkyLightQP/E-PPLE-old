package me.efe.efeserver.additory;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OptionGUI implements Listener {
	public EfeServer plugin;
	public List<String> users = new ArrayList<String>();
	
	public OptionGUI(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*5, "��6�ơ�r ����");
		
		refresh(inv, p);
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	public void refresh(Inventory inv, Player p) {
		PlayerData data = PlayerData.get(p);
		
		inv.clear();
		
		if (data.getOptionMenu()) {
			inv.setItem(10, plugin.util.createDisplayItem("��e�� �޴� ������", new ItemStack(Material.NETHER_STAR), 
					new String[]{"Ŭ���ϸ� �޴��� ���� ��������", "�ֹٿ��� �����մϴ�.", "", "��aTip)��2 \"/�޴�\" ��ɾ�ε�", "      ��2�޴��� �� �� �ֽ��ϴ�."}));
			inv.setItem(28, plugin.util.createDisplayItem("��aON", new ItemStack(Material.INK_SACK, 1, (short) 10), 
					new String[]{"", "��9Ŭ���ϸ� �������� �����մϴ�."}));
		} else {
			inv.setItem(10, plugin.util.createDisplayItem("��e�� �޴� ������", new ItemStack(Material.NETHER_STAR), 
					new String[]{"Ŭ���ϸ� �޴��� ���� ��������", "�ֹٿ� �߰��մϴ�.", "", "��aTip)��2 \"/�޴�\" ��ɾ�ε�", "      ��2�޴��� �� �� �ֽ��ϴ�."}));
			inv.setItem(28, plugin.util.createDisplayItem("��cOFF", new ItemStack(Material.INK_SACK, 1, (short) 8), 
					new String[]{"", "��9Ŭ���ϸ� �������� �߰��մϴ�."}));
		}
		
		if (data.getOptionBoat()) {
			inv.setItem(12, plugin.util.createDisplayItem("��b��Ʈ ������", new ItemStack(Material.BOAT), 
					new String[]{"Ŭ���ϸ� ���ظ� �����ϴ� ��������", "�ֹٿ��� �����մϴ�.", "", "��aTip)��2 �����ϸ� ���ذ� �Ұ����մϴ�."}));
			inv.setItem(30, plugin.util.createDisplayItem("��aON", new ItemStack(Material.INK_SACK, 1, (short) 10), 
					new String[]{"", "��9Ŭ���ϸ� �������� �����մϴ�."}));
		} else {
			inv.setItem(12, plugin.util.createDisplayItem("��b��Ʈ ������", new ItemStack(Material.BOAT), 
					new String[]{"Ŭ���ϸ� ���ظ� �����ϴ� ��������", "�ֹٿ� �߰��մϴ�.", "", "��aTip)��2 �����ϸ� ���ذ� �Ұ����մϴ�."}));
			inv.setItem(30, plugin.util.createDisplayItem("��cOFF", new ItemStack(Material.INK_SACK, 1, (short) 8), 
					new String[]{"", "��9Ŭ���ϸ� �������� �߰��մϴ�."}));
		}
		
		if (data.getOptionChat()) {
			inv.setItem(14, plugin.util.createDisplayItem("��a���󸮽�ƽ ä��", new ItemStack(Material.REDSTONE_COMPARATOR), 
					new String[]{"Ŭ���ϸ� 300 �� ���� ä�� �޼�����", "���޹޽��ϴ�."}));
			inv.setItem(32, plugin.util.createDisplayItem("��aON", new ItemStack(Material.INK_SACK, 1, (short) 10), 
					new String[]{"", "��9Ŭ���ϸ� ä���� �޽��ϴ�."}));
		} else {
			inv.setItem(14, plugin.util.createDisplayItem("��a���󸮽�ƽ ä��", new ItemStack(Material.REDSTONE_COMPARATOR), 
					new String[]{"Ŭ���ϸ� 300 �� ���� ä�� �޼�����", "���޹��� �ʽ��ϴ�."}));
			inv.setItem(32, plugin.util.createDisplayItem("��cOFF", new ItemStack(Material.INK_SACK, 1, (short) 8), 
					new String[]{"", "��9Ŭ���ϸ� ���޹��� �ʽ��ϴ�."}));
		}
		
		if (data.getOptionWhisper()) {
			inv.setItem(16, plugin.util.createDisplayItem("��c�ӼӸ�", new ItemStack(Material.BOOK_AND_QUILL), 
					new String[]{"Ŭ���ϸ� �ӼӸ��� �����մϴ�.", "�ڽŵ� �ӼӸ��� �� �� �����ϴ�."}));
			inv.setItem(34, plugin.util.createDisplayItem("��aON", new ItemStack(Material.INK_SACK, 1, (short) 10), 
					new String[]{"", "��9Ŭ���ϸ� �ӼӸ��� �����մϴ�."}));
		} else {
			inv.setItem(16, plugin.util.createDisplayItem("��c�ӼӸ�", new ItemStack(Material.BOOK_AND_QUILL), 
					new String[]{"Ŭ���ϸ� �ӼӸ��� ����մϴ�."}));
			inv.setItem(34, plugin.util.createDisplayItem("��cOFF", new ItemStack(Material.INK_SACK, 1, (short) 8), 
					new String[]{"", "��9Ŭ���ϸ� �ӼӸ��� ����մϴ�."}));
		}
		
		inv.setItem(36, plugin.util.createDisplayItem("��9ä��â û��", new ItemStack(Material.WATER_BUCKET), new String[]{"Ȥ�� F3 + D"}));
		inv.setItem(43, plugin.util.createDisplayItem("��c�ݱ�", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		inv.setItem(44, plugin.util.createDisplayItem("��e���� �޴�", new ItemStack(Material.NETHER_STAR), new String[]{}));
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 9*5) return;
		
		Player p = (Player) e.getWhoClicked();
		PlayerData data = PlayerData.get(p);
		
		if (e.getRawSlot() == 10 || e.getRawSlot() == 28) {
			data.setOptionMenu(!data.getOptionMenu());
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			
			if (!updateHotBar(p)) {
				data.setOptionMenu(!data.getOptionMenu());
				
				p.sendMessage("��c�ơ�r �ֹ� 9��° ������ ����ּ���!");
			}
			
			refresh(e.getInventory(), p);
		}
		
		if (e.getRawSlot() == 12 || e.getRawSlot() == 30) {
			data.setOptionBoat(!data.getOptionBoat());
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			
			if (!updateHotBar(p)) {
				data.setOptionBoat(!data.getOptionBoat());
				
				if (data.getOptionMenu())
					p.sendMessage("��c�ơ�r �ֹ� 8��° ������ ����ּ���!");
				else
					p.sendMessage("��c�ơ�r �ֹ� 9��° ������ ����ּ���!");
			}
			
			refresh(e.getInventory(), p);
		}
		
		if (e.getRawSlot() == 14 || e.getRawSlot() == 32) {
			data.setOptionChat(!data.getOptionChat());
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			
			refresh(e.getInventory(), p);
		}
		
		if (e.getRawSlot() == 16 || e.getRawSlot() == 34) {
			data.setOptionWhisper(!data.getOptionWhisper());
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			
			refresh(e.getInventory(), p);
		}
		
		if (e.getRawSlot() == 36) {
			for (int i = 0; i < 30; i ++) {
				p.sendMessage("");
			}
			
			p.playSound(p.getLocation(), Sound.WATER, 1.0F, 1.0F);
		}
		
		if (e.getRawSlot() == 43) {
			p.closeInventory();
		}
		
		if (e.getRawSlot() == 44) {
			p.closeInventory();
			plugin.mainGui.openGUI(p);
		}
	}
	
	public boolean updateHotBar(Player p) {
		PlayerData data = PlayerData.get(p);
		
		p.getInventory().remove(Material.BOAT);
		p.getInventory().remove(Material.COMPASS);
		p.getInventory().remove(Material.NETHER_STAR);
		
		if (data.getOptionBoat()) {
			
			if (data.getOptionMenu()) {
				if (p.getInventory().getItem(7) != null && !p.getInventory().getItem(7).getType().equals(Material.AIR)) return false;
				
				p.getInventory().setItem(7, plugin.myboat.getBoatItem(p));
			} else {
				if (p.getInventory().getItem(8) != null && !p.getInventory().getItem(8).getType().equals(Material.AIR)) return false;
				
				p.getInventory().setItem(8, plugin.myboat.getBoatItem(p));
			}
		}
		
		if (data.getOptionMenu()) {
			if (p.getInventory().getItem(8) != null && !p.getInventory().getItem(8).getType().equals(Material.AIR)) return false;
			
			p.getInventory().setItem(8, plugin.util.createDisplayItem("��e/�޴�", new ItemStack(Material.NETHER_STAR), new String[]{"Ŭ���ϸ� ���� �޴��� ���ϴ�."}));
		}
		
		return true;
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}