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
		Inventory inv = plugin.getServer().createInventory(null, 9*5, "§a▒§r 친구");
		
		inv.setItem(42, plugin.util.createDisplayItem("§a친구 추가", new ItemStack(Material.WATER_BUCKET), new String[]{}));
		inv.setItem(43, plugin.util.createDisplayItem("§c친구 삭제", new ItemStack(Material.LAVA_BUCKET), new String[]{}));
		inv.setItem(44, plugin.util.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), new String[]{}));
		
		for (UUID id : data.getFriends()) {
			OfflinePlayer friend = plugin.getServer().getOfflinePlayer(id);
			ItemStack icon = plugin.util.createDisplayItem((friend.isOnline() ? "§a" : "§c")+friend.getName(), 
					new ItemStack(Material.SKULL_ITEM, 1, (short) 3), new String[]{});
			
			SkullMeta meta = (SkullMeta) icon.getItemMeta();
			meta.setOwner(friend.getName());
			
			icon.setItemMeta(meta);
			
			if (new UserData(friend).getFriends().contains(p.getUniqueId())) {
				plugin.util.addLore(icon, "");
				plugin.util.addLore(icon, "§9서로 친구");
			}
			
			if (!friend.isOnline()) {
				plugin.util.addLore(icon, "§9클릭하면 섬을 방문합니다.");
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
				p.sendMessage("§c▒§r 최대 친구수는 40명입니다!");
				return;
			}
			
			p.closeInventory();
			
			p.sendMessage("§a▒§r 친구로 추가할 플레이어 닉네임을 입력해주세요!");
			
			AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
						return;
					}
					
					if (event.getName().equals("닉네임") || event.getName().isEmpty()) {
						event.getPlayer().sendMessage("§c▒§r 플레이어 닉네임이 입력되지 않았습니다!");
						return;
					}
					
					OfflinePlayer target = plugin.util.getOfflinePlayer(event.getName());
					
					if (target == null) {
						event.getPlayer().sendMessage("§c▒§r <§a"+event.getName()+"§r> 플레이어는 존재하지 않습니다!");
						return;
					}
					
					if (event.getPlayer().equals(target)) {
						event.getPlayer().sendMessage("§c▒§r 자신을 친구로 추가할 수는 없습니다!");
						return;
					}
					
					data.addFriend(target.getUniqueId());
					if (PlayerData.get(event.getPlayer()).hasIsland())
						plugin.efeIsland.getIsleRegion(event.getPlayer()).getMembers().addPlayer(target.getUniqueId());
					
					event.getPlayer().sendMessage("§a▒§r §b"+target.getName()+"§r님이 친구로 등록되었습니다!");
					
					if (target.isOnline()) {
						target.getPlayer().sendMessage("§a▒§r "+event.getPlayer().getName()+"님께서 당신을 친구로 등록했습니다!");
						
						if (!new UserData(target).getFriends().contains(event.getPlayer().getUniqueId())) {
							new FancyMessage("§a▒§r ")
							.then("§b§n/친구§r")
								.command("/친구")
								.tooltip("§b/친구")
							.then(" 명령어로 "+event.getPlayer().getName()+"님을 친구로 등록해보세요.")
							.send(target.getPlayer());
						}
					}
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("닉네임", new ItemStack(Material.WATER_BUCKET), 
					new String[]{}));
			gui.open();
			
		} else if (e.getRawSlot() == 43) {
			p.closeInventory();
			
			p.sendMessage("§a▒§r 삭제할 친구 닉네임을 입력해주세요!");
			
			AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
						return;
					}
					
					if (event.getName().equals("닉네임") || event.getName().isEmpty()) {
						event.getPlayer().sendMessage("§c▒§r 플레이어 닉네임이 입력되지 않았습니다!");
						return;
					}
					
					OfflinePlayer target = plugin.util.getOfflinePlayer(event.getName());
					
					if (target == null) {
						event.getPlayer().sendMessage("§c▒§r <§a"+event.getName()+"§r> 플레이어는 존재하지 않습니다!");
						return;
					}
					
					if (!data.getFriends().contains(target.getUniqueId())) {
						event.getPlayer().sendMessage("§c▒§r 친구로 등록되지 않은 플레이어입니다!");
						return;
					}
					
					data.removeFriend(target.getUniqueId());
					if (PlayerData.get(event.getPlayer()).hasIsland())
						plugin.efeIsland.getIsleRegion(event.getPlayer()).getMembers().removePlayer(target.getUniqueId());
					
					event.getPlayer().sendMessage("§a▒§r §b"+target.getName()+"§r님이 친구 리스트에서 삭제되었습니다.");
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("닉네임", new ItemStack(Material.LAVA_BUCKET), 
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