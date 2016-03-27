package me.efe.fishkg.commands;

import me.efe.fishkg.Fishkg;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLure implements CommandExecutor {
	public Fishkg plugin;
	
	public CommandLure(Fishkg plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		try {
			if (!plugin.getConfig().getBoolean("fishingRod.lure")) {
				s.sendMessage(plugin.main+"config.yml ���Ͽ��� fishingRod.lure�� true�� �ϼž� ����� �� �ֽ��ϴ�.");
				return false;
			}
			
			if (plugin.enchLure == null) {
				s.sendMessage(plugin.main+"�� ����� 1.7 �̻��� ����ũ����Ʈ������ �����˴ϴ�.");
				return false;
			}
			
			if (!(s instanceof Player)) {
				s.sendMessage(plugin.main+"�÷��̾ ������ ��ɾ��Դϴ�.");
				return false;
			}
			
			Player p = (Player) s;
			
			if (!plugin.checkPermission(p, "fishkg.lure")) {
				p.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.lure]");
				return false;
			}
			
			if (!p.getItemInHand().getType().equals(Material.FISHING_ROD)) {
				p.sendMessage(plugin.main+"��a�ơ�r �̳��� ������ ���˴븦 ����ּ���.");
				return false;
			}
			
			if (plugin.hasLure(p.getItemInHand())) {
				p.sendMessage(plugin.main+"��a�ơ�r �̹� �̳��� ������ ���˴��Դϴ�.");
				return false;
			}
			
			plugin.lureGui.openGUI(p);
		} catch (Exception e) {
			s.sendMessage(plugin.main+"�߸��� ��ɾ��Դϴ�. ��8[/�̳�]");
			
			if (plugin.getConfig().getBoolean("general.debug")) e.printStackTrace();
		}
		
		return false;
	}
}