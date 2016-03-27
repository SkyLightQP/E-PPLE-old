package me.efe.efeserver.additory;

import java.util.HashMap;

import me.efe.efeserver.EfeServer;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class ChairManager {
	public EfeServer plugin;
	public HashMap<Player, ArmorStand> chairs = new HashMap<Player, ArmorStand>();
	
	public ChairManager(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	public void clear() {
		for (ArmorStand armorStand : chairs.values()) {
			
			Chunk chunk = armorStand.getLocation().getChunk();
			if (!chunk.isLoaded())
				chunk.load();
			
			armorStand.remove();
		}
	}
	
	public void setChair(Player player) {
		if (chairs.containsKey(player))
			return;
		
		Location loc = player.getLocation().subtract(0, 1.65, 0);
		ArmorStand armorStand = player.getWorld().spawn(loc, ArmorStand.class);
		
		armorStand.setGravity(false);
		armorStand.setVisible(false);
		armorStand.teleport(loc);
		armorStand.setPassenger(player);
		armorStand.setMetadata("chair", new FixedMetadataValue(plugin, new ChairTask(player).runTaskTimer(plugin, 1, 1)));
		
		chairs.put(player, armorStand);
		
		player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
	}
	
	public void removeChair(final Player player) {
		if (!chairs.containsKey(player))
			return;
		
		ArmorStand armorStand = chairs.get(player);
		final Location loc = armorStand.getLocation();
		
		player.eject();
		armorStand.remove();
		chairs.remove(player);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (player == null || !player.isOnline())
					return;
				
				loc.setYaw(player.getLocation().getYaw());
				loc.setPitch(player.getLocation().getPitch());
				
				player.teleport(loc.add(0, 2, 0));
				player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
			}
		}, 3L);
	}
	
	private class ChairTask extends BukkitRunnable {
		private final Player player;
		
		public ChairTask(Player player) {
			this.player = player;
		}
		
		@Override
		public void run() {
			if (player == null || !chairs.containsKey(player)) {
				cancel();
				return;
			}
			
			if (player.isDead() || player.getVehicle() == null || !player.getVehicle().hasMetadata("chair")) {
				removeChair(player);
				cancel();
				return;
			}
		}
	}
}