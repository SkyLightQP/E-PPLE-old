package me.efe.efeshops.listeners;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeitems.ItemStorage;
import me.efe.efeitems.SkullStorage;
import me.efe.efeitems.SkullStorage.Skull;
import me.efe.efeserver.EfeServer;
import me.efe.efeshops.EfeShops;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HeadConverterGUI implements Listener {
	public EfeShops plugin;
	public List<String> users = new ArrayList<String>();
	public Location converterLoc1;
	public Location converterLoc2;
	
	public HeadConverterGUI(EfeShops plugin) {
		this.plugin = plugin;
		this.converterLoc1 = new Location(EfeServer.getInstance().world, -60, 72, 19);
		this.converterLoc2 = new Location(EfeServer.getInstance().world, 87, 70, -756);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
				(event.getClickedBlock().getLocation().equals(converterLoc1) || event.getClickedBlock().getLocation().equals(converterLoc2))) {
			event.setCancelled(true);
			
			openGUI(event.getPlayer());
		}
 	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9*3, "��5�ơ�r ���� ��� ��ȯ��");
		
		inv.setItem(25, plugin.util.createDisplayItem("��a��ȯ", new ItemStack(Material.ANVIL), new String[]{}));
		inv.setItem(26, plugin.util.createDisplayItem("��e��nHow to use?", new ItemStack(Material.SIGN),
				new String[]{"���� ��� ��ȯ��� ���� ���", "5���� ���� ��� ���� 1����", "��ȯ���� �ݴϴ�.", "���� ��带 �ø��� ��ȯ ��ư��", "�����ּ���."}));

		users.add(player.getName());
		player.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getName())) return;
		
		if (!event.getInventory().getTitle().startsWith("��5�ơ�r ���� ��� ��ȯ��")) return;
		
		if (event.getRawSlot() == 25) {
			event.setCancelled(true);

			Player player = (Player) event.getWhoClicked();
			List<ItemStack> list = new ArrayList<ItemStack>();
			
			for (int i = 0; i < 25; i ++) {
				ItemStack item = event.getInventory().getItem(i);
				
				if (item == null || item.getType() == Material.AIR)
					continue;
				
				if (item.getType() == Material.SKULL_ITEM && item.getDurability() == 3) {
					Skull skull = SkullStorage.getSkull(item);
					
					if (!skull.isConvertable()) {
						player.sendMessage("��c�ơ�r �������� �Ǹ��ϴ� ���� ��ȯ�� �� �����ϴ�!");
						return;
					}
					
					list.add(item.clone());
				} else {
					player.sendMessage("��c�ơ�r ���� ��常 ��ȯ�� �� �ֽ��ϴ�!");
					return;
				}
			}
			
			if (list.size() < 5) {
				player.sendMessage("��c�ơ�r 5�� �̻��� ���� ��带 ��ȯ�⿡ �÷��ּ���.");
				return;
			}
			
			for (int i = 0; i < 25; i ++)
				event.getInventory().setItem(i, new ItemStack(Material.AIR));
			
			int amount = 0;
			
			while (true) {
				for (int i = 0; i < 5; i ++)
					list.remove(0);
				
				ItemStack item = ItemStorage.DECORATION_HEAD_BOX.clone();
				giveItem(player, item);
				
				amount ++;
				
				if (list.size() / 5 == 0)
					break;
			}
			
			for (ItemStack item : list)
				giveItem(player, item);
			
			player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);
			player.sendMessage("��a�ơ�r "+(amount * 5)+"���� ���� ��带 "+amount+"���� ���ڷ� ��ȯ�߽��ϴ�.");
			
		} else if (event.getRawSlot() == 26) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getName())) {
			
			for (int i = 0; i < 25; i ++) {
				ItemStack item = event.getInventory().getItem(i);
				
				if (item != null && item.getType() != Material.AIR) {
					giveItem((Player) event.getPlayer(), item.clone());
				}
			}
			
			users.remove(event.getPlayer().getName());
		}
	}
	
	public void giveItem(Player player, ItemStack item) {
		if (player.getInventory().firstEmpty() == -1)
			player.getWorld().dropItem(player.getLocation(), item);
		else
			player.getInventory().addItem(item);
	}
}