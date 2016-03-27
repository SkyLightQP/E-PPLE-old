package me.efe.efeserver.reform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import me.efe.efeisland.IslandUtils;
import me.efe.efeitems.ItemStorage;
import me.efe.efemastery.MasteryManager;
import me.efe.efemastery.MasteryManager.MasteryType;
import me.efe.efemobs.rudish.enchant.EnchantUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.util.Scoreboarder;
import me.efe.efetutorial.TutorialState;
import me.efe.efetutorial.listeners.TutorialListener;
import me.efe.skilltree.UserData;
import me.efe.unlimitedrpg.customexp.CustomExpAPI;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.horyu1234.horyulogger.HoryuLogger;

import de.slikey.effectlib.util.ParticleEffect;

public class MiningListener implements Listener {
	public EfeServer plugin;
	public List<OreTask> regen = new ArrayList<OreTask>();
	public BlockFace[] faces = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};
	
	public MiningListener(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	public void clear() {
		for (OreTask task : regen) {
			task.loc.getBlock().setType(Material.STONE);
		}
		
		regen.clear();
	}
	
	@EventHandler
	public void physics(BlockPhysicsEvent e) {
		Material type = e.getBlock().getType();
		
		if (type == Material.RAILS || type == Material.TORCH || type == Material.REDSTONE_WIRE) {
			if (!IslandUtils.isMineras(e.getBlock().getLocation())) return;
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void lava(PlayerBucketFillEvent e) {
		if (e.getPlayer().getWorld() != plugin.world) return;
		if (!IslandUtils.isMineras(e.getBlockClicked().getLocation())) return;
		
		if (!e.getBlockClicked().getType().equals(Material.STATIONARY_LAVA)) {
			e.setCancelled(true);
			return;
		}
		
		
		boolean fillable = false;
		
		for (OreTask task : regen) {
			if (task.loc.equals(e.getBlockClicked().getLocation())) {
				fillable = true;
			}
		}
		
		if (!fillable) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void empty(PlayerBucketEmptyEvent e) {
		if (e.getPlayer().getWorld() != plugin.world) return;
		if (!IslandUtils.isMineras(e.getBlockClicked().getLocation())) return;
		
		e.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void blockExplode(EntityExplodeEvent e) {
		List<Block> list = new ArrayList<Block>();
		
		Iterator<Block> it = e.blockList().iterator();
		while (it.hasNext()) {
			list.add(it.next());
			it.remove();
		}
		
		for (Block block : list) {
			Location loc = block.getLocation();
			boolean alpha = IslandUtils.getIsleRegion(IslandUtils.MINERAS_ALPHA).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			boolean beta = IslandUtils.getIsleRegion(IslandUtils.MINERAS_BETA).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			boolean gamma = IslandUtils.getIsleRegion(IslandUtils.MINERAS_GAMMA).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			boolean tutorial = IslandUtils.getIsleRegion(IslandUtils.TUTORIAL_MINE).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			
			if (!alpha && !beta && !gamma && !tutorial) break;
			
			if (block.getType().equals(Material.STONE)) {
				if (alpha) {
					Material mat = Material.COBBLESTONE;
					double rand = Math.random();
					
					if (rand <= 0.14)
						mat = Material.DIRT;
					else if (rand <= 0.15)
						mat = Material.GRASS;
					else if (rand <= 0.2)
						mat = Material.SAND;
					
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(mat));
					
				} else if (beta) {
					
					block.breakNaturally();
					
				} else if (gamma) {
					
					Material mat = Material.COBBLESTONE;
					double rand = Math.random();
					
					if (rand <= 0.05)
						mat = Material.GRAVEL;
					
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(mat));
					
				} else if (tutorial) {
					
					block.breakNaturally();
				}
				
				ItemStack ore = getRandomOre(loc, Material.DIAMOND_PICKAXE, false, 0);
				
				if (!ore.getType().equals(Material.STONE)) {
					loc.getBlock().setType(ore.getType());
					loc.getBlock().setData((byte) ore.getDurability());
					loc.getBlock().getState().update(true, false);
				} else {
					loc.getBlock().setType(Material.BEDROCK);
					loc.getBlock().getState().update(true, false);
					
					new OreTask(loc).runTaskLater(plugin, 20 * 60);
				}
			}
		}
	}
	
	@EventHandler
	public void damageExplode(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getCause().equals(DamageCause.ENTITY_EXPLOSION) && e.getDamager() instanceof TNTPrimed) {
			if (!IslandUtils.isMineras(e.getEntity().getLocation())) return;
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void chestOpen(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) {
			if (!IslandUtils.isMineras(e.getPlayer().getLocation())) return;
			
			boolean openable = false;
			
			for (OreTask task : regen) {
				if (task.loc.equals(e.getClickedBlock().getLocation())) {
					openable = true;
				}
			}
			
			if (openable) {
				Chest chest = (Chest) e.getClickedBlock().getState();
				Inventory inv = plugin.getServer().createInventory(e.getPlayer(), 9*3, "§6▒§r 보물 상자");
				
				for (int i = 0; i < 27; i ++)
					inv.setItem(i, chest.getBlockInventory().getItem(i));
				
				e.getPlayer().openInventory(inv);
				chest.getBlockInventory().clear();
				chest.getBlock().setType(Material.AIR);
				chest.getBlock().getWorld().playEffect(chest.getLocation(), Effect.STEP_SOUND, Material.WOOD);
			}
		}
	}
	
	private int getNewFatigueAmplifier(Player p) {
		for (PotionEffect effect : p.getActivePotionEffects()) {
			if (effect.getType().getName().equals(PotionEffectType.SLOW.getName())) {
				return effect.getAmplifier() < 9 ? effect.getAmplifier() + 1 : 9;
			}
		}
		
		return 0;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void createOre(BlockBreakEvent e) {
		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || !e.getBlock().getWorld().equals(plugin.world)) return;
		UserData data = new UserData(e.getPlayer());
		
		Location loc = e.getBlock().getLocation();
		boolean alpha = IslandUtils.getIsleRegion(IslandUtils.MINERAS_ALPHA).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		boolean beta = IslandUtils.getIsleRegion(IslandUtils.MINERAS_BETA).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		boolean gamma = IslandUtils.getIsleRegion(IslandUtils.MINERAS_GAMMA).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		boolean tutorial = IslandUtils.getIsleRegion(IslandUtils.TUTORIAL_MINE).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		
		if (!alpha && !beta && !gamma && !tutorial) return;
		
		if (e.getBlock().getType().equals(Material.STONE) || e.getBlock().getType().name().endsWith("ORE")) {
			e.setCancelled(true);
			
			if (e.getPlayer().getItemInHand() == null || !e.getPlayer().getItemInHand().getType().name().endsWith("PICKAXE")) return;
			
			if (Math.random() <= 0.5) {
				Fatigue.addFatigue(e.getPlayer(), 1);
				Scoreboarder.updateObjectives(e.getPlayer());
			}
			
			if (Fatigue.getFatigue(e.getPlayer()) >= 500) {
				int lv = getNewFatigueAmplifier(e.getPlayer());
				e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				e.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, lv));
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 200, lv));
				
				if (lv >= 5) {
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
				}
				
				if (lv >= 9) {
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
				}
				
				ActionBarAPI.sendActionBar(e.getPlayer(), "§c너무 피곤해요! 피로회복제를 사용해주세요.");
			}
			
			if (e.getBlock().getType().name().endsWith("ORE")) {
				MasteryManager.giveMasteryExp(e.getPlayer(), MasteryType.MINE, 1.0D);
				
				int exp = MasteryManager.getGivingExp(e.getPlayer(), MasteryType.MINE);
				HoryuLogger.getInstance().getLogManager().logExp(e.getPlayer(), exp, "mine:" + e.getBlock().getType().name());
				
				CustomExpAPI.giveExp(e.getPlayer(), exp);
				
				Scoreboarder.message(e.getPlayer(), new String[]{"§2Exp +"+exp, ""}, 1);
			}
			
			short durability = e.getPlayer().getItemInHand().getType().getMaxDurability();
			e.getPlayer().setItemInHand(plugin.util.getDurabilityUsed(e.getPlayer().getItemInHand(), e.getPlayer(), durability));
			
			if (TutorialState.get(e.getPlayer()) == TutorialState.LETS_BREAK_STONES) {
				TutorialListener.onBreakBlock(e.getPlayer());
			}
			

			ItemStack hand = e.getPlayer().getItemInHand();
			Material type = e.getBlock().getType();
			
			if (type.equals(Material.STONE)) {
				if (alpha) {
					Material mat = Material.STONE;
					int amount = 1;
					
					if (!e.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
						double rand = Math.random();
						boolean isNearWater = isNearWater(e.getBlock());
						
						mat = Material.COBBLESTONE;
						
						if (rand <= 0.14)
							mat = Material.DIRT;
						else if (rand <= 0.15)
							mat = Material.GRASS;
						else if (isNearWater && rand <= 0.2)
							mat = Material.SAND;
						else if (isNearWater && rand <= 0.23) {
							mat = Material.CLAY_BALL;
							amount = 4;
						}
					}
					
					ItemStack item = new ItemStack(mat, amount);
					
					if (e.getPlayer().getInventory().firstEmpty() == -1)
						e.getBlock().getWorld().dropItemNaturally(getDropLocation(e.getPlayer(), e.getBlock()), item);
					else
						e.getPlayer().getInventory().addItem(item);
					
				} else if (beta || tutorial) {
					Material mat = Material.STONE;
					
					if (!e.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
						mat = Material.COBBLESTONE;
					}
					
					ItemStack item = new ItemStack(mat);
					
					if (e.getPlayer().getInventory().firstEmpty() == -1)
						e.getBlock().getWorld().dropItemNaturally(getDropLocation(e.getPlayer(), e.getBlock()), item);
					else
						e.getPlayer().getInventory().addItem(item);
					
				} else if (gamma) {
					Material mat = Material.STONE;
					int amount = 1;
					
					if (!e.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
						double rand = Math.random();
						boolean isNearWater = isNearWater(e.getBlock());
						
						mat = Material.COBBLESTONE;
						
						if (rand <= 0.05)
							mat = Material.GRAVEL;
						else if (isNearWater && rand <= 0.1)
							mat = Material.SAND;
						else if (isNearWater && rand <= 0.13) {
							mat = Material.CLAY_BALL;
							amount = 4;
						}
					}
					
					ItemStack item = new ItemStack(mat, amount);
					
					if (e.getPlayer().getInventory().firstEmpty() == -1)
						e.getBlock().getWorld().dropItemNaturally(getDropLocation(e.getPlayer(), e.getBlock()), item);
					else
						e.getPlayer().getInventory().addItem(item);
				}
			} else if (type != Material.EMERALD_ORE) {
				Collection<ItemStack> drops = null;
				
				if (hand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					ItemStack item = hand.clone();
					item.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
					
					drops = e.getBlock().getDrops(item);
				} else {
					drops = e.getBlock().getDrops(hand);
				}
				
				int level = data.getLevel("mine.smeltery");
				
				if (level > 0 && !e.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
					Iterator<ItemStack> it = drops.iterator();
					while (it.hasNext()) {
						ItemStack item = it.next();
						Material material = item.getType();
						
						if (material.name().endsWith("_ORE") && Math.random() <= 0.01 * level) {
							if (material == Material.IRON_ORE) {
								item.setType(Material.IRON_INGOT);
							} else if (material == Material.GOLD_ORE) {
								item.setType(Material.GOLD_INGOT);
							}
						}
					}
				}
				
				for (ItemStack item : drops) {
					if (e.getPlayer().getInventory().firstEmpty() == -1)
						e.getBlock().getWorld().dropItemNaturally(getDropLocation(e.getPlayer(), e.getBlock()), item);
					else
						e.getPlayer().getInventory().addItem(item);
				}
			}
			
			if (type.equals(Material.COAL_ORE)) {
				
				if (Math.random() <= 0.1) {
					ParticleEffect.SMOKE_NORMAL.display(0.1F, 0.1F, 0.1F, 0.1F, 300, loc.clone().add(0.5, 0.5, 0.5), 32);
					
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 0));
					
					ActionBarAPI.sendActionBar(e.getPlayer(), "§e으악!");
				}
				
			} else if (type.name().endsWith("REDSTONE_ORE")) {
				if (Math.random() <= 0.5) {
					loc.getBlock().setType(Material.STATIONARY_LAVA);
					loc.getBlock().getState().update(true, false);
					
					int mas = data.getLevel("mine.minerals-mastery");
					new OreTask(loc).runTaskLater(plugin, 20 * (105 - mas * 5));
					return;
				} else {
					e.getBlock().setType(Material.STONE);
					
					TNTPrimed tnt = e.getPlayer().getWorld().spawn(loc, TNTPrimed.class);
					tnt.setCustomName("TNT");
					tnt.setCustomNameVisible(true);
					
					ActionBarAPI.sendActionBar(e.getPlayer(), "§e이런! TNT였습니다.");
					
					return;
				}
			} else if (type == Material.EMERALD_ORE) {
				plugin.vault.give(e.getPlayer(), 10);
				
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
				ActionBarAPI.sendActionBar(e.getPlayer(), "§e10E를 획득했습니다!");
			}
			
			if (type.name().endsWith("ORE") && Math.random() <= 0.01) {
				loc.getBlock().setType(Material.CHEST);
				loc.getBlock().getState().update(true, false);
				
				Chest chest = (Chest) loc.getBlock().getState();
				List<ItemStack> list = new ArrayList<ItemStack>();
				
				if (Math.random() <= 0.2)
					list.add(new ItemStack(Material.ROTTEN_FLESH, plugin.util.rand(1, 6)));
				if (Math.random() <= 0.2)
					list.add(new ItemStack(Material.WEB, plugin.util.rand(1, 6)));
				if (Math.random() <= 0.2)
					list.add(new ItemStack(Material.BONE, plugin.util.rand(1, 6)));
				if (Math.random() <= 0.1)
					list.add(new ItemStack(Material.TNT, plugin.util.rand(1, 6)));
				if (Math.random() <= 0.1)
					list.add(ItemStorage.ANVIL_ABANDONED.clone());
				if (Math.random() <= 0.2)
					list.add(enchant(new ItemStack(randType(new Material[]{Material.STONE_SWORD, Material.STONE_PICKAXE, Material.STONE_AXE})), 1));
				if (Math.random() <= 0.25)
					list.add(enchant(new ItemStack(randType(new Material[]{Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS})), 1));
				if (Math.random() <= 0.25)
					list.add(ItemStorage.DECORATION_HEAD_BOX.clone());
				
				for (ItemStack item : list) {
					int slot = plugin.util.rand(0, 26);
					chest.getInventory().setItem(slot, item);
				}
				
				ActionBarAPI.sendActionBar(e.getPlayer(), "§e보물 상자를 발견했습니다!");
				
				int mas = data.getLevel("mine.minerals-mastery");
				new OreTask(loc).runTaskLater(plugin, 20 * (105 - mas * 5));
				return;
			}
			
			boolean hasDiamond = false; //TODO
			int fortune = hand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) ? hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) : 0;
			
			ItemStack ore = getRandomOre(loc, e.getPlayer().getItemInHand().getType(), hasDiamond, fortune);
			
			if (!ore.getType().equals(Material.STONE)) {
				loc.getBlock().setType(ore.getType());
				loc.getBlock().setData((byte) ore.getDurability());
				loc.getBlock().getState().update(true, false);
				
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
			} else {
				loc.getBlock().setType(Material.BEDROCK);
				loc.getBlock().getState().update(true, false);
				
				int mas = data.getLevel("mine.minerals-mastery");
				new OreTask(loc).runTaskLater(plugin, 20 * (105 - mas * 5));
			}
		} else if (e.getBlock().getType().equals(Material.STAINED_CLAY)) {
			e.setCancelled(true);
			
			short dur = e.getBlock().getState().getData().toItemStack().getDurability();
			boolean isSpawned = false;
			
			switch (dur) {
			case 9:
				isSpawned = true;
				spawnOre(e.getBlock(), Material.COAL_ORE);
				break;
			case 0:
				isSpawned = true;
				spawnOre(e.getBlock(), Material.IRON_ORE);
				break;
			case 4:
				isSpawned = true;
				spawnOre(e.getBlock(), Material.GOLD_ORE);
				break;
			case 6:
				isSpawned = true;
				spawnOre(e.getBlock(), Material.REDSTONE_ORE);
				break;
			case 11:
				isSpawned = true;
				spawnOre(e.getBlock(), Material.LAPIS_ORE);
				break;
			case 3:
				isSpawned = true;
				spawnOre(e.getBlock(), Material.DIAMOND_ORE);
				break;
			}
			
			if (!isSpawned) return;
			
			if (Math.random() <= 0.5) {
				Fatigue.addFatigue(e.getPlayer(), 1);
			}
			
			int exp = plugin.util.rand(15, 20);
			
			CustomExpAPI.giveExp(e.getPlayer(), exp);
			
			Scoreboarder.message(e.getPlayer(), new String[]{"§2Exp +"+exp, ""}, 1);
			ActionBarAPI.sendActionBar(e.getPlayer(), "§e광맥을 발견했습니다!");
			
			short durability = e.getPlayer().getItemInHand().getType().getMaxDurability();
			e.getPlayer().setItemInHand(plugin.util.getDurabilityUsed(e.getPlayer().getItemInHand(), e.getPlayer(), durability));
		} else {
			e.setCancelled(true);
		}
	}
	
	protected Material randType(Material[] types) {
		return types[plugin.util.random.nextInt(types.length)];
	}
	
	private ItemStack enchant(ItemStack item, int grade) {
		if (EnchantUtils.isEnchantable(item)) {
			EnchantUtils.enchant(null, item, grade);
		}
		
		return item;
	}
	
	public boolean isNearWater(Block block) {
		for (BlockFace face : faces) {
			Block target = block.getRelative(face);
			
			if (target.getType().equals(Material.STATIONARY_WATER)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Location getDropLocation(Player p, Block block) {
		Block dir = block.getRelative(getFace(p.getLocation().getYaw()).getOppositeFace());
		
		if (!dir.getType().isSolid()) {
			return dir.getLocation().add(0.5, 0, 0.5);
		}
		
		for (BlockFace face : faces) {
			Block target = block.getRelative(face);
			
			if (!target.getType().isSolid()) {
				return target.getLocation().add(0.5, 0, 0.5);
			}
		}
		
		return block.getLocation().add(0.5, 0, 0.5);
	}
	
	public BlockFace getFace(float yaw) {
		BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		
		return axis[Math.round(yaw / 90f) & 0x3];
	}
	
	public void spawnOre(Block block, Material type) {
		block.setType(type);
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
		
		for (BlockFace face : faces) {
			Block target = block.getRelative(face);
			
			if (target.getType().equals(Material.STONE) && Math.random() <= 0.5) {
				target.setType(type);
				target.getWorld().playEffect(target.getLocation(), Effect.STEP_SOUND, target.getType());
			}
		}
	}
	
	public ItemStack getRandomOre(Location loc, Material type, boolean hasDiamond, int fortune) {
		double rand = Math.random();
		double percent = 0.0D;
		double multiplier = 1.0D + fortune * 0.1D;
		
		if (type == Material.WOOD_PICKAXE) {
			percent += 0.04;
			if (rand <= percent * multiplier) return new ItemStack(Material.COAL_ORE);
			
			percent += 0.004;
			if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 9);
			
			return new ItemStack(Material.STONE);
		}
		
		if (type == Material.STONE_PICKAXE) {
			percent += 0.04;
			if (rand <= percent * multiplier) return new ItemStack(Material.COAL_ORE);
			percent += 0.02;
			if (rand <= percent * multiplier) return new ItemStack(Material.IRON_ORE);
			
			if (hasDiamond) {
				percent += 0.008;
				if (rand <= percent * multiplier) return new ItemStack(Material.LAPIS_ORE);
			}
			
			percent += 0.004;
			if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 9);
			percent += 0.0025;
			if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 0);
			
			if (hasDiamond) {
				percent += 0.001;
				if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 11);
			}
			
			return new ItemStack(Material.STONE);
		}
		
		if (type == Material.GOLD_PICKAXE) {
			percent += 0.04;
			if (rand <= percent * multiplier) return new ItemStack(Material.COAL_ORE);
			percent += 0.02;
			if (rand <= percent * multiplier) return new ItemStack(Material.IRON_ORE);
			percent += 0.015;
			if (rand <= percent * multiplier) return new ItemStack(Material.REDSTONE_ORE);
			percent += 0.0125;
			if (rand <= percent * multiplier) return new ItemStack(Material.EMERALD_ORE);
			
			if (hasDiamond) {
				percent += 0.008;
				if (rand <= percent * multiplier) return new ItemStack(Material.LAPIS_ORE);
			}
			
			if (hasDiamond) {
				percent += 0.0008;
				if (rand <= percent * multiplier * 2) return new ItemStack(Material.DIAMOND_ORE);
			}
			
			percent += 0.004;
			if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 9);
			percent += 0.0025;
			if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 0);
			percent += 0.001;
			if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 6);
			
			if (hasDiamond) {
				percent += 0.0004;
				if (rand <= percent * multiplier * 2) return new ItemStack(Material.STAINED_CLAY, 1, (short) 4);
				percent += 0.001;
				if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 11);
			}
			
			return new ItemStack(Material.STONE);
		}
		
		percent += 0.04;
		if (rand <= percent * multiplier) return new ItemStack(Material.COAL_ORE);
		percent += 0.02;
		if (rand <= percent * multiplier) return new ItemStack(Material.IRON_ORE);
		percent += 0.015;
		if (rand <= percent * multiplier) return new ItemStack(Material.REDSTONE_ORE);
		percent += 0.0125;
		if (rand <= percent * multiplier) return new ItemStack(Material.EMERALD_ORE);
		
		if (hasDiamond) {
			percent += 0.008;
			if (rand <= percent * multiplier) return new ItemStack(Material.LAPIS_ORE);
			percent += 0.0008;
			if (rand <= percent * multiplier) return new ItemStack(Material.DIAMOND_ORE);
		}
		
		percent += 0.004;
		if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 9);
		percent += 0.0025;
		if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 0);
		percent += 0.001;
		if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 6);
		
		if (hasDiamond) {
			percent += 0.0004;
			if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 4);
			percent += 0.001;
			if (rand <= percent * multiplier) return new ItemStack(Material.STAINED_CLAY, 1, (short) 11);
		}
		
		return new ItemStack(Material.STONE);
	}
	
	@EventHandler
	public void place(HangingPlaceEvent e) {
		if (e.getEntity().getType() == EntityType.ITEM_FRAME) {
			if (!IslandUtils.isMineras(e.getEntity().getLocation()) && !IslandUtils.isNimbus(e.getEntity().getLocation())) return;
			if (e.getEntity() == null || e.getPlayer().isOp()) return;
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void rotate(PlayerInteractEntityEvent e) {
		if (e.getRightClicked().getType() == EntityType.ITEM_FRAME) {
			if (!IslandUtils.isMineras(e.getRightClicked().getLocation()) && !IslandUtils.isNimbus(e.getRightClicked().getLocation())) return;
			if (e.getRightClicked() == null || e.getPlayer().isOp()) return;
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void take(EntityDamageByEntityEvent e) {
		if (e.getEntity().getType() == EntityType.ITEM_FRAME) {
			if (!IslandUtils.isMineras(e.getEntity().getLocation()) && !IslandUtils.isNimbus(e.getEntity().getLocation())) return;
			if (e.getEntity() == null || (e.getDamager() instanceof Player && ((Player) e.getDamager()).isOp())) return;
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void removeByEntity(HangingBreakByEntityEvent e) {
		if (e.getEntity().getType() == EntityType.ITEM_FRAME) {
			if (!IslandUtils.isMineras(e.getEntity().getLocation()) && !IslandUtils.isNimbus(e.getEntity().getLocation())) return;
			if (e.getEntity() == null || (e.getRemover() instanceof Player && ((Player) e.getRemover()).isOp())) return;
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void remove(HangingBreakEvent e) {
		if (e.getEntity().getType().equals(EntityType.ITEM_FRAME)) {
			if (!IslandUtils.isMineras(e.getEntity().getLocation()) && !IslandUtils.isNimbus(e.getEntity().getLocation())) return;
			if (e instanceof HangingBreakByEntityEvent) return;
			
			e.setCancelled(true);
		}
	}
	
	private class OreTask extends BukkitRunnable {
		private final Location loc;
		
		public OreTask(Location loc) {
			this.loc = loc;
			regen.add(this);
		}
		
		@SuppressWarnings("deprecation")
		public void run() {
			ItemStack ore = getRandomOre(loc, Material.DIAMOND_PICKAXE, false, 0);
			
			BlockState state = loc.getBlock().getState();
			state.setType(ore.getType());
			state.setRawData((byte) ore.getDurability());
			state.update(true, false);
			
			loc.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType());
			
			regen.remove(this);
		}
	}
}