package me.efe.efemobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.libraryaddict.disguise.DisguiseAPI;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Expedition {
	private List<Player> members;
	private int floor;
	private int channel;
	private Date startDate;
	private BukkitTask task;
	private Object data = null;
	
	public Expedition(List<Player> members, int floor, int channel, BukkitTask task) {
		this.members = new ArrayList<Player>(members);
		this.floor = floor;
		this.channel = channel;
		this.startDate = Calendar.getInstance().getTime();
		this.task = task;
	}
	
	public List<Player> getMembers() {
		return this.members;
	}
	
	public void kick(Player p) {
		members.remove(p);
		
		DisguiseAPI.undisguiseToAll(p);
	}
	
	public void broadcast(String message) {
		for (Player m : members) {
			m.sendMessage(message);
		}
	}
	
	public int getFloor() {
		return this.floor;
	}
	
	public int getChannel() {
		return this.channel;
	}
	
	public String getPlayTime() {
		Date now = Calendar.getInstance().getTime();
		
		long diff = now.getTime() - startDate.getTime();
		
		return (int) (diff / (60 * 1000) % 60) + "Ка " + (int) (diff / 1000 % 60) + "УЪ";
	}
	
	public BukkitTask getTask() {
		return this.task;
	}
	
	public Object getData() {
		return this.data;
	}
	
	public void cancelTask() {
		if (task == null) return;
		
		this.task.cancel();
		this.task = null;
	}
	
	public void setData(Object obj) {
		this.data = obj;
	}
}