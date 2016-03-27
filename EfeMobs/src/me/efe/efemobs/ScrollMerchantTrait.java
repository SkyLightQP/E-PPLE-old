package me.efe.efemobs;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.event.EventHandler;

public class ScrollMerchantTrait extends Trait {
	
	public ScrollMerchantTrait() {
		super("ScrollMerchant");
	}
	
	@EventHandler (ignoreCancelled = true)
	public void click(NPCRightClickEvent e) {
		if (e.getNPC() == this.getNPC()) {
			EfeMobs.openMerchantGUI(e.getClicker());
		}
		
		return;
	}
}