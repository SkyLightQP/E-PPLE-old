package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish5FBabySpider implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getEntity().setMetadata("boss_babyspider", new FixedMetadataValue(plugin, ""));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		LivingEntity entity = (LivingEntity) victim.getEntity().getBukkitEntity().getVehicle();
		
		if (entity == null) return;
		
		entity.damage(e.getDamage(), damager);
		
		e.setDamage(0.0D);
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}