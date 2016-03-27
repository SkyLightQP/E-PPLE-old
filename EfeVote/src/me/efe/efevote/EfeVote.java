package me.efe.efevote;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.efe.efeserver.PlayerData;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EfeVote extends JavaPlugin {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private WheelOfFortuneGUI wheelOfFortuneGUI;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();

		getServer().getPluginManager().registerEvents(new VoteListener(this), this);
		getServer().getPluginManager().registerEvents(wheelOfFortuneGUI = new WheelOfFortuneGUI(this), this);
		
		getLogger().info(getDescription().getFullName()+" has been enabled!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info(getDescription().getFullName()+" has been disabled.");
	}
	
	public boolean canVote(OfflinePlayer player) {
		String now = dateFormat.format(new Date());
		String date = getConfig().getString("date");
		
		if (!now.equals(date)) {
			for (String path : getConfig().getKeys(false)) {
				getConfig().set(path, null);
			}
			
			getConfig().set("date", now);
			
			return true;
		}
		
		return !getConfig().contains(player.getUniqueId().toString());
	}
	
	public void setVote(OfflinePlayer player) {
		String now = dateFormat.format(new Date());
		String date = getConfig().getString("date");
		
		if (!now.equals(date)) {
			for (String path : getConfig().getKeys(false)) {
				getConfig().set(path, null);
			}
			
			getConfig().set("date", now);
		}
		
		getConfig().set(player.getUniqueId().toString(), true);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("�ٿ�������") || label.equalsIgnoreCase("wheeloffortune")) {
			Player player = (Player) sender;
			PlayerData data = PlayerData.get(player);
			
			if (data.getWheelOfFortune() > 0) {
				this.wheelOfFortuneGUI.openGUI(player);
				
				player.sendMessage("��a�ơ�r �� ���� ������ �����մϴ�!");
			} else {
				player.sendMessage("��a�ơ�r ������ ���� ��ȸ�� ��� �����߽��ϴ�.");
				player.sendMessage("��a�ơ�r ���ϵ� E-PPLE�� ��õ�ؼ� �� ���� ���ῡ �����غ�����!");
			}
		}
		
		return false;
	}
}
