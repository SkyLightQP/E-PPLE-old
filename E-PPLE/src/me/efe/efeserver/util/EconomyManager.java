package me.efe.efeserver.util;

import org.bukkit.OfflinePlayer;

import me.efe.efeserver.EfeServer;

import com.horyu1234.horyulogger.HoryuLogger;
import com.horyu1234.horyulogger.LogManager;

public class EconomyManager {
	private static EfeServer plugin;
	private static LogManager log;
	
	static {
		plugin = EfeServer.getInstance();
		log = HoryuLogger.getInstance().getLogManager();
	}
	
	public static double getBalance(OfflinePlayer player) {
		return plugin.vault.getBalance(player);
	}
	
	public static void deposit(OfflinePlayer player, double amount, String reason) {
		plugin.vault.give(player, amount);
		log.logEco(player, amount, "Server", reason);
	}
	
	public static void deposit(OfflinePlayer player, double amount, OfflinePlayer target, String reason) {
		plugin.vault.give(player, amount);
		log.logEco(player, amount, target.getUniqueId().toString(), reason);
	}
	
	public static void withdraw(OfflinePlayer player, double amount, String reason) {
		plugin.vault.take(player, amount);
		log.logEco(player, -amount, "Server", reason);
	}
	
	public static void withdraw(OfflinePlayer player, double amount, OfflinePlayer target, String reason) {
		plugin.vault.take(player, amount);
		log.logEco(player, -amount, target.getUniqueId().toString(), reason);
	}
}
