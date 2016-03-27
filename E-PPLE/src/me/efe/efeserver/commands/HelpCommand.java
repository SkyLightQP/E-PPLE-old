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
			s.sendMessage("§a▒§r §b/메뉴§r: 메인 메뉴를 엽니다.");
			s.sendMessage("§a▒§r §b/숙련도§r: 자신의 채집 활동 숙련도를 확인합니다.");
			s.sendMessage("§a▒§r §b/귓속말§r: 귓속말에 대한 명령어를 확인합니다.");
			s.sendMessage("§a▒§r §b/거래 <Player>§r: 거래를 신청합니다.");
			s.sendMessage("§a▒§r §b/정보 <Player>§r: 다른 유저의 정보를 확인합니다.");
			s.sendMessage("§a▒§r <Tip> 다른 유저를 더블 우클릭하면 정보를 확인할 수 있습니다.");
		} catch (Exception e) {
			s.sendMessage("§c▒§r 잘못된 명령어입니다!");
		}
		
		return false;
	}
}