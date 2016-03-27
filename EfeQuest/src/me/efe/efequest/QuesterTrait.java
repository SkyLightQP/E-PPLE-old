package me.efe.efequest;

import org.bukkit.event.EventHandler;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;

public class QuesterTrait extends Trait {
	
	public QuesterTrait() {
		super("EfeQuester");
	}
	
	@EventHandler (ignoreCancelled = true)
	public void click(NPCRightClickEvent e) {
		if (e.getNPC() == this.getNPC()) {
			EfeQuest.openQuesterGUI(e.getClicker(), e.getNPC());
		}
		
		return;
	}
}