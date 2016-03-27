package me.efe.efeserver.additory;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OptionGUI implements Listener {
	public EfeServer plugin;
	public List<String> users = new ArrayList<String>();
	
	public OptionGUI(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*5, "§6▒§r 설정");
		
		refresh(inv, p);
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	public void refresh(Inventory inv, Player p) {
		PlayerData data = PlayerData.get(p);
		
		inv.clear();
		
		if (data.getOptionMenu()) {
			inv.setItem(10, plugin.util.createDisplayItem("§e퀵 메뉴 아이템", new ItemStack(Material.NETHER_STAR), 
					new String[]{"클릭하면 메뉴를 여는 아이템을", "핫바에서 제거합니다.", "", "§aTip)§2 \"/메뉴\" 명령어로도", "      §2메뉴를 열 수 있습니다."}));
			inv.setItem(28, plugin.util.createDisplayItem("§aON", new ItemStack(Material.INK_SACK, 1, (short) 10), 
					new String[]{"", "§9클릭하면 아이템을 제거합니다."}));
		} else {
			inv.setItem(10, plugin.util.createDisplayItem("§e퀵 메뉴 아이템", new ItemStack(Material.NETHER_STAR), 
					new String[]{"클릭하면 메뉴를 여는 아이템을", "핫바에 추가합니다.", "", "§aTip)§2 \"/메뉴\" 명령어로도", "      §2메뉴를 열 수 있습니다."}));
			inv.setItem(28, plugin.util.createDisplayItem("§cOFF", new ItemStack(Material.INK_SACK, 1, (short) 8), 
					new String[]{"", "§9클릭하면 아이템을 추가합니다."}));
		}
		
		if (data.getOptionBoat()) {
			inv.setItem(12, plugin.util.createDisplayItem("§b보트 아이템", new ItemStack(Material.BOAT), 
					new String[]{"클릭하면 항해를 시작하는 아이템을", "핫바에서 제거합니다.", "", "§aTip)§2 제거하면 항해가 불가능합니다."}));
			inv.setItem(30, plugin.util.createDisplayItem("§aON", new ItemStack(Material.INK_SACK, 1, (short) 10), 
					new String[]{"", "§9클릭하면 아이템을 제거합니다."}));
		} else {
			inv.setItem(12, plugin.util.createDisplayItem("§b보트 아이템", new ItemStack(Material.BOAT), 
					new String[]{"클릭하면 항해를 시작하는 아이템을", "핫바에 추가합니다.", "", "§aTip)§2 제거하면 항해가 불가능합니다."}));
			inv.setItem(30, plugin.util.createDisplayItem("§cOFF", new ItemStack(Material.INK_SACK, 1, (short) 8), 
					new String[]{"", "§9클릭하면 아이템을 추가합니다."}));
		}
		
		if (data.getOptionChat()) {
			inv.setItem(14, plugin.util.createDisplayItem("§a리얼리스틱 채팅", new ItemStack(Material.REDSTONE_COMPARATOR), 
					new String[]{"클릭하면 300 블럭 밖의 채팅 메세지도", "전달받습니다."}));
			inv.setItem(32, plugin.util.createDisplayItem("§aON", new ItemStack(Material.INK_SACK, 1, (short) 10), 
					new String[]{"", "§9클릭하면 채팅을 받습니다."}));
		} else {
			inv.setItem(14, plugin.util.createDisplayItem("§a리얼리스틱 채팅", new ItemStack(Material.REDSTONE_COMPARATOR), 
					new String[]{"클릭하면 300 블럭 밖의 채팅 메세지를", "전달받지 않습니다."}));
			inv.setItem(32, plugin.util.createDisplayItem("§cOFF", new ItemStack(Material.INK_SACK, 1, (short) 8), 
					new String[]{"", "§9클릭하면 전달받지 않습니다."}));
		}
		
		if (data.getOptionWhisper()) {
			inv.setItem(16, plugin.util.createDisplayItem("§c귓속말", new ItemStack(Material.BOOK_AND_QUILL), 
					new String[]{"클릭하면 귓속말을 차단합니다.", "자신도 귓속말을 할 수 없습니다."}));
			inv.setItem(34, plugin.util.createDisplayItem("§aON", new ItemStack(Material.INK_SACK, 1, (short) 10), 
					new String[]{"", "§9클릭하면 귓속말을 차단합니다."}));
		} else {
			inv.setItem(16, plugin.util.createDisplayItem("§c귓속말", new ItemStack(Material.BOOK_AND_QUILL), 
					new String[]{"클릭하면 귓속말을 허용합니다."}));
			inv.setItem(34, plugin.util.createDisplayItem("§cOFF", new ItemStack(Material.INK_SACK, 1, (short) 8), 
					new String[]{"", "§9클릭하면 귓속말을 허용합니다."}));
		}
		
		inv.setItem(36, plugin.util.createDisplayItem("§9채팅창 청소", new ItemStack(Material.WATER_BUCKET), new String[]{"혹은 F3 + D"}));
		inv.setItem(43, plugin.util.createDisplayItem("§c닫기", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		inv.setItem(44, plugin.util.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), new String[]{}));
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 9*5) return;
		
		Player p = (Player) e.getWhoClicked();
		PlayerData data = PlayerData.get(p);
		
		if (e.getRawSlot() == 10 || e.getRawSlot() == 28) {
			data.setOptionMenu(!data.getOptionMenu());
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			
			if (!updateHotBar(p)) {
				data.setOptionMenu(!data.getOptionMenu());
				
				p.sendMessage("§c▒§r 핫바 9번째 슬롯을 비워주세요!");
			}
			
			refresh(e.getInventory(), p);
		}
		
		if (e.getRawSlot() == 12 || e.getRawSlot() == 30) {
			data.setOptionBoat(!data.getOptionBoat());
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			
			if (!updateHotBar(p)) {
				data.setOptionBoat(!data.getOptionBoat());
				
				if (data.getOptionMenu())
					p.sendMessage("§c▒§r 핫바 8번째 슬롯을 비워주세요!");
				else
					p.sendMessage("§c▒§r 핫바 9번째 슬롯을 비워주세요!");
			}
			
			refresh(e.getInventory(), p);
		}
		
		if (e.getRawSlot() == 14 || e.getRawSlot() == 32) {
			data.setOptionChat(!data.getOptionChat());
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			
			refresh(e.getInventory(), p);
		}
		
		if (e.getRawSlot() == 16 || e.getRawSlot() == 34) {
			data.setOptionWhisper(!data.getOptionWhisper());
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			
			refresh(e.getInventory(), p);
		}
		
		if (e.getRawSlot() == 36) {
			for (int i = 0; i < 30; i ++) {
				p.sendMessage("");
			}
			
			p.playSound(p.getLocation(), Sound.WATER, 1.0F, 1.0F);
		}
		
		if (e.getRawSlot() == 43) {
			p.closeInventory();
		}
		
		if (e.getRawSlot() == 44) {
			p.closeInventory();
			plugin.mainGui.openGUI(p);
		}
	}
	
	public boolean updateHotBar(Player p) {
		PlayerData data = PlayerData.get(p);
		
		p.getInventory().remove(Material.BOAT);
		p.getInventory().remove(Material.COMPASS);
		p.getInventory().remove(Material.NETHER_STAR);
		
		if (data.getOptionBoat()) {
			
			if (data.getOptionMenu()) {
				if (p.getInventory().getItem(7) != null && !p.getInventory().getItem(7).getType().equals(Material.AIR)) return false;
				
				p.getInventory().setItem(7, plugin.myboat.getBoatItem(p));
			} else {
				if (p.getInventory().getItem(8) != null && !p.getInventory().getItem(8).getType().equals(Material.AIR)) return false;
				
				p.getInventory().setItem(8, plugin.myboat.getBoatItem(p));
			}
		}
		
		if (data.getOptionMenu()) {
			if (p.getInventory().getItem(8) != null && !p.getInventory().getItem(8).getType().equals(Material.AIR)) return false;
			
			p.getInventory().setItem(8, plugin.util.createDisplayItem("§e/메뉴", new ItemStack(Material.NETHER_STAR), new String[]{"클릭하면 메인 메뉴를 엽니다."}));
		}
		
		return true;
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}