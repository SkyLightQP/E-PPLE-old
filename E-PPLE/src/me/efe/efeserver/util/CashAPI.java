package me.efe.efeserver.util;

import java.util.HashMap;
import java.util.UUID;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.mysql.SQLManager;

import org.bukkit.OfflinePlayer;

public class CashAPI {
	private static HashMap<UUID, Integer> cashMap = new HashMap<UUID, Integer>();
	private static SQLManager manager;
	
	public static void update() {
		manager = EfeServer.getInstance().sqlManager;
		
		cashMap = manager.loadCash();
	}
	
	public static int getBalance(OfflinePlayer player) {
		UUID id = player.getUniqueId();
		
		return cashMap.containsKey(id) ? cashMap.get(id) : 0;
	}
	
	public static boolean hasEnough(OfflinePlayer player, int amount) {
		UUID id = player.getUniqueId();
		
		return cashMap.containsKey(id) && cashMap.get(id) >= amount;
	}
	
	public static void deposit(OfflinePlayer player, int amount) {
		UUID id = player.getUniqueId();
		int balance = cashMap.containsKey(id) ? cashMap.get(id) : 0;
		
		balance += amount;
		
		if (cashMap.containsKey(id))
			manager.updateCash(id, balance);
		else
			manager.insertCash(id, balance);
		
		cashMap.put(id, balance);
		manager.log("cash", amount + " coins has been deposited to " + player.getName() + " (" + id.toString() + ").");
	}
	
	public static void withdraw(OfflinePlayer player, int amount) {
		UUID id = player.getUniqueId();
		int balance = cashMap.containsKey(id) ? cashMap.get(id) : 0;
		
		balance -= amount;
		
		if (cashMap.containsKey(id))
			manager.updateCash(id, balance);
		else
			manager.insertCash(id, balance);
		
		cashMap.put(id, balance);
		manager.log("cash", amount + " coins of " + player.getName() + " (" + id.toString() + ") has been withdrow.");
	}
	
	public static void log(String log) {
		manager.log("cash", log);
	}
	
	public static void logPurchase(OfflinePlayer player, String item, int price) {
		manager.log("cash", player.getName() + " has bought the item called '" + item + "' ("+price+" coins).");
	}
}