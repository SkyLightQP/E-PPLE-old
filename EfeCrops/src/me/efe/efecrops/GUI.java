package me.efe.efecrops;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI implements Listener{
	public EfeCrops plugin;
	public HashMap<String, Sign> viewers = new HashMap<String, Sign>();
	public ItemStack wheat;
	public ItemStack carrot;
	public ItemStack potato;
	public ItemStack pumpkin;
	public ItemStack melon;
	public ItemStack water;
	
	public GUI(EfeCrops plugin){
		this.plugin = plugin;
		wheat = createItem(new ItemStack(Material.WHEAT), "��6��l��", new String[]{"* ���� 9�� �Ҹ�", "* ���� -30", "* 10�� �ҿ�"});
		carrot = createItem(new ItemStack(Material.CARROT_ITEM), "��c��l���", new String[]{"* ��� 9�� �Ҹ�", "* ���� -50", "* 20�� �ҿ�"});
		potato = createItem(new ItemStack(Material.POTATO_ITEM), "��6��l����", new String[]{"* ���� 9�� �Ҹ�", "* ���� -50", "* 20�� �ҿ�"});
		pumpkin = createItem(new ItemStack(Material.PUMPKIN), "��6��lȣ��", new String[]{"* ȣ�ھ� 4�� �Ҹ�", "* ���� -70", "* 40�� �ҿ�"});
		melon = createItem(new ItemStack(Material.MELON_BLOCK), "��a��l����", new String[]{"* ���ھ� 4�� �Ҹ�", "* ���� -100", "* 60�� �ҿ�"});
		water = createItem(new ItemStack(Material.WATER_BUCKET), "��9��l���ֱ�", new String[]{"�� �絿�̸� ����Ͽ� ���� �� �� �ֽ��ϴ�.", "* ���� +50"});
	}
	
	public void openGUI(Player p, Sign sign){
		Inventory inv = plugin.getServer().createInventory(null, 9*3, "[EfeCrops] - �۹� �ɱ�");
		inv.addItem(wheat);
		inv.addItem(carrot);
		inv.addItem(potato);
		inv.addItem(pumpkin);
		inv.addItem(melon);
		inv.setItem(26, water);
		p.openInventory(inv);
		viewers.put(p.getName(), sign);
	}
	
	@EventHandler
	public void choose(InventoryClickEvent e){
		if (!viewers.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		Sign sign = viewers.get(e.getWhoClicked().getName());
		Player p = (Player) e.getWhoClicked();
		int amount = 0;
		if (e.getRawSlot() == 0){
			if (!hasWater(sign)){
				p.sendMessage("��c���r ������ �����մϴ�!");
				return;
			}
			amount = getTotalAmount(Material.SEEDS, p.getInventory());
			if (amount < 9){
				p.sendMessage("��c���r ������ ������ ��c"+(9 - amount)+"��r�� �����մϴ�!");
				return;
			}
			p.getInventory().removeItem(new ItemStack(Material.SEEDS, 9));
			sign.setLine(3, plugin.farmTimer(10, "��"));
			water(sign, 30, false);
			sign.update();
			p.playSound(p.getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);
			p.closeInventory();
			plugin.updateInventory(p);
			p.sendMessage("��a���r �۹��� �ɾ����ϴ�. ��8[��]");
		}else if (e.getRawSlot() == 1){
			if (!hasWater(sign)){
				p.sendMessage("��c���r ������ �����մϴ�!");
				return;
			}
			amount = getTotalAmount(Material.CARROT_ITEM, p.getInventory());
			if (amount < 9){
				p.sendMessage("��c���r ������ ������ ��c"+(9 - amount)+"��r�� �����մϴ�!");
				return;
			}
			p.getInventory().removeItem(new ItemStack(Material.CARROT_ITEM, 9));
			sign.setLine(3, plugin.farmTimer(20, "���"));
			water(sign, 50, false);
			sign.update();
			p.playSound(p.getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);
			p.closeInventory();
			plugin.updateInventory(p);
			p.sendMessage("��a���r �۹��� �ɾ����ϴ�. ��8[���]");
		}else if (e.getRawSlot() == 2){
			if (!hasWater(sign)){
				p.sendMessage("��c���r ������ �����մϴ�!");
				return;
			}
			amount = getTotalAmount(Material.POTATO_ITEM, p.getInventory());
			if (amount < 9){
				p.sendMessage("��c���r ������ ������ ��c"+(9 - amount)+"��r�� �����մϴ�!");
				return;
			}
			p.getInventory().removeItem(new ItemStack(Material.POTATO_ITEM, 9));
			sign.setLine(3, plugin.farmTimer(20, "����"));
			water(sign, 50, false);
			sign.update();
			p.playSound(p.getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);
			p.closeInventory();
			plugin.updateInventory(p);
			p.sendMessage("��a���r �۹��� �ɾ����ϴ�. ��8[����]");
		}else if (e.getRawSlot() == 3){
			if (!hasWater(sign)){
				p.sendMessage("��c���r ������ �����մϴ�!");
				return;
			}
			amount = getTotalAmount(Material.PUMPKIN_SEEDS, p.getInventory());
			if (amount < 4){
				p.sendMessage("��c���r ������ ������ ��c"+(4 - amount)+"��r�� �����մϴ�!");
				return;
			}
			p.getInventory().removeItem(new ItemStack(Material.PUMPKIN_SEEDS, 4));
			sign.setLine(3, plugin.farmTimer(40, "ȣ��"));
			water(sign, 70, false);
			sign.update();
			p.playSound(p.getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);
			p.closeInventory();
			plugin.updateInventory(p);
			p.sendMessage("��a���r �۹��� �ɾ����ϴ�. ��8[ȣ��]");
		}else if (e.getRawSlot() == 4){
			if (!hasWater(sign)){
				p.sendMessage("��c���r ������ �����մϴ�!");
				return;
			}
			amount = getTotalAmount(Material.MELON_SEEDS, p.getInventory());
			if (amount < 4){
				p.sendMessage("��c���r ������ ������ ��c"+(4 - amount)+"��r�� �����մϴ�!");
				return;
			}
			p.getInventory().removeItem(new ItemStack(Material.MELON_SEEDS, 4));
			sign.setLine(3, plugin.farmTimer(60, "����"));
			water(sign, 100, false);
			sign.update();
			p.playSound(p.getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);
			p.closeInventory();
			plugin.updateInventory(p);
			p.sendMessage("��a���r �۹��� �ɾ����ϴ�. ��8[����]");
		}else if (e.getRawSlot() == 26){
			amount = -1;
			for (int i = 0; i < p.getInventory().getContents().length; i ++){
				if (p.getInventory().getContents()[i] == null) continue;
				if (p.getInventory().getContents()[i].getType().equals(Material.WATER_BUCKET)){
					amount = i;
					break;
				}
			}
			if (amount == -1){
				p.sendMessage("��c���r �� �絿�̸� �����ϰ����� �ʽ��ϴ�.");
				return;
			}
			p.getInventory().getItem(amount).setType(Material.BUCKET);
			water(sign, 50, true);
			sign.update();
			plugin.updateInventory(p);
			p.playSound(p.getLocation(), Sound.SWIM, 1.0F, 1.0F);
			p.sendMessage("��a���r �翡 ���� �־����ϴ�. ��8[���� +50]");
		}
	}
	
	public int getTotalAmount(Material type, Inventory inv){
		int amount = 0;
		for (ItemStack item : inv.getContents()){
			if (item == null) continue;
			if (item.getType().equals(type)) amount += item.getAmount();
		}
		return amount;
	}
	
	public void water(Sign sign, int amount, boolean isAdd){
		int water = Integer.parseInt(sign.getLine(2));
		if (isAdd){
			water += amount;
			if (water > 100) water = 100;
		}else {
			water -= amount;
		}
		sign.setLine(2, water+"");
		sign.update();
	}
	
	public boolean hasWater(Sign sign){
		if (Integer.parseInt(sign.getLine(2)) >= 0) return true;
		return false;
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e){
		if (viewers.containsKey(e.getPlayer().getName())) viewers.remove(e.getPlayer().getName());
	}
	
	public ItemStack createItem(ItemStack item, String name, String[] lore){
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> metaLore = new ArrayList<String>();
		for (int i = 0; i < lore.length; i ++){
			metaLore.add(ChatColor.GRAY+lore[i]);
		}
		meta.setDisplayName(name);
		meta.setLore(metaLore);
		item.setItemMeta(meta);
		return item;
	}
}
