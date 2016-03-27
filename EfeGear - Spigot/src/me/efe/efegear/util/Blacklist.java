package me.efe.efegear.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Blacklist {
	private static HashMap<String, BlackData> map;
	
	public static void init() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��_HH��mm��ss��");
		
		map = new HashMap<String, BlackData>();
		
		for (String type : new String[]{
				"http://list.nickname.mc-blacklist.kr/", 
				"http://list.uuid.mc-blacklist.kr/", 
				"http://list.ip.mc-blacklist.kr/"}) {
			
			try {
				URL url = new URL(type);
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setDoOutput(true);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
				br.readLine();
				
				String line;
				while ((line = br.readLine()) != null) {
					for (String value : line.split("</br>")) {
						if (value.isEmpty() || value.equals(" ") || value.startsWith("ban_date | ")) continue;
						
						String[] data = value.split(" \\| ");
						String date = data[0];
						String ban = data[1];
						String reason = data[2].replaceAll("_", " ");
						String punisher = data[3];
						
						map.put(ban, new BlackData(format.parse(date), reason, punisher));
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		try {
			URL url = new URL("http://ip.mc-blacklist.kr/");
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoOutput(true);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
			String ip = br.readLine().substring(1);
			
			if (map.containsKey(ip)) {
				Bukkit.getConsoleSender().sendMessage("��c#============================#");
				Bukkit.getConsoleSender().sendMessage("������ ������ ������Ʈ�� ��ϵǾ�");
				Bukkit.getConsoleSender().sendMessage("�÷����ο� ���� ������ ���ѵ˴ϴ�.");
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage("��4����: ");
				Bukkit.getConsoleSender().sendMessage("  \""+map.get(ip).getReason()+"\"");
				Bukkit.getConsoleSender().sendMessage("��c#============================#");
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Bukkit.shutdown();
				
				return;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static boolean contains(OfflinePlayer player) {
		if (map.containsKey(player.getUniqueId().toString())) return true;
		if (map.containsKey(player.getName())) return true;
		
		if (player.isOnline()) {
			Player p = player.getPlayer();
			
			if (map.containsKey(p.getAddress().getAddress().getHostAddress())) {
				return true;
			}
		}
		
		return false;
	}
	
	public static BlackData getBlackData(OfflinePlayer player) {
		if (map.containsKey(player.getUniqueId().toString())) return map.get(player.getUniqueId().toString());
		if (map.containsKey(player.getName())) return map.get(player.getName());
		
		if (player.isOnline()) {
			String ip = player.getPlayer().getAddress().getAddress().getHostAddress();
			
			if (map.containsKey(ip)) {
				return map.get(ip);
			}
		}
		
		return null;
	}
	
	public static void kick(Player player) {
		player.kickPlayer("��7��l[Blacklist]��r\n��8�� ����Ʈ�� ��ϵ� ������Դϴ�!��r\n\n��c��l\"��4"+getBlackData(player).getReason()+"��c��l\"��r");
	}
	
	public static class BlackData {
		private final Date date;
		private final String reason;
		private final String punisher;
		
		public BlackData(Date date, String reason, String punisher) {
			this.date = date;
			this.reason = reason;
			this.punisher = punisher;
		}
		
		public Date getDate() {
			return this.date;
		}
		
		public String getReason() {
			return this.reason;
		}
		
		public String getPunisher() {
			return this.punisher;
		}
	}
}