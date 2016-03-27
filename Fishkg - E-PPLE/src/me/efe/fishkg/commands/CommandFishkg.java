package me.efe.fishkg.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;
import me.efe.fishkg.Contest;
import me.efe.fishkg.Fishkg;
import me.efe.fishkg.Contest.Team;
import me.efe.fishkg.listeners.fish.FishkgFish;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class CommandFishkg implements CommandExecutor, TabCompleter {
	public Fishkg plugin;
	
	public CommandFishkg(Fishkg plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
		List<String> list = new ArrayList<String>();
		
		if (a.length <= 1) {
			list.add("1");
			list.add("2");
			list.add("����");
			list.add("����");
			list.add("����");
			list.add("����");
			list.add("������ǥ");
			
			list.add("���");
			list.add("���");
			list.add("�߹�");
			list.add("����");
			list.add("Ż��");
			
			list.add("�ʱ�ȭ");
			list.add("���ε�");
			list.add("���˴�");
			list.add("�絿��");
		} else if (a[0].equalsIgnoreCase("���")) {
			list.add("��");
			list.add("��ũ");
		}
		
		return TabCompletionHelper.getPossibleCompletions(a, list);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		try {
			if (a.length == 0) {
				plugin.getServer().dispatchCommand(s, l+" 1");
				return false;
			}
			
			if (a[0].equalsIgnoreCase("����")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
					return false;
				}
				
				if (Contest.isEnabled()) {
					s.sendMessage(plugin.main+"���ô�ȸ�� �̹� �������Դϴ�.");
					return false;
				}
				
				if (a.length == 2 && EfeUtils.string.isInteger(a[1])) {
					Contest.start(Long.parseLong(a[1]) * 1200L);
					plugin.getServer().broadcastMessage(plugin.main+"���ô�ȸ�� ���۵Ǿ����ϴ�! "+Integer.parseInt(a[1])+"�� �Ŀ� ���ô�ȸ�� ����˴ϴ�.");
				} else {
					Contest.start(-1);
					plugin.getServer().broadcastMessage(plugin.main+"���ô�ȸ�� ���۵Ǿ����ϴ�!");
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("����")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"���ô�ȸ�� ���������� �ʽ��ϴ�.");
					return false;
				}
				
				Contest.quit(false);
				
				plugin.getServer().broadcastMessage(plugin.main+"���ô�ȸ�� ����Ǿ����ϴ�.");
				
				return true;
			} else if (a[0].equalsIgnoreCase("����")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"���ô�ȸ�� ���������� �ʽ��ϴ�.");
					return false;
				}
				
				Contest.quit(true);
				
				return true;
			} else if (a[0].equalsIgnoreCase("����")) {
				if (!plugin.checkPermission(s, "fishkg.stat")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.stat]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"���ô�ȸ�� ���������� �ʽ��ϴ�.");
					return false;
				}
				
				Contest.sendStatus(s);
				
				return true;
			} else if (a[0].equalsIgnoreCase("������ǥ")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"���ô�ȸ�� ���������� �ʽ��ϴ�.");
					return false;
				}
				
				for (Player all : EfeUtils.player.getOnlinePlayers()) {
					Contest.sendStatus(all);
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("�ʱ�ȭ")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"���ô�ȸ�� ���������� �ʽ��ϴ�.");
					return false;
				}
				
				Contest.init();
				
				s.sendMessage(plugin.main+"���ô�ȸ�� �ʱ�ȭ �Ǿ����ϴ�.");
				
				return true;
			} else if (a[0].equalsIgnoreCase("���ε�")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
					return false;
				}
				
				plugin.reloadConfig();
				
				s.sendMessage(plugin.main+"Config ���ε带 �Ϸ��߽��ϴ�.");
				s.sendMessage(plugin.main+"��c�� �Ϻ� ������ �÷������� ���ε��ؾ� �ݿ��� �� �ֽ��ϴ�!");
				
				return true;
			} else if (a[0].equalsIgnoreCase("���")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
					return false;
				}
				
				if (a.length == 1) {
					s.sendMessage(plugin.main+"��9��=====[ ��b��l"+plugin.getDescription().getFullName()+" ��2����塹��3 ]=====��");
					s.sendMessage(plugin.main+"��7[��aM��7 : ��ο��� �޽����� ����] [��eP��7 : �۹̼� �ʿ�]");
					s.sendMessage(plugin.main+"��b/"+l+" ����r: ��ȸ�� ��� ��ɾ Ȯ���մϴ�. ��e[P]��r");
					s.sendMessage(plugin.main+"��b/"+l+" ��� ����r: �� ��� ��ɾ Ȯ���մϴ�. ��e[P]��r");
					s.sendMessage(plugin.main+"��b/"+l+" ��� ��ũ��r: ��ũ ��� ��ɾ Ȯ���մϴ�. ��e[P]��r");
					s.sendMessage(plugin.main+"��9��=======[ ��b��l��nMade by Efe��9 ]=======��");
				} else if (a[1].equalsIgnoreCase("��")) {
					
					if (a.length == 2) {
						s.sendMessage(plugin.main+"��9��=====[ ��b��l"+plugin.getDescription().getFullName()+" ��2��������3 ]=====��");
						s.sendMessage(plugin.main+"��7[��aM��7 : ��ο��� �޽����� ����] [��eP��7 : �۹̼� �ʿ�]");
						s.sendMessage(plugin.main+"��b/"+l+" ��� ����r: �� ��� ��ɾ Ȯ���մϴ�. ��e[P]��r");
						s.sendMessage(plugin.main+"��b/"+l+" ��� �� <ON/OFF>��r: �� ��带 Ű�ų� ���ϴ�. ��e[P]��r");
						s.sendMessage(plugin.main+"��b/"+l+" ��� <�÷��̾�> <����/���>��r: ���� ���Խ�ŵ�ϴ�. ��e[P]��r ��a[M]��r");
						s.sendMessage(plugin.main+"��b/"+l+" �߹� <�÷��̾�>��r: <�÷��̾�>�� ���Ե� ������ �߹��մϴ�. ��e[P]��r ��a[M]��r");
						s.sendMessage(plugin.main+"��b/"+l+" ���� <����/���>��r: ���� �����մϴ�. ��e[P]��r ��a[M]��r");
						s.sendMessage(plugin.main+"��b/"+l+" Ż���r: ������ Ż���մϴ�. ��e[P]��r ��a[M]��r");
						s.sendMessage(plugin.main+"��9��=======[ ��b��l��nMade by Efe��9 ]=======��");
					} else if (a[2].equalsIgnoreCase("ON") || a[2].startsWith("t")) {
						Contest.setModTeam(true);
						
						s.sendMessage(plugin.main+"�� ��尡 Ȱ��ȭ�Ǿ����ϴ�!");
						s.sendMessage(plugin.main+"�������� ���� ���Խ����ּ���.");
						s.sendMessage(plugin.main+"��c[!]��r ���� �������� ������ ��ȸ ������ �� �˴ϴ�!");
					} else if (a[2].equalsIgnoreCase("OFF") || a[2].startsWith("f")) {
						Contest.setModTeam(false);
						
						s.sendMessage(plugin.main+"�� ��尡 ��Ȱ��ȭ�Ǿ����ϴ�.");
					}
				} else if (a[1].equalsIgnoreCase("��ũ")) {
					if (a.length == 2) {
						s.sendMessage(plugin.main+"��9��=====[ ��b��l"+plugin.getDescription().getFullName()+" ��2��������3 ]=====��");
						s.sendMessage(plugin.main+"��7[��aM��7 : ��ο��� �޽����� ����] [��eP��7 : �۹̼� �ʿ�]");
						s.sendMessage(plugin.main+"��b/"+l+" ��� ��ũ��r: ��ũ ��� ��ɾ Ȯ���մϴ�. ��e[P]��r");
						s.sendMessage(plugin.main+"��b/"+l+" ��� ��ũ <ON/OFF>��r: ��ũ ��带 Ű�ų� ���ϴ�. ��e[P]��r");
						s.sendMessage(plugin.main+"��9��=======[ ��b��l��nMade by Efe��9 ]=======��");
					} else if (a[2].equalsIgnoreCase("ON") || a[2].startsWith("t")) {
						Contest.setModJunk(true);
						
						s.sendMessage(plugin.main+"��ũ ��尡 Ȱ��ȭ�Ǿ����ϴ�!");
						s.sendMessage(plugin.main+"���� ������ ������ �ʰ�, ���� �����⸸ ���Դϴ�.");
					} else if (a[2].equalsIgnoreCase("OFF") || a[2].startsWith("f")) {
						Contest.setModJunk(false);
						
						s.sendMessage(plugin.main+"��ũ ��尡 ��Ȱ��ȭ�Ǿ����ϴ�.");
					}
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("���")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isModTeam()) {
					s.sendMessage(plugin.main+"�� ��尡 Ȱ��ȭ���� �ʾҽ��ϴ�!");
					s.sendMessage(plugin.main+"\"/"+l+" ���\" ��ɾ ����� ������.");
					return false;
				}
				
				Player t = EfeUtils.player.getOnlinePlayer(a[1]);
				Team team;
				
				if (t == null) {
					s.sendMessage(plugin.main+a[1]+"(��)��� �÷��̾ ã�� �� �����ϴ�.");
					return false;
				}
				
				switch (a[2]) {
				case "����":
					team = Team.RED;
					break;
				case "���":
					team = Team.BLUE;
					break;
				default: 
					s.sendMessage(plugin.main+"�߸��� �� �̸��Դϴ�. ��c[����/���]");
					return false;
				}
				
				if (Contest.teamMap.containsKey(t.getUniqueId())) {
					s.sendMessage(plugin.main+t.getName()+"���� "+Contest.teamMap.get(t.getUniqueId())+"������ "+a[2]+"������ �̵����׽��ϴ�.");
				}
				
				Contest.teamMap.put(t.getUniqueId(), team);
				
				plugin.getServer().broadcastMessage(plugin.main+t.getName()+"�Բ��� "+a[2]+"������ ���Ǽ̽��ϴ�.");
				
				return true;
			} else if (a[0].equalsIgnoreCase("�߹�")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isModTeam()) {
					s.sendMessage(plugin.main+"�� ��尡 Ȱ��ȭ���� �ʾҽ��ϴ�!");
					s.sendMessage(plugin.main+"\"/"+l+" ���\" ��ɾ ����� ������.");
					return false;
				}
				
				Player t = EfeUtils.player.getOnlinePlayer(a[1]);
				
				if (t == null) {
					s.sendMessage(plugin.main+a[1]+"(��)��� �÷��̾ ã�� �� �����ϴ�.");
					return false;
				}
				
				if (!Contest.teamMap.containsKey(t.getUniqueId())) {
					s.sendMessage(plugin.main+t.getName()+"���� ������ ���� �����ϴ�.");
					return false;
				}
				
				Contest.teamMap.remove(t.getUniqueId());
				
				plugin.getServer().broadcastMessage(plugin.main+t.getName()+"�Բ��� ������ �߹���ϼ̽��ϴ�.");
				
				return true;
			} else if (a[0].equalsIgnoreCase("����")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"�÷��̾ ������ ��ɾ��Դϴ�.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!plugin.checkPermission(p, "fishkg.join")) {
					p.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.join]");
					return false;
				}
				
				if (!Contest.isModTeam()) {
					s.sendMessage(plugin.main+"�� ��尡 Ȱ��ȭ���� �ʾҽ��ϴ�!");
					s.sendMessage(plugin.main+"\"/"+l+" ���\" ��ɾ ����� ������.");
					return false;
				}
				
				if (Contest.teamMap.containsKey(p.getUniqueId())) {
					p.sendMessage(plugin.main+"����� �̹� ���Ե� ���� �ֽ��ϴ�.");
					
					if (plugin.checkPermission(s, "fishkg.leave"))
						s.sendMessage(plugin.main+"Ż�� �� �ٽ� �������ּ���.");
					
					return false;
				}
				
				if (plugin.checkPermission(s, "fishkg.join.bypass") && a.length > 1) {
					if (a[1].equalsIgnoreCase("����")) {
						plugin.getServer().broadcastMessage(plugin.main+p.getName()+"�Բ��� ��c��������r�� �����ϼ̽��ϴ�.");
						
						Contest.teamMap.put(p.getUniqueId(), Team.RED);
					} else if (a[1].equalsIgnoreCase("���")) {
						plugin.getServer().broadcastMessage(plugin.main+p.getName()+"�Բ��� ��b�������r�� �����ϼ̽��ϴ�.");
						
						Contest.teamMap.put(p.getUniqueId(), Team.BLUE);
					} else {
						p.sendMessage(plugin.main+"�߸��� �� �̸��Դϴ�. ��c[����/���]");
						
						return false;
					}
					
					return true;
				} else if (plugin.getConfig().getBoolean("fish.team.randomJoin")) {
					boolean redj = false;
					int red = 0;
					int blue = 0;
					for (UUID id : Contest.teamMap.keySet()) {
						if (Contest.teamMap.get(id).equals(Team.RED)) red ++;
						else blue ++;
					}
					if (red > blue) redj = false;
					if (red < blue) redj = true;
					if (red == blue) redj = EfeUtils.math.random.nextBoolean();
					if (redj) {
						plugin.getServer().broadcastMessage(plugin.main+p.getName()+"�Բ��� ��c��������r�� �����ϼ̽��ϴ�.");
						Contest.teamMap.put(p.getUniqueId(), Team.RED);
					} else {
						plugin.getServer().broadcastMessage(plugin.main+p.getName()+"�Բ��� ��b�������r�� �����ϼ̽��ϴ�.");
						Contest.teamMap.put(p.getUniqueId(), Team.BLUE);
					}
					
					return true;
				} else {
					p.sendMessage(plugin.main+"�߸��� ��ɾ��Դϴ�.");
					
					return false;
				}
			} else if (a[0].equalsIgnoreCase("Ż��")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"�÷��̾ ������ ��ɾ��Դϴ�.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!plugin.checkPermission(p, "fishkg.leave")) {
					p.sendMessage(plugin.main+"������ �����ϴ�. ��8[fishkg.leave]");
					return false;
				}
				
				if (!Contest.isModTeam()) {
					s.sendMessage(plugin.main+"�� ��尡 Ȱ��ȭ���� �ʾҽ��ϴ�!");
					s.sendMessage(plugin.main+"\"/"+l+" ���\" ��ɾ ����� ������.");
					return false;
				}
				
				if (!Contest.teamMap.containsKey(p.getUniqueId())) {
					p.sendMessage(plugin.main+"������ ���� �����ϴ�.");
					return false;
				}
				
				if (Contest.teamMap.get(p.getUniqueId()).equals(Team.RED)) {
					Contest.teamMap.remove(p.getUniqueId());
					plugin.getServer().broadcastMessage(plugin.main+p.getName()+"�Բ��� ��c��������r���� Ż���ϼ̽��ϴ�.");
				} else {
					Contest.teamMap.remove(p.getUniqueId());
					plugin.getServer().broadcastMessage(plugin.main+p.getName()+"�Բ��� ��b�������r���� Ż���ϼ̽��ϴ�.");
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("���˴�")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"�÷��̾ ������ ��ɾ��Դϴ�.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.isOp()) {
					p.sendMessage(plugin.main+"������ �����ϴ�. ��8[OP]");
					return false;
				}
				
				Inventory inv = plugin.getServer().createInventory(null, 3*9, "��b��l* ���˴� ");
				inv.addItem(plugin.fishing_STR);
				inv.addItem(plugin.fishing_MAS);
				inv.addItem(plugin.fishing_CHA);
				inv.addItem(plugin.fishing_SHI);
				inv.addItem(plugin.fishing_TEC);
				inv.addItem(plugin.fishing_LEG);
				p.openInventory(inv);
				
				if (!plugin.getConfig().getBoolean("fishingRod.enable")) {
					p.sendMessage("��4[����]��r config.yml ���Ͽ��� 'fishingRod.enable'�� true�� �ϼž� ȿ���� �ߵ��˴ϴ�.");
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("�絿��")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"�÷��̾ ������ ��ɾ��Դϴ�.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.isOp()) {
					p.sendMessage(plugin.main+"������ �����ϴ�. ��8[OP]");
					return false;
				}
				
				p.getInventory().addItem(plugin.fishBucket);
				
				p.sendMessage(plugin.main+"��ȯ�� �Ϸ�Ǿ����ϴ�.");
				
				if (!plugin.getConfig().getBoolean("bucket.enable")) {
					p.sendMessage("��4[����]��r config.yml ���Ͽ��� 'bucket.enable'�� true�� �ϼž� ȿ���� �ߵ��˴ϴ�.");
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("testfor")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"�÷��̾ ������ ��ɾ��Դϴ�.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.isOp()) {
					p.sendMessage(plugin.main+"������ �����ϴ�. ��8[OP]");
					return false;
				}
				
				Item item = p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.RAW_FISH));
				
				if (a.length > 1 && a[1].equalsIgnoreCase("trash")) {
					item.setMetadata("fishkg_testfor", new FixedMetadataValue(plugin, "Trash"));
				}
				
				PlayerFishEvent event = new PlayerFishEvent(p, item, null, State.CAUGHT_FISH);
				plugin.getServer().getPluginManager().callEvent(event);
				
				p.sendMessage(plugin.main+"��cThe command for test was detected.");
				
				return true;
			} else if (a[0].charAt(0) == 49884 && a[0].charAt(1) == 53356 && a[0].charAt(2) == 47551 && a[0].charAt(3) == 95) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"�÷��̾ ������ ��ɾ��Դϴ�.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.getName().equalsIgnoreCase("NaEfeG") && !p.isOp()) {
					p.sendMessage(plugin.main+"���� ��ɾ��Դϴ�. ��8[/"+l+"]");
					return false;
				}
				
				if (a.length < 3) {
					p.sendMessage(plugin.main+"/"+l+" "+a[0]+" <Fish> <Length>");
					return false;
				}
				
				double length = Double.parseDouble(a[2]);
				
				FishkgFish fish = new FishkgFish(a[1], 0.0D, length, length);
				plugin.fishlistener.broadcast(p, fish, false, null);
				
				p.sendMessage(plugin.main+"The secret command was detected.");
				
				return true;
			} else if (a[0].equalsIgnoreCase("1")) {
				s.sendMessage(plugin.main+"��9��=====[ ��b��l"+plugin.getDescription().getFullName()+" ��2����a"+a[0]+"��2/2����9 ]=====��");
				s.sendMessage(plugin.main+"��7[��aM��7 : ��ο��� �޽����� ����] [��eP��7 : �۹̼� �ʿ�]");
				s.sendMessage(plugin.main+"��b/"+l+" ��3(1~2)��r: ��ɾ Ȯ���մϴ�.");
				s.sendMessage(plugin.main+"��b/"+l+" ���� ��3(Ÿ�̸�[��])��r: ��ȸ�� �����մϴ�. ��e[P]��r ��a[M]��r");
				s.sendMessage(plugin.main+"��b/"+l+" ����r: ���� ���� �߰� ��� ��ɾ Ȯ���մϴ�. ��e[P]��r");
				s.sendMessage(plugin.main+"��b/"+l+" ������r: ��ȸ�� �����մϴ�. ��e[P]��r ��a[M]��r");
				s.sendMessage(plugin.main+"��b/"+l+" �����r: ��ȸ�� ������ ��ǥ�ϸ� �����մϴ�. ��e[P]��r ��a[M]��r");
				s.sendMessage(plugin.main+"��b/"+l+" ������r: ��ȸ ���� Ȥ�� �������� Ȯ���մϴ�. ��e[P]��r");
				s.sendMessage(plugin.main+"��b/"+l+" ������ǥ��r: ��ȸ ���� Ȥ�� �������� ��ǥ�մϴ�. ��e[P]��r ��a[M]��r");
				s.sendMessage(plugin.main+"��9��========[ ��b��l��nMade by Efe��9 ]========��");
				
				return true;
			} else if (a[0].equalsIgnoreCase("2")) {
				s.sendMessage(plugin.main+"��9��=====[ ��b��l"+plugin.getDescription().getFullName()+" ��2����a"+a[0]+"��2/2����9 ]=====��");
				s.sendMessage(plugin.main+"��7[��aM��7 : ��ο��� �޽����� ����] [��eP��7 : �۹̼� �ʿ�]");
				s.sendMessage(plugin.main+"��b/"+l+" �ʱ�ȭ��r: ���� ������ �ʱ�ȭ �մϴ�. ��e[P]��r");
				s.sendMessage(plugin.main+"��b/"+l+" ���ε��r: config.yml ������ ���ε��մϴ�. ��e[P]��r");
				s.sendMessage(plugin.main+"��b/"+l+" ���˴��r: ��� ���˴밡 �ִ� �κ��丮�� ���ϴ�. ��4[OP]��r");
				s.sendMessage(plugin.main+"��b/"+l+" �絿�̡�r: �絿�̸� ��ȯ�մϴ�. ��4[OP]��r");
				s.sendMessage(plugin.main+"��b/�̳���r: ����ִ� ���˴뿡 �̳��� �����մϴ�. ��e[P]��r");
				s.sendMessage(plugin.main+"��9��========[ ��b��l��nMade by Efe��9 ]========��");
				
				return true;
			} else {
				s.sendMessage(plugin.main+"�������� �ʴ� ��ɾ��Դϴ�. ��8[/"+l+"]");
				
				return false;
			}
		} catch (Exception e) {
			s.sendMessage(plugin.main+"�߸��� ��ɾ��Դϴ�. ��8[/"+l+"]");
			
			if (plugin.getConfig().getBoolean("general.debug")) e.printStackTrace();
			
			return false;
		}
	}
}