package me.efe.efemobs.rudish.mobs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class RudishMarioZombie implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		e.getLivingEntity().getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		Zombie zombie = (Zombie) damager.getLivingEntity();
		
		zombie.setBaby(false);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		Zombie zombie = (Zombie) victim.getLivingEntity();
		
		zombie.setBaby(true);
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}