package me.efe.unlimitedrpg.playerinfo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInfoEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private Player target;
	private Inventory inv;
	
	public PlayerInfoEvent(Player player, Player target, Inventory inv) {
		super(player);
		this.player = player;
		this.target = target;
		this.inv = inv;
	}
	
	public Player getTarget() {
		return target;
	}
	
	public Inventory getGUI() {
		return inv;
	}
	
	public void setGUISize(int row) {
		if (row > 6) row = 6;
		
		Inventory inventory = Bukkit.createInventory(null, 9*row, target.getName()+"¥‘¿« ¡§∫∏ -");
		
		for (int i = 0; i < inv.getSize(); i ++)
			inventory.setItem(i, inv.getItem(i));
		
		this.inv = inventory;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}