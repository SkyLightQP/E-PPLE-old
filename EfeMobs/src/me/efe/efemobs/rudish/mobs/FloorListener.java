package me.efe.efemobs.rudish.mobs;

import java.util.HashMap;

import me.efe.efemobs.EfeMobs;
import me.efe.efemobs.ScrollUtils;
import me.efe.efemobs.rudish.mobs.boss.Rudish10FClone;
import me.efe.efemobs.rudish.mobs.boss.Rudish10FGiant;
import me.efe.efemobs.rudish.mobs.boss.Rudish10FZombie;
import me.efe.efemobs.rudish.mobs.boss.Rudish1FBabyZombie;
import me.efe.efemobs.rudish.mobs.boss.Rudish1FZombie;
import me.efe.efemobs.rudish.mobs.boss.Rudish2FSnowman;
import me.efe.efemobs.rudish.mobs.boss.Rudish2FWolf;
import me.efe.efemobs.rudish.mobs.boss.Rudish3FDoubleSlimes;
import me.efe.efemobs.rudish.mobs.boss.Rudish3FWitch;
import me.efe.efemobs.rudish.mobs.boss.Rudish4FIronGolem;
import me.efe.efemobs.rudish.mobs.boss.Rudish4FSilverfish;
import me.efe.efemobs.rudish.mobs.boss.Rudish5FBabySpider;
import me.efe.efemobs.rudish.mobs.boss.Rudish5FClone;
import me.efe.efemobs.rudish.mobs.boss.Rudish5FSpider;
import me.efe.efemobs.rudish.mobs.boss.Rudish5FSwitcher;
import me.efe.efemobs.rudish.mobs.boss.Rudish6FCreeper;
import me.efe.efemobs.rudish.mobs.boss.Rudish7FBow;
import me.efe.efemobs.rudish.mobs.boss.Rudish7FSkeleton;
import me.efe.efemobs.rudish.mobs.boss.Rudish7FSpider;
import me.efe.efemobs.rudish.mobs.boss.Rudish7FSword;
import me.efe.efemobs.rudish.mobs.boss.Rudish7FWraith;
import me.efe.efemobs.rudish.mobs.boss.Rudish8FRider;
import me.efe.efemobs.rudish.mobs.boss.Rudish8FPigZombie;
import me.efe.efemobs.rudish.mobs.boss.Rudish8FROCube;
import me.efe.efemobs.rudish.mobs.boss.Rudish8FSmoker;
import me.efe.efemobs.rudish.mobs.boss.Rudish9FEnderman;
import me.efe.efemobs.rudish.mobs.boss.Rudish9FTeleporter;
import me.efe.efemobs.rudish.mobs.boss.RudishBoss;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMobHandler;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FloorListener implements Listener {
	public EfeMobs plugin;
	public HashMap<String, RudishMob> mobs = new HashMap<String, RudishMob>();
	
	public FloorListener(EfeMobs plugin) {
		this.plugin = plugin;
		
		mobs.put("1F_Zombie", new RudishZombie());
		mobs.put("2F_Wolf", new RudishWolf());
		mobs.put("2F_Snowman", new RudishSnowman());
		mobs.put("3F_Slime", new RudishSlime());
		mobs.put("3F_Witch", new RudishWitch());
		mobs.put("4F_IronGolem", new RudishIronGolem());
		mobs.put("4F_Silverfish", new RudishSilverfish());
		mobs.put("5F_Spider", new RudishSpider());
		mobs.put("5F_CaveSpider", new RudishCaveSpider());
		mobs.put("6F_ExplodeCreeper", new RudishCreeper());
		mobs.put("6F_TNTCreeper", new RudishTNTCreeper());
		mobs.put("7F_Skeleton", new RudishSkeleton());
		mobs.put("7F_WitherSkeleton", new RudishWitherSkeleton());
		mobs.put("8F_MagmaCube", new RudishMagmaCube());
		mobs.put("8F_MagmaCube2", new RudishMagmaCube());
		mobs.put("8F_Blaze", new RudishBlaze());
		mobs.put("9F_Enderman", new RudishEnderman());
		mobs.put("10F_SwordZombie", new RudishSwordZombie());
		mobs.put("10F_MarioZombie", new RudishMarioZombie());
		mobs.put("10F_MirrorZombie", new RudishMirrorZombie());
		mobs.put("10F_SeparateZombie", new RudishSeparateZombie());
		mobs.put("10F_DrainZombie", new RudishDrainZombie());
		
		mobs.put("Boss_1F", new Rudish1FZombie());
		mobs.put("Minion_1F_BabyZombie", new Rudish1FBabyZombie());
		mobs.put("Boss_2F", new Rudish2FSnowman());
		mobs.put("Minion_2F_Wolf", new Rudish2FWolf());
		mobs.put("Boss_3F", new Rudish3FWitch());
		mobs.put("Minion_3F_DoubleSlimes", new Rudish3FDoubleSlimes());
		mobs.put("Boss_4F", new Rudish4FIronGolem());
		mobs.put("Minion_4F_Silverfish", new Rudish4FSilverfish());
		mobs.put("Boss_5F", new Rudish5FSpider());
		mobs.put("Minion_5F_BabySpider", new Rudish5FBabySpider());
		mobs.put("Minion_5F_Clone", new Rudish5FClone());
		mobs.put("Minion_5F_Switcher", new Rudish5FSwitcher());
		mobs.put("Boss_6F", new Rudish6FCreeper());
		mobs.put("Boss_7F", new Rudish7FSkeleton());
		mobs.put("Minion_7F_Spider", new Rudish7FSpider());
		mobs.put("Minion_7F_Wraith", new Rudish7FWraith());
		mobs.put("Minion_7F_Sword", new Rudish7FSword());
		mobs.put("Minion_7F_Bow", new Rudish7FBow());
		mobs.put("Boss_8F", new Rudish8FPigZombie());
		mobs.put("Minion_8F_Rider", new Rudish8FRider());
		mobs.put("Minion_8F_ROCube", new Rudish8FROCube());
		mobs.put("Minion_8F_Smoker", new Rudish8FSmoker());
		mobs.put("Boss_9F", new Rudish9FEnderman());
		mobs.put("Minion_9F_Enderman", new Rudish9FTeleporter());
		mobs.put("Boss_10F", new Rudish10FGiant());
		mobs.put("Minion_10F_Clone", new Rudish10FClone());
		mobs.put("Minion_10F_Zombie", new Rudish10FZombie());
		mobs.put("Minion_10F_QuizZombie", new Rudish10FZombie());
		mobs.put("Minion_10F_QuizBabyZombie", new Rudish10FZombie());
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void spawn(MythicMobSpawnEvent e) {
		if (!mobs.containsKey(e.getMobType().getInternalName())) return;
		
		mobs.get(e.getMobType().getInternalName()).onSpawn(e);
	}
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		LivingEntity damager = plugin.util.getDamager(e);
		
		if (damager == null || !(e.getEntity() instanceof LivingEntity)) return;
		
		if (plugin.mythicMobs.getAPI().getMobAPI().isMythicMob(damager) && e.getEntity() instanceof Player) {
			ActiveMob mob = ActiveMobHandler.getMythicMobInstance(damager);
			Player player = (Player) e.getEntity();
			
			if (!mobs.containsKey(mob.getType().getInternalName())) return;
			
			mobs.get(mob.getType().getInternalName()).onAttack(e, mob, player);
		}
		
		if (plugin.mythicMobs.getAPI().getMobAPI().isMythicMob((LivingEntity) e.getEntity()) && damager instanceof Player) {
			ActiveMob mob = ActiveMobHandler.getMythicMobInstance(e.getEntity());
			Player player = (Player) damager;
			
			if (!mobs.containsKey(mob.getType().getInternalName())) return;
			
			mobs.get(mob.getType().getInternalName()).onDamaged(e, mob, player);
		}
	}
	
	@EventHandler
	public void death(MythicMobDeathEvent e) {
		if (!mobs.containsKey(e.getMobType().getInternalName())) return;
		
		mobs.get(e.getMobType().getInternalName()).onDeath(e);
	}
	
	@EventHandler
	public void regainHealth(EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof LivingEntity && plugin.mythicMobs.getAPI().getMobAPI().isMythicMob((LivingEntity) e.getEntity())) {
			ActiveMob mob = ActiveMobHandler.getMythicMobInstance(e.getEntity());
			
			if (!mobs.containsKey(mob.getType().getInternalName())) return;
			
			RudishMob rudishMob = mobs.get(mob.getType().getInternalName());
			
			if (rudishMob instanceof RudishBoss) {
				((RudishBoss) rudishMob).onRegainHealth(mob);
			}
		}
	}
	
	@EventHandler
	public void trail(EntityBlockFormEvent e) {
		if (e.getEntity() instanceof Snowman && e.getNewState().getType() == Material.SNOW) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void explode(EntityExplodeEvent e) {
		if (e.getEntity() instanceof LivingEntity && plugin.mythicMobs.getAPI().getMobAPI().isMythicMob((LivingEntity) e.getEntity())) {
			ActiveMob mob = ActiveMobHandler.getMythicMobInstance(e.getEntity());
			
			if (!mobs.containsKey(mob.getType().getInternalName())) return;
			
			if (mobs.get(mob.getType().getInternalName()) instanceof RudishCreeper) {
				MobSpawner.SpawnMythicMob("6F_TNTCreeper", e.getLocation());
				
				e.getEntity().remove();
				
			}
		}
	}
	
	@EventHandler
	public void land(EntityChangeBlockEvent e) {
		if (e.getEntity().hasMetadata("icicle")) {
			e.setCancelled(true);
			
			for (Entity entity : e.getEntity().getNearbyEntities(0.2D, 0.2D, 0.2D)) {
				if (!(entity instanceof Player)) return;
				
				Player p = (Player) entity;
				
				p.damage(10.0D, e.getEntity());
				
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*5, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*10, 1));
			}
			
			e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.STEP_SOUND, Material.ICE);
			e.getEntity().remove();
		}
	}
	
	@EventHandler
	public void damageSelf(final EntityDamageEvent e) {
		if (e instanceof EntityDamageByEntityEvent) return;
		
		if (e.getCause() == DamageCause.LAVA && plugin.bossListener.isBossRoom(e.getEntity().getLocation())) {
			e.setCancelled(true);
			
			int channel = plugin.bossListener.getChannel(e.getEntity().getLocation());
			
			if (e.getEntity() instanceof Player)
				e.getEntity().teleport(ScrollUtils.getUserSpawn(channel));
			else
				e.getEntity().teleport(ScrollUtils.getBossSpawn(channel));
		}
		
		if (e.getEntity().hasMetadata("boss_witch") && e.getEntity().getVehicle() != null && e.getCause() != DamageCause.POISON) {
			e.setCancelled(true);
			
			e.getEntity().eject();
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (e.getEntity() == null || e.getEntity().isDead())
						return;
					
					int channel = plugin.bossListener.getChannel(e.getEntity().getLocation());
					e.getEntity().teleport(ScrollUtils.getBossSpawn(channel));
				}
			}, 3L);
		}
		
		if (e.getEntity().hasMetadata("boss_babyspider")) {
			e.setCancelled(true);
		}
		
		if (e.getEntity().hasMetadata("wraith") && e.getCause() == DamageCause.FALL) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void potion(PotionSplashEvent e) {
		if (e.getEntity().getShooter() instanceof LivingEntity && ((LivingEntity) e.getEntity().getShooter()).hasMetadata("boss_witch")) {
			if (e.getPotion().getItem().getDurability() != 32660) return;
			
			for (LivingEntity entity : e.getAffectedEntities()) {
				entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*5, 0));
			}
			
			e.getPotion().setItem(new ItemStack(Material.POTION));
		}
	}
	
	@EventHandler
	public void launch(ProjectileLaunchEvent e) {
		if (e.getEntity().getShooter() == null) return;
		
		final Entity shooter = (Entity) e.getEntity().getShooter();
		
		if (shooter.hasMetadata("boss_witch") && e.getEntity() instanceof ThrownPotion) {
			ThrownPotion potion = (ThrownPotion) e.getEntity();
			
			if (potion.getItem().getDurability() == 32660) {
				ItemStack item = potion.getItem().clone();
				item.setDurability((short) 16460);
				
				potion.setItem(item);
			}
			
		} else if (shooter.hasMetadata("boss_skeleton")) {
			if (shooter.hasMetadata("long_time_shoot")) {
				e.setCancelled(true);
				
				shooter.removeMetadata("long_time_shoot", plugin);
				return;
			}
			
			shooter.setMetadata("long_time_shoot", new FixedMetadataValue(plugin, ""));
			
			e.getEntity().setFireTicks(0);
			
			final Creature creature = (Creature) shooter;
			final Vector direction = e.getEntity().getVelocity();
			
			for (int i = 0; i < 4; i ++) {
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						Arrow arrow = shooter.getWorld().spawnArrow(creature.getEyeLocation(), direction, (float) direction.length(), 8.0F);
						arrow.setShooter(creature);
						arrow.setCritical(true);
						arrow.setMetadata("instant_hit", new FixedMetadataValue(plugin, ""));
					}
				}, i + 1);
			}
		}
	}
	
	@EventHandler
	public void target(EntityTargetLivingEntityEvent e) {
		if (e.getEntityType() == EntityType.IRON_GOLEM && e.getTarget() != null && e.getTarget().getType() != EntityType.PLAYER) {
			e.setCancelled(true);
		} else if (e.getEntity() instanceof LivingEntity && plugin.mythicMobs.getAPI().getMobAPI().isMythicMob((LivingEntity) e.getEntity())) {
			ActiveMob mob = ActiveMobHandler.getMythicMobInstance(e.getEntity());
			
			if (mob.getType().getInternalName().equals("Minion_4F_Silverfish") && e.getEntity().getPassenger() != null) {
				int channel = plugin.bossListener.getChannel(mob.getEntity().getBukkitEntity().getLocation());
				
				e.setTarget(plugin.bossListener.getBoss(channel).getLivingEntity());
			}
		}
	}
	
