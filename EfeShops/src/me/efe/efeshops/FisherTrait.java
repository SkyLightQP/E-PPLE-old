package me.efe.efeshops;

import me.efe.fishkg.Fishkg;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.event.EventHandler;

public class FisherTrait extends Trait {
	
	public FisherTrait() {
		super("Fisher");
	}
	
	@EventHandler (ignoreCancelled = true)
	public void click(NPCRightClickEvent e) {
		if (e.getNPC() == this.getNPC()) {
			Fishkg.getInstance().shopGui.openGUI(e.getClicker());
		}
		
		return;
	}
}