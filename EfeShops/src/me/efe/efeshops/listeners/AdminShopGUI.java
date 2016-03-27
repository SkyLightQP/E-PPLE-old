package me.efe.efeshops.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.efe.efecommunity.Post;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;
import me.efe.efeshops.EfeShops;
import mkremins.fanciful.FancyMessage;

public class AdminShopGUI implements Listener {
	public EfeShops plugin;
	public HashMap<String, Integer> users = new HashMap<String, Integer>();
	public List<Integer> buySlotList;
	public List<Integer> sellSlotList;
	
	public AdminShopGUI(EfeShops plugin) {
		this.plugin = plugin;
		
		buySlotList = new ArrayList<Integer>();
		buySlotList.add(9);
		buySlotList.add(18);
		buySlotList.add(27);
		buySlotList.add(36);
		buySlotList.add(45);
		buySlotList.add(11);
		buySlotList.add(20);
		buySlotList.add(29);
		buySlotList.add(38);
		buySlotList.add(47);
		
		sellSlotList = new ArrayList<Integer>();
		sellSlotList.add(15);
		sellSlotList.add(24);
		sellSlotList.add(33);
		sellSlotList.add(42);
		sellSlotList.add(51);
		sellSlotList.add(17);
		sellSlotList.add(26);
		sellSlotList.add(35);
		sellSlotList.add(44);
		sellSlotList.add(53);
	}
	
	public void openGUI(Player p, NPC npc) {
		String balance = "§2["+plugin.vault.getBalance(p)+"E]";
		
		if (plugin.getConfig().contains(npc.getId()+".currency")) {
			int amount = getItemAmount(p, plugin.getConfig().getItemStack(npc.getId()+".currency").clone());
			balance = "§6["+amount+" Tokens]";
		}
		
		Inventory inv = plugin.getServer().createInventory(p, 9*6, "§2▒§r "+npc.getFullName().substring(11)+" "+balance);
		
		users.put(p.getName(), npc.getId());
		
		refresh(inv, p);
		p.openInventory(inv);
	}
	
