package me.efe.bloodskin;

import me.efe.efeserver.PlayerData;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BloodListener implements Listener {
	public BloodSkin plugin;
	
	public BloodListener(BloodSkin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void damage(EntityDamageByEntityEvent e) {
		if (plugin.util.getDamager(e) instanceof Player && e.getEntity() instanceof LivingEntity) {
			Player p = (Player) plugin.util.getDamager(e);
			PlayerData data = PlayerData.get(p);
			
			if (data.getBloodSkin() == -1 || data.getBloodSkin() >= data.getBloodSkins().size()) return;
			
			String name = data.getBloodSkins().get(data.getBloodSkin());
			
			plugin.skins.get(name).effect(p, (LivingEntity) e.getEntity());
		}
	}
}