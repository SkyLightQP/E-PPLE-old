package me.efe.efeauction;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class AuctionItem {
	private UUID id;
	private UUID sellerId;
	private double price;
	private Date date;
	
	public AuctionItem(UUID id, UUID sellerId, double price, Date date) {
		this.id = id;
		this.sellerId = sellerId;
		this.price = price;
		this.date = date;
	}
	
	public UUID getUniqueId() {
		return this.id;
	}
	
	public OfflinePlayer getSeller() {
		return Bukkit.getOfflinePlayer(sellerId);
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public Date getDate() {
		return this.date;
	}
}
