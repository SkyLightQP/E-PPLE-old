package me.efe.efeserver.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.google.gson.JsonParser;


public class SignGUI {
	protected ProtocolManager protocolManager;
	protected PacketAdapter packetListener;
	protected Map<String, SignGUIListener> listeners;
	protected Map<String, Vector> signLocations;
	
	
	
	public SignGUI(Plugin plugin) {
		protocolManager = ProtocolLibrary.getProtocolManager();		
		listeners = new ConcurrentHashMap<String, SignGUIListener>();
		signLocations = new ConcurrentHashMap<String, Vector>();
		

		ProtocolLibrary.getProtocolManager().addPacketListener(
		packetListener =  new PacketAdapter(plugin, PacketType.Play.Client.UPDATE_SIGN) 
		{
			@Override
			public void onPacketReceiving(PacketEvent event) {
				final Player player = event.getPlayer();
				
				Vector v = signLocations.remove(player.getName());
				if (v == null) return;		
				BlockPosition pos = event.getPacket().getBlockPositionModifier().read(0);
				if (pos.getX() != v.getBlockX()) return;
				if (pos.getY() != v.getBlockY()) return;
				if (pos.getZ() != v.getBlockZ()) return;
				
				JsonParser parser = new JsonParser();
				
				final String[] lines = new String[]{
						parser.parse(event.getPacket().getChatComponentArrays().read(0)[0].getJson()).getAsString(), 
						parser.parse(event.getPacket().getChatComponentArrays().read(0)[1].getJson()).getAsString(), 
						parser.parse(event.getPacket().getChatComponentArrays().read(0)[2].getJson()).getAsString(), 
						parser.parse(event.getPacket().getChatComponentArrays().read(0)[3].getJson()).getAsString()};
				final SignGUIListener response = listeners.remove(event.getPlayer().getName());
				if (response != null) {
					event.setCancelled(true);
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							response.onSignDone(player, lines);
						}
					});
				}
			}
		});
	}
	
	
	public void open(Player player, /* String[] defaultText, */ SignGUIListener response) {
		List<PacketContainer> packets = new ArrayList<PacketContainer>();
		
		int x = 0, y = 0, z = 0;
//		if (defaultText != null) {
//			x = player.getLocation().getBlockX();
//			z = player.getLocation().getBlockZ();
//			
//			player.sendBlockChange(player.getLocation(), Material.SIGN_POST, (byte) 0);
//			
//			WrappedChatComponent[] chats = {
//					WrappedChatComponent.fromText(defaultText[0]), 
//					WrappedChatComponent.fromText(defaultText[1]), 
//					WrappedChatComponent.fromText(defaultText[2]), 
//					WrappedChatComponent.fromText(defaultText[3])
//			};
//			
//			PacketContainer packet130 = protocolManager.createPacket(PacketType.Play.Server.UPDATE_SIGN);
//			packet130.getBlockPositionModifier().write(0, new BlockPosition(x, y, z));
//			packet130.getChatComponentArrays().write(0, chats);
//			packets.add(packet130);
//		}
		
		PacketContainer packet133 = protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_ENTITY);
		packet133.getBlockPositionModifier().write(0, new BlockPosition(x, y, z));
		packets.add(packet133);
		
//		if (defaultText != null) {
//			player.sendBlockChange(new Location(player.getLocation().getWorld(), x, y, z), Material.BEDROCK, (byte) 0);
//		}
		
		try {
			for (PacketContainer packet : packets) {
				protocolManager.sendServerPacket(player, packet);
			}
			signLocations.put(player.getName(), new Vector(x, y, z));
			listeners.put(player.getName(), response);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}	   
		
	}
	
	
	

	
	public void destroy() {
		protocolManager.removePacketListener(packetListener);
		listeners.clear();
		signLocations.clear();
	}
	
	public interface SignGUIListener {
		public void onSignDone(Player player, String[] lines);
	   
	}
}