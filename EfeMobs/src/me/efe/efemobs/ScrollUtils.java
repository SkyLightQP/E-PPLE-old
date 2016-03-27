package me.efe.efemobs;

import java.util.ArrayList;
import java.util.List;

import me.efe.unlimitedrpg.unlimitedtag.TagType;
import me.efe.unlimitedrpg.unlimitedtag.UnlimitedTagAPI;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class ScrollUtils {
	private static List<Location> pointUser = new ArrayList<Location>();
	private static List<Location> pointBoss = new ArrayList<Location>();
	private static List<String> bossName = new ArrayList<String>();
	private static List<String> bossSubtitle = new ArrayList<String>();
	
	public static void init(EfeMobs plugin) {
		World world = plugin.epple.world;
		
		pointUser.add(new Location(world, 3022, 192, 2433)); //0
		pointBoss.add(new Location(world, 3022, 192, 2546)); //0
		
		pointUser.add(new Location(world, 2956, 192, 2433)); //1
		pointBoss.add(new Location(world, 2956, 192, 2546)); //1
		
		pointUser.add(new Location(world, 2890, 192, 2433)); //2
		pointBoss.add(new Location(world, 2890, 192, 2546)); //2
		
		pointUser.add(new Location(world, 2824, 192, 2433)); //3
		pointBoss.add(new Location(world, 2824, 192, 2546)); //3
		
		pointUser.add(new Location(world, 2758, 192, 2433)); //4
		pointBoss.add(new Location(world, 2758, 192, 2546)); //4
		
		pointUser.add(new Location(world, 2692, 192, 2433)); //5
		pointBoss.add(new Location(world, 2692, 192, 2546)); //5
		
		pointUser.add(new Location(world, 2626, 192, 2433)); //6
		pointBoss.add(new Location(world, 2626, 192, 2546)); //6
		
		pointUser.add(new Location(world, 2560, 192, 2433)); //7
		pointBoss.add(new Location(world, 2560, 192, 2546)); //7
		
		pointUser.add(new Location(world, 2494, 192, 2433)); //8
		pointBoss.add(new Location(world, 2494, 192, 2546)); //8
		
		pointUser.add(new Location(world, 2428, 192, 2433)); //9
		pointBoss.add(new Location(world, 2428, 192, 2546)); //9
		
		pointUser.add(new Location(world, 2362, 192, 2433)); //10
		pointBoss.add(new Location(world, 2362, 192, 2546)); //10
		
		pointUser.add(new Location(world, 2296, 192, 2433)); //11
		pointBoss.add(new Location(world, 2296, 192, 2546)); //11
		
		pointUser.add(new Location(world, 2230, 192, 2433)); //12
		pointBoss.add(new Location(world, 2230, 192, 2546)); //12
		
		pointUser.add(new Location(world, 2164, 192, 2433)); //13
		pointBoss.add(new Location(world, 2164, 192, 2546)); //13
		
		pointUser.add(new Location(world, 2098, 192, 2433)); //14
		pointBoss.add(new Location(world, 2098, 192, 2546)); //14
		
		
		bossName.add("Plaguer");
		bossSubtitle.add("�÷���");
		
		bossName.add("Laughter");
		bossSubtitle.add("������");
		
		bossName.add("Alchemist");
		bossSubtitle.add("��ĳ�̽�Ʈ");
		
		bossName.add("Yowler");
		bossSubtitle.add("�߿﷯");
		
		bossName.add("Waff");
		bossSubtitle.add("����");
		
		bossName.add("Igniter");
		bossSubtitle.add("�̱׳�����");
		
		bossName.add("Tragedy");
		bossSubtitle.add("Ʈ������");
		
		bossName.add("Hellraiser");
		bossSubtitle.add("�ﷹ����");
		
		bossName.add("Menace");
		bossSubtitle.add("�޳ʽ�");
		
		bossName.add("Enormous");
		bossSubtitle.add("�̳�ӽ�");
	}
	
	public static Location getUserSpawn(int channel) {
		return pointUser.get(channel);
	}
	
	public static Location getBossSpawn(int channel) {
		return pointBoss.get(channel);
	}
	
	public static String getBossName(int floor) {
		return bossName.get(floor - 1);
	}
	
	public static String getBossSubtitle(int floor) {
		return bossSubtitle.get(floor - 1);
	}
	
	public static ItemStack getScroll(int floor) {
		return item("��c���� ��ũ�ѡ�4: "+floor+"F", new ItemStack(Material.MAP, 1, (short) floor));
	}
	
	public static boolean isScroll(ItemStack item) {
		if (!item.hasItemMeta()) return false;
		if (!item.getItemMeta().hasDisplayName()) return false;
		
		return item.getItemMeta().getDisplayName().startsWith("��c���� ��ũ�ѡ�4: ");
	}
	
	public static int getFloor(ItemStack item) {
		return Integer.parseInt(item.getItemMeta().getDisplayName().replace("��c���� ��ũ�ѡ�4: ", "").replace("F", ""));
	}
	
	private static ItemStack item(String name, ItemStack item) {
		MapMeta meta = (MapMeta) item.getItemMeta();
		
		List<String> lore = new ArrayList<String>();
		
		lore.add("��7��Ŭ���ϸ� �����մϴ�.");
		lore.add("");
		lore.add("��8������ �����ϼ���.");
		lore.add("��8�׵��� ���� ���� ��밡 �ƴմϴ�.");
		
		meta.setDisplayName(name);
		meta.setLore(lore);
		meta.setScaling(false);
		item.setItemMeta(meta);
		
		UnlimitedTagAPI.addTag(item, TagType.VEST_ON_PICKUP, null);
		
		return item;
	}
}