package me.efe.skilltree.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efetutorial.TutorialState;
import me.efe.efetutorial.listeners.TutorialListener;
import me.efe.skilltree.Skill;
import me.efe.skilltree.SkillManager.SkillType;
import me.efe.skilltree.SkillManager;
import me.efe.skilltree.SkillTree;
import me.efe.skilltree.UserData;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SkillTreeGUI  implements Listener {
	private SkillTree plugin;
	private Map<UUID, SkillType> users = new HashMap<UUID, SkillType>();
	
	public SkillTreeGUI(SkillTree plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9*5, "§c▒§r 스킬트리");

		updateGUI(player, inv, null, false);
		player.openInventory(inv);
	}
	
	private void updateGUI(Player player, Inventory inv, SkillType type, boolean hasAnimation) {
		UserData data = new UserData(player);
		
		inv.clear();
		
		inv.setItem(0, EfeUtils.item.createDisplayItem("§6남은 SP: §e§l" + data.getSP(), new ItemStack(Material.DOUBLE_PLANT, data.getSP()), 
				new String[]{"§6사용한 SP: §e" + data.getUsedSP()}));
		
		if (type == null) {
			for (int i = 0; i < 5; i ++) {
				inv.setItem(1 + i * 9, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.RAILS), null));
				inv.setItem(7 + i * 9, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.RAILS), null));
			}
			
			inv.setItem(12, EfeUtils.item.createDisplayItem("§c사냥", new ItemStack(Material.STONE_SWORD), null));
			inv.setItem(14, EfeUtils.item.createDisplayItem("§d채광", new ItemStack(Material.IRON_PICKAXE), null));
			inv.setItem(30, EfeUtils.item.createDisplayItem("§a농사", new ItemStack(Material.WHEAT), null));
			inv.setItem(32, EfeUtils.item.createDisplayItem("§b항해", new ItemStack(Material.BOAT), null));

			inv.setItem(8, EfeUtils.item.createDisplayItem("§9숙련도", new ItemStack(Material.BOOK), null));
			inv.setItem(36, EfeUtils.item.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), null));
		} else {
			int frameNum = 0;
			
			for (Skill skill : SkillManager.getSkillList(type)) {
				ItemStack icon = getSkillIcon(skill, data);
				
				if (hasAnimation) {
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							if (!users.containsKey(player.getUniqueId()) || users.get(player.getUniqueId()) != type)
								return;
							
							inv.setItem(skill.getSlot(), icon);
						}
					}, frameNum);
					
					frameNum ++;
				} else {
					inv.setItem(skill.getSlot(), icon);
				}
			}
			
			inv.setItem(36, EfeUtils.item.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), null));
			inv.setItem(37, EfeUtils.item.createDisplayItem("§c뒤로가기", new ItemStack(Material.WOOD_DOOR), null));
		}
		
		users.put(player.getUniqueId(), type);
	}
	
	private ItemStack getSkillIcon(Skill skill, UserData data) {
		int level = data.getLevel(skill);
		
		String displayName = "§d" + skill.getDisplayName() + "§7| Lv." + level;
		List<String> lore = new ArrayList<String>();
		
		for (String line : skill.getDescription())
			lore.add(line);
		
		lore.add("");
		
		boolean isLearnable = true;
		
		int requiredSP = skill.getRequiredSP(level);
		if (data.getUsedSP() < requiredSP) {
			lore.add("§cSP 사용량이 \"" + requiredSP + "\"만큼 요구됩니다. [" + data.getUsedSP() + "/" + requiredSP + "]");
			isLearnable = false;
		}
		
		for (String name : skill.getRequiredSkillMap().keySet()) {
			Skill requiredSkill = SkillManager.getSkill(name);
			int requiredLevel = skill.getRequiredSkillMap().get(name);
			
			if (data.getLevel(requiredSkill) < requiredLevel) {
				if (requiredLevel == requiredSkill.getMaxLevel())
					lore.add("§c\""+skill.getDisplayName()+"\" 스킬 숙련을 완수해야 합니다.");
				else
					lore.add("§c\""+skill.getDisplayName()+"\" 스킬이 Lv." + requiredLevel + " 이상이여야 합니다.");
				isLearnable = false;
			}
		}
		
		if (!isLearnable)
			lore.add("");
		
		if (level > 0) {
			String[] lines = skill.getFunction(level);
			
			for (int i = 0; i < lines.length; i ++) {
				String line = lines[i];
				
				lore.add(((i == 0) ? "§9현재 레벨: " : "") + line);
			}
		}
		
		if (level < skill.getMaxLevel()) {
			String[] lines = skill.getFunction(level + 1);
			
			for (int i = 0; i < lines.length; i ++) {
				String line = lines[i];
				
				lore.add(((i == 0) ? "§9다음 레벨: " : "") + line);
			}
		}
		
		ItemStack itemStack = EfeUtils.item.createDisplayItem(displayName, skill.getIcon(), lore.toArray(new String[lore.size()]));
		itemStack.setAmount(level);
		
		if (level >= skill.getMaxLevel())
			setGrow(itemStack);
		
		return itemStack;
	}
	
	private ItemStack setGrow(ItemStack item) {
		if (item.getType() == Material.GOLDEN_APPLE) {
			item.setDurability((short) 1);
		} else {
			item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
		}
		
		return item;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.containsKey(event.getWhoClicked().getUniqueId()))
			return;
		
		event.setCancelled(true);
		
		if (event.getRawSlot() >= 45 || event.getAction() == InventoryAction.NOTHING ||
				event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
			return;
		
		Player player = (Player) event.getWhoClicked();
		UserData data = new UserData(player);
		SkillType type = users.get(player.getUniqueId());
		
		if (event.getRawSlot() == 36) {
			player.closeInventory();
			EfeServer.getInstance().mainGui.openGUI(player);
			
			return;
		} else if (event.getRawSlot() == 37) {
			player.closeInventory();
			openGUI(player);
			
			return;
		}
		
		if (type == null) {
			switch (event.getRawSlot()) {
			case 8:
				player.closeInventory();
				plugin.getServer().dispatchCommand(player, "숙련도");
				break;
			case 12:
				updateGUI(player, event.getInventory(), SkillType.HUNT, true);
				break;
			case 14:
				updateGUI(player, event.getInventory(), SkillType.MINE, true);
				break;
			case 30:
				updateGUI(player, event.getInventory(), SkillType.FARM, true);
				break;
			case 32:
				updateGUI(player, event.getInventory(), SkillType.SAIL, true);
				break;
			}
		} else {
			Skill skill = SkillManager.getSkill(type, event.getRawSlot());
			
			if (data.getSP() < 1) {
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 0.5F);
				player.sendMessage("§c▒§r SP가 부족합니다!");
				return;
			}
			
			if (TutorialState.get(player) < TutorialState.WELCOME_TO_POLARIS) {
				if (TutorialState.get(player) == TutorialState.LEVEL_UP) {
					if (!skill.getName().equals("boat.sailing")) {
						player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
						player.sendMessage("§c▒§r 항해 스킬을 배우세요!");
						return;
					}
					
					TutorialListener.onUseSPBoat(player);
				} else if (TutorialState.get(player) == TutorialState.WATERD_FARM) {
					if (!skill.getName().equals("farm.carrot")) {
						player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
						player.sendMessage("§c▒§r 당근 스킬을 배우세요!");
						return;
					}
					
					TutorialListener.onUseSPCarrot(player);
				} else {
					player.sendMessage("§c▒§r 튜토리얼이 끝난 뒤 이용해주세요.");
					return;
				}
			}
			
			data.levelup(skill);
			
			player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.5F);
			
			updateGUI(player, event.getInventory(), type, false);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (users.containsKey(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
}
