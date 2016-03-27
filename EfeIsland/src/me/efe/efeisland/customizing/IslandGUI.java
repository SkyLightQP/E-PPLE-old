package me.efe.efeisland.customizing;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeisland.EfeIsland;
import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class IslandGUI implements Listener {
	public EfeIsland plugin;
	public List<String> users = new ArrayList<String>();
	
	public IslandGUI(EfeIsland plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*3, "��3�ơ�r ��");
		
		inv.setItem(10, plugin.util.createDisplayItem("��c�� ����", new ItemStack(Material.REDSTONE), new String[]{"�ڽ��� ���� ����", "Ŀ���͸���¡ �մϴ�."}));
		
		if (p.getWorld().equals(plugin.world)) {
			inv.setItem(13, plugin.util.createDisplayItem("��d�ڷ���Ʈ", new ItemStack(Material.ENDER_PEARL), new String[]{"���� �ִ� ������ ���ư��ϴ�."}));
			
			ProtectedRegion pRegion = plugin.getIsleRegion(p);
			ProtectedRegion lRegion = plugin.getIsleRegion(p.getLocation());
			
			if (pRegion != null && pRegion != lRegion) {
				inv.setItem(4, plugin.util.createDisplayItem("��d��ȯ", new ItemStack(Material.ENDER_PEARL), new String[]{"�ڽ��� ������ �̵��մϴ�."}));
			} else {
				inv.setItem(22, plugin.util.createDisplayItem("��b�ʴ�/�湮", new ItemStack(Material.BOAT), new String[]{"Ÿ���� �ʴ��ϰų� �ٸ� ���� �湮�մϴ�."}));
			}
		} else {
			inv.setItem(13, plugin.util.createDisplayItem("��d�ڷ���Ʈ", new ItemStack(Material.ENDER_PEARL), new String[]{"�ڽ��� ������ �̵��մϴ�."}));
		}
		
		//TODO Update
		inv.setItem(16, plugin.util.createDisplayItem("��7Comming Soon!", new ItemStack(Material.SULPHUR), 
				new String[]{}));
//		inv.setItem(16, plugin.util.createDisplayItem("��e���� �ߴ� ��", new ItemStack(Material.GLOWSTONE_DUST), 
//				new String[]{"�α��ִ� ���� Ȯ���մϴ�."}));
		
		inv.setItem(25, plugin.util.createDisplayItem("��c�ݱ�", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		inv.setItem(26, plugin.util.createDisplayItem("��e���� �޴�", new ItemStack(Material.NETHER_STAR), new String[]{}));
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 27) return;
		
		Player p = (Player) e.getWhoClicked();
		
		switch (e.getRawSlot()) {
		case 0:
			p.closeInventory();
			plugin.infoGui.openGUI(p);
			
			break;
		case 4:
			p.closeInventory();
			
			IslandUtils.teleportIsland(p, p);
			break;
		case 8:
			p.closeInventory();
			plugin.rankingGui.openGUI(p);
			
			break;
		case 10:
			Location loc = p.getLocation();
			
			if (!plugin.getIsleRegion(p).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
				p.sendMessage("��c�ơ�r �ڽ��� �������� ������ �� �ֽ��ϴ�.");
				return;
			}
			
			e.getInventory().setItem(0, plugin.util.createDisplayItem("��b/�� ����", new ItemStack(Material.BOOK), 
					new String[]{"�� �̸�, ���� ��", "�⺻ �������� �����մϴ�."}));
			e.getInventory().setItem(20, plugin.util.createDisplayItem("��d/�� Ŀ���͸���¡", new ItemStack(Material.CLAY_BRICK), 
					new String[]{"����, PvP ��", "�� Ư�� �ɼ��� �����մϴ�."}));
			
			break;
		case 13:
			p.closeInventory();
			
			if (p.getVehicle() != null) {
				p.sendMessage("��c�ơ�r �迡�� ������ �̵��� �����մϴ�.");
				return;
			}
			
			if (p.getWorld().equals(plugin.world))
				IslandUtils.teleportReturn(p);
			else
				IslandUtils.teleportIsland(p, p);
			
			break;
		case 16:
			//TODO Update
//			e.getInventory().setItem(8, plugin.util.createDisplayItem("��b/�� ��ŷ", new ItemStack(Material.PAINTING), 
//					new String[]{"�����ϰ� ���� ��õ�� ����", "������ ������� Ȯ���մϴ�."}));
//			e.getInventory().setItem(24, plugin.util.createDisplayItem("��d/�� ȫ��", new ItemStack(Material.SIGN), 
//					new String[]{"�ڽ��� ���� ȫ���ϰų�", "ȫ���ǰ� �ִ� �ٸ� ���� Ȯ���մϴ�."}));
			
			break;
		case 20:
			p.closeInventory();
			plugin.customGui.openGUI(p);
			
			break;
		case 22:
			p.closeInventory();
			plugin.teleportGui.openGUI(p);
			
			break;
		case 24:
			p.closeInventory();
			plugin.advGui.openGUI(p);
			
			break;
		case 25:
			p.closeInventory();
			
			break;
		case 26:
			p.closeInventory();
			EfeServer.getInstance().mainGui.openGUI(p);
			
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