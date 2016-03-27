package me.efe.efecrops;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class FarmCreater implements Listener{
	public EfeCrops plugin;
	
	public FarmCreater(EfeCrops plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void create(SignChangeEvent e){
		if (e.getLine(0).equalsIgnoreCase("밭") || e.getLine(1).equalsIgnoreCase("밭") ||
				e.getLine(2).equalsIgnoreCase("밭") || e.getLine(3).equalsIgnoreCase("밭")){
			if (getFarmers().contains(e.getPlayer().getName())){
				e.getPlayer().sendMessage("§c■§r 당신은 이미 밭이 있습니다!");
				e.getBlock().breakNaturally();
				return;
			}
			Location l = e.getBlock().getLocation();
			if (!isGround(l)){
				e.getPlayer().sendMessage("§c■§r 5x5의 잔디 중앙에 설치해야합니다!");
				e.getBlock().breakNaturally();
				return;
			}
			Sign sign = (Sign) e.getBlock().getState();
			org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
			if (!signData.getFacing().equals(BlockFace.EAST) && !signData.getFacing().equals(BlockFace.NORTH) && 
					!signData.getFacing().equals(BlockFace.SOUTH) && !signData.getFacing().equals(BlockFace.WEST)){
				e.getPlayer().sendMessage("§c■§r 기울어진 방향입니다!");
				e.getBlock().breakNaturally();
				return;
			}
			fence(l);
			enterance(e.getBlock(), signData.getFacing(), e.getPlayer());
			e.getBlock().getRelative(BlockFace.DOWN).setType(Material.WATER);
			e.getBlock().setType(Material.AIR);
			e.getPlayer().sendMessage("§a■§r 당신의 밭이 개설되었습니다!!");
			addFarmer(e.getPlayer());
		}
	}
	
	public boolean isGround(Location l){
		int x = l.getBlockX() - 2;
		int	y = l.getBlockY() - 1;
		int z = l.getBlockZ() - 2;
		for (int i = x; i < x + 5; i ++){
			for (int j = z; j < z + 5; j ++){
				Block block = l.getWorld().getBlockAt(i, y, j);
				if (!block.getType().equals(Material.GRASS)) return false;
			}
		}
		return true;
	}
	
	public void fence(Location l){
		int x = l.getBlockX() - 2;
		int y = l.getBlockY();
		int z = l.getBlockZ() - 2;
		for (int i = x; i < x + 5; i ++){
			for (int j = z; j < z + 5; j ++){
				if (i - x > 0 && i - x < 4 && j - z > 0 && j - z < 4){
					Block block = l.getWorld().getBlockAt(i, y - 1, j);
					block.setType(Material.SOIL);
					continue;
				}
				Block block = l.getWorld().getBlockAt(i, y, j);
				if (!block.getType().equals(Material.AIR)) block.breakNaturally();
				block.setType(Material.FENCE);
			}
		}
	}
	
	public void enterance(Block center, BlockFace face, Player p){
		Block block = center.getRelative(face).getRelative(face);
		block.setType(Material.SIGN_POST);
		Sign sign = (Sign) block.getState();
		sign.setLine(0, ChatColor.DARK_GREEN+"[MyFarm]");
		sign.setLine(1, p.getName());
		sign.setLine(2, "0");
		sign.update();
		org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
		signData.setFacingDirection(face);
		sign.setData(signData);
		sign.update();
	}
	
	public void addFarmer(Player p){
		File f = new File("plugins/EfeCrops/farmers.txt");
		File folder = new File("plugins/EfeCrops");
		List<String> data = getFarmers();
		data.add(p.getName());
		try {
			if (!f.exists()){
				folder.mkdir();
				f.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (String str : data){
				bw.append(str+"\r\n");
			}
			bw.flush();
			bw.close();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
	}
	
	public List<String> getFarmers(){
		File f = new File("plugins/EfeCrops/farmers.txt");
		File folder = new File("plugins/EfeCrops");
		List<String> value = new ArrayList<String>();
		try {
			if (!f.exists()){
				folder.mkdir();
				f.createNewFile();
			}
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s;
			while ((s = br.readLine()) != null){
				value.add(s);
			}
			br.close();
			return value;
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		return value;
	}
}