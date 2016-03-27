package me.efe.efemobs;

import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.World;

public class FixedEnderman extends EntityEnderman {
	
	public FixedEnderman(World world) {
		super(world);
	}
	
	@Override
	protected boolean k(double d0, double d1, double d2) {
		
		return false;
	}
}