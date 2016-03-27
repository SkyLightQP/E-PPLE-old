package me.efe.efeserver.additory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import me.efe.efeserver.EfeServer;

public class Announcer {
	public EfeServer plugin;
	public List<String> messageList = new ArrayList<String>();
	
	public Announcer(EfeServer plugin) {
		this.plugin = plugin;
		
		messageList.add("E-PPLE에서의 최대 레벨은 30입니다.");
		messageList.add("Polaris 섬의 마법 부여대에서 청금석으로 도구를 인첸트 할 수 있습니다.");
		messageList.add("\"/거래\" 명령어로 다른 유저와 거래할 수 있습니다.");
		messageList.add("\"/도움말\" 명령어를 사용하면 명령어 목록을 확인할 수 있습니다.");
		messageList.add("가축의 교배는 특정 스킬을 배워야만 가능합니다.");
		messageList.add("하루 최대 보스 도전 가능 횟수는 7회입니다.");
		messageList.add("채광시에 희귀하게 나오는 용암은 양동이로 퍼갈 수 있습니다.");
		messageList.add("비매너 유저를 발견했다면 서버 카페의 신고 게시판을 이용해주세요.");
		messageList.add("보스 도전은 최대 5명의 파티로 가능합니다.");
		messageList.add("한 가지 활동에 집중하면 숙련도를 쌓을 수 있습니다. \"/숙련도\"");
		messageList.add("숙련도의 레벨을 올리면 경험치를 더 많이 획득할 수 있습니다. \"/숙련도\"");
		messageList.add("E-PPLE에서 지옥 문 개설은 불가능합니다.");
		messageList.add("Tab키를 누르면 자신의 소지금을 확인할 수 있습니다.");
		messageList.add("E-PPLE의 통화단위는 '에페 (Ephe)'입니다.");
		messageList.add("특정 스킬을 배우면 밭을 2~3개까지 개설할 수 있습니다.");
		messageList.add("청금석과 금, 다이아몬드는 특정 스킬을 배우지 않으면 채집할 수 없습니다.");
		messageList.add("질병에 걸린 가축을 교배하면 새끼도 질병을 갖고 태어납니다.");
		messageList.add("질병을 가진 가축을 도축하면 썩은 고기가 드랍됩니다.");
		messageList.add("자신의 최상 접근 가능 층이 아니라면 소울을 획득할 수 없습니다.");
		messageList.add("달걀에서 나온 병아리는 질병을 가질 확률이 높습니다.");
		messageList.add("미네라스 알파의 블럭 마켓에서는 섬을 꾸미는 데에 유용한 아이템이 거래되고 있습니다.");
		
		long delay = 20 * 60 * 5;
		new BroadcastTask().runTaskTimer(plugin, delay, delay);
	}
	
	private class BroadcastTask extends BukkitRunnable {
		
		@Override
		public void run() {
			plugin.getServer().broadcastMessage("§e▒§r "+messageList.get(plugin.util.random.nextInt(messageList.size())));
		}
	}
}