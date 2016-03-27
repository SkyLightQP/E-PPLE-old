package me.efe.efeserver.reform.farming;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.skilltree.UserData;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Farm {
	private static Random random = new Random();
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd_HHmm");
	private static FarmBuilder farmBuilder = EfeServer.getInstance().farmBuilder;
	private final OfflinePlayer owner;
	private final Sign sign;
	private final Location centerLoc;
	private String farmDate;
	private Date harvestDate;
	private Crop crop;
	private Disease disease;
	
	public Farm(Sign sign) {
		ProtectedRegion region = EfeServer.getInstance().efeIsland.getIsleRegion(sign.getLocation());
		this.owner = EfeServer.getInstance().efeIsland.getIsleOwner(region);
		
		PlayerData data = PlayerData.get(owner);
		
		this.sign = sign;
		this.centerLoc = farmBuilder.getCenter(sign);
		this.farmDate = data.getFarmDate(farmBuilder.getCenter(sign));
		
		String[] split = farmDate.split("\\|");
		Date date = null;
		
		try {
			date = dateFormat.parse(split[1]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.harvestDate = date;
		this.crop = Crop.valueOf(split[0]);
		this.disease = Disease.valueOf(split[2].split(",")[Disease.values().length]);
	}
	
	public OfflinePlayer getOwner() {
		return this.owner;
	}
	
	public Sign getSign() {
		return this.sign;
	}
	
	public Location getCenterLocation() {
		return this.centerLoc;
	}
	
	public int getWater() {
		String line = this.sign.getLine(2);
		
		return Integer.parseInt(line.substring(4, line.length() - 1));
	}
	
	public void setWater(int point) {
		this.sign.setLine(2, "수분: " + point + "%");
		this.sign.update();
	}
	
	public void updateWater(int point) {
		int water = getWater() + point;
		
		if (water > 100)
			water = 100;
		
		setWater(water);
	}
	
	public Crop getCrop() {
		return this.crop;
	}

	public boolean hasDisease() {
		return this.disease != null;
	}
	
	public Disease getDisease() {
		return this.disease;
	}
	
	public void plantSeeds(Crop crop, int rottenFleshAmount, int boneMealAmount) {
		PlayerData data = PlayerData.get(owner);
		int level = new UserData(owner).getLevel(crop.getSkill());
		Calendar cal = Calendar.getInstance();
		
		updateWater(-crop.getWater(level));
		
		cal.add(Calendar.MINUTE, crop.getTime(level));
		cal.add(Calendar.SECOND, boneMealAmount * 10);
		
		this.harvestDate = cal.getTime();
		
		
		String fertData = rottenFleshAmount + "," + boneMealAmount + ",";
		
		if (rottenFleshAmount >= random.nextInt(13) + 18) { //TODO 18~30
			fertData += "ROTTEN_FLESH";
		} else if (boneMealAmount >= random.nextInt(13) + 18) { // 18~30
			fertData += "BONE_MEAL";
		} else {
			fertData = fertData.substring(0, fertData.length() - 1);
		}
		
		
		this.farmDate = crop.toString() + "|" + dateFormat.format(harvestDate) + "|" + fertData;
		data.setFarmDate(centerLoc, farmDate);
		
		this.crop = crop;
	}
	
	public boolean isEmpty() {
		return this.farmDate.isEmpty();
	}
	
	public boolean isHarvestable() {
		return this.harvestDate.after(new Date());
	}
	
	public String getRestTime() {
		if (isHarvestable())
			return "0초";
		
		Date now = new Date();
		
		long diff = -(now.getTime() - harvestDate.getTime());
		
		int min = (int) (diff / (60 * 1000) % 60);
		int sec = (int) (diff / 1000 % 60);
		
		return min != 0 ? min+"분" : sec+"초";
	}
	
	public void harvest() {
		if (!hasDisease()) {
			List<ItemStack> list = new ArrayList<ItemStack>();
			
			int yield = crop.getYield();
			yield *= Double.parseDouble(farmDate.split("|")[2].split(",")[1]); //TODO
			
			list.add(new ItemStack(crop.getItemType(), yield));
			
			if (crop == Crop.WHEAT) {
				list.add(new ItemStack(Material.SEEDS, random.nextInt(5) + 6));
			}
			
			if (crop.getSpecialItem() != null && Math.random() <= 0.8D) {
				list.add(crop.getSpecialItem());
			}
			
			
			String message = "§a▒§r 수확물: ";
			
			Iterator<ItemStack> it = list.iterator();
			while (it.hasNext()) {
				ItemStack itemStack = it.next();
				String name;
				
				if (itemStack.getType() == Material.SEEDS) {
					name = "씨앗";
				} else {
					name = crop.getName();
				}
				
				name += "×" + itemStack.getAmount();
				
				message += name;
				
				if (it.hasNext())
					message += ", ";
				
				if (owner.getPlayer().getInventory().firstEmpty() == -1)
					owner.getPlayer().getWorld().dropItemNaturally(owner.getPlayer().getLocation(), itemStack);
				else
					owner.getPlayer().getInventory().addItem(itemStack);
			}
			
			owner.getPlayer().sendMessage(message);
		}

		clearup();
	}
	
	public void clearup() {
		this.farmDate = "";
		this.harvestDate = null;
		this.crop = null;
		this.disease = null;
		
		PlayerData.get(owner).setFarmDate(centerLoc, farmDate);
	}
	
	public static enum Disease {
		ROTTEN_FLESH("§a▒§r 작물에서 시체 썩은 냄새가 진동을 하는 듯 합니다.."),
		BONE_MEAL("§a▒§r 작물이 너무 빨리 자란 것이 원인인 것 같습니다..");
		
		private final String message;
		
		Disease(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return this.message;
		}
	}
}
