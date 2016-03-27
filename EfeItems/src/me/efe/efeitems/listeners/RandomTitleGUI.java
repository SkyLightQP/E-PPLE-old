package me.efe.efeitems.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeitems.EfeItems;
import me.efe.efeitems.ItemStorage;
import me.efe.titlemaker.TitleManager;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RandomTitleGUI implements Listener {
	public EfeItems plugin;
	public Random random = new Random();
	public String[] colors = new String[]{"��2", "��3", "��5", "��6", "��9", "��a", "��b", "��c", "��d", "��e"};
	public HashMap<String, GUIData> users = new HashMap<String, GUIData>();
	public List<ItemStack> determiners = new ArrayList<ItemStack>();
	public List<ItemStack> nouns = new ArrayList<ItemStack>();
	public final int detSlot = 11;
	public final int nouSlot = 15;
	public final int titleSlot = 13;
	
	public RandomTitleGUI(EfeItems plugin) {
		this.plugin = plugin;
		
		determiners.add(EfeUtils.item.createDisplayItem("�� ����", new ItemStack(Material.EMERALD), null));
		determiners.add(EfeUtils.item.createDisplayItem("�Ǹ��� �����", new ItemStack(Material.NETHER_BRICK_STAIRS), null));
		determiners.add(EfeUtils.item.createDisplayItem("���ڹ��� ���� ������", new ItemStack(Material.LEASH), null));
		determiners.add(EfeUtils.item.createDisplayItem("������ �ִ�", new ItemStack(Material.CHAINMAIL_CHESTPLATE), null));
		determiners.add(EfeUtils.item.createDisplayItem("��׷� ����", new ItemStack(Material.GOLD_SWORD), null));
		determiners.add(EfeUtils.item.createDisplayItem("���� ü���� å������", new ItemStack(Material.POTION, 1, (short) 8197), null));
		determiners.add(EfeUtils.item.createDisplayItem("���� �����", new ItemStack(Material.IRON_SWORD), null));
		determiners.add(EfeUtils.item.createDisplayItem("�� ������", new ItemStack(Material.TNT), null));
		determiners.add(EfeUtils.item.createDisplayItem("�� �� ����", new ItemStack(Material.BUCKET), null));
		determiners.add(EfeUtils.item.createDisplayItem("gksrmfdmfahtclsms", new ItemStack(Material.BARRIER), null));
		determiners.add(EfeUtils.item.createDisplayItem("���� �ƴ�", new ItemStack(Material.LEATHER_CHESTPLATE), null));
		determiners.add(EfeUtils.item.createDisplayItem("�ƴ� ô �ϴ�", new ItemStack(Material.BOOK), null));
		determiners.add(EfeUtils.item.createDisplayItem("��ٶ�", new ItemStack(Material.STICK), null));
		determiners.add(EfeUtils.item.createDisplayItem("�ɽ���", new ItemStack(Material.PAPER), null));
		determiners.add(EfeUtils.item.createDisplayItem("����� 6�г�", new ItemStack(Material.WRITTEN_BOOK), null));
		determiners.add(EfeUtils.item.createDisplayItem("�� ������", new ItemStack(Material.INK_SACK, 1, (short) 3), null));
		determiners.add(EfeUtils.item.createDisplayItem("������Ż", new ItemStack(Material.GLASS), null));
		determiners.add(EfeUtils.item.createDisplayItem("�����ϴ�", new ItemStack(Material.IRON_SPADE), null));
		determiners.add(EfeUtils.item.createDisplayItem("�Ϳ���", new ItemStack(Material.SKULL_ITEM, 1, (short) 4), null));
		determiners.add(EfeUtils.item.createDisplayItem("�� ����", new ItemStack(Material.MUSHROOM_SOUP), null));
		determiners.add(EfeUtils.item.createDisplayItem("�̱��� �ƴ϶� �ϴó��� ��", new ItemStack(Material.YELLOW_FLOWER), null));
		
		nouns.add(EfeUtils.item.createDisplayItem("��ɲ�", new ItemStack(Material.BOW), null));
		nouns.add(EfeUtils.item.createDisplayItem("����", new ItemStack(Material.DIAMOND_PICKAXE), null));
		nouns.add(EfeUtils.item.createDisplayItem("���", new ItemStack(Material.IRON_HOE), null));
		nouns.add(EfeUtils.item.createDisplayItem("���ò�", new ItemStack(Material.FISHING_ROD), null));
		nouns.add(EfeUtils.item.createDisplayItem("���డ", new ItemStack(Material.BRICK), null));
		nouns.add(EfeUtils.item.createDisplayItem("��ü", new ItemStack(Material.ROTTEN_FLESH), null));
		nouns.add(EfeUtils.item.createDisplayItem("������", new ItemStack(Material.CHEST), null));
		nouns.add(EfeUtils.item.createDisplayItem("������", new ItemStack(Material.POTION, 1, (short) 8196), null));
		nouns.add(EfeUtils.item.createDisplayItem("����", new ItemStack(Material.LEASH), null));
		nouns.add(EfeUtils.item.createDisplayItem("��Ÿ��", new ItemStack(Material.ITEM_FRAME), null));
		nouns.add(EfeUtils.item.createDisplayItem("���ϳ�", new ItemStack(Material.LEATHER_CHESTPLATE), null));
		nouns.add(EfeUtils.item.createDisplayItem("�丮��", new ItemStack(Material.CAKE), null));
		nouns.add(EfeUtils.item.createDisplayItem("��ô��ô", new ItemStack(Material.COOKED_CHICKEN), null));
		nouns.add(EfeUtils.item.createDisplayItem("������ ������", new ItemStack(Material.SLIME_BALL), null));
		nouns.add(EfeUtils.item.createDisplayItem("�����ýý�Ʈ", new ItemStack(Material.THIN_GLASS), null));
		nouns.add(EfeUtils.item.createDisplayItem("���ǵ�ְ�", new ItemStack(Material.BOOK), null));
		nouns.add(EfeUtils.item.createDisplayItem("�ĸ�", new ItemStack(Material.IRON_BLOCK), null));
		nouns.add(EfeUtils.item.createDisplayItem("�׷�����Ʈ", new ItemStack(Material.TNT), null));
		nouns.add(EfeUtils.item.createDisplayItem("�м� ����", new ItemStack(Material.LEATHER_CHESTPLATE), null));
		nouns.add(EfeUtils.item.createDisplayItem("�ʵ��л�", new ItemStack(Material.SKULL_ITEM, 1, (short) 3), null));
		nouns.add(EfeUtils.item.createDisplayItem("��2��", new ItemStack(Material.SKULL_ITEM, (short) 1), null));
		nouns.add(EfeUtils.item.createDisplayItem("�ٺ�", new ItemStack(Material.PAPER), null));
		nouns.add(EfeUtils.item.createDisplayItem("������", new ItemStack(Material.MOB_SPAWNER), null));
	}
	
	public void openGUI(Player player) {
		Inventory inv = plugin.getServer().createInventory(player, 9*3, "��9�ơ�r ������ Īȣ");
		GUIData data = new GUIData(inv, player);
		
		refresh(inv, player, data);
		
		users.put(player.getName(), data);
		player.openInventory(inv);
	}
	
	public void refresh(Inventory inv, Player player, GUIData data) {
		for (int i : new int[]{1, 2, 3, 10, 12, 19, 20, 21}) {
			short dur = (short) ((data.determiner != null) ? 5 : 15);
			
			inv.setItem(i, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, dur), null));
		}
		
		for (int i : new int[]{5, 6, 7, 14, 16, 23, 24, 25}) {
			short dur = (short) ((data.noun != null) ? 5 : 15);
			
			inv.setItem(i, EfeUtils.item.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, dur), null));
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (!users.containsKey(event.getWhoClicked().getName())) return;
		event.setCancelled(true);
		
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) || event.getRawSlot() >= 27) return;
		
		Player player = (Player) event.getWhoClicked();
		GUIData data = users.get(player.getName());
		
		if (data.isCompleted())
			return;
		
		if (event.getRawSlot() == detSlot && data.determiner == null) {
			data.selectDeterminer();
		} else if (event.getRawSlot() == nouSlot && data.noun == null) {
			data.selectNoun();
		}
		
		refresh(event.getInventory(), player, data);
		
		if (data.isCompleted()) {
			data.cancelTasks();
			
			String itemName = ItemStorage.getName(ItemStorage.RANDOM_TITLE_BOOK);
			if (!ItemStorage.hasItem(player, itemName)) {
				player.sendMessage("��c�ơ�r �������� �������� �ʽ��ϴ�.");
				return;
			}
			
			ItemStorage.takeItem(player, itemName, 1);
			
			String title = data.getResultTitle();
			
			List<String> list = TitleManager.getTitles(player);
			list.add(title);
			TitleManager.setTitles(player, list);
			
			event.getInventory().setItem(titleSlot, EfeUtils.item.createDisplayItem(title, new ItemStack(Material.ENCHANTED_BOOK), null));
			
			player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 2.0F);
			player.sendMessage("��a�ơ�r "+title+"��r Īȣ�� ȹ���߽��ϴ�!");
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent event) {
		if (users.containsKey(event.getPlayer().getName())) {
			users.get(event.getPlayer().getName()).cancelTasks();
			users.remove(event.getPlayer().getName());
		}
	}
	
	private class GUIData {
		private String determiner = null;
		private String noun = null;
		private final Inventory inv;
		private final BukkitTask detTask;
		private final BukkitTask nouTask;
		private final BukkitTask soundTask;
		
		public GUIData(Inventory inv, Player player) {
			this.inv = inv;
			this.detTask = new AnimationTask(inv, detSlot, determiners).runTaskTimer(plugin, 2, 2);
			this.nouTask = new AnimationTask(inv, nouSlot, nouns).runTaskTimer(plugin, 2, 2);
			this.soundTask = new SoundTask(player).runTaskTimer(plugin, 2, 2);
		}
		
		public void selectDeterminer() {
			this.detTask.cancel();
			
			ItemStack item = inv.getItem(detSlot);
			ItemMeta meta = item.getItemMeta();
			meta.setLore(new ArrayList<String>());
			
			inv.setItem(detSlot, item);
			
			this.determiner = meta.getDisplayName().substring(2);
			
			if (isCompleted())
				soundTask.cancel();
		}
		
		public void selectNoun() {
			this.nouTask.cancel();
			
			ItemStack item = inv.getItem(nouSlot);
			ItemMeta meta = item.getItemMeta();
			meta.setLore(new ArrayList<String>());
			
			inv.setItem(nouSlot, item);
			
			this.noun = meta.getDisplayName().substring(2);
			
			if (isCompleted())
				soundTask.cancel();
		}
		
		public boolean isCompleted() {
			return this.determiner != null && this.noun != null;
		}
		
		public String getResultTitle() {
			String color = colors[random.nextInt(colors.length)];
			
			return color + "<" + this.determiner + " " + this.noun + ">";
		}
		
		public void cancelTasks() {
			this.detTask.cancel();
			this.nouTask.cancel();
			this.soundTask.cancel();
		}
	}
	
	private class AnimationTask extends BukkitRunnable {
		private final Inventory inv;
		private final int slot;
		private final List<ItemStack> icons;
		
		public AnimationTask(Inventory inv, int slot, List<ItemStack> icons) {
			this.inv = inv;
			this.slot = slot;
			this.icons = icons;
		}
		
		@Override
		public void run() {
			String color = colors[random.nextInt(colors.length)];
			
			ItemStack icon = icons.get(random.nextInt(icons.size())).clone();
			ItemMeta meta = icon.getItemMeta();
			
			meta.setDisplayName(color + meta.getDisplayName());
			
			List<String> lore = new ArrayList<String>();
			lore.add("��7Ŭ������ ���߼���!");
			meta.setLore(lore);
			
			icon.setItemMeta(meta);
			
			inv.setItem(slot, icon);
		}
	}
	
	private class SoundTask extends BukkitRunnable {
		private final Player player;
		
		public SoundTask(Player player) {
			this.player = player;
		}
		
		@Override
		public void run() {
			if (player == null || !users.containsKey(player.getName())) {
				cancel();
				return;
			}
			
			player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Tone.C));
		}
	}
}