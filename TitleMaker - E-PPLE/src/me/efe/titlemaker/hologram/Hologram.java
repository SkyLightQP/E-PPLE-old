package me.efe.titlemaker.hologram;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import me.efe.titlemaker.hologram.packetwrappers.WrapperPlayServerEntityDestroy;
import me.efe.titlemaker.hologram.packetwrappers.WrapperPlayServerEntityTeleport;
import me.efe.titlemaker.hologram.packetwrappers.WrapperPlayServerSpawnEntityLiving;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

public class Hologram {
	private String title;
	private int id;
	private Player owner;
	private World world;
	private List<Player> packetReceiver = new ArrayList<Player>();
	
	public Hologram(String title, int id, Player owner) {
		this.title = title;
		this.id = id;
		this.owner = owner;
		this.world = owner.getWorld();
	}
	
	public Player getOwner() {
		return this.owner;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public void addReceiver(Player p) {
		if (this.packetReceiver.contains(p) || owner.equals(p))
			return;
		
		this.packetReceiver.add(p);
		
		show(p);
	}
	
	public void removeReceiver(Player p) {
		if (!this.packetReceiver.contains(p))
			return;
		
		this.packetReceiver.remove(p);
		
		hide(p);
	}
	
	public void show(Player p) {
		Location loc = owner.getLocation().add(0, getHeight(p), 0);
		
		WrapperPlayServerSpawnEntityLiving entity = new WrapperPlayServerSpawnEntityLiving();
		
		entity.setEntityID(id);
		entity.setType(30);
		
		entity.setX(loc.getX());
		entity.setY(loc.getY() - 55.799999999999997D);
		entity.setZ(loc.getZ());
		
		
		WrappedDataWatcher data = new WrappedDataWatcher();
		
		data.setObject(0, (byte) 32);
		data.setObject(2, this.title);
		data.setObject(3, (byte) 1);
		data.setObject(10, (byte) 15);
		
		entity.setMetadata(data);
		
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(p, entity.getHandle());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void hide(Player p) {
		WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
		
		destroy.setEntities(new int[]{id});
		
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(p, destroy.getHandle());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		for (Player p : packetReceiver) {
			Location loc = owner.getLocation().add(0, getHeight(p), 0);
			
			WrapperPlayServerEntityTeleport teleport = new WrapperPlayServerEntityTeleport();
			
			teleport.setEntityID(id);
			
			teleport.setX(loc.getX());
			teleport.setY(loc.getY() - 55.799999999999997D);
			teleport.setZ(loc.getZ());
			
			try {
				ProtocolLibrary.getProtocolManager().sendServerPacket(p, teleport.getHandle());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	private double getHeight(Player p) {
		double height = 56.9D;
		
		if (p.getScoreboard() != null && p.getScoreboard().getObjective(DisplaySlot.BELOW_NAME) != null) {
			height += 0.23D;
		}
		
		return height;
	}
}