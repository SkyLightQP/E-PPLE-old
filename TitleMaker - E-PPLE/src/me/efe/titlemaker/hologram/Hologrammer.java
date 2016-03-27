package me.efe.titlemaker.hologram;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;
import me.efe.titlemaker.TitleMaker;
import me.efe.titlemaker.TitleManager;
import me.efe.titlemaker.api.TitleAPI;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Hologrammer implements Listener {
	public TitleMaker plugin;
	public Random random = new Random();
	public HashMap<UUID, Hologram> holograms = new HashMap<UUID, Hologram>();
	public BukkitTask task;
	
	public Hologrammer(TitleMaker plugin) {
		this.plugin = plugin;
		
		task = new TimeTask().runTaskTimer(plugin, 1, 1);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		for (Hologram holo : holograms.values()) {
			holo.addReceiver(e.getPlayer());
		}
		
		apply(e.getPlayer());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if (hasHologram(e.getPlayer())) {
			removeHologram(e.getPlayer());
		}
		
		for (Hologram holo : holograms.values()) {
			holo.removeReceiver(e.getPlayer());
		}
	}
	
	@EventHandler
	public void death(PlayerDeathEvent e) {
		if (hasHologram(e.getEntity())) {
			removeHologram(e.getEntity());
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		apply(e.getPlayer());
	}
	
	@EventHandler
	public void changedWorld(PlayerChangedWorldEvent e) {
		apply(e.getPlayer());
		
		for (Hologram holo : holograms.values()) {
			if (holo.getOwner().equals(e.getPlayer()))
				continue;
			
			if (e.getPlayer().getWorld().equals(holo.getWorld())) {
				holo.addReceiver(e.getPlayer());
			} else {
				holo.removeReceiver(e.getPlayer());
			}
		}
	}
	
	public void apply(Player p) {
		String main = TitleManager.getMainTitle(p);
		
		if (!canSpawnHologram(p)) {
			
			if (hasHologram(p)) {
				removeHologram(p);
			}
			
			return;
		}
		
		if (hasHologram(p)) {
			removeHologram(p);
		}
		
		createHologram(p, main);
	}
	
	public boolean canSpawnHologram(Player p) {
		String main = TitleManager.getMainTitle(p);
		
		return main != null && !main.isEmpty() && !p.hasPotionEffect(PotionEffectType.INVISIBILITY) && !TitleAPI.hasInvisibleHologram(p) &&
				p.getGameMode() != GameMode.SPECTATOR;
	}
	
	public void createHologram(Player owner, String title) {
		Hologram holo = new Hologram(title, random.nextInt(5000)+500, owner);
		
		for (Player all : owner.getWorld().getPlayers()) {
			holo.addReceiver(all);
		}
		
		holograms.put(owner.getUniqueId(), holo);
	}
	
	public void removeHologram(Player owner) {
		if (!holograms.containsKey(owner.getUniqueId())) return;
		
		Hologram holo = holograms.get(owner.getUniqueId());
		
		for (Player all : holo.getWorld().getPlayers()) {
			holo.removeReceiver(all);
		}
		
		holograms.remove(owner.getUniqueId());
	}
	
	public boolean hasHologram(Player p) {
		return holograms.containsKey(p.getUniqueId());
	}
	
	public void reset() {
		task.cancel();
		
		for (Player all : EfeUtils.player.getOnlinePlayers()) {
			for (Hologram holo : holograms.values()) {
				if (!all.getWorld().equals(holo.getWorld())) continue;
				
				holo.hide(all);
			}
		}
		
		holograms.clear();
	}
	
	private class TimeTask extends BukkitRunnable {
		
		@Override
		public void run() {
			for (Player p : EfeUtils.player.getOnlinePlayers()) {
				if (p == null || p.isDead()) continue;
				
				if (!canSpawnHologram(p)) {
					
					if (hasHologram(p)) {
						removeHologram(p);
					}
					
					continue;
				}
				
				if (!hasHologram(p)) {
					String main = TitleManager.getMainTitle(p);
					
					createHologram(p, main);
				} else {
					holograms.get(p.getUniqueId()).update();
				}
			}
		}
	}
}