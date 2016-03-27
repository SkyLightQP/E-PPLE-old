package me.efe.efeisland.customizing;

import java.util.HashMap;

import me.efe.efecommunity.UserData;
import me.efe.efeisland.EfeFlag;
import me.efe.efeisland.EfeIsland;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class CustomGUI implements Listener {
	public EfeIsland plugin;
	public HashMap<String, GUIType> users = new HashMap<String, GUIType>();
	
	public CustomGUI(EfeIsland plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*6, "��3�ơ�r �� Ŀ���͸���¡");
		ProtectedRegion region = plugin.getIsleRegion(p);
		
		updateInv(inv, region, GUIType.MAIN, p);
		
		p.openInventory(inv);
		users.put(p.getName(), GUIType.MAIN);
	}
	
	public void updateInv(Inventory inv, ProtectedRegion region, GUIType type, Player p) {
		inv.clear();
		
		if (type == GUIType.MAIN) {
			for (int i : new int[]{2, 11, 20, 6, 15, 24}) 
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.LADDER), new String[]{}));
			
			for (int i : new int[]{28, 29, 30, 37, 39, 46, 47, 48})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			for (int i : new int[]{30, 31, 32, 39, 41, 48, 49, 50})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			for (int i : new int[]{32, 33, 34, 41, 43, 50, 51, 52})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			
			inv.setItem(7, plugin.util.createDisplayItem("��e�߹�", new ItemStack(Material.MINECART), 
					new String[]{"������ �Ͻ�������", "�� ������ �������ϴ�."}));
			inv.setItem(8, plugin.util.createDisplayItem("��c������Ʈ", new ItemStack(Material.BARRIER), 
					new String[]{"������Ʈ�� �����մϴ�."}));
			
			inv.setItem(38, plugin.util.createDisplayItem("��a��nBUILD", new ItemStack(Material.IRON_PICKAXE), 
					new String[]{"���� ����, �� ���� �", "���Ͽ� �����մϴ�."}));
			inv.setItem(40, plugin.util.createDisplayItem("��a��nFEATURE", new ItemStack(Material.COOKIE), 
					new String[]{"�ö���, ��ü ���� �", "���Ͽ� �����մϴ�."}));
			inv.setItem(42, plugin.util.createDisplayItem("��a��nCOMBAT", new ItemStack(Material.IRON_SWORD), 
					new String[]{"PvP ����, ��ų ���� �", "���Ͽ� �����մϴ�."}));
			
			//Entrance
			if (region.getFlag(EfeFlag.ENTRANCE) == State.ALLOW) {
				String[] lore = {
						"���� ���� ���ο� ���� �����մϴ�.", 
						"OFF�� ģ���� ������ ��� �������", 
						"�� ���� ������ �� �����ϴ�.", 
						"", 
						"��a��lON:��2 ��� ���� ����", 
						"��cOFF:��4 ģ���� ���� ����"
				};
				
				inv.setItem(13, plugin.util.createDisplayItem("��a��n���� ��", new ItemStack(Material.WOOD_DOOR), lore));
			} else {
				String[] lore = {
						"���� ���� ���ο� ���� �����մϴ�.", 
						"OFF�� ģ���� ������ ��� �������", 
						"�� ���� ������ �� �����ϴ�.", 
						"", 
						"��aON:��2 ��� ���� ����", 
						"��c��lOFF:��4 ģ���� ���� ����"
				};
				
				inv.setItem(13, plugin.util.createDisplayItem("��c��n���� ��", new ItemStack(Material.IRON_DOOR), lore));
			}
		} else if (type == GUIType.BLOCKS) {
			for (int i : new int[]{28, 29, 30, 37, 39, 46, 47, 48})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			
			inv.setItem(38, plugin.util.createDisplayItem("��cBack", new ItemStack(Material.IRON_PICKAXE), new String[]{"�������� ���ư��ϴ�."}));
			
			//Build
			if (region.getFlag(DefaultFlag.BUILD) == State.ALLOW) {
				String[] lore = {
						"���� ���ο� ���� �����մϴ�.", 
						"OFF�� �ڽ��� ������ ��� �������", 
						"�� �ױ�/�μ���� ����,", 
						"���� ����, īƮ ��ġ � �Ұ����մϴ�.", 
						"", 
						"��a��lON:��2 �� ģ���� ���� ����", 
						"��cOFF:��4 �� �ڽŸ� ���� ����"
				};
				
				inv.setItem(14, plugin.util.createDisplayItem("��a��n����", new ItemStack(Material.COBBLESTONE), lore));
				inv.setItem(13, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"���� ���ο� ���� �����մϴ�.", 
						"OFF�� �ڽ��� ������ ��� �������", 
						"�� �ױ�/�μ���� ����,", 
						"���� ����, īƮ ��ġ � �Ұ����մϴ�.", 
						"", 
						"��aON:��2 �� ģ���� ���� ����", 
						"��c��lOFF:��4 �� �ڽŸ� ���� ����"
				};
				
				inv.setItem(14, plugin.util.createDisplayItem("��c��n����", new ItemStack(Material.COBBLESTONE), lore));
				inv.setItem(13, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
			
			//Use
			if (region.getFlag(DefaultFlag.USE) == State.ALLOW) {
				String[] lore = {
						"�� ���� ��� ���ο� ���� �����մϴ�.", 
						"OFF�� ģ���� ������ ��� �������", 
						"��, ��ư, ���� ���� ���� ����� �� �����ϴ�.", 
						"", 
						"��a��lON:��2 ��� ���� ����", 
						"��cOFF:��4 �� ģ���� ���� ����"
				};
				
				inv.setItem(16, plugin.util.createDisplayItem("��a��n�� ����", new ItemStack(Material.TRAP_DOOR), lore));
				inv.setItem(17, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"�� ���� ��� ���ο� ���� �����մϴ�.", 
						"OFF�� ģ���� ������ ��� �������", 
						"��, ��ư, ���� ���� ���� ����� �� �����ϴ�.", 
						"", 
						"��aON:��2 ��� ���� ����", 
						"��c��lOFF:��4 �� ģ���� ���� ����"
				};
				
				inv.setItem(16, plugin.util.createDisplayItem("��c��n�� ����", new ItemStack(Material.TRAP_DOOR), lore));
				inv.setItem(17, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
			
			//Container
			if (region.getFlag(DefaultFlag.CHEST_ACCESS) == State.ALLOW) {
				String[] lore = {
						"�����̳� ��� ���ο� ���� �����մϴ�.", 
						"OFF�� �ڽ��� ������ ��� �������", 
						"����, ȭ��, ���ޱ�, ���� ��", 
						"�������� �����ϴ� ���� �� �� �����ϴ�.", 
						"", 
						"��a��lON:��2 �� ģ���� �̿� ����", 
						"��cOFF:��4 �� �ڽŸ� �̿� ����"
				};
				
				inv.setItem(41, plugin.util.createDisplayItem("��a��n�����̳�", new ItemStack(Material.CHEST), lore));
				inv.setItem(40, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"�����̳� ��� ���ο� ���� �����մϴ�.", 
						"OFF�� �ڽ��� ������ ��� �������", 
						"����, ȭ��, ���ޱ�, ���� ��", 
						"�������� �����ϴ� ���� �� �� �����ϴ�.", 
						"", 
						"��aON:��2 �� ģ���� �̿� ����", 
						"��c��lOFF:��4 �� �ڽŸ� �̿� ����"
				};
				
				inv.setItem(41, plugin.util.createDisplayItem("��c��n�����̳�", new ItemStack(Material.CHEST), lore));
				inv.setItem(40, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
			
			//Melt
			if (region.getFlag(DefaultFlag.SNOW_MELT) == State.ALLOW) {
				String[] lore = {
						"���� ���ο� ���� �����մϴ�.", 
						"OFF�� ���� ���� �ʰ�, ", 
						"������ ����ų� ���� �ʽ��ϴ�.", 
						"", 
						"��a��lON:��2 ���� ���", 
						"��cOFF:��4 ���� �����"
				};
				
				inv.setItem(43, plugin.util.createDisplayItem("��a��n����", new ItemStack(Material.PACKED_ICE), lore));
				inv.setItem(44, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"���� ���ο� ���� �����մϴ�.", 
						"OFF�� ���� ���� �ʰ�, ", 
						"������ ����ų� ���� �ʽ��ϴ�.", 
						"", 
						"��aON:��2 ���� ���", 
						"��c��lOFF:��4 ���� �����"
				};
				
				inv.setItem(43, plugin.util.createDisplayItem("��c��n����", new ItemStack(Material.PACKED_ICE), lore));
				inv.setItem(44, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
		} else if (type == GUIType.FUN) {
			for (int i : new int[]{30, 31, 32, 39, 41, 48, 49, 50})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			
			inv.setItem(40, plugin.util.createDisplayItem("��cBack", new ItemStack(Material.COOKIE), new String[]{"�������� ���ư��ϴ�."}));
			
			//Fly
			if (region.getFlag(EfeFlag.FLY) == State.ALLOW) {
				String[] lore = {
						"�ö��� ���� ���ο� ���� �����մϴ�.", 
						"ON�� ��ΰ� �� ������", 
						"���ƴٴ� �� �ֽ��ϴ�.", 
						"", 
						"��a��lON:��2 �� �� ����", 
						"��cOFF:��4 �� �� ����"
				};
				
				inv.setItem(10, plugin.util.createDisplayItem("��a��n�ö���", new ItemStack(Material.FEATHER), lore));
				inv.setItem(9, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"�ö��� ���� ���ο� ���� �����մϴ�.", 
						"ON�� ��ΰ� �� ������", 
						"���ƴٴ� �� �ֽ��ϴ�.", 
						"", 
						"��aON:��2 �� �� ����", 
						"��c��lOFF:��4 �� �� ����"
				};
				
				inv.setItem(10, plugin.util.createDisplayItem("��c��n�ö���", new ItemStack(Material.FEATHER), lore));
				inv.setItem(9, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
			
			//Fire
			if (region.getFlag(DefaultFlag.FIRE_SPREAD) == State.ALLOW) {
				String[] lore = {
						"�� ���� ���ο� ���� �����մϴ�.", 
						"ON�� ���� ���� �� �ְ� �˴ϴ�.", 
						"", 
						"��a��lON:��2 ���� �� ����", 
						"��cOFF:��4 ���� �� ����"
				};
				
				inv.setItem(37, plugin.util.createDisplayItem("��a��n�� ����", new ItemStack(Material.FIREBALL), lore));
				inv.setItem(36, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"�� ���� ���ο� ���� �����մϴ�.", 
						"ON�� ���� ���� �� �ְ� �˴ϴ�.", 
						"", 
						"��aON:��2 ���� �� ����", 
						"��c��lOFF:��4 ���� �� ����"
				};
				
				inv.setItem(37, plugin.util.createDisplayItem("��c��n�� ����", new ItemStack(Material.FIREBALL), lore));
				inv.setItem(36, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
			
			//Grass
			if (region.getFlag(DefaultFlag.GRASS_SPREAD) == State.ALLOW) {
				String[] lore = {
						"�ڿ�ȭ ���ο� ���� �����մϴ�.", 
						"OFF�� �ܵ�, ����, ���� ����", 
						"������� �ڶ��� �ʽ��ϴ�.", 
						"", 
						"��a��lON:��2 �ڿ�ȭ Ȱ��ȭ", 
						"��cOFF:��4 �ڿ�ȭ ��Ȱ��ȭ"
				};
				
				inv.setItem(16, plugin.util.createDisplayItem("��a��n�ڿ�ȭ", new ItemStack(Material.VINE), lore));
				inv.setItem(17, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"�ڿ�ȭ ���ο� ���� �����մϴ�.", 
						"OFF�� �ܵ�, ����, ���� ����", 
						"������� �ڶ��� �ʽ��ϴ�.", 
						"", 
						"��aON:��2 �ڿ�ȭ Ȱ��ȭ", 
						"��c��lOFF:��4 �ڿ�ȭ ��Ȱ��ȭ"
				};
				
				inv.setItem(16, plugin.util.createDisplayItem("��c��n�ڿ�ȭ", new ItemStack(Material.VINE), lore));
				inv.setItem(17, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
			
			//Liquid
			if (region.getFlag(DefaultFlag.WATER_FLOW) == State.ALLOW) {
				String[] lore = {
						"��ü ���� ���ο� ���� �����մϴ�.", 
						"OFF�� ��, ��� ����", 
						"�帣�� �ʰ� �˴ϴ�.", 
						"", 
						"��a��lON:��2 ��ü ���� ��Ȱ��ȭ", 
						"��cOFF:��4 ��ü ���� Ȱ��ȭ"
				};
				
				inv.setItem(43, plugin.util.createDisplayItem("��a��n��ü ����", new ItemStack(Material.WATER_BUCKET), lore));
				inv.setItem(44, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"��ü ���� ���ο� ���� �����մϴ�.", 
						"OFF�� ��, ��� ����", 
						"�帣�� �ʰ� �˴ϴ�.", 
						"", 
						"��aON:��2 ��ü ���� ��Ȱ��ȭ", 
						"��c��lOFF:��4 ��ü ���� Ȱ��ȭ"
				};
				
				inv.setItem(43, plugin.util.createDisplayItem("��c��n��ü ����", new ItemStack(Material.WATER_BUCKET), lore));
				inv.setItem(44, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
		} else if (type == GUIType.PVP) {
			for (int i : new int[]{32, 33, 34, 41, 43, 50, 51, 52})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			
			inv.setItem(42, plugin.util.createDisplayItem("��cBack", new ItemStack(Material.IRON_SWORD), new String[]{"�������� ���ư��ϴ�."}));
			
			//PvP
			if (region.getFlag(DefaultFlag.PVP) == State.ALLOW) {
				String[] lore = {
						"PvP ���� ���ο� ���� �����մϴ�.", 
						"ON�� ���濡�� ��������", 
						"���� �� �ְ� �˴ϴ�.", 
						"���� ��� �г�Ƽ�� �������� �ʽ��ϴ�.", 
						"", 
						"��a��lON:��2 PvP ����", 
						"��cOFF:��4 PvP �Ұ���"
				};
				
				inv.setItem(10, plugin.util.createDisplayItem("��a��nPvP", new ItemStack(Material.REDSTONE), lore));
				inv.setItem(9, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"PvP ���� ���ο� ���� �����մϴ�.", 
						"ON�� ���濡�� ��������", 
						"���� �� �ְ� �˴ϴ�.", 
						"���� ��� �г�Ƽ�� �������� �ʽ��ϴ�.", 
						"", 
						"��aON:��2 PvP ����", 
						"��c��lOFF:��4 PvP �Ұ���"
				};
				
				inv.setItem(10, plugin.util.createDisplayItem("��c��nPvP", new ItemStack(Material.REDSTONE), lore));
				inv.setItem(9, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
			
			//Instant Kill
			if (region.getFlag(EfeFlag.INSTANT_KILL) == State.ALLOW) {
				String[] lore = {
						"�ν���Ʈ ų ���ο� ���� �����մϴ�.", 
						"ON�� ��� �÷��̾ �� �濡", 
						"�װ� �˴ϴ�.", 
						"", 
						"��a��lON:��2 �ν���Ʈ ų Ȱ��ȭ", 
						"��cOFF:��4 �ν���Ʈ ų ��Ȱ��ȭ"
				};
				
				inv.setItem(12, plugin.util.createDisplayItem("��a��n�ν���Ʈ ų", new ItemStack(Material.DIAMOND_SWORD), lore));
				inv.setItem(13, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"�ν���Ʈ ų ���ο� ���� �����մϴ�.", 
						"ON�� ��� �÷��̾ �� �濡", 
						"�װ� �˴ϴ�.", 
						"", 
						"��aON:��2 �ν���Ʈ ų Ȱ��ȭ", 
						"��c��lOFF:��4 �ν���Ʈ ų ��Ȱ��ȭ"
				};
				
				inv.setItem(12, plugin.util.createDisplayItem("��c��n�ν���Ʈ ų", new ItemStack(Material.DIAMOND_SWORD), lore));
				inv.setItem(13, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
			
			//Skill
			if (region.getFlag(EfeFlag.SKILL) == State.ALLOW) {
				String[] lore = {
						"��ų ���� ���ο� ���� �����մϴ�.", 
						"ON�� ������ �÷��̾", 
						"��ų�� ����� �� �ְ� �˴ϴ�.", 
						"����, ��� �нú� ��ų�� ���õ˴ϴ�.", 
						"", 
						"��a��lON:��2 ��ų ��� ����", 
						"��cOFF:��4 ��ų ��� �Ұ���"
				};
				
				inv.setItem(37, plugin.util.createDisplayItem("��a��n��ų ����", new ItemStack(Material.BLAZE_POWDER), lore));
				inv.setItem(36, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"��ų ���� ���ο� ���� �����մϴ�.", 
						"ON�� ������ �÷��̾", 
						"��ų�� ����� �� �ְ� �˴ϴ�.", 
						"����, ��� �нú� ��ų�� ���õ˴ϴ�.", 
						"", 
						"��aON:��2 ��ų ��� ����", 
						"��c��lOFF:��4 ��ų ��� �Ұ���"
				};
				
				inv.setItem(37, plugin.util.createDisplayItem("��c��n��ų ����", new ItemStack(Material.BLAZE_POWDER), lore));
				inv.setItem(36, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
			
			//Potion
			if (region.getFlag(EfeFlag.NO_POTION) == State.ALLOW) {
				String[] lore = {
						"���� ���� ���ο� ���� �����մϴ�.", 
						"ON�� ������ �÷��̾", 
						"���� ȿ���� ���� �� ���� �մϴ�.", 
						"�湮�ڴ� ������ ���� ȿ���� �ҰԵ˴ϴ�.", 
						"", 
						"��a��lON:��2 ���� ȿ�� �����", 
						"��cOFF:��4 ���� ȿ�� ���"
				};
				
				inv.setItem(39, plugin.util.createDisplayItem("��a��n���� ����", new ItemStack(Material.MILK_BUCKET), lore));
				inv.setItem(40, plugin.util.createDisplayItem("��aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"��9Ŭ������ OFF�ϼ���."}));
			} else {
				String[] lore = {
						"���� ���� ���ο� ���� �����մϴ�.", 
						"ON�� ������ �÷��̾", 
						"���� ȿ���� ���� �� ���� �մϴ�.", 
						"�湮�ڴ� ������ ���� ȿ���� �ҰԵ˴ϴ�.", 
						"", 
						"��aON:��2 ���� ȿ�� �����", 
						"��c��lOFF:��4 ���� ȿ�� ���"
				};
				
				inv.setItem(39, plugin.util.createDisplayItem("��c��n���� ����", new ItemStack(Material.MILK_BUCKET), lore));
				inv.setItem(40, plugin.util.createDisplayItem("��cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"��9Ŭ������ ON�ϼ���."}));
			}
		}
		
		users.put(p.getName(), type);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 54) return;
		
		Player p = (Player) e.getWhoClicked();
		GUIType gui = users.get(p.getName());
		final ProtectedRegion region = plugin.getIsleRegion(p);
		int slot = e.getRawSlot();
		
		if (gui == GUIType.MAIN) {
			
			if (slot == 7) {
				AnvilGUI anvilGui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
					
					@Override
					public void onAnvilClick(AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
							event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
							return;
						}
						
						if (event.getName().equals("�г���") || event.getName().isEmpty()) {
							event.getPlayer().sendMessage("��c�ơ�r �÷��̾� �г����� �Էµ��� �ʾҽ��ϴ�!");
							return;
						}
						
						Player target = plugin.util.getOnlinePlayer(event.getName());
						
						if (target == null) {
							event.getPlayer().sendMessage("��c�ơ�r <��a"+event.getName()+"��r> �÷��̾�� �������� �ʽ��ϴ�!");
							return;
						}
						
						if (event.getPlayer().equals(target)) {
							event.getPlayer().sendMessage("��c�ơ�r �ڽ��� ������ ���� �����ϴ�!");
							return;
						}
						
						if (!plugin.getVisiters(region).contains(target)) {
							event.getPlayer().sendMessage("��c�ơ�r "+target.getName()+"���� ����� ���� �湮������ �ʽ��ϴ�.");
							return;
						}
						
						target.sendMessage("��a�ơ�r �湮���� ���� ������ ����� �߹��߽��ϴ�.");
						target.teleport(plugin.getIsleSpawnLoc(target));
						
						event.getPlayer().sendMessage("��a�ơ�r "+target.getName()+"���� ������ �߹�Ǿ����ϴ�.");
						
						event.setWillClose(true);
						event.setWillDestroy(true);
					}
				});
				
				anvilGui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("�г���", new ItemStack(Material.NAME_TAG), 
						new String[]{}));
				anvilGui.open();
				return;
			}
			
			if (slot == 8) {
				p.closeInventory();
				plugin.blacklistGui.openGUI(p);
				
				return;
			}
			
			if (slot == 13) {
				if (region.getFlag(EfeFlag.ENTRANCE) == State.ALLOW) {
					region.setFlag(EfeFlag.ENTRANCE, State.DENY);
					
					UserData data = new UserData(p);
					
					for (Player visiter : plugin.getVisiters(region)) {
						if (visiter.equals(plugin.getIsleOwner(region)))
							continue;
						
						if (!data.getFriends().contains(visiter.getUniqueId())) {
							visiter.sendMessage("��a�ơ�r �湮���� ���� �ܺ��� ������ ������ �߹�Ǿ����ϴ�.");
							visiter.teleport(plugin.getIsleSpawnLoc(visiter));
						}
					}
				} else {
					region.setFlag(EfeFlag.ENTRANCE, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 38) updateInv(e.getInventory(), region, GUIType.BLOCKS, p);
			if (slot == 40) updateInv(e.getInventory(), region, GUIType.FUN, p);
			if (slot == 42) updateInv(e.getInventory(), region, GUIType.PVP, p);
			
		} else if (gui == GUIType.BLOCKS) {
			
			if (slot == 38) updateInv(e.getInventory(), region, GUIType.MAIN, p);
			
			if (slot == 14 || slot == 13) {
				if (region.getFlag(DefaultFlag.BUILD) == State.ALLOW) {
					region.setFlag(DefaultFlag.BUILD, State.DENY);
					region.setFlag(DefaultFlag.BUILD.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
				} else {
					region.setFlag(DefaultFlag.BUILD, State.ALLOW);
					region.setFlag(DefaultFlag.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 16 || slot == 17) {
				if (region.getFlag(DefaultFlag.USE) == State.ALLOW) {
					region.setFlag(DefaultFlag.USE, State.DENY);
					region.setFlag(DefaultFlag.USE.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
				} else {
					region.setFlag(DefaultFlag.USE, State.ALLOW);
					region.setFlag(DefaultFlag.USE.getRegionGroupFlag(), RegionGroup.ALL);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 41 || slot == 40) {
				if (region.getFlag(DefaultFlag.CHEST_ACCESS) == State.ALLOW) {
					region.setFlag(DefaultFlag.CHEST_ACCESS, State.DENY);
					region.setFlag(DefaultFlag.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
				} else {
					region.setFlag(DefaultFlag.CHEST_ACCESS, State.ALLOW);
					region.setFlag(DefaultFlag.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.MEMBERS);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 43 || slot == 44) {
				if (region.getFlag(DefaultFlag.SNOW_MELT) == State.ALLOW) {
					region.setFlag(DefaultFlag.SNOW_MELT, State.DENY);
					region.setFlag(DefaultFlag.ICE_FORM, State.DENY);
					region.setFlag(DefaultFlag.ICE_MELT, State.DENY);
				} else {
					region.setFlag(DefaultFlag.SNOW_MELT, State.ALLOW);
					region.setFlag(DefaultFlag.ICE_FORM, State.ALLOW);
					region.setFlag(DefaultFlag.ICE_MELT, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
		} else if (gui == GUIType.FUN) {
			
			if (slot == 40) updateInv(e.getInventory(), region, GUIType.MAIN, p);
			
			if (slot == 10 || slot == 9) {
				if (region.getFlag(EfeFlag.FLY) == State.ALLOW) {
					region.setFlag(EfeFlag.FLY, State.DENY);
					
					for (Player all : plugin.getVisiters(region)) {
						all.setAllowFlight(false);
					}
				} else {
					region.setFlag(EfeFlag.FLY, State.ALLOW);
					
					for (Player all : plugin.getVisiters(region)) {
						all.setAllowFlight(true);
					}
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 37 || slot == 36) {
				if (region.getFlag(DefaultFlag.FIRE_SPREAD) == State.ALLOW) {
					region.setFlag(DefaultFlag.FIRE_SPREAD, State.DENY);
					region.setFlag(DefaultFlag.LAVA_FIRE, State.DENY);
				} else {
					region.setFlag(DefaultFlag.FIRE_SPREAD, State.ALLOW);
					region.setFlag(DefaultFlag.LAVA_FIRE, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 16 || slot == 17) {
				if (region.getFlag(DefaultFlag.GRASS_SPREAD) == State.ALLOW) {
					region.setFlag(DefaultFlag.MUSHROOMS, State.DENY);
					region.setFlag(DefaultFlag.GRASS_SPREAD, State.DENY);
					region.setFlag(DefaultFlag.MYCELIUM_SPREAD, State.DENY);
					region.setFlag(DefaultFlag.VINE_GROWTH, State.DENY);
				} else {
					region.setFlag(DefaultFlag.MUSHROOMS, State.ALLOW);
					region.setFlag(DefaultFlag.GRASS_SPREAD, State.ALLOW);
					region.setFlag(DefaultFlag.MYCELIUM_SPREAD, State.ALLOW);
					region.setFlag(DefaultFlag.VINE_GROWTH, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 43 || slot == 44) {
				if (region.getFlag(DefaultFlag.WATER_FLOW) == State.ALLOW) {
					region.setFlag(DefaultFlag.WATER_FLOW, State.DENY);
					region.setFlag(DefaultFlag.LAVA_FLOW, State.DENY);
				} else {
					region.setFlag(DefaultFlag.WATER_FLOW, State.ALLOW);
					region.setFlag(DefaultFlag.LAVA_FLOW, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
		} else if (gui == GUIType.PVP) {
			
			if (slot == 42) updateInv(e.getInventory(), region, GUIType.MAIN, p);
			
			if (slot == 10 || slot == 9) {
				if (region.getFlag(DefaultFlag.PVP) == State.ALLOW) {
					region.setFlag(DefaultFlag.PVP, State.DENY);
					region.setFlag(DefaultFlag.PVP.getRegionGroupFlag(), RegionGroup.ALL);
				} else {
					region.setFlag(DefaultFlag.PVP, State.ALLOW);
					region.setFlag(DefaultFlag.PVP.getRegionGroupFlag(), RegionGroup.ALL);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 12 || slot == 13) {
				if (region.getFlag(EfeFlag.INSTANT_KILL) == State.ALLOW)
					region.setFlag(EfeFlag.INSTANT_KILL, State.DENY);
				else
					region.setFlag(EfeFlag.INSTANT_KILL, State.ALLOW);
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 37 || slot == 36) {
				if (region.getFlag(EfeFlag.SKILL) == State.ALLOW)
					region.setFlag(EfeFlag.SKILL, State.DENY);
				else
					region.setFlag(EfeFlag.SKILL, State.ALLOW);
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 39 || slot == 40) {
				if (region.getFlag(EfeFlag.NO_POTION) == State.ALLOW) {
					region.setFlag(EfeFlag.NO_POTION, State.DENY);
					region.setFlag(DefaultFlag.POTION_SPLASH, State.ALLOW);
					region.setFlag(DefaultFlag.POTION_SPLASH.getRegionGroupFlag(), RegionGroup.ALL);
					
					for (Player all : plugin.getVisiters(region)) {
						for (PotionEffect effect : all.getActivePotionEffects()) {
							all.removePotionEffect(effect.getType());
						}
					}
				} else {
					region.setFlag(EfeFlag.NO_POTION, State.ALLOW);
					region.setFlag(DefaultFlag.POTION_SPLASH, State.DENY);
					region.setFlag(DefaultFlag.POTION_SPLASH.getRegionGroupFlag(), RegionGroup.ALL);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.containsKey(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public enum GUIType {
		MAIN, BLOCKS, FUN, PVP
	}
}