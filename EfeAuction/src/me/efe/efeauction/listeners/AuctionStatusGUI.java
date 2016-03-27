package me.efe.efeauction.listeners;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.efe.efeauction.AuctionItem;
import me.efe.efeauction.AuctionMaterial;
import me.efe.efeauction.EfeAuction;
import me.efe.efecommunity.Post;
import me.efe.efecore.util.EfeUtils;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;
import me.efe.unlimitedrpg.unlimitedtag.TagType;
import me.efe.unlimitedrpg.unlimitedtag.UnlimitedTagAPI;
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
import org.bukkit.inventory.meta.ItemMeta;

public class AuctionStatusGUI implements Listener {
	public EfeAuction plugin;
	public Map<UUID, List<AuctionItem>> users = new HashMap<UUID, List<AuctionItem>>();
	
	public AuctionStatusGUI(EfeAuction plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9*6, "��2�ơ�r ���� ���� - �� ��� ����");
		
		List<AuctionItem> list = plugin.sqlManager.getAuctionItems(player);
		
		refreshGUI(player, inv, list);
		
		users.put(player.getUniqueId(), list);
		player.openInventory(inv);
	}
	
	public void refreshGUI(Player player, Inventory inv, List<AuctionItem> itemList) {
		inv.clear();
		
		if (itemList.isEmpty()) {
			inv.setItem(22, EfeUtils.item.createDisplayItem("��c����� �������� �����ϴ�.", new ItemStack(Material.BARRIER), null));
		} else {
			int startSlot = 22 - itemList.size() / 2;
			
			for (int i = 0; i < itemList.size(); i ++) {
				AuctionItem item = itemList.get(i);
				ItemStack itemStack = plugin.getItemStack(item.getUniqueId());
				
				EfeUtils.item.addLore(itemStack, "��d��m==================", "��3����: " + item.getPrice() + "E", "��9Ŭ������ ����� ����մϴ�.");
				
				inv.setItem(startSlot + i, itemStack);
			}
		}

		inv.setItem(40, EfeUtils.item.createDisplayItem("��e��nHow to use?", new ItemStack(Material.NETHER_STAR),
				new String[]{"����� ���ϴ� ��������", "�Ʒ� �κ��丮���� Ŭ���ϼ���!", "���õ� �� ������ ��ϵ˴ϴ�.", "", "��c��� ������: �Ǹ� ������ 10%"}));
		
		inv.setItem(45, EfeUtils.item.createDisplayItem("��c�ڷΰ���", new ItemStack(Material.WOOD_DOOR), null));
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.containsKey(event.getWhoClicked().getUniqueId()))
			return;
		
		event.setCancelled(true);
		
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
			return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (event.getRawSlot() >= 54) {
			Material type = event.getCurrentItem().getType();
			if (type == Material.BOAT || type == Material.COMPASS || type == Material.NETHER_STAR || type == Material.SKULL_ITEM) {
				return;
			}
			
			if (UnlimitedTagAPI.hasTag(event.getCurrentItem(), TagType.VESTED) ||
					UnlimitedTagAPI.hasTag(event.getCurrentItem(), TagType.LOCKED) ||
					UnlimitedTagAPI.hasTag(event.getCurrentItem(), TagType.STAMPED)) {
				return;
			}
			
			if (users.get(player.getUniqueId()).size() >= 7) {
				player.sendMessage("��c�ơ�r �ִ� 7������ ��� �����մϴ�.");
				return;
			}
			
			ItemStack itemStack = event.getCurrentItem().clone();
			int slot = event.getSlot();
			
			AuctionMaterial material = AuctionMaterial.getAuctionMaterial(itemStack);
			double averagePrice = plugin.auctionLogHandler.getAveragePrice(material);
			
			player.closeInventory();
			player.sendMessage("��a�ơ�r �Ǹ��� ������ �Է����ּ���!");
			player.sendMessage("��a�ơ�r �ֱ� ��� �ŷ� ����: "+averagePrice+"E");
			
			AnvilGUI gui = new AnvilGUI(plugin, player, new AnvilGUI.AnvilClickEventHandler() {
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
						return;
					}
					
					if (!EfeUtils.string.isInteger(event.getName())) {
						event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
						return;
					}
					
					double price = Integer.parseInt(event.getName());
					
					if (price <= 0) {
						event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
						return;
					}
					
					int charge = (int) (price / 10);
					
					if (!plugin.vault.hasEnough(player, charge)) {
						event.getPlayer().sendMessage("��a�ơ�r ������� ������ �ݾ��� �����մϴ�!");
						return;
					}
					
					UUID id = UUID.randomUUID();
					AuctionItem item = new AuctionItem(id, event.getPlayer().getUniqueId(), price, new Date());
					
					plugin.setItemStack(id, itemStack);
					plugin.sqlManager.addAuctionItem(item, material);
					plugin.vault.take(player, charge);
					
					event.getPlayer().getInventory().setItem(slot, new ItemStack(Material.AIR));
					
					event.getPlayer().sendMessage("��a�ơ�r ǰ���� ��ϵǾ����ϴ�!");
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			ItemStack icon = itemStack.clone();
			ItemMeta meta = icon.getItemMeta();
			
			meta.setDisplayName(averagePrice+"");
			icon.setItemMeta(meta);
			
			EfeUtils.item.addLore(icon, "", "��c������ ��ư�� Ŭ�����ּ���.");
			
			gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
			gui.open();
		} else if (event.getRawSlot() == 45) {
			player.closeInventory();
			
			plugin.auctionMainGUI.openGUI(player);
		} else if (19 <= event.getRawSlot() && event.getRawSlot() <= 25) {
			ItemStack icon = event.getCurrentItem().clone();
			
			if (icon.getType() == Material.BARRIER)
				return;
			
			String lore = "��c���� �����Ͻðڽ��ϱ�?";
			if (!EfeUtils.item.containsLore(icon, lore)) {
				EfeUtils.item.addLore(icon, "��c������� �������� ���մϴ�.", lore);
				
				event.setCurrentItem(icon);
				
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 2.0F);
				return;
			}
			
			List<AuctionItem> list = users.get(player.getUniqueId());
			int startSlot = 22 - list.size() / 2;
			
			int index = event.getRawSlot() - startSlot;
			AuctionItem item = list.get(index);
			
			ItemStack itemStack = plugin.getItemStack(item.getUniqueId());
			
			if (player.getInventory().firstEmpty() == -1) {
				Post post = Post.getBuilder()
						.setSender("���� ����")
						.setMessage("��a������ ������", "�κ��丮�� ���� �� �߼۵Ǿ����ϴ�.")
						.setItems(new ItemStack[]{itemStack})
						.build();
				Post.sendPost(player, post);
				
				new FancyMessage("��a�ơ�r �κ��丮�� ���� ��")
				.then("��b��n�����ԡ�r")
					.command("/������")
					.tooltip("��b/������")
				.then("���� �������� �߼۵Ǿ����ϴ�.")
				.send(player);
			} else {
				player.getInventory().addItem(itemStack);
			}
			
			plugin.setItemStack(item.getUniqueId(), null);
			plugin.sqlManager.removeAuctionItem(item);
			list.remove(index);
			
			player.sendMessage("��a�ơ�r ��ϵ� �������� �����߽��ϴ�.");
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
			
			refreshGUI(player, event.getInventory(), list);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (users.containsKey(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
}
