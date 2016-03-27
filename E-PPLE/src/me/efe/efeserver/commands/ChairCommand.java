package me.efe.efeserver.commands;

import me.efe.efeserver.EfeServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChairCommand implements CommandExecutor {
	public EfeServer plugin;
	
	public ChairCommand(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		try {
			Player p = (Player) s;
			
			if (p.hasPermission("epple.whereverchair")) {
				plugin.chairManager.setChair(p);
				
			} else {
				p.sendMessage("§a▒§r 후원자만 이용할 수 있는 기능입니다.");
			}
			
		} catch (Exception e) {
			s.sendMessage("§c▒§r 잘못된 명령어입니다!");
		}
		
		return false;
	}
}