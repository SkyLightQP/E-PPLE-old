package me.efe.fishkg.listeners;

import java.util.ArrayList;
import java.util.List;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeserver.EfeServer;
import me.efe.fishkg.Fishkg;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopGUI implements Listener {
	public Fishkg plugin;
	public List<String> users = new ArrayList<String>();
	
	public ShopGUI(Fishkg plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (ignoreCancelled = true)
	public void createShop(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[Fishkg]") || e.getLine(0).equalsIgnoreCase("��9[Fishkg]")) {
			if (!plugin.checkPermission(e.getPlayer(), "fishkg.control")) {
				e.getBlock().breakNaturally();
				e.getPlayer().sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
				return;
			}
			
			if (!plugin.getConfig().getBoolean("fish.enable")) {
				e.getPlayer().sendMessage(plugin.main+"fish.enable�� false�̹Ƿ� ������ ����� �� �����ϴ�!");
				return;
			}
			
			e.setLine(0, "��9[Fishkg]");
			
			e.getPlayer().sendMessage(plugin.main+"������ �����Ǿ����ϴ�!");
		}
	}
	
	@EventHandler
	public void openShop(PlayerInteractEvent e) {
		if (EfeUtils.event.isSignClicked(e) && EfeUtils.event.isRightClick(e)) {
			Sign sign = (Sign) e.getClickedBlock().getState();
			
			if (sign.getLine(0).equalsIgnoreCase("��9[Fishkg]") && plugin.getConfig().getBoolean("shop.enable")) {
				openGUI(e.getPlayer());
			}
		}
	}
	
	public void openGUI(Player p) {
		Inventory inv = Bukkit.getServer().createInventory(p, 9*5, "��9�ơ�r ����� ����");
		
		refresh(inv, p);
		
		p.openInventory(inv);
		users.add(p.getName());
	}
	
	public void refresh(Inventory inv, Player p) {
		ItemStack line = EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3), null);
		for (int i = 0; i < 45; i += 9) inv.setItem(i, line.clone());
		for (int i = 8; i < 53; i += 9) inv.setItem(i, line.clone());
		
		inv.setItem(22, EfeUtils.item.createDisplayItem("��b�Ǹ��� ����⸦ �̰��� �μ���!", new ItemStack(Material.THIN_GLASS), null));
		inv.setItem(29, EfeUtils.item.createDisplayItem("��a�Ǹ�", new ItemStack(Material.GOLD_NUGGET), new String[]{"Ŭ���� ���õ� ����⸦ �Ǹ��մϴ�."}));
		inv.setItem(31, EfeUtils.item.createDisplayItem("��a������", new ItemStack(Material.WOOD_DOOR), new String[]{"Ŭ���� ������ �ݽ��ϴ�."}));
		inv.setItem(33, EfeUtils.item.createDisplayItem("��a�����ֱ�", new ItemStack(Material.WATER_BUCKET), new String[]{"Ŭ���� ���õ� ����⸦ ���� ���������ϴ�."}));
		//inv.setItem(44, EfeUtils.item.createDisplayItem("��b��l"+plugin.getDescription().getFullName(), new ItemStack(Material.BOOK_AND_QUILL), new String[]{"Made by ��lEfe"}));
		
		int emerald = plugin.getConfig().getInt("shop.emerald");
		
		if (emerald != 0) {
			ItemStack item = inv.getItem(29);
			item.setType(EfeUtils.item.getMaterial(emerald));
			
			inv.setItem(29, item);
		}
		
		if (plugin.addon_extraShop) {
			inv.setItem(33, EfeUtils.item.createDisplayItem("��a�ټ� �Ǹ�", new ItemStack(Material.GOLD_INGOT), new String[]{"�ټ��� ����⸦ �� ���� �Ǹ��մϴ�."}));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler (ignoreCancelled = true)
	public void invClick(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getAction() != InventoryAction.PICKUP_ALL && e.getAction() != InventoryAction.PLACE_ALL && e.getAction() != InventoryAction.SWAP_WITH_CURSOR) {
			e.setCancelled(true);
			
			EfeUtils.player.updateInv(p);
			return;
		}
		
		if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.RAW_FISH) {
			if (e.getRawSlot() == 22 && e.getCursor().getType() != Material.RAW_FISH) {
				e.setCancelled(true);
				e.setCursor(new ItemStack(e.getCurrentItem()));
				e.getInventory().setItem(22, EfeUtils.item.createDisplayItem("��b�Ǹ��� ����⸦ �̰��� �μ���!", new ItemStack(Material.THIN_GLASS), null));
				
				EfeUtils.player.updateInv(p);
			} else if (e.getRawSlot() < 9*5) {
				e.setCancelled(true);
				
				EfeUtils.player.updateInv(p);
			}
			
			return;
		}
		
		if (e.getCursor().getType().equals(Material.RAW_FISH)) {
			if (e.getRawSlot() == 22) {
				e.setCancelled(true);
				
				EfeUtils.player.updateInv(p);
				
				e.getInventory().setItem(e.getRawSlot(), e.getCursor());
				e.setCursor(new ItemStack(Material.AIR));
			} else if (e.getRawSlot() < 9*5) {
				e.setCancelled(true);
				
				EfeUtils.player.updateInv(p);
			}
			
			return;
		}
		
		if (e.getRawSlot() == 29) {
			e.setCancelled(true);
			
			EfeUtils.player.updateInv(p);
			
			if (e.getInventory().getItem(22).getType() == Material.RAW_FISH) {
				ItemStack fish = e.getInventory().getItem(22);
				ItemMeta meta = fish.getItemMeta();
				
				if (!plugin.isFish(fish)) {
					p.sendMessage(plugin.main+"��c�ơ�r �� �� ���� ������Դϴ�.");
					return;
				}
				
				double cm = plugin.getLength(fish);
				double price = cm * plugin.getConfig().getDouble("shop.price");
				
				if (cm == 0) {
					p.sendMessage(plugin.main+"��c�ơ�r ������ Fishkg�� ������ Ȯ�εǾ� ���ŵǾ����ϴ�.");
					
					refresh(e.getInventory(), p);
					return;
				}
				
				if (plugin.getConfig().getBoolean("fish.addGrade")) {
					String grade = meta.getLore().get(0);
					
					for (String str : new String[]{"S", "A", "B", "C", "D"})
						if (grade.contains(str))
							price += plugin.getConfig().getDouble("shop.grade."+str);
				}
				
				if (price < 0)
					price = 0;
				
				if (plugin.getConfig().getBoolean("shop.pricePoint"))
					price = (int) price;
				
				if (plugin.getConfig().getInt("shop.emerald") != 0) {
					EfeUtils.player.giveItem(p, new ItemStack(EfeUtils.item.getMaterial(plugin.getConfig().getInt("shop.emerald")), (int) price));
					
					String name = meta.getDisplayName().replace("��9", "").replace("��6��l", "");
					p.sendMessage(plugin.main+"��a�ơ�r "+name+"��r ����Ⱑ �ȷ� "+(int) price+"E�� ȹ���߽��ϴ�.");
				} else {
					plugin.vault.give(p, price);
					EfeServer.getInstance().extraListener.sendTabTitle(p);
					
					String name = meta.getDisplayName().replace("��9", "").replace("��6��l", "");
					p.sendMessage(plugin.main+"��a�ơ�r "+name+"��r ����Ⱑ �ȷ� "+price+"E�� ȹ���߽��ϴ�.");
				}
				
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.5F);
				
				refresh(e.getInventory(), p);
			} else {
				p.sendMessage(plugin.main+"��c�ơ�r �Ǹ��� ����⸦ �÷��ּ���.");
			}
		} else if (e.getRawSlot() == 31) {
			p.playSound(p.getLocation(), Sound.DOOR_CLOSE, 1.0F, 1.0F);
			p.closeInventory();
			
		} else if (e.getRawSlot() == 33) {
			e.setCancelled(true);
			
			EfeUtils.player.updateInv(p);
			
			if (plugin.addon_extraShop) return;
			
			if (e.getInventory().getItem(22).getType() == Material.RAW_FISH) {
				ItemStack fish = e.getInventory().getItem(22);
				
				if (!plugin.isFish(fish)) {
					p.sendMessage(plugin.main+"��c�ơ�r Ǯ���� �� ���� ������Դϴ�.");
					return;
				}
				
				p.playSound(p.getLocation(), Sound.SWIM, 1.0F, 1.0F);
				p.sendMessage(plugin.main+"��a�ơ�r "+fish.getItemMeta().getDisplayName().replace("��9", "").replace("��6��l", "")+"��r ����⸦ Ǯ���־����ϴ�.");
				
				refresh(e.getInventory(), p);
				return;
			}
			
			p.sendMessage(plugin.main+"��c�ơ�r Ǯ���� ����⸦ �÷��ּ���.");
		} else {
			e.setCancelled(true);
			
			EfeUtils.player.updateInv(p);
		}
	}
	
	@EventHandler
	public void invDrag(InventoryDragEvent e) {
		if (users.contains(e.getWhoClicked().getName())) {
			e.setCancelled(true);
			
			EfeUtils.player.updateInv((Player) e.getWhoClicked());
		}
	}
	
	@EventHandler
	public void invClose(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
			
			if (e.getInventory().getItem(22).getType().equals(Material.RAW_FISH)) {
				EfeUtils.player.giveItem((Player) e.getPlayer(), e.getInventory().getItem(22));
			}
		}
	}
}