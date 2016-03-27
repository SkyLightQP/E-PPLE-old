package me.efe.fishkg.commands;

import me.efe.fishkg.Fishkg;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLure implements CommandExecutor {
	public Fishkg plugin;
	
	public CommandLure(Fishkg plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		try {
			if (!plugin.getConfig().getBoolean("fishingRod.lure")) {
				s.sendMessage(plugin.main+"config.yml 파일에서 fishingRod.lure를 true로 하셔야 사용할 수 있습니다.");
				return false;
			}
			
			if (plugin.enchLure == null) {
				s.sendMessage(plugin.main+"이 기능은 1.7 이상의 마인크래프트에서만 지원됩니다.");
				return false;
			}
			
			if (!(s instanceof Player)) {
				s.sendMessage(plugin.main+"플레이어만 가능한 명령어입니다.");
				return false;
			}
			
			Player p = (Player) s;
			
			if (!plugin.checkPermission(p, "fishkg.lure")) {
				p.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.lure]");
				return false;
			}
			
			if (!p.getItemInHand().getType().equals(Material.FISHING_ROD)) {
				p.sendMessage(plugin.main+"§a▒§r 미끼를 장착할 낚싯대를 들어주세요.");
				return false;
			}
			
			if (plugin.hasLure(p.getItemInHand())) {
				p.sendMessage(plugin.main+"§a▒§r 이미 미끼가 장착된 낚싯대입니다.");
				return false;
			}
			
			plugin.lureGui.openGUI(p);
		} catch (Exception e) {
			s.sendMessage(plugin.main+"잘못된 명령어입니다. §8[/미끼]");
			
			if (plugin.getConfig().getBoolean("general.debug")) e.printStackTrace();
		}
		
		return false;
	}
}