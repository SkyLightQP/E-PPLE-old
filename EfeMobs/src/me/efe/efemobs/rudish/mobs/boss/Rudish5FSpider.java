package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.efe.efemobs.Expedition;
import me.efe.efemobs.ScrollUtils;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class Rudish5FSpider extends RudishBoss {
	private int[][] xyz = new int[][]{{0, 0, 0}, {0, 0, -20}, {0, 0, -40}, {0, 0, -60}, {23, 1, -5}, {23, 1, -25}, {23, 1, -45}, {-23, 1, -5}, {-23, 1, -25}, {-23, 1, -45}};

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.STRING, plugin.util.rand(3, 7)));
		
		if (Math.random() <= 0.3)
			list.add(new ItemStack(Material.SPIDER_EYE, plugin.util.rand(1, 2)));
		if (Math.random() <= 0.03)
			list.add(plugin.util.createRawItem("§b방어의 낚싯대", new ItemStack(Material.FISHING_ROD), new String[]{"", "§9물고기 공격 100% 방어"}));
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		
		return 20 * plugin.util.rand(10, 15);
	}
	
	@Override
	public void onSkill(ActiveMob mob, Expedition exp) {
		switch (plugin.util.random.nextInt(4)) {
		case 0:
			mob.getLivingEntity().setMetadata("has_clone", new FixedMetadataValue(plugin, ""));
			
			for (int i = 0; i < 4; i ++) {
				MobSpawner.SpawnMythicMob("Minion_5F_Clone", mob.getEntity().getBukkitEntity().getLocation());
			}
			
			ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 30, mob.getEntity().getBukkitEntity().getLocation(), 32);
			break;
		case 1:
			mob.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*10, 0));
			
			ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 30, mob.getEntity().getBukkitEntity().getLocation(), 32);
			
			
			for (Entity near : mob.getEntity().getBukkitEntity().getNearbyEntities(7.0, 7.0, 7.0)) {
				if (near instanceof Player) continue;
				if (!(near instanceof LivingEntity)) continue;
				
				LivingEntity entity = (LivingEntity) near;
				
				entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*10, 0));
				
				ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 30, entity.getLocation(), 32);
			}
			
			
			for (final Player p : exp.getMembers()) {
				p.playSound(p.getLocation(), Sound.GHAST_SCREAM, 1.0F, 1.0F);
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						for (int i = 0; i < 4; i ++) {
							int x = plugin.util.rand(-2, 2);
							int z = plugin.util.rand(-2, 2);
							
							LivingEntity entity = (LivingEntity) MobSpawner.SpawnMythicMob("Minion_5F_Switcher", p.getLocation().add(x, 7, z));
							entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 10));
						}
					}
				}, 20L);
			}
			
			break;
		}
	}
	
	private void playWebEffect(final Set<Entity> entities, final Expedition exp, final ActiveMob boss) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				Iterator<Entity> it = entities.iterator();
				while (it.hasNext()) {
					Entity entity = it.next();
					
					if (entity == null || entity.isDead()) {
						it.remove();
					}
				}
				
				if (boss == null || boss.isDead() || entities.isEmpty()) {
					if (exp != null) {
						exp.setData(false);
					}
					
					return;
				}
				
				Set<Entity> set = new HashSet<Entity>();
				
				set.addAll(entities);
				set.addAll(exp.getMembers());
				set.add(boss.getLivingEntity());
				
				for (Entity from : set) {
					for (Entity to : set) {
						if (to.equals(from))
							continue;
						
						LineEffect effect = new LineEffect(plugin.em);
						
						effect.setDynamicOrigin(new DynamicLocation((from.getLocation().add(0, 0.3, 0))));
						effect.setDynamicTarget(new DynamicLocation((to.getLocation().add(0, 0.3, 0))));
						effect.particle = ParticleEffect.CRIT;
						effect.iterations = 1;
						
						effect.start();
					}
					
					if (from instanceof Player) {
						Player player = (Player) from;
						
						player.removePotionEffect(PotionEffectType.SLOW);
						player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 127));
						player.getWorld().playEffect(player.getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.WEB);
						
						ActionBarAPI.sendActionBar(player, "§c거미줄에 걸렸습니다!");
					}
				}
				
				playWebEffect(entities, exp, boss);
			}
		}, 10L);
	}
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e, Expedition exp) {
		e.getLivingEntity().setMetadata("phase", new FixedMetadataValue(plugin, 0));
		
		Entity entity = MobSpawner.SpawnMythicMob("Minion_5F_BabySpider", e.getEntity().getLocation());
		
		e.getEntity().setPassenger(entity);
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim, Expedition exp) {
		if (damager.getEntity().getBukkitEntity().hasMetadata("angry")) {
			victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 999999, 0));
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp) {
		if (e.getDamager() instanceof Projectile) {
			ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 10, e.getDamager().getLocation(), 32);
			e.getDamager().remove();
			
			e.setCancelled(true);
			return;
		}
		
		if (damager.hasPotionEffect(PotionEffectType.SLOW)) {
			e.setCancelled(true);
			
			damager.playSound(damager.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
			return;
		}
		
		if (victim.getLivingEntity().hasMetadata("has_clone")) {
			ProtectedRegion region = WGBukkit.getRegionManager(plugin.epple.world).getRegion("boss-room_"+exp.getChannel());
			
			for (Entity entity : plugin.epple.world.getEntities()) {
				final Location loc = entity.getLocation();
				
				if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
					if (entity.hasMetadata("clone")) {
						ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 30, entity.getLocation(), 32);
						
						entity.remove();
					}
				}
			}
		}
		
		int phase = victim.getLivingEntity().getMetadata("phase").get(0).asInt();
		double health = victim.getLivingEntity().getHealth();
		double maxHealth = victim.getType().getHealth();
		boolean newPhase = false;
		
		int i = 0;
		
		if (health <= maxHealth * 0.75 && phase < ++ i)
			newPhase = true;
		if (health <= maxHealth * 0.45 && phase < ++ i)
			newPhase = true;
		if (health <= maxHealth * 0.2 && phase < ++ i)
			newPhase = true;
		
		if (newPhase) {
			victim.getLivingEntity().setMetadata("phase", new FixedMetadataValue(plugin, phase + 1));
			
			if (exp.getData() != null && (boolean) exp.getData()) {
				return;
			}
			
			Set<Location> locations = new HashSet<Location>();
			Set<Integer> indexSet = new HashSet<Integer>();
			
			while (locations.size() < exp.getMembers().size() * 3 - exp.getMembers().size() / 2) {
				Location loc = ScrollUtils.getBossSpawn(exp.getChannel()).clone();
				int index = plugin.util.random.nextInt(xyz.length);
				
				if (indexSet.contains(index))
					continue;
				
				int[] array = xyz[index];
				loc.add(array[0], array[1], array[2]);
				
				locations.add(loc);
			}
			
			Set<Entity> entities = new HashSet<Entity>();
			
			for (Location loc : locations) {
				entities.add(MobSpawner.SpawnMythicMob("Minion_5F_Web", loc));
			}
			
			playWebEffect(entities, exp, victim);
			
			exp.setData(true);
		}
		
		if (!victim.getEntity().getBukkitEntity().hasMetadata("angry") && victim.getLivingEntity().getHealth() <= 166) {
			victim.getEntity().getBukkitEntity().setMetadata("angry", new FixedMetadataValue(plugin, ""));
			
			for (Player p : exp.getMembers()) {
				p.playSound(p.getLocation(), Sound.SPIDER_IDLE, 10.0F, 0.5F);
				ActionBarAPI.sendActionBar(p, "§c끝나지 않을 고통의 냄새가 느껴지기 시작했습니다.");
			}
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		
	}
}