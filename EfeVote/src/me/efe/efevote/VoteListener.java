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
			player.getPlayer().sendMessage("��a�ơ�r ��7============================================");
			player.getPlayer().sendMessage("��a�ơ�r ");
			player.getPlayer().sendMessage("��a�ơ�r ��õ���ּż� �����մϴ�!");
			player.getPlayer().sendMessage("��a�ơ�r �������� ��b�� ���� ���� Ƽ�ϡ�r�� �ϳ� ������ϴ�.");
			
			new FancyMessage("��a�ơ�r ���� ")
			.then("��b��n�� ���� �����r")
				.command("/�ٿ�������")
				.tooltip("��b/�ٿ�������")
			.then("�� ����������!")
			.send(player.getPlayer());
			
			player.getPlayer().sendMessage("��a�ơ�r ");
			player.getPlayer().sendMessage("��a�ơ�r ��7============================================");
		}
	}
	
	private class VoteTask extends BukkitRunnable {
		private final Player player;
		
		public VoteTask(Player player) {
			this.player = player;
		}
		
		@Override
		public void run() {
			player.sendMessage("��a�ơ�r ��7============================================");
			player.sendMessage("��a�ơ�r ");
			player.sendMessage("��a�ơ�r ���θ���Ʈ���� E-PPLE�� ��õ���ּ���!");
			player.sendMessage("��a�ơ�r ��õ�ϸ� ��b�� ���� �����r�� ���� ġ�� �������� ȹ���� �� �ֽ��ϴ�.");
			
			new FancyMessage("��a�ơ�r ���� ")
			.then("��b��n���θ���Ʈ��r")
				.link("https://minelist.kr/servers/e-pple.kr")
				.tooltip("��bhttps://minelist.kr/servers/e-pple.kr")
			.then("���� ��õ�غ�����!")
			.send(player);
			
			player.sendMessage("��a�ơ�r ");
			player.sendMessage("��a�ơ�r ��7============================================");
		}
	}
}
