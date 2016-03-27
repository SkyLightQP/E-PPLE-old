package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinNote extends Skin {
	
	public SkinNote() {
		super("note", false, true, "À½Ç¥", new ItemStack(Material.NOTE_BLOCK));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.NOTE.display(0.3F, 0.3F, 0.3F, 1.0F, 3, entity.getEyeLocation().add(0, 0.3, 0), 32);
	}
}