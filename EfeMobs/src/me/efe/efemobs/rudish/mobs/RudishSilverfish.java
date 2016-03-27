package me.efe.efemobs.rudish.mobs;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class RudishSilverfish implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}