package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.util.ParticleEffect;
import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

public class Rudish5FClone implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getEntity().setMetadata("clone", new FixedMetadataValue(plugin, ""));
		
		Entity entity = MobSpawner.SpawnMythicMob("Minion_5F_BabySpider", e.getEntity().getLocation());
		entity.setMetadata("clone", new FixedMetadataValue(plugin, ""));
		
		e.getEntity().setPassenger(entity);
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		int amount = 0;
		
		int channel = plugin.bossListener.getChannel(victim.getLivingEntity().getLocation());
		ProtectedRegion region = WGBukkit.getRegionManager(plugin.epple.world).getRegion("boss-room_"+channel);
		
		for (Entity entity : plugin.epple.world.getEntities()) {
			final Location loc = entity.getLocation();
			
			if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
				if (entity.hasMetadata("clone"))
					amount ++;
			}
		}
		
		if (amount < 20) {
			for (int i = 0; i < 2; i ++) {
				MobSpawner.SpawnMythicMob("Minion_5F_Clone", victim.getLocation());
			}
		}
		
		ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 30, victim.getLivingEntity().getLocation(), 32);
	}
	
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}