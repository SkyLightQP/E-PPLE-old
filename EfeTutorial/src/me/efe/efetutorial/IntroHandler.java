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
	public String LINE = "��e��l************************************************";
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
		TitleAPI.sendTitle(player, 30, 50, 20, "��c��l* E-PPLE *", "- Intro -");
		
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
						TitleAPI.sendTitle(player, 10, 40, 10, "", "��Ʈ�θ� ��ŵ�Ϸ���: ��e/��ŵ");
					}
					
					list.add(LINE);
					list.add("");
					list.add("");
					list.add("�ȳ��ϼ���!");
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
					list.add("�ȳ��ϼ���!");
					list.add("E-PPLE�� ���� ���� �������� ȯ���մϴ�.");
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
					list.add("�ȳ��ϼ���!");
					list.add("E-PPLE�� ���� ���� �������� ȯ���մϴ�.");
					list.add("");
					list.add("�̰��� ���� ����� ����, ª�� ��a��Ʈ�Ρ�r�Դϴ�!");
					list.add("");
					list.add("");
					list.add(LINE);
					
					nextDelay = 100;
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("���� E-PPLE�� ��a�Ű��� �߻� ������r�Դϴ�.");
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
					list.add("���� E-PPLE�� ��a�Ű��� �߻� ������r�Դϴ�.");
					list.add("");
					list.add("�ٴٸ� ������� �� ���忡�� ä���ϸ�");
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
					list.add("���� E-PPLE�� ��a�Ű��� �߻� ������r�Դϴ�.");
					list.add("");
					list.add("�ٴٸ� ������� �� ���忡�� ä���ϸ�");
					list.add("��b�ڽŸ��� ����r�� ���� �Ը���� �ٹ� �� ������!");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("���� E-PPLE�� ��a�Ű��� �߻� ������r�Դϴ�.");
					list.add("");
					list.add("�ٴٸ� ������� �� ���忡�� ä���ϸ�");
					list.add("��b�ڽŸ��� ����r�� ���� �Ը���� �ٹ� �� ������!");
					list.add("");
					list.add("�� ������ E-PPLE�� ������ �����Դϴ�.");
					list.add("");
					list.add(LINE);
					
					nextDelay = 100;
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("����� �� ���� �������� �����ϴ�.");
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
					list.add("����� �� ���� �������� �����ϴ�.");
					list.add("");
					list.add("��c���ڶ� ���ǡ�r�� ä��, ����Ʈ �� �ֿ��� �ý����� ���� �����̸�");
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
					list.add("����� �� ���� �������� �����ϴ�.");
					list.add("");
					list.add("��c���ڶ� ���ǡ�r�� ä��, ����Ʈ �� �ֿ��� �ý����� ���� �����̸�");
					list.add("��b���� �����r�� ������ ������ ����Ǿ��ִ� ������ �����Դϴ�.");
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
					list.add("����� �� ���� �������� �����ϴ�.");
					list.add("");
					list.add("��c���ڶ� ���ǡ�r�� ä��, ����Ʈ �� �ֿ��� �ý����� ���� �����̸�");
					list.add("��b���� �����r�� ������ ������ ����Ǿ��ִ� ������ �����Դϴ�.");
					list.add("");
					list.add("���� �� ���� ��� �ٴ��Դϴ�!");
					list.add("");
					list.add(LINE);
					
					nextDelay = 100;
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("�ռ� �����帰 ���� �ڽŸ��� ���� ���� �� �ֽ��ϴ�.");
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
					list.add("�ռ� �����帰 ���� �ڽŸ��� ���� ���� �� �ֽ��ϴ�.");
					list.add("�׷�, ��ī�̺��� �ٸ� ���� �����ϱ��?");
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
					list.add("�ռ� �����帰 ���� �ڽŸ��� ���� ���� �� �ֽ��ϴ�.");
					list.add("�׷�, ��ī�̺��� �ٸ� ���� �����ϱ��?");
					list.add("");
					list.add("�ٷ� ��b�� Ŀ���͸���¡��r�Դϴ�!");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("�ռ� �����帰 ���� �ڽŸ��� ���� ���� �� �ֽ��ϴ�.");
					list.add("�׷�, ��ī�̺��� �ٸ� ���� �����ϱ��?");
					list.add("");
					list.add("�ٷ� ��b�� Ŀ���͸���¡��r�Դϴ�!");
					list.add("E-PPLE�� ���� ���� ��a������ ������r�� �� ����, ��ŷ ����");
					list.add("");
					list.add("");
					list.add(LINE);
					
					setBlock(player, centerLoc, Material.MYCEL);
					particleBlock(player, centerLoc.clone().add(0.5, 0.5, 0.5), Material.MYCEL);
					
					sound(player, Sound.DIG_GRASS);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("�ռ� �����帰 ���� �ڽŸ��� ���� ���� �� �ֽ��ϴ�.");
					list.add("�׷�, ��ī�̺��� �ٸ� ���� �����ϱ��?");
					list.add("");
					list.add("�ٷ� ��b�� Ŀ���͸���¡��r�Դϴ�!");
					list.add("E-PPLE�� ���� ���� ��a������ ������r�� �� ����, ��ŷ ����");
					list.add("�پ��� �� ���� �ý��۵��� �����ϰ� �ֽ��ϴ�.");
					list.add("");
					list.add(LINE);
					
					nextDelay = 100;
					
					setBlock(player, centerLoc, Material.DIRT, (byte) 2);
					particleBlock(player, centerLoc.clone().add(0.5, 0.5, 0.5), Material.DIRT, (byte) 2);
					
					sound(player, Sound.DIG_GRASS);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("���� ���, �������� �ڽ��� ���� ī��� ���� ���� �ְ�,");
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
					list.add("���� ���, �������� �ڽ��� ���� ī��� ���� ���� �ְ�,");
					list.add("PvP ���������� ����� ȫ���� ���� �ְ�,");
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
					list.add("���� ���, �������� �ڽ��� ���� ī��� ���� ���� �ְ�,");
					list.add("PvP ���������� ����� ȫ���� ���� �ְ�,");
					list.add("���� ������ �̿��� ��Ը� ������ ��� ���� ������");
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
					list.add("���� ���, �������� �ڽ��� ���� ī��� ���� ���� �ְ�,");
					list.add("PvP ���������� ����� ȫ���� ���� �ְ�,");
					list.add("���� ������ �̿��� ��Ը� ������ ��� ���� ������");
					list.add("ģ������ ������ �־� �Բ� �� ������ ��Ȱ�� ���� �ֽ��ϴ�.");
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
					list.add("���� ���, �������� �ڽ��� ���� ī��� ���� ���� �ְ�,");
					list.add("PvP ���������� ����� ȫ���� ���� �ְ�,");
					list.add("���� ������ �̿��� ��Ը� ������ ��� ���� ������");
					list.add("ģ������ ������ �־� �Բ� �� ������ ��Ȱ�� ���� �ֽ��ϴ�.");
					list.add("");
					list.add("��a���̵� �ִٸ� �����̵��� �����մϴ�.��r");
					list.add("");
					list.add(LINE);
					
					changeItems(player, new ItemStack(Material.AIR));
					particleItem(player, centerLoc.clone().add(0.5, 1.6, 0.5), Material.BED);
					
					nextDelay = 100;
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("E-PPLE�� ä�� ����� ������ ���� ������ ���ҽ��ϴ�.");
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
					list.add("E-PPLE�� ä�� ����� ������ ���� ������ ���ҽ��ϴ�.");
					list.add("");
					list.add("ä��, ���, ���, ����, ��þƮ, ���� ���̵� ��");
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
					list.add("E-PPLE�� ä�� ����� ������ ���� ������ ���ҽ��ϴ�.");
					list.add("");
					list.add("ä��, ���, ���, ����, ��þƮ, ���� ���̵� ��");
					list.add("����ũ����Ʈ ���� �߻��� ������� �Ͽ�");
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
					list.add("E-PPLE�� ä�� ����� ������ ���� ������ ���ҽ��ϴ�.");
					list.add("");
					list.add("ä��, ���, ���, ����, ��þƮ, ���� ���̵� ��");
					list.add("����ũ����Ʈ ���� �߻��� ������� �Ͽ�");
					list.add("������ Ư���� �����ϵ�, ��̿� ���� �� �� ����");
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
					list.add("E-PPLE�� ä�� ����� ������ ���� ������ ���ҽ��ϴ�.");
					list.add("");
					list.add("ä��, ���, ���, ����, ��þƮ, ���� ���̵� ��");
					list.add("����ũ����Ʈ ���� �߻��� ������� �Ͽ�");
					list.add("������ Ư���� �����ϵ�, ��̿� ���� �� �� ����");
					list.add("�����п��� ���� ����, �ŷ����� ä�� ����Դϴ�.");
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
					list.add("��Ʈ�θ� ���ƽ��ϴ�! �о��ּż� �����մϴ�. :)");
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
					list.add("��Ʈ�θ� ���ƽ��ϴ�! �о��ּż� �����մϴ�. :)");
					list.add("");
					list.add("�������� ����� ��aƩ�丮���r��, ä�� ���� ���� Ȱ����");
					list.add("");
					list.add("");
					list.add("");
					list.add(LINE);
					
					sound(player, Sound.ORB_PICKUP);
				} else if (num == i++) {
					list.add(LINE);
					list.add("");
					list.add("");
					list.add("��Ʈ�θ� ���ƽ��ϴ�! �о��ּż� �����մϴ�. :)");
					list.add("");
					list.add("�������� ����� ��aƩ�丮���r��, ä�� ���� ���� Ȱ����");
					list.add("���� ü���� ���� ���̵尡 �ǰڽ��ϴ�.");
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
					player.sendMessage("��a�ơ�r "+message);
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