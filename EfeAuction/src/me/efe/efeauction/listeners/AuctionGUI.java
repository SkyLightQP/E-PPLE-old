package me.efe.efeauction.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.efe.efeauction.AuctionItem;
import me.efe.efeauction.AuctionLabel;
import me.efe.efeauction.AuctionMaterial;
import me.efe.efeauction.EfeAuction;
import me.efe.efecommunity.Post;
import me.efe.efecore.util.EfeUtils;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;
import mkremins.fanciful.FancyMessage;

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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class AuctionGUI implements Listener {
	public EfeAuction plugin;
	public ItemComparator comparator = new ItemComparator();
	public Map<UUID, GUIData> users = new HashMap<UUID, GUIData>();
	
	public AuctionGUI(EfeAuction plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player, AuctionLabel label) {
		List<AuctionItem> itemList = plugin.sqlManager.getAuctionItems(label);
		
		openGUI(player, null, itemList);
	}
	
	public void openGUI(Player player, AuctionLabel label, List<AuctionItem> itemList) {
		if (player.hasMetadata("async_auctiongui"))
			return;
		
		player.setMetadata("async_auctiongui", new FixedMetadataValue(plugin, ""));
		
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				Inventory inv = plugin.getServer().createInventory(player, 9*6, "��2�ơ�r ���� ����");
				GUIData data = new GUIData(label, inv, itemList);
				
				refreshGUI(player, inv, data);
				
				users.put(player.getUniqueId(), data);
				player.openInventory(inv);
				
				player.removeMetadata("async_auctiongui", plugin);
			}
		});
	}
	
	public void refreshGUI(Player player, Inventory inv, GUIData data) {
		inv.clear();
		
		inv.setItem(45, EfeUtils.item.createDisplayItem("��c�ڷΰ���", new ItemStack(Material.WOOD_DOOR), null));
		
		inv.setItem(6, EfeUtils.item.createDisplayItem("��e��ϼ� ����", new ItemStack(Material.WATCH), null));
		inv.setItem(7, EfeUtils.item.createDisplayItem("��e���ݼ� ����", new ItemStack(Material.EMERALD), null));
		inv.setItem(8, EfeUtils.item.createDisplayItem("��e�˻�", new ItemStack(Material.EMPTY_MAP), null));
		
		if (data.isSortedByDate()) {
			ItemStack icon = EfeUtils.item.enchant(inv.getItem(6), Enchantment.SILK_TOUCH, 100);
			
			if (!data.isReversed) {
				EfeUtils.item.addLore(icon, "��7�ֱ� ��ϼ����� ���ĵ�");
			} else {
				EfeUtils.item.addLore(icon, "��7���� ��ϼ����� ���ĵ�");
			}
			
			inv.setItem(6, icon);
		}
		
		if (data.isSortedByPrice()) {
			ItemStack icon = EfeUtils.item.enchant(inv.getItem(7), Enchantment.SILK_TOUCH, 100);
			
			if (!data.isReversed) {
				EfeUtils.item.addLore(icon, "��7���� ���ݼ����� ���ĵ�");
			} else {
				EfeUtils.item.addLore(icon, "��7���� ���ݼ����� ���ĵ�");
			}
			
			inv.setItem(7, icon);
		}
		
		int maxPage = (data.itemList.size() - 1) / 45;
		if (maxPage < 4) {
			for (int i = 0; i < maxPage; i ++) {
				ItemStack icon = EfeUtils.item.createDisplayItem("Page " + (i + 1), new ItemStack(Material.PAPER), null);
				
				if (i == data.page)
					icon.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
				
				inv.setItem(49 - i / 2, icon);
			}
		} else {
			int startPage = 0;
			if (maxPage >= data.page + 2)
				startPage = data.page - 2;
			else
				startPage = maxPage - 4;
			
			for (int i = 0; i < 0 + 5; i ++) {
				int page = startPage + i;
				ItemStack icon = EfeUtils.item.createDisplayItem("Page " + (page + 1), new ItemStack(Material.PAPER), null);
				
				if (page == data.page)
					icon.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
				
				inv.setItem(47 + i, icon);
			}
		}
		
		
		for (int i = 0; i < 45; i ++) {
			int index = data.page * 45 + i;
			
			if (index >= data.itemList.size())
				break;
			
			AuctionItem item = data.itemList.get(index);
			ItemStack itemStack = plugin.getItemStack(item.getUniqueId());
			
			EfeUtils.item.addLore(itemStack, "��d��m==================", "��3����: " + item.getPrice() + "E","��9Ŭ������ �����մϴ�.");
			
			inv.setItem(9 + i, itemStack);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.containsKey(event.getWhoClicked().getUniqueId()))
			return;
		
		event.setCancelled(true);
		
		if (event.getRawSlot() >= 54 || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
			return;
		
		Player player = (Player) event.getWhoClicked();
		GUIData data = users.get(player.getUniqueId());
		
		if (!event.getInventory().equals(data.inventory))
			return;
		
		if (8 < event.getRawSlot() && event.getRawSlot() < 45) {
			int index = data.page * 45 + event.getRawSlot() - 9;
			AuctionItem item = data.itemList.get(index);
			
			if (!plugin.vault.hasEnough(player, item.getPrice())) {
				player.sendMessage("��c�ơ�r �������� �����մϴ�!");
				return;
			}
			
			ItemStack icon = event.getCurrentItem().clone();
			
			if (item.getSeller().equals(player)) {
				String lore = "��c�� �������� ����� �Խ��� �������Դϴ�.";
				if (!EfeUtils.item.containsLore(icon, lore)) {
					EfeUtils.item.addLore(icon, lore);
					
					event.setCurrentItem(icon);
					return;
				}
				
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				return;
			}
			
			String lore = "��a���� �����Ͻðڽ��ϱ�?";
			if (!EfeUtils.item.containsLore(icon, lore)) {
				EfeUtils.item.addLore(icon, lore);
				
				event.setCurrentItem(icon);
				
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
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
			
			plugin.vault.take(player, item.getPrice());
			plugin.vault.give(item.getSeller(), item.getPrice());
			
			plugin.setItemStack(item.getUniqueId(), null);
			plugin.sqlManager.removeAuctionItem(item);
			
			plugin.auctionLogHandler.addLog(itemStack.clone(), item.getPrice());
			
			player.sendMessage("��a�ơ�r �������� �����߽��ϴ�! ��c[-" + item.getPrice() + "E]");
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
			
			if (item.getSeller().isOnline())
				item.getSeller().getPlayer().sendMessage("��a�ơ�r "+player.getName()+"�Բ��� ����� �������� �����ϼ̽��ϴ�! ��a[+" + item.getPrice() + "E]");
			
			data.itemList.remove(index);
			
			if (event.getRawSlot() == 9 && data.page > 2)
				data.page --;
			
			refreshGUI(player, event.getInventory(), data);
		} else if (event.getRawSlot() == 6) {
			data.sortByDate();
			
			refreshGUI(player, event.getInventory(), data);
		} else if (event.getRawSlot() == 7) {
			data.sortByPrice();
			
			refreshGUI(player, event.getInventory(), data);
		} else if (event.getRawSlot() == 8) {
			player.closeInventory();
			
			openSearchingGUI(player, data.selectedLabel);
		} else if (event.getRawSlot() == 45) {
			if (data.selectedLabel != null) {
				player.closeInventory();
				
				openGUI(player, data.selectedLabel);
			} else {
				player.closeInventory();
				
				plugin.auctionMainGUI.openGUI(player);
			}
		}
	}
	
	public void openSearchingGUI(Player player, AuctionLabel selectedLabel) {
		player.sendMessage("��a�ơ�r �˻��� ������ �̸��� �Է����ּ���.");
		
		AnvilGUI gui = new AnvilGUI(plugin, player, new AnvilGUI.AnvilClickEventHandler() {
			@Override
			public void onAnvilClick(AnvilClickEvent event) {
				if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
					event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
					return;
				}
				
				if (event.getName().isEmpty() || event.getName().equals("������")) {
					event.getPlayer().sendMessage("��c�ơ�r �˻��� ������ �̸��� �Է����ּ���.");
					return;
				}
				
				List<AuctionMaterial> list = AuctionMaterial.getAuctionMaterial(event.getName());
				List<AuctionItem> itemList = new ArrayList<AuctionItem>();
				
				if (selectedLabel != null) {
					for (AuctionMaterial material : list) {
						itemList.addAll(plugin.sqlManager.getAuctionItems(selectedLabel, material));
					}
				} else {
					for (AuctionMaterial material : list) {
						itemList.addAll(plugin.sqlManager.getAuctionItems(material));
					}
				}
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						openGUI(event.getPlayer(), selectedLabel, itemList);
					}
				}, 5L);
				
				event.setWillClose(true);
				event.setWillDestroy(true);
			}
		});
		
		ItemStack icon = new ItemStack(Material.EMPTY_MAP);
		ItemMeta meta = icon.getItemMeta();
		
		meta.setDisplayName("������");
		
		icon.setItemMeta(meta);
		
		EfeUtils.item.addLore(icon, "", "��c������ ��ư�� Ŭ�����ּ���.");
		
		gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
		gui.open();
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (users.containsKey(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
	
	private class GUIData {
		private final AuctionLabel selectedLabel;
		private final Inventory inventory;
		private List<AuctionItem> itemList;
		private int page;
		private int sortWay;
		private boolean isReversed;
		
		public GUIData(AuctionLabel selectedLabel, Inventory inventory, List<AuctionItem> itemList) {
			this.selectedLabel = selectedLabel;
			this.inventory = inventory;
			this.itemList = itemList;
			this.page = 0;
		}
		
		public void sortByDate() {
			if (sortWay == 0) {
				isReversed = !isReversed;
				
				Collections.reverse(itemList);
			} else {
				sortWay = 0;
				
				comparator.setSortWay(sortWay);
				Collections.sort(itemList, comparator);
			}
		}
		
		public void sortByPrice() {
			if (sortWay == 1) {
				isReversed = !isReversed;
				
				Collections.reverse(itemList);
			} else {
				sortWay = 1;
				
				comparator.setSortWay(sortWay);
				Collections.sort(itemList, comparator);
			}
		}
		
		public boolean isSortedByDate() {
			return sortWay == 0;
		}
		
		public boolean isSortedByPrice() {
			return sortWay == 1;
		}
	}
	
	private class ItemComparator implements Comparator<AuctionItem> {
		private int sortWay;
		
		public void setSortWay(int sortWay) {
			this.sortWay = sortWay;
		}
		
		@Override
		public int compare(AuctionItem arg0, AuctionItem arg1) {
			if (sortWay == 1)
				return (arg0.getDate().after(arg1.getDate())) ? -1 : 1;
			else if (sortWay == 2)
				return (arg0.getPrice() > arg1.getPrice()) ? -1 : (arg0.getPrice() < arg1.getPrice()) ? 1 : 0;
			else
				return 0;
		}
	}
}
