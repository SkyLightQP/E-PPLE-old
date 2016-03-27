package me.efe.efemobs;

import java.lang.reflect.Field;
import java.util.HashMap;

import me.confuser.barapi.BarAPI;
import me.efe.efegear.EfeUtil;
import me.efe.efeisland.IslandUtils;
import me.efe.efemobs.rudish.BossListener;
import me.efe.efemobs.rudish.ChestListener;
import me.efe.efemobs.rudish.GuiderGUI;
import me.efe.efemobs.rudish.HuntListener;
import me.efe.efemobs.rudish.RudishSpawner;
import me.efe.efemobs.rudish.ScrollListener;
import me.efe.efemobs.rudish.ScrollMerchantGUI;
import me.efe.efemobs.rudish.TeleportGUI;
import me.efe.efemobs.rudish.UserData;
import me.efe.efemobs.rudish.enchant.EnchantGUI;
import me.efe.efemobs.rudish.mobs.FloorListener;
import me.efe.efeserver.EfeServer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.elseland.xikage.MythicMobs.MythicMobs;
import net.elseland.xikage.MythicMobs.Adapters.Bukkit.Commands.CommandHandler;
import net.elseland.xikage.MythicMobs.Mobs.Entities.MythicEntity;
import net.elseland.xikage.MythicMobs.Mobs.Entities.MythicEntityType;
import net.elseland.xikage.MythicMobs.VolatileCode.VolatileCodeHandler;
import net.minecraft.server.v1_8_R3.EntityEnderman;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WGBukkit;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

public class EfeMobs extends JavaPlugin {
	public EfeUtil util;
	public EffectManager em;
	
	public HuntListener huntListener;
	public TeleportGUI teleportGui;
	public EnchantGUI enchantGui;
	public ScrollListener scrollListener;
	public BossListener bossListener;
	public FloorListener floorListener;
	public ChestListener chestListener;
	public RudishSpawner rudishSpawner;
	private static ScrollMerchantGUI scrollMerchantGui;
	private static GuiderGUI guiderGui;
	
	public EfeServer epple;
	public MythicMobs mythicMobs;
	public VolatileCodeHandler vch;
	
	@Override
	public void onDisable() {
		util.logDisable();
		
		bossListener.clear();
	}
	
