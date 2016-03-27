package me.efe.titlemaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

public class TitleManager {
	protected static boolean autoMain;
	
	public static String getMainTitle(OfflinePlayer p) {
		HashMap<UUID, Integer> map = getMainTitles();
		if (!map.containsKey(p.getUniqueId())) {
			if (autoMain) {
				List<String> list = getTitles(p);
				return !list.isEmpty() ? list.get(0) : null;
			} else {
				return null;
			}
		}
		
		List<String> list = getTitles(p);
		int index = map.get(p.getUniqueId());
		
		return (list.size() > index) ? list.get(index) : null;
	}
	
	public static void setMainTitle(OfflinePlayer p, int index) {
		HashMap<UUID, Integer> map = getMainTitles();
		if (index == -1) {
			if (!map.containsKey(p.getUniqueId())) return;
			
			map.remove(p.getUniqueId());
		} else {
			map.put(p.getUniqueId(), index);
		}
		
		setMainTitles(map);
	}
	
	public static void setMainTitles(HashMap<UUID, Integer> map) {
		File f = new File("plugins/TitleMaker/main");
		File folder = new File("plugins/TitleMaker");
		try {
			if (!f.exists()) {
				folder.mkdir();
				f.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
			for (UUID arg : map.keySet()) {
				bw.write(arg.toString()+","+map.get(arg)+"\n");
			}
			
			bw.flush();
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static HashMap<UUID, Integer> getMainTitles() {
		File f = new File("plugins/TitleMaker/main");
		File folder = new File("plugins/TitleMaker");
		HashMap<UUID, Integer> map = new HashMap<UUID, Integer>();
		
		try {
			if (!f.exists()) {
				folder.mkdir();
				f.createNewFile();
				return map;
			}
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			String l;
			while ((l = br.readLine()) != null) {
				String[] args = l.split(",");
				if (args.length != 2) continue;
				map.put(UUID.fromString(args[0]), Integer.parseInt(args[1]));
			}
			
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return map;
	}
	
	public static List<String> getTitles(OfflinePlayer p) {
		File f = new File("plugins/TitleMaker/Titles/"+p.getUniqueId().toString());
		File folder = new File("plugins/TitleMaker");
		File folder2 = new File("plugins/TitleMaker/Titles");
		List<String> value = new ArrayList<String>();
		
		try {
			if (!f.exists()) {
				folder.mkdir();
				folder2.mkdir();
				f.createNewFile();
				return value;
			}
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s;
			while ((s = br.readLine()) != null) {
				value.add(s);
			}
			
			br.close();
			return value;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return value;
	}
	
	public static void setTitles(OfflinePlayer p, List<String> titles) {
		File f = new File("plugins/TitleMaker/Titles/"+p.getUniqueId().toString());
		File folder = new File("plugins/TitleMaker");
		File folder2 = new File("plugins/TitleMaker/Titles");
		
		try {
			if (!f.exists()) {
				folder.mkdir();
				folder2.mkdir();
				f.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (String title : titles) {
				bw.append(title+"\n");
			}
			
			bw.flush();
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}