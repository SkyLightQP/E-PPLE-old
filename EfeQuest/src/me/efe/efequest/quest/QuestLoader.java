package me.efe.efequest.quest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.efe.efequest.EfeQuest;
import me.efe.efequest.gui.ChatTrigger;
import me.efe.efeserver.EfeServer;

public class QuestLoader {
	private static EfeQuest plugin;
	public static List<Quest> quests = new ArrayList<Quest>();
	public static HashMap<Integer, List<Quest>> questMap = new HashMap<Integer, List<Quest>>();
	
	public static void init(EfeQuest plugin) {
		QuestLoader.plugin = plugin;
		load();
	}
	
	private static void load() {
		File questFolder = new File(plugin.getDataFolder(), "Quests");
		File dataFolder = new File(plugin.getDataFolder(), "UserData");
		
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
			questFolder.mkdir();
			dataFolder.mkdir();
			return;
		}
		
		for (File file : questFolder.listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			Quest quest = Quest.getBuilder()
					.setIdentity(config.getInt("id"), config.getString("name"))
					.setNPC(config.getInt("startNPC"), config.getInt("quitNPC"))
					.setLevel(config.getInt("lv"))
					.setNeedQuests(config.getIntegerList("need-quests"))
					.setChats(config.getStringList("start-messages"), 
							config.getStringList("wait-messages"), 
							config.getStringList("quit-messages"), 
							config.getStringList("end-messages"))
					.setGoals(config.getStringList("goals"))
					.build();
			
			if (questMap.containsKey(quest.getStartNPC())) {
				questMap.get(quest.getStartNPC()).add(quest);
			} else {
				List<Quest> list = new ArrayList<Quest>();
				list.add(quest);
				questMap.put(quest.getStartNPC(), list);
			}
			
			if (quest.getStartNPC() != quest.getQuitNPC()) {
				if (questMap.containsKey(quest.getQuitNPC())) {
					questMap.get(quest.getQuitNPC()).add(quest);
				} else {
					List<Quest> list = new ArrayList<Quest>();
					list.add(quest);
					questMap.put(quest.getQuitNPC(), list);
				}
			}
			
			quests.add(quest);
		}
		
