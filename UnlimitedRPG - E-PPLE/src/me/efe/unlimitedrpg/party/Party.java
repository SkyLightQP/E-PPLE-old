package me.efe.unlimitedrpg.party;

import java.util.ArrayList;
import java.util.List;

import me.efe.efegear.util.Token;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Party {
	private String name;
	private String owner;
	private List<String> members = new ArrayList<String>();
	
	public Party(OfflinePlayer owner, String name) {
		this.name = name;
		this.owner = Token.getToken(owner);
		this.members.add(this.owner);
	}
	
	public String getName() {
		return this.name;
	}
	
	public OfflinePlayer getOwner() {
		return Token.getOfflinePlayer(owner);
	}
	
	public boolean isOwner(OfflinePlayer p) {
		return getOwner().equals(p);
	}
	
	public List<OfflinePlayer> getMembers() {
		List<OfflinePlayer> list = new ArrayList<OfflinePlayer>();
		
		for (String token : members) {
			OfflinePlayer p = Token.getOfflinePlayer(token);
			if (p != null) list.add(p);
		}
		
		return list;
	}
	
	public List<Player> getOnlineMembers() {
		List<Player> list = new ArrayList<Player>();
		
		for (String token : members) {
			Player p = Token.getPlayer(token);
			if (p != null) list.add(p);
		}
		
		return list;
	}
	
	public void transferOwner(OfflinePlayer p) {
		this.owner = Token.getToken(p);
	}
	
	public void join(OfflinePlayer p) {
		members.add(Token.getToken(p));
	}
	
	public void leave(OfflinePlayer p) {
		members.remove(Token.getToken(p));
	}
}