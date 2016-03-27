package me.efe.efegear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Title: EfeUtil.class
 * Description: Be used to develop more useful plugins.
 * Copyright: Copyright(c)2014 By Efe All Right Reserved.
 * 
 * @version 2.3
 * @author Efe (cwjh1002@naver.com)
 */

public class EfeUtil {
	public Logger log = Logger.getLogger("Minecraft");
	public Random random = new Random();
	private Plugin plugin;
	private PluginDescriptionFile pdf;
	private static List<User> users = new ArrayList<User>();
	private static double version = 2.3;
	private static double versionGear;
	
	public EfeUtil(Plugin plugin) {
		this.plugin = plugin;
		this.pdf = plugin.getDescription();
	}
	
	public static double getVersion() {
		return version;
	}
	
	public static double getGearVersion() {
		return versionGear;
	}
	
	public String getPluginName() {
		return this.pdf.getName();
	}
	
	public String getPluginFullName() {
		return this.pdf.getFullName();
	}
	
	public String getPluginVersion() {
		return this.pdf.getVersion();
	}
	
	
	/* Server */
	
	public void logDisable() {
		log.info("["+pdf.getFullName()+"] 神覗! Made by Efe");
	}
	
	public void logEnable() {
		log.info("["+pdf.getFullName()+"] 紳! Made by Efe");
	}
	
	public void register(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}
	
	public void broadcast(String message) {
		Bukkit.broadcastMessage(message);
	}
	
	public void console(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}
	
	
	/* Math */
	
	public boolean rand(double percent) {
		if (Math.random() <= percent) return true;
		return false;
	}
	
	public boolean rand(int denom) {
		if (random.nextInt(denom) == 0) return true;
		return false;
	}
	
	public int rand(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}
	
	public boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
	/* Command */
	
	public boolean isPlayer(CommandSender s) {
		if (s instanceof Player) return true;
		return false;
	}
	
	@Deprecated
	public boolean error(CommandSender s, String message) {
		s.sendMessage(message);
		return false;
	}
	
	public String getFinalArg(String[] args, int start) {
		StringBuilder bldr = new StringBuilder();
		for (int i = start; i < args.length; i++) {
	    	if (i != start) bldr.append(" ");
	    	bldr.append(args[i]);
		}
		return bldr.toString();
	}
	
	
	/* Player */

