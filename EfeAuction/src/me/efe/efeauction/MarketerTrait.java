package me.efe.efeauction;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.event.EventHandler;

public class MarketerTrait extends Trait {
	
	public MarketerTrait() {
		super("Marketer");
	}
	
	@EventHandler (ignoreCancelled = true)
	public void click(NPCRightClickEvent e) {
		if (e.getNPC() == this.getNPC()) {
			EfeAuction.getInstance().auctionMainGUI.openGUI(e.getClicker());
		}
		
		return;
	}
}