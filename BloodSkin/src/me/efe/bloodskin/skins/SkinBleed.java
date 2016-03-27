package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinBleed extends Skin {
	
	public SkinBleed() {
		super("bleed", false, true, "ÃâÇ÷", new ItemStack(Material.REDSTONE));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte) 0), 0.05F, 0.05F, 0.05F, 0.075F, 20, getBody(entity), 32);
	}
}