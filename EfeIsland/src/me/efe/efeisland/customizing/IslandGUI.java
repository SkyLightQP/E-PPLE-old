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
		Inventory inv = plugin.getServer().createInventory(null, 9*3, "§3▒§r 섬");
		
		inv.setItem(10, plugin.util.createDisplayItem("§c섬 관리", new ItemStack(Material.REDSTONE), new String[]{"자신의 섬에 대해", "커스터마이징 합니다."}));
		
		if (p.getWorld().equals(plugin.world)) {
			inv.setItem(13, plugin.util.createDisplayItem("§d텔레포트", new ItemStack(Material.ENDER_PEARL), new String[]{"원래 있던 섬으로 돌아갑니다."}));
			
			ProtectedRegion pRegion = plugin.getIsleRegion(p);
			ProtectedRegion lRegion = plugin.getIsleRegion(p.getLocation());
			
			if (pRegion != null && pRegion != lRegion) {
				inv.setItem(4, plugin.util.createDisplayItem("§d귀환", new ItemStack(Material.ENDER_PEARL), new String[]{"자신의 섬으로 이동합니다."}));
			} else {
				inv.setItem(22, plugin.util.createDisplayItem("§b초대/방문", new ItemStack(Material.BOAT), new String[]{"타인을 초대하거나 다른 섬에 방문합니다."}));
			}
		} else {
			inv.setItem(13, plugin.util.createDisplayItem("§d텔레포트", new ItemStack(Material.ENDER_PEARL), new String[]{"자신의 섬으로 이동합니다."}));
		}
		
		//TODO Update
		inv.setItem(16, plugin.util.createDisplayItem("§7Comming Soon!", new ItemStack(Material.SULPHUR), 
				new String[]{}));
//		inv.setItem(16, plugin.util.createDisplayItem("§e요즘 뜨는 섬", new ItemStack(Material.GLOWSTONE_DUST), 
//				new String[]{"인기있는 섬을 확인합니다."}));
		
		inv.setItem(25, plugin.util.createDisplayItem("§c닫기", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		inv.setItem(26, plugin.util.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), new String[]{}));
		
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
				p.sendMessage("§c▒§r 자신의 섬에서만 설정할 수 있습니다.");
				return;
			}
			
			e.getInventory().setItem(0, plugin.util.createDisplayItem("§b/섬 정보", new ItemStack(Material.BOOK), 
					new String[]{"섬 이름, 설명 등", "기본 정보들을 설정합니다."}));
			e.getInventory().setItem(20, plugin.util.createDisplayItem("§d/섬 커스터마이징", new ItemStack(Material.CLAY_BRICK), 
					new String[]{"건축, PvP 등", "섬 특수 옵션을 설정합니다."}));
			
			break;
		case 13:
			p.closeInventory();
			
			if (p.getVehicle() != null) {
				p.sendMessage("§c▒§r 배에서 내려야 이동이 가능합니다.");
				return;
			}
			
			if (p.getWorld().equals(plugin.world))
				IslandUtils.teleportReturn(p);
			else
				IslandUtils.teleportIsland(p, p);
			
			break;
		case 16:
			//TODO Update
//			e.getInventory().setItem(8, plugin.util.createDisplayItem("§b/섬 랭킹", new ItemStack(Material.PAINTING), 
//					new String[]{"일주일간 많은 추천을 받은", "섬들을 순위대로 확인합니다."}));
//			e.getInventory().setItem(24, plugin.util.createDisplayItem("§d/섬 홍보", new ItemStack(Material.SIGN), 
//					new String[]{"자신의 섬을 홍보하거나", "홍보되고 있는 다른 섬을 확인합니다."}));
			
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