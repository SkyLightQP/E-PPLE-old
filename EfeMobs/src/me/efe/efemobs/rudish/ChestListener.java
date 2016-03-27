package me.efe.efemobs.rudish;

import java.util.Collections;
import java.util.List;

import me.efe.efemobs.EfeMobs;
import me.efe.efemobs.Expedition;
import me.efe.efemobs.ScrollUtils;
import me.efe.efemobs.rudish.mobs.boss.RudishBoss;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;

import de.slikey.effectlib.util.ParticleEffect;

public class ChestListener implements Listener {
	public static EfeMobs plugin;
	
	public ChestListener(EfeMobs pl) {
		plugin = pl;
		
		registerListener();
	}
	
	public void registerListener() {
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.BLOCK_PLACE) {
			
			@Override
			public void onPacketReceiving(final PacketEvent e) {
				BlockPosition pos = e.getPacket().getBlockPositionModifier().read(0);
				
				if (e.getPlayer().hasMetadata("boss_chest") && e.getPlayer().hasMetadata("boss_hologram")) {
					Location chest = (Location) e.getPlayer().getMetadata("boss_chest").get(0).value();
					
					if (pos.getX() == chest.getBlockX() && pos.getZ() == chest.getBlockZ()) {
						e.setCancelled(true);
						
						final Hologram hologram = (Hologram) e.getPlayer().getMetadata("boss_hologram").get(0).value();
						final double oldHeight = hologram.getHeight();
						final Location hologramLoc = hologram.getLocation().clone();
						
						if (hologram.getLine(0) instanceof ItemLine || (hologram.size() > 1 && hologram.getLine(1) instanceof ItemLine)) return;
						
						Expedition exp = ChestListener.plugin.bossListener.getExpedition(e.getPlayer());
						RudishBoss boss = (RudishBoss) ChestListener.plugin.floorListener.mobs.get("Boss_"+exp.getFloor()+"F");
						
						
						for (Player all : exp.getMembers()) {
							sendActionPacket(all, chest);
							
							all.playSound(chest, Sound.CHEST_OPEN, 1.0F, 1.0F);
						}
						
						ParticleEffect.FIREWORKS_SPARK.display(0.3F, 0.3F, 0.3F, 0.1F, 50, hologram.getLocation(), 32);
						
						
						hologram.clearLines();
						
						List<ItemStack> list = boss.getRewards(exp.getFloor());
						Collections.reverse(list);
						
						for (int i = 0; i < list.size(); i ++) {
							final ItemStack item = list.get(i);
							
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								
								@Override
								public void run() {
									hologram.appendItemLine(item.clone());
									
									hologram.teleport(hologramLoc.clone().add(0.0D, hologram.getHeight() - oldHeight, 0.0D));
								}
							}, (i + 1) * 5L);
							
							ChestListener.plugin.util.giveItem(e.getPlayer(), item.clone());
						}
						
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							
							@Override
							public void run() {
								hologram.appendTextLine("」a」l"+e.getPlayer().getName()+"」a's Chest");
								
								hologram.teleport(hologramLoc.clone().add(0.0D, hologram.getHeight() - oldHeight, 0.0D));
							}
						}, (list.size() + 1) * 5L + 30L);
					}
				}
			}
		});
	}
	
	private void sendActionPacket(Player p, Location loc) {
		net.minecraft.server.v1_8_R3.BlockPosition pos = new net.minecraft.server.v1_8_R3.BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		
		PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, 1);
		
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
	@SuppressWarnings("deprecation")
	public void createChest(Expedition exp) {
		Location center = ScrollUtils.getBossSpawn(exp.getChannel()).clone().add(0, -1, -2);
		
		for (int i = 0; i < exp.getMembers().size(); i ++) {
			Player p = exp.getMembers().get(i);
			Location loc = getChestLocation(center, i);
			
			p.setMetadata("boss_chest", new FixedMetadataValue(plugin, loc));
			
			for (Player all : plugin.util.getOnlinePlayers()) {
				all.sendBlockChange(loc, Material.CHEST, (byte) 0);
				all.sendBlockChange(loc.clone().add(0, 1, 0), Material.BARRIER, (byte) 0);
			}
			
			ParticleEffect.EXPLOSION_NORMAL.display(0.0F, 0.0F, 0.0F, 0.1F, 30, loc.clone().add(0.5F, 1.0F, 0.5F), 32);
			
			
			Hologram hologram = HologramsAPI.createHologram(plugin, loc.clone().add(0.5F, 1.5F, 0.5F));
			
			hologram.appendTextLine("」a」l"+p.getName()+"」a's Chest");
			
			p.setMetadata("boss_hologram", new FixedMetadataValue(plugin, hologram));
		}
	}
	
	private Location getChestLocation(Location center, int index) {
		switch (index) {
		case 0:
			return center.clone();
		case 1:
			return center.clone().add(2, 0, 0);
		case 2:
			return center.clone().add(-2, 0, 0);
		case 3:
			return center.clone().add(4, 0, 0);
		case 4:
			return center.clone().add(-4, 0, 0);
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public void removeChest(Player p) {
		if (p.hasMetadata("boss_chest")) {
			Location chest = (Location) p.getMetadata("boss_chest").get(0).value();
			
			for (Player all : plugin.util.getOnlinePlayers()) {
				all.sendBlockChange(chest, Material.AIR, (byte) 0);
				all.sendBlockChange(chest.clone().add(0, 1, 0), Material.AIR, (byte) 0);
			}
			
			p.removeMetadata("boss_chest", plugin);
		}
		
		if (p.hasMetadata("boss_hologram")) {
			Hologram hologram = (Hologram) p.getMetadata("boss_hologram").get(0).value();
			
			hologram.delete();
			
			p.removeMetadata("boss_hologram", plugin);
		}
	}
}