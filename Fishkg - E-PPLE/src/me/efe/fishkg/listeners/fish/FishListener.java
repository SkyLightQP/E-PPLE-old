package me.efe.fishkg.listeners.fish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.efe.efecore.util.EfeUtils;
import me.efe.fishkg.Contest;
import me.efe.fishkg.Fishkg;
import me.efe.fishkg.Contest.Team;
import me.efe.fishkg.Fishkg.RodType;
import me.efe.fishkg.events.PlayerFishkgEvent;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

public class FishListener implements Listener {
	public Fishkg plugin;
	public Random rand = new Random();
	public HashMap<FishkgBiome, List<FishkgFish>> fishMap = new HashMap<FishkgBiome, List<FishkgFish>>();
	public HashMap<FishkgBiome, List<Biome>> biomeMap = new HashMap<FishkgBiome, List<Biome>>();
	
	public FishListener(Fishkg plugin) {
		this.plugin = plugin;
		createFishList();
	}
	
	public FishkgBiome getBiome(Biome biome) {
		FishkgBiome[] biomes = new FishkgBiome[] {FishkgBiome.OCEAN, FishkgBiome.SWAMPLAND, FishkgBiome.ICE, FishkgBiome.JUNGLE};
		if (biome == null) return biomes[EfeUtils.math.random.nextInt(biomes.length)];
		
		for (int i = 0; i < biomes.length; i ++) {
			for (Biome checkBiome : biomeMap.get(biomes[i])) {
				if (biome.equals(checkBiome)) {
					return biomes[i];
				}
			}
		}
		
		return FishkgBiome.ETC;
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void chat(AsyncPlayerChatEvent e) {
		if (Contest.isModTeam() && plugin.getConfig().getBoolean("fish.team.showTeam") && Contest.teamMap.containsKey(e.getPlayer().getUniqueId())) {
			
			Team team = Contest.teamMap.get(e.getPlayer().getUniqueId());
			
			if (team.equals(Team.RED)) {
				e.setFormat("¡×c[Red]¡×r "+e.getFormat());
			} else if (team.equals(Team.BLUE)) {
				e.setFormat("¡×b[Blue]¡×r "+e.getFormat());
			}
		}
	}
	
	@EventHandler
	public void fishing(PlayerFishEvent e) {
		if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) && ((e.getCaught() instanceof Item)) && Contest.isEnabled()) {
			
			UUID id = e.getPlayer().getUniqueId();
			if (Contest.isModTeam() && !plugin.getConfig().getBoolean("fish.team.noTeamCanPlay") && !Contest.teamMap.containsKey(id)) {
				return;
			}
			
			/* Randomize Fish */
			FishkgBiome biome = getBiome(e.getCaught().getWorld().getBiome(e.getCaught().getLocation().getBlockX(), e.getCaught().getLocation().getBlockZ()));
			FishkgFish fish = null;
			
			double currentRandomVal = Math.random();
			double cumulativeVal = 0;
			
			if (!plugin.getConfig().getBoolean("fish.separateBiome")) biome = getBiome(null);
			
			for (FishkgFish f : fishMap.get(biome)) {
				cumulativeVal += f.getPercent();
				if (currentRandomVal <= cumulativeVal) {
					fish = f.clone();
					
					break;
				}
			}
			
			if (fish == null) {
				plugin.getServer().getConsoleSender().sendMessage("¡×c[Fishkg] Fish couldn't be found! Biome: "+biome.name());
				return;
			}
			
			
			/* Define */
			boolean isFirst = false;
			boolean mas = false;
			boolean shi = false;
			boolean tec = false;
			
			Player p = e.getPlayer();
			
			
			/* Trash */
			if (Contest.isModJunk() || Math.random() <= plugin.getConfig().getDouble("fish.trashPercent") / 100 || e.getCaught().hasMetadata("fishkg_testfor")) {
				fish = setTrash(biome);
				
				e.getCaught().removeMetadata("fishkg_testfor", plugin);
			}
			
			
			/* Fishing Rods Effect */
			RodType type = plugin.getRodType(p.getItemInHand());
			
			if (plugin.getConfig().getBoolean("fishingRod.enable") == true && type != null) {
				double length = fish.getLength();
				
				if (type.equals(RodType.MASTER)) {
					mas = true;
				} else if (type.equals(RodType.CHAOS)) {
					
					if (EfeUtils.math.random.nextBoolean())
						fish.setLength((int)(length * 1.3) + EfeUtils.math.random.nextInt(10));
					else
						fish.setLength((int)(length * 0.7) + EfeUtils.math.random.nextInt(10));
					
				} else if (type.equals(RodType.SHIELD)) {
					shi = true;
				} else if (type.equals(RodType.TECHNICAL)) {
					tec = true;
					
					fish.setLength((int)(length * 1.5) + EfeUtils.math.random.nextInt(10));
				} else if (type.equals(RodType.LEGEND)) {
					length += (double) rand.nextInt(31);
				}
			}
			
			
			/* ETC */
			if (Contest.getRanker().isNewDate()) {
				Contest.getRanker().reset();
				Contest.getRanker().setNewDate();
			}
			
			if (fish.getLength() >= Contest.getRanker().getLength(1) && !Contest.isModTeam())
				isFirst = true;
			
			fish.generateItemStack(plugin, e.getPlayer(), isFirst, mas);
			
			if (plugin.getConfig().getBoolean("fish.forceTrashFish")) {
				fish.getItemStack().setType(Material.RAW_FISH);
				fish.getItemStack().setDurability((short) 0);
			}
			
			
			/* Event */
			PlayerFishkgEvent event = new PlayerFishkgEvent(e.getPlayer(), fish, biome, type);
			plugin.getServer().getPluginManager().callEvent(event);
			
			if (event.isCancelled()) {
				e.setCancelled(true);
				return;
			}
			
			
			/* Lure */
			if (plugin.getConfig().getBoolean("fishingRod.lure") && plugin.enchLure != null && plugin.hasLure(e.getPlayer().getItemInHand())) {
				plugin.removeLure(e.getPlayer().getItemInHand());
			}
			
			
			/* Debuff */
			if (!shi) {
				if (fish.hasEffect())
					p.addPotionEffect(fish.getEffect());
				
				if (fish.hasDamage())
					p.damage(fish.getDamage());
			}
			
			
			/* Message */
			broadcast(p, fish, shi, null);
			
			if (tec && Math.random() <= 0.7D) {
				announce(e.getPlayer(), "±×·¯³ª ¾Æ½±°Ôµµ Å×Å©´ÏÄÃ ³¬½Ë´ëÀÇ ¿µÇâÀ¸·Î ³õÄ¡°í ¸»¾Ò½À´Ï´Ù..");
				
				e.setCancelled(true);
				return;
			}
			
			if (isFirst) {
				broadcastFirst(p, fish, shi, null);
				
				if (plugin.getConfig().getBoolean("fish.prize.enable")) {
					givePrize(p);
				}
			}
			
			if (Contest.isModTeam()) {
				if (Contest.teamMap.containsKey(id)) {
					int score = (int) fish.getLength();
					
					if (Contest.teamMap.get(id).equals(Team.RED)) {
						Contest.addScore(Team.RED, score);
						
						plugin.getServer().broadcastMessage(plugin.main+"¡×c·¹µåÆÀ µæÁ¡!¡×r ¡×7[ÇöÀç "+Contest.getScore(Team.RED)+"Á¡]");
					} else if (Contest.teamMap.get(id).equals(Team.BLUE)) {
						Contest.addScore(Team.BLUE, score);
						
						plugin.getServer().broadcastMessage(plugin.main+"¡×bºí·çÆÀ µæÁ¡!¡×r ¡×7[ÇöÀç "+Contest.getScore(Team.BLUE)+"Á¡]");
					}
				} else if (p.hasPermission("plugin.join")) {
					p.sendMessage(plugin.main+"³¬½Ã´ëÈ¸ ÆÀÀü ¸ğµå°¡ ÁøÇàÁßÀÔ´Ï´Ù.");
					p.sendMessage(plugin.main+"Âü¿©ÇØº¸½Ã´Â °ÍÀº ¾î¶²°¡¿ä? ¡×8[/³¬½Ã´ëÈ¸ Âü°¡]");
				}
			}
			
			
			/* Sort Ranking */
			if (!Contest.isModTeam()) {
				Contest.getRanker().sort(fish.getLength(), p);
			}
			
			
			/* Set Fish */
			Item item = (Item) e.getCaught();
			item.setItemStack(fish.getItemStack());
		}
	}
	
	public void createFishList() {
		// ÀÌ¸§, È®·ü, ÃÖ¼Ò ±æÀÌ, ÃÖ´ë ±æÀÌ
		List<FishkgFish> list = new ArrayList<FishkgFish>();
		List<Biome> biomes = new ArrayList<Biome>();
		list.add(new FishkgFish("¹ü°í·¡", 0.001, 700, 1000));
		list.add(new FishkgFish("¹é»ó¾Æ¸®", 0.005, 400, 1000).damage(10, "%player%´Ô²²¼­ ¹é»ó¾Æ¸®·ÎºÎÅÍ °ø°İÀ» ¹Ş¾Ò½À´Ï´Ù!"));
		list.add(new FishkgFish("Âü´Ù¶û¾î", 0.01, 200, 300));
		list.add(new FishkgFish("¹Ù´Ù°ÅºÏ", 0.03, 90, 130));
		list.add(new FishkgFish("¿¬¾î", 0.05, 50, 100));
		list.add(new FishkgFish("°¨¼ºµ¼", 0.05, 30, 50));
		list.add(new FishkgFish("°íµî¾î", 0.2, 20, 40));
		list.add(new FishkgFish("¿ÀÂ¡¾î", 0.2, 10, 20).effect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 3), "%player%´Ô²²¼­ ¿ÀÂ¡¾î·ÎºÎÅÍ °ø°İÀ» ¹Ş¾Ò½À´Ï´Ù!"));
		list.add(new FishkgFish("²É°Ô", 0.1, 5, 15));
		list.add(new FishkgFish("ÇØÆÄ¸®", 0.1, 5, 15).effect(new PotionEffect(PotionEffectType.POISON, 100, 3), "%player%´Ô²²¼­ ÇØÆÄ¸®·ÎºÎÅÍ °ø°İÀ» ¹Ş¾Ò½À´Ï´Ù!"));
		list.add(new FishkgFish("¸êÄ¡", 0.1, 3, 8));
		list.add(new FishkgFish("»õ¿ì", 0.154, 3, 6));
		biomes.add(Biome.BEACH);
		biomes.add(Biome.OCEAN);
		addBiome(biomes, "DEEP_OCEAN");
		fishMap.put(FishkgBiome.OCEAN, list);
		biomeMap.put(FishkgBiome.OCEAN, biomes);
		
		list = new ArrayList<FishkgFish>();
		biomes = new ArrayList<Biome>();
		list.add(new FishkgFish("¹ìÀå¾î", 0.05, 70, 150));
		list.add(new FishkgFish("¸Ş±â", 0.15, 30, 100));
		list.add(new FishkgFish("À×¾î", 0.2, 50, 120));
		list.add(new FishkgFish("ºØ¾î", 0.2, 20, 53));
		list.add(new FishkgFish("Âü¸ô°³", 0.2, 8, 14));
		list.add(new FishkgFish("°¢½ÃºØ¾î", 0.2, 3, 6));
		biomes.add(Biome.SWAMPLAND);
		addBiome(biomes, "SWAMPLAND_MOUNTAINS");
		biomes.add(Biome.MUSHROOM_ISLAND);
		biomes.add(Biome.MUSHROOM_SHORE);
		addBiome(biomes, "ROOFED_FOREST");
		addBiome(biomes, "ROOFED_FOREST_MOUNTAINS");
		fishMap.put(FishkgBiome.SWAMPLAND, list);
		biomeMap.put(FishkgBiome.SWAMPLAND, biomes);
		
		list = new ArrayList<FishkgFish>();
		biomes = new ArrayList<Biome>();
		list.add(new FishkgFish("¼Û¾î", 0.1, 50, 70));
		list.add(new FishkgFish("ºù¾î", 0.9, 10, 20));
		//Snowy Biomes
		biomes.add(Biome.FROZEN_RIVER);
		biomes.add(Biome.ICE_PLAINS);
		addBiome(biomes, "ICE_PLAINS_SPIKES");
		addBiome(biomes, "COLD_BEACH");
		addBiome(biomes, "COLD_TAIGA");
		addBiome(biomes, "COLD_TAIGA_MOUNTAINS");
		//Cold Biomes
		biomes.add(Biome.EXTREME_HILLS);
		addBiome(biomes, "EXTREME_HILLS_MOUNTAINS");
		biomes.add(Biome.TAIGA);
		addBiome(biomes, "TAIGA_MOUNTAINS");
		addBiome(biomes, "MEGA_TAIGA");
		addBiome(biomes, "MEGA_SPRUCE_TAIGA");
		addBiome(biomes, "EXTREME_HILLS_PLUS");
		addBiome(biomes, "EXTREME_HILLS_PLUS_MOUNTAINS");
		addBiome(biomes, "STONE_BEACH");
		fishMap.put(FishkgBiome.ICE, list);
		biomeMap.put(FishkgBiome.ICE, biomes);
		
		list = new ArrayList<FishkgFish>();
		biomes = new ArrayList<Biome>();
		list.add(new FishkgFish("ÇÇ¶ó·çÅ©", 0.1, 400, 500));
		list.add(new FishkgFish("°ñ¸®¾Ñ Å¸ÀÌ°Å ÇÇ½Ã", 0.15, 100, 150));
		list.add(new FishkgFish("¾Æ·Î¿Í³ª", 0.2, 70, 140));
		list.add(new FishkgFish("Á¤±Û À×¾î", 0.2, 70, 140));
		list.add(new FishkgFish("ÇÇ¶ó³Ä", 0.35, 20, 40).damage(4, "%player%´Ô²²¼­ ÇÇ¶ó³Ä·ÎºÎÅÍ °ø°İÀ» ¹Ş¾Ò½À´Ï´Ù!"));
		biomes.add(Biome.JUNGLE);
		addBiome(biomes, "JUNGLE_MOUNTAINS");
		addBiome(biomes, "JUNGLE_EDGE");
		addBiome(biomes, "JUNGLE_EDGE_MOUNTAINS");
		biomes.add(Biome.JUNGLE_HILLS);
		fishMap.put(FishkgBiome.JUNGLE, list);
		biomeMap.put(FishkgBiome.JUNGLE, biomes);
		
		list = new ArrayList<FishkgFish>();
		biomes = new ArrayList<Biome>();
		list.add(new FishkgFish("¿­¸ñ¾î", 0.04, 30, 70));
		list.add(new FishkgFish("»êÃµ¾î", 0.05, 20, 30));
		list.add(new FishkgFish("½î°¡¸®", 0.15, 20, 30));
		list.add(new FishkgFish("ÀÚ°¡»ç¸®", 0.2, 10, 14));
		list.add(new FishkgFish("Àº¾î", 0.2, 10, 20));
		list.add(new FishkgFish("¹Ğ¾î", 0.2, 4, 12));
		list.add(new FishkgFish("ÇÇ¶ó¹Ì", 0.16, 8, 12));
		fishMap.put(FishkgBiome.ETC, list);
		biomeMap.put(FishkgBiome.ETC, biomes);
	}
	
	public void addBiome(List<Biome> list, String name) {
		Biome biome = Biome.valueOf(name);
		if (biome == null) return;
		
		list.add(biome);
	}
	
	public FishkgFish setTrash(FishkgBiome biome) {
		FishkgTrash[] trashes = new FishkgTrash[5];
		
		if (biome.equals(FishkgBiome.OCEAN)) {
			trashes = new FishkgTrash[]{
					new FishkgTrash("±ÜÀº ¹®È­»óÇ°±Ç", 10, 15, new ItemStack(Material.PAPER)), 
					new FishkgTrash("Minecraft.exe", 15.2, new ItemStack(Material.GRASS)), 
					new FishkgTrash("´©°¡ ¾²´Ù¹ö¸° ¿¬°¡½Ã", 10, 20, new ItemStack(Material.STRING, 1)), 
					new FishkgTrash("Wi-¥ğ", 3.14, new ItemStack(Material.PUMPKIN_PIE)), 
					new FishkgTrash("¹«ÀÎµµ¿¡ ÀÖ´ø ÇÑ ³²¼ºÀÌ SOS¸¦ º¸³»±â À§ÇØ ¾´ ÆíÁö°¡ ´ã°ÜÀÖ¾úÁö¸¸ ¹°¿¡ Á¥¾î ÇüÃ¼¸¦ ¾Ë¾Æº¼ ¼ö ¾ø°Ô µÇ¾ú´ø ½½ÇÂ »ç¿¬ÀÇ ¹°º´", 10, 20, new ItemStack(Material.POTION))
			};
		} else if (biome.equals(FishkgBiome.SWAMPLAND)) {
			trashes = new FishkgTrash[]{
					new FishkgTrash("ÃòÆÄ®½º ¸·´ë", 5, 7, new ItemStack(Material.STICK)), 
					new FishkgTrash("ÀÏÈ¸¿ë Á¾ÀÌÄÅ Á¾ÀÌ", 5, 10, new ItemStack(Material.PAPER)), 
					new FishkgTrash("Àß¸ø ¼³Ä¡ÇÑ Èæ¿ä¼®", 2, 5, new ItemStack(Material.OBSIDIAN)), 
					new FishkgTrash("Ã»¼Ò³â º¸È£¹ı Á¦ 23Á¶ 3Ç×", 2, 5, new ItemStack(Material.PAPER)), 
					new FishkgTrash("°í¾çÀÌ¸À ¹ö¼¸", 10, 20, new ItemStack(Material.BROWN_MUSHROOM))
			};
		} else if (biome.equals(FishkgBiome.ICE)) {
			trashes = new FishkgTrash[]{
					new FishkgTrash("´©±º°¡ ½ÇÅ©ÅÍÄ¡·Î Ã¤ÁıÇÏ´Ù ¾È ÁÖ¿î ¾óÀ½", 2, 5, new ItemStack(Material.ICE)), 
					new FishkgTrash("½ºÆùÁö¹ä", 5, 6, new ItemStack(Material.SPONGE)), 
					new FishkgTrash("Çã¼öÀÇ ¾Æ¹öÁö Çã¼ö¾Æºñ", 0.01, 5, new ItemStack(Material.WHEAT)), 
					new FishkgTrash("³í¹®ÀÌ µÉ °õ±¹", 0.01, new ItemStack(Material.MUSHROOM_SOUP)), 
					new FishkgTrash("\"ÆÄÀÎ\" ¾ÖÇÃ", 2, 5, new ItemStack(Material.APPLE))
			};
		} else if (biome.equals(FishkgBiome.JUNGLE)) {
			trashes = new FishkgTrash[]{
					new FishkgTrash("Àı´ë ÀÌºĞµéÀ» ³î¶ó°Ô ÇØ¼± ¾È µÉ ³ª¹µÀÙ", 2, 5, new ItemStack(Material.LEAVES, 1, (short) 3)), 
					new FishkgTrash("ÀÏÈ¸¿ë Á¾ÀÌÄÅ", 2, 5, new ItemStack(Material.FISHING_ROD, 1, (short) 60)), 
					new FishkgTrash("¿·Áı_´©³ª¿Í_ÇÔ²².avi", 2, 5, new ItemStack(Material.RECORD_4)), 
					new FishkgTrash("Èæ¿°·æÀÇ ¾Ë", 2, 5, new ItemStack(Material.EGG)), 
					new FishkgTrash("ÀÌ¹ø¿¡ Ã³À½ ¹Ş¾Æº¸´Â ÃÊÄİ¸´", 5, 10, new ItemStack(Material.INK_SACK, 1, (short) 3))
			};
		} else {
			trashes = new FishkgTrash[]{
					new FishkgTrash("°ü¸®ÀÚ°¡ Èê¸° ³ª¹« µµ³¢", 10, 25, new ItemStack(Material.WOOD_AXE, 1, (short) 100)), 
					new FishkgTrash("Åä³¢ÀÇ °£", 10, 15, new ItemStack(Material.ROTTEN_FLESH)), 
					new FishkgTrash("¥ğ", 3.14, new ItemStack(Material.PUMPKIN_PIE)), 
					new FishkgTrash("Craftbukkit 1.5.2-R2.0", 10, 15, new ItemStack(Material.BUCKET)), 
					new FishkgTrash("½Ç¼ö·Î Q´©¸¥ ³¬½Ë´ë", 10, 25, new ItemStack(Material.FISHING_ROD, 1, (short) 4))
			};
		}
		
		return trashes[rand.nextInt(trashes.length)];
	}
	
	public void broadcast(Player p, FishkgFish fish, boolean isShield, String message) {
		announce(p, p.getName()+"´Ô²²¼­ "+fish.getLength()+"cmÂ¥¸® "+fish.getObjectiveName()+" ³¬À¸¼Ì½À´Ï´Ù!");
		
		if (plugin.getConfig().getBoolean("fish.titleMessage")) {
			for (Player all : EfeUtils.player.getOnlinePlayers()) {
				TitleAPI.sendTitle(all, 10, 30, 10, "", "¡×b"+fish.getName()+" ["+fish.getLength()+"cm]");
			}
		}
		
		if (fish.hasEffect() || fish.hasDamage()) {
			announce(p, fish.getMessage().replace("%player%", p.getName()));
			
			if (isShield)
				announce(p, "±×·¯³ª ³»¼º ³¬½Ë´ëÀÇ ¿µÇâÀ¸·Î °ø°İÀ» 100% ¹æ¾îÇß½À´Ï´Ù!");
		}
	}
	
	public void broadcastFirst(Player p, FishkgFish fish, boolean isShield, String message) {
		double range = plugin.getConfig().getDouble("fish.announceRangeFirst");
		
		if (range != 0.0D) {
			for (Player all : p.getWorld().getPlayers()) {
				if (p.getLocation().distance(all.getLocation()) <= range) {
					
					if (range > plugin.getConfig().getDouble("fish.announceRange")) {
						all.sendMessage(plugin.main+"¡×a¢Æ¡×r "+p.getName()+"´Ô²²¼­ "+fish.getLength()+"cmÂ¥¸® "+fish.getObjectiveName()+" ³¬À¸¼Ì½À´Ï´Ù!");
					}
					
					all.sendMessage(plugin.main+"¡×a¢Æ¡×r »õ·Î¿î 1µîÀÇ Åº»ıÀÔ´Ï´Ù!");
					
					if (plugin.getConfig().getBoolean("fish.prize.title"))
						TitleAPI.sendTitle(all, 10, 50, 20, "¡×b¡×lNew 1st Fish", "¡×9"+fish.getName()+" ["+fish.getLength()+"cm]");
				}
			}
		} else {
			plugin.getServer().broadcastMessage(plugin.main+"¡×a¢Æ¡×r »õ·Î¿î 1µîÀÇ Åº»ıÀÔ´Ï´Ù!");
			
			if (plugin.getConfig().getBoolean("fish.prize.title"))
				for (Player all : EfeUtils.player.getOnlinePlayers())
					TitleAPI.sendTitle(all, 10, 50, 20, "¡×b¡×lNew 1st Fish", "¡×9"+fish.getName()+" ["+fish.getLength()+"cm]");
		}
	}
	
	public void announce(Player p, String message) {
		double range = plugin.getConfig().getDouble("fish.announceRange");
		
		if (range != 0.0D) {
			for (Player all : p.getWorld().getPlayers()) {
				if (p.getLocation().distance(all.getLocation()) <= range) {
					all.sendMessage(plugin.main+"¡×a¢Æ¡×r "+message);
				}
			}
		} else {
			plugin.getServer().broadcastMessage(plugin.main+"¡×a¢Æ¡×r "+message);
		}
	}
	
	public void givePrize(Player p) {
		if (plugin.getConfig().getBoolean("fish.prize.fireworks")) {
			Firework firework = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
			FireworkMeta meta = firework.getFireworkMeta();
			
			Type type = Type.values()[rand.nextInt(Type.values().length)];
			Color color = Color.fromBGR(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
			Color fade = Color.fromBGR(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
			if (rand.nextBoolean()) fade = color;
			
			FireworkEffect effect = FireworkEffect.builder()
					.flicker(rand.nextBoolean())
					.trail(rand.nextBoolean())
					.with(type)
					.withColor(color)
					.withFade(fade)
					.build();
			meta.addEffect(effect);
			meta.setPower(1);
			firework.setFireworkMeta(meta);
		}
		
		for (String commands : plugin.getConfig().getStringList("fish.prize.console-commands")) {
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), commands.replaceAll("%player%", p.getName()));
		}
	}
	
	public enum FishkgBiome {
		OCEAN, SWAMPLAND, ICE, JUNGLE, ETC
	}
}
