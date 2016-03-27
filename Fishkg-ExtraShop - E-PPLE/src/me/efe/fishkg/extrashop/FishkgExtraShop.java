package me.efe.fishkg.extrashop;

import me.efe.efecore.hook.VaultHooker;
import me.efe.fishkg.Fishkg;
import me.efe.fishkg.extrashop.citizens.CitizensHooker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FishkgExtraShop extends JavaPlugin {
	public Fishkg fishkg;
	public String main;
	public int guiRow;
	
	public FishkgExtraShopListener listener;
	public VaultHooker vault;
	
	@Override
	public void onEnable() {
		Plugin plugin = getServer().getPluginManager().getPlugin("Fishkg");
		
		if (plugin == null) {
			getLogger().warning("[Fishkg-ExtraShop] Fishkg�� �߰ߵ��� �ʾҽ��ϴ�. �÷������� ��Ȱ��ȭ�˴ϴ�.");
			
			this.setEnabled(false);
			return;
		}
		
		fishkg = (Fishkg) plugin;
		
		if (!fishkg.getConfig().getBoolean("shop.enable")) {
			getLogger().warning("[Fishkg-ExtraShop] Fishkg�� ���� ����� ����ϰ� ���� �ʽ��ϴ�. �÷������� ��Ȱ��ȭ�˴ϴ�.");
			
			this.setEnabled(false);
			return;
		}
		
		saveDefaultConfig();
		
		if (getConfig().getBoolean("easy-sell")) {
			getServer().getPluginManager().registerEvents(listener = new FishkgExtraShopListener(this), this);
			
			fishkg.addon_extraShop = true;
		}
		
		if (getConfig().getBoolean("citizens")) {
			if (!CitizensHooker.exists()) {
				getLogger().warning("[Fishkg-ExtraShop] Citizens�� �߰ߵ��� �ʾҽ��ϴ�. ���� ����� ��Ȱ��ȭ�˴ϴ�.");
			} else {
				CitizensHooker.hook();
			}
		}
		
		if (fishkg.getConfig().getInt("shop.emerald") == 0) {
			if (!VaultHooker.exists() || !VaultHooker.hasEconomy()) {
				this.setEnabled(false);
				return;
			}
			
			vault = new VaultHooker();
			vault.setupEconomy();
		}
		
		main = fishkg.main;
		guiRow = getConfig().getInt("gui-row");
		
		getLogger().info(this.getDescription().getFullName()+" has been enabled!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info(this.getDescription().getFullName()+" has been disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("���û���")) {
			try {
				if (!(sender instanceof Player)) {
					sender.sendMessage(main+"�÷��̾ ��� ������ ��ɾ��Դϴ�.");
					return false;
				}
				
				Player p = (Player) sender;
				
				if (!p.hasPermission("fishkg.cmdshop")) {
					p.sendMessage(main+"������ �����ϴ�. ��8[fishkg.cmdshop]");
					return false;
				}
				
				fishkg.shopGui.openGUI(p);
			} catch (Exception e) {
				sender.sendMessage(main+"�߸��� ��ɾ��Դϴ�.");
			}
		}
		
		return false;
	}
}