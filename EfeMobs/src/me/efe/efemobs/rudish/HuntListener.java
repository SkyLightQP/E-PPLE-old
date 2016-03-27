package me.efe.efemobs.rudish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.efe.efeitems.ItemStorage;
import me.efe.efemastery.MasteryManager;
import me.efe.efemastery.MasteryManager.MasteryType;
import me.efe.efemobs.EfeMobs;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.util.Scoreboarder;
import me.efe.unlimitedrpg.customexp.CustomExpAPI;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.horyu1234.horyulogger.HoryuLogger;

public class HuntListener implements Listener {
	public EfeMobs plugin;
	public Location chestLoc;
	public Location hologramLoc;
	public HashMap<UUID, Hologram> hologramMap = new HashMap<UUID, Hologram>();
	
	public HuntListener(EfeMobs plugin) {
		this.plugin = plugin;
		this.chestLoc = new Location(EfeServer.getInstance().world, 1299, 46, 1288);
		this.hologramLoc = chestLoc.clone().add(0.5, 2, 0.5);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_BLOCK && e.getClickedBlock().getLocation().equals(chestLoc)) {
			e.setCancelled(true);
			
			UserData data = new UserData(e.getPlayer());
			int soul = data.getStoredSoul();
			
			if (soul == 0) {
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ZOMBIE_METAL, 1.0F, 1.0F);
				e.getPlayer().sendMessage("§c▒§r 보관된 소울이 없습니다!");
				return;
			}
			
			data.setSoul(data.getSoul() + soul);
			data.setStoredSoul(0);
			
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
			e.getPlayer().sendMessage("§a▒§r §b"+soul+" 소울§r을 수거했습니다.");
			
			Scoreboarder.updateObjectives(e.getPlayer());
			
			updateStorageHologram(e.getPlayer());
		} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getLocation().equals(chestLoc)) {
			e.setCancelled(true);
			
			UserData data = new UserData(e.getPlayer());
			int soul = data.getSoul();
			
			if (soul == 0) {
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ZOMBIE_METAL, 1.0F, 1.0F);
				e.getPlayer().sendMessage("§c▒§r 소지중인 소울이 없습니다!");
				return;
			}
			
			data.setSoul(0);
			data.setStoredSoul(data.getStoredSoul() + soul);
			
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_PICKUP, 1.0F, 1.0F);
			e.getPlayer().sendMessage("§a▒§r §b"+soul+" 소울§r을 보관했습니다.");
			
			Scoreboarder.updateObjectives(e.getPlayer());
			
			updateStorageHologram(e.getPlayer());
		}
	}
	
	public void updateStorageHologram(Player p) {
		UserData data = new UserData(p);
		
		if (hologramMap.containsKey(p.getUniqueId())) {
			Hologram holo = hologramMap.get(p.getUniqueId());
			
			holo.removeLine(0);
			holo.appendTextLine("§b§l"+data.getStoredSoul()+" Souls Stored");
			
		} else {
			Hologram holo = HologramsAPI.createHologram(plugin, hologramLoc);
			
			holo.getVisibilityManager().setVisibleByDefault(false);
			holo.getVisibilityManager().showTo(p);
			holo.appendTextLine("§b§l"+data.getStoredSoul()+" Souls Stored");
			
			hologramMap.put(p.getUniqueId(), holo);
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		updateStorageHologram(e.getPlayer());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if (hologramMap.containsKey(e.getPlayer().getUniqueId())) {
			hologramMap.get(e.getPlayer().getUniqueId()).delete();
			hologramMap.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		LivingEntity damager = plugin.util.getDamager(e);
		
		if (damager != null && damager instanceof Player) {
			Player p = (Player) damager;
			UserData data = new UserData(p);
			int floor = EfeMobs.getFloor(p.getLocation());
			
			if (data.getMaxFloor() < floor) {
				e.setCancelled(true);
				
				p.teleport(plugin.teleportGui.centers[0]);
				return;
			}
			
			if (e.getEntity().hasMetadata("damage_"+p.getUniqueId().toString())) {
				double damage = e.getEntity().getMetadata("damage_"+p.getUniqueId().toString()).get(0).asDouble();
				damage += e.getDamage();
				e.getEntity().setMetadata("damage_"+p.getUniqueId().toString(), new FixedMetadataValue(plugin, damage));
			} else {
				e.getEntity().setMetadata("damage_"+p.getUniqueId().toString(), new FixedMetadataValue(plugin, e.getDamage()));
			}
		}
	}
	
	@EventHandler
	public void death(MythicMobDeathEvent e) {
		int floor = EfeMobs.getFloor(e.getLivingEntity().getLocation());
		
		if (floor == 0) return;
		
		if (e.getLivingEntity() instanceof Wolf && e.getLivingEntity().getVehicle() instanceof Snowman) {
			e.setExp(0);
			e.setDrops(new ArrayList<ItemStack>());
			return;
		}
		
		if (Math.random() <= 0.125) {
			e.getDrops().add(ItemStorage.EMELARD_BOX.clone());
		}
		
		for (Entity entity : e.getEntity().getNearbyEntities(10.0D, 10.0D, 10.0D)) {
			if (!(entity instanceof Player)) continue;
			
			Player p = (Player) entity;
			
			if (!e.getEntity().hasMetadata("damage_"+p.getUniqueId().toString()))
				continue;
			
			double damage = e.getEntity().getMetadata("damage_"+p.getUniqueId().toString()).get(0).asDouble();
			
			if (damage < e.getMobType().getHealth() * 0.3)
				continue;
			
			UserData data = new UserData(p);
			int soul = (floor >= 7) ? 3 : (floor >= 4) ? 2 : 1;
			int exp = MasteryManager.getGivingExp(p, MasteryType.HUNT);
			
			if (e.getEntity() instanceof Slime || e.getEntity() instanceof MagmaCube) {
				exp /= 4;
				soul = (Math.random() <= 0.4) ? 1 : 0;
			} else if (e.getEntity() instanceof Silverfish) {
				soul = 0;
				exp = 0;
			} else if (e.getEntity() instanceof IronGolem) {
				soul = 4;
			}
			
			if (data.getMaxFloor() != floor)
				soul = 0;
			
			data.giveSoul(soul);
			
			MasteryManager.giveMasteryExp(p, MasteryType.HUNT, 1.0D);
			
			CustomExpAPI.giveExp(p, exp);
			HoryuLogger.getInstance().getLogManager().logExp(p, exp, "hunt:" + e.getMobType().getInternalName());
			
			Scoreboarder.message(p, new String[]{"§2Exp +"+exp, "§b소울 +"+soul}, 1);
		}
		
		e.setExp(0);
	}
	
	@EventHandler
	public void changeBlock(EntityChangeBlockEvent e) {
		if (e.getEntityType() == EntityType.ENDERMAN) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void fixJockey(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.JOCKEY) {
			e.setCancelled(true);
		}
	}
}