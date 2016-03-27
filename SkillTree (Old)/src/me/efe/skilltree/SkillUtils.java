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
				"§9나약함 §nⅠ 10초§9 부여", 
				"§9나약함 §nⅠ 15초§9 부여", 
				"§9나약함 §nⅡ 10초§9 부여", 
				"§9나약함 §nⅡ 15초§9 부여", 
				"§9나약함 §nⅢ 10초§9 부여", 
				"§9나약함 §nⅢ 15초§9 부여"})
		.setIcon(plugin.util.createDisplayItem("위크니스", new ItemStack(Material.STONE_SWORD), new String[]{
			"§8[§eWeakness§8 | §bPassive§8 | §dMax Lv.6§8]", "", "검 공격시 30% 확률로", "적에게 나약함을 부여한다."}), 12)
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.binding", 6, 0)
		.setEffects(new String[] {
				"§9§n25%§9 확률로 성공", 
				"§9§n40%§9 확률로 성공", 
				"§9§n55%§9 확률로 성공", 
				"§9§n70%§9 확률로 성공", 
				"§9§n85%§9 확률로 성공", 
				"§9§n100%§9 확률로 성공"})
		.setIcon(plugin.util.createDisplayItem("바인딩", new ItemStack(Material.IRON_SWORD), new String[]{
			"§8[§eBinding§8 | §cActive§8 | §dMax Lv.6§8]", "", "검을 적의 급소에 가격하여", "2초간 동작 불능 상태로 만든다.", "§b→ 검 들고 크리티컬 히트"}), 20)
		.setNeedSkill("hunt.loot-bonus")
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.guard", 6, 0)
		.setEffects(new String[] {
				"§9데미지 §n10%§9 감소", 
				"§9데미지 §n15%§9 감소", 
				"§9데미지 §n20%§9 감소", 
				"§9데미지 §n25%§9 감소", 
				"§9데미지 §n30%§9 감소", 
				"§9데미지 §n35%§9 감소"})
		.setIcon(plugin.util.createDisplayItem("가드", new ItemStack(Material.CHAINMAIL_CHESTPLATE), new String[]{
			"§8[§eGuard§8 | §bPassive§8 | §dMax Lv.6§8]", "", "웅크린 상태에서 발사체 데미지를", "일정량 감소시킨다."}), 13)
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.angelic-bless", 6, 0)
		.setEffects(new String[] {
				"§9§n나약함§9 제거", 
				"§9§n나약함, 독§9 제거", 
				"§9§n나약함, 독, 구속§9 제거", 
				"§9§n나약함, 독, 구속, 허기§9 제거", 
				"§9§n나약함, 독, 구속, 허기, 실명§9 제거", 
				"§9§n나약함, 독, 구속, 허기, 실명, 멀미§9 제거"})
		.setIcon(plugin.util.createDisplayItem("엔젤릭 블레스", new ItemStack(Material.IRON_CHESTPLATE), new String[]{
			"§8[§eAngelic Bless§8 | §cActive§8 | §dMax Lv.6§8]", "", "자신과 4블럭 이내 플레이어의", "특정 디버프를 제거한다.",
			"§b→ 더블 Shift로 시전", "§b→ 딜레이 3분"}), 22)
		.setNeedSkill("hunt.loot-bonus")
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.slowdown", 6, 0)
		.setEffects(new String[] {
				"§9구속 §nⅠ 1.5초§9 부여", 
				"§9구속 §nⅠ 2초§9 부여", 
				"§9구속 §nⅡ 2초§9 부여", 
				"§9구속 §nⅡ 2.5초§9 부여", 
				"§9구속 §nⅢ 2.5초§9 부여", 
				"§9구속 §nⅢ 3초§9 부여"})
		.setIcon(plugin.util.createDisplayItem("슬로우 다운", new ItemStack(Material.BOW), new String[]{
			"§8[§e§eSlowdown§8 | §bPassive§8 | §dMax Lv.6§8]", "", "피격당한 적에게 구속 효과를 부여한다."}), 14)
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.multiple-shot", 6, 0)
		.setEffects(new String[] {
				"§9§n2발§9 연속 사격", 
				"§9§n3발§9 연속 사격", 
				"§9§n4발§9 연속 사격", 
				"§9§n5발§9 연속 사격", 
				"§9§n6발§9 연속 사격", 
				"§9§n7발§9 연속 사격"})
		.setIcon(plugin.util.createDisplayItem("멀티플 샷", new ItemStack(Material.BOW), new String[]{
			"§8[§eMultiple Shot§8 | §cActive§8 | §dMax Lv.6§8]", "", "더 많은 양의 화살을 연속적으로 발사한다.", "화살을 추가로 소모하진 않는다.", "§b→ Shift + 사격으로 시전"}), 24)
		.setNeedSkill("hunt.loot-bonus")
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.loot-bonus", 1, 0)
		.setEffects(new String[] {
				"§9전리품 드랍 확률 증가"})
		.setIcon(plugin.util.createDisplayItem("루트 보너스", new ItemStack(Material.CHEST), new String[]{
			"§8[§eLoot Bonus§8 | §bPassive§8 | §dMax Lv.1§8]", "", "전리품의 드랍 확률을 대폭 강화한다."}), 17)
		.setNeedSkills("hunt.weakness", "hunt.guard", "hunt.slowdown")
		);
		
		skillMap.get(SkillType.HUNT).add(new Skill("hunt.bounty-hunting", 1, 0)
		.setEffects(new String[] {
				"§9보스 보상 아이템 확률 증가"})
		.setIcon(plugin.util.createDisplayItem("바운티 헌팅", new ItemStack(Material.ENDER_CHEST), new String[]{
			"§8[§eAlchemy§8 | §bPassive§8 | §dMax Lv.1§8]", "", "보스 보상의 질을 대폭 강화한다."}), 26)
		.setNeedSkills("hunt.binding", "hunt.angelic-bless", "hunt.multiple-shot")
		);
		
		
		/** ==================================================================================================================================================== **/
		
		
		skillMap.put(SkillType.MINE, new ArrayList<Skill>());
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.minerals-mastery", 9, 0)
		.setEffects(new String[] {
				"§9광물 재생성 §n100초", 
				"§9광물 재생성 §n95초", 
				"§9광물 재생성 §n90초", 
				"§9광물 재생성 §n85초", 
				"§9광물 재생성 §n80초", 
				"§9광물 재생성 §n75초", 
				"§9광물 재생성 §n70초", 
				"§9광물 재생성 §n65초", 
				"§9광물 재생성 §n60초"})
		.setIcon(plugin.util.createDisplayItem("미네랄 마스터리", new ItemStack(Material.IRON_PICKAXE), new String[]{
			"§8[§eMinerals Mastery§8 | §bPassive§8 | §dMax Lv.9§8]", "", "광물의 재생성 시간을 단축한다."}), 14)
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.smeltery", 9, 0)
		.setEffects(new String[] {
				"§9§n1%§9의 확률로 발동", 
				"§9§n2%§9의 확률로 발동",
				"§9§n3%§9의 확률로 발동",
				"§9§n4%§9의 확률로 발동",
				"§9§n5%§9의 확률로 발동",
				"§9§n6%§9의 확률로 발동",
				"§9§n7%§9의 확률로 발동",
				"§9§n8%§9의 확률로 발동",
				"§9§n9%§9의 확률로 발동"})
		.setIcon(plugin.util.createDisplayItem("스멜터리", new ItemStack(Material.GOLD_PICKAXE), new String[]{
			"§8[§eSmeltery§8 | §bPassive§8 | §dMax Lv.9§8]", "", "채광시 일정 확률로 광물이", "제련되어 주괴로 드랍된다."}), 22)
		.setNeedSkill("mine.minerals-mastery")
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.steelmaking", 1, 0)
		.setEffects(new String[] {
				"§9강철 제조 가능"})
		.setIcon(plugin.util.createDisplayItem("제강", new ItemStack(Material.IRON_INGOT), new String[]{
			"§8[§eSteelmaking§8 | §bPassive§8 | §dMax Lv.1§8]", "", "철을 이용해 강철을 만들어낼 수 있다.", "철괴를 구워서 제조할 수 있다."}), 30)
		.setNeedSkill("mine.smeltery")
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.dynamite", 9, 0)
		.setEffects(new String[] {
				"§9일반 TNT §n0.4배§9 강도의 폭발", 
				"§9일반 TNT §n0.6배§9 강도의 폭발", 
				"§9일반 TNT §n0.8배§9 강도의 폭발", 
				"§9일반 TNT §n1.0배§9 강도의 폭발", 
				"§9일반 TNT §n1.2배§9 강도의 폭발", 
				"§9일반 TNT §n1.4배§9 강도의 폭발", 
				"§9일반 TNT §n1.6배§9 강도의 폭발", 
				"§9일반 TNT §n1.8배§9 강도의 폭발", 
				"§9일반 TNT §n2.0배§9 강도의 폭발"})
		.setIcon(plugin.util.createDisplayItem("다이너마이트", new ItemStack(Material.EXPLOSIVE_MINECART), new String[]{
			"§8[§eDynamite§8 | §cActive§8 | §dMax Lv.9§8]", "", "돌을 터트리는 TNT를 발사한다.", "플레이어에게 데미지를 주지는 않는다.", "단, TNT를 1개씩 소모한다.", 
			"§b→ TNT를 들고 우클릭해서 시전", "§b→ 허기 2.5칸 소모"}), 31)
		.setNeedSkill("mine.steelmaking")
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.diamond-rush", 9, 0)
		.setEffects(new String[] {
				"§n10초§9간 지속", 
				"§n15초§9간 지속", 
				"§n20초§9간 지속", 
				"§n25초§9간 지속", 
				"§n30초§9간 지속", 
				"§n35초§9간 지속", 
				"§n40초§9간 지속", 
				"§n45초§9간 지속", 
				"§n50초§9간 지속"})
		.setIcon(plugin.util.createDisplayItem("다이아몬드 러쉬", new ItemStack(Material.DIAMOND_PICKAXE), new String[]{
			"§8[§eDiamond Rush§8 | §cActive§8 | §dMax Lv.9§8]", "", "일정 시간동안 청금석과 금, 다이아몬드를", "발견할 수 있다.", 
			"§b→ 곡괭이 우클릭-좌클릭-우클릭으로 시전", "§b→ 허기 5칸 소모"}), 32)
		.setNeedSkill("mine.dynamite")
		);
		
		skillMap.get(SkillType.MINE).add(new Skill("mine.enchanting", 1, 0)
		.setEffects(new String[] {
				"§9장비 인첸트 가능"})
		.setIcon(plugin.util.createDisplayItem("인첸팅", new ItemStack(Material.ENCHANTED_BOOK), new String[]{
			"§8[§eEnchanting§8 | §bPassive§8 | §dMax Lv.1§8]", "", "Polaris 섬의 마법 부여대에서", "장비를 인첸트 할 수 있다."}), 33)
		.setNeedSkill("mine.diamond-rush")
		);
		
		
		/** ==================================================================================================================================================== **/
		
		
		skillMap.put(SkillType.FARM, new ArrayList<Skill>());
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.carrot", 6, 0)
		.setEffects(new String[] {
				"§9수분 §n-20§9, §n20분§9 소요", 
				"§9수분 §n-20§9, §n18분§9 소요", 
				"§9수분 §n-20§9, §n16분§9 소요", 
				"§9수분 §n-15§9, §n14분§9 소요", 
				"§9수분 §n-15§9, §n12분§9 소요", 
				"§9수분 §n-15§9, §n10분§9 소요"})
		.setIcon(plugin.util.createDisplayItem("당근", new ItemStack(Material.CARROT_ITEM), new String[]{
			"§8[§eCarrot§8 | §bPassive§8 | §dMax Lv.6§8]", "", "당근을 재배할 수 있다."}), 30)
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.potato", 6, 0)
		.setEffects(new String[] {
				"§9수분 §n-20§9, §n20분§9 소요", 
				"§9수분 §n-20§9, §n18분§9 소요", 
				"§9수분 §n-20§9, §n16분§9 소요", 
				"§9수분 §n-15§9, §n14분§9 소요", 
				"§9수분 §n-15§9, §n12분§9 소요", 
				"§9수분 §n-15§9, §n10분§9 소요"})
		.setIcon(plugin.util.createDisplayItem("감자", new ItemStack(Material.POTATO_ITEM), new String[]{
			"§8[§ePotato§8 | §bPassive§8 | §dMax Lv.6§8]", "", "감자를 재배할 수 있다."}), 31)
		.setNeedSkill("farm.carrot")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.wheat", 6, 0)
		.setEffects(new String[] {
				"§9수분 §n-30§9, §n20분§9 소요", 
				"§9수분 §n-30§9, §n18분§9 소요", 
				"§9수분 §n-30§9, §n16분§9 소요", 
				"§9수분 §n-25§9, §n14분§9 소요", 
				"§9수분 §n-25§9, §n12분§9 소요", 
				"§9수분 §n-25§9, §n10분§9 소요"})
		.setIcon(plugin.util.createDisplayItem("밀", new ItemStack(Material.WHEAT), new String[]{
			"§8[§eWheat§8 | §bPassive§8 | §dMax Lv.6§8]", "", "밀을 재배할 수 있다."}), 32)
			.setNeedSkill("farm.potato")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.diligence", 1, 0)
		.setEffects(new String[] {
				"§9동물 교배 가능, 밭 2개 소지 가능"})
		.setIcon(plugin.util.createDisplayItem("근면", new ItemStack(Material.IRON_HOE), new String[]{
			"§8[§eDiligence§8 | §bPassive§8 | §dMax Lv.1§8]", "", "농사의 기본인 근면함을 기른다."}), 23)
			.setNeedSkill("farm.wheat")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.melon", 6, 0)
		.setEffects(new String[] {
				"§9수분 §n-80§9, §n30분§9 소요", 
				"§9수분 §n-80§9, §n28분§9 소요", 
				"§9수분 §n-80§9, §n26분§9 소요", 
				"§9수분 §n-70§9, §n24분§9 소요", 
				"§9수분 §n-70§9, §n22분§9 소요", 
				"§9수분 §n-70§9, §n20분§9 소요"})
		.setIcon(plugin.util.createDisplayItem("수박", new ItemStack(Material.MELON_BLOCK), new String[]{
			"§8[§eMelon§8 | §bPassive§8 | §dMax Lv.6§8]", "", "수박을 재배할 수 있다."}), 33)
		.setNeedSkill("farm.diligence")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.pumpkin", 6, 0)
		.setEffects(new String[] {
				"§9수분 §n-40§9, §n30분§9 소요", 
				"§9수분 §n-40§9, §n28분§9 소요", 
				"§9수분 §n-40§9, §n26분§9 소요", 
				"§9수분 §n-35§9, §n24분§9 소요", 
				"§9수분 §n-35§9, §n22분§9 소요", 
				"§9수분 §n-35§9, §n20분§9 소요"})
		.setIcon(plugin.util.createDisplayItem("호박", new ItemStack(Material.PUMPKIN), new String[]{
			"§8[§ePumpkin§8 | §bPassive§8 | §dMax Lv.6§8]", "", "호박을 재배할 수 있다."}), 34)
		.setNeedSkill("farm.melon")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.nether-wart", 6, 0)
		.setEffects(new String[] {
				"§9수분 -0, §n40분§9 소요", 
				"§9수분 -0, §n38분§9 소요", 
				"§9수분 -0, §n36분§9 소요", 
				"§9수분 -0, §n34분§9 소요", 
				"§9수분 -0, §n32분§9 소요", 
				"§9수분 -0, §n30분§9 소요"})
		.setIcon(plugin.util.createDisplayItem("지옥 사마귀", new ItemStack(Material.NETHER_STALK), new String[]{
			"§8[§eNether Wart§8 | §bPassive§8 | §dMax Lv.6§8]", "", "지옥 사마귀를 재배할 수 있다."}), 35)
		.setNeedSkill("farm.pumpkin")
		);
		
		skillMap.get(SkillType.FARM).add(new Skill("farm.faithfulness", 1, 0)
		.setEffects(new String[] {
				"§9포션 제조 가능, 밭 3개 소지 가능"})
		.setIcon(plugin.util.createDisplayItem("성실", new ItemStack(Material.DIAMOND_HOE), new String[]{
			"§8[§eFaithfulness§8 | §bPassive§8 | §dMax Lv.1§8]", "", "농사의 기본인 성실함을 기른다.", "Polaris 섬의 양조기를 이용할 수 있다."}), 26)
			.setNeedSkill("farm.nether-wart")
		);
		
		
		/** ==================================================================================================================================================== **/
		
		
		skillMap.put(SkillType.SAIL, new ArrayList<Skill>());
		
		skillMap.get(SkillType.SAIL).add(new Skill("boat.sailing", 1, 0)
		.setEffects(new String[] {
				"§9보트를 들고 우클릭하여 항해 가능"})
		.setIcon(plugin.util.createDisplayItem("세일링", new ItemStack(Material.BOAT), new String[]{
			"§8[§eSailing§8 | §bPassive§8 | §dMax Lv.1§8]", "", "어디서든 보트를 탈 수 있다."}), 32)
		);
		
		skillMap.get(SkillType.SAIL).add(new Skill("boat.autopilot", 1, 42)
		.setEffects(new String[] {
				"§9자동 항해 이용 가능"})
		.setIcon(plugin.util.createDisplayItem("오토파일럿", new ItemStack(Material.COMPASS), new String[]{
			"§8[§eAutopilot§8 | §cActive§8 | §dMax Lv.1§8]", "", "방향키를 누르지 않아도", "향하고 있는 방향으로 자동 항해한다.", "§b→ 나침반 들고 우클릭으로 ON/OFF"}), 23)
		.setNeedSkill("boat.sailing")
		);
		
		skillMap.get(SkillType.SAIL).add(new Skill("boat.into-the-storm", 5, 84)
		.setEffects(new String[] {
				"§9재사용 대기 §n90분", 
				"§9재사용 대기 §n75분", 
				"§9재사용 대기 §n60분", 
				"§9재사용 대기 §n45분", 
				"§9재사용 대기 §n30분"})
		.setIcon(plugin.util.createDisplayItem("인투 더 스톰", new ItemStack(Material.ENDER_PEARL), new String[]{
			"§8[§eInto The Storm§8 | §cActive§8 | §dMax Lv.5§8]", "", "바다의 폭풍우와 함께 원하는 섬으로 이동한다.", "§b→ 메뉴에서 시전"}), 14)
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