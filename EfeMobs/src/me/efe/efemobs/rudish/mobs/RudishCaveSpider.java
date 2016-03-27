package me.efe.efemobs.rudish.mobs;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class RudishCaveSpider implements RudishMob {
	
	@Override
	public void onSpawn(final MythicMobSpawnEvent e) {
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0));
		
		skill((Creature) e.getEntity());
	}
	
	public void skill(final Creature entity) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if (entity.isDead() || entity.getTarget() == null) return;
				
				if (entity.getTarget() == null || entity.getTarget().isDead()) {
					skill(entity);
					return;
				}
				
				Vector from = entity.getLocation().toVector();
				Vector to = entity.getTarget().getLocation().toVector();
				
				Vector vector = to.subtract(from).multiply(0.3D);
				
				entity.launchProjectile(Arrow.class, vector);
				
				skill(entity);
			}
		}, 20L * 3);
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		e.setDamage(0.0D);
		
		victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 0));
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		if (victim.getLivingEntity().getHealth() - e.getFinalDamage() > 0.0D && Math.random() <= 0.45) {
			victim.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 0));
			victim.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 0));
			victim.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}