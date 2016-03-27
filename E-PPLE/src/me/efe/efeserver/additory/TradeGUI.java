package me.efe.efeserver.additory;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.efe.efecommunity.Post;
import me.efe.efeserver.EfeServer;
import mkremins.fanciful.FancyMessage;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.horyu1234.horyulogger.HoryuLogger;

public class TradeGUI implements Listener {
	public EfeServer plugin;
	public HashMap<UUID, UUID> users = new HashMap<UUID, UUID>();
	public HashMap<UUID, Inventory> inventories = new HashMap<UUID, Inventory>();
	public List<Integer> tradeSlots = new ArrayList<Integer>();
	
	public TradeGUI(EfeServer plugin) {
		this.plugin = plugin;
		
		tradeSlots.add(6);
		tradeSlots.add(7);
		tradeSlots.add(8);
		tradeSlots.add(15);
		tradeSlots.add(16);
		tradeSlots.add(17);
		tradeSlots.add(24);
		tradeSlots.add(25);
		tradeSlots.add(26);
	}
	
	public void openGUI(Player p, Player target) {
		Inventory inv = plugin.getServer().createInventory(p, 9*3, "§5▒§r "+target.getName()+"님과의 거래");
		
		ItemStack border = plugin.util.createDisplayItem(" ", new ItemStack(Material.IRON_FENCE), new String[]{});
		for (int i : new int[]{4, 13, 22}) {
			inv.setItem(i, border.clone());
		}
		
		inv.setItem(3, plugin.util.createDisplayItem("§a거래 승인 대기중", new ItemStack(Material.WOOL, 1, (short) 5), new String[]{}));
		inv.setItem(5, plugin.util.createDisplayItem("§a거래 승인", new ItemStack(Material.WOOL, 1, (short) 5), new String[]{}));
		inv.setItem(14, plugin.util.createDisplayItem("§c닫기", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		inv.setItem(23, plugin.util.createDisplayItem("§a0.0Ephe", new ItemStack(Material.EMERALD), new String[]{"클릭하면 금액을 추가합니다."}));
		
		users.put(p.getUniqueId(), target.getUniqueId());
		inventories.put(p.getUniqueId(), inv);
		p.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getUniqueId())) return;
		if (!e.getInventory().getTitle().endsWith("님과의 거래")) return;
		
		if (e.getInventory().getItem(5).getDurability() == 4) {
			e.setCancelled(true);
			
			if (e.getRawSlot() == 14) {
				e.getWhoClicked().closeInventory();
			}
			
			return;
		}
		
		if (e.getRawSlot() >= 9*3) {
			if (e.getAction() != InventoryAction.PICKUP_ALL &&
					e.getAction() != InventoryAction.PLACE_ALL &&
					e.getAction() != InventoryAction.SWAP_WITH_CURSOR &&
					e.getAction() != InventoryAction.PICKUP_HALF &&
					e.getAction() != InventoryAction.PLACE_ONE) {
				e.setCancelled(true);
			}
			
			return;
		}
		
		e.setCancelled(true);
		
		final Player p = (Player) e.getWhoClicked();
		final Player target = plugin.getServer().getPlayer(users.get(p.getUniqueId()));
		final Inventory inv = e.getInventory();
		final Inventory targetInv = inventories.get(target.getUniqueId());
		int slot = e.getRawSlot();
		
		if (tradeSlots.contains(slot)) {
			if (e.getAction() != InventoryAction.PICKUP_ALL &&
					e.getAction() != InventoryAction.PLACE_ALL &&
					e.getAction() != InventoryAction.SWAP_WITH_CURSOR &&
					e.getAction() != InventoryAction.PICKUP_HALF &&
					e.getAction() != InventoryAction.PLACE_ONE) {
				return;
			}
			
			e.setCancelled(false);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					for (int i : tradeSlots) {
						inv.setItem(i - 6, targetInv.getItem(i));
						targetInv.setItem(i - 6, inv.getItem(i));
					}
				}
			}, 1);
			
			return;
		}
		
		ItemStack icon = e.getCurrentItem();
		if (icon != null && icon.getType() != Material.AIR) {
			switch (slot) {
			case 5:
				if (icon.getDurability() == 5) {
					if (plugin.util.containsLore(inv.getItem(23), "§9클릭하면 입력된 값을 금액으로 등록합니다.")) {
						p.sendMessage("§c▒§r 금액을 입력을 취소해주세요.");
						return;
					}
					
					inv.setItem(5, plugin.util.createDisplayItem("§e거래 대기중", new ItemStack(Material.WOOL, 1, (short) 4), new String[]{}));
					targetInv.setItem(3, plugin.util.createDisplayItem("§e거래 대기중", new ItemStack(Material.WOOL, 1, (short) 4), 
							new String[]{"상대방이 거래를 승락했습니다."}));
					
					if (targetInv.getItem(5).getDurability() == 4) {
						
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								if (!users.containsKey(p.getUniqueId()) || !users.containsKey(target.getUniqueId())) return;
								
								String displayP = inv.getItem(23).getItemMeta().getDisplayName();
								Double amountP = Double.parseDouble(displayP.substring(2, displayP.length() - 4));
								if (plugin.vault.getBalance(p) < amountP) {
									p.sendMessage("§a▒§r 소지금이 부족합니다!");
									p.closeInventory();
									return;
								}
								
								String displayT = targetInv.getItem(23).getItemMeta().getDisplayName();
								Double amountT = Double.parseDouble(displayT.substring(2, displayT.length() - 4));
								if (plugin.vault.getBalance(target) < amountT) {
									target.sendMessage("§a▒§r 소지금이 부족합니다!");
									target.closeInventory();
									return;
								}
								
								
								for (int tradeSlot : tradeSlots) {
									ItemStack tradeItem = inv.getItem(tradeSlot);
									
									if (tradeItem == null || tradeItem.getType() == Material.AIR)
										continue;
									
									giveItem(target, tradeItem);
								}
								
								for (int tradeSlot : tradeSlots) {
									ItemStack tradeItem = targetInv.getItem(tradeSlot);
									
									if (tradeItem == null || tradeItem.getType() == Material.AIR)
										continue;
									
									giveItem(p, tradeItem);
								}
								
								plugin.vault.take(p, amountP);
								plugin.vault.give(target, amountP * 0.85);
								plugin.vault.take(target, amountT);	
								plugin.vault.give(p, amountT * 0.85);
								
								users.remove(p.getUniqueId());
								users.remove(target.getUniqueId());
								inventories.remove(p.getUniqueId());
								inventories.remove(target.getUniqueId());
								
								p.closeInventory();
								target.closeInventory();
								
								p.sendMessage("§a▒§r 거래가 성사되었습니다.");
								target.sendMessage("§a▒§r 거래가 성사되었습니다.");
							}
						}, 10L);
						
					}
				}
				
				
				break;
			case 14:
				p.closeInventory();
				
				break;
			case 23:
				if (icon.getDurability() == 4)
					return;
				
				if (!plugin.util.containsLore(icon, "§9클릭하면 입력된 값을 금액으로 등록합니다.")) {
					ItemStack newIcon = icon.clone();
					ItemMeta meta = newIcon.getItemMeta();
					List<String> list = new ArrayList<String>();
					
					list.add("§71~9 핫바 슬롯키로 숫자를 입력하세요.");
					list.add("§7버튼을 우클릭하면 0이 입력됩니다.");
					list.add("§7쉬프트 클릭하면 입력을 취소합니다.");
					list.add("§7거래 수수료: 15%");
					list.add("");
					list.add("§9클릭하면 입력된 값을 금액으로 등록합니다.");
					
					meta.setDisplayName("§a_");
					meta.setLore(list);
					newIcon.setItemMeta(meta);
					
					inv.setItem(23, newIcon);
					
					p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
					p.sendMessage("§a▒§r 등록할 금액을 입력해주세요. §a소지금: "+plugin.vault.getBalance(p)+"E");
					return;
				}
				
				if (e.getHotbarButton() != -1) {
					ItemStack newIcon = icon.clone();
					ItemMeta meta = newIcon.getItemMeta();
					
					meta.setDisplayName("§a"+meta.getDisplayName().substring(2, meta.getDisplayName().length() - 1)+(e.getHotbarButton() + 1)+"_");
					newIcon.setItemMeta(meta);
					
					inv.setItem(23, newIcon);
					return;
				}
				
				if (e.isRightClick()) {
					ItemStack newIcon = icon.clone();
					ItemMeta meta = newIcon.getItemMeta();
					
					meta.setDisplayName("§a"+meta.getDisplayName().substring(2, meta.getDisplayName().length() - 1)+"0_");
					newIcon.setItemMeta(meta);
					
					inv.setItem(23, newIcon);
					return;
				}
				
				if (e.isLeftClick()) {
					
					if (e.isShiftClick()) {
						inv.setItem(23, plugin.util.createDisplayItem("§a0.0Ephe", new ItemStack(Material.EMERALD), new String[]{"클릭하면 돈을 추가합니다."}));
						
						p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
						p.sendMessage("§a▒§r 금액 입력이 취소되었습니다.");
						return;
					}
					
					String display = icon.getItemMeta().getDisplayName();
					Double amount = Double.parseDouble(display.substring(2, display.length() - 1));
					
					if (!plugin.vault.hasEnough(p, amount)) {
						inv.setItem(23, plugin.util.createDisplayItem("§a0.0Ephe", new ItemStack(Material.EMERALD), new String[]{"클릭하면 돈을 추가합니다."}));
						
						p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
						p.sendMessage("§a▒§r 소지금보다 많은 금액입니다!");
						return;
					}
					
					if (amount > 0.0D) {
						inv.setItem(23, plugin.util.enchant(plugin.util.createDisplayItem("§a"+amount+"Ephe", new ItemStack(Material.EMERALD), 
								new String[]{"클릭하면 금액을 변경합니다."}), Enchantment.SILK_TOUCH, 100));
						
						targetInv.setItem(21, plugin.util.enchant(plugin.util.createDisplayItem("§a"+amount+"Ephe", new ItemStack(Material.EMERALD), 
								new String[]{"§2수수료 차감 후: "+(amount * 0.85)+"Ephe"}), Enchantment.SILK_TOUCH, 100));
						
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
						p.sendMessage("§a▒§r 금액이 등록되었습니다.");
						
						target.playSound(target.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
						target.sendMessage("§a▒§r 상대방이 금액을 입력했습니다.");
					} else {
						inv.setItem(23, plugin.util.createDisplayItem("§a"+amount+"Ephe", new ItemStack(Material.EMERALD), 
								new String[]{"클릭하면 금액을 변경합니다."}));
						
						targetInv.setItem(21, new ItemStack(Material.AIR));
						
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
						p.sendMessage("§a▒§r 금액이 제거되었습니다.");
						
						target.playSound(target.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
						target.sendMessage("§a▒§r 상대방이 금액 등록을 취소했습니다.");
					}
				}
				
				break;
			}
		}
	}
	
	@EventHandler
	public void drag(InventoryDragEvent e) {
		if (users.containsKey(e.getWhoClicked().getUniqueId())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.containsKey(e.getPlayer().getUniqueId()) && e.getInventory().getTitle().endsWith("님과의 거래")) {
			UUID id = users.get(e.getPlayer().getUniqueId());
			Player target = plugin.getServer().getPlayer(id);
			
			backToInventory(target, inventories.get(target.getUniqueId()));
			backToInventory((Player) e.getPlayer(), e.getInventory());
			
			users.remove(e.getPlayer().getUniqueId());
			users.remove(target.getUniqueId());
			inventories.remove(e.getPlayer().getUniqueId());
			inventories.remove(target.getUniqueId());
			
			target.closeInventory();
			
			e.getPlayer().sendMessage("§a▒§r 거래가 취소되었습니다.");
			target.sendMessage("§a▒§r 상대방이 거래를 취소하셨습니다.");
		}
	}
	
	private void backToInventory(Player player, Inventory inv) {
		for (int slot : tradeSlots) {
			ItemStack item = inv.getItem(slot);
			
			if (item == null || item.getType() == Material.AIR)
				continue;
			
			giveItem(player, item.clone());
		}
	}
	
	private void giveItem(Player player, ItemStack item) {
		if (player.getInventory().firstEmpty() == -1) {
			Post post = Post.getBuilder()
					.setSender(player.getName())
					.setMessage("§a거래 취소된 아이템", "인벤토리가 가득 차 발송되었습니다.")
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
}