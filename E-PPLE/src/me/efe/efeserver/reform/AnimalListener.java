package me.efe.efeserver.reform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.UUID;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.skilltree.SkillManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import de.slikey.effectlib.util.ParticleEffect;

public class AnimalListener implements Listener {
	public EfeServer plugin;
	public SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH mm");
	public HashMap<EntityType, Double> types = new HashMap<EntityType, Double>();
	public int maxCount = 30;
	
	public AnimalListener(EfeServer plugin) {
		this.plugin = plugin;
		
		types.put(EntityType.CHICKEN, 0.35D);
		types.put(EntityType.RABBIT, 0.35D);
		types.put(EntityType.COW, 0.1D);
		types.put(EntityType.PIG, 0.1D);
		types.put(EntityType.SHEEP, 0.1D);
	}
	
	public void spawn(Location loc, Player p) {
		double percent = 0.0D;
		double random = Math.random();
		
		for (EntityType type : types.keySet()) {
			percent += types.get(type);
			
			if (percent >= random) {
				LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
				entity.setCustomName("덫으로 유인해 포획하세요!");
				entity.setCustomNameVisible(true);
				
				setOwner(entity, p);
				
				ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.1F, 50, entity.getEyeLocation(), 32);
				return;
			}
		}
	}
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Animals && e.getEntity().getWorld().equals(plugin.world)) {
			LivingEntity entity = plugin.util.getDamager(e);
			
			if (!(entity instanceof Player))
				return;
			
			Player player = (Player) entity;
			OfflinePlayer owner = getOwner((LivingEntity) e.getEntity());
			
			if (owner == null)
				return;
			
			if (!player.equals(owner)) {
				e.setCancelled(true);
				
				player.sendMessage("§c▒§r "+owner.getName()+"님이 발견한 동물입니다!");
			}
		}
	}
	
	@EventHandler
	public void death(EntityDeathEvent e) {
		if (e.getEntity() instanceof Animals) {
			e.setDroppedExp(10);
			
			if (e.getEntity() instanceof Ageable) {
				Ageable ageable = (Ageable) e.getEntity();
				
				if (hasDisease(ageable)) {
					ListIterator<ItemStack> it = e.getDrops().listIterator();
					
					while (it.hasNext()) {
						ItemStack item = it.next();
						Material type = item.getType();
						
						if (type.name().endsWith("CHICKEN") || type.name().endsWith("BEEF") || type.name().endsWith("PORK") || type.name().endsWith("MUTTON") ||
								type.name().endsWith("RABBIT")) {
							it.set(new ItemStack(Material.ROTTEN_FLESH, item.getAmount()));
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void breed(PlayerInteractEntityEvent e) {
		if (isBreedItem(e.getPlayer().getItemInHand(), e.getRightClicked().getType()) && e.getRightClicked() instanceof Ageable) {
			Ageable ageable = (Ageable) e.getRightClicked();
			EntityType type = ageable.getType();
			
			if (!ageable.isAdult()) {
				if (Math.random() <= 0.2) {
					ageable.setAdult();
					ageable.setBreed(false);
				}
				
				return;
			}
			
			boolean isAvailable = true;
			
			if (type == EntityType.CHICKEN || type == EntityType.RABBIT) {
				isAvailable = SkillManager.hasLearned(e.getPlayer(), "farm.diligence");
			} else if (type == EntityType.COW || type == EntityType.PIG || type == EntityType.SHEEP || type == EntityType.MUSHROOM_COW) {
				isAvailable = SkillManager.hasLearned(e.getPlayer(), "farm.faithfulness");
			}
			
			if (!canBreed(ageable)) {
				isAvailable = false;
			}
			
			if (!e.getPlayer().getWorld().equals(plugin.worldIsl)) {
				isAvailable = false;
			}
			
			if (isAvailable) {
				setReadyToBreed(ageable, true);
				
				ageable.setBreed(true);
				
				PlayerData pData = PlayerData.get(e.getPlayer());
				if (!pData.hasTip("from-baby-to-adult")) {
					e.getPlayer().sendMessage("§a▒§r §e§l[Tip]§r 새끼 동물은 먹이를 줘야만 성장합니다.");
					pData.addTip("from-baby-to-adult");
				}
			} else {
				e.setCancelled(true);
				plugin.util.updateInv(e.getPlayer());
				
				ageable.setBreed(false);
			}
		}
	}
	
	@EventHandler
	public void spawn(CreatureSpawnEvent e) {
		if (e.getSpawnReason() == SpawnReason.BREEDING) {
			Ageable ageable = (Ageable) e.getEntity();
			
			for (Entity entity : e.getEntity().getNearbyEntities(2.0D, 2.0D, 2.0D)) {
				if (entity.getType() != e.getEntity().getType())
					continue;
				
				Ageable parent = (Ageable) entity;
				
				if (!isReadyToBreed(parent))
					continue;
				
				if (hasDisease(parent) || Math.random() <= 0.1) {
					setDisease(ageable);
				}
				
				setBreedDelay(parent);
				setReadyToBreed(parent, false);
			}
			
			ageable.setAgeLock(true);
		} else if (e.getSpawnReason() == SpawnReason.EGG) {
			Ageable ageable = (Ageable) e.getEntity();
			
			if (Math.random() <= 0.4) {
				setDisease(ageable);
			}
			
			ageable.setAgeLock(true);
		}
	}
	
	public boolean hasDisease(Ageable entity) {
		ItemStack item = entity.getEquipment().getHelmet();
		
		return (item != null && item.getType() == Material.APPLE);
	}
	
	public void setDisease(Ageable entity) {
		entity.getEquipment().setHelmet(new ItemStack(Material.APPLE));
		entity.getEquipment().setHelmetDropChance(0.0F);
	}
	
	public void cure(LivingEntity entity) {
		entity.getEquipment().setHelmet(new ItemStack(Material.AIR));
	}
	
	public boolean isReadyToBreed(Ageable entity) {
		ItemStack item = entity.getEquipment().getLeggings();
		
		return (item != null && item.getType() == Material.APPLE);
	}
	
	public void setReadyToBreed(Ageable entity, boolean value) {
		if (value) {
			entity.getEquipment().setLeggings(new ItemStack(Material.APPLE));
			entity.getEquipment().setLeggingsDropChance(0.0F);
		} else {
			entity.getEquipment().setLeggings(new ItemStack(Material.AIR));
		}
	}
	
	public boolean canBreed(Ageable entity) {
		ItemStack item = entity.getEquipment().getChestplate();
		
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
			return true;
		
		Date date;
		Date now = new Date();
		
		try {
			date = format.parse(item.getItemMeta().getDisplayName());
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		
		return now.after(date);
	}
	
	public void setBreedDelay(Ageable entity) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, 5);
		
		ItemStack item = plugin.util.createRawItem(format.format(cal.getTime()), new ItemStack(Material.NAME_TAG), new String[]{});
		
		entity.getEquipment().setChestplate(item);
		entity.getEquipment().setChestplateDropChance(0.0F);
	}
	
	public OfflinePlayer getOwner(LivingEntity entity) {
		if (!entity.hasMetadata("nimbus_owner"))
			return null;
		
		return plugin.getServer().getOfflinePlayer((UUID) entity.getMetadata("nimbus_owner").get(0).value());
	}
	
	public void setOwner(LivingEntity entity, OfflinePlayer player) {
		entity.setMetadata("nimbus_owner", new FixedMetadataValue(plugin, player.getUniqueId()));
	}
	
	public boolean isBreedItem(ItemStack item, EntityType type) {
		Material mat = item.getType();
		
		if (type.equals(EntityType.CHICKEN) && (mat == Material.SEEDS || mat == Material.PUMPKIN_SEEDS || mat == Material.MELON_SEEDS)) return true;
		if (type.equals(EntityType.COW) && mat == Material.WHEAT) return true;
		if (type.equals(EntityType.PIG) && mat == Material.CARROT_ITEM) return true;
		if (type.equals(EntityType.SHEEP) && mat == Material.WHEAT) return true;
		if (type.equals(EntityType.WOLF) && mat == Material.BONE) return true;
		if (type.equals(EntityType.OCELOT) && (mat == Material.RAW_FISH || mat == Material.COOKED_FISH)) return true;
		if (type.equals(EntityType.HORSE) && (mat == Material.GOLDEN_APPLE || mat == Material.GOLDEN_CARROT)) return true;
		if (type.equals(EntityType.RABBIT) && (mat == Material.CARROT_ITEM || mat == Material.GOLDEN_CARROT || mat == Material.YELLOW_FLOWER)) return true;
		
		return false;
	}
}