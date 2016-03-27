package me.efe.efemobs;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.elseland.xikage.MythicMobs.Mobs.MythicMob;
import net.elseland.xikage.MythicMobs.Mobs.Entities.MythicEnderman;
import net.minecraft.server.v1_8_R3.World;

public class MythicFixedEnderman extends MythicEnderman {
	
	@Override
	public Entity spawn(MythicMob mm, Location loc) {
		World world = ((CraftWorld) loc.getWorld()).getHandle();
		FixedEnderman enderman = new FixedEnderman(world);
		
		enderman.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0F, 0.0F);
		world.addEntity(enderman, SpawnReason.CUSTOM);
		
		return enderman.getBukkitEntity();
	}
	
	@Override
	public Entity spawn(Location loc) {
		World world = ((CraftWorld) loc.getWorld()).getHandle();
		FixedEnderman enderman = new FixedEnderman(world);
		
		enderman.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0F, 0.0F);
		world.addEntity(enderman, SpawnReason.CUSTOM);
		
		return enderman.getBukkitEntity();
	}
}