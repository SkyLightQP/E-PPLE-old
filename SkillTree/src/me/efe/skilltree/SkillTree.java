package me.efe.skilltree;

import me.efe.efecore.util.EfeUtils;
import me.efe.skilltree.listeners.SkillTreeGUI;
import me.efe.skilltree.listeners.TeleportGUI;
import mkremins.fanciful.FancyMessage;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.ParticleEffect;

public class SkillTree extends JavaPlugin implements Listener {
	private static SkillTree instance;
	private SkillTreeGUI skillTreeGUI;
	private TeleportGUI teleportGUI;
	
	public WorldGuardPlugin wgp;
	public EffectManager em;
	
	@Override
	public void onEnable() {
		instance = this;
		
		PluginManager manager = getServer().getPluginManager();
		
		manager.registerEvents(this, this);
		manager.registerEvents(skillTreeGUI = new SkillTreeGUI(this), this);
		manager.registerEvents(teleportGUI = new TeleportGUI(this), this);
		
		wgp = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
		em = new EffectManager(EffectLib.instance());
		
		getLogger().info(getDescription().getFullName() + " has been enabled!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info(getDescription().getFullName() + " has been disabled.");
	}
	
	public static SkillTree getInstance() {
		return instance;
	}
	
	public SkillTreeGUI getSkillTreeGUI() {
		return this.skillTreeGUI;
	}
	
	public TeleportGUI getTeleportGUI() {
		return this.teleportGUI;
	}
	
	@EventHandler
	public void onLevelChange(PlayerLevelChangeEvent event) {
		if (event.getNewLevel() > event.getOldLevel()) {
			UserData data = new UserData(event.getPlayer());
			
			for (int i = 0; i < event.getNewLevel() - event.getOldLevel(); i ++) {
				data.giveSP(3);
			}
			
			event.getPlayer().sendMessage("§a▒§r §7============================================");
			event.getPlayer().sendMessage("§a▒§r ");
			event.getPlayer().sendMessage("§a▒§r §b§l>> LEVEL UP!!");
			event.getPlayer().sendMessage("§a▒§r ");
			event.getPlayer().sendMessage("§a▒§r 이제 당신은 §aLv."+event.getNewLevel()+"§r 입니다!");
			
			new FancyMessage("§a▒§r ")
			.then("§b§n스킬트리§r")
				.command("/스킬트리")
				.tooltip("§b/스킬트리")
			.then("에서 스킬을 배우거나 업그레이드하세요.")
			.send(event.getPlayer());
			
			event.getPlayer().sendMessage("§a▒§r ");
			event.getPlayer().sendMessage("§a▒§r §e사용하지 않은 SP§r가 §e"+data.getSP()+"§r 있습니다.");
			event.getPlayer().sendMessage("§a▒§r ");
			event.getPlayer().sendMessage("§a▒§r §7============================================");
			
			Location loc = event.getPlayer().getLocation();
			
			ParticleEffect.FIREWORKS_SPARK.display(null, loc, Color.GREEN, 32, 0.05F, 0.05F, 0.05F, 0.075F, 20);
			
			Firework firework = loc.getWorld().spawn(loc, Firework.class);
			FireworkMeta meta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder()
					.with(Type.BALL_LARGE)
					.withColor(Color.LIME, Color.GREEN)
					.withFade(Color.GREEN)
					.build();
			
			meta.addEffect(effect);
			meta.setPower(1);
			firework.setFireworkMeta(meta);
			
			event.getPlayer().playSound(loc, Sound.LEVEL_UP, 1.0F, 1.0F);
			
			for (Player all : EfeUtils.player.getOnlinePlayers()) {
				me.efe.efecommunity.UserData communityData = new me.efe.efecommunity.UserData(all);
				
				if (communityData.getFriends().contains(event.getPlayer().getUniqueId())) {
					all.getPlayer().sendMessage("§d▒§r "+event.getPlayer().getName()+"님이 Lv."+event.getNewLevel()+"에 도달했습니다.");
				}
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("스킬트리") || label.equalsIgnoreCase("스킬") || label.equalsIgnoreCase("skilltree") || label.equalsIgnoreCase("skill")) {
			Player player = (Player) sender;
			
			getSkillTreeGUI().openGUI(player);
		}
		
		return false;
	}
}
