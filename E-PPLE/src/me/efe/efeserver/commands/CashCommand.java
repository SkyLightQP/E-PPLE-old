package me.efe.efeserver.commands;

import me.efe.efeserver.EfeServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashCommand implements CommandExecutor {
	public EfeServer plugin;
	
	public CashCommand(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		try {
			plugin.cashGui.openGUI((Player) s);
		} catch (Exception e) {
			s.sendMessage("§c▒§r 잘못된 명령어입니다!");
		}
		
		return false;
	}
}