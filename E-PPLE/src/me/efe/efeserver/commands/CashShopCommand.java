package me.efe.efeserver.commands;

import me.efe.efeserver.EfeServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashShopCommand implements CommandExecutor {
	public EfeServer plugin;
	
	public CashShopCommand(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		try {
			plugin.cashShopGui.openGUI((Player) s);
		} catch (Exception e) {
			s.sendMessage("��c�ơ�r �߸��� ��ɾ��Դϴ�!");
		}
		
		return false;
	}
}