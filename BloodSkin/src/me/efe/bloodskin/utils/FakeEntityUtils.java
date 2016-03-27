package me.efe.bloodskin.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import me.efe.bloodskin.utils.WrapperPlayServerSpawnEntity.ObjectTypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

public class FakeEntityUtils {
	
	public static int generateId() {
		return 500 + new Random().nextInt(1000);
	}
	
	public static void spawnItem(Location loc, ItemStack item, int id) {
		try {
			WrapperPlayServerSpawnEntity spawn = new WrapperPlayServerSpawnEntity();
			
			spawn.setEntityID(id);
			spawn.setType(ObjectTypes.ITEM_STACK);
			
			spawn.setX(loc.getX());
			spawn.setY(loc.getY());
			spawn.setZ(loc.getZ());
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				ProtocolLibrary.getProtocolManager().sendServerPacket(p, spawn.getHandle());
			}
			
			
			WrappedDataWatcher watcher = new WrappedDataWatcher();
			
			watcher.setObject(10, item.clone());
			
			
			WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata();
			
			metadata.setEntityId(id);
			metadata.setEntityMetadata(watcher.getWatchableObjects());
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				ProtocolLibrary.getProtocolManager().sendServerPacket(p, metadata.getHandle());
			}
			
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void setVelocity(int id, Vector vector) {
		try {
			WrapperPlayServerEntityVelocity velocity = new WrapperPlayServerEntityVelocity();
			
			velocity.setEntityId(id);
			
			velocity.setVelocityX(vector.getX());
			velocity.setVelocityY(vector.getY());
			velocity.setVelocityZ(vector.getZ());
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				ProtocolLibrary.getProtocolManager().sendServerPacket(p, velocity.getHandle());
			}
			
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeEntity(int id) {
		try {
			WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
			
			destroy.setEntities(new int[]{id});
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				ProtocolLibrary.getProtocolManager().sendServerPacket(p, destroy.getHandle());
			}
			
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}