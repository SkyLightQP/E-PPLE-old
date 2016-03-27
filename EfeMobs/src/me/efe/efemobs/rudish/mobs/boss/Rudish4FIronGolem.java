package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeitems.ItemStorage;
import me.efe.efemobs.Expedition;
import me.efe.efemobs.ScrollUtils;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.util.ParticleEffect;

public class Rudish4FIronGolem extends RudishBoss {

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.IRON_INGOT, plugin.util.rand(2, 6)));
		
		if (Math.random() <= 0.3)
			list.add(new ItemStack(Material.RED_ROSE, plugin.util.rand(1, 2)));
		if (Math.random() <= 0.3)
			list.add(new ItemStack(Material.YELLOW_FLOWER, plugin.util.rand(1, 2)));
		if (Math.random() <= 0.01)
			list.add(ItemStorage.createSkinBook("rose"));
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		
		return 20 * (mob.getEntity().getBukkitEntity().hasMetadata("angry") ? plugin.util.rand(5, 10) : plugin.util.rand(15, 20));
	}
	
	private void killSilverfish(int channel, boolean isAngry) {
		ProtectedRegion region = WGBukkit.getRegionManager(plugin.epple.world).getRegion("boss-room_"+channel);
		
		for (Entity entity : plugin.epple.world.getEntities()) {
			final Location loc = entity.getLocation();
			
			if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
				if (entity instanceof Silverfish) {
					if (entity.getPassenger() != null) {
						final Entity passenger = entity.getPassenger();
						passenger.eject();
						
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							
							@Override
							public void run() {
								passenger.teleport(loc.add(0, 1, 0));
							}
						}, 20L);
					}
					
					((Silverfish) entity).damage(99999);
					
					ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.RED_ROSE, (byte) 0), 0.0F, 0.0F, 0.0F, 0.1F, 50, entity.getLocation(), 32);
					
				} else if (entity instanceof Player && isAngry) {
					Player p = (Player) entity;
					
					p.setHealth(1.0D);
					p.damage(0.0D);
					
					ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.RED_ROSE, (byte) 0), 0.0F, 0.0F, 0.0F, 0.1F, 50, p.getLocation().add(0, 1.5, 0), 32);
					
					ActionBarAPI.sendActionBar(p, "§e아얏! 장미 가시에 찔렸습니다.");
				}
			}
		}
	}
	
	@Override
	public void onSkill(final ActiveMob mob, final Expedition exp) {
		boolean isAngry = mob.getEntity().getBukkitEntity().hasMetadata("angry");
		
		killSilverfish(exp.getChannel(), isAngry);
		
		for (int i = 0; i < exp.getMembers().size() * 5.5; i ++) {
			double x = plugin.util.rand(-2, 2) + Math.random();
			double z = plugin.util.rand(-2, 2) + Math.random();
			
			MobSpawner.SpawnMythicMob("Minion_4F_Silverfish", mob.getEntity().getBukkitEntity().getLocation().add(x, 0, z));
		}
		
		if (!isAngry) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					mob.getEntity().getBukkitEntity().teleport(ScrollUtils.getBossSpawn(exp.getChannel()));
				}
			}, 20L);
		}
	}
	
	@Override
	public void onSpawn(final MythicMobSpawnEvent e, final Expedition exp) {
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 127));
		
		for (int i = 0; i < exp.getMembers().size() * 5.5; i ++) {
			double x = plugin.util.rand(-2, 2) + Math.random();
			double z = plugin.util.rand(-2, 2) + Math.random();
			
			MobSpawner.SpawnMythicMob("Minion_4F_Silverfish", e.getEntity().getLocation().add(x, 0, z));
		}
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				e.getEntity().teleport(ScrollUtils.getBossSpawn(exp.getChannel()));
			}
		}, 20L);
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, final ActiveMob damager, final Player victim, Expedition exp) {
		if (victim.getVehicle() != null) {
			victim.eject();
		}
		
		e.setDamage(victim.getHealth() / 2);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if (victim == null || victim.isDead()) return;
				
				victim.damage(99999.0D);
			}
		}, 20L);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp) {
		if (exp.getData() == null || (int) exp.getData() == 0) {
			e.setCancelled(true);
			
			damager.playSound(damager.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.5F);
			ActionBarAPI.sendActionBar(damager, "§c마치 마법이 깃든 금속처럼 단단합니다.");
		} else {
			exp.setData((int) exp.getData() - 1);
			
			for (Player p : exp.getMembers()) {
				p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.5F);
				ActionBarAPI.sendActionBar(p, "§e남은 공격 찬스: §l"+exp.getData());
			}
		}
		
		if (!victim.getEntity().getBukkitEntity().hasMetadata("angry") && victim.getLivingEntity().getHealth() <= 100) {
			victim.getEntity().getBukkitEntity().setMetadata("angry", new FixedMetadataValue(plugin, ""));
			
			victim.getLivingEntity().removePotionEffect(PotionEffectType.SLOW);
			
			killSilverfish(exp.getChannel(), false);
			
			ParticleEffect.EXPLOSION_HUGE.display(0.0F, 0.0F, 0.0F, 0.0F, 1, victim.getEntity().getBukkitEntity().getLocation(), 32);
			
			for (Player p : exp.getMembers()) {
				p.damage(7.0D);
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 127));
				
				ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.RED_ROSE, (byte) 0), 0.0F, 0.0F, 0.0F, 0.1F, 50, p.getLocation().add(0, 1.5, 0), 32);
				
				p.playSound(p.getLocation(), Sound.HORSE_ZOMBIE_DEATH, 10.0F, 0.5F);
				ActionBarAPI.sendActionBar(p, "§c거대한 철 덩어리가 움직이기 시작합니다!");
			}
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		
	}
}