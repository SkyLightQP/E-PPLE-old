package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish8FSmoker implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		if (victim.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
			victim.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			
			victim.playSound(victim.getLocation(), Sound.GHAST_FIREBALL, 10.0F, 1.0F);
			ActionBarAPI.sendActionBar(victim, "§c당신의 화염 저항 효과가 제거되었습니다!");
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}