package me.efe.efemobs.rudish.mobs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.slikey.effectlib.util.ParticleEffect;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class RudishMirrorZombie implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		e.getLivingEntity().getEquipment().setItemInHand(new ItemStack(Material.THIN_GLASS));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		EntityEquipment equip = damager.getLivingEntity().getEquipment();
		PlayerInventory inv = victim.getInventory();
		
		if (inv.getHelmet() == null || inv.getHelmet().getType() == Material.AIR)
			equip.setHelmet(new ItemStack(Material.APPLE));
		else
			equip.setHelmet(inv.getHelmet());
		
		equip.setChestplate(inv.getChestplate());
		equip.setLeggings(inv.getLeggings());
		equip.setBoots(inv.getBoots());
		equip.setItemInHand(inv.getItemInHand());
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		ItemStack handV = victim.getLivingEntity().getEquipment().getItemInHand();
		ItemStack handD = damager.getItemInHand();
		
		if (handV == null || handD == null) return;
		
		if (handV.getType() != handD.getType()) {
			damager.damage(e.getDamage(), victim.getEntity().getBukkitEntity());
			
			e.setDamage(0.0D);
			
			damager.playSound(damager.getLocation(), Sound.ZOMBIE_METAL, 1.0F, 1.0F);
			
			ParticleEffect.SMOKE_NORMAL.display(0.1F, 0.1F, 0.1F, 0.1F, 100, victim.getEntity().getBukkitEntity().getLocation().add(0.5D, 1.5D, 0.5D), 32);
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}