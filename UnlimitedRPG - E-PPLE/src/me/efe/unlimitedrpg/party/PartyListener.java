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
				m.sendMessage(plugin.main+"§a▒§r 파티장이 접속을 종료하여 "+owner.getName()+"님께서 새로운 파티장이 되었습니다.");
			}
		}
		
		if (plugin.getConfig().getBoolean("party.kick-on-quit")) {
			if (e.getPlayer().equals(party.getOwner())) {
				if (plugin.getConfig().getBoolean("party.remove-on-leave")) {
					for (Player m : party.getOnlineMembers()) {
						m.sendMessage(plugin.main+"§a▒§r 파티장이 파티를 탈퇴하여 파티가 제거되었습니다.");
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
						m.sendMessage(plugin.main+"§a▒§r 파티장이 파티를 탈퇴하여 "+owner.getName()+"님께서 새로운 파티장이 되었습니다.");
					}
					
					party.leave(e.getPlayer());
				}
			} else {
				party.leave(e.getPlayer());
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(plugin.main+"§a▒§r "+e.getPlayer().getName()+"님께서 접속을 종료하여 파티에서 탈퇴되었습니다.");
				}
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (!(s instanceof Player)) {
			s.sendMessage(plugin.main+"플레이어만 사용할 수 있는 명령어입니다!");
			return false;
		}
		
		try {
			Player p = (Player) s;
			
			if (a.length == 0) {
				EfeCommunity.getInstance().partyListener.openGUI(p);
				
			} else if (a[0].equalsIgnoreCase("생성")) {
				if (!p.hasPermission("urpg.party.create")) {
					p.sendMessage(plugin.main+"§c▒§r 권한이 없습니다. §8[urpg.party.create]");
					return false;
				}
				
				if (PartyAPI.getJoinedParty(p) != null) {
					p.sendMessage(plugin.main+"§c▒§r 이미 가입된 파티가 있습니다.");
					return false;
				}
				
				if (a.length > 2) {
					p.sendMessage(plugin.main+"§c▒§r 파티명에는 띄어쓰기를 사용할 수 없습니다.");
					return false;
				}
				
				if (a[1].getBytes().length > 16) {
					p.sendMessage(plugin.main+"§c▒§r 한글 8자, 영문 16자 제한입니다.");
					return false;
				}
				
				if (PartyAPI.isExist(a[1])) {
					p.sendMessage(plugin.main+"§c▒§r 이미 존재하는 파티명입니다.");
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
				
				p.sendMessage(plugin.main+"§a▒§r §d§l「"+a[1]+"」§r 파티를 생성했습니다!");
			} else if (a[0].equalsIgnoreCase("탈퇴")) {
				if (!p.hasPermission("urpg.party.leave")) {
					p.sendMessage(plugin.main+"권한이 없습니다. §8[urpg.party.leave]");
					return false;
				}
				
				Party party = PartyAPI.getJoinedParty(p);
				
				if (party == null) {
					p.sendMessage(plugin.main+"§c▒§r 가입된 파티가 없습니다.");
					return false;
				}
				
				if (party.getOwner().equals(p)) {
					
					if (plugin.getConfig().getBoolean("party.remove-on-leave")) {
						
						for (Player m : party.getOnlineMembers()) {
							m.sendMessage(plugin.main+"§a▒§r 파티장이 파티를 탈퇴하여 파티가 제거되었습니다.");
						}
						
						PartyAPI.unregisterParty(party);
						
					} else {
						
						if (party.getMembers().size() == 1) {
							p.sendMessage(plugin.main+"§a▒§r 파티를 탈퇴하여 파티가 제거되었습니다.");
							PartyAPI.unregisterParty(party);
							return false;
						}
						
						List<OfflinePlayer> list = party.getMembers();
						list.remove(p);
						OfflinePlayer owner = list.get(util.random.nextInt(list.size()));
						party.transferOwner(owner);
						
						for (Player m : party.getOnlineMembers()) {
							m.sendMessage(plugin.main+"§a▒§r 파티장이 파티를 탈퇴하여 "+owner.getName()+"님께서 새로운 파티장이 되었습니다.");
						}
						
						party.leave(p);
					}
				} else {
					
					party.leave(p);
					
					for (Player m : party.getOnlineMembers()) {
						m.sendMessage(plugin.main+"§a▒§r "+p.getName()+"님께서 파티를 탈퇴했습니다.");
					}
					
					p.sendMessage(plugin.main+"§a▒§r 가입했던 파티를 탈퇴했습니다.");
					
				}
			} else if (a[0].equalsIgnoreCase("양도")) {
				Party party = PartyAPI.getJoinedParty(p);
				
				if (party == null) {
					p.sendMessage(plugin.main+"§c▒§r 가입된 파티가 없습니다.");
					return false;
				}
				
				if (!party.isOwner(p)) {
					p.sendMessage(plugin.main+"§c▒§r 당신은 파티장이 아닙니다.");
					return false;
				}
				
				if (a[1].equalsIgnoreCase(p.getName())) {
					p.sendMessage(plugin.main+"§c▒§r 자신에게 양도할 수 없습니다.");
					return false;
				}
				
				OfflinePlayer t = plugin.util.getOfflinePlayer(a[1]);
				
				if (t == null || !party.getMembers().contains(t)) {
					p.sendMessage(plugin.main+"§c▒§r 가입된 파티원이 아닙니다.");
					return false;
				}
				
				party.transferOwner(t);
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(plugin.main+"§a▒§r "+t.getName()+"님께서 새로운 파티장이 되었습니다.");
				}
			} else if (a[0].equalsIgnoreCase("초대")) {
				if (!p.hasPermission("urpg.party.invite")) {
					p.sendMessage(plugin.main+"권한이 없습니다. §8[urpg.party.invite]");
					return false;
				}
				
				Party party = PartyAPI.getJoinedParty(p);
				
				if (party == null) {
					p.sendMessage(plugin.main+"§c▒§r 가입된 파티가 없습니다.");
					return false;
				}
				
				if (!party.isOwner(p)) {
					p.sendMessage(plugin.main+"§c▒§r 당신은 파티장이 아닙니다.");
					return false;
				}
				
				if (party.getMembers().size() >= limit) {
					p.sendMessage(plugin.main+"§c▒§r 더이상 파티원을 늘릴 수 없습니다!");
					return false;
				}
				
				Player t = plugin.util.getOnlinePlayer(a[1]);
				
				if (t == null || party.getMembers().contains(t)) {
					p.sendMessage(plugin.main+"§c▒§r 존재하지 않거나 이미 가입된 플레이어입니다.");
					return false;
				}
				
				if (PartyAPI.getJoinedParty(t) != null) {
					p.sendMessage(plugin.main+"§c▒§r 이미 가입한 파티가 있는 플레이어입니다.");
					return false;
				}
				
				if (requests.containsKey(Token.getToken(t))) {
					p.sendMessage(plugin.main+"§c▒§r "+t.getName()+"님은 다른 신청을 처리중입니다.");
					return false;
				}
				
				requests.put(Token.getToken(t), party);
				
				t.sendMessage(plugin.main+"§a▒§r "+p.getName()+"님께서 당신을 §d§l「"+party.getName()+"」§r 파티에 초대하셨습니다!");
				t.sendMessage(plugin.main+"§a▒§r §d/파티§r 명령어를 입력해서 초대에 답해주세요.");
				p.sendMessage(plugin.main+"§a▒§r "+t.getName()+"님을 파티에 초대했습니다.");
			} else if (a[0].equalsIgnoreCase("추방")) {
				Party party = PartyAPI.getJoinedParty(p);
				
				if (party == null) {
					p.sendMessage(plugin.main+"§c▒§r 가입된 파티가 없습니다.");
					return false;
				}
				
				if (!party.isOwner(p)) {
					p.sendMessage(plugin.main+"§c▒§r 당신은 파티장이 아닙니다.");
					return false;
				}
				
				if (a[1].equalsIgnoreCase(p.getName())) {
					p.sendMessage(plugin.main+"§c▒§r 자신을 추방할 수 없습니다.");
					return false;
				}
				
				OfflinePlayer t = plugin.util.getOfflinePlayer(a[1]);
				
				if (t == null || !party.getMembers().contains(t)) {
					p.sendMessage(plugin.main+"§c▒§r 존재하지 않거나 가입되지 않은 플레이어입니다.");
					return false;
				}
				
				party.leave(t);
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(plugin.main+"§a▒§r 파티장이 "+t.getName()+"님을 파티에서 추방시켰습니다.");
				}
				
				if (t.isOnline())
					t.getPlayer().sendMessage(plugin.main+"§a▒§r 파티에서 강퇴당했습니다.");
			} else if (a[0].equalsIgnoreCase("수락")) {
				if (!requests.containsKey(Token.getToken(p))) {
					p.sendMessage(plugin.main+"§c▒§r 초대받은 파티가 없습니다.");
					return false;
				}
				
				Party party = requests.get(Token.getToken(p));
				requests.remove(Token.getToken(p));
				
				if (PartyAPI.getJoinedParty(p) != null) {
					p.sendMessage(plugin.main+"§c▒§r 이미 가입한 파티가 있습니다.");
					return false;
				}
				
				if (!PartyAPI.isExist(party.getName())) {
					p.sendMessage(plugin.main+"§c▒§r 초대받은 파티가 제거되어 처리가 최소되었습니다.");
					return false;
				}
				
				if (party.getMembers().size() >= limit) {
					p.sendMessage(plugin.main+"§c▒§r 초대받은 파티가 정원이 가득 차서 처리가 취소되었습니다.");
					return false;
				}
				
				party.join(p);
				
				for (Player m : party.getOnlineMembers()) {
					m.sendMessage(plugin.main+"§a▒§r "+p.getName()+"님께서 파티에 가입하셨습니다!");
				}
			} else if (a[0].equalsIgnoreCase("거절")) {
				if (!requests.containsKey(Token.getToken(p))) {
					p.sendMessage(plugin.main+"§c▒§r 초대받은 파티가 없습니다.");
					return false;
				}
				
				Party party = requests.get(Token.getToken(p));
				requests.remove(Token.getToken(p));
				
				if (!PartyAPI.isExist(party.getName())) {
					p.sendMessage(plugin.main+"§c▒§r 초대받은 파티가 제거되어 처리가 최소되었습니다.");
					return false;
				}
				
				if (party.getMembers().size() >= limit) {
					p.sendMessage(plugin.main+"§c▒§r 초대받은 파티가 정원이 가득 차서 처리가 취소되었습니다.");
					return false;
				}
				
				if (party.getOwner().isOnline()) {
					party.getOwner().getPlayer().sendMessage(plugin.main+"§a▒§r "+p.getName()+"님께서 파티 초대를 거절하셨습니다.");
				}
				
				p.sendMessage(plugin.main+"§a▒§r 파티 초대를 거절했습니다.");
			}
		} catch (Exception e) {
			s.sendMessage(plugin.main+"잘못된 명령어입니다. §8[/파티 명령어]");
		}
		
		return false;
	}
}