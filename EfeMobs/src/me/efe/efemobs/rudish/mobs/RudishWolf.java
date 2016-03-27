package me.efe.efemobs.rudish.mobs;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

public class RudishWolf implements RudishMob {
	
	@Override
	public void onSpawn(final MythicMobSpawnEvent e) {
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
		
		skill((Creature) e.getEntity());
	}
	
	public void skill(final Creature entity) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if (entity == null || entity.isDead()) return;
				
				if (entity.getTarget() == null || entity.getTarget().isDead()) {
					skill(entity);
					return;
				}
				
				Vector from = entity.getLocation().toVector();
				Vector to = entity.getTarget().getLocation().toVector();
				
				Snowball snowball = entity.launchProjectile(Snowball.class);
				
				snowball.setShooter(entity);
				snowball.setVelocity(to.subtract(from).multiply(0.3D));
				
				if (entity.getVehicle() instanceof Snowman) {
					snowball.setShooter(entity);
					snowball.setVelocity(to.subtract(from).multiply(0.3D));
					
					Location loc = snowball.getLocation();
					Vector dir = loc.getDirection().clone().multiply(2);
					
					loc.add(dir);
					snowball.teleport(loc);
					
					from = loc.toVector();
					to = entity.getTarget().getLocation().toVector();
					
					snowball.setVelocity(to.subtract(from).multiply(0.3D));
				}
				
				skill(entity);
			}
		}, 20L * 2);
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		e.setDamage(0.5D);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		if (e.getLivingEntity().getVehicle() instanceof Snowman) {
			final Entity snowman = e.getLivingEntity().getVehicle();
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					Entity wolf = MobSpawner.SpawnMythicMob("2F_Wolf", snowman.getLocation());
					
					snowman.setPassenger(wolf);
				}
			}, 30L);
		}
	}
}