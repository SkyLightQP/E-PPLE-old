package me.efe.skilltree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class Skill {
	private final String name;
	private final String displayName;
	private final String[] description;
	private final List<String[]> functions;
	private final int maxLv;
	private final Map<Integer, Integer> requiredSPMap;
	private final Map<String, Integer> requiredSkillMap;
	private final ItemStack icon;
	private final int slot;
	
	public Skill(String name, String displayName, String[] description, List<String[]> functions, int maxLv,
			Map<Integer, Integer> requiredSPMap, Map<String, Integer> requiredSkillMap, ItemStack icon, int slot) {
		this.name = name;
		this.displayName = displayName;
		this.description = description;
		this.functions = functions;
		this.maxLv = maxLv;
		this.requiredSPMap = requiredSPMap;
		this.requiredSkillMap = requiredSkillMap;
		this.icon = icon;
		this.slot = slot;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public String[] getDescription() {
		return this.description;
	}
	
	public String[] getFunction(int level) {
		return this.functions.get(level - 1);
	}
	
	public int getMaxLevel() {
		return this.maxLv;
	}
	
	public int getRequiredSP(int level) {
		return this.requiredSPMap.get(level);
	}
	
	public Map<String, Integer> getRequiredSkillMap() {
		return this.requiredSkillMap;
	}
	
	public ItemStack getIcon() {
		return this.icon;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String name;
		private String displayName;
		private String[] description;
		private List<String[]> functions = new ArrayList<String[]>();
		private int maxLv;
		private Map<Integer, Integer> requiredSPMap = new HashMap<Integer, Integer>();
		private Map<String, Integer> requiredSkillMap = new HashMap<String, Integer>();
		private ItemStack icon;
		private int slot;
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder setDisplayName(String displayName) {
			this.displayName = displayName;
			return this;
		}
		
		public Builder setDescription(String... description) {
			this.description = description;
			return this;
		}
		
		public Builder addFunction(String... function) {
			this.functions.add(function);
			return this;
		}
		
		public Builder setMaxLevel(int maxLv) {
			this.maxLv = maxLv;
			return this;
		}
		
		public Builder setRequiredSP(int level, int requiredSP) {
			this.requiredSPMap.put(level, requiredSP);
			return this;
		}
		
		public Builder setRequiredSkillName(String skillName, int level) {
			this.requiredSkillMap.put(skillName, level);
			return this;
		}
		
		public Builder setIcon(ItemStack icon) {
			this.icon = icon;
			return this;
		}
		
		public Builder setSlot(int slot) {
			this.slot = slot;
			return this;
		}
		
		public Skill build() {
			return new Skill(name, displayName, description, functions, maxLv, requiredSPMap, requiredSkillMap, icon, slot);
		}
	}
}