	public void refresh(Inventory inv, Player p) {
		inv.clear();
		
		boolean hasCurrency = plugin.getConfig().contains(users.get(p.getName())+".currency");
		ItemStack currencyItem = (hasCurrency) ?
				plugin.getConfig().getItemStack(users.get(p.getName())+".currency").clone() : new ItemStack(Material.EMERALD);
		String currencyName = (hasCurrency) ? " Tokens" : "E";
		
		inv.setItem(4, plugin.util.createDisplayItem("§a§nHow to use?", plugin.util.enchant(currencyItem.clone(), Enchantment.SILK_TOUCH, 100), 
				new String[]{"§e<§6<§r 아이템 구매   ", "     아이템 판매 §6>§e>", "", "품목을 클릭하면", "아이템을 거래합니다."}));
		
		inv.setItem(13, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), new String[]{}));
		
		
		int[] buySlots = new int[]{9, 18, 27, 36, 45, 11, 20, 29, 38, 47};
		
		double minPrice = 0.0D;
		boolean noItemL = true;
		
		for (int i = 0; i < 10; i ++) {
			if (!plugin.getConfig().contains(users.get(p.getName())+".buy."+i)) continue;
			
			noItemL = false;
			
			int price = plugin.getConfig().getInt(users.get(p.getName())+".buy."+i+".price");
			ItemStack item = plugin.getConfig().getItemStack(users.get(p.getName())+".buy."+i+".item").clone();
			
			if (minPrice == 0.0D || minPrice > price)
				minPrice = price;
			
			int amount = getItemAmount(p, item);
			
			if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
				plugin.util.addLore(item, "");
			
			ItemMeta meta = item.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(meta);
			
			plugin.util.addLore(item, "§d§m==================");
			plugin.util.addLore(item, "§3소지수: §b"+amount+"개");
			
			if ((!hasCurrency && plugin.vault.hasEnough(p, price)) || (hasCurrency && hasItem(p, currencyItem, price))) {
				plugin.util.addLore(item, "§3가격: §b§l-"+price+currencyName);
				plugin.util.addLore(item, "");
				plugin.util.addLore(item, "§9클릭으로 아이템을 구매합니다.");
			} else
				plugin.util.addLore(item, "§4가격: §c§l-"+price+currencyName);
			
			inv.setItem(buySlots[i], item);
		}
		
		
		int[] sellSlots = new int[]{15, 24, 33, 42, 51, 17, 26, 35, 44, 53};
		
		boolean sellable = false;
		boolean noItemR = true;
		
		for (int i = 0; i < 10; i ++) {
			if (!plugin.getConfig().contains(users.get(p.getName())+".sell."+i)) continue;
			
			noItemR = false;
			
			int price = plugin.getConfig().getInt(users.get(p.getName())+".sell."+i+".price");
			ItemStack item = plugin.getConfig().getItemStack(users.get(p.getName())+".sell."+i+".item").clone();
			
			int amount = getItemAmount(p, item);
			
			if (amount >= item.clone().getAmount())
				sellable = true;
			
			if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
				plugin.util.addLore(item, "");
			
			ItemMeta meta = item.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(meta);
			
			plugin.util.addLore(item, "§d§m==================");
			plugin.util.addLore(item, "§3소지수: §b"+amount+"개");
			
			if (amount >= item.clone().getAmount()) {
				plugin.util.addLore(item, "§3가격: §b§l+"+price+currencyName);
				plugin.util.addLore(item, "");
				plugin.util.addLore(item, "§9클릭으로 아이템을 판매합니다.");
			} else
				plugin.util.addLore(item, "§4가격: §c§l+"+price+currencyName);
			
			inv.setItem(sellSlots[i], item);
		}
		
		
		ItemStack paneLeft = plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, 
				(short) (noItemL ? 15 : minPrice != 0.0D &&
				((!hasCurrency && plugin.vault.hasEnough(p, minPrice)) || (hasCurrency && hasItem(p, currencyItem, (int) minPrice))) ? 5 : 14)), new String[]{});
		ItemStack paneRight = plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, 
				(short) (noItemR ? 15 : sellable ? 5 : 14)), new String[]{});
		
		for (int i : new int[]{0, 1, 2, 3, 12})
			inv.setItem(i, paneLeft.clone());
		for (int i : new int[]{14, 5, 6, 7, 8})
			inv.setItem(i, paneRight.clone());
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 54) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if (buySlotList.contains(e.getRawSlot())) {
			if (p.getInventory().firstEmpty() == -1) {
				p.sendMessage("§c▒§r 인벤토리가 가득해 아이템을 구입할 수 없습니다.");
				return;
			}
			
			final boolean hasCurrency = plugin.getConfig().contains(users.get(p.getName())+".currency");
			final ItemStack currencyItem = (hasCurrency) ? plugin.getConfig().getItemStack(users.get(p.getName())+".currency").clone() : null;
			final int index = buySlotList.indexOf(e.getRawSlot());
			final int price = plugin.getConfig().getInt(users.get(p.getName())+".buy."+index+".price");
			final ItemStack item = plugin.getConfig().getItemStack(users.get(p.getName())+".buy."+index+".item");
			
			if ((!hasCurrency && plugin.vault.hasEnough(p, price)) || (hasCurrency && hasItem(p, currencyItem, price))) {
				p.closeInventory();
				
				AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
					
					@Override
					public void onAnvilClick(AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
							event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
							return;
						}
						
						if (!plugin.util.isInteger(event.getName())) {
							event.getPlayer().sendMessage("§c▒§r 구매 개수는 자연수만 입력할 수 있습니다!");
							return;
						}
						
						int amount = Integer.parseInt(event.getName());
						int amountEntire = Integer.parseInt(event.getName()) * item.clone().getAmount();
						
						if (amount <= 0) {
							event.getPlayer().sendMessage("§c▒§r 구매 개수는 자연수만 입력할 수 있습니다!");
							return;
						}
						
						int priceEntire = price * amount;
						
						if (!hasCurrency && priceEntire > plugin.vault.getBalance(event.getPlayer())) {
							event.getPlayer().sendMessage("§c▒§r 소지금이 부족해 아이템을 구매할 수 없습니다!");
							return;
						}
						
						if (hasCurrency && !hasItem(event.getPlayer(), currencyItem, priceEntire)) {
							event.getPlayer().sendMessage("§c▒§r 소지금이 부족해 아이템을 구매할 수 없습니다!");
							return;
						}
						
						ItemStack giveEntire = item.clone();
						giveEntire.setAmount(amountEntire);
						
						int amountLeft = amountEntire;
						int maxSize = giveEntire.getType().getMaxStackSize();
						
						if (amountEntire / maxSize < 1) {
							event.getPlayer().getInventory().addItem(giveEntire);
						} else {
							for (int i = 0; i < amountEntire / maxSize; i ++) {
								ItemStack give = giveEntire.clone();
								
								if (amountLeft / maxSize < 0)
									give.setAmount(amountLeft);
								else
									give.setAmount(maxSize);
								
								amountLeft -= give.clone().getAmount();
								
								if (event.getPlayer().getInventory().firstEmpty() == -1) {
									Post post = Post.getBuilder()
											.setSender("상점")
											.setMessage("§a구매한 아이템", "인벤토리가 가득 차 발송되었습니다.")
											.setItems(new ItemStack[]{give})
											.build();
									Post.sendPost(event.getPlayer(), post);
									
									new FancyMessage("§a▒§r 인벤토리가 가득 차")
									.then("§b§n우편함§r")
										.command("/우편함")
										.tooltip("§b/우편함")
									.then("으로 아이템이 발송되었습니다.")
									.send(event.getPlayer());
								} else {
									event.getPlayer().getInventory().addItem(give);
								}
							}
						}
						
						if (!hasCurrency)
							plugin.vault.take(event.getPlayer(), priceEntire);
						else
							removeItem(event.getPlayer(), currencyItem, priceEntire);
						
						event.getPlayer().sendMessage("§a▒§r 아이템을 구매했습니다! §c[-"+priceEntire+(hasCurrency ? " Tokens" : "E")+"]");
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
						
						event.setWillClose(true);
						event.setWillDestroy(true);
					}
				});
				
				ItemStack icon = e.getCurrentItem().clone();
				ItemMeta meta = icon.getItemMeta();
				
				meta.setDisplayName("구매할 개수");
				
				icon.setItemMeta(meta);
				
				plugin.util.addLore(icon, "");
				plugin.util.addLore(icon, "§c오른쪽 버튼을 클릭해주세요.");
				
				gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
				gui.open();
				
				if (item.clone().getAmount() == 1)
					p.sendMessage("§a▒§r 구매할 개수를 입력해주세요!");
				else
					p.sendMessage("§a▒§r 구매할 묶음 개수를 입력해주세요!");
			} else {
				p.sendMessage("§c▒§r 소지금이 부족해 구매할 수 없습니다!");
			}
		} else if (sellSlotList.contains(e.getRawSlot())) {
			final boolean hasCurrency = plugin.getConfig().contains(users.get(p.getName())+"currrency");
			final ItemStack currencyItem = plugin.getConfig().getItemStack(users.get(p.getName())+"currency");
			final int index = sellSlotList.indexOf(e.getRawSlot());
			final int price = plugin.getConfig().getInt(users.get(p.getName())+".sell."+index+".price");
			final ItemStack item = plugin.getConfig().getItemStack(users.get(p.getName())+".sell."+index+".item");
			
			if (hasItem(p, item, item.clone().getAmount())) {
				p.closeInventory();
				
				AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
					
					@Override
					public void onAnvilClick(AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
							event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
							return;
						}
						
						if (!plugin.util.isInteger(event.getName())) {
							event.getPlayer().sendMessage("§c▒§r 판매 개수는 자연수만 입력할 수 있습니다!");
							return;
						}
						
						int amount = Integer.parseInt(event.getName());
						int amountEntire = Integer.parseInt(event.getName()) * item.clone().getAmount();
						
						if (amount <= 0) {
							event.getPlayer().sendMessage("§c▒§r 판매 개수는 자연수만 입력할 수 있습니다!");
							return;
						}
						
						if (amountEntire > getItemAmount(event.getPlayer(), item)) {
							event.getPlayer().sendMessage("§c▒§r 소지중인 아이템 개수보다 많이 판매할 수 없습니다!");
							event.getPlayer().sendMessage("§c▒§r 현재 해당 아이템을 §e"+getItemAmount(event.getPlayer(), item)+"§r개 소지중입니다.");
							return;
						}
						
						int priceEntire = price * amount;
						
						removeItem(event.getPlayer(), item, amountEntire);
						
						if (!hasCurrency)
							plugin.vault.give(event.getPlayer(), priceEntire);
						else {
							if (event.getPlayer().getInventory().firstEmpty() == -1)
								event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), currencyItem);
							else {
								ItemStack item = currencyItem.clone();
								item.setAmount(amountEntire);
								
								event.getPlayer().getInventory().addItem(item);
							}
						}
						
						event.getPlayer().sendMessage("§a▒§r 아이템을 판매했습니다! §a[+"+priceEntire+(hasCurrency ? " Tokens" : "E")+"]");
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
						
						event.setWillClose(true);
						event.setWillDestroy(true);
					}
				});
				
				ItemStack icon = e.getCurrentItem().clone();
				ItemMeta meta = icon.getItemMeta();
				
				meta.setDisplayName("판매할 개수");
				
				icon.setItemMeta(meta);
				
				plugin.util.addLore(icon, "");
				plugin.util.addLore(icon, "§c오른쪽 버튼을 클릭해주세요.");
				
				gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
				gui.open();
				
				if (item.clone().getAmount() == 1)
					p.sendMessage("§a▒§r 판매할 개수를 입력해주세요!");
				else
					p.sendMessage("§a▒§r 판매할 묶음 개수를 입력해주세요!");
			} else {
				p.sendMessage("§c▒§r 소지한 아이템이 부족합니다!");
			}
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.containsKey(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public int getItemAmount(Player p, ItemStack target) {
		int amount = 0;
		
		for (ItemStack item : p.getInventory().getContents()) {
			if (item == null || !item.getType().equals(target.getType())) continue;
			
			if (item.isSimilar(target)) {
				amount += item.clone().getAmount();
			}
		}
		
		return amount;
	}
	
	public boolean hasItem(Player p, ItemStack item, int amount) {
		return getItemAmount(p, item.clone()) >= amount;
	}
	
	public void removeItem(Player p, ItemStack target, int amount) {
		ItemStack[] contents = p.getInventory().getContents().clone();
		
		for (int i = 0; i < contents.length; i ++) {
			ItemStack item = contents[i];
			
			if (item == null || !item.getType().equals(target.getType())) continue;
			if (!item.isSimilar(target)) continue;
			
			if (item.clone().getAmount() > amount) {
				item.setAmount(item.clone().getAmount() - amount);
				amount = 0;
			} else {
				amount -= item.clone().getAmount();
				item.setType(Material.AIR);
			}
			
			if (amount == 0) {
				break;
			}
		}
		
		p.getInventory().setContents(contents);
	}
}