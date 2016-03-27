package me.efe.efetutorial;

import me.efe.efeserver.PlayerData;

import org.bukkit.entity.Player;

public final class TutorialState {
	
	private static int i = 0;
	
	public static int ON_THE_MAGIC_CIRCLE = i ++;
	public static int FALLING = i ++;
	public static int INTRO_STARTED = i ++;
	public static int INTRO_FINISHED = i ++;
	public static int LEVEL_UP = i ++;
	public static int SP_USED_BOAT = i ++;
	public static int LETS_ENTER_BOAT = i ++;
	public static int ENTERED_BOAT = i ++;
	public static int LETS_CLICK_NAVIGATION = i ++;
	public static int CLICKED_NAVIGATION = i ++;
	public static int LETS_SELECT_SKILL = i ++;
	public static int NAVIGATED_SKILL = i ++;
	public static int GO_TO_SKILL = i ++;
	public static int LEFT_START_REGION = i ++;
	public static int ARRIVED_SKILL_ISLAND = i ++;
	public static int GO_TO_MINE = i ++;
	public static int LEFT_SKILL_REGION = i ++;
	public static int ARRIVED_MINE_ISLAND = i ++;
	public static int LETS_BREAK_STONES = i ++;
	public static int BROKE_STONE = i ++;
	public static int GO_TO_WEED = i ++;
	public static int ARRIVED_WEED_ISLAND = i ++;
	public static int LETS_COLLECT_SEEDS = i ++;
	public static int COLLECTED_SEEDS = i ++;
	public static int GO_TO_FARM = i ++;
	public static int LEFT_WEED_REGION = i ++;
	public static int ARRIVED_FARM_ISLAND = i ++;
	public static int SP_USED_CARROT = i ++;
	public static int CREATED_FARM = i ++;
	public static int WATERD_FARM = i ++;
	public static int PLANTED_SEEDS = i ++;
	public static int LETS_HARVEST_CROPS = i ++;
	public static int HARVESTED_CROPS = i ++;
	public static int GO_TO_FISH = i ++;
	public static int LEFT_FARM_REGION = i ++;
	public static int ARRIVED_FISH_ISLAND = i ++;
	public static int LETS_FISH = i ++;
	public static int FISHED = i ++;
	public static int GO_TO_HUNT = i ++;
	public static int LEFT_FISH_REGION = i ++;
	public static int ARRIVED_HUNT_ISLAND = i ++;
	public static int LETS_KILL_ZOMBIES = i ++;
	public static int WELCOME_TO_POLARIS = i ++;
	public static int QUEST_ACCEPTED = i ++;
	public static int ISLAND_CREATED = 999;
	
	
	public static int get(Player player) {
		PlayerData data = PlayerData.get(player);
		
		return data.getTutorialState();
	}
	
	public static void set(Player player, int state) {
		PlayerData data = PlayerData.get(player);
		
		data.setTutorialState(state);
	}
}