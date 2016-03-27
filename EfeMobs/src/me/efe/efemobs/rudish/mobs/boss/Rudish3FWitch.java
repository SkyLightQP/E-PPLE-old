package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.efe.efemobs.Expedition;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Rudish3FWitch extends RudishBoss {
	private HashMap<PotionEffectType, PotionEffectType> reversedEffects = new HashMap<PotionEffectType, PotionEffectType>();
	
	public Rudish3FWitch() {
		reversedEffects.put(PotionEffectType.SPEED, PotionEffectType.SLOW);
		reversedEffects.put(PotionEffectType.FAST_DIGGING, PotionEffectType.SLOW_DIGGING);
		reversedEffects.put(PotionEffectType.INCREASE_DAMAGE, PotionEffectType.WEAKNESS);
		reversedEffects.put(PotionEffectType.JUMP, PotionEffectType.SLOW);
		reversedEffects.put(PotionEffectType.REGENERATION, PotionEffectType.POISON);
		reversedEffects.put(PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.WEAKNESS);
		reversedEffects.put(PotionEffectType.FIRE_RESISTANCE, PotionEffectType.WEAKNESS);
		reversedEffects.put(PotionEffectType.WATER_BREATHING, PotionEffectType.CONFUSION);
		reversedEffects.put(PotionEffectType.NIGHT_VISION, PotionEffectType.BLINDNESS);
		reversedEffects.put(PotionEffectType.HEALTH_BOOST, PotionEffectType.POISON);
		reversedEffects.put(PotionEffectType.ABSORPTION, PotionEffectType.HUNGER);
	}

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.GLASS_BOTTLE, plugin.util.rand(1, 2)));
		
		if (Math.random() <= 0.2)
			list.add(new ItemStack(Material.POTION, 1, (short) 8261));
		if (Math.random() <= 0.2)
			list.add(new ItemStack(Material.POTION, 1, (short) 8225));
		if (Math.random() <= 0.2)
			list.add(new ItemStack(Material.POTION, 1, (short) 16393));
		if (Math.random() <= 0.1)
			list.add(new ItemStack(Material.POTION, 1, (short) 8227));
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		
		return 20 * plugin.util.rand(15, 20);
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
	public void onSkill(ActiveMob mob, Expedition exp) {
		switch (plugin.util.random.nextInt(3)) {
		case 0:
			for (final Player p : exp.getMembers()) {
				p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.25F);
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						short[] effects = {16424, 16426, 16460, 0, 16388};
						
						for (int i = 0; i < 5; i ++) {
							ItemStack item = potion(effects[i]);
							
							p.getWorld().spawn(p.getLocation().add(0, 18 + i * 0.5, 0), ThrownPotion.class).setItem(item);
						}
					}
				}, 20L);
			}
			
			break;
		case 1:
			for (int i = 0; i < 15; i ++) {
				final Bat bat = mob.getEntity().getBukkitEntity().getWorld().spawn(mob.getEntity().getBukkitEntity().getLocation(), Bat.class);
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						if (bat == null || bat.isDead()) return;
						
						bat.damage(99999);
					}
				}, 20*30L);
			}
			
			if (mob.getEntity().getBukkitEntity().getVehicle() == null) {
				final Bat bat = mob.getEntity().getBukkitEntity().getWorld().spawn(mob.getEntity().getBukkitEntity().getLocation(), Bat.class);
				
				bat.setPassenger(mob.getEntity().getBukkitEntity());
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						if (bat == null || bat.isDead()) return;
						
						bat.damage(99999);
					}
				}, 20*30L);
			}
			
			break;
//		case 2:
//			for (Entity near : mob.getEntity().getBukkitEntity().getNearbyEntities(7.0, 7.0, 7.0)) {
//				if (near instanceof Player) {
//					Player p = (Player) near;
//					
//					if (p.getActivePotionEffects().isEmpty()) continue;
//					
//					for (PotionEffect effect : p.getActivePotionEffects()) {
//						if (!reversedEffects.containsKey(effect.getType())) continue;
//						
//						PotionEffect newEffect = new PotionEffect(reversedEffects.get(effect.getType()), effect.getDuration(), effect.getAmplifier());
//						
//						p.addPotionEffect(newEffect);
//						p.removePotionEffect(effect.getType());
//					}
//					
//					p.playSound(p.getLocation(), Sound.GHAST_SCREAM, 1.0F, 1.0F);
//					
//					ActionBarAPI.sendActionBar(p, "§c버프 효과가 반전되었습니다!");
//					
//					ParticleEffect.ENCHANTMENT_TABLE.display(0.3F, 0.3F, 0.3F, 1.0F, 100, p.getLocation(), 32);
//					ParticleEffect.SMOKE_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 50, p.getLocation(), 32);
//				}
//			}
//			
//			break;
		case 2:
			for (int i = 0; i < exp.getMembers().size() * 1.5; i ++) {
				MobSpawner.SpawnMythicMob("Minion_3F_DoubleSlimes", mob.getEntity().getBukkitEntity().getLocation())
				.setPassenger(MobSpawner.SpawnMythicMob("Minion_3F_DoubleSlimes", mob.getEntity().getBukkitEntity().getLocation()));
			}
			
			break;
		}
	}
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e, Expedition exp) {
		e.getEntity().setMetadata("boss_witch", new FixedMetadataValue(plugin, ""));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim, Expedition exp) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp) {
		for (Player p : exp.getMembers())
			p.playSound(victim.getEntity().getBukkitEntity().getLocation(), Sound.VILLAGER_HIT, 1.0F, 1.25F);
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		
	}
}