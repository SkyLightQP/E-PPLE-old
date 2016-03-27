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
		
		ItemStack skull = plugin.util.createDisplayItem("��b"+e.getTarget().getName(), new ItemStack(Material.SKULL_ITEM, 1, (short) 3), new String[]{});
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		
		meta.setOwner(e.getTarget().getName());
		skull.setItemMeta(meta);
		
		e.getGUI().setItem(13, skull);
		
		
		e.getGUI().setItem(20, plugin.util.createDisplayItem("��aLv. "+e.getTarget().getLevel(), new ItemStack(Material.EXP_BOTTLE), 
				new String[]{"EXP: [��9"+(int)(e.getTarget().getExp()*100)+"��7%]"}));
		
		e.getGUI().setItem(22, plugin.util.createDisplayItem("User", new ItemStack(Material.ENDER_PEARL), new String[]{"�Ϲ� ����"}));
		
		if (PermissionsEx.getUser(e.getTarget()).inGroup("moderator"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("��bModerator", new ItemStack(Material.EYE_OF_ENDER), new String[]{"������"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("appler"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("��cAppler", new ItemStack(Material.APPLE), new String[]{"�Ŀ���"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("golden_appler"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("��6G. Appler", new ItemStack(Material.GOLDEN_APPLE), new String[]{"�Ŀ���"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("supporter"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("��a��lSupporter", new ItemStack(Material.REDSTONE), new String[]{"������"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("sub_admin"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("��e��lSub. ADMIN", new ItemStack(Material.GOLDEN_APPLE), new String[]{"�� ������"}));
		if (PermissionsEx.getUser(e.getTarget()).inGroup("admin"))
			e.getGUI().setItem(22, plugin.util.createDisplayItem("��6��lADMIN", new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), new String[]{"������"}));
		
		
		List<String> list = TitleManager.getTitles(e.getTarget());
		
		String[] lore;
		
		if (list.isEmpty()) {
			lore = new String[]{"����"};
		} else {
			for (int j = 0; j < list.size(); j ++) {
				list.set(j, plugin.util.replaceColors(list.get(j)));
			}
			
			lore = list.toArray(new String[list.size()]);
		}
		
		e.getGUI().setItem(24, plugin.util.createDisplayItem("��9Īȣ", new ItemStack(Material.ENCHANTED_BOOK), lore));
		
		
		if (!e.getTarget().equals(e.getPlayer())) {
			UserData fData = new UserData(e.getPlayer());
			
			e.getGUI().setItem(8, plugin.util.createDisplayItem("��a/ģ��", new ItemStack(Material.COOKIE), 
					fData.getFriends().contains(e.getTarget().getUniqueId()) ? 
							new UserData(e.getTarget()).getFriends().contains(e.getPlayer().getUniqueId()) ? 
									new String[]{"���� ģ���Դϴ�."} : 
										new String[]{"ģ���� ��ϵ� �����Դϴ�."} : 
											new String[]{"Ŭ���ϸ� ģ���� ����մϴ�."}));
			
			
			Party tParty = PartyAPI.getJoinedParty(e.getTarget());
			Party pParty = PartyAPI.getJoinedParty(e.getPlayer());
			
			if (tParty == null && pParty != null && pParty.getOwner().equals(e.getPlayer())) lore = new String[]{"��9Ŭ���ϸ� �ʴ��մϴ�."};
			if (tParty == null && (pParty == null || !pParty.getOwner().equals(e.getPlayer()))) lore = new String[]{"������ ��Ƽ�� �����ϴ�."};
			if (tParty != null) lore = new String[]{"������ ��Ƽ�� �ֽ��ϴ�."};
			if (tParty != null && tParty == pParty) lore = new String[]{"���� ��Ƽ�� �ҼӵǾ��ֽ��ϴ�."};
			
			e.getGUI().setItem(17, plugin.util.createDisplayItem("��d/��Ƽ", new ItemStack(Material.CAKE), lore));
			
			
			PlayerData tData = PlayerData.get(e.getTarget());
			
			e.getGUI().setItem(26, plugin.util.createDisplayItem("��e/�ŷ� "+e.getTarget().getName(), new ItemStack(Material.CHEST), new String[]{
				"Ŭ���ϸ� �ŷ��� ��û�մϴ�."
			}));
			
			
			e.getGUI().setItem(35, plugin.util.createDisplayItem("��b/��", new ItemStack(Material.SAND), 
					tData.hasIsland() ? new String[]{"��9Ŭ���ϸ� �̵��մϴ�."} : new String[]{"���� �����ϴ�."}));
		} else {
			e.getGUI().setItem(8, plugin.util.createDisplayItem("��a/ģ��", new ItemStack(Material.COOKIE), 
					new String[]{"ģ�� ����Ʈ�� �����ϰ�", "ģ���� ���� ������ Ȯ���մϴ�."}));
			e.getGUI().setItem(17, plugin.util.createDisplayItem("��d/��Ƽ", new ItemStack(Material.CAKE), 
					new String[]{"���ο� ��Ƽ�� �����ϰų�", "������ ��Ƽ�� ���� �����մϴ�."}));
			
			
			PlayerData tData = PlayerData.get(e.getTarget());
			
			e.getGUI().setItem(26, plugin.util.createDisplayItem("��e/�ӼӸ� <ON/OFF>", new ItemStack(Material.BOOK_AND_QUILL), new String[]{
				(tData.getOptionWhisper()) ? "�ӼӸ�: ���" : "�ӼӸ�: �����", "", "��9Ŭ���ϸ� ����ġ�մϴ�."
			}));
			
			
			e.getGUI().setItem(35, plugin.util.createDisplayItem("��b/��", new ItemStack(Material.SAND), 
					new String[]{"�ڽ��� ������ �̵��ϰų�", "���� �ɼ��� �����մϴ�."}));
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
					e.getPlayer().sendMessage("��c�ơ�r �ִ� ģ������ 40���Դϴ�!");
					return;
				}
				
				data.addFriend(e.getTarget().getUniqueId());
				plugin.efeIsland.getIsleRegion(e.getPlayer()).getMembers().addPlayer(e.getTarget().getUniqueId());
				
				e.getPlayer().sendMessage("��a�ơ�r ��b��n"+e.getTarget().getName()+"��r���� ģ���� ��ϵǾ����ϴ�!");
				
				e.getGUI().setItem(8, plugin.util.createDisplayItem("��a/ģ��", new ItemStack(Material.COOKIE), 
						data.getFriends().contains(e.getTarget().getUniqueId()) ? 
								new UserData(e.getTarget()).getFriends().contains(e.getPlayer().getUniqueId()) ? 
										new String[]{"���� ģ���Դϴ�."} : 
											new String[]{"ģ���� ��ϵ� �����Դϴ�."} : 
												new String[]{"Ŭ���ϸ� ģ���� ����մϴ�."}));
				break;
			case 17:
				Party tParty = PartyAPI.getJoinedParty(e.getTarget());
				Party pParty = PartyAPI.getJoinedParty(e.getPlayer());
				
				if (tParty != null || pParty == null || !pParty.getOwner().equals(e.getPlayer())) return;
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "��Ƽ �ʴ� "+e.getTarget().getName());
				break;
			case 26:
				e.getPlayer().closeInventory();
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "�ŷ� "+e.getTarget().getName());
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
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "ģ��");
				break;
			case 17:
				e.getPlayer().closeInventory();
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "��Ƽ");
				break;
			case 26:
				PlayerData data = PlayerData.get(e.getTarget());
				
				data.setOptionWhisper(!data.getOptionWhisper());
				
				e.getGUI().setItem(26, plugin.util.createDisplayItem("��e/�ӼӸ� <ON/OFF>", new ItemStack(Material.BOOK_AND_QUILL), new String[]{
					(data.getOptionWhisper()) ? "�ӼӸ�: ���" : "�ӼӸ�: �����", "", "��9Ŭ���ϸ� ����ġ�մϴ�."
				}));
				
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				break;
			case 35:
				e.getPlayer().closeInventory();
				
				plugin.getServer().dispatchCommand(e.getPlayer(), "��");
				break;
			}
		}
	}
}