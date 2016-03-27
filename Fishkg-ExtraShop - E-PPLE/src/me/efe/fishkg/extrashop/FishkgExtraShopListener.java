package me.efe.fishkg.extrashop;

import java.util.ArrayList;
import java.util.List;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeserver.EfeServer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FishkgExtraShopListener implements Listener {
	public FishkgExtraShop plugin;
	public List<String> users = new ArrayList<String>();
	
	public FishkgExtraShopListener(FishkgExtraShop plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void invClick(InventoryClickEvent e) {
		if (!plugin.fishkg.shopGui.users.contains(e.getWhoClicked().getName())) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getRawSlot() == 33) {
			p.closeInventory();
			openGUI(p);
		}
	}
	
	public void openGUI(Player p) {
		Inventory inv = Bukkit.getServer().createInventory(p, 9 * plugin.guiRow, "§9▒§r 물고기 상점 - 다수 판매");
		
		refresh(inv);
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	public void refresh(Inventory inv) {
		inv.clear();
		
		for (int i = 9 * (plugin.guiRow - 1); i < 9 * plugin.guiRow; i ++) {
			inv.setItem(i, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3), null));
		}
		
		inv.setItem(9 * plugin.guiRow - 5, EfeUtils.item.createDisplayItem("§b판매", new ItemStack(Material.GOLD_INGOT), new String[]{"등록된 물고기를 모두 판매합니다."}));
		
		int emerald = plugin.getConfig().getInt("shop.emerald");
		
		if (emerald != 0) {
			ItemStack item = inv.getItem(9);
			item.setType(EfeUtils.item.getMaterial(emerald));
			
			inv.setItem(9, item);
		}
	}
	
	@EventHandler (ignoreCancelled = true)
	public void guiClick(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getRawSlot() == 9 * plugin.guiRow - 5) {
			e.setCancelled(true);
			
			double total = 0.0D;
			boolean hasFish = false;
			
			for (int i = 0; i < 9 * (plugin.guiRow - 1); i ++) {
				ItemStack item = e.getInventory().getItem(i);
				
				if (!isValid(item))
					continue;
				else if (!plugin.fishkg.isFish(item)) {
					p.sendMessage(plugin.main+"§c▒§r 물고기가 아닌 아이템이 포함되어 있습니다!");
					p.closeInventory();
					return;
				}
				
				hasFish = true;
				
				ItemMeta meta = item.getItemMeta();
				
				double cm = plugin.fishkg.getLength(item);
				double price = cm * plugin.fishkg.getConfig().getDouble("shop.price");
				
				if (cm == 0) {
					p.sendMessage(plugin.main+"§a▒§r 구버전 Fishkg의 물고기로 확인되어 제거되었습니다.");
					continue;
				}
				
				if (plugin.fishkg.getConfig().getBoolean("fish.addGrade")) {
					String grade = meta.getLore().get(0);
					
					for (String str : new String[]{"S", "A", "B", "C", "D"})
						if (grade.contains(str))
							price += plugin.fishkg.getConfig().getDouble("shop.grade."+str);
				}
				
				if (price < 0)
					price = 0;
				
				total += price;
			}
			
			if (!hasFish) {
				p.sendMessage(plugin.main+"§c▒§r 판매할 물고기를 위 슬롯에 올려주세요!");
				return;
			}
			
			refresh(e.getInventory());
			
			if (plugin.fishkg.getConfig().getBoolean("shop.pricePoint"))
				total = (int) total;
			
			if (plugin.fishkg.getConfig().getInt("shop.emerald") != 0) {
				EfeUtils.player.giveItem(p, new ItemStack(EfeUtils.item.getMaterial(plugin.fishkg.getConfig().getInt("shop.emerald")), (int) total));
				
				p.sendMessage(plugin.main+"§a▒§r 물고기를 팔아 총 §e"+(int) total+"§r E를 획득했습니다.");
			} else {
				plugin.vault.give(p, total);
				EfeServer.getInstance().extraListener.sendTabTitle(p);
				
				p.sendMessage(plugin.main+"§a▒§r 물고기를 팔아 총 §e"+total+"§r E를 획득했습니다.");
			}
			
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.5F);
			
		} else if (!isFish(e)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void guiDrag(InventoryDragEvent e) {
		if (users.contains(e.getWhoClicked().getName())) {
			e.setCancelled(true);
			
			EfeUtils.player.updateInv((Player) e.getWhoClicked());
		}
	}
	
	@EventHandler
	public void guiClose(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
			
			for (int i = 0; i < 9 * (plugin.guiRow - 1); i ++) {
				ItemStack item = e.getInventory().getItem(i);
				
				if (!isValid(item)) continue;
				
				EfeUtils.player.giveItem((Player) e.getPlayer(), item);
			}
		}
	}
	
	public boolean isFish(InventoryClickEvent e) {
		if (!isValid(e.getCurrentItem()) && isValid(e.getCursor()) && plugin.fishkg.isFish(e.getCursor())) return true;
		if (!isValid(e.getCursor()) && isValid(e.getCurrentItem()) && plugin.fishkg.isFish(e.getCurrentItem())) return true;
		if (isValid(e.getCurrentItem()) && isValid(e.getCursor()) && plugin.fishkg.isFish(e.getCurrentItem()) && plugin.fishkg.isFish(e.getCursor())) return true;
		
		return false;
	}
	
	public boolean isValid(ItemStack item) {
		return item != null && item.getType() != Material.AIR;
	}
}