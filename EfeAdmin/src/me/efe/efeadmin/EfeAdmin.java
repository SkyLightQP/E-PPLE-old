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
				p.getPlayer().sendMessage("§d▒§r §e§m************************************************");
				p.getPlayer().sendMessage("§d▒§r ");
				p.getPlayer().sendMessage("§d▒§r §l관리자에 의해 경고받았습니다!");
				p.getPlayer().sendMessage("§d▒§r 규정에 위반된 행동은 불이익을 초래할 수 있습니다.");
				p.getPlayer().sendMessage("§d▒§r 부디 주의해주시기 바랍니다.");
				p.getPlayer().sendMessage("§d▒§r ");
				p.getPlayer().sendMessage("§d▒§r §l사유: §7"+reason);
				p.getPlayer().sendMessage("§d▒§r ");
				p.getPlayer().sendMessage("§d▒§r §e§m************************************************");
			}
			
			break;
		case 2:
			cal.add(Calendar.HOUR_OF_DAY, 3);
			
			if (p.isOnline()) {
				p.getPlayer().kickPlayer("§c§l[E-PPLE]§r\n§7관리자에 의해 처벌받았습니다!\nE-PPLE 서버 이용이 3시간 제한됩니다.§r\n\n§c§l\""+reason+"\"");
			}
			
			getServer().getBanList(Type.NAME).addBan(p.getName(), reason, cal.getTime(), null);
			break;
		case 3:
			cal.add(Calendar.HOUR_OF_DAY, 12);
			
			if (p.isOnline()) {
				p.getPlayer().kickPlayer("§c§l[E-PPLE]§r\n§7관리자에 의해 처벌받았습니다!\nE-PPLE 서버 이용이 12시간 제한됩니다.§r\n\n§c§l\""+reason+"\"");
			}
			
			getServer().getBanList(Type.NAME).addBan(p.getName(), reason, cal.getTime(), null);
			break;
		case 4:
			cal.add(Calendar.DAY_OF_MONTH, 3);
			
			if (p.isOnline()) {
				p.getPlayer().kickPlayer("§c§l[E-PPLE]§r\n§7관리자에 의해 처벌받았습니다!\nE-PPLE 서버 이용이 3일 제한됩니다.§r\n\n§c§l\""+reason+"\"");
			}
			
			getServer().getBanList(Type.NAME).addBan(p.getName(), reason, cal.getTime(), null);
			break;
		case 5:
			if (p.isOnline()) {
				p.getPlayer().kickPlayer("§c§l[E-PPLE]§r\n§7관리자에 의해 처벌받았습니다!\nE-PPLE 서버 이용이 영구 제한됩니다.§r\n\n§c§l\""+reason+"\"");
			}
			
			getServer().getBanList(Type.NAME).addBan(p.getName(), reason, null, null);
			break;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("admin") || l.equalsIgnoreCase("관리") || l.equalsIgnoreCase("ad")) {
			try {
				Player p = (Player) s;
				
				if (!p.hasPermission("epple.admin") && !p.getName().equals("Efe")) return false;
				
				if (a.length == 0) {
					p.sendMessage("§a▒§r §b/"+l+"§r: 명령어를 확인합니다.");
					p.sendMessage("§a▒§r §b/"+l+" vanish§r: 몸을 숨깁니다.");
					p.sendMessage("§a▒§r §b/"+l+" warp <island>§r: 섬을 이동합니다.");
					p.sendMessage("§a▒§r §b/"+l+" op <player>§r: OP를 토글합니다.");
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
						p.sendMessage("§a▒§r Vanish 모드가 해제되었습니다.");
					} else {
						vanishers.add(p);
						
						for (Player all : util.getOnlinePlayers()) {
							all.hidePlayer(p);
						}
						
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
						
						p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
						p.sendMessage("§a▒§r Vanish 모드가 적용되었습니다.");
					}
				} else if (a[0].equalsIgnoreCase("warp")) {
					Location loc = IslandUtils.getIsleLoc(a[1]);
					
					if (loc == null) {
						p.sendMessage("§c▒§r 「"+a[1]+"」 섬은 존재하지 않습니다.");
						return false;
					}
					
					p.teleport(loc);
					
					p.sendMessage("§a▒§r 섬을 이동했습니다.");
				} else if (a[0].equalsIgnoreCase("op")) {
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					
					if (target == null) {
						p.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않는 플레이어입니다.");
						return false;
					}
					
					if (getServer().getOperators().contains(target)) {
						p.setOp(false);
						
						p.sendMessage("§a▒§r "+target.getName()+"님은 이제 OP가 아닙니다.");
					} else {
						p.setOp(true);
						
						p.sendMessage("§a▒§r "+target.getName()+"님은 이제 OP입니다.");
					}
				}
				
			} catch (Exception e) {
				s.sendMessage("§c▒§r 잘못된 명령어입니다: "+e.getMessage());
			}
		} else if (l.equalsIgnoreCase("moderator") || l.equalsIgnoreCase("중재자") || l.equalsIgnoreCase("중재") ||
				l.equalsIgnoreCase("moderate") || l.equalsIgnoreCase("mod")) {
			try {
				Player p = (Player) s;
				
				if (!p.hasPermission("epple.moderator")) return false;
				
				if (a.length == 0) {
					p.sendMessage("§a▒§r §b/"+l+"§7: 명령어를 확인합니다.");
					p.sendMessage("§a▒§r §b/"+l+" 관전§7: 관전 모드를 ON/OFF합니다. 당신의 몸이 숨겨집니다.");
					p.sendMessage("§a▒§r §b/"+l+" 관전 <플레이어>§7: 관전 모드로 유저에게 이동합니다. 관전 모드를 해제하면 원래 있던 자리로 되돌아옵니다.");
					p.sendMessage("§a▒§r §b/"+l+" 경고 <플레이어> <횟수> <사유>§7: 경고를 줍니다.");
					return false;
				}
				
				if (a[0].equalsIgnoreCase("관전")) {
					if (a.length > 1) {
						Player target = util.getOnlinePlayer(a[1]);
						
						if (target == null) {
							p.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않는 플레이어입니다.");
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
							p.sendMessage("§a▒§r 관전 모드가 적용되었습니다.");
						}
						
						p.teleport(target);
						p.sendMessage("§a▒§r "+target.getName()+"님에게 텔레포트 했습니다.");
						
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
						p.sendMessage("§a▒§r 관전 모드가 해제되었습니다.");
					} else {
						vanishers.add(p);
						
						for (Player all : util.getOnlinePlayers()) {
							all.hidePlayer(p);
						}
						
						p.setGameMode(GameMode.SPECTATOR);
						originLoc.put(p.getUniqueId(), p.getLocation());
						
						p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
						p.sendMessage("§a▒§r 관전 모드가 적용되었습니다.");
					}
				} else if (a[0].equalsIgnoreCase("경고")) {
					if (a.length < 4) {
						p.sendMessage("§c▒§r 잘못된 명령어입니다.");
						p.sendMessage("§a▒§r §l/"+l+" 경고 <플레이어> <횟수> <사유>§7: 경고를 줍니다.");
						return false;
					}
					
					
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					
					if (target == null) {
						p.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않는 플레이어입니다.");
						return false;
					}
					
					int count = Integer.parseInt(a[2]);
					
					if (count > 5 || count < 1) {
						s.sendMessage("§c▒§r 처벌 횟수는 1~5번만 가능합니다.");
						s.sendMessage("§c▒§r 제대로 처벌하고 계신 것이 맞습니까?");
						return false;
					}
					
					String reason = util.getFinalArg(a, 3);
					
					punish(target, count, reason);
				}
				
				
			} catch (Exception e) {
				s.sendMessage("§c▒§r 잘못된 명령어입니다: "+e.getMessage());
			}
		}
		
		return false;
	}
}