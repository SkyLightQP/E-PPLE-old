package me.efe.efeshops;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.efe.efegear.EfeUtil;
import me.efe.efegear.util.VaultHooker;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.util.SignGUI;
import me.efe.efeshops.listeners.AdminShopGUI;
import me.efe.efeshops.listeners.CaseGUI;
import me.efe.efeshops.listeners.CrateGUI;
import me.efe.efeshops.listeners.HeadConverterGUI;
import me.efe.efeshops.listeners.HeadMerchantGUI;
import me.efe.efeshops.listeners.KeysmithGUI;
import me.efe.efeshops.listeners.PrivateShop;
import me.efe.efeshops.listeners.PrivateShopGUI;

public class EfeShops extends JavaPlugin {
	public EfeUtil util;
	public ItemStack buyShop;
	public ItemStack sellShop;
	
	private static KeysmithGUI keysmithGui;
	private static AdminShopGUI adminShopGui;
	private static HeadMerchantGUI headMerchantGui;
	public PrivateShop privateShop;
	public PrivateShopGUI privateShopGui;
	public CrateGUI crateGui;
	public CaseGUI caseGui;
	public HeadConverterGUI headConverterGui;
	
	public SignGUI signGUI;
	public VaultHooker vault;
	
	public EfeServer epple;
	
	@Override
	public void onDisable() {
		util.logDisable();
	}
	
	@Override
	public void onEnable() {
		util = new EfeUtil(this);
		util.logEnable();
		
		saveDefaultConfig();
		
		keysmithGui = new KeysmithGUI(this);
		adminShopGui = new AdminShopGUI(this);
		headMerchantGui = new HeadMerchantGUI(this);
		privateShop = new PrivateShop(this);
		privateShopGui = new PrivateShopGUI(this);
		crateGui = new CrateGUI(this);
		caseGui = new CaseGUI(this);
		headConverterGui = new HeadConverterGUI(this);
		
		util.register(keysmithGui);
		util.register(adminShopGui);
		util.register(headMerchantGui);
		util.register(privateShop);
		util.register(privateShopGui);
		util.register(crateGui);
		util.register(caseGui);
		util.register(headConverterGui);
		
		signGUI = new SignGUI(this);
		
		vault = EfeServer.getInstance().vault;
		
		this.buyShop = util.createDisplayItem("��b���� ������3:: ���ſ�", new ItemStack(Material.TRAPPED_CHEST), 
				new String[]{"�����鿡�Լ� ��������", "�� �� �ִ� ���� ���� ����.", "���� ������ ��ġ�� �� �ִ�."});
		this.sellShop = util.createDisplayItem("��b���� ������3:: �Ǹſ�", new ItemStack(Material.TRAPPED_CHEST), 
				new String[]{"�����鿡�� ��������", "�� �� �ִ� ���� ���� ����.", "���� ������ ��ġ�� �� �ִ�."});
		
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(MerchantTrait.class).withName("Merchant"));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(HeadMerchantTrait.class).withName("HeadMerchant"));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(FisherTrait.class).withName("Fisher"));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(KeysmithTrait.class).withName("Keysmith"));
		
		this.epple = EfeServer.getInstance();
	}
	
	public static void openGUIAdminShop(Player p, NPC npc) {
		adminShopGui.openGUI(p, npc);
	}
	
	public static void openGUIKeysmith(Player p) {
		keysmithGui.openGUI(p);
	}
	
	public static void openGUIHeadMerchant(Player p) {
		headMerchantGui.openGUI(p);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("efeshops")) {
			try {
				Player p = (Player) s;
				
				if (!p.isOp()) return false;
				
				if (a.length == 0) {
					p.sendMessage("/efeshops <T/F>");
					p.sendMessage("/efeshops <T: buy/F: sell> <slot> <price>");
					p.sendMessage("/efeshops remove <buy/sell> <slot>");
					p.sendMessage("/efeshops price <buy/sell> <slot> <price>");
					p.sendMessage("/efeshops currency");
					return false;
				}
				
				NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(p);
				
				if (npc == null) {
					p.sendMessage("��c�ơ�r NPC�� �������ּ���.");
					return false;
				}
				
				int id = npc.getId();
				
				if (a.length == 1 && a[0].equalsIgnoreCase("currency")) {
					ItemStack item = p.getItemInHand();
					
					if (item == null || item.getType() == Material.AIR)
						getConfig().set(id+".currency", null);
					else {
						ItemStack i = item.clone();
						i.setAmount(1);
						
						getConfig().set(id+".currency", i);
					}
					
					saveConfig();
					
					p.sendMessage("��a�ơ�r �Ϸ�Ǿ����ϴ�.");
				} else if (a.length == 1) {
					if (Boolean.parseBoolean(a[0])) {
						ItemStack item = buyShop.clone();
						item.setAmount(64);
						
						p.getInventory().addItem(item);
					} else {
						ItemStack item = sellShop.clone();
						item.setAmount(64);
						
						p.getInventory().addItem(item);
					}
					
					p.sendMessage("��a�ơ�r �Ϸ�Ǿ����ϴ�.");
				} else if (a.length == 3 && a[0].equalsIgnoreCase("remove")) {
					int slot = Integer.parseInt(a[2]);
					
					if (Boolean.parseBoolean(a[1]))
						getConfig().set(id+".buy."+slot, null);
					else
						getConfig().set(id+".sell."+slot, null);
					
					saveConfig();
					
					p.sendMessage("��a�ơ�r �Ϸ�Ǿ����ϴ�.");
				} else if (a.length == 4 && a[0].equalsIgnoreCase("price")) {
					int slot = Integer.parseInt(a[2]);
					int price = Integer.parseInt(a[3]);
					
					if (Boolean.parseBoolean(a[1]))
						getConfig().set(id+".buy."+slot+".price", price);
					else
						getConfig().set(id+".sell."+slot+".price", price);
					
					saveConfig();
					
					p.sendMessage("��a�ơ�r �Ϸ�Ǿ����ϴ�.");
				} else {
					int slot = Integer.parseInt(a[1]);
					int price = Integer.parseInt(a[2]);
					
					if (Boolean.parseBoolean(a[0])) {
						getConfig().set(id+".buy."+slot+".price", price);
						
						if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR)
							getConfig().set(id+".buy."+slot+".item", p.getItemInHand().clone());
					} else {
						getConfig().set(id+".sell."+slot+".price", price);
						
						if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR)
							getConfig().set(id+".sell."+slot+".item", p.getItemInHand().clone());
					}
					
					saveConfig();
					
					p.sendMessage("��a�ơ�r �Ϸ�Ǿ����ϴ�.");
				}
			} catch (Exception e) {
				s.sendMessage("Exception: "+e.getMessage());
				e.printStackTrace();
			}
		}
		
		return false;
	}
}