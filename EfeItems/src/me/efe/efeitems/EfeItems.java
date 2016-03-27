package me.efe.efeitems;

import me.efe.efeitems.SkullStorage.Skull;
import me.efe.efeitems.listeners.AnimalListener;
import me.efe.efeitems.listeners.AnvilGUI;
import me.efe.efeitems.listeners.ItemListener;
import me.efe.efeitems.listeners.RandomTitleGUI;
import me.efe.efeitems.listeners.TeleportGUI;
import me.efe.efeitems.listeners.VillagerGUI;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EfeItems extends JavaPlugin {
	public ItemListener itemListener;
	public AnimalListener animalListener;
	public AnvilGUI anvilGui;
	public TeleportGUI teleportGui;
	public RandomTitleGUI randomTitleGui;
	public VillagerGUI villagerGui;
	
	@Override
	public void onEnable() {
		PluginManager manager = getServer().getPluginManager();
		
		manager.registerEvents(this.itemListener = new ItemListener(this), this);
		manager.registerEvents(this.animalListener = new AnimalListener(this), this);
		manager.registerEvents(this.anvilGui = new AnvilGUI(this), this);
		manager.registerEvents(this.teleportGui = new TeleportGUI(this), this);
		manager.registerEvents(this.randomTitleGui = new RandomTitleGUI(this), this);
		manager.registerEvents(this.villagerGui = new VillagerGUI(this), this);
		
		getLogger().info(this.getDescription().getFullName()+" has been enabled!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info(this.getDescription().getFullName()+" has been disabled.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("efeitems")) {
			try {
				if (!sender.isOp()) return false;
				
				Player player = (Player) sender;
				
				if (args.length == 0) {
					player.sendMessage("§a▒§r §l/efeitems§r");
					player.sendMessage("§a▒§r §l/efeitems item <name>§r");
					player.sendMessage("§a▒§r §l/efeitems bloodskin <name>§r");
					player.sendMessage("§a▒§r §l/efeitems skull <name>§r");
					
					return true;
				} else if (args[0].equalsIgnoreCase("item")) {
					ItemStack item = (ItemStack) ItemStorage.class.getField(args[1].toUpperCase()).get(new ItemStack(Material.AIR));
					player.getInventory().addItem(item.clone());
					
					player.sendMessage("§a▒§r 성공적으로 소환되었습니다.");
					
					return true;
				} else if (args[0].equalsIgnoreCase("bloodskin")) {
					ItemStack item = ItemStorage.createSkinBook(args[1]);
					player.getInventory().addItem(item);
					
					player.sendMessage("§a▒§r 성공적으로 소환되었습니다.");
					
					return true;
				} else if (args[0].equalsIgnoreCase("skull")) {
					Skull skull = Skull.valueOf(args[1].toUpperCase());
					player.getInventory().addItem(SkullStorage.getItem(skull).clone());
					
					player.sendMessage("§a▒§r 성공적으로 소환되었습니다.");
					
					return true;
				}
				
			} catch (Exception e) {
				sender.sendMessage("§c▒§r Exception Occured: "+e.getMessage());
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
}