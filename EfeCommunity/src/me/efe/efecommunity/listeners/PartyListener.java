package me.efe.efecommunity.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.efe.efecommunity.EfeCommunity;
import me.efe.efegear.util.Token;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;
import me.efe.unlimitedrpg.UnlimitedRPG;
import me.efe.unlimitedrpg.party.Party;
import me.efe.unlimitedrpg.party.PartyAPI;
import me.efe.unlimitedrpg.playerinfo.PlayerInfo;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PartyListener implements Listener {
	public EfeCommunity plugin;
	public HashMap<String, Type> users = new HashMap<String, Type>();
	
	public PartyListener(EfeCommunity plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Party party = PartyAPI.getJoinedParty(p);
		
		if (party != null) {
			Inventory inv = plugin.getServer().createInventory(null, 9*6, "��d�ơ�r ��Ƽ");
			
			inv.setItem(13, plugin.util.createDisplayItem("��d��l��"+party.getName()+"����5 ��Ƽ", new ItemStack(Material.CAKE), 
					new String[]{"��Ƽ��: "+party.getOwner().getName(), "��Ƽ��: "+party.getMembers().size()+"/9��"}));
			
			inv.setItem(22, getSkull(party.getOwner(), true));
			
			int[] slots = {21, 23, 20, 24, 19, 25, 18, 26};
			
			int i = 0;
			for (OfflinePlayer m : party.getMembers()) {
				if (m.equals(party.getOwner())) continue;
				
				inv.setItem(slots[i], getSkull(m, false));
				
				i ++;
			}
			
			inv.setItem(0, plugin.util.createDisplayItem("��e\"@�Ҹ�\", \"/��\"���� ��Ƽ ä���� �����ϸ�", new ItemStack(Material.SIGN), 
					new String[]{"��e\"/��Ƽä�� ����\"���� �ڵ� ��Ƽ ä���� �����մϴ�.", "��eģ���� ��ȭ�� ��Ƽ ä���� �̿��ϵ��� �սô�!"}));
			inv.setItem(52, plugin.util.createDisplayItem("��c�ݱ�", new ItemStack(Material.WOOD_DOOR), new String[]{}));
			inv.setItem(53, plugin.util.createDisplayItem("��e���� �޴�", new ItemStack(Material.NETHER_STAR), new String[]{}));
			
			if (party.getOwner().equals(p)) {
				inv.setItem(37, plugin.util.createDisplayItem("��a�ʴ�", new ItemStack(Material.WATER_BUCKET), 
						new String[]{"��Ƽ�� ������ �ʴ��մϴ�."}));
				inv.setItem(39, plugin.util.createDisplayItem("��a�߹�", new ItemStack(Material.LAVA_BUCKET), 
						new String[]{"��Ƽ���� ��Ƽ���� �߹��ŵ�ϴ�."}));
				inv.setItem(41, plugin.util.createDisplayItem("��a�絵", new ItemStack(Material.MILK_BUCKET), 
						new String[]{"�ٸ� ��Ƽ������ ��Ƽ���� �絵�մϴ�."}));
				inv.setItem(43, plugin.util.createDisplayItem("��aŻ��", new ItemStack(Material.BUCKET), 
						new String[]{"��Ƽ�� Ż���մϴ�.", "��Ƽ���� �ڵ����� �絵�˴ϴ�."}));
				
				p.openInventory(inv);
				users.put(p.getName(), Type.BASIC_MASTER);
			} else {
				inv.setItem(40, plugin.util.createDisplayItem("��aŻ��", new ItemStack(Material.BUCKET), 
						new String[]{"��Ƽ�� Ż���մϴ�.", "��Ƽ���� �ڵ����� �絵�˴ϴ�."}));
				
				p.openInventory(inv);
				users.put(p.getName(), Type.BASIC_USER);
			}
			
			return;
		}
		
		HashMap<String, Party> map = UnlimitedRPG.getInstance().party.requests;
		if (map.containsKey(Token.getToken(p))) {
			Inventory inv = plugin.getServer().createInventory(null, 9*3, "��d�ơ�r ��Ƽ - ��Ƽ �ʴ�");
			party = map.get(Token.getToken(p));
			
			inv.setItem(13, plugin.util.createDisplayItem("��a��Ƽ �ʴ�", getSkull(party.getOwner(), false), 
					new String[]{"��d��l��"+party.getName()+"����r ��Ƽ���� ����� �ʴ��߽��ϴ�!"}));
			
			int[] accepts = {0, 1, 2, 9, 10, 11, 18, 19, 20};
			int[] denys = {6, 7, 8, 15, 16, 17, 24, 25, 26};
			
			for (int i = 0; i < 9; i ++) inv.setItem(accepts[i], plugin.util.createDisplayItem("��a����", new ItemStack(Material.EMERALD_BLOCK), new String[]{}));
			for (int i = 0; i < 9; i ++) inv.setItem(denys[i], plugin.util.createDisplayItem("��c����", new ItemStack(Material.REDSTONE_BLOCK), new String[]{}));
			
			p.openInventory(inv);
			users.put(p.getName(), Type.INVITED);
			return;
		}
		
		Inventory inv = plugin.getServer().createInventory(null, 9*3, "��d�ơ�r ��Ƽ - ���Ե� ��Ƽ�� �����ϴ�.");
		
		inv.setItem(13, plugin.util.createDisplayItem("��a��Ƽ ����", new ItemStack(Material.CAKE), new String[]{"���Ⱑ �Ұ����ϸ�,", "�ѱ� 8��, ���� 16�� �����Դϴ�."}));
		inv.setItem(25, plugin.util.createDisplayItem("��c�ݱ�", new ItemStack(Material.WOOD_DOOR), new String[]{}));
		inv.setItem(26, plugin.util.createDisplayItem("��e���� �޴�", new ItemStack(Material.NETHER_STAR), new String[]{}));
		
		p.openInventory(inv);
		users.put(p.getName(), Type.CREATE);
	}
	
	public ItemStack getSkull(OfflinePlayer p, boolean isMaster) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		
		if (p.isOnline()) {
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwner(p.getName());
			
			if (isMaster) meta.setDisplayName("��e[M]��6 "+p.getName());
			else meta.setDisplayName("��6"+p.getName());
			
			List<String> lore = new ArrayList<String>();
			lore.add("��aLv."+p.getPlayer().getLevel()+" ��2["+((int)(p.getPlayer().getExp() * 100))+"%]");
			
			meta.setLore(lore);
			
			item.setItemMeta(meta);
			
			return item;
		} else {
			item.setDurability((short) 2);
			
			ItemMeta meta = item.getItemMeta();
			
			if (isMaster) meta.setDisplayName("��e[M]��6 "+p.getName());
			else meta.setDisplayName("��6"+p.getName());
			
			List<String> lore = new ArrayList<String>();
			lore.add("��cOffline");
			
			meta.setLore(lore);
			
			item.setItemMeta(meta);
			
			return item;
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
		
		Player p = (Player) e.getWhoClicked();
		Type type = users.get(p.getName());
		
		if (type == Type.BASIC_MASTER) {
			if (e.getRawSlot() >= 9*6) return;
			
			AnvilGUI gui;
			
			switch (e.getRawSlot()) {
			case 37:
				p.closeInventory();
				
				p.sendMessage("��a�ơ�r ��Ƽ�� �ʴ��� �÷��̾� �г����� �Է����ּ���!");
				
				gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
					
					@Override
					public void onAnvilClick(final AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
							event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
							return;
						}
						
						if (event.getName().equals("�г���") || event.getName().isEmpty()) {
							event.getPlayer().sendMessage("��c�ơ�r �÷��̾� �г����� �Էµ��� �ʾҽ��ϴ�!");
							return;
						}
						
						plugin.getServer().dispatchCommand(event.getPlayer(), "��Ƽ �ʴ� "+event.getName());
						
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								plugin.getServer().dispatchCommand(event.getPlayer(), "��Ƽ");
							}
						}, 1L);
						
						event.setWillClose(true);
						event.setWillDestroy(true);
					}
				});
				
				gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("�г���", new ItemStack(Material.WATER_BUCKET), 
						new String[]{}));
				gui.open();
				
				break;
			case 39:
				p.closeInventory();
				
				p.sendMessage("��a�ơ�r ��Ƽ���� �߹��� ��Ƽ�� �г����� �Է����ּ���!");
				
				gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
					
					@Override
					public void onAnvilClick(final AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
							event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
							return;
						}
						
						if (event.getName().equals("�г���") || event.getName().isEmpty()) {
							event.getPlayer().sendMessage("��c�ơ�r �÷��̾� �г����� �Էµ��� �ʾҽ��ϴ�!");
							return;
						}
						
						plugin.getServer().dispatchCommand(event.getPlayer(), "��Ƽ �߹� "+event.getName());
						
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								plugin.getServer().dispatchCommand(event.getPlayer(), "��Ƽ");
							}
						}, 1L);
						
						event.setWillClose(true);
						event.setWillDestroy(true);
					}
				});
				
				gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("�г���", new ItemStack(Material.LAVA_BUCKET), 
						new String[]{}));
				gui.open();
				
				break;
			case 41:
				p.closeInventory();
				
				p.sendMessage("��a�ơ�r ��Ƽ���� �絵���� ��Ƽ�� �г����� �Է����ּ���!");
				
				gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
					
					@Override
					public void onAnvilClick(final AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
							event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
							return;
						}
						
						if (event.getName().equals("�г���") || event.getName().isEmpty()) {
							event.getPlayer().sendMessage("��c�ơ�r �÷��̾� �г����� �Էµ��� �ʾҽ��ϴ�!");
							return;
						}
						
						plugin.getServer().dispatchCommand(event.getPlayer(), "��Ƽ �絵 "+event.getName());
						
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								plugin.getServer().dispatchCommand(event.getPlayer(), "��Ƽ");
							}
						}, 1L);
						
						event.setWillClose(true);
						event.setWillDestroy(true);
					}
				});
				
				gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("�г���", new ItemStack(Material.MILK_BUCKET), 
						new String[]{}));
				gui.open();
				
				break;
			case 43:
				p.closeInventory();
				
				plugin.getServer().dispatchCommand(p, "��Ƽ Ż��");
				
				break;
			case 52:
				p.closeInventory();
				
				break;
			case 53:
				p.closeInventory();
				EfeServer.getInstance().mainGui.openGUI(p);
				
				break;
			default:
				if (e.getCurrentItem().getType().equals(Material.SKULL_ITEM) && e.getCurrentItem().getDurability() == 3) {
					SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
					Player t = plugin.util.getOnlinePlayer(meta.getOwner());
					
					if (t == null) return;
					
					p.closeInventory();
					
					PlayerInfo.openGUI(t, p);
				}
			}
		} else if (type == Type.BASIC_USER) {
			if (e.getRawSlot() >= 9*6) return;
			
			if (e.getRawSlot() == 40) {
				p.closeInventory();
				
				plugin.getServer().dispatchCommand(p, "��Ƽ Ż��");
			} else if (e.getRawSlot() == 52) {
				p.closeInventory();
			} else if (e.getRawSlot() == 53) {
				p.closeInventory();
				EfeServer.getInstance().mainGui.openGUI(p);
			}
			
		} else if (type == Type.CREATE) {
			if (e.getRawSlot() >= 9*3) return;
			
			if (e.getRawSlot() == 13) {
				p.closeInventory();
				
				p.sendMessage("��a�ơ�r ������ ��Ƽ�� �̸��� �Է����ּ���!");
				p.sendMessage("��a�ơ�r ��, ��c�����r�� ����� �� �����ϴ�.");
				
				AnvilGUI gui = new AnvilGUI(plugin, p, new AnvilGUI.AnvilClickEventHandler() {
					
					@Override
					public void onAnvilClick(final AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
							event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
							return;
						}
						
						if (event.getName().equals("��Ƽ��") || event.getName().isEmpty()) {
							event.getPlayer().sendMessage("��c�ơ�r ��Ƽ �̸��� �Էµ��� �ʾҽ��ϴ�!");
							return;
						}
						
						plugin.getServer().dispatchCommand(event.getPlayer(), "��Ƽ ���� "+event.getName());
						
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								plugin.getServer().dispatchCommand(event.getPlayer(), "��Ƽ");
							}
						}, 1L);
						
						event.setWillClose(true);
						event.setWillDestroy(true);
					}
				});
				
				gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, plugin.util.createDisplayItem("��Ƽ��", new ItemStack(Material.MILK_BUCKET), 
						new String[]{}));
				gui.open();
				
			} else if (e.getRawSlot() == 25) {
				p.closeInventory();
			} else if (e.getRawSlot() == 26) {
				p.closeInventory();
				EfeServer.getInstance().mainGui.openGUI(p);
			}
		} else if (type == Type.INVITED) {
			if (e.getRawSlot() >= 9*3) return;
			
			if (e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
				p.closeInventory();
				
				plugin.getServer().dispatchCommand(p, "��Ƽ ����");
				plugin.getServer().dispatchCommand(p, "��Ƽ");
			} else if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
				p.closeInventory();
				
				plugin.getServer().dispatchCommand(p, "��Ƽ ����");
				plugin.getServer().dispatchCommand(p, "��Ƽ");
			}
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.containsKey(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public enum Type {
		BASIC_MASTER, BASIC_USER, CREATE, INVITED
	}
}