package me.efe.fishkg.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.efe.efecore.util.EfeUtils;
import me.efe.fishkg.Fishkg;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BucketGUI implements Listener {
	public Fishkg plugin;
	public List<String> users = new ArrayList<String>();
	
	public BucketGUI(Fishkg plugin) {
		this.plugin = plugin;
	}
	
	public void loadBucket(Player p, int row) {
		Inventory inv = p.getServer().createInventory(p, 9*row, "§9["+plugin.getDescription().getFullName()+"] 물고기 양동이");
		
		for (ItemStack item : getFishList(p.getItemInHand())) {
			inv.addItem(item);
		}
		
		p.openInventory(inv);
		users.add(p.getName());
	}
	
	public void saveBucket(Player p, Inventory inv) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		for (ItemStack item : inv.getContents()) {
			if (item == null || item.getType().equals(Material.AIR)) continue;
			
			list.add(item);
		}
		
		setFishList(p.getItemInHand(), list);
	}
	
	public List<ItemStack> getFishList(ItemStack item) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore() || item.getItemMeta().getLore().size() < 2) return list;
		
		for (String stack : item.getItemMeta().getLore().get(1).replace("§", "").split("#")) {
			if (stack == null || stack.isEmpty() || stack.equals("7")) continue;
			
			String[] data = stack.split("\\|");
			
			String name = data[3].replace("&", "§");
			
			Material type = Material.getMaterial(data[0]);
			int amount = Integer.parseInt(data[2]);
			short durability = Short.parseShort(data[1]);
			
			List<String> lore = new ArrayList<String>();
			for (String str : data[4].split("\n")) lore.add(str.replace("&", "§"));
			
			list.add(EfeUtils.item.createItem(name, new ItemStack(type, amount, durability), lore.toArray(new String[lore.size()])));
		}
		
		return list;
	}
	
	public void setFishList(ItemStack item, List<ItemStack> list) {
		String allData = "";
		
		for (ItemStack fish : list) {
			if (fish == null || fish.getType().equals(Material.AIR)) continue;
			
			String data = 
					fish.getType().name().replace("§", "&") + "|" + 
					fish.getDurability() + "|" + 
					fish.getAmount() + "|" + 
					fish.getItemMeta().getDisplayName().replace("§", "&") + "|";
			
			Iterator<String> it = fish.getItemMeta().getLore().iterator();
			while (it.hasNext()) {
				data += it.next().replace("§", "&");
				if (it.hasNext()) data += "\n";
			}
			
			allData += data + "#";
		}
		
		String value = "";
		for (char c : allData.toCharArray()) {
			value += "§" + c;
		}
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(1, value);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	@EventHandler (ignoreCancelled = true)
	public void openBucket(PlayerInteractEvent e) {
		if (EfeUtils.event.isRightClick(e) && e.getItem() != null && EfeUtils.item.containsLore(e.getItem(), plugin.getConfig().getString("data-token.bucket"))) {
			e.setCancelled(true);
			
			loadBucket(e.getPlayer(), plugin.getConfig().getInt("bucket.row"));
		}
	}
	
	@EventHandler
	public void bucketClick(InventoryClickEvent e) {
		if (users.contains(e.getWhoClicked().getName()) && !checkItem(e)) {
			e.setCancelled(true);
		}
	}
	
	public boolean checkItem(InventoryClickEvent e) {
		if (e.getCursor().getType().equals(Material.RAW_FISH) && e.getCurrentItem().getType().equals(Material.AIR)) {
			return true;
		} else if (e.getCursor().getType().equals(Material.AIR) && e.getCurrentItem().getType().equals(Material.RAW_FISH)) {
			return true;
		} else if (e.getCursor().getType().equals(Material.RAW_FISH) && e.getCurrentItem().getType().equals(Material.RAW_FISH)) {
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void bucketDrag(InventoryDragEvent e) {
		if (users.contains(e.getWhoClicked().getName())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void closeBucket(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
			saveBucket((Player) e.getPlayer(), e.getInventory());
		}
	}
}
