package me.efe.efemobs.rudish;

import java.util.ArrayList;
import java.util.List;

import me.efe.efemobs.EfeMobs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiderGUI  implements Listener {
	public EfeMobs plugin;
	public List<String> users = new ArrayList<String>();
	
	public GuiderGUI(EfeMobs plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(null, 9*3, "§6▒§r 루디쉬 타워 가이드");
		
		inv.setItem(11, plugin.util.createDisplayItem("§b§n루디쉬 타워에 오신 걸 환영합니다.", plugin.util.enchant(new ItemStack(Material.SMOOTH_BRICK), Enchantment.SILK_TOUCH, 100),
				new String[]{"§9총 10층으로 구성§r되어있는", "이 탑에는 다양한 몬스터가 서식합니다.", "처음 오신 분들은 오직 §91층만", "§9접근 가능§r합니다."}));
		inv.setItem(13, plugin.util.createDisplayItem("§e§n소울을 모아보세요!", plugin.util.enchant(new ItemStack(Material.SOUL_SAND), Enchantment.SILK_TOUCH, 100),
				new String[]{"자신의 최상층에서 몬스터를 사냥하면", "일정량의 §6소울을 획득§r합니다.", "소울을 모아 다음 층을 개방할 수 있습니다.", "사망시 모든 소울을 잃으므로",
			"§6소울 보관함에 소울을 보관§r해두는", "것이 좋습니다!"}));
		inv.setItem(15, plugin.util.createDisplayItem("§c§n보스에게 도전", plugin.util.enchant(new ItemStack(Material.EMPTY_MAP), Enchantment.SILK_TOUCH, 100),
				new String[]{"소울을 많이 모아두셨다면", "탑 입구의 상인에게 §4스크롤§r을 구입하여", "보스에게 도전하세요!", "보스를 쓰러트리면 §4다음 층이 개방§r됩니다.",
			"1~5명의 파티로 도전할 수 있으며,", "하루 최대 7회 도전할 수 있습니다.", "다음 층이 개방되면 쌓아놓은 소울은", "모두 초기화됩니다."}));
		
		UserData data = new UserData(player);
		if (!data.hasPlayedBefore())
			data.setPlayedBefore();
		
		users.add(player.getName());
		player.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (users.contains(event.getWhoClicked().getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getName())) {
			users.remove(event.getPlayer().getName());
		}
	}
}