//	@EventHandler
//	public void exit(final EntityDismountEvent e) {
//		if (e.getDismounted().hasMetadata("boss_silverfish")) {
//			
//			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//				public void run() {
//					e.getDismounted().setPassenger(e.getEntity());
//				}
//			}, 1L);
//		}
//	}
	
	@EventHandler
	public void damageByMob(EntityDamageByEntityEvent e) {
		LivingEntity damager = plugin.util.getDamager(e);
		
		if (damager == null) return;
		
		if (e.getEntity().hasMetadata("boss_babyspider") && !(damager instanceof Player)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void pvp(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && plugin.util.getDamager(e) instanceof Player) {
			Player damager = (Player) plugin.util.getDamager(e);

			if (plugin.bossListener.isBossRoom(damager.getLocation())) return;
			if (!plugin.bossListener.playerMap.containsKey(damager)) return;
			if (plugin.bossListener.getExpedition(damager).getFloor() != 10) return;
			
			damager.damage(999999);
		}
	}
	
	@EventHandler
	public void arrow(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player && e.getProjectile() instanceof Arrow && e.getForce() == 1.0F) {
			Player p = (Player) e.getEntity();
			
			if (!plugin.bossListener.playerMap.containsKey(p)) return;

			Arrow arrow = (Arrow) e.getProjectile();
			double damage = 9.0D;
			
			if (arrow.isCritical())
				damage = 10.0D;
			
			if (e.getBow().containsEnchantment(Enchantment.ARROW_DAMAGE)) {
				int level = e.getBow().getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
				damage *= 1 + 0.4 * (level + 1);
			}
			
			int channel = plugin.bossListener.getChannel(e.getEntity().getLocation());
			
			new ArrowTask(p, arrow, plugin.bossListener.getBoss(channel), damage).runTaskTimer(plugin, 1, 1);
		}
	}
	
	private class ArrowTask extends BukkitRunnable {
		private final Player p;
		private final Arrow arrow;
		private final ActiveMob mob;
		private final double damage;
		private long tick;
		
		public ArrowTask(Player p, Arrow arrow, ActiveMob mob, double damage) {
			this.p = p;
			this.arrow = arrow;
			this.mob = mob;
			this.damage = damage;
			this.tick = 0;
		}
		
		@Override
		public void run() {
			if (mob == null || mob.getLivingEntity() == null || mob.isDead() || tick >= 60) {
				this.cancel();
				return;
			}
			
			Location locArrow = arrow.getLocation().clone();
			Location locMob = mob.getLivingEntity().getLocation().clone();
			
			double distance = Math.abs(locArrow.getY() - locMob.getY());
			if (9 < distance && distance < 12) {
				locArrow.setY(0.0D);
				locMob.setY(0.0D);
				
				if (locArrow.distance(locMob) <= 3.0D) {
					p.setMetadata("arrow", new FixedMetadataValue(plugin, ""));
					
					mob.getLivingEntity().damage(damage, p);
					arrow.remove();
					this.cancel();
					return;
				}
			}
			
			tick ++;
		}
	}
}