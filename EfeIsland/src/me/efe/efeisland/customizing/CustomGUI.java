package me.efe.efeisland.customizing;

import java.util.HashMap;

import me.efe.efecommunity.UserData;
import me.efe.efeisland.EfeFlag;
import me.efe.efeisland.EfeIsland;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class CustomGUI implements Listener {
	public EfeIsland plugin;
	public HashMap<String, GUIType> users = new HashMap<String, GUIType>();
	
	public CustomGUI(EfeIsland plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*6, "§3▒§r 섬 커스터마이징");
		ProtectedRegion region = plugin.getIsleRegion(p);
		
		updateInv(inv, region, GUIType.MAIN, p);
		
		p.openInventory(inv);
		users.put(p.getName(), GUIType.MAIN);
	}
	
	public void updateInv(Inventory inv, ProtectedRegion region, GUIType type, Player p) {
		inv.clear();
		
		if (type == GUIType.MAIN) {
			for (int i : new int[]{2, 11, 20, 6, 15, 24}) 
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.LADDER), new String[]{}));
			
			for (int i : new int[]{28, 29, 30, 37, 39, 46, 47, 48})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			for (int i : new int[]{30, 31, 32, 39, 41, 48, 49, 50})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			for (int i : new int[]{32, 33, 34, 41, 43, 50, 51, 52})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			
			inv.setItem(7, plugin.util.createDisplayItem("§e추방", new ItemStack(Material.MINECART), 
					new String[]{"유저를 일시적으로", "내 섬에서 내보냅니다."}));
			inv.setItem(8, plugin.util.createDisplayItem("§c블랙리스트", new ItemStack(Material.BARRIER), 
					new String[]{"블랙리스트를 관리합니다."}));
			
			inv.setItem(38, plugin.util.createDisplayItem("§a§nBUILD", new ItemStack(Material.IRON_PICKAXE), 
					new String[]{"건축 여부, 블럭 제어 등에", "대하여 설정합니다."}));
			inv.setItem(40, plugin.util.createDisplayItem("§a§nFEATURE", new ItemStack(Material.COOKIE), 
					new String[]{"플라이, 액체 제어 등에", "대하여 설정합니다."}));
			inv.setItem(42, plugin.util.createDisplayItem("§a§nCOMBAT", new ItemStack(Material.IRON_SWORD), 
					new String[]{"PvP 여부, 스킬 제어 등에", "대하여 설정합니다."}));
			
			//Entrance
			if (region.getFlag(EfeFlag.ENTRANCE) == State.ALLOW) {
				String[] lore = {
						"입장 가능 여부에 대해 설정합니다.", 
						"OFF시 친구를 제외한 모든 사람들은", 
						"이 섬에 입장할 수 없습니다.", 
						"", 
						"§a§lON:§2 모두 입장 가능", 
						"§cOFF:§4 친구만 입장 가능"
				};
				
				inv.setItem(13, plugin.util.createDisplayItem("§a§n열린 섬", new ItemStack(Material.WOOD_DOOR), lore));
			} else {
				String[] lore = {
						"입장 가능 여부에 대해 설정합니다.", 
						"OFF시 친구를 제외한 모든 사람들은", 
						"이 섬에 입장할 수 없습니다.", 
						"", 
						"§aON:§2 모두 입장 가능", 
						"§c§lOFF:§4 친구만 입장 가능"
				};
				
				inv.setItem(13, plugin.util.createDisplayItem("§c§n닫힌 섬", new ItemStack(Material.IRON_DOOR), lore));
			}
		} else if (type == GUIType.BLOCKS) {
			for (int i : new int[]{28, 29, 30, 37, 39, 46, 47, 48})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			
			inv.setItem(38, plugin.util.createDisplayItem("§cBack", new ItemStack(Material.IRON_PICKAXE), new String[]{"메인으로 돌아갑니다."}));
			
			//Build
			if (region.getFlag(DefaultFlag.BUILD) == State.ALLOW) {
				String[] lore = {
						"건축 여부에 대해 설정합니다.", 
						"OFF시 자신을 제외한 모든 사람들은", 
						"블럭 쌓기/부수기는 물론,", 
						"액자 조작, 카트 설치 등도 불가능합니다.", 
						"", 
						"§a§lON:§2 내 친구도 건축 가능", 
						"§cOFF:§4 나 자신만 건축 가능"
				};
				
				inv.setItem(14, plugin.util.createDisplayItem("§a§n건축", new ItemStack(Material.COBBLESTONE), lore));
				inv.setItem(13, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"건축 여부에 대해 설정합니다.", 
						"OFF시 자신을 제외한 모든 사람들은", 
						"블럭 쌓기/부수기는 물론,", 
						"액자 조작, 카트 설치 등도 불가능합니다.", 
						"", 
						"§aON:§2 내 친구도 건축 가능", 
						"§c§lOFF:§4 나 자신만 건축 가능"
				};
				
				inv.setItem(14, plugin.util.createDisplayItem("§c§n건축", new ItemStack(Material.COBBLESTONE), lore));
				inv.setItem(13, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
			
			//Use
			if (region.getFlag(DefaultFlag.USE) == State.ALLOW) {
				String[] lore = {
						"블럭 조작 허용 여부에 대해 설정합니다.", 
						"OFF시 친구를 제외한 모든 사람들은", 
						"문, 버튼, 레버 등의 블럭을 사용할 수 없습니다.", 
						"", 
						"§a§lON:§2 모두 조작 가능", 
						"§cOFF:§4 내 친구만 조작 가능"
				};
				
				inv.setItem(16, plugin.util.createDisplayItem("§a§n블럭 조작", new ItemStack(Material.TRAP_DOOR), lore));
				inv.setItem(17, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"블럭 조작 허용 여부에 대해 설정합니다.", 
						"OFF시 친구를 제외한 모든 사람들은", 
						"문, 버튼, 레버 등의 블럭을 사용할 수 없습니다.", 
						"", 
						"§aON:§2 모두 조작 가능", 
						"§c§lOFF:§4 내 친구만 조작 가능"
				};
				
				inv.setItem(16, plugin.util.createDisplayItem("§c§n블럭 조작", new ItemStack(Material.TRAP_DOOR), lore));
				inv.setItem(17, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
			
			//Container
			if (region.getFlag(DefaultFlag.CHEST_ACCESS) == State.ALLOW) {
				String[] lore = {
						"컨테이너 허용 여부에 대해 설정합니다.", 
						"OFF시 자신을 제외한 모든 사람들은", 
						"상자, 화로, 공급기, 깔대기 등", 
						"아이템을 보관하는 블럭을 열 수 없습니다.", 
						"", 
						"§a§lON:§2 내 친구도 이용 가능", 
						"§cOFF:§4 나 자신만 이용 가능"
				};
				
				inv.setItem(41, plugin.util.createDisplayItem("§a§n컨테이너", new ItemStack(Material.CHEST), lore));
				inv.setItem(40, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"컨테이너 허용 여부에 대해 설정합니다.", 
						"OFF시 자신을 제외한 모든 사람들은", 
						"상자, 화로, 공급기, 깔대기 등", 
						"아이템을 보관하는 블럭을 열 수 없습니다.", 
						"", 
						"§aON:§2 내 친구도 이용 가능", 
						"§c§lOFF:§4 나 자신만 이용 가능"
				};
				
				inv.setItem(41, plugin.util.createDisplayItem("§c§n컨테이너", new ItemStack(Material.CHEST), lore));
				inv.setItem(40, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
			
			//Melt
			if (region.getFlag(DefaultFlag.SNOW_MELT) == State.ALLOW) {
				String[] lore = {
						"용해 여부에 대해 설정합니다.", 
						"OFF시 눈이 녹지 않고, ", 
						"얼음이 생기거나 녹지 않습니다.", 
						"", 
						"§a§lON:§2 용해 허용", 
						"§cOFF:§4 용해 비허용"
				};
				
				inv.setItem(43, plugin.util.createDisplayItem("§a§n용해", new ItemStack(Material.PACKED_ICE), lore));
				inv.setItem(44, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"용해 여부에 대해 설정합니다.", 
						"OFF시 눈이 녹지 않고, ", 
						"얼음이 생기거나 녹지 않습니다.", 
						"", 
						"§aON:§2 용해 허용", 
						"§c§lOFF:§4 용해 비허용"
				};
				
				inv.setItem(43, plugin.util.createDisplayItem("§c§n용해", new ItemStack(Material.PACKED_ICE), lore));
				inv.setItem(44, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
		} else if (type == GUIType.FUN) {
			for (int i : new int[]{30, 31, 32, 39, 41, 48, 49, 50})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			
			inv.setItem(40, plugin.util.createDisplayItem("§cBack", new ItemStack(Material.COOKIE), new String[]{"메인으로 돌아갑니다."}));
			
			//Fly
			if (region.getFlag(EfeFlag.FLY) == State.ALLOW) {
				String[] lore = {
						"플라이 가능 여부에 대해 설정합니다.", 
						"ON시 모두가 이 섬에서", 
						"날아다닐 수 있습니다.", 
						"", 
						"§a§lON:§2 날 수 있음", 
						"§cOFF:§4 날 수 없음"
				};
				
				inv.setItem(10, plugin.util.createDisplayItem("§a§n플라이", new ItemStack(Material.FEATHER), lore));
				inv.setItem(9, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"플라이 가능 여부에 대해 설정합니다.", 
						"ON시 모두가 이 섬에서", 
						"날아다닐 수 있습니다.", 
						"", 
						"§aON:§2 날 수 있음", 
						"§c§lOFF:§4 날 수 없음"
				};
				
				inv.setItem(10, plugin.util.createDisplayItem("§c§n플라이", new ItemStack(Material.FEATHER), lore));
				inv.setItem(9, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
			
			//Fire
			if (region.getFlag(DefaultFlag.FIRE_SPREAD) == State.ALLOW) {
				String[] lore = {
						"불 번짐 여부에 대해 설정합니다.", 
						"ON시 불이 번질 수 있게 됩니다.", 
						"", 
						"§a§lON:§2 번질 수 있음", 
						"§cOFF:§4 번질 수 없음"
				};
				
				inv.setItem(37, plugin.util.createDisplayItem("§a§n불 번짐", new ItemStack(Material.FIREBALL), lore));
				inv.setItem(36, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"불 번짐 여부에 대해 설정합니다.", 
						"ON시 불이 번질 수 있게 됩니다.", 
						"", 
						"§aON:§2 번질 수 있음", 
						"§c§lOFF:§4 번질 수 없음"
				};
				
				inv.setItem(37, plugin.util.createDisplayItem("§c§n불 번짐", new ItemStack(Material.FIREBALL), lore));
				inv.setItem(36, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
			
			//Grass
			if (region.getFlag(DefaultFlag.GRASS_SPREAD) == State.ALLOW) {
				String[] lore = {
						"자연화 여부에 대해 설정합니다.", 
						"OFF시 잔디, 덩쿨, 버섯 등이", 
						"사방으로 자라나지 않습니다.", 
						"", 
						"§a§lON:§2 자연화 활성화", 
						"§cOFF:§4 자연화 비활성화"
				};
				
				inv.setItem(16, plugin.util.createDisplayItem("§a§n자연화", new ItemStack(Material.VINE), lore));
				inv.setItem(17, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"자연화 여부에 대해 설정합니다.", 
						"OFF시 잔디, 덩쿨, 버섯 등이", 
						"사방으로 자라나지 않습니다.", 
						"", 
						"§aON:§2 자연화 활성화", 
						"§c§lOFF:§4 자연화 비활성화"
				};
				
				inv.setItem(16, plugin.util.createDisplayItem("§c§n자연화", new ItemStack(Material.VINE), lore));
				inv.setItem(17, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
			
			//Liquid
			if (region.getFlag(DefaultFlag.WATER_FLOW) == State.ALLOW) {
				String[] lore = {
						"액체 제어 여부에 대해 설정합니다.", 
						"OFF시 물, 용암 블럭이", 
						"흐르지 않게 됩니다.", 
						"", 
						"§a§lON:§2 액체 제어 비활성화", 
						"§cOFF:§4 액체 제어 활성화"
				};
				
				inv.setItem(43, plugin.util.createDisplayItem("§a§n액체 제어", new ItemStack(Material.WATER_BUCKET), lore));
				inv.setItem(44, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"액체 제어 여부에 대해 설정합니다.", 
						"OFF시 물, 용암 블럭이", 
						"흐르지 않게 됩니다.", 
						"", 
						"§aON:§2 액체 제어 비활성화", 
						"§c§lOFF:§4 액체 제어 활성화"
				};
				
				inv.setItem(43, plugin.util.createDisplayItem("§c§n액체 제어", new ItemStack(Material.WATER_BUCKET), lore));
				inv.setItem(44, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
		} else if (type == GUIType.PVP) {
			for (int i : new int[]{32, 33, 34, 41, 43, 50, 51, 52})
				inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.STAINED_GLASS_PANE), new String[]{}));
			
			inv.setItem(42, plugin.util.createDisplayItem("§cBack", new ItemStack(Material.IRON_SWORD), new String[]{"메인으로 돌아갑니다."}));
			
			//PvP
			if (region.getFlag(DefaultFlag.PVP) == State.ALLOW) {
				String[] lore = {
						"PvP 가능 여부에 대해 설정합니다.", 
						"ON시 상대방에게 데미지를", 
						"입힐 수 있게 됩니다.", 
						"또한 사망 패널티가 존재하지 않습니다.", 
						"", 
						"§a§lON:§2 PvP 가능", 
						"§cOFF:§4 PvP 불가능"
				};
				
				inv.setItem(10, plugin.util.createDisplayItem("§a§nPvP", new ItemStack(Material.REDSTONE), lore));
				inv.setItem(9, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"PvP 가능 여부에 대해 설정합니다.", 
						"ON시 상대방에게 데미지를", 
						"입힐 수 있게 됩니다.", 
						"또한 사망 패널티가 존재하지 않습니다.", 
						"", 
						"§aON:§2 PvP 가능", 
						"§c§lOFF:§4 PvP 불가능"
				};
				
				inv.setItem(10, plugin.util.createDisplayItem("§c§nPvP", new ItemStack(Material.REDSTONE), lore));
				inv.setItem(9, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
			
			//Instant Kill
			if (region.getFlag(EfeFlag.INSTANT_KILL) == State.ALLOW) {
				String[] lore = {
						"인스턴트 킬 여부에 대해 설정합니다.", 
						"ON시 모든 플레이어가 한 방에", 
						"죽게 됩니다.", 
						"", 
						"§a§lON:§2 인스턴트 킬 활성화", 
						"§cOFF:§4 인스턴트 킬 비활성화"
				};
				
				inv.setItem(12, plugin.util.createDisplayItem("§a§n인스턴트 킬", new ItemStack(Material.DIAMOND_SWORD), lore));
				inv.setItem(13, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"인스턴트 킬 여부에 대해 설정합니다.", 
						"ON시 모든 플레이어가 한 방에", 
						"죽게 됩니다.", 
						"", 
						"§aON:§2 인스턴트 킬 활성화", 
						"§c§lOFF:§4 인스턴트 킬 비활성화"
				};
				
				inv.setItem(12, plugin.util.createDisplayItem("§c§n인스턴트 킬", new ItemStack(Material.DIAMOND_SWORD), lore));
				inv.setItem(13, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
			
			//Skill
			if (region.getFlag(EfeFlag.SKILL) == State.ALLOW) {
				String[] lore = {
						"스킬 방지 여부에 대해 설정합니다.", 
						"ON시 섬에서 플레이어가", 
						"스킬을 사용할 수 있게 됩니다.", 
						"또한, 모든 패시브 스킬도 무시됩니다.", 
						"", 
						"§a§lON:§2 스킬 사용 가능", 
						"§cOFF:§4 스킬 사용 불가능"
				};
				
				inv.setItem(37, plugin.util.createDisplayItem("§a§n스킬 방지", new ItemStack(Material.BLAZE_POWDER), lore));
				inv.setItem(36, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"스킬 방지 여부에 대해 설정합니다.", 
						"ON시 섬에서 플레이어가", 
						"스킬을 사용할 수 있게 됩니다.", 
						"또한, 모든 패시브 스킬도 무시됩니다.", 
						"", 
						"§aON:§2 스킬 사용 가능", 
						"§c§lOFF:§4 스킬 사용 불가능"
				};
				
				inv.setItem(37, plugin.util.createDisplayItem("§c§n스킬 방지", new ItemStack(Material.BLAZE_POWDER), lore));
				inv.setItem(36, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
			
			//Potion
			if (region.getFlag(EfeFlag.NO_POTION) == State.ALLOW) {
				String[] lore = {
						"포션 제어 여부에 대해 설정합니다.", 
						"ON시 섬에서 플레이어가", 
						"포션 효과를 얻을 수 없게 합니다.", 
						"방문자는 소지한 포션 효과를 잃게됩니다.", 
						"", 
						"§a§lON:§2 포션 효과 비허용", 
						"§cOFF:§4 포션 효과 허용"
				};
				
				inv.setItem(39, plugin.util.createDisplayItem("§a§n포션 제어", new ItemStack(Material.MILK_BUCKET), lore));
				inv.setItem(40, plugin.util.createDisplayItem("§aON:", new ItemStack(Material.INK_SACK, 1, (short) 10), new String[]{"§9클릭으로 OFF하세요."}));
			} else {
				String[] lore = {
						"포션 제어 여부에 대해 설정합니다.", 
						"ON시 섬에서 플레이어가", 
						"포션 효과를 얻을 수 없게 합니다.", 
						"방문자는 소지한 포션 효과를 잃게됩니다.", 
						"", 
						"§aON:§2 포션 효과 비허용", 
						"§c§lOFF:§4 포션 효과 허용"
				};
				
				inv.setItem(39, plugin.util.createDisplayItem("§c§n포션 제어", new ItemStack(Material.MILK_BUCKET), lore));
				inv.setItem(40, plugin.util.createDisplayItem("§cOFF:", new ItemStack(Material.INK_SACK, 1, (short) 8), new String[]{"§9클릭으로 ON하세요."}));
			}
		}
		
		users.put(p.getName(), type);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 54) return;
		
		Player p = (Player) e.getWhoClicked();
		GUIType gui = users.get(p.getName());
		final ProtectedRegion region = plugin.getIsleRegion(p);
		int slot = e.getRawSlot();
		
		if (gui == GUIType.MAIN) {
			
			if (slot == 7) {
				AnvilGUI anvilGui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
					
					@Override
					public void onAnvilClick(AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
							event.getPlayer().sendMessage("§c▒§r 오른쪽 버튼을 사용해주세요.");
							return;
						}
						
						if (event.getName().equals("닉네임") || event.getName().isEmpty()) {
							event.getPlayer().sendMessage("§c▒§r 플레이어 닉네임이 입력되지 않았습니다!");
							return;
						}
						
						Player target = plugin.util.getOnlinePlayer(event.getName());
						
						if (target == null) {
							event.getPlayer().sendMessage("§c▒§r <§a"+event.getName()+"§r> 플레이어는 존재하지 않습니다!");
							return;
						}
						
						if (event.getPlayer().equals(target)) {
							event.getPlayer().sendMessage("§c▒§r 자신을 내보낼 수는 없습니다!");
							return;
						}
						
						if (!plugin.getVisiters(region).contains(target)) {
							event.getPlayer().sendMessage("§c▒§r "+target.getName()+"님은 당신의 섬을 방문중이지 않습니다.");
							return;
						}
						
						target.sendMessage("§a▒§r 방문중인 섬의 주인이 당신을 추방했습니다.");
						target.teleport(plugin.getIsleSpawnLoc(target));
						
						event.getPlayer().sendMessage("§a▒§r "+target.getName()+"님이 섬에서 추방되었습니다.");
						
						event.setWillClose(true);
						event.setWillDestroy(true);
					}
				});
				
				anvilGui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("닉네임", new ItemStack(Material.NAME_TAG), 
						new String[]{}));
				anvilGui.open();
				return;
			}
			
			if (slot == 8) {
				p.closeInventory();
				plugin.blacklistGui.openGUI(p);
				
				return;
			}
			
			if (slot == 13) {
				if (region.getFlag(EfeFlag.ENTRANCE) == State.ALLOW) {
					region.setFlag(EfeFlag.ENTRANCE, State.DENY);
					
					UserData data = new UserData(p);
					
					for (Player visiter : plugin.getVisiters(region)) {
						if (visiter.equals(plugin.getIsleOwner(region)))
							continue;
						
						if (!data.getFriends().contains(visiter.getUniqueId())) {
							visiter.sendMessage("§a▒§r 방문중인 섬이 외부인 차단을 설정해 추방되었습니다.");
							visiter.teleport(plugin.getIsleSpawnLoc(visiter));
						}
					}
				} else {
					region.setFlag(EfeFlag.ENTRANCE, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 38) updateInv(e.getInventory(), region, GUIType.BLOCKS, p);
			if (slot == 40) updateInv(e.getInventory(), region, GUIType.FUN, p);
			if (slot == 42) updateInv(e.getInventory(), region, GUIType.PVP, p);
			
		} else if (gui == GUIType.BLOCKS) {
			
			if (slot == 38) updateInv(e.getInventory(), region, GUIType.MAIN, p);
			
			if (slot == 14 || slot == 13) {
				if (region.getFlag(DefaultFlag.BUILD) == State.ALLOW) {
					region.setFlag(DefaultFlag.BUILD, State.DENY);
					region.setFlag(DefaultFlag.BUILD.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
				} else {
					region.setFlag(DefaultFlag.BUILD, State.ALLOW);
					region.setFlag(DefaultFlag.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 16 || slot == 17) {
				if (region.getFlag(DefaultFlag.USE) == State.ALLOW) {
					region.setFlag(DefaultFlag.USE, State.DENY);
					region.setFlag(DefaultFlag.USE.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
				} else {
					region.setFlag(DefaultFlag.USE, State.ALLOW);
					region.setFlag(DefaultFlag.USE.getRegionGroupFlag(), RegionGroup.ALL);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 41 || slot == 40) {
				if (region.getFlag(DefaultFlag.CHEST_ACCESS) == State.ALLOW) {
					region.setFlag(DefaultFlag.CHEST_ACCESS, State.DENY);
					region.setFlag(DefaultFlag.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
				} else {
					region.setFlag(DefaultFlag.CHEST_ACCESS, State.ALLOW);
					region.setFlag(DefaultFlag.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.MEMBERS);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 43 || slot == 44) {
				if (region.getFlag(DefaultFlag.SNOW_MELT) == State.ALLOW) {
					region.setFlag(DefaultFlag.SNOW_MELT, State.DENY);
					region.setFlag(DefaultFlag.ICE_FORM, State.DENY);
					region.setFlag(DefaultFlag.ICE_MELT, State.DENY);
				} else {
					region.setFlag(DefaultFlag.SNOW_MELT, State.ALLOW);
					region.setFlag(DefaultFlag.ICE_FORM, State.ALLOW);
					region.setFlag(DefaultFlag.ICE_MELT, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
		} else if (gui == GUIType.FUN) {
			
			if (slot == 40) updateInv(e.getInventory(), region, GUIType.MAIN, p);
			
			if (slot == 10 || slot == 9) {
				if (region.getFlag(EfeFlag.FLY) == State.ALLOW) {
					region.setFlag(EfeFlag.FLY, State.DENY);
					
					for (Player all : plugin.getVisiters(region)) {
						all.setAllowFlight(false);
					}
				} else {
					region.setFlag(EfeFlag.FLY, State.ALLOW);
					
					for (Player all : plugin.getVisiters(region)) {
						all.setAllowFlight(true);
					}
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 37 || slot == 36) {
				if (region.getFlag(DefaultFlag.FIRE_SPREAD) == State.ALLOW) {
					region.setFlag(DefaultFlag.FIRE_SPREAD, State.DENY);
					region.setFlag(DefaultFlag.LAVA_FIRE, State.DENY);
				} else {
					region.setFlag(DefaultFlag.FIRE_SPREAD, State.ALLOW);
					region.setFlag(DefaultFlag.LAVA_FIRE, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 16 || slot == 17) {
				if (region.getFlag(DefaultFlag.GRASS_SPREAD) == State.ALLOW) {
					region.setFlag(DefaultFlag.MUSHROOMS, State.DENY);
					region.setFlag(DefaultFlag.GRASS_SPREAD, State.DENY);
					region.setFlag(DefaultFlag.MYCELIUM_SPREAD, State.DENY);
					region.setFlag(DefaultFlag.VINE_GROWTH, State.DENY);
				} else {
					region.setFlag(DefaultFlag.MUSHROOMS, State.ALLOW);
					region.setFlag(DefaultFlag.GRASS_SPREAD, State.ALLOW);
					region.setFlag(DefaultFlag.MYCELIUM_SPREAD, State.ALLOW);
					region.setFlag(DefaultFlag.VINE_GROWTH, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 43 || slot == 44) {
				if (region.getFlag(DefaultFlag.WATER_FLOW) == State.ALLOW) {
					region.setFlag(DefaultFlag.WATER_FLOW, State.DENY);
					region.setFlag(DefaultFlag.LAVA_FLOW, State.DENY);
				} else {
					region.setFlag(DefaultFlag.WATER_FLOW, State.ALLOW);
					region.setFlag(DefaultFlag.LAVA_FLOW, State.ALLOW);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
		} else if (gui == GUIType.PVP) {
			
			if (slot == 42) updateInv(e.getInventory(), region, GUIType.MAIN, p);
			
			if (slot == 10 || slot == 9) {
				if (region.getFlag(DefaultFlag.PVP) == State.ALLOW) {
					region.setFlag(DefaultFlag.PVP, State.DENY);
					region.setFlag(DefaultFlag.PVP.getRegionGroupFlag(), RegionGroup.ALL);
				} else {
					region.setFlag(DefaultFlag.PVP, State.ALLOW);
					region.setFlag(DefaultFlag.PVP.getRegionGroupFlag(), RegionGroup.ALL);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 12 || slot == 13) {
				if (region.getFlag(EfeFlag.INSTANT_KILL) == State.ALLOW)
					region.setFlag(EfeFlag.INSTANT_KILL, State.DENY);
				else
					region.setFlag(EfeFlag.INSTANT_KILL, State.ALLOW);
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 37 || slot == 36) {
				if (region.getFlag(EfeFlag.SKILL) == State.ALLOW)
					region.setFlag(EfeFlag.SKILL, State.DENY);
				else
					region.setFlag(EfeFlag.SKILL, State.ALLOW);
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
			
			if (slot == 39 || slot == 40) {
				if (region.getFlag(EfeFlag.NO_POTION) == State.ALLOW) {
					region.setFlag(EfeFlag.NO_POTION, State.DENY);
					region.setFlag(DefaultFlag.POTION_SPLASH, State.ALLOW);
					region.setFlag(DefaultFlag.POTION_SPLASH.getRegionGroupFlag(), RegionGroup.ALL);
					
					for (Player all : plugin.getVisiters(region)) {
						for (PotionEffect effect : all.getActivePotionEffects()) {
							all.removePotionEffect(effect.getType());
						}
					}
				} else {
					region.setFlag(EfeFlag.NO_POTION, State.ALLOW);
					region.setFlag(DefaultFlag.POTION_SPLASH, State.DENY);
					region.setFlag(DefaultFlag.POTION_SPLASH.getRegionGroupFlag(), RegionGroup.ALL);
				}
				
				updateInv(e.getInventory(), region, gui, p);
				
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				return;
			}
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.containsKey(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public enum GUIType {
		MAIN, BLOCKS, FUN, PVP
	}
}