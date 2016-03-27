package me.efe.fishkg.extrashop.citizens;

import org.bukkit.Bukkit;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class CitizensHooker {
	
	public static boolean exists() {
		return Bukkit.getPluginManager().getPlugin("Citizens") != null;
	}
	
	public static void hook() {
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(FishMerchantTrait.class).withName("FishMerchant"));
	}
}