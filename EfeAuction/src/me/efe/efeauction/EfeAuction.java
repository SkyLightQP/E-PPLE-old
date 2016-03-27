package me.efe.efeauction;

import java.util.UUID;

import me.efe.efeauction.listeners.AuctionGUI;
import me.efe.efeauction.listeners.AuctionMainGUI;
import me.efe.efeauction.listeners.AuctionStatusGUI;
import me.efe.efegear.util.VaultHooker;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.mysql.SQLManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EfeAuction extends JavaPlugin {
	private static EfeAuction instance;
	public VaultHooker vault;
	public SQLManager sqlManager;
	public AuctionGUI auctionGUI;
	public AuctionMainGUI auctionMainGUI;
	public AuctionStatusGUI auctionStatusGUI;
	public AuctionLogHandler auctionLogHandler;
	
	@Override
	public void onEnable() {
		instance = this;
		
		saveDefaultConfig();
		
		this.vault = EfeServer.getInstance().vault;
		this.sqlManager = EfeServer.getInstance().sqlManager;
		this.auctionLogHandler = new AuctionLogHandler(this);
		
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(this.auctionGUI = new AuctionGUI(this), this);
		manager.registerEvents(this.auctionMainGUI = new AuctionMainGUI(this), this);
		manager.registerEvents(this.auctionStatusGUI = new AuctionStatusGUI(this), this);
		
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(MarketerTrait.class).withName("Marketer"));
		
		getLogger().info(getDescription().getFullName()+" has been enabled!");
	}
	
	public static EfeAuction getInstance() {
		return instance;
	}
	
	@Override
	public void onDisable() {
		getLogger().info(getDescription().getFullName()+" has been disabled.");
	}
	
	public ItemStack getItemStack(UUID id) {
		return getConfig().getItemStack(id.toString()).clone();
	}
	
	public void setItemStack(UUID id, ItemStack itemStack) {
		getConfig().set(id.toString(), (itemStack == null) ? null : itemStack.clone());
		saveConfig();
	}
}
