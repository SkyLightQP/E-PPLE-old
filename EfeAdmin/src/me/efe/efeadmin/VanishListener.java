package me.efe.efeadmin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class VanishListener implements Listener {
	public EfeAdmin plugin;
	
	public VanishListener(EfeAdmin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		if (!e.getPlayer().hasPermission("epple.admin")) {
			for (Player p : plugin.vanishers) {
				e.getPlayer().hidePlayer(p);
			}
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if (plugin.vanishers.contains(e.getPlayer())) {
			e.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
			plugin.vanishers.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void pickup(PlayerPickupItemEvent e) {
		if (plugin.vanishers.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (plugin.vanishers.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void interactEntity(PlayerInteractEntityEvent e) {
		if (plugin.vanishers.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void commandPreprocess(PlayerCommandPreprocessEvent e) {
		if (plugin.vanishers.contains(e.getPlayer()) && !e.getMessage().startsWith("/moderator") && !e.getMessage().startsWith("/중재자") &&
				!e.getMessage().startsWith("/중재") && !e.getMessage().startsWith("/moderate") && !e.getMessage().startsWith("/mod")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§c▒§r 관전 모드를 해제해주세요.");
		}
	}
	
	@EventHandler
	public void target(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player && plugin.vanishers.contains(e.getTarget())) {
			e.setCancelled(true);
		}
	}
}