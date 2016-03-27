package me.efe.skilltree.listeners;

import java.util.HashMap;

import me.efe.efetutorial.TutorialState;
import me.efe.efetutorial.listeners.TutorialListener;
import me.efe.skilltree.Skill;
import me.efe.skilltree.SkillTree;
import me.efe.skilltree.SkillUtils;
import me.efe.skilltree.SkillUtils.SkillType;
import me.efe.skilltree.UserData;
import mkremins.fanciful.FancyMessage;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import de.slikey.effectlib.util.ParticleEffect;

public class SkillTreeGUI implements Listener {
	public SkillTree plugin;
	public HashMap<String, SkillType> users = new HashMap<String, SkillType>();
	
	public SkillTreeGUI(SkillTree plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void level(PlayerLevelChangeEvent e) {
		if (e.getNewLevel() > e.getOldLevel()) {
			UserData data = new UserData(e.getPlayer());
			
			for (int i = 0; i < e.getNewLevel() - e.getOldLevel(); i ++) {
				data.giveSP(3);
			}
			
			e.getPlayer().sendMessage("��a�ơ�r ��7============================================");
			e.getPlayer().sendMessage("��a�ơ�r ");
			e.getPlayer().sendMessage("��a�ơ�r ��b��l>> LEVEL UP!!");
			e.getPlayer().sendMessage("��a�ơ�r ");
			e.getPlayer().sendMessage("��a�ơ�r ���� ����� ��aLv."+e.getNewLevel()+"��r �Դϴ�!");
			
			new FancyMessage("��a�ơ�r ")
			.then("��b��n��ųƮ����r")
				.command("/��ųƮ��")
				.tooltip("��b/��ųƮ��")
			.then("���� ��ų�� ���ų� ���׷��̵��ϼ���.")
			.send(e.getPlayer());
			
			e.getPlayer().sendMessage("��a�ơ�r ");
			e.getPlayer().sendMessage("��a�ơ�r ��e������� ���� SP��r�� ��e"+data.getSP()+"��r �ֽ��ϴ�.");
			e.getPlayer().sendMessage("��a�ơ�r ");
			e.getPlayer().sendMessage("��a�ơ�r ��7============================================");
			
			Location loc = e.getPlayer().getLocation();
			
			ParticleEffect.FIREWORKS_SPARK.display(null, loc, Color.GREEN, 32, 0.05F, 0.05F, 0.05F, 0.075F, 20);
			
			Firework firework = loc.getWorld().spawn(loc, Firework.class);
			FireworkMeta meta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder()
					.with(Type.BALL_LARGE)
					.withColor(Color.LIME, Color.GREEN)
					.withFade(Color.GREEN)
					.build();
			
			meta.addEffect(effect);
			meta.setPower(1);
			firework.setFireworkMeta(meta);
			
			e.getPlayer().playSound(loc, Sound.LEVEL_UP, 1.0F, 1.0F);
			
			for (Player all : plugin.util.getOnlinePlayers()) {
				me.efe.efecommunity.UserData communityData = new me.efe.efecommunity.UserData(all);
				
				if (communityData.getFriends().contains(e.getPlayer().getUniqueId())) {
					all.getPlayer().sendMessage("��d�ơ�r "+e.getPlayer().getName()+"���� Lv."+e.getNewLevel()+"�� �����߽��ϴ�.");
				}
			}
		}
	}
	
	public void openGUI(Player p, SkillType type) {
		Inventory inv = plugin.getServer().createInventory(null, 9*5, "��c�ơ�r ��ųƮ��");
		
		updateInv(p, type, inv, false);
		
		p.openInventory(inv);
		users.put(p.getName(), type);
	}
	
	public void updateInv(Player p, final SkillType type, final Inventory inv, boolean hasAnimation) {
		UserData data = new UserData(p);
		
		int amount = data.getSP();
		if (amount > 64) amount = 64;
		
		inv.clear();
		
		inv.setItem(0, plugin.util.createDisplayItem("��6���� SP: ��e��l"+data.getSP(), new ItemStack(Material.DOUBLE_PLANT, amount), 
				new String[]{"��6����� SP: ��e"+data.getUsedSP()}));
		
		if (type == null) {
			for (int i = 0; i < 5; i ++) inv.setItem(1 + i * 9, plugin.util.createDisplayItem(" ", new ItemStack(Material.RAILS), new String[]{}));
			for (int i = 0; i < 5; i ++) inv.setItem(7 + i * 9, plugin.util.createDisplayItem(" ", new ItemStack(Material.RAILS), new String[]{}));
			
			inv.setItem(12, plugin.util.createDisplayItem("��c���", new ItemStack(Material.STONE_SWORD), new String[]{}));
			inv.setItem(14, plugin.util.createDisplayItem("��eä��", new ItemStack(Material.IRON_PICKAXE), new String[]{}));
			inv.setItem(30, plugin.util.createDisplayItem("��b���", new ItemStack(Material.WHEAT), new String[]{}));
			inv.setItem(32, plugin.util.createDisplayItem("��d����", new ItemStack(Material.BOAT), new String[]{}));
			
			inv.setItem(36, plugin.util.createDisplayItem("��e���� �޴�", new ItemStack(Material.NETHER_STAR), new String[]{}));
			inv.setItem(8, plugin.util.createDisplayItem("��c���õ�", new ItemStack(Material.BOOK), new String[]{}));
		} else {
			int number = 1;
			for (final Skill skill : SkillUtils.skillMap.get(type)) {
				ItemStack icon = skill.getIcon(data).clone();
				
				if (data.getLevel(skill) >= skill.getMaxLv())
					icon = enchant(icon);
				
				if (hasAnimation) {
					final String name = p.getName();
					final ItemStack item = icon;
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							if (!users.containsKey(name) || users.get(name) != type)
								return;
							
							inv.setItem(skill.getSlot(), item);
						}
					}, number);
					
					number ++;
				} else {
					inv.setItem(skill.getSlot(), icon);
				}
			}
			
