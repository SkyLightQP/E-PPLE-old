package me.efe.skilltree.listeners;

import me.efe.efeisland.EfeFlag;
import me.efe.skilltree.SkillTree;
import me.efe.skilltree.SkillUtils;
import me.efe.skilltree.UserData;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.mewin.util.Util;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import de.slikey.effectlib.util.ParticleEffect;

public class BowListener implements Listener {
	public SkillTree plugin;
	
	public BowListener(SkillTree plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void slowdown(EntityDamageByEntityEvent e) {
		if (Util.getFlagValue(plugin.wgp, e.getEntity().getLocation(), EfeFlag.SKILL) == State.DENY) return;
		
		if (e.getDamager() instanceof Arrow && e.getEntity() instanceof LivingEntity) {
			final LivingEntity entity = (LivingEntity) e.getEntity();
			Arrow arrow = (Arrow) e.getDamager();
			
			if (!(arrow.getShooter() instanceof Player)) return;
			
			Player p = (Player) arrow.getShooter();
			UserData data = new UserData(p);
			
			if (arrow.hasMetadata("instant_hit")) {
				if (arrow.getShooter().equals(e.getEntity())) {
					e.setCancelled(true);
					return;
				}
				
				final int tick = entity.getNoDamageTicks();
				entity.setNoDamageTicks(0);
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						entity.setNoDamageTicks(tick);
					}
				}, 1L);
			}
			
			if (entity instanceof Player && Util.getFlagValue(plugin.wgp, p.getLocation(), DefaultFlag.PVP) != State.ALLOW) return;
			
			int level = data.getLevel(SkillUtils.getSkill("hunt.slowdown"));
			if (level == 0) return;
			
			switch (level) {
			case 1:
				entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 0));
				break;
			case 2:
				entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
				break;
			case 3:
				entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
				break;
			case 4:
				entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 1));
				break;
			case 5:
				entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 2));
				break;
			case 6:
				entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2));
				break;
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void multipleShot(EntityShootBowEvent e) {
		if (Util.getFlagValue(plugin.wgp, e.getEntity().getLocation(), EfeFlag.SKILL) == State.DENY) return;
		
		if (e.getEntity() instanceof Player && e.getForce() == 1.0F) {
			final Player p = (Player) e.getEntity();
			UserData data = new UserData(p);
			
			if (!p.isSneaking()) return;
			
			int level = data.getLevel(SkillUtils.getSkill("hunt.multiple-shot"));
			if (level == 0) return;
			
			final Vector direction = e.getProjectile().getVelocity();
			
			for (int i = 0; i < level; i ++) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						if (!p.getInventory().contains(Material.ARROW)) return;
						
						final Arrow arrow = p.getWorld().spawnArrow(p.getEyeLocation(), direction, (float) direction.length(), 8.0F);
						arrow.setShooter(p);
						arrow.setCritical(true);
						arrow.setMetadata("instant_hit", new FixedMetadataValue(plugin, ""));
						
						plugin.util.playSoundAll(p, Sound.SHOOT_ARROW, 1.0F);
						ParticleEffect.CRIT.display(0.0F, 0.0F, 0.0F, 0.5F, 5, p.getEyeLocation(), 32);
						
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							
							@Override
							public void run() {
								arrow.remove();
							}
						}, 140L);
					}
				}, i + 1);
			}
		}
	}
}