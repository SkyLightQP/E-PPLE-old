package me.efe.efeisland;

import java.util.HashMap;

import me.efe.efecommunity.UserData;
import me.efe.efeisland.utils.EfeCylinderEffect;
import me.efe.efemobs.EfeMobs;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.mewin.util.Util;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;

public class IslandUtils {
	private static EfeIsland plugin;
	private static World world;
	private static HashMap<String, Location> islands = new HashMap<String, Location>();
	
	public static String POLARIS = "polaris";
	public static String MINERAS_ALPHA = "mineras_alpha";
	public static String MINERAS_BETA = "mineras_beta";
	public static String MINERAS_GAMMA = "mineras_gamma";
	public static String NIMBUS = "nimbus";
	public static String RUDISH = "rudish";
	public static String AQU = "aqu";
	public static String FOREST_ISLAND = "forest_island";
	public static String SNOW_ISLAND = "snow_island";
	public static String JUNGLE_ISLAND = "jungle_island";
	public static String SAVANA_ISLAND = "savana_island";
	public static String TUTORIAL_START = "tutorial_start";
	public static String TUTORIAL_SKILL = "tutorial_skill";
	public static String TUTORIAL_MINE = "tutorial_mine";
	public static String TUTORIAL_WEED = "tutorial_weed";
	public static String TUTORIAL_FARM = "tutorial_farm";
	public static String TUTORIAL_FISH = "tutorial_fish";
	public static String TUTORIAL_HUNT = "tutorial_hunt";
	
	public static String D_POLARIS = "Polaris";
	public static String D_MINERAS_ALPHA = "Mineras α";
	public static String D_MINERAS_BETA = "Mineras β";
	public static String D_MINERAS_GAMMA = "Mineras γ";
	public static String D_NIMBUS = "Nimbus";
	public static String D_RUDISH = "Rudish";
	public static String D_AQU = "Aqu";
	public static String D_FOREST_ISLAND = "Forest Isl.";
	public static String D_SNOW_ISLAND = "Snow Isl.";
	public static String D_JUNGLE_ISLAND = "Jungle Isl.";
	public static String D_SAVANA_ISLAND = "Savana Isl.";
	public static String D_TUTORIAL_START = "시작의 섬";
	public static String D_TUTORIAL_SKILL = "분업의 섬";
	public static String D_TUTORIAL_MINE = "채광의 섬";
	public static String D_TUTORIAL_WEED = "잔디의 섬";
	public static String D_TUTORIAL_FARM = "농사의 섬";
	public static String D_TUTORIAL_FISH = "낚시의 섬";
	public static String D_TUTORIAL_HUNT = "사냥의 섬";
	
	public static void init(EfeIsland pl) {
		plugin = pl;
		world = plugin.getServer().getWorld("world");
		
		islands.put(POLARIS, new Location(world, 0, 70, 0));
		islands.put(MINERAS_ALPHA, new Location(world, 18, 68, -641));
		islands.put(MINERAS_BETA, new Location(world, -49, 66, 1148));
		islands.put(MINERAS_GAMMA, new Location(world, -1473, 71, 346));
		islands.put(NIMBUS, new Location(world, 655, 67, -660));
		islands.put(RUDISH, new Location(world, 1297, 49, 1214));
		islands.put(AQU, new Location(world, -699, 68, -218));
		islands.put(FOREST_ISLAND, new Location(world, 650, 74, 49));
		islands.put(SNOW_ISLAND, new Location(world, -698, 68, -694));
		islands.put(JUNGLE_ISLAND, new Location(world, 521, 72, 709));
		islands.put(SAVANA_ISLAND, new Location(world, -875, 67, 672));
		islands.put(TUTORIAL_START, new Location(world, 2612, 67, 1933));
		islands.put(TUTORIAL_SKILL, new Location(world, 2800, 67, 1930));
		islands.put(TUTORIAL_MINE, new Location(world, 3019, 65, 1905));
		islands.put(TUTORIAL_WEED, new Location(world, 3303, 65, 1938));
		islands.put(TUTORIAL_FARM, new Location(world, 3480, 65, 2098));
		islands.put(TUTORIAL_FISH, new Location(world, 3245, 65, 2147));
		islands.put(TUTORIAL_HUNT, new Location(world, 3100, 65, 2196));
	}
	
