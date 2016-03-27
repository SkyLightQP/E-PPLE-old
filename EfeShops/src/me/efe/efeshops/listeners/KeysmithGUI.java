package me.efe.efeshops.listeners;

import java.util.ArrayList;
import java.util.List;

import me.efe.bloodskin.skins.Skin;
import me.efe.efeitems.ItemStorage;
import me.efe.efeserver.PlayerData;
import me.efe.efeshops.EfeShops;

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

public class KeysmithGUI implements Listener {
	public EfeShops plugin;
	public List<String> users = new ArrayList<String>();
	
	public KeysmithGUI(EfeShops plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9*5, "§5▒§r 잠긴 상자 열기");
		
		inv.setItem(7, plugin.util.createDisplayItem("§a"+plugin.vault.getBalance(player)+"E 소지중", new ItemStack(Material.EMERALD), new String[]{}));
		inv.setItem(8, plugin.util.enchant(plugin.util.createDisplayItem("§e§nHow to use?", new ItemStack(Material.TRIPWIRE_HOOK), new String[]{
			"잠긴 상자는 희귀하게 발견하거나",
			"코인샵에서 구매할 수 있으며",
			"개봉에는 소량의 에페가 필요합니다."}), Enchantment.SILK_TOUCH, 100));
		
		List<ItemStack> chests = getChests(player);
		
		if (chests.isEmpty()) {
			inv.setItem(22, plugin.util.createDisplayItem("§c소지중인 상자가 없습니다.", new ItemStack(Material.BARRIER), new String[]{}));
		} else {
			int maxSize = chests.size() > 9 ? 9 : chests.size();
			int start = 22 - maxSize / 2;
			
			for (int i = 0; i < maxSize; i ++) {
				ItemStack item = chests.get(i);
				plugin.util.addLore(item, "");
				plugin.util.addLore(item, "§9클릭하면 상자를 엽니다: "+getPrice(item));
				
				inv.setItem(start + i, item);
			}
		}
		
		users.add(player.getName());
		player.openInventory(inv);
	}
	
	private List<ItemStack> getChests(Player player) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null || item.getType().equals(Material.AIR)) continue;
			if (!ItemStorage.getName(item).equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX)) &&
					!ItemStorage.getName(item).equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_POPULAR)) &&
					!ItemStorage.getName(item).equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_PREMIUM)) &&
					!ItemStorage.getName(item).equals(ItemStorage.getName(ItemStorage.DECORATION_HEAD_BOX))) continue;
			
			int amount = item.clone().getAmount();
			
			if (amount > 1) {
				for (int i = 0; i < amount; i ++) {
					ItemStack splited = item.clone();
					splited.setAmount(1);
					
					list.add(splited);
				}
			} else {
				list.add(item.clone());
			}
		}
		
		return list;
	}
	
	private String getPrice(ItemStack item) {
		String name = ItemStorage.getName(item);
		
		if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX))) {
			
			return "50 Ephe";
		} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_POPULAR))) {
			
			return "50 Ephe";
		} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_PREMIUM))) {
			
			return "50 Ephe";
		} else if (name.equals(ItemStorage.getName(ItemStorage.DECORATION_HEAD_BOX))) {
			
			return "100 Ephe";
		}
		
		return null;
	}
	
	private boolean hasEnough(Player player, ItemStack item) {
		String name = ItemStorage.getName(item);
		int price = 0;
		
		if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX))) {
			price = 50;
		} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_POPULAR))) {
			price = 50;
		} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_PREMIUM))) {
			price = 50;
		} else if (name.equals(ItemStorage.getName(ItemStorage.DECORATION_HEAD_BOX))) {
			price = 100;
		}
		
		return plugin.vault.hasEnough(player, price);
	}
	
	private void takeMoney(Player player, ItemStack item) {
		String name = ItemStorage.getName(item);
		int price = 0;
		
		if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX))) {
			price = 50;
		} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_POPULAR))) {
			price = 50;
		} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_PREMIUM))) {
			price = 50;
		} else if (name.equals(ItemStorage.getName(ItemStorage.DECORATION_HEAD_BOX))) {
			price = 100;
		}
		
		plugin.vault.take(player, price);
		player.sendMessage("§a▒§r 소지금을 지불했습니다: "+price+"E");
	}
	
	private boolean hasAllSkin(Player player, boolean isPremium, boolean isPopular) {
		PlayerData data = PlayerData.get(player);
		List<Skin> skins = new ArrayList<Skin>();
		
		for (Skin skin : plugin.crateGui.bloodSkin.skins.values()) {
			if (!skin.isGettable() || data.hasBloodSkin(skin.getName()))
				continue;
			
			if (isPremium && !skin.isPremium())
				continue;
			
			if (isPopular && skin.isPremium())
				continue;
			
			skins.add(skin);
		}
		
		return skins.isEmpty();
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getName())) return;
		event.setCancelled(true);
		
		if (!event.getInventory().getTitle().startsWith("§5▒§r 잠긴 상자 열기")) return;
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) || event.getRawSlot() >= 45) return;
		
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem().clone();
		String name = ItemStorage.getName(item);
		
		if (item.getType() == Material.CHEST || item.getType() == Material.ENDER_CHEST) {
			
			if ((name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX)) && hasAllSkin(player, false, false)) ||
					name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_POPULAR)) && hasAllSkin(player, false, true) ||
					name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_PREMIUM)) && hasAllSkin(player, true, false)) {
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				player.sendMessage("§c▒§r 모든 스킨을 소지중입니다.");
				return;
			}
			
			if (!hasEnough(player, item)) {
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				player.sendMessage("§c▒§r 결제에 필요한 금액이 부족합니다.");
				return;
			}
			
			if (plugin.util.containsLore(item, "§c한 번 더 눌러주세요.")) {
				takeMoney(player, item);
				
				if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX))) {
					ItemStorage.takeItem(player, ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX), 1);
					
					player.closeInventory();
					plugin.crateGui.openGUI(player, false, false);
				} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_POPULAR))) {
					ItemStorage.takeItem(player, ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_POPULAR), 1);
					
					player.closeInventory();
					plugin.crateGui.openGUI(player, false, true);
				} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_PREMIUM))) {
					ItemStorage.takeItem(player, ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_PREMIUM), 1);
					
					player.closeInventory();
					plugin.crateGui.openGUI(player, true, false);
				} else if (name.equals(ItemStorage.getName(ItemStorage.DECORATION_HEAD_BOX))) {
					
					player.closeInventory();
					plugin.caseGui.openGUI(player);
				}
				
			} else {
				plugin.util.addLore(item, "§c한 번 더 눌러주세요.");
				event.getInventory().setItem(event.getRawSlot(), item);
				
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 2.0F);
				
				player.sendMessage("§a▒§r 일정량의 에페를 소모합니다. 동의하시면 한 번 더 눌러주세요.");
			}
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getName())) {
			users.remove(event.getPlayer().getName());
		}
	}
}