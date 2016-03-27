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
				p.sendMessage("��a�ơ�r �Ŀ��ڸ� �̿��� �� �ִ� ����Դϴ�.");
			}
			
		} catch (Exception e) {
			s.sendMessage("��c�ơ�r �߸��� ��ɾ��Դϴ�!");
		}
		
		return false;
	}
}