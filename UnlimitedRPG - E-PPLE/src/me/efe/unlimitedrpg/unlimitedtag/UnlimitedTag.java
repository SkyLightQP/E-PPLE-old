package me.efe.unlimitedrpg.unlimitedtag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import me.efe.efegear.util.Token;
import me.efe.unlimitedrpg.UnlimitedRPG;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class UnlimitedTag implements Listener {
	public UnlimitedRPG plugin;
	public List<String> users = new ArrayList<String>();
	public FileConfiguration config;
	
	public UnlimitedTag(UnlimitedRPG plugin) {
		this.plugin = plugin;
		
		addPacketListener();
	}
	
	public void addPacketListener() {
		HashSet<PacketType> packets = new HashSet<PacketType>();
		packets.add(PacketType.Play.Server.WINDOW_ITEMS);
		packets.add(PacketType.Play.Server.SET_SLOT);
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, packets) {
			
			@Override
			public void onPacketSending(PacketEvent e) {
				PacketContainer packet = e.getPacket();
				PacketType type = e.getPacketType();
				
				if (type.equals(PacketType.Play.Server.WINDOW_ITEMS)) {
					ItemStack[] items = packet.getItemArrayModifier().read(0);
					
					for (int i = 0; i < items.length; i ++) {
						items[i] = replaceTags(items[i], e.getPlayer());
					}
					
					packet.getItemArrayModifier().write(0, items);
				} else if (type.equals(PacketType.Play.Server.SET_SLOT)) {
					ItemStack item = ((ItemStack) packet.getItemModifier().read(0));
					
					packet.getItemModifier().write(0, replaceTags(item, e.getPlayer()));
				}
			}
		});
	}
	
	public ItemStack replaceTags(ItemStack i, Player p) {
		if (i == null || !i.hasItemMeta() || !i.getItemMeta().hasLore()) return i;
		
		ItemStack item = i.clone();
		ItemMeta meta = item.getItemMeta();
		
		List<String> lore = meta.getLore();
		ListIterator<String> it = lore.listIterator();
		
		while (it.hasNext()) {
			String data = it.next().split("§\\|")[0];
			String[] cont = data.replaceAll("§", "").split(":");
			
			data += "§|";
			
			switch (cont[0]) {
			case "VESTED":
				if (p.isOp()) {
					it.set(data+"§d>>§5 "+Token.getPlayerName(cont[1])+" 귀속");
				} else {
					if (Token.getPlayer(cont[1]).equals(p)) {
						it.set(data+"§d>>§5 귀속된 아이템");
					} else {
						it.set(data+"§d>>§5 당신의 아이템이 아닙니다!");
					}
				}
				break;
			case "VEST_ON_PICKUP":
				it.set(data+"§d>>§5 획득시 귀속");
				break;
			case "REQUIRE_LV":
				if (p.getLevel() >= Integer.parseInt(cont[1])) {
					it.set(data+"§a>>§2 Lv."+cont[1]+"↑");
				} else {
					it.set(data+"§c>>§4 Lv."+cont[1]+"↑");
				}
				break;
			case "LOCKED":
				it.set(data+"§d>>§5 잠겨진 아이템");
				break;
			case "STAMPED":
				it.set(data+"§d>>§5 각인된 아이템");
				break;
			case "DEADLINE":
				it.set(data+"§b>>§9 "+UnlimitedTagAPI.Deadline.getDisplay(TagType.DEADLINE, cont[1]));
				break;
			case "DEADLINE_ON_PICKUP":
				it.set(data+"§b>>§9 "+UnlimitedTagAPI.Deadline.getDisplay(TagType.DEADLINE_ON_PICKUP, cont[1]));
				break;
			case "PERMISSION":
				if (p.isOp()) {
					it.set(data+"§d>>§5 '"+cont[1]+"' 권한 필요");
				} else {
					if (p.hasPermission(cont[1])) {
						it.set(data+"§a>>§2 권한 충족");
					} else {
						it.set(data+"§c>>§4 권한 필요");
					}
				}
				break;
			case "SPATIAL":
				if (p.isOp()) {
					it.set(data+"§d>>§5 '"+cont[1]+"' 이탈시 증발");
				} else {
					it.set(data+"§d>>§5 제약된 아이템");
				}
				break;
			case "ICON":
				if (p.isOp()) {
					it.set(data+"§e>>§6 아이콘으로만 사용");
				} else {
					it.set(data+"§e>>§6 토큰 아이템");
				}
				break;
			case "NO_CRAFT":
				if (p.isOp()) {
					it.set(data+"§e>>§6 조합 재료로 사용 불가");
				} else {
					it.set(data+"§e>>§6 비재료 아이템");
				}
				break;
			}
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.isOp()) return;
		
		for (ItemStack item : p.getInventory().getContents()) {
			if (item == null) return;
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.DEADLINE)) {
				item.setType(Material.AIR);
				plugin.util.updateInv(p);
				
				p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
				p.sendMessage(plugin.main+"§a▒§r 아이템이 기간이 만료되어 사라졌습니다.");
			}
		}
		
		for (ItemStack item : p.getInventory().getArmorContents()) {
			if (item == null) return;
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.DEADLINE)) {
				item.setType(Material.AIR);
				plugin.util.updateInv(p);
				
				p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
				p.sendMessage(plugin.main+"§a▒§r 아이템이 기간이 만료되어 사라졌습니다.");
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void noInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		
		if (item == null || p.isOp()) return;
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.VESTED)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.getWorld().dropItem(p.getLocation(), item);
			p.setItemInHand(null);
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0F, 1.0F);
			p.sendMessage(plugin.main+"§c▒§r 당신의 아이템이 아닙니다!");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.REQUIRE_LV)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.5F);
			p.sendMessage(plugin.main+"§c▒§r 레벨이 부족합니다!");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.DEADLINE)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.setItemInHand(null);
			p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
			p.sendMessage(plugin.main+"§a▒§r 아이템이 기간이 만료되어 사라졌습니다.");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.PERMISSION)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.5F);
			p.sendMessage(plugin.main+"§c▒§r 이 아이템을 사용할 권한이 없습니다!");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.ICON)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			return;
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void noEntityInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getPlayer().getItemInHand();
		
		if (item == null || p.isOp()) return;
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.VESTED)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.getWorld().dropItem(p.getLocation(), item);
			p.setItemInHand(null);
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0F, 1.0F);
			p.sendMessage(plugin.main+"§c▒§r 당신의 아이템이 아닙니다!");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.ICON)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			return;
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void noAttack(EntityDamageByEntityEvent e) {
		if (!plugin.util.isPlayer(plugin.util.getDamager(e))) return;
		
		Player p = (Player) plugin.util.getDamager(e);
		ItemStack item = p.getItemInHand();
		
		if (item == null || p.isOp()) return;
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.VESTED)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.getWorld().dropItem(p.getLocation(), item);
			p.setItemInHand(null);
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0F, 1.0F);
			p.sendMessage(plugin.main+"§c▒§r 당신의 아이템이 아닙니다!");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.REQUIRE_LV)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.5F);
			p.sendMessage(plugin.main+"§c▒§r 레벨이 부족합니다!");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.PERMISSION)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.5F);
			p.sendMessage(plugin.main+"§c▒§r 이 아이템을 사용할 권한이 없습니다!");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.ICON)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			return;
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void noBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		
		if (item == null || p.isOp()) return;
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.REQUIRE_LV)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.5F);
			p.sendMessage(plugin.main+"§c▒§r 레벨이 부족합니다!");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.PERMISSION)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			
			p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.5F);
			p.sendMessage(plugin.main+"§c▒§r 이 아이템을 사용할 권한이 없습니다!");
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.ICON)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			return;
		}
	}
	
	@EventHandler
	public void noPrepareCraft(PrepareItemCraftEvent e) {
		for (ItemStack item : e.getInventory().getMatrix()) {
			if (item == null) continue;
			if (UnlimitedTagAPI.hasTag(item, TagType.LOCKED) ||
					UnlimitedTagAPI.hasTag(item, TagType.STAMPED) ||
					UnlimitedTagAPI.hasTag(item, TagType.DEADLINE) ||
					UnlimitedTagAPI.hasTag(item, TagType.SPATIAL) ||
					UnlimitedTagAPI.hasTag(item, TagType.ICON) ||
					UnlimitedTagAPI.hasTag(item, TagType.NO_CRAFT)) {
				e.getInventory().setResult(null);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void noCraft(CraftItemEvent e) {
		String owner = null;
		int requireLv = -1;
		List<String> permissions = new ArrayList<String>();
		
		for (ItemStack item : e.getInventory().getMatrix()) {
			if (item == null) continue;
			
			if (UnlimitedTagAPI.hasTag(item, TagType.VESTED)) owner = UnlimitedTagAPI.getData(item, TagType.VESTED);
			if (UnlimitedTagAPI.hasTag(item, TagType.REQUIRE_LV)) {
				int lv = Integer.parseInt(UnlimitedTagAPI.getData(item, TagType.REQUIRE_LV));
				if (lv > requireLv) requireLv = lv;
			}
			if (UnlimitedTagAPI.hasTag(item, TagType.LOCKED) ||
					UnlimitedTagAPI.hasTag(item, TagType.STAMPED) ||
					UnlimitedTagAPI.hasTag(item, TagType.DEADLINE) ||
					UnlimitedTagAPI.hasTag(item, TagType.SPATIAL) ||
					UnlimitedTagAPI.hasTag(item, TagType.ICON) ||
					UnlimitedTagAPI.hasTag(item, TagType.NO_CRAFT)) {
				e.setCancelled(true);
				return;
			}
			if (UnlimitedTagAPI.hasTag(item, TagType.PERMISSION)) permissions.add(UnlimitedTagAPI.getData(item, TagType.PERMISSION));
		}
		
		ItemStack item = e.getInventory().getResult().clone();
		if (owner != null) UnlimitedTagAPI.addTag(item, TagType.VESTED, owner);
		if (requireLv != -1) UnlimitedTagAPI.addTag(item, TagType.REQUIRE_LV, requireLv+"");
		if (!permissions.isEmpty()) for (String permission : permissions) UnlimitedTagAPI.addTag(item, TagType.PERMISSION, permission);
		
		e.getInventory().setResult(item);
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void noPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem().getItemStack();
		
		if (item == null || p.isOp()) return;
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.VESTED)) {
			e.setCancelled(true);
			return;
		}
		
		if (!UnlimitedTagAPI.isAvailable(p, item, TagType.DEADLINE)) {
			e.setCancelled(true);
			plugin.util.updateInv(p);
			e.getItem().remove();
			
			p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
			p.sendMessage(plugin.main+"§a▒§r 아이템이 기간이 만료되어 사라졌습니다.");
			return;
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void noDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItemDrop().getItemStack();
		
		if (item == null || p.isOp()) return;
		
		if (UnlimitedTagAPI.hasTag(item, TagType.STAMPED)) {
			e.setCancelled(true);
			
			p.playSound(p.getLocation(), Sound.ZOMBIE_METAL, 1.0F, 1.0F);
			p.sendMessage(plugin.main+"§c▒§r 버릴 수 없는 아이템입니다!");
			return;
		}
		
		if (UnlimitedTagAPI.hasTag(item, TagType.LOCKED)) {
			e.setCancelled(true);
			
			Inventory inv = plugin.getServer().createInventory(null, 9, "아이템을 삭제하겠습니까?");
			inv.setItem(0, plugin.util.createDisplayItem("§c§l확인", new ItemStack(Material.LAVA_BUCKET), 
					new String[]{"삭제된 아이템은 다시 복구할 수 없습니다.", "또한, 한 스택이 한꺼번에 삭제됩니다.", "동의한다면 클릭해주세요."}));
			inv.setItem(4, item.clone());
			inv.setItem(8, plugin.util.createDisplayItem("§a§l취소", new ItemStack(Material.WOOD_DOOR), new String[]{"취소하려면 §n클릭§r해주세요."}));
			
			p.openInventory(inv);
			users.add(p.getName());
			
			p.sendMessage(plugin.main+"§a▒§r 정말 아이템을 삭제하겠습니까?");
			return;
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		Player p = (Player) e.getWhoClicked();
		
		if (e.getRawSlot() == 0) {
			p.setItemInHand(null);
			p.sendMessage(plugin.main+"§a▒§r 아이템이 삭제되었습니다.");
			p.playSound(p.getLocation(), Sound.WITHER_SHOOT, 1.0F, 2.0F);
			p.closeInventory();
		} else if (e.getRawSlot() == 8) {
			p.closeInventory();
		}
	}
	
	@EventHandler
	public void noDeathDrop(PlayerDeathEvent e) {
		if (e.getDrops() == null) return;
		
		List<ItemStack> hold = new ArrayList<ItemStack>();
		for (ItemStack item : new ArrayList<ItemStack>(e.getDrops())) {
			if (UnlimitedTagAPI.hasTag(item, TagType.STAMPED)) {
				hold.add(item);
				e.getDrops().remove(item);
				continue;
			}
			
			if (UnlimitedTagAPI.hasTag(item, TagType.LOCKED)) {
				e.getDrops().remove(item);
			}
		}
		
		if (!hold.isEmpty()) {
			setHold(e.getEntity(), hold);
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		if (hasHold(e.getPlayer())) {
			for (ItemStack item : getHold(e.getPlayer())) {
				e.getPlayer().getInventory().addItem(item);
			}
		}
	}
	
	public boolean hasHold(Player p) {
		File file = new File(plugin.getDataFolder(), "deathDropSave.yml");
		if (!file.exists()) return false;
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config.contains(Token.getToken(p));
	}
	
	public List<ItemStack> getHold(Player p) {
		File file = new File(plugin.getDataFolder(), "deathDropSave.yml");
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		if (!file.exists()) return list;
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		Object obj = config.get(Token.getToken(p));
		if (obj instanceof List<?>) {
			List<?> objList = (List<?>) obj;
			for (Object a : objList) {
				list.add((ItemStack) a);
			}
		}
		
		config.set(Token.getToken(p), null);
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void setHold(Player p, List<ItemStack> list) {
		File file = new File(plugin.getDataFolder(), "deathDropSave.yml");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set(Token.getToken(p), list);
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void noFrame(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getPlayer().getItemInHand();
		
		if (item == null || p.isOp()) return;
		
		if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME) &&
				(UnlimitedTagAPI.hasTag(item, TagType.LOCKED) ||
				UnlimitedTagAPI.hasTag(item, TagType.STAMPED) ||
				UnlimitedTagAPI.hasTag(item, TagType.DEADLINE) ||
				UnlimitedTagAPI.hasTag(item, TagType.SPATIAL))) {
			e.setCancelled(true);
			
			e.getPlayer().sendMessage(plugin.main+"§c▒§r 액자에 걸 수 없는 아이템입니다!");
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void noDispenser(BlockDispenseEvent e) {
		if (UnlimitedTagAPI.hasTag(e.getItem(), TagType.REQUIRE_LV) ||
				UnlimitedTagAPI.hasTag(e.getItem(), TagType.PERMISSION) ||
				UnlimitedTagAPI.hasTag(e.getItem(), TagType.ICON)) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void noClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		ItemStack cursor = e.getCursor();
		
		if (p.isOp() || (item == null && cursor == null)) return;
		
		if (e.getInventory().getType().equals(InventoryType.CRAFTING)) {
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.VESTED)) {
				e.setCancelled(true);
				plugin.util.updateInv(p);
				
				p.getWorld().dropItem(p.getLocation(), item.clone());
				e.setCurrentItem(null);
				p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0F, 1.0F);
				p.sendMessage(plugin.main+"§c▒§r 당신의 아이템이 아닙니다!");
				return;
			}
			
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.DEADLINE)) {
				e.setCancelled(true);
				plugin.util.updateInv(p);
				
				e.setCurrentItem(null);
				p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
				p.sendMessage(plugin.main+"§a▒§r 아이템이 기간이 만료되어 사라졌습니다.");
				return;
			}
			
			if (UnlimitedTagAPI.hasTag(item, TagType.VEST_ON_PICKUP)) {
				UnlimitedTagAPI.addTag(item, TagType.VESTED, Token.getToken(p));
				UnlimitedTagAPI.removeTag(item, TagType.VEST_ON_PICKUP);
			}
			
			if (UnlimitedTagAPI.hasTag(item, TagType.DEADLINE_ON_PICKUP)) {
				UnlimitedTagAPI.addTag(item, TagType.DEADLINE, UnlimitedTagAPI.Deadline.getDeadline(item));
				UnlimitedTagAPI.removeTag(item, TagType.DEADLINE_ON_PICKUP);
			}
			
			boolean isCursor = cursor != null && 
					(e.getSlot() == 5 || e.getSlot() == 6 || e.getSlot() == 7 || e.getSlot() == 8) && 
					!UnlimitedTagAPI.isAvailable(p, cursor, TagType.REQUIRE_LV);
			boolean isShift = item != null && e.isShiftClick();
			
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.REQUIRE_LV) && (isCursor || isShift)) {
				e.setCancelled(true);
				plugin.util.updateInv(p);
				
				p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.5F);
				p.sendMessage(plugin.main+"§c▒§r 레벨이 부족합니다!");
				return;
			}
			
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.PERMISSION) && (isCursor || isShift)) {
				e.setCancelled(true);
				plugin.util.updateInv(p);
				
				p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.5F);
				p.sendMessage(plugin.main+"§c▒§r 이 아이템을 사용할 권한이 없습니다!");
				return;
			}
		} else {
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.VESTED)) {
				e.setCancelled(true);
				plugin.util.updateInv(p);
				
				p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 1.0F, 1.0F);
				p.sendMessage(plugin.main+"§c▒§r 당신의 아이템이 아닙니다!");
				return;
			}
			
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.LOCKED)) {
				e.setCancelled(true);
				plugin.util.updateInv(p);
				
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.5F);
				p.sendMessage(plugin.main+"§c▒§r 잠겨진 아이템입니다!");
				return;
			}
			
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.STAMPED)) {
				e.setCancelled(true);
				plugin.util.updateInv(p);
				
				p.playSound(p.getLocation(), Sound.ZOMBIE_METAL, 1.0F, 1.5F);
				p.sendMessage(plugin.main+"§c▒§r 각인된 아이템입니다!");
				return;
			}
			
			if (!UnlimitedTagAPI.isAvailable(p, item, TagType.DEADLINE)) {
				e.setCancelled(true);
				plugin.util.updateInv(p);
				
				e.setCurrentItem(null);
				p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.5F);
				p.sendMessage(plugin.main+"§a▒§r 아이템이 기간이 만료되어 사라졌습니다.");
				return;
			}
			
			if (UnlimitedTagAPI.hasTag(item, TagType.DEADLINE_ON_PICKUP)) {
				UnlimitedTagAPI.addTag(item, TagType.DEADLINE, UnlimitedTagAPI.Deadline.getDeadline(item));
				UnlimitedTagAPI.removeTag(item, TagType.DEADLINE_ON_PICKUP);
			}
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}