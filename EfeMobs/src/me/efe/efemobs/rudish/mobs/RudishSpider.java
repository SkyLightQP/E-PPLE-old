package me.efe.efemobs.rudish.mobs;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RudishSpider implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0));
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 0));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 0));
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		if (victim.getLivingEntity().getHealth() - e.getFinalDamage() > 0.0D && Math.random() <= 0.3) {
			victim.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 0));
			victim.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 0));
			victim.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}