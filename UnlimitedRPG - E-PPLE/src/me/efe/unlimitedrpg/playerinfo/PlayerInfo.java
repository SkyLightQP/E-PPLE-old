package me.efe.unlimitedrpg.playerinfo;

import java.util.ArrayList;
import java.util.List;

import me.efe.unlimitedrpg.UnlimitedRPG;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInfo implements Listener {
	public UnlimitedRPG plugin;
	public List<String> clicker = new ArrayList<String>();
	
	public PlayerInfo(UnlimitedRPG plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void click(PlayerInteractEntityEvent e) {
		if (e.getRightClicked().getType().equals(EntityType.PLAYER)) {
			if (clicker.contains(e.getPlayer().getName())) {
				clicker.remove(e.getPlayer().getName());
				plugin.playerInfoGUI.openGUI(e.getPlayer(), (Player) e.getRightClicked());
			} else {
				clickTimer(e.getPlayer().getName());
			}
		}
	}
	
	public void clickTimer(final String str) {
		clicker.add(str);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (clicker.contains(str)) {
					clicker.remove(str);
				}
			}
		}, 20*1L);
	}
	
	public static void openGUI(Player target, Player viewer) {
		UnlimitedRPG.getInstance().playerInfoGUI.openGUI(viewer, target);
	}
}