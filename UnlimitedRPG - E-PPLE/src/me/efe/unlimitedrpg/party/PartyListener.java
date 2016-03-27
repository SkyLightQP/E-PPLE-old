package me.efe.unlimitedrpg.party;

import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.efe.efecommunity.EfeCommunity;
import me.efe.efegear.EfeUtil;
import me.efe.efegear.util.Token;
import me.efe.efeserver.EfeServer;
import me.efe.unlimitedrpg.UnlimitedRPG;
import me.efe.unlimitedrpg.customexp.CustomExpAPI;

public class PartyListener implements Listener, CommandExecutor {
	public UnlimitedRPG plugin;
	public EfeUtil util;
	public HashMap<String, Party> requests = new HashMap<String, Party>();
	public int limit;

	public PartyListener(UnlimitedRPG plugin) {
		this.plugin = plugin;
		this.util = plugin.util;
		this.limit = plugin.getConfig().getInt("party.limit-amount");
	}
	
	@EventHandler
	public void exp(PlayerExpChangeEvent e) {
		if (plugin.getConfig().getBoolean("party.exp-share.enable")) {
			Party party = PartyAPI.getJoinedParty(e.getPlayer());
			if (party == null) return;
			for (Player m : party.getOnlineMembers()) {
				if (m.getName().equals(e.getPlayer().getName())) continue;
				
				if (!m.getLocation().getWorld().equals(e.getPlayer().getWorld())) continue;
				if (m.getLocation().distance(e.getPlayer().getLocation()) > plugin.getConfig().getInt("party.exp-share.range")) continue;
				if (!enoughLevel(e.getPlayer(), m)) continue;
				
				int amount = (int) (e.getAmount() * plugin.getConfig().getDouble("party.exp-share.percent") / 100);
				
				if (plugin.getConfig().getBoolean("use.custom-exp")) {
					CustomExpAPI.giveExp(m, amount);
				} else {
					m.giveExp(amount);
				}
			}
		}
	}
	
	public boolean enoughLevel(Player p, Player m) {
		int max = plugin.getConfig().getInt("party.exp-share.max-level-gap");
		int gap = Math.abs(p.getLevel() - m.getLevel());
		
		return max >= gap;
	}
	
