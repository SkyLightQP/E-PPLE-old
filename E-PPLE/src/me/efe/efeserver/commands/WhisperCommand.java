package me.efe.efeserver.commands;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efeserver.util.TabCompletionHelper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class WhisperCommand implements CommandExecutor, TabCompleter {
	public EfeServer plugin;
	
	public WhisperCommand(EfeServer plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("�ӼӸ�").setTabCompleter(this);
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
			Player p = (Player) s;
			PlayerData data = PlayerData.get(p);
			
			if (!data.getOptionWhisper()) {
				p.sendMessage("��c�ơ�r �ӼӸ��� ����� �����Դϴ�.");
				return false;
			}
			
			if (a.length == 0) {
				p.sendMessage("��a�ơ�r ��b/"+l+" <�÷��̾�>��r: �ڵ� �ӼӸ� ��带 �մϴ�.");
				
				if (l.equalsIgnoreCase("msg") || l.equalsIgnoreCase("tell"))
					p.sendMessage("��a�ơ�r ��b/"+l+" off��r: �ڵ� �ӼӸ� ��带 ���ϴ�.");
				else
					p.sendMessage("��a�ơ�r ��b/"+l+" ������r: �ڵ� �ӼӸ� ��带 ���ϴ�.");
				
				p.sendMessage("��a�ơ�r ��b/"+l+" <�÷��̾�> <�޼���>��r: �ӼӸ��� �����ϴ�.");
				return false;
			}
			
			if (a.length == 1) {
				if (a[0].equalsIgnoreCase("����") || a[0].equalsIgnoreCase("����") || a[0].equalsIgnoreCase("off")) {
					plugin.chatListener.whispers.remove(p.getUniqueId());
					
					p.sendMessage("��a�ơ�r �ڵ� �ӼӸ� ��尡 �����Ǿ����ϴ�.");
					return false;
				}
				
				Player target = plugin.util.getOnlinePlayer(a[0]);
				
				if (target == null) {
					p.sendMessage("��c�ơ�r �ӼӸ� ��밡 �������� �����Դϴ�.");
					return false;
				}
				
				if (target.equals(p)) {
					p.sendMessage("��c�ơ�r �ڽ��� ������� �� ���� �����ϴ�.");
					return false;
				}
				
				if (!PlayerData.get(target).getOptionWhisper()) {
					p.sendMessage("��c�ơ�r ��밡 �ӼӸ� �ź� �����Դϴ�.");
					return false;
				}
				
				plugin.chatListener.whispers.put(p.getUniqueId(), target.getUniqueId());
				
				if (plugin.chatListener.partyChats.containsKey(p.getUniqueId()))
					plugin.chatListener.partyChats.remove(p.getUniqueId());
				
				p.sendMessage("��a�ơ�r �ԷµǴ� ä���� �ڵ����� �ӼӸ��� ��뿡�� �߼۵˴ϴ�.");
				p.sendMessage("��a�ơ�r �����Ϸ��� ��a/"+l+" ������r ��ɾ �Է����ּ���.");
			}
			
			if (a.length >= 2) {
				Player target = plugin.util.getOnlinePlayer(a[0]);
				
				if (target == null) {
					p.sendMessage("��c�ơ�r �ӼӸ� ��밡 �������� �����Դϴ�.");
					return false;
				}
				
				if (target.equals(p)) {
					p.sendMessage("��c�ơ�r �ڽ��� ������� �� ���� �����ϴ�.");
					return false;
				}
				
				if (!PlayerData.get(target).getOptionWhisper()) {
					p.sendMessage("��c�ơ�r ��밡 �ӼӸ� �ź� �����Դϴ�.");
					return false;
				}
				
				String message = plugin.util.getFinalArg(a, 1);
				
				for (String words : plugin.chatListener.bannedWords) {
					String replace = "";
					
					for (int i = 0; i < words.length(); i ++)
						replace += '*';
					
					message = message.replaceAll(words, replace);
				}
				
				message = message
						.replaceAll("e-pple.kr", "%����%").replaceAll("e-pple.com", "%������%")
						.replaceAll("[a-zA-Z-_]{1,20}[.][a-zA-Z-_]{1,20}", "*****.**")
						.replaceAll("%����%", "e-pple.kr").replaceAll("%������%", "e-pple.com");
				
				p.sendMessage(" ��e��"+target.getName()+" ��6> ��r"+message);
				target.sendMessage(" ��e"+p.getName()+"��e�� ��6> ��r"+message);
			}
			
			
		} catch (Exception e) {
			s.sendMessage("��c�ơ�r �߸��� ��ɾ��Դϴ�!");
		}
		
		return false;
	}
}