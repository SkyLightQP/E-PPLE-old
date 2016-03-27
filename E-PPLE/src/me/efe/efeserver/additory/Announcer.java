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
		
		messageList.add("E-PPLE������ �ִ� ������ 30�Դϴ�.");
		messageList.add("Polaris ���� ���� �ο��뿡�� û�ݼ����� ������ ��þƮ �� �� �ֽ��ϴ�.");
		messageList.add("\"/�ŷ�\" ��ɾ�� �ٸ� ������ �ŷ��� �� �ֽ��ϴ�.");
		messageList.add("\"/����\" ��ɾ ����ϸ� ��ɾ� ����� Ȯ���� �� �ֽ��ϴ�.");
		messageList.add("������ ����� Ư�� ��ų�� ����߸� �����մϴ�.");
		messageList.add("�Ϸ� �ִ� ���� ���� ���� Ƚ���� 7ȸ�Դϴ�.");
		messageList.add("ä���ÿ� ����ϰ� ������ ����� �絿�̷� �۰� �� �ֽ��ϴ�.");
		messageList.add("��ų� ������ �߰��ߴٸ� ���� ī���� �Ű� �Խ����� �̿����ּ���.");
		messageList.add("���� ������ �ִ� 5���� ��Ƽ�� �����մϴ�.");
		messageList.add("�� ���� Ȱ���� �����ϸ� ���õ��� ���� �� �ֽ��ϴ�. \"/���õ�\"");
		messageList.add("���õ��� ������ �ø��� ����ġ�� �� ���� ȹ���� �� �ֽ��ϴ�. \"/���õ�\"");
		messageList.add("E-PPLE���� ���� �� ������ �Ұ����մϴ�.");
		messageList.add("TabŰ�� ������ �ڽ��� �������� Ȯ���� �� �ֽ��ϴ�.");
		messageList.add("E-PPLE�� ��ȭ������ '���� (Ephe)'�Դϴ�.");
		messageList.add("Ư�� ��ų�� ���� ���� 2~3������ ������ �� �ֽ��ϴ�.");
		messageList.add("û�ݼ��� ��, ���̾Ƹ��� Ư�� ��ų�� ����� ������ ä���� �� �����ϴ�.");
		messageList.add("������ �ɸ� ������ �����ϸ� ������ ������ ���� �¾�ϴ�.");
		messageList.add("������ ���� ������ �����ϸ� ���� ��Ⱑ ����˴ϴ�.");
		messageList.add("�ڽ��� �ֻ� ���� ���� ���� �ƴ϶�� �ҿ��� ȹ���� �� �����ϴ�.");
		messageList.add("�ް����� ���� ���Ƹ��� ������ ���� Ȯ���� �����ϴ�.");
		messageList.add("�̳׶� ������ �� ���Ͽ����� ���� �ٹ̴� ���� ������ �������� �ŷ��ǰ� �ֽ��ϴ�.");
		
		long delay = 20 * 60 * 5;
		new BroadcastTask().runTaskTimer(plugin, delay, delay);
	}
	
	private class BroadcastTask extends BukkitRunnable {
		
		@Override
		public void run() {
			plugin.getServer().broadcastMessage("��e�ơ�r "+messageList.get(plugin.util.random.nextInt(messageList.size())));
		}
	}
}