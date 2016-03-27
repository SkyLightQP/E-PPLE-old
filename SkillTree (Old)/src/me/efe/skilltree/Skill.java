package me.efe.skilltree;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Skill implements Cloneable {
	private String name;
	private String[] effects;
	private int maxLv;
	private int needSP;
	private String[] needSkills;
	private ItemStack icon;
	private int slot;
	
	public Skill(String name, int maxLv, int needSP) {
		this.name = name;
		this.maxLv = maxLv;
		this.needSP = needSP;
		this.needSkills = new String[]{};
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDisplayName() {
		return this.icon.getItemMeta().getDisplayName();
	}
	
	public int getMaxLv() {
		return this.maxLv;
	}
	
	public int getNeedSP() {
		return this.needSP;
	}
	
	public List<Skill> getNeedSkills() {
		List<Skill> list = new ArrayList<Skill>();
		
		for (String skill : needSkills) {
			list.add(SkillUtils.getSkill(skill));
		}
		
		return list;
	}
	
	public ItemStack getIcon(UserData data) {
		ItemStack item = icon.clone();
		
		int nowLv = data.getLevel(this);
		int usedSP = data.getUsedSP();
		boolean isAvailable = true;
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = item.getItemMeta().getLore();
		
		lore.add("");
		
		if (nowLv == 0 && needSP != 0 && needSP > usedSP) {
			lore.add("§cSP 사용량 §n"+needSP+"§c 필요 §4["+usedSP+"/"+needSP+"]");
			
			isAvailable = false;
		}
		
		List<Skill> skills = getNeedSkills();
		if (!skills.isEmpty()) {
			for (Skill skill : skills) {
				if (data.getLevel(skill) != skill.getMaxLv()) {
					lore.add("§c"+skill.getDisplayName()+"§c 스킬 마스터 필요");
					isAvailable = false;
				}
			}
		}
		
		if (!isAvailable) lore.add("");
		
		if (nowLv != 0) lore.add("§9현재 레벨: " + effects[nowLv - 1]);
		if (nowLv != maxLv) lore.add("§9다음 레벨: " + effects[nowLv]);
		
		if (nowLv != maxLv && isAvailable) {
			lore.add("§2§m====================================");
			lore.add("§2클릭으로 스킬을 향상시키세요! §a[SP -1]");
			lore.add("§2남은 SP: §a"+data.getSP());
		}
		
		item.setAmount(nowLv);
		
		meta.setDisplayName("§d"+meta.getDisplayName()+"§7| Lv."+nowLv);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public Skill setEffects(String[] lore) {
		this.effects = lore;
		
		return this;
	}
	
	public Skill setNeedSkill(String skill) {
		this.needSkills = new String[]{skill};
		
		return this;
	}
	
	public Skill setNeedSkills(String... skills) {
		this.needSkills = skills;
		
		return this;
	}
	
	public Skill setIcon(ItemStack item, int slot) {
		this.icon = item;
		this.slot = slot;
		
		return this;
	}
	
	public Skill clone() {
		try {
			return (Skill) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}