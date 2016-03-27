package me.efe.skilltree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.efe.efeserver.util.Scoreboarder;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DelayUtils {
	private static SkillTree plugin;
	private static HashMap<UUID, List<String>> delays = new HashMap<UUID, List<String>>();
	
	public static void init(SkillTree pl) {
		plugin = pl;
	}
	
	public static void delay(final Player p, final String skill, final String name, int sec) {
		final UUID id = p.getUniqueId();
		
		if (delays.containsKey(id)) {
			delays.get(id).add(skill);
			
		} else {
			List<String> list = new ArrayList<String>();
			list.add(skill);
			
			delays.put(id, list);
		}
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (!delays.containsKey(id) || !delays.get(id).contains(skill)) return;
				
				delays.get(id).remove(skill);
				
				if (p != null) {
					p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.75F);
					Scoreboarder.message(p, new String[]{"§a§l>>§a "+name+"", "§a재사용 가능"}, 2);
				}
			}
		}, 20L * sec);
	}
	
	public static boolean isDelayed(Player p, String skill) {
		if (!delays.containsKey(p.getUniqueId())) return false;
		
		return delays.get(p.getUniqueId()).contains(skill);
	}
}