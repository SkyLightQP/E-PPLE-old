package me.efe.efeshops.listeners;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeitems.ItemStorage;
import me.efe.efeitems.SkullStorage;
import me.efe.efeitems.SkullStorage.Skull;
import me.efe.efeserver.EfeServer;
import me.efe.efeshops.EfeShops;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HeadConverterGUI implements Listener {
	public EfeShops plugin;
	public List<String> users = new ArrayList<String>();
	public Location converterLoc1;
	public Location converterLoc2;
	
	public HeadConverterGUI(EfeShops plugin) {
		this.plugin = plugin;
		this.converterLoc1 = new Location(EfeServer.getInstance().world, -60, 72, 19);
		this.converterLoc2 = new Location(EfeServer.getInstance().world, 87, 70, -756);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
				(event.getClickedBlock().getLocation().equals(converterLoc1) || event.getClickedBlock().getLocation().equals(converterLoc2))) {
			event.setCancelled(true);
			
			openGUI(event.getPlayer());
		}
 	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9*3, "§5▒§r 데코 헤드 변환기");
		
		inv.setItem(25, plugin.util.createDisplayItem("§a변환", new ItemStack(Material.ANVIL), new String[]{}));
		inv.setItem(26, plugin.util.createDisplayItem("§e§nHow to use?", new ItemStack(Material.SIGN),
				new String[]{"데코 헤드 변환기는 데코 헤드", "5개를 데코 헤드 상자 1개로", "변환시켜 줍니다.", "데코 헤드를 올리고 변환 버튼을", "눌러주세요."}));

		users.add(player.getName());
		player.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getName())) return;
		
		if (!event.getInventory().getTitle().startsWith("§5▒§r 데코 헤드 변환기")) return;
		
		if (event.getRawSlot() == 25) {
			event.setCancelled(true);

			Player player = (Player) event.getWhoClicked();
			List<ItemStack> list = new ArrayList<ItemStack>();
			
			for (int i = 0; i < 25; i ++) {
				ItemStack item = event.getInventory().getItem(i);
				
				if (item == null || item.getType() == Material.AIR)
					continue;
				
				if (item.getType() == Material.SKULL_ITEM && item.getDurability() == 3) {
					Skull skull = SkullStorage.getSkull(item);
					
					if (!skull.isConvertable()) {
						player.sendMessage("§c▒§r 상점에서 판매하는 헤드는 변환할 수 없습니다!");
						return;
					}
					
					list.add(item.clone());
				} else {
					player.sendMessage("§c▒§r 데코 헤드만 변환할 수 있습니다!");
					return;
				}
			}
			
			if (list.size() < 5) {
				player.sendMessage("§c▒§r 5개 이상의 데코 헤드를 변환기에 올려주세요.");
				return;
			}
			
			for (int i = 0; i < 25; i ++)
				event.getInventory().setItem(i, new ItemStack(Material.AIR));
			
			int amount = 0;
			
			while (true) {
				for (int i = 0; i < 5; i ++)
					list.remove(0);
				
				ItemStack item = ItemStorage.DECORATION_HEAD_BOX.clone();
				giveItem(player, item);
				
				amount ++;
				
				if (list.size() / 5 == 0)
					break;
			}
			
			for (ItemStack item : list)
				giveItem(player, item);
			
			player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);
			player.sendMessage("§a▒§r "+(amount * 5)+"개의 데코 헤드를 "+amount+"개의 상자로 변환했습니다.");
			
		} else if (event.getRawSlot() == 26) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getName())) {
			
			for (int i = 0; i < 25; i ++) {
				ItemStack item = event.getInventory().getItem(i);
				
				if (item != null && item.getType() != Material.AIR) {
					giveItem((Player) event.getPlayer(), item.clone());
				}
			}
			
			users.remove(event.getPlayer().getName());
		}
	}
	
	public void giveItem(Player player, ItemStack item) {
		if (player.getInventory().firstEmpty() == -1)
			player.getWorld().dropItem(player.getLocation(), item);
		else
			player.getInventory().addItem(item);
	}
}