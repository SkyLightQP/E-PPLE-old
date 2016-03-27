package me.efe.efeserver.reform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efetutorial.TutorialState;
import me.efe.efetutorial.listeners.TutorialListener;
import me.efe.skilltree.UserData;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.BlockIterator;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class MyBoat implements Listener {
	public EfeServer plugin;
	public HashMap<Player, Boat> boats = new HashMap<Player, Boat>();
	public HashMap<UUID, String> destMap = new HashMap<UUID, String>();
	public List<String> users = new ArrayList<String>();

	public MyBoat(EfeServer plugin) {
		this.plugin = plugin;
	}

	public ItemStack getBoatItem(Player p) {
		return plugin.util.createDisplayItem("��b��Ʈ", new ItemStack(
				Material.BOAT), new String[] {"��Ʈ�� ��ġ�ϸ�", "���ظ� �����մϴ�."});
	}

	public ItemStack getCompassItem(Player p) {
		return plugin.util.createDisplayItem("��b������", new ItemStack(
				Material.COMPASS), new String[] {"��Ŭ��: ���� ��ǥ �� ����", "��Ŭ��: �ڵ� ����"});
	}

	public void createBoat(Player p, Location loc) {
		if (boats.containsKey(p))
			removeBoat(p);

		Boat boat = (Boat) loc.getWorld().spawnEntity(loc, EntityType.BOAT);

		boat.getLocation().setPitch(p.getLocation().getPitch());
		boat.getLocation().setYaw(p.getLocation().getYaw());
		boat.setPassenger(p);

		boats.put(p, boat);
		
		if (destMap.containsKey(p.getUniqueId())) {
			String dest = destMap.get(p.getUniqueId());
			
			Location destLoc = IslandUtils.getIsleLoc(dest);
			p.setCompassTarget(destLoc);
			
			ActionBarAPI.sendActionBar(p, "��b"+IslandUtils.toDisplay(dest)+" ���� ���� ������");
		}
		
		TutorialListener.onEnterBoat(p);
	}
	
	public boolean isAutopilot(Boat boat) {
		return boat.hasMetadata("autopilot");
	}
	
	public void setAutopilot(Boat boat, boolean val) {
		if (val) {
			boat.setMetadata("autopilot", new FixedMetadataValue(plugin, autopilot(boat)));
		} else {
			int id = boat.getMetadata("autopilot").get(0).asInt();
			plugin.getServer().getScheduler().cancelTask(id);
			
			boat.removeMetadata("autopilot", plugin);
		}
	}
	
	private int autopilot(final Boat boat) {
		int id = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (boat == null || boat.isDead() || !boats.containsValue(boat) || !boat.hasMetadata("autopilot")) return;
				
				boat.setVelocity(boat.getVelocity().multiply(2.0D));
				
				boat.setMetadata("autopilot", new FixedMetadataValue(plugin, autopilot(boat)));
			}
		}, 20L);
		
		return id;
	}

	public void removeBoat(final Player p) {
		if (!boats.containsKey(p))
			return;

		p.getWorld().playEffect(boats.get(p).getLocation(), Effect.STEP_SOUND,
				Material.WOOD);

		if (boats.get(p) != null && boats.get(p).getPassenger().equals(p)) {
			final float pitch = p.getLocation().getPitch();
			final float yaw = p.getLocation().getYaw();
			final Location loc = boats.get(p).getLocation().clone();

			loc.setPitch(pitch);
			loc.setYaw(yaw);

			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							if (p == null || !p.isOnline())
								return;

							p.teleport(loc.add(0, 1, 0));
						}
					}, 3L);
		}

		boats.get(p).remove();
		boats.remove(p);

		if (PlayerData.get(p).getOptionMenu())
			p.getInventory().setItem(7, getBoatItem(p));
		else
			p.getInventory().setItem(8, getBoatItem(p));
	}

	public Player getOwner(Boat boat) {
		for (Player p : boats.keySet()) {
			if (boats.get(p).equals(boat)) {
				return p;
			}
		}

		return null;
	}

	@EventHandler
	public void exit(VehicleExitEvent e) {
		if (e.getVehicle() instanceof Boat && e.getExited() instanceof Player) {
			Player p = (Player) e.getExited();
			if (!boats.containsKey(p))
				return;

			removeBoat(p);
		}
	}

	@EventHandler
	public void destroy(VehicleDestroyEvent e) {
		if (e.getVehicle() instanceof Boat && e.getVehicle().getPassenger() instanceof Player) {
			Player attacker = null;

			if (e.getAttacker() instanceof Player) {
				attacker = (Player) e.getAttacker();
			} else if (e.getAttacker() instanceof Projectile) {
				Projectile proj = (Projectile) e.getAttacker();

				if (proj.getShooter() instanceof Player) {
					attacker = (Player) proj.getShooter();
				}
			}

			Player p = (Player) e.getVehicle().getPassenger();
			if (!boats.containsKey(p))
				return;
			
			if (attacker == null) {
				e.setCancelled(true);
				removeBoat(p);
				return;
			}
			
			if (!attacker.isOp()) {
				e.setCancelled(true);
				return;
			}

			e.setCancelled(true);
			removeBoat(p);
		}
	}
	
	@EventHandler
	public void death(PlayerDeathEvent e) {
		if (boats.containsKey(e.getEntity())) {
			removeBoat(e.getEntity());
		}
	}

	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (plugin.util.isRightClick(e) && e.getItem() != null) {
			if (e.getItem().getType().equals(Material.BOAT)) {
				e.setCancelled(true);
				plugin.util.updateInv(e.getPlayer());

				UserData data = new UserData(e.getPlayer());
				int level = data.getLevel("boat.sailing");
				
				if (level == 0) {
					e.getPlayer().sendMessage("��c�ơ�r ���� ������ �� �����ϴ�.");
					return;
				}

				Block block = getTarget(e.getPlayer(), 4);
				if (block == null
						|| !block.getType().equals(Material.STATIONARY_WATER))
					return;

				createBoat(e.getPlayer(), block.getLocation());

				if (PlayerData.get(e.getPlayer()).getOptionMenu())
					e.getPlayer().getInventory()
							.setItem(7, getCompassItem(e.getPlayer()));
				else
					e.getPlayer().getInventory()
							.setItem(8, getCompassItem(e.getPlayer()));
				
			} else if (e.getItem().getType().equals(Material.COMPASS)) {
				e.setCancelled(true);

				UserData data = new UserData(e.getPlayer());
				int level = data.getLevel("boat.autopilot");
				
				if (level == 0) {
					e.getPlayer().sendMessage("��c�ơ�r �ڵ� ���ش� ���� ����� �� �����ϴ�.");
					return;
				}
				
				Boat boat = boats.get(e.getPlayer());
				
				if (!isAutopilot(boat)) {
					setAutopilot(boat, true);
					
					e.getPlayer().sendMessage("��a�ơ�r �ڵ� ���� ��尡 �����˴ϴ�.");
				} else {
					setAutopilot(boat, false);
					
					e.getPlayer().sendMessage("��a�ơ�r �ڵ� ���� ��尡 ��ҵǾ����ϴ�.");
				}
			}

		} else if (plugin.util.isLeftClick(e) && e.getItem() != null
				&& e.getItem().getType().equals(Material.COMPASS)) {
			e.setCancelled(true);
			plugin.util.updateInv(e.getPlayer());

			if (!e.getPlayer().getWorld().equals(plugin.world)) {
				e.getPlayer().sendMessage("��c�ơ�r �� ���迡���� ����� �� ���� ����Դϴ�.");
				return;
			}
			
			TutorialListener.onClickNavigation(e.getPlayer());

			openGUI(e.getPlayer());
		}
	}

	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9 * 5,
				"��1�ơ�r ��ħ��");
		
		if (TutorialState.get(p) >= TutorialState.WELCOME_TO_POLARIS) {
			inv.setItem(22, getIcon(p, IslandUtils.POLARIS, "��a��l", new ItemStack(Material.BED), new String[] {
				"��2[���󸮽�]",
				"�ٴ��� �߽��� �Ǵ� ��.",
				"������ ���۹��� �Ǹ��� �� �ִ�." }));
			inv.setItem(4, getIcon(p, IslandUtils.MINERAS_ALPHA, "��6��l", new ItemStack(Material.STONE_PICKAXE), new String[] { 
				"��2[�̳׶� ����]",
				"ù ��° ���� ��.",
				"�پ��� ������ ä���� �� �ִ�.",
				"��Ը� ���� �� ������ �ڸ���� �ִ�." }));
			inv.setItem(40, getIcon(p, IslandUtils.MINERAS_BETA, "��6��l", new ItemStack(Material.IRON_PICKAXE), new String[] {
				"��2[�̳׶� ��Ÿ]",
				"�� ��° ���� ��.",
				"�پ��� ������ ä���� �� �ִ�." }));
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
		} else {
			inv.setItem(10, getIcon(p, IslandUtils.TUTORIAL_START, "��a", new ItemStack(Material.GRASS), new String[] {
				"Ʃ�丮���� �����ϴ� ��." }));
			inv.setItem(12, getIcon(p, IslandUtils.TUTORIAL_MINE, "��6", new ItemStack(Material.STONE), new String[] {
				"ä���� �����ϴ� ��." }));
			inv.setItem(14, getIcon(p, IslandUtils.TUTORIAL_WEED, "��2", new ItemStack(Material.DIRT), new String[] {
				"���� ä���� ���� ��." }));
			inv.setItem(25, getIcon(p, IslandUtils.TUTORIAL_FARM, "��a", new ItemStack(Material.HAY_BLOCK), new String[] {
				"��縦 ü���غ��� ��." }));
			inv.setItem(33, getIcon(p, IslandUtils.TUTORIAL_FISH, "��b", new ItemStack(Material.SAND), new String[] {
				"���ø� �����غ��� ��." }));
			inv.setItem(31, getIcon(p, IslandUtils.TUTORIAL_HUNT, "��b", new ItemStack(Material.SMOOTH_BRICK), new String[] {
				"������ ��." }));
		}

		p.openInventory(inv);
		users.add(p.getName());
		
		TutorialListener.onClickNavigation(p);
	}

	private ItemStack getIcon(Player p, String isle, String prefix, ItemStack item, String[] lore) {
		ItemStack icon = plugin.util.createDisplayItem(prefix + IslandUtils.toDisplay(isle), item, lore);
		int distance = (int) IslandUtils.getIsleLoc(isle).distance(p.getLocation());

		plugin.util.addLore(icon, "");
		plugin.util.addLore(icon, "��9" + distance + " �� �Ÿ�");
		
		if (IslandUtils.getIsleName(p.getLocation()).equals(isle)) {
			plugin.util.addLore(icon, "��9���� �湮��");
		}
		
		if (destMap.containsKey(p.getUniqueId()) && destMap.get(p.getUniqueId()).equals(isle)) {
			icon.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
			plugin.util.addLore(icon, "��9�� ���� ���� ������");
		}

		return icon;
	}

	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName()))
			return;
		e.setCancelled(true);

		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 45)
			return;

		Player p = (Player) e.getWhoClicked();
		PlayerData data = PlayerData.get(p);
		int subStringNum = TutorialState.get(p) >= TutorialState.WELCOME_TO_POLARIS ? 4 : 2;
		String name = e.getCurrentItem().getItemMeta().getDisplayName().substring(subStringNum);
		String region = IslandUtils.fromDisplay(name);
		
		if (TutorialState.get(p) == TutorialState.LETS_SELECT_SKILL) {
			if (region.equals(IslandUtils.TUTORIAL_MINE)) {
				TutorialListener.onClickSkill(p);
			} else {
				p.sendMessage("��c�ơ�r �켱 ä�� ���� Ŭ�����ּ���.");
				return;
			}
		}
		
		p.setCompassTarget(IslandUtils.getIsleLoc(region));
		
		destMap.put(p.getUniqueId(), region);
		ActionBarAPI.sendActionBar(p, "��b"+name+" ���� ���� ������");

		ItemStack compass = getCompassItem(p);
		plugin.util.addLore(compass, "��9" + name + " ������ �̵���");

		p.getInventory().setItem(data.getOptionMenu() ? 7 : 8, compass);

		p.sendMessage("��a�ơ�r " + name + " ������ ������ �����Ǿ����ϴ�!");
		p.closeInventory();
	}

	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}

	@EventHandler
	public void drop(PlayerDropItemEvent e) {
		ItemStack item = e.getItemDrop().getItemStack().clone();

		if (item.getType().equals(Material.BOAT) || item.getType().equals(Material.COMPASS)) {
			e.getItemDrop().remove();

			e.getPlayer().getInventory().setItem(PlayerData.get(e.getPlayer()).getOptionMenu() ? 7 : 8, item);
		}
	}

	@EventHandler
	public void quit(PlayerQuitEvent e) {
		removeBoat(e.getPlayer());

		e.getPlayer().getInventory().setItem(8, getBoatItem(e.getPlayer()));
	}

	public Block getTarget(Player p, int range) {
		BlockIterator it = new BlockIterator(p, range);
		Block block = it.next();

		while (it.hasNext()) {
			block = it.next();
			if (block.getType() != Material.AIR) {
				return block;
			}
		}

		return block;
	}
}