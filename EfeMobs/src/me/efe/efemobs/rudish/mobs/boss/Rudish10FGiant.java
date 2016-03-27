package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeitems.ItemStorage;
import me.efe.efemobs.Expedition;
import me.efe.efemobs.ScrollUtils;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.util.ParticleEffect;

public class Rudish10FGiant extends RudishBoss {
	public String[] mobs = new String[]{"좀비", "늑대", "슬라임", "좀벌레", "거미", "크리퍼", "스켈레톤"};
	private int[][] xyz = new int[][]{{0, 0, 0}, {0, 0, -20}, {0, 0, -40}, {0, 0, -60}, {23, 1, -5}, {23, 1, -25}, {23, 1, -45}, {-23, 1, -5}, {-23, 1, -25}, {-23, 1, -45}};

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.ROTTEN_FLESH, plugin.util.rand(32, 64)));
		
		if (Math.random() <= 0.3)
			list.add(ItemStorage.ANVIL_ENCHANTED.clone());
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		boolean angry1 = mob.getEntity().getBukkitEntity().hasMetadata("angry1");
		boolean angry2 = mob.getEntity().getBukkitEntity().hasMetadata("angry2");
		boolean angry3 = mob.getEntity().getBukkitEntity().hasMetadata("angry3");
		
		return angry3 ? 999 : 20 * (angry1 || angry2 ? 20 : plugin.util.rand(15, 20));
	}
	
	@Override
	public void onSkill(final ActiveMob mob, final Expedition exp) {
		if (mob.getEntity().getBukkitEntity().hasMetadata("angry1")) {
			killMinions(exp.getChannel());
			
			int x = plugin.util.rand(1, 7);
			int y = plugin.util.rand(1, 5);
			int z = plugin.util.rand(1, 5);
			
			int answer = x + y * z;
			exp.setData(new int[]{0, answer});
			
			String str = mobs[x - 1]+" + "+mobs[y - 1]+" × "+mobs[z - 1]+" = ?";
			
			for (Player p : exp.getMembers()) {
				TitleAPI.sendTitle(p, 40, 100, 40, "", str);
				p.sendMessage("§a▒§r "+str);
			}
			
			for (int i = 0; i < 30; i ++) {
				MobSpawner.SpawnMythicMob("Minion_10F_QuizZombie", mob.getEntity().getBukkitEntity().getLocation());
			}
			
		} else if (mob.getEntity().getBukkitEntity().hasMetadata("angry2")) {
			killMinions(exp.getChannel());
			
			String str = "";
			
			for (int i = 0; i < 7; i ++) {
				str += (plugin.util.random.nextBoolean() ? "z" : "Z");
			}
			
			exp.setData(new String[]{"", str});
			
			for (Player p : exp.getMembers()) {
				TitleAPI.sendTitle(p, 40, 100, 40, "", str);
				p.sendMessage("§a▒§r "+str);
			}
			
			for (int i = 0; i < 20; i ++) {
				MobSpawner.SpawnMythicMob("Minion_10F_QuizZombie", mob.getEntity().getBukkitEntity().getLocation());
			}
			for (int i = 0; i < 20; i ++) {
				MobSpawner.SpawnMythicMob("Minion_10F_QuizBabyZombie", mob.getEntity().getBukkitEntity().getLocation());
			}
			
		} else if (mob.getEntity().getBukkitEntity().hasMetadata("angry3")) {
			//Nothing
		} else {
			switch (plugin.util.random.nextInt(3)) {
			case 0:
				for (final Player p : exp.getMembers()) {
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10.0F, 2.0F);
					ParticleEffect.SMOKE_LARGE.display(0.0F, 0.0F, 0.0F, 0.1F, 50, p.getEyeLocation(), p);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						
						@Override
						public void run() {
							for (int i = 0; i < 4; i ++) {
								LivingEntity entity = (LivingEntity) MobSpawner.SpawnMythicMob("Minion_7F_Wraith", p.getLocation().add(0, 7, 0));
								entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 2));
							}
						}
					}, 20L);
				}
				
				break;
			case 1:
				for (final Player p : exp.getMembers()) {
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10.0F, 2.0F);
					ParticleEffect.SMOKE_LARGE.display(0.0F, 0.0F, 0.0F, 0.1F, 50, p.getEyeLocation(), p);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						
						@Override
						public void run() {
							for (int i = 0; i < 4; i ++) {
								LivingEntity entity = (LivingEntity) MobSpawner.SpawnMythicMob("Minion_8F_Rider", p.getLocation().add(0, 7, 0));
								entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 2));
							}
						}
					}, 20L);
				}
				
				break;
			case 2:
				for (final Player p : exp.getMembers()) {
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10.0F, 2.0F);
					ParticleEffect.SMOKE_LARGE.display(0.0F, 0.0F, 0.0F, 0.1F, 50, p.getEyeLocation(), p);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						
						@Override
						public void run() {
							for (int i = 0; i < 4; i ++) {
								LivingEntity entity = (LivingEntity) MobSpawner.SpawnMythicMob("Minion_9F_Enderman", p.getLocation().add(0, 7, 0));
								entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 2));
							}
						}
					}, 20L);
				}
				
				break;
			}
		}
	}
	
	private void killMinions(int channel) {
		ProtectedRegion region = WGBukkit.getRegionManager(plugin.epple.world).getRegion("boss-room_"+channel);
		
		for (Entity entity : plugin.epple.world.getEntities()) {
			final Location loc = entity.getLocation();
			
			if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
				if (entity.getType() == EntityType.ZOMBIE) {
					entity.setMetadata("deathnity", new FixedMetadataValue(plugin, ""));
					((Zombie) entity).damage(99999);
				}
			}
		}
	}
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e, Expedition exp) {
		for (Player p : exp.getMembers()) {
			MobDisguise disguise = new MobDisguise(DisguiseType.ZOMBIE);
			LivingWatcher watcher = disguise.getWatcher();
			ItemStack air = new ItemStack(Material.AIR);
			
			watcher.setArmor(new ItemStack[]{air.clone(), air.clone(), air.clone(), air.clone(), air.clone()});
			disguise.setWatcher(watcher);
			DisguiseAPI.disguiseToPlayers(p, disguise, exp.getMembers());
			me.efe.titlemaker.api.TitleAPI.setInvisibleHologram(p, true);
		}
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim, Expedition exp) {
		victim.setFoodLevel(victim.getFoodLevel() != 0 ? victim.getFoodLevel() - 1 : 0);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp) {
		boolean angry1 = victim.getEntity().getBukkitEntity().hasMetadata("angry1");
		boolean angry2 = victim.getEntity().getBukkitEntity().hasMetadata("angry2");
		boolean angry3 = victim.getEntity().getBukkitEntity().hasMetadata("angry3");
		
		if (angry1) {
			int[] data = (int[]) exp.getData();
			
			if (data == null || data[0] != data[1]) {
				e.setCancelled(true);
				ActionBarAPI.sendActionBar(damager, "§c몬스터가 공격을 튕겨냈습니다!");
				return;
			}
		}
		
		if (angry2) {
			String[] data = (String[]) exp.getData();
			
			if (data == null || !data[0].equals(data[1])) {
				e.setCancelled(true);
				ActionBarAPI.sendActionBar(damager, "§c몬스터가 공격을 튕겨냈습니다!");
				return;
			}
		}
		
		if (!angry1 && !angry2 && !angry3 && victim.getLivingEntity().getHealth() - e.getFinalDamage() > 0) {
			LivingEntity entity = (LivingEntity) MobSpawner.SpawnMythicMob("Minion_10F_Zombie", damager.getLocation().add(0, 7, 0));
			
			entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 2));
		}
		
		if (angry3 && victim.getLivingEntity().getHealth() - e.getFinalDamage() > 0) {
			for (Player p : exp.getMembers()) {
				p.teleport(victim.getLivingEntity().getLocation());
				p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10.0F, 1.0F);
			}
			
			Location loc = ScrollUtils.getBossSpawn(exp.getChannel()).clone();
			int[] array = xyz[plugin.util.random.nextInt(xyz.length)];
			
			loc.add(array[0], array[1], array[2]);
			
			victim.getLivingEntity().teleport(loc);
			
			for (int i = 0; i < 30; i ++) {
				Zombie zombie = (Zombie) MobSpawner.SpawnMythicMob("Minion_10F_Zombie", victim.getLocation());
				
				zombie.setVelocity(new Vector(Math.random() * 2 - 1, 0.3, Math.random() * 2 - 1).multiply(1.5D));
				zombie.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 2));
			}
		}
		
		if (!angry1 && !angry2 && !angry3 && victim.getLivingEntity().getHealth() <= 490) {
			victim.getEntity().getBukkitEntity().setMetadata("angry1", new FixedMetadataValue(plugin, ""));
			
			for (Player p : exp.getMembers()) {
				p.setFoodLevel(0);
				
				p.playSound(p.getLocation(), Sound.WITHER_DEATH, 10.0F, 0.5F);
				ActionBarAPI.sendActionBar(p, "§c그들은 당신의 뇌만을 노리고 있습니다.");
			}
		}
		
		if (!angry2 && !angry3 && victim.getLivingEntity().getHealth() <= 350) {
			victim.getEntity().getBukkitEntity().removeMetadata("angry1", plugin);
			victim.getEntity().getBukkitEntity().setMetadata("angry2", new FixedMetadataValue(plugin, ""));
			
			exp.setData(null);
			
			for (Player p : exp.getMembers()) {
				p.setFoodLevel(0);
				
				p.playSound(p.getLocation(), Sound.WITHER_DEATH, 10.0F, 0.5F);
				ActionBarAPI.sendActionBar(p, "§c그는 분노를 참지 못하고 있습니다.");
			}
		}
		
		if (!angry3 && victim.getLivingEntity().getHealth() <= 140) {
			victim.getEntity().getBukkitEntity().removeMetadata("angry2", plugin);
			victim.getEntity().getBukkitEntity().setMetadata("angry3", new FixedMetadataValue(plugin, ""));
			
			exp.setData(null);
			killMinions(exp.getChannel());
			
			for (Player p : exp.getMembers()) {
				p.setFoodLevel(0);
				
				p.playSound(p.getLocation(), Sound.WITHER_DEATH, 10.0F, 0.5F);
				ActionBarAPI.sendActionBar(p, "§c최후의 전투가 시작되었습니다.");
			}
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		for (Player p : exp.getMembers()) {
			ActionBarAPI.sendActionBar(p, "§a루디쉬 타워 클리어를 축하합니다 !!");
		}
	}
}