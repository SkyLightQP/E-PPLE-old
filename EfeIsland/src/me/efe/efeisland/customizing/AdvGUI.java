package me.efe.efeisland.customizing;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeisland.EfeFlag;
import me.efe.efeisland.EfeIsland;
import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class AdvGUI implements Listener {
	public EfeIsland plugin;
	public List<String> users = new ArrayList<String>();
	public final double price = 1000.0D;
	
	public AdvGUI(EfeIsland plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*6, "��3�ơ�r �� ȫ��");
		
		List<Island> list = Lists.reverse(getAdvertisements());
		
		for (int i = 0; i < list.size(); i ++) {
			Island isl = list.get(i);
			
			inv.setItem(i, plugin.util.createDisplayItem("��b>> "+isl.title, new ItemStack(Material.GRASS), 
					new String[]{
				"\""+isl.description+"\"", 
				"����: "+isl.owner, 
				"���� "+isl.visit+"�� �湮��", 
				"", 
				"��9Ŭ���ϸ� �̵��մϴ�."}));
		}
		
		inv.setItem(52, plugin.util.createDisplayItem("��eȫ�� ���", new ItemStack(Material.GLOWSTONE_DUST), 
				new String[]{"��� ���: "+price+"E", "Ŭ���ϸ� �� ȫ���� ����մϴ�.", "��ϵ� ȫ���� ������ �Ұ����մϴ�."}));
		
		inv.setItem(53, plugin.util.createDisplayItem("��6��nHow to use?", new ItemStack(Material.WRITTEN_BOOK), 
				new String[]{
			"Ÿ�ο��� ����� ���� ȫ���غ�����!", 
			"ȫ�� ����Ʈ�� �ִ� ��e50����r����", 
			"���� ������ �� ������,", 
			"�� �̻��� �Ѿ�� ��n���� ��ϵ�", 
			"������ ����Ʈ���� ���š�r�˴ϴ�.", 
			"���ŵ� �� ȫ���� ��n�ٽ� ��ϡ�r�Ͽ�", 
			"ȫ�� ����Ʈ�� ������ �� �ֽ��ϴ�."}));
		
		p.openInventory(inv);
		users.add(p.getName());
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (!e.getInventory().getTitle().equals("��3�ơ�r �� ȫ��")) return;
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 54) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getRawSlot() == 52) {
			List<Island> list = Lists.reverse(getAdvertisements());
			int i = -1;
			
			for (int j = 0; j < list.size(); j ++) {
				if (list.get(j).owner == p.getName()) {
					i = j;
				}
			}
			
			if (i < 10 && i != -1) {
				p.sendMessage("��c�ơ�r ����Ʈ���� �ּ� 10��° �̻��� ȫ������ ������ �����մϴ�!");
				p.sendMessage("��c�ơ�r �ٸ� ������ ȫ���� �� ��ϵǱ⸦ ��ٷ��ּ���.");
				return;
			}
			
			if (!EfeServer.getInstance().vault.hasEnough(p, price)) {
				p.sendMessage("��c�ơ�r �������� �����մϴ�!");
				return;
			}
			
			EfeServer.getInstance().vault.take(p, price);
			
			addAdvertisement(p);
			
			p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
			p.sendMessage("��a�ơ�r �� ȫ���� ��ϵǾ����ϴ�! ��8[-" + price + "E]");
			
			p.closeInventory();
			openGUI(p);
		} else if (0 <= e.getRawSlot() && e.getRawSlot() <= 49) {
			List<Island> list = Lists.reverse(getAdvertisements());
			if (list.size() <= e.getRawSlot()) return;
			
			p.closeInventory();
			
			PlayerData data = PlayerData.getPlayerData(list.get(e.getRawSlot()).name);
			OfflinePlayer t = data.getPlayer();
			
			IslandUtils.teleportIsland(p, t);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public List<Island> getAdvertisements() {
		List<Island> list = new ArrayList<Island>();
		
		if (!plugin.getConfig().contains("adv")) return list;
		
		for (String adv : plugin.getConfig().getStringList("adv")) {
			ProtectedRegion region = WGBukkit.getRegionManager(plugin.world).getRegion(adv);
			
			String owner = plugin.getIsleOwner(region).getName();
			int visit = plugin.getVisiters(region).size();
			
			list.add(new Island(adv, region.getFlag(EfeFlag.TITLE), region.getFlag(EfeFlag.DESCRIPTION), owner, visit));
		}
		
		return list;
	}
	
	public void addAdvertisement(Player p) {
		List<String> list = plugin.getConfig().getStringList("adv");
		String value = PlayerData.get(p).getIslName();
		
		if (list.contains(value)) list.remove(value);
		
		list.add(value);
		
		if (list.size() >= 50) list.remove(1);
		
		plugin.getConfig().set("adv", list);
		plugin.saveConfig();
	}
	
	private class Island {
		private final String name;
		private final String title;
		private final String description;
		private final String owner;
		private final int visit;
		
		public Island(String name, String title, String description, String owner, int visit) {
			this.name = name;
			this.title = title;
			this.description = description;
			this.owner = owner;
			this.visit = visit;
		}
	}
}