package me.efe.efemobs.rudish.mobs;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class RudishDrainZombie implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		e.getLivingEntity().getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		double hp = damager.getLivingEntity().getHealth() + e.getFinalDamage();
		
		if (hp > damager.getLivingEntity().getMaxHealth())
			hp = damager.getLivingEntity().getMaxHealth();
		
		damager.getLivingEntity().setHealth(hp);
		
		ParticleEffect.HEART.display(0.3F, 0.3F, 0.3F, 0.0F, 3, damager.getLivingEntity().getLocation().add(0, 1.5, 0), 32);
		
		for (Entity entity : damager.getEntity().getBukkitEntity().getNearbyEntities(5.0D, 5.0D, 5.0D)) {
			if (entity instanceof Zombie) {
				Zombie zombie = (Zombie) entity;
				double health = zombie.getHealth() + e.getFinalDamage();
				
				if (health > zombie.getMaxHealth())
					health = zombie.getMaxHealth();
				
				zombie.setHealth(health);
				
				ParticleEffect.HEART.display(0.3F, 0.3F, 0.3F, 0.0F, 3, damager.getLivingEntity().getLocation().add(0, 1.5, 0), 32);
			}
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}