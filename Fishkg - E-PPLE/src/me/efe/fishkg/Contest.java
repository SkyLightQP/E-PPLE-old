package me.efe.fishkg;

import java.util.HashMap;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Contest {
	private static Fishkg plugin;
	private static RankMaker ranker = new RankMaker();
	private static BukkitTask task;
	
	private static boolean enabled;
	private static boolean mod_team;
	private static boolean mod_junk;
	
	public static HashMap<UUID, Team> teamMap = new HashMap<UUID, Team>();
	private static int red = 0;
	private static int blue = 0;
	
	public static void init(Fishkg fishkg) {
		plugin = fishkg;
		ranker.load();
	}
	
	public static void start(long tick) {
		enabled = true;
		
		if (tick > 0) task = new TimeTask().runTaskLater(plugin, tick);
	}
	
	public static void quit(boolean message) {
		enabled = false;
		if (task != null) task.cancel();
		
		if (message) {
			plugin.getServer().broadcastMessage(plugin.main+"���ô�ȸ�� ����Ǿ����ϴ�!");
			
			for (Player all : EfeUtils.player.getOnlinePlayers()) {
				sendStatus(all);
			}
			
			if (ranker.isExist(1)) {
				plugin.getServer().broadcastMessage(plugin.main+ranker.getOwnerName(1)+"��, ���ϵ帳�ϴ�!");
			} else {
				plugin.getServer().broadcastMessage(plugin.main+"�ƽ��Ե� 1���� ������ �ʾҽ��ϴ�.");
			}
		}
		
		ranker = new RankMaker();
	}
	
	public static void setModTeam(boolean team) {
		mod_team = team;
	}
	
	public static void setModJunk(boolean junk) {
		mod_junk = junk;
	}
	
	public static void sendStatus(CommandSender s) {
		if (mod_team) {
			s.sendMessage(plugin.main+"��cRed : "+red+" ��3Blue : "+blue);
			return;
		}
		
		if (!ranker.isExist(1)) {
			s.sendMessage(plugin.main+"���� �ƹ��� ���ø� ���� �ʾҽ��ϴ�.");
			return;
		}
		
		for (int i = 1; i <= plugin.getConfig().getInt("fish.announceRank"); i ++) {
			if (!ranker.isExist(i)) break;
			s.sendMessage(plugin.main+i+"�� : "+ranker.getOwnerName(i)+", ���� : "+ranker.getLength(i)+"cm");
		}
		
		if (s instanceof Player) {
			UUID id = ((Player) s).getUniqueId();
			if (!ranker.isExist(id)) return;
			
			s.sendMessage(plugin.main+"���� ���� : "+ranker.getRank(id)+"��, ���� : "+ranker.getLength(id)+"cm");
		}
	}
	
	public static void addScore(Team team, int amount) {
		if (team.equals(Team.RED)) {
			red += amount;
		} else {
			blue += amount;
		}
	}
	
	public static int getScore(Team team) {
		if (team.equals(Team.RED)) {
			return red;
		} else {
			return blue;
		}
	}
	
	public static void init() {
		ranker = new RankMaker();
		
		red = 0;
		blue = 0;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
	
	public static boolean isModTeam() {
		return mod_team;
	}
	
	public static boolean isModJunk() {
		return mod_junk;
	}
	
	public static RankMaker getRanker() {
		return ranker;
	}
	
	private static class TimeTask extends BukkitRunnable {
		
		@Override
		public void run() {
			Contest.quit(true);
		}
	}
	
	public enum Team {
		RED, BLUE
	}
}