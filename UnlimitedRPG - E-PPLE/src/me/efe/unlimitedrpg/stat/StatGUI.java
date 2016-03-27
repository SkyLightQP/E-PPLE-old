package me.efe.unlimitedrpg.stat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.efe.efegear.util.Token;
import me.efe.unlimitedrpg.UnlimitedRPG;

/**
 * 
 * 스텟 기능은 1.5.2에서 사용이 금지되고있습니다.
 * 
 * 무단으로 수정하여 사용할 경우 EULA 위반으로 간주됩니다.
 * 
 */

public class StatGUI implements Listener {
	public UnlimitedRPG plugin;
	public Random rand = new Random();
	public List<String> user = new ArrayList<String>();
	
	public StatGUI(UnlimitedRPG plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*4, p.getName()+"님의 스텟");
		
		refresh(inv, p);
		
		p.openInventory(inv);
		user.add(p.getName());
	}
	
	public void refresh(Inventory inv, Player p) {
		int[] stat = StatAPI.getStat(p);
		
		inv.setItem(10, plugin.util.createDisplayItem("§c§l힘", new ItemStack(Material.IRON_SWORD), 
				new String[]{"§oStrength", "공격에 대한 수치.", "공격 추가 데미지에 관여한다.", "", "§9수치 30 이상 효과 : ", "근접 공격시 30% 확률로 적에게 화염 부여"}));
		inv.setItem(12, plugin.util.createDisplayItem("§e§l명중률", new ItemStack(Material.BOW), 
				new String[]{"§oAccuracy", "정확도에 대한 수치.", "발사체 추가 데미지에 관여한다.", "", "§9수치 30 이상 효과 : ", "헤드샷일 경우 데미지 2배"}));
		inv.setItem(14, plugin.util.createDisplayItem("§a§l방어력", new ItemStack(Material.DIAMOND_CHESTPLATE), 
				new String[]{"§oDefensive", "방어에 대한 수치.", "피격 데미지 절감에 관여한다.", "", "§9수치 30 이상 효과 : ", "칼로 방어할 경우 10%의 확률로 데미지 완전히 무시"}));
		inv.setItem(16, plugin.util.createDisplayItem("§b§l손재주", new ItemStack(Material.SHEARS), 
				new String[]{"§oDexterity", "기술에 대한 수치.", "더블 데미지에 관여한다.", "", "§9수치 30 이상 효과 : ", "근접 공격시 10% 확률로 적에게 강한 넉백 부여"}));
		
		inv.setItem(19, plugin.util.createDisplayItem("§8----------------------", new ItemStack(Material.SNOW_BALL, stat[1]), 
				new String[]{"               §l"+stat[1], "§8----------------------", "클릭으로 스텟을 올립니다.", "남은 SP : §l"+stat[0]}));
		inv.setItem(21, plugin.util.createDisplayItem("§8----------------------", new ItemStack(Material.SNOW_BALL, stat[2]), 
				new String[]{"               §l"+stat[2], "§8----------------------", "클릭으로 스텟을 올립니다.", "남은 SP : §l"+stat[0]}));
		inv.setItem(23, plugin.util.createDisplayItem("§8----------------------", new ItemStack(Material.SNOW_BALL, stat[3]), 
				new String[]{"               §l"+stat[3], "§8----------------------", "클릭으로 스텟을 올립니다.", "남은 SP : §l"+stat[0]}));
		inv.setItem(25, plugin.util.createDisplayItem("§8----------------------", new ItemStack(Material.SNOW_BALL, stat[4]), 
				new String[]{"               §l"+stat[4], "§8----------------------", "클릭으로 스텟을 올립니다.", "남은 SP : §l"+stat[0]}));
		
		inv.setItem(27, plugin.util.createDisplayItem("§8§l========================", new ItemStack(Material.ENCHANTED_BOOK), new String[]{
			"              Total Stats", "§8§l========================", 
			"* §cSTR§7        -   §l"+stat[1], 
			"* §eACC§7        -   §l"+stat[2], 
			"* §aDEF§7        -   §l"+stat[3], 
			"* §bDEX§7        -   §l"+stat[4], 
			"                         남은 SP §l["+stat[0]+"]"}));
		
		inv.setItem(35, plugin.util.createDisplayItem("§d§l"+plugin.util.getPluginFullName(), new ItemStack(Material.NAME_TAG), new String[]{"Made by Efe"}));
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!user.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
		
		Player p = (Player) e.getWhoClicked();
		int[] stat = StatAPI.getStat(p);
		
		if (!hasSP(p)) return;
		
		switch (e.getRawSlot()) {
		case 19:
			if (stat[1] >= getMaxStat("str")) return;
			
			StatAPI.editSP(p, -1);
			StatAPI.editSTR(p, 1);
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			break;
		case 21:
			if (stat[2] >= getMaxStat("acc")) return;
			
			StatAPI.editSP(p, -1);
			StatAPI.editACC(p, 1);
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			break;
		case 23:
			if (stat[3] >= getMaxStat("def")) return;

			StatAPI.editSP(p, -1);
			StatAPI.editDEF(p, 1);
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			break;
		case 25:
			if (stat[4] >= getMaxStat("dex")) return;

			StatAPI.editSP(p, -1);
			StatAPI.editDEX(p, 1);
			
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 2.0F);
			break;
		case 35:
			p.playSound(p.getLocation(), Sound.FUSE, 1.0F, 1.0F);
			p.sendMessage(plugin.main+"1.5.2 Out!");
			break;
		}
		
		refresh(e.getInventory(), p);
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (user.contains(e.getPlayer().getName())) {
			user.remove(e.getPlayer().getName());
		}
	}
	
	public double getValue(String name, double damage, Player p) {
		int[] stats = StatAPI.getStat(p);
		
		String data = plugin.getConfig().getString("stat.calculate-data." + name)
				.replace("%Damage%", damage+"")
				.replace("%STR%", stats[1]+"")
				.replace("%ACC%", stats[2]+"")
				.replace("%DEF%", stats[3]+"")
				.replace("%DEX%", stats[4]+"")
				.replace("%Level%", p.getLevel()+"");
		
		while (data.contains("rand(")) {
			data = calculate(data);
		}
		
		return Double.parseDouble(data);
	}
	
	public String calculate(String data) {
		try {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
			
			Pattern pattern = Pattern.compile("rand\\(.*\\)");
			Matcher matcher = pattern.matcher(data);
			
			while (matcher.find()) {
				String group = matcher.group();
				String content = group.substring(5, group.length() - 1);
				StringBuffer buffer = new StringBuffer();
				
				if (content.contains("rand(")) content = calculate(content);
				
				matcher.appendReplacement(buffer, "Math.floor(Math.random() * ("+content+"))");
				matcher.appendTail(buffer);
				
				data = buffer.toString();
			}
			
			return engine.eval(data)+"";
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		return "0";
	}
	
	public int getMaxStat(String name) {
		int limit = plugin.getConfig().getInt("stat.limit."+name);
		
		return limit == -1 ? Integer.MAX_VALUE : limit;
	}
	
	public static boolean hasSP(Player p) {
		return StatAPI.getSP(p) >= 1;
	}
	
	@Deprecated
	public static void setStat(Player p, int[] stats) {
		File f = new File("plugins/UnlimitedRPG/Stats/"+Token.getToken(p)+".txt");
		File folder = new File("plugins/UnlimitedRPG");
		File folder2 = new File("plugins/UnlimitedRPG/Stats");
		
		try {
			if (!f.exists()) {
				folder.mkdir();
				folder2.mkdir();
				f.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.append(stats[0]+"\r\n"+stats[1]+"\r\n"+stats[2]+"\r\n"+stats[3]+"\r\n"+stats[4]);
			bw.flush();
			bw.close();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
	
	@Deprecated
	public static int[] getStat(Player p) {
		File f = new File("plugins/UnlimitedRPG/Stats/"+Token.getToken(p)+".txt");
		File folder = new File("plugins/UnlimitedRPG");
		File folder2 = new File("plugins/UnlimitedRPG/Stats");
		int[] stat = {0, 0, 0, 0, 0};
		
		try {
			if (!f.exists()) {
				folder.mkdir();
				folder2.mkdir();
				f.createNewFile();
				setStat(p, stat);
			}
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			List<Integer> list = new ArrayList<Integer>();
			
			String s;
			while ((s = br.readLine()) != null) {
				list.add(Integer.parseInt(s));
			}
			
			br.close();
			
			for (int i = 0; i < 5; i ++) {
				stat[i] = list.get(i);
			}
			
			return stat;
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
		return stat;
	}
}