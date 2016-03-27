package me.efe.efemobs.rudish;

import java.util.ArrayList;
import java.util.List;

import me.efe.efemobs.EfeMobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.connorlinfoot.titleapi.TitleAPI;

import de.slikey.effectlib.util.ParticleEffect;

public class TeleportGUI implements Listener {
	public EfeMobs plugin;
	public List<String> users = new ArrayList<String>();
	public List<Location> cores = new ArrayList<Location>();
	public Location[] centers;
	public byte[] colors = new byte[]{14, 1, 4, 5, 3, 9, 11, 10, 2, 6};
	
	public TeleportGUI(EfeMobs plugin) {
		this.plugin = plugin;
		
		cores.add(new Location(plugin.epple.world, 1299, 49, 1283));
		cores.add(new Location(plugin.epple.world, 1283, 49, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 49, 1315));
		cores.add(new Location(plugin.epple.world, 1315, 49, 1299));
		
		cores.add(new Location(plugin.epple.world, 1299, 92, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 108, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 125, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 140, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 156, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 172, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 189, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 205, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 222, 1299));
		cores.add(new Location(plugin.epple.world, 1299, 233, 1299));
		
		centers = new Location[]{
				new Location(plugin.epple.world, 1299, 46, 1283),
				new Location(plugin.epple.world, 1299, 90, 1299),
				new Location(plugin.epple.world, 1299, 106, 1299),
				new Location(plugin.epple.world, 1299, 123, 1299),
				new Location(plugin.epple.world, 1299, 138, 1299),
				new Location(plugin.epple.world, 1299, 154, 1299),
				new Location(plugin.epple.world, 1299, 170, 1299),
				new Location(plugin.epple.world, 1299, 187, 1299),
				new Location(plugin.epple.world, 1299, 203, 1299),
				new Location(plugin.epple.world, 1299, 220, 1299),
				new Location(plugin.epple.world, 1299, 235, 1299),
		};
		
		runTask(0);
	}
	
	public void runTask(final int index) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@SuppressWarnings("deprecation")
			public void run() {
				for (Location loc : cores) {
					loc.getBlock().setData(colors[index]);
				}
				
				runTask(index + 1 >= colors.length ? 0 : index + 1);
			}
		}, 10L);
	}
	
	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		if (cores.contains(e.getBlock().getLocation())) {
			e.setCancelled(true);
			plugin.util.updateInv(e.getPlayer());
			
			if (!new UserData(e.getPlayer()).hasPlayedBefore()) {
				TitleAPI.sendTitle(e.getPlayer(), 20, 100, 20, "§c처음이신가요?", "입장하기 전, NPC의 가이드를 확인해주세요!");
				return;
			}
			
			openGUI(e.getPlayer());
		}
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*3, "§5▒§r 이동");
		UserData data = new UserData(p);
		
		int floor = EfeMobs.getFloor(p.getLocation());
		
		for (int i : new int[]{0, 9, 18, 8, 17, 26})
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), new String[]{}));
		for (int i : new int[]{1, 10, 19, 7, 16, 25})
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), new String[]{}));
		for (int i : new int[]{2, 11, 20, 6, 15, 24})
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.IRON_FENCE), new String[]{}));
		
		if (floor < 10)
			inv.setItem(4, plugin.util.createDisplayItem("§d§l"+(floor + 1)+"F", new ItemStack(Material.ENDER_PEARL), new String[]{}));
		if (floor + 1 != data.getMaxFloor() && floor != data.getMaxFloor())
			inv.setItem(5, plugin.util.createDisplayItem("§d§l"+data.getMaxFloor()+"F", new ItemStack(Material.ENDER_PEARL), new String[]{"", "§9클릭하면 이동합니다."}));
		if (floor != 0)
			inv.setItem(13, plugin.util.createDisplayItem("§d§lLobby", new ItemStack(Material.EYE_OF_ENDER), new String[]{"", "§9클릭하면 귀환합니다."}));
		if (floor > 1)
			inv.setItem(22, plugin.util.createDisplayItem("§d§l"+(floor - 1)+"F", new ItemStack(Material.ENDER_PEARL), new String[]{"", "§9클릭하면 이동합니다."}));
		if (floor == 1)
			inv.setItem(22, plugin.util.createDisplayItem("§d§lLobby", new ItemStack(Material.ENDER_PEARL), new String[]{"", "§9클릭하면 귀환합니다."}));
		
		
		if (floor < 10) {
			ItemStack item = inv.getItem(4);
			
			plugin.util.addLore(item, "");
			
			if (data.getMaxFloor() >= floor + 1) {
				item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
				
				plugin.util.addLore(item, "§9클릭하면 이동합니다.");
			} else {
				plugin.util.addLore(item, "§c잠겨진 층입니다.");
			}
			
			inv.setItem(4, item);
		}
		
		if (inv.getItem(5) != null)
			inv.setItem(5, plugin.util.enchant(inv.getItem(5), Enchantment.SILK_TOUCH, 100));	
		if (inv.getItem(22) != null)
			inv.setItem(22, plugin.util.enchant(inv.getItem(22), Enchantment.SILK_TOUCH, 100));
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 27) return;
		
		Player p = (Player) e.getWhoClicked();
		UserData data = new UserData(p);
		
		int floor = EfeMobs.getFloor(p.getLocation());
		
		if (e.getRawSlot() == 4 && data.getMaxFloor() >= floor + 1) {
			ParticleEffect.PORTAL.display(0.1F, 0.1F, 0.1F, 1.0F, 500, p.getLocation().add(0, 1.5, 0), 32);
			
			Location loc = centers[floor + 1].clone();
			
			loc.add(0.5, 0, 0.5);
			loc.setPitch(p.getLocation().getPitch());
			loc.setYaw(p.getLocation().getYaw());
			
			p.teleport(loc);
			p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		} else if (e.getRawSlot() == 5) {
			ParticleEffect.PORTAL.display(0.1F, 0.1F, 0.1F, 1.0F, 500, p.getLocation().add(0, 1.5, 0), 32);
			
			Location loc = centers[data.getMaxFloor()].clone();
			
			loc.add(0.5, 0, 0.5);
			loc.setPitch(p.getLocation().getPitch());
			loc.setYaw(p.getLocation().getYaw());
			
			p.teleport(loc);
			p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		} else if (e.getRawSlot() == 13) {
			ParticleEffect.PORTAL.display(0.1F, 0.1F, 0.1F, 1.0F, 500, p.getLocation().add(0, 1.5, 0), 32);
			
			Location loc = centers[0].clone();
			
			loc.add(0.5, 0, 0.5);
			loc.setPitch(p.getLocation().getPitch());
			loc.setYaw(p.getLocation().getYaw());
			
			p.teleport(loc);
			p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		} else if (e.getRawSlot() == 22) {
			ParticleEffect.PORTAL.display(0.1F, 0.1F, 0.1F, 1.0F, 500, p.getLocation().add(0, 1.5, 0), 32);
			
			Location loc = centers[floor - 1].clone();
			
			loc.add(0.5, 0, 0.5);
			loc.setPitch(p.getLocation().getPitch());
			loc.setYaw(p.getLocation().getYaw());
			
			p.teleport(loc);
			p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}