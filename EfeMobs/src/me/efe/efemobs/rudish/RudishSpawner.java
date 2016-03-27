package me.efe.efemobs.rudish;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMobHandler;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.efe.efemobs.EfeMobs;

public class RudishSpawner implements Listener {
	public EfeMobs plugin;
	public Random random = new Random();
	public List<RespawnTask> respawnTasks = new ArrayList<RespawnTask>();
	public List<String[]> mobTypes = new ArrayList<String[]>();
	
	public RudishSpawner(EfeMobs plugin) {
		this.plugin = plugin;
		
		for (int i = 0; i < 10; i ++) {
			RespawnTask task = new RespawnTask(plugin.teleportGui.cores.get(i + 4), i + 1);
			
			task.runTaskTimer(plugin, 100, 100);
			respawnTasks.add(task);
		}
		
		mobTypes.add(new String[]{"1F_Zombie"});
		mobTypes.add(new String[]{"2F_Wolf", "2F_Snowman"});
		mobTypes.add(new String[]{"3F_Slime", "3F_Witch"});
		mobTypes.add(new String[]{"4F_IronGolem", "4F_Silverfish"});
		mobTypes.add(new String[]{"5F_Spider", "5F_CaveSpider"});
		mobTypes.add(new String[]{"6F_ExplodeCreeper"});
		mobTypes.add(new String[]{"7F_Skeleton", "7F_WitherSkeleton"});
		mobTypes.add(new String[]{"8F_MagmaCube", "8F_Blaze"});
		mobTypes.add(new String[]{"9F_Enderman"});
		mobTypes.add(new String[]{"10F_SwordZombie", "10F_MarioZombie", "10F_MirrorZombie", "10F_SeparateZombie", "10F_DrainZombie"});
		
		new TeleportTask().runTaskTimer(plugin, 20, 20);
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void spawn(final MythicMobSpawnEvent e) {
		final int floor = EfeMobs.getFloor(e.getLocation());
		
		if (floor < 1) return;
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				ActiveMob mob = ActiveMobHandler.getMythicMobInstance(e.getEntity());
				respawnTasks.get(floor - 1).spawnedMobs.add(mob);
			}
		}, 20*1L);
	}
	
	@EventHandler
	public void death(MythicMobDeathEvent e) {
		int floor = EfeMobs.getFloor(e.getEntity().getLocation());
		
		if (floor < 1) return;
		
		respawnTasks.get(floor - 1).spawnedMobs.remove(e.getMobInstance());
	}
	
	private class RespawnTask extends BukkitRunnable {
		private List<ActiveMob> spawnedMobs = new ArrayList<ActiveMob>();
		private Location center;
		private int floor;
		
		public RespawnTask(Location center, int floor) {
			this.center = center;
			this.floor = floor;
		}
		
		@Override
		public void run() {
			int players = getPlayerAmount();
			int max = players * 6;
			
			if (spawnedMobs.size() >= max) return;
			
			int amount = plugin.util.rand(players * 2 + 1, players * 3);
			int possible = max - spawnedMobs.size();
			
			if (amount >= possible)
				amount = possible;
			
			for (int i = 0; i < amount; i ++) {
				Location loc = getRandomLocation();
				
				if (loc == null)
					continue;
				
				String[] types = mobTypes.get(floor - 1);
				String type = types[random.nextInt(types.length)];
				
				LivingEntity entity = (LivingEntity) MobSpawner.SpawnMythicMob(type, loc);
				
				if (entity != null)
					entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 0));
			}
		}
		
		private int getPlayerAmount() {
			int amount = 0;
			
			for (Player p : center.getWorld().getPlayers()) {
				Location pLoc = p.getLocation();
				Location cLoc = center.clone();
				
				if (Math.abs(pLoc.getY() - cLoc.getY()) > 7)
					continue;
				
				pLoc.setY(0);
				cLoc.setY(0);
				
				if (pLoc.distance(cLoc) <= 20) {
					amount ++;
				}
			}
			
			return amount;
		}
		
		private Location getRandomLocation() {
			Location loc = center.clone();
			
			loc.add(plugin.util.rand(-20, 20), -2, plugin.util.rand(-20, 20));
			
			return getGround(loc.clone());
		}
		
		private Location getGround(Location loc) {
			for (int i = 0; i < 9; i ++) {
				loc.add(0, i, 0);
				
				if (loc.getWorld().getBlockAt(loc) != null && !loc.getWorld().getBlockAt(loc).getType().isSolid()) {
					loc.add(0.5, 0.5, 0.5);
					
					return loc;
				}
			}
			
			return null;
		}
	}
	
	private class TeleportTask extends BukkitRunnable {
		
		@Override
		public void run() {
			for (RespawnTask task : respawnTasks) {
				ListIterator<ActiveMob> it = task.spawnedMobs.listIterator();
				
				while (it.hasNext()) {
					ActiveMob mob = it.next();
					
					if (mob == null || mob.getEntity().getBukkitEntity() == null || mob.getEntity().getBukkitEntity().isDead()) {
						it.remove();
						return;
					}
					
					Location mLoc = mob.getEntity().getBukkitEntity().getLocation();
					Location cLoc = task.center.clone();
					mLoc.setY(0);
					cLoc.setY(0);
					
					if (mLoc.distance(cLoc) > 40) {
						mob.getEntity().getBukkitEntity().teleport(cLoc);
					}
				}
			}
		}
	}
}