		System.out.println("[EfeQuest] "+quests.size()+" Quests Loaded Successful!");
	}
	
	public static Quest getQuest(int id) {
		for (Quest quest : quests) {
			if (quest.getIdentity() == id) {
				return quest;
			}
		}
		return null;
	}
	
	public static UserData getUserData(Player p) {
		return getUserData(p.getUniqueId());
	}
	
	public static UserData getUserData(UUID id) {
		File file = new File(plugin.getDataFolder(), "UserData/"+id.toString()+".yml");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			List<Integer> possible = new ArrayList<Integer>();
			for (Quest quest : quests) possible.add(quest.getIdentity());
			
			return new UserData(id, possible, new ArrayList<Integer>(), new ArrayList<Integer>());
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		UserData data = 
				new UserData(id, config.getIntegerList("possible-quests"), config.getIntegerList("accepted-quests"), config.getIntegerList("ended-quests"));
		
		for (Quest quest : quests) {
			int identity = quest.getIdentity();
			
			if (!data.isPossible(identity) && !data.isAccepted(identity) && !data.isEnded(identity)) {
				data.registerNewQuest(identity);
			}
		}
		
		return data;
	}
	
	public static void refreshParticles(Player p) {
		plugin.refreshParticles(p);
	}
	
	public static String getNPCName(NPC npc) {
		if (npc.getName().contains("]¡×r "))
			return npc.getName().split("]¡×r ")[1];
		else
			return npc.getName();
	}
	
	public static boolean canStart(Player p, Quest quest) {
		UserData data = QuestLoader.getUserData(p);
		
		if (p.getLevel() < quest.getLevelRequired()) {
			return false;
		}
		
		for (int need : quest.getNeedQuests()) {
			if (!data.isEnded(need)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isEndable(Player p, Quest quest) {
		UserData data = getUserData(p.getUniqueId());
		
		if (!data.isAccepted(quest.getIdentity())) return false;
		
		for (int i = 0; i < quest.getGoals().size(); i ++) {
			String goal = quest.getGoals().get(i);
			String[] split = goal.split("\\|");
			
			String valueType = split[0];
			
			if (valueType.startsWith("I:")) {
				Object obj = data.getData(quest.getIdentity(), i);
				
				if (obj == null)
					return false;
				
				int value = (int) obj;
				int need = Integer.parseInt(valueType.substring(2));
				
				if (value < need) {
					return false;
				}
				
				continue;
			} else if (valueType.equals("B")) {
				Object obj = data.getData(quest.getIdentity(), i);
				
				if (obj == null)
					return false;
				
				boolean value = (boolean) obj;
				
				if (!value) {
					return false;
				}
				
				continue;
			} else if (valueType.equals("N")) {
				String[] goalData = split[1].split(":");
				String goalType = goalData[0];
				
				if (goalType.equals("ITEM")) {
					ItemStack item = QuestLoader.getItem(goalData[1]).clone();
					
					if (goalData.length > 3)
						item.setDurability(Short.parseShort(goalData[3]));
					
					if (!ChatTrigger.hasItem(p, item, Integer.parseInt(goalData[2]))) {
						return false;
					}
					
					continue;
				} else if (goalType.equals("MONEY")) {
					double amount = Double.parseDouble(goalData[1]);
					
					if (!EfeServer.getInstance().vault.hasEnough(p, amount)) {
						return false;
					}
					
					continue;
				}
			}
		}
		
		return true;
	}
	
	public static void achieveGoal(Player p, String goalType, Object... goalData) {
		UserData data = getUserData(p);
		boolean changed = false;
		
		for (int id : data.getAcceptedQuests()) {
			Quest quest = getQuest(id);
			
			for (int i = 0; i < quest.getGoals().size(); i ++) {
				String goal = quest.getGoals().get(i);
				String[] split = goal.split("\\|");
				
				if (split[1].equals(goalType) || split[1].startsWith(goalType+":")) {
					if (split[0].startsWith("N"))
						continue;
					
					if (split[1].startsWith(goalType+":")) {
						String[] needData = split[1].split(":");
						
						if (goalType.equals("FISH_TYPED")) {
							String type = (String) goalType;
							String needType = needData[0];
							
							if (!type.equals(needType)) {
								return;
							}
						} else if (goalType.equals("FISH_LENGTH")) {
							double length = (double) goalData[0];
							double needLength = Double.parseDouble(needData[1]);
							
							if (length < needLength) {
								return;
							}
						} else if (goalType.equals("REGION_ENTER")) {
							String region = (String) goalType;
							String needRegion = needData[0];
							
							if (!region.equals(needRegion)) {
								return;
							}
						} else if (goalType.equals("KILL")) {
							String mob = (String) goalType;
							String needMob = needData[0];
							
							if (!mob.equals(needMob)) {
								return;
							}
						} else if (goalType.equals("KILLED_BY")) {
							String mob = (String) goalType;
							String needMob = needData[0];
							
							if (!mob.equals(needMob)) {
								return;
							}
						}
					}
					
					if (split[0].startsWith("I:")) {
						Object obj = data.getData(id, i);
						
						int old = (obj != null) ? (int) obj : 0;
						
						data.setData(id, i, old + 1);
						
						changed = true;
					} else if (split[0].equals("B")) {
						data.setData(id, i, true);
						
						changed = true;
					}
				}
			}
		}
		
		if (changed)
			refreshParticles(p);
	}
	
	public static ItemStack getItem(String name) {
		if (Material.getMaterial(name) != null)
			return new ItemStack(Material.getMaterial(name));
		
		File file = new File(plugin.getDataFolder(), "items.yml");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		return config.getItemStack(name).clone();
	}
	
	public static void setItem(String name, ItemStack item) {
		File file = new File(plugin.getDataFolder(), "items.yml");
		
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			config.set(name, item);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}