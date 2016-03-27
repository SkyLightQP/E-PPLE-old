package me.efe.fishkg.extrashop.citizens;

import me.efe.fishkg.Fishkg;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.event.EventHandler;

public class FishMerchantTrait extends Trait {
	
	public FishMerchantTrait() {
		super("FishMerchant");
	}
	
	@EventHandler (ignoreCancelled = true)
	public void click(NPCRightClickEvent e) {
		if (e.getNPC() == this.getNPC()) {
			Fishkg.getInstance().shopGui.openGUI(e.getClicker());
		}
		
		return;
	}
}