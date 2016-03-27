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
				
				e.getPlayer().sendMessage("��c�ơ�r ��� �������� ����� �� �ֽ��ϴ�!");
				return;
			}
			
			if (plugin.bossListener.playerMap.containsKey(e.getPlayer())) {
				e.getPlayer().sendMessage("��c�ơ�r �̹� �������� �������Դϴ�!");
				return;
			}
			
			final List<Player> list = new ArrayList<Player>();
			final int floor = ScrollUtils.getFloor(e.getItem());
			
			UserData data = new UserData(e.getPlayer());
			
			if (data.getMaxFloor() < floor) {
				e.getPlayer().sendMessage("��c�ơ�r �ش� ���� ������ �� �����ϴ�!");
				return;
			}
			
			if (data.getBossCount() >= 7) {
				e.getPlayer().sendMessage("��c�ơ�r �Ϸ� ���� Ƚ���� �����߽��ϴ�! ��8(7ȸ)");
				return;
			}
			
			Party party = PartyAPI.getJoinedParty(e.getPlayer());
			
			list.add(e.getPlayer());
			
			if (party != null) {
				if (!party.getOwner().equals(e.getPlayer())) {
					e.getPlayer().sendMessage("��c�ơ�r ��Ƽ�常 ������ �� �ֽ��ϴ�!");
					return;
				}
				
				if (party.getMembers().size() > 5) {
					e.getPlayer().sendMessage("��c�ơ�r �ִ� 5���� ��Ƽ�� ������ �� �ֽ��ϴ�!");
					return;
				}
				
				for (OfflinePlayer m : party.getMembers()) {
					UserData mData = new UserData(m);
					
					if (m.equals(e.getPlayer())) continue;
					
					if (!m.isOnline()) {
						e.getPlayer().sendMessage("��c�ơ�r ���������� ��Ƽ ����� �ֽ��ϴ�!��8: "+m.getName());
						return;
					}
					
					if (!m.getPlayer().getWorld().equals(e.getPlayer().getWorld()) || m.getPlayer().getLocation().distance(e.getPlayer().getLocation()) >= 7.0D) {
						e.getPlayer().sendMessage("��c�ơ�r 7m �̳��� ���� ��Ƽ ����� �ֽ��ϴ�!��8: "+m.getName());
						return;
					}
					
					if (plugin.bossListener.playerMap.containsKey(m)) {
						e.getPlayer().sendMessage("��c�ơ�r �̹� �������� �������� ����� �ֽ��ϴ�!��8: "+m.getName());
						return;
					}
					
					if (!hasItem(m.getPlayer(), e.getItem(), 1)) {
						e.getPlayer().sendMessage("��c�ơ�r ��ũ���� �������� ���� ����� �ֽ��ϴ�!��8: "+m.getName());
						return;
					}
					
					if (mData.getMaxFloor() < floor) {
						e.getPlayer().sendMessage("��c�ơ�r "+floor+"���� ������ �� ���� ����� �ֽ��ϴ�!��8: "+m.getName());
						return;
					}
					
					if (mData.getBossCount() >= 7) {
						e.getPlayer().sendMessage("��c�ơ�r �Ϸ� ���� Ƚ���� ������ ����� �ֽ��ϴ�!��8: "+m.getName());
						return;
					}
					
					list.add(m.getPlayer());
				}
			}
			
			if (plugin.bossListener.expMap.size() >= 15) {
				e.getPlayer().sendMessage("��c�ơ�r �������� �����밡 �����ϴ�.");
				e.getPlayer().sendMessage("��c�ơ�r ��� �Ŀ� �ٽ� �õ����ּ���.");
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
				all.sendMessage("��a�ơ�r ��c'�׵�'��r�� ����� ���ٿ� �����߽��ϴ�.");
				all.sendMessage("��a�ơ�r �� �׵���� ������ ���۵˴ϴ�..");
				all.sendMessage("��a�ơ�r ���� ���� ���� Ƚ��: "+(7 - pData.getBossCount())+"ȸ ��8(0�ÿ� �ʱ�ȭ)");
			}
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					for (Player all : exp.getMembers()) {
						if (all == null) continue;
						
						Location loc = ScrollUtils.getUserSpawn(channel);
						
						//loc.getWorld().refreshChunk(loc.getBlockX(), loc.getBlockZ());
						all.teleport(loc);
						
						all.playSound(loc, Sound.WITHER_IDLE, 100.0F, 0.75F);
						
						all.sendMessage("��a�ơ�r ������ �غ��ϼ���!");
						all.sendMessage("��a�ơ�r �׵��� ����� �巯���� �ֽ��ϴ�.");
					}
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							MobSpawner.SpawnMythicMob("Boss_"+floor+"F", ScrollUtils.getBossSpawn(channel));
							
							for (Player all : exp.getMembers()) {
								if (all == null) continue;
								
								all.playSound(all.getLocation(), Sound.WITHER_SPAWN, 100.0F, 0.75F);
								all.sendMessage("��a�ơ�r ������ ���۵Ǿ����ϴ�!!");
								
								TitleAPI.sendTitle(all, 30, 60, 40, "��a"+ScrollUtils.getBossName(floor), "��4"+floor+"F :: "+ScrollUtils.getBossSubtitle(floor));
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
				BarAPI.setMessage(p, "��c" + bossName + " ��e[" + restTime + "]");
				
				if (this.time <= 30) {
					p.playSound(p.getLocation(), Sound.CLICK, 10.0F, 2.0F);
				}
			}
			
			if (time >= limitTime) {
				exp.broadcast("��a�ơ�r =====================================");
				exp.broadcast("��a�ơ�r ");
				exp.broadcast("��a�ơ�r ");
				exp.broadcast("��a�ơ�r ���ѽð��� ����Ͽ� ������ �����߽��ϴ�.");
				exp.broadcast("��a�ơ�r ");
				exp.broadcast("��a�ơ�r ");
				exp.broadcast("��a�ơ�r =====================================");
				
				
				plugin.bossListener.clearRoom(channel);
			}
		}
	}
}