package me.efe.efefurnace;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.TileEntityFurnace;

public class FurnacePlayer {
	private final EfeFurnace plugin;
	private final Player player;
	private final EntityPlayer handle;
	private final BlastFurnace furnace;
	
	public FurnacePlayer(EfeFurnace plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
		this.handle = ((CraftPlayer) player).getHandle();
		this.furnace = new BlastFurnace(this.handle);
	}

	public BlastFurnace getFurnace() {
		return this.furnace;
	}
	
	public void openFurnace() {
		this.handle.openTileEntity(this.furnace);
	}
	
	public void loadData() {
		String id = this.player.getUniqueId().toString();
		
		if (this.plugin.getConfig().contains(id))
			return;
		
		this.furnace.burnTime = this.plugin.getConfig().getInt(id + ".burnTime");
		
		for (int i = 0; i < 3; i ++) {
			if (this.plugin.getConfig().contains(id + ".items." + i)) {
				ItemStack itemStack = CraftItemStack.asNMSCopy(this.plugin.getConfig().getItemStack(id + ".items." + id));
				
				this.furnace.setItem(i, itemStack);
			}
		}
	}
	
	public void saveData() {
		String id = this.player.getUniqueId().toString();
		
		for (int i = 0; i < this.furnace.getContents().length; i ++) {
			ItemStack itemStack = this.furnace.getContents()[i];
			
			if (itemStack == null)
				continue;
			
			this.plugin.getConfig().set(id + ".items." + i, CraftItemStack.asBukkitCopy(itemStack));
		}
		
		this.plugin.getConfig().set(id + ".burnTime", this.furnace.burnTime);
	}
	
	public class BlastFurnace extends TileEntityFurnace {
		
		public BlastFurnace(EntityHuman entity) {
			this.world = entity.world;
		}
		
		@Override
		public boolean a(EntityHuman entity) {
			return true;
		}
		
		@Override
		public int u() {
			return 0;
		}
		
		@Override
		public void update() {
			
		}
		
		@Override
		public Block w() {
			return Blocks.FURNACE;
		}
		
		@Override
		public String getName() {
			return "¿ë±¤·Î";
		}
		
		@Override
		public InventoryHolder getOwner() {
			Furnace furnace = new CraftFurnace(this.world.getWorld().getBlockAt(0, 0, 0));
			
			try {
				Field field = CraftFurnace.class.getDeclaredField("furnace");
				field.setAccessible(true);
				
				Field mField = Field.class.getDeclaredField("modifiers");
				mField.setAccessible(true);
				mField.set(field, field.getModifiers() & ~Modifier.FINAL);
				
				field.set(furnace, this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			return furnace;
		}
	}
}
