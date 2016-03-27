package me.efe.efecommunity;

import me.efe.efecommunity.listeners.FriendListener;
import me.efe.efecommunity.listeners.PartyListener;
import me.efe.efecommunity.listeners.PostListener;
import me.efe.efegear.EfeUtil;
import me.efe.efegear.util.VaultHooker;
import me.efe.efeisland.EfeIsland;
import me.efe.efeserver.EfeServer;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EfeCommunity extends JavaPlugin {
	private static EfeCommunity instance;
	public EfeUtil util;
	
	public FriendListener friendListener;
	public PostListener postListener;
	public PartyListener partyListener;
	
	public VaultHooker vault;
	public EfeIsland efeIsland;
	
	@Override
	public void onDisable() {
		util.logDisable();
	}
	
	@Override
	public void onEnable() {
		instance = this;
		util = new EfeUtil(this);
		util.logEnable();
		
		friendListener = new FriendListener(this);
		postListener = new PostListener(this);
		partyListener = new PartyListener(this);
		
		util.register(friendListener);
		util.register(postListener);
		util.register(partyListener);
		
		vault = EfeServer.getInstance().vault;
		
		efeIsland = (EfeIsland) getServer().getPluginManager().getPlugin("EfeIsland");
	}
	
	public static EfeCommunity getInstance() {
		return instance;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("친구") || l.equalsIgnoreCase("friend")) {
			try {
				Player p = (Player) s;
				
				friendListener.openGUI(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (l.equalsIgnoreCase("우편함") || l.equalsIgnoreCase("우편") || l.equalsIgnoreCase("메일함") || l.equalsIgnoreCase("메일") || l.equalsIgnoreCase("post")) {
			try {
				Player p = (Player) s;
				
				postListener.openGUI(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (l.equalsIgnoreCase("efecommunity") && s.isOp()) {
			if (a.length == 0) {
				s.sendMessage("§a▒§r /efecommunity <player> <sender> <title> <message>");
				return false;
			}
			
			OfflinePlayer player = util.getOfflinePlayer(a[0]);
			String sender = a[1];
			String title = a[2];
			String message = a[3];
			ItemStack item = ((Player) s).getItemInHand();
			
			Post post;
			
			if (item != null && item.getType() != Material.AIR) {
				post = Post.getBuilder()
						.setSender(sender)
						.setMessage(title, message)
						.setItems(new ItemStack[]{item.clone()})
						.build();
			} else {
				post = Post.getBuilder()
						.setSender(sender)
						.setMessage(title, message)
						.build();
			}
			
			Post.sendPost(player, post);
			
			s.sendMessage("§a▒§r 우편물이 전송되었습니다.");
		}
		
		return false;
	}
}