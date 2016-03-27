package me.efe.efetutorial;

import me.efe.efecore.util.EfeUtils;
import me.efe.eferudish.EfeRudish;
import me.efe.efeserver.PlayerData;
import me.efe.efeserver.reform.Fatigue;
import me.efe.efeserver.util.Scoreboarder;
import me.efe.efetutorial.listeners.FarmListener;
import me.efe.efetutorial.listeners.PlayerListener;
import me.efe.efetutorial.listeners.SelectListener;
import me.efe.efetutorial.listeners.TutorialListener;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class EfeTutorial extends JavaPlugin {
	public IntroHandler introHandler;
	public TutorialHandler tutorialHandler;
	public PlayerListener playerListener;
	public TutorialListener tutorialListener;
	public FarmListener farmListener;
	public SelectListener selectListener;
	
	@Override
	public void onEnable() {
		introHandler = new IntroHandler(this);
		tutorialHandler = new TutorialHandler(this);
		
		PluginManager manager = getServer().getPluginManager();
		
		manager.registerEvents(playerListener = new PlayerListener(this), this);
		manager.registerEvents(tutorialListener = new TutorialListener(this), this);
		manager.registerEvents(farmListener = new FarmListener(this), this);
		manager.registerEvents(selectListener = new SelectListener(this), this);
		
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.UPDATE_TIME) {
			@Override
			public void onPacketSending(PacketEvent e) {
				int state = TutorialState.get(e.getPlayer());
				
				if (state < TutorialState.WELCOME_TO_POLARIS) {
					if (state <= TutorialState.FALLING) {
						e.getPacket().getLongs().write(1, 18000L);
					} else {
						e.getPacket().getLongs().write(1, 6000L);
					}
				} else {
					long tick = (e.getPacket().getLongs().read(1) - 2) % 24000 / 20;
					
					if (tick == 0) {
						Fatigue.addFatigue(e.getPlayer(), -150);
						
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 0.5F);
						
						if (Fatigue.getFatigue(e.getPlayer()) == 0)
							e.getPlayer().sendMessage("§a▒§r 아침이 밝았습니다.");
						else
							e.getPlayer().sendMessage("§a▒§r 아침이 밝아 피로가 회복되었습니다. §8[피로도: "+(int) (Fatigue.getFatigue(e.getPlayer()) / 5)+"%]");
						
						Scoreboarder.updateObjectives(e.getPlayer());
						
						EfeRudish.getInstance().setBrewingRecipeOfDay();
					}
				}
			}
		});
		
		getLogger().info(getDescription().getFullName()+" has been enabled!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info(getDescription().getFullName()+" has been disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("efetutorial")) {
			if (args.length == 0)
				return false;
			
			Player player = EfeUtils.player.getOnlinePlayer(args[0]);
			
			if (player == null)
				return false;
			
			this.selectListener.takeItems(player, true);
		} else if (label.equalsIgnoreCase("skip") || label.equalsIgnoreCase("스킵")) {
			Player player = (Player) sender;
			
			if (TutorialState.get(player) < TutorialState.INTRO_STARTED) {
				player.sendMessage("§c▒§r 인트로 진행 중에만 사용할 수 있습니다.");
				return false;
			}
			
			if (TutorialState.get(player) >= TutorialState.INTRO_FINISHED) {
				player.sendMessage("§c▒§r 이미 인트로를 수행했습니다.");
				return false;
			}
			
			if (!PlayerData.get(player).hasPlayedIntro()) {
				player.sendMessage("§c▒§r 최소한 한 번은 인트로를 감상해주세요!");
				return false;
			}
			
			player.sendMessage("§a▒§r 인트로를 스킵하고 튜토리얼을 시작합니다..");
			
			if (this.introHandler.isInIntro(player)) {
				this.introHandler.finishIntro(player);
				return false;
			}
			
			player.setGameMode(GameMode.SURVIVAL);
			player.teleport(this.introHandler.tutorialLoc);
			player.setFallDistance(0.0F);
			
			TutorialState.set(player, TutorialState.INTRO_FINISHED);
			
			this.tutorialHandler.startTutorial(player);
		}
		
		return false;
	}
}