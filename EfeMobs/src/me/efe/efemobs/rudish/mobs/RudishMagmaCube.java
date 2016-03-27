package me.efe.efemobs.rudish.mobs;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.entity.Entity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class RudishMagmaCube implements RudishMob {
	
	@Override
	public void onSpawn(final MythicMobSpawnEvent e) {
		if (!e.getMobType().getInternalName().equals("8F_MagmaCube2")) {
			Entity entity = MobSpawner.SpawnMythicMob("8F_MagmaCube2", e.getLocation());
			
			e.getEntity().setPassenger(entity);
		}
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, final ActiveMob damager, Player victim) {
		e.setDamage(0.5D);

		final int tick = damager.getLivingEntity().getNoDamageTicks();
		damager.getLivingEntity().setNoDamageTicks(0);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				damager.getLivingEntity().setNoDamageTicks(tick);
			}
		}, 1L);
		
		if (victim.getFireTicks() < 60) {
			victim.setFireTicks(60);
		}
		
		if (victim.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
			victim.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		if (e.getDamager() instanceof SmallFireball) {
			e.setCancelled(true);
			return;
		}
		
		if (e.getEntity().getPassenger() != null && !e.getEntity().getPassenger().isDead()) {
			MagmaCube bottom = (MagmaCube) victim.getEntity().getBukkitEntity();
			MagmaCube top = (MagmaCube) bottom.getPassenger();
			
			if (bottom.getSize() == 2) {
				top.setSize(top.getSize() + 1);
				return;
			}
			
			bottom.setSize(bottom.getSize() - 1);
			top.setSize(top.getSize() + 1);
			
		} else if (e.getEntity().getVehicle() != null && !e.getEntity().getVehicle().isDead()) {
			MagmaCube top = (MagmaCube) victim.getEntity().getBukkitEntity();
			MagmaCube bottom = (MagmaCube) top.getVehicle();
			
			if (top.getSize() == 2) {
				bottom.setSize(top.getSize() + 1);
				return;
			}
			
			top.setSize(top.getSize() - 1);
			bottom.setSize(bottom.getSize() + 1);
			
		} else {
			MagmaCube magmaCube = (MagmaCube) victim.getEntity().getBukkitEntity();
			
			if (magmaCube.getSize() == 2) {
				return;
			}
			
			magmaCube.setSize(magmaCube.getSize() - 1);
		}
		
		e.setDamage(0.0D);
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}