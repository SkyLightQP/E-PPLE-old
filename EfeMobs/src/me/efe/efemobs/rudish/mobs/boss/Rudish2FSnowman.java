package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.List;

import me.efe.efemobs.EfeCylinderEffect;
import me.efe.efemobs.Expedition;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class Rudish2FSnowman extends RudishBoss {

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.SNOW_BALL, plugin.util.rand(4, 13)));
		
		if (Math.random() <= 0.2)
			list.add(new ItemStack(Material.PUMPKIN));
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		
		return 20 * plugin.util.rand(15, 20);
	}
	
	@Override
	public void onSkill(final ActiveMob mob, final Expedition exp) {
		
		switch (plugin.util.random.nextInt(4)) {
		case 0:
			for (Entity near : mob.getEntity().getBukkitEntity().getNearbyEntities(5.0, 5.0, 5.0)) {
				if (near instanceof Player) {
					((Player) near).damage(5.0D, mob.getEntity().getBukkitEntity());
					
					near.getWorld().playEffect(near.getLocation(), Effect.STEP_SOUND, Material.ICE);
				}
			}
			
			EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
			
			cylinder.particle = ParticleEffect.SNOW_SHOVEL;
			cylinder.radius = 5.0F;
			cylinder.height = 0.3F;
			cylinder.iterations = 1;
			
			cylinder.setDynamicOrigin(new DynamicLocation((mob.getEntity().getBukkitEntity().getLocation().add(0, 1, 0))));
			cylinder.start();
			
			for (Player all : exp.getMembers())
				all.playSound(all.getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					for (Entity near : mob.getEntity().getBukkitEntity().getNearbyEntities(6.0, 6.0, 6.0)) {
						if (near instanceof Player) {
							((Player) near).damage(5.0D, mob.getEntity().getBukkitEntity());
							
							near.getWorld().playEffect(near.getLocation(), Effect.STEP_SOUND, Material.ICE);
						}
					}
					
					EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
					
					cylinder.particle = ParticleEffect.SNOW_SHOVEL;
					cylinder.radius = 6.0F;
					cylinder.height = 0.3F;
					cylinder.iterations = 1;
					
					cylinder.setDynamicOrigin(new DynamicLocation((mob.getEntity().getBukkitEntity().getLocation().add(0, 1, 0))));
					cylinder.start();
					
					for (Player all : exp.getMembers())
						all.playSound(all.getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.25F);
				}
			}, 20L);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					for (Entity near : mob.getEntity().getBukkitEntity().getNearbyEntities(7.0, 7.0, 7.0)) {
						if (near instanceof Player) {
							((Player) near).damage(5.0D, mob.getEntity().getBukkitEntity());
							
							near.getWorld().playEffect(near.getLocation(), Effect.STEP_SOUND, Material.ICE);
						}
					}
					
					EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
					
					cylinder.particle = ParticleEffect.SNOW_SHOVEL;
					cylinder.radius = 7.0F;
					cylinder.height = 0.3F;
					cylinder.iterations = 1;
					
					cylinder.setDynamicOrigin(new DynamicLocation((mob.getEntity().getBukkitEntity().getLocation().add(0, 1, 0))));
					cylinder.start();
					
					for (Player all : exp.getMembers())
						all.playSound(all.getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.5F);
				}
			}, 40L);
			
			break;
		case 1:
			final Location loc = mob.getEntity().getBukkitEntity().getLocation().add(0, 0.3, 0);
			
			for (int i = 0; i < 20; i++) {
				final int j = i;
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						double angle = 2 * Math.PI * j / 20;
						double x = Math.cos(angle) * 3;
						double z = Math.sin(angle) * 3;
						
						Snowball snowball = mob.getLivingEntity().launchProjectile(Snowball.class);
						
						snowball.teleport(loc.clone().add(x, 0, z));
						snowball.setVelocity(new Vector(x, 0, z));
						snowball.setMetadata("football", new FixedMetadataValue(plugin, ""));
						snowball.setShooter(mob.getLivingEntity());
						snowball.setFireTicks(60);
					}
				}, i * 2);
			}
			
			break;
		case 2:
			for (Player p : exp.getMembers()) {
				for (int x = -1; x <= 1; x ++) {
					for (int z = -1; z <= 1; z ++) {
						@SuppressWarnings("deprecation")
						FallingBlock falling = p.getWorld().spawnFallingBlock(p.getLocation().add(x, 20, z), Material.ICE, (byte) 0);
						
						falling.setDropItem(false);
						falling.setMetadata("icicle", new FixedMetadataValue(plugin, ""));
					}
				}
			}
			
			break;
		case 3:
			for (int i = 0; i < exp.getMembers().size() * 1.5; i ++) {
				MobSpawner.SpawnMythicMob("Minion_2F_Wolf", mob.getEntity().getBukkitEntity().getLocation());
			}
			
			break;
		}
	}
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e, Expedition exp) {
		
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim, Expedition exp) {
		if (e.getDamager().hasMetadata("football")) {
			e.setDamage(victim.getHealth() / 2);
			
			ParticleEffect.SNOW_SHOVEL.display(0.1F, 0.1F, 0.1F, 0.1F, 100, victim.getLocation().add(0, 1.5, 0), 32);
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		
	}
}