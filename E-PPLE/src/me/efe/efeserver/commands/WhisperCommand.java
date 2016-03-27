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
		this.plugin.getCommand("귓속말").setTabCompleter(this);
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
				p.sendMessage("§c▒§r 귓속말이 비허용 상태입니다.");
				return false;
			}
			
			if (a.length == 0) {
				p.sendMessage("§a▒§r §b/"+l+" <플레이어>§r: 자동 귓속말 모드를 켭니다.");
				
				if (l.equalsIgnoreCase("msg") || l.equalsIgnoreCase("tell"))
					p.sendMessage("§a▒§r §b/"+l+" off§r: 자동 귓속말 모드를 끕니다.");
				else
					p.sendMessage("§a▒§r §b/"+l+" 해제§r: 자동 귓속말 모드를 끕니다.");
				
				p.sendMessage("§a▒§r §b/"+l+" <플레이어> <메세지>§r: 귓속말을 보냅니다.");
				return false;
			}
			
			if (a.length == 1) {
				if (a[0].equalsIgnoreCase("해제") || a[0].equalsIgnoreCase("끄기") || a[0].equalsIgnoreCase("off")) {
					plugin.chatListener.whispers.remove(p.getUniqueId());
					
					p.sendMessage("§a▒§r 자동 귓속말 모드가 해제되었습니다.");
					return false;
				}
				
				Player target = plugin.util.getOnlinePlayer(a[0]);
				
				if (target == null) {
					p.sendMessage("§c▒§r 귓속말 상대가 오프라인 상태입니다.");
					return false;
				}
				
				if (target.equals(p)) {
					p.sendMessage("§c▒§r 자신을 대상으로 할 수는 없습니다.");
					return false;
				}
				
				if (!PlayerData.get(target).getOptionWhisper()) {
					p.sendMessage("§c▒§r 상대가 귓속말 거부 상태입니다.");
					return false;
				}
				
				plugin.chatListener.whispers.put(p.getUniqueId(), target.getUniqueId());
				
				if (plugin.chatListener.partyChats.containsKey(p.getUniqueId()))
					plugin.chatListener.partyChats.remove(p.getUniqueId());
				
				p.sendMessage("§a▒§r 입력되는 채팅이 자동으로 귓속말로 상대에게 발송됩니다.");
				p.sendMessage("§a▒§r 해제하려면 §a/"+l+" 해제§r 명령어를 입력해주세요.");
			}
			
			if (a.length >= 2) {
				Player target = plugin.util.getOnlinePlayer(a[0]);
				
				if (target == null) {
					p.sendMessage("§c▒§r 귓속말 상대가 오프라인 상태입니다.");
					return false;
				}
				
				if (target.equals(p)) {
					p.sendMessage("§c▒§r 자신을 대상으로 할 수는 없습니다.");
					return false;
				}
				
				if (!PlayerData.get(target).getOptionWhisper()) {
					p.sendMessage("§c▒§r 상대가 귓속말 거부 상태입니다.");
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
						.replaceAll("e-pple.kr", "%이플%").replaceAll("e-pple.com", "%이플컴%")
						.replaceAll("[a-zA-Z-_]{1,20}[.][a-zA-Z-_]{1,20}", "*****.**")
						.replaceAll("%이플%", "e-pple.kr").replaceAll("%이플컴%", "e-pple.com");
				
				p.sendMessage(" §e→"+target.getName()+" §6> §r"+message);
				target.sendMessage(" §e"+p.getName()+"§e→ §6> §r"+message);
			}
			
			
		} catch (Exception e) {
			s.sendMessage("§c▒§r 잘못된 명령어입니다!");
		}
		
		return false;
	}
}