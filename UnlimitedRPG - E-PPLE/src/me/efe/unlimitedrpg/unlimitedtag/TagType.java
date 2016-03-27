package me.efe.unlimitedrpg.unlimitedtag;

import java.util.Arrays;
import java.util.Iterator;

public enum TagType {
	VESTED(true, "�ͼ�"),
	VEST_ON_PICKUP(false, "ȹ��ñͼ�"),
	REQUIRE_LV(true, "�ʿ䷹��"),
	LOCKED(false, "���"),
	STAMPED(false, "����"),
	DEADLINE(true, "�Ⱓ��"),
	DEADLINE_ON_PICKUP(true, "ȹ���ıⰣ��"),
	PERMISSION(true, "����"),
	SPATIAL(true, "����"),
	ICON(false, "������"),
	NO_CRAFT(false, "���պҰ�");
	
	private final boolean hasData;
	private final String name;
	
	private TagType(boolean hasData, String name) {
		this.hasData = hasData;
		this.name = name;
	}
	
	public boolean hasData() {
		return hasData;
	}
	
	public static TagType fromString(String str) {
		for (TagType tag : values()) {
			if (tag.toString().equals(str)) {
				return tag;
			}
		}
		
		return null;
	}
	
	public String toString() {
		return name;
	}
	
	public String toCode() {
		String value = "";
		
		for (char c : name().toCharArray()) {
			value += "��" + c;
		}
		
		return value;
	}
	
	public String toCode(String data) {
		String value = "";
		
		for (char c : (name() + ":" + data).toCharArray()) {
			value += "��" + c;
		}
		
		return value;
	}
	
	public static String getTypes() {
		String value = "��7";
		
		Iterator<TagType> it = Arrays.asList(values()).iterator();
		while (it.hasNext()) {
			value += it.next().toString();
			if (it.hasNext()) {
				value += "��8, ��7";
			}
		}
		
		return value;
	}
	
	public static TagType[] linked() {
		return new TagType[] {
			TagType.DEADLINE,
			TagType.DEADLINE_ON_PICKUP,
			TagType.ICON,
			TagType.NO_CRAFT,
			TagType.PERMISSION,
			TagType.VESTED,
			TagType.VEST_ON_PICKUP,
			TagType.LOCKED,
			TagType.STAMPED,
			TagType.SPATIAL,
			TagType.REQUIRE_LV
		};
	}
}