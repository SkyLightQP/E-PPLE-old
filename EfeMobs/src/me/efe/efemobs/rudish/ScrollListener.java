package me.efe.efemobs.rudish;

import java.util.ArrayList;
import java.util.List;

import me.confuser.barapi.BarAPI;
import me.efe.efeisland.IslandUtils;
import me.efe.efemobs.EfeMobs;
import me.efe.efemobs.Expedition;
import me.efe.efemobs.ScrollUtils;
import me.efe.unlimitedrpg.party.Party;
import me.efe.unlimitedrpg.party.PartyAPI;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.titleapi.TitleAPI;

public class ScrollListener implements Listener {
	public EfeMobs plugin;
	
	public ScrollListener(EfeMobs plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (plugin.util.isRightClick(e) && e.getItem() != null && ScrollUtils.isScroll(e.getItem())) {
			if (!IslandUtils.getIsleName(e.getPlayer().getLocation()).equals(IslandUtils.RUDISH)) {
				e.setCancelled(true);
				plugin.util.updateInv(e.getPlayer());
				
				e.getPlayer().sendMessage("§c▒§r 루디쉬 섬에서만 사용할 수 있습니다!");
				return;
			}
			
			if (plugin.bossListener.playerMap.containsKey(e.getPlayer())) {
				e.getPlayer().sendMessage("§c▒§r 이미 보스에게 도전중입니다!");
				return;
			}
			
			final List<Player> list = new ArrayList<Player>();
			final int floor = ScrollUtils.getFloor(e.getItem());
			
			UserData data = new UserData(e.getPlayer());
			
			if (data.getMaxFloor() < floor) {
				e.getPlayer().sendMessage("§c▒§r 해당 층에 도전할 수 없습니다!");
				return;
			}
			
			if (data.getBossCount() >= 7) {
				e.getPlayer().sendMessage("§c▒§r 하루 도전 횟수를 소진했습니다! §8(7회)");
				return;
			}
			
			Party party = PartyAPI.getJoinedParty(e.getPlayer());
			
			list.add(e.getPlayer());
			
			if (party != null) {
				if (!party.getOwner().equals(e.getPlayer())) {
					e.getPlayer().sendMessage("§c▒§r 파티장만 도전할 수 있습니다!");
					return;
				}
				
				if (party.getMembers().size() > 5) {
					e.getPlayer().sendMessage("§c▒§r 최대 5명의 파티로 도전할 수 있습니다!");
					return;
				}
				
				for (OfflinePlayer m : party.getMembers()) {
					UserData mData = new UserData(m);
					
					if (m.equals(e.getPlayer())) continue;
					
					if (!m.isOnline()) {
						e.getPlayer().sendMessage("§c▒§r 오프라인인 파티 멤버가 있습니다!§8: "+m.getName());
						return;
					}
					
					if (!m.getPlayer().getWorld().equals(e.getPlayer().getWorld()) || m.getPlayer().getLocation().distance(e.getPlayer().getLocation()) >= 7.0D) {
						e.getPlayer().sendMessage("§c▒§r 7m 이내에 없는 파티 멤버가 있습니다!§8: "+m.getName());
						return;
					}
					
					if (plugin.bossListener.playerMap.containsKey(m)) {
						e.getPlayer().sendMessage("§c▒§r 이미 보스에게 도전중인 멤버가 있습니다!§8: "+m.getName());
						return;
					}
					
					if (!hasItem(m.getPlayer(), e.getItem(), 1)) {
						e.getPlayer().sendMessage("§c▒§r 스크롤을 소지하지 않은 멤버가 있습니다!§8: "+m.getName());
						return;
					}
					
					if (mData.getMaxFloor() < floor) {
						e.getPlayer().sendMessage("§c▒§r "+floor+"층에 도전할 수 없는 멤버가 있습니다!§8: "+m.getName());
						return;
					}
					
					if (mData.getBossCount() >= 7) {
						e.getPlayer().sendMessage("§c▒§r 하루 도전 횟수를 소진한 멤버가 있습니다!§8: "+m.getName());
						return;
					}
					
					list.add(m.getPlayer());
				}
			}
			
			if (plugin.bossListener.expMap.size() >= 15) {
				e.getPlayer().sendMessage("§c▒§r 도전중인 원정대가 많습니다.");
				e.getPlayer().sendMessage("§c▒§r 잠시 후에 다시 시도해주세요.");
				return;
			}
			
			final int channel = generateChannel();
			int minutes = 7;
			
			if (floor >= 7)
				minutes = 10;
			
			final Expedition exp = new Expedition(list, floor, channel, new TimeTask(channel, 60 * minutes, ScrollUtils.getBossName(floor))
				.runTaskTimer(plugin, 20 * 13, 20));
			plugin.bossListener.expMap.put(channel, exp);
			
			for (Player all : exp.getMembers()) {
				plugin.bossListener.playerMap.put(all, channel);
				
				UserData pData = new UserData(all);
				pData.addBossCount();
				
				removeItem(all, e.getItem().clone(), 1);
				
				all.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0));
				all.sendMessage("§a▒§r §c'그들'§r이 당신의 접근에 반응했습니다.");
				all.sendMessage("§a▒§r 곧 그들과의 전투가 시작됩니다..");
				all.sendMessage("§a▒§r 남은 도전 가능 횟수: "+(7 - pData.getBossCount())+"회 §8(0시에 초기화)");
			}
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					for (Player all : exp.getMembers()) {
						if (all == null) continue;
						
						Location loc = ScrollUtils.getUserSpawn(channel);
						
						//loc.getWorld().refreshChunk(loc.getBlockX(), loc.getBlockZ());
						all.teleport(loc);
						
						all.playSound(loc, Sound.WITHER_IDLE, 100.0F, 0.75F);
						
						all.sendMessage("§a▒§r 전투를 준비하세요!");
						all.sendMessage("§a▒§r 그들이 모습을 드러내고 있습니다.");
					}
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							MobSpawner.SpawnMythicMob("Boss_"+floor+"F", ScrollUtils.getBossSpawn(channel));
							
