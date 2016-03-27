package me.efe.skilltree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.efe.efeserver.util.Scoreboarder;

import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SkillManager {
	private static Map<SkillType, List<Skill>> skillMap = new HashMap<SkillType, List<Skill>>();
	private static HashMap<UUID, List<String>> delayMap = new HashMap<UUID, List<String>>();
	
	static {
		skillMap.put(SkillType.HUNT, new ArrayList<Skill>());
		skillMap.put(SkillType.MINE, new ArrayList<Skill>());
		skillMap.put(SkillType.FARM, new ArrayList<Skill>());
		skillMap.put(SkillType.SAIL, new ArrayList<Skill>());
		
		skillMap.get(SkillType.HUNT).add(Skill.builder()
				.setName("")
				.setDescription("")
				.addFunction("")
				.setMaxLevel(0)
				.setRequiredSP(0, 0)
				.setRequiredSkillName("", 0)
				.setSlot(0)
				.build());
	}
	
	public static Skill getSkill(String name) {
		for (SkillType type : skillMap.keySet()) {
			for (Skill skill : skillMap.get(type)) {
				if (skill.getName().equals(name)) {
					return skill;
				}
			}
		}
		
		return null;
	}
	
	public static List<Skill> getSkillList(SkillType type) {
		return skillMap.get(type);
	}
	
	public static Skill getSkill(SkillType type, int slot) {
		for (Skill skill : skillMap.get(type)) {
			if (skill.getSlot() == slot) {
				return skill;
			}
		}
		
		return null;
	}
	
	public static int getLevel(OfflinePlayer player, String skillName) {
		return new UserData(player).getLevel(skillName);
	}
	
	public static boolean hasLearned(OfflinePlayer player, String skillName) {
		return new UserData(player).hasLearned(skillName);
	}
	
	public static void setDelay(final Player player, final String skillName, final String name, int sec) {
		final UUID id = player.getUniqueId();
		
		if (delayMap.containsKey(id)) {
			delayMap.get(id).add(skillName);
		} else {
			List<String> list = new ArrayList<String>();
			list.add(skillName);
			
			delayMap.put(id, list);
		}
		
		SkillTree.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(SkillTree.getInstance(), new Runnable() {
			public void run() {
				if (!delayMap.containsKey(id) || !delayMap.get(id).contains(skillName)) return;
				
				delayMap.get(id).remove(skillName);
				
				if (player != null) {
					player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.75F);
					Scoreboarder.message(player, new String[]{"§a§l>>§a " + name, "§a재사용 가능"}, 2);
				}
			}
		}, 20L * sec);
	}
	
	public static boolean isDelayed(Player p, String skill) {
		return delayMap.containsKey(p.getUniqueId()) ? delayMap.get(p.getUniqueId()).contains(skill) : false;
	}
	
	public static enum SkillType {
		HUNT, MINE, FARM, SAIL
	}
}
