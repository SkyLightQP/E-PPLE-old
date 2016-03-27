package me.efe.efecommunity.listeners;

import java.util.ArrayList;
import java.util.List;
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

import me.efe.efecommunity.EfeCommunity;
import me.efe.efecommunity.UserData;
import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;
import me.efe.unlimitedrpg.playerinfo.PlayerInfo;
import mkremins.fanciful.FancyMessage;

public class FriendListener implements Listener {
	public EfeCommunity plugin;
	public List<String> users = new ArrayList<String>();
	
	public FriendListener(EfeCommunity plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		UserData data = new UserData(p);
		Inventory inv = plugin.getServer().createInventory(null, 9*5, "��a�ơ�r ģ��");
		
		inv.setItem(42, plugin.util.createDisplayItem("��aģ�� �߰�", new ItemStack(Material.WATER_BUCKET), new String[]{}));
		inv.setItem(43, plugin.util.createDisplayItem("��cģ�� ����", new ItemStack(Material.LAVA_BUCKET), new String[]{}));
		inv.setItem(44, plugin.util.createDisplayItem("��e���� �޴�", new ItemStack(Material.NETHER_STAR), new String[]{}));
		
		for (UUID id : data.getFriends()) {
			OfflinePlayer friend = plugin.getServer().getOfflinePlayer(id);
			ItemStack icon = plugin.util.createDisplayItem((friend.isOnline() ? "��a" : "��c")+friend.getName(), 
					new ItemStack(Material.SKULL_ITEM, 1, (short) 3), new String[]{});
			
			SkullMeta meta = (SkullMeta) icon.getItemMeta();
			meta.setOwner(friend.getName());
			
			icon.setItemMeta(meta);
			
			if (new UserData(friend).getFriends().contains(p.getUniqueId())) {
				plugin.util.addLore(icon, "");
				plugin.util.addLore(icon, "��9���� ģ��");
			}
			
			if (!friend.isOnline()) {
				plugin.util.addLore(icon, "��9Ŭ���ϸ� ���� �湮�մϴ�.");
			}
			
			inv.addItem(icon);
		}
		
		p.openInventory(inv);
		users.add(p.getName());
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getRawSlot() >= 45) return;
		
		Player p = (Player) e.getWhoClicked();
		final UserData data = new UserData(p);
		
		if (e.getRawSlot() == 42) {
			if (data.getFriends().size() > 40) {
				p.sendMessage("��c�ơ�r �ִ� ģ������ 40���Դϴ�!");
				return;
			}
			
			p.closeInventory();
			
			p.sendMessage("��a�ơ�r ģ���� �߰��� �÷��̾� �г����� �Է����ּ���!");
			
			AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
						return;
					}
					
					if (event.getName().equals("�г���") || event.getName().isEmpty()) {
						event.getPlayer().sendMessage("��c�ơ�r �÷��̾� �г����� �Էµ��� �ʾҽ��ϴ�!");
						return;
					}
					
					OfflinePlayer target = plugin.util.getOfflinePlayer(event.getName());
					
					if (target == null) {
						event.getPlayer().sendMessage("��c�ơ�r <��a"+event.getName()+"��r> �÷��̾�� �������� �ʽ��ϴ�!");
						return;
					}
					
					if (event.getPlayer().equals(target)) {
						event.getPlayer().sendMessage("��c�ơ�r �ڽ��� ģ���� �߰��� ���� �����ϴ�!");
						return;
					}
					
					data.addFriend(target.getUniqueId());
					if (PlayerData.get(event.getPlayer()).hasIsland())
						plugin.efeIsland.getIsleRegion(event.getPlayer()).getMembers().addPlayer(target.getUniqueId());
					
					event.getPlayer().sendMessage("��a�ơ�r ��b"+target.getName()+"��r���� ģ���� ��ϵǾ����ϴ�!");
					
					if (target.isOnline()) {
						target.getPlayer().sendMessage("��a�ơ�r "+event.getPlayer().getName()+"�Բ��� ����� ģ���� ����߽��ϴ�!");
						
						if (!new UserData(target).getFriends().contains(event.getPlayer().getUniqueId())) {
							new FancyMessage("��a�ơ�r ")
							.then("��b��n/ģ����r")
								.command("/ģ��")
								.tooltip("��b/ģ��")
							.then(" ��ɾ�� "+event.getPlayer().getName()+"���� ģ���� ����غ�����.")
							.send(target.getPlayer());
						}
					}
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("�г���", new ItemStack(Material.WATER_BUCKET), 
					new String[]{}));
			gui.open();
			
		} else if (e.getRawSlot() == 43) {
			p.closeInventory();
			
			p.sendMessage("��a�ơ�r ������ ģ�� �г����� �Է����ּ���!");
			
			AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
						return;
					}
					
					if (event.getName().equals("�г���") || event.getName().isEmpty()) {
						event.getPlayer().sendMessage("��c�ơ�r �÷��̾� �г����� �Էµ��� �ʾҽ��ϴ�!");
						return;
					}
					
					OfflinePlayer target = plugin.util.getOfflinePlayer(event.getName());
					
					if (target == null) {
						event.getPlayer().sendMessage("��c�ơ�r <��a"+event.getName()+"��r> �÷��̾�� �������� �ʽ��ϴ�!");
						return;
					}
					
					if (!data.getFriends().contains(target.getUniqueId())) {
						event.getPlayer().sendMessage("��c�ơ�r ģ���� ��ϵ��� ���� �÷��̾��Դϴ�!");
						return;
					}
					
					data.removeFriend(target.getUniqueId());
					if (PlayerData.get(event.getPlayer()).hasIsland())
						plugin.efeIsland.getIsleRegion(event.getPlayer()).getMembers().removePlayer(target.getUniqueId());
					
					event.getPlayer().sendMessage("��a�ơ�r ��b"+target.getName()+"��r���� ģ�� ����Ʈ���� �����Ǿ����ϴ�.");
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("�г���", new ItemStack(Material.LAVA_BUCKET), 
					new String[]{}));
			gui.open();
			
		} else if (e.getRawSlot() == 44) {
			p.closeInventory();
			EfeServer.getInstance().mainGui.openGUI(p);
			
		} else if (e.getCurrentItem().getDurability() == 3) {
			String name = e.getCurrentItem().clone().getItemMeta().getDisplayName().substring(2);
			OfflinePlayer target = plugin.util.getOfflinePlayer(name);
			
			if (target == null) return;
			
			if (target.isOnline())
				PlayerInfo.openGUI(target.getPlayer(), p);
			else
				IslandUtils.teleportIsland(p, target);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}