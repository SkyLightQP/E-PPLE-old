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
		Inventory inv = plugin.getServer().createInventory(null, 9*4, "§3▒§r 섬 정보 설정");
		ProtectedRegion region = plugin.getIsleRegion(p);
		
		for (int i : new int[]{10, 11, 19, 28, 29, 30})
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), new String[]{}));
		for (int i : new int[]{15, 16, 25, 32, 33, 34})
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11), new String[]{}));
		for (int i : new int[]{3, 4, 5, 12, 14, 21, 22, 23})
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10), new String[]{}));
		
		inv.setItem(13, plugin.util.createDisplayItem("§b>> "+region.getFlag(EfeFlag.TITLE), new ItemStack(Material.NAME_TAG), 
				new String[]{
			"§aLv. "+toLevelString(region.getFlag(EfeFlag.LEVEL)), 
			"범위 제한: 반경 "+getRadius(region.getFlag(EfeFlag.LEVEL))+"m 정육면체", 
			"누적방문 수: "+region.getFlag(EfeFlag.VISITERS).size(), 
			"최대 방문 수: "+region.getFlag(EfeFlag.MAX_VISIT), 
			"이번주 추천 수: "+plugin.rankingGui.getRecommendation(p), 
			"", 
			"§9클릭으로 섬 이름을 수정합니다."}));
		
		inv.setItem(20, plugin.util.createDisplayItem("§a소개글", new ItemStack(Material.PAPER), 
				new String[]{region.getFlag(EfeFlag.DESCRIPTION), "", "§9클릭으로 수정합니다."}));
		
		inv.setItem(24, plugin.util.createDisplayItem("§a기후", new ItemStack(Material.SAPLING), new String[]{"준비중"}));
		
		p.openInventory(inv);
		users.add(p.getName());
	}
	
	public String toLevelString(int lv) {
		switch (lv) {
		case 1:
			return "α [알파]";
		case 2:
			return "β [베타]";
		case 3:
			return "γ [감마]";
		case 4:
			return "δ [델타]";
		case 5:
			return "ω [오메가]";
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
			
			p.sendMessage("§a▒§r 섬 이름은 한글 최대 10자, 영어 최대 20자입니다.");
			
			gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
						return;
					}
					
					if (event.getName().isEmpty()) {
						event.getPlayer().sendMessage("§c▒§r 섬 이름이 입력되지 않았습니다!");
						return;
					}
					
					if (event.getName().getBytes().length > 20) {
						event.getPlayer().sendMessage("§c▒§r 섬 이름이 너무 깁니다!");
						return;
					}
					
					region.setFlag(EfeFlag.TITLE, event.getName());
					
					event.getPlayer().sendMessage("§a▒§r 섬 이름이 변경되었습니다.");
					
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
			
			p.sendMessage("§a▒§r 소개글은 한글 최대 20자, 영어 최대 40자입니다.");
			
			gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
						return;
					}
					
					if (event.getName().isEmpty()) {
						event.getPlayer().sendMessage("§c▒§r 소개글이 입력되지 않았습니다!");
						return;
					}
					
					if (event.getName().getBytes().length > 40) {
						event.getPlayer().sendMessage("§c▒§r 소개글이 너무 깁니다!");
						return;
					}
					
					region.setFlag(EfeFlag.DESCRIPTION, event.getName());
					
					event.getPlayer().sendMessage("§a▒§r 소개글이 변경되었습니다.");
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem(region.getFlag(EfeFlag.DESCRIPTION), new ItemStack(Material.NAME_TAG), 
					new String[]{}));
			gui.open();
			
			break;
		case 24:
			p.sendMessage("§c▒§r 기후 변경은 준비중인 시스템입니다.");
			
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