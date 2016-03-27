package me.efe.efegear.util;

import me.efe.efeserver.EfeServer;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHooker {
	public Economy economy = null;
	
	public static boolean exist() {
		return Bukkit.getPluginManager().getPlugin("Vault") != null;
	}
	
	public static boolean hasEconomy() {
		RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		return provider != null;
	}
	
	public void setupEconomy() {
		if (!exist() || !hasEconomy()) return;
		RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		this.economy = provider.getProvider();
	}
	
	public void give(OfflinePlayer p, double amount) {
		economy.depositPlayer(p, amount);
		
		if (p.isOnline())
			EfeServer.getInstance().extraListener.sendTabTitle(p.getPlayer());
	}
	
	public void take(OfflinePlayer p, double amount) {
		economy.withdrawPlayer(p, amount);
		
		if (p.isOnline())
			EfeServer.getInstance().extraListener.sendTabTitle(p.getPlayer());
	}
	
	public double getBalance(OfflinePlayer p) {
		try {
			return economy.hasAccount(p) ? economy.getBalance(p) : 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	
	public boolean hasEnough(OfflinePlayer p, double amount) {
		
		return economy.hasAccount(p) && economy.has(p, amount);
	}
}