							for (Player all : exp.getMembers()) {
								if (all == null) continue;
								
								all.playSound(all.getLocation(), Sound.WITHER_SPAWN, 100.0F, 0.75F);
								all.sendMessage("§a▒§r 전투가 시작되었습니다!!");
								
								TitleAPI.sendTitle(all, 30, 60, 40, "§a"+ScrollUtils.getBossName(floor), "§4"+floor+"F :: "+ScrollUtils.getBossSubtitle(floor));
							}
						}
					}, 20*10L);
				}
			}, 20*3L);
		}
	}
	
	public int generateChannel() {
		for (int i = 0; i < 15; i ++) {
			if (plugin.bossListener.expMap.containsKey(new Integer(i))) continue;
			
			return i;
		}
		
		return -1;
	}
	
	public int getItemAmount(Player p, ItemStack target) {
		int amount = 0;
		
		for (ItemStack item : p.getInventory().getContents()) {
			if (item == null || !item.getType().equals(target.getType()) || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) continue;
			
			if (item.getItemMeta().getDisplayName().equals(target.getItemMeta().getDisplayName())) {
				amount += item.clone().getAmount();
			}
		}
		
		return amount;
	}
	
	public boolean hasItem(Player p, ItemStack item, int amount) {
		return getItemAmount(p, item.clone()) >= amount;
	}
	
	public void removeItem(Player p, ItemStack target, int amount) {
		ItemStack[] contents = p.getInventory().getContents().clone();
		
		for (int i = 0; i < contents.length; i ++) {
			ItemStack item = contents[i];
			
			if (item == null || !item.getType().equals(target.getType()) || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) continue;
			if (!item.getItemMeta().getDisplayName().equals(target.getItemMeta().getDisplayName())) continue;
			
			if (item.clone().getAmount() > amount) {
				item.setAmount(item.clone().getAmount() - amount);
				amount = 0;
			} else {
				amount -= item.clone().getAmount();
				item.setType(Material.AIR);
			}
			
			if (amount == 0) {
				break;
			}
		}
		
		p.getInventory().setContents(contents);
	}
	
	private class TimeTask extends BukkitRunnable {
		private final int channel;
		private final int limitTime;
		private final String bossName;
		private int time;
		
		public TimeTask(int channel, int limitTime, String bossName) {
			this.channel = channel;
			this.limitTime = limitTime;
			this.bossName = bossName;
		}
		
		public void run() {
			this.time ++;
			
			int min = time / 60;
			int sec = time % 60;
			String restTime = ((min < 10) ? "0" : "") + min + ":" + ((sec < 10) ? "0" : "") + sec;
			
			Expedition exp = plugin.bossListener.expMap.get(channel);
			
			for (Player p : exp.getMembers()) {
				BarAPI.setMessage(p, "§c" + bossName + " §e[" + restTime + "]");
				
				if (this.time <= 30) {
					p.playSound(p.getLocation(), Sound.CLICK, 10.0F, 2.0F);
				}
			}
			
			if (time >= limitTime) {
				exp.broadcast("§a▒§r =====================================");
				exp.broadcast("§a▒§r ");
				exp.broadcast("§a▒§r ");
				exp.broadcast("§a▒§r 제한시간이 경과하여 도전에 실패했습니다.");
				exp.broadcast("§a▒§r ");
				exp.broadcast("§a▒§r ");
				exp.broadcast("§a▒§r =====================================");
				
				
				plugin.bossListener.clearRoom(channel);
			}
		}
	}
}