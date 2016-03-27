package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinSnow extends Skin {
	
	public SkinSnow() {
		super("snow", false, true, "´«½Î¿ò", new ItemStack(Material.SNOW_BALL));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.SNOW_SHOVEL.display(0.1F, 0.1F, 0.1F, 0.1F, 30, getBody(entity), 32);
	}
}