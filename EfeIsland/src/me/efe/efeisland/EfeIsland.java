package me.efe.efeisland;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.efe.efegear.EfeUtil;
import me.efe.efeisland.customizing.AdvGUI;
import me.efe.efeisland.customizing.BlacklistGUI;
import me.efe.efeisland.customizing.CustomGUI;
import me.efe.efeisland.customizing.InfoGUI;
import me.efe.efeisland.customizing.IslandGUI;
import me.efe.efeisland.customizing.RankingGUI;
import me.efe.efeisland.customizing.TeleportGUI;
import me.efe.efeserver.PlayerData;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

public class EfeIsland extends JavaPlugin {
	public EfeUtil util;
	public World world;
	
	public IslandBuilder builder;
	public IslandListener listener;
	public IslandGUI islandGui;
	public InfoGUI infoGui;
	public CustomGUI customGui;
	public AdvGUI advGui;
	public TeleportGUI teleportGui;
	public RankingGUI rankingGui;
	public BlacklistGUI blacklistGui;
	
	public WorldEditPlugin wep;
	public WorldGuardPlugin wgp;
	public EffectManager em;
	
	@Override
	public void onDisable() {
		util.logDisable();
	}
	
	@Override
	public void onEnable() {
		util = new EfeUtil(this);
		util.logEnable();
		
		WorldCreator f = new WorldCreator("world_islands")
		.environment(World.Environment.NORMAL)
		.type(WorldType.FLAT);
		world = getServer().createWorld(f);
		
		saveDefaultConfig();
		
		this.builder = new IslandBuilder(this);
		this.listener = new IslandListener(this);
		this.islandGui = new IslandGUI(this);
		this.infoGui = new InfoGUI(this);
		this.customGui = new CustomGUI(this);
		this.advGui = new AdvGUI(this);
		this.teleportGui = new TeleportGUI(this);
		this.rankingGui = new RankingGUI(this);
		this.blacklistGui = new BlacklistGUI(this);
		
		util.register(listener);
		util.register(islandGui);
		util.register(infoGui);
		util.register(customGui);
		util.register(advGui);
		util.register(teleportGui);
		util.register(rankingGui);
		util.register(blacklistGui);
		
		wep = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		wgp = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
		em = new EffectManager(EffectLib.instance());
		
		EfeFlag.init(this);
		IslandUtils.init(this);
	}
	
	public Location asLocation(List<String> list) {
		World world = getServer().getWorld(list.get(0));
		double x = Double.parseDouble(list.get(1));
		double y = Double.parseDouble(list.get(2));
		double z = Double.parseDouble(list.get(3));
		return new Location(world, x, y, z);
	}
	
	public List<String> asList(Location loc) {
		List<String> list = new ArrayList<String>();
		list.add(loc.getWorld().getName());
		list.add(loc.getX()+"");
		list.add(loc.getY()+"");
		list.add(loc.getZ()+"");
		return list;
	}
	
	public Location getLastIsleLoc() {
		return (getConfig().contains("last-isle")) ? asLocation(getConfig().getStringList("last-isle")) : new Location(world, 0, 63, 0);
	}
	
	public void setLastIsleLoc(Location loc) {
		loc.setY(63);
		getConfig().set("last-isle", asList(loc));
		
		saveConfig();
	}
	
	public Location getIsleLoc(OfflinePlayer p) {
		PlayerData data = PlayerData.get(p.getUniqueId());
		
		return asLocation(getConfig().getStringList(data.getIslName()));
	}
	
	public void setIsleLoc(OfflinePlayer p, Location loc) {
		PlayerData data = PlayerData.get(p.getUniqueId());
		
		getConfig().set(data.getIslName(), asList(loc));
		
		saveConfig();
	}
	
	public Location getIsleSpawnLoc(OfflinePlayer p) {
		ProtectedRegion region = getIsleRegion(p);
		
		return BukkitUtil.toLocation(region.getFlag(DefaultFlag.SPAWN_LOC));
	}
	
	public ProtectedRegion getIsleRegion(OfflinePlayer p) {
		PlayerData data = PlayerData.get(p.getUniqueId());
		
		return WGBukkit.getRegionManager(world).getRegion(data.getIslName());
	}
	
	public OfflinePlayer getIsleOwner(ProtectedRegion region) {
		return PlayerData.getPlayerData(region.getId()).getPlayer();
	}
	
	public ProtectedRegion getIsleRegion(ApplicableRegionSet set) {
		Iterator<ProtectedRegion> it = set.iterator();
		
		while (it.hasNext()) {
			ProtectedRegion next = it.next();
			
			if (next.getFlag(EfeFlag.TITLE) != null) {
				return next;
			}
		}
		
		return null;
	}
	
	public ProtectedRegion getIsleRegion(Location loc) {
		ApplicableRegionSet set = WGBukkit.getRegionManager(world).getApplicableRegions(loc);
		
		if (set == null) return null;
		
		return getIsleRegion(set);
	}
	
