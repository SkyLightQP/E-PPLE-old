package me.efe.efequest.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import me.efe.efecommunity.Post;
import me.efe.efegear.util.VaultHooker;
import me.efe.efequest.quest.QuestLoader;
import me.efe.efeserver.EfeServer;
import me.efe.efetutorial.TutorialState;
import me.efe.unlimitedrpg.customexp.CustomExpAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatTrigger {
	public static String[] getAction(String content, Player p, NPC npc) {
		List<String> list = new ArrayList<String>();
		VaultHooker vault = new VaultHooker();
		boolean sound = false;
		
		for (String str : content.split("\\|")) list.add(str);
		
		vault.setupEconomy();
		
		for (int i = 0; i < list.size(); i ++) {
			String value = list.get(i)
					.replaceAll("%player%", p.getName())
					.replaceAll("%money%", vault.getBalance(p)+"");
			
			if (value.startsWith("%name[")) {
				String val = value.replace("%name[", "").replace("]", "");
				if (val.equals("npc")) val = QuestLoader.getNPCName(npc);
				if (val.equals("p")) val = p.getName();
				
				list.set(i, "§b『"+val+"§b』");
				continue;
			}
			
			if (value.startsWith("%sound[")) {
				String[] val = value.replace("%sound[", "").replace("]", "").split("-");
				p.playSound(p.getLocation(), Sound.valueOf(val[0]), 1.0F, Float.parseFloat(val[1]));
				
				sound = true;
				
				list.set(i, "null");
				continue;
			}
			
			if (value.startsWith("%amountItem[")) {
				String val = value.replace("%amountItem[", "").replace("]", "");
				ItemStack item = QuestLoader.getItem(val);
				
				list.set(i, getItemAmount(p, item)+"");
				continue;
			}
			
			if (value.startsWith("%giveItem[")) {
				String[] val = value.replace("%giveItem[", "").replace("]", "").split("-");
				String[] split = val[0].split(":");
				ItemStack item = QuestLoader.getItem(split[0]);
				item.setAmount(Integer.parseInt(val[1]));
				
				if (split.length > 1)
					item.setDurability(Short.parseShort(split[1]));
				
				if (p.getInventory().firstEmpty() == -1) {
					Post post = Post.getBuilder()
							.setSender(npc.getFullName())
							.setMessage("퀘스트 아이템", "인벤토리가 부족해 우편함으로 전송되었습니다.")
							.setItems(new ItemStack[]{item})
							.build();
					Post.sendPost(p, post);
					
					p.sendMessage("§a▒§r 인벤토리가 부족해 아이템이 우편함으로 전송되었습니다.");
				} else {
					p.getInventory().addItem(item);
				}
				
				list.set(i, "null");
				continue;
			}
			
			if (value.startsWith("%takeItem[")) {
				String[] val = value.replace("%takeItem[", "").replace("]", "").split("-");
				String[] split = val[0].split(":");
				ItemStack item = QuestLoader.getItem(split[0]);
				int amount = Integer.parseInt(val[1]);
				
				if (split.length > 1)
					item.setDurability(Short.parseShort(split[1]));
				
				if (!hasItem(p, item, amount)) {
					p.sendMessage("§c▒§r §e[퀘스트 오류]§r 필요한 아이템이 부족해 퀘스트를 수행할 수 없습니다.");
					p.closeInventory();
					return null;
				} else {
					removeItem(p, item, amount);
				}
				
				list.set(i, "null");
				continue;
			}
			
			if (value.startsWith("%giveExp[")) {
				String val = value.replace("%giveExp[", "").replace("]", "");
				
				CustomExpAPI.giveExp(p, Integer.parseInt(val));
				
				list.set(i, "null");
				continue;
			}
			
			if (value.startsWith("%giveMoney[")) {
				String val = value.replace("%giveMoney[", "").replace("]", "");
				
				vault.give(p, Double.parseDouble(val));
				
				list.set(i, "null");
				continue;
			}
			
			if (value.startsWith("%takeMoney[")) {
				String val = value.replace("%takeMoney[", "").replace("]", "");
				
				vault.take(p, Double.parseDouble(val));
				
				list.set(i, "null");
				continue;
			}
			
			if (value.startsWith("%setTutorialState[")) {
				String val = value.replace("%setTutorialState[", "").replace("]", "");
				
				try {
					TutorialState.set(p, TutorialState.class.getField(val.toUpperCase()).getInt(0));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				list.set(i, "null");
				continue;
			}
			
			if (value.equals("%upgradeIsland[]")) {
				EfeServer.getInstance().efeIsland.builder.upgrade(p);
				
				list.set(i, "null");
				continue;
			}
			
			list.set(i, "  "+value);
		}
		
		ListIterator<String> it = list.listIterator();
		while (it.hasNext()) {
			if (it.next().equals("null")) {
				it.remove();
			}
		}
		
		if (!sound) {
			p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 1.5F);
		}
		
		return list.toArray(new String[list.size()]);
	}
	
	public static int getItemAmount(Player p, ItemStack target) {
		int amount = 0;
		
		for (ItemStack item : p.getInventory().getContents()) {
			if (item == null || !item.getType().equals(target.getType())) continue;
			
			if (item.isSimilar(target)) {
				amount += item.clone().getAmount();
			}
		}
		
		return amount;
	}
	
	public static boolean hasItem(Player p, ItemStack item, int amount) {
		return getItemAmount(p, item.clone()) >= amount;
	}
	
	public static void removeItem(Player p, ItemStack target, int amount) {
		ItemStack[] contents = p.getInventory().getContents().clone();
		
		for (int i = 0; i < contents.length; i ++) {
			ItemStack item = contents[i];
			
			if (item == null || !item.getType().equals(target.getType())) continue;
			if (!item.isSimilar(target)) continue;
			
			if (item.clone().getAmount() > amount) {
				item.setAmount(item.clone().getAmount() - amount);
				amount = 0;
			} else {
				amount -= item.clone().getAmount();
				item.setType(Material.AIR);
			}
			
			if (amount == 0) {
				break;
			}
		}
		
		p.getInventory().setContents(contents);
	}
}