package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

public class Rudish8FRider implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		Entity entity = MobSpawner.SpawnMythicMob("Minion_8F_BraveChicken", e.getEntity().getLocation());
		
		entity.setPassenger(e.getEntity());
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		int foodLevel = victim.getFoodLevel();
		
		foodLevel -= 3;
		if (foodLevel < 0)
			foodLevel = 0;
		
		victim.setFoodLevel(foodLevel);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}