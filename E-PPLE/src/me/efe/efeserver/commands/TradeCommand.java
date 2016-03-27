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
		this.plugin.getCommand("거래").setTabCompleter(this);
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
				p.sendMessage("§a▒§r §b/"+l+" <Player>§r: 거래를 신청하거나 수락합니다.");
			} else {
				Player target = plugin.util.getOnlinePlayer(a[0]);
				
				if (target == null) {
					p.sendMessage("§c▒§r "+a[0]+"님은 존재하지 않거나 오프라인 상태입니다.");
					return false;
				}
				
				if (target.equals(p)) {
					p.sendMessage("§c▒§r 말도 안 되는 명령어입니다.");
					return false;
				}
				
				if (requests.containsKey(p.getUniqueId()) && requests.get(p.getUniqueId()).equals(target.getUniqueId())) {
					p.sendMessage("§c▒§r 이미 거래를 신청했습니다.");
					return false;
				}
				
				if (requests.containsKey(target.getUniqueId()) && requests.get(target.getUniqueId()).equals(p.getUniqueId())) {
					requests.remove(p.getUniqueId());
					requests.remove(target.getUniqueId());
					
					plugin.tradeGui.openGUI(p, target);
					plugin.tradeGui.openGUI(target, p);
					
					p.sendMessage("§a▒§r 거래 신청을 수락했습니다.");
					target.sendMessage("§a▒§r 거래 신청이 수락되었습니다.");
					return true;
				}
				
				requests.put(p.getUniqueId(), target.getUniqueId());
				
				target.sendMessage("§a▒§r "+p.getName()+"님께서 거래를 신청하셨습니다.");
				
				new FancyMessage("§a▒§r ")
				.then("§b§n/거래 "+p.getName()+"§r")
					.command("/거래 "+p.getName())
					.tooltip("§b/거래 "+p.getName())
				.then(" 명령어로 수락해주세요.")
				.send(target);
				
				p.sendMessage("§a▒§r "+target.getName()+"님께 거래를 신청했습니다.");
			}
			
		} catch (Exception e) {
			s.sendMessage("§c▒§r 잘못된 명령어입니다!");
		}
		
		return false;
	}
}