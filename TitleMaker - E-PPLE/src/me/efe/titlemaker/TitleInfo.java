package me.efe.titlemaker;

import java.util.List;

import me.efe.efecore.util.EfeUtils;
import me.efe.unlimitedrpg.playerinfo.PlayerInfoEvent;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class TitleInfo implements Listener {
	public TitleMaker plugin;
	
	public TitleInfo(TitleMaker plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void info(PlayerInfoEvent e) {
		int slot = plugin.getConfig().getInt("title-info.slot");
		
		List<String> list = TitleManager.getTitles(e.getTarget());
		String[] lore;
		
		if (list.isEmpty()) {
			lore = new String[]{"¾øÀ½"};
		} else {
			for (int i = 0; i < list.size(); i ++) {
				list.set(i, EfeUtils.string.replaceColors(list.get(i)));
			}
			
			lore = list.toArray(new String[list.size()]);
		}
		
		e.getGUI().setItem(slot, EfeUtils.item.createDisplayItem("¡×9¡×lÄªÈ£", new ItemStack(Material.MAP), lore));
	}
}