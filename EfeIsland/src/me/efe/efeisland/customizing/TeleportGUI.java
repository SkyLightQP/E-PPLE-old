package me.efe.efeisland.customizing;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.efe.efecommunity.UserData;
import me.efe.efeisland.EfeFlag;
import me.efe.efeisland.EfeIsland;
import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.PlayerData;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;
import mkremins.fanciful.FancyMessage;

public class TeleportGUI implements Listener {
	public EfeIsland plugin;
	public HashMap<String, Integer> users = new HashMap<String, Integer>();
	
	public TeleportGUI(EfeIsland plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(p, 9*4, "��3�ơ�r �� �ʴ�/�湮");
		UserData data = new UserData(p);
		
		int total = data.getFriends().size();
		int index = total < 7 ? total : 7;
		
		refresh(inv, p, index);
		
		users.put(p.getName(), index);
		p.openInventory(inv);
	}
	
	public void refresh(Inventory inv, Player p, int index) {
		UserData data = new UserData(p);
		int total = data.getFriends().size();
		
		if (index > total)
			index = total;
		if (index < 0)
			index = 0;
		
		if (total == 0) {
			inv.setItem(13, plugin.util.createDisplayItem("��c�湮�� ģ���� �����ϴ�.", skull("MHF_Exclamation"), new String[]{}));
		} else if (total <= 7) {
			int start = 13 - index / 2;
			
			for (int i = 0; i < total; i ++) {
				UUID id = data.getFriends().get(i);
				
				inv.setItem(start + i, friend(id));
			}
			
		} else {
			if (index == 0)
				inv.setItem(9, new ItemStack(Material.AIR));
			else
				inv.setItem(9, plugin.util.createDisplayItem("��a��l<��2��l<", skull("MHF_ArrowLeft"), new String[]{}));
			
			if (index == total)
				inv.setItem(17, new ItemStack(Material.AIR));
			else
				inv.setItem(17, plugin.util.createDisplayItem("��2��l>��a��l>", skull("MHF_ArrowRight"), new String[]{}));
			
			for (int i = index - 7; i < index; i ++) {
				UUID id = data.getFriends().get(i);
				
				inv.setItem(10 + i, friend(id));
			}
		}
		
		inv.setItem(30, plugin.util.createDisplayItem("��b/�� �ʴ�", new ItemStack(Material.BOOK_AND_QUILL), new String[]{"�ٸ� ������ ����� ����", "��ȯ�ǵ��� �ʴ��մϴ�."}));
		inv.setItem(32, plugin.util.createDisplayItem("��b/�� �湮", new ItemStack(Material.BOAT), new String[]{"�ٸ� ������ ���� �湮�մϴ�."}));
	}
	
	private ItemStack skull(String owner) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		
		meta.setOwner(owner);
		item.setItemMeta(meta);
		
		return item;
	}
	
	private ItemStack friend(UUID id) {
		OfflinePlayer player = plugin.getServer().getOfflinePlayer(id);
		PlayerData data = PlayerData.get(id);
		
		if (data.hasIsland()) {
			ProtectedRegion region = plugin.getIsleRegion(player);
			
			String title = region.getFlag(EfeFlag.TITLE);
			String description = region.getFlag(EfeFlag.DESCRIPTION);
			int visiters = plugin.getVisiters(region).size();
			
			return plugin.util.createDisplayItem("��a"+title, skull(player.getName()), 
					new String[]{"\""+description+"\"", "����: "+player.getName(), "���� "+visiters+"�� �湮��", "", "��9��Ŭ��: �湮", "��9��Ŭ��: �ʴ�"});
		} else
			return plugin.util.createDisplayItem("��c"+player.getName(), skull("MHF_Question"), new String[]{"���� �����ϴ�."});
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 36) return;
		
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem().clone();
		SkullMeta meta = item.getItemMeta() instanceof SkullMeta ? (SkullMeta) item.getItemMeta() : null;
		int index = users.get(p.getName());
		
		if (meta != null && !meta.getOwner().startsWith("MHF_")) {
			OfflinePlayer target = plugin.util.getOfflinePlayer(meta.getOwner());
			
			if (e.isRightClick()) {
				if (!target.isOnline()) {
					p.sendMessage("��c�ơ�r ���������� �����Դϴ�.");
					return;
				}
				
				invite(p, target.getPlayer());
			} else {
				p.closeInventory();
				
				IslandUtils.teleportIsland(p, target);
			}
			
		} else if (e.getRawSlot() == 9) {
			refresh(e.getInventory(), p, index - 3);
		} else if (e.getRawSlot() == 17) {
			refresh(e.getInventory(), p, index + 3);
		} else if (e.getRawSlot() == 30) {
			p.closeInventory();
			
			p.sendMessage("��a�ơ�r �ʴ��� ������ �г����� �Է����ּ���.");
			
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
					
					Player target = plugin.util.getOnlinePlayer(event.getName());
					
					if (target == null) {
						event.getPlayer().sendMessage("��c�ơ�r "+event.getName()+"���� �������� �ʰų� �������� �����Դϴ�.");
						return;
					}
					
					invite(p, target);
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("�г���", new ItemStack(Material.NAME_TAG), 
					new String[]{}));
			gui.open();
		} else if (e.getRawSlot() == 32) {
			p.closeInventory();
			
			p.sendMessage("��a�ơ�r �湮�� ������ �г����� �Է����ּ���.");
			
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

					IslandUtils.teleportIsland(p, target);
					
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
		if (users.containsKey(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public void invite(Player p, Player target) {
		UserData data = new UserData(p);
		ProtectedRegion region = plugin.getIsleRegion(p);
		
		if (region.getFlag(EfeFlag.ENTRANCE) == State.DENY && !data.getFriends().contains(target.getUniqueId())) {
			new FancyMessage("��c�ơ�r ����� ���� ��a���� ����r���� �������ּ���. ")
			.then("��b��n/�� Ŀ���͸���¡")
				.command("/�� Ŀ���͸���¡")
				.tooltip("��b/�� Ŀ���͸���¡")
			.send(p);
			
			return;
		}
		
		target.sendMessage("��a�ơ�r "+p.getName()+"�Բ��� ���� �ʴ��ϼ̽��ϴ�.");
		
		new FancyMessage("��a�ơ�r ")
		.then("��b��n/�� �湮 "+p.getName()+"��r")
			.command("/�� �湮 "+p.getName())
			.tooltip("��b/�� �湮 "+p.getName())
		.then(" ��ɾ�� ���� �湮�غ�����.")
		.send(target);
		
		p.sendMessage("��a�ơ�r "+target.getName()+"���� ����� ���� �ʴ��߽��ϴ�.");
	}
}