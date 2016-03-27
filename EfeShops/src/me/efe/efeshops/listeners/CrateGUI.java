package me.efe.efeshops.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.efe.bloodskin.BloodSkin;
import me.efe.bloodskin.skins.Skin;
import me.efe.efeserver.PlayerData;
import me.efe.efeshops.EfeShops;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CrateGUI implements Listener {
	public EfeShops plugin;
	public BloodSkin bloodSkin;
	public Random random = new Random();
	public List<String> users = new ArrayList<String>();
	
	public CrateGUI(EfeShops plugin) {
		this.plugin = plugin;
		this.bloodSkin = (BloodSkin) plugin.getServer().getPluginManager().getPlugin("BloodSkin");
	}
	
	public void openGUI(Player player, boolean isPremium, boolean isPopular) {
		Inventory inv = plugin.getServer().createInventory(player, 9*3, "§5▒§r 블러드 스킨 상자 개봉");
		
		inv.setItem(4, plugin.util.createDisplayItem("§r↓", plugin.util.enchant(new ItemStack(Material.TRIPWIRE_HOOK), Enchantment.SILK_TOUCH, 100), new String[]{}));
		
		users.add(player.getName());
		player.openInventory(inv);
		
		PlayerData data = PlayerData.get(player);
		List<Skin> skins = new ArrayList<Skin>();
		
		for (Skin skin : bloodSkin.skins.values()) {
			if (!skin.isGettable() || data.hasBloodSkin(skin.getName()))
				continue;
			
			if (isPremium && !skin.isPremium())
				continue;
			
			if (isPopular && skin.isPremium())
				continue;
			
			skins.add(skin);
		}
		
		if (skins.isEmpty()) {
			player.closeInventory();
			return;
		}
		
		List<Skin> list = new ArrayList<Skin>();
		int size = 100 + random.nextInt(20);
		
		while (list.size() < size) {
			Skin skin = skins.get(random.nextInt(skins.size()));
			
			if (skin.isPremium() && !isPremium && Math.random() <= 0.5)
				continue;
			
			list.add(skin);
		}
		
		data.addBloodSkin(list.get(size - 5).getName());
		
		playAnimation(player, inv, list, 0, 5, true);
	}
	
	public void playAnimation(final Player player, final Inventory inv, final List<Skin> skins, final int frame, long delay, final boolean soundValue) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (player == null|| !users.contains(player.getName())) return;
				
				if (soundValue)
					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				else
					player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.5F);
				
				for (int i = 0; i < 9; i ++) {
					Skin skin = skins.get(frame + i);
					
					inv.setItem(9 + i, skin.getIcon().clone());
					
					ItemStack pane = plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) (skin.isPremium() ? 3 : 0)), new String[]{});
					inv.setItem(i, pane.clone());
					inv.setItem(18 + i, pane.clone());
				}
				
				inv.setItem(4, plugin.util.createDisplayItem("§r↓", plugin.util.enchant(new ItemStack(Material.TRIPWIRE_HOOK),
						Enchantment.SILK_TOUCH, 100), new String[]{}));
				
				int lastFrame = skins.size() - 9;
				long nextDelay;
				double percent = (double) frame / lastFrame;
				
				if (percent >= 0.975) {
					nextDelay = 20;
				} else if (percent >= 0.95) {
					nextDelay = 10;
				} else if (percent >= 0.85) {
					nextDelay = 5;
				} else if (percent >= 0.7) {
					nextDelay = 3;
				} else {
					nextDelay = 1;
				}
				
				if (frame != lastFrame) {
					playAnimation(player, inv, skins, frame + 1, nextDelay, !soundValue);
					return;
				}
				
				Skin skin = skins.get(skins.size() - 5);
				
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 0.25F);
				player.sendMessage("§a▒§r "+(skin.isPremium() ? "§b" : "§r")+"<"+skin.getIcon().getItemMeta().getDisplayName().substring(2)+">§r 블러드 스킨을 획득했습니다!");
				
				playClearAnimation(player, inv, 0);
			}
		}, delay);
	}
	
	public void playClearAnimation(final Player player, final Inventory inv, final int frame) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (player == null || !users.contains(player.getName())) return;
				
				if (frame == 4) {
					for (int i = 0; i < 3; i ++) {
						inv.setItem(5 + i * 9, new ItemStack(Material.AIR));
					}
					
					playClearAnimation(player, inv, frame + 2);
				} else {
					for (int i = 0; i < 3; i ++) {
						inv.setItem(frame + i * 9, new ItemStack(Material.AIR));
					}
					
					if (frame < 8) {
						playClearAnimation(player, inv, frame + 1);
					}
				}
			}
		}, 5L);
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