package me.efe.efemobs;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.event.EventHandler;

public class RudishGuiderTrait extends Trait {
	
	public RudishGuiderTrait() {
		super("RudishGuider");
	}
	
	@EventHandler (ignoreCancelled = true)
	public void click(NPCRightClickEvent e) {
		if (e.getNPC() == this.getNPC()) {
			EfeMobs.openGuiderGUI(e.getClicker());
		}
		
		return;
	}
}