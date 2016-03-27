package me.efe.efeauction.listeners;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.efe.efeauction.AuctionItem;
import me.efe.efeauction.AuctionLabel;
import me.efe.efeauction.EfeAuction;
import me.efe.efecommunity.Post;
import me.efe.efecore.util.EfeUtils;
import mkremins.fanciful.FancyMessage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AuctionMainGUI implements Listener {
	private EfeAuction plugin;
	private Set<UUID> users = new HashSet<UUID>();
	
	public AuctionMainGUI(EfeAuction plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9*6, "��2�ơ�r ���� ����");

		inv.setItem(8, EfeUtils.item.createDisplayItem("��e�˻�", new ItemStack(Material.EMPTY_MAP), null));
		
		inv.setItem(10, EfeUtils.item.createDisplayItem("��a����", new ItemStack(Material.IRON_INGOT), null));
		inv.setItem(12, EfeUtils.item.createDisplayItem("��a���۹�/����", new ItemStack(Material.BREAD), null));
		inv.setItem(14, EfeUtils.item.createDisplayItem("��a����/����", new ItemStack(Material.PORK), null));
		inv.setItem(16, EfeUtils.item.createDisplayItem("��a����ǰ/��ġǰ", new ItemStack(Material.BONE), null));
		inv.setItem(28, EfeUtils.item.createDisplayItem("��a����/���", new ItemStack(Material.POTION), null));
		inv.setItem(30, EfeUtils.item.createDisplayItem("��a����/��", new ItemStack(Material.IRON_CHESTPLATE), null));
		inv.setItem(32, EfeUtils.item.createDisplayItem("��a���� ����", new ItemStack(Material.BRICK), null));
		inv.setItem(34, EfeUtils.item.createDisplayItem("��a��Ÿ", new ItemStack(Material.BOWL), null));
		
		inv.setItem(53, EfeUtils.item.createDisplayItem("��e������ ���", new ItemStack(Material.ENCHANTED_BOOK), null));
		
		users.add(player.getUniqueId());
		player.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getUniqueId()))
			return;
		
		event.setCancelled(true);
		
		if (event.getRawSlot() >= 54 || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
			return;
		
		Player player = (Player) event.getWhoClicked();
		AuctionLabel label;
		
		switch (event.getRawSlot()) {
		case 8:
			player.closeInventory();
			
			plugin.auctionGUI.openSearchingGUI(player, null);
			return;
		case 10:
			label = AuctionLabel.MINERALS;
			break;
		case 12:
			label = AuctionLabel.FARM_PRODUCE;
			break;
		case 14:
			label = AuctionLabel.MEATS;
			break;
		case 16:
			label = AuctionLabel.MONSTER_DROPS;
			break;
		case 28:
			label = AuctionLabel.POTIONS;
			break;
		case 30:
			label = AuctionLabel.TOOLS;
			break;
		case 32:
			label = AuctionLabel.BUILDING_SUPPLIES;
			break;
		case 34:
			label = AuctionLabel.OTHERS;
			break;
		case 53:
			player.closeInventory();
			
			plugin.auctionStatusGUI.openGUI(player);
			return;
		default:
			return;
		}

		event.getInventory().clear();
		player.closeInventory();
		
		List<AuctionItem> itemList = plugin.sqlManager.getAuctionItems(label);
		
		checkDeadline(itemList);
		plugin.auctionGUI.openGUI(player, label);
	}
	
	public void checkDeadline(List<AuctionItem> itemList) {
		Date date = new Date();
		
		Iterator<AuctionItem> it = itemList.iterator();
		while (it.hasNext()) {
			AuctionItem item = it.next();
			
			long diff = date.getTime() - item.getDate().getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			if (diffDays >= 3) {
				it.remove();
				
				ItemStack itemStack = plugin.getItemStack(item.getUniqueId());
				
				Post post = Post.getBuilder()
						.setSender("���� ����")
						.setMessage("��a������ ������", "�κ��丮�� ���� �� �߼۵Ǿ����ϴ�.")
						.setItems(new ItemStack[]{itemStack})
						.build();
				Post.sendPost(item.getSeller(), post);
				
				if (item.getSeller().isOnline()) {
					new FancyMessage("��a�ơ�r ���� ������ ��� �Ⱓ�� ����Ǿ� ")
					.then("��b��n�����ԡ�r")
						.command("/������")
						.tooltip("��b/������")
					.then("���� �������� �߼۵Ǿ����ϴ�.")
					.send(item.getSeller().getPlayer());
				}
				
				plugin.setItemStack(item.getUniqueId(), null);
				plugin.sqlManager.removeAuctionItem(item);
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
}
