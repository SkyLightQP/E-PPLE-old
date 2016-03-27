package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.slikey.effectlib.util.ParticleEffect;
import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish7FWraith implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getEntity().setMetadata("wraith", new FixedMetadataValue(plugin, ""));
		
		e.getLivingEntity().getEquipment().setHelmet(new ItemStack(Material.SKULL_ITEM, 1, (short) 1));
		e.getLivingEntity().getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		e.getLivingEntity().getEquipment().setItemInHand(new ItemStack(Material.STONE_HOE));
		
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 0));
		
		victim.playSound(victim.getLocation(), Sound.BLAZE_DEATH, 1.0F, 1.0F);
		ParticleEffect.EXPLOSION_HUGE.display(0.0F, 0.0F, 0.0F, 0.0F, 1, damager.getEntity().getBukkitEntity().getLocation(), 32);
		
		damager.getEntity().getBukkitEntity().remove();
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		damager.damage(e.getDamage() * 10);
		
		e.setDamage(0.0D);
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}