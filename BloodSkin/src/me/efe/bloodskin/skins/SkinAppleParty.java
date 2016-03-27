package me.efe.bloodskin.skins;

import java.util.Random;

import me.efe.bloodskin.utils.FakeEntityUtils;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinAppleParty extends Skin {
	
	public SkinAppleParty() {
		super("apple_party", true, false, "애플 파티", new ItemStack(Material.APPLE));
	}
	
	@Override
	public void effect(Player p, final LivingEntity entity) {
		ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.APPLE, (byte) 0), 0.1F, 0.1F, 0.1F, 0.1F, 30, getBody(entity), 32);
		
		for (int i = 0; i < new Random().nextInt(2) + 2; i ++) {
			final int id = FakeEntityUtils.generateId();
			
			FakeEntityUtils.spawnItem(getBody(entity), new ItemStack(Material.APPLE), id);
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