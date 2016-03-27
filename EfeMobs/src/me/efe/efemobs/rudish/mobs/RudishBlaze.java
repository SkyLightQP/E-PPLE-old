package me.efe.efemobs.rudish.mobs;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RudishBlaze implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0));
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		if (!victim.getLivingEntity().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			e.setDamage(0.0D);
			victim.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}