package me.efe.efevote;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeserver.PlayerData;
import me.efe.efetutorial.TutorialState;
import mkremins.fanciful.FancyMessage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.swifteh.GAL.RewardEvent;

public class VoteListener implements Listener {
	public EfeVote plugin;
	public Map<UUID, BukkitTask> taskMap = new HashMap<UUID, BukkitTask>();
	public long period = 12000; // 10 minutes
	
	public VoteListener(EfeVote plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (TutorialState.get(event.getPlayer()) >= TutorialState.WELCOME_TO_POLARIS && plugin.canVote(event.getPlayer())) {
			taskMap.put(event.getPlayer().getUniqueId(), new VoteTask(event.getPlayer()).runTaskTimer(plugin, 20, period));
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (taskMap.containsKey(event.getPlayer().getUniqueId())) {
			taskMap.get(event.getPlayer().getUniqueId()).cancel();
			taskMap.remove(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onReard(RewardEvent event) {
		OfflinePlayer player = EfeUtils.player.getOfflinePlayer(event.getCommandList().get(0).substring(8));
		
		if (player == null)
			return;
		
		plugin.setVote(player);
		
		PlayerData data = PlayerData.get(player);
		data.giveWheelOfFortune();
		
		if (taskMap.containsKey(player.getUniqueId())) {
			taskMap.get(player.getUniqueId()).cancel();
			taskMap.remove(player.getUniqueId());
		}
		
		if (player.isOnline()) {
			player.getPlayer().sendMessage("§a▒§r §7============================================");
			player.getPlayer().sendMessage("§a▒§r ");
			player.getPlayer().sendMessage("§a▒§r 추천해주셔서 감사합니다!");
			player.getPlayer().sendMessage("§a▒§r 보상으로 §b휠 오브 포춘 티켓§r을 하나 얻었습니다.");
			
			new FancyMessage("§a▒§r 지금 ")
			.then("§b§n휠 오브 포춘§r")
				.command("/휠오브포춘")
				.tooltip("§b/휠오브포춘")
			.then("을 돌려보세요!")
			.send(player.getPlayer());
			
			player.getPlayer().sendMessage("§a▒§r ");
			player.getPlayer().sendMessage("§a▒§r §7============================================");
		}
	}
	
	private class VoteTask extends BukkitRunnable {
		private final Player player;
		
		public VoteTask(Player player) {
			this.player = player;
		}
		
		@Override
		public void run() {
			player.sendMessage("§a▒§r §7============================================");
			player.sendMessage("§a▒§r ");
			player.sendMessage("§a▒§r 마인리스트에서 E-PPLE을 추천해주세요!");
			player.sendMessage("§a▒§r 추천하면 §b휠 오브 포춘§r을 돌려 치장 아이템을 획득할 수 있습니다.");
			
			new FancyMessage("§a▒§r 지금 ")
			.then("§b§n마인리스트§r")
				.link("https://minelist.kr/servers/e-pple.kr")
				.tooltip("§bhttps://minelist.kr/servers/e-pple.kr")
			.then("에서 추천해보세요!")
			.send(player);
			
			player.sendMessage("§a▒§r ");
			player.sendMessage("§a▒§r §7============================================");
		}
	}
}
