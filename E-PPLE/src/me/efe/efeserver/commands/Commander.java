package me.efe.efeserver.commands;

import me.efe.efegear.EfeUtil;
import me.efe.efeserver.EfeServer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Commander implements Listener {
	public EfeServer plugin;
	public EfeUtil util;
	
	public Commander(EfeServer plugin) {
		this.plugin = plugin;
		this.util = plugin.util;
	}
	
	@EventHandler
	public void overrideCommand(PlayerCommandPreprocessEvent e) {
		String command = e.getMessage().replaceAll(" ", "").substring(1);
		
		if (command.equals("seed")) {
			e.setCancelled(true);
			
			e.getPlayer().sendMessage("§a▒§r Seed: 3.141592653589793238462643383279…");
		} else if ((command.equals("pl") || command.equals("plugin")) && !e.getPlayer().isOp()) {
			e.setCancelled(true);
			
			e.getPlayer().sendMessage("§a▒§r Plugins (1): §aE-PPLE");
		} else if (command.equals("스폰") || command.equals("넴주") || command.equals("spawn")) {
			e.setCancelled(true);
			
			e.getPlayer().sendMessage("§c▒§r E-PPLE에는 스폰 명령어가 존재하지 않습니다.");
			e.getPlayer().sendMessage("§c▒§r 직접 배를 타고 항해하세요!");
		}
	}
	
	public void serverCommand(Player p, String[] a) {
		if (a.length == 0) {
			p.sendMessage("§a▒§r ▶───────────────◀");
			p.sendMessage("§a▒§r - /서버 월드 <world>");
			p.sendMessage("§a▒§r - /서버 머리 <name>, /서버 머리코드 <int>");
			p.sendMessage("§a▒§r - /서버 Entity <name>");
			p.sendMessage("§a▒§r - /서버 청소");
			p.sendMessage("§a▒§r - /서버 좌표");
			p.sendMessage("§a▒§r - /서버 리붓안내");
			p.sendMessage("§a▒§r ▶───────────────◀");
		} else if (a[0].equalsIgnoreCase("월드")) {
			World world = plugin.getServer().getWorld(a[1]);
			Location l = p.getLocation().clone();
			l.setWorld(world);
			p.teleport(l);
			p.sendMessage("§a▒§r - 명령어 실행 완료");
		} else if (a[0].equalsIgnoreCase("Entity")) {
			EntityType type = EntityType.valueOf(a[1]);
			if (type == null) {
				p.sendMessage("§a▒§r "+a[1]+" 타입은 존재하지 않습니다.");
				return;
			}
			
			for (Entity entity : p.getWorld().getEntities()) {
				if (entity.getType().equals(type)) {
					entity.remove();
				}
			}
			
			p.sendMessage("§a▒§r "+"- 명령어 실행 완료");
		} else if (a[0].equalsIgnoreCase("청소")) {
			for (World world : plugin.getServer().getWorlds()) {
				for (Entity entity : world.getEntities()) {
					if (entity.getType().equals(EntityType.ARROW) || 
							entity.getType().equals(EntityType.DROPPED_ITEM) || 
							entity.getType().equals(EntityType.EXPERIENCE_ORB)) {
						entity.remove();
					}
				}
			}
			
			p.sendMessage("§a▒§r "+"- 명령어 실행 완료");
		}
		return;
	}
}