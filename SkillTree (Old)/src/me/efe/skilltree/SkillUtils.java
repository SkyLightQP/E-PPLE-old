package me.efe.skilltree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class SkillUtils {
	private static SkillTree plugin;
	public static HashMap<SkillType, List<Skill>> skillMap = new HashMap<SkillType, List<Skill>>();
	private static HashMap<String, Skill> skills = new HashMap<String, Skill>();
	
	public static void init(SkillTree pl) {
		plugin = pl;
		
		/** ==================================================================================================================================================== **/
		
		
		skillMap.put(SkillType.HUNT, new ArrayList<Skill>());
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.weakness", 6, 0)
		.setEffects(new String[] {
				"��9������ ��n�� 10�ʡ�9 �ο�", 
				"��9������ ��n�� 15�ʡ�9 �ο�", 
				"��9������ ��n�� 10�ʡ�9 �ο�", 
				"��9������ ��n�� 15�ʡ�9 �ο�", 
				"��9������ ��n�� 10�ʡ�9 �ο�", 
				"��9������ ��n�� 15�ʡ�9 �ο�"})
		.setIcon(plugin.util.createDisplayItem("��ũ�Ͻ�", new ItemStack(Material.STONE_SWORD), new String[]{
			"��8[��eWeakness��8 | ��bPassive��8 | ��dMax Lv.6��8]", "", "�� ���ݽ� 30% Ȯ����", "������ �������� �ο��Ѵ�."}), 12)
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.binding", 6, 0)
		.setEffects(new String[] {
				"��9��n25%��9 Ȯ���� ����", 
				"��9��n40%��9 Ȯ���� ����", 
				"��9��n55%��9 Ȯ���� ����", 
				"��9��n70%��9 Ȯ���� ����", 
				"��9��n85%��9 Ȯ���� ����", 
				"��9��n100%��9 Ȯ���� ����"})
		.setIcon(plugin.util.createDisplayItem("���ε�", new ItemStack(Material.IRON_SWORD), new String[]{
			"��8[��eBinding��8 | ��cActive��8 | ��dMax Lv.6��8]", "", "���� ���� �޼ҿ� �����Ͽ�", "2�ʰ� ���� �Ҵ� ���·� �����.", "��b�� �� ��� ũ��Ƽ�� ��Ʈ"}), 20)
		.setNeedSkill("hunt.loot-bonus")
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.guard", 6, 0)
		.setEffects(new String[] {
				"��9������ ��n10%��9 ����", 
				"��9������ ��n15%��9 ����", 
				"��9������ ��n20%��9 ����", 
				"��9������ ��n25%��9 ����", 
				"��9������ ��n30%��9 ����", 
				"��9������ ��n35%��9 ����"})
		.setIcon(plugin.util.createDisplayItem("����", new ItemStack(Material.CHAINMAIL_CHESTPLATE), new String[]{
			"��8[��eGuard��8 | ��bPassive��8 | ��dMax Lv.6��8]", "", "��ũ�� ���¿��� �߻�ü ��������", "������ ���ҽ�Ų��."}), 13)
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.angelic-bless", 6, 0)
		.setEffects(new String[] {
				"��9��n�����ԡ�9 ����", 
				"��9��n������, ����9 ����", 
				"��9��n������, ��, ���ӡ�9 ����", 
				"��9��n������, ��, ����, ����9 ����", 
				"��9��n������, ��, ����, ���, �Ǹ��9 ����", 
				"��9��n������, ��, ����, ���, �Ǹ�, �ֹ̡�9 ����"})
		.setIcon(plugin.util.createDisplayItem("������ ����", new ItemStack(Material.IRON_CHESTPLATE), new String[]{
			"��8[��eAngelic Bless��8 | ��cActive��8 | ��dMax Lv.6��8]", "", "�ڽŰ� 4�� �̳� �÷��̾���", "Ư�� ������� �����Ѵ�.",
			"��b�� ���� Shift�� ����", "��b�� ������ 3��"}), 22)
		.setNeedSkill("hunt.loot-bonus")
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.slowdown", 6, 0)
		.setEffects(new String[] {
				"��9���� ��n�� 1.5�ʡ�9 �ο�", 
				"��9���� ��n�� 2�ʡ�9 �ο�", 
				"��9���� ��n�� 2�ʡ�9 �ο�", 
				"��9���� ��n�� 2.5�ʡ�9 �ο�", 
				"��9���� ��n�� 2.5�ʡ�9 �ο�", 
				"��9���� ��n�� 3�ʡ�9 �ο�"})
		.setIcon(plugin.util.createDisplayItem("���ο� �ٿ�", new ItemStack(Material.BOW), new String[]{
			"��8[��e��eSlowdown��8 | ��bPassive��8 | ��dMax Lv.6��8]", "", "�ǰݴ��� ������ ���� ȿ���� �ο��Ѵ�."}), 14)
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.multiple-shot", 6, 0)
		.setEffects(new String[] {
				"��9��n2�ߡ�9 ���� ���", 
				"��9��n3�ߡ�9 ���� ���", 
				"��9��n4�ߡ�9 ���� ���", 
				"��9��n5�ߡ�9 ���� ���", 
				"��9��n6�ߡ�9 ���� ���", 
				"��9��n7�ߡ�9 ���� ���"})
		.setIcon(plugin.util.createDisplayItem("��Ƽ�� ��", new ItemStack(Material.BOW), new String[]{
			"��8[��eMultiple Shot��8 | ��cActive��8 | ��dMax Lv.6��8]", "", "�� ���� ���� ȭ���� ���������� �߻��Ѵ�.", "ȭ���� �߰��� �Ҹ����� �ʴ´�.", "��b�� Shift + ������� ����"}), 24)
		.setNeedSkill("hunt.loot-bonus")
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.loot-bonus", 1, 0)
		.setEffects(new String[] {
				"��9����ǰ ��� Ȯ�� ����"})
		.setIcon(plugin.util.createDisplayItem("��Ʈ ���ʽ�", new ItemStack(Material.CHEST), new String[]{
			"��8[��eLoot Bonus��8 | ��bPassive��8 | ��dMax Lv.1��8]", "", "����ǰ�� ��� Ȯ���� ���� ��ȭ�Ѵ�."}), 17)
		.setNeedSkills("hunt.weakness", "hunt.guard", "hunt.slowdown")
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.bounty-hunting", 1, 0)
		.setEffects(new String[] {
				"��9���� ���� ������ Ȯ�� ����"})
		.setIcon(plugin.util.createDisplayItem("�ٿ�Ƽ ����", new ItemStack(Material.ENDER_CHEST), new String[]{
			"��8[��eAlchemy��8 | ��bPassive��8 | ��dMax Lv.1��8]", "", "���� ������ ���� ���� ��ȭ�Ѵ�."}), 26)
		.setNeedSkills("hunt.binding", "hunt.angelic-bless", "hunt.multiple-shot")
		);
		
		
		/** ==================================================================================================================================================== **/
		
		
		skillMap.put(SkillType.MINE, new ArrayList<Skill>());
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.minerals-mastery", 9, 0)
		.setEffects(new String[] {
				"��9���� ����� ��n100��", 
				"��9���� ����� ��n95��", 
				"��9���� ����� ��n90��", 
				"��9���� ����� ��n85��", 
				"��9���� ����� ��n80��", 
				"��9���� ����� ��n75��", 
				"��9���� ����� ��n70��", 
				"��9���� ����� ��n65��", 
				"��9���� ����� ��n60��"})
		.setIcon(plugin.util.createDisplayItem("�̳׶� �����͸�", new ItemStack(Material.IRON_PICKAXE), new String[]{
			"��8[��eMinerals Mastery��8 | ��bPassive��8 | ��dMax Lv.9��8]", "", "������ ����� �ð��� �����Ѵ�."}), 14)
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.smeltery", 9, 0)
		.setEffects(new String[] {
				"��9��n1%��9�� Ȯ���� �ߵ�", 
				"��9��n2%��9�� Ȯ���� �ߵ�",
				"��9��n3%��9�� Ȯ���� �ߵ�",
				"��9��n4%��9�� Ȯ���� �ߵ�",
				"��9��n5%��9�� Ȯ���� �ߵ�",
				"��9��n6%��9�� Ȯ���� �ߵ�",
				"��9��n7%��9�� Ȯ���� �ߵ�",
				"��9��n8%��9�� Ȯ���� �ߵ�",
				"��9��n9%��9�� Ȯ���� �ߵ�"})
		.setIcon(plugin.util.createDisplayItem("�����͸�", new ItemStack(Material.GOLD_PICKAXE), new String[]{
			"��8[��eSmeltery��8 | ��bPassive��8 | ��dMax Lv.9��8]", "", "ä���� ���� Ȯ���� ������", "���õǾ� �ֱ��� ����ȴ�."}), 22)
		.setNeedSkill("mine.minerals-mastery")
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.steelmaking", 1, 0)
		.setEffects(new String[] {
				"��9��ö ���� ����"})
		.setIcon(plugin.util.createDisplayItem("����", new ItemStack(Material.IRON_INGOT), new String[]{
			"��8[��eSteelmaking��8 | ��bPassive��8 | ��dMax Lv.1��8]", "", "ö�� �̿��� ��ö�� ���� �� �ִ�.", "ö���� ������ ������ �� �ִ�."}), 30)
		.setNeedSkill("mine.smeltery")
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.dynamite", 9, 0)
		.setEffects(new String[] {
				"��9�Ϲ� TNT ��n0.4���9 ������ ����", 
				"��9�Ϲ� TNT ��n0.6���9 ������ ����", 
				"��9�Ϲ� TNT ��n0.8���9 ������ ����", 
				"��9�Ϲ� TNT ��n1.0���9 ������ ����", 
				"��9�Ϲ� TNT ��n1.2���9 ������ ����", 
				"��9�Ϲ� TNT ��n1.4���9 ������ ����", 
				"��9�Ϲ� TNT ��n1.6���9 ������ ����", 
				"��9�Ϲ� TNT ��n1.8���9 ������ ����", 
				"��9�Ϲ� TNT ��n2.0���9 ������ ����"})
		.setIcon(plugin.util.createDisplayItem("���̳ʸ���Ʈ", new ItemStack(Material.EXPLOSIVE_MINECART), new String[]{
			"��8[��eDynamite��8 | ��cActive��8 | ��dMax Lv.9��8]", "", "���� ��Ʈ���� TNT�� �߻��Ѵ�.", "�÷��̾�� �������� ������ �ʴ´�.", "��, TNT�� 1���� �Ҹ��Ѵ�.", 
			"��b�� TNT�� ��� ��Ŭ���ؼ� ����", "��b�� ��� 2.5ĭ �Ҹ�"}), 31)
		.setNeedSkill("mine.steelmaking")
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.diamond-rush", 9, 0)
		.setEffects(new String[] {
				"��n10�ʡ�9�� ����", 
				"��n15�ʡ�9�� ����", 
				"��n20�ʡ�9�� ����", 
				"��n25�ʡ�9�� ����", 
				"��n30�ʡ�9�� ����", 
				"��n35�ʡ�9�� ����", 
				"��n40�ʡ�9�� ����", 
				"��n45�ʡ�9�� ����", 
				"��n50�ʡ�9�� ����"})
		.setIcon(plugin.util.createDisplayItem("���̾Ƹ�� ����", new ItemStack(Material.DIAMOND_PICKAXE), new String[]{
			"��8[��eDiamond Rush��8 | ��cActive��8 | ��dMax Lv.9��8]", "", "���� �ð����� û�ݼ��� ��, ���̾Ƹ�带", "�߰��� �� �ִ�.", 
			"��b�� ��� ��Ŭ��-��Ŭ��-��Ŭ������ ����", "��b�� ��� 5ĭ �Ҹ�"}), 32)
		.setNeedSkill("mine.dynamite")
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.enchanting", 1, 0)
		.setEffects(new String[] {
				"��9��� ��þƮ ����"})
		.setIcon(plugin.util.createDisplayItem("��þ��", new ItemStack(Material.ENCHANTED_BOOK), new String[]{
			"��8[��eEnchanting��8 | ��bPassive��8 | ��dMax Lv.1��8]", "", "Polaris ���� ���� �ο��뿡��", "��� ��þƮ �� �� �ִ�."}), 33)
		.setNeedSkill("mine.diamond-rush")
		);
		
		
		/** ==================================================================================================================================================== **/
		
		
		skillMap.put(SkillType.FARM, new ArrayList<Skill>());
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.carrot", 6, 0)
		.setEffects(new String[] {
				"��9���� ��n-20��9, ��n20�С�9 �ҿ�", 
				"��9���� ��n-20��9, ��n18�С�9 �ҿ�", 
				"��9���� ��n-20��9, ��n16�С�9 �ҿ�", 
				"��9���� ��n-15��9, ��n14�С�9 �ҿ�", 
				"��9���� ��n-15��9, ��n12�С�9 �ҿ�", 
				"��9���� ��n-15��9, ��n10�С�9 �ҿ�"})
		.setIcon(plugin.util.createDisplayItem("���", new ItemStack(Material.CARROT_ITEM), new String[]{
			"��8[��eCarrot��8 | ��bPassive��8 | ��dMax Lv.6��8]", "", "����� ����� �� �ִ�."}), 30)
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.potato", 6, 0)
		.setEffects(new String[] {
				"��9���� ��n-20��9, ��n20�С�9 �ҿ�", 
				"��9���� ��n-20��9, ��n18�С�9 �ҿ�", 
				"��9���� ��n-20��9, ��n16�С�9 �ҿ�", 
				"��9���� ��n-15��9, ��n14�С�9 �ҿ�", 
				"��9���� ��n-15��9, ��n12�С�9 �ҿ�", 
				"��9���� ��n-15��9, ��n10�С�9 �ҿ�"})
		.setIcon(plugin.util.createDisplayItem("����", new ItemStack(Material.POTATO_ITEM), new String[]{
			"��8[��ePotato��8 | ��bPassive��8 | ��dMax Lv.6��8]", "", "���ڸ� ����� �� �ִ�."}), 31)
		.setNeedSkill("farm.carrot")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.wheat", 6, 0)
		.setEffects(new String[] {
				"��9���� ��n-30��9, ��n20�С�9 �ҿ�", 
				"��9���� ��n-30��9, ��n18�С�9 �ҿ�", 
				"��9���� ��n-30��9, ��n16�С�9 �ҿ�", 
				"��9���� ��n-25��9, ��n14�С�9 �ҿ�", 
				"��9���� ��n-25��9, ��n12�С�9 �ҿ�", 
				"��9���� ��n-25��9, ��n10�С�9 �ҿ�"})
		.setIcon(plugin.util.createDisplayItem("��", new ItemStack(Material.WHEAT), new String[]{
			"��8[��eWheat��8 | ��bPassive��8 | ��dMax Lv.6��8]", "", "���� ����� �� �ִ�."}), 32)
			.setNeedSkill("farm.potato")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.diligence", 1, 0)
		.setEffects(new String[] {
				"��9���� ���� ����, �� 2�� ���� ����"})
		.setIcon(plugin.util.createDisplayItem("�ٸ�", new ItemStack(Material.IRON_HOE), new String[]{
			"��8[��eDiligence��8 | ��bPassive��8 | ��dMax Lv.1��8]", "", "����� �⺻�� �ٸ����� �⸥��."}), 23)
			.setNeedSkill("farm.wheat")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.melon", 6, 0)
		.setEffects(new String[] {
				"��9���� ��n-80��9, ��n30�С�9 �ҿ�", 
				"��9���� ��n-80��9, ��n28�С�9 �ҿ�", 
				"��9���� ��n-80��9, ��n26�С�9 �ҿ�", 
				"��9���� ��n-70��9, ��n24�С�9 �ҿ�", 
				"��9���� ��n-70��9, ��n22�С�9 �ҿ�", 
				"��9���� ��n-70��9, ��n20�С�9 �ҿ�"})
		.setIcon(plugin.util.createDisplayItem("����", new ItemStack(Material.MELON_BLOCK), new String[]{
			"��8[��eMelon��8 | ��bPassive��8 | ��dMax Lv.6��8]", "", "������ ����� �� �ִ�."}), 33)
		.setNeedSkill("farm.diligence")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.pumpkin", 6, 0)
		.setEffects(new String[] {
				"��9���� ��n-40��9, ��n30�С�9 �ҿ�", 
				"��9���� ��n-40��9, ��n28�С�9 �ҿ�", 
				"��9���� ��n-40��9, ��n26�С�9 �ҿ�", 
				"��9���� ��n-35��9, ��n24�С�9 �ҿ�", 
				"��9���� ��n-35��9, ��n22�С�9 �ҿ�", 
				"��9���� ��n-35��9, ��n20�С�9 �ҿ�"})
		.setIcon(plugin.util.createDisplayItem("ȣ��", new ItemStack(Material.PUMPKIN), new String[]{
			"��8[��ePumpkin��8 | ��bPassive��8 | ��dMax Lv.6��8]", "", "ȣ���� ����� �� �ִ�."}), 34)
		.setNeedSkill("farm.melon")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.nether-wart", 6, 0)
		.setEffects(new String[] {
				"��9���� -0, ��n40�С�9 �ҿ�", 
				"��9���� -0, ��n38�С�9 �ҿ�", 
				"��9���� -0, ��n36�С�9 �ҿ�", 
				"��9���� -0, ��n34�С�9 �ҿ�", 
				"��9���� -0, ��n32�С�9 �ҿ�", 
				"��9���� -0, ��n30�С�9 �ҿ�"})
		.setIcon(plugin.util.createDisplayItem("���� �縶��", new ItemStack(Material.NETHER_STALK), new String[]{
			"��8[��eNether Wart��8 | ��bPassive��8 | ��dMax Lv.6��8]", "", "���� �縶�͸� ����� �� �ִ�."}), 35)
		.setNeedSkill("farm.pumpkin")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.faithfulness", 1, 0)
		.setEffects(new String[] {
				"��9���� ���� ����, �� 3�� ���� ����"})
		.setIcon(plugin.util.createDisplayItem("����", new ItemStack(Material.DIAMOND_HOE), new String[]{
			"��8[��eFaithfulness��8 | ��bPassive��8 | ��dMax Lv.1��8]", "", "����� �⺻�� �������� �⸥��.", "Polaris ���� �����⸦ �̿��� �� �ִ�."}), 26)
			.setNeedSkill("farm.nether-wart")
		);
		
		
		/** ==================================================================================================================================================== **/
		
		
		skillMap.put(SkillType.SAIL, new ArrayList<Skill>());
		
		skillMap.get(SkillType.SAIL).add(new Skill("boat.sailing", 1, 0)
		.setEffects(new String[] {
				"��9��Ʈ�� ��� ��Ŭ���Ͽ� ���� ����"})
		.setIcon(plugin.util.createDisplayItem("���ϸ�", new ItemStack(Material.BOAT), new String[]{
			"��8[��eSailing��8 | ��bPassive��8 | ��dMax Lv.1��8]", "", "��𼭵� ��Ʈ�� Ż �� �ִ�."}), 32)
		);
		
		skillMap.get(SkillType.SAIL).add(new Skill("boat.autopilot", 1, 42)
		.setEffects(new String[] {
				"��9�ڵ� ���� �̿� ����"})
		.setIcon(plugin.util.createDisplayItem("�������Ϸ�", new ItemStack(Material.COMPASS), new String[]{
			"��8[��eAutopilot��8 | ��cActive��8 | ��dMax Lv.1��8]", "", "����Ű�� ������ �ʾƵ�", "���ϰ� �ִ� �������� �ڵ� �����Ѵ�.", "��b�� ��ħ�� ��� ��Ŭ������ ON/OFF"}), 23)
		.setNeedSkill("boat.sailing")
		);
		
		skillMap.get(SkillType.SAIL).add(new Skill("boat.into-the-storm", 5, 84)
		.setEffects(new String[] {
				"��9���� ��� ��n90��", 
				"��9���� ��� ��n75��", 
				"��9���� ��� ��n60��", 
				"��9���� ��� ��n45��", 
				"��9���� ��� ��n30��"})
		.setIcon(plugin.util.createDisplayItem("���� �� ����", new ItemStack(Material.ENDER_PEARL), new String[]{
			"��8[��eInto The Storm��8 | ��cActive��8 | ��dMax Lv.5��8]", "", "�ٴ��� ��ǳ��� �Բ� ���ϴ� ������ �̵��Ѵ�.", "��b�� �޴����� ����"}), 14)
		.setNeedSkill("boat.autopilot")
		);
		
		
		/** ==================================================================================================================================================== **/
		
		for (List<Skill> list : skillMap.values()) {
			for (Skill skill : list) {
				skills.put(skill.getName(), skill);
			}
		}
	}
	
	public static Skill getSkill(String name) {
		return skills.get(name);
	}
	
	public static Skill getSkillFromDisplay(String name) {
		for (Skill skill : skills.values()) {
			if (skill.getDisplayName().equals(name)) {
				return skill;
			}
		}
		
		return null;
	}
	
	public static void giveSP(OfflinePlayer p, int amount) {
		UserData data = new UserData(p.getUniqueId());
		data.giveSP(amount);
	}
	
	public enum SkillType {
		HUNT, MINE, FARM, SAIL
	}
}