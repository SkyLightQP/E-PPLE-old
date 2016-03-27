package me.efe.efetutorial;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.efetutorial.listeners.TutorialListener;
import me.efe.unlimitedrpg.customexp.CustomExpAPI;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.connorlinfoot.titleapi.TitleAPI;

public class TutorialHandler {
	private EfeTutorial plugin;
	
	public TutorialHandler(EfeTutorial plugin) {
		this.plugin = plugin;
	}
	
	public void startTutorial(Player player) {
		proceedTutorial(player, 0, 60);
	}
	
	public void proceedTutorial(final Player player, final int num, final long delay) {
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (player == null) return;
				
				int i = 0;
				int nextDelay = 100;
				
				if (num == i++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "��d��lTutorial", "��5Ʃ�丮���� �����մϴ�!");
					player.sendMessage("��a�ơ�r Ʃ�丮���� �����մϴ�!");
					
					PlayerData data = PlayerData.get(player);
					
					if (data.getOptionBoat()) {
						
						if (data.getOptionMenu())
							player.getInventory().setItem(7, EfeServer.getInstance().myboat.getBoatItem(player));
						else
							player.getInventory().setItem(8, EfeServer.getInstance().myboat.getBoatItem(player));
					}
					
					if (data.getOptionMenu()) {
						player.getInventory().setItem(8, EfeUtils.item.createDisplayItem("��e/�޴�", new ItemStack(Material.NETHER_STAR), 
								new String[]{"Ŭ���ϸ� ���� �޴��� ���ϴ�."}));
					}
					
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "��d/��ųƮ��", "��5ä���� ������ �ִ� ��ų�� ���������.");
					player.sendMessage("��a�ơ�r ä���� ������ �ִ� ��ų�� ���������.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5���������� ��� SP�� ��ų�� ��� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ���������� ��� SP�� ��ų�� ��� �� �ֽ��ϴ�.");
					
					giveExpToLevelUp(player);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5���� ��ų�� ���������.");
					player.sendMessage("��a�ơ�r ���� ��ų�� ���������.");
					
