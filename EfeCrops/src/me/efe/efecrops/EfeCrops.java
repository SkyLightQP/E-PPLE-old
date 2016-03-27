package me.efe.efecrops;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

public class EfeCrops extends JavaPlugin implements Listener{
	public Logger log = Logger.getLogger("Minecraft");
	public Time time = new Time();
	public ArrayList<Material> dirts = new ArrayList<Material>();
	public GUI gui;
	public FarmCreater farmer;
	
	public void onDisable() {
		log.info("[EfeCrops v2.0] 오프! Made by Efe");
	}
	
	public void onEnable() {
		log.info("[EfeCrops v2.0] 온! Made by Efe");
		gui = new GUI(this);
		farmer = new FarmCreater(this);
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(gui, this);
		getServer().getPluginManager().registerEvents(farmer, this);
		dirts.add(Material.DIRT);
		dirts.add(Material.GRASS);
		dirts.add(Material.MYCEL);
		removeRecipes();
	}
	
	public void removeRecipes(){
		Iterator<Recipe> it = getServer().recipeIterator();
		while (it.hasNext()){
			Recipe recipe = it.next();
			if (recipe.getResult().getType().equals(Material.PUMPKIN_SEEDS) || recipe.getResult().getType().equals(Material.MELON_SEEDS)){
				it.remove();
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onSignChange(SignChangeEvent e){
		if (e.getLine(0).contains("[MyFarm]") && !e.getPlayer().isOp()){
			e.setLine(0, "Fail");
			e.getPlayer().sendMessage("§c■§r 관리자만 밭을 생성할 수 있습니다.");
		}else if (e.getLine(0).contains("[MyFarm]") && e.getPlayer().isOp()){
			e.setLine(0, ChatColor.DARK_GREEN+"[MyFarm]");
			e.setLine(2, "0");
			e.setLine(3, "");
			getServer().getPlayer(e.getLines()[1]).sendMessage("§a■§r 당신의 밭이 생성되었습니다!");
		}
		if (e.getLine(0).contains("[Windmill]") && !e.getPlayer().isOp()){
			e.setLine(0, "Fail");
			e.getPlayer().sendMessage("§c■§r 관리자만 풍차를 생성할 수 있습니다.");
		}else if (e.getLine(0).contains("[Windmill]") && e.getPlayer().isOp()){
			e.setLine(0, ChatColor.DARK_BLUE+"[Windmill]");
			e.setLine(3, farmTimer(30, ""));
		}
	}
	
	public State isTimeToHarvest(String value){
		String t = new String(value);
		t = t.replace("[밀]", "");
		t = t.replace("[감자]", "");
		t = t.replace("[당근]", "");
		t = t.replace("[호박]", "");
		t = t.replace("[수박]", "");
		t = t.replace(":", "");
		t = t.replace("/", "");
		if (t.isEmpty()) return State.EMPTY;
		int Hour = Integer.parseInt(new String(t.charAt(0)+"")+new String(t.charAt(1)+""));
		int Minute = Integer.parseInt(new String(t.charAt(2)+"")+new String(t.charAt(3)+""));
		int Month = Integer.parseInt(new String(t.charAt(4)+"")+new String(t.charAt(5)+""));
		int Date = Integer.parseInt(new String(t.charAt(6)+"")+new String(t.charAt(7)+""));
		if (time.getMonth() == Month && time.getDate() == Date){
			if (Hour == time.getHour() && Minute <= time.getMinute()) return State.HARVESTABLE;
			if (Hour < time.getHour()) return State.HARVESTABLE;
			return State.UNHARVESTABLE;
		}else {
			return State.ROTTED;
		}
	}
	
	public String farmTimer(int add, String crop){
		int hour = time.getHour();
		int minute = time.getMinute();
		if (time.getMinute() + add >= 60){
			hour++;
			minute = (time.getMinute() + add) - 60;
		}else {
			minute += add;
		}
		String minuteChecker = minute+"";
		if (minuteChecker.length() == 1) minuteChecker = "0"+minute;
		int Month = time.getMonth();
		int Date = time.getDate();
		String MonthChecker = Month+"";
		if (MonthChecker.length() == 1) MonthChecker = "0"+Month;
		String DateChecker = Date+"";
		if (DateChecker.length() == 1) DateChecker = "0"+Date;
		String what = "["+crop+"]";
		if (crop == "") what = "";
		return what+hour+":"+minuteChecker+"/"+MonthChecker+"/"+DateChecker;
	}
	
	public boolean isFarmItem(ItemStack item){
		if (item.getType() == Material.WOOD_HOE || item.getType() == Material.STONE_HOE || item.getType() == Material.IRON_HOE || item.getType() == Material.GOLD_HOE || item.getType() == Material.DIAMOND_HOE){
			return true;
		}else {
			return false;	
		}
	}
	
	public boolean equalsPlayer(String name, Player p){
		try {
			if (getServer().getPlayerExact(name) == p) return true;
			return false;
		}catch (Exception e){
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void updateInventory(Player p){
		p.updateInventory();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		if (isFarmItem(e.getPlayer().getItemInHand())){
			if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && dirts.contains(e.getClickedBlock().getType())){
				e.setCancelled(true);
				e.getPlayer().sendMessage("§c■§r 땅을 갈 수 없습니다! 가이드북을 참조하세요.");
			}else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Sign){
				Sign sign = (Sign) e.getClickedBlock().getState();
				if (!sign.getLine(0).contains("[MyFarm]")) return;
				if (!equalsPlayer(sign.getLines()[1], e.getPlayer())){
					e.getPlayer().sendMessage("§c■§r 당신의 밭이 아닙니다!");
					return;
				}
				if (sign.getLine(3).isEmpty()) gui.openGUI(e.getPlayer(), sign);
				else e.getPlayer().sendMessage("§c■§r 이미 작물이 심어져있습니다.");
			}else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Sign){
				Sign sign = (Sign) e.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Windmill]")){
					if (isTimeToHarvest(sign.getLine(3)) == State.HARVESTABLE){
						e.getPlayer().sendMessage("§a■§r 풍차가 모은 에너지 5 level을 얻었습니다.");
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
						e.getPlayer().giveExpLevels(5);
						sign.setLine(3, farmTimer(30, ""));
						sign.update();
					}else if (isTimeToHarvest(sign.getLine(3)) == State.ROTTED){
						e.getPlayer().sendMessage("§a■§r 풍차가 밤동안 모은 에너지 7 level을 얻었습니다.");
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
						e.getPlayer().giveExpLevels(7);
						sign.setLine(3, farmTimer(30, ""));
						sign.update();
					}else e.getPlayer().sendMessage("§c■§r 에너지를 생성중입니다.");
					return;
				}
				if (!sign.getLine(0).contains("[MyFarm]")) return;
				if (!equalsPlayer(sign.getLines()[1], e.getPlayer())){
					e.getPlayer().sendMessage("§c■§r 당신의 밭이 아닙니다!");
					return;
				}
				State state = isTimeToHarvest(sign.getLines()[3]);
				if (state.equals(State.HARVESTABLE)){
					Random random = new Random();
					if (sign.getLine(3).contains("밀")){
						int wheat = random.nextInt(5)+7;
						int seeds = random.nextInt(7)+5;
						e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.WHEAT, wheat));
						e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.SEEDS, seeds));
						e.getPlayer().sendMessage("§a■§r 밀을 수확했습니다.");
						e.getPlayer().sendMessage("§a■§r 수확 : 씨x"+seeds+", 밀x"+wheat);
						sign.setLine(3, "");
						sign.update();
					}else if (sign.getLine(3).contains("감자")){
						int potato = random.nextInt(5)+7;
						e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.POTATO_ITEM, potato));
						e.getPlayer().sendMessage("§a■§r 감자를 수확했습니다.");
						e.getPlayer().sendMessage("§a■§r 수확 : 감자x"+potato);
						sign.setLine(3, "");
						sign.update();
					}else if (sign.getLine(3).contains("당근")){
						int carrot = random.nextInt(5)+7;
						e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.CARROT_ITEM, carrot));
						e.getPlayer().sendMessage("§a■§r 당근을 수확했습니다.");
						e.getPlayer().sendMessage("§a■§r 수확 : 당근x"+carrot);
						sign.setLine(3, "");
						sign.update();
					}else if (sign.getLine(3).contains("호박")){
						int pumpkin = random.nextInt(2)+3;
						int pumpkin_seeds = random.nextInt(3)+3;
						e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.PUMPKIN, pumpkin));
						e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.PUMPKIN_SEEDS, pumpkin_seeds));
						e.getPlayer().sendMessage("§a■§r 호박을 수확했습니다.");
						e.getPlayer().sendMessage("§a■§r 수확 : 호박x"+pumpkin+", 호박씨x"+pumpkin_seeds);
						sign.setLine(3, "");
						sign.update();
					}else if (sign.getLine(3).contains("수박")){
						int melon = random.nextInt(2)+3;
						int melon_seeds = random.nextInt(3)+3;
						e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.MELON_BLOCK, melon));
						e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.MELON, melon_seeds));
						e.getPlayer().sendMessage("§a■§r 수박을 수확했습니다.");
						e.getPlayer().sendMessage("§a■§r 수확 : 수박x"+melon+", 수박씨x"+melon_seeds);
						sign.setLine(3, "");
						sign.update();
					}
				}else if (state.equals(State.ROTTED)){
					e.getPlayer().sendMessage("§e■§r 작물이 썩어서 초기화 되었습니다.");
					sign.setLine(3, "");
					sign.update();
				}else if (state.equals(State.UNHARVESTABLE)){
					e.getPlayer().sendMessage("§c■§r 아직 수확할 수 없습니다.");
				}else {
					e.getPlayer().sendMessage("§e■§r 작물이 심어져있지 않습니다.");
					e.getPlayer().sendMessage("§e■§r §7괭이로 클릭 : 심기/물주기, 괭이로 우클릭 : 수확하기");
				}
			}
		}else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Sign && 
				e.getPlayer().getItemInHand().getType().equals(Material.WATER_BUCKET)){
			Sign sign = (Sign) e.getClickedBlock().getState();
			if (!sign.getLine(0).contains("[MyFarm]")) return;
			e.getPlayer().setItemInHand(new ItemStack(Material.BUCKET));
			gui.water(sign, 50, true);
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.SWIM, 1.0F, 1.0F);
			e.getPlayer().sendMessage("§a■§r 밭에 물을 주었습니다. §8[수분 +50]");
			return;
		}
		
	}
}
