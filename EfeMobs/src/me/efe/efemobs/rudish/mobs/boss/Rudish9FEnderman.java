package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.Collections;
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
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class Rudish9FEnderman extends RudishBoss {
	private List<MaterialData> blocks = new ArrayList<MaterialData>();
	private int[][] xyz = new int[][]{{0, 0, 0}, {0, 0, -20}, {0, 0, -40}, {0, 0, -60}, {23, 1, -5}, {23, 1, -25}, {23, 1, -45}, {-23, 1, -5}, {-23, 1, -25}, {-23, 1, -45}};

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.ENDER_PEARL, plugin.util.rand(3, 5)));
		
		if (Math.random() <= 0.3)
			list.add(new ItemStack(Material.ENDER_STONE, plugin.util.rand(5, 13)));
		
		list.add(ItemStorage.EYE_OF_TELEPORTATION.clone());
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		
		return 20 * (mob.getEntity().getBukkitEntity().hasMetadata("angry") ? 8 : plugin.util.rand(20, 25));
	}
	
	@Override
	public void onSkill(ActiveMob mob, Expedition exp) {
		if (mob.getEntity().getBukkitEntity().hasMetadata("angry")) {
			for (Player p : exp.getMembers()) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
			}
		} else {
			int nowAmount = -1;
			
			ProtectedRegion region = WGBukkit.getRegionManager(plugin.epple.world).getRegion("boss-room_"+exp.getChannel());
			
			for (Entity entity : plugin.epple.world.getEntities()) {
				final Location loc = entity.getLocation();
				
				if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) && entity instanceof Enderman) {
					nowAmount ++;
				}
			}
			
			int max = 6 * exp.getMembers().size();
			int amount = 4;
			
			if (nowAmount + amount > max)
				amount = max - nowAmount;
			
			for (int i = 0; i < amount; i ++) {
				MobSpawner.SpawnMythicMob("Minion_9F_Enderman", mob.getEntity().getBukkitEntity().getLocation());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onSpawn(MythicMobSpawnEvent e, Expedition exp) {
		blocks.clear();
		blocks.add(new MaterialData(Material.TNT));
		blocks.add(new MaterialData(Material.ANVIL, (byte) 2));
		blocks.add(new MaterialData(Material.COMMAND));
		blocks.add(new MaterialData(Material.ICE));
		blocks.add(new MaterialData(Material.COAL_BLOCK));
		blocks.add(new MaterialData(Material.MOB_SPAWNER));
		blocks.add(new MaterialData(Material.PISTON_STICKY_BASE, (byte) 2));
		blocks.add(new MaterialData(Material.OBSIDIAN));
		blocks.add(new MaterialData(Material.CACTUS));
		
		Enderman enderman = (Enderman) e.getEntity();
		
		enderman.setCarriedMaterial(blocks.get(plugin.util.random.nextInt(blocks.size())));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, final Player victim, Expedition exp) {
		Enderman enderman = (Enderman) damager.getEntity().getBukkitEntity();
		
		switch (enderman.getCarriedMaterial().getItemType()) {
		case TNT:
			if (exp.getData() != null) break;
			
			exp.setData(new TNTTask(exp).runTaskTimer(plugin, 20, 20));
			break;
		case ANVIL:
			for (ItemStack item : victim.getEquipment().getArmorContents()) {
				if (item != null && (item.getType().name().endsWith("_HELMET") || 
								item.getType().name().endsWith("_CHESTPLATE") || 
								item.getType().name().endsWith("_LEGGINGS") || 
								item.getType().name().endsWith("_BOOTS"))) {
					item.setDurability((short) (item.getDurability() + (item.getType().getMaxDurability() / 3)));
				}
			}
			
			victim.playSound(victim.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
			ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.MINECART, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 50, victim.getLocation(), 32);
			
			ActionBarAPI.sendActionBar(victim, "§e방어구가 심각히 손상되었습니다!");
			break;
		case COMMAND:
			if (damager.getEntity().getBukkitEntity().hasMetadata("cmdTask") || exp.getData() != null) break;
			
			new CommandTask(damager, exp).runTaskTimer(plugin, 20, 40);
			
			damager.getEntity().getBukkitEntity().setMetadata("cmdTask", new FixedMetadataValue(plugin, ""));
			break;
		case ICE:
			victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*30, 127));
			
			ActionBarAPI.sendActionBar(victim, "§e온몸이 얼어붙어옵니다.");
			break;
		case NETHERRACK:
			victim.setFireTicks(140);
			
			ActionBarAPI.sendActionBar(victim, "§e앗, 뜨거워!");
			break;
		case MOB_SPAWNER:
			for (int i = 0; i < 4; i ++) {
				MobSpawner.SpawnMythicMob("Minion_9F_Enderman", damager.getEntity().getBukkitEntity().getLocation());
			}
			
			ParticleEffect.FLAME.display(0.5F, 0.5F, 0.5F, 0.0F, 15, damager.getEntity().getBukkitEntity().getLocation().add(0, 1.5, 0), 32);
			
			break;
		case PISTON_STICKY_BASE:
			for (Player p : exp.getMembers()) {
				Vector from = p.getLocation().toVector();
				Vector to = damager.getEntity().getBukkitEntity().getLocation().toVector();
				
				p.setVelocity(to.subtract(from).multiply(0.5D));
				
				p.playSound(p.getLocation(), Sound.PISTON_RETRACT, 1.0F, 1.0F);
			}
			
			break;
		case OBSIDIAN:
			
			return;
		case CACTUS:
			ItemStack[] contents = victim.getInventory().getContents().clone();
			
			for (ItemStack item : contents) {
				if (item == null || item.getType() != Material.POTION) continue;
				
				item.setType(Material.GLASS_BOTTLE);
				item.setDurability((short) 0);
			}
			
			victim.getInventory().setContents(contents);
			
			ActionBarAPI.sendActionBar(victim, "§e소지중인 모든 포션이 말라버렸습니다!");
			break;
		default:
			break;
		}
		
		teleport(damager, exp);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp) {
		Enderman enderman = (Enderman) victim.getEntity().getBukkitEntity();
		
		if (enderman.getCarriedMaterial().getItemType() == Material.OBSIDIAN) {
			e.setCancelled(true);
			
			if (damager.getItemInHand().getType().name().endsWith("_PICKAXE")) {
				damager.playSound(damager.getLocation(), Sound.ENDERDRAGON_HIT, 1.0F, 2.0F);
				
				Location loc = enderman.getLocation().add(0, 1.5, 0);
				
				if (Math.random() <= 0.3) {
					teleport(victim, exp);
					
					ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.OBSIDIAN, (byte) 0), 0.0F, 0.0F, 0.0F, 0.0F, 50, loc, 32);
				} else {
					ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.OBSIDIAN, (byte) 0), 0.0F, 0.0F, 0.0F, 0.0F, 50, loc, 32);
				}
			} else {
				damager.playSound(damager.getLocation(), Sound.ZOMBIE_METAL, 1.0F, 2.0F);
				
				ActionBarAPI.sendActionBar(damager, "§c공격이 먹히지 않습니다!");
			}
			
			return;
		}
		
		if (Math.random() <= 0.1 && victim.getLivingEntity().getHealth() - e.getFinalDamage() > 0) {
			teleport(victim, exp);
		}
		
		if (exp.getData() != null) {
			((BukkitTask) exp.getData()).cancel();
			exp.setData(null);
		}
		
		if (!victim.getEntity().getBukkitEntity().hasMetadata("angry") && victim.getLivingEntity().getHealth() <= 200) {
			victim.getEntity().getBukkitEntity().setMetadata("angry", new FixedMetadataValue(plugin, ""));
			
			for (Player p : exp.getMembers()) {
				p.playSound(p.getLocation(), Sound.ENDERMAN_SCREAM, 10.0F, 0.25F);
				ActionBarAPI.sendActionBar(p, "§4§lIT'S TIME TO DIE!");
			}
		}
	}
	
	public void teleport(ActiveMob mob, Expedition exp) {
		Enderman enderman = (Enderman) mob.getEntity().getBukkitEntity();
		
		enderman.setCarriedMaterial(blocks.get(plugin.util.random.nextInt(blocks.size())));
		
		ParticleEffect.PORTAL.display(0.1F, 0.1F, 0.1F, 1.0F, 500, enderman.getLocation(), 32);
		
		Location loc = ScrollUtils.getBossSpawn(exp.getChannel()).clone();
		int[] array = xyz[plugin.util.random.nextInt(xyz.length)];
		
		loc.add(array[0], array[1], array[2]);
		
		enderman.teleport(loc);
		
		ParticleEffect.PORTAL.display(0.1F, 0.1F, 0.1F, 1.0F, 500, loc, 32);
		
		for (Player p : exp.getMembers()) {
			p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			
			LineEffect effect = new LineEffect(plugin.em);
			
			effect.setDynamicOrigin(new DynamicLocation((mob.getEntity().getBukkitEntity().getLocation().add(0, 1, 0))));
			effect.setDynamicTarget(new DynamicLocation((p.getLocation().add(0, 1, 0))));
			effect.particle = ParticleEffect.PORTAL;
			effect.iterations = 1;
			
			effect.start();
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		for (Player p : exp.getMembers()) {
			p.playSound(p.getLocation(), Sound.ENDERMAN_DEATH, 10.0F, 0.25F);
		}
	}
	
	private class TNTTask extends BukkitRunnable {
		private int phase = 0;
		private final Expedition exp;
		
		public TNTTask(Expedition exp) {
			this.exp = exp;
		}
		
		@Override
		public void run() {
			switch (phase) {
			case 0:
				for (Player p : exp.getMembers()) {
					p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
					TitleAPI.sendTitle(p, 5, 30, 20, "", "§e3");
				}
				
				phase ++;
				break;
			case 1:
				for (Player p : exp.getMembers()) {
					p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
					TitleAPI.sendTitle(p, 5, 30, 20, "", "§c2");
				}
				
				phase ++;
				break;
			case 2:
				for (Player p : exp.getMembers()) {
					p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
					TitleAPI.sendTitle(p, 5, 30, 20, "", "§41");
				}
				
				phase ++;
				break;
			case 3:
				for (Player p : exp.getMembers()) {
					for (int i = 0; i < 3; i ++)
						p.getWorld().spawn(p.getLocation(), TNTPrimed.class).setFuseTicks(5);
					
					p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
					TitleAPI.sendTitle(p, 5, 40, 20, "", "§4§lBoom!");
				}
				
				this.cancel();
				exp.setData(null);
				break;
			}
		}
	}
	
	private class CommandTask extends BukkitRunnable {
		public final String text = "/kill ";
		
		private int phase = 0;
		private final Expedition exp;
		private final ActiveMob mob;
		
		public CommandTask(ActiveMob mob, Expedition exp) {
			this.exp = exp;
			this.mob = mob;
		}
		
		@Override
		public void run() {
			if (phase == 6) {
				List<Player> players = new ArrayList<Player>();
				for (Player p : exp.getMembers())
					players.add(p);
				
				Collections.shuffle(players);
				
				final Player target = players.get(0);
				
				for (Player p : exp.getMembers()) {
					ActionBarAPI.sendActionBar(p, "§c/kill "+target.getName());
				}
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						if (target != null && !target.isDead() && mob != null && mob.getEntity().getBukkitEntity() != null && !mob.getLivingEntity().isDead()) {
							target.damage(999999);
						}
					}
				}, 60L);
				
				this.cancel();
				mob.getEntity().getBukkitEntity().removeMetadata("cmdTask", plugin);
			} else {
				for (Player p : exp.getMembers()) {
					ActionBarAPI.sendActionBar(p, "§e"+text.substring(0, phase));
					
					p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
				}
				
				phase ++;
			}
		}
	}
}