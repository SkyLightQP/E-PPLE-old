package me.efe.efeserver.additory;

import java.util.List;

import me.efe.efecommunity.UserData;
import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.titlemaker.TitleManager;
import me.efe.unlimitedrpg.party.Party;
import me.efe.unlimitedrpg.party.PartyAPI;
import me.efe.unlimitedrpg.playerinfo.PlayerInfoClickEvent;
import me.efe.unlimitedrpg.playerinfo.PlayerInfoEvent;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerInfoListener implements Listener {
	public EfeServer plugin;
	
	public PlayerInfoListener(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void open(PlayerInfoEvent e) {
		e.setGUISize(4);
		
		int[] i = {27, 18, 9, 0};
		for (int j = 0; j < 4; j ++) {
			e.getGUI().setItem(i[j], e.getTarget().getInventory().getArmorContents()[j]);
		}
		
		ItemStack skull = plugin.util.createDisplayItem("§b"+e.getTarget().getName(), new ItemStack(Material.SKULL_ITEM, 1, (short) 3), new String[]{});
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		
		meta.setOwner(e.getTarget().getName());
		skull.setItemMeta(meta);
		
		e.getGUI().setItem(13, skull);
		
		
		e.getGUI().setItem(20, plugin.util.createDisplayItem("§aLv. "+e.getTarget().getLevel(), new ItemStack(Material.EXP_BOTTLE), 
				new String[]{"EXP: [§9"+(int)(e.getTarget().getExp()*100)+"§7%]"}));
		
		e.getGUI().setItem(22, plugin.util.createDisplayItem("User", new ItemStack(Material.ENDER_PEARL), new String[]{"일반 유저"}));
		
		if (PermissionsEx.getUser(e.getTarget()).inGroup("moderator"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("§bModerator", new ItemStack(Material.EYE_OF_ENDER), new String[]{"중재자"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("appler"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("§cAppler", new ItemStack(Material.APPLE), new String[]{"후원자"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("golden_appler"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("§6G. Appler", new ItemStack(Material.GOLDEN_APPLE), new String[]{"후원자"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("supporter"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("§a§lSupporter", new ItemStack(Material.REDSTONE), new String[]{"서포터"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("sub_admin"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("§e§lSub. ADMIN", new ItemStack(Material.GOLDEN_APPLE), new String[]{"부 관리자"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("admin"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("§6§lADMIN", new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), new String[]{"관리자"}));
		
		
		List<String> list = TitleManager.getTitles(e.getTarget());
		
		String[] lore;
		
		if (list.isEmpty()) {
			lore = new String[]{"없음"};
		} else {
			for (int j = 0; j < list.size(); j ++) {
				list.set(j, plugin.util.replaceColors(list.get(j)));
			}
			
			lore = list.toArray(new String[list.size()]);
		}
		
		e.getGUI().setItem(24, plugin.util.createDisplayItem("§9칭호", new ItemStack(Material.ENCHANTED_BOOK), lore));
		
		
		if (!e.getTarget().equals(e.getPlayer())) {
			UserData fData = new UserData(e.getPlayer());
			
			e.getGUI().setItem(8, plugin.util.createDisplayItem("§a/친구", new ItemStack(Material.COOKIE), 
					fData.getFriends().contains(e.getTarget().getUniqueId()) ? 
							new UserData(e.getTarget()).getFriends().contains(e.getPlayer().getUniqueId()) ? 
									new String[]{"서로 친구입니다."} : 
										new String[]{"친구로 등록된 유저입니다."} : 
											new String[]{"클릭하면 친구로 등록합니다."}));
			
			
			Party tParty = PartyAPI.getJoinedParty(e.getTarget());
			Party pParty = PartyAPI.getJoinedParty(e.getPlayer());
			
			if (tParty == null && pParty != null && pParty.getOwner().equals(e.getPlayer())) lore = new String[]{"§9클릭하면 초대합니다."};
			if (tParty == null && (pParty == null || !pParty.getOwner().equals(e.getPlayer()))) lore = new String[]{"가입한 파티가 없습니다."};
			if (tParty != null) lore = new String[]{"가입한 파티가 있습니다."};
			if (tParty != null && tParty == pParty) lore = new String[]{"같은 파티에 소속되어있습니다."};
			
			e.getGUI().setItem(17, plugin.util.createDisplayItem("§d/파티", new ItemStack(Material.CAKE), lore));
			
			
			PlayerData tData = PlayerData.get(e.getTarget());
			
			e.getGUI().setItem(26, plugin.util.createDisplayItem("§e/거래 "+e.getTarget().getName(), new ItemStack(Material.CHEST), new String[]{
				"클릭하면 거래를 신청합니다."
			}));
			
			
			e.getGUI().setItem(35, plugin.util.createDisplayItem("§b/섬", new ItemStack(Material.SAND), 
					tData.hasIsland() ? new String[]{"§9클릭하면 이동합니다."} : new String[]{"섬이 없습니다."}));
		} else {
			e.getGUI().setItem(8, plugin.util.createDisplayItem("§a/친구", new ItemStack(Material.COOKIE), 
					new String[]{"친구 리스트를 편집하고", "친구에 대한 정보를 확인합니다."}));
			e.getGUI().setItem(17, plugin.util.createDisplayItem("§d/파티", new ItemStack(Material.CAKE), 
					new String[]{"새로운 파티를 개설하거나", "가입한 파티에 대해 설정합니다."}));
			
			
			PlayerData tData = PlayerData.get(e.getTarget());
			
			e.getGUI().setItem(26, plugin.util.createDisplayItem("§e/귓속말 <ON/OFF>", new ItemStack(Material.BOOK_AND_QUILL), new String[]{
				(tData.getOptionWhisper()) ? "귓속말: 허용" : "귓속말: 비허용", "", "§9클릭하면 스위치합니다."
			}));
			
			
			e.getGUI().setItem(35, plugin.util.createDisplayItem("§b/섬", new ItemStack(Material.SAND), 
					new String[]{"자신의 섬으로 이동하거나", "섬의 옵션을 설정합니다."}));
		}
	}
	
	@EventHandler
	public void click(PlayerInfoClickEvent e) {
		if (!e.getPlayer().equals(e.getTarget())) {
			switch (e.getSlotNum()) {
			case 8:
				UserData data = new UserData(e.getPlayer());
				
				if (data.getFriends().contains(e.getTarget().getUniqueId())) return;
				
				if (data.getFriends().size() > 40) {
					e.getPlayer().sendMessage("§c▒§r 최대 친구수는 40명입니다!");
					return;
				}
				
				data.addFriend(e.getTarget().getUniqueId());
				plugin.efeIsland.getIsleRegion(e.getPlayer()).getMembers().addPlayer(e.getTarget().getUniqueId());
				
				e.getPlayer().sendMessage("§a▒§r §b§n"+e.getTarget().getName()+"§r님이 친구로 등록되었습니다!");
				
				e.getGUI().setItem(8, plugin.util.createDisplayItem("§a/친구", new ItemStack(Material.COOKIE), 
						data.getFriends().contains(e.getTarget().getUniqueId()) ? 
								new UserData(e.getTarget()).getFriends().contains(e.getPlayer().getUniqueId()) ? 
										new String[]{"서로 친구입니다."} : 
											new String[]{"친구로 등록된 유저입니다."} : 
												new String[]{"클릭하면 친구로 등록합니다."}));
				break;
			case 17:
				Party tParty = PartyAPI.getJoinedParty(e.getTarget());
				Party pParty = PartyAPI.getJoinedParty(e.getPlayer());
				
				if (tParty != null || pParty == null || !pParty.getOwner().equals(e.getPlayer())) return;
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "파티 초대 "+e.getTarget().getName());
				break;
			case 26:
				e.getPlayer().closeInventory();
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "거래 "+e.getTarget().getName());
				break;
			case 35:
				e.getPlayer().closeInventory();
				
				IslandUtils.teleportIsland(e.getPlayer(), e.getTarget());
				break;
			}
		} else {
			switch (e.getSlotNum()) {
			case 8:
				e.getPlayer().closeInventory();
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "친구");
				break;
			case 17:
				e.getPlayer().closeInventory();
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "파티");
				break;
			case 26:
				PlayerData data = PlayerData.get(e.getTarget());
				
				data.setOptionWhisper(!data.getOptionWhisper());
				
				e.getGUI().setItem(26, plugin.util.createDisplayItem("§e/귓속말 <ON/OFF>", new ItemStack(Material.BOOK_AND_QUILL), new String[]{
					(data.getOptionWhisper()) ? "귓속말: 허용" : "귓속말: 비허용", "", "§9클릭하면 스위치합니다."
				}));
				
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				break;
			case 35:
				e.getPlayer().closeInventory();
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "섬");
				break;
			}
		}
	}
}