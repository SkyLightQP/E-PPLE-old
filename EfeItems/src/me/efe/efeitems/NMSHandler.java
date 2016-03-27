package me.efe.efeitems;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.efe.efeisland.EfeIsland;
import me.efe.efeserver.EfeServer;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunkBulk;
import net.minecraft.server.v1_8_R3.TileEntityMobSpawner;
import net.minecraft.server.v1_8_R3.TileEntitySkull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSkull;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/*
 * 0:"END", 1:"BYTE", 2:"SHORT", 3:"INT", 4:"LONG", 5:"FLOAT", 6:"DOUBLE", 7:"BYTE[]", 8:"STRING", 9:"LIST", 10:"COMPOUND", 11:"INT[]"
 */
public class NMSHandler {
	private static Field tileField;
	
	static {
		try {
			tileField = CraftCreatureSpawner.class.getDeclaredField("spawner");
			tileField.setAccessible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static ItemStack createSkull(String id, String value) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
		
		NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new NBTTagCompound();
		
		NBTTagCompound skullOwner = new NBTTagCompound();
		skullOwner.setString("Id", id);
		
		NBTTagCompound properties = new NBTTagCompound();
		NBTTagList textures = new NBTTagList();
		
		NBTTagCompound texture = new NBTTagCompound();
		texture.setString("Value", value);
		
		textures.add(texture);
		properties.set("textures", textures);
		
		skullOwner.set("Properties", properties);
		
		tag.set("SkullOwner", skullOwner);
		stack.setTag(tag);
		
		return CraftItemStack.asBukkitCopy(stack);
	}
	
	public static String getSkullValue(Block block) {
		try {
			CraftSkull craftSkull = (CraftSkull) block.getState();
			TileEntitySkull skull = craftSkull.getTileEntity();
			
			NBTTagCompound compound = new NBTTagCompound();
			skull.b(compound);
			
			String value = compound.getCompound("Owner")
					.getCompound("Properties")
					.getList("textures", 10)
					.get(0)
					.getString("Value");
			
			return value;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static void updateChunks(Location loc, Biome biome) {
		List<Chunk> list = new ArrayList<Chunk>();
		
		for (int x = -3; x <= 3; x ++) {
			for (int z = -3; z <= 3; z ++) {
				Block block = loc.getWorld().getBlockAt(loc.clone().add(x, 0, z));
				
				if (biome != null)
					block.setBiome(biome);
				
				Chunk chunk = ((CraftChunk) loc.getWorld().getChunkAt(block)).getHandle();
				
				if (!list.contains(chunk)) {
					list.add(chunk);
				}
			}
		}
		
		EfeIsland efeIsland = EfeServer.getInstance().efeIsland;
		ProtectedRegion region = efeIsland.getIsleRegion(loc);
		
		for (Player visiter : efeIsland.getVisiters(region)) {
			((CraftPlayer) visiter).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunkBulk(list));
		}
	}
	
	public static void setHealingSpawner(BlockState state) {
		try {
			CraftCreatureSpawner craftSpawner = (CraftCreatureSpawner) state;
			TileEntityMobSpawner spawner = craftSpawner.getTileEntity();
			
			NBTTagCompound compound = new NBTTagCompound();
			
			spawner.b(compound);
			
			compound.setString("EntityId", "ThrownPotion");
			compound.setShort("Delay", (short) 10);
			compound.setShort("MinSpawnDelay", (short) 10);
			compound.setShort("MaxSpawnDelay", (short) 10);
			compound.setShort("SpawnCount", (short) 1);
			compound.setShort("MaxNearbyEntities", (short) 3);
			compound.setShort("RequiredPlayerRange", (short) 3);
			compound.setShort("SpawnRange", (short) 0);
			
			NBTTagCompound spawnData = new NBTTagCompound();
			NBTTagCompound potion = new NBTTagCompound();
			potion.setInt("Damage", 8197);
			potion.setString("id", "potion");
			spawnData.set("Potion", potion);
			
			compound.set("SpawnData", spawnData);
			
			spawner.a(compound);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static boolean isHealingSpawner(Block block) {
		try {
			CraftCreatureSpawner craftSpawner = (CraftCreatureSpawner) block.getState();
			TileEntityMobSpawner spawner = craftSpawner.getTileEntity();
			
			NBTTagCompound compound = new NBTTagCompound();
			spawner.b(compound);
			
			return compound.getShort("MinSpawnDelay") == 10 && compound.getShort("MaxSpawnDelay") == 10 && compound.getShort("SpawnCount") == 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
}