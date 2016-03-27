package me.efe.efemobs.rudish.mobs;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.slikey.effectlib.util.ParticleEffect;

public class RudishZombie implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		if (victim.getLivingEntity().getHealth() - e.getFinalDamage() > 0.0D && Math.random() <= 0.3) {
			
			victim.getLivingEntity().getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			
			victim.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0));
			victim.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
			
			ParticleEffect.VILLAGER_ANGRY.display(0.2F, 0.2F, 0.2F, 0.0F, 5, victim.getLivingEntity().getEyeLocation(), 32);
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}