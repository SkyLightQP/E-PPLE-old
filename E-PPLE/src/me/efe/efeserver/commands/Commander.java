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
			
			e.getPlayer().sendMessage("��a�ơ�r Seed: 3.141592653589793238462643383279��");
		} else if ((command.equals("pl") || command.equals("plugin")) && !e.getPlayer().isOp()) {
			e.setCancelled(true);
			
			e.getPlayer().sendMessage("��a�ơ�r Plugins (1): ��aE-PPLE");
		} else if (command.equals("����") || command.equals("����") || command.equals("spawn")) {
			e.setCancelled(true);
			
			e.getPlayer().sendMessage("��c�ơ�r E-PPLE���� ���� ��ɾ �������� �ʽ��ϴ�.");
			e.getPlayer().sendMessage("��c�ơ�r ���� �踦 Ÿ�� �����ϼ���!");
		}
	}
	
	public void serverCommand(Player p, String[] a) {
		if (a.length == 0) {
			p.sendMessage("��a�ơ�r ����������������������������������");
			p.sendMessage("��a�ơ�r - /���� ���� <world>");
			p.sendMessage("��a�ơ�r - /���� �Ӹ� <name>, /���� �Ӹ��ڵ� <int>");
			p.sendMessage("��a�ơ�r - /���� Entity <name>");
			p.sendMessage("��a�ơ�r - /���� û��");
			p.sendMessage("��a�ơ�r - /���� ��ǥ");
			p.sendMessage("��a�ơ�r - /���� ���׾ȳ�");
			p.sendMessage("��a�ơ�r ����������������������������������");
		} else if (a[0].equalsIgnoreCase("����")) {
			World world = plugin.getServer().getWorld(a[1]);
			Location l = p.getLocation().clone();
			l.setWorld(world);
			p.teleport(l);
			p.sendMessage("��a�ơ�r - ��ɾ� ���� �Ϸ�");
		} else if (a[0].equalsIgnoreCase("Entity")) {
			EntityType type = EntityType.valueOf(a[1]);
			if (type == null) {
				p.sendMessage("��a�ơ�r "+a[1]+" Ÿ���� �������� �ʽ��ϴ�.");
				return;
			}
			
			for (Entity entity : p.getWorld().getEntities()) {
				if (entity.getType().equals(type)) {
					entity.remove();
				}
			}
			
			p.sendMessage("��a�ơ�r "+"- ��ɾ� ���� �Ϸ�");
		} else if (a[0].equalsIgnoreCase("û��")) {
			for (World world : plugin.getServer().getWorlds()) {
				for (Entity entity : world.getEntities()) {
					if (entity.getType().equals(EntityType.ARROW) || 
							entity.getType().equals(EntityType.DROPPED_ITEM) || 
							entity.getType().equals(EntityType.EXPERIENCE_ORB)) {
						entity.remove();
					}
				}
			}
			
			p.sendMessage("��a�ơ�r "+"- ��ɾ� ���� �Ϸ�");
		}
		return;
	}
}