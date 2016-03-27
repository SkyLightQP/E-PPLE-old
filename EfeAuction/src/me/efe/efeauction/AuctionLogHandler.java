package me.efe.efeauction;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.bukkit.inventory.ItemStack;

public class AuctionLogHandler {
	private EfeAuction plugin;
	private LogComparator comparator = new LogComparator();
	
	public AuctionLogHandler(EfeAuction plugin) {
		this.plugin = plugin;
	}
	
	public double getAveragePrice(AuctionMaterial material) {
		double averagePrice = 0.0D;
		
		List<AuctionLog> logList = plugin.sqlManager.getAuctionLogList(material);
		
		if (!logList.isEmpty()) {
			for (AuctionLog log : logList) {
				averagePrice += log.getPrice();
			}
			
			averagePrice /= logList.size();
		}
		
		return averagePrice;
	}
	
	public void addLog(ItemStack itemStack, double price) {
		AuctionMaterial material = AuctionMaterial.getAuctionMaterial(itemStack);
		
		List<AuctionLog> logList = plugin.sqlManager.getAuctionLogList(material);
		
		if (logList.size() >= 10) {
			Collections.sort(logList, comparator);
			
			plugin.sqlManager.removeAuctionLog(logList.get(0));
		}
		
		plugin.sqlManager.addAuctionLog(new AuctionLog(UUID.randomUUID(), material, price, new Date()));
	}
	
	private class LogComparator implements Comparator<AuctionLog> {
		
		@Override
		public int compare(AuctionLog arg0, AuctionLog arg1) {
			return (arg0.getDate().after(arg1.getDate())) ? 1 : -1;
		}
		
		@Override
		public Comparator<AuctionLog> reversed() {
			return null;
		}
		
		@Override
		public Comparator<AuctionLog> thenComparing(Comparator<? super AuctionLog> arg0) {
			return null;
		}
		
		@Override
		public <U extends Comparable<? super U>> Comparator<AuctionLog> thenComparing(
				Function<? super AuctionLog, ? extends U> arg0) {
			return null;
		}
		
		@Override
		public <U> Comparator<AuctionLog> thenComparing(
				Function<? super AuctionLog, ? extends U> arg0,
				Comparator<? super U> arg1) {
			return null;
		}
		
		@Override
		public Comparator<AuctionLog> thenComparingDouble(
				ToDoubleFunction<? super AuctionLog> arg0) {
			return null;
		}
		
		@Override
		public Comparator<AuctionLog> thenComparingInt(
				ToIntFunction<? super AuctionLog> arg0) {
			return null;
		}
		
		@Override
		public Comparator<AuctionLog> thenComparingLong(
				ToLongFunction<? super AuctionLog> arg0) {
			return null;
		}
	}
}
