package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish3FDoubleSlimes implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		e.setDamage(0.0D);
		
		if (e.getEntity().getPassenger() != null && !e.getEntity().getPassenger().isDead()) {
			Slime bottom = (Slime) victim.getEntity().getBukkitEntity();
			Slime top = (Slime) bottom.getPassenger();
			
			if (bottom.getSize() == 1) {
				top.setSize(top.getSize() + 1);
				bottom.damage(99999);
				return;
			}
			
			bottom.setSize(bottom.getSize() - 1);
			top.setSize(top.getSize() + 1);
			
		} else if (e.getEntity().getVehicle() != null && !e.getEntity().getVehicle().isDead()) {
			Slime top = (Slime) victim.getEntity().getBukkitEntity();
			Slime bottom = (Slime) top.getVehicle();
			
			if (top.getSize() == 1) {
				bottom.setSize(top.getSize() + 1);
				top.damage(99999);
				return;
			}
			
			top.setSize(top.getSize() - 1);
			bottom.setSize(bottom.getSize() + 1);
			
		} else {
			Slime slime = (Slime) victim.getEntity().getBukkitEntity();
			
			if (slime.getSize() == 1) {
				slime.damage(99999);
				return;
			}
			
			slime.setSize(slime.getSize() - 1);
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}