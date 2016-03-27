package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinAngry extends Skin {
	
	public SkinAngry() {
		super("angry", false, true, "ºÐ³ë", new ItemStack(Material.FIREBALL));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.VILLAGER_ANGRY.display(0.3F, 0.3F, 0.3F, 0.0F, 1, entity.getEyeLocation().add(0, 0.3, 0), 32);
	}
}