	public static String fromDisplay(String display) {
		if (display.equals(D_POLARIS)) return POLARIS;
		if (display.equals(D_MINERAS_ALPHA)) return MINERAS_ALPHA;
		if (display.equals(D_MINERAS_BETA)) return MINERAS_BETA;
		if (display.equals(D_MINERAS_GAMMA)) return MINERAS_GAMMA;
		if (display.equals(D_NIMBUS)) return NIMBUS;
		if (display.equals(D_RUDISH)) return RUDISH;
		if (display.equals(D_AQU)) return AQU;
		if (display.equals(D_FOREST_ISLAND)) return FOREST_ISLAND;
		if (display.equals(D_SNOW_ISLAND)) return SNOW_ISLAND;
		if (display.equals(D_JUNGLE_ISLAND)) return JUNGLE_ISLAND;
		if (display.equals(D_SAVANA_ISLAND)) return SAVANA_ISLAND;
		if (display.equals(D_TUTORIAL_START)) return TUTORIAL_START;
		if (display.equals(D_TUTORIAL_SKILL)) return TUTORIAL_SKILL;
		if (display.equals(D_TUTORIAL_MINE)) return TUTORIAL_MINE;
		if (display.equals(D_TUTORIAL_WEED)) return TUTORIAL_WEED;
		if (display.equals(D_TUTORIAL_FARM)) return TUTORIAL_FARM;
		if (display.equals(D_TUTORIAL_FISH)) return TUTORIAL_FISH;
		if (display.equals(D_TUTORIAL_HUNT)) return TUTORIAL_HUNT;
		
		return null;
	}
	
	public static String toDisplay(String name) {
		if (name.equals(POLARIS)) return D_POLARIS;
		if (name.equals(MINERAS_ALPHA)) return D_MINERAS_ALPHA;
		if (name.equals(MINERAS_BETA)) return D_MINERAS_BETA;
		if (name.equals(MINERAS_GAMMA)) return D_MINERAS_GAMMA;
		if (name.equals(NIMBUS)) return D_NIMBUS;
		if (name.equals(RUDISH)) return D_RUDISH;
		if (name.equals(AQU)) return D_AQU;
		if (name.equals(FOREST_ISLAND)) return D_FOREST_ISLAND;
		if (name.equals(SNOW_ISLAND)) return D_SNOW_ISLAND;
		if (name.equals(JUNGLE_ISLAND)) return D_JUNGLE_ISLAND;
		if (name.equals(SAVANA_ISLAND)) return D_SAVANA_ISLAND;
		if (name.equals(TUTORIAL_START)) return D_TUTORIAL_START;
		if (name.equals(TUTORIAL_SKILL)) return D_TUTORIAL_SKILL;
		if (name.equals(TUTORIAL_MINE)) return D_TUTORIAL_MINE;
		if (name.equals(TUTORIAL_WEED)) return D_TUTORIAL_WEED;
		if (name.equals(TUTORIAL_FARM)) return D_TUTORIAL_FARM;
		if (name.equals(TUTORIAL_FISH)) return D_TUTORIAL_FISH;
		if (name.equals(TUTORIAL_HUNT)) return D_TUTORIAL_HUNT;
		
		return null;
	}
	
	public static Location getIsleLoc(String name) {
		return islands.get(name);
	}
	
	public static ProtectedRegion getIsleRegion(String name) {
		return WGBukkit.getRegionManager(world).getRegion(name);
	}
	
	public static String getIsleName(Location loc) {
		if (loc.getWorld().equals(plugin.world)) return "";
		
		for (ProtectedRegion region : WGBukkit.getRegionManager(world).getApplicableRegions(loc)) {
			for (String name : islands.keySet()) {
				if (region.getId().equals(name)) {
					return name;
				}
			}
		}
		
		return "";
	}
	