	@EventHandler
	public void pvp(EntityDamageByEntityEvent e) {
		if (plugin.getConfig().getBoolean("party.no-pvp")) {
			LivingEntity entity = util.getDamager(e);
			if (!util.isPlayer(e.getEntity()) || !util.isPlayer(entity)) return;
			
			Player p = (Player) e.getEntity();
			Player damager = (Player) entity;
			
			Party party = PartyAPI.getJoinedParty(p);
			Party dparty = PartyAPI.getJoinedParty(damager);
			
			if (party == null || dparty == null) return;
			
			if (party.equals(dparty)) {
				e.setCancelled(true);
				util.updateInv(damager);
			}
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		Party party = PartyAPI.getJoinedParty(e.getPlayer());
		
		if (party == null) return;
		
		if (plugin.getConfig().getBoolean("party.transfer-on-quit") && e.getPlayer().equals(party.getOwner()) && party.getMembers().size() > 1) {
			List<OfflinePlayer> list = party.getMembers();
			list.remove(e.getPlayer());
			OfflinePlayer owner = list.get(util.random.nextInt(list.size()));
			party.transferOwner(owner);
			
			for (Player m : party.getOnlineMembers()) {
				m.sendMessage(plugin.main+"��a�ơ�r ��Ƽ���� ������ �����Ͽ� "+owner.getName()+"�Բ��� ���ο� ��Ƽ���� �Ǿ����ϴ�.");
			}
		}
		
		if (plugin.getConfig().getBoolean("party.kick-on-quit")) {
			if (e.getPlayer().equals(party.getOwner())) {
				if (plugin.getConfig().getBoolean("party.remove-on-leave")) {
					for (Player m : party.getOnlineMembers()) {
						m.sendMessage(plugin.main+"��a�ơ�r ��Ƽ���� ��Ƽ�� Ż���Ͽ� ��Ƽ�� ���ŵǾ����ϴ�.");
					}
					
					PartyAPI.unregisterParty(party);
				} else {
					if (party.getMembers().size() == 1) {
						PartyAPI.unregisterParty(party);
						return;
					}
					
					List<OfflinePlayer> list = party.getMembers();
					list.remove(e.getPlayer());
					OfflinePlayer owner = list.get(util.random.nextInt(list.size()));
					party.transferOwner(owner);
					
					for (Player m : party.getOnlineMembers()) {
						m.sendMessage(plugin.main+"��a�ơ�r ��Ƽ���� ��Ƽ�� Ż���Ͽ� "+owner.getName()+"�Բ��� ���ο� ��Ƽ���� �Ǿ����ϴ�.");
					}
					
					party.leave(e.getPlayer());
				}
			} else {
				party.leave(e.getPlayer());
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(plugin.main+"��a�ơ�r "+e.getPlayer().getName()+"�Բ��� ������ �����Ͽ� ��Ƽ���� Ż��Ǿ����ϴ�.");
				}
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (!(s instanceof Player)) {
			s.sendMessage(plugin.main+"�÷��̾ ����� �� �ִ� ��ɾ��Դϴ�!");
			return false;
		}
		
		try {
			Player p = (Player) s;
			
			if (a.length == 0) {
				EfeCommunity.getInstance().partyListener.openGUI(p);
				
			} else if (a[0].equalsIgnoreCase("����")) {
				if (!p.hasPermission("urpg.party.create")) {
					p.sendMessage(plugin.main+"��c�ơ�r ������ �����ϴ�. ��8[urpg.party.create]");
					return false;
				}
				
				if (PartyAPI.getJoinedParty(p) != null) {
					p.sendMessage(plugin.main+"��c�ơ�r �̹� ���Ե� ��Ƽ�� �ֽ��ϴ�.");
					return false;
				}
				
				if (a.length > 2) {
					p.sendMessage(plugin.main+"��c�ơ�r ��Ƽ���� ���⸦ ����� �� �����ϴ�.");
					return false;
				}
				
				if (a[1].getBytes().length > 16) {
					p.sendMessage(plugin.main+"��c�ơ�r �ѱ� 8��, ���� 16�� �����Դϴ�.");
					return false;
				}
				
				if (PartyAPI.isExist(a[1])) {
					p.sendMessage(plugin.main+"��c�ơ�r �̹� �����ϴ� ��Ƽ���Դϴ�.");
					return false;
				}
				
				String name = a[1];
				
				for (String words : EfeServer.getInstance().chatListener.bannedWords) {
					String replace = "";
					
					for (int i = 0; i < words.length(); i ++)
						replace += '*';
					
					name = name.replaceAll(words, replace);
				}
				
				Party party = new Party(p, name);
				PartyAPI.registerParty(party);
				
				p.sendMessage(plugin.main+"��a�ơ�r ��d��l��"+a[1]+"����r ��Ƽ�� �����߽��ϴ�!");
			} else if (a[0].equalsIgnoreCase("Ż��")) {
				if (!p.hasPermission("urpg.party.leave")) {
					p.sendMessage(plugin.main+"������ �����ϴ�. ��8[urpg.party.leave]");
					return false;
				}
				
				Party party = PartyAPI.getJoinedParty(p);
				
				if (party == null) {
					p.sendMessage(plugin.main+"��c�ơ�r ���Ե� ��Ƽ�� �����ϴ�.");
					return false;
				}
				
				if (party.getOwner().equals(p)) {
					
					if (plugin.getConfig().getBoolean("party.remove-on-leave")) {
						
						for (Player m : party.getOnlineMembers()) {
							m.sendMessage(plugin.main+"��a�ơ�r ��Ƽ���� ��Ƽ�� Ż���Ͽ� ��Ƽ�� ���ŵǾ����ϴ�.");
						}
						
						PartyAPI.unregisterParty(party);
						
					} else {
						
						if (party.getMembers().size() == 1) {
							p.sendMessage(plugin.main+"��a�ơ�r ��Ƽ�� Ż���Ͽ� ��Ƽ�� ���ŵǾ����ϴ�.");
							PartyAPI.unregisterParty(party);
							return false;
						}
						
						List<OfflinePlayer> list = party.getMembers();
						list.remove(p);
						OfflinePlayer owner = list.get(util.random.nextInt(list.size()));
						party.transferOwner(owner);
						
						for (Player m : party.getOnlineMembers()) {
							m.sendMessage(plugin.main+"��a�ơ�r ��Ƽ���� ��Ƽ�� Ż���Ͽ� "+owner.getName()+"�Բ��� ���ο� ��Ƽ���� �Ǿ����ϴ�.");
						}
						
						party.leave(p);
					}
				} else {
					
					party.leave(p);
					
					for (Player m : party.getOnlineMembers()) {
						m.sendMessage(plugin.main+"��a�ơ�r "+p.getName()+"�Բ��� ��Ƽ�� Ż���߽��ϴ�.");
					}
					
					p.sendMessage(plugin.main+"��a�ơ�r �����ߴ� ��Ƽ�� Ż���߽��ϴ�.");
					
				}
			} else if (a[0].equalsIgnoreCase("�絵")) {
				Party party = PartyAPI.getJoinedParty(p);
				
				if (party == null) {
					p.sendMessage(plugin.main+"��c�ơ�r ���Ե� ��Ƽ�� �����ϴ�.");
					return false;
				}
				
				if (!party.isOwner(p)) {
					p.sendMessage(plugin.main+"��c�ơ�r ����� ��Ƽ���� �ƴմϴ�.");
					return false;
				}
				
				if (a[1].equalsIgnoreCase(p.getName())) {
					p.sendMessage(plugin.main+"��c�ơ�r �ڽſ��� �絵�� �� �����ϴ�.");
					return false;
				}
				
				OfflinePlayer t = plugin.util.getOfflinePlayer(a[1]);
				
				if (t == null || !party.getMembers().contains(t)) {
					p.sendMessage(plugin.main+"��c�ơ�r ���Ե� ��Ƽ���� �ƴմϴ�.");
					return false;
				}
				
				party.transferOwner(t);
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(plugin.main+"��a�ơ�r "+t.getName()+"�Բ��� ���ο� ��Ƽ���� �Ǿ����ϴ�.");
				}
			} else if (a[0].equalsIgnoreCase("�ʴ�")) {
				if (!p.hasPermission("urpg.party.invite")) {
					p.sendMessage(plugin.main+"������ �����ϴ�. ��8[urpg.party.invite]");
					return false;
				}
				
				Party party = PartyAPI.getJoinedParty(p);
				
				if (party == null) {
					p.sendMessage(plugin.main+"��c�ơ�r ���Ե� ��Ƽ�� �����ϴ�.");
					return false;
				}
				
				if (!party.isOwner(p)) {
					p.sendMessage(plugin.main+"��c�ơ�r ����� ��Ƽ���� �ƴմϴ�.");
					return false;
				}
				
				if (party.getMembers().size() >= limit) {
					p.sendMessage(plugin.main+"��c�ơ�r ���̻� ��Ƽ���� �ø� �� �����ϴ�!");
					return false;
				}
				
				Player t = plugin.util.getOnlinePlayer(a[1]);
				
				if (t == null || party.getMembers().contains(t)) {
					p.sendMessage(plugin.main+"��c�ơ�r �������� �ʰų� �̹� ���Ե� �÷��̾��Դϴ�.");
					return false;
				}
				
				if (PartyAPI.getJoinedParty(t) != null) {
					p.sendMessage(plugin.main+"��c�ơ�r �̹� ������ ��Ƽ�� �ִ� �÷��̾��Դϴ�.");
					return false;
				}
				
				if (requests.containsKey(Token.getToken(t))) {
					p.sendMessage(plugin.main+"��c�ơ�r "+t.getName()+"���� �ٸ� ��û�� ó�����Դϴ�.");
					return false;
				}
				
				requests.put(Token.getToken(t), party);
				
				t.sendMessage(plugin.main+"��a�ơ�r "+p.getName()+"�Բ��� ����� ��d��l��"+party.getName()+"����r ��Ƽ�� �ʴ��ϼ̽��ϴ�!");
				t.sendMessage(plugin.main+"��a�ơ�r ��d/��Ƽ��r ��ɾ �Է��ؼ� �ʴ뿡 �����ּ���.");
				p.sendMessage(plugin.main+"��a�ơ�r "+t.getName()+"���� ��Ƽ�� �ʴ��߽��ϴ�.");
			} else if (a[0].equalsIgnoreCase("�߹�")) {
				Party party = PartyAPI.getJoinedParty(p);
				
				if (party == null) {
					p.sendMessage(plugin.main+"��c�ơ�r ���Ե� ��Ƽ�� �����ϴ�.");
					return false;
				}
				
				if (!party.isOwner(p)) {
					p.sendMessage(plugin.main+"��c�ơ�r ����� ��Ƽ���� �ƴմϴ�.");
					return false;
				}
				
				if (a[1].equalsIgnoreCase(p.getName())) {
					p.sendMessage(plugin.main+"��c�ơ�r �ڽ��� �߹��� �� �����ϴ�.");
					return false;
				}
				
				OfflinePlayer t = plugin.util.getOfflinePlayer(a[1]);
				
				if (t == null || !party.getMembers().contains(t)) {
					p.sendMessage(plugin.main+"��c�ơ�r �������� �ʰų� ���Ե��� ���� �÷��̾��Դϴ�.");
					return false;
				}
				
				party.leave(t);
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(plugin.main+"��a�ơ�r ��Ƽ���� "+t.getName()+"���� ��Ƽ���� �߹���׽��ϴ�.");
				}
				
				if (t.isOnline())
					t.getPlayer().sendMessage(plugin.main+"��a�ơ�r ��Ƽ���� ������߽��ϴ�.");
			} else if (a[0].equalsIgnoreCase("����")) {
				if (!requests.containsKey(Token.getToken(p))) {
					p.sendMessage(plugin.main+"��c�ơ�r �ʴ���� ��Ƽ�� �����ϴ�.");
					return false;
				}
				
				Party party = requests.get(Token.getToken(p));
				requests.remove(Token.getToken(p));
				
				if (PartyAPI.getJoinedParty(p) != null) {
					p.sendMessage(plugin.main+"��c�ơ�r �̹� ������ ��Ƽ�� �ֽ��ϴ�.");
					return false;
				}
				
				if (!PartyAPI.isExist(party.getName())) {
					p.sendMessage(plugin.main+"��c�ơ�r �ʴ���� ��Ƽ�� ���ŵǾ� ó���� �ּҵǾ����ϴ�.");
					return false;
				}
				
				if (party.getMembers().size() >= limit) {
					p.sendMessage(plugin.main+"��c�ơ�r �ʴ���� ��Ƽ�� ������ ���� ���� ó���� ��ҵǾ����ϴ�.");
					return false;
				}
				
				party.join(p);
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(plugin.main+"��a�ơ�r "+p.getName()+"�Բ��� ��Ƽ�� �����ϼ̽��ϴ�!");
				}
			} else if (a[0].equalsIgnoreCase("����")) {
				if (!requests.containsKey(Token.getToken(p))) {
					p.sendMessage(plugin.main+"��c�ơ�r �ʴ���� ��Ƽ�� �����ϴ�.");
					return false;
				}
				
				Party party = requests.get(Token.getToken(p));
				requests.remove(Token.getToken(p));
				
				if (!PartyAPI.isExist(party.getName())) {
					p.sendMessage(plugin.main+"��c�ơ�r �ʴ���� ��Ƽ�� ���ŵǾ� ó���� �ּҵǾ����ϴ�.");
					return false;
				}
				
				if (party.getMembers().size() >= limit) {
					p.sendMessage(plugin.main+"��c�ơ�r �ʴ���� ��Ƽ�� ������ ���� ���� ó���� ��ҵǾ����ϴ�.");
					return false;
				}
				
				if (party.getOwner().isOnline()) {
					party.getOwner().getPlayer().sendMessage(plugin.main+"��a�ơ�r "+p.getName()+"�Բ��� ��Ƽ �ʴ븦 �����ϼ̽��ϴ�.");
				}
				
				p.sendMessage(plugin.main+"��a�ơ�r ��Ƽ �ʴ븦 �����߽��ϴ�.");
			}
		} catch (Exception e) {
			s.sendMessage(plugin.main+"�߸��� ��ɾ��Դϴ�. ��8[/��Ƽ ��ɾ�]");
		}
		
		return false;
	}
}