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
		Inventory inv = plugin.getServer().createInventory(null, 9*5, "§5▒§r 순간이동의 눈");
		
		inv.setItem(22, getIcon(p, IslandUtils.POLARIS, "§a§l", new ItemStack(Material.BED), new String[] {
			"§2[폴라리스]",
			"바다의 중심이 되는 섬." }));
		inv.setItem(4, getIcon(p, IslandUtils.MINERAS_ALPHA, "§6§l", new ItemStack(Material.STONE_PICKAXE), new String[] { 
			"§2[미네라스 알파]",
			"첫 번째 광산 섬.",
			"다양한 광물을 채취할 수 있다.",
			"흙과 모래도 얻을 수 있다고 한다." }));
		inv.setItem(40, getIcon(p, IslandUtils.MINERAS_BETA, "§6§l", new ItemStack(Material.IRON_PICKAXE), new String[] {
			"§2[미네라스 베타]",
			"두 번째 광산 섬.",
			"다양한 광물을 채취할 수 있다.",
			"오직 광물만 채집할 수 있는 공간이다." }));
		inv.setItem(28, getIcon(p, IslandUtils.MINERAS_GAMMA, "§6§l", new ItemStack(Material.GOLD_PICKAXE), new String[] {
			"§2[미네라스 감마]",
			"세 번째 광산 섬.",
			"다양한 광물을 채취할 수 있다.",
			"희귀하게 자갈도 얻을 수 있다고 한다." }));
		inv.setItem(6, getIcon(p, IslandUtils.NIMBUS, "§2§l", new ItemStack(Material.LONG_GRASS, 1, (short) 1), new String[] {
			"§2[님버스]",
			"광활한 자연이 펼쳐져있는 섬.",
			"씨앗과 동물의 전리품을 채집할 수 있다." }));
		inv.setItem(43, getIcon(p, IslandUtils.RUDISH, "§c§l", new ItemStack(Material.STONE_SWORD), new String[] {
			"§2[루디쉬]",
			"의문으로 가득한 섬.",
			"언제 지어진지도 모르는 탑에는",
			"무서운 몬스터들이 살고있다." }));
		inv.setItem(20, getIcon(p, IslandUtils.AQU, "§b§l", new ItemStack(Material.FISHING_ROD), new String[] {
			"§2[아쿠]",
			"낚시하기에 좋은 평화의 섬.",
			"물고기를 팔 수 있는 상점이 존재한다." }));
		inv.setItem(24, getIcon(p, IslandUtils.FOREST_ISLAND, "§a§l", new ItemStack(Material.SAPLING), new String[] {
			"§2[숲의 섬]",
			"이름없는 자연의 섬.",
			"참나무, 자작나무, 짙은 참나무를", "채집할 수 있다." }));
		inv.setItem(2, getIcon(p, IslandUtils.SNOW_ISLAND, "§r§l", new ItemStack(Material.SAPLING, 1, (short) 1), new String[] {
			"§2[눈 덮인 섬]",
			"이름없는 자연의 섬.",
			"가문비 나무를 채집할 수 있다." }));
		inv.setItem(32, getIcon(p, IslandUtils.JUNGLE_ISLAND, "§2§l", new ItemStack(Material.SAPLING, 1, (short) 3), new String[] {
			"§2[정글의 섬]",
			"이름없는 자연의 섬.",
			"정글 나무를 채집할 수 있다." }));
		inv.setItem(38, getIcon(p, IslandUtils.SAVANA_ISLAND, "§c§l", new ItemStack(Material.SAPLING, 1, (short) 4), new String[] {
			"§2[사바나 섬]",
			"이름없는 자연의 섬.",
			"아카시아 나무를 채집할 수 있다." }));
		
		p.openInventory(inv);
		users.add(p.getName());
	}


	private ItemStack getIcon(Player p, String isle, String prefix, ItemStack item, String[] lore) {
		ItemStack icon = EfeUtils.item.createDisplayItem(prefix+IslandUtils.toDisplay(isle), item, lore);
		
		EfeUtils.item.addLore(icon, "");
		EfeUtils.item.addLore(icon, "§9클릭하면 이동합니다.");
		
		if (IslandUtils.getIsleName(p.getLocation()).equals(isle)) {
			icon.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
			EfeUtils.item.addLore(icon, "§9현재 방문중");
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
			player.sendMessage("§c▒§r 아이템이 부족합니다.");
			player.closeInventory();
			return;
		}
		
		if (!player.getWorld().equals(EfeServer.getInstance().world)) {
			player.sendMessage("§c▒§r 여기서는 사용할 수 없습니다!");
			player.closeInventory();
			return;
		}
		
		ItemStack used = EfeUtils.item.getUsed(player.getItemInHand().clone(), player);
		player.setItemInHand(used);
		
		player.closeInventory();
		player.setMetadata("teleporting", new FixedMetadataValue(plugin, "Teleporting"));
		
		player.sendMessage("§a▒§r 「"+e.getCurrentItem().getItemMeta().getDisplayName().substring(4)+"」 섬으로 이동합니다..");
		player.playSound(player.getLocation(), Sound.PORTAL_TRAVEL, 1.0F, 1.25F);
		
		if (!player.hasPotionEffect(PotionEffectType.BLINDNESS))
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*6, 0));
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (player != null)
					player.removeMetadata("teleporting", plugin);
				
				Location loc = IslandUtils.getIsleLoc(name);
				player.teleport(loc);
				
				player.sendMessage("§a▒§r 텔레포트에 성공했습니다.");
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