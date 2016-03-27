package me.efe.unlimitedrpg.party;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

public class PartyAPI {
	private static List<Party> partyList = new ArrayList<Party>();
	
	public static Party getParty(String name) {
		for (Party party : partyList) {
			if (party.getName().equalsIgnoreCase(name)) {
				return party;
			}
		}
		return null;
	}
	
	public static Party getJoinedParty(OfflinePlayer p) {
		for (Party party : partyList) {
			if (party.getMembers().contains(p)) {
				return party;
			}
		}
		return null;
	}
	
	public static boolean isExist(String name) {
		for (Party party : partyList) {
			if (party.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static void registerParty(Party party) {
		partyList.add(party);
	}
	
	public static void unregisterParty(Party party) {
		partyList.remove(party);
		party = null;
	}
	
	public static List<Party> getPartyList() {
		return partyList;
	}
}