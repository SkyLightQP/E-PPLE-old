package me.efe.unlimitedrpg.customexp;

import java.util.List;

import me.efe.unlimitedrpg.UnlimitedRPG;

import org.bukkit.entity.Player;

public class CustomExpAPI {
	private static List<Integer> costs;
	
	public static void init(UnlimitedRPG plugin) {
		costs = plugin.getConfig().getIntegerList("custom-exp.exp-along-level");
	}
	
	public static void giveExp(Player p, int amount) {
		if (p.getLevel() >= CustomExpAPI.getMaxLevel()) {
			p.setExp(0.0F);
			p.setLevel(CustomExpAPI.getMaxLevel());
			return;
		}
		
		int total = costs.get(p.getLevel());
		float exp = amount;
		
		float xp = p.getExp() + exp / total;
		int lv = 0;
		while (xp >= 1.0F) {
			lv ++;
			xp --;
		}
		
		p.setExp(xp);
		p.giveExpLevels(lv);
	}
	
	public static List<Integer> getCosts() {
		return costs;
	}
	
	public static int getMaxLevel() {
		return costs.size();
	}
}