package me.efe.efemobs.rudish.mobs;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class RudishWitch implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1));
		
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 127));
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, final ActiveMob victim, Player damager) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				victim.getType().applyMobVolatileOptions(victim);
			}
		}, 1L);
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}