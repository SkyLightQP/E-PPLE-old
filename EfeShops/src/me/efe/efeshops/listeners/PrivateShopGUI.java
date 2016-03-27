package me.efe.efeshops.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.efe.efecommunity.Post;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;
import me.efe.efeshops.EfeShops;
import me.efe.unlimitedrpg.unlimitedtag.TagType;
import me.efe.unlimitedrpg.unlimitedtag.UnlimitedTagAPI;
import mkremins.fanciful.FancyMessage;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PrivateShopGUI implements Listener {
	public EfeShops plugin;
	public List<String> users = new ArrayList<String>();
	public HashMap<String, ShopData> userData= new HashMap<String, ShopData>();
	
	public PrivateShopGUI(EfeShops plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p, ShopData data) {
		Inventory inv = plugin.getServer().createInventory(null, 9*5, "§2▒§r 개인 상점 §2["+plugin.vault.getBalance(p)+"E]");
		
		updateInv(p, inv, data);
		
		p.openInventory(inv);
		users.add(p.getName());
		userData.put(p.getName(), data);
	}
	
	public void updateInv(Player p, Inventory inv, ShopData data) {
		for (int i = 0; i < inv.getSize(); i ++) {
			if (inv.getItem(i) != null && inv.getItem(i).getType().equals(Material.SKULL_ITEM)) continue;
			
			inv.setItem(i, null);
		}
		
		for (int i = 0; i < 9; i ++) 
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3), new String[]{}));
		for (int i = 36; i < 45; i ++) 
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3), new String[]{}));
		
		if (data.isBuying()) {
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(data.getOwner().getName());
			
			skull.setItemMeta(meta);
			
			inv.setItem(4, plugin.util.createDisplayItem("§a"+data.getOwner().getName()+"§r님의 §b<삽니다>§r 상점", skull, data.getLines()));
			
			for (ItemStack i : data.getItems()) {
				ItemStack item = i.clone();
				
				int amount = getItemAmount(p, item);
				
				if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
					plugin.util.addLore(item, "");
				
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				item.setItemMeta(itemMeta);
				
				plugin.util.addLore(item, "§d§m==================");
				plugin.util.addLore(item, "§3소지수: §b"+amount+"개");
				
				if (data.getOwner().equals(p)) {
					plugin.util.addLore(item, "§3가격: §b§l+"+data.getPrice(i)+"E§3에 구매중");
					plugin.util.addLore(item, "");
					plugin.util.addLore(item, "§9가격 변경: §n우클릭");
					plugin.util.addLore(item, "§9품목 삭제: §n우클릭 + Shift");
				} else {
					if (amount > 0) plugin.util.addLore(item, "§3가격: §b§l+"+data.getPrice(i)+"E");
					else 			plugin.util.addLore(item, "§4가격: §c§l+"+data.getPrice(i)+"E");
					plugin.util.addLore(item, "");
					plugin.util.addLore(item, "§9클릭으로 아이템을 §c§n판매§9합니다.");
				}
				
				inv.setItem(inv.firstEmpty(), item);
			}
			
			if (data.getOwner().equals(p)) {
				inv.setItem(35, plugin.util.createDisplayItem("§a§nHow to use?", new ItemStack(Material.NETHER_STAR), new String[]{
					"§b품목 등록: §9§n내 인벤토리§9에서 아이템 §n클릭", 
					"§b가격 변경: §9아이템 §n우클릭", 
					"§b품목 삭제: §9아이템 §n우클릭 + Shift", 
					"", 
					"§b상점 제거: §9우클릭해주세요!"}));
			}
		} else {
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(data.getOwner().getName());
			
			skull.setItemMeta(meta);
			
			inv.setItem(4, plugin.util.createDisplayItem("§a"+data.getOwner().getName()+"§r님의 §b<팝니다>§r 상점", skull, data.getLines()));
			
			for (ItemStack i : data.getItems()) {
				ItemStack item = i.clone();
				
				int amount = getItemAmount(p, item);
				
				if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
					plugin.util.addLore(item, "");
				
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				item.setItemMeta(itemMeta);
				
				plugin.util.addLore(item, "§d§m==================");
				
				if (data.getOwner().equals(p)) {
					plugin.util.addLore(item, "§3재고: §b"+item.getAmount()+"개");
					plugin.util.addLore(item, "§3가격: §b§l-"+data.getPrice(i)+"E§3에 판매중");
					plugin.util.addLore(item, "");
					plugin.util.addLore(item, "§9가격 변경: §n우클릭");
					plugin.util.addLore(item, "§9품목 삭제: §n우클릭 + Shift");
				} else {
					plugin.util.addLore(item, "§3소지수: §b"+amount+"개");
					if (plugin.vault.hasEnough(p, data.getPrice(i))) plugin.util.addLore(item, "§3가격: §b§l-"+data.getPrice(i)+"E");
					else 											 plugin.util.addLore(item, "§4가격: §c§l-"+data.getPrice(i)+"E");
					plugin.util.addLore(item, "");
					plugin.util.addLore(item, "§9클릭으로 아이템을 §c§n구매§9합니다.");
				}
				
				inv.setItem(inv.firstEmpty(), item);
			}
			
			if (data.getOwner().equals(p)) {
				inv.setItem(35, plugin.util.createDisplayItem("§a§nHow to use?", new ItemStack(Material.SIGN), new String[]{
					"§b품목 등록: §9§n내 인벤토리§9에서 아이템 §n클릭", 
					"§b가격 변경: §9아이템 §n우클릭", 
					"§b품목 삭제: §9아이템 §n우클릭 + Shift", 
					"", 
					"§b상점 제거: §9우클릭해주세요!"}));
			}
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
		
		Player p = (Player) e.getWhoClicked();
		ShopData data = userData.get(p.getName());
		
		Material type = e.getCurrentItem().getType();
		if (e.getRawSlot() >= 45 && (type == Material.BOAT || type == Material.COMPASS || type == Material.NETHER_STAR)) {
			return;
		}
		
		if (UnlimitedTagAPI.hasTag(e.getCurrentItem(), TagType.VESTED) || UnlimitedTagAPI.hasTag(e.getCurrentItem(), TagType.LOCKED) || UnlimitedTagAPI.hasTag(e.getCurrentItem(), TagType.STAMPED)) {
			return;
		}
		
		if (e.getRawSlot() == 35 && e.isRightClick() && data.getOwner().equals(p)) {
			if (!data.getItems().isEmpty()) {
				p.sendMessage("§c▒§r 품목을 모두 제거한 뒤 이용해주세요!");
				return;
			}
			
			if (!plugin.util.containsLore(e.getCurrentItem().clone(), "§c제거하려면 다시 한 번 클릭해주세요.")) {
				plugin.util.addLore(e.getCurrentItem(), "§c제거하려면 다시 한 번 클릭해주세요.");
				
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				p.sendMessage("§a▒§r 정말로 상점을 제거하시겠습니까?");
				return;
			}
			
			p.closeInventory();
			
			if (data.getChest() != null) {
				data.getChest().getBlock().getWorld().playEffect(data.getChest().getBlock().getLocation(), Effect.STEP_SOUND, Material.CHEST);
				
				if (data.isBuying()) data.getChest().getBlock().getWorld().dropItem(data.getChest().getBlock().getLocation(), plugin.buyShop);
				else 				 data.getChest().getBlock().getWorld().dropItem(data.getChest().getBlock().getLocation(), plugin.sellShop);
				
				plugin.privateShop.getShopSign(data.getChest().getBlock()).getBlock().setType(Material.AIR);
				data.getChest().getInventory().clear();
				data.getChest().getBlock().setType(Material.AIR);
			}
			
			if (data.getMerchant() != null && !data.getMerchant().isDead()) {
				plugin.privateShop.removeMerchant(data.getMerchant());
				
				if (data.isBuying()) {
					if (p.getInventory().firstEmpty() == -1)
						p.getWorld().dropItem(p.getLocation(), plugin.buyShop);
					else
						p.getInventory().addItem(plugin.buyShop);
				} else {
					if (p.getInventory().firstEmpty() == -1)
						p.getWorld().dropItem(p.getLocation(), plugin.sellShop);
					else
						p.getInventory().addItem(plugin.sellShop);
				}
				
				p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
			}
			
			p.sendMessage("§a▒§r 상점이 제거되었습니다.");
			return;
		}
		
		if (data.isBuying()) {
			if (data.getOwner().equals(p)) {
				if (e.getRawSlot() >= 45) {
					final ItemStack item = e.getCurrentItem();
					final List<Integer> list = new ArrayList<Integer>();
					
					p.closeInventory();
					
					AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent event) {
							if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
								event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								if (list.isEmpty())
									event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
								else
									event.getPlayer().sendMessage("§c▒§r 개수는 자연수만 입력할 수 있습니다!");
								
								return;
							}
							
							int amount = Integer.parseInt(event.getName());
							
							if (amount <= 0) {
								if (list.isEmpty())
									event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
								else
									event.getPlayer().sendMessage("§c▒§r 개수는 자연수만 입력할 수 있습니다!");
								
								return;
							}
							
							if (list.isEmpty()) {
								list.add(amount);
								
								event.getPlayer().sendMessage("§a▒§r 개당 가격이 설정되었습니다.");
								event.getPlayer().sendMessage("§a▒§r 지우고 최대 구매 개수를 입력해주세요!");
								return;
							}
							
							int price = list.get(0);
							
							ItemStack add = item.clone();
							add.setAmount(amount);
							
							ShopData data = userData.get(event.getPlayer().getName());
							data.addItem(add, price);
							
							event.getPlayer().sendMessage("§a▒§r 품목이 등록되었습니다!");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = item.clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("개당 가격");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "§c오른쪽 버튼을 클릭해주세요.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("§a▒§r 개당 구매 가격을 입력해주세요!");
				} else if (9 <= e.getRawSlot() && e.getRawSlot() < 35 && e.isRightClick()) {
					final ItemStack item = data.getItems().get(e.getRawSlot() - 9);
					
					if (e.isShiftClick()) {
						data.removeItem(item);
						
						updateInv(p, e.getInventory(), data);
						p.sendMessage("§a▒§r 구매중인 품목이 제거되었습니다.");
						return;
					}
					
					p.closeInventory();
					
					final List<Integer> list = new ArrayList<Integer>();
					
					AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent event) {
							if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
								event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								if (list.isEmpty())
									event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
								else
									event.getPlayer().sendMessage("§c▒§r 개수는 자연수만 입력할 수 있습니다!");
								
								return;
							}
							
							int amount = Integer.parseInt(event.getName());
							
							if (amount <= 0) {
								if (list.isEmpty())
									event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
								else
									event.getPlayer().sendMessage("§c▒§r 개수는 자연수만 입력할 수 있습니다!");
								
								return;
							}
							
							if (list.isEmpty()) {
								list.add(amount);
								
								event.getPlayer().sendMessage("§a▒§r 개당 가격이 설정되었습니다.");
								event.getPlayer().sendMessage("§a▒§r 지우고 최대 구매 개수를 입력해주세요!");
								return;
							}
							
							int price = list.get(0);
							
							ItemStack replace = item.clone();
							replace.setAmount(amount);
							
							ShopData data = userData.get(event.getPlayer().getName());
							data.replaceItem(item, replace);
							data.setPrice(replace, price);
							
							event.getPlayer().sendMessage("§a▒§r 품목 정보가 수정되었습니다!");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = e.getCurrentItem().clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("개당 가격");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "§c오른쪽 버튼을 클릭해주세요.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("§a▒§r 개당 구매 가격을 입력해주세요!");
				}
			} else {
				if (e.getRawSlot() >= 45) return;
				
				final ItemStack item = data.getItems().get(e.getRawSlot() - 9);
				
				if (hasItem(p, item, 1)) {
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
							
							if (amount <= 0) {
								event.getPlayer().sendMessage("§c▒§r 판매 개수는 자연수만 입력할 수 있습니다!");
								return;
							}
							
							if (amount > item.clone().getAmount()) {
								event.getPlayer().sendMessage("§c▒§r 구매중인 아이템 개수보다 많이 판매할 수 없습니다!");
								event.getPlayer().sendMessage("§c▒§r 현재 해당 아이템을 최대 §e"+item.clone().getAmount()+"§r개 구매중입니다.");
								return;
							}
							
							if (amount > getItemAmount(event.getPlayer(), item)) {
								event.getPlayer().sendMessage("§c▒§r 소지중인 아이템 개수보다 많이 판매할 수 없습니다!");
								event.getPlayer().sendMessage("§c▒§r 현재 해당 아이템을 §e"+getItemAmount(event.getPlayer(), item)+"§r개 소지중입니다.");
								return;
							}
							
							ShopData data = userData.get(event.getPlayer().getName());
							
							int price = data.getPrice(item) * amount;
							
							if (price > plugin.vault.getBalance(data.getOwner())) {
								event.getPlayer().sendMessage("§c▒§r 구매자의 소지금이 부족해 아이템을 판매할 수 없습니다!");
								return;
							}
							
							data.updateAmount(item, amount);
							
							removeItem(event.getPlayer(), item, amount);
							
							ItemStack give = item.clone();
							give.setAmount(amount);
							
							Post post = Post.getBuilder()
									.setSender("개인 상점")
									.setMessage("§a구매한 아이템", "유저가 판매한 아이템이 배달되었습니다.")
									.setItems(new ItemStack[]{give})
									.build();
							Post.sendPost(data.getOwner(), post);
							
							plugin.vault.give(event.getPlayer(), price);
							plugin.vault.take(data.getOwner(), price);
							
							event.getPlayer().sendMessage("§a▒§r 아이템을 판매했습니다! §a[+"+price+"E]");
							event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
							
							if (data.getOwner().isOnline()) {
								new FancyMessage("§a▒§r "+event.getPlayer().getName()+"님께서 당신에게 아이템을 판매하셨습니다! §c[-"+price+"E] ")
								.then("§b§n/우편함")
									.command("/우편함")
									.tooltip("§b/우편함")
								.send(data.getOwner().getPlayer());
							}
							
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
					
					p.sendMessage("§a▒§r 판매할 개수를 입력해주세요!");
				} else {
					p.sendMessage("§c▒§r 소지한 아이템이 부족합니다!");
				}
			}
		} else {
			if (data.getOwner().equals(p)) {
				if (e.getRawSlot() >= 45) {
					final ItemStack item = e.getCurrentItem();
					
					p.closeInventory();
					
					AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent event) {
							if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
								event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
								return;
							}
							
							int price = Integer.parseInt(event.getName());
							
							if (price <= 0) {
								event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
								return;
							}
							
							ShopData data = userData.get(event.getPlayer().getName());
							data.addItem(item.clone(), price);
							
							event.getPlayer().getInventory().removeItem(item);
							
							event.getPlayer().sendMessage("§a▒§r 품목이 등록되었습니다!");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = e.getCurrentItem().clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("개당 가격");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "§c오른쪽 버튼을 클릭해주세요.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("§a▒§r 판매할 가격을 입력해주세요!");
				} else if (9 <= e.getRawSlot() && e.getRawSlot() < 35 && e.isRightClick()) {
					final ItemStack item = data.getItems().get(e.getRawSlot() - 9);
					
					if (e.isShiftClick()) {
						Post post = Post.getBuilder()
								.setSender("§a개인 상점")
								.setMessage("판매 중단 아이템", "개인 상점에서 삭제하신 품목이 배달되었습니다.")
								.setItems(new ItemStack[]{item})
								.build();
						Post.sendPost(p, post);
						
						data.removeItem(item);
						
						updateInv(p, e.getInventory(), data);
						p.sendMessage("§a▒§r 판매중인 품목이 제거되었습니다.");
						p.sendMessage("§a▒§r 판매가 중단된 아이템이 우편함으로 전송되었습니다!");
						
						new FancyMessage("§a▒§r ")
						.then("§b§n/우편함")
							.command("/우편함")
							.tooltip("§b/우편함")
						.send(data.getOwner().getPlayer());
						
						return;
					}
					
					p.closeInventory();
					
					AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent event) {
							if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
								event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
								return;
							}
							
							int price = Integer.parseInt(event.getName());
							
							if (price <= 0) {
								event.getPlayer().sendMessage("§c▒§r 가격은 자연수만 입력할 수 있습니다!");
								return;
							}
							
							ShopData data = userData.get(event.getPlayer().getName());
							data.setPrice(item, price);
							
							event.getPlayer().sendMessage("§a▒§r 가격이 변경되었습니다!");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = e.getCurrentItem().clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("개당 가격");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "§c오른쪽 버튼을 클릭해주세요.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("§a▒§r 개당 판매 가격을 입력해주세요!");
				}
			} else {
				if (e.getRawSlot() >= 45) return;
				
				if (p.getInventory().firstEmpty() == -1) {
					p.sendMessage("§c▒§r 인벤토리가 가득해 아이템을 구입할 수 없습니다.");
					return;
				}
				
				final ItemStack item = data.getItems().get(e.getRawSlot() - 9);
				
				if (plugin.vault.hasEnough(p, data.getPrice(item))) {
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
							
							if (amount <= 0) {
								event.getPlayer().sendMessage("§c▒§r 구매 개수는 자연수만 입력할 수 있습니다!");
								return;
							}
							
							if (amount > item.clone().getAmount()) {
								event.getPlayer().sendMessage("§c▒§r 판매중인 아이템 개수보다 많이 구매할 수 없습니다!");
								event.getPlayer().sendMessage("§c▒§r 현재 해당 아이템은 §e"+item.getAmount()+"§r개 판매중입니다.");
								return;
							}
							
							ShopData data = userData.get(event.getPlayer().getName());
							
							int price = data.getPrice(item) * amount;
							
							if (price > plugin.vault.getBalance(event.getPlayer())) {
								event.getPlayer().sendMessage("§c▒§r 소지금이 부족해 아이템을 구매할 수 없습니다!");
								return;
							}
							
							data.updateAmount(item, amount);
							
							ItemStack give = item.clone();
							give.setAmount(amount);
							
							event.getPlayer().getInventory().addItem(give);
							
							plugin.vault.give(data.getOwner(), price);
							plugin.vault.take(event.getPlayer(), price);
							
							event.getPlayer().sendMessage("§a▒§r 아이템을 구매했습니다! §c[-"+price+"E]");
							event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
							
							if (data.getOwner().isOnline())
								data.getOwner().getPlayer().sendMessage("§a▒§r "+event.getPlayer().getName()+"님께서 당신의 아이템을 구매하셨습니다! §a[+"+price+"E]");
							
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
					
					p.sendMessage("§a▒§r 구매할 개수를 입력해주세요!");
				} else {
					p.sendMessage("§c▒§r 소지금이 부족해 구매할 수 없습니다!");
				}
			}
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
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
