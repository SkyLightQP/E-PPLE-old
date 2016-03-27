package me.efe.efeserver.additory;

import java.util.ArrayList;
import java.util.List;

import me.efe.efegear.util.Token;
import me.efe.efemobs.EfeMobs;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.reform.Fatigue;
import me.efe.efeserver.util.CashAPI;
import me.efe.skilltree.SkillManager;
import me.efe.skilltree.SkillTree;
import me.efe.unlimitedrpg.UnlimitedRPG;
import me.efe.unlimitedrpg.playerinfo.PlayerInfo;
import mkremins.fanciful.FancyMessage;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class MainGUI implements Listener {
	public EfeServer plugin;
	public List<String> users = new ArrayList<String>();
	
	public MainGUI(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*6, "��6�ơ�r ���� �޴�");
		
		
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(p.getName());
		skull.setItemMeta(meta);
		
		String[] status = {
				"��aLv."+p.getLevel()+" ��2["+((int)(p.getExp() * 100))+"%]", 
				"��a������:��2 "+plugin.vault.getBalance(p)+"E", 
				"��c�Ƿε�:��4 "+(int) (Fatigue.getFatigue(p) / 5)+"% ["+Fatigue.getFatigue(p)+"/500]", 
				"��b�ҿ�:��3 "+new me.efe.efemobs.rudish.UserData(p).getSoul()};
		
		inv.setItem(13, plugin.util.createDisplayItem("��e��l"+p.getName(), skull, status));
		
		
		inv.setItem(0, plugin.util.enchant(plugin.util.createDisplayItem("��aE-PPLE Cafe", new ItemStack(Material.APPLE), 
				new String[]{"E-PPLE�� ī�� �ּҸ� Ȯ���մϴ�.", "��8http://cafe.e-pple.kr"}), Enchantment.SILK_TOUCH, 100));
		inv.setItem(8, plugin.util.createDisplayItem("��c�ݱ�", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		
		
		me.efe.skilltree.UserData sData = new me.efe.skilltree.UserData(p);
		
		if (sData.hasLearned("boat.into-the-storm")) {
			if (SkillManager.isDelayed(p, "boat.into-the-storm"))
				inv.setItem(1, plugin.util.createDisplayItem("��b���� �� ����", new ItemStack(Material.BOAT), 
						new String[]{"���� ������ �Ұ����մϴ�."}));
			else
				inv.setItem(1, plugin.util.enchant(plugin.util.createDisplayItem("��b���� �� ����", new ItemStack(Material.BOAT), 
						new String[]{"��ų ����� �����մϴ�."}), Enchantment.SILK_TOUCH, 100));
		}
		
		me.efe.efecommunity.UserData cData = new me.efe.efecommunity.UserData(p);
		
		if (!cData.getPosts().isEmpty()) {
			inv.setItem(15, plugin.util.enchant(plugin.util.createDisplayItem("��c/������", new ItemStack(Material.STORAGE_MINECART), 
					new String[]{cData.getPosts().size()+"���� ������ �����մϴ�!"}), Enchantment.SILK_TOUCH, 100));
		}
		
		
		if (sData.getSP() != 0) {
			inv.setItem(28, plugin.util.enchant(plugin.util.createDisplayItem("��c/��ųƮ��", new ItemStack(Material.BLAZE_POWDER	), 
					new String[]{"��ųƮ���� Ȯ���ϰ�", "��ų�� ���׷��̵� �մϴ�.", "", "��9������� ���� SP�� ��l"+sData.getSP()+"��9 ����Ʈ �ֽ��ϴ�!"}), 
					Enchantment.SILK_TOUCH, 100));
		} else {
			inv.setItem(28, plugin.util.createDisplayItem("��c/��ųƮ��", new ItemStack(Material.BLAZE_POWDER	), 
					new String[]{"��ųƮ���� Ȯ���ϰ�", "��ų�� ���׷��̵� �մϴ�."}));
		}
		
		
		inv.setItem(30, plugin.util.createDisplayItem("��e/����Ʈ", new ItemStack(Material.BOOK), 
				new String[]{"���� ������ ����Ʈ��", "�������� ����Ʈ ����Ʈ�� Ȯ���մϴ�."}));
		inv.setItem(32, plugin.util.createDisplayItem("��a/ģ��", new ItemStack(Material.COOKIE), 
				new String[]{"ģ�� ����Ʈ�� �����ϰ�", "ģ���� ���� ������ Ȯ���մϴ�."}));
		
		if (UnlimitedRPG.getInstance().party.requests.containsKey(Token.getToken(p))) {
			inv.setItem(34, plugin.util.enchant(plugin.util.createDisplayItem("��b/��Ƽ", new ItemStack(Material.CAKE), 
					new String[]{"���ο� ��Ƽ�� �����ϰų�", "������ ��Ƽ�� ���� �����մϴ�.", "", "��Ƽ�� �ʴ뿡 �����ּ���!"}), 
					Enchantment.SILK_TOUCH, 100));
		} else {
			inv.setItem(34, plugin.util.createDisplayItem("��b/��Ƽ", new ItemStack(Material.CAKE), 
					new String[]{"���ο� ��Ƽ�� �����ϰų�", "������ ��Ƽ�� ���� �����մϴ�."}));
		}
		
		inv.setItem(38, plugin.util.createDisplayItem("��9/��", new ItemStack(Material.SAND), 
				new String[]{"�ڽ��� ������ �̵��ϰų�", "���� �ɼ��� �����մϴ�."}));
		inv.setItem(40, plugin.util.createDisplayItem("��d/����", new ItemStack(Material.DIAMOND), 
				new String[]{"���μ��� �̿��ϰų�", "�Ŀ� ����� �����մϴ�.", "", "��9"+CashAPI.getBalance(p)+" ���� ������"}));
		inv.setItem(42, plugin.util.createDisplayItem("��5/����", new ItemStack(Material.REDSTONE), 
				new String[]{"�پ��� �ý��ۿ� ����", "�����մϴ�."}));
		
		p.openInventory(inv);
		users.add(p.getName());
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 9*6) return;
		
		Player p = (Player) e.getWhoClicked();
		
		switch (e.getRawSlot()) {
		case 0:
			p.closeInventory();
			
			new FancyMessage("��a�ơ�r ")
				.then("��8��l>��7��l>��r E-PPLE ī�� �ٷΰ���")
					.link("http://cafe.e-pple.kr")
					.tooltip("��bhttp://cafe.e-pple.kr")
				.send(p);
			
			break;
		case 1:
			if (SkillManager.isDelayed(p, "boat.into-the-storm")) return;
			
			EfeMobs efeMobs = (EfeMobs) plugin.getServer().getPluginManager().getPlugin("EfeMobs");
			
			if (p.getWorld().equals(plugin.worldIsl) || efeMobs.bossListener.isBossRoom(p.getLocation())) {
				p.sendMessage("��c�ơ�r �̰����� �̿��� �� �����ϴ�.");
				return;
			}
			
			p.closeInventory();
			
			SkillTree.getInstance().getTeleportGUI().openGUI(p);
			break;
		case 8:
			p.closeInventory();
			break;
		case 13:
			p.closeInventory();
			
			PlayerInfo.openGUI(p, p);
			break;
		case 15:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "������");
			break;
		case 28:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "��ųƮ��");
			break;
		case 30:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "����Ʈ");
			break;
		case 32:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "ģ��");
			break;
		case 34:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "��Ƽ");
			break;
		case 38:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "��");
			break;
		case 40:
			p.closeInventory();
			plugin.getServer().dispatchCommand(p, "����");
			break;
		case 42:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "����");
			break;
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}