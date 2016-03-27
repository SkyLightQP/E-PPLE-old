package me.efe.efeisland;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;

public class EfeFlag {
	private static WGCustomFlagsPlugin wgf;
	
	public static final StateFlag FLY = new StateFlag("Fly", false);
	public static final StateFlag INSTANT_KILL = new StateFlag("InstantKill", false);
	public static final StateFlag SKILL = new StateFlag("Skill", false);
	public static final StateFlag NO_POTION = new StateFlag("NoPotion", false);
	public static final StringFlag TITLE = new StringFlag("Title");
	public static final StringFlag DESCRIPTION = new StringFlag("Description");
	public static final IntegerFlag LEVEL = new IntegerFlag("Level");
	public static final IntegerFlag MAX_VISIT = new IntegerFlag("MaxVisit");
	public static final SetFlag<String> VISITERS = new SetFlag<String>("Visiters", new StringFlag(null));
	public static final IntegerFlag NEED_QUEST = new IntegerFlag("NeedQuest");
	public static final StateFlag ENTRANCE = new StateFlag("Entrance", true);
	public static final SetFlag<String> BLACKLIST = new SetFlag<String>("Blacklist", new StringFlag(null));
	
	public static void init(EfeIsland plugin) {
		wgf = (WGCustomFlagsPlugin) plugin.getServer().getPluginManager().getPlugin("WGCustomFlags");
		
		wgf.addCustomFlag(EfeFlag.FLY);
		wgf.addCustomFlag(EfeFlag.INSTANT_KILL);
		wgf.addCustomFlag(EfeFlag.SKILL);
		wgf.addCustomFlag(EfeFlag.NO_POTION);
		wgf.addCustomFlag(EfeFlag.TITLE);
		wgf.addCustomFlag(EfeFlag.DESCRIPTION);
		wgf.addCustomFlag(EfeFlag.LEVEL);
		wgf.addCustomFlag(EfeFlag.MAX_VISIT);
		wgf.addCustomFlag(EfeFlag.VISITERS);
		wgf.addCustomFlag(EfeFlag.NEED_QUEST);
		wgf.addCustomFlag(EfeFlag.ENTRANCE);
		wgf.addCustomFlag(EfeFlag.BLACKLIST);
	}
}