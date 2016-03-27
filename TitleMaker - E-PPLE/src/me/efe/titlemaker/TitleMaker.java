package me.efe.titlemaker;

import java.util.ArrayList;
import java.util.List;

import me.efe.efecore.util.EfeUtils;
import me.efe.efecore.util.TabCompletionHelper;
import me.efe.efecore.util.UpdateChecker;
import me.efe.titlemaker.hologram.Hologrammer;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class TitleMaker extends JavaPlugin implements Listener {
	public String main = "��e[TitleMaker]��r ";
	
	public TitleGUI titleGui;
	public Hologrammer hologrammer;
	public TitleInfo titleInfo;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(titleGui = new TitleGUI(this), this);
		
		saveDefaultConfig();
		
		CommandSender sender = getServer().getConsoleSender();
		
		if (getConfig().getInt("config") != 50) {
			sender.sendMessage("��c[TitleMaker] ���� ������ Config�� ������Դϴ�!");
			sender.sendMessage("��c[TitleMaker] config.yml ���� ���� �� ������ �ٽ� �����ּ���.");
			
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.setEnabled(false);
			return;
		}
		
		TitleManager.autoMain = getConfig().getBoolean("use.auto-main");
		
		if (getConfig().getBoolean("use.hologram")) {
			if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
				sender.sendMessage("��c[TitleMaker] ProtocolLib �÷������� �߰ߵ��� �ʾҽ��ϴ�! [Config: 'use.hologram: true']");
			} else {
				getServer().getPluginManager().registerEvents(hologrammer = new Hologrammer(this), this);
				sender.sendMessage("[TitleMaker] ProtocolLib �÷����ΰ� ���������� ȣȯ�Ǿ����ϴ�!");
			}
		}
		
		if (getConfig().getBoolean("title-info.enable")) {
			if (getServer().getPluginManager().getPlugin("UnlimitedRPG") == null) {
				sender.sendMessage("��c[TitleMaker] UnlimitedRPG �÷������� �߰ߵ��� �ʾҽ��ϴ�! [Config: 'title-info.enable: true']");
				
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				getServer().getPluginManager().registerEvents(titleInfo = new TitleInfo(this), this);
				sender.sendMessage("[TitleMaker] UnlimitedRPG �÷����ΰ� ���������� ȣȯ�Ǿ����ϴ�!");
			}
		}
		
		if (getConfig().getBoolean("update-check")) {
			double now = Double.parseDouble(getDescription().getVersion());
			double newest = UpdateChecker.getVersion("TitleMaker");
			
			if (newest > now) {
				sender.sendMessage("��2[TitleMaker] TitleMaker ���ο� ������ �߰ߵǾ����ϴ�!");
				sender.sendMessage("��2[TitleMaker] v"+now+" �� v"+newest);
				sender.sendMessage("��2[TitleMaker] �÷������� �Ź������� ���Ƴ����ּ���!");
				sender.sendMessage("��2[TitleMaker] �� �� ���� Config�� �����ؼ� �Ź������� �ٽ� �������ּ���.");
				
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		getLogger().info(getDescription().getFullName()+" has been enabled!");
	}
	
	@Override
	public void onDisable() {
		if (getConfig().getBoolean("use.hologram")) {
			hologrammer.reset();
		}
		
		getLogger().info(getDescription().getFullName()+" has been disabled.");
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void chat(AsyncPlayerChatEvent e) {
		if (!getConfig().getBoolean("use.chat")) return;
		
		if (TitleManager.getMainTitle(e.getPlayer()) != null) {
			String title = TitleManager.getMainTitle(e.getPlayer());
			
			e.setFormat(title+"��r "+e.getFormat());
		}
		
		List<String> tags = getTagTitles(e.getPlayer());
		
		if (getConfig().getBoolean("use.title-tag") && !tags.isEmpty()) {
			for (String tag : tags) {
				e.setFormat(tag+"��r "+e.getFormat());
			}
		}
	}
	
	public List<String> getTagTitles(Player p) {
		List<String> list = new ArrayList<String>();
		
		for (ItemStack item : p.getInventory().getArmorContents()) {
			if (item == null || item.getType().equals(Material.AIR)) continue;
			if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) continue;
			
			for (String str : item.getItemMeta().getLore()) {
				if (!str.startsWith("��7Īȣ: ")) continue;
				
				list.add(str.replace("��7Īȣ: ", ""));
				break;
			}
		}
		
		return list;
	}
	
	@EventHandler
	public void useBook(PlayerInteractEvent e) {
		if (!getConfig().getBoolean("use.title-book")) return;
		if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		
		if (e.getItem() != null && isTitleBook(e.getItem())) {
			e.setCancelled(true);
			EfeUtils.player.updateInv(e.getPlayer());
			
			String title = e.getItem().getItemMeta().getLore().get(0).substring(2);
			
			if (TitleManager.getTitles(e.getPlayer()).contains(title)) {
				e.getPlayer().sendMessage(main+"�̹� �������� Īȣ�Դϴ�.");
				return;
			}
			
			List<String> list = TitleManager.getTitles(e.getPlayer());
			list.add(title);
			TitleManager.setTitles(e.getPlayer(), list);
			
			if (hologrammer != null) {
				hologrammer.apply(e.getPlayer());
			}
			
			e.getPlayer().setItemInHand(EfeUtils.item.getUsed(e.getItem(), e.getPlayer()));
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.PORTAL_TRAVEL, 1.0F, 1.0F);
			
			String message = EfeUtils.string.replaceColors(getConfig().getString("message.book-use").replaceAll("%title%", title+"��r"));
			
			e.getPlayer().sendMessage(main+message);
		}
	}
	
	public boolean isTitleBook(ItemStack item) {
		return item.getType().equals(Material.ENCHANTED_BOOK) && EfeUtils.item.containsLore(item, "��9��Ŭ���Ͽ� �� Īȣ�� �����մϴ�.");
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		if (!getConfig().getBoolean("newbie-title.enable")) return;
		
		if (!e.getPlayer().hasPlayedBefore()) {
			String title = getConfig().getString("newbie-title.title");
			
			if (TitleManager.getTitles(e.getPlayer()).contains(title)) return;
			
			List<String> list = TitleManager.getTitles(e.getPlayer());
			list.add(title);
			TitleManager.setTitles(e.getPlayer(), list);
			
			if (getConfig().getBoolean("newbie-title.auto-main")) {
				TitleManager.setMainTitle(e.getPlayer(), 0);
			}
			
			String message = EfeUtils.string.replaceColors(getConfig().getString("message.newbie").replaceAll("%title%", title+"��r"));
			
			e.getPlayer().sendMessage(main+message);
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
		List<String> list = new ArrayList<String>();
		
		if (l.equalsIgnoreCase("Īȣ") || l.equalsIgnoreCase("tm") || l.equalsIgnoreCase("titlemaker")) {
			if (a.length == 1) {
				list.add("���");
				list.add("��ǥ");
				list.add("�߰�");
				list.add("����");
				list.add("����");
				list.add("Ÿ��Ʋ��");
				list.add("Ÿ��Ʋ�±�");
			} else if ((a[0].equalsIgnoreCase("���") && s.hasPermission("title.view")) ||
					(a[0].equalsIgnoreCase("�߰�") && s.hasPermission("title.add")) ||
					(a[0].equalsIgnoreCase("����") && s.hasPermission("title.edit")) ||
					(a[0].equalsIgnoreCase("����") && s.hasPermission("title.remove"))) {
				
				for (Player all : EfeUtils.player.getOnlinePlayers()) {
					list.add(all.getName());
				}
			}
		}
		
		return TabCompletionHelper.getPossibleCompletions(a, list);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("Īȣ") || l.equalsIgnoreCase("tm") || l.equalsIgnoreCase("titlemaker")) {
			try {
				if (!s.isOp() || a.length == 0) {
					Player player = (Player) s;
					
					titleGui.openGUI(player);
					return true;
				}
				
				if (a[0].equalsIgnoreCase("�߰�")) {
					if (!s.hasPermission("title.add")) {
						s.sendMessage(main+"������ �����ϴ�. ��8[title.add]");
						return false;
					}
					
					OfflinePlayer t = EfeUtils.player.getOfflinePlayer(a[1]);
					
					if (t == null) {
						s.sendMessage(main+a[1]+"���� �������� �ʴ� �÷��̾��Դϴ�.");
						return false;
					}
					
					List<String> list = TitleManager.getTitles(t);
					String args = EfeUtils.string.getFinalArg(a, 2);
					String title = EfeUtils.string.replaceColors(args);
					
					if (list.contains(title)) {
						s.sendMessage(main+title+"��r Īȣ�� �̹� �������Դϴ�.");
						return false;
					}
					
					list.add(title);
					TitleManager.setTitles(t, list);
					
					if (hologrammer != null && t.isOnline()) {
						hologrammer.apply(t.getPlayer());
					}
					
					s.sendMessage(main+t.getName()+"�Կ��� "+title+"��r Īȣ�� �߰��Ǿ����ϴ�.");
					
					if (t.isOnline()) {
						if (!s.equals(t)) {
							String message = EfeUtils.string.replaceColors(getConfig().getString("message.receive").replaceAll("%title%", title+"��r"));
							t.getPlayer().sendMessage(main+message);
						}
					}
					
					return true;
				} else if (a[0].equalsIgnoreCase("����")) {
					if (!s.hasPermission("title.edit")) {
						s.sendMessage(main+"������ �����ϴ�. ��8[title.edit]");
						return false;
					}
					
					OfflinePlayer t = EfeUtils.player.getOfflinePlayer(a[1]);
					
					if (t == null) {
						s.sendMessage(main+a[1]+"���� �������� �ʴ� �÷��̾��Դϴ�.");
						return false;
					}
					
					List<String> list = TitleManager.getTitles(t);
					String args = EfeUtils.string.getFinalArg(a, 3);
					String title = EfeUtils.string.replaceColors(args);
					
					if (Integer.parseInt(a[2]) > list.size() || Integer.parseInt(a[2]) == 0) {
						s.sendMessage(main+"��ȿ���� ���� �����Դϴ�.");
						return false;
					}
					
					if (list.contains(title)) {
						s.sendMessage(main+title+"��r Īȣ�� �̹� �������Դϴ�.");
						return false;
					}
					
					String old = list.get(Integer.parseInt(a[2]) - 1);
					list.set(Integer.parseInt(a[2]) - 1, title);
					TitleManager.setTitles(t, list);
					
					if (hologrammer != null && t.isOnline()) {
						hologrammer.apply(t.getPlayer());
					}
					
					s.sendMessage(main+t.getName()+"���� "+Integer.parseInt(a[2])+"��° Īȣ�� "+title+"��r Īȣ�� �ٲ�����ϴ�.");
					
					if (t.isOnline()) {
						if (!s.equals(t)) {
							String message = EfeUtils.string.replaceColors(getConfig().getString("message.edit")
									.replaceAll("%number%", Integer.parseInt(a[2])+"")
									.replaceAll("%old%", old+"��r")
									.replaceAll("%title%", title+"��r"));
							t.getPlayer().sendMessage(main+message);
						}
					}
					
					return true;
				} else if (a[0].equalsIgnoreCase("����")) {
					if (!s.hasPermission("title.remove")) {
						s.sendMessage(main+"������ �����ϴ�. ��8[title.remove]");
						return false;
					}
					
					OfflinePlayer t = EfeUtils.player.getOfflinePlayer(a[1]);
					
					if (t == null) {
						s.sendMessage(main+a[1]+"���� �������� �ʴ� �÷��̾��Դϴ�.");
						return false;
					}
					
					List<String> list = TitleManager.getTitles(t);
					
					if (Integer.parseInt(a[2]) > list.size() || Integer.parseInt(a[2]) == 0) {
						s.sendMessage(main+"��ȿ���� ���� �����Դϴ�.");
						return false;
					}
					
					String title = list.get(Integer.parseInt(a[2]) - 1);
					list.remove(title);
					TitleManager.setTitles(t, list);
					
					if (TitleManager.getMainTitles().containsKey(t.getUniqueId()) && TitleManager.getMainTitles().get(t.getUniqueId()) >= list.size()) {
						TitleManager.setMainTitle(t, -1);
					}
					
					if (hologrammer != null && t.isOnline()) {
						hologrammer.apply(t.getPlayer());
					}
					
					s.sendMessage(main+t.getName()+"���� "+title+"��r Īȣ�� �����Ǿ����ϴ�.");
					
					if (t.isOnline()) {
						if (!s.equals(t)) {
							String message = EfeUtils.string.replaceColors(getConfig().getString("message.remove")
									.replaceAll("%number%", Integer.parseInt(a[2])+"")
									.replaceAll("%title%", title+"��r"));
							t.getPlayer().sendMessage(main+message);
						}
					}
					
					return true;
				} else if (a[0].equalsIgnoreCase("��ǥ")) {
					if (!(s instanceof Player)) {
						s.sendMessage(main+"�÷��̾ ������ ��ɾ��Դϴ�.");
						return false;
					}
					
					if (a.length == 1) {
						s.sendMessage(main+"��b/"+l+" ��ǥ ��3<Num>��r: �ڽ��� ��ǥ Īȣ�� <Num>��°�� ����");
						s.sendMessage(main+"��b/"+l+" ��ǥ ������r: ��ϵ� ��ǥ Īȣ ����");
						return true;
					}
					
					Player p = (Player) s;
					List<String> list = TitleManager.getTitles(p);
					
					int index = EfeUtils.string.isInteger(a[1]) ? Integer.parseInt(a[1]) - 1 : a[1].equals("����") ? -1 : -2;
					
					if (index == -1) {
						TitleManager.setMainTitle(p, -1);
						p.sendMessage(main+"��ǥ Īȣ�� �����Ǿ����ϴ�.");
						return false;
					}
					
					if (index < 0 || index > list.size() || index == -2) {
						s.sendMessage(main+"��ȿ���� ���� �����Դϴ�.");
						return false;
					}
					
					String title = list.get(index);
					TitleManager.setMainTitle(p, index);
					
					if (hologrammer != null) {
						hologrammer.apply(p);
					}
					
					p.sendMessage(main+"����� ��ǥ Īȣ�� "+title+"��r Īȣ�� �����Ǿ����ϴ�.");
					
					return true;
				} else if (a[0].equalsIgnoreCase("Ÿ��Ʋ��")) {
					if (!s.hasPermission("title.add")) {
						s.sendMessage(main+"������ �����ϴ�. ��8[title.add]");
						return false;
					}
					
					if (!(s instanceof Player)) {
						s.sendMessage(main+"�÷��̾ ������ ��ɾ��Դϴ�.");
						return false;
					}
					
					Player p = (Player) s;
					
					String name = EfeUtils.string.replaceColors(getConfig().getString("message.book-name"));
					ItemStack item = EfeUtils.item.createItem(name, new ItemStack(Material.ENCHANTED_BOOK), new String[]{
						"��r"+EfeUtils.string.replaceColors(EfeUtils.string.getFinalArg(a, 1)), 
						"", 
						"��9��Ŭ���Ͽ� �� Īȣ�� �����մϴ�."});
					p.getInventory().addItem(item);
					
					p.sendMessage(main+"��ȯ�� �Ϸ�Ǿ����ϴ�.");
					
					if (!getConfig().getBoolean("use.title-book")) {
						p.sendMessage(main+"��c[!]��r Config���� 'use.title-book'�� true�� �ؾ� Ÿ��Ʋ���� ����� �� �ֽ��ϴ�.");
					}
					
					return true;
				} else if (a[0].equalsIgnoreCase("Ÿ��Ʋ�±�")) {
					if (!(s instanceof Player)) {
						s.sendMessage(main+"�÷��̾ ������ ��ɾ��Դϴ�.");
						return false;
					}
					
					Player p = (Player) s;
					
					if (!p.hasPermission("title.add")) {
						s.sendMessage(main+"������ �����ϴ�. ��8[title.add]");
						return false;
					}
					
					if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
						s.sendMessage(main+"���� �������? �����Դϴ�.");
						return false;
					}
					
					ItemMeta meta = p.getItemInHand().getItemMeta();
					if (meta.hasLore()) {
						List<String> lore = meta.getLore();
						for (int i = 0; i < lore.size(); i ++) {
							if (lore.get(i).startsWith("��7Īȣ: ")) {
								lore.set(i, "��7Īȣ: "+EfeUtils.string.replaceColors(EfeUtils.string.getFinalArg(a, 1)));
								break;
							}
						}
						meta.setLore(lore);
						p.getItemInHand().setItemMeta(meta);
					} else {
						EfeUtils.item.addLore(p.getItemInHand(), "��7Īȣ: "+EfeUtils.string.replaceColors(EfeUtils.string.getFinalArg(a, 1)));
					}
					
					p.sendMessage(main+"�±� ������ �Ϸ�Ǿ����ϴ�.");
					
					if (!getConfig().getBoolean("use.title-tag")) {
						p.sendMessage(main+"��c[!]��r Config���� 'use.title-tag'�� true�� �ؾ� Ÿ��Ʋ���� ����� �� �ֽ��ϴ�.");
					}
					
					return true;
				} else if (a[0].equalsIgnoreCase("���")) {
					OfflinePlayer t;
					int page;
					
					if (a.length > 1 && !EfeUtils.string.isInteger(a[1])) {
						if (!s.hasPermission("title.view")) {
							s.sendMessage(main+"������ �����ϴ�. ��8[title.view]");
							return false;
						}
						
						t = EfeUtils.player.getOfflinePlayer(a[1]);
						page = (a.length > 2) ? Integer.parseInt(a[2]) : 1;
						
						if (t == null) {
							s.sendMessage(main+a[1]+"���� �������� �ʴ� �÷��̾��Դϴ�.");
							return false;
						}
						
					} else if (s instanceof Player) {
						t = (Player) s;
						page = (a.length > 1) ? Integer.parseInt(a[1]) : 1;
						
					} else {
						s.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/"+l+"]");
						
						return false;
					}
					
					List<String> titles = TitleManager.getTitles(t);
					
					if (titles.size() == 0) {
						s.sendMessage(main+"�������� Īȣ�� �����ϴ�.");
						return true;
					}
					
					int maxPage = titles.size() / 8 + ((titles.size() % 8 == 0) ? 0 : 1);
					page = (page > maxPage) ? maxPage : (page < 1) ? 1 : page;
					
					int start = (page - 1) * 8;
					int end = start + (titles.size() - page * 8 < 0 ? titles.size() % 8 : 8);
					
					s.sendMessage(main+"��6��m=====��6[ ��e"+t.getName()+"���� Īȣ ��b[Page: "+page+"/"+maxPage+"]��6 ]��m=====");
					
					for (int i = start; i < end; i ++) {
						if (titles.get(i) == null || titles.get(i) == "") break;
						
						s.sendMessage(main+"��a["+(i+1)+"]��7-��r "+titles.get(i));
					}
					
					s.sendMessage(main+"��6��m========��6[ ��eMade by Efe��6 ]��m========");
					
					return true;
				} else {
					s.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/"+l+"]");
					
					return false;
				}
			} catch (Exception ex) {
				s.sendMessage(main+"�߸��� ��ɾ��Դϴ�. ��8[/"+l+"]");
				
				return false;
			}
		}
		
		return false;
	}
}