package me.efe.efeitems.listeners;

import java.util.ArrayList;
import java.util.List;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeisland.IslandUtils;
import me.efe.efeitems.EfeItems;
import me.efe.efeitems.ItemStorage;
import me.efe.efeserver.EfeServer;

import org.bukkit.Location;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TeleportGUI implements Listener {
	public EfeItems plugin;
	public List<String> users = new ArrayList<String>();
	
	public TeleportGUI(EfeItems plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*5, "��5�ơ�r �����̵��� ��");
		
		inv.setItem(22, getIcon(p, IslandUtils.POLARIS, "��a��l", new ItemStack(Material.BED), new String[] {
			"��2[���󸮽�]",
			"�ٴ��� �߽��� �Ǵ� ��." }));
		inv.setItem(4, getIcon(p, IslandUtils.MINERAS_ALPHA, "��6��l", new ItemStack(Material.STONE_PICKAXE), new String[] { 
			"��2[�̳׶� ����]",
			"ù ��° ���� ��.",
			"�پ��� ������ ä���� �� �ִ�.",
			"��� �𷡵� ���� �� �ִٰ� �Ѵ�." }));
		inv.setItem(40, getIcon(p, IslandUtils.MINERAS_BETA, "��6��l", new ItemStack(Material.IRON_PICKAXE), new String[] {
			"��2[�̳׶� ��Ÿ]",
			"�� ��° ���� ��.",
			"�پ��� ������ ä���� �� �ִ�.",
			"���� ������ ä���� �� �ִ� �����̴�." }));
		inv.setItem(28, getIcon(p, IslandUtils.MINERAS_GAMMA, "��6��l", new ItemStack(Material.GOLD_PICKAXE), new String[] {
			"��2[�̳׶� ����]",
			"�� ��° ���� ��.",
			"�پ��� ������ ä���� �� �ִ�.",
			"����ϰ� �ڰ��� ���� �� �ִٰ� �Ѵ�." }));
		inv.setItem(6, getIcon(p, IslandUtils.NIMBUS, "��2��l", new ItemStack(Material.LONG_GRASS, 1, (short) 1), new String[] {
			"��2[�Թ���]",
			"��Ȱ�� �ڿ��� �������ִ� ��.",
			"���Ѱ� ������ ����ǰ�� ä���� �� �ִ�." }));
		inv.setItem(43, getIcon(p, IslandUtils.RUDISH, "��c��l", new ItemStack(Material.STONE_SWORD), new String[] {
			"��2[���]",
			"�ǹ����� ������ ��.",
			"���� ���������� �𸣴� ž����",
			"������ ���͵��� ����ִ�." }));
		inv.setItem(20, getIcon(p, IslandUtils.AQU, "��b��l", new ItemStack(Material.FISHING_ROD), new String[] {
			"��2[����]",
			"�����ϱ⿡ ���� ��ȭ�� ��.",
			"����⸦ �� �� �ִ� ������ �����Ѵ�." }));
		inv.setItem(24, getIcon(p, IslandUtils.FOREST_ISLAND, "��a��l", new ItemStack(Material.SAPLING), new String[] {
			"��2[���� ��]",
			"�̸����� �ڿ��� ��.",
			"������, ���۳���, £�� ��������", "ä���� �� �ִ�." }));
		inv.setItem(2, getIcon(p, IslandUtils.SNOW_ISLAND, "��r��l", new ItemStack(Material.SAPLING, 1, (short) 1), new String[] {
			"��2[�� ���� ��]",
			"�̸����� �ڿ��� ��.",
			"������ ������ ä���� �� �ִ�." }));
		inv.setItem(32, getIcon(p, IslandUtils.JUNGLE_ISLAND, "��2��l", new ItemStack(Material.SAPLING, 1, (short) 3), new String[] {
			"��2[������ ��]",
			"�̸����� �ڿ��� ��.",
			"���� ������ ä���� �� �ִ�." }));
		inv.setItem(38, getIcon(p, IslandUtils.SAVANA_ISLAND, "��c��l", new ItemStack(Material.SAPLING, 1, (short) 4), new String[] {
			"��2[��ٳ� ��]",
			"�̸����� �ڿ��� ��.",
			"��ī�þ� ������ ä���� �� �ִ�." }));
		
		p.openInventory(inv);
		users.add(p.getName());
	}


	private ItemStack getIcon(Player p, String isle, String prefix, ItemStack item, String[] lore) {
		ItemStack icon = EfeUtils.item.createDisplayItem(prefix+IslandUtils.toDisplay(isle), item, lore);
		
		EfeUtils.item.addLore(icon, "");
		EfeUtils.item.addLore(icon, "��9Ŭ���ϸ� �̵��մϴ�.");
		
		if (IslandUtils.getIsleName(p.getLocation()).equals(isle)) {
			icon.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
			EfeUtils.item.addLore(icon, "��9���� �湮��");
		}

		return icon;
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 45) return;
		
		final Player player = (Player) e.getWhoClicked();
		final String name = IslandUtils.fromDisplay(e.getCurrentItem().getItemMeta().getDisplayName().substring(4));
		
		if (!EfeUtils.player.hasItem(player, ItemStorage.EYE_OF_TELEPORTATION.clone(), 1)) {
			player.sendMessage("��c�ơ�r �������� �����մϴ�.");
			player.closeInventory();
			return;
		}
		
		if (!player.getWorld().equals(EfeServer.getInstance().world)) {
			player.sendMessage("��c�ơ�r ���⼭�� ����� �� �����ϴ�!");
			player.closeInventory();
			return;
		}
		
		ItemStack used = EfeUtils.item.getUsed(player.getItemInHand().clone(), player);
		player.setItemInHand(used);
		
		player.closeInventory();
		player.setMetadata("teleporting", new FixedMetadataValue(plugin, "Teleporting"));
		
		player.sendMessage("��a�ơ�r ��"+e.getCurrentItem().getItemMeta().getDisplayName().substring(4)+"�� ������ �̵��մϴ�..");
		player.playSound(player.getLocation(), Sound.PORTAL_TRAVEL, 1.0F, 1.25F);
		
		if (!player.hasPotionEffect(PotionEffectType.BLINDNESS))
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*6, 0));
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (player != null)
					player.removeMetadata("teleporting", plugin);
				
				Location loc = IslandUtils.getIsleLoc(name);
				player.teleport(loc);
				
				player.sendMessage("��a�ơ�r �ڷ���Ʈ�� �����߽��ϴ�.");
				player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}
		}, 80L);
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.contains(event.getPlayer().getName())) {
			users.remove(event.getPlayer().getName());
		}
	}
}