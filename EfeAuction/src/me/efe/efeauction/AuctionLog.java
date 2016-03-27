package me.efe.efeauction;

import java.util.Date;
import java.util.UUID;

public class AuctionLog {
	private final UUID id;
	private final AuctionMaterial material;
	private final double price;
	private final Date date;
	
	public AuctionLog(UUID id, AuctionMaterial material, double price, Date date) {
		this.id = id;
		this.material = material;
		this.price = price;
		this.date = date;
	}
	
	public UUID getUniqueId() {
		return this.id;
	}
	
	public AuctionMaterial getMaterial() {
		return this.material;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public Date getDate() {
		return this.date;
	}
}
