package me.efe.efeisland.customizing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.efe.efeisland.EfeFlag;
import me.efe.efeisland.EfeIsland;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;

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

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class BlacklistGUI implements Listener {
	public EfeIsland plugin;
	public List<String> users = new ArrayList<String>();
	
	public BlacklistGUI(EfeIsland plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*6, "§3▒§r 블랙리스트 설정");
		ProtectedRegion region = plugin.getIsleRegion(p);
		
		int i = 0;
		for (String id : region.getFlag(EfeFlag.BLACKLIST)) {
			if (i >= 52)
				break;
			
			OfflinePlayer player = plugin.getServer().getOfflinePlayer(UUID.fromString(id));
			ItemStack icon = plugin.util.createDisplayItem("§c"+player.getName(), new ItemStack(Material.SKULL_ITEM, 1, (short) 3),
					new String[]{"", "§9클릭하면 리스트에서 삭제합니다."});
			
			SkullMeta meta = (SkullMeta) icon.getItemMeta();
			meta.setOwner(player.getName());
			
			icon.setItemMeta(meta);
			
			inv.addItem(icon);
			i ++;
		}
		
		inv.setItem(52, plugin.util.createDisplayItem("§a유저 추가", new ItemStack(Material.WATER_BUCKET), new String[]{}));
		inv.setItem(53, plugin.util.createDisplayItem("§c유저 삭제", new ItemStack(Material.LAVA_BUCKET), new String[]{}));
		
		users.add(p.getName());
		p.openInventory(inv);
	}

	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (!e.getInventory().getTitle().equals("§3▒§r 블랙리스트 설정")) return;
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getRawSlot() >= 54) return;
		
		Player p = (Player) e.getWhoClicked();
		ProtectedRegion region = plugin.getIsleRegion(p);
		
		if (e.getRawSlot() == 52) {
			p.sendMessage("§a▒§r 추가할 유저 닉네임을 입력해주세요!");
			
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
					
					if (target.equals(p)) {
						event.getPlayer().sendMessage("§c▒§r 자신을 등록할 순 없습니다!");
						return;
					}
					
					Set<String> set = region.getFlag(EfeFlag.BLACKLIST);
					
					if (set.contains(target.getUniqueId().toString())) {
						event.getPlayer().sendMessage("§c▒§r 이미 블랙리스트에 등록된 플레이어입니다!");
						return;
					}
					
					set.add(target.getUniqueId().toString());
					region.setFlag(EfeFlag.BLACKLIST, set);
					
					if (target.isOnline() && plugin.getVisiters(region).contains(target.getPlayer())) {
						target.getPlayer().sendMessage("§a▒§r 방문중인 섬의 주인이 당신을 블랙리스트에 등록했습니다.");
						target.getPlayer().teleport(plugin.getIsleSpawnLoc(target));
					}
					
					event.getPlayer().sendMessage("§a▒§r §c"+target.getName()+"§r님이 블랙리스트에 등록되었습니다.");
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("닉네임", new ItemStack(Material.LAVA_BUCKET), 
					new String[]{}));
			gui.open();
			
		} else if (e.getRawSlot() == 53) {
			p.sendMessage("§a▒§r 삭제할 유저 닉네임을 입력해주세요!");
			
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
					
					Set<String> set = region.getFlag(EfeFlag.BLACKLIST);
					
					if (!set.contains(target.getUniqueId().toString())) {
						event.getPlayer().sendMessage("§c▒§r 블랙리스트에 등록되지 않은 플레이어입니다!");
						return;
					}
					
					set.remove(target.getUniqueId().toString());
					region.setFlag(EfeFlag.BLACKLIST, set);
					
					event.getPlayer().sendMessage("§a▒§r §c"+target.getName()+"§r님이 블랙리스트에서 삭제되었습니다.");
					
					event.setWillClose(true);
					event.setWillDestroy(true);
				}
			});
			
			gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("닉네임", new ItemStack(Material.LAVA_BUCKET), 
					new String[]{}));
			gui.open();
		} else {
			String name = e.getCurrentItem().getItemMeta().getDisplayName();
			OfflinePlayer target = plugin.util.getOfflinePlayer(name);
			
			Set<String> set = region.getFlag(EfeFlag.BLACKLIST);
			set.remove(target.getUniqueId().toString());
			region.setFlag(EfeFlag.BLACKLIST, set);
			
			p.sendMessage("§a▒§r §c"+target.getName()+"§r님이 블랙리스트에서 삭제되었습니다.");
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}