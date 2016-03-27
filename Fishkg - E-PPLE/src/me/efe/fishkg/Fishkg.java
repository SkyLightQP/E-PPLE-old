package me.efe.fishkg;

import java.util.List;
import java.util.ListIterator;

import me.efe.efecore.hook.VaultHooker;
import me.efe.efecore.util.EfeUtils;
import me.efe.efecore.util.UpdateChecker;
import me.efe.fishkg.commands.CommandFishkg;
import me.efe.fishkg.commands.CommandLure;
import me.efe.fishkg.listeners.BucketGUI;
import me.efe.fishkg.listeners.LureGUI;
import me.efe.fishkg.listeners.ShopGUI;
import me.efe.fishkg.listeners.fish.FishListener;
import me.efe.fishkg.listeners.fish.FishListener.FishkgBiome;
import me.efe.fishkg.listeners.fish.FishkgFish;
import me.efe.fishkg.listeners.fish.FishkgFish.FishRank;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

 /*
  * Fishkg 6.2  by Efe
  * 
  * # 물고기 종류: 총 32마리
  * # 쓰레기 종류: 총 25개
  * 
  * # 업데이트
  * [+] 양동이 추가
  * [+] 미끼 기능 추가
  * [+] 낚싯대 3종 추가
  * [+] API 지원
  * [+] Config에 showTeam 추가
  * [+] 새 펄미션 추가
  * [+] 랜덤 팀 기능 추가
  * [+] 등급에 따라 +α 가격 기능 추가
  * [+] 1.7 바이옴 추가 및 미세 수정
  * 
  * [/] 소스 정리
  * [/] Config 정리
  * [/] 플러그인 호환성 증가 [다른 플러그인을 꼭 필요로 하지 않음.]
  * [/] 물고기 크기 미세 수정
  * [/] 명령어 미세 수정 및 버그 수정
  * [/] 전설의 낚싯대 조합법 미세 수정
  * [/] 물고기 버그 수정
  * [/] EfeUtil.class Added
  * [/] 디버그 기능 추가
  * [/] String → UUID (1.8 대비)
  * 
  * <6.1> [/] Vault 호환 버그 수정
  * 
  * <6.2> [+] 모드 기능 추가 및 팀전 모드화
  * <6.2> [+] Config에 쓰레기 확률 추가
  * <6.2> [+] Config에 Lore 데이터 토큰 추가
  * <6.2> [+] EfeCore 사용, 1.7.9 이하 지원 중단
  * <6.2> [+] 타이틀 메세지 기능 추가
  * <6.2> [-] Exp 기능 삭제
  * <6.2> [-] 물고기 데이터 코드 삭제
  * <6.2> [/] 소스 정리
  * <6.2> [/] 1등 보상 시스템 변경
  * <6.2> [/] 업데이트 체크 수정
  * <6.2> [/] 미끼 시스템 인첸트와의 호환성 향상
  * <6.2> [/] 물고기 아이콘 버그 수정
  * <6.2> [/] 물고기 상점 미세 수정
  */

public class Fishkg extends JavaPlugin {
	private static Fishkg instance;
	public String main;
	public boolean addon_extraShop = false;
	
	private int configVers = 62;
	private double pluginVers = 6.2;
	
	public CommandFishkg commander;
	public FishListener fishlistener;
	public ShopGUI shopGui;
	public BucketGUI bucketGui;
	public LureGUI lureGui;
	public VaultHooker vault;
	
	public ItemStack fishing_STR;
	public ItemStack fishing_MAS;
	public ItemStack fishing_CHA;
	public ItemStack fishing_SHI;
	public ItemStack fishing_TEC;
	public ItemStack fishing_LEG;
	public ItemStack fishBucket;
	
	public Enchantment enchLuck;
	public Enchantment enchLure;
	
