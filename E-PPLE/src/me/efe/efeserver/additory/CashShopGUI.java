package me.efe.efeserver.additory;

import java.util.ArrayList;
import java.util.List;

import me.efe.efecommunity.Post;
import me.efe.efeitems.ItemStorage;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.util.CashAPI;
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

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CashShopGUI implements Listener {
	public EfeServer plugin;
	public List<String> users = new ArrayList<String>();
	
	public CashShopGUI(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(p, 9*6, "��1�ơ�r ���μ� ��9["+CashAPI.getBalance(p)+"����]");
		
		if (PermissionsEx.getUser(p).inGroup("appler") || PermissionsEx.getUser(p).inGroup("golden_appler")) {
			inv.setItem(12, plugin.util.createDisplayItem("��c��lAppler", new ItemStack(Material.APPLE), new String[]{
				"- ������ �Ŀ��� ��ũ",
				"- ä�ý� ��c*��r ���λ�",
				"- (����) ���� ��õ�� 100���� ȹ�� ����",
				"- '/�ɱ�' ��ɾ�",
				"- '���� ��Ų <����>' ����",
				"- '������ ���� Īȣ' ����",
				"",
				"��9������ ǰ���Դϴ�."}));
		} else {
			inv.setItem(12, plugin.util.createDisplayItem("��c��lAppler", new ItemStack(Material.APPLE), new String[]{
				"- ������ �Ŀ��� ��ũ",
				"- ä�ý� ��c*��r ���λ�",
				"- (����) ���� ��õ�� 100���� ȹ�� ����",
				"- '/�ɱ�' ��ɾ�",
				"- '���� ��Ų <����>' ����",
				"- '������ ���� Īȣ' ����",
				"",
				"��94900 Coins"}));
		}
		
		if (PermissionsEx.getUser(p).inGroup("golden_appler")) {
			inv.setItem(14, plugin.util.createDisplayItem("��e��lGolden Appler", new ItemStack(Material.GOLDEN_APPLE), new String[]{
				"- ������ �Ŀ��� ��ũ",
				"- ä�ý� ��c*��r ���λ�",
				"- (����) ���� ��õ�� 150���� ȹ�� ����",
				"- '/�ɱ�' ��ɾ�",
				"- ǥ���ǿ� �� �ڵ� ��� ����",
				"- '���� ��Ų <����>' ����",
				"- '���� ��Ų <���� ��Ƽ>' ����",
				"- '������ ���� Īȣ' ����",
				"",
				"��9������ ǰ���Դϴ�."}));
			
		} else if (PermissionsEx.getUser(p).inGroup("appler")) {
			inv.setItem(14, plugin.util.createDisplayItem("��e��lGolden Appler", new ItemStack(Material.GOLDEN_APPLE), new String[]{
				"- ������ �Ŀ��� ��ũ",
				"- (����) ���� ��õ�� 150���� ȹ�� ����",
				"- ǥ���ǿ� �� �ڵ� ��� ����",
				"- '���� ��Ų <���� ��Ƽ>' ����",
				"",
				"��94000 Coins To UPGRADE"}));
		} else {
			inv.setItem(14, plugin.util.createDisplayItem("��e��lGolden Appler", new ItemStack(Material.GOLDEN_APPLE), new String[]{
				"- ������ �Ŀ��� ��ũ",
				"- ä�ý� ��c*��r ���λ�",
				"- (����) ���� ��õ�� 150���� ȹ�� ����",
				"- '/�ɱ�' ��ɾ�",
				"- ǥ���ǿ� �� �ڵ� ��� ����",
				"- '���� ��Ų <����>' ����",
				"- '���� ��Ų <���� ��Ƽ>' ����",
				"- '������ ���� Īȣ' ����",
				"",
				"��98900 Coins"}));
		}
		
		
		inv.setItem(28, price(ItemStorage.BLOOD_SKIN_BOX.clone(), 900));
		inv.setItem(29, price(ItemStorage.BLOOD_SKIN_BOX_PREMIUM.clone(), 1800));
		inv.setItem(30, price(ItemStorage.RANDOM_TITLE_BOOK.clone(), 1300));
		//inv.setItem(31, price(ItemStorage.SPAWN_EGG_VILLAGER.clone(), 900)); //TODO Update
		
		inv.setItem(7, plugin.util.createDisplayItem("��6/����", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		inv.setItem(8, plugin.util.createDisplayItem("��e/���θ޴�", new ItemStack(Material.NETHER_STAR), new String[]{}));
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	public void openBuyGUI(Player p, ItemStack item, int price) {
		Inventory inv = plugin.getServer().createInventory(p, 9*6, "��1�ơ�r ǰ�� ����");
		int balance = CashAPI.getBalance(p);
		
		inv.setItem(4, plugin.util.createDisplayItem("��e����:��6 "+price+" ����", new ItemStack(Material.GOLD_INGOT), new String[]{
			"��e���� ��:��6 "+balance+" ����", "��e���� ��:��6 "+(balance - price)+" ����"}));
		inv.setItem(22, item);
		
		for (int i : new int[]{27, 28, 29, 36, 37, 38, 45, 46, 47}) {
			inv.setItem(i, plugin.util.createDisplayItem("��a����", new ItemStack(Material.EMERALD_BLOCK), new String[]{}));
		}
		
		for (int i : new int[]{33, 34, 35, 42, 43, 44, 51, 52, 53}) {
			inv.setItem(i, plugin.util.createDisplayItem("��c���", new ItemStack(Material.REDSTONE_BLOCK), new String[]{}));
		}
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	private ItemStack price(ItemStack item, int price) {
		UnlimitedTagAPI.addTag(item, TagType.VEST_ON_PICKUP, null);
		
		plugin.util.addLore(item, "");
		plugin.util.addLore(item, "��7��9"+price+" Coins");
		
		return item;
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 9*6) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getInventory().getTitle().startsWith("��1�ơ�r ���μ�")) {
			if (e.getRawSlot() == 7) {
				p.closeInventory();
				plugin.cashGui.openGUI(p);
				return;
			} else if (e.getRawSlot() == 8) {
				p.closeInventory();
				plugin.mainGui.openGUI(p);
				return;
			}
			
			ItemStack item = e.getCurrentItem().clone();
			int price = getPrice(item);
			
			if (price == -1)
				return;
			
			if (!CashAPI.hasEnough(p, price)) {
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				p.sendMessage("��c�ơ�r �������� ������ �����մϴ�.");
				return;
			}
			
			users.remove(p.getName());
			p.closeInventory();
			
			openBuyGUI(p, item, price);
			
		} else if (e.getInventory().getTitle().startsWith("��1�ơ�r ǰ�� ����")) {
			ItemStack icon = e.getCurrentItem().clone();
			ItemStack item = e.getInventory().getItem(22).clone();
			
			int price = getPrice(item);
			
			if (icon.getType() == Material.EMERALD_BLOCK) {
				users.remove(p.getName());
				
				CashAPI.withdraw(p, price);
				
				String display = item.getItemMeta().getDisplayName().substring(2);
				switch (display) {
				case "��lAppler":
					CashAPI.logPurchase(p, "Appler Rank", price);
					
					ItemStack item0 = ItemStorage.createSkinBook("apple");
					ItemStack item1 = ItemStorage.RANDOM_TITLE_BOOK.clone();
					
					UnlimitedTagAPI.addTag(item0, TagType.VESTED, p.getUniqueId().toString());
					UnlimitedTagAPI.addTag(item1, TagType.VESTED, p.getUniqueId().toString());
					
					giveItem(p, item0);
					giveItem(p, item1);
					
					PermissionsEx.getUser(p).addGroup("appler");
					break;
				case "��lGolden Appler":
					CashAPI.logPurchase(p, "Golden Appler Rank", price);
					
					ItemStack item2 = ItemStorage.createSkinBook("apple");
					ItemStack item3 = ItemStorage.createSkinBook("apple_party");
					ItemStack item4 = ItemStorage.RANDOM_TITLE_BOOK.clone();
					
					UnlimitedTagAPI.addTag(item2, TagType.VESTED, p.getUniqueId().toString());
					UnlimitedTagAPI.addTag(item3, TagType.VESTED, p.getUniqueId().toString());
					UnlimitedTagAPI.addTag(item4, TagType.VESTED, p.getUniqueId().toString());
					
					if (!PermissionsEx.getUser(p).inGroup("appler")) {
						giveItem(p, item2);
						giveItem(p, item4);
						
						PermissionsEx.getUser(p).removeGroup("appler");
					}
					
					giveItem(p, item3);
					
					PermissionsEx.getUser(p).addGroup("golden_appler");
					break;
				default:
					String name = item.getItemMeta().getDisplayName();
					ItemStack bought;
					
					if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX))) {
						bought = ItemStorage.BLOOD_SKIN_BOX.clone();
					} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_PREMIUM))) {
						bought = ItemStorage.BLOOD_SKIN_BOX_PREMIUM.clone();
					} else if (name.equals(ItemStorage.getName(ItemStorage.RANDOM_TITLE_BOOK))) {
						bought = ItemStorage.RANDOM_TITLE_BOOK.clone();
					} else if (name.equals(ItemStorage.getName(ItemStorage.SPAWN_EGG_VILLAGER))) {
						bought = ItemStorage.SPAWN_EGG_VILLAGER.clone();
					} else {
						p.closeInventory();
						p.sendMessage("��c�ơ�r ó�� ���� �� ������ �߻��߽��ϴ�. �����ڿ��� �������ּ���.");
						CashAPI.log("[Error] Can't load item '"+name+"'! "+price+" coins of "+p.getName()+" ("+p.getUniqueId().toString()+") was taken wrongly.");
						return;
					}
					
					
					UnlimitedTagAPI.addTag(bought, TagType.VESTED, p.getUniqueId().toString());
					giveItem(p, bought);
				}
				
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
				p.sendMessage("��a�ơ�r ���� ǰ���� �����߽��ϴ�! �̿����ּż� �����մϴ�.");
				
				p.closeInventory();
				openGUI(p);
			} else if (icon.getType() == Material.REDSTONE_BLOCK) {
				p.closeInventory();
				openGUI(p);
			}
		}
	}
	
	private int getPrice(ItemStack icon) {
		for (String str : icon.getItemMeta().getLore()) {
			if (str.contains(" Coins")) {
				return Integer.parseInt(str.substring(4, str.indexOf(" Coins")));
			}
		}
		
		return -1;
	}
	
	private void giveItem(Player player, ItemStack item) {
		if (player.getInventory().firstEmpty() == -1) {
			Post post = Post.getBuilder()
					.setSender(player.getName())
					.setMessage("��a���η� ������ ������", "�κ��丮�� ���� �� �߼۵Ǿ����ϴ�.")
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
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}