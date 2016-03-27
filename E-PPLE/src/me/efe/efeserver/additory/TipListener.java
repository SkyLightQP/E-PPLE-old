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
			e.getPlayer().sendMessage("��a�ơ�r ��e��l[Tip]��r ���ڿ� ǥ������ ������ ��׸� ���ڸ� ��ȣ�� �� �ֽ��ϴ�.");
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
			e.getPlayer().sendMessage("��a�ơ�r ��e��l[Tip]��r �̳׶� ���Ŀ� �������� ������ �ִ� ���� ĳ�� �𷡸� ���� �� �ֽ��ϴ�.");
			data.addTip("stone-1");
		} else if (type == Material.STONE && !data.hasTip("stone-2")) {
			e.getPlayer().sendMessage("��a�ơ�r ��e��l[Tip]��r �̳׶� ���Ŀ����� ���� ĳ�� �ܵ�� ����, ���������� �ڰ��� ä���� �� �ֽ��ϴ�.");
			data.addTip("stone-2");
		} else if (type == Material.GOLD_ORE && !data.hasTip("gold-pickaxe")) {
			e.getPlayer().sendMessage("��a�ơ�r ��e��l[Tip]��r �ݰ�̷� ä���ϸ� �ݰ� ���̾Ƹ�� ������ ���� Ȯ���� �� �谡 �˴ϴ�. ����, �� ������ Ư�� ��ų�� ����߸� ä���� �� �ֽ��ϴ�.");
			data.addTip("gold-pickaxe");
		} else if (type == Material.LAPIS_ORE && !data.hasTip("lapis")) {
			e.getPlayer().sendMessage("��a�ơ�r ��e��l[Tip]��r û�ݼ��� ��þƮ�� ���Ǵ� �ڿ��Դϴ�.");
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
				e.getPlayer().sendMessage("��a�ơ�r ��e��l[Tip]��r ��bmap.e-pple.kr��r���� ���� ������ Ȯ���� �� �ֽ��ϴ�.");
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
			e.getPlayer().sendMessage("��a�ơ�r ��e��l[Tip]��r ����⿡ ���� ����� �Ǹ� ���ݿ� ������ �ݴϴ�.");
			data.addTip("fish-rank");
		}
	}
}