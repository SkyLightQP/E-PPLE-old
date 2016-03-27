package me.efe.bloodskin.skins;

import java.util.Random;

import me.efe.bloodskin.utils.FakeEntityUtils;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinWool extends Skin {
	private Random random = new Random();
	
	public SkinWool() {
		super("wool", true, true, "알록달록 양털", new ItemStack(Material.WOOL));
	}
	
	@Override
	public void effect(Player p, final LivingEntity entity) {
		ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.WOOL, (byte) random.nextInt(16)), 0.3F, 0.3F, 0.3F, 0.0F, 100, getBody(entity), 32);
		
		for (int i = 0; i < random.nextInt(2) + 2; i ++) {
			final int id = FakeEntityUtils.generateId();
			
			FakeEntityUtils.spawnItem(getBody(entity), new ItemStack(Material.WOOL, 1, (short) random.nextInt(16)), id);
			FakeEntityUtils.setVelocity(id, new Vector(Math.random() * 2 - 1, 0.3, Math.random() * 2 - 1).multiply(0.5D));
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					FakeEntityUtils.removeEntity(id);
				}
			}, 20L);
		}
	}
}