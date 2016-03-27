package me.efe.fishkg;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/** 
 *  == Ranking System v2.3 ==
 *  
 *  Special thanks to Teledong for help
 */

public class RankMaker {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
	private PointComparator comparator = new PointComparator();
	private List<Point> points = new ArrayList<Point>();
	private Date startDate;
	
	public boolean isNewDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		
		return (cal.get(Calendar.DAY_OF_MONTH) != Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
	}
	
	public void reset() {
		this.points.clear();
	}
	
	public void setNewDate() {
		this.startDate = new Date();
	}
	
	public void load() {
		startDate = new Date();
		points.clear();
		
		FileConfiguration config = getConfig();
		
		for (String path : config.getKeys(false)) {
			points.add(new Point(config.getDouble(path), UUID.fromString(path)));
		}
		
		Collections.sort(points, comparator);
	}
	
	public void save() {
		FileConfiguration config = getConfig();
		
		for (String path : config.getKeys(false))
			config.set(path, null);
		
		for (Point point : points) {
			config.set(point.getOwner().toString(), point.getLength());
		}
		
		saveConfig(config);
	}
	
	public void removeOldData() {
		Date date = new Date();
		
		for (File file : new File("plugins/Fishkg").listFiles()) {
			if (!file.getName().equals("config.yml") && !file.getName().equals(format.format(date)+".yml")) {
				file.delete();
			}
		}
	}
	
	private FileConfiguration getConfig() {
		try {
			Date date = new Date();
			File file = new File("plugins/Fishkg/"+format.format(date)+".yml");
			
			if (!file.exists())
				file.createNewFile();
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			return config;
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private void saveConfig(FileConfiguration config) {
		try {
			Date date = new Date();
			File file = new File("plugins/Fishkg/"+format.format(date)+".yml");
			
			config.save(file);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void sort(double length, Player fisher) {
		boolean isOverlaped = false;
		
		for (int i = 0; i < points.size(); i ++) {
			if (points.get(i).getOwner().equals(fisher.getUniqueId())) {
				if (length > points.get(i).getLength()) {
					points.set(i, new Point(length, fisher.getUniqueId()));
				}
				isOverlaped = true;
			}
		}
		
		if (!isOverlaped) {
			points.add(new Point(length, fisher.getUniqueId()));
		}
		
		Collections.sort(points, comparator);
	}
	
	public UUID getOwner(int rank) {
		if (points.size() < rank) return null;
		
		return points.get(rank - 1).getOwner();
	}
	
	public String getOwnerName(int rank) {
		if (points.size() < rank) return null;
		
		return Bukkit.getOfflinePlayer(points.get(rank - 1).getOwner()).getName();
	}
	
	public double getLength(int rank) {
		if (points.size() < rank) return 0;
		
		return points.get(rank - 1).getLength();
	}
	
	public double getLength(UUID id) {
		for (Point point : points) {
			if (point.getOwner().equals(id)) return point.getLength();
		}
		
		return 0;
	}
	
	public boolean isExist(int rank) {
		return points.size() >= rank;
	}
	
	public boolean isExist(UUID id) {
		for (Point point : points) {
			if (point.getOwner().equals(id)) return true;
		}
		
		return false;
	}
	
	public int getRank(UUID id) {
		for (int i = 0; i < points.size(); i ++) {
			Point point = points.get(i);
			if (point.getOwner().equals(id)) return i + 1;
		}
		
		return 0;
	}
	
	private class PointComparator implements Comparator<Point> {
		
		@Override
		public int compare(Point arg0, Point arg1) {
			return (arg0.getLength() > arg1.getLength()) ? -1 : (arg0.getLength() < arg1.getLength()) ? 1 : 0;
		}
		
		@Override
		public Comparator<Point> reversed() {
			return null;
		}
		
		@Override
		public Comparator<Point> thenComparing(Comparator<? super Point> arg0) {
			return null;
		}
		
		@Override
		public <U extends Comparable<? super U>> Comparator<Point> thenComparing(
				Function<? super Point, ? extends U> arg0) {
			return null;
		}
		
		@Override
		public <U> Comparator<Point> thenComparing(
				Function<? super Point, ? extends U> arg0,
				Comparator<? super U> arg1) {
			return null;
		}
		
		@Override
		public Comparator<Point> thenComparingDouble(
				ToDoubleFunction<? super Point> arg0) {
			return null;
		}
		
		@Override
		public Comparator<Point> thenComparingInt(
				ToIntFunction<? super Point> arg0) {
			return null;
		}
		
		@Override
		public Comparator<Point> thenComparingLong(
				ToLongFunction<? super Point> arg0) {
			return null;
		}
	}
	
	public class Point {
		private double cm;
		private UUID owner;
		
		public Point(double length, UUID fisher) {
			this.cm = length;
			this.owner = fisher;
		}
		
		public double getLength() {
			return this.cm;
		}
		
		public UUID getOwner() {
			return this.owner;
		}
	}
}