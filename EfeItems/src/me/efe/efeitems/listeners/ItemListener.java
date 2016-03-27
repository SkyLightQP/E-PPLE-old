package me.efe.efeitems.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.efe.bloodskin.skins.Skin;
import me.efe.efecore.util.EfeUtils;
import me.efe.efeisland.EfeIsland;
import me.efe.efeitems.EfeItems;
import me.efe.efeitems.ItemStorage;
import me.efe.efeitems.NMSHandler;
import me.efe.efeitems.SkullStorage;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efeserver.reform.Fatigue;
import me.efe.efeserver.util.Scoreboarder;
import me.tade.NoAI.API.NoAI;
import mkremins.fanciful.FancyMessage;

import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.github.jikoo.enchantedfurnace.EnchantedFurnace;
import com.github.jikoo.enchantedfurnace.Furnace;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.slikey.effectlib.util.ParticleEffect;

public class ItemListener implements Listener {
	public EfeItems plugin;
	public EnchantedFurnace enchantedFurnace;
	public Random random = new Random();
	public List<Material> crops = new ArrayList<Material>();
	
	public ItemListener(EfeItems plugin) {
		this.plugin = plugin;
		
		this.enchantedFurnace = (EnchantedFurnace) plugin.getServer().getPluginManager().getPlugin("EnchantedFurnace");
		
		crops.add(Material.CROPS);
		crops.add(Material.CARROT);
		crops.add(Material.POTATO);
		crops.add(Material.PUMPKIN_STEM);
		crops.add(Material.MELON_STEM);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (EfeUtils.event.isRightClick(event) && event.getItem() != null && event.getItem().hasItemMeta()) {
			String name = ItemStorage.getName(event.getItem());
			
			if (event.getPlayer().hasMetadata("teleporting")) return;
			
			if (name.equals(ItemStorage.getName(ItemStorage.ANVIL_ABANDONED))) {
				event.setCancelled(true);
				
				plugin.anvilGui.openGUI(event.getPlayer(), "버려진 모루", (short) 2);
			} else if (name.equals(ItemStorage.getName(ItemStorage.ANVIL_OLD))) {
				event.setCancelled(true);
				
				plugin.anvilGui.openGUI(event.getPlayer(), "낡은 모루", (short) 1);
			} else if (name.equals(ItemStorage.getName(ItemStorage.ANVIL_ENCHANTED))) {
				event.setCancelled(true);
				
				plugin.anvilGui.openGUI(event.getPlayer(), "마법의 모루", (short) 0);
			} else if (name.equals(ItemStorage.getName(ItemStorage.EYE_OF_TELEPORTATION))) {
				event.setCancelled(true);
				
				if (!event.getPlayer().getWorld().equals(EfeServer.getInstance().world)) {
					event.getPlayer().sendMessage("§c▒§r 여기서는 사용할 수 없습니다!");
					return;
				}
				
				plugin.teleportGui.openGUI(event.getPlayer());
			} else if (name.equals(ItemStorage.getName(ItemStorage.SP_RESET_SCROLL))) {
				event.setCancelled(true);
				
				me.efe.skilltree.UserData data = new me.efe.skilltree.UserData(event.getPlayer());
				
				int sp = data.getSP() + data.getUsedSP();

				data.reset();
				data.giveSP(sp);
				
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.PORTAL_TRIGGER, 1.0F, 2.0F);
				event.getPlayer().sendMessage("§a▒§r 투자한 SP가 모두 돌아왔습니다!");
				
			} else if (ItemStorage.isBiomeCreeper(event.getItem())) {
				event.setCancelled(true);
				
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					Location loc = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);
					EfeIsland efeIsland = EfeServer.getInstance().efeIsland;
					ProtectedRegion region = efeIsland.getIsleRegion(loc);
					
					if (!efeIsland.getIsleOwner(region).equals(event.getPlayer())) {
						event.getPlayer().sendMessage("§c▒§r 당신의 섬이 아닙니다!");
						return;
					}
					
					if (!event.getClickedBlock().getType().isSolid())
						loc.subtract(0, 1, 0);
					
					Creeper creeper = loc.getWorld().spawn(loc, Creeper.class);
					Biome biome = ItemStorage.getCreeperBiome(event.getItem());
					
					NoAI.setNoAI(creeper);
					
					creeper.setCustomName("§a우클릭: 기후 적용§r / §c좌클릭: 취소");
					creeper.setCustomNameVisible(true);
					creeper.setHealth(1.0D);
					creeper.setMaxHealth(1.0D);
					creeper.setMetadata("biome_creeper", new FixedMetadataValue(plugin, biome));
					creeper.setMetadata("biome_creeper_owner", new FixedMetadataValue(plugin, event.getPlayer().getUniqueId()));
					
					for (int x = -3; x <= 3; x ++) {
						for (int z = -3; z <= 3; z ++) {
							Block block = loc.clone().add(x, 0, z).getBlock();
							
							if (block.getType().isSolid()) {
								event.getPlayer().sendBlockChange(block.getLocation(), Material.STAINED_CLAY, (byte) 14);
							} else {
								event.getPlayer().sendBlockChange(block.getLocation(), Material.CARPET, (byte) 14);
							}
						}
					}
					
					event.getPlayer().setItemInHand(EfeUtils.item.getUsed(event.getItem().clone(), event.getPlayer()));
					
					event.getPlayer().playSound(loc, Sound.FUSE, 3.0F, 1.0F);
					ActionBarAPI.sendActionBar(event.getPlayer(), "§a우클릭: 기후 적용§r / §c좌클릭: 취소");
				}
			} else if (name.equals(ItemStorage.getName(ItemStorage.RAINBOW_BOX_WOOL)) || name.equals(ItemStorage.getName(ItemStorage.RAINBOW_BOX_CLAY))) {
				event.setCancelled(true);
				
				boolean wool = name.equals(ItemStorage.getName(ItemStorage.RAINBOW_BOX_WOOL));
				
				DyeColor color = DyeColor.values()[random.nextInt(DyeColor.values().length)];
				ItemStack item = new ItemStack(wool ? Material.WOOL : Material.STAINED_CLAY, 64, color.getWoolData());
				
				event.getPlayer().setItemInHand(EfeUtils.item.getUsed(event.getItem().clone(), event.getPlayer()));
				
				if (event.getPlayer().getInventory().firstEmpty() != -1)
					event.getPlayer().getInventory().addItem(item);
				else
					event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), item);
				
				Firework firework = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), Firework.class);
				FireworkMeta meta = firework.getFireworkMeta();
				FireworkEffect effect = FireworkEffect.builder().with(Type.BURST).withColor(color.getFireworkColor()).withTrail().build();
				meta.addEffect(effect);
				meta.setPower(0);
				firework.setFireworkMeta(meta);
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						firework.detonate();
					}
				}, 3L);
				
				event.getPlayer().sendMessage("§a▒§r 64개의 "+(wool ? "양모" : "염색된 점토")+"를 획득했습니다!");
			} else if (name.equals(ItemStorage.getName(ItemStorage.RANDOM_TITLE_BOOK))) {
				event.setCancelled(true);
				
				plugin.randomTitleGui.openGUI(event.getPlayer());
			} else if (name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX)) ||
					name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_POPULAR)) ||
					name.equals(ItemStorage.getName(ItemStorage.BLOOD_SKIN_BOX_PREMIUM)) ||
					name.equals(ItemStorage.getName(ItemStorage.DECORATION_HEAD_BOX))) {
				event.setCancelled(true);
				
				event.getPlayer().sendMessage("§a▒§r 폴라리스 지하 술집의 열쇠공을 찾아가보세요:§7 x: 27, y: 50, z: -19");
			} else if (name.equals(ItemStorage.getName(ItemStorage.EMELARD_BOX))) {
				event.setCancelled(true);
				
				event.getPlayer().setItemInHand(EfeUtils.item.getUsed(event.getItem().clone(), event.getPlayer()));
				
				double amount = random.nextInt(16) + 15;
				EfeServer.getInstance().vault.give(event.getPlayer(), amount);
				
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
				event.getPlayer().sendMessage("§a▒§r "+amount+"E를 획득했습니다!");
			}
			
			
			Skin skin = ItemStorage.getBloodSkin(event.getItem());
			
			if (skin != null) {
				event.setCancelled(true);
				
				PlayerData data = PlayerData.get(event.getPlayer());
				
				if (data.hasBloodSkin(skin.getName())) {
					event.getPlayer().sendMessage("§c▒§r 이미 소지중인 스킨입니다.");
					return;
				}
				
				ItemStack used = EfeUtils.item.getUsed(event.getItem().clone(), event.getPlayer());
				event.getPlayer().setItemInHand(used);
				
				data.addBloodSkin(skin.getName());
				
				event.getPlayer().sendMessage("§a▒§r 새로운 블러드 스킨을 읽어냈습니다!");
				
				new FancyMessage("§a▒§r ")
				.then("§b§n블러드 스킨 GUI§r")
					.command("/블러드스킨")
					.tooltip("§b/블러드스킨")
				.then("에서 스킨을 적용해보세요.")
				.send(event.getPlayer());
				
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ZOMBIE_REMEDY, 1.0F, 1.25F);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getBlock().getWorld() == EfeServer.getInstance().worldIsl &&
				event.getItemInHand().hasItemMeta() && event.getItemInHand().getItemMeta().hasDisplayName()) {
			
			String name = ItemStorage.getName(event.getItemInHand());
			
			if (name.equals(ItemStorage.SPAWN_CHANGER.getItemMeta().getDisplayName())) {
				event.setCancelled(true);
				
				EfeIsland efeIsland = EfeServer.getInstance().efeIsland;
				ProtectedRegion region = efeIsland.getIsleRegion(event.getPlayer());
				Location loc = event.getBlock().getLocation();
				
				if (!region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
					event.getPlayer().sendMessage("§c▒§r 자신의 섬에서만 사용할 수 있습니다.");
					return;
				}
				
				efeIsland.builder.setSpawnPoint(region, loc);
				
				event.getPlayer().setItemInHand(EfeUtils.item.getUsed(event.getItemInHand().clone(), event.getPlayer()));
				
				event.getPlayer().playSound(loc, Sound.LEVEL_UP, 3.0F, 2.0F);
				event.getPlayer().sendMessage("§a▒§r 섬의 스폰 포인트가 변경되었습니다!");
				
			} else if (name.equals(ItemStorage.HEALING_SPAWNER.getItemMeta().getDisplayName())) {
				NMSHandler.setHealingSpawner(event.getBlock().getState());
				
				event.getBlockReplacedState().update();
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getWorld() == EfeServer.getInstance().worldIsl) {
			if (event.getBlock().getType() == Material.ANVIL) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = ItemStorage.DEFORMED_ANVIL.clone();
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
				
			} else if (event.getBlock().getType() == Material.ENCHANTMENT_TABLE) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = ItemStorage.DEFORMED_ENCHANTMENT_TABLE.clone();
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
				
			} else if (event.getBlock().getType() == Material.BEACON) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = ItemStorage.DEFORMED_BEACON.clone();
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
				
			} else if (event.getBlock().getType() == Material.BREWING_STAND) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = ItemStorage.DEFORMED_BREWING_STAND.clone();
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
				
			} else if (event.getBlock().getType() == Material.MOB_SPAWNER && NMSHandler.isHealingSpawner(event.getBlock())) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = ItemStorage.HEALING_SPAWNER.clone();
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
				
			} else if (event.getBlock().getType() == Material.CROPS) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = new ItemStack(Material.SEEDS);
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
				
			} else if (event.getBlock().getType() == Material.CARROT) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = new ItemStack(Material.CARROT_ITEM);
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
				
			} else if (event.getBlock().getType() == Material.POTATO) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = new ItemStack(Material.POTATO_ITEM);
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
				
			} else if (event.getBlock().getType() == Material.PUMPKIN_STEM) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = new ItemStack(Material.PUMPKIN_SEEDS);
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
				
			} else if (event.getBlock().getType() == Material.MELON_STEM) {
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = new ItemStack(Material.MELON_SEEDS);
				
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
			} else if (event.getBlock().getType() == Material.SKULL) {
				SkullStorage.Skull skull = SkullStorage.getSkull(event.getBlock());
				
				if (skull == null)
					return;
				
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				
				ItemStack item = SkullStorage.getItem(skull);
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
			}
		}
	}
	
	@EventHandler
	public void consume(PlayerItemConsumeEvent event) {
		String name = ItemStorage.getName(event.getItem());
		
		if (name.equals(ItemStorage.PICK_ME_UP_1.getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			event.getPlayer().setItemInHand(EfeUtils.item.getUsed(event.getItem().clone(), event.getPlayer()));
			event.getPlayer().getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE));

			Fatigue.addFatigue(event.getPlayer(), -250);
			Scoreboarder.updateObjectives(event.getPlayer());
			
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 0.5F);
			event.getPlayer().sendMessage("§a▒§r 피로가 50% 회복되었습니다!");
			
			ParticleEffect.VILLAGER_HAPPY.display(0.3F, 0.3F, 0.3F, 0.0F, 10, event.getPlayer().getLocation(), 32);
			
		} else if (name.equals(ItemStorage.PICK_ME_UP_2.getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			event.getPlayer().setItemInHand(EfeUtils.item.getUsed(event.getItem().clone(), event.getPlayer()));
			event.getPlayer().getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE));

			Fatigue.setFatigue(event.getPlayer(), 0);
			Scoreboarder.updateObjectives(event.getPlayer());
			
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 0.5F);
			event.getPlayer().sendMessage("§a▒§r 피로가 모두 회복되었습니다!");
			
			ParticleEffect.VILLAGER_HAPPY.display(0.3F, 0.3F, 0.3F, 0.0F, 10, event.getPlayer().getLocation(), 32);
			
		}
	}
	
	@EventHandler
	public void launch(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof ThrownPotion) {
			ThrownPotion potion = (ThrownPotion) event.getEntity();
			
			if (ItemStorage.getName(potion.getItem()).equals(ItemStorage.ANIMAL_MEDICINE.getItemMeta().getDisplayName())) {
				ItemStack item = ItemStorage.ANIMAL_MEDICINE.clone();
				
				item.setDurability((short) 0);
				potion.setItem(item);
			}
		}
	}
	
	@EventHandler
	public void splash(PotionSplashEvent event) {
		String name = ItemStorage.getName(event.getPotion().getItem());
		
		if (name.equals(ItemStorage.ANIMAL_MEDICINE.getItemMeta().getDisplayName())) {
			event.getPotion().getEffects().clear();
			
			for (LivingEntity entity : event.getAffectedEntities()) {
				if (!(entity instanceof Ageable))
					continue;
				
				Ageable ageable = (Ageable) entity;
				
				if (EfeServer.getInstance().animalListener.hasDisease(ageable)) {
					EfeServer.getInstance().animalListener.cure(entity);
				}
				
				ParticleEffect.VILLAGER_HAPPY.display(0.3F, 0.3F, 0.3F, 0.0F, 10, entity.getEyeLocation(), 32);
			}
		}
	}
	
	@EventHandler
	public void spawn(ItemSpawnEvent event) {
		if (event.getEntity().getItemStack().getType() == Material.FURNACE && !event.getEntity().getItemStack().getEnchantments().isEmpty()) {
			for (Enchantment ench : event.getEntity().getItemStack().getEnchantments().keySet()) {
				int level = event.getEntity().getItemStack().getEnchantments().get(ench);
				ItemStack item = ItemStorage.getFurnace(ench, level);
				
				if (item != null) {
					event.getEntity().setItemStack(item);
				}
			}
		}
	}
	
	@EventHandler
	public void smelt(FurnaceSmeltEvent event) {
		Furnace furnace = enchantedFurnace.getFurnace(event.getBlock());
		
		if (furnace != null && Math.random() <= 0.01) {
			event.getBlock().breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
			
			ParticleEffect.EXPLOSION_HUGE.display(0.0F, 0.0F, 0.0F, 1.0F, 1, event.getBlock().getLocation(), 32);
			
			for (Player player : event.getBlock().getWorld().getPlayers()) {
				player.playSound(event.getBlock().getLocation(), Sound.EXPLODE, 3.0F, 1.0F);
			}
		}
	}
	
	@EventHandler
	public void interactEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().hasMetadata("biome_creeper") && !event.getRightClicked().isDead()) {
			Biome biome = (Biome) event.getRightClicked().getMetadata("biome_creeper").get(0).value();
			UUID id = (UUID) event.getRightClicked().getMetadata("biome_creeper_owner").get(0).value();
			
			if (!id.equals(event.getPlayer().getUniqueId()))
				return;
			
			Location loc = event.getRightClicked().getLocation();
			NMSHandler.updateChunks(loc, biome);
			
			event.getRightClicked().remove();
			
			ParticleEffect.EXPLOSION_HUGE.display(0.0F, 0.0F, 0.0F, 1.0F, 1, loc, event.getPlayer());
			
			event.getPlayer().playSound(loc, Sound.EXPLODE, 1.0F, 1.0F);
			ActionBarAPI.sendActionBar(event.getPlayer(), "§a기후가 변경되었습니다.");
		}
	}
	
	@EventHandler
	public void death(EntityDeathEvent event) {
		if (event.getEntity().hasMetadata("biome_creeper")) {
			Location loc = event.getEntity().getLocation();
			Biome biome = (Biome) event.getEntity().getMetadata("biome_creeper").get(0).value();
			
			event.setDroppedExp(0);
			
			event.getDrops().clear();
			event.getDrops().add(ItemStorage.getBiomeCreeper(biome));
			
			if (event.getEntity().getKiller() != null) {
				NMSHandler.updateChunks(loc, null);
			}
		}
	}
	
	@EventHandler
	public void explode(EntityExplodeEvent event) {
		if (event.getLocation().getWorld() == EfeServer.getInstance().worldIsl) {
			Iterator<Block> it = event.blockList().iterator();
			
			while (it.hasNext()) {
				Block block = it.next();
				
				if (crops.contains(block.getType())) {
					it.remove();
					block.setType(Material.AIR);
				}
			}
		}
	}
	
	@EventHandler
	public void physics(BlockPhysicsEvent event) {
		if (event.getBlock().getWorld() == EfeServer.getInstance().worldIsl &&
				crops.contains(event.getBlock().getType())) {
			event.setCancelled(true);
		}
	}
}