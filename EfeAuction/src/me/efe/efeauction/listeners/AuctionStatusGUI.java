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
		Inventory inv = plugin.getServer().createInventory(player, 9*6, "§2▒§r 유저 마켓 - 내 등록 정보");
		
		List<AuctionItem> list = plugin.sqlManager.getAuctionItems(player);
		
		refreshGUI(player, inv, list);
		
		users.put(player.getUniqueId(), list);
		player.openInventory(inv);
	}
	
	public void refreshGUI(Player player, Inventory inv, List<AuctionItem> itemList) {
		inv.clear();
		
		if (itemList.isEmpty()) {
			inv.setItem(22, EfeUtils.item.createDisplayItem("§c등록한 아이템이 없습니다.", new ItemStack(Material.BARRIER), null));
		} else {
			int startSlot = 22 - itemList.size() / 2;
			
			for (int i = 0; i < itemList.size(); i ++) {
				AuctionItem item = itemList.get(i);
				ItemStack itemStack = plugin.getItemStack(item.getUniqueId());
				
				EfeUtils.item.addLore(itemStack, "§d§m==================", "§3가격: " + item.getPrice() + "E", "§9클릭으로 등록을 취소합니다.");
				
				inv.setItem(startSlot + i, itemStack);
			}
		}

		inv.setItem(40, EfeUtils.item.createDisplayItem("§e§nHow to use?", new ItemStack(Material.NETHER_STAR),
				new String[]{"등록을 원하는 아이템을", "아래 인벤토리에서 클릭하세요!", "선택된 한 스택이 등록됩니다.", "", "§c등록 수수료: 판매 가격의 10%"}));
		
		inv.setItem(45, EfeUtils.item.createDisplayItem("§c뒤로가기", new ItemStack(Material.WOOD_DOOR), null));
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
				player.sendMessage("§c▒§r 최대 7개까지 등록 가능합니다.");
				return;
			}
			
			ItemStack itemStack = event.getCurrentItem().clone();
			int slot = event.getSlot();
			
			AuctionMaterial material = AuctionMaterial.getAuctionMaterial(itemStack);
			double averagePrice = plugin.auctionLogHandler.getAveragePrice(material);
			
			player.closeInventory();
			player.sendMessage("§a▒§r 판매할 가격을 입력해주세요!");
			player.sendMessage("§a▒§r 최근 평균 거래 가격: "+averagePrice+"E");
			
			AnvilGUI gui = new AnvilGUI(plugin, player, new AnvilGUI.AnvilClickEventHandler() {
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
						return;
					}
					
					if (!EfeUtils.string.isInteger(event.getName())) {
						event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
						return;
					}
					
					double price = Integer.parseInt(event.getName());
					
					if (price <= 0) {
						event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
						return;
					}
					
					int charge = (int) (price / 10);
					
					if (!plugin.vault.hasEnough(player, charge)) {
						event.getPlayer().sendMessage("§a▒§r 수수료로 지급할 금액이 부족합니다!");
						return;
					}
					
					UUID id = UUID.randomUUID();
					AuctionItem item = new AuctionItem(id, event.getPlayer().getUniqueId(), price, new Date());
					
					plugin.setItemStack(id, itemStack);
					plugin.sqlManager.addAuctionItem(item, material);
					plugin.vault.take(player, charge);
					
					event.getPlayer().getInventory().setItem(slot, new ItemStack(Material.AIR));
					
					event.getPlayer().sendMessage("§a▒§r 품목이 등록되었습니다!");
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			ItemStack icon = itemStack.clone();
			ItemMeta meta = icon.getItemMeta();
			
			meta.setDisplayName(averagePrice+"");
			icon.setItemMeta(meta);
			
			EfeUtils.item.addLore(icon, "", "§c오른쪽 버튼을 클릭해주세요.");
			
			gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
			gui.open();
		} else if (event.getRawSlot() == 45) {
			player.closeInventory();
			
			plugin.auctionMainGUI.openGUI(player);
		} else if (19 <= event.getRawSlot() && event.getRawSlot() <= 25) {
			ItemStack icon = event.getCurrentItem().clone();
			
			if (icon.getType() == Material.BARRIER)
				return;
			
			String lore = "§c정말 수거하시겠습니까?";
			if (!EfeUtils.item.containsLore(icon, lore)) {
				EfeUtils.item.addLore(icon, "§c수수료는 돌려받지 못합니다.", lore);
				
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
						.setSender("유저 마켓")
						.setMessage("§a수거한 아이템", "인벤토리가 가득 차 발송되었습니다.")
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
			
			plugin.setItemStack(item.getUniqueId(), null);
			plugin.sqlManager.removeAuctionItem(item);
			list.remove(index);
			
			player.sendMessage("§a▒§r 등록된 아이템을 수거했습니다.");
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
