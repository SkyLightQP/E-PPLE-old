package me.efe.efequest.quest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class UserData {
	private UUID id;
	private List<Integer> possibleQuests;
	private List<Integer> acceptedQuests;
	private List<Integer> endedQuests;
	
	public UserData(UUID id, List<Integer> possibleQuests, List<Integer> acceptedQuests, List<Integer> endedQuests) {
		this.id = id;
		this.possibleQuests = possibleQuests;
		this.acceptedQuests = acceptedQuests;
		this.endedQuests = endedQuests;
	}
	
	public UUID getUniqueId() {
		return this.id;
	}
	
	public List<Integer> getPossibleQuests() {
		return this.possibleQuests;
	}
	
	public List<Integer> getAcceptedQuests() {
		return this.acceptedQuests;
	}
	
	public List<Integer> getEndedQuests() {
		return this.endedQuests;
	}
	
	public boolean isPossible(int quest) {
		return this.possibleQuests.contains(quest);
	}
	
	public boolean isAccepted(int quest) {
		return this.acceptedQuests.contains(quest);
	}
	
	public boolean isEnded(int quest) {
		return this.endedQuests.contains(quest);
	}
	
	public void registerNewQuest(int quest) {
		this.possibleQuests.add(quest);
		
		save();
	}
	
	public void acceptQuest(int quest) {
		this.possibleQuests.remove(new Integer(quest));
		this.acceptedQuests.add(new Integer(quest));
		
		save();
		
		QuestLoader.refreshParticles(Bukkit.getPlayer(id));
	}
	
	public void endQuest(int quest) {
		if (this.acceptedQuests.contains(quest)) this.acceptedQuests.remove(new Integer(quest));
		this.endedQuests.add(new Integer(quest));
		
		save();
		
		QuestLoader.refreshParticles(Bukkit.getPlayer(id));
	}
	
	public Quest getFish() {
		for (int id : this.acceptedQuests) {
			Quest quest = QuestLoader.getQuest(id);
			
			for (String goal : quest.getGoals()) {
				if (goal.equals("FISH")) {
					return quest;
				}
			}
		}
		return null;
	}
	
	public Object getData(int quest, int goal) {
		File file = new File("plugins/EfeQuest/UserData/"+id.toString()+".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		return config.get("quests-data."+quest+"."+goal);
	}
	
	public void setData(int quest, int goal, Object value) {
		try {
			File file = new File("plugins/EfeQuest/UserData/"+id.toString()+".yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			config.set("quests-data."+quest+"."+goal, value);
			
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void save() {
		try {
			File file = new File("plugins/EfeQuest/UserData/"+id.toString()+".yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			config.set("possible-quests", this.possibleQuests);
			config.set("accepted-quests", this.acceptedQuests);
			config.set("ended-quests", this.endedQuests);
			
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}