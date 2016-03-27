package me.efe.eferudish;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;
import me.efe.efemobs.rudish.UserData;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AlchemyGUI implements Listener {
	private EfeRudish plugin;
	private Set<UUID> users = new HashSet<UUID>();
	private Location tableLocation;
	
	public AlchemyGUI(EfeRudish plugin) {
		this.plugin = plugin;
		this.tableLocation = new Location(plugin.getServer().getWorld("world"), 1299, 199, 1357);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getLocation().equals(tableLocation)) {
			if (new UserData(event.getPlayer()).getMaxFloor() < 8)
				return;
			
			openGUI(event.getPlayer());
		}
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9 * 5, "§4▒§r 연금술");
		
		refreshGUI(player, inv, 0);
		
		player.openInventory(inv);
		users.add(player.getUniqueId());
	}
	
	public void refreshGUI(Player player, Inventory inv, int slot) {
		inv.clear();
		
		inv.setItem(8, EfeUtils.item.createDisplayItem("§d인첸트된 책은 4층 이상의", new ItemStack(Material.ENCHANTED_BOOK),
				new String[]{"§d보스 도전을 통해 획득할 수 있습니다."}));
		
		for (int i = 0; i < plugin.getAlchemyRecipes().size(); i ++) {
			AlchemyRecipe recipe = plugin.getAlchemyRecipes().get(i);
			ItemStack result = recipe.getDisplayResult();
			
			EfeUtils.item.addLore(result, "", "§9클릭하면 제조합니다.");
			
			inv.setItem(11 + 9 * i, result);
		}
		
		if (slot != 0) {
			AlchemyRecipe recipe = plugin.getAlchemyRecipes().get((slot - 11) / 9);
			
			for (int i = 0; i < recipe.getIngredients().size(); i ++) {
				final int j = i;
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						inv.setItem(slot + 2 + j, recipe.getIngredients().get(j));
					}
				}, i + 1);
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getUniqueId()))
			return;
		
		event.setCancelled(true);
		
		if (event.getRawSlot() >= 45 || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
			return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (event.getRawSlot() == 11 || event.getRawSlot() == 20 || event.getRawSlot() == 29) {
			ItemStack firstIngredient = event.getInventory().getItem(event.getRawSlot() + 2);
			
			if (firstIngredient == null || firstIngredient.getType() == Material.AIR) {
				refreshGUI(player, event.getInventory(), event.getRawSlot());
				return;
			}
			
			AlchemyRecipe recipe = plugin.getAlchemyRecipes().get((event.getRawSlot() - 11) / 9);
			
			for (ItemStack ingredient : recipe.getIngredients()) {
				if (!EfeUtils.player.hasItem(player, ingredient, ingredient.clone().getAmount())) {
					player.sendMessage("§c▒§r 재료 아이템이 부족합니다.");
					return;
				}
			}
			
			for (ItemStack ingredient : recipe.getIngredients()) {
				EfeUtils.player.takeItem(player, ingredient, ingredient.clone().getAmount());
			}
			
			if (player.getInventory().firstEmpty() == -1)
				player.getWorld().dropItem(player.getLocation(), recipe.getResult());
			else
				player.getInventory().addItem(recipe.getResult());
			
			player.playSound(player.getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);
			player.sendMessage("§a▒§r 아이템 연금에 성공했습니다.");
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
}