	public static ProtectedRegion getIsleRegion(Location loc) {
		String name = getIsleName(loc);
		
		return (name != null) ? getIsleRegion(name) : null;
	}
	
	public static boolean isMineras(Location loc) {
		String name = getIsleName(loc);
		
		ProtectedRegion region = EfeServer.getInstance().getRegion(WGBukkit.getRegionManager(world).getApplicableRegions(loc), MINERAS_BETA);
		
		return name.startsWith("mineras") || name.equals(TUTORIAL_MINE) || region != null;
	}
	
	public static boolean isNimbus(Location loc) {
		String name = getIsleName(loc);
		
		return name.equals(NIMBUS) || name.equals(TUTORIAL_WEED);
	}
	
	public static boolean isRudish(Location loc) {
		String name = getIsleName(loc);
		
		return name.equals(RUDISH);
	}
	
	public static boolean isAqu(Location loc) {
		String name = getIsleName(loc);
		
		return name.equals(AQU) || name.equals(TUTORIAL_FISH);
	}
	
	public static boolean isTreeIsland(Location loc) {
		String name = getIsleName(loc);
		
		return name.equals(FOREST_ISLAND) || name.equals(SNOW_ISLAND) || name.equals(JUNGLE_ISLAND) || name.equals(SAVANA_ISLAND);
	}
	
