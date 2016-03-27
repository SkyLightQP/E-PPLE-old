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
				e.setFormat("��c[Red]��r "+e.getFormat());
			} else if (team.equals(Team.BLUE)) {
				e.setFormat("��b[Blue]��r "+e.getFormat());
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
				plugin.getServer().getConsoleSender().sendMessage("��c[Fishkg] Fish couldn't be found! Biome: "+biome.name());
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
				announce(e.getPlayer(), "�׷��� �ƽ��Ե� ��ũ���� ���˴��� �������� ��ġ�� ���ҽ��ϴ�..");
				
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
						
						plugin.getServer().broadcastMessage(plugin.main+"��c������ ����!��r ��7[���� "+Contest.getScore(Team.RED)+"��]");
					} else if (Contest.teamMap.get(id).equals(Team.BLUE)) {
						Contest.addScore(Team.BLUE, score);
						
						plugin.getServer().broadcastMessage(plugin.main+"��b����� ����!��r ��7[���� "+Contest.getScore(Team.BLUE)+"��]");
					}
				} else if (p.hasPermission("plugin.join")) {
					p.sendMessage(plugin.main+"���ô�ȸ ���� ��尡 �������Դϴ�.");
					p.sendMessage(plugin.main+"�����غ��ô� ���� �����? ��8[/���ô�ȸ ����]");
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
		// �̸�, Ȯ��, �ּ� ����, �ִ� ����
		List<FishkgFish> list = new ArrayList<FishkgFish>();
		List<Biome> biomes = new ArrayList<Biome>();
		list.add(new FishkgFish("����", 0.001, 700, 1000));
		list.add(new FishkgFish("���Ƹ�", 0.005, 400, 1000).damage(10, "%player%�Բ��� ���Ƹ��κ��� ������ �޾ҽ��ϴ�!"));
		list.add(new FishkgFish("���ٶ���", 0.01, 200, 300));
		list.add(new FishkgFish("�ٴٰź�", 0.03, 90, 130));
		list.add(new FishkgFish("����", 0.05, 50, 100));
		list.add(new FishkgFish("������", 0.05, 30, 50));
		list.add(new FishkgFish("����", 0.2, 20, 40));
		list.add(new FishkgFish("��¡��", 0.2, 10, 20).effect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 3), "%player%�Բ��� ��¡��κ��� ������ �޾ҽ��ϴ�!"));
		list.add(new FishkgFish("�ɰ�", 0.1, 5, 15));
		list.add(new FishkgFish("���ĸ�", 0.1, 5, 15).effect(new PotionEffect(PotionEffectType.POISON, 100, 3), "%player%�Բ��� ���ĸ��κ��� ������ �޾ҽ��ϴ�!"));
		list.add(new FishkgFish("��ġ", 0.1, 3, 8));
		list.add(new FishkgFish("����", 0.154, 3, 6));
		biomes.add(Biome.BEACH);
		biomes.add(Biome.OCEAN);
		addBiome(biomes, "DEEP_OCEAN");
		fishMap.put(FishkgBiome.OCEAN, list);
		biomeMap.put(FishkgBiome.OCEAN, biomes);
		
		list = new ArrayList<FishkgFish>();
		biomes = new ArrayList<Biome>();
		list.add(new FishkgFish("�����", 0.05, 70, 150));
		list.add(new FishkgFish("�ޱ�", 0.15, 30, 100));
		list.add(new FishkgFish("�׾�", 0.2, 50, 120));
		list.add(new FishkgFish("�ؾ�", 0.2, 20, 53));
		list.add(new FishkgFish("������", 0.2, 8, 14));
		list.add(new FishkgFish("���úؾ�", 0.2, 3, 6));
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
		list.add(new FishkgFish("�۾�", 0.1, 50, 70));
		list.add(new FishkgFish("����", 0.9, 10, 20));
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
		list.add(new FishkgFish("�Ƕ��ũ", 0.1, 400, 500));
		list.add(new FishkgFish("�񸮾� Ÿ�̰� �ǽ�", 0.15, 100, 150));
		list.add(new FishkgFish("�Ʒοͳ�", 0.2, 70, 140));
		list.add(new FishkgFish("���� �׾�", 0.2, 70, 140));
		list.add(new FishkgFish("�Ƕ��", 0.35, 20, 40).damage(4, "%player%�Բ��� �Ƕ�ķκ��� ������ �޾ҽ��ϴ�!"));
		biomes.add(Biome.JUNGLE);
		addBiome(biomes, "JUNGLE_MOUNTAINS");
		addBiome(biomes, "JUNGLE_EDGE");
		addBiome(biomes, "JUNGLE_EDGE_MOUNTAINS");
		biomes.add(Biome.JUNGLE_HILLS);
		fishMap.put(FishkgBiome.JUNGLE, list);
		biomeMap.put(FishkgBiome.JUNGLE, biomes);
		
		list = new ArrayList<FishkgFish>();
		biomes = new ArrayList<Biome>();
		list.add(new FishkgFish("�����", 0.04, 30, 70));
		list.add(new FishkgFish("��õ��", 0.05, 20, 30));
		list.add(new FishkgFish("���", 0.15, 20, 30));
		list.add(new FishkgFish("�ڰ��縮", 0.2, 10, 14));
		list.add(new FishkgFish("����", 0.2, 10, 20));
		list.add(new FishkgFish("�о�", 0.2, 4, 12));
		list.add(new FishkgFish("�Ƕ��", 0.16, 8, 12));
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
					new FishkgTrash("���� ��ȭ��ǰ��", 10, 15, new ItemStack(Material.PAPER)), 
					new FishkgTrash("Minecraft.exe", 15.2, new ItemStack(Material.GRASS)), 
					new FishkgTrash("���� ���ٹ��� ������", 10, 20, new ItemStack(Material.STRING, 1)), 
					new FishkgTrash("Wi-��", 3.14, new ItemStack(Material.PUMPKIN_PIE)), 
					new FishkgTrash("���ε��� �ִ� �� ������ SOS�� ������ ���� �� ������ ����־����� ���� ���� ��ü�� �˾ƺ� �� ���� �Ǿ��� ���� �翬�� ����", 10, 20, new ItemStack(Material.POTION))
			};
		} else if (biome.equals(FishkgBiome.SWAMPLAND)) {
			trashes = new FishkgTrash[]{
					new FishkgTrash("���Į��� ����", 5, 7, new ItemStack(Material.STICK)), 
					new FishkgTrash("��ȸ�� ������ ����", 5, 10, new ItemStack(Material.PAPER)), 
					new FishkgTrash("�߸� ��ġ�� ��伮", 2, 5, new ItemStack(Material.OBSIDIAN)), 
					new FishkgTrash("û�ҳ� ��ȣ�� �� 23�� 3��", 2, 5, new ItemStack(Material.PAPER)), 
					new FishkgTrash("����̸� ����", 10, 20, new ItemStack(Material.BROWN_MUSHROOM))
			};
		} else if (biome.equals(FishkgBiome.ICE)) {
			trashes = new FishkgTrash[]{
					new FishkgTrash("������ ��ũ��ġ�� ä���ϴ� �� �ֿ� ����", 2, 5, new ItemStack(Material.ICE)), 
					new FishkgTrash("��������", 5, 6, new ItemStack(Material.SPONGE)), 
					new FishkgTrash("����� �ƹ��� ����ƺ�", 0.01, 5, new ItemStack(Material.WHEAT)), 
					new FishkgTrash("���� �� ����", 0.01, new ItemStack(Material.MUSHROOM_SOUP)), 
					new FishkgTrash("\"����\" ����", 2, 5, new ItemStack(Material.APPLE))
			};
		} else if (biome.equals(FishkgBiome.JUNGLE)) {
			trashes = new FishkgTrash[]{
					new FishkgTrash("���� �̺е��� ���� �ؼ� �� �� ������", 2, 5, new ItemStack(Material.LEAVES, 1, (short) 3)), 
					new FishkgTrash("��ȸ�� ������", 2, 5, new ItemStack(Material.FISHING_ROD, 1, (short) 60)), 
					new FishkgTrash("����_������_�Բ�.avi", 2, 5, new ItemStack(Material.RECORD_4)), 
					new FishkgTrash("�濰���� ��", 2, 5, new ItemStack(Material.EGG)), 
					new FishkgTrash("�̹��� ó�� �޾ƺ��� ���ݸ�", 5, 10, new ItemStack(Material.INK_SACK, 1, (short) 3))
			};
		} else {
			trashes = new FishkgTrash[]{
					new FishkgTrash("�����ڰ� �기 ���� ����", 10, 25, new ItemStack(Material.WOOD_AXE, 1, (short) 100)), 
					new FishkgTrash("�䳢�� ��", 10, 15, new ItemStack(Material.ROTTEN_FLESH)), 
					new FishkgTrash("��", 3.14, new ItemStack(Material.PUMPKIN_PIE)), 
					new FishkgTrash("Craftbukkit 1.5.2-R2.0", 10, 15, new ItemStack(Material.BUCKET)), 
					new FishkgTrash("�Ǽ��� Q���� ���˴�", 10, 25, new ItemStack(Material.FISHING_ROD, 1, (short) 4))
			};
		}
		
		return trashes[rand.nextInt(trashes.length)];
	}
	
	public void broadcast(Player p, FishkgFish fish, boolean isShield, String message) {
		announce(p, p.getName()+"�Բ��� "+fish.getLength()+"cm¥�� "+fish.getObjectiveName()+" �����̽��ϴ�!");
		
		if (plugin.getConfig().getBoolean("fish.titleMessage")) {
			for (Player all : EfeUtils.player.getOnlinePlayers()) {
				TitleAPI.sendTitle(all, 10, 30, 10, "", "��b"+fish.getName()+" ["+fish.getLength()+"cm]");
			}
		}
		
		if (fish.hasEffect() || fish.hasDamage()) {
			announce(p, fish.getMessage().replace("%player%", p.getName()));
			
			if (isShield)
				announce(p, "�׷��� ���� ���˴��� �������� ������ 100% ����߽��ϴ�!");
		}
	}
	
	public void broadcastFirst(Player p, FishkgFish fish, boolean isShield, String message) {
		double range = plugin.getConfig().getDouble("fish.announceRangeFirst");
		
		if (range != 0.0D) {
			for (Player all : p.getWorld().getPlayers()) {
				if (p.getLocation().distance(all.getLocation()) <= range) {
					
					if (range > plugin.getConfig().getDouble("fish.announceRange")) {
						all.sendMessage(plugin.main+"��a�ơ�r "+p.getName()+"�Բ��� "+fish.getLength()+"cm¥�� "+fish.getObjectiveName()+" �����̽��ϴ�!");
					}
					
					all.sendMessage(plugin.main+"��a�ơ�r ���ο� 1���� ź���Դϴ�!");
					
					if (plugin.getConfig().getBoolean("fish.prize.title"))
						TitleAPI.sendTitle(all, 10, 50, 20, "��b��lNew 1st Fish", "��9"+fish.getName()+" ["+fish.getLength()+"cm]");
				}
			}
		} else {
			plugin.getServer().broadcastMessage(plugin.main+"��a�ơ�r ���ο� 1���� ź���Դϴ�!");
			
			if (plugin.getConfig().getBoolean("fish.prize.title"))
				for (Player all : EfeUtils.player.getOnlinePlayers())
					TitleAPI.sendTitle(all, 10, 50, 20, "��b��lNew 1st Fish", "��9"+fish.getName()+" ["+fish.getLength()+"cm]");
		}
	}
	
	public void announce(Player p, String message) {
		double range = plugin.getConfig().getDouble("fish.announceRange");
		
		if (range != 0.0D) {
			for (Player all : p.getWorld().getPlayers()) {
				if (p.getLocation().distance(all.getLocation()) <= range) {
					all.sendMessage(plugin.main+"��a�ơ�r "+message);
				}
			}
		} else {
			plugin.getServer().broadcastMessage(plugin.main+"��a�ơ�r "+message);
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
