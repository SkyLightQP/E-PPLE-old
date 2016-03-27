package me.efe.efeserver.additory;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.util.CashAPI;
import mkremins.fanciful.FancyMessage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CashGUI implements Listener {
	public EfeServer plugin;
	public List<String> users = new ArrayList<String>();
	
	public CashGUI(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(p, 9*4, "§1▒§r 후원 §9["+CashAPI.getBalance(p)+"코인]");
		
		inv.setItem(22, plugin.util.createDisplayItem("§e코인 인벤토리", new ItemStack(Material.CHEST), new String[]{"칭호와 블러드 스킨을", "확인하거나 설정합니다."}));
		inv.setItem(19, plugin.util.createDisplayItem("§a코인샵", new ItemStack(Material.SPECKLED_MELON), new String[]{"코인로 특별한 아이템을", "구매할 수 있습니다."}));
		inv.setItem(25, plugin.util.createDisplayItem("§b후원", new ItemStack(Material.GOLDEN_APPLE), new String[]{"카페 게시글로 연결됩니다."}));
		inv.setItem(8, plugin.util.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), new String[]{}));
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 9*4) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getRawSlot() == 8) {
			p.closeInventory();
			plugin.mainGui.openGUI(p);
		} else if (e.getRawSlot() == 22) {
			e.getInventory().setItem(13, plugin.util.createDisplayItem("§c/블러드스킨", new ItemStack(Material.REDSTONE), new String[]{}));
			e.getInventory().setItem(31, plugin.util.createDisplayItem("§d/칭호", new ItemStack(Material.WRITTEN_BOOK), new String[]{}));
		} else if (e.getRawSlot() == 13) {
			p.closeInventory();
			plugin.getServer().dispatchCommand(p, "블러드스킨");
		} else if (e.getRawSlot() == 31) {
			p.closeInventory();
			plugin.getServer().dispatchCommand(p, "칭호");
		} else if (e.getRawSlot() == 19) {
			p.closeInventory();
			plugin.cashShopGui.openGUI(p);
		} else if (e.getRawSlot() == 25) {
			p.closeInventory();
			
			new FancyMessage("§a▒§r ")
			.then("§8§l>§7§l>§r E-PPLE 카페 후원 게시글 바로가기")
				.link("http://cafe.naver.com/efeservercafe/231")
				.tooltip("§bhttp://cafe.naver.com/efeservercafe/231")
			.send(p);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}