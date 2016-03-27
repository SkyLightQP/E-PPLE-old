package me.efe.efeserver.reform.farming;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.efe.efeitems.ItemStorage;
import me.efe.efemastery.MasteryManager;
import me.efe.efemastery.MasteryManager.MasteryType;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efeserver.util.Scoreboarder;
import me.efe.skilltree.UserData;
import me.efe.unlimitedrpg.customexp.CustomExpAPI;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.horyu1234.horyulogger.HoryuLogger;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.util.ParticleEffect;

public class FarmingListener implements Listener {
	public EfeServer plugin;
	public Map<UUID, Farm> users = new HashMap<UUID, Farm>();
	
	public FarmingListener(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	private void plantSeeds(Player player, Farm farm, Crop crop, int rottenFleshAmount, int boneMealAmount) {
		farm.plantSeeds(crop, rottenFleshAmount, boneMealAmount);
		
		plugin.farmBuilder.plantCrops(farm.getCenterLocation(), farm.getCrop().getBlockType());
		
		updateDurability(player, 0.1);
		
		player.playSound(player.getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);
		player.sendMessage("��a�ơ�r �۹��� �ɾ����ϴ�! " + farm.getRestTime() + " �Ŀ� ��Ȯ�� �� �ֽ��ϴ�.");
		
		Scoreboarder.message(player, new String[]{"��6��l>>��6 �۹� ����", ""}, 3);
	}
	
	private void harvest(Player player, Farm farm) {
		Crop crop = farm.getCrop();
		
		if (!farm.hasDisease()) {
			if (Math.random() <= getDiseaseProbability(player.getItemInHand())) {
				player.sendMessage("��a�ơ�r �����ط� ���� �ƹ��͵� ��Ȯ���� ���߽��ϴ�!");
				player.sendMessage("��a�ơ�r �ƹ����� ������ ������ ������ ��ģ �� �����ϴ�..");
			} else {
				player.sendMessage("��a�ơ�r �۹��� ��Ȯ�߽��ϴ�!");
			}
		} else {
			player.sendMessage("��a�ơ�r �����ط� ���� �ƹ��͵� ��Ȯ���� ���߽��ϴ�!");
			player.sendMessage(farm.getDisease().getMessage());
		}
		
		plugin.farmBuilder.clearCrops(farm);
		
		farm.harvest();
		
		MasteryManager.giveMasteryExp(player, MasteryType.FARM, crop.getExpMultiplier());
		
		int exp = (int) (MasteryManager.getGivingExp(player, MasteryType.FARM) * crop.getExpMultiplier());
		CustomExpAPI.giveExp(player, exp);
		HoryuLogger.getInstance().getLogManager().logExp(player, exp, "farm:" + crop.getName());
		
		Scoreboarder.message(player, new String[]{"��6��l>>��6 " + crop.getName() + " ��Ȯ", "��2Exp +" + exp}, 3);
	}
	
	private double getDiseaseProbability(ItemStack handItem) {
		switch (handItem.getType()) {
		case WOOD_HOE:
			return 0.9D;
		case STONE_HOE:
			return 0.6D;
		case IRON_HOE:
			return 0.15D;
		case GOLD_HOE:
			return 0.0D;
		case DIAMOND_HOE:
			return 0.0D;
		default:
			return 0.0D;
		}
	}
	
	private void updateDurability(Player player, double percent) {
		ItemStack item = player.getItemInHand();
		Material type = item.getType();
		
		if (type == Material.GOLD_HOE)
			percent *= 2.0D;
		
		item.setDurability((short) (item.getDurability() + type.getMaxDurability() * percent));
		
		if (item.getDurability() >= type.getMaxDurability()) {
			player.setItemInHand(new ItemStack(Material.AIR));
			
			ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(type, (byte) 0), 0.0F, 0.0F, 0.0F, 0.075F, 10, player.getEyeLocation(), 32);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getItem() == null)
			return;
		
		if (event.getItem().getType().name().endsWith("_HOE") && (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() &&
						ItemStorage.getName(event.getItem()).equals(ItemStorage.LUMINOUS_HOE.getItemMeta().getDisplayName())) {
					return;
				}
				
				event.setCancelled(true);
				plugin.util.updateInv(event.getPlayer());
			}
			
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				
				if (!sign.getLine(0).equals("��2[Farm]"))
					return;
				
				Farm farm = new Farm(sign);
				
				if (!farm.getOwner().equals(event.getPlayer())) {
					event.getPlayer().sendMessage("��c�ơ�r ����� �� ���� ������ �ƴմϴ�!");
					return;
				}
				
				int maxAmount = plugin.farmBuilder.getMaxFarmAmount(event.getPlayer());
				int amount = PlayerData.get(event.getPlayer()).getFarmAmount();
				
				if (amount > maxAmount) {
					plugin.farmBuilder.destroyFarm(event.getPlayer(), farm.getCenterLocation());
					ParticleEffect.EXPLOSION_HUGE.display(0.0F, 0.0F, 0.0F, 0.0F, 1, farm.getCenterLocation(), 32);
					
					event.getPlayer().sendMessage("��a�ơ�r ���Ѻ��� ���� ���� �����Ͽ� ���� ���� ó���Ǿ����ϴ�.");
					return;
				}
				
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					openGUI(event.getPlayer(), farm);
					
				} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (farm.isEmpty()) {
						event.getPlayer().sendMessage("��c�ơ�r �۹��� ���� �ʾҽ��ϴ�!");
						return;
					}
					
