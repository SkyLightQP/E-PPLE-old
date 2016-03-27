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
					TitleAPI.sendTitle(player, 20, 60, 20, "§d§lTutorial", "§5튜토리얼을 시작합니다!");
					player.sendMessage("§a▒§r 튜토리얼을 시작합니다!");
					
					PlayerData data = PlayerData.get(player);
					
					if (data.getOptionBoat()) {
						
						if (data.getOptionMenu())
							player.getInventory().setItem(7, EfeServer.getInstance().myboat.getBoatItem(player));
						else
							player.getInventory().setItem(8, EfeServer.getInstance().myboat.getBoatItem(player));
					}
					
					if (data.getOptionMenu()) {
						player.getInventory().setItem(8, EfeUtils.item.createDisplayItem("§e/메뉴", new ItemStack(Material.NETHER_STAR), 
								new String[]{"클릭하면 메인 메뉴를 엽니다."}));
					}
					
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "§d/스킬트리", "§5채집에 도움을 주는 스킬을 배워보세요.");
					player.sendMessage("§a▒§r 채집에 도움을 주는 스킬을 배워보세요.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5레벨업으로 얻는 SP로 스킬을 배울 수 있습니다.");
					player.sendMessage("§a▒§r 레벨업으로 얻는 SP로 스킬을 배울 수 있습니다.");
					
					giveExpToLevelUp(player);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5항해 스킬을 배워보세요.");
					player.sendMessage("§a▒§r 항해 스킬을 배워보세요.");
					
					TutorialListener.USED_SP_BOAT = i;
					TutorialState.set(player, TutorialState.LEVEL_UP);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "§d항해", "§5보트를 이용해 항해해보세요.");
					player.sendMessage("§a▒§r 보트를 이용해 항해해보세요.");
					
					TutorialListener.ENTERED_BOAT = i;
					TutorialState.set(player, TutorialState.LETS_ENTER_BOAT);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5나침반으로 네비게이션을 이용할 수 있습니다.");
					player.sendMessage("§a▒§r 나침반으로 네비게이션을 이용할 수 있습니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5나침반을 들고 좌클릭해보세요.");
					player.sendMessage("§a▒§r 나침반을 들고 좌클릭해보세요.");
					
					TutorialListener.CLICKED_NAVIGATION = i;
					TutorialState.set(player, TutorialState.LETS_CLICK_NAVIGATION);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5\\\"분업의 섬\\\"을 클릭하세요.");
					player.sendMessage("§a▒§r \"분업의 섬\"을 클릭하세요.");
					
					TutorialListener.SELECTED_SKILL = i;
					TutorialState.set(player, TutorialState.LETS_SELECT_SKILL);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5나침반의 방향대로 섬을 찾아가보세요.");
					player.sendMessage("§a▒§r 나침반의 방향대로 섬을 찾아가보세요.");
					
					TutorialListener.LEFT_START_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_SKILL);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "§d분업", "§5생산 활동은 사냥, 채광, 농사로 나뉘어져 있습니다.");
					player.sendMessage("§a▒§r 생산 활동은 사냥, 채광, 농사로 나뉘어져 있습니다.");
					
					TutorialListener.ARRIVED_SKILL_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5크게 스킬은 사냥, 채광, 농사로 나뉩니다.");
					player.sendMessage("§a▒§r 크게 스킬은 사냥, 채광, 농사로 나뉩니다.");
					player.sendMessage("§a▒§r 항해는 생산 스킬이 아니므로 제외합니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5스킬을 배워 자신의 생산 분야를 넓힐 수 있습니다.");
					player.sendMessage("§a▒§r 스킬을 배워 자신의 생산 분야를 넓힐 수 있습니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5Lv.30까지 총 2개의 분야를 마스터할 수 있습니다.");
					player.sendMessage("§a▒§r Lv.30(최고 레벨)까지 총 2개의 분야를 마스터할 수 있습니다.");
					player.sendMessage("§a▒§r 즉, 두 분야를 마스터하면 한 분야는 배울 수 없는 구조입니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5배우지 못한 분야는 유저 마켓을 이용해 대체합니다.");
					player.sendMessage("§a▒§r 배우지 못한 분야는 유저 마켓을 이용해 대체합니다.");
					player.sendMessage("§a▒§r 농사와 사냥을 마스터한다면, 다이아몬드를 구매하는 식입니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5채광 분야는 강력한 금속을 생산합니다.");
					player.sendMessage("§a▒§r 채광 분야는 강력한 금속을 생산합니다.");
					player.sendMessage("§a▒§r 1차 생산: 강철. 기존 철의 자리를 대신할 금속.");
					player.sendMessage("§a▒§r 2차 생산: 다이아몬드, 금, 인첸트.");
					
					EfeServer.getInstance().myboat.removeBoat(player);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							player.teleport(new Location(player.getWorld(), 2798, 65, 1929, -180.0F, 3.0F));
						}
					}, 5L);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5농사 분야는 음식을 생산합니다.");
					player.sendMessage("§a▒§r 농사 분야는 음식을 생산합니다.");
					player.sendMessage("§a▒§r 1차 생산: 감자, 빵.");
					player.sendMessage("§a▒§r 2차 생산: 포션, 황금 사과.");
					
					EfeServer.getInstance().myboat.removeBoat(player);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							player.teleport(new Location(player.getWorld(), 2801, 65, 1932, -40.7F, 11.3F));
						}
					}, 5L);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5사냥 분야는 재료와 희귀 아이템을 생산합니다.");
					player.sendMessage("§a▒§r 사냥 분야는 재료와 희귀 아이템을 생산합니다.");
					player.sendMessage("§a▒§r 1차 생산: 전리품, 포션 재료.");
					player.sendMessage("§a▒§r 2차 생산: 상위 작물의 씨앗, 인첸트 재료.");
					
					EfeServer.getInstance().myboat.removeBoat(player);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							player.teleport(new Location(player.getWorld(), 2807, 65, 1927, -148.3F, 9.5F));
						}
					}, 5L);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5" + player.getName() + "님은 어떤 분야가 맘에 드시나요?");
					player.sendMessage("§a▒§r " + player.getName() + "님은 어떤 분야가 맘에 드시나요?");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "§d숙련도", "§5스킬과 관계없이, 각 분야는 숙련도 개념이 존재합니다.");
					player.sendMessage("§a▒§r 스킬과 관계없이, 각 분야는 숙련도 개념이 존재합니다.");

					EfeServer.getInstance().myboat.removeBoat(player);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							player.teleport(new Location(player.getWorld(), 2813.5, 65, 1929.5, -90.0F, 5.5F));
						}
					}, 5L);
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5해당 분야의 활동을 할 수록 숙련도 EXP가 쌓입니다.");
					player.sendMessage("§a▒§r 해당 분야의 활동을 할 수록 숙련도 EXP가 쌓입니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5EXP가 다 차면 돈을 지불해 숙련도 레벨업이 가능합니다.");
					player.sendMessage("§a▒§r EXP가 다 차면 돈을 지불해 숙련도 레벨업이 가능합니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5숙련도가 높을 수록 일반 경험치를 더 많이 얻습니다.");
					player.sendMessage("§a▒§r 숙련도 레벨이 높을 수록 해당 분야 활동을 할 때 얻는 유저 경험치의 양이 더 큽니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5\\\"/숙련도\\\" 명령어, 기억해두세요!");
					player.sendMessage("§a▒§r \"/숙련도\" 명령어, 기억해두세요!");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5이제, 채광의 섬으로 이동해보세요.");
					player.sendMessage("§a▒§r 이제, 채광의 섬으로 이동해보세요.");
					
					TutorialListener.LEFT_START_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_MINE);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "§d채광", "§5이후에, 채광은 \\\"미네라스\\\" 섬에서만 가능합니다.");
					player.sendMessage("§a▒§r 이후에, 채광은 \"미네라스\" 섬에서만 가능합니다.");
					
					TutorialListener.ARRIVED_MINE_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5돌을 5번 캐보세요.");
					player.sendMessage("§a▒§r 돌을 5번 캐보세요.");
					
					ItemStack item = new ItemStack(Material.STONE_PICKAXE);
					player.getInventory().addItem(item);
					
					TutorialListener.BROKE_STONES = i;
					TutorialState.set(player, TutorialState.LETS_BREAK_STONES);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5돌을 캐면 약 1분 뒤에 재생성됩니다.");
					player.sendMessage("§a▒§r 돌을 캐면 약 1분 뒤에 재생성됩니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5재생성되거나 돌을 캤을 때, 가끔 광석이 나옵니다.");
					player.sendMessage("§a▒§r 재생성되거나 돌을 캤을 때, 가끔 광석이 나옵니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 100, 20, "", "§5등장하는 광석은 곡괭이의 재질에 영향을 받습니다.");
					player.sendMessage("§a▒§r 등장하는 광석은 곡괭이의 재질에 영향을 받습니다.");
					player.sendMessage("§a▒§r 예를 들어, 나무 곡괭이는 석탄만 발견할 수 있지만");
					player.sendMessage("§a▒§r 철곡괭이는 모든 광석을 발견할 수 있습니다.");
					
					nextDelay = 140;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5이제, 잔디의 섬으로 배를 타고 가보세요.");
					player.sendMessage("§a▒§r 이제, 농경섬으로 배를 타고 가보세요.");
					
					TutorialListener.ARRIVED_WEED_ISLAND = i;
					TutorialState.set(player, TutorialState.GO_TO_WEED);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "§d초원 채집", "§5농경섬에선 식물과 씨앗을 얻을 수 있습니다.");
					player.sendMessage("§a▒§r 농경섬에선 식물과 씨앗을 얻을 수 있습니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5잔디와 꽃을 캐서 밀씨앗을 2개 모아보세요.");
					player.sendMessage("§a▒§r 잔디를 캐서 씨앗을 2개 모아보세요.");
					
					TutorialListener.COLLECTED_SEEDS = i;
					TutorialState.set(player, TutorialState.LETS_COLLECT_SEEDS);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5잔디를 부수다보면 동물이 나오기도 합니다.");
					player.sendMessage("§a▒§r 잔디를 부수다보면 동물이 나오기도 합니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5덫을 이용해 포획하고, 집에서 키울 수 있습니다.");
					player.sendMessage("§a▒§r 덫을 이용해 포획하고, 집에서 키울 수 있습니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5농사의 섬으로 이동해보세요.");
					player.sendMessage("§a▒§r 농사의 섬으로 이동해보세요.");
					
					TutorialListener.LEFT_WEED_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_FARM);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "§d농사", "§5이후에, 밭은 자신의 섬에서, 한 개만 개설할 수 있습니다.");
					player.sendMessage("§a▒§r 이후에, 밭은 자신의 섬에서, 한 개만 개설할 수 있습니다.");
					
					TutorialListener.ARRIVED_FARM_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5농사를 위해선 5×5 크기의 흙 평지가 필요합니다.");
					player.sendMessage("§a▒§r 농사를 위해선 5×5 크기의 흙 평지가 필요합니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5잔디밭 중앙에 표지판을 꽂고 \\'밭\\'이라고 쓰세요.");
					player.sendMessage("§a▒§r 잔디밭 중앙에 표지판을 꽂고 '밭' (혹은 'farm') 이라고 쓰세요.");
					
					ItemStack item = new ItemStack(Material.SIGN);
					player.getInventory().addItem(item);
					
					TutorialListener.PLACED_SIGN = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5작물을 심기 위해선 수분이 필요합니다.");
					player.sendMessage("§a▒§r 작물을 심기 위해선 수분이 필요합니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5물 양동이로 표지판을 좌클릭하세요.");
					player.sendMessage("§a▒§r 물 양동이로 표지판을 좌클릭하세요.");
					
					ItemStack item = new ItemStack(Material.WATER_BUCKET);
					player.getInventory().addItem(item);
					
					TutorialListener.WATERED_FARM = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5괭이로 표지판을 우클릭해 작물을 심을 수 있습니다.");
					player.sendMessage("§a▒§r 괭이로 표지판을 우클릭해 작물을 심을 수 있습니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5스킬트리에서 배운 작물만 심을 수 있습니다.");
					player.sendMessage("§a▒§r 스킬트리에서 배운 작물만 심을 수 있습니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5당근 스킬을 배우고, 괭이로 표지판을 우클릭하여 밭에 심어보세요.");
					player.sendMessage("§a▒§r 당근 스킬을 배우고, 괭이로 표지판을 우클릭하여 밭에 심어보세요.");
					
					player.getInventory().addItem(new ItemStack(Material.IRON_HOE));
					player.getInventory().addItem(new ItemStack(Material.CARROT_ITEM, 8));
					
					TutorialListener.PLANTED_SEEDS = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5작물이 다 자라면 썩기 전에 수확해야합니다.");
					player.sendMessage("§a▒§r 작물이 다 자라면 썩기 전에 수확해야합니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5괭이로 표지판을 클릭해 작물을 수확해보세요.");
					player.sendMessage("§a▒§r 괭이로 표지판을 클릭해 작물을 수확해보세요.");
					
					TutorialListener.HARVESTED_CROPS = i;
					TutorialState.set(player, TutorialState.LETS_HARVEST_CROPS);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5다음 섬으로 출발!");
					player.sendMessage("§a▒§r 다음 섬으로 출발!");
					
					TutorialListener.LEFT_FARM_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_FISH);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "§d낚시", "§5낚시는 어디에서나 물만 있으면 가능합니다.");
					player.sendMessage("§a▒§r 낚시는 어디에서나 물만 있으면 가능합니다.");
					
					TutorialListener.ARRIVED_FISH_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5낚은 물고기에는 종과 길이가 적힙니다.");
					player.sendMessage("§a▒§r 낚은 물고기에는 종과 길이가 적힙니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5물고기를 한 마리 낚아보세요.");
					player.sendMessage("§a▒§r 물고기를 한 마리 낚아보세요.");
					
					ItemStack item = new ItemStack(Material.FISHING_ROD);
					player.getInventory().addItem(item);
					
					TutorialListener.FISHED = i;
					TutorialState.set(player, TutorialState.LETS_FISH);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5낚시 섬에선 낚시 순위를 볼 수 있습니다.");
					player.sendMessage("§a▒§r 낚시 섬에선 낚시 순위를 볼 수 있습니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5낚싯대를 들고 좌클릭하면 미끼를 달 수도 있습니다.");
					player.sendMessage("§a▒§r 낚싯대를 들고 좌클릭하면 미끼를 달 수도 있습니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "§d사냥", "§5마지막 섬으로 이동하세요.");
					player.sendMessage("§a▒§r 마지막 섬으로 이동하세요.");
					
					TutorialListener.LEFT_FISH_REGION = i;
					TutorialState.set(player, TutorialState.GO_TO_HUNT);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5사냥 섬은 유일하게 몬스터가 나오는 곳입니다.");
					player.sendMessage("§a▒§r 사냥 섬은 유일하게 몬스터가 나오는 곳입니다.");
					
					TutorialListener.ARRIVED_HUNT_ISLAND = i;
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 60, 20, "", "§5사냥은 IQ 두 자리가 아닌 이상 너무나도 쉽습니다.");
					player.sendMessage("§a▒§r 사냥은 IQ 두 자리가 아닌 이상 너무나도 쉽습니다.");
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 20, 99999, 0, "", "§5눈 앞의 좀비 세 마리를 사냥해보세요.");
					player.sendMessage("§a▒§r 눈 앞의 좀비 세 마리를 사냥해보세요.");
					
					ItemStack item = new ItemStack(Material.IRON_SWORD);
					player.getInventory().addItem(item);
					
					for (int j = 0; j < 3; j ++)
						MobSpawner.SpawnMythicMob("Tutorial_Zombie", plugin.playerListener.zombieLoc);
					
					TutorialListener.BE_KILLED_BY_ZOMBIE = i;
					TutorialState.set(player, TutorialState.LETS_KILL_ZOMBIES);
					return;
				} else if (num == i ++) {
					TitleAPI.sendTitle(player, 0, 20, 20, "", "§5이런, IQ 두 자리시군요.");
					player.sendMessage("§a▒§r 이런, IQ 두 자리시군요.");
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