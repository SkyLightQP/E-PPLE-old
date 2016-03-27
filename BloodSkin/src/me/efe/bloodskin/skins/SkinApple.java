package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinApple extends Skin {
	
	public SkinApple() {
		super("apple", false, true, "»ç°ú", new ItemStack(Material.APPLE));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.APPLE, (byte) 0), 0.1F, 0.1F, 0.1F, 0.1F, 30, getBody(entity), 32);
	}
}