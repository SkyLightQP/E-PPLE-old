package me.efe.unlimitedrpg.stat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

import me.efe.efegear.util.Token;
import me.efe.unlimitedrpg.UnlimitedRPG;

public class StatAPI {
	private static UnlimitedRPG plugin = UnlimitedRPG.getInstance();
	
	public static int[] getStat(OfflinePlayer p) {
		File f = new File(plugin.getDataFolder(), "/Stats/"+Token.getToken(p)+".txt");
		File folder = new File(plugin.getDataFolder(), "/Stats");
		int[] stat = {0, 0, 0, 0, 0};
		
		try {
			if (!f.exists()) {
				folder.mkdir();
				f.createNewFile();
				setStat(p, stat);
				return stat;
			}
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			List<Integer> list = new ArrayList<Integer>();
			
			String s;
			while ((s = br.readLine()) != null) {
				list.add(Integer.parseInt(s));
			}
			
			br.close();
			
			for (int i = 0; i < 5; i ++) {
				stat[i] = list.get(i);
			}
			
			return stat;
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
		
		return stat;
	}
	
	public static void setStat(OfflinePlayer p, int[] stat) {
		File f = new File(plugin.getDataFolder(), "/Stats/"+Token.getToken(p)+".txt");
		File folder = new File(plugin.getDataFolder(), "/Stats");
		
		try {
			if (!f.exists()) {
				folder.mkdir();
				f.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.append(stat[0]+"\r\n"+stat[1]+"\r\n"+stat[2]+"\r\n"+stat[3]+"\r\n"+stat[4]);
			bw.flush();
			bw.close();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
	
	public static int getSP(OfflinePlayer p) {
		return getStat(p)[0];
	}
	
	public static int getSTR(OfflinePlayer p) {
		return getStat(p)[1];
	}
	
	public static int getACC(OfflinePlayer p) {
		return getStat(p)[2];
	}
	
	public static int getDEF(OfflinePlayer p) {
		return getStat(p)[3];
	}
	
	public static int getDEX(OfflinePlayer p) {
		return getStat(p)[4];
	}
	
	public static void setSP(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[0] = amount;
		setStat(p, stat);
	}
	
	public static void setSTR(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[1] = amount;
		setStat(p, stat);
	}
	
	public static void setACC(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[2] = amount;
		setStat(p, stat);
	}
	
	public static void setDEF(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[3] = amount;
		setStat(p, stat);
	}
	
	public static void setDEX(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[4] = amount;
		setStat(p, stat);
	}
	
	public static void editSP(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[0] += amount;
		
		if (stat[0] < 0) stat[0] = 0;
		
		setStat(p, stat);
	}
	
	public static void editSTR(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[1] += amount;
		
		if (stat[1] < 0) stat[1] = 0;
		
		setStat(p, stat);
	}
	
	public static void editACC(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[2] += amount;

		if (stat[2] < 0) stat[2] = 0;
		
		setStat(p, stat);
	}
	
	public static void editDEF(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[3] += amount;

		if (stat[3] < 0) stat[3] = 0;
		
		setStat(p, stat);
	}
	
	public static void editDEX(OfflinePlayer p, int amount) {
		int[] stat = getStat(p);
		stat[4] += amount;

		if (stat[4] < 0) stat[4] = 0;
		
		setStat(p, stat);
	}
}