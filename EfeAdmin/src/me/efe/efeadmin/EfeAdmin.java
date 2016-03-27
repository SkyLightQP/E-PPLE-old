package me.efe.efeadmin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.efe.efegear.EfeUtil;
import me.efe.efeisland.IslandUtils;

import org.bukkit.BanList.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EfeAdmin extends JavaPlugin {
	public EfeUtil util;
	public List<Player> vanishers = new ArrayList<Player>();
	public HashMap<UUID, Location> originLoc = new HashMap<UUID, Location>();
	
	public VanishListener vanishListener;
	
	@Override
	public void onDisable() {
		util.logDisable();
	}
	
	@Override
	public void onEnable() {
		util = new EfeUtil(this);
		util.logEnable();
		
		saveDefaultConfig();
		
		this.vanishListener = new VanishListener(this);
		util.register(vanishListener);
	}
	
	public void setPunishment(UUID id, int count) {
		if (count > 5) count = 5;
		if (count < 0) count = 0;
		
		getConfig().set(id.toString(), count);
	}
	
	public int getPunishment(UUID id) {
		return getConfig().contains(id.toString()) ? getConfig().getInt(id.toString()) : 0;
	}
	
	public void punish(OfflinePlayer p, int count, String reason) {
		if (count > 5) count = 5;
		if (count < 1) return;
		
		setPunishment(p.getUniqueId(), getPunishment(p.getUniqueId()) + count);
		
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(new Date());
		
		switch (getPunishment(p.getUniqueId())) {
		case 1:
			if (p.isOnline()) {
				p.getPlayer().sendMessage("��d�ơ�r ��e��m************************************************");
				p.getPlayer().sendMessage("��d�ơ�r ");
				p.getPlayer().sendMessage("��d�ơ�r ��l�����ڿ� ���� ���޾ҽ��ϴ�!");
				p.getPlayer().sendMessage("��d�ơ�r ������ ���ݵ� �ൿ�� �������� �ʷ��� �� �ֽ��ϴ�.");
				p.getPlayer().sendMessage("��d�ơ�r �ε� �������ֽñ� �ٶ��ϴ�.");
				p.getPlayer().sendMessage("��d�ơ�r ");
				p.getPlayer().sendMessage("��d�ơ�r ��l����: ��7"+reason);
				p.getPlayer().sendMessage("��d�ơ�r ");
				p.getPlayer().sendMessage("��d�ơ�r ��e��m************************************************");
			}
			
			break;
		case 2:
			cal.add(Calendar.HOUR_OF_DAY, 3);
			
			if (p.isOnline()) {
				p.getPlayer().kickPlayer("��c��l[E-PPLE]��r\n��7�����ڿ� ���� ó���޾ҽ��ϴ�!\nE-PPLE ���� �̿��� 3�ð� ���ѵ˴ϴ�.��r\n\n��c��l\""+reason+"\"");
			}
			
			getServer().getBanList(Type.NAME).addBan(p.getName(), reason, cal.getTime(), null);
			break;
		case 3:
			cal.add(Calendar.HOUR_OF_DAY, 12);
			
			if (p.isOnline()) {
				p.getPlayer().kickPlayer("��c��l[E-PPLE]��r\n��7�����ڿ� ���� ó���޾ҽ��ϴ�!\nE-PPLE ���� �̿��� 12�ð� ���ѵ˴ϴ�.��r\n\n��c��l\""+reason+"\"");
			}
			
			getServer().getBanList(Type.NAME).addBan(p.getName(), reason, cal.getTime(), null);
			break;
		case 4:
			cal.add(Calendar.DAY_OF_MONTH, 3);
			
			if (p.isOnline()) {
				p.getPlayer().kickPlayer("��c��l[E-PPLE]��r\n��7�����ڿ� ���� ó���޾ҽ��ϴ�!\nE-PPLE ���� �̿��� 3�� ���ѵ˴ϴ�.��r\n\n��c��l\""+reason+"\"");
			}
			
			getServer().getBanList(Type.NAME).addBan(p.getName(), reason, cal.getTime(), null);
			break;
		case 5:
			if (p.isOnline()) {
				p.getPlayer().kickPlayer("��c��l[E-PPLE]��r\n��7�����ڿ� ���� ó���޾ҽ��ϴ�!\nE-PPLE ���� �̿��� ���� ���ѵ˴ϴ�.��r\n\n��c��l\""+reason+"\"");
			}
			
			getServer().getBanList(Type.NAME).addBan(p.getName(), reason, null, null);
			break;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("admin") || l.equalsIgnoreCase("����") || l.equalsIgnoreCase("ad")) {
			try {
				Player p = (Player) s;
				
				if (!p.hasPermission("epple.admin") && !p.getName().equals("Efe")) return false;
				
				if (a.length == 0) {
					p.sendMessage("��a�ơ�r ��b/"+l+"��r: ��ɾ Ȯ���մϴ�.");
					p.sendMessage("��a�ơ�r ��b/"+l+" vanish��r: ���� ����ϴ�.");
					p.sendMessage("��a�ơ�r ��b/"+l+" warp <island>��r: ���� �̵��մϴ�.");
					p.sendMessage("��a�ơ�r ��b/"+l+" op <player>��r: OP�� ����մϴ�.");
					return false;
				}
				
				if (a[0].equalsIgnoreCase("vanish")) {
					if (vanishers.contains(p)) {
						vanishers.remove(p);
						
						for (Player all : util.getOnlinePlayers()) {
							all.showPlayer(p);
						}
						
						p.removePotionEffect(PotionEffectType.INVISIBILITY);
						
						p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 0.5F);
						p.sendMessage("��a�ơ�r Vanish ��尡 �����Ǿ����ϴ�.");
					} else {
						vanishers.add(p);
						
						for (Player all : util.getOnlinePlayers()) {
							all.hidePlayer(p);
						}
						
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
						
						p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
						p.sendMessage("��a�ơ�r Vanish ��尡 ����Ǿ����ϴ�.");
					}
				} else if (a[0].equalsIgnoreCase("warp")) {
					Location loc = IslandUtils.getIsleLoc(a[1]);
					
					if (loc == null) {
						p.sendMessage("��c�ơ�r ��"+a[1]+"�� ���� �������� �ʽ��ϴ�.");
						return false;
					}
					
					p.teleport(loc);
					
					p.sendMessage("��a�ơ�r ���� �̵��߽��ϴ�.");
				} else if (a[0].equalsIgnoreCase("op")) {
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					
					if (target == null) {
						p.sendMessage("��c�ơ�r "+a[1]+"���� �������� �ʴ� �÷��̾��Դϴ�.");
						return false;
					}
					
					if (getServer().getOperators().contains(target)) {
						p.setOp(false);
						
						p.sendMessage("��a�ơ�r "+target.getName()+"���� ���� OP�� �ƴմϴ�.");
					} else {
						p.setOp(true);
						
						p.sendMessage("��a�ơ�r "+target.getName()+"���� ���� OP�Դϴ�.");
					}
				}
				
			} catch (Exception e) {
				s.sendMessage("��c�ơ�r �߸��� ��ɾ��Դϴ�: "+e.getMessage());
			}
		} else if (l.equalsIgnoreCase("moderator") || l.equalsIgnoreCase("������") || l.equalsIgnoreCase("����") ||
				l.equalsIgnoreCase("moderate") || l.equalsIgnoreCase("mod")) {
			try {
				Player p = (Player) s;
				
				if (!p.hasPermission("epple.moderator")) return false;
				
				if (a.length == 0) {
					p.sendMessage("��a�ơ�r ��b/"+l+"��7: ��ɾ Ȯ���մϴ�.");
					p.sendMessage("��a�ơ�r ��b/"+l+" ������7: ���� ��带 ON/OFF�մϴ�. ����� ���� �������ϴ�.");
					p.sendMessage("��a�ơ�r ��b/"+l+" ���� <�÷��̾�>��7: ���� ���� �������� �̵��մϴ�. ���� ��带 �����ϸ� ���� �ִ� �ڸ��� �ǵ��ƿɴϴ�.");
					p.sendMessage("��a�ơ�r ��b/"+l+" ��� <�÷��̾�> <Ƚ��> <����>��7: ��� �ݴϴ�.");
					return false;
				}
				
				if (a[0].equalsIgnoreCase("����")) {
					if (a.length > 1) {
						Player target = util.getOnlinePlayer(a[1]);
						
						if (target == null) {
							p.sendMessage("��c�ơ�r "+a[1]+"���� �������� �ʴ� �÷��̾��Դϴ�.");
							return false;
						}
						
						if (!vanishers.contains(p)) {
							vanishers.add(p);
							
							for (Player all : util.getOnlinePlayers()) {
								all.hidePlayer(p);
							}
							
							p.setGameMode(GameMode.SPECTATOR);
							originLoc.put(p.getUniqueId(), p.getLocation());
							
							p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
							p.sendMessage("��a�ơ�r ���� ��尡 ����Ǿ����ϴ�.");
						}
						
						p.teleport(target);
						p.sendMessage("��a�ơ�r "+target.getName()+"�Կ��� �ڷ���Ʈ �߽��ϴ�.");
						
						return false;
					}
					
					if (vanishers.contains(p)) {
						vanishers.remove(p);
						
						for (Player all : util.getOnlinePlayers()) {
							all.showPlayer(p);
						}
						
						p.setGameMode(GameMode.SURVIVAL);
						p.teleport(originLoc.get(p.getUniqueId()));
						
						p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 0.5F);
						p.sendMessage("��a�ơ�r ���� ��尡 �����Ǿ����ϴ�.");
					} else {
						vanishers.add(p);
						
						for (Player all : util.getOnlinePlayers()) {
							all.hidePlayer(p);
						}
						
						p.setGameMode(GameMode.SPECTATOR);
						originLoc.put(p.getUniqueId(), p.getLocation());
						
						p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
						p.sendMessage("��a�ơ�r ���� ��尡 ����Ǿ����ϴ�.");
					}
				} else if (a[0].equalsIgnoreCase("���")) {
					if (a.length < 4) {
						p.sendMessage("��c�ơ�r �߸��� ��ɾ��Դϴ�.");
						p.sendMessage("��a�ơ�r ��l/"+l+" ��� <�÷��̾�> <Ƚ��> <����>��7: ��� �ݴϴ�.");
						return false;
					}
					
					
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					
					if (target == null) {
						p.sendMessage("��c�ơ�r "+a[1]+"���� �������� �ʴ� �÷��̾��Դϴ�.");
						return false;
					}
					
					int count = Integer.parseInt(a[2]);
					
					if (count > 5 || count < 1) {
						s.sendMessage("��c�ơ�r ó�� Ƚ���� 1~5���� �����մϴ�.");
						s.sendMessage("��c�ơ�r ����� ó���ϰ� ��� ���� �½��ϱ�?");
						return false;
					}
					
					String reason = util.getFinalArg(a, 3);
					
					punish(target, count, reason);
				}
				
				
			} catch (Exception e) {
				s.sendMessage("��c�ơ�r �߸��� ��ɾ��Դϴ�: "+e.getMessage());
			}
		}
		
		return false;
	}
}