package me.efe.efeisland;

import java.util.Calendar;
import java.util.Set;

import me.efe.efecommunity.UserData;
import me.efe.efequest.quest.QuestLoader;
import me.efe.efeserver.util.Scoreboarder;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

import com.connorlinfoot.titleapi.TitleAPI;
import com.mewin.WGRegionEvents.MovementWay;
import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionEnteredEvent;
import com.mewin.WGRegionEvents.events.RegionLeftEvent;
import com.mewin.util.Util;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class IslandListener implements Listener {
	public EfeIsland plugin;
	public Calendar cal = null;
	
	public IslandListener(EfeIsland plugin) {
		this.plugin = plugin;
		
		if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			cal = Calendar.getInstance();
		}
	}
	
	@EventHandler
	public void enter(RegionEnterEvent e) {
		if (e.getRegion().getFlag(EfeFlag.BLACKLIST) != null &&
				e.getRegion().getFlag(EfeFlag.BLACKLIST).contains(e.getPlayer().getUniqueId().toString()) && !e.getPlayer().isOp()) {
			e.getPlayer().sendMessage("§c▒§r 주인이 당신의 접근을 차단한 섬입니다.");
			e.getPlayer().teleport(plugin.getIsleSpawnLoc(e.getPlayer()));
			e.setCancelled(true);
			return;
		}
		
		if (e.getRegion().getFlag(EfeFlag.ENTRANCE) == State.DENY && !e.getPlayer().isOp()) {
			OfflinePlayer owner = plugin.getIsleOwner(e.getRegion());
			UserData data = new UserData(owner);
			
			if (owner.equals(e.getPlayer())) return;
			if (data.getFriends().contains(e.getPlayer().getUniqueId())) return;
			
			e.getPlayer().sendMessage("§c▒§r 주인이 다른 유저의 접근을 제한한 섬입니다.");
			e.getPlayer().teleport(plugin.getIsleSpawnLoc(e.getPlayer()));
			e.setCancelled(true);
			return;
		}
		
		if (e.getRegion().getFlag(EfeFlag.NEED_QUEST) != null && !e.getPlayer().isOp()) {
			int quest = e.getRegion().getFlag(EfeFlag.NEED_QUEST);
			
			if (!QuestLoader.getUserData(e.getPlayer()).isEnded(quest)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void entered(RegionEnteredEvent e) {
		Scoreboarder.updateObjectives(e.getPlayer());
		
		if (e.getRegion().getFlag(EfeFlag.TITLE) != null) {
			String title = e.getRegion().getFlag(EfeFlag.TITLE);
			OfflinePlayer owner = plugin.getIsleOwner(e.getRegion());
			
			for (Player p : plugin.getVisiters(e.getRegion())) {
				Scoreboarder.updateObjectives(p);
			}
			
			if (e.getMovementWay() != MovementWay.SPAWN) {
				TitleAPI.sendTitle(e.getPlayer(), 20, 30, 10, "§b"+title, e.getRegion().getFlag(EfeFlag.DESCRIPTION));
			}
			
			e.getPlayer().sendMessage("§a▒§r §e"+owner.getName()+"§r님의 §b"+title+"§r에 오신 것을 환영합니다!");

			//TODO Update
//			PlayerData data = PlayerData.get(e.getPlayer());
//			
//			if (data.canRecommend() && !plugin.rankingGui.isAlreadyRecommended(e.getPlayer(), owner) &&
//					!e.getRegion().equals(plugin.getIsleRegion(e.getPlayer()))) {
//				
//				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//					@Override
//					public void run() {
//						if (data.canRecommend() && plugin.getIsleRegion(e.getPlayer().getLocation()).equals(e.getRegion())) {
//							e.getPlayer().sendMessage("§a▒§r "+owner.getName()+"님의 섬이 마음에 드신다면 §b추천§r해주세요!");
//							
//							new FancyMessage("§a▒§r ")
//							.then("§b§n/섬 추천 "+owner.getName())
//								.command("/섬 추천 "+owner.getName())
//								.tooltip("§7클릭하면 "+owner.getName()+"님을 추천합니다.")
//							.send(e.getPlayer());
//						}
//					}
//				}, 100L);
//			}
			
			
			Set<String> set = e.getRegion().getFlag(EfeFlag.VISITERS);
			
			if (!set.contains(e.getPlayer().getUniqueId().toString())) {
				set.add(e.getPlayer().getUniqueId().toString());
				
				e.getRegion().setFlag(EfeFlag.VISITERS, set);
			}
			
			
			int now = plugin.getVisiters(e.getRegion()).size();
			
			if (now > e.getRegion().getFlag(EfeFlag.MAX_VISIT)) {
				e.getRegion().setFlag(EfeFlag.MAX_VISIT, now);
			}
		}
		
		if (e.getRegion().getFlag(EfeFlag.FLY) == State.ALLOW) {
			final Player p = e.getPlayer();
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					p.setAllowFlight(true);
				}
			}, 5L);
			
		}
		
		if (e.getRegion().getFlag(EfeFlag.NO_POTION) == State.ALLOW) {
			for (PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
				e.getPlayer().removePotionEffect(effect.getType());
			}
		}
	}
	
	@EventHandler
	public void left(RegionLeftEvent e) {
		Scoreboarder.updateObjectives(e.getPlayer());
		
		if (e.getRegion().getFlag(EfeFlag.TITLE) != null) {
			for (Player p : plugin.getVisiters(e.getRegion())) {
				Scoreboarder.updateObjectives(p);
			}
		}
		
		if (e.getRegion().getFlag(EfeFlag.FLY) == State.ALLOW && (e.getRegion().getParent() == null || !e.getRegion().getParent().getId().endsWith("_farm"))) {
			e.getPlayer().setAllowFlight(false);
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		if (e.getPlayer().hasMetadata("teleporting")) {
			e.getPlayer().removeMetadata("teleporting", plugin);
		}
		
		if (cal != null && cal.get(Calendar.DAY_OF_WEEK) != Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
			plugin.rankingGui.resetData();
			cal = null;
		}
	}
	
	@EventHandler
	public void teleportWorld(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				Scoreboarder.updateObjectives(p);
			}
		}, 20L);
	}
	
	@EventHandler
	public void death(PlayerDeathEvent e) {
		if (e.getEntity().getWorld() == plugin.world) {
			ProtectedRegion region = plugin.getIsleRegion(e.getEntity().getLocation());
			Location respawn = null;
			
			if (region == null) {
				respawn = plugin.getIsleSpawnLoc(e.getEntity()).clone();
			} else {
				OfflinePlayer player = plugin.getIsleOwner(region);
				respawn = plugin.getIsleSpawnLoc(player).clone();
			}
			
			e.getEntity().setMetadata("death_island", new FixedMetadataValue(plugin, respawn));
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		if (e.getPlayer().getAllowFlight() && 
				e.getPlayer().getGameMode() != GameMode.CREATIVE && 
				Util.getFlagValue(plugin.wgp, e.getRespawnLocation(), EfeFlag.FLY) != State.ALLOW) {
			e.getPlayer().setAllowFlight(false);
		}
	}
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && plugin.util.getDamager(e) instanceof Player) {
			if (Util.getFlagValue(plugin.wgp, e.getEntity().getLocation(), EfeFlag.INSTANT_KILL) == State.ALLOW) {
				e.setDamage(999999.0D);
			}
		}
	}
	
	@EventHandler
	public void potion(PlayerItemConsumeEvent e) {
		if (e.getItem().getType().equals(Material.POTION) && Util.getFlagValue(plugin.wgp, e.getPlayer().getLocation(), EfeFlag.NO_POTION) == State.ALLOW) {
			e.setCancelled(true);
			plugin.util.updateInv(e.getPlayer());
		}
	}
}