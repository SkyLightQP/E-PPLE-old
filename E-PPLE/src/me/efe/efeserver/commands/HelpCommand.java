package me.efe.efeserver.commands;

import me.efe.efeserver.EfeServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {
	public EfeServer plugin;
	
	public HelpCommand(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		try {
			s.sendMessage("��a�ơ�r ��b/�޴���r: ���� �޴��� ���ϴ�.");
			s.sendMessage("��a�ơ�r ��b/���õ���r: �ڽ��� ä�� Ȱ�� ���õ��� Ȯ���մϴ�.");
			s.sendMessage("��a�ơ�r ��b/�ӼӸ���r: �ӼӸ��� ���� ��ɾ Ȯ���մϴ�.");
			s.sendMessage("��a�ơ�r ��b/�ŷ� <Player>��r: �ŷ��� ��û�մϴ�.");
			s.sendMessage("��a�ơ�r ��b/���� <Player>��r: �ٸ� ������ ������ Ȯ���մϴ�.");
			s.sendMessage("��a�ơ�r <Tip> �ٸ� ������ ���� ��Ŭ���ϸ� ������ Ȯ���� �� �ֽ��ϴ�.");
		} catch (Exception e) {
			s.sendMessage("��c�ơ�r �߸��� ��ɾ��Դϴ�!");
		}
		
		return false;
	}
}