	public List<Player> getVisiters(ProtectedRegion region) {
		List<Player> list = new ArrayList<Player>();
		
		for (Player all : world.getPlayers()) {
			Location loc = all.getLocation();
			
			if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) && all.getGameMode() != GameMode.SPECTATOR) {
				list.add(all);
			}
		}
		
		return list;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("efeisland")) {
			try {
				Player p = (Player) s;
				PlayerData data = PlayerData.get(p);
				
				if (a[0].equals("upgrade")) {
					builder.upgrade(p);
					
					p.sendMessage("§a▒§r 업그레이드가 완료되었습니다.");
					return false;
				}
				
				if (data.hasIsland()) return false;
				
				builder.build(p, a[0]);
				
				p.sendMessage("§a▒§r 생성이 완료되었습니다.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (l.equalsIgnoreCase("섬") || l.equalsIgnoreCase("is") || l.equalsIgnoreCase("isl") || l.equalsIgnoreCase("isle") || l.equalsIgnoreCase("island")) {
			try {
				Player p = (Player) s;
				PlayerData data = PlayerData.get(p);
				
				if (!data.hasIsland()) {
					p.sendMessage("§c▒§r 당신은 아직 섬이 없습니다!");
					return false;
				}
				
				if (a.length == 0) {
					islandGui.openGUI(p);
					
				} else if (a[0].equalsIgnoreCase("초대") || a[0].equalsIgnoreCase("invite")) {
					
					if (a.length == 1) {
						p.sendMessage("§a▒§r /"+l+" "+a[0]+" <Player>");
						return false;
					}
					
					Player target = util.getOnlinePlayer(a[1]);
					
					if (target == null) {
						p.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않거나 오프라인 상태입니다.");
						return false;
					}
					
					this.teleportGui.invite(p, target);
					
				} else if (a[0].equalsIgnoreCase("방문") || a[0].equalsIgnoreCase("visit")) {
					
					if (a.length == 1) {
						p.sendMessage("§a▒§r /"+l+" "+a[0]+" <Player>");
						return false;
					}
					
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					
					if (target == null) {
						p.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않습니다.");
						return false;
					}
					
					IslandUtils.teleportIsland(p, target);
					
				} else if (a[0].equalsIgnoreCase("정보") || a[0].equalsIgnoreCase("info") || a[0].equalsIgnoreCase("inform") || a[0].equalsIgnoreCase("information")) {
					
					Location loc = p.getLocation();
					
					if (!getIsleRegion(p).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
						p.sendMessage("§c▒§r 자신의 섬에서만 설정할 수 있습니다.");
						return false;
					}
					
					this.infoGui.openGUI(p);
					
				} else if (a[0].equalsIgnoreCase("커스터마이징") || a[0].equalsIgnoreCase("custom") || a[0].equalsIgnoreCase("customize") ||
						a[0].equalsIgnoreCase("customizing")) {
					
					Location loc = p.getLocation();
					
					if (!getIsleRegion(p).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
						p.sendMessage("§c▒§r 자신의 섬에서만 설정할 수 있습니다.");
						return false;
					}
					
					this.customGui.openGUI(p);
					
				} else if (a[0].equalsIgnoreCase("순위") || a[0].equalsIgnoreCase("랭킹") || a[0].equalsIgnoreCase("rank") || a[0].equalsIgnoreCase("ranking")) {
					
					this.rankingGui.openGUI(p);
					
				} else if (a[0].equalsIgnoreCase("홍보") || a[0].equalsIgnoreCase("광고") || a[0].equalsIgnoreCase("ad") || a[0].equalsIgnoreCase("adv") ||
						a[0].equalsIgnoreCase("advertise") || a[0].equalsIgnoreCase("advertisement")) {
					
					this.advGui.openGUI(p);
					
				} else if (a[0].equalsIgnoreCase("추천") || a[0].equalsIgnoreCase("recommend")) {
					
					//TODO Update
					if (!s.isOp()) {
						return false;
					}
					
					if (a.length == 1) {
						p.sendMessage("§a▒§r /"+l+" "+a[0]+" <Player>");
						return false;
					}
					
					OfflinePlayer target = util.getOfflinePlayer(a[1]);
					
					if (target == null) {
						p.sendMessage("§c▒§r "+a[1]+"님은 존재하지 않습니다.");
						return false;
					}
					
					if (target.equals(p)) {
						p.sendMessage("§c▒§r 자신을 추천할 수는 없습니다.");
						return false;
					}
					
					if (!data.canRecommend()) {
						p.sendMessage("§c▒§r 하루에 한 번만 추천할 수 있습니다.");
						return false;
					}
					
					if (this.rankingGui.isAlreadyRecommended(p, target)) {
						p.sendMessage("§c▒§r 이번주에 이미 추천한 유저입니다.");
						return false;
					}
					
					this.rankingGui.recommend(p, target);
					
					p.sendMessage("§a▒§r "+target.getName()+"님의 섬을 추천했습니다!");
					p.sendMessage("§a▒§r 이번주는 더 이상 해당 유저의 섬을 추천할 수 없으며,");
					p.sendMessage("§a▒§r 내일 다시 다른 섬을 추천할 수 있습니다.");
					
					if (target.isOnline())
						target.getPlayer().sendMessage("§a▒§r §b"+p.getName()+"§r님께서 당신의 섬을 추천하셨습니다!");
					
				} else if (a[0].equalsIgnoreCase("블랙리스트") || a[0].equalsIgnoreCase("blacklist")) {
					
					this.blacklistGui.openGUI(p);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
}