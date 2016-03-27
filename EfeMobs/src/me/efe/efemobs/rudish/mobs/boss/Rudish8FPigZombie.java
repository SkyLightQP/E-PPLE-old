package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.efe.efemobs.Expedition;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class Rudish8FPigZombie extends RudishBoss {
	private Random random = new Random();
	private ItemStack goldSword;

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.GRILLED_PORK, plugin.util.rand(3, 5)));
		list.add(new ItemStack(Material.GHAST_TEAR, plugin.util.rand(3, 10)));
		
		if (Math.random() <= 0.3)
			list.add(new ItemStack(Material.GOLD_NUGGET, plugin.util.rand(3, 6)));
		if (Math.random() <= 0.1)
			list.add(goldSword.clone());
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		
		return 20 * plugin.util.rand(15, 20);
	}
	
	@Override
	public void onSkill(ActiveMob mob, Expedition exp) {
		switch (plugin.util.random.nextInt(3)) {
		case 0:
			for (int i = 0; i < exp.getMembers().size() * 3; i ++) {
				MobSpawner.SpawnMythicMob("Minion_8F_Rider", mob.getEntity().getBukkitEntity().getLocation());
			}
			
			break;
		case 1:
			for (int i = 0; i < exp.getMembers().size() * 3.5; i ++) {
				Entity entity = MobSpawner.SpawnMythicMob("Minion_8F_Smoker", mob.getEntity().getBukkitEntity().getLocation());
				
				entity.setVelocity(new Vector(Math.random() * 2 - 1, 0.3, Math.random() * 2 - 1).multiply(1.5D));
			}
			
			break;
		case 2:
			for (Player p : exp.getMembers()) {
				Vector from = p.getLocation().toVector();
				Vector to = mob.getEntity().getBukkitEntity().getLocation().toVector();
				
				p.setVelocity(to.subtract(from).multiply(0.5D));
				
				p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, 1.5F);
				ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.3F, 50, p.getLocation().add(0, 1.5, 0), 32);
			}
			
			break;
		}
	}
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e, Expedition exp) {
		PigZombie entity = (PigZombie) e.getEntity();
		
		entity.setAngry(true);
		
		this.goldSword = plugin.util.createDisplayItem("§b헬레이저의 검", new ItemStack(Material.GOLD_SWORD), new String[]{});
		this.goldSword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
		this.goldSword.addEnchantment(Enchantment.DURABILITY, 2);
		this.goldSword.addEnchantment(Enchantment.KNOCKBACK, 2);
		
		entity.getEquipment().setArmorContents(new ItemStack[]{null, null, null, null});
		entity.getEquipment().setItemInHand(goldSword.clone());
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, final Player victim, Expedition exp) {
		ItemStack hand = damager.getLivingEntity().getEquipment().getItemInHand();
		
		if (hand == null || hand.getType() != Material.GOLD_SWORD) return;
		
		victim.setHealth(1.0D);
		e.setDamage(0.0D);
		
		if (damager.getLivingEntity().getHealth() < damager.getType().getHealth() * 0.5) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					if (victim == null || victim.isDead() || victim.getHealth() <= 1) return;
					
					victim.setHealth(1.0D);
				}
			}, 1L);
			return;
		}
		
		if (victim.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
			victim.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			
			victim.playSound(victim.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.5F);
			ParticleEffect.SMOKE_NORMAL.display(0.0F, 0.0F, 0.0F, 0.2F, 200, victim.getLocation(), 32);
		}
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, final ActiveMob victim, Player damager, final Expedition exp) {
		if (Math.random() <= 0.2) {
			Monster monster = (Monster) victim.getLivingEntity();
			monster.setTarget(exp.getMembers().get(random.nextInt(exp.getMembers().size())));
		}
		
		if (Math.random() <= 0.2) {
			for (Player p : exp.getMembers()) {
				p.damage(5.0D);
				p.setFireTicks(100);
				
				p.playSound(p.getLocation(), Sound.GHAST_FIREBALL, 1.0F, 1.0F);
				
				LineEffect effect = new LineEffect(plugin.em);
				
				effect.setDynamicOrigin(new DynamicLocation((victim.getEntity().getBukkitEntity().getLocation().add(0, 1, 0))));
				effect.setDynamicTarget(new DynamicLocation((p.getLocation().add(0, 1, 0))));
				effect.particle = ParticleEffect.FLAME;
				effect.iterations = 1;
				
				effect.start();
			}
		}
		
		if (victim.getEntity().getBukkitEntity().hasMetadata("angry2") && damager.getFireTicks() < 60) {
			damager.setFireTicks(60);
		}
		
		if (!victim.getEntity().getBukkitEntity().hasMetadata("angry1") && !victim.getEntity().getBukkitEntity().hasMetadata("angry2") &&
				victim.getLivingEntity().getHealth() <= 300) {
			victim.getEntity().getBukkitEntity().setMetadata("angry1", new FixedMetadataValue(plugin, ""));
			
			for (Player p : exp.getMembers()) {
				p.setFoodLevel(0);
				
				p.playSound(p.getLocation(), Sound.WITHER_DEATH, 10.0F, 0.5F);
				ActionBarAPI.sendActionBar(p, "§c더 이상 그의 검은 갈라지지 않습니다.");
			}
		}
		
		if (!victim.getEntity().getBukkitEntity().hasMetadata("angry2") && victim.getLivingEntity().getHealth() <= 120) {
			victim.getEntity().getBukkitEntity().removeMetadata("angry1", plugin);
			victim.getEntity().getBukkitEntity().setMetadata("angry2", new FixedMetadataValue(plugin, ""));
			
			for (Player p : exp.getMembers()) {
				p.setFoodLevel(0);
				
				p.playSound(p.getLocation(), Sound.WITHER_DEATH, 10.0F, 0.5F);
				ActionBarAPI.sendActionBar(p, "§c그의 온몸에 뜨거운 열기가 솟아오르기 시작합니다.");
			}
		}
		
		ItemStack hand = victim.getLivingEntity().getEquipment().getItemInHand();
		
		if (hand == null || hand.getType() != Material.GOLD_SWORD || victim.getLivingEntity().getHealth() < victim.getType().getHealth() * 0.5) return;
		
		
		victim.getLivingEntity().getEquipment().setItemInHand(null);
		
		for (Player p : exp.getMembers()) {
			p.playSound(victim.getEntity().getBukkitEntity().getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
		}
		
		ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.GOLD_SWORD, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 50, victim.getEntity().getBukkitEntity().getLocation().add(0.0D, 1.5D, 0.0D), 32);
		
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if (victim == null || victim.getEntity().getBukkitEntity() == null || victim.getEntity().getBukkitEntity().isDead()) return;
				
				ItemStack hand = victim.getLivingEntity().getEquipment().getItemInHand();
				if (hand != null && hand.getType() == Material.GOLD_SWORD) return;
				
				
				victim.getLivingEntity().getEquipment().setItemInHand(goldSword.clone());
				
				for (Player p : exp.getMembers()) {
					p.playSound(victim.getEntity().getBukkitEntity().getLocation(), Sound.ANVIL_USE, 1.0F, 1.5F);
				}
				
				ParticleEffect.ENCHANTMENT_TABLE.display(1.0F, 1.0F, 1.0F, 0.5F, 100, victim.getEntity().getBukkitEntity().getLocation(), 32);
			}
		}, 20*5L);
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		
	}
}