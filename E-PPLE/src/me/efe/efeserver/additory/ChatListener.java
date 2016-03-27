package me.efe.efeserver.additory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.unlimitedrpg.party.Party;
import me.efe.unlimitedrpg.party.PartyAPI;
import mkremins.fanciful.FancyMessage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatListener implements Listener {
	public EfeServer plugin;
	public HashMap<UUID, UUID> whispers = new HashMap<UUID, UUID>();
	public HashMap<UUID, Party> partyChats = new HashMap<UUID, Party>();
	public List<String> bannedWords = new ArrayList<String>();
	
	public ChatListener(EfeServer plugin) {
		this.plugin = plugin;
		
		bannedWords.add("ㅅㅂ");
		bannedWords.add("ㅄ");
		bannedWords.add("ㅂㅅ");
		bannedWords.add("ㅈㄴ");
		bannedWords.add("시발");
		bannedWords.add("병신");
		bannedWords.add("존나");
		bannedWords.add("개새끼");
		bannedWords.add("개새");
		bannedWords.add("새끼");
		bannedWords.add("씨발");
		bannedWords.add("애미");
		bannedWords.add("좆");
		bannedWords.add("섹스");
	}
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent e) {
		e.setFormat(" %s §7> §r%s");
		
		
		if (PermissionsEx.getUser(e.getPlayer()).inGroup("golden_appler"))
			e.setFormat(" §cä§r"+e.getFormat().substring(1));
		else if (PermissionsEx.getUser(e.getPlayer()).inGroup("appler"))
			e.setFormat(" §cå§r"+e.getFormat().substring(1));
		if (PermissionsEx.getUser(e.getPlayer()).inGroup("supporter"))
			e.setFormat(" §aş§r"+e.getFormat().substring(1));
		if (e.getPlayer().hasPermission("epple.admin"))
			e.setFormat(" §dAdmin|§r"+e.getFormat());
		
		for (String words : bannedWords) {
			String replace = "";
			
			for (int i = 0; i < words.length(); i ++)
				replace += '*';
			
			e.setMessage(e.getMessage().replaceAll(words, replace));
		}
		
//		e.setMessage(e.getMessage()
//				.replaceAll("e-pple.kr", "%이플%").replaceAll("e-pple.com", "%이플컴%")
//				.replaceAll("[a-zA-Z-_]{1,20}[.][a-zA-Z-_]{1,20}", "§c**DOMAIN**§r")
//				.replaceAll("%이플%", "e-pple.kr").replaceAll("%이플컴%", "e-pple.com"));
		
		Iterator<Player> it = e.getRecipients().iterator();
		
		while (it.hasNext()) {
			Player next = it.next();
			
			if (PlayerData.get(next).getOptionChat() && (e.getPlayer().getWorld() != next.getWorld() || e.getPlayer().getLocation().distance(next.getLocation()) >= 300.0D)) {
				it.remove();
			}
		}
		
		if (e.getMessage().startsWith("@")) {
			e.setCancelled(true);
			
			plugin.getServer().dispatchCommand(e.getPlayer(), "ㅍ "+e.getMessage().substring(1));
			return;
		}
		
		if (partyChats.containsKey(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			
			Party joined = PartyAPI.getJoinedParty(e.getPlayer());
			if (joined == null) {
				partyChats.remove(e.getPlayer().getUniqueId());
				e.getPlayer().sendMessage("§a▒§r 가입한 파티가 없어 파티 대화가 해제되었습니다.");
			}
			
			plugin.getServer().dispatchCommand(e.getPlayer(), "ㅍ "+e.getMessage());
			return;
		}
		
		if (whispers.containsKey(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			
			Player target = plugin.getServer().getPlayer(whispers.get(e.getPlayer().getUniqueId()));
			
			if (target == null) {
				e.getPlayer().sendMessage("§c▒§r 귓속말 상대가 오프라인 상태입니다.");
				
				new FancyMessage("§c▒§r ")
				.then("§b§n귓속말 모드 해제하기")
					.command("귓속말 해제")
					.tooltip("§b/귓속말 해제")
				.send(e.getPlayer());
				
				return;
			}
			
			if (!PlayerData.get(target).getOptionWhisper()) {
				e.getPlayer().sendMessage("§c▒§r 상대가 귓속말 거부 상태입니다.");
				
				new FancyMessage("§c▒§r ")
				.then("§b§n귓속말 모드 해제하기")
					.command("귓속말 해제")
					.tooltip("§b/귓속말 해제")
				.send(e.getPlayer());
				
				return;
			}
			
			e.getPlayer().sendMessage(" §e→"+target.getName()+" §6> §r"+e.getMessage());
			target.sendMessage(" §e"+e.getPlayer().getName()+"§e→ §6> §r"+e.getMessage());
			
			return;
		}
		
		PlayerData data = PlayerData.get(e.getPlayer());
		if (!data.hasTip("chat") && Math.random() <= 0.2) {
			e.getPlayer().sendMessage("§a▒§r §e§l[Tip]§r 친목적 대화는 §b/귓속말§r이나 §b/파티채팅§r을 사용합시다.");
			data.addTip("chat");
		}
	}
}