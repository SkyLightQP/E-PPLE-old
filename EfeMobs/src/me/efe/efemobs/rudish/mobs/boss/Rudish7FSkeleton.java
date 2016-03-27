package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.List;

import me.efe.efemobs.Expedition;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.slikey.effectlib.util.ParticleEffect;

public class Rudish7FSkeleton extends RudishBoss {

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.COAL, plugin.util.rand(2, 6)));
		
		if (Math.random() <= 0.2)
			list.add(new ItemStack(Material.STONE_SWORD, 1));
		if (Math.random() <= 0.01)
			list.add(plugin.util.enchant(plugin.util.createRawItem("§d무한의 활", new ItemStack(Material.BOW), new String[]{}), Enchantment.ARROW_INFINITE, 1));
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		
		return 20 * plugin.util.rand(15, 20);
	}
	
	@Override
	public void onSkill(final ActiveMob mob, final Expedition exp) {
		switch (plugin.util.random.nextInt(3)) {
		case 0:
			for (int i = 0; i < exp.getMembers().size() * 1.5; i ++) {
				double x = plugin.util.rand(-2, 2) + Math.random();
				double z = plugin.util.rand(-2, 2) + Math.random();
				
				final Location loc = mob.getEntity().getBukkitEntity().getLocation().add(x, 0, z);
				
				ParticleEffect.SMOKE_LARGE.display(0.1F, 0.1F, 0.1F, 0.1F, 100, loc, 32);
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						MobSpawner.SpawnMythicMob("Minion_7F_Wraith", loc);
					}
				}, 10L);
			}
			
			break;
		case 1:
			for (int i = 0; i < exp.getMembers().size() * 3.5; i ++)
				MobSpawner.SpawnMythicMob("Minion_7F_Sword", mob.getEntity().getBukkitEntity().getLocation());
			for (int i = 0; i < exp.getMembers().size() * 3.5; i ++)
				MobSpawner.SpawnMythicMob("Minion_7F_Bow", mob.getEntity().getBukkitEntity().getLocation());
			
			break;
		case 2:
			for (final Player p : exp.getMembers()) {
				for (int i = 0; i < 14; i ++) {
					double x = plugin.util.rand(-3, 3) + Math.random();
					double z = plugin.util.rand(-3, 3) + Math.random();
					
					Arrow arrow = p.getWorld().spawn(p.getLocation().add(x, 7, z), Arrow.class);
					
					arrow.setShooter(mob.getLivingEntity());
					arrow.setMetadata("airstrike", new FixedMetadataValue(plugin, ""));
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
		}
	}
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e, Expedition exp) {
		e.getLivingEntity().getEquipment().setItemInHand(new ItemStack(Material.BOW));
		
		e.getEntity().setMetadata("boss_skeleton", new FixedMetadataValue(plugin, ""));
		
		Entity entity = MobSpawner.SpawnMythicMob("Minion_7F_Spider", e.getEntity().getLocation());
		
		entity.setPassenger(e.getEntity());
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, final Player victim, Expedition exp) {
		if (!e.getDamager().hasMetadata("airstrike") && e.getDamager() instanceof Arrow)
			victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20*5, 0));
		
		if (e.getDamager().hasMetadata("instant_hit")) {
			Arrow arrow = (Arrow) e.getDamager();
			
			if (arrow.getShooter().equals(e.getEntity())) {
				e.setCancelled(true);
				return;
			}
			
			final int tick = victim.getNoDamageTicks();
			victim.setNoDamageTicks(0);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					victim.setNoDamageTicks(tick);
				}
			}, 1L);
		} else if (e.getDamager().hasMetadata("airstrike")) {
			victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*15, 2));
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp) {
		if (e.getDamager() instanceof Projectile) {
			e.setCancelled(true);
		}
		
		if (!victim.getEntity().getBukkitEntity().hasMetadata("angry") && victim.getLivingEntity().getHealth() <= 300) {
			victim.getEntity().getBukkitEntity().setMetadata("angry", new FixedMetadataValue(plugin, ""));
			
			victim.getLivingEntity().getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));
			
			victim.getEntity().getBukkitEntity().getVehicle().remove();
			
			ParticleEffect.SMOKE_NORMAL.display(0.0F, 0.0F, 0.0F, 0.2F, 200, victim.getEntity().getBukkitEntity().getLocation(), 32);
			
			for (Player p : exp.getMembers()) {
				p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 10.0F, 1.0F);
				ActionBarAPI.sendActionBar(p, "§c'그'가, 끝내 검을 들기 시작합니다.");
			}
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		
	}
}