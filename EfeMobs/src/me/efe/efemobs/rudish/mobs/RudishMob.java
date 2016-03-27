package me.efe.efemobs.rudish.mobs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.efe.efemobs.EfeMobs;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public interface RudishMob {
	public EfeMobs plugin = (EfeMobs) Bukkit.getPluginManager().getPlugin("EfeMobs");
	
	public void onSpawn(MythicMobSpawnEvent e);
	
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim);
	
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager);
	
	public void onDeath(MythicMobDeathEvent e);
}