	@Override
	public void onEnable() {
		util = new EfeUtil(this);
		util.logEnable();
		
		this.epple = (EfeServer) getServer().getPluginManager().getPlugin("EfeServer");
		
		this.huntListener = new HuntListener(this);
		this.teleportGui = new TeleportGUI(this);
		this.enchantGui = new EnchantGUI(this);
		this.scrollListener = new ScrollListener(this);
		this.bossListener = new BossListener(this);
		this.floorListener = new FloorListener(this);
		this.chestListener = new ChestListener(this);
		this.rudishSpawner = new RudishSpawner(this);
		scrollMerchantGui = new ScrollMerchantGUI(this);
		guiderGui = new GuiderGUI(this);
		
		util.register(huntListener);
		util.register(teleportGui);
		util.register(enchantGui);
		util.register(scrollListener);
		util.register(bossListener);
		util.register(floorListener);
		util.register(chestListener);
		util.register(rudishSpawner);
		util.register(scrollMerchantGui);
		util.register(guiderGui);
		
		NMSUtils.registerEntity("Enderman", 58, EntityEnderman.class, FixedEnderman.class);
		registerMythicEntity();
		CommandHandler.cmdReload(getServer().getConsoleSender(), null);
		
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ScrollMerchantTrait.class).withName("ScrollMerchant"));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(RudishGuiderTrait.class).withName("RudishGuider"));
		
		mythicMobs = (MythicMobs) getServer().getPluginManager().getPlugin("MythicMobs");
		vch = mythicMobs.volatileCodeHandler;
		
		em = new EffectManager(EffectLib.instance());
		
		ScrollUtils.init(this);
	}
	
	public static void openMerchantGUI(Player p) {
		scrollMerchantGui.openGUI(p);
	}
	
	public static void openGuiderGUI(Player p) {
		guiderGui.openGUI(p);
	}
	
	private void registerMythicEntity() {
		try {
			Field field = MythicEntity.class.getDeclaredField("entities");
			field.setAccessible(true);
			
			@SuppressWarnings("unchecked")
			HashMap<MythicEntityType, Class<? extends MythicEntity>> entities = (HashMap<MythicEntityType, Class<? extends MythicEntity>>) field.get(null);
			entities.put(MythicEntityType.ENDERMAN, MythicFixedEnderman.class);
			
			field.set(null, entities);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getFloor(Location loc) {
		if (!loc.getWorld().getName().equals("world")) return 0;
		
		for (int i = 1; i <= 10; i ++) {
			if (WGBukkit.getRegionManager(loc.getWorld()).getRegion("rudish_"+i+"f").contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
				return i;
			}
		}
		
		return 0;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("efemobs")) {
			try {
				Player p = (Player) s;
				
				if (!p.isOp()) return false;
				
				if (a.length == 0) {
					p.sendMessage("§a▒§r /efemobs <floor> (amount)");
					p.sendMessage("§a▒§r /efemobs maxfloor <player> <soul>");
					p.sendMessage("§a▒§r /efemobs soul give <player> <soul>");
					p.sendMessage("§a▒§r /efemobs soul take <player> <soul>");
					p.sendMessage("§a▒§r /efemobs storedsoul give <player> <soul>");
					p.sendMessage("§a▒§r /efemobs storedsoul take <player> <soul>");
				} else if (a[0].equalsIgnoreCase("maxfloor")) {
					OfflinePlayer player = util.getOfflinePlayer(a[1]);
					UserData data = new UserData(player);
					int floor = Integer.parseInt(a[2]);
					
					data.setMaxFloor(floor);
					
					s.sendMessage("§a▒§r 완료");
				} else if (a[0].equalsIgnoreCase("soul")) {
					OfflinePlayer player = util.getOfflinePlayer(a[2]);
					UserData data = new UserData(player);
					int amount = Integer.parseInt(a[3]);
					
					if (a[1].equalsIgnoreCase("take")) {
						amount = -amount;
					} else if (!a[1].equalsIgnoreCase("give")) {
						return false;
					}
					
					data.setSoul(data.getSoul() + amount);
					
					s.sendMessage("§a▒§r 완료");
				} else if (a[0].equalsIgnoreCase("storedsoul")) {
					OfflinePlayer player = util.getOfflinePlayer(a[2]);
					UserData data = new UserData(player);
					int amount = Integer.parseInt(a[3]);
					
					if (a[1].equalsIgnoreCase("take")) {
						amount = -amount;
					} else if (!a[1].equalsIgnoreCase("give")) {
						return false;
					}
					
					data.setSoul(data.getSoul() + amount);
					
					s.sendMessage("§a▒§r 완료");
				} else {
					int floor = Integer.parseInt(a[0]);
					ItemStack item = ScrollUtils.getScroll(floor);
					
					if (a.length >= 2)
						item.setAmount(Integer.parseInt(a[1]));
					
					p.getInventory().addItem(item);
					
					p.sendMessage("§a▒§r 성공적으로 소환되었습니다.");
				}
			} catch (Exception e) {
				s.sendMessage("§c▒§r Exception Occured: "+e.getMessage());
			}
		} else if (l.equalsIgnoreCase("귀환") || l.equalsIgnoreCase("return")) {
			try {
				Player p = (Player) s;
				Expedition exp = bossListener.getExpedition(p);
				
				if (exp == null || exp.getTask() != null) {
					p.sendMessage("§c▒§r 지금은 사용할 수 없습니다.");
					return false;
				}
				
				BarAPI.removeBar(p);
				
				Location loc = IslandUtils.getIsleLoc(IslandUtils.RUDISH);
				
				//loc.getWorld().refreshChunk(loc.getBlockX(), loc.getBlockZ());
				p.teleport(loc);
				
				chestListener.removeChest(p);
				
				exp.kick(p);
				bossListener.playerMap.remove(p);
				
				if (exp.getMembers().isEmpty()) {
					bossListener.expMap.remove(exp.getChannel());
				}
				
				p.sendMessage("§a▒§r 루디쉬로 귀환했습니다.");
				exp.broadcast("§a▒§r "+p.getName()+"님께서 루디쉬로 귀환하셨습니다.");
				
			} catch (Exception e) {
				s.sendMessage("§c▒§r 잘못된 명령어입니다: "+e.getMessage());
				e.printStackTrace();
			}
		}
		
		return false;
	}
}