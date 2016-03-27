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
		Inventory inv = plugin.getServer().createInventory(player, 9 * 5, "��8�ơ�r "+name);
		
		for (int i : new int[]{0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44}) {
			inv.setItem(i, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.IRON_FENCE), null));
		}
		
		inv.setItem(22, EfeUtils.item.createDisplayItem("��b"+name, new ItemStack(Material.ANVIL, 1, damage), new String[]{"������ �������ּ���."}));
		
		inv.setItem(40, EfeUtils.item.createDisplayItem("��a��lHow to use", new ItemStack(Material.EMPTY_MAP), new String[]{
			"������ �������� ȸ���մϴ�.", 
			"���� ��ȸ���Դϴ�.", 
			"", 
			"��e��l1.��e ���ϴ� ������ �����Ѵ�.", 
			"��e��l2.��e �߾ӿ� �� 8���� ��Ÿ����.", 
			"��e��l3.��e ��� Ŭ���Ѵ�.", 
			"��e��l4.��e �̶� �� ������ �����ϸ�", 
			" ��e�ϳ��� ������ 10% ȸ��", 
			"��e��l5.��e ������ ���� ������.", 
			" ��e����� �ı����� �ʴ´�", 
			"��e��l6.��e �� ������ ������ ��ŭ", 
			" ��e�������� ȸ���ȴ�."}));
		
		if (damage == 2)
			inv.setItem(4, EfeUtils.item.createDisplayItem("��c��翡 ������ ����� ������", new ItemStack(Material.LAVA_BUCKET), 
					new String[]{"��c�� ������ 4ȸ �����ϸ�", "��c�������� �ı��˴ϴ�."}));
		else if (damage == 1)
			inv.setItem(4, EfeUtils.item.createDisplayItem("��c������ �ߵ� �� ������", new ItemStack(Material.LAVA_BUCKET), 
					new String[]{"��c�� ������ 5ȸ �����ϸ�", "��c�������� �ı��˴ϴ�."}));
		
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
				player.sendMessage("��c�ơ�r ������ �� ���� �������Դϴ�!");
				return;
			}
			
			event.getInventory().setItem(22, event.getCurrentItem().clone());
			event.setCurrentItem(null);
			
			ItemStack used = EfeUtils.item.getUsed(player.getItemInHand().clone(), player);
			player.setItemInHand(used);
			
			for (int i : slots)
				event.getInventory().setItem(i, EfeUtils.item.createDisplayItem("��bŬ���ؼ� �����ϼ���!", new ItemStack(Material.STONE), null));
			
			player.sendMessage("��a�ơ�r ������ �����մϴ�!");
			player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);
		} else if (event.getRawSlot() <= 45) {
			
			if (event.getCurrentItem().getType() == Material.STONE && event.getCurrentItem().getDurability() == 0 && center.getType() != Material.ANVIL) {
				if (Math.random() <= 0.5) {
					if (chance != 9)
						chance --;
					
					if (chance <= 0) {
						player.closeInventory();
						
						player.sendMessage("��a�ơ�r ������ ������ �������� �ı��Ǿ����ϴ�.");
						player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0F, 0.75F);
						return;
					} else {
						event.setCurrentItem(EfeUtils.item.createDisplayItem("��c������ �����߽��ϴ�.", new ItemStack(Material.COBBLESTONE, 0), null));
						
						if (chance != 9)
							player.sendMessage("��a�ơ�r �� ������ �����߽��ϴ�. ��c[���� ��ȸ: "+chance+"��]");
						else
							player.sendMessage("��a�ơ�r �� ������ �����߽��ϴ�.");
						player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
						
						users.put(player.getName(), chance);
					}
				} else {
					event.setCurrentItem(EfeUtils.item.createDisplayItem("��e������ �����߽��ϴ�!", new ItemStack(Material.STONE, 1, (short) 6), null));
					
					center.setDurability((short) (center.getDurability() - center.getType().getMaxDurability() / 10));
					event.getInventory().setItem(22, center.clone());
					
					player.sendMessage("��a�ơ�r �� ������ �����߽��ϴ�!");
					player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
				}
				
				if (isComplete(event.getInventory())) {
					users.put(player.getName(), -1);
					
					player.getInventory().addItem(center.clone());
					
					player.sendMessage("��a�ơ�r ������ ������ �����߽��ϴ�!!");
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