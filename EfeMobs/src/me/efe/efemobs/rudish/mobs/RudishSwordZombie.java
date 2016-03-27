package me.efe.efemobs.rudish.mobs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class RudishSwordZombie implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
		e.getLivingEntity().getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
		e.getLivingEntity().getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		Material type = Material.DIAMOND_SWORD;
		double health = victim.getLivingEntity().getHealth();
		
		if (health <= 45) type = Material.IRON_SWORD;
		if (health <= 30) type = Material.GOLD_SWORD;
		if (health <= 20) type = Material.STONE_SWORD;
		if (health <= 10) type = Material.WOOD_SWORD;
		
		victim.getLivingEntity().getEquipment().setItemInHand(new ItemStack(type));
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}