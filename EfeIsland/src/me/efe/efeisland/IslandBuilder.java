package me.efe.efeisland;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;

import me.efe.efeserver.PlayerData;
import me.efe.efetutorial.TutorialState;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.registry.WorldData;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class IslandBuilder {
	public EfeIsland plugin;
	
	public IslandBuilder(EfeIsland plugin) {
		this.plugin = plugin;
	}
	
	public void build(Player p, String name) {
		File file = new File(plugin.getDataFolder(), name+".schematic");
		Location loc = nextIslandLocation();
		PlayerData data = PlayerData.get(p);
		
		int y = (name.equals("type_a")) ? 2 : (name.equals("type_b")) ? 4 : 1;
		Biome biome = (name.equals("type_a")) ? Biome.ICE_PLAINS : (name.equals("type_b")) ? Biome.JUNGLE : Biome.PLAINS;
		
		for (int i = 0; i < 5; i ++) {
			loc.getWorld().loadChunk(loc.getBlockX() + 32 - 16 * i, loc.getBlockZ() + 32 - 16 * i, true);
		}
		
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				pasteSchematic(file, loc.add(0, y, 0));
			}
		});
		
		if (name.equals("type_b"))
			loc.add(0, 3, 0);
		
		plugin.setIsleLoc(p, loc);
		plugin.setLastIsleLoc(loc);
		
		BlockVector pos1 = BukkitUtil.toVector(plugin.world.getBlockAt(loc.getBlockX() + 30, 255, loc.getBlockZ()  + 30));
		BlockVector pos2 = BukkitUtil.toVector(plugin.world.getBlockAt(loc.getBlockX() - 30, 1, loc.getBlockZ() - 30));
		ProtectedRegion region = new ProtectedCuboidRegion(data.getIslName(), pos1, pos2);
		
		region.setFlag(EfeFlag.ENTRANCE, State.ALLOW);
		region.setFlag(DefaultFlag.SPAWN_LOC, BukkitUtil.toLocation(loc));
		region.setFlag(DefaultFlag.BUILD, State.DENY);
		region.setFlag(DefaultFlag.BUILD.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
		region.setFlag(DefaultFlag.USE, State.DENY);
		region.setFlag(DefaultFlag.USE.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
		region.setFlag(DefaultFlag.CHEST_ACCESS, State.DENY);
		region.setFlag(DefaultFlag.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
		region.setFlag(DefaultFlag.SNOW_MELT, State.ALLOW);
		region.setFlag(DefaultFlag.ICE_FORM, State.ALLOW);
		region.setFlag(DefaultFlag.ICE_MELT, State.ALLOW);
		region.setFlag(EfeFlag.FLY, State.DENY);
		region.setFlag(DefaultFlag.FIRE_SPREAD, State.DENY);
		region.setFlag(DefaultFlag.LAVA_FIRE, State.DENY);
		region.setFlag(DefaultFlag.MUSHROOMS, State.ALLOW);
		region.setFlag(DefaultFlag.GRASS_SPREAD, State.ALLOW);
		region.setFlag(DefaultFlag.MYCELIUM_SPREAD, State.ALLOW);
		region.setFlag(DefaultFlag.VINE_GROWTH, State.ALLOW);
		region.setFlag(DefaultFlag.WATER_FLOW, State.ALLOW);
		region.setFlag(DefaultFlag.LAVA_FLOW, State.ALLOW);
		region.setFlag(DefaultFlag.PVP, State.DENY);
		region.setFlag(DefaultFlag.PVP.getRegionGroupFlag(), RegionGroup.ALL);
		region.setFlag(EfeFlag.INSTANT_KILL, State.DENY);
		region.setFlag(EfeFlag.SKILL, State.DENY);
		region.setFlag(EfeFlag.NO_POTION, State.DENY);
		region.setFlag(DefaultFlag.POTION_SPLASH, State.ALLOW);
		region.setFlag(DefaultFlag.POTION_SPLASH.getRegionGroupFlag(), RegionGroup.ALL);
		region.setFlag(DefaultFlag.MOB_SPAWNING, State.DENY);
		region.setFlag(DefaultFlag.INTERACT, State.ALLOW);
		region.setFlag(DefaultFlag.DAMAGE_ANIMALS, State.DENY);
		region.setFlag(DefaultFlag.DAMAGE_ANIMALS.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
		region.setFlag(DefaultFlag.MOB_SPAWNING, State.ALLOW);
		
		region.setFlag(EfeFlag.TITLE, p.getName()+"ÀÇ ¼¶");
		region.setFlag(EfeFlag.DESCRIPTION, p.getName()+"ÀÇ ¼¶¿¡ ¾î¼­¿À¼¼¿ä!");
		region.setFlag(EfeFlag.LEVEL, 1);
		region.setFlag(EfeFlag.MAX_VISIT, 0); 
		
		region.setFlag(EfeFlag.VISITERS, new HashSet<String>());
		region.setFlag(EfeFlag.BLACKLIST, new HashSet<String>());
		
		region.getOwners().addPlayer(WGBukkit.getPlugin().wrapPlayer(p));
		WGBukkit.getRegionManager(plugin.world).addRegion(region);
		
		for (int x = -30; x <= 30; x ++) {
			for (int z = -30; z <= 30; z ++) {
				loc.getWorld().setBiome(loc.getBlockX() + x, loc.getBlockZ() + z, biome);
			}
		}
		
		data.setIsland(true);
		
		TutorialState.set(p, TutorialState.ISLAND_CREATED);
	}
	
	public void upgrade(Player p) {
		ProtectedRegion old = plugin.getIsleRegion(p);
		Location loc = plugin.getIsleLoc(p);
		int lv = old.getFlag(EfeFlag.LEVEL);
		
		int distance = 0;
		
		switch (lv) {
		case 1:
			distance = 45;
			break;
		case 2:
			distance = 60;
			break;
		case 3:
			distance = 75;
			break;
		case 4:
			distance = 100;
			break;
		}
		
		BlockVector pos1 = BukkitUtil.toVector(plugin.world.getBlockAt(loc.getBlockX() + distance, 256, loc.getBlockZ()  + distance));
		BlockVector pos2 = BukkitUtil.toVector(plugin.world.getBlockAt(loc.getBlockX() - distance, 1, loc.getBlockZ() - distance));
		ProtectedRegion region = new ProtectedCuboidRegion(old.getId(), pos1, pos2);
		
		region.copyFrom(old);
		region.setFlag(EfeFlag.LEVEL, lv + 1);
		
		RegionManager manager = WGBukkit.getRegionManager(plugin.world);
		
		manager.removeRegion(region.getId(), RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
		manager.addRegion(region);
	}
	
	public Location nextIslandLocation() {
		Location lastIsland = plugin.getLastIsleLoc();
		
		int x = (int) lastIsland.getX();
		int z = (int) lastIsland.getZ();
		Location nextPos = lastIsland.clone();
		
		if (x < z) {
			if (-1 * x < z) {
				nextPos.setX(nextPos.getX() + 300);
				return nextPos;
			}
			nextPos.setZ(nextPos.getZ() + 300);
			return nextPos;
		}
		
		if (x > z) {
			if (-1 * x >= z) {
				nextPos.setX(nextPos.getX() - 300);
				return nextPos;
			}
			nextPos.setZ(nextPos.getZ() - 300);
			return nextPos;
		}
		
		if (x <= 0) {
			nextPos.setZ(nextPos.getZ() + 300);
			return nextPos;
		}
		
		nextPos.setZ(nextPos.getZ() - 300);
		return nextPos.clone();
	}
	
	public void pasteSchematic(File file, Location loc) {
		ClipboardHolder holder = null;
		BukkitWorld world = new BukkitWorld(loc.getWorld());
		
		Closer closer = Closer.create();
		
		try {
			FileInputStream fis = closer.register(new FileInputStream(file));
			BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
			ClipboardReader reader = ClipboardFormat.findByFile(file).getReader(bis);
			
			WorldData worldData = world.getWorldData();
			Clipboard clipboard = reader.read(world.getWorldData());
			holder = new ClipboardHolder(clipboard, worldData);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
		} finally {
			
			try {
				closer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
		
		if (holder == null) return;
		
		
		try {
			@SuppressWarnings("deprecation")
			EditSession editSession = plugin.wep.getWorldEdit().getEditSessionFactory().getEditSession(world, 1000000);
			Vector to = new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			
			Operation operation = holder
					.createPaste(editSession, world.getWorldData())
					.to(to)
					.ignoreAirBlocks(false)
					.build();
			Operations.completeLegacy(operation);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		plugin.getLogger().info("An island was generated at "+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+".");
	}
	
	public void setSpawnPoint(ProtectedRegion region, Location loc) {
		region.setFlag(DefaultFlag.SPAWN_LOC, BukkitUtil.toLocation(loc));
	}
}