package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinFlame extends Skin {
	
	public SkinFlame() {
		super("flame", false, true, "�Ҳ�", new ItemStack(Material.FIREBALL));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.FLAME.display(0.01F, 0.01F, 0.01F, 0.15F, 3, getBody(entity), 32);
	}
}