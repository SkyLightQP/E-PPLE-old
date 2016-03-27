package me.efe.efequest.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import me.efe.efeisland.IslandUtils;
import me.efe.efequest.EfeQuest;
import me.efe.efequest.quest.Quest;
import me.efe.efequest.quest.QuestLoader;
import me.efe.efequest.quest.UserData;
import me.efe.efeserver.EfeServer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class QuestGUI implements Listener {
	public EfeQuest plugin;
	public HashMap<String, GUIData> users = new HashMap<String, GUIData>();
	
	public QuestGUI(EfeQuest plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		Inventory inv = plugin.getServer().createInventory(null, 9*6, "§5▒§r 퀘스트 목록");
		QuestComparator comparator = new QuestComparator(p);
		List<Quest> list = new ArrayList<Quest>(QuestLoader.quests);
		
		Collections.sort(list, comparator);
		GUIData data = new GUIData(0, list);
		
		refresh(inv, p, data);
		
		users.put(p.getName(), data);
		p.openInventory(inv);
	}
	
	public void refresh(Inventory inv, Player p, GUIData gui) {
		UserData data = QuestLoader.getUserData(p);
		
		inv.clear();
		
		for (int i : new int[]{0, 1, 2, 3, 4, 5, 46, 47, 48, 49, 50, 51, 52}) 
			inv.setItem(i, plugin.util.createDisplayItem(" ", new ItemStack(Material.WOOD_BUTTON), new String[]{}));
		
		inv.setItem(6, plugin.util.createDisplayItem("§a§n수행 가능한 퀘스트", new ItemStack(Material.WRITTEN_BOOK), new String[]{"새로 수행 가능한 퀘스트만 확인합니다."}));
		inv.setItem(7, plugin.util.createDisplayItem("§a§n완료되지 않은 퀘스트", new ItemStack(Material.WRITTEN_BOOK), new String[]{"완료되지 않은 퀘스트만 확인합니다."}));
		inv.setItem(8, plugin.util.createDisplayItem("§a§n전체 퀘스트", new ItemStack(Material.ENCHANTED_BOOK), new String[]{"모든 퀘스트 목록을 확인합니다."}));
		
		int amount = gui.quests.size() - gui.page * 36;
		
		for (int i = 0; i < amount; i ++) {
			Quest quest = gui.quests.get(i);
			
			String name = "§e§n"+quest.getName();
			String[] lore = null;
			
			if (data.isPossible(quest.getIdentity())) {
				if (QuestLoader.canStart(p, quest)) {
					NPC startNPC = CitizensAPI.getNPCRegistry().getById(quest.getStartNPC());
					
					lore = new String[]{
							"시작 NPC: "+QuestLoader.getNPCName(startNPC)+" §8["+locToString(startNPC.getStoredLocation())+"]",
							"필요 Lv: "+quest.getLevelRequired()
					};
				} else {
					List<String> list = new ArrayList<String>();
					
					NPC startNPC = CitizensAPI.getNPCRegistry().getById(quest.getStartNPC());
					
					list.add("시작 NPC: "+QuestLoader.getNPCName(startNPC));
					list.add("필요 Lv: "+quest.getLevelRequired());
					
					if (!quest.getNeedQuests().isEmpty()) {
						list.add("필요 퀘스트: ");
						
						for (int j : quest.getNeedQuests()) {
							Quest need = QuestLoader.getQuest(j);
							
							if (data.isEnded(j))
								list.add("  §a\u2611§r "+need.getName());
							else
								list.add("  §c\u2610§r "+need.getName());
						}
					}
					
					lore = list.toArray(new String[list.size()]);
				}
			} else if (data.isAccepted(quest.getIdentity())) {
				List<String> list = new ArrayList<String>();
				
				NPC startNPC = CitizensAPI.getNPCRegistry().getById(quest.getStartNPC());
				NPC quitNPC = CitizensAPI.getNPCRegistry().getById(quest.getQuitNPC());
				
				list.add("시작 NPC: "+QuestLoader.getNPCName(startNPC));
				list.add("종료 NPC: "+QuestLoader.getNPCName(quitNPC)+" §8["+locToString(quitNPC.getStoredLocation())+"]");
				list.add("필요 Lv: "+quest.getLevelRequired());
				
				if (!quest.getGoals().isEmpty()) {
					list.add("목표: ");
					
					for (int j = 0; j < quest.getGoals().size(); j ++) {
						String[] split = quest.getGoals().get(j).split("\\|");
						
						if (split[0].startsWith("I:")) {
							Object obj = data.getData(quest.getIdentity(), j);
							
							int k = (obj != null) ? (int) obj : 0;
							int need = Integer.parseInt(split[0].substring(2));
							
							if (k >= need) 
								list.add("  §a\u2611§r "+split[2]+" §e("+k+"/"+need+")");
							else 
								list.add("  §c\u2610§r "+split[2]+" §e(§6"+k+"§e/"+need+")");
							
						} else if (split[0].equals("B")) {
							Object obj = data.getData(quest.getIdentity(), j);
							
							boolean k = (obj != null) ? (boolean) obj : false;
							
							if (k) 
								list.add("  §a\u2611§r "+split[2]);
							else 
								list.add("  §c\u2610§r "+split[2]);
							
						} else if (split[0].equals("N")) {
							list.add("  §e\u2610§r "+split[2]);
						}
					}
				}
				
				lore = list.toArray(new String[list.size()]);
			} else if (data.isEnded(quest.getIdentity())) {
				List<String> list = new ArrayList<String>();
				
				NPC startNPC = CitizensAPI.getNPCRegistry().getById(quest.getStartNPC());
				NPC quitNPC = CitizensAPI.getNPCRegistry().getById(quest.getQuitNPC());
				
				list.add("시작 NPC: "+QuestLoader.getNPCName(startNPC));
				list.add("종료 NPC: "+QuestLoader.getNPCName(quitNPC));
				list.add("필요 Lv: "+quest.getLevelRequired());
				
				if (!quest.getGoals().isEmpty()) {
					list.add("목표: ");
					
					for (int j = 0; j < quest.getGoals().size(); j ++) {
						String[] split = quest.getGoals().get(j).split("\\|");
						
						list.add("  §a\u2611§r "+split[2]);
					}
				}
				
				lore = list.toArray(new String[list.size()]);
			}
			
			short durability = (short) ((data.isPossible(quest.getIdentity())) ? (QuestLoader.canStart(p, quest) ? 5 : 13) : 
				(data.isAccepted(quest.getIdentity())) ? 4 : 
				(QuestLoader.isEndable(p, quest)) ? 3 : 2);

			ItemStack item = plugin.util.createDisplayItem(name, new ItemStack(Material.STAINED_CLAY, 1, durability), lore);
			
			inv.addItem(item);
		}
		
		if (gui.page == 0)
			inv.setItem(45, plugin.util.createDisplayItem("§c이전 페이지가 없습니다.", new ItemStack(Material.EMPTY_MAP), new String[]{}));
		else
			inv.setItem(45, plugin.util.createDisplayItem("§a§l<<", new ItemStack(Material.MAP), new String[]{}));
		
		if (amount - gui.quests.size() <= 0)
			inv.setItem(53, plugin.util.createDisplayItem("§c다음 페이지가 없습니다.", new ItemStack(Material.EMPTY_MAP), new String[]{}));
		else
			inv.setItem(53, plugin.util.createDisplayItem("§a§l>>", new ItemStack(Material.MAP), new String[]{}));
		
		inv.setItem(49, plugin.util.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), new String[]{}));
	}
	
	private String locToString(Location loc) {
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		
		String name = IslandUtils.getIsleName(loc);
		
		return (!name.isEmpty()) ? IslandUtils.toDisplay(name)+": "+x+", "+y+", "+z : x+", "+y+", "+z;
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 54) return;
		
		Player p = (Player) e.getWhoClicked();
		UserData data = QuestLoader.getUserData(p);
		GUIData gui = users.get(p.getName());
		QuestComparator comparator = new QuestComparator(p);
		
		if (e.getRawSlot() == 6) {
			List<Quest> list = new ArrayList<Quest>();
			
			for (int id : data.getPossibleQuests())
				if (QuestLoader.canStart(p, QuestLoader.getQuest(id)))
					list.add(QuestLoader.getQuest(id));
			
			Collections.sort(list, comparator);
			
			gui.page = 0;
			gui.quests = list;
			
			refresh(e.getInventory(), p, gui);
		} else if (e.getRawSlot() == 7) {
			List<Quest> list = new ArrayList<Quest>();
			
			for (int id : data.getPossibleQuests())
				list.add(QuestLoader.getQuest(id));
			for (int id : data.getAcceptedQuests())
				list.add(QuestLoader.getQuest(id));
			
			Collections.sort(list, comparator);
			
			gui.page = 0;
			gui.quests = list;
			
			refresh(e.getInventory(), p, gui);
		} else if (e.getRawSlot() == 8) {
			List<Quest> list = new ArrayList<Quest>(QuestLoader.quests);
			
			Collections.sort(list, comparator);
			
			gui.page = 0;
			gui.quests = list;
			
			refresh(e.getInventory(), p, gui);
		} else if (e.getRawSlot() == 45 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§a§l◀")) {
			gui.page --;
			
			refresh(e.getInventory(), p, gui);
		} else if (e.getRawSlot() == 53 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§l▶")) {
			gui.page ++;
			
			refresh(e.getInventory(), p, gui);
		} else if (e.getRawSlot() == 49) {
			p.closeInventory();
			EfeServer.getInstance().mainGui.openGUI(p);
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.containsKey(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
	
	public class GUIData {
		public int page;
		public List<Quest> quests;
		
		public GUIData(int page, List<Quest> quests) {
			this.page = page;
			this.quests = quests;
		}
	}
	
	private class QuestComparator implements Comparator<Quest> {
		private final Player p;
		
		public QuestComparator(Player p) {
			this.p = p;
		}
		
		@Override
		public int compare(Quest quest0, Quest quest1) {
			UserData data = QuestLoader.getUserData(p);
			boolean pos0 = data.isPossible(quest0.getIdentity()) && QuestLoader.canStart(p, quest0);
			boolean pos1 = data.isPossible(quest1.getIdentity()) && QuestLoader.canStart(p, quest1);
			boolean acc0 = data.isAccepted(quest0.getIdentity());
			boolean acc1 = data.isAccepted(quest1.getIdentity());
			boolean end0 = acc0 && QuestLoader.isEndable(p, quest0);
			boolean end1 = acc1 && QuestLoader.isEndable(p, quest1);
			boolean ended0 = data.isEnded(quest0.getIdentity());
			boolean ended1 = data.isEnded(quest1.getIdentity());
			int lv0 = quest0.getLevelRequired();
			int lv1 = quest1.getLevelRequired();
			String name0 = quest0.getName();
			String name1 = quest1.getName();
			
			return (pos0 && !pos1) ? -1 : (!pos0 && pos1) ? 1 : 
				(end0 && !end1) ? -1 : (!end0 && end1) ? 1 : 
					(acc0 && !acc1) ? -1 : (!acc0 && acc1) ? 1 : 
						(!ended0 && ended1) ? -1 : (ended0 && !ended1) ? 1 : 
							(lv0 < lv1) ? -1 : (lv0 > lv1) ? 1 : 
								(name0.compareTo(name1));
		}

		@Override
		public Comparator<Quest> reversed() {
			return null;
		}

		@Override
		public Comparator<Quest> thenComparing(Comparator<? super Quest> other) {
			return null;
		}

		@Override
		public <U> Comparator<Quest> thenComparing(Function<? super Quest, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
			return null;
		}

		@Override
		public <U extends Comparable<? super U>> Comparator<Quest> thenComparing(Function<? super Quest, ? extends U> keyExtractor) {
			return null;
		}

		@Override
		public Comparator<Quest> thenComparingInt(ToIntFunction<? super Quest> keyExtractor) {
			return null;
		}

		@Override
		public Comparator<Quest> thenComparingLong(ToLongFunction<? super Quest> keyExtractor) {
			return null;
		}

		@Override
		public Comparator<Quest> thenComparingDouble(ToDoubleFunction<? super Quest> keyExtractor) {
			return null;
		}
	}
}