package me.efe.efeshops.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.efe.efecommunity.Post;
import me.efe.efeitems.ItemStorage;
import me.efe.efeitems.SkullStorage;
import me.efe.efeitems.SkullStorage.Skull;
import me.efe.efeshops.EfeShops;
import mkremins.fanciful.FancyMessage;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CaseGUI implements Listener {
	public EfeShops plugin;
	public Random random = new Random();
	public HashMap<String, List<ItemStack>> users = new HashMap<String, List<ItemStack>>();
	
	public CaseGUI(EfeShops plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9*3, "��5�ơ�r ���ڷ��̼� ��� ���� ����");
		
		refresh(inv, null);
		
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		for (int i = 0; i < 8; i ++) {
			while (true) {
				Skull skull = Skull.values()[random.nextInt(Skull.values().length)];
				
				if (skull.isRare())
					continue;
				
				list.add(SkullStorage.getItem(skull));
				break;
			}
		}
		
		users.put(player.getName(), list);
		player.openInventory(inv);
	}
	
	public void refresh(Inventory inv, List<ItemStack> list) {
		if (list == null) {
			for (int i : new int[]{1, 10, 19, 7, 16, 25})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), new String[]{}));
			for (int i : new int[]{0, 9, 18, 8, 17, 26})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), new String[]{}));
			
			for (int i : new int[]{3, 4, 5, 12, 14, 21, 22, 23})
				inv.setItem(i, plugin.util.createDisplayItem("��e1���� ���ڸ� �������ּ���!", new ItemStack(Material.CHEST), new String[]{}));
			
		} else {
			for (int i : new int[]{0, 1, 9, 10, 18, 19, 7, 8, 16, 17, 25, 26})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), new String[]{}));
			
			int[] slots = new int[]{3, 4, 5, 12, 14, 21, 22, 23};
			for (int i = 0; i < 8; i ++) {
				inv.setItem(slots[i], list.get(i));
			}
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (!users.containsKey(event.getWhoClicked().getName())) return;
		event.setCancelled(true);
		
		if (!event.getInventory().getTitle().startsWith("��5�ơ�r ���ڷ��̼� ��� ���� ����")) return;
		if (event.getCurrentItem() == null || !event.getCurrentItem().getType().equals(Material.CHEST) || event.getRawSlot() >= 27) return;
		
		Player player = (Player) event.getWhoClicked();
		List<ItemStack> list = users.get(player.getName());
		int index = toIndex(event.getRawSlot());
		
		
		String itemName = ItemStorage.getName(ItemStorage.DECORATION_HEAD_BOX);
		
		if (!ItemStorage.hasItem(player, itemName)) {
			player.closeInventory();
			return;
		}
		
		ItemStorage.takeItem(player, itemName, 1);
		
		
		ItemStack item = list.get(index);
		Skull skull = SkullStorage.getSkull(item);
		
		giveItem(player, item);
		
		refresh(event.getInventory(), list);
		
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 0.25F);
		player.sendMessage("��a�ơ�r ��a<"+skull.getName()+">��r ���� ��带 ȹ���߽��ϴ�!");
	}
	
	private int toIndex(int slot) {
		return slot <= 5 ? slot - 3 : slot == 12 ? 3 : slot == 14 ? 4 : slot - 16;
	}
	
	private void giveItem(Player player, ItemStack item) {
		if (player.getInventory().firstEmpty() == -1) {
			Post post = Post.getBuilder()
					.setSender(player.getName())
					.setMessage("��a���ڿ��� �߰��� ���� ���", "�κ��丮�� ���� �� �߼۵Ǿ����ϴ�.")
					.setItems(new ItemStack[]{item.clone()})
					.build();
			Post.sendPost(player, post);
			
			new FancyMessage("��a�ơ�r �κ��丮�� ���� ��")
			.then("��b��n�����ԡ�r")
				.command("/������")
				.tooltip("��b/������")
			.then("���� �������� �߼۵Ǿ����ϴ�.")
			.send(player);
		} else {
			player.getInventory().addItem(item.clone());
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.containsKey(event.getPlayer().getName())) {
			users.remove(event.getPlayer().getName());
		}
	}
}