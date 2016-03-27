package me.efe.efemobs.rudish.mobs;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

public class RudishSlime implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
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
		
		if (Math.random() <= 0.2) {
			victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*3, 0));
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		Slime slime = (Slime) victim.getLivingEntity();
		
		if (slime.getSize() > 1) {
			e.setDamage(0.0D);
			
			int newSize = slime.getSize() - 1;
			slime.setSize(newSize);
			
			Slime entity = (Slime) MobSpawner.SpawnMythicMob("3F_Slime", slime.getLocation());
			entity.setSize(newSize);
			
			
			Entity passenger = slime.getPassenger();
			
			if (passenger != null) {
				slime.eject();
				entity.setPassenger(passenger);
			}
			
			slime.setPassenger(entity);
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}