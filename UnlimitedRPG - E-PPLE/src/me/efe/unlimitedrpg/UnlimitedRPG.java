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
				util.console("��a=======================================");
				util.console("��a������Ʈ�� �߰ߵǾ����ϴ�!");
				util.console("��a�� ����: ��3"+nowV);
				util.console("��a�Ź���: ��b"+newV);
				util.console("��aī�� Ȥ�� �������� ��α׿��� ���ο� ������ �ٿ�ε��ϼ���.");
				util.console("��ahttp://blog.naver.com/cwjh1002");
				util.console("��a=======================================");
				
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		boolean vers = !getServer().getBukkitVersion().startsWith("1.5.2");
		if (!vers) {
			util.console("��c=======================================");
			util.console("��c����� �� ���� ������ �����ִٰ� �����Ͻó���?");
			util.console("��c���� �ƴմϴ�. �ܿ� �׷� ������ �ںν��� �����ø� �� ����.");
			util.console("��c�ּ��� ��n�ؿ� ������c�� �����̽��ϱ�? �ѱ��� ������ �ƽó���?");
			util.console("��c�� ������ �ڱ��� �ް�, \"�� ���� ����\"�� ����� �ʹٸ�");
			util.console("��c��l���� ���� ����ũ����Ʈ �ֽ� �������� ������ ��ȹ�ϼ���.");
			util.console("��cģ�� ������ ��ϸ� �ŴϾ����� �Բ� ���");
			util.console("��c�ѱ� ����ũ����Ʈ�� ���ο� ���縦 �����");
			util.console("��c�װ��� ����� �����Դϴ�.");
			util.console("��c=======================================");
			util.console("��a10�� �� ���� ������ �̾����ϴ�...");
			
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
			util.console(main+"��c��� ����� �����ֽ��ϴ�. Config���� ����� ������ּ���.");
			util.console(main+"��c��� ����� �����ֽ��ϴ�. Config���� ����� ������ּ���.");
			util.console(main+"��c��� ����� �����ֽ��ϴ�. Config���� ����� ������ּ���.");
			
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
			
			util.console("["+util.getPluginName()+"] ���� ����� Ȱ��ȭ�Ǿ����ϴ�.");
		}
		
		if (getConfig().getBoolean("use.custom-exp")) {
			customExp = new CustomExp(this);
			util.register(customExp);
			
			util.console("["+util.getPluginName()+"] Ŀ���� Exp ����� Ȱ��ȭ�Ǿ����ϴ�.");
		}
		
		if (getConfig().getBoolean("use.unlimited-tag") && getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
			unlimitedTag = new UnlimitedTag(this);
			util.register(unlimitedTag);
			
			util.console("["+util.getPluginName()+"] �𸮹�Ƽ�� �±� ����� Ȱ��ȭ�Ǿ����ϴ�.");
			util.console("["+util.getPluginName()+"] ���������� ProtocolLib �÷����ΰ� �����Ǿ����ϴ�.");
			
			if (getServer().getPluginManager().getPlugin("WorldGuard") != null && getConfig().getBoolean("unlimited-tag.worldguard")) {
				unlimitedTagWG = new WGListener(this);
				util.register(unlimitedTagWG);
				
				util.console("["+util.getPluginName()+"] ���������� WorldGuard �÷����ο� �����Ǿ����ϴ�.");
			}
		}
		
		if (getConfig().getBoolean("use.player-info") && vers) {
			playerInfo = new PlayerInfo(this);
			playerInfoGUI = new PlayerInfoGUI(this);
			util.register(playerInfo);
			util.register(playerInfoGUI);
			
			util.console("["+util.getPluginFullName()+"] �÷��̾� ���� ����� Ȱ��ȭ�Ǿ����ϴ�.");
		}
		
		if (getConfig().getBoolean("use.party")) {
			party = new PartyListener(this);
			util.register(party);
			this.getCommand("��Ƽ").setExecutor(party);
			
			util.console("["+util.getPluginFullName()+"] ��Ƽ ����� Ȱ��ȭ�Ǿ����ϴ�.");
		}
	}
	
	public static UnlimitedRPG getInstance() {
		return instance;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
		List<String> list = new ArrayList<String>();
		
		if (l.equalsIgnoreCase("urpg") || l.equalsIgnoreCase("�𸮹�")) {
			if (a.length == 0) {
				list.add("���ε�");
				list.add("SP�ֹ���");
				list.add("SP�ʱ�ȭ�ֹ���");
			}
		} else if (l.equalsIgnoreCase("�±�")) {
			if (a.length == 1) {
				list.add("�߰�");
				list.add("����");
			} else if (a.length == 2) {
				for (TagType tag : TagType.values()) {
					list.add(tag.toString());
				}
			} else if (a.length == 3 && a[1].equalsIgnoreCase("�ͼ�")) {
				for (Player p : util.getOnlinePlayers()) {
					list.add(p.getName());
				}
			}
		}
		
		return list;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("urpg") || l.equalsIgnoreCase("�𸮹�")) {
			try {
				if (!s.isOp()) {
					s.sendMessage(main+"OP�� ��� ������ ��ɾ��Դϴ�.");
					return false;
				}
				
				if (a.length == 0) {
					s.sendMessage(main+"��d�ݡ�5=====[ ��d��l"+util.getPluginFullName()+"��5 ]=====��d��");
					s.sendMessage(main+"/"+l+"��7: ��ɾ� Ȯ��");
					s.sendMessage(main+"/"+l+" ���ε��7: Config ���ε� (�Ϻ� ������ ���� ���ε� �ʿ�)");
					if (getConfig().getBoolean("use.stat"))
						s.sendMessage(main+"/"+l+" SP�ֹ�����7: SP �ֹ����� ��ȯ");
					if (getConfig().getBoolean("use.stat"))
						s.sendMessage(main+"/"+l+" SP�ʱ�ȭ�ֹ�����7: SP �ʱ�ȭ �ֹ����� ��ȯ");
					if (getConfig().getBoolean("use.stat"))
						s.sendMessage(main+"/���ݡ�7: ���� GUI");
					if (getConfig().getBoolean("use.unlimited-tag"))
						s.sendMessage(main+"/�±ס�7: �±� ��ɾ� Ȯ��");
					if (getConfig().getBoolean("use.party"))
						s.sendMessage(main+"/��Ƽ ��ɾ��7: ��Ƽ ��ɾ� Ȯ��");
					s.sendMessage(main+"��d�ݡ�5======[ ��d��l* Made by Efe *��5 ]======��d��");
				} else if (a[0].equalsIgnoreCase("���ε�")) {
					reloadConfig();
					
					s.sendMessage(main+"���ε尡 �Ϸ�Ǿ����ϴ�.");
				} else if (a[0].equalsIgnoreCase("SP�ֹ���")) {
					if (!util.isPlayer(s)) {
						s.sendMessage(main+"�÷��̾ �Է��� �� �ִ� ��ɾ��Դϴ�.");
						return false;
					}
					
					if (!getConfig().getBoolean("use.stat")) {
						s.sendMessage(main+"���� ����� Ȱ��ȭ���� �ʾҽ��ϴ�.");
						return false;
					}
					
					Player p = (Player) s;
					
					p.getInventory().addItem(statListener.SPpaper);
					
					p.sendMessage(main+"��ȯ�� �Ϸ�Ǿ����ϴ�.");
				} else if (a[0].equalsIgnoreCase("SP�ʱ�ȭ�ֹ���")) {
					if (!util.isPlayer(s)) {
						s.sendMessage(main+"�÷��̾ �Է��� �� �ִ� ��ɾ��Դϴ�.");
						return false;
					}
					
					if (!getConfig().getBoolean("use.stat")) {
						s.sendMessage(main+"���� ����� Ȱ��ȭ���� �ʾҽ��ϴ�.");
						return false;
					}
					
					Player p = (Player) s;
					
					p.getInventory().addItem(statListener.SPreset);
					
					p.sendMessage(main+"��ȯ�� �Ϸ�Ǿ����ϴ�.");
				}
			} catch (Exception e) {
				s.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/"+l+"]");
			}
		} else if (l.equalsIgnoreCase("����")) {
			if (!getConfig().getBoolean("use.stat")) {
				s.sendMessage(main+"���� ����� Ȱ��ȭ���� �ʾҽ��ϴ�.");
				s.sendMessage(main+"���̵带 �����Ͽ� Config�� �������ּ���.");
				return false;
			}
			
			try {
				if (!util.isPlayer(s)) {
					s.sendMessage(main+"�÷��̾ �Է��� �� �ִ� ��ɾ��Դϴ�.");
					return false;
				}
				
				Player p = (Player) s;
				
				if (!p.hasPermission("urpg.stat")) {
					s.sendMessage(main+"������ �����ϴ�! ��8[urpg.stat]");
					return false;
				}
				
				statGUI.openGUI(p);
			} catch (Exception e) {
				s.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/����]");
			}
		} else if (l.equalsIgnoreCase("�±�")) {
			if (!getConfig().getBoolean("use.unlimited-tag")) {
				s.sendMessage(main+"���� ����� Ȱ��ȭ���� �ʾҽ��ϴ�.");
				return false;
			}
			
			try {
				if (!s.isOp()) {
					s.sendMessage(main+"OP�� ��� ������ ��ɾ��Դϴ�.");
					return false;
				}
				
				if (a.length == 0) {
					s.sendMessage(main+"��d�ݡ�5=====[ ��d��l"+util.getPluginFullName()+"��5 ]=====��d��");
					s.sendMessage(main+"/�±�");
					s.sendMessage(main+"/�±� �߰� <�±� �̸�> (��)");
					s.sendMessage(main+"/�±� ���� <�±� �̸�> (��)");
					s.sendMessage(main+"��l* �±� ����Ʈ:");
					s.sendMessage(main+"  "+TagType.getTypes());
					s.sendMessage(main+"��d�ݡ�5======[ ��d��l* Made by Efe *��5 ]======��d��");
				} else if (a[0].equalsIgnoreCase("�߰�")) {
					if (!util.isPlayer(s)) {
						s.sendMessage(main+"�÷��̾ �Է��� �� �ִ� ��ɾ��Դϴ�.");
						return false;
					}
					
					Player p = (Player) s;
					
					if (p.getItemInHand().getType().equals(Material.AIR)) {
						p.sendMessage(main+"���� �������? �����Դϴ�.");
						return false;
					}
					
					if (a.length < 2) {
						p.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/�±� �߰� <�±�>]");
						return false;
					}
					
					TagType tag = TagType.fromString(a[1]);
					
					if (!tag.hasData() && UnlimitedTagAPI.hasTag(p.getItemInHand(), tag)) {
						p.sendMessage(main+"�� �����ۿ��� �̹� �ش� �±װ� �����մϴ�!");
						return false;
					}
					
					if (tag.hasData() && a.length < 3) {
						if (tag.equals(TagType.DEADLINE) || tag.equals(TagType.DEADLINE_ON_PICKUP)) {
							p.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/�±� �߰� "+a[1]+" ��c<�⵵> <��> <��> <��> <��>��8]");
							if (tag.equals(TagType.DEADLINE)) p.sendMessage(main+"��9ex)��7 /�±� �߰� "+a[1]+" 2014 05 01 18 30");
							if (tag.equals(TagType.DEADLINE_ON_PICKUP)) p.sendMessage(main+"��9ex)��7 /�±� �߰� "+a[1]+" 0000 00 01 12 00");
							return false;
						}
						
						p.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/�±� �߰� "+a[1]+" ��c<��>��8]");
						return false;
					}
					
					if (tag.equals(TagType.VESTED) && util.getOfflinePlayer(a[2]) == null) {
						p.sendMessage(main+a[2]+" �÷��̾�� �������� �ʽ��ϴ�!");
						return false;
					}
					
					if ((tag.equals(TagType.DEADLINE) || tag.equals(TagType.DEADLINE_ON_PICKUP)) && a.length < 7 && 
							(a[2].length() != 4 || a[3].length() != 2 || a[4].length() != 2 || a[5].length() != 2 || a[6].length() != 2)) {
						p.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/�±� �߰� "+a[1]+" ��c<�⵵> <��> <��> <��> <��>��8]");
						if (tag.equals(TagType.DEADLINE)) p.sendMessage(main+"��9ex)��7 /�±� �߰� "+a[1]+" 2014 05 01 18 30");
						if (tag.equals(TagType.DEADLINE_ON_PICKUP)) p.sendMessage(main+"��9ex)��7 /�±� �߰� "+a[1]+" 0000 00 01 12 00");
						return false;
					}
					
					if (tag.equals(TagType.SPATIAL) && unlimitedTagWG == null) {
						p.sendMessage(main+"���� �±״� ��cWorldGuard��r �÷������� �ʿ��մϴ�!");
						
						if (!getConfig().getBoolean("unlimited-tag.worldguard")) {
							p.sendMessage(main+"Config���� ��c'unlimited-tag.worldguard'��r�� ��ctrue��r�� �������ּ���.");
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
					
					p.sendMessage(main+"�±� �߰��� �Ϸ�Ǿ����ϴ�.");
				} else if (a[0].equalsIgnoreCase("����")) {
					if (!util.isPlayer(s)) {
						s.sendMessage(main+"�÷��̾ �Է��� �� �ִ� ��ɾ��Դϴ�.");
						return false;
					}
					
					Player p = (Player) s;
					
					if (p.getItemInHand().getType().equals(Material.AIR)) {
						p.sendMessage(main+"���� �������? �����Դϴ�.");
						return false;
					}
					
					if (a.length < 2) {
						p.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/�±� ���� <�±� �̸�>]");
						return false;
					}
					
					TagType tag = TagType.fromString(a[1]);
					
					if (!UnlimitedTagAPI.hasTag(p.getItemInHand(), tag)) {
						p.sendMessage(main+"�� �����ۿ��� "+tag+" �±װ� �����ϴ�.");
						return false;
					}
					
					UnlimitedTagAPI.removeTag(p.getItemInHand(), tag);
					
					p.sendMessage(main+"�±� ���Ű� �Ϸ�Ǿ����ϴ�.");
				}
			} catch (Exception e) {
				s.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/�±�]");
			}
		} else if (l.equalsIgnoreCase("��") || l.equalsIgnoreCase("��Ƽä��")) {
			if (!getConfig().getBoolean("use.party")) {
				s.sendMessage(main+"��Ƽ ����� Ȱ��ȭ���� �ʾҽ��ϴ�.");
				return false;
			}
			
			try {
				if (!util.isPlayer(s)) {
					s.sendMessage(main+"�÷��̾ �Է��� �� �ִ� ��ɾ��Դϴ�.");
					return false;
				}
				
				Player p = (Player) s;
				
				Party party = PartyAPI.getJoinedParty(p);
				if (party == null) {
					p.sendMessage(main+"��c�ơ�r ���Ե� ��Ƽ�� �����ϴ�.");
					return false;
				}
				
				if (a.length == 0) {
					p.sendMessage(main+"��a�ơ�r ��b/"+l+" <�Ҹ�>��r: ��Ƽ ä���� �̿��մϴ�.");
					p.sendMessage(main+"��a�ơ�r ��b/"+l+" ������r: �ڵ� ��Ƽ ä���� ON/OFF �մϴ�.");
					return false;
				}
				
				if (a[0].equals("����") && a.length == 1) {
					ChatListener chatListener = EfeServer.getInstance().chatListener;
					
					if (chatListener.partyChats.containsKey(p.getUniqueId())) {
						chatListener.partyChats.remove(p.getUniqueId());
						p.sendMessage("��a�ơ�r ��Ƽ ä���� �����Ǿ����ϴ�.");
					} else {
						if (chatListener.whispers.containsKey(p.getUniqueId())) {
							chatListener.whispers.remove(p.getUniqueId());
						}
						
						chatListener.partyChats.put(p.getUniqueId(), party);
						p.sendMessage("��a�ơ�r ��Ƽ ä���� �����߽��ϴ�.");
					}
					
					return false;
				}
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(" ��d"+p.getName()+" ��d>��5 "+util.getFinalArg(a, 0));
				}
			} catch (Exception e) {
				s.sendMessage(main+"��a�ơ�r �߸��� ��ɾ��Դϴ�. ��8[/�� �Ҹ�]");
			}
		}
		return false;
	}
}