					TutorialListener.USED_SP_BOAT = i;
					TutorialState.set(player, TutorialState.LEVEL_UP);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "��d����", "��5��Ʈ�� �̿��� �����غ�����.");
					player.sendMessage("��a�ơ�r ��Ʈ�� �̿��� �����غ�����.");
					
					TutorialListener.ENTERED_BOAT = i;
					TutorialState.set(player, TutorialState.LETS_ENTER_BOAT);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5��ħ������ �׺���̼��� �̿��� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ��ħ������ �׺���̼��� �̿��� �� �ֽ��ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5��ħ���� ��� ��Ŭ���غ�����.");
					player.sendMessage("��a�ơ�r ��ħ���� ��� ��Ŭ���غ�����.");
					
					TutorialListener.CLICKED_NAVIGATION = i;
					TutorialState.set(player, TutorialState.LETS_CLICK_NAVIGATION);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5\\\"�о��� ��\\\"�� Ŭ���ϼ���.");
					player.sendMessage("��a�ơ�r \"�о��� ��\"�� Ŭ���ϼ���.");
					
					TutorialListener.SELECTED_SKILL = i;
					TutorialState.set(player, TutorialState.LETS_SELECT_SKILL);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5��ħ���� ������ ���� ã�ư�������.");
					player.sendMessage("��a�ơ�r ��ħ���� ������ ���� ã�ư�������.");
					
					TutorialListener.LEFT_START_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_SKILL);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "��d�о�", "��5���� Ȱ���� ���, ä��, ���� �������� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ���� Ȱ���� ���, ä��, ���� �������� �ֽ��ϴ�.");
					
					TutorialListener.ARRIVED_SKILL_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5ũ�� ��ų�� ���, ä��, ���� �����ϴ�.");
					player.sendMessage("��a�ơ�r ũ�� ��ų�� ���, ä��, ���� �����ϴ�.");
					player.sendMessage("��a�ơ�r ���ش� ���� ��ų�� �ƴϹǷ� �����մϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5��ų�� ��� �ڽ��� ���� �о߸� ���� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ��ų�� ��� �ڽ��� ���� �о߸� ���� �� �ֽ��ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5Lv.30���� �� 2���� �о߸� �������� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r Lv.30(�ְ� ����)���� �� 2���� �о߸� �������� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ��, �� �о߸� �������ϸ� �� �оߴ� ��� �� ���� �����Դϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5����� ���� �оߴ� ���� ������ �̿��� ��ü�մϴ�.");
					player.sendMessage("��a�ơ�r ����� ���� �оߴ� ���� ������ �̿��� ��ü�մϴ�.");
					player.sendMessage("��a�ơ�r ���� ����� �������Ѵٸ�, ���̾Ƹ�带 �����ϴ� ���Դϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5ä�� �оߴ� ������ �ݼ��� �����մϴ�.");
					player.sendMessage("��a�ơ�r ä�� �оߴ� ������ �ݼ��� �����մϴ�.");
					player.sendMessage("��a�ơ�r 1�� ����: ��ö. ���� ö�� �ڸ��� ����� �ݼ�.");
					player.sendMessage("��a�ơ�r 2�� ����: ���̾Ƹ��, ��, ��þƮ.");
					
					EfeServer.getInstance().myboat.removeBoat(player);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							player.teleport(new Location(player.getWorld(), 2798, 65, 1929, -180.0F, 3.0F));
						}
					}, 5L);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5��� �оߴ� ������ �����մϴ�.");
					player.sendMessage("��a�ơ�r ��� �оߴ� ������ �����մϴ�.");
					player.sendMessage("��a�ơ�r 1�� ����: ����, ��.");
					player.sendMessage("��a�ơ�r 2�� ����: ����, Ȳ�� ���.");
					
					EfeServer.getInstance().myboat.removeBoat(player);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							player.teleport(new Location(player.getWorld(), 2801, 65, 1932, -40.7F, 11.3F));
						}
					}, 5L);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5��� �оߴ� ���� ��� �������� �����մϴ�.");
					player.sendMessage("��a�ơ�r ��� �оߴ� ���� ��� �������� �����մϴ�.");
					player.sendMessage("��a�ơ�r 1�� ����: ����ǰ, ���� ���.");
					player.sendMessage("��a�ơ�r 2�� ����: ���� �۹��� ����, ��þƮ ���.");
					
					EfeServer.getInstance().myboat.removeBoat(player);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							player.teleport(new Location(player.getWorld(), 2807, 65, 1927, -148.3F, 9.5F));
						}
					}, 5L);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5" + player.getName() + "���� � �о߰� ���� ��ó���?");
					player.sendMessage("��a�ơ�r " + player.getName() + "���� � �о߰� ���� ��ó���?");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "��d���õ�", "��5��ų�� �������, �� �оߴ� ���õ� ������ �����մϴ�.");
					player.sendMessage("��a�ơ�r ��ų�� �������, �� �оߴ� ���õ� ������ �����մϴ�.");

					EfeServer.getInstance().myboat.removeBoat(player);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							player.teleport(new Location(player.getWorld(), 2813.5, 65, 1929.5, -90.0F, 5.5F));
						}
					}, 5L);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5�ش� �о��� Ȱ���� �� ���� ���õ� EXP�� ���Դϴ�.");
					player.sendMessage("��a�ơ�r �ش� �о��� Ȱ���� �� ���� ���õ� EXP�� ���Դϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5EXP�� �� ���� ���� ������ ���õ� �������� �����մϴ�.");
					player.sendMessage("��a�ơ�r EXP�� �� ���� ���� ������ ���õ� �������� �����մϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5���õ��� ���� ���� �Ϲ� ����ġ�� �� ���� ����ϴ�.");
					player.sendMessage("��a�ơ�r ���õ� ������ ���� ���� �ش� �о� Ȱ���� �� �� ��� ���� ����ġ�� ���� �� Ů�ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5\\\"/���õ�\\\" ��ɾ�, ����صμ���!");
					player.sendMessage("��a�ơ�r \"/���õ�\" ��ɾ�, ����صμ���!");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5����, ä���� ������ �̵��غ�����.");
					player.sendMessage("��a�ơ�r ����, ä���� ������ �̵��غ�����.");
					
					TutorialListener.LEFT_START_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_MINE);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "��dä��", "��5���Ŀ�, ä���� \\\"�̳׶�\\\" �������� �����մϴ�.");
					player.sendMessage("��a�ơ�r ���Ŀ�, ä���� \"�̳׶�\" �������� �����մϴ�.");
					
					TutorialListener.ARRIVED_MINE_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5���� 5�� ĳ������.");
					player.sendMessage("��a�ơ�r ���� 5�� ĳ������.");
					
					ItemStack item = new ItemStack(Material.STONE_PICKAXE);
					player.getInventory().addItem(item);
					
					TutorialListener.BROKE_STONES = i;
					TutorialState.set(player, TutorialState.LETS_BREAK_STONES);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5���� ĳ�� �� 1�� �ڿ� ������˴ϴ�.");
					player.sendMessage("��a�ơ�r ���� ĳ�� �� 1�� �ڿ� ������˴ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5������ǰų� ���� ĺ�� ��, ���� ������ ���ɴϴ�.");
					player.sendMessage("��a�ơ�r ������ǰų� ���� ĺ�� ��, ���� ������ ���ɴϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 100, 20, "", "��5�����ϴ� ������ ����� ������ ������ �޽��ϴ�.");
					player.sendMessage("��a�ơ�r �����ϴ� ������ ����� ������ ������ �޽��ϴ�.");
					player.sendMessage("��a�ơ�r ���� ���, ���� ��̴� ��ź�� �߰��� �� ������");
					player.sendMessage("��a�ơ�r ö��̴� ��� ������ �߰��� �� �ֽ��ϴ�.");
					
					nextDelay = 140;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5����, �ܵ��� ������ �踦 Ÿ�� ��������.");
					player.sendMessage("��a�ơ�r ����, ��漶���� �踦 Ÿ�� ��������.");
					
					TutorialListener.ARRIVED_WEED_ISLAND = i;
					TutorialState.set(player, TutorialState.GO_TO_WEED);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "��d�ʿ� ä��", "��5��漶���� �Ĺ��� ������ ���� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ��漶���� �Ĺ��� ������ ���� �� �ֽ��ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5�ܵ�� ���� ĳ�� �о����� 2�� ��ƺ�����.");
					player.sendMessage("��a�ơ�r �ܵ� ĳ�� ������ 2�� ��ƺ�����.");
					
					TutorialListener.COLLECTED_SEEDS = i;
					TutorialState.set(player, TutorialState.LETS_COLLECT_SEEDS);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5�ܵ� �μ��ٺ��� ������ �����⵵ �մϴ�.");
					player.sendMessage("��a�ơ�r �ܵ� �μ��ٺ��� ������ �����⵵ �մϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5���� �̿��� ��ȹ�ϰ�, ������ Ű�� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ���� �̿��� ��ȹ�ϰ�, ������ Ű�� �� �ֽ��ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5����� ������ �̵��غ�����.");
					player.sendMessage("��a�ơ�r ����� ������ �̵��غ�����.");
					
					TutorialListener.LEFT_WEED_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_FARM);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "��d���", "��5���Ŀ�, ���� �ڽ��� ������, �� ���� ������ �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ���Ŀ�, ���� �ڽ��� ������, �� ���� ������ �� �ֽ��ϴ�.");
					
					TutorialListener.ARRIVED_FARM_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5��縦 ���ؼ� 5��5 ũ���� �� ������ �ʿ��մϴ�.");
					player.sendMessage("��a�ơ�r ��縦 ���ؼ� 5��5 ũ���� �� ������ �ʿ��մϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5�ܵ�� �߾ӿ� ǥ������ �Ȱ� \\'��\\'�̶�� ������.");
					player.sendMessage("��a�ơ�r �ܵ�� �߾ӿ� ǥ������ �Ȱ� '��' (Ȥ�� 'farm') �̶�� ������.");
					
					ItemStack item = new ItemStack(Material.SIGN);
					player.getInventory().addItem(item);
					
					TutorialListener.PLACED_SIGN = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5�۹��� �ɱ� ���ؼ� ������ �ʿ��մϴ�.");
					player.sendMessage("��a�ơ�r �۹��� �ɱ� ���ؼ� ������ �ʿ��մϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5�� �絿�̷� ǥ������ ��Ŭ���ϼ���.");
					player.sendMessage("��a�ơ�r �� �絿�̷� ǥ������ ��Ŭ���ϼ���.");
					
					ItemStack item = new ItemStack(Material.WATER_BUCKET);
					player.getInventory().addItem(item);
					
					TutorialListener.WATERED_FARM = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5���̷� ǥ������ ��Ŭ���� �۹��� ���� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ���̷� ǥ������ ��Ŭ���� �۹��� ���� �� �ֽ��ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5��ųƮ������ ��� �۹��� ���� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ��ųƮ������ ��� �۹��� ���� �� �ֽ��ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5��� ��ų�� ����, ���̷� ǥ������ ��Ŭ���Ͽ� �翡 �ɾ����.");
					player.sendMessage("��a�ơ�r ��� ��ų�� ����, ���̷� ǥ������ ��Ŭ���Ͽ� �翡 �ɾ����.");
					
					player.getInventory().addItem(new ItemStack(Material.IRON_HOE));
					player.getInventory().addItem(new ItemStack(Material.CARROT_ITEM, 8));
					
					TutorialListener.PLANTED_SEEDS = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5�۹��� �� �ڶ�� ��� ���� ��Ȯ�ؾ��մϴ�.");
					player.sendMessage("��a�ơ�r �۹��� �� �ڶ�� ��� ���� ��Ȯ�ؾ��մϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5���̷� ǥ������ Ŭ���� �۹��� ��Ȯ�غ�����.");
					player.sendMessage("��a�ơ�r ���̷� ǥ������ Ŭ���� �۹��� ��Ȯ�غ�����.");
					
					TutorialListener.HARVESTED_CROPS = i;
					TutorialState.set(player, TutorialState.LETS_HARVEST_CROPS);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5���� ������ ���!");
					player.sendMessage("��a�ơ�r ���� ������ ���!");
					
					TutorialListener.LEFT_FARM_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_FISH);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "��d����", "��5���ô� ��𿡼��� ���� ������ �����մϴ�.");
					player.sendMessage("��a�ơ�r ���ô� ��𿡼��� ���� ������ �����մϴ�.");
					
					TutorialListener.ARRIVED_FISH_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5���� ����⿡�� ���� ���̰� �����ϴ�.");
					player.sendMessage("��a�ơ�r ���� ����⿡�� ���� ���̰� �����ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5����⸦ �� ���� ���ƺ�����.");
					player.sendMessage("��a�ơ�r ����⸦ �� ���� ���ƺ�����.");
					
					ItemStack item = new ItemStack(Material.FISHING_ROD);
					player.getInventory().addItem(item);
					
					TutorialListener.FISHED = i;
					TutorialState.set(player, TutorialState.LETS_FISH);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5���� ������ ���� ������ �� �� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ���� ������ ���� ������ �� �� �ֽ��ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5���˴븦 ��� ��Ŭ���ϸ� �̳��� �� ���� �ֽ��ϴ�.");
					player.sendMessage("��a�ơ�r ���˴븦 ��� ��Ŭ���ϸ� �̳��� �� ���� �ֽ��ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "��d���", "��5������ ������ �̵��ϼ���.");
					player.sendMessage("��a�ơ�r ������ ������ �̵��ϼ���.");
					
					TutorialListener.LEFT_FISH_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_HUNT);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5��� ���� �����ϰ� ���Ͱ� ������ ���Դϴ�.");
					player.sendMessage("��a�ơ�r ��� ���� �����ϰ� ���Ͱ� ������ ���Դϴ�.");
					
					TutorialListener.ARRIVED_HUNT_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "��5����� IQ �� �ڸ��� �ƴ� �̻� �ʹ����� �����ϴ�.");
					player.sendMessage("��a�ơ�r ����� IQ �� �ڸ��� �ƴ� �̻� �ʹ����� �����ϴ�.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "��5�� ���� ���� �� ������ ����غ�����.");
					player.sendMessage("��a�ơ�r �� ���� ���� �� ������ ����غ�����.");
					
					ItemStack item = new ItemStack(Material.IRON_SWORD);
					player.getInventory().addItem(item);
					
					for (int j = 0; j < 3; j ++)
						MobSpawner.SpawnMythicMob("Tutorial_Zombie", plugin.playerListener.zombieLoc);
					
					TutorialListener.BE_KILLED_BY_ZOMBIE = i;
					TutorialState.set(player, TutorialState.LETS_KILL_ZOMBIES);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 0, 20, 20, "", "��5�̷�, IQ �� �ڸ��ñ���.");
					player.sendMessage("��a�ơ�r �̷�, IQ �� �ڸ��ñ���.");
					return;
				}
				
				
				proceedTutorial(player, num + 1, nextDelay);
			}
		}, delay);
	}
	
	public void giveExpToLevelUp(Player player) {
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (player.getLevel() >= 1)
					return;
				
				CustomExpAPI.giveExp(player, 40);
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
				
				giveExpToLevelUp(player);
			}
		}, 20L);
	}
}