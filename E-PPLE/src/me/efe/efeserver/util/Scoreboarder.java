package me.efe.efeserver.util;

import java.util.HashMap;
import java.util.UUID;

import me.efe.efeisland.IslandUtils;
import me.efe.efemobs.EfeMobs;
import me.efe.efemobs.rudish.UserData;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.reform.Fatigue;
import me.efe.fishkg.Contest;
import me.efe.fishkg.RankMaker;
import me.efe.fishkg.events.PlayerFishkgEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Scoreboarder implements Listener {
	private static EfeServer plugin;
	private static HashMap<UUID, BukkitTask> tasks = new HashMap<UUID, BukkitTask>();
	private static HashMap<String, String> langKor = new HashMap<String, String>();
	private static HashMap<String, String> langEng = new HashMap<String, String>();
	
	public Scoreboarder(EfeServer pl) {
		plugin = pl;
		
		langKor.put("ocean", "郊陥");
		langEng.put("ocean", "Ocean");
		langKor.put("island_owner", "雫 爽昔");
		langEng.put("island_owner", "Island Owner");
		langKor.put("now_visiters", "薄仙 号庚切 呪");
		langEng.put("now_visiters", "Visiters");
		langKor.put("visiters", "誤");
		langEng.put("visiters", "");
		langKor.put("fatigue", "杷稽亀");
		langEng.put("fatigue", "Fatigue");
		langKor.put("my_rank", "鎧 授是");
		langEng.put("my_rank", "My Rank");
		langKor.put("now_ranking", "薄仙 粂天");
		langEng.put("now_ranking", "Ranking");
		langKor.put("floor", "薄仙 寵");
		langEng.put("floor", "Floor");
		langKor.put("soul", "社随");
		langEng.put("soul", "My Souls");
		langKor.put("", "");
		langEng.put("", "");
	}
	
	public static Scoreboard getDefaultBoard(Player p) {
		Scoreboard board = plugin.getServer().getScoreboardManager().getNewScoreboard();
		
		Team team1 = board.registerNewTeam("default_admin");
		team1.setCanSeeFriendlyInvisibles(true);
		team1.addEntry("Efe");
		team1.addEntry("Geuneul");
		team1.addEntry("horyu1234");
		team1.setPrefix("」dAdmin|」r ");
		
		Objective obj2 = board.registerNewObjective("default_sidebar", "dummy");
		obj2.setDisplayName("」6* 」c」lE-PPLE」7」l.」4」lkr」6 *");
		obj2.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj2.getScore(" ").setScore(14);
		obj2.getScore(" 」aWe're Back!").setScore(13);
		obj2.getScore("  ").setScore(12);
		obj2.getScore("」7----------------").setScore(11);
		obj2.getScore("=================").setScore(0);
		
		return board;
	}
	
	public static void updateObjectives(Player p) {
		if (tasks.containsKey(p.getUniqueId())) {
			tasks.get(p.getUniqueId()).cancel();
			tasks.remove(p.getUniqueId());
		}
		
		p.setScoreboard(getDefaultBoard(p));
		
		ApplicableRegionSet set = WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
		Objective obj = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		
		if (p.getWorld().equals(plugin.worldIsl)) {
			ProtectedRegion region = plugin.efeIsland.getIsleRegion(set);
			
			if (region == null) {
				obj.getScore("」0").setScore(10);
				obj.getScore("」9").setScore(9);
				obj.getScore("」b」l"+lang("ocean", p)).setScore(8);
				obj.getScore("」7").setScore(7);
				obj.getScore("」6").setScore(6);
				obj.getScore("」5").setScore(5);
				obj.getScore("」4").setScore(4);
				obj.getScore("」3").setScore(3);
				obj.getScore("」2").setScore(2);
				obj.getScore("」1").setScore(1);
			} else {
				obj.getScore("」0").setScore(10);
				obj.getScore("」b」l"+lang("island_owner", p)+":").setScore(9);
				obj.getScore(plugin.efeIsland.getIsleOwner(region).getName()).setScore(8);
				obj.getScore("」7").setScore(7);
				obj.getScore("」b」l"+lang("now_visiters", p)+":").setScore(6);
				obj.getScore(plugin.efeIsland.getVisiters(region).size()+lang("visiters", p)).setScore(5);
				obj.getScore("」4").setScore(4);
				obj.getScore("」3").setScore(3);
				obj.getScore("」2").setScore(2);
				obj.getScore("」1").setScore(1);
			}
		} else if (IslandUtils.isMineras(p.getLocation())) {
			
			obj.getScore("」0").setScore(10);
			obj.getScore("」9").setScore(9);
			obj.getScore("」b」l"+lang("fatigue", p)+":").setScore(8);
			obj.getScore((Fatigue.getFatigue(p)/5)+"%").setScore(7);
			obj.getScore("」6").setScore(6);
			obj.getScore("」5").setScore(5);
			obj.getScore("」4").setScore(4);
			obj.getScore("」3").setScore(3);
			obj.getScore("」2").setScore(2);
			obj.getScore("」1").setScore(1);
		} else if (IslandUtils.isAqu(p.getLocation())) {
			RankMaker ranker = Contest.getRanker();
			
			obj.getScore("」0").setScore(10);
			obj.getScore("」b」l"+lang("my_rank", p)+":").setScore(9);
			obj.getScore(substring(ranker.getRank(p.getUniqueId())+"是 」7["+ranker.getLength(p.getUniqueId())+"cm]", 16)).setScore(8);
			obj.getScore("」7").setScore(7);
			obj.getScore("」b」l"+lang("now_ranking", p)+":").setScore(6);
			if (ranker.isExist(1)) obj.getScore(substring(ranker.getLength(1)+"cm / "+ranker.getOwnerName(1), 16)).setScore(5);
			else obj.getScore(" -").setScore(5);
			if (ranker.isExist(2)) obj.getScore(substring(ranker.getLength(2)+"cm / "+ranker.getOwnerName(2), 16)).setScore(4);
			else obj.getScore("」4").setScore(4);
			if (ranker.isExist(3)) obj.getScore(substring(ranker.getLength(3)+"cm / "+ranker.getOwnerName(3), 16)).setScore(3);
			else obj.getScore("」3").setScore(3);
			obj.getScore("」2").setScore(2);
			obj.getScore("」1").setScore(1);
		} else if (IslandUtils.isRudish(p.getLocation())) {
			int floor = EfeMobs.getFloor(p.getLocation());
			
			obj.getScore("」b」l"+lang("floor", p)+":").setScore(10);
			obj.getScore((floor > 0) ? floor+"F" : "Lobby").setScore(9);
			obj.getScore("」8").setScore(8);
			obj.getScore("」b」l"+lang("soul", p)+":").setScore(7);
			obj.getScore(new UserData(p).getSoul()+"").setScore(6);
			obj.getScore("」5").setScore(5);
			obj.getScore("」4").setScore(4);
			obj.getScore("」3").setScore(3);
			obj.getScore("」2").setScore(2);
			obj.getScore("」1").setScore(1);
		} else {
			obj.getScore("」0").setScore(10);
			obj.getScore("」9").setScore(9);
			obj.getScore("」8").setScore(8);
			obj.getScore("」7").setScore(7);
			obj.getScore("」6").setScore(6);
			obj.getScore("」5").setScore(5);
			obj.getScore("」4").setScore(4);
			obj.getScore("」3").setScore(3);
			obj.getScore("」2").setScore(2);
			obj.getScore("」1").setScore(1);
		}
	}
	
	private static String lang(String key, Player p) {
		return Localizer.getLocale(p) == Localizer.KOREAN ? langKor.get(key) : langEng.get(key);
	}
	
	private static String substring(String str, int length) {
		return (str.length() > length) ? str.substring(0, length - 1) + "・" : str;
	}
	
	public static void message(Player p, String[] texts, int second) {
		if (tasks.containsKey(p.getUniqueId())) {
			tasks.get(p.getUniqueId()).cancel();
			tasks.remove(p.getUniqueId());
		}
		
		p.setScoreboard(getDefaultBoard(p));
		updateObjectives(p);
		
		Objective obj = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		
		if (p.getWorld().equals(plugin.worldIsl)) {
			obj.getScoreboard().resetScores("」3");
			obj.getScoreboard().resetScores("」2");
			
			obj.getScore(texts[0]).setScore(3);
			obj.getScore(texts[1]).setScore(2);
			
		} else if (IslandUtils.isMineras(p.getLocation())) {
			obj.getScoreboard().resetScores("」4");
			obj.getScoreboard().resetScores("」3");
			
			obj.getScore(texts[0]).setScore(4);
			obj.getScore(texts[1]).setScore(3);
		} else if (IslandUtils.isRudish(p.getLocation())) {
			obj.getScoreboard().resetScores("」3");
			obj.getScoreboard().resetScores("」2");
			
			obj.getScore(texts[0]).setScore(3);
			obj.getScore(texts[1]).setScore(2);
		} else if (IslandUtils.isAqu(p.getLocation())) {
			obj.getScoreboard().resetScores("」2");
			obj.getScoreboard().resetScores("」1");
			
			obj.getScore(texts[0]).setScore(2);
			obj.getScore(texts[1]).setScore(1);
		} else if (IslandUtils.isNimbus(p.getLocation())) {
			obj.getScoreboard().resetScores("」4");
			obj.getScoreboard().resetScores("」3");
			
			obj.getScore(texts[0]).setScore(8);
			obj.getScore(texts[1]).setScore(7);
		} else {
			obj.getScoreboard().resetScores("」8");
			obj.getScoreboard().resetScores("」7");
			
			obj.getScore(texts[0]).setScore(8);
			obj.getScore(texts[1]).setScore(7);
		}
		
		tasks.put(p.getUniqueId(), new TimeTask(p).runTaskLater(plugin, second * 20));
	}
	
	private static class TimeTask extends BukkitRunnable {
		private Player p;
		
		public TimeTask(Player p) {
			this.p = p;
		}
		
		public void run() {
			p.setScoreboard(getDefaultBoard(p));
			updateObjectives(p);
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		
		p.setScoreboard(getDefaultBoard(p));
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				updateObjectives(p);
			}
		}, 20L);
	}
	
	@EventHandler
	public void fish(final PlayerFishkgEvent e) {
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				for (Player p : e.getPlayer().getWorld().getPlayers()) {
					if (IslandUtils.isAqu(p.getLocation())) {
						updateObjectives(p);
					}
				}
			}
		}, 1L);
	}
}