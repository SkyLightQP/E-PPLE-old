package me.efe.bloodskin.skins;

import java.util.Random;

import me.efe.bloodskin.utils.FakeEntityUtils;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinTNT extends Skin {
	
	public SkinTNT() {
		super("tnt", true, true, "TNT", new ItemStack(Material.TNT));
	}
	
	@Override
	public void effect(Player p, final LivingEntity entity) {
		ParticleEffect.SMOKE_NORMAL.display(0.0F, 0.0F, 0.0F, 0.05F, 100, getBody(entity), 32);
		
		for (int i = 0; i < new Random().nextInt(2) + 2; i ++) {
			final int id = FakeEntityUtils.generateId();
			
			FakeEntityUtils.spawnItem(getBody(entity), new ItemStack(Material.TNT), id);
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