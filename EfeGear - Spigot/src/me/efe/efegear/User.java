package me.efe.efegear;

import org.bukkit.entity.Player;

public class User {
	private String name;
	
	public User(Player p) {
		this.name = p.getName();
	}
	
	public User(String name) {
		this.name = name;
	}
	
	public String getToken() {
		return this.name;
	}
	
	public String getName() {
		return this.name;
	}
}