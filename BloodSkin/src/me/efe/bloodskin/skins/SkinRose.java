package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinRose extends Skin {
	
	public SkinRose() {
		super("rose", false, true, "¿ÂπÃ", new ItemStack(Material.RED_ROSE));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.RED_ROSE, (byte) 0), 0.0F, 0.0F, 0.0F, 0.1F, 50, getBody(entity), 32);
	}
}