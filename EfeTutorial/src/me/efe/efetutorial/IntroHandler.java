package me.efe.efetutorial;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import de.slikey.effectlib.util.ParticleEffect;

public class IntroHandler {
	private EfeTutorial plugin;
	public String LINE = "§e§l************************************************";
	public Location centerLoc;
	public Location playerLoc;
	public Location tutorialLoc;
	public List<UUID> users = new ArrayList<UUID>();
	
	public IntroHandler(EfeTutorial plugin) {
		this.plugin = plugin;
		
		World world = EfeServer.getInstance().world;
		
		this.centerLoc = new Location(world, 3700, 200, 1500);
		this.playerLoc = new Location(world, 3703, 201, 1503, -226.1F, 31.8F);
		this.tutorialLoc = IslandUtils.getIsleLoc(IslandUtils.TUTORIAL_START);
	}
	
	public void startIntro(Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		
		player.setAllowFlight(true);
		player.setFlying(true);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*7, 0));
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*60*999, 0));
		
		for (Player all : plugin.getServer().getOnlinePlayers()) {
			player.hidePlayer(all);
		}
		
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0F, 0.1F);
		TitleAPI.sendTitle(player, 30, 50, 20, "§c§l* E-PPLE *", "- Intro -");
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				player.teleport(playerLoc);
				player.setFlying(true);
				
				users.add(player.getUniqueId());
				
				TutorialState.set(player, TutorialState.INTRO_STARTED);
				
				proceedIntro(player, 0, 100);
			}
		}, 100L);
	}
	
	public void proceedIntro(final Player player, final int num, final long delay) {
		if (!isInIntro(player))
			return;
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (player == null) return;
				
				int i = 0;
				long nextDelay = 60;
				List<String> list = new ArrayList<String>();
				
				if (num == i++) {
					if (PlayerData.get(player).hasPlayedIntro()) {
						TitleAPI.sendTitle(player, 10, 40, 10, "", "인트로를 스킵하려면: §e/스킵");
					}
					
					list.add(LINE);
					list.add("");
					list.add("");
					list.add("안녕하세요!");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);

					spawnItem(player, centerLoc.clone().add(0.5, 1.6, 0.5));
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("");
					list.add("안녕하세요!");
					list.add("E-PPLE에 오신 것을 진심으로 환영합니다.");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("");
					list.add("안녕하세요!");
					list.add("E-PPLE에 오신 것을 진심으로 환영합니다.");
					list.add("");
					list.add("이곳이 낯설 당신을 위한, 짧은 §a인트로§r입니다!");
					list.add("");
					list.add("");
					list.add(LINE);
					
					nextDelay = 100;
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("먼저 E-PPLE은 §a신개념 야생 서버§r입니다.");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("먼저 E-PPLE은 §a신개념 야생 서버§r입니다.");
					list.add("");
					list.add("바다를 배경으로 한 월드에서 채집하며");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					setBlock(player, centerLoc, Material.SAND);
					particleBlock(player, centerLoc.clone().add(0.5, 0.5, 0.5), Material.SAND);
					
					sound(player, Sound.DIG_SAND);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("먼저 E-PPLE은 §a신개념 야생 서버§r입니다.");
					list.add("");
					list.add("바다를 배경으로 한 월드에서 채집하며");
					list.add("§b자신만의 섬§r을 가져 입맛대로 꾸밀 수 있지요!");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("먼저 E-PPLE은 §a신개념 야생 서버§r입니다.");
					list.add("");
					list.add("바다를 배경으로 한 월드에서 채집하며");
					list.add("§b자신만의 섬§r을 가져 입맛대로 꾸밀 수 있지요!");
					list.add("");
					list.add("이 정도가 E-PPLE의 고유한 컨셉입니다.");
					list.add("");
					list.add(LINE);
					
					nextDelay = 100;
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("월드는 두 가지 개념으로 나뉩니다.");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					setBlock(player, centerLoc, Material.AIR);
					particleBlock(player, centerLoc.clone().add(0.5, 0.5, 0.5), Material.SAND);
					
					sound(player, Sound.DIG_SAND);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("월드는 두 가지 개념으로 나뉩니다.");
					list.add("");
					list.add("§c스텔라 오션§r은 채집, 퀘스트 등 주요한 시스템을 위한 공간이며");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					setBlock(player, centerLoc.clone().add(-1, 0, 1), Material.STATIONARY_WATER);
					particleBlock(player, centerLoc.clone().add(-1, 0, 1).add(0.5, 0.5, 0.5), Material.STATIONARY_WATER);
					
					sound(player, Sound.WATER);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("월드는 두 가지 개념으로 나뉩니다.");
					list.add("");
					list.add("§c스텔라 오션§r은 채집, 퀘스트 등 주요한 시스템을 위한 공간이며");
					list.add("§b유저 월드§r는 유저의 섬들이 저장되어있는 쉼터의 공간입니다.");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					setBlock(player, centerLoc.clone().add(1, 0, -1), Material.STATIONARY_WATER);
					particleBlock(player, centerLoc.clone().add(1, 0, -1).add(0.5, 0.5, 0.5), Material.STATIONARY_WATER);
					
					sound(player, Sound.WATER);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("월드는 두 가지 개념으로 나뉩니다.");
					list.add("");
					list.add("§c스텔라 오션§r은 채집, 퀘스트 등 주요한 시스템을 위한 공간이며");
					list.add("§b유저 월드§r는 유저의 섬들이 저장되어있는 쉼터의 공간입니다.");
					list.add("");
					list.add("물론 두 월드 모두 바다입니다!");
					list.add("");
					list.add(LINE);
					
					nextDelay = 100;
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("앞서 말씀드린 듯이 자신만의 섬을 가질 수 있습니다.");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					setBlock(player, centerLoc.clone().add(-1, 0, 1), Material.AIR);
					particleBlock(player, centerLoc.clone().add(-1, 0, 1).add(0.5, 0.5, 0.5), Material.STATIONARY_WATER);
					setBlock(player, centerLoc.clone().add(1, 0, -1), Material.AIR);
					particleBlock(player, centerLoc.clone().add(1, 0, -1).add(0.5, 0.5, 0.5), Material.STATIONARY_WATER);
					setBlock(player, centerLoc, Material.GRASS);
					particleBlock(player, centerLoc.clone().add(0.5, 0.5, 0.5), Material.GRASS);
					
					sound(player, Sound.DIG_GRASS);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("앞서 말씀드린 듯이 자신만의 섬을 가질 수 있습니다.");
					list.add("그럼, 스카이블럭과 다른 점은 무엇일까요?");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("앞서 말씀드린 듯이 자신만의 섬을 가질 수 있습니다.");
					list.add("그럼, 스카이블럭과 다른 점은 무엇일까요?");
					list.add("");
					list.add("바로 §b섬 커스터마이징§r입니다!");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("앞서 말씀드린 듯이 자신만의 섬을 가질 수 있습니다.");
					list.add("그럼, 스카이블럭과 다른 점은 무엇일까요?");
					list.add("");
					list.add("바로 §b섬 커스터마이징§r입니다!");
					list.add("E-PPLE은 섬에 대한 §a수많은 설정§r과 섬 광고, 랭킹 등의");
					list.add("");
					list.add("");
					list.add(LINE);
					
					setBlock(player, centerLoc, Material.MYCEL);
					particleBlock(player, centerLoc.clone().add(0.5, 0.5, 0.5), Material.MYCEL);
					
					sound(player, Sound.DIG_GRASS);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("앞서 말씀드린 듯이 자신만의 섬을 가질 수 있습니다.");
					list.add("그럼, 스카이블럭과 다른 점은 무엇일까요?");
					list.add("");
					list.add("바로 §b섬 커스터마이징§r입니다!");
					list.add("E-PPLE은 섬에 대한 §a수많은 설정§r과 섬 광고, 랭킹 등의");
					list.add("다양한 섬 관련 시스템들을 제공하고 있습니다.");
					list.add("");
					list.add(LINE);
					
					nextDelay = 100;
					
					setBlock(player, centerLoc, Material.DIRT, (byte) 2);
					particleBlock(player, centerLoc.clone().add(0.5, 0.5, 0.5), Material.DIRT, (byte) 2);
					
					sound(player, Sound.DIG_GRASS);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("예를 들어, 여러분은 자신의 섬을 카페로 만들 수도 있고,");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					changeItems(player, new ItemStack(Material.POTION));
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("예를 들어, 여러분은 자신의 섬을 카페로 만들 수도 있고,");
					list.add("PvP 결투장으로 만들어 홍보할 수도 있고,");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					changeItems(player, new ItemStack(Material.IRON_SWORD));
					particleItem(player, centerLoc.clone().add(0.5, 1.6, 0.5), Material.POTION);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("예를 들어, 여러분은 자신의 섬을 카페로 만들 수도 있고,");
					list.add("PvP 결투장으로 만들어 홍보할 수도 있고,");
					list.add("개인 상점을 이용해 대규모 시장을 운영할 수도 있으며");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					changeItems(player, new ItemStack(Material.CHEST));
					particleItem(player, centerLoc.clone().add(0.5, 1.6, 0.5), Material.IRON_SWORD);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("예를 들어, 여러분은 자신의 섬을 카페로 만들 수도 있고,");
					list.add("PvP 결투장으로 만들어 홍보할 수도 있고,");
					list.add("개인 상점을 이용해 대규모 시장을 운영할 수도 있으며");
					list.add("친구에게 권한을 주어 함께 한 섬에서 생활할 수도 있습니다.");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					changeItems(player, new ItemStack(Material.BED));
					particleItem(player, centerLoc.clone().add(0.5, 1.6, 0.5), Material.WOOD);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("예를 들어, 여러분은 자신의 섬을 카페로 만들 수도 있고,");
					list.add("PvP 결투장으로 만들어 홍보할 수도 있고,");
					list.add("개인 상점을 이용해 대규모 시장을 운영할 수도 있으며");
					list.add("친구에게 권한을 주어 함께 한 섬에서 생활할 수도 있습니다.");
					list.add("");
					list.add("§a아이디어만 있다면 무엇이든지 가능합니다.§r");
					list.add("");
					list.add(LINE);
					
					changeItems(player, new ItemStack(Material.AIR));
					particleItem(player, centerLoc.clone().add(0.5, 1.6, 0.5), Material.BED);
					
					nextDelay = 100;
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("E-PPLE의 채집 방법은 기존의 것을 뒤집어 놓았습니다.");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					setBlock(player, centerLoc, Material.GRASS);
					particleBlock(player, centerLoc.clone().add(0.5, 0.5, 0.5), Material.GRASS);
					
					sound(player, Sound.DIG_GRASS);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("E-PPLE의 채집 방법은 기존의 것을 뒤집어 놓았습니다.");
					list.add("");
					list.add("채광, 사냥, 농사, 낚시, 인첸트, 보스 레이드 등");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					changeItems(player, new ItemStack(Material.IRON_PICKAXE));
					
					nextDelay = 30;
					
					sound(player, Sound.ITEM_PICKUP);
				} else if (num == i++) {
					changeItems(player, new ItemStack(Material.BOW));
					
					nextDelay = 30;
					
					sound(player, Sound.ITEM_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("E-PPLE의 채집 방법은 기존의 것을 뒤집어 놓았습니다.");
					list.add("");
					list.add("채광, 사냥, 농사, 낚시, 인첸트, 보스 레이드 등");
					list.add("마인크래프트 기존 야생을 기반으로 하여");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					changeItems(player, new ItemStack(Material.IRON_HOE));
					
					nextDelay = 30;
					
					sound(player, Sound.ITEM_PICKUP);
				} else if (num == i++) {
					changeItems(player, new ItemStack(Material.FISHING_ROD));
					
					nextDelay = 30;
					
					sound(player, Sound.ITEM_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("E-PPLE의 채집 방법은 기존의 것을 뒤집어 놓았습니다.");
					list.add("");
					list.add("채광, 사냥, 농사, 낚시, 인첸트, 보스 레이드 등");
					list.add("마인크래프트 기존 야생을 기반으로 하여");
					list.add("고유의 특성은 유지하되, 재미와 편리를 한 층 더한");
					list.add("");
					list.add("");
					list.add(LINE);
					
					changeItems(player, new ItemStack(Material.ENCHANTMENT_TABLE));
					
					nextDelay = 30;
					
					sound(player, Sound.ITEM_PICKUP);
				} else if (num == i++) {
					changeItems(player, new ItemStack(Material.DRAGON_EGG));
					
					nextDelay = 30;
					
					sound(player, Sound.ITEM_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("E-PPLE의 채집 방법은 기존의 것을 뒤집어 놓았습니다.");
					list.add("");
					list.add("채광, 사냥, 농사, 낚시, 인첸트, 보스 레이드 등");
					list.add("마인크래프트 기존 야생을 기반으로 하여");
					list.add("고유의 특성은 유지하되, 재미와 편리를 한 층 더한");
					list.add("여러분에게 조금 낯설, 매력적인 채집 방법입니다.");
					list.add("");
					list.add(LINE);
					
					removeItems(player);
					particleItem(player, centerLoc.clone().add(0.5, 1.6, 0.5), Material.DRAGON_EGG);
					
					nextDelay = 100;
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("");
					list.add("인트로를 마쳤습니다! 읽어주셔서 감사합니다. :)");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					setBlock(player, centerLoc, Material.AIR);
					particleBlock(player, centerLoc.clone().add(0.5, 0.5, 0.5), Material.GRASS);
					
					sound(player, Sound.DIG_GRASS);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("");
					list.add("인트로를 마쳤습니다! 읽어주셔서 감사합니다. :)");
					list.add("");
					list.add("다음으로 진행될 §a튜토리얼§r은, 채집 등의 서버 활동을");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("");
					list.add("인트로를 마쳤습니다! 읽어주셔서 감사합니다. :)");
					list.add("");
					list.add("다음으로 진행될 §a튜토리얼§r은, 채집 등의 서버 활동을");
					list.add("직접 체험해 배우는 가이드가 되겠습니다.");
					list.add("");
					list.add("");
					list.add(LINE);
					
					nextDelay = 100;
					
					sound(player, Sound.LEVEL_UP);
				} else if (num == i++) {
					finishIntro(player);
					
					return;
				}
				
				for (String message : list) {
					player.sendMessage("§a▒§r "+message);
				}
				
				
				proceedIntro(player, num + 1, nextDelay);
			}
		}, delay);
	}
	
	public void finishIntro(final Player player) {
		removeItems(player);
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*6, 0));
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (player == null || player.isDead()) return;
				
				users.remove(player.getUniqueId());
				
				for (Player all : plugin.getServer().getOnlinePlayers()) {
					player.showPlayer(all);
				}
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						if (player == null || player.isDead()) return;
						
						player.removePotionEffect(PotionEffectType.INVISIBILITY);
						player.setGameMode(GameMode.SURVIVAL);
						player.setAllowFlight(false);
						
						PlayerData.get(player).setPlayedIntro();
						TutorialState.set(player, TutorialState.INTRO_FINISHED);
						
						player.teleport(tutorialLoc);
						player.setFallDistance(0.0F);
						
						plugin.tutorialHandler.startTutorial(player);
					}
				}, 40);
			}
		}, 60);
	}
	
	public boolean isInIntro(Player player) {
		return users.contains(player.getUniqueId());
	}
	
	public void sound(Player player, Sound sound) {
		sound(player, sound, 1.0F);
	}
	
	public void sound(Player player, Sound sound, float pitch) {
		player.playSound(player.getLocation(), sound, 1.0F, pitch);
	}
	
	public void setBlock(Player player, Location loc, Material type) {
		setBlock(player, loc, type, (byte) 0x0);
	}
	
	@SuppressWarnings("deprecation")
	public void setBlock(Player player, Location loc, Material type, byte data) {
		player.sendBlockChange(loc, type, data);
	}
	
	public void particleBlock(Player player, Location loc, Material type) {
		particleBlock(player, loc, type, (byte) 0x0);
	}
	
	public void particleBlock(Player player, Location loc, Material type, byte data) {
		ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(type, data), 0.05F, 0.05F, 0.05F, 1.0F, 75, loc, player);
	}
	
	public void spawnItem(Player player, Location loc) {
		Hologram holo = HologramsAPI.createHologram(plugin, loc);
		
		holo.getVisibilityManager().setVisibleByDefault(false);
		holo.getVisibilityManager().showTo(player);
	}
	
	public void changeItems(Player player, ItemStack item) {
		for (Hologram holo : HologramsAPI.getHolograms(plugin)) {
			if (holo.getVisibilityManager().isVisibleTo(player)) {
				
				if (holo.size() > 0)
					holo.removeLine(0);
				
				if (item.getType() != Material.AIR)
					holo.appendItemLine(item);
			}
		}
	}
	
	public void removeItems(Player player) {
		for (Hologram holo : HologramsAPI.getHolograms(plugin)) {
			if (holo.getVisibilityManager().isVisibleTo(player)) {
				holo.delete();
			}
		}
	}
	
	public void particleItem(Player player, Location loc, Material type) {
		ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(type, (byte) 0), 0.1F, 0.1F, 0.1F, 0.05F, 10, loc, player);
	}
}