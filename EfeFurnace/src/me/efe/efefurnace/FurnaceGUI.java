package me.efe.efefurnace;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class FurnaceGUI implements Listener {
	private EfeFurnace plugin;
	private final Set<UUID> users = new HashSet<UUID>();
	
	public FurnaceGUI(EfeFurnace plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player) {
		FurnacePlayer fp = plugin.getFurnacePlayer(player);
		
		fp.openFurnace();
		users.add(player.getUniqueId());
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getUniqueId()))
			return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (event.getRawSlot() == 0 || event.getRawSlot() == 2) {
			if (!checkItem(event, Material.IRON_INGOT)) {
				event.setCancelled(true);
				
				player.sendMessage("§c▒§r 철괴만 구울 수 있습니다.");
			}
		} else if (event.getRawSlot() == 1) {
			if (!checkItem(event, Material.LAVA_BUCKET) && !checkItem(event, Material.BUCKET)) {
				event.setCancelled(true);
				
				player.sendMessage("§c▒§r 용암 양동이만 연료로 사용할 수 있습니다.");
			}
		}
	}
	
	private boolean checkItem(InventoryClickEvent event, Material type) {
		if (event.getCursor() != null && event.getCursor().getType() != Material.AIR && event.getCursor().getType() != type) {
			return false;
		}
		
		if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().getType() != type) {
			return false;
		}
		
		return true;
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
}