	public Player getOnlinePlayer(String str) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (all.getName().equalsIgnoreCase(str)) return all;
		}
		return null;
	}
	
	public OfflinePlayer getOfflinePlayer(String str) {
		for (OfflinePlayer all : Bukkit.getOfflinePlayers()) {
			if (all.getName().equalsIgnoreCase(str)) return all;
		}
		return null;
	}
	
	public List<Player> getOnlinePlayers() {
		List<Player> list = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers()) list.add(p);
		return list;
	}
	
	public void playSoundAll(Location l, Sound sound, float pitch) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.playSound(l, sound, 1.0F, pitch);
		}
	}
	
	public void playSoundAll(Player p, Sound sound, float pitch) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.playSound(p.getLocation(), sound, 1.0F, pitch);
		}
	}
	
	public void giveEffect(Player p, PotionEffectType type, int second, int level) {
		p.addPotionEffect(new PotionEffect(type, second*20, level-1));
	}
	
	public void giveItem(Player p, ItemStack item) {
		if (p.getInventory().firstEmpty() != -1) p.getInventory().addItem(item);
		else p.getWorld().dropItem(p.getLocation(), item);
	}
	
	public User getRandomUser() {
		if (users.size() > 0) return users.get(random.nextInt(users.size()));
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public void updateInv(Player p) {
		p.updateInventory();
	}
	
	
	/* ItemStack */
	
	@SuppressWarnings("deprecation")
	public Material getMaterial(int id) {
		return Material.getMaterial(id);
	}
	
	@Deprecated
	public ItemStack createItem(String name, ItemStack item, String[] lore) {
		ItemMeta meta = item.getItemMeta();
		List<String> metaLore = new ArrayList<String>();
		for (String str : lore) {
			metaLore.add("」7"+str);
		}
		meta.setDisplayName(name);
		meta.setLore(metaLore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack createDisplayItem(String name, ItemStack item, String[] lore) {
		ItemMeta meta = item.getItemMeta();
		List<String> metaLore = new ArrayList<String>();
		for (String str : lore) {
			metaLore.add("」7"+str.replaceAll("」r", "」7"));
		}
		meta.setDisplayName(name);
		meta.setLore(metaLore);
		meta.addItemFlags(ItemFlag.values());
		item.setItemMeta(meta);
		
		return item;
	}
	
	public ItemStack createRawItem(String name, ItemStack item, String[] lore) {
		ItemMeta meta = item.getItemMeta();
		List<String> metaLore = new ArrayList<String>();
		for (String str : lore) {
			metaLore.add(str);
		}
		meta.setDisplayName(name);
		meta.setLore(metaLore);
		item.setItemMeta(meta);
		return item;
	}
	
	public String getDisplayName(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta.hasDisplayName()) return meta.getDisplayName();
		return item.getType().name();
	}
	
	public void setDisplayName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}
	
	public void addLore(ItemStack item, String str) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if (meta != null && meta.hasLore()) lore.addAll(meta.getLore());
		lore.add(str);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public boolean containsLore(ItemStack item, String str) {
		if (!item.hasItemMeta()) return false;
		if (!item.getItemMeta().hasLore()) return false;
		if (item.getItemMeta().getLore().contains(str)) return true;
		return false;
	}
	
	public ItemStack color(ItemStack item, Color color) {
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack enchant(ItemStack item, Enchantment ench, int level) {
		item.addUnsafeEnchantment(ench, level);
		return item;
	}
	
	public boolean hasEnchant(ItemStack item, Enchantment type, int level) {
		return item.containsEnchantment(type) && item.getEnchantmentLevel(type) == level;
	}
	
	public ItemStack getUsed(ItemStack itemstack, Player p) {
		ItemStack item = itemstack.clone();
		if (p != null && p.getGameMode().equals(GameMode.CREATIVE)) return item;
		if (item.getAmount() == 1) return null;
		item.setAmount(item.getAmount() - 1);
		return item;
	}
	
	public ItemStack getDurabilityUsed(ItemStack itemstack, Player p, int max) {
		ItemStack item = itemstack.clone();
		if (p != null && p.getGameMode().equals(GameMode.CREATIVE)) return item;
		if (item.getDurability() == max) return null;
		item.setDurability((short) (item.getDurability() + 1));
		return item;
	}
	
	/* Event */
	
	public boolean isBlockClicked(PlayerInteractEvent e, boolean isLeft) {
		if (isLeft && e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return true;
		else if (!isLeft && e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return true;
		return false;
	}
	
	public boolean isSignClicked(PlayerInteractEvent e) {
		if (e.getClickedBlock() != null && e.getClickedBlock().getState() instanceof Sign) return true;
		return false;
	}
	
	public boolean isLeftClick(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return true;
		return false;
	}
	
	public boolean isRightClick(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return true;
		return false;
	}
	
	@Deprecated
	public ItemStack getUsed(PlayerInteractEvent e) {
		ItemStack item = e.getItem().clone();
		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return item;
		if (item.getAmount() == 1) return null;
		item.setAmount(item.getAmount() - 1);
		return item;
	}
	
	@Deprecated
	public ItemStack getDurabilityUsed(PlayerInteractEvent e, int max) {
		ItemStack item = e.getItem().clone();
		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return item;
		if (item.getDurability() == max) return null;
		item.setDurability((short) (item.getDurability() + 1));
		return item;
	}
	
	public LivingEntity getDamager(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof LivingEntity) return (LivingEntity) e.getDamager();
		if (e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			if (proj.getShooter() != null) return (LivingEntity) proj.getShooter();
		}
		return null;
	}
	
	public LivingEntity getKiller(EntityDeathEvent e) {
		if (!(e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) return null;
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
		Entity damager = event.getDamager();
		if (damager instanceof LivingEntity) return (LivingEntity) damager;
		if (damager instanceof Projectile) {
			Projectile proj = (Projectile) damager;
			if (proj.getShooter() != null) return (LivingEntity) proj.getShooter();
		}
		return null;
	}
	
	public boolean equalsEntity(Entity entity, EntityType type) {
		if (entity == null) return false;
		if (entity.getType().equals(type)) return true;
		return false;
	}
	
	public boolean isPlayer(Entity entity) {
		if (entity == null) return false;
		if (entity.getType().equals(EntityType.PLAYER)) return true;
		return false;
	}
	
	
	/* ETC */
	
	public double getHealth(LivingEntity entity) {
		return entity.getHealth();
	}
	
	public double getMaxHealth(LivingEntity entity) {
		return entity.getMaxHealth();
	}
	
	public void setHealth(LivingEntity entity, double health) {
		entity.setHealth(health);
	}
	
	public void setMaxHealth(LivingEntity entity, double health) {
		entity.setMaxHealth(health);
	}
	
	public void heal(LivingEntity entity) {
		entity.setHealth(entity.getMaxHealth());
	}
	
	public void damage(LivingEntity entity, double amount) {
		entity.damage(amount);
	}
	
	public String replaceColors(String str) {
		for (int i = 0; i < 10; i ++) str = str.replaceAll("&"+i, "」"+i);
		char[] c = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'l', 'm', 'n', 'o', 'r'};
		for (int i = 0; i < c.length; i ++) str = str.replaceAll("&"+c[i], "」"+c[i]);
		return str;
	}
	
	public boolean debug(CommandSender s, String name) {
		if (s == null) return Material.getMaterial("NAME_TAG") == null;
		return false;
	}
	
	public static void initalize(EfeGear gear) {
		versionGear = Double.parseDouble(gear.getDescription().getVersion().split(" for ")[0]);
		
		try {
			URL url = new URL("https://raw.githubusercontent.com/cwjh1002/Advertisement/master/README.md");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), Charset.forName("UTF-8")));
			
			String str;
			while ((str = br.readLine()) != null) {
				if (!str.contains("%adv%")) continue;
				for (String player : str.split("%adv%")[1].split("%next%")) {
					users.add(new User(player));
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("[EfeGear] Failed to get advertisements list!");
		}
	}
}