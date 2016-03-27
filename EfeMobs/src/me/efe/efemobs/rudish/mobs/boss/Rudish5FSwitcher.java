package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish5FSwitcher implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		int handSlot = victim.getInventory().getHeldItemSlot();
		int itemSlot = plugin.util.rand(0, 6);
		
		if (handSlot > 6) return;
		
		ItemStack hand = victim.getItemInHand().clone();
		ItemStack item = victim.getInventory().getItem(itemSlot).clone();
		
		victim.getInventory().setItem(itemSlot, hand);
		victim.getInventory().setItem(handSlot, item);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}