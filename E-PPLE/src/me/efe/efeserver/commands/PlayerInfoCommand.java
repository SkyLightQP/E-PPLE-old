package me.efe.efeserver.commands;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.util.TabCompletionHelper;
import me.efe.unlimitedrpg.playerinfo.PlayerInfo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PlayerInfoCommand implements CommandExecutor, TabCompleter {
	public EfeServer plugin;
	
	public PlayerInfoCommand(EfeServer plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("����").setTabCompleter(this);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
		List<String> list = new ArrayList<String>();
		
		for (Player all : plugin.getServer().getOnlinePlayers()) {
			list.add(all.getName());
		}
		
		return TabCompletionHelper.getPossibleCompletions(a, list);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		try {
			if (a.length == 0) {
				s.sendMessage("��a�ơ�r ��b/"+l+" <�÷��̾�>��r: ������ ������ Ȯ���մϴ�.");
				return false;
			}
			
			Player target = plugin.util.getOnlinePlayer(a[0]);
			
			if (target == null) {
				s.sendMessage("��c�ơ�r �������� �ʴ� �÷��̾��Դϴ�.");
				return false;
			}
			
			PlayerInfo.openGUI(target, (Player) s);
		} catch (Exception e) {
			s.sendMessage("��c�ơ�r �߸��� ��ɾ��Դϴ�!");
		}
		
		return false;
	}
}