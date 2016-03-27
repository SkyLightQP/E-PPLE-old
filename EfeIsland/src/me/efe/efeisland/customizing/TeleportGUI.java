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
		Inventory inv = plugin.getServer().createInventory(p, 9*4, "§3▒§r 섬 초대/방문");
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
			inv.setItem(13, plugin.util.createDisplayItem("§c방문할 친구가 없습니다.", skull("MHF_Exclamation"), new String[]{}));
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
				inv.setItem(9, plugin.util.createDisplayItem("§a§l<§2§l<", skull("MHF_ArrowLeft"), new String[]{}));
			
			if (index == total)
				inv.setItem(17, new ItemStack(Material.AIR));
			else
				inv.setItem(17, plugin.util.createDisplayItem("§2§l>§a§l>", skull("MHF_ArrowRight"), new String[]{}));
			
			for (int i = index - 7; i < index; i ++) {
				UUID id = data.getFriends().get(i);
				
				inv.setItem(10 + i, friend(id));
			}
		}
		
		inv.setItem(30, plugin.util.createDisplayItem("§b/섬 초대", new ItemStack(Material.BOOK_AND_QUILL), new String[]{"다른 유저를 당신의 섬에", "소환되도록 초대합니다."}));
		inv.setItem(32, plugin.util.createDisplayItem("§b/섬 방문", new ItemStack(Material.BOAT), new String[]{"다른 유저의 섬에 방문합니다."}));
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
			
			return plugin.util.createDisplayItem("§a"+title, skull(player.getName()), 
					new String[]{"\""+description+"\"", "주인: "+player.getName(), "현재 "+visiters+"명 방문중", "", "§9좌클릭: 방문", "§9우클릭: 초대"});
		} else
			return plugin.util.createDisplayItem("§c"+player.getName(), skull("MHF_Question"), new String[]{"섬이 없습니다."});
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
					p.sendMessage("§c▒§r 오프라인인 유저입니다.");
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
			
			p.sendMessage("§a▒§r 초대할 유저의 닉네임을 입력해주세요.");
			
			AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
						return;
					}
					
					if (event.getName().isEmpty()) {
						event.getPlayer().sendMessage("§c▒§r 닉네임을 입력해주세요.");
						return;
					}
					
					Player target = plugin.util.getOnlinePlayer(event.getName());
					
					if (target == null) {
						event.getPlayer().sendMessage("§c▒§r "+event.getName()+"님은 존재하지 않거나 오프라인 상태입니다.");
						return;
					}
					
					invite(p, target);
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("닉네임", new ItemStack(Material.NAME_TAG), 
					new String[]{}));
			gui.open();
		} else if (e.getRawSlot() == 32) {
			p.closeInventory();
			
			p.sendMessage("§a▒§r 방문할 유저의 닉네임을 입력해주세요.");
			
			AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
				
				@Override
				public void onAnvilClick(AnvilClickEvent event) {
					if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
						event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
						return;
					}
					
					if (event.getName().isEmpty()) {
						event.getPlayer().sendMessage("§c▒§r 닉네임을 입력해주세요.");
						return;
					}
					
					OfflinePlayer target = plugin.util.getOfflinePlayer(event.getName());
					
					if (target == null) {
						event.getPlayer().sendMessage("§c▒§r "+event.getName()+"님은 존재하지 않습니다.");
						return;
					}

					IslandUtils.teleportIsland(p, target);
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("닉네임", new ItemStack(Material.NAME_TAG), 
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
			new FancyMessage("§c▒§r 당신의 섬을 §a열린 섬§r으로 설정해주세요. ")
			.then("§b§n/섬 커스터마이징")
				.command("/섬 커스터마이징")
				.tooltip("§b/섬 커스터마이징")
			.send(p);
			
			return;
		}
		
		target.sendMessage("§a▒§r "+p.getName()+"님께서 섬에 초대하셨습니다.");
		
		new FancyMessage("§a▒§r ")
		.then("§b§n/섬 방문 "+p.getName()+"§r")
			.command("/섬 방문 "+p.getName())
			.tooltip("§b/섬 방문 "+p.getName())
		.then(" 명령어로 섬을 방문해보세요.")
		.send(target);
		
		p.sendMessage("§a▒§r "+target.getName()+"님을 당신의 섬에 초대했습니다.");
	}
}