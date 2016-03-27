package me.efe.efeserver.reform.farming;

import me.efe.efeserver.EfeServer;
import me.efe.efeserver.PlayerData;
import me.efe.skilltree.UserData;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException;

import de.slikey.effectlib.util.ParticleEffect;

public class FarmBuilder implements Listener {
	public EfeServer plugin;
	
	public FarmBuilder(EfeServer plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void createSign(SignChangeEvent e) {
		Location loc = e.getBlock().getLocation();
		
		if (!e.getBlock().getWorld().equals(plugin.worldIsl)) return;
		if (!plugin.efeIsland.getIsleRegion(e.getPlayer()).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) return;
		
		if (e.getLine(0).equalsIgnoreCase("밭") || e.getLine(1).equalsIgnoreCase("밭") ||
				e.getLine(2).equalsIgnoreCase("밭") || e.getLine(3).equalsIgnoreCase("밭") ||
				e.getLine(0).equalsIgnoreCase("farm") || e.getLine(1).equalsIgnoreCase("farm") ||
				e.getLine(2).equalsIgnoreCase("farm") || e.getLine(3).equalsIgnoreCase("farm")) {
			
			PlayerData data = PlayerData.get(e.getPlayer());
			int max = getMaxFarmAmount(e.getPlayer());
			
			if (data.getFarmAmount() >= max) {
				e.getPlayer().sendMessage("§c▒§r 당신은 이미 밭이 있습니다!");
				e.getBlock().breakNaturally();
				return;
			}
			
			if (!isGround(loc)) {
				e.getPlayer().sendMessage("§c▒§r 5×5의 흙 중앙에 설치해야합니다!");
				e.getBlock().breakNaturally();
				return;
			}
			
			Sign sign = (Sign) e.getBlock().getState();
			org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
			
			if (!signData.getFacing().equals(BlockFace.EAST) && !signData.getFacing().equals(BlockFace.NORTH) && 
					!signData.getFacing().equals(BlockFace.SOUTH) && !signData.getFacing().equals(BlockFace.WEST)) {
				e.getPlayer().sendMessage("§c▒§r 기울어진 방향입니다!");
				e.getBlock().breakNaturally();
				return;
			}
			
			fence(loc);
			enterance(e.getBlock(), signData.getFacing());
			region(e.getBlock().getLocation(), e.getPlayer(), data.getIslName());
			
			e.getBlock().getRelative(BlockFace.DOWN).setType(Material.WATER);
			e.getBlock().setType(Material.AIR);
			
			data.addFarm(loc);
			
			e.getPlayer().sendMessage("§a▒§r §7============================================");
			e.getPlayer().sendMessage("§a▒§r ");
			e.getPlayer().sendMessage("§a▒§r §b§l>> Your Farm Has Been Created!");
			e.getPlayer().sendMessage("§a▒§r ");
			e.getPlayer().sendMessage("§a▒§r §d괭이§r로 밭 입구의 표지판을 §e우클릭§r해서 §e작물을 심을 수§r 있습니다.");
			e.getPlayer().sendMessage("§a▒§r 반대로, 괭이로 §c클릭§r하면 심은 작물을 §c수확§r할 수 있습니다.");
			e.getPlayer().sendMessage("§a▒§r ");
			e.getPlayer().sendMessage("§a▒§r 이제 당신만의 작물을 키워 수확해보세요!");
			e.getPlayer().sendMessage("§a▒§r ");
			e.getPlayer().sendMessage("§a▒§r §7============================================");
		} else if (e.getLine(0).equalsIgnoreCase("§4[Farm]")) {
			e.setCancelled(true);
		}
	}
	
	public int getMaxFarmAmount(Player p) {
		int max = 1;
		
		UserData sData = new UserData(p);
		if (sData.hasLearned("farm.diligence"))
			max = 2;
		if (sData.hasLearned("farm.faithfulness"))
			max = 3;
		
		return max;
	}
	
	public boolean isGround(Location loc) {
		int x = loc.getBlockX() - 2;
		int	y = loc.getBlockY() - 1;
		int z = loc.getBlockZ() - 2;
		
		for (int i = x; i < x + 5; i ++) {
			for (int j = z; j < z + 5; j ++) {
				Block block = loc.getWorld().getBlockAt(i, y, j);
				
				if (!block.getType().equals(Material.GRASS) && !block.getType().equals(Material.DIRT) && !block.getType().equals(Material.MYCEL)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void fence(Location loc) {
		int x = loc.getBlockX() - 2;
		int y = loc.getBlockY();
		int z = loc.getBlockZ() - 2;
		
		for (int i = x; i < x + 5; i ++) {
			for (int j = z; j < z + 5; j ++) {
				if (i - x > 0 && i - x < 4 && j - z > 0 && j - z < 4) {
					loc.getWorld().getBlockAt(i, y - 1, j).setType(Material.SOIL);
					
					Block block = loc.getWorld().getBlockAt(i, y, j);
					if (!block.getType().equals(Material.AIR)) block.breakNaturally();
					
					continue;
				}
				
				Block block = loc.getWorld().getBlockAt(i, y, j);
				if (!block.getType().equals(Material.AIR)) block.breakNaturally();
				
				block.setType(Material.FENCE);
			}
		}
	}
	
	public void enterance(Block center, BlockFace face) {
		Block block = center.getRelative(face).getRelative(face);
		block.setType(Material.SIGN_POST);
		
		Sign sign = (Sign) block.getState();
		sign.setLine(0, "§2[Farm]");
		sign.setLine(2, "수분: 100%");
		sign.update();
		
		org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
		signData.setFacingDirection(face);
		
		sign.setData(signData);
		sign.update();
	}
	
	public void region(Location loc, Player p, String name) {
		BlockVector pos1 = BukkitUtil.toVector(loc.getWorld().getBlockAt(loc.getBlockX() - 2, loc.getBlockY() - 1, loc.getBlockZ() - 2));
		BlockVector pos2 = BukkitUtil.toVector(loc.getWorld().getBlockAt(loc.getBlockX() + 2, loc.getBlockY(), loc.getBlockZ() + 2));
		
		ProtectedCuboidRegion region = new ProtectedCuboidRegion(name+"_farm_"+convertLoc(loc), pos1, pos2);
		
		region.setFlag(DefaultFlag.BUILD, State.DENY);
		region.setFlag(DefaultFlag.BUILD.getRegionGroupFlag(), RegionGroup.ALL);
		region.setFlag(DefaultFlag.OTHER_EXPLOSION, State.DENY);
		region.setFlag(DefaultFlag.TNT, State.DENY);
		region.setFlag(DefaultFlag.FIRE_SPREAD, State.DENY);
		region.setFlag(DefaultFlag.WATER_FLOW, State.DENY);
		region.setFlag(DefaultFlag.LAVA_FLOW, State.DENY);
		region.setFlag(DefaultFlag.ICE_FORM, State.DENY);
		region.setFlag(DefaultFlag.MUSHROOMS, State.DENY);
		region.setFlag(DefaultFlag.VINE_GROWTH, State.ALLOW);
		
		try {
			region.setParent(plugin.getIslRegion(p));
		} catch (CircularInheritanceException e) {
			e.printStackTrace();
		}
		
		WGBukkit.getRegionManager(loc.getWorld()).addRegion(region);
	}
	
	private String convertLoc(Location loc) {
		return loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ();
	}
	
	public void destroyFarm(Player p, Location loc) {
		PlayerData data = PlayerData.get(p);
		
		data.removeFarm(loc);
		
		int x = loc.getBlockX() - 2;
		int	y = loc.getBlockY();
		int z = loc.getBlockZ() - 2;
		
		for (int i = x; i < x + 5; i ++) {
			for (int j = z; j < z + 5; j ++) {
				Block block = loc.getWorld().getBlockAt(i, y, j);
				
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
				block.setType(Material.AIR);
			}
		}
		
		y --;
		
		for (int i = x; i < x + 5; i ++) {
			for (int j = z; j < z + 5; j ++) {
				Block block = loc.getWorld().getBlockAt(i, y, j);
				
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
				block.setType(Material.DIRT);
			}
		}
		
		WGBukkit.getRegionManager(loc.getWorld()).removeRegion(data.getIslName()+"_farm_"+convertLoc(loc));
	}
	
	public Location getCenter(Sign sign) {
		org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
		
		BlockFace face = signData.getFacing().getOppositeFace();
		
		return sign.getBlock().getRelative(face).getRelative(face).getLocation();
	}
	
	@SuppressWarnings("deprecation")
	public void plantCrops(Location loc, Material type) {
		int x = loc.getBlockX() - 1;
		int y = loc.getBlockY();
		int z = loc.getBlockZ() - 1;
		
		for (int i = x; i < x + 3; i ++) {
			for (int j = z; j < z + 3; j ++) {
				if (i == loc.getBlockX() && j == loc.getBlockZ())
					continue;
				
				Block block = loc.getWorld().getBlockAt(i, y, j);
				
				block.setType(type);
				block.setData((byte) 7);
				
				if (type == Material.NETHER_WARTS) {
					block.getRelative(BlockFace.DOWN).setType(Material.SOUL_SAND);
				}
			}
		}
	}
	
	public void clearCrops(Farm farm) {
		Location loc = farm.getCenterLocation();
		
		int x = loc.getBlockX() - 1;
		int y = loc.getBlockY();
		int z = loc.getBlockZ() - 1;
		
		for (int i = x; i < x + 3; i ++) {
			for (int j = z; j < z + 3; j ++) {
				if (i == loc.getBlockX() && j == loc.getBlockZ()) continue;
				
				Block block = loc.getWorld().getBlockAt(i, y, j);
				
				loc.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
				block.setType(Material.AIR);
				
				if (farm.getCrop().getBlockType() == Material.NETHER_WARTS) {
					block.getRelative(BlockFace.DOWN).setType(Material.SOIL);
				}
			}
		}
	}
	
	public void waterEffect(Location loc) {
		int x = loc.getBlockX() - 1;
		int y = loc.getBlockY();
		int z = loc.getBlockZ() - 1;
		
		for (int i = x; i < x + 3; i ++) {
			for (int j = z; j < z + 3; j ++) {
				if (i == loc.getBlockX() && j == loc.getBlockZ()) continue;
				
				ParticleEffect.WATER_DROP.display(0.0F, 0.0F, 0.0F, 0.5F, 10, new Location(loc.getWorld(), i, y + 0.5, j), 32);
			}
		}
	}
}