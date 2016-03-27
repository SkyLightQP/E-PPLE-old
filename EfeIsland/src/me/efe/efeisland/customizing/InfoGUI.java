package me.efe.efeisland.customizing;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeisland.EfeFlag;
import me.efe.efeisland.EfeIsland;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class InfoGUI implements Listener {
	public EfeIsland plugin;
	public List<String> users = new ArrayList<String>();
	
	public InfoGUI(EfeIsland plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*4, "��3�ơ�r �� ���� ����");
		ProtectedRegion region = plugin.getIsleRegion(p);
		
		for (int i : new int[]{10, 11, 19, 28, 29, 30})
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), new String[]{}));
		for (int i : new int[]{15, 16, 25, 32, 33, 34})
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11), new String[]{}));
		for (int i : new int[]{3, 4, 5, 12, 14, 21, 22, 23})
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10), new String[]{}));
		
		inv.setItem(13, plugin.util.createDisplayItem("��b>> "+region.getFlag(EfeFlag.TITLE), new ItemStack(Material.NAME_TAG), 
				new String[]{
			"��aLv. "+toLevelString(region.getFlag(EfeFlag.LEVEL)), 
			"���� ����: �ݰ� "+getRadius(region.getFlag(EfeFlag.LEVEL))+"m ������ü", 
			"�����湮 ��: "+region.getFlag(EfeFlag.VISITERS).size(), 
			"�ִ� �湮 ��: "+region.getFlag(EfeFlag.MAX_VISIT), 
			"�̹��� ��õ ��: "+plugin.rankingGui.getRecommendation(p), 
			"", 
			"��9Ŭ������ �� �̸��� �����մϴ�."}));
		
		inv.setItem(20, plugin.util.createDisplayItem("��a�Ұ���", new ItemStack(Material.PAPER), 
				new String[]{region.getFlag(EfeFlag.DESCRIPTION), "", "��9Ŭ������ �����մϴ�."}));
		
		inv.setItem(24, plugin.util.createDisplayItem("��a����", new ItemStack(Material.SAPLING), new String[]{"�غ���"}));
		
		p.openInventory(inv);
		users.add(p.getName());
	}
	
	public String toLevelString(int lv) {
		switch (lv) {
		case 1:
			return "�� [����]";
		case 2:
			return "�� [��Ÿ]";
		case 3:
			return "�� [����]";
		case 4:
			return "�� [��Ÿ]";
		case 5:
			return "�� [���ް�]";
		}
		
		return null;
	}
	
	public int getRadius(int lv) {
		switch (lv) {
		case 1:
			return 30;
		case 2:
			return 45;
		case 3:
			return 60;
		case 4:
			return 75;
		case 5:
			return 100;
		}
		
		return -1;
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 36) return;
		
		final Player p = (Player) e.getWhoClicked();
		final ProtectedRegion region = plugin.getIsleRegion(p);
		
		AnvilGUI gui;
		
		switch (e.getRawSlot()) {
		case 13:
			p.closeInventory();
			
			p.sendMessage("��a�ơ�r �� �̸��� �ѱ� �ִ� 10��, ���� �ִ� 20���Դϴ�.");
			
			gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
						return;
					}
					
					if (event.getName().isEmpty()) {
						event.getPlayer().sendMessage("��c�ơ�r �� �̸��� �Էµ��� �ʾҽ��ϴ�!");
						return;
					}
					
					if (event.getName().getBytes().length > 20) {
						event.getPlayer().sendMessage("��c�ơ�r �� �̸��� �ʹ� ��ϴ�!");
						return;
					}
					
					region.setFlag(EfeFlag.TITLE, event.getName());
					
					event.getPlayer().sendMessage("��a�ơ�r �� �̸��� ����Ǿ����ϴ�.");
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem(region.getFlag(EfeFlag.TITLE), new ItemStack(Material.NAME_TAG), 
					new String[]{}));
			gui.open();
			
			break;
		case 20:
			p.closeInventory();
			
			p.sendMessage("��a�ơ�r �Ұ����� �ѱ� �ִ� 20��, ���� �ִ� 40���Դϴ�.");
			
			gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
						return;
					}
					
					if (event.getName().isEmpty()) {
						event.getPlayer().sendMessage("��c�ơ�r �Ұ����� �Էµ��� �ʾҽ��ϴ�!");
						return;
					}
					
					if (event.getName().getBytes().length > 40) {
						event.getPlayer().sendMessage("��c�ơ�r �Ұ����� �ʹ� ��ϴ�!");
						return;
					}
					
					region.setFlag(EfeFlag.DESCRIPTION, event.getName());
					
					event.getPlayer().sendMessage("��a�ơ�r �Ұ����� ����Ǿ����ϴ�.");
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem(region.getFlag(EfeFlag.DESCRIPTION), new ItemStack(Material.NAME_TAG), 
					new String[]{}));
			gui.open();
			
			break;
		case 24:
			p.sendMessage("��c�ơ�r ���� ������ �غ����� �ý����Դϴ�.");
			
			break;
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}