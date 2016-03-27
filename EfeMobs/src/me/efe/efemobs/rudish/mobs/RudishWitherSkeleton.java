package me.efe.efemobs.rudish.mobs;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RudishWitherSkeleton implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		if (Math.random() <= 0.5) {
			Skeleton skeleton = (Skeleton) e.getEntity();
			
			skeleton.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));
		}
		
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, final ActiveMob victim, final Player damager) {
		if (victim.getLivingEntity().getEquipment().getHelmet().getType() == Material.AIR) {
			if (victim.getLivingEntity().getHealth() - e.getFinalDamage() <= 0.0D || Math.random() > 0.2) return;
			
			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
			victim.getLivingEntity().getEquipment().setHelmet(item);
			
		} else if (victim.getLivingEntity().getEquipment().getHelmet().getType() == Material.SKULL_ITEM) {
			
			if (e.getDamager() instanceof Projectile && e.getDamager().getLocation().getY() - victim.getLivingEntity().getLocation().getY() > 1.4D) {
				victim.getLivingEntity().getEquipment().setHelmet(new ItemStack(Material.AIR));
				
				victim.getLivingEntity().getWorld().playEffect(victim.getLivingEntity().getEyeLocation(), Effect.STEP_SOUND, Material.SKULL);
			} else {
				e.setCancelled(true);
			}
			
		}
	}
	
	
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}