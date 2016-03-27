package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish8FROCube implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		if (Math.random() <= 0.3) {
			final Location loc = victim.getEntity().getBukkitEntity().getLocation().clone();
			
			launchTNT(loc, 7);
			
			for (int i = 0; i < 3; i ++) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						int x = plugin.util.rand(-5, 5);
						int y = plugin.util.rand(0, 10);
						int z = plugin.util.rand(-5, 5);
						
						loc.getWorld().createExplosion(loc.getX() + x, loc.getY() + y, loc.getZ() + z, 10.0F, false, false);
					}
				}, i * 5L);
			}
			
			victim.getEntity().getBukkitEntity().remove();
		}
	}
	
	private void launchTNT(final Location loc, final int amount) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				TNTPrimed tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
				
				tnt.setFuseTicks(40);
				tnt.setVelocity(new Vector(Math.random() * 2 - 1, 0.3, Math.random() * 2 - 1).multiply(1.5D));
				
				
				if (amount == 0) return;
				
				launchTNT(loc, amount - 1);
			}
		}, 5L);
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}