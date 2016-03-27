package me.efe.efemobs.rudish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.confuser.barapi.BarAPI;
import me.efe.efeisland.IslandUtils;
import me.efe.efemobs.EfeCylinderEffect;
import me.efe.efemobs.EfeMobs;
import me.efe.efemobs.Expedition;
import me.libraryaddict.disguise.DisguiseAPI;
import mkremins.fanciful.FancyMessage;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMobHandler;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class BossListener implements Listener {
	public EfeMobs plugin;
	public HashMap<Integer, Expedition> expMap = new HashMap<Integer, Expedition>();
	public HashMap<Player, Integer> playerMap = new HashMap<Player, Integer>();
	private List<PotionEffectType> debuffs = new ArrayList<PotionEffectType>();
	
	public BossListener(EfeMobs plugin) {
		this.plugin = plugin;
		
		debuffs.add(PotionEffectType.SLOW);
		debuffs.add(PotionEffectType.SLOW_DIGGING);
		debuffs.add(PotionEffectType.CONFUSION);
		debuffs.add(PotionEffectType.BLINDNESS);
		debuffs.add(PotionEffectType.HUNGER);
		debuffs.add(PotionEffectType.WEAKNESS);
		debuffs.add(PotionEffectType.POISON);
		debuffs.add(PotionEffectType.WITHER);
	}
	
	public void clear() {
		for (int channel : expMap.keySet()) {
			clearRoom(channel);
		}
	}
	
	public Expedition getExpedition(Player p) {
		return expMap.get(playerMap.get(p));
	}
	
	@EventHandler
	public void kill(MythicMobDeathEvent e) {
		if (e.getMobType().getInternalName().startsWith("Boss_")) {
			final int channel = getChannel(e.getLivingEntity().getLocation());
			final Expedition exp = expMap.get(channel);
			final int floor = exp.getFloor();
			
			exp.broadcast("��a�ơ�r ��7============================================");
			exp.broadcast("��a�ơ�r ");
			
			for (Player m : exp.getMembers()) {
				UserData data = new UserData(m);
				
				data.setSoul(0);
				data.setStoredSoul(0);
				plugin.huntListener.updateStorageHologram(m);
				
				if (data.getMaxFloor() == floor && floor != 10) {
					data.raiseFloor();
					
					m.sendMessage("��a�ơ�r ��b��l>> New Floor Is Now Available!");
					m.sendMessage("��a�ơ�r ");
					m.sendMessage("��a�ơ�r ���� ��e"+data.getMaxFloor()+"����r�� ������ �� �ֽ��ϴ�!");
					m.sendMessage("��a�ơ�r �߾ӿ� �ִ� ��d����� ���ڸ� ���� ������ ȹ���r�ϼ���.");
				} else {
					m.sendMessage("��a�ơ�r ��b��l>> The Boss Has Been Eliminated!");
					m.sendMessage("��a�ơ�r ");
					m.sendMessage("��a�ơ�r �߾ӿ� �ִ� ����� ��d���ڸ� ���� ������ ȹ���r�ϼ���!");
					m.sendMessage("��a�ơ�r ");
				}
				
				m.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*7, 4));
				m.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20*3, 0));
				
				DisguiseAPI.undisguiseToAll(m);
				me.efe.titlemaker.api.TitleAPI.setInvisibleHologram(m, false);
				BarAPI.setMessage(m, "��aVICTORY!!", 0.0F);
				clearDebuff(m);
			}
			
			exp.broadcast("��a�ơ�r ");
			exp.broadcast("��a�ơ�r �ҿ� �ð�: ��e"+exp.getPlayTime());
			exp.broadcast("��a�ơ�r ");
			exp.broadcast("��a�ơ�r ��7============================================");
			
			ProtectedRegion region = WGBukkit.getRegionManager(plugin.epple.world).getRegion("boss-room_"+channel);
			
			for (Entity entity : plugin.epple.world.getEntities()) {
				if (!(entity instanceof Player) && !entity.equals(e.getEntity())) {
					Location loc = entity.getLocation();
					
					if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
						entity.remove();
					}
				}
			}
			
			effect(e.getLivingEntity().getLocation());
			
			exp.cancelTask();
			
			plugin.chestListener.createChest(exp);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					for (Player m : exp.getMembers()) {
						new FancyMessage("��a�ơ�r ������ �ް� ")
						.then("��b��n/��ȯ��r")
							.command("/��ȯ")
							.tooltip("��b/��ȯ")
						.then(" ��ɾ�� ��ȯ�ϼ���!")
						.send(m);
						
						BarAPI.removeBar(m);
					}
					
					exp.broadcast("��a�ơ�r 90�� �� ��𽬷� ���� ��ȯ�˴ϴ�.");
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@SuppressWarnings("deprecation")
						public void run() {
							if (!expMap.containsValue(exp)) return;
							
							for (Player m : exp.getMembers()) {
								plugin.chestListener.removeChest(m);
								
								playerMap.remove(m);
								
								Location loc = IslandUtils.getIsleLoc(IslandUtils.RUDISH);
								
								loc.getWorld().refreshChunk(loc.getBlockX(), loc.getBlockZ());
								m.teleport(loc);
							}
							
							expMap.remove(channel);
						}
					}, 20*90L);
				}
			}, 20*5L);
		}
	}
	
	@EventHandler
	public void death(PlayerDeathEvent e) {
		if (playerMap.containsKey(e.getEntity())) {
			int floor = playerMap.get(e.getEntity());
			Expedition exp = expMap.get(floor);
			
			e.getEntity().setMetadata("boss_death", new FixedMetadataValue(plugin, "RIP"));
			
			BarAPI.removeBar(e.getEntity());
			
			exp.kick(e.getEntity());
			exp.broadcast("��a�ơ�r "+e.getEntity().getName()+"���� ����߽��ϴ�.");
			
			playerMap.remove(e.getEntity());
			
			plugin.chestListener.removeChest(e.getEntity());
			
			if (exp.getMembers().isEmpty()) {
				clearRoom(exp.getChannel());
				expMap.remove(floor);
				
				e.getEntity().sendMessage("��a�ơ�r ������� ��� ����� ���� ������ �����߽��ϴ�!");
			}
			
			playerMap.remove(e.getEntity());
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if (playerMap.containsKey(e.getPlayer())) {
			int channel = playerMap.get(e.getPlayer());
			Expedition exp = expMap.get(channel);
			
			exp.kick(e.getPlayer());
			exp.broadcast("��a�ơ�r "+e.getPlayer().getName()+"���� �����Ͽ� �����뿡�� ���ܵǾ����ϴ�.");
			
			playerMap.remove(e.getPlayer());
			
			plugin.chestListener.removeChest(e.getPlayer());
			
			if (exp.getMembers().isEmpty()) {
				clearRoom(channel);
				return;
			}
			
			playerMap.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		if (isBossRoom(e.getPlayer().getLocation())) {
			e.getPlayer().teleport(IslandUtils.getIsleLoc(IslandUtils.RUDISH));
			e.getPlayer().removeMetadata("boss_genocide", plugin);
			
			clearDebuff(e.getPlayer());
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void respawn(PlayerRespawnEvent e) {
		if (e.getPlayer().hasMetadata("boss_death")) {
			e.setRespawnLocation(IslandUtils.getIsleLoc(IslandUtils.RUDISH));
			
			e.getPlayer().removeMetadata("boss_death", plugin);
		}
	}
	
	public ActiveMob getBoss(int channel) {
		ProtectedRegion region = WGBukkit.getRegionManager(plugin.epple.world).getRegion("boss-room_"+channel);
		
		for (Entity entity : plugin.epple.world.getEntities()) {
			if (entity instanceof LivingEntity && !(entity instanceof Player)) {
				Location loc = entity.getLocation();
				
				if (!region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) continue;
				if (!plugin.mythicMobs.getAPI().getMobAPI().isMythicMob(entity)) continue;
				
				ActiveMob mob = ActiveMobHandler.getMythicMobInstance(entity);
				
				if (mob.getType().getInternalName().startsWith("Boss_")) return mob;
			}
		}
		
		return null;
	}
	
	public void clearRoom(int channel) {
		ProtectedRegion region = WGBukkit.getRegionManager(plugin.epple.world).getRegion("boss-room_"+channel);
		
		region.setFlag(DefaultFlag.PVP, State.DENY);
		
		for (Entity entity : plugin.epple.world.getEntities()) {
			if (!(entity instanceof Player)) {
				Location loc = entity.getLocation();
				
				if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
					entity.remove();
				}
			}
		}
		
		for (Player m : expMap.get(channel).getMembers()) {
			BarAPI.removeBar(m);
			DisguiseAPI.undisguiseToAll(m);
			
			plugin.chestListener.removeChest(m);
			
			playerMap.remove(m);
			
			m.teleport(IslandUtils.getIsleLoc(IslandUtils.RUDISH));
		}
		
		expMap.get(channel).cancelTask();
		expMap.remove(channel);
	}
	
	public boolean isBossRoom(Location loc) {
		for (ProtectedRegion region : WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc)) {
			if (region.getId().startsWith("boss-room_")) {
				return true;
			}
		}
		
		return false;
	}
	
	public int getChannel(Location loc) {
		for (ProtectedRegion region : WGBukkit.getRegionManager(loc.getWorld()).getApplicableRegions(loc)) {
			if (region.getId().startsWith("boss-room_")) {
				return Integer.parseInt(region.getId().substring(10));
			}
		}
		
		return -1;
	}
	
	private void clearDebuff(Player p) {
		Iterator<PotionEffect> it = p.getActivePotionEffects().iterator();
		while (it.hasNext()) {
			if (debuffs.contains(it.next().getType())) {
				it.remove();
			}
		}
	}
	
	private void effect(final Location loc) {
		for (Player all : plugin.util.getOnlinePlayers())
			all.playSound(loc, Sound.ENDERDRAGON_DEATH, 10.0F, 1.5F);
		
		final Firework firework = loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		FireworkEffect effect = FireworkEffect.builder().with(Type.BALL_LARGE).withColor(Color.RED).withTrail().build();
		
		meta.addEffect(effect);
		meta.setPower(1);
		firework.setFireworkMeta(meta);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				firework.detonate();
			}
		}, 3);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.EXPLOSION_LARGE;
				cylinder.radius = 7.0F;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation((loc)));
				cylinder.start();
				
				for (Player all : plugin.util.getOnlinePlayers())
					all.playSound(loc, Sound.EXPLODE, 1.0F, 1.0F);
			}
		}, 10L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.EXPLOSION_LARGE;
				cylinder.radius = 7.0F;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation((loc.clone().add(0, 1, 0))));
				cylinder.start();
				
				for (Player all : plugin.util.getOnlinePlayers())
					all.playSound(loc, Sound.EXPLODE, 1.0F, 1.0F);
			}
		}, 20L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.EXPLOSION_LARGE;
				cylinder.radius = 7.0F;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation((loc.clone().add(0, 2, 0))));
				cylinder.start();
				
				for (Player all : plugin.util.getOnlinePlayers())
					all.playSound(loc, Sound.EXPLODE, 1.0F, 1.0F);
			}
		}, 30L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.EXPLOSION_LARGE;
				cylinder.radius = 7.0F;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation((loc.clone().add(0, 3, 0))));
				cylinder.start();
				
				for (Player all : plugin.util.getOnlinePlayers())
					all.playSound(loc, Sound.EXPLODE, 1.0F, 1.0F);
			}
		}, 40L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.EXPLOSION_LARGE;
				cylinder.radius = 7.0F;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation((loc.clone().add(0, 4, 0))));
				cylinder.start();
				
				for (Player all : plugin.util.getOnlinePlayers())
					all.playSound(loc, Sound.EXPLODE, 1.0F, 1.0F);
			}
		}, 50L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.FLAME;
				cylinder.radius = 1.0F;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation((loc)));
				cylinder.start();
				
				for (Player all : plugin.util.getOnlinePlayers())
					all.playSound(loc, Sound.FUSE, 1.0F, 1.0F);
			}
		}, 60L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.FLAME;
				cylinder.radius = 4.0F;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation((loc)));
				cylinder.start();
				
				for (Player all : plugin.util.getOnlinePlayers())
					all.playSound(loc, Sound.FUSE, 1.0F, 1.0F);
			}
		}, 65L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.FLAME;
				cylinder.radius = 7.0F;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation((loc)));
				cylinder.start();
				
				for (Player all : plugin.util.getOnlinePlayers())
					all.playSound(loc, Sound.FUSE, 1.0F, 1.0F);
			}
		}, 70L);
	}
}