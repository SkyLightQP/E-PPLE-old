package me.efe.efemobs.rudish.mobs.boss;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import me.efe.efemobs.Expedition;
import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

public class Rudish10FZombie implements RudishMob {
	private Random random = new Random();
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		ItemStack item = null;
		int i = random.nextInt(10);
		
		switch (i) {
		case 0:
			item = new ItemStack(Material.MONSTER_EGG, 1, (short) 54);
			break;
		case 1:
			item = new ItemStack(Material.MONSTER_EGG, 1, (short) 95);
			break;
		case 2:
			item = new ItemStack(Material.POTION, 1, (short) 16388);
			break;
		case 3:
			item = new ItemStack(Material.RED_ROSE);
			break;
		case 4:
			item = new ItemStack(Material.SPIDER_EYE);
			break;
		case 5:
			item = new ItemStack(Material.TNT);
			break;
		case 6:
			item = new ItemStack(Material.BOW);
			break;
		case 7:
			item = new ItemStack(Material.FLINT_AND_STEEL);
			break;
		case 8:
			item = new ItemStack(Material.EYE_OF_ENDER);
			break;
		}
		
		e.getLivingEntity().getEquipment().setItemInHand(item);
		e.getLivingEntity().setMetadata("10f", new FixedMetadataValue(plugin, i));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		int channel = plugin.bossListener.getChannel(e.getEntity().getLocation());
		final Expedition exp = plugin.bossListener.expMap.get(channel);
		
		int i = damager.getLivingEntity().getMetadata("10f").get(0).asInt();
		
		switch (i) {
		case 0:
			for (Player p : exp.getMembers()) {
				for (int j = 0; j < 5; j ++) {
					Location loc = p.getLocation().add(0, 7, 0);
					
					Entity entity1 = MobSpawner.SpawnMythicMob("Minion_1F_BabyZombie", loc);
					Entity entity2 = MobSpawner.SpawnMythicMob("Minion_1F_BabyZombie", loc);
					Entity entity3 = MobSpawner.SpawnMythicMob("Minion_1F_BabyZombie", loc);
					
					entity1.setPassenger(entity2);
					entity2.setPassenger(entity3);
					
					((LivingEntity) entity1).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 2));
				}
			}
			
			break;
		case 1:
			for (Player p : exp.getMembers()) {
				for (int j = 0; j < exp.getMembers().size() * 1.5; j ++) {
					LivingEntity entity = (LivingEntity) MobSpawner.SpawnMythicMob("Minion_2F_Wolf", p.getLocation().add(0, 7, 0));
					entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 2));
				}
			}
			
			break;
		case 2:
			for (Player p : exp.getMembers()) {
				short[] effects = {16424, 16426, 16460, 0, 16388};
				
				for (int j = 0; j < 5; j ++) {
					ItemStack item = potion(effects[j]);
					
					p.getWorld().spawn(p.getLocation().add(0, 18 + j * 0.5, 0), ThrownPotion.class).setItem(item);
				}
			}
			
			break;
		case 3:
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					for (Player p : exp.getMembers()) {
						p.setHealth(1.0D);
						p.damage(0.0D);
						
						ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.RED_ROSE, (byte) 0), 0.0F, 0.0F, 0.0F, 0.1F, 50, p.getLocation().add(0, 1.5, 0), 32);
					}
				}
			}, 5L);
			
			break;
		case 4:
			for (Player p : exp.getMembers()) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 999999, 2));
			}
			
			break;
		case 5:
			for (Player p : exp.getMembers()) {
				TNTPrimed tnt = damager.getLivingEntity().getWorld().spawn(p.getLocation().add(0, 1, 0), TNTPrimed.class);
				
				tnt.setFuseTicks(30);
				tnt.setPassenger(p);
			}
			
			break;
		case 6:
			for (final Player p : exp.getMembers()) {
				for (int j = 0; j < 14; j ++) {
					double x = plugin.util.rand(-3, 3) + Math.random();
					double z = plugin.util.rand(-3, 3) + Math.random();
					
					p.getWorld().spawn(p.getLocation().add(x, 7, z), Arrow.class);
				}
				
				p.playSound(p.getLocation(), Sound.SHOOT_ARROW, 1.0F, 1.0F);
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						p.playSound(p.getLocation(), Sound.SHOOT_ARROW, 1.0F, 1.0F);
						
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							
							@Override
							public void run() {
								p.playSound(p.getLocation(), Sound.SHOOT_ARROW, 1.0F, 1.0F);
							}
						}, 1L);
					}
				}, 1L);
			}
			
			break;
		case 7:
			for (Player p : exp.getMembers()) {
				p.damage(5.0D);
				p.setFireTicks(100);
				
				p.playSound(p.getLocation(), Sound.GHAST_FIREBALL, 1.0F, 1.0F);
				
				if (p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
					p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
					
					p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.5F);
					ParticleEffect.SMOKE_NORMAL.display(0.0F, 0.0F, 0.0F, 0.2F, 200, p.getLocation(), 32);
				}
				
				LineEffect effect = new LineEffect(plugin.em);
				
				effect.setDynamicOrigin(new DynamicLocation((damager.getLivingEntity().getLocation().add(0, 1, 0))));
				effect.setDynamicTarget(new DynamicLocation((p.getLocation().add(0, 1, 0))));
				effect.particle = ParticleEffect.FLAME;
				effect.iterations = 1;
				
				effect.start();
			}
			
			break;
		case 8:
			for (Player p : exp.getMembers()) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 0));
			}
			
			break;
		}
	}
	
	private ItemStack potion(short data) {
		ItemStack item = new ItemStack(Material.POTION);
		
		if (data != 0) {
			item.setDurability(data);
			
			return item;
		}
		
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*15, 0), false);
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		if (e.getMobType().getInternalName().contains("Quiz")) {
			if (e.getLivingEntity().hasMetadata("deathnity"))
				return;
			
			int channel = plugin.bossListener.getChannel(e.getEntity().getLocation());
			Expedition exp = plugin.bossListener.expMap.get(channel);
			Entity boss = plugin.bossListener.getBoss(channel).getEntity().getBukkitEntity();
			
			if (boss.hasMetadata("angry1")) {
				int[] data = (int[]) exp.getData();
				data[0] ++;
				
				exp.setData(data);
				
				for (Player p : exp.getMembers()) {
					ActionBarAPI.sendActionBar(p, "§e현재 포인트: "+data[0]);
				}
			} else if (boss.hasMetadata("angry2")) {
				String[] data = (String[]) exp.getData();
				data[0] += ((Zombie) e.getLivingEntity()).isBaby() ? "z" : "Z";
				
				exp.setData(data);
				
				for (Player p : exp.getMembers()) {
					ActionBarAPI.sendActionBar(p, "§e현재 값: "+data[0]);
				}
			}
		}
	}
}