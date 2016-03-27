package me.efe.fishkg.listeners;

import java.util.ArrayList;
import java.util.List;

import me.efe.efecore.util.EfeUtils;
import me.efe.fishkg.Fishkg;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LureGUI implements Listener {
	public Fishkg plugin;
	public List<String> users = new ArrayList<String>();
	
	public LureGUI(Fishkg plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(p, 9, "��9�ơ�r �̳�");
		
//		inv.setItem(0, EfeUtils.item.createDisplayItem("��b��l"+plugin.getDescription().getFullName(), new ItemStack(Material.BOOK_AND_QUILL), 
//				new String[]{"Made by ��lEfe"}));
		
		inv.setItem(4, EfeUtils.item.createDisplayItem("��b�̳��� �� ����⸦ �������ּ���.", new ItemStack(Material.RAW_FISH), 
				new String[]{
			"��b�̳��� �޸� ����Ⱑ �� ���� �����ϴ�!", 
			"", 
			"��9[!] 1~6cm ����⸸ �����ϸ�, ��ȸ���Դϴ�."}));
		
		//inv.setItem(8, EfeUtils.item.createDisplayItem("��e��l������", new ItemStack(Material.WOOD_DOOR), new String[]{"Ŭ���� â�� �ݽ��ϴ�."}));
		
		p.openInventory(inv);
		users.add(p.getName());
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
		
		if (e.getRawSlot() != 4 && plugin.isFish(e.getCurrentItem())) {
			if (plugin.getLength(e.getCurrentItem()) > 6.0) {
				p.sendMessage(plugin.main+"��a�ơ�r 1~6cm�� ����⸸ �����մϴ�.");
				return;
			}
			
			p.getInventory().setItem(e.getSlot(), EfeUtils.item.getUsed(e.getCurrentItem(), p));
			plugin.setLure(p.getItemInHand());
			
			p.sendMessage(plugin.main+"��a�ơ�r ���˴뿡 �̳��� �޾ҽ��ϴ�.");
			
			p.closeInventory();
		} else if (e.getRawSlot() == 8) {
			p.closeInventory();
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}