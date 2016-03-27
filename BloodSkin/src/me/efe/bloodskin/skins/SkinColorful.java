package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinColorful extends Skin {
	
	public SkinColorful() {
		super("colorful", false, true, "»öÄ¥°øºÎ", new ItemStack(Material.INK_SACK, 1, (short) 14));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.REDSTONE.display(0.3F, 0.3F, 0.3F, 1.0F, 10, getBody(entity), 32);
	}
}