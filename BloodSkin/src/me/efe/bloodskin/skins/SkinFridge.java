package me.efe.bloodskin.skins;

import java.util.Random;

import me.efe.bloodskin.utils.FakeEntityUtils;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinFridge extends Skin {
	private Random random = new Random();
	private ItemStack[] items = new ItemStack[]{
			new ItemStack(Material.MUSHROOM_SOUP), new ItemStack(Material.BREAD), new ItemStack(Material.GRILLED_PORK), new ItemStack(Material.COOKED_FISH),
			new ItemStack(Material.MELON), new ItemStack(Material.COOKED_BEEF), new ItemStack(Material.PUMPKIN_PIE), new ItemStack(Material.CARROT_ITEM)
	};
	
	public SkinFridge() {
		super("fridge", true, true, "≥√¿Â∞Ì", new ItemStack(Material.IRON_BLOCK));
	}
	
	@Override
	public void effect(Player p, final LivingEntity entity) {
		ItemStack item = items[random.nextInt(items.length)];
		
		ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(item.getType(), (byte) item.getDurability()), 0.1F, 0.1F, 0.1F, 0.1F, 30, getBody(entity), 32);
		
		for (int i = 0; i < random.nextInt(2) + 2; i ++) {
			final int id = FakeEntityUtils.generateId();
			
			FakeEntityUtils.spawnItem(getBody(entity), item.clone(), id);
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