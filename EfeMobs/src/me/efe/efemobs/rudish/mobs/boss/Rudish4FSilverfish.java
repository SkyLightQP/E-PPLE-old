package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.efe.efemobs.Expedition;
import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish4FSilverfish implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getEntity().setMetadata("boss_silverfish", new FixedMetadataValue(plugin, ""));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, final ActiveMob damager, final Player victim) {
		if (victim.getVehicle() != null) {
			e.setCancelled(true);
			return;
		}
		
		if (victim.getVehicle() == null) {
			damager.getEntity().getBukkitEntity().setPassenger(victim);
			
			plugin.vch.setFollowRange(damager.getEntity().getBukkitEntity(), 70.0D);
			plugin.vch.setMobSpeed(damager.getEntity().getBukkitEntity(), 0.4D);
			
			int channel = plugin.bossListener.getChannel(damager.getEntity().getBukkitEntity().getLocation());
			plugin.vch.setTarget(damager.getLivingEntity(), plugin.bossListener.getBoss(channel).getLivingEntity());
			
			new TimeTask(damager.getEntity().getBukkitEntity(), victim).runTaskTimer(plugin, 1L, 1L);
		}
	}
	
	private class TimeTask extends BukkitRunnable {
		private final Entity entity;
		private final Player p;
		
		public TimeTask(Entity entity, Player p) {
			this.entity = entity;
			this.p = p;
			
			p.setMetadata("boss_genocide", new FixedMetadataValue(plugin, ""));
		}
		
		@Override
		public void run() {
			if (p == null || p.isDead() || entity == null || entity.isDead()) {
				if (p != null)
					p.removeMetadata("boss_genocide", plugin);
				
				cancel();
				return;
			}
			
			if (p.getVehicle() == null) {
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						if (p == null || p.isDead() || p.getVehicle() != null || entity == null || entity.isDead() || entity.getPassenger() != null) return;
						
						entity.setPassenger(p);
					}
				}, 1L);
			}
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		final int channel = plugin.bossListener.getChannel(victim.getEntity().getBukkitEntity().getLocation());
		final Expedition exp = plugin.bossListener.expMap.get(channel);
		
		if (exp.getData() == null) {
			exp.setData(1);
		} else {
			exp.setData((int) exp.getData() + 1);
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		if (e.getEntity().getPassenger() != null) {
			final Location loc = e.getEntity().getLocation();
			final Entity passenger = e.getEntity().getPassenger();
			passenger.eject();
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					passenger.teleport(loc.add(0, 1, 0));
				}
			}, 20L);
		}
	}
}