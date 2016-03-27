package me.efe.efefurnace;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.efe.skilltree.SkillManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EfeFurnace extends JavaPlugin implements Listener {
	private final Map<UUID, FurnacePlayer> playerMap = new HashMap<UUID, FurnacePlayer>();
	private final ItemStack ironIngot = new ItemStack(Material.IRON_INGOT);
	private final int updateDelay = 3;
	private FurnaceGUI furnaceGUI;
	private Location furnaceLoc;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		this.furnaceLoc = new Location(getServer().getWorld("world"), -1482, 65, 519);
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(this.furnaceGUI = new FurnaceGUI(this), this);
		
		new FurnaceTask(this.updateDelay);
		
		getLogger().info(getDescription().getFullName() + " has been enabled!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info(getDescription().getFullName() + " has been disabled.");
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		getFurnacePlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		UUID id = event.getPlayer().getUniqueId();
		
		if (playerMap.containsKey(id)) {
			playerMap.remove(id).saveData();;
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Furnace) {
			if (event.getClickedBlock().getLocation().equals(this.furnaceLoc)) {
				if (!SkillManager.hasLearned(event.getPlayer(), "mine.steelmaking")) {
					event.getPlayer().sendMessage("§c▒§r \"제강\" 스킬을 배워야 이용할 수 있습니다.");
					return;
				}
			
				this.furnaceGUI.openGUI(event.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void hopper(InventoryMoveItemEvent event) {
		if (event.getDestination().getType() == InventoryType.FURNACE && event.getItem().isSimilar(ironIngot)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (event.getInventory().getType() == InventoryType.FURNACE && !event.getInventory().getTitle().equals("용광로")) {
			if ((event.getRawSlot() == 0 && event.getCursor() != null && event.getCursor().isSimilar(ironIngot)) ||
					(event.isShiftClick() && event.getCurrentItem() != null && event.getCurrentItem().isSimilar(ironIngot))) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void drag(InventoryDragEvent event) {
		if (event.getInventory().getType() == InventoryType.FURNACE && !event.getInventory().getTitle().equals("용광로")) {
			if (event.getRawSlots().contains(0) && event.getOldCursor() != null && event.getOldCursor().isSimilar(ironIngot)) {
				event.setCancelled(true);
			}
		}
	}
	
	public FurnacePlayer getFurnacePlayer(Player player) {
		if (this.playerMap.containsKey(player.getUniqueId())) {
			return this.playerMap.get(player.getUniqueId());
		} else {
			FurnacePlayer fp = new FurnacePlayer(this, player);
			fp.loadData();
			
			playerMap.put(player.getUniqueId(), fp);
			
			return fp;
		}
	}
	
	private class FurnaceTask extends BukkitRunnable {
		private final int updateDelay;
		private int updateTicks;
		
		public FurnaceTask(int updateDelay) {
			this.runTaskTimer(EfeFurnace.this, updateDelay, updateDelay);
			this.updateDelay = updateDelay;
		}
		
		@Override
		public void run() {
			this.updateTicks ++;
			
			for (Player player : EfeFurnace.this.getServer().getOnlinePlayers()) {
				FurnacePlayer fp = getFurnacePlayer(player);
				
				for (int i = 0; i <this.updateDelay; i ++) {
					fp.getFurnace().c();
				}
				
				if (this.updateTicks > (400 / this.updateDelay)) {
					fp.saveData();
				}
			}
			
			if (this.updateTicks > (400 / this.updateDelay)) {
				this.updateTicks = 0;
			}
		}
	}
}
