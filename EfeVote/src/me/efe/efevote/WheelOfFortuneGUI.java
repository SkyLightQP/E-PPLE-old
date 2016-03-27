package me.efe.efevote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeitems.ItemStorage;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efeserver.util.CashAPI;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class WheelOfFortuneGUI implements Listener {
	private EfeVote plugin;
	private Set<UUID> users = new HashSet<UUID>();
	private int[] wheelSlots;
	private int[] paneSlots;
	
	public WheelOfFortuneGUI(EfeVote plugin) {
		this.plugin = plugin;
		
		this.wheelSlots = new int[]{13, 14, 24, 33, 42, 50, 49, 48, 38, 29, 20, 12};
		this.paneSlots = new int[]{0, 9, 18, 27, 36, 45, 8, 17, 26, 35, 44, 53};
	}
	
	public void openGUI(Player player) {
		PlayerData data = PlayerData.get(player);
		data.takeWheelOfFortune();
		
		Inventory inv = plugin.getServer().createInventory(player, 9 * 6, "¡×6¢Æ¡×r ÈÙ ¿Àºê Æ÷Ãá");
		
		for (int slot : paneSlots) {
			inv.setItem(slot, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0), null));
		}

		inv.setItem(4, EfeUtils.item.createDisplayItem("¡×r¡å", new ItemStack(Material.HOPPER), null));
		inv.setItem(31, EfeUtils.item.createDisplayItem("¡×aÅ¬¸¯ÇÏ¼¼¿ä!", new ItemStack(Material.ARROW), null));
		
		List<ItemStack> itemList = new ArrayList<ItemStack>();
		
		while (itemList.size() != wheelSlots.length) {
			if (itemList.size() >= wheelSlots.length - 3) {
				if (Math.random() <= 0.1) {
					itemList.add(ItemStorage.RANDOM_TITLE_BOOK.clone());
				} else if (Math.random() <= 0.3) {
					itemList.add(ItemStorage.BLOOD_SKIN_BOX_POPULAR.clone());
				} else {
					itemList.add(ItemStorage.DECORATION_HEAD_BOX.clone());
				}
			} else {
				if (PermissionsEx.getUser(player).inGroup("appler")) {
					itemList.add(EfeUtils.item.createDisplayItem("¡×e100 Coins", new ItemStack(Material.GOLD_INGOT), null));
				} else if (PermissionsEx.getUser(player).inGroup("golden_appler")) {
					itemList.add(EfeUtils.item.createDisplayItem("¡×e150 Coins", new ItemStack(Material.GOLD_INGOT), null));
				} else {
					itemList.add(EfeUtils.item.createDisplayItem("¡×a50 Ephe", new ItemStack(Material.EMERALD), null));
				}
			}
		}
		
		Collections.shuffle(itemList);
		
		playAnimation(player, inv, itemList, 0, 3, false);
		
		users.add(player.getUniqueId());
		player.openInventory(inv);
	}
	
	public void playAnimation(Player player, Inventory inv, List<ItemStack> itemList, final int index, long period, boolean isStopping) {
		UUID id = player.getUniqueId();
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (!users.contains(id))
					return;
				
				int newIndex = index + 1;
				
				if (newIndex >= wheelSlots.length)
					newIndex = 0;
				
				for (int i = 0; i < itemList.size(); i ++) {
					ItemStack itemStack = itemList.get(i);
					
					int slotIndex = newIndex + i;
					if (slotIndex >= wheelSlots.length)
						slotIndex -= wheelSlots.length;
					
					inv.setItem(wheelSlots[slotIndex], itemStack);
				}
				
				short durability = (short) ((inv.getItem(0).getDurability() == 0) ? 4 : 0);
				for (int slot : paneSlots) {
					inv.setItem(slot, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, durability), null));
				}
				
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.5F);
				
				
				
				if (inv.getItem(22) != null && inv.getItem(22).getType() == Material.ARROW) {
					long newPeriod = period;
					
					if (newPeriod >= 22 || Math.random() <= 0.75)
						newPeriod += 3;
					
					if (newPeriod >= 21) {
						ItemStack reward = inv.getItem(wheelSlots[0]);
						
						if (reward.getType() == Material.GOLD_INGOT) {
							int amount = Integer.parseInt(reward.getItemMeta().getDisplayName().substring(2).replace(" Coins", ""));
							
							CashAPI.deposit(player, amount);
							
							player.sendMessage("¡×a¢Æ¡×r ÈÙ ¿Àºê Æ÷Ãá¿¡¼­ ¡×e" + amount + " Coins¡×r¸¦ È¹µæÇß½À´Ï´Ù.");
						} else if (reward.getType() == Material.EMERALD) {
							double amount = Double.parseDouble(reward.getItemMeta().getDisplayName().substring(2).replace(" Ephe", ""));
							
							EfeServer.getInstance().vault.give(player, amount);
							
							player.sendMessage("¡×a¢Æ¡×r ÈÙ ¿Àºê Æ÷Ãá¿¡¼­ ¡×e" + amount + "E¡×r¸¦ È¹µæÇß½À´Ï´Ù.");
						} else {
							if (player.getInventory().firstEmpty() == -1)
								player.getWorld().dropItem(player.getLocation(), reward.clone());
							else
								player.getInventory().addItem(reward.clone());
							
							player.sendMessage("¡×a¢Æ¡×r ÈÙ ¿Àºê Æ÷Ãá¿¡¼­ " + ItemStorage.getName(reward) + "¡×r ¾ÆÀÌÅÛÀ» È¹µæÇß½À´Ï´Ù.");
						}
						
						player.sendMessage("¡×a¢Æ¡×r ³»ÀÏµµ E-PPLEÀ» ÃßÃµÇØ¼­ ÈÙ ¿Àºê Æ÷Ãá¿¡ µµÀüÇØº¸¼¼¿ä!");
						
						reward.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
						
						ItemMeta meta = reward.getItemMeta();
						meta.addItemFlags(ItemFlag.values());
						reward.setItemMeta(meta);
						
						inv.setItem(wheelSlots[0], reward);
						
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 0.5F);
						return;
					}
					
					playAnimation(player, inv, itemList, newIndex, newPeriod, true);
				} else {
					playAnimation(player, inv, itemList, newIndex, 1, false);
				}
			}
		}, period);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getUniqueId()))
			return;
		
		event.setCancelled(true);
		
		if (event.getRawSlot() >= 54 || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
			return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (event.getRawSlot() == 31) {
			event.getInventory().setItem(22, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.ARROW), null));
			event.getInventory().setItem(31, new ItemStack(Material.AIR));
			
			player.playSound(player.getLocation(), Sound.PISTON_EXTEND, 1.0F, 0.75F);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
}
