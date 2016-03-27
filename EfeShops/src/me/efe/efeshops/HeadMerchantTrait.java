package me.efe.efeshops;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.event.EventHandler;

public class HeadMerchantTrait extends Trait {
	
	public HeadMerchantTrait() {
		super("HeadMerchant");
	}
	
	@EventHandler (ignoreCancelled = true)
	public void click(NPCRightClickEvent e) {
		if (e.getNPC() == this.getNPC()) {
			EfeShops.openGUIHeadMerchant(e.getClicker());
		}
		
		return;
	}
}