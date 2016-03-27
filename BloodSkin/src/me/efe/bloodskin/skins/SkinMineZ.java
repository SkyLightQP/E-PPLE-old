package me.efe.bloodskin.skins;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.util.ParticleEffect;

public class SkinMineZ extends Skin {
	
	public SkinMineZ() {
		super("minez", false, true, "MineZ", new ItemStack(Material.ROTTEN_FLESH));
	}
	
	@Override
	public void effect(Player p, LivingEntity entity) {
		ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.ENDER_STONE, (byte) 0), 0.3F, 0.3F, 0.3F, 0.0F, 30, getBody(entity), 32);
	}
}