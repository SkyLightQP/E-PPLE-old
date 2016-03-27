package me.efe.efemobs.rudish.mobs;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

public class RudishIronGolem implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
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
				
				Vector from = entity.getLocation().add(0, 1.5, 0).toVector();
				Vector to = entity.getTarget().getLocation().add(0, 1.0, 0).toVector();
				
				Vector vector = to.subtract(from).multiply(0.5D);
				
				Entity silverfish = MobSpawner.SpawnMythicMob("4F_Silverfish", entity.getLocation().add(0, 1.5, 0).setDirection(vector));
				
				silverfish.setVelocity(vector);
				
				ParticleEffect.EXPLOSION_NORMAL.display(0.1F, 0.1F, 0.1F, 0.1F, 30, entity.getLocation().add(0, 1.5, 0), 32);
				
				skill(entity);
			}
		}, 20L * plugin.util.rand(5, 10));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		e.setDamage(0.0D);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, final ActiveMob victim, final Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}