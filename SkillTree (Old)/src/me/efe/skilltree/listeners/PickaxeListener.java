package me.efe.skilltree.listeners;

import java.util.ArrayList;
import java.util.List;

import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.util.Scoreboarder;
import me.efe.skilltree.PitchUtils;
import me.efe.skilltree.SkillTree;
import me.efe.skilltree.SkillUtils;
import me.efe.skilltree.UserData;
import me.efe.skilltree.PitchUtils.PitchNote;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class PickaxeListener implements Listener {
	public SkillTree plugin;
	public List<Material> useClick = new ArrayList<Material>();
	
	public PickaxeListener(SkillTree plugin) {
		this.plugin = plugin;
		useClick.add(Material.DISPENSER);
		useClick.add(Material.NOTE_BLOCK);
		useClick.add(Material.BED_BLOCK);
		useClick.add(Material.CHEST);
		useClick.add(Material.WORKBENCH);
		useClick.add(Material.FURNACE);
		useClick.add(Material.BURNING_FURNACE);
		useClick.add(Material.WOODEN_DOOR);
		useClick.add(Material.LEVER);
		useClick.add(Material.IRON_DOOR_BLOCK);
		useClick.add(Material.STONE_BUTTON);
		useClick.add(Material.JUKEBOX);
		useClick.add(Material.CAKE_BLOCK);
		useClick.add(Material.DIODE_BLOCK_OFF);
		useClick.add(Material.DIODE_BLOCK_ON);
		useClick.add(Material.TRAP_DOOR);
		useClick.add(Material.FENCE_GATE);
		useClick.add(Material.ENCHANTMENT_TABLE);
		useClick.add(Material.BREWING_STAND);
		useClick.add(Material.ENDER_CHEST);
		useClick.add(Material.COMMAND);
		useClick.add(Material.BEACON);
		useClick.add(Material.WOOD_BUTTON);
		useClick.add(Material.ANVIL);
		useClick.add(Material.TRAPPED_CHEST);
		useClick.add(Material.REDSTONE_COMPARATOR_OFF);
		useClick.add(Material.REDSTONE_COMPARATOR_ON);
		useClick.add(Material.HOPPER);
		useClick.add(Material.DROPPER);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (e.getItem() == null) return;
		
		int rush = new UserData(e.getPlayer()).getLevel(SkillUtils.getSkill("mine.diamond-rush"));
		
		if (e.getItem().getType().name().endsWith("_PICKAXE") && rush > 0) {
			
			if (e.getClickedBlock() != null && useClick.contains(e.getClickedBlock().getType())) return;
			
			if (plugin.util.isRightClick(e)) {
				if (!e.getPlayer().hasMetadata("pickaxe_skill")) {
					e.getPlayer().setMetadata("pickaxe_skill", new FixedMetadataValue(plugin, false));
					
					delay(e.getPlayer(), false);
					
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CLICK, 1.0F, PitchUtils.getPitch(PitchNote.C4));
				} else if (e.getPlayer().getMetadata("pickaxe_skill").get(0).asBoolean()) {
					e.getPlayer().removeMetadata("pickaxe_skill", plugin);
					
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CLICK, 1.0F, PitchUtils.getPitch(PitchNote.G4));
					
					startRush(e.getPlayer());
				}
			} else if (plugin.util.isLeftClick(e)) {
				if (e.getPlayer().hasMetadata("pickaxe_skill") && !e.getPlayer().getMetadata("pickaxe_skill").get(0).asBoolean()) {
					e.getPlayer().setMetadata("pickaxe_skill", new FixedMetadataValue(plugin, true));
					
					delay(e.getPlayer(), true);
					
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CLICK, 1.0F, PitchUtils.getPitch(PitchNote.E4));
				}
			}
		} else if (e.getItem().getType() == Material.TNT && plugin.util.isRightClick(e)) {
			Location loc = e.getPlayer().getLocation();
			UserData data = new UserData(e.getPlayer());
			
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				loc = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
			}
			
			if (!IslandUtils.isMineras(e.getPlayer().getLocation())) {
				e.getPlayer().sendMessage("§c▒§r 미네라스에서만 사용할 수 있습니다.");
				return;
			}
			
			if (!e.getPlayer().getInventory().contains(Material.TNT)) {
				e.getPlayer().sendMessage("§c▒§r TNT를 소지하고 있지 않습니다.");
				return;
			}
			
			if (e.getPlayer().getFoodLevel() < 5) {
				e.getPlayer().sendMessage("§c▒§r 허기가 부족합니다!");
				return;
			}
			
			TNTPrimed tnt = e.getPlayer().getWorld().spawn(loc, TNTPrimed.class);
			int level = data.getLevel(SkillUtils.getSkill("mine.dynamite"));
			
			int food = e.getPlayer().getFoodLevel() - 5;
			if (food < 0)
				food = 0;
			e.getPlayer().setFoodLevel(food);
			
			takeItem(e.getPlayer(), new ItemStack(Material.TNT), 1);
			
			e.getPlayer().playSound(loc, Sound.ORB_PICKUP, 1.0F, 1.0F);
			Scoreboarder.message(e.getPlayer(), new String[]{"§a§l>>§a 다이너마이트", "§c허기 -5"}, 2);
			
			tnt.setYield(4.0F * (0.2F + level * 0.2F));
			tnt.setCustomName("§c§l"+e.getPlayer().getName()+"§c's TNT");
			tnt.setCustomNameVisible(true);
			
			if (e.getAction() == Action.RIGHT_CLICK_AIR) {
				tnt.setVelocity(loc.getDirection().multiply(2.0D));
			}
		}
	}
	
	public void delay(final Player p, final boolean value) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (p == null || !p.hasMetadata("pickaxe_skill")) return;
				
				if (p.getMetadata("pickaxe_skill").get(0).asBoolean() == value) {
					p.removeMetadata("pickaxe_skill", plugin);
				}
			}
		}, 10L);
	}
	
	public void startRush(final Player p) {
		UserData data = new UserData(p);
		int level = data.getLevel(SkillUtils.getSkill("mine.diamond-rush"));
		
		if (hasRush(p)) {
			p.sendMessage("§c▒§r 이미 시전중인 스킬입니다.");
			return;
		}
		
		if (p.getFoodLevel() < 10) {
			p.sendMessage("§c▒§r 허기가 부족합니다!");
			return;
		}
		
		int food = p.getFoodLevel() - 10;
		if (food < 0)
			food = 0;
		p.setFoodLevel(food);
		
		p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
		Scoreboarder.message(p, new String[]{"§a§l>>§a 다이아몬드 러쉬", "§c허기 -10"}, 2);
		
		p.setMetadata("skill-diamond-rush", new FixedMetadataValue(plugin, ""));
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (p == null || !p.isOnline()) return;
				
				p.removeMetadata("skill-diamond-rush", plugin);
				
				Scoreboarder.message(p, new String[]{"§a§l>>§a 다이아몬드 러쉬", "§c버프 해제됨"}, 2);
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.5F);
				p.sendMessage("§a▒§r §e다이아몬드 러쉬§r 시전이 종료되었습니다.");
			}
		}, 20L * (5 + level * 5));
	}
	
	public boolean hasRush(Player p) {
		return p.hasMetadata("skill-diamond-rush");
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if (hasRush(e.getPlayer())) {
			e.getPlayer().removeMetadata("skill-diamond-rush", plugin);
		}
	}
	
	public int getItemAmount(Player player, ItemStack target) {
		int amount = 0;
		
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null || !item.getType().equals(target.getType())) continue;
			
			if (item.isSimilar(target)) {
				amount += item.clone().getAmount();
			}
		}
		
		return amount;
	}
	
	public boolean hasItem(Player player, ItemStack item, int amount) {
		return getItemAmount(player, item.clone()) >= amount;
	}
	
	public void takeItem(Player player, ItemStack target, int amount) {
		ItemStack[] contents = player.getInventory().getContents().clone();
		
		for (int i = 0; i < contents.length; i ++) {
			ItemStack item = contents[i];
			
			if (item == null || !item.getType().equals(target.getType())) continue;
			if (!item.isSimilar(target)) continue;
			
			if (item.clone().getAmount() > amount) {
				item.setAmount(item.clone().getAmount() - amount);
				amount = 0;
			} else {
				amount -= item.clone().getAmount();
				item.setType(Material.AIR);
			}
			
			if (amount == 0) {
				break;
			}
		}
		
		player.getInventory().setContents(contents);
	}
}