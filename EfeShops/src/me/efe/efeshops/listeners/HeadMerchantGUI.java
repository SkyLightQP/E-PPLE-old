package me.efe.efeshops.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import me.efe.efeitems.SkullStorage;
import me.efe.efeitems.SkullStorage.Skull;
import me.efe.efeshops.EfeShops;

public class HeadMerchantGUI implements Listener {
	private final double price = 200.0D;
	private EfeShops plugin;
	private Set<UUID> users = new HashSet<UUID>();
	
	public HeadMerchantGUI(EfeShops plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(final Player player) {
		if (player.hasMetadata("async_headshop"))
			return;
		
		player.setMetadata("async_headshop", new FixedMetadataValue(plugin, ""));
		
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				openGUIPrivate(player);
				
				player.removeMetadata("async_headshop", plugin);
			}
		});
	}
	
	private void openGUIPrivate(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9 * 6, "��2�ơ�r ���ڷ��̼� ��� ���� ��2["+plugin.vault.getBalance(player)+"E]");
		
		for (int i = 0; i < 6; i ++) {
			inv.setItem(i * 9, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), new String[]{}));
			inv.setItem(8 + i * 9, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), new String[]{}));
		}
		
		for (Skull skull : Skull.values()) {
			if (skull.isConvertable())
				continue;
			
			ItemStack item = SkullStorage.getItem(skull).clone();
			
			if (plugin.vault.hasEnough(player, price)) {
				plugin.util.addLore(item, "��3����: ��b��l-"+price+"E");
				plugin.util.addLore(item, "");
				plugin.util.addLore(item, "��9Ŭ������ �������� �����մϴ�.");
			} else {
				plugin.util.addLore(item, "��4����: ��c��l-"+price+"E");
			}
			
			inv.addItem(item);
		}
		
		users.add(player.getUniqueId());
		player.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getUniqueId()))
			return;
		
		event.setCancelled(true);
		
		if (event.getRawSlot() >= 54 || event.getAction() == InventoryAction.NOTHING ||
				event.getCurrentItem() == null || event.getCurrentItem().getType() != Material.SKULL_ITEM)
			return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (!plugin.vault.hasEnough(player, price)) {
			player.sendMessage("��c�ơ�r �������� �����մϴ�!");
			return;
		}
		
		plugin.vault.take(player, price);
		
		Skull skull = SkullStorage.getSkull(event.getCurrentItem());
		ItemStack item = SkullStorage.getItem(skull).clone();
		
		if (player.getInventory().firstEmpty() == -1)
			player.getWorld().dropItem(player.getLocation(), item);
		else
			player.getInventory().addItem(item);
		
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
		player.sendMessage("��a�ơ�r " + skull.getName().substring(1) + "�Ӹ��� �����߽��ϴ�! ��c[-" + price + "E]");
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
}
