package me.efe.efetutorial.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;
import me.efe.efequest.quest.QuestLoader;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efetutorial.EfeTutorial;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;

public class SelectListener implements Listener {
	public EfeTutorial plugin;
	public Set<UUID> users = new HashSet<UUID>();
	
	public SelectListener(EfeTutorial plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p, ItemStack item) {
		Inventory inv = plugin.getServer().createInventory(p, 9 * 3, "§9▒§r 섬 선택");
		
		for (int i : new int[]{0, 1, 2, 9, 10, 11, 18, 19, 20})
			inv.setItem(i, EfeUtils.item.createDisplayItem("§a선택", new ItemStack(Material.EMERALD_BLOCK), null));
		for (int i : new int[]{6, 7, 8, 15, 16, 17, 24, 25, 26})
			inv.setItem(i, EfeUtils.item.createDisplayItem("§c취소", new ItemStack(Material.REDSTONE_BLOCK), null));
		
		inv.setItem(13, item.clone());
		
		users.add(p.getUniqueId());
		p.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getUniqueId())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 27) return;
		
		Player p = (Player) e.getWhoClicked();
		PlayerData data = PlayerData.get(p);
		
		if (data.hasIsland())
			return;
		
		if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
			p.closeInventory();
			return;
		}
		
		if (e.getCurrentItem().getType() != Material.EMERALD_BLOCK)
			return;
		
		String name = e.getInventory().getItem(13).getItemMeta().getDisplayName();
		
		p.closeInventory();
		
		if (name.equals("§c§lType A")) {
			EfeServer.getInstance().efeIsland.builder.build(p, "type_a");
			
			takeItems(p, false);
			
			QuestLoader.achieveGoal(p, "ISLAND");
			
			p.sendMessage("§a▒§r 당신의 섬이 생성되었습니다!");
			p.sendMessage("§a▒§r 모델: §cType A");
			p.sendMessage("§a▒§r 섬이 생성되었으니 NPC를 통해 퀘스트를 완료해주세요.");
			
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 0.5F);
			
			Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
			FireworkMeta meta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder().with(Type.BALL).withColor(Color.AQUA).build();
			
			meta.addEffect(effect);
			meta.setPower(1);
			firework.setFireworkMeta(meta);
			
		} else if (name.equals("§b§lType B")) {
			EfeServer.getInstance().efeIsland.builder.build(p, "type_b");
			
			takeItems(p, false);
			
			QuestLoader.achieveGoal(p, "ISLAND");
			
			p.sendMessage("§a▒§r 당신의 섬이 생성되었습니다!");
			p.sendMessage("§a▒§r 모델: §bType B");
			p.sendMessage("§a▒§r 섬이 생성되었으니 NPC를 통해 퀘스트를 완료해주세요.");
			
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 0.5F);
			
			Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
			FireworkMeta meta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder().with(Type.BALL).withColor(Color.AQUA).build();
			
			meta.addEffect(effect);
			meta.setPower(1);
			firework.setFireworkMeta(meta);
			
		} else if (name.equals("§a§lType C")) {
			EfeServer.getInstance().efeIsland.builder.build(p, "type_c");
			
			takeItems(p, false);
			
			QuestLoader.achieveGoal(p, "ISLAND");
			
			p.sendMessage("§a▒§r 당신의 섬이 생성되었습니다!");
			p.sendMessage("§a▒§r 모델: §aType C");
			p.sendMessage("§a▒§r 섬이 생성되었으니 NPC를 통해 퀘스트를 완료해주세요.");
			
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 0.5F);
			
			Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
			FireworkMeta meta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder().with(Type.BALL).withColor(Color.AQUA).build();
			
			meta.addEffect(effect);
			meta.setPower(1);
			firework.setFireworkMeta(meta);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getUniqueId())) {
			users.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && e.getItem().getType().equals(Material.MAP)) {
			if (!e.getItem().hasItemMeta()) return;
			if (!e.getItem().getItemMeta().hasDisplayName()) return;
			if (!e.getItem().getItemMeta().getDisplayName().contains("Type ")) return;
			
			openGUI(e.getPlayer(), e.getItem());
		}
	}
	
	public void takeItems(final Player player, final boolean onlyBook) {
		ItemStack[] contents = player.getInventory().getContents().clone();
		
		for (int i = 0; i < contents.length; i ++) {
			ItemStack item = contents[i];
			
			if (item == null || (item.getType() != Material.MAP && item.getType() != Material.WRITTEN_BOOK)) continue;
			if (onlyBook)
				if (!isGuideBook(item)) continue;
			else
				if (!isGuideBook(item)) continue;
			
			item.setType(Material.AIR);
		}
		
		player.getInventory().setContents(contents);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				EfeUtils.player.updateInv(player);
			}
		}, 20L);
	}
	
	public boolean isGuideBook(ItemStack item) {
		if (item.getType() != Material.WRITTEN_BOOK) return false;
		if (!item.hasItemMeta() || !(item.getItemMeta() instanceof BookMeta)) return false;
		
		BookMeta meta = (BookMeta) item.getItemMeta();
		
		if (!meta.hasTitle()) return false;
		
		return meta.getTitle().equals("Guide Book");
	}
}