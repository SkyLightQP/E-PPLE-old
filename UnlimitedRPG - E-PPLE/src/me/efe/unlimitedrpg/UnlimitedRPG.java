package me.efe.unlimitedrpg;

import java.util.ArrayList;
import java.util.List;

import me.efe.efegear.EfeUtil;
import me.efe.efegear.util.Token;
import me.efe.efegear.util.UpdateChecker;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.additory.ChatListener;
import me.efe.unlimitedrpg.customexp.CustomExp;
import me.efe.unlimitedrpg.party.Party;
import me.efe.unlimitedrpg.party.PartyAPI;
import me.efe.unlimitedrpg.party.PartyListener;
import me.efe.unlimitedrpg.playerinfo.PlayerInfo;
import me.efe.unlimitedrpg.playerinfo.PlayerInfoGUI;
import me.efe.unlimitedrpg.stat.StatGUI;
import me.efe.unlimitedrpg.stat.StatListener;
import me.efe.unlimitedrpg.unlimitedtag.TagType;
import me.efe.unlimitedrpg.unlimitedtag.UnlimitedTag;
import me.efe.unlimitedrpg.unlimitedtag.UnlimitedTagAPI;
import me.efe.unlimitedrpg.unlimitedtag.WGListener;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UnlimitedRPG extends JavaPlugin {
	private static UnlimitedRPG instance;
	public EfeUtil util;
	public String main = "";
	
	public StatGUI statGUI;
	public StatListener statListener;
	public CustomExp customExp;
	public UnlimitedTag unlimitedTag;
	public WGListener unlimitedTagWG;
	public PlayerInfo playerInfo;
	public PlayerInfoGUI playerInfoGUI;
	public PartyListener party;
	
	public void onDisable() {
		util.logDisable();
	}
	
	public void onEnable() {
		instance = this;
		util = new EfeUtil(this);
		util.logEnable();
		
		saveDefaultConfig();
		
		if (getConfig().getBoolean("update-check")) {
			double nowV = Double.parseDouble(this.getDescription().getVersion());
			double newV = UpdateChecker.getVersion("UnlimitedRPG");
			
			if (nowV < newV) {
				util.console("§a=======================================");
				util.console("§a업데이트가 발견되었습니다!");
				util.console("§a현 버전: §3"+nowV);
				util.console("§a신버전: §b"+newV);
				util.console("§a카페 혹은 제작자의 블로그에서 새로운 버전을 다운로드하세요.");
				util.console("§ahttp://blog.naver.com/cwjh1002");
				util.console("§a=======================================");
				
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		boolean vers = !getServer().getBukkitVersion().startsWith("1.5.2");
		if (!vers) {
			util.console("§c=======================================");
			util.console("§c당신이 잘 만든 서버를 열고있다고 생각하시나요?");
			util.console("§c절대 아닙니다. 겨우 그런 서버로 자부심을 가지시면 안 되죠.");
			util.console("§c최소한 §n해외 서버§c라도 가보셨습니까? 한국의 현실을 아시나요?");
			util.console("§c이 문구에 자극을 받고, \"잘 만든 서버\"를 만들고 싶다면");
			util.console("§c§l지금 당장 마인크래프트 최신 버전으로 서버를 계획하세요.");
			util.console("§c친목 서버를 운영하며 매니아층과 함께 놀던");
			util.console("§c한국 마인크래프트에 새로운 역사를 만들던");
			util.console("§c그것은 당신의 선택입니다.");
			util.console("§c=======================================");
			util.console("§a10초 뒤 서버 구동이 이어집니다...");
			
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (!getConfig().getBoolean("use.stat") && 
				!getConfig().getBoolean("use.custom-exp") &&
				!getConfig().getBoolean("use.unlimited-tag") &&
				!getConfig().getBoolean("use.player-info") &&
				!getConfig().getBoolean("use.party")) {
			util.console(main+"§c모든 기능이 꺼져있습니다. Config에서 기능을 사용해주세요.");
			util.console(main+"§c모든 기능이 꺼져있습니다. Config에서 기능을 사용해주세요.");
			util.console(main+"§c모든 기능이 꺼져있습니다. Config에서 기능을 사용해주세요.");
			
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (getConfig().getBoolean("use.stat") && vers) {
			statGUI = new StatGUI(this);
			statListener = new StatListener(this);
			util.register(statGUI);
			util.register(statListener);
			
			util.console("["+util.getPluginName()+"] 스텟 기능이 활성화되었습니다.");
		}
		
		if (getConfig().getBoolean("use.custom-exp")) {
			customExp = new CustomExp(this);
			util.register(customExp);
			
			util.console("["+util.getPluginName()+"] 커스텀 Exp 기능이 활성화되었습니다.");
		}
		
		if (getConfig().getBoolean("use.unlimited-tag") && getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
			unlimitedTag = new UnlimitedTag(this);
			util.register(unlimitedTag);
			
			util.console("["+util.getPluginName()+"] 언리미티드 태그 기능이 활성화되었습니다.");
			util.console("["+util.getPluginName()+"] 성공적으로 ProtocolLib 플러그인과 연동되었습니다.");
			
			if (getServer().getPluginManager().getPlugin("WorldGuard") != null && getConfig().getBoolean("unlimited-tag.worldguard")) {
				unlimitedTagWG = new WGListener(this);
				util.register(unlimitedTagWG);
				
				util.console("["+util.getPluginName()+"] 성공적으로 WorldGuard 플러그인와 연동되었습니다.");
			}
		}
		
		if (getConfig().getBoolean("use.player-info") && vers) {
			playerInfo = new PlayerInfo(this);
			playerInfoGUI = new PlayerInfoGUI(this);
			util.register(playerInfo);
			util.register(playerInfoGUI);
			
			util.console("["+util.getPluginFullName()+"] 플레이어 정보 기능이 활성화되었습니다.");
		}
		
		if (getConfig().getBoolean("use.party")) {
			party = new PartyListener(this);
			util.register(party);
			this.getCommand("파티").setExecutor(party);
			
			util.console("["+util.getPluginFullName()+"] 파티 기능이 활성화되었습니다.");
		}
	}
	
	public static UnlimitedRPG getInstance() {
		return instance;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
		List<String> list = new ArrayList<String>();
		
		if (l.equalsIgnoreCase("urpg") || l.equalsIgnoreCase("언리밋")) {
			if (a.length == 0) {
				list.add("리로드");
				list.add("SP주문서");
				list.add("SP초기화주문서");
			}
		} else if (l.equalsIgnoreCase("태그")) {
			if (a.length == 1) {
				list.add("추가");
				list.add("제거");
			} else if (a.length == 2) {
				for (TagType tag : TagType.values()) {
					list.add(tag.toString());
				}
			} else if (a.length == 3 && a[1].equalsIgnoreCase("귀속")) {
				for (Player p : util.getOnlinePlayers()) {
					list.add(p.getName());
				}
			}
		}
		
		return list;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("urpg") || l.equalsIgnoreCase("언리밋")) {
			try {
				if (!s.isOp()) {
					s.sendMessage(main+"OP만 사용 가능한 명령어입니다.");
					return false;
				}
				
				if (a.length == 0) {
					s.sendMessage(main+"§d◎§5=====[ §d§l"+util.getPluginFullName()+"§5 ]=====§d◎");
					s.sendMessage(main+"/"+l+"§7: 명령어 확인");
					s.sendMessage(main+"/"+l+" 리로드§7: Config 리로드 (일부 변경은 서버 리로드 필요)");
					if (getConfig().getBoolean("use.stat"))
						s.sendMessage(main+"/"+l+" SP주문서§7: SP 주문서를 소환");
					if (getConfig().getBoolean("use.stat"))
						s.sendMessage(main+"/"+l+" SP초기화주문서§7: SP 초기화 주문서를 소환");
					if (getConfig().getBoolean("use.stat"))
						s.sendMessage(main+"/스텟§7: 스텟 GUI");
					if (getConfig().getBoolean("use.unlimited-tag"))
						s.sendMessage(main+"/태그§7: 태그 명령어 확인");
					if (getConfig().getBoolean("use.party"))
						s.sendMessage(main+"/파티 명령어§7: 파티 명령어 확인");
					s.sendMessage(main+"§d◎§5======[ §d§l* Made by Efe *§5 ]======§d◎");
				} else if (a[0].equalsIgnoreCase("리로드")) {
					reloadConfig();
					
					s.sendMessage(main+"리로드가 완료되었습니다.");
				} else if (a[0].equalsIgnoreCase("SP주문서")) {
					if (!util.isPlayer(s)) {
						s.sendMessage(main+"플레이어만 입력할 수 있는 명령어입니다.");
						return false;
					}
					
					if (!getConfig().getBoolean("use.stat")) {
						s.sendMessage(main+"스텟 기능이 활성화되지 않았습니다.");
						return false;
					}
					
					Player p = (Player) s;
					
					p.getInventory().addItem(statListener.SPpaper);
					
					p.sendMessage(main+"소환이 완료되었습니다.");
				} else if (a[0].equalsIgnoreCase("SP초기화주문서")) {
					if (!util.isPlayer(s)) {
						s.sendMessage(main+"플레이어만 입력할 수 있는 명령어입니다.");
						return false;
					}
					
					if (!getConfig().getBoolean("use.stat")) {
						s.sendMessage(main+"스텟 기능이 활성화되지 않았습니다.");
						return false;
					}
					
					Player p = (Player) s;
					
					p.getInventory().addItem(statListener.SPreset);
					
					p.sendMessage(main+"소환이 완료되었습니다.");
				}
			} catch (Exception e) {
				s.sendMessage(main+"잘못된 명령어입니다. §8[/"+l+"]");
			}
		} else if (l.equalsIgnoreCase("스텟")) {
			if (!getConfig().getBoolean("use.stat")) {
				s.sendMessage(main+"스텟 기능이 활성화되지 않았습니다.");
				s.sendMessage(main+"가이드를 참조하여 Config를 수정해주세요.");
				return false;
			}
			
			try {
				if (!util.isPlayer(s)) {
					s.sendMessage(main+"플레이어만 입력할 수 있는 명령어입니다.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.hasPermission("urpg.stat")) {
					s.sendMessage(main+"권한이 없습니다! §8[urpg.stat]");
					return false;
				}
				
				statGUI.openGUI(p);
			} catch (Exception e) {
				s.sendMessage(main+"잘못된 명령어입니다. §8[/스텟]");
			}
		} else if (l.equalsIgnoreCase("태그")) {
			if (!getConfig().getBoolean("use.unlimited-tag")) {
				s.sendMessage(main+"스텟 기능이 활성화되지 않았습니다.");
				return false;
			}
			
			try {
				if (!s.isOp()) {
					s.sendMessage(main+"OP만 사용 가능한 명령어입니다.");
					return false;
				}
				
				if (a.length == 0) {
					s.sendMessage(main+"§d◎§5=====[ §d§l"+util.getPluginFullName()+"§5 ]=====§d◎");
					s.sendMessage(main+"/태그");
					s.sendMessage(main+"/태그 추가 <태그 이름> (값)");
					s.sendMessage(main+"/태그 제거 <태그 이름> (값)");
					s.sendMessage(main+"§l* 태그 리스트:");
					s.sendMessage(main+"  "+TagType.getTypes());
					s.sendMessage(main+"§d◎§5======[ §d§l* Made by Efe *§5 ]======§d◎");
				} else if (a[0].equalsIgnoreCase("추가")) {
					if (!util.isPlayer(s)) {
						s.sendMessage(main+"플레이어만 입력할 수 있는 명령어입니다.");
						return false;
					}
					
					Player p = (Player) s;
					
					if (p.getItemInHand().getType().equals(Material.AIR)) {
						p.sendMessage(main+"손이 비었군요? 유감입니다.");
						return false;
					}
					
					if (a.length < 2) {
						p.sendMessage(main+"잘못된 명령어입니다. §8[/태그 추가 <태그>]");
						return false;
					}
					
					TagType tag = TagType.fromString(a[1]);
					
					if (!tag.hasData() && UnlimitedTagAPI.hasTag(p.getItemInHand(), tag)) {
						p.sendMessage(main+"이 아이템에는 이미 해당 태그가 존재합니다!");
						return false;
					}
					
					if (tag.hasData() && a.length < 3) {
						if (tag.equals(TagType.DEADLINE) || tag.equals(TagType.DEADLINE_ON_PICKUP)) {
							p.sendMessage(main+"잘못된 명령어입니다. §8[/태그 추가 "+a[1]+" §c<년도> <월> <일> <시> <분>§8]");
							if (tag.equals(TagType.DEADLINE)) p.sendMessage(main+"§9ex)§7 /태그 추가 "+a[1]+" 2014 05 01 18 30");
							if (tag.equals(TagType.DEADLINE_ON_PICKUP)) p.sendMessage(main+"§9ex)§7 /태그 추가 "+a[1]+" 0000 00 01 12 00");
							return false;
						}
						
						p.sendMessage(main+"잘못된 명령어입니다. §8[/태그 추가 "+a[1]+" §c<값>§8]");
						return false;
					}
					
					if (tag.equals(TagType.VESTED) && util.getOfflinePlayer(a[2]) == null) {
						p.sendMessage(main+a[2]+" 플레이어는 존재하지 않습니다!");
						return false;
					}
					
					if ((tag.equals(TagType.DEADLINE) || tag.equals(TagType.DEADLINE_ON_PICKUP)) && a.length < 7 && 
							(a[2].length() != 4 || a[3].length() != 2 || a[4].length() != 2 || a[5].length() != 2 || a[6].length() != 2)) {
						p.sendMessage(main+"잘못된 명령어입니다. §8[/태그 추가 "+a[1]+" §c<년도> <월> <일> <시> <분>§8]");
						if (tag.equals(TagType.DEADLINE)) p.sendMessage(main+"§9ex)§7 /태그 추가 "+a[1]+" 2014 05 01 18 30");
						if (tag.equals(TagType.DEADLINE_ON_PICKUP)) p.sendMessage(main+"§9ex)§7 /태그 추가 "+a[1]+" 0000 00 01 12 00");
						return false;
					}
					
					if (tag.equals(TagType.SPATIAL) && unlimitedTagWG == null) {
						p.sendMessage(main+"제약 태그는 §cWorldGuard§r 플러그인이 필요합니다!");
						
						if (!getConfig().getBoolean("unlimited-tag.worldguard")) {
							p.sendMessage(main+"Config에서 §c'unlimited-tag.worldguard'§r를 §ctrue§r로 설정해주세요.");
						}
						return false;
					}
					
					String data = null;
					
					if (tag.equals(TagType.VESTED)) data = Token.getToken(util.getOfflinePlayer(a[2]));
					if (tag.equals(TagType.REQUIRE_LV)) data = Integer.parseInt(a[2])+"";
					if (tag.equals(TagType.DEADLINE) || tag.equals(TagType.DEADLINE_ON_PICKUP)) data = a[2]+" "+a[3]+" "+a[4]+" "+a[5]+" "+a[6];
					if (tag.equals(TagType.PERMISSION)) data = a[2];
					if (tag.equals(TagType.SPATIAL)) data = a[2];
					
					UnlimitedTagAPI.addTag(p.getItemInHand(), tag, data);
					
					p.sendMessage(main+"태그 추가가 완료되었습니다.");
				} else if (a[0].equalsIgnoreCase("제거")) {
					if (!util.isPlayer(s)) {
						s.sendMessage(main+"플레이어만 입력할 수 있는 명령어입니다.");
						return false;
					}
					
					Player p = (Player) s;
					
					if (p.getItemInHand().getType().equals(Material.AIR)) {
						p.sendMessage(main+"손이 비었군요? 유감입니다.");
						return false;
					}
					
					if (a.length < 2) {
						p.sendMessage(main+"잘못된 명령어입니다. §8[/태그 제거 <태그 이름>]");
						return false;
					}
					
					TagType tag = TagType.fromString(a[1]);
					
					if (!UnlimitedTagAPI.hasTag(p.getItemInHand(), tag)) {
						p.sendMessage(main+"이 아이템에는 "+tag+" 태그가 없습니다.");
						return false;
					}
					
					UnlimitedTagAPI.removeTag(p.getItemInHand(), tag);
					
					p.sendMessage(main+"태그 제거가 완료되었습니다.");
				}
			} catch (Exception e) {
				s.sendMessage(main+"잘못된 명령어입니다. §8[/태그]");
			}
		} else if (l.equalsIgnoreCase("ㅍ") || l.equalsIgnoreCase("파티채팅")) {
			if (!getConfig().getBoolean("use.party")) {
				s.sendMessage(main+"파티 기능이 활성화되지 않았습니다.");
				return false;
			}
			
			try {
				if (!util.isPlayer(s)) {
					s.sendMessage(main+"플레이어만 입력할 수 있는 명령어입니다.");
					return false;
				}
				
				Player p = (Player) s;
				
				Party party = PartyAPI.getJoinedParty(p);
				if (party == null) {
					p.sendMessage(main+"§c▒§r 가입된 파티가 없습니다.");
					return false;
				}
				
				if (a.length == 0) {
					p.sendMessage(main+"§a▒§r §b/"+l+" <할말>§r: 파티 채팅을 이용합니다.");
					p.sendMessage(main+"§a▒§r §b/"+l+" 고정§r: 자동 파티 채팅을 ON/OFF 합니다.");
					return false;
				}
				
				if (a[0].equals("고정") && a.length == 1) {
					ChatListener chatListener = EfeServer.getInstance().chatListener;
					
					if (chatListener.partyChats.containsKey(p.getUniqueId())) {
						chatListener.partyChats.remove(p.getUniqueId());
						p.sendMessage("§a▒§r 파티 채팅이 해제되었습니다.");
					} else {
						if (chatListener.whispers.containsKey(p.getUniqueId())) {
							chatListener.whispers.remove(p.getUniqueId());
						}
						
						chatListener.partyChats.put(p.getUniqueId(), party);
						p.sendMessage("§a▒§r 파티 채팅을 고정했습니다.");
					}
					
					return false;
				}
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(" §d"+p.getName()+" §d>§5 "+util.getFinalArg(a, 0));
				}
			} catch (Exception e) {
				s.sendMessage(main+"§a▒§r 잘못된 명령어입니다. §8[/ㅍ 할말]");
			}
		}
		return false;
	}
}