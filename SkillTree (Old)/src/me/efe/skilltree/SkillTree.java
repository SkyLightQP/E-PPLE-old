package me.efe.skilltree;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import me.efe.efegear.EfeUtil;
import me.efe.efeserver.EfeServer;
import me.efe.skilltree.listeners.ArmorListener;
import me.efe.skilltree.listeners.BoatListener;
import me.efe.skilltree.listeners.BowListener;
import me.efe.skilltree.listeners.PickaxeListener;
import me.efe.skilltree.listeners.SkillTreeGUI;
import me.efe.skilltree.listeners.SwordListener;

public class SkillTree extends JavaPlugin {
	private static SkillTree instance;
	public EfeUtil util;
	
	public SwordListener sworldListener;
	public ArmorListener armorListener;
	public BowListener bowListener;
	public PickaxeListener pickaxeListener;
	public BoatListener boatListener;
	public SkillTreeGUI skillTreeGui;
	
	public WorldGuardPlugin wgp;
	public EffectManager em;
	public EfeServer epple;
	
	@Override
	public void onDisable() {
		util.logDisable();
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		util = new EfeUtil(this);
		util.logEnable();
		
		SkillUtils.init(this);
		DelayUtils.init(this);
		
		sworldListener = new SwordListener(this);
		armorListener = new ArmorListener(this);
		bowListener = new BowListener(this);
		pickaxeListener = new PickaxeListener(this);
		boatListener = new BoatListener(this);
		skillTreeGui = new SkillTreeGUI(this);
		
		util.register(sworldListener);
		util.register(armorListener);
		util.register(bowListener);
		util.register(pickaxeListener);
		util.register(boatListener);
		util.register(skillTreeGui);
		
		wgp = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
		epple = (EfeServer) getServer().getPluginManager().getPlugin("EfeServer");
		
		em = new EffectManager(EffectLib.instance());
	}
	
	public static SkillTree getInstance() {
		return instance;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("스킬") || l.equalsIgnoreCase("스킬트리") || l.equalsIgnoreCase("skilltree") || l.equalsIgnoreCase("skill")) {
			try {
				Player p = (Player) s;
				
				skillTreeGui.openGUI(p, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}