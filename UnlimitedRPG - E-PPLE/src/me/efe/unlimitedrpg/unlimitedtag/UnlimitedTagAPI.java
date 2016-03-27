package me.efe.unlimitedrpg.unlimitedtag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.efe.efegear.util.Token;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UnlimitedTagAPI {
	
	public static void addTag(ItemStack item, TagType tag, String data) {
		List<String> lore = new ArrayList<String>();
		
		if (hasTag(item, tag)) return;
		
		for (TagType type : TagType.linked()) {
			if (type == tag) {
				if (tag.hasData()) lore.add(tag.toCode(data));
				else lore.add(tag.toCode());
				continue;
			}
			
			if (hasTag(item, type)) {
				if (type.hasData()) lore.add(type.toCode(getData(item, type)));
				else lore.add(type.toCode());
			}
		}
		
		for (TagType type : TagType.values()) {
			removeTag(item, type);
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if (meta.hasLore()) {
			lore.addAll(meta.getLore());
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static void removeTag(ItemStack item, TagType tag) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		if (!meta.hasLore()) return;
		
		if (tag.hasData()) {
			for (String cont : meta.getLore()) {
				if (!cont.startsWith(tag.toCode()+"§:")) {
					lore.add(cont);
				}
			}
		} else {
			for (String cont : meta.getLore()) {
				if (!cont.equals(tag.toCode()) && !cont.startsWith(tag.toCode()+"§|")) {
					lore.add(cont);
				}
			}
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static boolean hasTag(ItemStack item, TagType tag) {
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return false;
		
		if (tag.hasData()) {
			for (String cont : item.getItemMeta().getLore()) {
				if (cont.startsWith(tag.toCode()+"§:")) {
					return true;
				}
			}
		} else {
			for (String cont : item.getItemMeta().getLore()) {
				if (cont.equals(tag.toCode()) || cont.startsWith(tag.toCode()+"§|")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static String getData(ItemStack item, TagType tag) {
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return null;
		
		for (String cont : item.getItemMeta().getLore()) {
			if (cont.startsWith(tag.toCode()+"§:")) {
				return cont.split("§\\|")[0].replaceAll("§", "").split(":")[1];
			}
		}
		
		return null;
	}
	
	public static boolean isAvailable(Player p, ItemStack item, TagType tag) {
		if (!hasTag(item, tag)) return true;
		
		if (tag.hasData()) {
			switch (tag) {
			case VESTED: 
				return getData(item, tag).equals(Token.getToken(p));
			case REQUIRE_LV:
				return (p.getLevel() >= Integer.parseInt(getData(item, tag)));
			case DEADLINE:
				return !Deadline.isOverdue(item);
			case PERMISSION:
				return p.hasPermission(getData(item, tag));
			default:
				return true;
			}
		} else {
			return false;
		}
	}
	
	public static class Deadline {
		private final static SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH mm");
		
		public static String getDeadline(ItemStack item) {
			Date date = Calendar.getInstance().getTime();
			CalendarData data = CalendarData.parse(getData(item, TagType.DEADLINE_ON_PICKUP));
			
			return format.format(data.after(date));
		}
		
		public static boolean isOverdue(ItemStack item) {
			try {
				Date limit = format.parse(getData(item, TagType.DEADLINE));
				Date now = Calendar.getInstance().getTime();
				
				return ((limit.getTime() - now.getTime()) <= 0);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return true;
		}
		
		public static String getDisplay(TagType type, String data) {
			try {
				if (type.equals(TagType.DEADLINE)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분까지 사용 가능");
					Date date = format.parse(data);
					
					return sdf.format(date);
				} else if (type.equals(TagType.DEADLINE_ON_PICKUP)) {
					CalendarData cal = CalendarData.parse(data);
					
					String value = "획득 후 ";
					
					if (cal.getYear() != 0) value += cal.getYear()+"년 ";
					if (cal.getMonth() != 0) value += cal.getMonth()+"개월 ";
					if (cal.getDay() != 0) value += cal.getDay()+"일 ";
					if (cal.getHour() != 0) value += cal.getHour()+"시간 ";
					if (cal.getMinute() != 0) value += cal.getMinute()+"분 ";
					
					value += "뒤까지 사용 가능";
					
					return value;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}
}