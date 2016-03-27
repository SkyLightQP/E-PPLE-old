package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.slikey.effectlib.util.ParticleEffect;
import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish10FClone implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		e.setDamage(0.0D);
		
		ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 30, victim.getEntity().getBukkitEntity().getLocation(), 32);
		
		victim.getEntity().getBukkitEntity().remove();
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}