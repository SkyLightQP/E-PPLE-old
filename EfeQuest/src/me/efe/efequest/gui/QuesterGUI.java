package me.efe.efequest.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.efe.efequest.EfeQuest;
import me.efe.efequest.quest.Quest;
import me.efe.efequest.quest.QuestLoader;
import me.efe.efequest.quest.UserData;
import me.efe.efeserver.PlayerData;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class QuesterGUI implements Listener {
	public EfeQuest plugin;
	public HashMap<String, GUIData> users = new HashMap<String, GUIData>();
	
	public QuesterGUI(EfeQuest plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p, NPC npc) {
		Inventory inv = plugin.getServer().createInventory(null, 9*6, "§6▒§r "+QuestLoader.getNPCName(npc));
		List<Quest> quests = QuestLoader.questMap.get(npc.getId());
		UserData data = QuestLoader.getUserData(p);
		
		if (quests == null || quests.isEmpty()) return;
		
		ItemStack item = plugin.util.createDisplayItem("§e§l"+npc.getFullName(), new ItemStack(Material.SKULL_ITEM, 1, (short) 3), new String[]{});
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner("MHF_Villager");
		item.setItemMeta(meta);
		
		inv.setItem(13, item);
		
		int[] slots = {19, 21, 23, 25, 37, 39, 41, 43};
		for (int i = 0; i < quests.size(); i ++) {
			Quest quest = quests.get(i);
			
			if (data.isEnded(quest.getIdentity())) {
				String[] contents = {
						"§6 【[Lv."+quest.getLevelRequired()+"] §e"+quest.getName()+"§6 】 ", 
						"", 
						"§5§l|§d§l| §b완료"};
				inv.setItem(slots[i], plugin.util.createDisplayItem("§a§l*§2 Quest §a#"+(i + 1)+"§2§l)", new ItemStack(Material.ENCHANTED_BOOK), contents));
			} else if (QuestLoader.isEndable(p, quest)) {
				if (quest.getQuitNPC() != npc.getId()) {
					String[] contents = {
							"§6 【[Lv."+quest.getLevelRequired()+"] §e"+quest.getName()+"§6 】 ", 
							"", 
							"§8§l>§7§l> §b진행중"};
					inv.setItem(slots[i], plugin.util.createDisplayItem("§a§l*§2 Quest §a#"+(i + 1)+"§2§l)", new ItemStack(Material.BOOK_AND_QUILL), contents));
				} else {
					String[] contents = {
							"§6 【 [Lv."+quest.getLevelRequired()+"] §e"+quest.getName()+"§6 】 ", 
							"", 
							"§6§l>§e§l> §b완료 가능 [Click!]"};
					inv.setItem(slots[i], plugin.util.createDisplayItem("§a§l*§2 Quest §a#"+(i + 1)+"§2§l)", new ItemStack(Material.WRITTEN_BOOK), contents));
				}
			} else if (data.isAccepted(quest.getIdentity())) {
				String[] contents = {
						"§6 【[Lv."+quest.getLevelRequired()+"] §e"+quest.getName()+"§6 】 ", 
						"", 
						"§8§l>§7§l> §b진행중"};
				inv.setItem(slots[i], plugin.util.createDisplayItem("§a§l*§2 Quest §a#"+(i + 1)+"§2§l)", new ItemStack(Material.BOOK_AND_QUILL), contents));
			} else if (data.isPossible(quest.getIdentity())) {
				ItemStack icon = new ItemStack(Material.BOOK);
				
				List<String> list = new ArrayList<String>();
				
				list.add("§6 【[Lv."+quest.getLevelRequired()+"] §e"+quest.getName()+"§6 】 ");
				list.add("");
				
				if (!QuestLoader.canStart(p, quest)) {
					list.add("§8§l>§7§l> §c수행 불가능");
					
					if (quest.getLevelRequired() > p.getLevel()) {
						list.add("§8§l>§7§l> §7필요 Lv: "+quest.getLevelRequired());
					}
					
					if (!quest.getNeedQuests().isEmpty()) {
						list.add("§8§l>§7§l> §7필요 퀘스트: ");
						
						for (int j : quest.getNeedQuests()) {
							Quest need = QuestLoader.getQuest(j);
							
							if (data.isEnded(j))
								list.add("    §a\u2611§r "+need.getName());
							else
								list.add("    §c\u2610§r "+need.getName());
						}
					}
					
				} else if (quest.getStartNPC() != npc.getId()) {
					list.add("§8§l>§7§l> §c시작 NPC가 아님");
				} else {
					list.add("§6§l>§e§l> §b수행 가능 [Click!]");
					icon.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 100);
				}
				
				inv.setItem(slots[i], plugin.util.createDisplayItem("§a§l*§2 Quest §a#"+(i + 1)+"§2§l)", icon, list.toArray(new String[list.size()])));
			}
		}
		
		p.openInventory(inv);
		users.put(p.getName(), new GUIData(plugin, npc, 0, null, null, p));
	}
	
	
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.containsKey(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getRawSlot() >= 54 || e.getRawSlot() == 13) return;
		
		Player p = (Player) e.getWhoClicked();
		int slot = e.getRawSlot();
		GUIData gui = users.get(p.getName());
		List<Quest> quests = QuestLoader.questMap.get(gui.getNPC().getId());
		
		if (e.getAction() == InventoryAction.NOTHING) return;
		if (plugin.util.containsLore(e.getCurrentItem().clone(), "§7§8§l>§7§l> §c수행 불가능")) return;
		if (plugin.util.containsLore(e.getCurrentItem().clone(), "§7§8§l>§7§l> §c시작 NPC가 아님")) return;
		
		if (gui.getQuest() == null) {
			int index = asQuest(slot);
			Quest quest = quests.get(index);
			List<String> chats = getChats(e.getCurrentItem(), quest);
			
			if (e.getCurrentItem().getType().equals(Material.ENCHANTED_BOOK) && gui.getNPC().getId() != quest.getStartNPC()) return;
			
			e.getInventory().setItem(slot, plugin.util.createDisplayItem("§e§n▷ "+quest.getName()+"", e.getCurrentItem().clone(), new String[]{""}));
			
			String[] lore = ChatTrigger.getAction(chats.get(0), p, gui.getNPC());
			
			for (int i = 0; i < lore.length; i ++) {
				lore[i] = "§7"+lore[i].replaceAll("§r", "§7");
			}
			
			users.put(p.getName(), new GUIData(plugin, gui.getNPC(), slot, quest, e.getInventory(), p));
			users.get(p.getName()).updateChatIndex();
			users.get(p.getName()).startTask(Arrays.asList(lore));
		} else {
			if (!gui.isTyped()) {
				gui.stopTyping();
				return;
			}
			
			List<String> chats = getChats(e.getCurrentItem(), gui.getQuest());
			
			if (!gui.canSkip() && gui.getChatIndex() != chats.size() - 1) {
				gui.cancelSkip();
				
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 0.5F);
				ActionBarAPI.sendActionBar(p, "§c대화 스킵은 퀘스트 진행에 방해를 줄 수 있습니다!");
				return;
			}
			
			if (gui.getSlot() != slot) {
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 0.5F);
				p.sendMessage("§c▒§r 이미 다른 퀘스트의 대화를 보고있습니다!");
				p.sendMessage("§c▒§r 진행중인 대화를 취소하려면 ESC를 눌러주세요.");
				return;
			}
			
			if (gui.getChatIndex() >= chats.size() - 1) {
				NPC npc = gui.getNPC();
				p.closeInventory();
				openGUI(p, npc);
				return;
			}
			
			if (!PlayerData.get(p).hasTip("quest")) {
				p.sendMessage("§a▒§r §e§l[Tip]§r 아이콘을 클릭하면 대화를 넘기고, 우클릭하면 뒤로 갑니다.");
				PlayerData.get(p).addTip("quest");
			}
			
			if (e.isRightClick() && gui.getChatIndex() != 0)
				gui.backChatIndex();
			else
				gui.updateChatIndex();
			
			String[] lore = ChatTrigger.getAction(chats.get(gui.getChatIndex()), p, gui.getNPC());
			if (lore == null) return;
			
			for (int i = 0; i < lore.length; i ++) {
				lore[i] = "§7"+lore[i].replaceAll("§r", "§7");
			}
			
			e.getInventory().setItem(slot, plugin.util.createDisplayItem("§e§n▷ "+gui.getQuest().getName()+"", e.getCurrentItem().clone(), new String[]{""}));
			
			gui.startTask(Arrays.asList(lore));
			
			if (gui.getChatIndex() == chats.size() - 1) {
				UserData data = QuestLoader.getUserData(p);
				Material icon = e.getCurrentItem().getType();
				
				e.getInventory().getItem(slot).setType(icon);
				
				if (icon.equals(Material.BOOK)) {
					data.acceptQuest(gui.getQuest().getIdentity());
					
					p.sendMessage("§a▒§r 퀘스트를 받았습니다. §7["+gui.getQuest().getName()+"]");
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
					
					e.getInventory().getItem(slot).setType(Material.BOOK_AND_QUILL);
					e.getInventory().getItem(slot).removeEnchantment(Enchantment.SILK_TOUCH);
				}
				
				if (icon.equals(Material.WRITTEN_BOOK)) {
					data.endQuest(gui.getQuest().getIdentity());
					
					p.sendMessage("§a▒§r 퀘스트를 완료했습니다! §7["+gui.getQuest().getName()+"]");
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
					
					e.getInventory().getItem(slot).setType(Material.ENCHANTED_BOOK);
				}
			}
		}
	}
	
	public int asQuest(int slot) {
		if (slot == 19) return 0;
		if (slot == 21) return 1;
		if (slot == 23) return 2;
		if (slot == 25) return 3;
		if (slot == 37) return 4;
		if (slot == 39) return 5;
		if (slot == 41) return 6;
		if (slot == 43) return 7;
		return -1;
	}
	
	public List<String> getChats(ItemStack item, Quest quest) {
		if (item.getType().equals(Material.BOOK)) return quest.getAskChats();
		if (item.getType().equals(Material.BOOK_AND_QUILL)) return quest.getWaitChats();
		if (item.getType().equals(Material.WRITTEN_BOOK)) return quest.getRewardChats();
		if (item.getType().equals(Material.ENCHANTED_BOOK)) return quest.getThankChats();
		return null;
	}
	
	public Material getQuestIcon(UserData data, Quest quest, Player p) {
		int id = quest.getIdentity();
		if (data.isEnded(id)) return Material.ENCHANTED_BOOK;
		if (QuestLoader.isEndable(p, quest)) return Material.WRITTEN_BOOK;
		if (data.isAccepted(id)) return Material.BOOK_AND_QUILL;
		if (data.isPossible(id)) return Material.BOOK;
		return null;
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.containsKey(e.getPlayer().getName())) {
			users.get(e.getPlayer().getName()).stopTask();
			
			users.remove(e.getPlayer().getName());
		}
	}
	
	public static class TimeTask extends BukkitRunnable {
		private final Player p;
		private final Inventory inv;
		private final int slot;
		private final String[] text;
		private int letter = 0;
		private int line = 0;
		private int skip = 0;
		private boolean typed;
		
		public TimeTask(Player p, Inventory inv, int slot, String[] text) {
			this.p = p;
			this.inv = inv;
			this.slot = slot;
			this.text = text;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			letter ++;
			skip ++;
			
			ItemStack item = inv.getItem(slot).clone();
			ItemMeta meta = item.getItemMeta();
			
			if (skip%20 == 1) meta.setDisplayName(meta.getDisplayName().replace("▷", "▶"));
			else if (skip%10 == 1) meta.setDisplayName(meta.getDisplayName().replace("▶", "▷"));
			
			if (!typed)
				meta.setLore(getNextLore());
			
			item.setItemMeta(meta);
			inv.setItem(slot, item);
			
			p.updateInventory();
		}
		
		private List<String> getNextLore() {
			List<String> list = new ArrayList<String>();
			
			if (text[line].length() <= letter) {
				letter = 0;
				line ++;
				
				if (text.length <= line) {
					typed = true;
					
					return Arrays.asList(text);
				}
			}
			
			for (int i = 0; i <= line; i ++) {
				if (i == line) {
					String content = "";
					
					for (int j = 0; j <= letter; j ++) {
						char c = text[i].toCharArray()[j];
						
						if (j == letter && (c == ' ' || c == '§')) {
							letter ++;
							return getNextLore();
						}
						
						content += c;
					}
					
					list.add(content);
				} else {
					list.add(text[i]);
				}
			}
			
			return list;
		}
		
		public boolean isTyped() {
			return this.typed;
		}
		
		public void stopTyping() {
			this.typed = true;
		}
		
		public boolean canSkip() {
			return skip >= 10;
		}
		
		public void initSkip() {
			skip = 0;
		}
	}
}