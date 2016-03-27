package me.efe.fishkg.events;

import me.efe.fishkg.Fishkg.RodType;
import me.efe.fishkg.listeners.fish.FishkgFish;
import me.efe.fishkg.listeners.fish.FishListener.FishkgBiome;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerFishkgEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean isCancelled;
	private FishkgFish fish;
	private FishkgBiome biome;
	private RodType rodType;
	
	public PlayerFishkgEvent(Player who, FishkgFish fish, FishkgBiome biome, RodType rodType) {
		super(who);
		this.fish = fish;
		this.biome = biome;
		this.rodType = rodType;
	}
	
	public FishkgFish getFish() {
		return this.fish;
	}
	
	public FishkgBiome getBiome() {
		return this.biome;
	}
	
	public RodType getRodType() {
		return this.rodType;
	}
	
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}
	
	@Override
	public void setCancelled(boolean arg0) {
		isCancelled = arg0;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}