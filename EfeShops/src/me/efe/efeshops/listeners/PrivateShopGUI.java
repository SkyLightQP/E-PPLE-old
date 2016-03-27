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
		Inventory inv = plugin.getServer().createInventory(null, 9*5, "��2�ơ�r ���� ���� ��2["+plugin.vault.getBalance(p)+"E]");
		
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
			
			inv.setItem(4, plugin.util.createDisplayItem("��a"+data.getOwner().getName()+"��r���� ��b<��ϴ�>��r ����", skull, data.getLines()));
			
			for (ItemStack i : data.getItems()) {
				ItemStack item = i.clone();
				
				int amount = getItemAmount(p, item);
				
				if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
					plugin.util.addLore(item, "");
				
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				item.setItemMeta(itemMeta);
				
				plugin.util.addLore(item, "��d��m==================");
				plugin.util.addLore(item, "��3������: ��b"+amount+"��");
				
				if (data.getOwner().equals(p)) {
					plugin.util.addLore(item, "��3����: ��b��l+"+data.getPrice(i)+"E��3�� ������");
					plugin.util.addLore(item, "");
					plugin.util.addLore(item, "��9���� ����: ��n��Ŭ��");
					plugin.util.addLore(item, "��9ǰ�� ����: ��n��Ŭ�� + Shift");
				} else {
					if (amount > 0) plugin.util.addLore(item, "��3����: ��b��l+"+data.getPrice(i)+"E");
					else 			plugin.util.addLore(item, "��4����: ��c��l+"+data.getPrice(i)+"E");
					plugin.util.addLore(item, "");
					plugin.util.addLore(item, "��9Ŭ������ �������� ��c��n�Ǹš�9�մϴ�.");
				}
				
				inv.setItem(inv.firstEmpty(), item);
			}
			
			if (data.getOwner().equals(p)) {
				inv.setItem(35, plugin.util.createDisplayItem("��a��nHow to use?", new ItemStack(Material.NETHER_STAR), new String[]{
					"��bǰ�� ���: ��9��n�� �κ��丮��9���� ������ ��nŬ��", 
					"��b���� ����: ��9������ ��n��Ŭ��", 
					"��bǰ�� ����: ��9������ ��n��Ŭ�� + Shift", 
					"", 
					"��b���� ����: ��9��Ŭ�����ּ���!"}));
			}
		} else {
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(data.getOwner().getName());
			
			skull.setItemMeta(meta);
			
			inv.setItem(4, plugin.util.createDisplayItem("��a"+data.getOwner().getName()+"��r���� ��b<�˴ϴ�>��r ����", skull, data.getLines()));
			
			for (ItemStack i : data.getItems()) {
				ItemStack item = i.clone();
				
				int amount = getItemAmount(p, item);
				
				if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
					plugin.util.addLore(item, "");
				
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				item.setItemMeta(itemMeta);
				
				plugin.util.addLore(item, "��d��m==================");
				
				if (data.getOwner().equals(p)) {
					plugin.util.addLore(item, "��3���: ��b"+item.getAmount()+"��");
					plugin.util.addLore(item, "��3����: ��b��l-"+data.getPrice(i)+"E��3�� �Ǹ���");
					plugin.util.addLore(item, "");
					plugin.util.addLore(item, "��9���� ����: ��n��Ŭ��");
					plugin.util.addLore(item, "��9ǰ�� ����: ��n��Ŭ�� + Shift");
				} else {
					plugin.util.addLore(item, "��3������: ��b"+amount+"��");
					if (plugin.vault.hasEnough(p, data.getPrice(i))) plugin.util.addLore(item, "��3����: ��b��l-"+data.getPrice(i)+"E");
					else 											 plugin.util.addLore(item, "��4����: ��c��l-"+data.getPrice(i)+"E");
					plugin.util.addLore(item, "");
					plugin.util.addLore(item, "��9Ŭ������ �������� ��c��n���š�9�մϴ�.");
				}
				
				inv.setItem(inv.firstEmpty(), item);
			}
			
			if (data.getOwner().equals(p)) {
				inv.setItem(35, plugin.util.createDisplayItem("��a��nHow to use?", new ItemStack(Material.SIGN), new String[]{
					"��bǰ�� ���: ��9��n�� �κ��丮��9���� ������ ��nŬ��", 
					"��b���� ����: ��9������ ��n��Ŭ��", 
					"��bǰ�� ����: ��9������ ��n��Ŭ�� + Shift", 
					"", 
					"��b���� ����: ��9��Ŭ�����ּ���!"}));
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
				p.sendMessage("��c�ơ�r ǰ���� ��� ������ �� �̿����ּ���!");
				return;
			}
			
			if (!plugin.util.containsLore(e.getCurrentItem().clone(), "��c�����Ϸ��� �ٽ� �� �� Ŭ�����ּ���.")) {
				plugin.util.addLore(e.getCurrentItem(), "��c�����Ϸ��� �ٽ� �� �� Ŭ�����ּ���.");
				
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				p.sendMessage("��a�ơ�r ������ ������ �����Ͻðڽ��ϱ�?");
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
			
			p.sendMessage("��a�ơ�r ������ ���ŵǾ����ϴ�.");
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
								event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								if (list.isEmpty())
									event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								else
									event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								
								return;
							}
							
							int amount = Integer.parseInt(event.getName());
							
							if (amount <= 0) {
								if (list.isEmpty())
									event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								else
									event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								
								return;
							}
							
							if (list.isEmpty()) {
								list.add(amount);
								
								event.getPlayer().sendMessage("��a�ơ�r ���� ������ �����Ǿ����ϴ�.");
								event.getPlayer().sendMessage("��a�ơ�r ����� �ִ� ���� ������ �Է����ּ���!");
								return;
							}
							
							int price = list.get(0);
							
							ItemStack add = item.clone();
							add.setAmount(amount);
							
							ShopData data = userData.get(event.getPlayer().getName());
							data.addItem(add, price);
							
							event.getPlayer().sendMessage("��a�ơ�r ǰ���� ��ϵǾ����ϴ�!");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = item.clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("���� ����");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "��c������ ��ư�� Ŭ�����ּ���.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("��a�ơ�r ���� ���� ������ �Է����ּ���!");
				} else if (9 <= e.getRawSlot() && e.getRawSlot() < 35 && e.isRightClick()) {
					final ItemStack item = data.getItems().get(e.getRawSlot() - 9);
					
					if (e.isShiftClick()) {
						data.removeItem(item);
						
						updateInv(p, e.getInventory(), data);
						p.sendMessage("��a�ơ�r �������� ǰ���� ���ŵǾ����ϴ�.");
						return;
					}
					
					p.closeInventory();
					
					final List<Integer> list = new ArrayList<Integer>();
					
					AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent event) {
							if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
								event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								if (list.isEmpty())
									event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								else
									event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								
								return;
							}
							
							int amount = Integer.parseInt(event.getName());
							
							if (amount <= 0) {
								if (list.isEmpty())
									event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								else
									event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								
								return;
							}
							
							if (list.isEmpty()) {
								list.add(amount);
								
								event.getPlayer().sendMessage("��a�ơ�r ���� ������ �����Ǿ����ϴ�.");
								event.getPlayer().sendMessage("��a�ơ�r ����� �ִ� ���� ������ �Է����ּ���!");
								return;
							}
							
							int price = list.get(0);
							
							ItemStack replace = item.clone();
							replace.setAmount(amount);
							
							ShopData data = userData.get(event.getPlayer().getName());
							data.replaceItem(item, replace);
							data.setPrice(replace, price);
							
							event.getPlayer().sendMessage("��a�ơ�r ǰ�� ������ �����Ǿ����ϴ�!");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = e.getCurrentItem().clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("���� ����");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "��c������ ��ư�� Ŭ�����ּ���.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("��a�ơ�r ���� ���� ������ �Է����ּ���!");
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
								event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								event.getPlayer().sendMessage("��c�ơ�r �Ǹ� ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								return;
							}
							
							int amount = Integer.parseInt(event.getName());
							
							if (amount <= 0) {
								event.getPlayer().sendMessage("��c�ơ�r �Ǹ� ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								return;
							}
							
							if (amount > item.clone().getAmount()) {
								event.getPlayer().sendMessage("��c�ơ�r �������� ������ �������� ���� �Ǹ��� �� �����ϴ�!");
								event.getPlayer().sendMessage("��c�ơ�r ���� �ش� �������� �ִ� ��e"+item.clone().getAmount()+"��r�� �������Դϴ�.");
								return;
							}
							
							if (amount > getItemAmount(event.getPlayer(), item)) {
								event.getPlayer().sendMessage("��c�ơ�r �������� ������ �������� ���� �Ǹ��� �� �����ϴ�!");
								event.getPlayer().sendMessage("��c�ơ�r ���� �ش� �������� ��e"+getItemAmount(event.getPlayer(), item)+"��r�� �������Դϴ�.");
								return;
							}
							
							ShopData data = userData.get(event.getPlayer().getName());
							
							int price = data.getPrice(item) * amount;
							
							if (price > plugin.vault.getBalance(data.getOwner())) {
								event.getPlayer().sendMessage("��c�ơ�r �������� �������� ������ �������� �Ǹ��� �� �����ϴ�!");
								return;
							}
							
							data.updateAmount(item, amount);
							
							removeItem(event.getPlayer(), item, amount);
							
							ItemStack give = item.clone();
							give.setAmount(amount);
							
							Post post = Post.getBuilder()
									.setSender("���� ����")
									.setMessage("��a������ ������", "������ �Ǹ��� �������� ��޵Ǿ����ϴ�.")
									.setItems(new ItemStack[]{give})
									.build();
							Post.sendPost(data.getOwner(), post);
							
							plugin.vault.give(event.getPlayer(), price);
							plugin.vault.take(data.getOwner(), price);
							
							event.getPlayer().sendMessage("��a�ơ�r �������� �Ǹ��߽��ϴ�! ��a[+"+price+"E]");
							event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
							
							if (data.getOwner().isOnline()) {
								new FancyMessage("��a�ơ�r "+event.getPlayer().getName()+"�Բ��� ��ſ��� �������� �Ǹ��ϼ̽��ϴ�! ��c[-"+price+"E] ")
								.then("��b��n/������")
									.command("/������")
									.tooltip("��b/������")
								.send(data.getOwner().getPlayer());
							}
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = e.getCurrentItem().clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("�Ǹ��� ����");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "��c������ ��ư�� Ŭ�����ּ���.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("��a�ơ�r �Ǹ��� ������ �Է����ּ���!");
				} else {
					p.sendMessage("��c�ơ�r ������ �������� �����մϴ�!");
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
								event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								return;
							}
							
							int price = Integer.parseInt(event.getName());
							
							if (price <= 0) {
								event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								return;
							}
							
							ShopData data = userData.get(event.getPlayer().getName());
							data.addItem(item.clone(), price);
							
							event.getPlayer().getInventory().removeItem(item);
							
							event.getPlayer().sendMessage("��a�ơ�r ǰ���� ��ϵǾ����ϴ�!");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = e.getCurrentItem().clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("���� ����");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "��c������ ��ư�� Ŭ�����ּ���.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("��a�ơ�r �Ǹ��� ������ �Է����ּ���!");
				} else if (9 <= e.getRawSlot() && e.getRawSlot() < 35 && e.isRightClick()) {
					final ItemStack item = data.getItems().get(e.getRawSlot() - 9);
					
					if (e.isShiftClick()) {
						Post post = Post.getBuilder()
								.setSender("��a���� ����")
								.setMessage("�Ǹ� �ߴ� ������", "���� �������� �����Ͻ� ǰ���� ��޵Ǿ����ϴ�.")
								.setItems(new ItemStack[]{item})
								.build();
						Post.sendPost(p, post);
						
						data.removeItem(item);
						
						updateInv(p, e.getInventory(), data);
						p.sendMessage("��a�ơ�r �Ǹ����� ǰ���� ���ŵǾ����ϴ�.");
						p.sendMessage("��a�ơ�r �ǸŰ� �ߴܵ� �������� ���������� ���۵Ǿ����ϴ�!");
						
						new FancyMessage("��a�ơ�r ")
						.then("��b��n/������")
							.command("/������")
							.tooltip("��b/������")
						.send(data.getOwner().getPlayer());
						
						return;
					}
					
					p.closeInventory();
					
					AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent event) {
							if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
								event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								return;
							}
							
							int price = Integer.parseInt(event.getName());
							
							if (price <= 0) {
								event.getPlayer().sendMessage("��c�ơ�r ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								return;
							}
							
							ShopData data = userData.get(event.getPlayer().getName());
							data.setPrice(item, price);
							
							event.getPlayer().sendMessage("��a�ơ�r ������ ����Ǿ����ϴ�!");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = e.getCurrentItem().clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("���� ����");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "��c������ ��ư�� Ŭ�����ּ���.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("��a�ơ�r ���� �Ǹ� ������ �Է����ּ���!");
				}
			} else {
				if (e.getRawSlot() >= 45) return;
				
				if (p.getInventory().firstEmpty() == -1) {
					p.sendMessage("��c�ơ�r �κ��丮�� ������ �������� ������ �� �����ϴ�.");
					return;
				}
				
				final ItemStack item = data.getItems().get(e.getRawSlot() - 9);
				
				if (plugin.vault.hasEnough(p, data.getPrice(item))) {
					p.closeInventory();
					
					AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent event) {
							if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
								event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
								return;
							}
							
							if (!plugin.util.isInteger(event.getName())) {
								event.getPlayer().sendMessage("��c�ơ�r ���� ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								return;
							}
							
							int amount = Integer.parseInt(event.getName());
							
							if (amount <= 0) {
								event.getPlayer().sendMessage("��c�ơ�r ���� ������ �ڿ����� �Է��� �� �ֽ��ϴ�!");
								return;
							}
							
							if (amount > item.clone().getAmount()) {
								event.getPlayer().sendMessage("��c�ơ�r �Ǹ����� ������ �������� ���� ������ �� �����ϴ�!");
								event.getPlayer().sendMessage("��c�ơ�r ���� �ش� �������� ��e"+item.getAmount()+"��r�� �Ǹ����Դϴ�.");
								return;
							}
							
							ShopData data = userData.get(event.getPlayer().getName());
							
							int price = data.getPrice(item) * amount;
							
							if (price > plugin.vault.getBalance(event.getPlayer())) {
								event.getPlayer().sendMessage("��c�ơ�r �������� ������ �������� ������ �� �����ϴ�!");
								return;
							}
							
							data.updateAmount(item, amount);
							
							ItemStack give = item.clone();
							give.setAmount(amount);
							
							event.getPlayer().getInventory().addItem(give);
							
							plugin.vault.give(data.getOwner(), price);
							plugin.vault.take(event.getPlayer(), price);
							
							event.getPlayer().sendMessage("��a�ơ�r �������� �����߽��ϴ�! ��c[-"+price+"E]");
							event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
							
							if (data.getOwner().isOnline())
								data.getOwner().getPlayer().sendMessage("��a�ơ�r "+event.getPlayer().getName()+"�Բ��� ����� �������� �����ϼ̽��ϴ�! ��a[+"+price+"E]");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
						}
					});
					
					ItemStack icon = e.getCurrentItem().clone();
					ItemMeta meta = icon.getItemMeta();
					
					meta.setDisplayName("������ ����");
					
					icon.setItemMeta(meta);
					
					plugin.util.addLore(icon, "");
					plugin.util.addLore(icon, "��c������ ��ư�� Ŭ�����ּ���.");
					
					gui.setSlot(AnvilSlot.INPUT_LEFT, icon);
					gui.open();
					
					p.sendMessage("��a�ơ�r ������ ������ �Է����ּ���!");
				} else {
					p.sendMessage("��c�ơ�r �������� ������ ������ �� �����ϴ�!");
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
