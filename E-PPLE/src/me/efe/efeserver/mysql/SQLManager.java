package me.efe.efeserver.mysql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import me.efe.efeauction.AuctionItem;
import me.efe.efeauction.AuctionLabel;
import me.efe.efeauction.AuctionLog;
import me.efe.efeauction.AuctionMaterial;

public class SQLManager {
	private DataInterface driver;
	private Connection connection;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public void connect() {
		String host = "e-net.kr";
		String port = "3306";
		String database = "epple";
		String username = "epple";
		String password = "delicious";

		try {
			this.driver = new MySQL(host, port, username, password, database);
			this.driver.connect();
			this.connection = this.driver.getConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private Date convert(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH mm");
		
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	private String convert(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH mm");
		
		return dateFormat.format(date);
	}
	
	public List<AuctionItem> getAuctionItems(AuctionLabel label) {
		List<AuctionItem> list = new ArrayList<AuctionItem>();
		
		try {
			Statement state = this.connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT `uuid` , `seller` , `price` , `date` FROM `auction_list` WHERE `label` = '"+label.toString()+"';");
			
			while (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				UUID sellerId = UUID.fromString(rs.getString(2));
				Date date = convert(rs.getString(4));
				
				AuctionItem item = new AuctionItem(id, sellerId, rs.getDouble(3), date);
				list.add(item);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("auction", ex.getMessage());
		}
		
		return list;
	}
	
	public List<AuctionItem> getAuctionItems(AuctionMaterial material) {
		List<AuctionItem> list = new ArrayList<AuctionItem>();
		
		try {
			Statement state = this.connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT `uuid` , `seller` , `price` , `date` FROM `auction_list` WHERE `material` = '"+material.toString()+"';");
			
			while (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				UUID sellerId = UUID.fromString(rs.getString(2));
				Date date = convert(rs.getString(4));
				
				AuctionItem item = new AuctionItem(id, sellerId, rs.getDouble(3), date);
				list.add(item);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("auction", ex.getMessage());
		}
		
		return list;
	}
	
	public List<AuctionItem> getAuctionItems(AuctionLabel label, AuctionMaterial material) {
		List<AuctionItem> list = new ArrayList<AuctionItem>();
		
		try {
			Statement state = this.connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT `uuid` , `seller` , `price` , `date` FROM `auction_list` WHERE `label` = '"+label.toString()+"'; AND `material` = '"+material.toString()+"';");
			
			while (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				UUID sellerId = UUID.fromString(rs.getString(2));
				Date date = convert(rs.getString(4));
				
				AuctionItem item = new AuctionItem(id, sellerId, rs.getDouble(3), date);
				list.add(item);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("auction", ex.getMessage());
		}
		
		return list;
	}
	
	public List<AuctionItem> getAuctionItems(OfflinePlayer seller) {
		List<AuctionItem> list = new ArrayList<AuctionItem>();
		
		try {
			Statement state = this.connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT `uuid` , `seller` , `price` , `date` FROM `auction_list` WHERE `seller` = '"+seller.getUniqueId().toString()+"';");
			
			while (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				UUID sellerId = UUID.fromString(rs.getString(2));
				Date date = convert(rs.getString(4));
				
				AuctionItem item = new AuctionItem(id, sellerId, rs.getDouble(3), date);
				list.add(item);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("auction", ex.getMessage());
		}
		
		return list;
	}
	
	public void addAuctionItem(AuctionItem item, AuctionMaterial material) {
		try {
			PreparedStatement prestate = this.connection.prepareStatement("INSERT INTO auction_list VALUES (?, ?, ?, ?, ?, ?)");
			
			prestate.setString(1, item.getUniqueId().toString());
			prestate.setString(2, item.getSeller().getUniqueId().toString());
			prestate.setDouble(3, item.getPrice());
			prestate.setString(4, material.getLabel().toString());
			prestate.setString(5, material.toString());
			prestate.setString(6, convert(item.getDate()));
			
			prestate.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("auction", ex.getMessage());
		}
	}
	
	public void removeAuctionItem(AuctionItem item) {
		try {
			PreparedStatement prestate = this.connection.prepareStatement("DELETE FROM `auction_list` WHERE `uuid` = '"+item.getUniqueId().toString()+"';");
			
			prestate.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("auction", ex.getMessage());
		}
	}
	
	public List<AuctionLog> getAuctionLogList(AuctionMaterial material) {
		List<AuctionLog> list = new ArrayList<AuctionLog>();
		
		try {
			Statement state = this.connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT `uuid` , `price` , `date` FROM `auction_log` WHERE `material` = '"+material.name()+"';");
			
			while (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				double price = rs.getDouble(2);
				Date date = convert(rs.getString(3));
				
				list.add(new AuctionLog(id, material, price, date));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("auction", ex.getMessage());
		}
		
		return list;
	}
	
	public void addAuctionLog(AuctionLog log) {
		try {
			PreparedStatement prestate = this.connection.prepareStatement("INSERT INTO auction_log VALUES (?, ?, ?, ?)");
			
			prestate.setString(1, log.getUniqueId().toString());
			prestate.setString(2, log.getMaterial().name());
			prestate.setDouble(3, log.getPrice());
			prestate.setString(4, convert(log.getDate()));
			
			prestate.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("auction", ex.getMessage());
		}
	}
	
	public void removeAuctionLog(AuctionLog log) {
		try {
			PreparedStatement prestate = this.connection.prepareStatement("DELETE FROM `auction_log` WHERE `uuid` = '"+log.getUniqueId()+"';");
			
			prestate.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("auction", ex.getMessage());
		}
	}
	
	public HashMap<UUID, Integer> loadCash() {
		HashMap<UUID, Integer> map = new HashMap<UUID, Integer>();
		
		try {
			Statement state = this.connection.createStatement();
			ResultSet rs = state.executeQuery("SELECT UUID, Balance FROM `cash`");
			
			while (rs.next()) {
				map.put(UUID.fromString(rs.getString(1)), rs.getInt(2));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("cash", ex.getMessage());
		}
		
		return map;
	}
	
	public void insertCash(UUID id, int amount) {
		try {
			PreparedStatement prestate = this.connection.prepareStatement("INSERT INTO cash VALUES (?, ?)");
			
			prestate.setString(1, id.toString());
			prestate.setInt(2, amount);
			
			prestate.executeUpdate();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("cash", ex.getMessage());
		}
	}
	
	public void updateCash(UUID id, int amount) {
		try {
			PreparedStatement prestate = this.connection.prepareStatement("UPDATE `cash` SET balance="+amount+" WHERE uuid='"+id.toString()+"'");
			
			prestate.executeUpdate();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			log("cash", ex.getMessage());
		}
	}
	
	public void log(String name, String... message) {
		try {
			File file = new File("logsSQL/"+name+".txt");
			File folder = new File("logsSQL");
			
			if (!file.exists()) {
				folder.mkdir();
				file.createNewFile();
			}
			
			String date = format.format(new Date());
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			
			for (String text : message) {
				bw.write(date + ": " + text+"\r\n");
			}
			
			bw.flush();
			bw.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}