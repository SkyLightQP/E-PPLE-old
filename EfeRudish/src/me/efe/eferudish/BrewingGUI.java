package me.efe.eferudish;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;
import me.efe.skilltree.SkillManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BrewingGUI implements Listener {
	private EfeRudish plugin;
	private Set<UUID> users = new HashSet<UUID>();
	private Location standLocation;
	
	public BrewingGUI(EfeRudish plugin) {
		this.plugin = plugin;
		this.standLocation = new Location(plugin.getServer().getWorld("world"), 18, 51, -15);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() == Material.NETHER_WARTS) {
			event.setCancelled(true);
			event.getBlock().setType(Material.AIR);
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.NETHER_STALK));
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getLocation().equals(standLocation)) {
			if (SkillManager.hasLearned(event.getPlayer(), "farm.faithfulness")) {
				event.getPlayer().sendMessage("§c▒§r \"성실\" 농사 스킬을 배워야 사용할 수 있습니다.");
				return;
			}
			
			openGUI(event.getPlayer());
		}
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9 * 6, "§4▒§r 포션 제조");
		PotionRecipe recipe = plugin.getBrewingRecipeOfDay();
		
		
		inv.setItem(6, EfeUtils.item.createDisplayItem("§a지옥 사마귀 씨앗은", new ItemStack(Material.DIAMOND_SWORD),
				new String[]{"§a보스 보상으로 획득할 수 있습니다."}));
		inv.setItem(7, EfeUtils.item.createDisplayItem("§a지옥 사마귀는 밭에서만", new ItemStack(Material.NETHER_STALK),
				new String[]{"§a재배할 수 있습니다."}));
		inv.setItem(8, EfeUtils.item.createDisplayItem("§e제작할 수 있는 포션의 종류는", new ItemStack(Material.WATCH),
				new String[]{"§e아침이 밝을 때마다 바뀝니다."}));
		
		
		ItemStack result = recipe.getResult();
		EfeUtils.item.addLore(result, "", "§9클릭하면 제조합니다.");
		
		inv.setItem(22, result);
		
		
		int startSlot = 40 - recipe.getIngredients().size() / 2;
		
		for (int i = 0; i < recipe.getIngredients().size(); i ++) {
			inv.setItem(startSlot + i, recipe.getIngredients().get(i).clone());
		}
		
		
		player.openInventory(inv);
		users.add(player.getUniqueId());
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.contains(event.getWhoClicked().getUniqueId()))
			return;
		
		event.setCancelled(true);
		
		if (event.getRawSlot() >= 54 || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
			return;
		
		Player player = (Player) event.getWhoClicked();
		
		
		if (event.getRawSlot() == 22) {
			PotionRecipe recipe = plugin.getBrewingRecipeOfDay();
			
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
			
			player.playSound(player.getLocation(), Sound.MAGMACUBE_JUMP, 1.0F, 1.0F);
			player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
			player.sendMessage("§a▒§r 포션 제조에 성공했습니다.");
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
}