	@Override
	public void onEnable() {
		instance = this;
		main = "";
		
		Contest.init(this);
		
		this.getCommand("fishkg").setExecutor(commander = new CommandFishkg(this));
		
		getServer().getPluginManager().registerEvents(fishlistener = new FishListener(this), this);
		
		saveDefaultConfig();
		
		//EfeGear Version
		if (EfeUtils.getCoreVersion() < 1.0) {
			for (int i = 0; i < 10; i ++) {
				getServer().getConsoleSender().sendMessage("§4EfeCore 1.0 이상 버전을 사용해주세요!");
				getServer().getConsoleSender().sendMessage("§chttp://blog.naver.com/cwjh1002");
			}
			
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.setEnabled(false);
			return;
		}
		
		//Config Check
		if (getConfig().getInt("config") != configVers) {
			getServer().getConsoleSender().sendMessage("§b[Fishkg]§r ========================");
			getServer().getConsoleSender().sendMessage("§b[Fishkg]§r §cConfig가 플러그인 버전과 일치하지 않습니다!");
			getServer().getConsoleSender().sendMessage("§b[Fishkg]§r Config를 삭제 후 다시 작성해주세요.");
			getServer().getConsoleSender().sendMessage("§b[Fishkg]§r ========================");
			
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.setEnabled(false);
			return;
		}
		
		//Update Check
		if (getConfig().getBoolean("general.updateCheck") && UpdateChecker.getVersion("Fishkg") > pluginVers) {
			getServer().getConsoleSender().sendMessage("§b[Fishkg]§r ========================");
			getServer().getConsoleSender().sendMessage("§b[Fishkg]§r 신버전 Fishkg가 릴리즈되었습니다!");
			getServer().getConsoleSender().sendMessage("§b[Fishkg]§r 현재 버전: §c"+pluginVers);
			getServer().getConsoleSender().sendMessage("§b[Fishkg]§r 신버전: §a"+UpdateChecker.getVersion("Fishkg"));
			getServer().getConsoleSender().sendMessage("§b[Fishkg]§r ========================");
			
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Debug
		if (getConfig().getBoolean("general.debug")) {
			for (FishkgBiome key : fishlistener.fishMap.keySet()) {
				List<FishkgFish> list = fishlistener.fishMap.get(key);
				
				double percent = 0;
				for (FishkgFish fish : list) percent += fish.getPercent();
				
				System.out.println("[Fishkg] Loaded fish map : "+key.name()+", total percent: "+percent);
			}
		}
		
		//Hook plugins
		if (getConfig().getBoolean("shop.enable")) {
			
			if (getConfig().getInt("shop.emerald") != 0) {
				shopGui = new ShopGUI(this);
				getServer().getPluginManager().registerEvents(shopGui, this);
			} else {
				if (VaultHooker.exists()) {
					if (!VaultHooker.hasEconomy()) {
						for (int i = 0; i < 10; i ++)
							getServer().getConsoleSender().sendMessage("§c[Fishkg] Vault가 발견되었으나 경제 플러그인을 찾을 수 없습니다!");
						
						try {
							Thread.sleep(5000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						vault = new VaultHooker();
						shopGui = new ShopGUI(this);
						getServer().getPluginManager().registerEvents(shopGui, this);
						
						vault.setupEconomy();
						
						getServer().getConsoleSender().sendMessage("[Fishkg] Vault 플러그인과 성공적으로 호환되었습니다!");
					}
				} else {
					for (int i = 0; i < 10; i ++)
						getServer().getConsoleSender().sendMessage("§c[Fishkg] Vault 플러그인이 발견되지 않았습니다!");
					
					try {
						Thread.sleep(5000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		//Auto Start
		if (getConfig().getBoolean("general.autoStart")) {
			Contest.start(-1);
		}
	    
		//Fishing Rods
		enchLuck = Enchantment.getByName("LUCK");
		enchLure = Enchantment.getByName("LURE");
		if (enchLuck == null) enchLuck = Enchantment.OXYGEN;
		
		fishing_STR = createFishingRod("§a튼튼한 낚싯대", new String[]{
				"§7막대기의 일부분이 철로 이루어져있어 단단한 낚싯대."}, Enchantment.DURABILITY);
		fishing_MAS = createFishingRod("§d명품 낚싯대", new String[]{
				"§7왠지 여성들이 선호할 것 같은 낚싯대.", 
				"§9물고기 랭크 한 단계 증가"}, Enchantment.SILK_TOUCH);
		fishing_CHA = createFishingRod("§5혼돈의 낚싯대", new String[]{
				"§7메X플의 한 유명한 주문서를 패러디한 낚싯대.", 
				"§7X의 값은 2라고 한다.", 
				"§8§oSpecial Thanks to Bam for Idea", 
				"§9물고기 길이 ±30%"}, Enchantment.LOOT_BONUS_BLOCKS);
		fishing_SHI = createFishingRod("§b내성 낚싯대", new String[]{
				"§7낚싯대를 방패삼아 공격을 막는다?!", 
				"§8§oSpecial Thanks to Bam for Idea", 
				"§9물고기 공격 100% 방어"}, Enchantment.PROTECTION_ENVIRONMENTAL);
		fishing_TEC = createFishingRod("§e§l테크니컬 낚싯대", new String[]{
				"§7상급자들이 사용하는 고급 낚싯대.", 
				"§7상당한 기술을 필요로한다.", 
				"§9물고기 길이 +50%, 30% 확률로 놓침"}, Enchantment.WATER_WORKER);
		fishing_LEG = createFishingRod("§6§l전설의 낚싯대", new String[]{
				"§7후광이 비치는 아름다운 낚싯대.", 
				"§9물고기 길이 +0~30cm"}, enchLuck);
		
		//Special Items
		fishBucket = EfeUtils.item.createItem("§b물고기 양동이", new ItemStack(Material.WATER_BUCKET), new String[]{
			getConfig().getString("data-token.bucket"), 
			"", 
			"§7물고기를 보관하기 위한 양동이.", 
			"§7우클릭해서 양동이를 열 수 있다."});
		
		// Recipes
		if (getConfig().getBoolean("fishingRod.craftable")) {
	    	addRecipe(fishing_STR, 0);
	    	addRecipe(fishing_MAS, 1);
	    	addRecipe(fishing_CHA, 2);
	    	addRecipe(fishing_SHI, 3);
	    	addRecipe(fishing_TEC, 4);
	    	addRecipe(fishing_LEG, 5);
	    }
		
		//Functions
		if (getConfig().getBoolean("bucket.enable")) {
			getServer().getPluginManager().registerEvents(bucketGui = new BucketGUI(this), this);
		}
		
		if (getConfig().getBoolean("fishingRod.lure") && enchLure != null) {
			getServer().getPluginManager().registerEvents(lureGui = new LureGUI(this), this);
			
			this.getCommand("lure").setExecutor(new CommandLure(this));
		}
		
		getLogger().info(this.getDescription().getFullName()+" has been enabled!");
	}
	
	@Override
	public void onDisable() {
		Contest.getRanker().save();
		Contest.getRanker().removeOldData();
		
		getLogger().info(this.getDescription().getFullName()+" has been disabled.");
	}
	
	public static Fishkg getInstance() {
		return instance;
	}
	
	public void addRecipe(ItemStack fishing_rod, int type) {
		ShapedRecipe recipe = null;
		
		switch (type) {
		case 0:
			recipe = new ShapedRecipe(fishing_rod)
			.shape(new String[] {"  I", " I@", "# @"})
			.setIngredient('#', Material.STICK)
			.setIngredient('I', Material.IRON_INGOT)
			.setIngredient('@', Material.STRING);
			break;
		case 1:
			recipe = new ShapedRecipe(fishing_rod)
			.shape(new String[] {"  D", " G@", "# @"})
			.setIngredient('#', Material.STICK)
			.setIngredient('D', Material.DIAMOND)
			.setIngredient('G', Material.GOLD_INGOT)
			.setIngredient('@', Material.STRING);
			break;
		case 2:
			recipe = new ShapedRecipe(fishing_rod)
			.shape(new String[] {"  B", " #@", "# @"})
			.setIngredient('B', Material.ENCHANTED_BOOK)
			.setIngredient('#', Material.STICK)
			.setIngredient('@', Material.STRING);
			break;
		case 3:
			recipe = new ShapedRecipe(fishing_rod)
			.shape(new String[] {"  O", " #@", "# @"})
			.setIngredient('O', Material.OBSIDIAN)
			.setIngredient('#', Material.STICK)
			.setIngredient('@', Material.STRING);
			break;
		case 4:
			recipe = new ShapedRecipe(fishing_rod)
			.shape(new String[] {"  R", " #@", "# @"})
			.setIngredient('R', Material.REDSTONE_BLOCK)
			.setIngredient('#', Material.BLAZE_ROD)
			.setIngredient('@', Material.STRING);
			break;
		case 5:
			recipe = new ShapedRecipe(fishing_rod)
			.shape(new String[] {"  D", " #@", "# @"})
			.setIngredient('D', Material.DIAMOND)
			.setIngredient('#', Material.BLAZE_ROD)
			.setIngredient('@', Material.STRING);
			break;
		}
		
		getServer().addRecipe(recipe);
	}
	
	public ItemStack createFishingRod(String name, String[] lore, Enchantment ench) {
		ItemStack item = EfeUtils.item.createItem(name, new ItemStack(Material.FISHING_ROD), lore);
		
		item.addUnsafeEnchantment(ench, 4);
		
		return item;
	}
	
	public RodType getRodType(ItemStack item) {
		if (item.containsEnchantment(Enchantment.DURABILITY) && item.getEnchantmentLevel(Enchantment.DURABILITY) == 4) return RodType.STRONG;
		if (EfeUtils.item.containsLore(item, "§9물고기 랭크 한 단계 증가")) return RodType.MASTER;
		if (EfeUtils.item.containsLore(item, "§9물고기 길이 ±30%")) return RodType.CHAOS;
		if (EfeUtils.item.containsLore(item, "§9물고기 공격 100% 방어")) return RodType.SHIELD;
		if (EfeUtils.item.containsLore(item, "§9물고기 길이 +50%, 30% 확률로 놓침")) return RodType.TECHNICAL;
		if (EfeUtils.item.containsLore(item, "§9물고기 길이 +0~30cm")) return RodType.LEGEND;
		return null;
	}
	
	public boolean isFish(ItemStack item) {
		if (item == null) return false;
		if (!item.getType().equals(Material.RAW_FISH)) return false;
		if (!item.hasItemMeta()) return false;
		if (!item.getItemMeta().hasLore() || !item.getItemMeta().hasDisplayName()) return false;
		
		for (String text : item.getItemMeta().getLore()) {
			if (text.endsWith("cm")) {
				return true;
			}
		}
		
		return false;
	}
	
	public double getLength(ItemStack item) {
		if (!isFish(item)) return 0;
		
		List<String> lore = item.getItemMeta().getLore();
		int index = getConfig().getBoolean("fish.addGrade") ? 1 : 0;
		
		if ((index == 1 && lore.get(0).endsWith("cm")) || (index == 0 && lore.get(1).endsWith("cm"))) return 0;
		if (!lore.get(index).contains("cm")) return 0;
		
		String length = lore.get(index).replace("§7", "").replace("cm", "");
		return Double.parseDouble(length);
	}
	
	public FishRank getRank(ItemStack item) {
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore() || !item.getItemMeta().getLore().get(0).contains("§7등급: ")) return null;
		switch (item.getItemMeta().getLore().get(0).replace("§7등급: ", "")) {
		case "§5§lS":
			return FishRank.S;
		case "§d§lA":
			return FishRank.A;
		case "§9§lB":
			return FishRank.B;
		case "§3C":
			return FishRank.C;
		case "§bD":
			return FishRank.D;
		default: 
			return null;
		}
	}
	
	public boolean hasLure(ItemStack item) {
		if (enchLure == null) return false;
		if (!item.hasItemMeta()) return false;
		if (!item.getItemMeta().hasLore()) return false;
		
		for (String text : item.getItemMeta().getLore()) {
			if (text.startsWith("§9미끼 장착됨")) {
				return true;
			}
		}
		
		return false;
	}
	
	public int getLureLv(ItemStack item) {
		if (enchLure == null) return 0;
		if (!item.hasItemMeta()) return 0;
		if (!item.getItemMeta().hasLore()) return 0;
		
		for (String text : item.getItemMeta().getLore()) {
			if (text.startsWith("§9미끼 장착됨§")) {
				return Integer.parseInt(text.replace("§9미끼 장착됨§", ""));
			}
		}
		
		return 0;
	}
	
	public void setLure(ItemStack item) {
		String text = "§9미끼 장착됨";
		
		if (item.containsEnchantment(enchLure)) {
			int lv = item.getEnchantmentLevel(enchLure);
			
			text += "§"+lv;
			item.removeEnchantment(enchLure);
		}
		
		item.addEnchantment(Enchantment.LURE, getConfig().getInt("fishingRod.lureLv"));
		
		EfeUtils.item.addLore(item, text);
	}
	
	public void removeLure(ItemStack item) {
		if (!hasLure(item)) return;
		
		item.removeEnchantment(enchLure);
		
		int lv = getLureLv(item);
		if (lv != 0)
			item.addUnsafeEnchantment(enchLure, lv);
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		
		ListIterator<String> it = lore.listIterator();
		while (it.hasNext()) {
			if (it.next().startsWith("§9미끼 장착됨")) {
				it.remove();
			}
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public boolean checkPermission(CommandSender s, String perm) {
		if (!getConfig().getBoolean("general.usePermissions")) return true;
		
		if (s instanceof Player) {
			Player p = (Player) s;
			
			return p.hasPermission(perm);
		} else {
			return s.hasPermission(perm);
		}
	}
	
	public enum RodType {
		STRONG, MASTER, CHAOS, SHIELD, TECHNICAL, LEGEND
	}
}
