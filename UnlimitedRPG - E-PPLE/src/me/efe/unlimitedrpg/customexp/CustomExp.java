package me.efe.unlimitedrpg.customexp;

import me.efe.unlimitedrpg.UnlimitedRPG;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class CustomExp implements Listener {
	public UnlimitedRPG plugin;
	
	public CustomExp(UnlimitedRPG plugin) {
		this.plugin = plugin;
		
		CustomExpAPI.init(plugin);
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void expChange(PlayerExpChangeEvent e) {
		int level = e.getPlayer().getLevel();
		float amount = e.getAmount();
		
		if (level >= CustomExpAPI.getMaxLevel()) {
			e.setAmount(0);
			
			e.getPlayer().setExp(0.0F);
			e.getPlayer().setLevel(CustomExpAPI.getMaxLevel());
			return;
		}
		
		float exp = e.getPlayer().getExp() + amount / CustomExpAPI.getCosts().get(level);
		int lv = 0;
		while (exp >= 1.0F) {
			lv ++;
			exp --;
		}
		
		e.getPlayer().setExp(exp);
		e.getPlayer().giveExpLevels(lv);
		e.setAmount(0);
	}
	
	@EventHandler
	public void command(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().equalsIgnoreCase("/xp") || e.getMessage().startsWith("/xp ")) {
			if (e.getPlayer().hasMetadata("customexp")) return;
			
			e.getPlayer().sendMessage(plugin.main+"§c[경고]§r /xp 명령어는 CustomEXP 기능과 호환되지 않습니다.");
			e.getPlayer().setMetadata("customexp", new FixedMetadataValue(plugin, "Made by Efe"));
		}
	}
}