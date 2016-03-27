package me.efe.efeisland.customizing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import me.efe.efeisland.EfeFlag;
import me.efe.efeisland.EfeIsland;
import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RankingGUI implements Listener {
	public EfeIsland plugin;
	public List<String> users = new ArrayList<String>();
	public List<Point> points = new ArrayList<Point>();
	private PointComparator comparator = new PointComparator();
	
	public RankingGUI(EfeIsland plugin) {
		this.plugin = plugin;
		this.sort();
	}
	
	public void openGUI(Player player) {
		String rank = (indexOf(player) != -1 ? indexOf(player)+"" : "-");
		Inventory inv = plugin.getServer().createInventory(null, 9*3, "��3�ơ�r �� ��ŷ [�� ����: "+rank+"��]");
		
		sort();
		
		if (points.isEmpty()) {
			inv.setItem(13, plugin.util.createDisplayItem("��c��õ���� ���� �����ϴ�.", new ItemStack(Material.BOWL), new String[]{"�ٸ� ���� ��õ�غ�����! ��e/�� ��õ��r"}));
			
		} else {
			for (int i = 0; i < 25; i ++) {
				if (points.size() <= i)
					break;
				
				Point point = points.get(i);
				OfflinePlayer owner = plugin.getServer().getOfflinePlayer(point.id);
				int recommended = getRecommendation(owner);
				
				if (recommended == 0)
					continue;
				
				ProtectedRegion region = plugin.getIsleRegion(owner);
				String title = region.getFlag(EfeFlag.TITLE);
				String description = region.getFlag(EfeFlag.DESCRIPTION);
				int visit = plugin.getVisiters(region).size();
				
				Material type = i == 0 ? Material.DIAMOND_BLOCK : i == 1 ? Material.GOLD_BLOCK : i == 2 ? Material.IRON_BLOCK : Material.GRASS;
				
				inv.setItem(i, plugin.util.createDisplayItem("��b>> "+title, new ItemStack(type), new String[]{
					"\""+description+"\"", 
					"����: "+owner.getName(), 
					"���� "+visit+"�� �湮��",
					"��õ ���� ��: "+recommended,
					"", 
					"��9Ŭ���ϸ� �湮�մϴ�."}));
			}
		}
		
		inv.setItem(25, plugin.util.createDisplayItem("��b/�� ��õ <Player>", new ItemStack(Material.COOKIE), new String[]{}));
		
		inv.setItem(26, plugin.util.createDisplayItem("��6��nHow to use?", new ItemStack(Material.WRITTEN_BOOK), 
				new String[]{
			"������ ����Ʈ�� �Ǿ����!", 
			"�Ϸ翡 �� �� Ÿ���� ����",
			"��õ�� �� ������, �� ����",
			"�� �ֿ� 2�� �̻� ��õ�� ��",
			"�����ϴ�. ������ ������ 0�ÿ�",
			"�ʱ�ȭ�˴ϴ�."}));
		
		users.add(player.getName());
		player.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (!e.getInventory().getTitle().startsWith("��3�ơ�r �� ��ŷ")) return;
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 27) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getRawSlot() < 25 && e.getCurrentItem().getType() != Material.BOWL) {
			p.closeInventory();
			
			UUID id = points.get(e.getRawSlot()).id;
			OfflinePlayer target = plugin.getServer().getOfflinePlayer(id);
			
			IslandUtils.teleportIsland(p, target);
			
		} else if (e.getRawSlot() == 25 || e.getCurrentItem().getType() == Material.BOWL) {
			p.closeInventory();
			
			p.sendMessage("��a�ơ�r ��õ�� ���� ���� �г����� �Է����ּ���.");
			
			AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
						return;
					}
					
					if (event.getName().isEmpty()) {
						event.getPlayer().sendMessage("��c�ơ�r �г����� �Է����ּ���.");
						return;
					}
					
					OfflinePlayer target = plugin.util.getOfflinePlayer(event.getName());
					
					if (target == null) {
						event.getPlayer().sendMessage("��c�ơ�r "+event.getName()+"���� �������� �ʽ��ϴ�.");
						return;
					}
					
					plugin.getServer().dispatchCommand(event.getPlayer(), "�� ��õ "+target.getName());
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("�г���", new ItemStack(Material.NAME_TAG), 
					new String[]{}));
			gui.open();
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public boolean isAlreadyRecommended(Player player, OfflinePlayer target) {
		FileConfiguration config = getConfig();
		String playerId = player.getUniqueId().toString();
		String targetId = target.getUniqueId().toString();
		
		return config.contains(playerId+".target") && config.getStringList(playerId+".target").contains(targetId);
	}
	
	public void recommend(Player player, OfflinePlayer target) {
		FileConfiguration config = getConfig();
		String playerId = player.getUniqueId().toString();
		String targetId = target.getUniqueId().toString();
		
		List<String> list = config.contains(playerId+".target") ? config.getStringList(playerId+".target") : new ArrayList<String>();
		list.add(targetId);
		
		int amount = config.contains(targetId+".amount") ? config.getInt(targetId+".amount") : 0;
		amount ++;
		
		config.set(playerId+".target", list);
		config.set(targetId+".amount", amount);
		saveConfig(config);
		
		sort(player.getUniqueId(), amount);
	}
	
	public int getRecommendation(OfflinePlayer player) {
		FileConfiguration config = getConfig();
		String playerId = player.getUniqueId().toString();
		
		return config.getInt(playerId+".amount");
	}
	
	public int indexOf(OfflinePlayer player) {
		for (int i = 0; i < points.size(); i ++) {
			if (points.get(i).id.equals(player.getUniqueId())) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void resetData() {
		File file = new File(plugin.getDataFolder(), "ranking.yml");
		
		if (file.exists())
			file.delete();
		
		points.clear();
	}
	
	private class PointComparator implements Comparator<Point> {
		
		@Override
		public int compare(Point arg0, Point arg1) {
			return (arg0.amount > arg1.amount) ? -1 : (arg0.amount < arg1.amount) ? 1 : 0;
		}
		
		@Override
		public Comparator<Point> reversed() {
			return null;
		}
		
		@Override
		public Comparator<Point> thenComparing(Comparator<? super Point> arg0) {
			return null;
		}
		
		@Override
		public <U extends Comparable<? super U>> Comparator<Point> thenComparing(
				Function<? super Point, ? extends U> arg0) {
			return null;
		}
		
		@Override
		public <U> Comparator<Point> thenComparing(
				Function<? super Point, ? extends U> arg0,
				Comparator<? super U> arg1) {
			return null;
		}
		
		@Override
		public Comparator<Point> thenComparingDouble(
				ToDoubleFunction<? super Point> arg0) {
			return null;
		}
		
		@Override
		public Comparator<Point> thenComparingInt(
				ToIntFunction<? super Point> arg0) {
			return null;
		}
		
		@Override
		public Comparator<Point> thenComparingLong(
				ToLongFunction<? super Point> arg0) {
			return null;
		}
	}
	
	public class Point {
		private UUID id;
		private int amount;
		
		public Point(UUID id, int amount) {
			this.id = id;
			this.amount = amount;
		}
	}
	
	private void sort(UUID newId, int newAmount) {
		boolean isOverlaped = false;
		
		for (int i = 0; i < points.size(); i ++) {
			if (points.get(i).id.equals(newId)) {
				points.set(i, new Point(newId, newAmount));
				
				isOverlaped = true;
			}
		}
		
		if (!isOverlaped) {
			points.add(new Point(newId, newAmount));
		}
		
		Collections.sort(points, comparator);
	}
	
	private void sort() {
		points.clear();
		
		FileConfiguration config = getConfig();
		
		for (String path : config.getKeys(false)) {
			int amount = config.getInt(path+".amount");
			
			points.add(new Point(UUID.fromString(path), amount));
		}
		
		Collections.sort(points, comparator);
	}
	
	private FileConfiguration getConfig() {
		File file = new File(plugin.getDataFolder(), "ranking.yml");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return YamlConfiguration.loadConfiguration(file);
	}
	
	private void saveConfig(FileConfiguration config) {
		File file = new File(plugin.getDataFolder(), "ranking.yml");
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}