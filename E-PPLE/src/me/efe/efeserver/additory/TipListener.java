package me.efe.efeserver.additory;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efetutorial.TutorialState;
import me.efe.fishkg.events.PlayerFishkgEvent;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class TipListener implements Listener {
	public EfeServer plugin;
	
	public TipListener(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void placeBlock(BlockPlaceEvent e) {
		PlayerData data = PlayerData.get(e.getPlayer());
		Material type = e.getBlock().getType();
		
		if (data.getTutorialState() < TutorialState.ISLAND_CREATED)
			return;
		
		if (type == Material.CHEST && !data.hasTip("lock-chest")) {
			e.getPlayer().sendMessage("§a▒§r §e§l[Tip]§r 상자에 표지판을 부착해 잠그면 상자를 보호할 수 있습니다.");
			data.addTip("lock-chest");
		}
	}
	
	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		PlayerData data = PlayerData.get(e.getPlayer());
		Material type = e.getBlock().getType();
		
		if (data.getTutorialState() < TutorialState.ISLAND_CREATED)
			return;
		
		if (type == Material.STONE && !data.hasTip("stone-1")) {
			e.getPlayer().sendMessage("§a▒§r §e§l[Tip]§r 미네라스 알파와 감마에서 물가에 있는 돌을 캐면 모래를 얻을 수 있습니다.");
			data.addTip("stone-1");
		} else if (type == Material.STONE && !data.hasTip("stone-2")) {
			e.getPlayer().sendMessage("§a▒§r §e§l[Tip]§r 미네라스 알파에서는 돌을 캐서 잔디와 흙을, 감마에서는 자갈을 채집할 수 있습니다.");
			data.addTip("stone-2");
		} else if (type == Material.GOLD_ORE && !data.hasTip("gold-pickaxe")) {
			e.getPlayer().sendMessage("§a▒§r §e§l[Tip]§r 금곡괭이로 채광하면 금과 다이아몬드 광물이 나올 확률이 두 배가 됩니다. 물론, 두 광물은 특정 스킬을 배워야만 채집할 수 있습니다.");
			data.addTip("gold-pickaxe");
		} else if (type == Material.LAPIS_ORE && !data.hasTip("lapis")) {
			e.getPlayer().sendMessage("§a▒§r §e§l[Tip]§r 청금석은 인첸트에 사용되는 자원입니다.");
			data.addTip("lapis");
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		PlayerData data = PlayerData.get(e.getPlayer());
		
		if (data.getTutorialState() < TutorialState.ISLAND_CREATED)
			return;
		
		if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (e.getItem() == null)
				return;
			
			if (e.getItem().getType() == Material.COMPASS && !data.hasTip("map")) {
				e.getPlayer().sendMessage("§a▒§r §e§l[Tip]§r §bmap.e-pple.kr§r에서 서버 지도를 확인할 수 있습니다.");
				data.addTip("map");
			}
		}
	}
	
	@EventHandler
	public void fish(PlayerFishkgEvent e) {
		PlayerData data = PlayerData.get(e.getPlayer());
		
		if (data.getTutorialState() < TutorialState.ISLAND_CREATED)
			return;
		
		if (!data.hasTip("fish-rank")) {
			e.getPlayer().sendMessage("§a▒§r §e§l[Tip]§r 물고기에 적힌 등급은 판매 가격에 영향을 줍니다.");
			data.addTip("fish-rank");
		}
	}
}