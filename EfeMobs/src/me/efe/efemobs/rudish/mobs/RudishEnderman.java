package me.efe.efemobs.rudish.mobs;

import java.util.Random;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;

public class RudishEnderman implements RudishMob {
	private Random random = new Random();
	private Material[] blocks = new Material[]{Material.WOOD, Material.COBBLESTONE, Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK};
	
	private MaterialData getRandomBlock() {
		return new MaterialData(blocks[random.nextInt(blocks.length)]);
	}
	
	private boolean isCorrectTool(MaterialData data, ItemStack hand) {
		if (hand == null) return false;
		
		if (data.getItemType() == Material.WOOD && hand.getType().name().startsWith("WOOD_")) return true;
		if (data.getItemType() == Material.COBBLESTONE && hand.getType().name().startsWith("STONE_")) return true;
		if (data.getItemType() == Material.IRON_BLOCK && hand.getType().name().startsWith("IRON_")) return true;
		if (data.getItemType() == Material.GOLD_BLOCK && hand.getType().name().startsWith("GOLD_")) return true;
		if (data.getItemType() == Material.DIAMOND_BLOCK && hand.getType().name().startsWith("DIAMOND_")) return true;
		
		return false;
	}
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		Enderman enderman = (Enderman) e.getLivingEntity();
		
		enderman.setCarriedMaterial(getRandomBlock());
		
		enderman.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
	}

	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		Enderman enderman = (Enderman) victim.getLivingEntity();
		
		if (victim.getLivingEntity().getHealth() <= 20.0D) {
			if (!victim.getLivingEntity().hasMetadata("angry")) {
				enderman.getWorld().playEffect(enderman.getLocation().add(0.5D, 1.5D, 0.5D), Effect.STEP_SOUND, enderman.getCarriedMaterial().getItemType());
				
				enderman.setCarriedMaterial(new MaterialData(Material.AIR));
				
				enderman.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
				enderman.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0));
				enderman.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
				
				Location loc = damager.getLocation();
				Vector dir = loc.getDirection().normalize();
				
				dir.multiply(2);
				loc.subtract(dir);
				
				enderman.teleport(loc);
				
				enderman.setMetadata("angry", new FixedMetadataValue(plugin, "angry"));
			}
		} else {
			if (!isCorrectTool(enderman.getCarriedMaterial(), damager.getItemInHand())) {
				e.setDamage(0.0D);
				
				ParticleEffect.SMOKE_NORMAL.display(0.1F, 0.1F, 0.1F, 0.1F, 100, enderman.getLocation().add(0.5D, 1.5D, 0.5D), 32);
			}
			
			enderman.setCarriedMaterial(getRandomBlock());
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}