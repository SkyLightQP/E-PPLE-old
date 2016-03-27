package me.efe.bloodskin;

import java.util.ArrayList;
import java.util.List;

import me.efe.bloodskin.skins.Skin;
import me.efe.efeserver.PlayerData;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BloodSkinGUI implements Listener {
	public BloodSkin plugin;
	public List<String> users = new ArrayList<String>();
	
	public BloodSkinGUI(BloodSkin plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(p, 9*6, "§4▒§r 블러드 스킨");
		
		refresh(inv, p);
		
		p.openInventory(inv);
		users.add(p.getName());
	}
	
	public void refresh(Inventory inv, Player p) {
		inv.clear();
		
		PlayerData data = PlayerData.get(p);
		
		inv.addItem(plugin.util.createDisplayItem("§r장착 해제", new ItemStack(Material.BARRIER), new String[]{"스킨을 사용하지 않습니다."}));
		
		if (data.getBloodSkin() == -1)
			inv.setItem(0, enchant(inv.getItem(0)));
		
		for (int i = 0; i < data.getBloodSkins().size(); i ++) {
			String name = data.getBloodSkins().get(i);
			Skin skin = plugin.skins.get(name);
			
			ItemStack icon = skin.getIcon().clone();
			
			if (data.getBloodSkin() == i)
				icon = enchant(icon);
			
			inv.addItem(icon);
		}
		
		inv.setItem(51, plugin.util.createDisplayItem("§c닫기", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		inv.setItem(52, plugin.util.createDisplayItem("§b코인", new ItemStack(Material.DIAMOND), new String[]{}));
		inv.setItem(53, plugin.util.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), new String[]{}));
	}
	
	public ItemStack enchant(ItemStack item) {
		if (item.getType().equals(Material.GOLDEN_APPLE)) {
			item.setDurability((short) 1);
			
			return item;
		}
		
		return plugin.util.enchant(item, Enchantment.SILK_TOUCH, 100);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName()) || !e.getInventory().getTitle().endsWith("블러드 스킨")) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 54) return;
		
		Player p = (Player) e.getWhoClicked();
		PlayerData data = PlayerData.get(p);
		
		if (e.getRawSlot() == 51) {
			p.closeInventory();
			return;
		} else if (e.getRawSlot() == 52) {
			p.closeInventory();
			plugin.getServer().dispatchCommand(p, "코인");
			return;
		} else if (e.getRawSlot() == 53) {
			p.closeInventory();
			plugin.getServer().dispatchCommand(p, "메뉴");
			return;
		}
		
		data.setBloodSkin(e.getRawSlot() - 1);
		
		p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 2.0F);
		
		refresh(e.getInventory(), p);
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}