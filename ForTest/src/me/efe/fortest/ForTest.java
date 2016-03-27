package me.efe.fortest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunkBulk;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.ParticleEffect;

public class ForTest extends JavaPlugin implements Listener {
	//public EfeUtil util;
	public String main = "¡×b[ForTest]¡×r ";
	
	public EffectManager em;
	
	@Override
	public void onDisable() {
		//util.logDisable();
	}
	
	@Override
	public void onEnable() {
		//util = new EfeUtil(this);
		//util.logEnable();
		
		getServer().getPluginManager().registerEvents(this, this);
		
		em = new EffectManager(EffectLib.instance());
	}
	
	public Block getTargetBlock(Player p, int range) {
        BlockIterator it = new BlockIterator(p, range);
        Block lastBlock = it.next();
        
        while (it.hasNext()) {
            lastBlock = it.next();
            
            if (lastBlock.getType() == Material.AIR)
            	continue;
            break;
        }
        
        return lastBlock;
    }
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("ft")) {
			Player p = (Player) s;
			
			if (a.length == 0) {
				p.sendMessage("/ft <Effect> <x> <y> <z> <speed> <amount>");
				p.sendMessage("/ft icon <id> <data> <x> <y> <z> <speed> <amount>");
				p.sendMessage("/ft block <id> <data> <x> <y> <z> <speed> <amount>");
				p.sendMessage("/ft color <id> <red> <green> <blue> <data> <x> <y> <z> <speed> <amount>");
				return true;
			}
			
			Location center = getTargetBlock(p, 5).getLocation().add(0, 1, 0);
			
			if (a[0].equals("icon")) {
				Material type = Material.getMaterial(a[1]);
				byte data = Byte.parseByte(a[2]);
				float offsetX = Float.parseFloat(a[3]);
				float offsetY = Float.parseFloat(a[4]);
				float offsetZ = Float.parseFloat(a[5]);
				float speed = Float.parseFloat(a[6]);
				int amount = Integer.parseInt(a[7]);
				
				ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(type, data), offsetX, offsetY, offsetZ, speed, amount, center, 32);
				
				return true;
			}
			
			if (a[0].equals("block")) {
				Material type = Material.getMaterial(a[1]);
				byte data = Byte.parseByte(a[2]);
				float offsetX = Float.parseFloat(a[3]);
				float offsetY = Float.parseFloat(a[4]);
				float offsetZ = Float.parseFloat(a[5]);
				float speed = Float.parseFloat(a[6]);
				int amount = Integer.parseInt(a[7]);
				
				ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(type, data), offsetX, offsetY, offsetZ, speed, amount, center, 32);
				
				return true;
			}
			
			if (a[0].equals("color")) {
				int red = Integer.parseInt(a[2]);
				int green = Integer.parseInt(a[3]);
				int blue = Integer.parseInt(a[4]);
				float offsetX = Float.parseFloat(a[5]);
				float offsetY = Float.parseFloat(a[6]);
				float offsetZ = Float.parseFloat(a[7]);
				float speed = Float.parseFloat(a[8]);
				int amount = Integer.parseInt(a[9]);
				
				ParticleEffect.valueOf(a[1]).display(null, center, Color.fromRGB(red, green, blue), 32, offsetX, offsetY, offsetZ, speed, amount);
				
				return true;
			}
			
			if (a[0].equals("firework")) {
				final Firework firework = p.getWorld().spawn(center, Firework.class);
				FireworkMeta meta = firework.getFireworkMeta();
				FireworkEffect effect = FireworkEffect.builder().with(Type.BALL_LARGE).withColor(Color.RED).withTrail().build();
				
				meta.addEffect(effect);
				meta.setPower(1);
				firework.setFireworkMeta(meta);
				
				getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {
						firework.detonate();
					}
				}, Long.parseLong(a[1]));
				
				return true;
			}
			
			if (a[0].equals("chunk")) {
				Chunk chunk = ((CraftChunk) p.getWorld().getChunkAt(center.getBlock())).getHandle();
				List<Chunk> list = new ArrayList<Chunk>();
				list.add(chunk);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunkBulk(list));
				
				return true;
			}
			
			float offsetX = Float.parseFloat(a[1]);
			float offsetY = Float.parseFloat(a[2]);
			float offsetZ = Float.parseFloat(a[3]);
			float speed = Float.parseFloat(a[4]);
			int amount = Integer.parseInt(a[5]);
			
			ParticleEffect.valueOf(a[0]).display(offsetX, offsetY, offsetZ, speed, amount, center, 32);
			
			return true;
		}
		
		return false;
	}
	
	public void write(String name, List<String> data) {
		File f = new File(getDataFolder(), name);
		
		try {
			if (!f.exists()) {
				getDataFolder().mkdir();
				
				f.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (String str : data) {
				bw.append(str+"\r\n");
			}
			
			bw.flush();
			bw.close();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
}