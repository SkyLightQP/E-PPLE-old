package me.efe.efeshops;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.event.EventHandler;

public class KeysmithTrait extends Trait {
	
	public KeysmithTrait() {
		super("Keysmith");
	}
	
	@EventHandler (ignoreCancelled = true)
	public void click(NPCRightClickEvent e) {
		if (e.getNPC() == this.getNPC()) {
			EfeShops.openGUIKeysmith(e.getClicker());
		}
		
		return;
	}
}