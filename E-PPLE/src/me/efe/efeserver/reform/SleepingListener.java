package me.efe.efeserver.reform;

import java.util.HashMap;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.Bed;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;

import me.efe.efeserver.EfeServer;

public class SleepingListener implements Listener {
	public EfeServer plugin;
	public HashMap<Player, Block> sleeping = new HashMap<Player, Block>();
	
	public SleepingListener(EfeServer plugin) {
		this.plugin = plugin;
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.ENTITY_ACTION) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
				if (e.getPacket().getPlayerActions().read(0).equals(PlayerAction.STOP_SLEEPING) && sleeping.containsKey(e.getPlayer())) {
					e.setCancelled(true);
					
					PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftPlayer) e.getPlayer()).getHandle(), 2);
					
					for (Player p : e.getPlayer().getWorld().getPlayers()) {
						((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
					}
					
					sleeping.remove(e.getPlayer());
				}
			}
		});
	}
	
	@EventHandler
	public void enter(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.BED_BLOCK) {
			e.setCancelled(true);
			
			Bed bed = (Bed) e.getClickedBlock().getState().getData();
			Location loc = e.getClickedBlock().getLocation();
			
			if (!bed.isHeadOfBed()) {
				loc = loc.getBlock().getRelative(bed.getFacing()).getLocation();
			}
			
			if (sleeping.containsValue(loc.getBlock())) {
				return;
			}
			
			e.getPlayer().teleport(loc.clone().add(0.5, 0.5, 0.5));
			
			int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
			PacketPlayOutBed packet = new PacketPlayOutBed(((CraftPlayer) e.getPlayer()).getHandle(), new BlockPosition(x, y, z));
			
			for (Player p : e.getPlayer().getWorld().getPlayers()) {
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			}
			
			sleeping.put(e.getPlayer(), loc.getBlock());
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if (sleeping.containsKey(e.getPlayer())) {
			sleeping.remove(e.getPlayer());
		}
	}
}