					if (!farm.isHarvestable()) {
						event.getPlayer().sendMessage("��c�ơ�r " + farm.getRestTime() + " �Ŀ� ��Ȯ�� �� �ֽ��ϴ�.");
						return;
					}
					
					harvest(event.getPlayer(), farm);
				}
			}
			
			
		} else if (event.getItem().getType() == Material.WATER_BUCKET && event.getAction() == Action.LEFT_CLICK_BLOCK &&
				event.getClickedBlock().getState() instanceof Sign) {
			Sign sign = (Sign) event.getClickedBlock().getState();
			
			if (!sign.getLine(0).equals("��2[Farm]"))
				return;
			
			event.setCancelled(true);
			
			Farm farm = new Farm(sign);
			farm.updateWater(50);
			
			event.getPlayer().setItemInHand(new ItemStack(Material.BUCKET));
			
			if (farm.getOwner().equals(event.getPlayer())) {
				event.getPlayer().sendMessage("��a�ơ�r �翡 ���� �־����ϴ�. ��7[���� +50%p]");
			} else {
				event.getPlayer().sendMessage("��a�ơ�r " + farm.getOwner().getName() + "���� �翡 ���� �־����ϴ�. ��7[���� +50%p]");
			}
			
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.SWIM, 1.0F, 1.0F);
			
			Scoreboarder.message(event.getPlayer(), new String[]{"��6��l>>��6 ���� ����", "��e����: " + farm.getWater() + "%"}, 3);
		}
	}
	
	public void openGUI(Player player, Farm farm) {
		Inventory inv = plugin.getServer().createInventory(player, 9*3, "��2�ơ�r �۹� ���");
		
		if (farm.isEmpty()) {
			UserData data = new UserData(player);
			
			for (Crop crop : Crop.values()) {
				int level = data.getLevel(crop.getSkill());
				
				if (level < 1)
					continue;
				
				boolean hasSeeds = hasItem(player, crop.getSeedItem(), crop.getSeedAmount());
				
				ItemStack item = plugin.util.createDisplayItem("��b" + crop.getName(), new ItemStack(crop.getSeedItem().getType(), crop.getSeedAmount()), new String[]{
					(hasSeeds ? "��a" : "��c") + ">>��r " + crop.getSeedName() + " " + crop.getSeedAmount() + "�� �ʿ�",
					"��a>>��r " + crop.getTime(level) + "�� �ҿ�"});
				
				if (crop == Crop.NETHER_WARTS) {
					plugin.util.addLore(item, ((player.getItemInHand().getType() == Material.DIAMOND_HOE) ? "��a" : "��c") + ">>��r ���̾Ƹ�� ���� �䱸��");
				}
				
				inv.addItem(item);
			}
		} else if (!farm.isHarvestable()) {
			inv.setItem(13, plugin.util.createDisplayItem("��b" + farm.getCrop().getName(), new ItemStack(farm.getCrop().getItemType()), new String[]{
				"��e>>��r " + farm.getRestTime() + " �� ��Ȯ ����", "", "��c��Ŭ������ ���� ���ƾ����ϴ�."}));
		} else {
			inv.setItem(13, plugin.util.createDisplayItem("��b" + farm.getCrop().getName(), new ItemStack(farm.getCrop().getItemType()), new String[]{
				"��e>>��r ��Ȯ ����", "", "��9��Ŭ���ϸ� ��Ȯ�մϴ�."}));
		}
		
		inv.setItem(18, plugin.util.createDisplayItem("��e��nTip", new ItemStack(Material.SIGN), new String[]{
			"����, �� ������ �����", "�۹��� �����ظ� ����ų �� �ֽ��ϴ�.", "ö, ���̾Ƹ�� ������ ����� �����մϴ�."}));
		
		inv.setItem(25, plugin.util.createDisplayItem("��b�� �ֱ�", new ItemStack(Material.WATER_BUCKET), new String[]{
			"��e>>��r ���� ����: " + farm.getWater() + "%", "", "��8��l[!]��8 ���� ������ 0%���� Ŀ��", "     ��8�۹��� ���� �� �ֽ��ϴ�."}));
		
		inv.setItem(26, plugin.util.createDisplayItem("��c�� ����", new ItemStack(Material.TNT), new String[]{
			"���� ������ �ٸ� ����", "���Ӱ� ���� ������ �� �ֽ��ϴ�.", "��, �ɾ��� �۹��� �������� �ʽ��ϴ�."}));
		
		users.put(player.getUniqueId(), farm);
		player.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!users.containsKey(event.getWhoClicked().getUniqueId()))
			return;
		
		event.setCancelled(true);
		
		if (event.getRawSlot() >= 27 || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR
				|| event.getAction() == InventoryAction.NOTHING)
			return;
		
		Player player = (Player) event.getWhoClicked();
		Farm farm = users.get(player.getUniqueId());
		
		if (event.getRawSlot() == 18)
			return;
		
		if (event.getRawSlot() == 25) {
			int slot = -1;
			
			for (int i = 0; i < player.getInventory().getContents().length; i ++) {
				if (player.getInventory().getContents()[i] == null)
					continue;
				
				if (player.getInventory().getContents()[i].getType().equals(Material.WATER_BUCKET)) {
					slot = i;
					break;
				}
			}
			
			if (slot == -1) {
				player.sendMessage("��c�ơ�r �� �絿�̸� �����ϰ� ���� �ʽ��ϴ�.");
				return;
			}
			
			farm.updateWater(50);
			
			player.getInventory().setItem(slot, new ItemStack(Material.BUCKET));
			
			player.playSound(player.getLocation(), Sound.SWIM, 1.0F, 1.0F);
			player.sendMessage("��a�ơ�r �翡 ���� �־����ϴ�. ��8[���� +50%]");
			
			player.closeInventory();
			openGUI(player, farm);
			
			return;
		} else if (event.getRawSlot() == 26) {
			if (plugin.util.containsLore(event.getCurrentItem().clone(), "��c�����Ϸ��� �ٽ� �� �� Ŭ�����ּ���.")) {
				player.closeInventory();
				
				plugin.farmBuilder.destroyFarm(player, farm.getCenterLocation());
				ParticleEffect.EXPLOSION_HUGE.display(0.0F, 0.0F, 0.0F, 0.0F, 1, farm.getCenterLocation(), 32);
				
				updateDurability(player, 0.8);
				
				player.sendMessage("��a�ơ�r ���� ���ŵǾ����ϴ�.");
				player.sendMessage("��a�ơ�r ���� ���� �ٽ� ������ �� �ֽ��ϴ�!");
			} else {
				plugin.util.addLore(event.getCurrentItem(), "��c�����Ϸ��� �ٽ� �� �� Ŭ�����ּ���.");
				
				player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
				player.sendMessage("��a�ơ�r ������ ���� �����Ͻðڽ��ϱ�?");
			}
			
			return;
		}
		
		if (farm.isEmpty()) {
			if (event.getInventory().getItem(13) == null || event.getInventory().getItem(13).getType() == Material.AIR) {
				Crop crop = Crop.getCrop(event.getCurrentItem().getItemMeta().getDisplayName().substring(2));
				
				if (!hasItem(player, crop.getSeedItem(), crop.getSeedAmount())) {
					player.sendMessage("��c�ơ�r �ʿ��� ������ ������ �����մϴ�!");
					return;
				}
				
				if (crop == Crop.NETHER_WARTS && player.getItemInHand().getType() != Material.DIAMOND_HOE) {
					player.sendMessage("��c�ơ�r �ش� �۹��� ���ۿ� ���̾Ƹ�� ���̰� �䱸�˴ϴ�!");
					return;
				}
				
				event.getInventory().clear();
				
				event.getInventory().setItem(13, plugin.util.createDisplayItem("��b��ᰡ �ִٸ� ����غ�����!", new ItemStack(crop.getItemType()),
						new String[]{"��b������� �ʴ´ٸ� �̰��� Ŭ�����ּ���."}));
				
				event.getInventory().setItem(11, plugin.util.createDisplayItem("��r���� ��� ��7��0", new ItemStack(Material.ROTTEN_FLESH),
						new String[]{"��Ȯ���� �ø� �� �ֽ��ϴ�.", "������ ����� �ذ� �˴ϴ�.", "", "��9��Ŭ��: +1��", "��9��Ŭ��: -1��", "��9Shift: ��10"}));
				event.getInventory().setItem(15, plugin.util.createDisplayItem("��r�İ��� ��7��0", new ItemStack(Material.INK_SACK, 1, (short) 15),
						new String[]{"�ҿ� �ð��� ������ �� �ֽ��ϴ�.", "������ ����� �ذ� �˴ϴ�.", "", "��9��Ŭ��: +1��", "��9��Ŭ��: -1��", "��9Shift: ��10"}));
			} else {
				if (event.getRawSlot() == 11) {
					ItemStack item = event.getInventory().getItem(11).clone();
					int maxAmount = getItemAmount(player, new ItemStack(Material.ROTTEN_FLESH));
					int amount = item.getAmount();
					int value = 1;
					
					if (event.isRightClick())
						value = -1;
					if (event.isShiftClick())
						value *= 10;
					
					amount += value;
					
					if (amount < 0)
						amount = 0;
					if (amount > maxAmount) {
						amount = maxAmount;
						player.sendMessage("��c�ơ�r �������� �������� �����մϴ�!");
					}
					
					ItemMeta meta = item.getItemMeta();
					
					meta.setDisplayName(meta.getDisplayName().split("��")[0] + "��" + amount);
					item.setItemMeta(meta);
					
					event.getInventory().setItem(11, item);
					
				} else if (event.getRawSlot() == 15) {
					int maxAmount = getItemAmount(player, new ItemStack(Material.INK_SACK, 1, (short) 15));
					int amount = event.getInventory().getItem(15).clone().getAmount();
					int value = 1;
					
					if (event.isRightClick())
						value = -1;
					if (event.isShiftClick())
						value *= 10;
					
					amount += value;
					
					if (amount < 0)
						amount = 0;
					if (amount > maxAmount) {
						amount = maxAmount;
						player.sendMessage("��c�ơ�r �������� �������� �����մϴ�!");
					}
					
					event.getInventory().getItem(15).setAmount(amount);
					
				} else if (event.getRawSlot() == 13) {
					Crop crop = Crop.getCrop(event.getCurrentItem().getItemMeta().getDisplayName().substring(2));
					int rottenFleshAmount = event.getInventory().getItem(11).clone().getAmount();
					int boneMealAmount = event.getInventory().getItem(15).clone().getAmount();
					
					if (!hasItem(player, crop.getSeedItem(), crop.getSeedAmount())) {
						player.sendMessage("��c�ơ�r �ʿ��� ������ ������ �����մϴ�!");
						return;
					}
					
					if (crop == Crop.NETHER_WARTS && player.getItemInHand().getType() != Material.DIAMOND_HOE) {
						player.sendMessage("��c�ơ�r �ش� �۹��� ���ۿ� ���̾Ƹ�� ���̰� �䱸�˴ϴ�!");
						return;
					}
					
					if (!hasItem(player, new ItemStack(Material.ROTTEN_FLESH), rottenFleshAmount)) {
						player.sendMessage("��c�ơ�r �������� �������� �����մϴ�!");
						return;
					}
					
					if (!hasItem(player, new ItemStack(Material.INK_SACK, 1, (short) 15), boneMealAmount)) {
						player.sendMessage("��c�ơ�r �������� �������� �����մϴ�!");
						return;
					}
					
					takeItem(player, crop.getSeedItem(), crop.getSeedAmount());
					takeItem(player, new ItemStack(Material.ROTTEN_FLESH), rottenFleshAmount);
					takeItem(player, new ItemStack(Material.INK_SACK, 1, (short) 15), boneMealAmount);
					
					player.closeInventory();
					
					plantSeeds(player, farm, crop, rottenFleshAmount, boneMealAmount);
				}
			}
		} else if (farm.isHarvestable() && event.getRawSlot() == 13) {
			if (event.isRightClick()) {
				if (plugin.util.containsLore(event.getCurrentItem().clone(), "��c���ƾ������� �ٽ� �� �� Ŭ�����ּ���.")) {
					player.closeInventory();
					
					plugin.farmBuilder.clearCrops(farm);
					
					farm.clearup();
					
					updateDurability(player, 0.1);
					
					player.sendMessage("��a�ơ�r ���� ���ƾ������ϴ�.");
				} else {
					plugin.util.addLore(event.getCurrentItem(), "��c���ƾ������� �ٽ� �� �� Ŭ�����ּ���.");
					
					player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
					player.sendMessage("��a�ơ�r ������ ���� ���ƾ��ڽ��ϱ�?");
				}
			} else {
				
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (users.containsKey(event.getPlayer().getUniqueId())) {
			users.remove(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void grow(StructureGrowEvent event) {
		for (ProtectedRegion region : WGBukkit.getRegionManager(event.getWorld()).getApplicableRegions(event.getLocation())) {
			if (region.getId().contains("_farm_")) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void fromTo(BlockFromToEvent event) {
		for (ProtectedRegion region : WGBukkit.getRegionManager(event.getBlock().getWorld()).getApplicableRegions(event.getToBlock().getLocation())) {
			if (region.getId().contains("_farm_")) {
				event.setCancelled(true);
			}
		}
	}
	
	private int getItemAmount(Player player, ItemStack target) {
		int amount = 0;
		
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null || !item.getType().equals(target.getType())) continue;
			
			if (item.isSimilar(target)) {
				amount += item.clone().getAmount();
			}
		}
		
		return amount;
	}
	
	private boolean hasItem(Player player, ItemStack item, int amount) {
		return getItemAmount(player, item.clone()) >= amount;
	}
	
	private void takeItem(Player player, ItemStack target, int amount) {
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
