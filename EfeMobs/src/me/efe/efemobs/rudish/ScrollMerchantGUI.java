package me.efe.efemobs.rudish;

import java.util.ArrayList;
import java.util.List;

import me.efe.efemobs.EfeMobs;
import me.efe.efeserver.util.Scoreboarder;
import me.efe.unlimitedrpg.unlimitedtag.TagType;
import me.efe.unlimitedrpg.unlimitedtag.UnlimitedTagAPI;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class ScrollMerchantGUI implements Listener {
	public EfeMobs plugin;
	public List<String> users = new ArrayList<String>();
	public int[] prices = new int[]{30, 40, 50, 120, 140, 160, 270, 300, 375, 450};
	
	public ScrollMerchantGUI(EfeMobs plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		UserData data = new UserData(p);
		Inventory inv = plugin.getServer().createInventory(null, 9*4, "§6▒§r 스크롤 상인 [§3"+data.getSoul()+" Souls§r]");
		
		int[] slots = new int[]{11, 12, 13, 14, 15, 20, 21, 22, 23, 24};
		
		for (int i = 1; i <= 10; i ++) {
			ItemStack item = scroll(i);
			
			plugin.util.addLore(item, "§d§m==================");
			
			if (data.getSoul() >= prices[i - 1])
				plugin.util.addLore(item, "§3가격: §b§l"+prices[i - 1]+" Souls");
			else
				plugin.util.addLore(item, "§4가격: §c§l"+prices[i - 1]+" Souls");
			
			plugin.util.addLore(item, "");
			plugin.util.addLore(item, "§9클릭으로 아이템을 교환합니다.");
			
			inv.setItem(slots[i - 1], item);
		}
		
//		ItemStack arrow = new ItemStack(Material.ARROW, 32);
//		if (data.getSoul() >= 30)
//			plugin.util.addLore(arrow, "§3가격: §b§l100 Souls");
//		else
//			plugin.util.addLore(arrow, "§4가격: §c§l100 Souls");
//		plugin.util.addLore(arrow, "");
//		plugin.util.addLore(arrow, "§9클릭으로 아이템을 교환합니다.");
//		inv.setItem(35, arrow);
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 36) return;
		
		Player p = (Player) e.getWhoClicked();
		UserData data = new UserData(p);
		
		if ((11 <= e.getRawSlot() && e.getRawSlot() <= 15) || (20 <= e.getRawSlot() && e.getRawSlot() <= 24)) {
			int floor = e.getRawSlot() - (e.getRawSlot() <= 15 ? 10 : 14);
			
			if (floor > data.getMaxFloor()) {
				p.sendMessage("§c▒§r 해당 층은 아직 거래할 수 없습니다!");
				return;
			}
			
			if (data.getSoul() < prices[floor - 1]) {
				p.sendMessage("§c▒§r 소울이 부족합니다!");
				return;
			}
			
			data.takeSoul(prices[floor - 1]);
			
			ItemStack item = scroll(floor);
			UnlimitedTagAPI.addTag(item, TagType.VESTED, p.getUniqueId().toString());
			plugin.util.giveItem(p, item);
			
			p.closeInventory();
			openGUI(p);
			
			Scoreboarder.updateObjectives(p);
			
			p.playSound(p.getLocation(), Sound.PORTAL_TRIGGER, 1.0F, 1.5F);
			p.sendMessage("§a▒§r 교환이 성사되었습니다.");
		} else if (e.getCurrentItem().getType() == Material.ARROW) {
			if (data.getSoul() < 100) {
				p.sendMessage("§c▒§r 소울이 부족합니다!");
				return;
			}
			
			data.takeSoul(100);
			
			plugin.util.giveItem(p, new ItemStack(Material.ARROW, 32));
			
			p.closeInventory();
			openGUI(p);
			
			Scoreboarder.updateObjectives(p);
			
			p.playSound(p.getLocation(), Sound.PORTAL_TRIGGER, 1.0F, 1.5F);
			p.sendMessage("§a▒§r 교환이 성사되었습니다.");
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public ItemStack scroll(int floor) {
		ItemStack item = new ItemStack(Material.MAP, 1, (short) floor);
		MapMeta meta = (MapMeta) item.getItemMeta();
		
		List<String> lore = new ArrayList<String>();
		
		lore.add("§7우클릭하면 도전합니다.");
		lore.add("");
		lore.add("§8죽음을 각오하세요.");
		lore.add("§8그들은 결코 쉬운 상대가 아닙니다.");
		
		meta.setDisplayName("§c보스 스크롤§4: "+floor+"F");
		meta.setLore(lore);
		meta.setScaling(false);
		item.setItemMeta(meta);
		
		return item;
	}
}