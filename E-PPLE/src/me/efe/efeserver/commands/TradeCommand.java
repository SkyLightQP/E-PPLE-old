package me.efe.efeserver.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.util.TabCompletionHelper;
import mkremins.fanciful.FancyMessage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TradeCommand implements CommandExecutor, TabCompleter {
	public EfeServer plugin;
	public HashMap<UUID, UUID> requests = new HashMap<UUID, UUID>();
	
	public TradeCommand(EfeServer plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("�ŷ�").setTabCompleter(this);
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
			
			if (a.length == 0) {
				p.sendMessage("��a�ơ�r ��b/"+l+" <Player>��r: �ŷ��� ��û�ϰų� �����մϴ�.");
			} else {
				Player target = plugin.util.getOnlinePlayer(a[0]);
				
				if (target == null) {
					p.sendMessage("��c�ơ�r "+a[0]+"���� �������� �ʰų� �������� �����Դϴ�.");
					return false;
				}
				
				if (target.equals(p)) {
					p.sendMessage("��c�ơ�r ���� �� �Ǵ� ��ɾ��Դϴ�.");
					return false;
				}
				
				if (requests.containsKey(p.getUniqueId()) && requests.get(p.getUniqueId()).equals(target.getUniqueId())) {
					p.sendMessage("��c�ơ�r �̹� �ŷ��� ��û�߽��ϴ�.");
					return false;
				}
				
				if (requests.containsKey(target.getUniqueId()) && requests.get(target.getUniqueId()).equals(p.getUniqueId())) {
					requests.remove(p.getUniqueId());
					requests.remove(target.getUniqueId());
					
					plugin.tradeGui.openGUI(p, target);
					plugin.tradeGui.openGUI(target, p);
					
					p.sendMessage("��a�ơ�r �ŷ� ��û�� �����߽��ϴ�.");
					target.sendMessage("��a�ơ�r �ŷ� ��û�� �����Ǿ����ϴ�.");
					return true;
				}
				
				requests.put(p.getUniqueId(), target.getUniqueId());
				
				target.sendMessage("��a�ơ�r "+p.getName()+"�Բ��� �ŷ��� ��û�ϼ̽��ϴ�.");
				
				new FancyMessage("��a�ơ�r ")
				.then("��b��n/�ŷ� "+p.getName()+"��r")
					.command("/�ŷ� "+p.getName())
					.tooltip("��b/�ŷ� "+p.getName())
				.then(" ��ɾ�� �������ּ���.")
				.send(target);
				
				p.sendMessage("��a�ơ�r "+target.getName()+"�Բ� �ŷ��� ��û�߽��ϴ�.");
			}
			
		} catch (Exception e) {
			s.sendMessage("��c�ơ�r �߸��� ��ɾ��Դϴ�!");
		}
		
		return false;
	}
}