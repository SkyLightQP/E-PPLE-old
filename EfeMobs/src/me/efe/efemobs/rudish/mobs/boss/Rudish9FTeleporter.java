package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.efe.efemobs.ScrollUtils;
import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish9FTeleporter implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		if (Math.random() <= 0.5) {
			int channel = plugin.bossListener.getChannel(damager.getEntity().getBukkitEntity().getLocation());
			
			victim.teleport(ScrollUtils.getUserSpawn(channel));
			victim.playSound(victim.getLocation(), Sound.ENDERMAN_TELEPORT, 10.0F, 1.0F);
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}