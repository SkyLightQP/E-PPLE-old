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
		Inventory inv = plugin.getServer().createInventory(p, 9*6, "§1▒§r 코인샵 §9["+CashAPI.getBalance(p)+"코인]");
		
		if (PermissionsEx.getUser(p).inGroup("appler") || PermissionsEx.getUser(p).inGroup("golden_appler")) {
			inv.setItem(12, plugin.util.createDisplayItem("§c§lAppler", new ItemStack(Material.APPLE), new String[]{
				"- 무기한 후원자 랭크",
				"- 채팅시 §c*§r 접두사",
				"- (매일) 서버 추천시 100코인 획득 가능",
				"- '/앉기' 명령어",
				"- '블러드 스킨 <애플>' 지급",
				"- '무작위 조합 칭호' 지급",
				"",
				"§9구매한 품목입니다."}));
		} else {
			inv.setItem(12, plugin.util.createDisplayItem("§c§lAppler", new ItemStack(Material.APPLE), new String[]{
				"- 무기한 후원자 랭크",
				"- 채팅시 §c*§r 접두사",
				"- (매일) 서버 추천시 100코인 획득 가능",
				"- '/앉기' 명령어",
				"- '블러드 스킨 <애플>' 지급",
				"- '무작위 조합 칭호' 지급",
				"",
				"§94900 Coins"}));
		}
		
		if (PermissionsEx.getUser(p).inGroup("golden_appler")) {
			inv.setItem(14, plugin.util.createDisplayItem("§e§lGolden Appler", new ItemStack(Material.GOLDEN_APPLE), new String[]{
				"- 무기한 후원자 랭크",
				"- 채팅시 §c*§r 접두사",
				"- (매일) 서버 추천시 150코인 획득 가능",
				"- '/앉기' 명령어",
				"- 표지판에 색 코드 사용 가능",
				"- '블러드 스킨 <애플>' 지급",
				"- '블러드 스킨 <애플 파티>' 지급",
				"- '무작위 조합 칭호' 지급",
				"",
				"§9구매한 품목입니다."}));
			
		} else if (PermissionsEx.getUser(p).inGroup("appler")) {
			inv.setItem(14, plugin.util.createDisplayItem("§e§lGolden Appler", new ItemStack(Material.GOLDEN_APPLE), new String[]{
				"- 무기한 후원자 랭크",
				"- (매일) 서버 추천시 150코인 획득 가능",
				"- 표지판에 색 코드 사용 가능",
				"- '블러드 스킨 <애플 파티>' 지급",
				"",
				"§94000 Coins To UPGRADE"}));
		} else {
			inv.setItem(14, plugin.util.createDisplayItem("§e§lGolden Appler", new ItemStack(Material.GOLDEN_APPLE), new String[]{
				"- 무기한 후원자 랭크",
				"- 채팅시 §c*§r 접두사",
				"- (매일) 서버 추천시 150코인 획득 가능",
				"- '/앉기' 명령어",
				"- 표지판에 색 코드 사용 가능",
				"- '블러드 스킨 <애플>' 지급",
				"- '블러드 스킨 <애플 파티>' 지급",
				"- '무작위 조합 칭호' 지급",
				"",
				"§98900 Coins"}));
		}
		
		
		inv.setItem(28, price(ItemStorage.BLOOD_SKIN_BOX.clone(), 900));
		inv.setItem(29, price(ItemStorage.BLOOD_SKIN_BOX_PREMIUM.clone(), 1800));
		inv.setItem(30, price(ItemStorage.RANDOM_TITLE_BOOK.clone(), 1300));
		//inv.setItem(31, price(ItemStorage.SPAWN_EGG_VILLAGER.clone(), 900)); //TODO Update
		
		inv.setItem(7, plugin.util.createDisplayItem("§6/코인", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		inv.setItem(8, plugin.util.createDisplayItem("§e/메인메뉴", new ItemStack(Material.NETHER_STAR), new String[]{}));
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	public void openBuyGUI(Player p, ItemStack item, int price) {
		Inventory inv = plugin.getServer().createInventory(p, 9*6, "§1▒§r 품목 구매");
		int balance = CashAPI.getBalance(p);
		
		inv.setItem(4, plugin.util.createDisplayItem("§e가격:§6 "+price+" 코인", new ItemStack(Material.GOLD_INGOT), new String[]{
			"§e구매 전:§6 "+balance+" 코인", "§e구매 후:§6 "+(balance - price)+" 코인"}));
		inv.setItem(22, item);
		
		for (int i : new int[]{27, 28, 29, 36, 37, 38, 45, 46, 47}) {
			inv.setItem(i, plugin.util.createDisplayItem("§a구매", new ItemStack(Material.EMERALD_BLOCK), new String[]{}));
		}
		
		for (int i : new int[]{33, 34, 35, 42, 43, 44, 51, 52, 53}) {
			inv.setItem(i, plugin.util.createDisplayItem("§c취소", new ItemStack(Material.REDSTONE_BLOCK), new String[]{}));
		}
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	private ItemStack price(ItemStack item, int price) {
		UnlimitedTagAPI.addTag(item, TagType.VEST_ON_PICKUP, null);
		
		plugin.util.addLore(item, "");
		plugin.util.addLore(item, "§7§9"+price+" Coins");
		
		return item;
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 9*6) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getInventory().getTitle().startsWith("§1▒§r 코인샵")) {
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
				p.sendMessage("§c▒§r 소지중인 코인이 부족합니다.");
				return;
			}
			
			users.remove(p.getName());
			p.closeInventory();
			
			openBuyGUI(p, item, price);
			
		} else if (e.getInventory().getTitle().startsWith("§1▒§r 품목 구매")) {
			ItemStack icon = e.getCurrentItem().clone();
			ItemStack item = e.getInventory().getItem(22).clone();
			
			int price = getPrice(item);
			
			if (icon.getType() == Material.EMERALD_BLOCK) {
				users.remove(p.getName());
				
				CashAPI.withdraw(p, price);
				
				String display = item.getItemMeta().getDisplayName().substring(2);
				switch (display) {
				case "§lAppler":
					CashAPI.logPurchase(p, "Appler Rank", price);
					
					ItemStack item0 = ItemStorage.createSkinBook("apple");
					ItemStack item1 = ItemStorage.RANDOM_TITLE_BOOK.clone();
					
					UnlimitedTagAPI.addTag(item0, TagType.VESTED, p.getUniqueId().toString());
					UnlimitedTagAPI.addTag(item1, TagType.VESTED, p.getUniqueId().toString());
					
					giveItem(p, item0);
					giveItem(p, item1);
					
					PermissionsEx.getUser(p).addGroup("appler");
					break;
				case "§lGolden Appler":
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
						p.sendMessage("§c▒§r 처리 과정 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
						CashAPI.log("[Error] Can't load item '"+name+"'! "+price+" coins of "+p.getName()+" ("+p.getUniqueId().toString()+") was taken wrongly.");
						return;
					}
					
					
					UnlimitedTagAPI.addTag(bought, TagType.VESTED, p.getUniqueId().toString());
					giveItem(p, bought);
				}
				
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
				p.sendMessage("§a▒§r 코인 품목을 구매했습니다! 이용해주셔서 감사합니다.");
				
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
					.setMessage("§a코인로 구매한 아이템", "인벤토리가 가득 차 발송되었습니다.")
					.setItems(new ItemStack[]{item.clone()})
					.build();
			Post.sendPost(player, post);
			
			new FancyMessage("§a▒§r 인벤토리가 가득 차")
			.then("§b§n우편함§r")
				.command("/우편함")
				.tooltip("§b/우편함")
			.then("으로 아이템이 발송되었습니다.")
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