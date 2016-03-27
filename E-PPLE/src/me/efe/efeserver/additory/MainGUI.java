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
		Inventory inv = plugin.getServer().createInventory(null, 9*6, "§6▒§r 메인 메뉴");
		
		
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(p.getName());
		skull.setItemMeta(meta);
		
		String[] status = {
				"§aLv."+p.getLevel()+" §2["+((int)(p.getExp() * 100))+"%]", 
				"§a소지금:§2 "+plugin.vault.getBalance(p)+"E", 
				"§c피로도:§4 "+(int) (Fatigue.getFatigue(p) / 5)+"% ["+Fatigue.getFatigue(p)+"/500]", 
				"§b소울:§3 "+new me.efe.efemobs.rudish.UserData(p).getSoul()};
		
		inv.setItem(13, plugin.util.createDisplayItem("§e§l"+p.getName(), skull, status));
		
		
		inv.setItem(0, plugin.util.enchant(plugin.util.createDisplayItem("§aE-PPLE Cafe", new ItemStack(Material.APPLE), 
				new String[]{"E-PPLE의 카페 주소를 확인합니다.", "§8http://cafe.e-pple.kr"}), Enchantment.SILK_TOUCH, 100));
		inv.setItem(8, plugin.util.createDisplayItem("§c닫기", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		
		
		me.efe.skilltree.UserData sData = new me.efe.skilltree.UserData(p);
		
		if (sData.hasLearned("boat.into-the-storm")) {
			if (SkillManager.isDelayed(p, "boat.into-the-storm"))
				inv.setItem(1, plugin.util.createDisplayItem("§b인투 더 스톰", new ItemStack(Material.BOAT), 
						new String[]{"아직 재사용이 불가능합니다."}));
			else
				inv.setItem(1, plugin.util.enchant(plugin.util.createDisplayItem("§b인투 더 스톰", new ItemStack(Material.BOAT), 
						new String[]{"스킬 사용이 가능합니다."}), Enchantment.SILK_TOUCH, 100));
		}
		
		me.efe.efecommunity.UserData cData = new me.efe.efecommunity.UserData(p);
		
		if (!cData.getPosts().isEmpty()) {
			inv.setItem(15, plugin.util.enchant(plugin.util.createDisplayItem("§c/우편함", new ItemStack(Material.STORAGE_MINECART), 
					new String[]{cData.getPosts().size()+"개의 우편물이 존재합니다!"}), Enchantment.SILK_TOUCH, 100));
		}
		
		
		if (sData.getSP() != 0) {
			inv.setItem(28, plugin.util.enchant(plugin.util.createDisplayItem("§c/스킬트리", new ItemStack(Material.BLAZE_POWDER	), 
					new String[]{"스킬트리를 확인하고", "스킬을 업그레이드 합니다.", "", "§9사용하지 않은 SP가 §l"+sData.getSP()+"§9 포인트 있습니다!"}), 
					Enchantment.SILK_TOUCH, 100));
		} else {
			inv.setItem(28, plugin.util.createDisplayItem("§c/스킬트리", new ItemStack(Material.BLAZE_POWDER	), 
					new String[]{"스킬트리를 확인하고", "스킬을 업그레이드 합니다."}));
		}
		
		
		inv.setItem(30, plugin.util.createDisplayItem("§e/퀘스트", new ItemStack(Material.BOOK), 
				new String[]{"시작 가능한 퀘스트와", "진행중인 퀘스트 리스트를 확인합니다."}));
		inv.setItem(32, plugin.util.createDisplayItem("§a/친구", new ItemStack(Material.COOKIE), 
				new String[]{"친구 리스트를 편집하고", "친구에 대한 정보를 확인합니다."}));
		
		if (UnlimitedRPG.getInstance().party.requests.containsKey(Token.getToken(p))) {
			inv.setItem(34, plugin.util.enchant(plugin.util.createDisplayItem("§b/파티", new ItemStack(Material.CAKE), 
					new String[]{"새로운 파티를 개설하거나", "가입한 파티에 대해 설정합니다.", "", "파티의 초대에 응해주세요!"}), 
					Enchantment.SILK_TOUCH, 100));
		} else {
			inv.setItem(34, plugin.util.createDisplayItem("§b/파티", new ItemStack(Material.CAKE), 
					new String[]{"새로운 파티를 개설하거나", "가입한 파티에 대해 설정합니다."}));
		}
		
		inv.setItem(38, plugin.util.createDisplayItem("§9/섬", new ItemStack(Material.SAND), 
				new String[]{"자신의 섬으로 이동하거나", "섬의 옵션을 설정합니다."}));
		inv.setItem(40, plugin.util.createDisplayItem("§d/코인", new ItemStack(Material.DIAMOND), 
				new String[]{"코인샵을 이용하거나", "후원 기능을 설정합니다.", "", "§9"+CashAPI.getBalance(p)+" 코인 소지중"}));
		inv.setItem(42, plugin.util.createDisplayItem("§5/설정", new ItemStack(Material.REDSTONE), 
				new String[]{"다양한 시스템에 대해", "설정합니다."}));
		
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
			
			new FancyMessage("§a▒§r ")
				.then("§8§l>§7§l>§r E-PPLE 카페 바로가기")
					.link("http://cafe.e-pple.kr")
					.tooltip("§bhttp://cafe.e-pple.kr")
				.send(p);
			
			break;
		case 1:
			if (SkillManager.isDelayed(p, "boat.into-the-storm")) return;
			
			EfeMobs efeMobs = (EfeMobs) plugin.getServer().getPluginManager().getPlugin("EfeMobs");
			
			if (p.getWorld().equals(plugin.worldIsl) || efeMobs.bossListener.isBossRoom(p.getLocation())) {
				p.sendMessage("§c▒§r 이곳에선 이용할 수 없습니다.");
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
			
			plugin.getServer().dispatchCommand(p, "우편함");
			break;
		case 28:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "스킬트리");
			break;
		case 30:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "퀘스트");
			break;
		case 32:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "친구");
			break;
		case 34:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "파티");
			break;
		case 38:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "섬");
			break;
		case 40:
			p.closeInventory();
			plugin.getServer().dispatchCommand(p, "코인");
			break;
		case 42:
			p.closeInventory();
			
			plugin.getServer().dispatchCommand(p, "설정");
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