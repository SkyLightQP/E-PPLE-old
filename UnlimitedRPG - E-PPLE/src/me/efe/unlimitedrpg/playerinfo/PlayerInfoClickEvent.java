package me.efe.unlimitedrpg.playerinfo;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInfoClickEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private Player target;
	private Inventory inv;
	private InventoryClickEvent e;
	
	public PlayerInfoClickEvent(Player player, Player target, Inventory inv, InventoryClickEvent e) {
		super(player);
		this.player = player;
		this.target = target;
		this.inv = inv;
		this.e = e;
	}
	
	public Player getTarget() {
		return target;
	}
	
	public Inventory getGUI() {
		return inv;
	}
	
	public InventoryClickEvent getClickEvent() {
		return e;
	}
	
	public int getSlotNum() {
		return e.getRawSlot();
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}