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
				Inventory inv = plugin.getServer().createInventory(player, 9*6, "§2▒§r 유저 마켓");
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
		
		inv.setItem(45, EfeUtils.item.createDisplayItem("§c뒤로가기", new ItemStack(Material.WOOD_DOOR), null));
		
		inv.setItem(6, EfeUtils.item.createDisplayItem("§e등록순 정렬", new ItemStack(Material.WATCH), null));
		inv.setItem(7, EfeUtils.item.createDisplayItem("§e가격순 정렬", new ItemStack(Material.EMERALD), null));
		inv.setItem(8, EfeUtils.item.createDisplayItem("§e검색", new ItemStack(Material.EMPTY_MAP), null));
		
		if (data.isSortedByDate()) {
			ItemStack icon = EfeUtils.item.enchant(inv.getItem(6), Enchantment.SILK_TOUCH, 100);
			
			if (!data.isReversed) {
				EfeUtils.item.addLore(icon, "§7최근 등록순으로 정렬됨");
			} else {
				EfeUtils.item.addLore(icon, "§7이전 등록순으로 정렬됨");
			}
			
			inv.setItem(6, icon);
		}
		
		if (data.isSortedByPrice()) {
			ItemStack icon = EfeUtils.item.enchant(inv.getItem(7), Enchantment.SILK_TOUCH, 100);
			
			if (!data.isReversed) {
				EfeUtils.item.addLore(icon, "§7높은 가격순으로 정렬됨");
			} else {
				EfeUtils.item.addLore(icon, "§7낮은 가격순으로 정렬됨");
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
			
			EfeUtils.item.addLore(itemStack, "§d§m==================", "§3가격: " + item.getPrice() + "E","§9클릭으로 구매합니다.");
			
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
				player.sendMessage("§c▒§r 소지금이 부족합니다!");
				return;
			}
			
			ItemStack icon = event.getCurrentItem().clone();
			
			if (item.getSeller().equals(player)) {
				String lore = "§c이 아이템은 당신이 게시한 아이템입니다.";
				if (!EfeUtils.item.containsLore(icon, lore)) {
					EfeUtils.item.addLore(icon, lore);
					
					event.setCurrentItem(icon);
					return;
				}
				
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				return;
			}
			
			String lore = "§a정말 구입하시겠습니까?";
			if (!EfeUtils.item.containsLore(icon, lore)) {
				EfeUtils.item.addLore(icon, lore);
				
				event.setCurrentItem(icon);
				
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			ItemStack itemStack = plugin.getItemStack(item.getUniqueId());
			
			if (player.getInventory().firstEmpty() == -1) {
				Post post = Post.getBuilder()
						.setSender("유저 마켓")
						.setMessage("§a구매한 아이템", "인벤토리가 가득 차 발송되었습니다.")
						.setItems(new ItemStack[]{itemStack})
						.build();
				Post.sendPost(player, post);
				
				new FancyMessage("§a▒§r 인벤토리가 가득 차")
				.then("§b§n우편함§r")
					.command("/우편함")
					.tooltip("§b/우편함")
				.then("으로 아이템이 발송되었습니다.")
				.send(player);
			} else {
				player.getInventory().addItem(itemStack);
			}
			
			plugin.vault.take(player, item.getPrice());
			plugin.vault.give(item.getSeller(), item.getPrice());
			
			plugin.setItemStack(item.getUniqueId(), null);
			plugin.sqlManager.removeAuctionItem(item);
			
			plugin.auctionLogHandler.addLog(itemStack.clone(), item.getPrice());
			
			player.sendMessage("§a▒§r 아이템을 구매했습니다! §c[-" + item.getPrice() + "E]");
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
			
			if (item.getSeller().isOnline())
				item.getSeller().getPlayer().sendMessage("§a▒§r "+player.getName()+"님께서 당신의 아이템을 구매하셨습니다! §a[+" + item.getPrice() + "E]");
			
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
		player.sendMessage("§a▒§r 검색할 아이템 이름을 입력해주세요.");
		
		AnvilGUI gui = new AnvilGUI(plugin, player, new AnvilGUI.AnvilClickEventHandler() {
			@Override
			public void onAnvilClick(AnvilClickEvent event) {
				if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
					event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
					return;
				}
				
				if (event.getName().isEmpty() || event.getName().equals("아이템")) {
					event.getPlayer().sendMessage("§c▒§r 검색할 아이템 이름을 입력해주세요.");
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
		
		meta.setDisplayName("아이템");
		
		icon.setItemMeta(meta);
		
		EfeUtils.item.addLore(icon, "", "§c오른쪽 버튼을 클릭해주세요.");
		
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
