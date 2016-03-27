package me.efe.titlemaker;

import java.util.ArrayList;
import java.util.List;

import me.efe.efecore.util.EfeUtils;

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

public class TitleGUI implements Listener {
	public TitleMaker plugin;
	public List<String> users = new ArrayList<String>();
	
	public TitleGUI(TitleMaker plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9*6, "§9▒§r 칭호");
		
		refresh(inv, player);
		
		users.add(player.getName());
		player.openInventory(inv);
	}
	
	public void refresh(Inventory inv, Player player) {
		inv.clear();
		
		String mainTitle = TitleManager.getMainTitle(player);
		
		inv.addItem(EfeUtils.item.createDisplayItem("§r장착 해제", new ItemStack(Material.BARRIER), new String[]{"칭호를 사용하지 않습니다."}));
		
		if (mainTitle == null)
			inv.setItem(0, EfeUtils.item.enchant(inv.getItem(0), Enchantment.SILK_TOUCH, 100));
		
		List<String> list = TitleManager.getTitles(player);
		
		for (int i = 0; i < list.size(); i ++) {
			ItemStack item = EfeUtils.item.createDisplayItem(list.get(i), new ItemStack(Material.BOOK), new String[]{"클릭하면 대표 칭호를 변경합니다."});
			
			if (mainTitle != null && mainTitle.equals(list.get(i)))
				item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
			
			inv.addItem(item);
		}
		
		inv.setItem(51, EfeUtils.item.createDisplayItem("§c닫기", new ItemStack(Material.WOOD_DOOR), null));
		inv.setItem(52, EfeUtils.item.createDisplayItem("§b코인", new ItemStack(Material.DIAMOND), null));
		inv.setItem(53, EfeUtils.item.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), null));
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getName()) || !event.getInventory().getTitle().endsWith("칭호")) return;
		event.setCancelled(true);
		
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) || event.getRawSlot() >= 54) return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (event.getRawSlot() == 51) {
			player.closeInventory();
			return;
		} else if (event.getRawSlot() == 52) {
			player.closeInventory();
			plugin.getServer().dispatchCommand(player, "코인");
			return;
		} else if (event.getRawSlot() == 53) {
			player.closeInventory();
			plugin.getServer().dispatchCommand(player, "메뉴");
			return;
		}
		
		TitleManager.setMainTitle(player, event.getRawSlot() - 1);
		plugin.hologrammer.apply(player);
		
		player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0F, 2.0F);
		
		refresh(event.getInventory(), player);
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getName())) {
			users.remove(event.getPlayer().getName());
		}
	}
}