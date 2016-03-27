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
		Inventory inv = plugin.getServer().createInventory(p, 9, "§9▒§r 미끼");
		
//		inv.setItem(0, EfeUtils.item.createDisplayItem("§b§l"+plugin.getDescription().getFullName(), new ItemStack(Material.BOOK_AND_QUILL), 
//				new String[]{"Made by §lEfe"}));
		
		inv.setItem(4, EfeUtils.item.createDisplayItem("§b미끼로 쓸 물고기를 선택해주세요.", new ItemStack(Material.RAW_FISH), 
				new String[]{
			"§b미끼를 달면 물고기가 더 빨리 잡힙니다!", 
			"", 
			"§9[!] 1~6cm 물고기만 가능하며, 일회용입니다."}));
		
		//inv.setItem(8, EfeUtils.item.createDisplayItem("§e§l나가기", new ItemStack(Material.WOOD_DOOR), new String[]{"클릭시 창을 닫습니다."}));
		
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
				p.sendMessage(plugin.main+"§a▒§r 1~6cm의 물고기만 가능합니다.");
				return;
			}
			
			p.getInventory().setItem(e.getSlot(), EfeUtils.item.getUsed(e.getCurrentItem(), p));
			plugin.setLure(p.getItemInHand());
			
			p.sendMessage(plugin.main+"§a▒§r 낚싯대에 미끼를 달았습니다.");
			
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