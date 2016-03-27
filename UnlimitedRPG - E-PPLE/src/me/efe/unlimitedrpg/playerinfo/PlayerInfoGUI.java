package me.efe.unlimitedrpg.playerinfo;

import java.util.HashMap;

import me.efe.efegear.util.Token;
import me.efe.unlimitedrpg.UnlimitedRPG;
import me.efe.unlimitedrpg.stat.StatAPI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerInfoGUI implements Listener {
	public UnlimitedRPG plugin;
	public HashMap<String, String> users = new HashMap<String, String>();
	
	public PlayerInfoGUI(UnlimitedRPG plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p, Player target) {
		Inventory inv = plugin.getServer().createInventory(null, 9*2, "」5�董�r "+target.getName()+"還税 舛左");
		
		if (plugin.getConfig().getBoolean("player-info.armor")) {
			int[] i = {3, 2, 1, 0};
			
			for (int j = 0; j < 4; j ++) {
				inv.setItem(j, target.getInventory().getArmorContents()[i[j]]);
			}
		}
		
		if (plugin.getConfig().getBoolean("player-info.level")) {
			inv.setItem(9, plugin.util.createDisplayItem("」a」lLv. "+target.getLevel(), new ItemStack(Material.EXP_BOTTLE), 
					new String[]{"EXP: [」9"+(int)(target.getExp()*100)+"」7%]"}));
		}
		
		if (plugin.getConfig().getBoolean("player-info.stat")) {
			int[] stat = StatAPI.getStat(p);
			
			inv.setItem(0, plugin.util.createDisplayItem("」8」l========================", new ItemStack(Material.ENCHANTED_BOOK), new String[]{
			"              Total Stats", "」8」l========================", 
			"* 」cSTR」7        -   」l"+stat[1], 
			"* 」eACC」7        -   」l"+stat[2], 
			"* 」aDEF」7        -   」l"+stat[3], 
			"* 」bDEX」7        -   」l"+stat[4], 
			"                         害精 SP 」l["+stat[0]+"]"}));
		}
		
		PlayerInfoEvent event = new PlayerInfoEvent(p, target, inv);
		plugin.getServer().getPluginManager().callEvent(event);
		
		p.openInventory(event.getGUI());
		users.put(p.getName(), Token.getToken(target));
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
		
		Player p = (Player) e.getWhoClicked();
		Player target = Token.getPlayer(users.get(p.getName()));
		
		if (target == null) {
			p.closeInventory();
			return;
		}
		
		PlayerInfoClickEvent event = new PlayerInfoClickEvent(p, target, e.getInventory(), e);
		plugin.getServer().getPluginManager().callEvent(event);
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.containsKey(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}