			inv.setItem(36, plugin.util.createDisplayItem("��e���� �޴�", new ItemStack(Material.NETHER_STAR), new String[]{}));
			inv.setItem(37, plugin.util.createDisplayItem("��c�ڷ�", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		}
		
		users.put(p.getName(), type);
	}
	
	public ItemStack enchant(ItemStack item) {
		if (item.getType().equals(Material.GOLDEN_APPLE)) {
			item.setDurability((short) 1);
			
			return item;
		}
		
		return plugin.util.enchant(item, Enchantment.SILK_TOUCH, 100);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 9*5) return;
		if (e.getAction() == InventoryAction.NOTHING) return;
		
		Player p = (Player) e.getWhoClicked();
		UserData data = new UserData(p);
		SkillType type = users.get(p.getName());
		
		if (e.getRawSlot() == 36) {
			p.closeInventory();
			plugin.epple.mainGui.openGUI(p);
			
			return;
		} else if (e.getRawSlot() == 37) {
			p.closeInventory();
			openGUI(p, null);
			
			return;
		}
		
		if (type == null) {
			switch (e.getRawSlot()) {
			case 8:
				p.closeInventory();
				plugin.getServer().dispatchCommand(p, "���õ�");
				break;
			case 12:
				updateInv(p, SkillType.HUNT, e.getInventory(), true);
				break;
			case 14:
				updateInv(p, SkillType.MINE, e.getInventory(), true);
				break;
			case 30:
				updateInv(p, SkillType.FARM, e.getInventory(), true);
				break;
			case 32:
				updateInv(p, SkillType.SAIL, e.getInventory(), true);
				break;
			}
			return;
		}
		
		if (!isSkill(e.getCurrentItem())) return;
		
		Skill skill = SkillUtils.getSkillFromDisplay(e.getCurrentItem().getItemMeta().getDisplayName().split("��7")[0].replace("��d", ""));
		
		if (data.getSP() == 0) {
			p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 0.5F);
			p.sendMessage("��c�ơ�r SP�� �����մϴ�!");
			return;
		}
		
		if (TutorialState.get(p) < TutorialState.WELCOME_TO_POLARIS) {
			if (TutorialState.get(p) == TutorialState.LEVEL_UP) {
				if (!skill.getName().equals("boat.sailing")) {
					p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
					p.sendMessage("��c�ơ�r ���� ��ų�� ��켼��!");
					return;
				}
				
				TutorialListener.onUseSPBoat(p);
			} else if (TutorialState.get(p) == TutorialState.WATERD_FARM) {
				if (!skill.getName().equals("farm.carrot")) {
					p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
					p.sendMessage("��c�ơ�r ��� ��ų�� ��켼��!");
					return;
				}
				
				TutorialListener.onUseSPCarrot(p);
			} else {
				p.sendMessage("��c�ơ�r Ʃ�丮���� ���� �� �̿����ּ���.");
				return;
			}
		}
		
		data.levelup(skill);
		
		p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.5F);
		
		updateInv(p, type, e.getInventory(), false);
	}
	
	public boolean isSkill(ItemStack item) {
		return plugin.util.containsLore(item, "��2��m====================================");
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.containsKey(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}