package me.efe.efecommunity.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.efe.efecommunity.EfeCommunity;
import me.efe.efecommunity.Post;
import me.efe.efecommunity.UserData;
import me.efe.efetutorial.TutorialState;

public class PostListener implements Listener {
	public EfeCommunity plugin;
	public List<String> users = new ArrayList<String>();
	
	public PostListener(EfeCommunity plugin) {
		this.plugin = plugin;
	}
	
	public void openGUI(Player p) {
		if (TutorialState.get(p) < TutorialState.WELCOME_TO_POLARIS) {
			p.sendMessage("§c▒§r 튜토리얼을 먼저 마쳐주세요!");
			return;
		}
		
		Inventory inv = plugin.getServer().createInventory(null, 9*3, "§c▒§r 우편함");
		
		updateInv(p, inv);
		
		users.add(p.getName());
		p.openInventory(inv);
	}
	
	public void updateInv(Player p, Inventory inv) {
		inv.clear();
		
		UserData data = new UserData(p);
		
		for (int i = 0; i < 25; i ++) {
			if (i >= data.getPosts().size()) break;
			
			Post post = data.getPosts().get(i);
			
			String[] text = post.getText().split("\\|");
			text = append(text, "");
			if (post.getSender() != null) text = append(text, "§6from. "+post.getSender());
			text = append(text, "§c§l§m============================");
			text = append(text, "클릭으로 우편물을 삭제합니다.");
			text = append(text, "아이템이 첨부되어있을 경우 아이템도 함께 받습니다.");
			
			ItemStack icon = new ItemStack(Material.WRITTEN_BOOK);
			
			if (post.getItems() != null && !post.getItems().isEmpty())
				icon.setType(Material.STORAGE_MINECART);
			
			ItemStack item = plugin.util.createDisplayItem("§r"+post.getTitle(), icon, text);
			
			inv.setItem(i, item);
		}
		
		inv.setItem(25, plugin.util.createDisplayItem("§a§nHow to use?", new ItemStack(Material.SIGN), 
				new String[]{"받은 우편물을 확인하는 우편함입니다.", "우편물을 클릭해서 삭제할 수 있습니다."}));
		inv.setItem(26, plugin.util.createDisplayItem("§e메인 메뉴", new ItemStack(Material.NETHER_STAR), new String[]{}));
	}
	
	public String[] append(String[] arr, String element) {
	    final int N = arr.length;
	    arr = Arrays.copyOf(arr, N + 1);
	    arr[N] = element;
	    return arr;
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!users.contains(e.getWhoClicked().getName())) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
		
		Player p = (Player) e.getWhoClicked();
		UserData data = new UserData(p);
		
		if (e.getRawSlot() < 26) {
			Post post = data.getPosts().get(e.getRawSlot());
			
			if (post.getItems() != null && !post.getItems().isEmpty()) {
				ListIterator<ItemStack> it = post.getItems().listIterator();
				
				while (it.hasNext()) {
					ItemStack item = it.next();
					
					if (p.getInventory().firstEmpty() == -1) {
						post.writeYml();
						
						p.sendMessage("§c▒§r 인벤토리에 공간이 부족합니다!");
						return;
					} else {
						p.getInventory().addItem(item.clone());
					}
					
					it.remove();
					
					p.sendMessage("§a▒§r 우편물에서 <"+getItemName(item)+"> 아이템을 수거했습니다.");
				}
			}
			
			data.removePost(post);
			post.delete();
			
			updateInv(p, e.getInventory());
		}
	}
	
	public String getItemName(ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			return item.getItemMeta().getDisplayName()+"§r";
		} else {
			String input = item.getType().name().toLowerCase().replaceAll("_", " ");
			String output = Character.toUpperCase(input.charAt(0)) + input.substring(1);
			
			return output;
		}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		if (users.contains(e.getPlayer().getName())) {
			users.remove(e.getPlayer().getName());
		}
	}
}