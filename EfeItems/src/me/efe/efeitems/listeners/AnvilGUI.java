package me.efe.efeitems.listeners;

import java.util.HashMap;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeitems.EfeItems;
import me.efe.efemobs.rudish.enchant.EnchantUtils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AnvilGUI implements Listener {
	public EfeItems plugin;
	public HashMap<String, Integer> users = new HashMap<String, Integer>();
	private int[] slots = new int[]{12, 13, 14, 21, 23, 30, 31, 32};
	
	public AnvilGUI(EfeItems plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player, String name, short damage) {
		Inventory inv = plugin.getServer().createInventory(player, 9 * 5, "§8▒§r "+name);
		
		for (int i : new int[]{0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44}) {
			inv.setItem(i, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.IRON_FENCE), null));
		}
		
		inv.setItem(22, EfeUtils.item.createDisplayItem("§b"+name, new ItemStack(Material.ANVIL, 1, damage), new String[]{"도구를 선택해주세요."}));
		
		inv.setItem(40, EfeUtils.item.createDisplayItem("§a§lHow to use", new ItemStack(Material.EMPTY_MAP), new String[]{
			"도구의 내구도를 회복합니다.", 
			"모루는 일회용입니다.", 
			"", 
			"§e§l1.§e 원하는 도구를 선택한다.", 
			"§e§l2.§e 중앙에 돌 8개가 나타난다.", 
			"§e§l3.§e 모두 클릭한다.", 
			"§e§l4.§e 이때 돌 수리에 성공하면", 
			" §e하나당 내구도 10% 회복", 
			"§e§l5.§e 실패해 돌이 깨져도.", 
			" §e무기는 파괴되지 않는다", 
			"§e§l6.§e 돌 수리에 성공한 만큼", 
			" §e내구도가 회복된다."}));
		
		if (damage == 2)
			inv.setItem(4, EfeUtils.item.createDisplayItem("§c모루에 수상한 기운이 감돌아", new ItemStack(Material.LAVA_BUCKET), 
					new String[]{"§c돌 수리를 4회 실패하면", "§c아이템이 파괴됩니다."}));
		else if (damage == 1)
			inv.setItem(4, EfeUtils.item.createDisplayItem("§c세월은 견딜 수 없는지", new ItemStack(Material.LAVA_BUCKET), 
					new String[]{"§c돌 수리를 5회 실패하면", "§c아이템이 파괴됩니다."}));
		
		users.put(player.getName(), (damage == 1) ? 4 : (damage == 2) ? 3 : 9);
		player.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (!users.containsKey(event.getWhoClicked().getName())) return;
		event.setCancelled(true);
		
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
		
		Player player = (Player) event.getWhoClicked();
		ItemStack center = event.getInventory().getItem(22);
		int chance = users.get(player.getName());
		
		if (chance == -1) return;
		
		
		if (event.getRawSlot() >= 45 && center.getType() == Material.ANVIL) {
			if (!EnchantUtils.isEnchantable(event.getCurrentItem().clone()) || event.getCurrentItem().getType() == Material.GOLDEN_APPLE) {
				player.sendMessage("§c▒§r 수리할 수 없는 아이템입니다!");
				return;
			}
			
			event.getInventory().setItem(22, event.getCurrentItem().clone());
			event.setCurrentItem(null);
			
			ItemStack used = EfeUtils.item.getUsed(player.getItemInHand().clone(), player);
			player.setItemInHand(used);
			
			for (int i : slots)
				event.getInventory().setItem(i, EfeUtils.item.createDisplayItem("§b클릭해서 수리하세요!", new ItemStack(Material.STONE), null));
			
			player.sendMessage("§a▒§r 수리를 시작합니다!");
			player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);
		} else if (event.getRawSlot() <= 45) {
			
			if (event.getCurrentItem().getType() == Material.STONE && event.getCurrentItem().getDurability() == 0 && center.getType() != Material.ANVIL) {
				if (Math.random() <= 0.5) {
					if (chance != 9)
						chance --;
					
					if (chance <= 0) {
						player.closeInventory();
						
						player.sendMessage("§a▒§r 수리에 실패해 아이템이 파괴되었습니다.");
						player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0F, 0.75F);
						return;
					} else {
						event.setCurrentItem(EfeUtils.item.createDisplayItem("§c수리에 실패했습니다.", new ItemStack(Material.COBBLESTONE, 0), null));
						
						if (chance != 9)
							player.sendMessage("§a▒§r 돌 수리에 실패했습니다. §c[남은 기회: "+chance+"번]");
						else
							player.sendMessage("§a▒§r 돌 수리에 실패했습니다.");
						player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
						
						users.put(player.getName(), chance);
					}
				} else {
					event.setCurrentItem(EfeUtils.item.createDisplayItem("§e수리에 성공했습니다!", new ItemStack(Material.STONE, 1, (short) 6), null));
					
					center.setDurability((short) (center.getDurability() - center.getType().getMaxDurability() / 10));
					event.getInventory().setItem(22, center.clone());
					
					player.sendMessage("§a▒§r 돌 수리에 성공했습니다!");
					player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
				}
				
				if (isComplete(event.getInventory())) {
					users.put(player.getName(), -1);
					
					player.getInventory().addItem(center.clone());
					
					player.sendMessage("§a▒§r 아이템 수리에 성공했습니다!!");
					player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0F, 0.75F);
					
				}
			}
		}
	}
	
	public boolean isComplete(Inventory inv) {
		for (int i : slots) {
			if (inv.getItem(i).getType() == Material.STONE && inv.getItem(i).getDurability() == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.containsKey(event.getPlayer().getName())) {
			users.remove(event.getPlayer().getName());
		}
	}
}