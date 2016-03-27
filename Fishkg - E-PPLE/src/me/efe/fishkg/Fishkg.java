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
  * # ����� ����: �� 32����
  * # ������ ����: �� 25��
  * 
  * # ������Ʈ
  * [+] �絿�� �߰�
  * [+] �̳� ��� �߰�
  * [+] ���˴� 3�� �߰�
  * [+] API ����
  * [+] Config�� showTeam �߰�
  * [+] �� �޹̼� �߰�
  * [+] ���� �� ��� �߰�
  * [+] ��޿� ���� +�� ���� ��� �߰�
  * [+] 1.7 ���̿� �߰� �� �̼� ����
  * 
  * [/] �ҽ� ����
  * [/] Config ����
  * [/] �÷����� ȣȯ�� ���� [�ٸ� �÷������� �� �ʿ�� ���� ����.]
  * [/] ����� ũ�� �̼� ����
  * [/] ��ɾ� �̼� ���� �� ���� ����
  * [/] ������ ���˴� ���չ� �̼� ����
  * [/] ����� ���� ����
  * [/] EfeUtil.class Added
  * [/] ����� ��� �߰�
  * [/] String �� UUID (1.8 ���)
  * 
  * <6.1> [/] Vault ȣȯ ���� ����
  * 
  * <6.2> [+] ��� ��� �߰� �� ���� ���ȭ
  * <6.2> [+] Config�� ������ Ȯ�� �߰�
  * <6.2> [+] Config�� Lore ������ ��ū �߰�
  * <6.2> [+] EfeCore ���, 1.7.9 ���� ���� �ߴ�
  * <6.2> [+] Ÿ��Ʋ �޼��� ��� �߰�
  * <6.2> [-] Exp ��� ����
  * <6.2> [-] ����� ������ �ڵ� ����
  * <6.2> [/] �ҽ� ����
  * <6.2> [/] 1�� ���� �ý��� ����
  * <6.2> [/] ������Ʈ üũ ����
  * <6.2> [/] �̳� �ý��� ��þƮ���� ȣȯ�� ���
  * <6.2> [/] ����� ������ ���� ����
  * <6.2> [/] ����� ���� �̼� ����
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
				getServer().getConsoleSender().sendMessage("��4EfeCore 1.0 �̻� ������ ������ּ���!");
				getServer().getConsoleSender().sendMessage("��chttp://blog.naver.com/cwjh1002");
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
			getServer().getConsoleSender().sendMessage("��b[Fishkg]��r ========================");
			getServer().getConsoleSender().sendMessage("��b[Fishkg]��r ��cConfig�� �÷����� ������ ��ġ���� �ʽ��ϴ�!");
			getServer().getConsoleSender().sendMessage("��b[Fishkg]��r Config�� ���� �� �ٽ� �ۼ����ּ���.");
			getServer().getConsoleSender().sendMessage("��b[Fishkg]��r ========================");
			
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
			getServer().getConsoleSender().sendMessage("��b[Fishkg]��r ========================");
			getServer().getConsoleSender().sendMessage("��b[Fishkg]��r �Ź��� Fishkg�� ������Ǿ����ϴ�!");
			getServer().getConsoleSender().sendMessage("��b[Fishkg]��r ���� ����: ��c"+pluginVers);
			getServer().getConsoleSender().sendMessage("��b[Fishkg]��r �Ź���: ��a"+UpdateChecker.getVersion("Fishkg"));
			getServer().getConsoleSender().sendMessage("��b[Fishkg]��r ========================");
			
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
							getServer().getConsoleSender().sendMessage("��c[Fishkg] Vault�� �߰ߵǾ����� ���� �÷������� ã�� �� �����ϴ�!");
						
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
						
						getServer().getConsoleSender().sendMessage("[Fishkg] Vault �÷����ΰ� ���������� ȣȯ�Ǿ����ϴ�!");
					}
				} else {
					for (int i = 0; i < 10; i ++)
						getServer().getConsoleSender().sendMessage("��c[Fishkg] Vault �÷������� �߰ߵ��� �ʾҽ��ϴ�!");
					
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
		
		fishing_STR = createFishingRod("��aưư�� ���˴�", new String[]{
				"��7������� �Ϻκ��� ö�� �̷�����־� �ܴ��� ���˴�."}, Enchantment.DURABILITY);
		fishing_MAS = createFishingRod("��d��ǰ ���˴�", new String[]{
				"��7���� �������� ��ȣ�� �� ���� ���˴�.", 
				"��9����� ��ũ �� �ܰ� ����"}, Enchantment.SILK_TOUCH);
		fishing_CHA = createFishingRod("��5ȥ���� ���˴�", new String[]{
				"��7��X���� �� ������ �ֹ����� �з����� ���˴�.", 
				"��7X�� ���� 2��� �Ѵ�.", 
				"��8��oSpecial Thanks to Bam for Idea", 
				"��9����� ���� ��30%"}, Enchantment.LOOT_BONUS_BLOCKS);
		fishing_SHI = createFishingRod("��b���� ���˴�", new String[]{
				"��7���˴븦 ���л�� ������ ���´�?!", 
				"��8��oSpecial Thanks to Bam for Idea", 
				"��9����� ���� 100% ���"}, Enchantment.PROTECTION_ENVIRONMENTAL);
		fishing_TEC = createFishingRod("��e��l��ũ���� ���˴�", new String[]{
				"��7����ڵ��� ����ϴ� ��� ���˴�.", 
				"��7����� ����� �ʿ���Ѵ�.", 
				"��9����� ���� +50%, 30% Ȯ���� ��ħ"}, Enchantment.WATER_WORKER);
		fishing_LEG = createFishingRod("��6��l������ ���˴�", new String[]{
				"��7�ı��� ��ġ�� �Ƹ��ٿ� ���˴�.", 
				"��9����� ���� +0~30cm"}, enchLuck);
		
		//Special Items
		fishBucket = EfeUtils.item.createItem("��b����� �絿��", new ItemStack(Material.WATER_BUCKET), new String[]{
			getConfig().getString("data-token.bucket"), 
			"", 
			"��7����⸦ �����ϱ� ���� �絿��.", 
			"��7��Ŭ���ؼ� �絿�̸� �� �� �ִ�."});
		
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
		if (EfeUtils.item.containsLore(item, "��9����� ��ũ �� �ܰ� ����")) return RodType.MASTER;
		if (EfeUtils.item.containsLore(item, "��9����� ���� ��30%")) return RodType.CHAOS;
		if (EfeUtils.item.containsLore(item, "��9����� ���� 100% ���")) return RodType.SHIELD;
		if (EfeUtils.item.containsLore(item, "��9����� ���� +50%, 30% Ȯ���� ��ħ")) return RodType.TECHNICAL;
		if (EfeUtils.item.containsLore(item, "��9����� ���� +0~30cm")) return RodType.LEGEND;
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
		
		String length = lore.get(index).replace("��7", "").replace("cm", "");
		return Double.parseDouble(length);
	}
	
	public FishRank getRank(ItemStack item) {
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore() || !item.getItemMeta().getLore().get(0).contains("��7���: ")) return null;
		switch (item.getItemMeta().getLore().get(0).replace("��7���: ", "")) {
		case "��5��lS":
			return FishRank.S;
		case "��d��lA":
			return FishRank.A;
		case "��9��lB":
			return FishRank.B;
		case "��3C":
			return FishRank.C;
		case "��bD":
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
			if (text.startsWith("��9�̳� ������")) {
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
			if (text.startsWith("��9�̳� �����ʡ�")) {
				return Integer.parseInt(text.replace("��9�̳� �����ʡ�", ""));
			}
		}
		
		return 0;
	}
	
	public void setLure(ItemStack item) {
		String text = "��9�̳� ������";
		
		if (item.containsEnchantment(enchLure)) {
			int lv = item.getEnchantmentLevel(enchLure);
			
			text += "��"+lv;
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
			if (it.next().startsWith("��9�̳� ������")) {
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
