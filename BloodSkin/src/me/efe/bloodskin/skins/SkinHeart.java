package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinHeart extends Skin {
	
	public SkinHeart() {
		super("heart", false, true, "ÇÏÆ®", new ItemStack(Material.INK_SACK, 1, (short) 1));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.HEART.display(0.3F, 0.3F, 0.3F, 0.0F, 1, entity.getEyeLocation().add(0, 0.3, 0), 32);
	}
}