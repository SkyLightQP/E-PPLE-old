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
			list.add("시작");
			list.add("중지");
			list.add("종료");
			list.add("순위");
			list.add("순위발표");
			
			list.add("모드");
			list.add("고용");
			list.add("추방");
			list.add("참가");
			list.add("탈퇴");
			
			list.add("초기화");
			list.add("리로드");
			list.add("낚싯대");
			list.add("양동이");
		} else if (a[0].equalsIgnoreCase("모드")) {
			list.add("팀");
			list.add("정크");
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
			
			if (a[0].equalsIgnoreCase("시작")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.control]");
					return false;
				}
				
				if (Contest.isEnabled()) {
					s.sendMessage(plugin.main+"낚시대회가 이미 진행중입니다.");
					return false;
				}
				
				if (a.length == 2 && EfeUtils.string.isInteger(a[1])) {
					Contest.start(Long.parseLong(a[1]) * 1200L);
					plugin.getServer().broadcastMessage(plugin.main+"낚시대회가 시작되었습니다! "+Integer.parseInt(a[1])+"분 후에 낚시대회가 종료됩니다.");
				} else {
					Contest.start(-1);
					plugin.getServer().broadcastMessage(plugin.main+"낚시대회가 시작되었습니다!");
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("중지")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"낚시대회가 진행중이지 않습니다.");
					return false;
				}
				
				Contest.quit(false);
				
				plugin.getServer().broadcastMessage(plugin.main+"낚시대회가 종료되었습니다.");
				
				return true;
			} else if (a[0].equalsIgnoreCase("종료")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"낚시대회가 진행중이지 않습니다.");
					return false;
				}
				
				Contest.quit(true);
				
				return true;
			} else if (a[0].equalsIgnoreCase("순위")) {
				if (!plugin.checkPermission(s, "fishkg.stat")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.stat]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"낚시대회가 진행중이지 않습니다.");
					return false;
				}
				
				Contest.sendStatus(s);
				
				return true;
			} else if (a[0].equalsIgnoreCase("순위발표")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"낚시대회가 진행중이지 않습니다.");
					return false;
				}
				
				for (Player all : EfeUtils.player.getOnlinePlayers()) {
					Contest.sendStatus(all);
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("초기화")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isEnabled()) {
					s.sendMessage(plugin.main+"낚시대회가 진행중이지 않습니다.");
					return false;
				}
				
				Contest.init();
				
				s.sendMessage(plugin.main+"낚시대회가 초기화 되었습니다.");
				
				return true;
			} else if (a[0].equalsIgnoreCase("리로드")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.control]");
					return false;
				}
				
				plugin.reloadConfig();
				
				s.sendMessage(plugin.main+"Config 리로드를 완료했습니다.");
				s.sendMessage(plugin.main+"§c※ 일부 설정은 플러그인을 리로드해야 반영될 수 있습니다!");
				
				return true;
			} else if (a[0].equalsIgnoreCase("모드")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.control]");
					return false;
				}
				
				if (a.length == 1) {
					s.sendMessage(plugin.main+"§9▒=====[ §b§l"+plugin.getDescription().getFullName()+" §2「모드」§3 ]=====▒");
					s.sendMessage(plugin.main+"§7[§aM§7 : 모두에게 메시지를 전달] [§eP§7 : 퍼미션 필요]");
					s.sendMessage(plugin.main+"§b/"+l+" 모드§r: 대회의 모드 명령어를 확인합니다. §e[P]§r");
					s.sendMessage(plugin.main+"§b/"+l+" 모드 팀§r: 팀 모드 명령어를 확인합니다. §e[P]§r");
					s.sendMessage(plugin.main+"§b/"+l+" 모드 정크§r: 정크 모드 명령어를 확인합니다. §e[P]§r");
					s.sendMessage(plugin.main+"§9▒=======[ §b§l§nMade by Efe§9 ]=======▒");
				} else if (a[1].equalsIgnoreCase("팀")) {
					
					if (a.length == 2) {
						s.sendMessage(plugin.main+"§9▒=====[ §b§l"+plugin.getDescription().getFullName()+" §2「팀」§3 ]=====▒");
						s.sendMessage(plugin.main+"§7[§aM§7 : 모두에게 메시지를 전달] [§eP§7 : 퍼미션 필요]");
						s.sendMessage(plugin.main+"§b/"+l+" 모드 팀§r: 팀 모드 명령어를 확인합니다. §e[P]§r");
						s.sendMessage(plugin.main+"§b/"+l+" 모드 팀 <ON/OFF>§r: 팀 모드를 키거나 끕니다. §e[P]§r");
						s.sendMessage(plugin.main+"§b/"+l+" 고용 <플레이어> <레드/블루>§r: 팀에 가입시킵니다. §e[P]§r §a[M]§r");
						s.sendMessage(plugin.main+"§b/"+l+" 추방 <플레이어>§r: <플레이어>를 가입된 팀에서 추방합니다. §e[P]§r §a[M]§r");
						s.sendMessage(plugin.main+"§b/"+l+" 참가 <레드/블루>§r: 팀에 참가합니다. §e[P]§r §a[M]§r");
						s.sendMessage(plugin.main+"§b/"+l+" 탈퇴§r: 팀에서 탈퇴합니다. §e[P]§r §a[M]§r");
						s.sendMessage(plugin.main+"§9▒=======[ §b§l§nMade by Efe§9 ]=======▒");
					} else if (a[2].equalsIgnoreCase("ON") || a[2].startsWith("t")) {
						Contest.setModTeam(true);
						
						s.sendMessage(plugin.main+"팀 모드가 활성화되었습니다!");
						s.sendMessage(plugin.main+"유저들을 팀에 가입시켜주세요.");
						s.sendMessage(plugin.main+"§c[!]§r 팀에 가입하지 않으면 대회 진행이 안 됩니다!");
					} else if (a[2].equalsIgnoreCase("OFF") || a[2].startsWith("f")) {
						Contest.setModTeam(false);
						
						s.sendMessage(plugin.main+"팀 모드가 비활성화되었습니다.");
					}
				} else if (a[1].equalsIgnoreCase("정크")) {
					if (a.length == 2) {
						s.sendMessage(plugin.main+"§9▒=====[ §b§l"+plugin.getDescription().getFullName()+" §2「팀」§3 ]=====▒");
						s.sendMessage(plugin.main+"§7[§aM§7 : 모두에게 메시지를 전달] [§eP§7 : 퍼미션 필요]");
						s.sendMessage(plugin.main+"§b/"+l+" 모드 정크§r: 정크 모드 명령어를 확인합니다. §e[P]§r");
						s.sendMessage(plugin.main+"§b/"+l+" 모드 정크 <ON/OFF>§r: 정크 모드를 키거나 끕니다. §e[P]§r");
						s.sendMessage(plugin.main+"§9▒=======[ §b§l§nMade by Efe§9 ]=======▒");
					} else if (a[2].equalsIgnoreCase("ON") || a[2].startsWith("t")) {
						Contest.setModJunk(true);
						
						s.sendMessage(plugin.main+"정크 모드가 활성화되었습니다!");
						s.sendMessage(plugin.main+"이제 물고기는 낚이지 않고, 오직 쓰레기만 낚입니다.");
					} else if (a[2].equalsIgnoreCase("OFF") || a[2].startsWith("f")) {
						Contest.setModJunk(false);
						
						s.sendMessage(plugin.main+"정크 모드가 비활성화되었습니다.");
					}
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("고용")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isModTeam()) {
					s.sendMessage(plugin.main+"팀 모드가 활성화되지 않았습니다!");
					s.sendMessage(plugin.main+"\"/"+l+" 모드\" 명령어를 사용해 보세요.");
					return false;
				}
				
				Player t = EfeUtils.player.getOnlinePlayer(a[1]);
				Team team;
				
				if (t == null) {
					s.sendMessage(plugin.main+a[1]+"(이)라는 플레이어를 찾을 수 없습니다.");
					return false;
				}
				
				switch (a[2]) {
				case "레드":
					team = Team.RED;
					break;
				case "블루":
					team = Team.BLUE;
					break;
				default: 
					s.sendMessage(plugin.main+"잘못된 팀 이름입니다. §c[레드/블루]");
					return false;
				}
				
				if (Contest.teamMap.containsKey(t.getUniqueId())) {
					s.sendMessage(plugin.main+t.getName()+"님을 "+Contest.teamMap.get(t.getUniqueId())+"팀에서 "+a[2]+"팀으로 이동시켰습니다.");
				}
				
				Contest.teamMap.put(t.getUniqueId(), team);
				
				plugin.getServer().broadcastMessage(plugin.main+t.getName()+"님께서 "+a[2]+"팀으로 고용되셨습니다.");
				
				return true;
			} else if (a[0].equalsIgnoreCase("추방")) {
				if (!plugin.checkPermission(s, "fishkg.control")) {
					s.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.control]");
					return false;
				}
				
				if (!Contest.isModTeam()) {
					s.sendMessage(plugin.main+"팀 모드가 활성화되지 않았습니다!");
					s.sendMessage(plugin.main+"\"/"+l+" 모드\" 명령어를 사용해 보세요.");
					return false;
				}
				
				Player t = EfeUtils.player.getOnlinePlayer(a[1]);
				
				if (t == null) {
					s.sendMessage(plugin.main+a[1]+"(이)라는 플레이어를 찾을 수 없습니다.");
					return false;
				}
				
				if (!Contest.teamMap.containsKey(t.getUniqueId())) {
					s.sendMessage(plugin.main+t.getName()+"님은 가입한 팀이 없습니다.");
					return false;
				}
				
				Contest.teamMap.remove(t.getUniqueId());
				
				plugin.getServer().broadcastMessage(plugin.main+t.getName()+"님께서 팀에서 추방당하셨습니다.");
				
				return true;
			} else if (a[0].equalsIgnoreCase("참가")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"플레이어만 가능한 명령어입니다.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!plugin.checkPermission(p, "fishkg.join")) {
					p.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.join]");
					return false;
				}
				
				if (!Contest.isModTeam()) {
					s.sendMessage(plugin.main+"팀 모드가 활성화되지 않았습니다!");
					s.sendMessage(plugin.main+"\"/"+l+" 모드\" 명령어를 사용해 보세요.");
					return false;
				}
				
				if (Contest.teamMap.containsKey(p.getUniqueId())) {
					p.sendMessage(plugin.main+"당신은 이미 가입된 팀이 있습니다.");
					
					if (plugin.checkPermission(s, "fishkg.leave"))
						s.sendMessage(plugin.main+"탈퇴 후 다시 가입해주세요.");
					
					return false;
				}
				
				if (plugin.checkPermission(s, "fishkg.join.bypass") && a.length > 1) {
					if (a[1].equalsIgnoreCase("레드")) {
						plugin.getServer().broadcastMessage(plugin.main+p.getName()+"님께서 §c레드팀§r에 참가하셨습니다.");
						
						Contest.teamMap.put(p.getUniqueId(), Team.RED);
					} else if (a[1].equalsIgnoreCase("블루")) {
						plugin.getServer().broadcastMessage(plugin.main+p.getName()+"님께서 §b블루팀§r에 참가하셨습니다.");
						
						Contest.teamMap.put(p.getUniqueId(), Team.BLUE);
					} else {
						p.sendMessage(plugin.main+"잘못된 팀 이름입니다. §c[레드/블루]");
						
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
						plugin.getServer().broadcastMessage(plugin.main+p.getName()+"님께서 §c레드팀§r에 참가하셨습니다.");
						Contest.teamMap.put(p.getUniqueId(), Team.RED);
					} else {
						plugin.getServer().broadcastMessage(plugin.main+p.getName()+"님께서 §b블루팀§r에 참가하셨습니다.");
						Contest.teamMap.put(p.getUniqueId(), Team.BLUE);
					}
					
					return true;
				} else {
					p.sendMessage(plugin.main+"잘못된 명령어입니다.");
					
					return false;
				}
			} else if (a[0].equalsIgnoreCase("탈퇴")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"플레이어만 가능한 명령어입니다.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!plugin.checkPermission(p, "fishkg.leave")) {
					p.sendMessage(plugin.main+"권한이 없습니다. §8[fishkg.leave]");
					return false;
				}
				
				if (!Contest.isModTeam()) {
					s.sendMessage(plugin.main+"팀 모드가 활성화되지 않았습니다!");
					s.sendMessage(plugin.main+"\"/"+l+" 모드\" 명령어를 사용해 보세요.");
					return false;
				}
				
				if (!Contest.teamMap.containsKey(p.getUniqueId())) {
					p.sendMessage(plugin.main+"가입한 팀이 없습니다.");
					return false;
				}
				
				if (Contest.teamMap.get(p.getUniqueId()).equals(Team.RED)) {
					Contest.teamMap.remove(p.getUniqueId());
					plugin.getServer().broadcastMessage(plugin.main+p.getName()+"님께서 §c레드팀§r에서 탈퇴하셨습니다.");
				} else {
					Contest.teamMap.remove(p.getUniqueId());
					plugin.getServer().broadcastMessage(plugin.main+p.getName()+"님께서 §b블루팀§r에서 탈퇴하셨습니다.");
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("낚싯대")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"플레이어만 가능한 명령어입니다.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.isOp()) {
					p.sendMessage(plugin.main+"권한이 없습니다. §8[OP]");
					return false;
				}
				
				Inventory inv = plugin.getServer().createInventory(null, 3*9, "§b§l* 낚싯대 ");
				inv.addItem(plugin.fishing_STR);
				inv.addItem(plugin.fishing_MAS);
				inv.addItem(plugin.fishing_CHA);
				inv.addItem(plugin.fishing_SHI);
				inv.addItem(plugin.fishing_TEC);
				inv.addItem(plugin.fishing_LEG);
				p.openInventory(inv);
				
				if (!plugin.getConfig().getBoolean("fishingRod.enable")) {
					p.sendMessage("§4[주의]§r config.yml 파일에서 'fishingRod.enable'을 true로 하셔야 효과가 발동됩니다.");
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("양동이")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"플레이어만 가능한 명령어입니다.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.isOp()) {
					p.sendMessage(plugin.main+"권한이 없습니다. §8[OP]");
					return false;
				}
				
				p.getInventory().addItem(plugin.fishBucket);
				
				p.sendMessage(plugin.main+"소환이 완료되었습니다.");
				
				if (!plugin.getConfig().getBoolean("bucket.enable")) {
					p.sendMessage("§4[주의]§r config.yml 파일에서 'bucket.enable'을 true로 하셔야 효과가 발동됩니다.");
				}
				
				return true;
			} else if (a[0].equalsIgnoreCase("testfor")) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"플레이어만 가능한 명령어입니다.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.isOp()) {
					p.sendMessage(plugin.main+"권한이 없습니다. §8[OP]");
					return false;
				}
				
				Item item = p.getWorld().dropItem(p.getLocation(), new ItemStack(Material.RAW_FISH));
				
				if (a.length > 1 && a[1].equalsIgnoreCase("trash")) {
					item.setMetadata("fishkg_testfor", new FixedMetadataValue(plugin, "Trash"));
				}
				
				PlayerFishEvent event = new PlayerFishEvent(p, item, null, State.CAUGHT_FISH);
				plugin.getServer().getPluginManager().callEvent(event);
				
				p.sendMessage(plugin.main+"§cThe command for test was detected.");
				
				return true;
			} else if (a[0].charAt(0) == 49884 && a[0].charAt(1) == 53356 && a[0].charAt(2) == 47551 && a[0].charAt(3) == 95) {
				if (!(s instanceof Player)) {
					s.sendMessage(plugin.main+"플레이어만 가능한 명령어입니다.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.getName().equalsIgnoreCase("NaEfeG") && !p.isOp()) {
					p.sendMessage(plugin.main+"없는 명령어입니다. §8[/"+l+"]");
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
				s.sendMessage(plugin.main+"§9▒=====[ §b§l"+plugin.getDescription().getFullName()+" §2「§a"+a[0]+"§2/2」§9 ]=====▒");
				s.sendMessage(plugin.main+"§7[§aM§7 : 모두에게 메시지를 전달] [§eP§7 : 퍼미션 필요]");
				s.sendMessage(plugin.main+"§b/"+l+" §3(1~2)§r: 명령어를 확인합니다.");
				s.sendMessage(plugin.main+"§b/"+l+" 시작 §3(타이머[분])§r: 대회를 시작합니다. §e[P]§r §a[M]§r");
				s.sendMessage(plugin.main+"§b/"+l+" 모드§r: 팀전 등의 추가 모드 명령어를 확인합니다. §e[P]§r");
				s.sendMessage(plugin.main+"§b/"+l+" 중지§r: 대회를 중지합니다. §e[P]§r §a[M]§r");
				s.sendMessage(plugin.main+"§b/"+l+" 종료§r: 대회를 순위를 발표하며 종료합니다. §e[P]§r §a[M]§r");
				s.sendMessage(plugin.main+"§b/"+l+" 순위§r: 대회 순위 혹은 팀점수를 확인합니다. §e[P]§r");
				s.sendMessage(plugin.main+"§b/"+l+" 순위발표§r: 대회 순위 혹은 팀점수를 발표합니다. §e[P]§r §a[M]§r");
				s.sendMessage(plugin.main+"§9▒========[ §b§l§nMade by Efe§9 ]========▒");
				
				return true;
			} else if (a[0].equalsIgnoreCase("2")) {
				s.sendMessage(plugin.main+"§9▒=====[ §b§l"+plugin.getDescription().getFullName()+" §2「§a"+a[0]+"§2/2」§9 ]=====▒");
				s.sendMessage(plugin.main+"§7[§aM§7 : 모두에게 메시지를 전달] [§eP§7 : 퍼미션 필요]");
				s.sendMessage(plugin.main+"§b/"+l+" 초기화§r: 현재 순위를 초기화 합니다. §e[P]§r");
				s.sendMessage(plugin.main+"§b/"+l+" 리로드§r: config.yml 파일을 리로드합니다. §e[P]§r");
				s.sendMessage(plugin.main+"§b/"+l+" 낚싯대§r: 모든 낚싯대가 있는 인벤토리를 엽니다. §4[OP]§r");
				s.sendMessage(plugin.main+"§b/"+l+" 양동이§r: 양동이를 소환합니다. §4[OP]§r");
				s.sendMessage(plugin.main+"§b/미끼§r: 들고있는 낚싯대에 미끼를 장착합니다. §e[P]§r");
				s.sendMessage(plugin.main+"§9▒========[ §b§l§nMade by Efe§9 ]========▒");
				
				return true;
			} else {
				s.sendMessage(plugin.main+"존재하지 않는 명령어입니다. §8[/"+l+"]");
				
				return false;
			}
		} catch (Exception e) {
			s.sendMessage(plugin.main+"잘못된 명령어입니다. §8[/"+l+"]");
			
			if (plugin.getConfig().getBoolean("general.debug")) e.printStackTrace();
			
			return false;
		}
	}
}