	@SuppressWarnings("deprecation")
	public static void teleportIsland(final Player p, final OfflinePlayer target) {
		final PlayerData data = PlayerData.get(p);
		
		if (!data.hasIsland() || !PlayerData.get(target.getUniqueId()).hasIsland()) {
			p.sendMessage("§c▒§r 섬이 존재하지 않습니다.");
			return;
		}
		
		if (p.hasMetadata("teleporting")) {
			p.sendMessage("§c▒§r 이미 텔레포트 중입니다.");
			return;
		}
		
		EfeMobs efeMobs = (EfeMobs) plugin.getServer().getPluginManager().getPlugin("EfeMobs");
		if (efeMobs.bossListener.isBossRoom(p.getLocation())) {
			p.sendMessage("§c▒§r 이곳에선 이용할 수 없습니다.");
			return;
		}
		
		ProtectedRegion region = plugin.getIsleRegion(target);
		
		if (region.getFlag(EfeFlag.BLACKLIST).contains(p.getUniqueId().toString())) {
			p.sendMessage("§c▒§r 주인이 당신의 접근을 차단한 섬입니다.");
			return;
		}
		
		if (region.getFlag(EfeFlag.ENTRANCE) == State.DENY &&
				!new UserData(target.getUniqueId()).getFriends().contains(p.getUniqueId()) && !p.equals(target)) {
			
			p.sendMessage("§c▒§r 주인이 다른 유저의 접근을 제한한 섬입니다.");
			return;
		}
		
		if (p.equals(target))
			p.sendMessage("§a▒§r 당신의 섬으로 이동합니다..");
		else {
			p.sendMessage("§a▒§r "+target.getName()+"님의 섬으로 이동합니다..");
			
			if (region.getFlag(DefaultFlag.PVP) == State.ALLOW)
				p.sendMessage("§a▒§r §c주의!§r PvP가 허용된 섬입니다.");
			if (!p.getActivePotionEffects().isEmpty() && region.getFlag(EfeFlag.NO_POTION) == State.ALLOW)
				p.sendMessage("§a▒§r §c주의!§r 소지중인 포션 효과가 제거됩니다.");
		}
		
		
		final Location loc = p.getLocation();
		
		p.setMetadata("teleporting", new FixedMetadataValue(plugin, "Teleporting"));
		
		p.playSound(loc, Sound.PORTAL_TRIGGER, 1.0F, 1.5F);
		
		ParticleEffect.PORTAL.display(1.0F, 1.0F, 1.0F, 1.0F, 500, p.getLocation().add(0, 1.5, 0), 32);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.PORTAL;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation(loc.clone().add(0.0D, 0.3D, 0.0D)));
				cylinder.start();
			}
		}, 3L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.PORTAL;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation(loc.clone().add(0.0D, 0.6D, 0.0D)));
				cylinder.start();
			}
		}, 6L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.PORTAL;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation(loc.clone().add(0.0D, 0.9D, 0.0D)));
				cylinder.start();
			}
		}, 9L);
		
		Location toLoc = plugin.getIsleSpawnLoc(target).clone().add(0, 2.5, 0);
		toLoc.getWorld().refreshChunk(toLoc.getBlockX(), toLoc.getBlockZ());
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (p != null)
					p.removeMetadata("teleporting", plugin);
				
				if (p == null || p.isDead()) return;
				if (p.getLocation().distance(loc) >= 1.0D) {
					ActionBarAPI.sendActionBar(p, "§c텔레포트가 취소되었습니다.");
					return;
				}
				
				if (!p.getWorld().equals(plugin.world))
					data.setSaveLocation(p.getLocation());
				
				if (region.getFlag(EfeFlag.BLACKLIST).contains(p.getUniqueId().toString())) {
					p.sendMessage("§c▒§r 주인이 당신의 접근을 차단한 섬입니다.");
					return;
				}
				
				if (region.getFlag(EfeFlag.ENTRANCE) == State.DENY &&
						!new UserData(target.getUniqueId()).getFriends().contains(p.getUniqueId()) && !p.equals(target)) {
					
					p.sendMessage("§a▒§r 주인이 다른 유저의 접근을 제한한 섬입니다.");
					return;
				}
				
				p.teleport(toLoc);
				p.setFallDistance(0.0F);
				
				ParticleEffect.SPELL_WITCH.display(0.2F, 0.2F, 0.2F, 1.0F, 20, p.getLocation().add(0, 0.5, 0), 32);
			}
		}, 50L);
	}
	
	public static void teleportReturn(final Player p) {
		if (p.hasMetadata("teleporting")) {
			p.sendMessage("§c▒§r 이미 텔레포트 중입니다.");
			return;
		}
		
		p.sendMessage("§a▒§r 원래 있던 곳으로 귀환합니다..");
		
		
		final Location loc = p.getLocation();
		
		p.setMetadata("teleporting", new FixedMetadataValue(plugin, "Teleporting"));
		
		p.playSound(loc, Sound.PORTAL_TRIGGER, 1.0F, 1.5F);
		
		ParticleEffect.PORTAL.display(1.0F, 1.0F, 1.0F, 1.0F, 500, p.getLocation().add(0, 1.5, 0), 32);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.PORTAL;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation(loc.clone().add(0.0D, 0.3D, 0.0D)));
				cylinder.start();
			}
		}, 3L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.PORTAL;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation(loc.clone().add(0.0D, 0.6D, 0.0D)));
				cylinder.start();
			}
		}, 6L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				EfeCylinderEffect cylinder = new EfeCylinderEffect(plugin.em);
				
				cylinder.particle = ParticleEffect.PORTAL;
				cylinder.height = 0.3F;
				cylinder.iterations = 1;
				
				cylinder.setDynamicOrigin(new DynamicLocation(loc.clone().add(0.0D, 0.9D, 0.0D)));
				cylinder.start();
			}
		}, 9L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (p != null)
					p.removeMetadata("teleporting", plugin);
				
				if (p == null || p.isDead()) return;
				if (p.getLocation().distance(loc) >= 1.0D) {
					ActionBarAPI.sendActionBar(p, "§c텔레포트가 취소되었습니다.");
					return;
				}
				
				PlayerData data = PlayerData.get(p);
				
				if (p.getAllowFlight() && Util.getFlagValue(plugin.wgp, data.getSaveLocation(), EfeFlag.FLY) != State.ALLOW && p.getGameMode() != GameMode.CREATIVE) {
					p.setAllowFlight(false);
				}
				
				p.teleport(data.getSaveLocation().clone().add(0, 1, 0));
				p.setFallDistance(0.0F);
				p.sendMessage("§a▒§r 귀환이 완료되었습니다.");
				
				ParticleEffect.SPELL_WITCH.display(0.2F, 0.2F, 0.2F, 1.0F, 20, p.getLocation().add(0, 0.5, 0), 32);
			}
		}, 50L);
	}
}