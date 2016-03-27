package me.efe.efemobs.rudish.enchant;

import java.util.ArrayList;
import java.util.List;

import me.efe.efemobs.EfeMobs;
import me.efe.efemobs.rudish.UserData;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import de.slikey.effectlib.util.ParticleEffect;

public class EnchantGUI implements Listener {
	public EfeMobs plugin;
	public List<String> users = new ArrayList<String>();
	
	public EnchantGUI(EfeMobs plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE)) {
			e.setCancelled(true);
			
			if (e.getClickedBlock().getLocation().equals(EnchantUtils.getTableLocation()) && e.getItem() != null) {
				if (EnchantUtils.isEnchantable(e.getItem())) {
					openGUI(e.getPlayer());
				} else if (e.getItem().clone().isSimilar(new ItemStack(Material.ENCHANTED_BOOK))) {
					UserData data = new UserData(e.getPlayer());
					
					if (data.getBookCount() >= 10) {
						e.getPlayer().sendMessage("��c�ơ�r �� �̻� Ȯ���� ���� �� �����ϴ�!");
						return;
					}

					e.getPlayer().setItemInHand(plugin.util.getUsed(e.getItem().clone(), e.getPlayer()));
					data.addBookCount();
					
					e.getPlayer().sendMessage("��a�ơ�r ���� ��þƮ ���� Ȯ���� 10%p ����߽��ϴ�. ��8(����: +"+data.getBookCount() * 10+"%p)");
				}
			}
		}
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*6, "��5�ơ�r ��þƮ");
		UserData data = new UserData(p);
		
		int maxGrade = EnchantUtils.getMaxGrade(p.getItemInHand());
		int middleGrade = 1;
		
		if (maxGrade >= 2) {
			middleGrade = 2;
			
			inv.setItem(19, plugin.util.createDisplayItem("��b�ϱ� ��þƮ��3| û�ݼ� x" + EnchantUtils.getPrice(p.getItemInHand(), 1), new ItemStack(Material.ENCHANTED_BOOK), 
					new String[]{"���� ���� ��þƮ�� �ο��մϴ�.", "���� Ȯ��: " + (int) (EnchantUtils.getProbability(p.getItemInHand(), 1) * 100) + "%", "", "��9Ŭ���ϸ� û�ݼ���", "��9�Ҹ��Ͽ� ��þƮ�մϴ�."}));
		}
		
		inv.setItem(22, plugin.util.createDisplayItem("��b�߱� ��þƮ��3| û�ݼ� x" + EnchantUtils.getPrice(p.getItemInHand(), middleGrade), new ItemStack(Material.ENCHANTED_BOOK, 2), 
				new String[]{"���緮�� ��þƮ�� �ο��մϴ�.", "���� Ȯ��: " + (int) (EnchantUtils.getProbability(p.getItemInHand(), middleGrade) * 100) + "%", "", "��9Ŭ���ϸ� û�ݼ���", "��9�Ҹ��Ͽ� ��þƮ�մϴ�."}));
		
		if (maxGrade >= 3) {
			inv.setItem(25, plugin.util.createDisplayItem("��b��� ��þƮ��3| û�ݼ� x" + EnchantUtils.getPrice(p.getItemInHand(), 3), new ItemStack(Material.ENCHANTED_BOOK, 3), 
					new String[]{"�ټ��� ��þƮ�� �ο��մϴ�.", "���� Ȯ��: " + (int) (EnchantUtils.getProbability(p.getItemInHand(), 3) * 100) + "%", "", "��9Ŭ���ϸ� û�ݼ���", "��9�Ҹ��Ͽ� ��þƮ�մϴ�."}));
		}
		
		inv.setItem(4, plugin.util.createDisplayItem("��a��� ��þƮ�� ȿ����", new ItemStack(Material.IRON_PICKAXE), 
				new String[]{"��a��� �߰��� �ƴ� ���� Ȯ�� �����Դϴ�."}));
		inv.setItem(5, plugin.util.createDisplayItem("��b���̾Ƹ�� ������", new ItemStack(Material.DIAMOND), 
				new String[]{"��b��þƮ�� �Ұ����մϴ�."}));
		inv.setItem(6, plugin.util.createDisplayItem("��e�� ������ ��þƮ �ִ�", new ItemStack(Material.GOLD_INGOT), 
				new String[]{"��e������ �ξ� �����ϴ�."}));
		inv.setItem(7, plugin.util.createDisplayItem("��d��þƮ�� å�� ��� ��Ŭ���ϸ�", new ItemStack(Material.ENCHANTED_BOOK), 
				new String[]{"��d���� ��þƮ�� ���� Ȯ����", "��d10%p�� ������ų �� �ֽ��ϴ�.", "��d��þƮ�� å�� ���� ���� ������", "��d���� ȹ���� �����մϴ�."}));
		inv.setItem(8, plugin.util.createDisplayItem("��a��þƮ ���н� û�ݼ��� �Ҹ�Ǹ�", new ItemStack(Material.ANVIL), 
				new String[]{"��a�������� �ı����� �ʽ��ϴ�."}));
		
		if (data.getBookCount() > 0) {
			inv.setItem(31, plugin.util.createDisplayItem("��b���� Ȯ�� +"+data.getBookCount()*10+"%p", new ItemStack(Material.ENCHANTED_BOOK),
					new String[]{"1ȸ ��þƮ �� �ʱ�ȭ�˴ϴ�."}));
		}
		
		
		int[] slots = new int[]{40, 39, 41, 38, 42, 37, 43, 36, 44};
		int amount = getItemAmount(p, new ItemStack(Material.INK_SACK, 1, (short) 4));
		
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		int blockAmount = amount / 9;
		int pieceAmount = amount - blockAmount * 9;
		
		while (blockAmount > 0) {
			if (blockAmount >= 64) {
				list.add(plugin.util.createDisplayItem("��9û�ݼ� "+amount+"�� ������", new ItemStack(Material.LAPIS_BLOCK, 64), new String[]{}));
				blockAmount -= 64;
			} else {
				list.add(plugin.util.createDisplayItem("��9û�ݼ� "+amount+"�� ������", new ItemStack(Material.LAPIS_BLOCK, blockAmount), new String[]{}));
				blockAmount = 0;
			}
		}
		
		if (pieceAmount > 0) {
			list.add(plugin.util.createDisplayItem("��9û�ݼ� "+amount+"�� ������", new ItemStack(Material.INK_SACK, pieceAmount, (short) 4), new String[]{}));
		}
		
		for (int i = 0; i < list.size(); i ++) {
			if (i >= slots.length) break;
			
			inv.setItem(slots[i], list.get(i));
		}
		
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 54) return;
		
		final Player p = (Player) e.getWhoClicked();
		final ItemStack itemStack = p.getItemInHand();
		
		if (!EnchantUtils.isEnchantable(itemStack)) return;
		
		int grade = 0;
		
		switch (e.getRawSlot()) {
		case 19:
			grade = 1;
			break;
		case 22:
			if (e.getInventory().getItem(19) != null && e.getInventory().getItem(19).getType() != Material.AIR)
				grade = 2;
			else
				grade = 1;
			break;
		case 25:
			grade = 3;
			break;
		}
		
		if (grade == 0)
			return;
		
		int price = EnchantUtils.getPrice(itemStack, grade);
		
		if (!hasItem(p, new ItemStack(Material.INK_SACK, 1, (short) 4), price)) {
			p.sendMessage("��c�ơ�r û�ݼ��� �����մϴ�!");
			return;
		}
		
		removeItem(p, new ItemStack(Material.INK_SACK, 1, (short) 4), price);
		
		p.closeInventory();
		
		UserData data = new UserData(p);
		
		double probability = EnchantUtils.getProbability(itemStack, grade) + data.getBookCount() * 0.1;
		data.setBookCount(0);
		
		if (Math.random() <= probability) {
			EnchantUtils.enchant(p.getItemInHand(), grade);
			
			final Firework firework = p.getWorld().spawn(EnchantUtils.getTableLocation().add(0.5, 0, 0.5), Firework.class);
			FireworkMeta meta = firework.getFireworkMeta();
			
			FireworkEffect effect = FireworkEffect.builder().with(Type.BALL_LARGE).withColor(Color.FUCHSIA).withFlicker().build();
			meta.addEffect(effect);
			meta.setPower(1);
			
			firework.setFireworkMeta(meta);
			
			ItemStack hand = p.getItemInHand().clone();
			hand.setAmount(1);
			
			final Item item = p.getWorld().dropItem(EnchantUtils.getTableLocation().add(0.5, 0, 0.5), hand);
			
			item.setPickupDelay(Integer.MAX_VALUE);
			
			firework.setPassenger(item);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					item.remove();
					firework.detonate();
					
					p.sendMessage("��a�ơ�r ��þƮ�� �����߽��ϴ�!!");
				}
			}, 20*1L);
		} else {
			final Firework firework = p.getWorld().spawn(EnchantUtils.getTableLocation().add(0.5, 0, 0.5), Firework.class);
			
			ItemStack hand = p.getItemInHand().clone();
			hand.setAmount(1);
			
			final Item item = p.getWorld().dropItem(EnchantUtils.getTableLocation().add(0.5, 0, 0.5), hand);
			
			item.setPickupDelay(Integer.MAX_VALUE);
			
			firework.setPassenger(item);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					for (Player all : firework.getWorld().getPlayers()) {
						all.playSound(firework.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
					}
					
					ParticleEffect.SMOKE_NORMAL.display(0.5F, 0.5F, 0.5F, 0.5F, 1000, firework.getLocation(), 32);
					
					item.remove();
					firework.remove();
					
					p.sendMessage("��a�ơ�r ��þƮ�� �����߽��ϴ�.");
				}
			}, 20*1L);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public int getItemAmount(Player p, ItemStack target) {
		int amount = 0;
		
		for (ItemStack item : p.getInventory().getContents()) {
			if (item == null || !item.getType().equals(target.getType())) continue;
			
			if (item.isSimilar(target)) {
				amount += item.clone().getAmount();
			}
		}
		
		return amount;
	}
	
	public boolean hasItem(Player p, ItemStack item, int amount) {
		return getItemAmount(p, item.clone()) >= amount;
	}
	
	public void removeItem(Player p, ItemStack target, int amount) {
		ItemStack[] contents = p.getInventory().getContents().clone();
		
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
		
		p.getInventory().setContents(contents);
	}
}