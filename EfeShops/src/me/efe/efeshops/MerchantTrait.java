package me.efe.efeshops;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.event.EventHandler;

public class MerchantTrait extends Trait {
	
	public MerchantTrait() {
		super("Merchant");
	}
	
	@EventHandler (ignoreCancelled = true)
	public void click(NPCRightClickEvent e) {
		if (e.getNPC() == this.getNPC()) {
			EfeShops.openGUIAdminShop(e.getClicker(), e.getNPC());
		}
		
		return;
	}
}