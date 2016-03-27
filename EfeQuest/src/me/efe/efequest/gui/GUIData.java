package me.efe.efequest.gui;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import net.citizensnpcs.api.npc.NPC;
import me.efe.efequest.EfeQuest;
import me.efe.efequest.quest.Quest;

public class GUIData {
	public EfeQuest plugin;
	
	private NPC npc;
	private int slot;
	private Quest quest;
	private int chat;
	private Inventory inv;
	private Player p;
	private QuesterGUI.TimeTask task;
	private List<String> lore;
	
	public GUIData(EfeQuest plugin, NPC npc, int slot, Quest quest, Inventory inv, Player p) {
		this.plugin = plugin;
		
		this.npc = npc;
		this.slot = slot;
		this.quest = quest;
		this.chat = -1;
		this.inv = inv;
		this.p = p;
	}
	
	public NPC getNPC() {
		return this.npc;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public Quest getQuest() {
		return this.quest;
	}
	
	public int getChatIndex() {
		return this.chat;
	}
	
	public void updateChatIndex() {
		this.chat ++;
	}
	
	public void backChatIndex() {
		this.chat --;
	}
	
	public void startTask(List<String> lore) {
		this.lore = lore;
		if (task != null) task.cancel();
		
		task = new QuesterGUI.TimeTask(p, inv, slot, lore.toArray(new String[lore.size()]));
		task.runTaskTimer(plugin, 1, 1);
	}
	
	public void stopTask() {
		if (task == null) return;
		
		task.cancel();
	}
	
	public boolean isTyped() {
		return task.isTyped();
	}
	
	public void stopTyping() {
		task.stopTyping();
		
		ItemMeta meta = inv.getItem(slot).getItemMeta();
		meta.setLore(lore);
		
		inv.getItem(slot).setItemMeta(meta);
	}
	
	public boolean canSkip() {
		return (task == null || task.canSkip());
	}
	
	public void cancelSkip() {
		task.initSkip();
	}
}