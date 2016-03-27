package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.slikey.effectlib.util.ParticleEffect;
import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish1FBabyZombie implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().getEquipment().setArmorContents(new ItemStack[]{null, null, null, null});
		
		switch (plugin.util.random.nextInt(4)) {
		case 0:
			e.getLivingEntity().getEquipment().setItemInHand(new ItemStack(Material.TNT));
			break;
		case 1:
			e.getLivingEntity().getEquipment().setItemInHand(new ItemStack(Material.ROTTEN_FLESH));
			break;
		case 2:
			e.getLivingEntity().getEquipment().setItemInHand(new ItemStack(Material.SLIME_BALL));
			break;
		case 3:
			e.getLivingEntity().getEquipment().setItemInHand(new ItemStack(Material.FIREBALL));
			break;
		}
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		e.setDamage(0.0D);
		
		ItemStack item = damager.getLivingEntity().getEquipment().getItemInHand();
		
		if (item == null) return;
		
		if (item.getType() == Material.TNT) {
			Location loc = damager.getEntity().getBukkitEntity().getLocation();
			
			victim.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 2.0F, false, false);
			
			damager.getEntity().getBukkitEntity().remove();
		}
		
		if (item.getType() == Material.ROTTEN_FLESH) {
			victim.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20*10, 0));
			
			ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.ROTTEN_FLESH, (byte) 0), 0.1F, 0.1F, 0.1F, 1.0F, 30, victim.getLocation().add(0, 1.5, 0), 32);
		}
		
		if (item.getType() == Material.SLIME_BALL) {
			victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*7, 2));
			
			ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.SLIME_BALL, (byte) 0), 0.1F, 0.1F, 0.1F, 1.0F, 30, victim.getLocation().add(0, 1.5, 0), 32);
		}
		
		if (item.getType() == Material.FIREBALL) {
			victim.setFireTicks(20 * 3);
			
			ParticleEffect.FLAME.display(0.3F, 0.3F, 0.3F, 0.0F, 10, victim.getLocation().add(0, 1.5, 0), 32);
			ParticleEffect.LAVA.display(0.3F, 0.3F, 0.3F, 0.0F, 10, victim.getLocation().add(0, 1.5, 0), 32);
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}