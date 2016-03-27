package me.efe.efetutorial.listeners;

import me.efe.efecore.util.EfeUtils;
import me.efe.efeisland.IslandUtils;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.reform.farming.FarmBuilder;
import me.efe.efetutorial.EfeTutorial;
import me.efe.efetutorial.TutorialState;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;

public class FarmListener implements Listener {
	private EfeTutorial plugin;
	
	public FarmListener(EfeTutorial plugin) {
		this.plugin = plugin;
		
		registerListener();
	}
	
	public void registerListener() {
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.BLOCK_PLACE) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				if (TutorialState.ARRIVED_FARM_ISLAND <= TutorialState.get(event.getPlayer()) && TutorialState.get(event.getPlayer()) <= TutorialState.GO_TO_FISH) {
					if (!event.getPlayer().hasMetadata("tutorial_farm"))
						return;
					
					BlockPosition pos = event.getPacket().getBlockPositionModifier().read(0);
					String[] data = event.getPlayer().getMetadata("tutorial_farm").get(0).asString().split("\\|");
					int x = Integer.parseInt(data[0]);
					int y = Integer.parseInt(data[1]);
					int z = Integer.parseInt(data[2]);
					
					Location center = new Location(event.getPlayer().getWorld(), x, y, z);
					BlockFace face = (BlockFace) event.getPlayer().getMetadata("tutorial_farm_sign").get(0).value();
					Location signLoc = getSignLoc(center, face);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							createFence(event.getPlayer(), center);
							createEntrance(event.getPlayer(), signLoc, face);
							if (TutorialState.get(event.getPlayer()) >= TutorialState.WATERD_FARM) {
								if (TutorialState.get(event.getPlayer()) >= TutorialState.PLANTED_SEEDS) {
									if (TutorialState.get(event.getPlayer()) < TutorialState.HARVESTED_CROPS) {
										plantCrops(event.getPlayer(), center);
									}
									
									event.getPlayer().sendSignChange(signLoc, new String[]{"§2[Farm]", "", "수분: 80%", ""});
								} else {
									event.getPlayer().sendSignChange(signLoc, new String[]{"§2[Farm]", "", "수분: 100%", ""});
								}
							} else {
								event.getPlayer().sendSignChange(signLoc, new String[]{"§2[Farm]", "", "수분: 50%", ""});
							}
						}
					}, 1L);
					
					if (pos.getX() == signLoc.getBlockX() && pos.getY() == y && pos.getZ() == signLoc.getBlockZ()) {
						event.setCancelled(true);
						
						if (signLoc.getBlock().getType() == Material.GRASS) return;
						
						if (event.getPlayer().getItemInHand().getType().toString().endsWith("_HOE")) {
							
							if (TutorialState.get(event.getPlayer()) == TutorialState.SP_USED_CARROT) {
								
								EfeUtils.player.takeItem(event.getPlayer(), new ItemStack(Material.SEEDS), 8);
								
								plantCrops(event.getPlayer(), center);
								
								event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);
								event.getPlayer().sendMessage("§a▒§r 작물을 심었습니다!");
								
								TutorialListener.onPlantSeeds(event.getPlayer());
							} else {
								event.getPlayer().sendMessage("§c▒§r 스킬트리에서 당근 스킬을 배워주세요!");
							}
							
						}
					}
				}
			}
		});
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.BLOCK_DIG) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				if (TutorialState.ARRIVED_FARM_ISLAND <= TutorialState.get(event.getPlayer()) && TutorialState.get(event.getPlayer()) <= TutorialState.GO_TO_FISH) {
					if (!event.getPlayer().hasMetadata("tutorial_farm"))
						return;
					
					BlockPosition pos = event.getPacket().getBlockPositionModifier().read(0);
					String[] data = event.getPlayer().getMetadata("tutorial_farm").get(0).asString().split("\\|");
					int x = Integer.parseInt(data[0]);
					int y = Integer.parseInt(data[1]);
					int z = Integer.parseInt(data[2]);
					
					Location center = new Location(event.getPlayer().getWorld(), x, y, z);
					BlockFace face = (BlockFace) event.getPlayer().getMetadata("tutorial_farm_sign").get(0).value();
					Location signLoc = getSignLoc(center, face);
					
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							createFence(event.getPlayer(), center);
							createEntrance(event.getPlayer(), signLoc, face);
							if (TutorialState.get(event.getPlayer()) >= TutorialState.WATERD_FARM) {
								if (TutorialState.get(event.getPlayer()) >= TutorialState.PLANTED_SEEDS) {
									if (TutorialState.get(event.getPlayer()) < TutorialState.HARVESTED_CROPS) {
										plantCrops(event.getPlayer(), center);
									}
									
									event.getPlayer().sendSignChange(signLoc, new String[]{"§2[Farm]", "", "수분: 80%", ""});
								} else {
									event.getPlayer().sendSignChange(signLoc, new String[]{"§2[Farm]", "", "수분: 100%", ""});
								}
							} else {
								event.getPlayer().sendSignChange(signLoc, new String[]{"§2[Farm]", "", "수분: 50%", ""});
							}
						}
					}, 1L);
					
					if (pos.getX() == signLoc.getBlockX() && pos.getY() == y && pos.getZ() == signLoc.getBlockZ()) {
						event.setCancelled(true);
						
						if (signLoc.getBlock().getType() == Material.GRASS) return;
						
						if (event.getPlayer().getItemInHand().getType() == Material.WATER_BUCKET &&
								TutorialState.get(event.getPlayer()) == TutorialState.CREATED_FARM) {
							
							event.getPlayer().getItemInHand().setType(Material.BUCKET);
							event.getPlayer().sendSignChange(signLoc, new String[]{"§2[Farm]", "", "수분: 100%", ""});
							
							event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.SWIM, 1.0F, 1.0F);
							event.getPlayer().sendMessage("§a▒§r 밭에 물을 주었습니다. §7[수분 +50%]");
							
							TutorialListener.onWaterFarm(event.getPlayer());
						} else if (event.getPlayer().getItemInHand().getType().toString().endsWith("_HOE") &&
								TutorialState.get(event.getPlayer()) == TutorialState.LETS_HARVEST_CROPS) {
							
							clearCrops(event.getPlayer(), center);
							
							event.getPlayer().sendMessage("§a▒§r 밀을 수확했습니다.");
							event.getPlayer().sendMessage("§a▒§r 수확: 당근§7×0§r");
							
							TutorialListener.onHarvestCrops(event.getPlayer());
						}
					}
				}
			}
		});
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlaceBlock(BlockPlaceEvent event) {
		Location loc = event.getBlock().getLocation();
		
		if (IslandUtils.getIsleName(loc).equals(IslandUtils.TUTORIAL_FARM)) {
			
			if (event.getBlock().getState() instanceof Sign &&
				TutorialState.get(event.getPlayer()) == TutorialState.ARRIVED_FARM_ISLAND) {
				
				if (event.getPlayer().hasMetadata("tutorial_farm")) return;
				
				FarmBuilder farmBuilder = EfeServer.getInstance().farmBuilder;
				
				if (!farmBuilder.isGround(loc)) {
					event.setCancelled(true);
					event.getPlayer().sendMessage("§c▒§r 5×5의 흙 중앙에 설치해야합니다!");
					return;
				}
				
				Sign sign = (Sign) event.getBlock().getState();
				org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
				
				if (!signData.getFacing().equals(BlockFace.EAST) && !signData.getFacing().equals(BlockFace.NORTH) && 
						!signData.getFacing().equals(BlockFace.SOUTH) && !signData.getFacing().equals(BlockFace.WEST)) {
					event.setCancelled(true);
					event.getPlayer().sendMessage("§c▒§r 기울어진 방향입니다!");
					return;
				}
				
				event.getPlayer().sendMessage("§a▒§r 표지판에 \"밭\"이라고 적어주세요!");
			} else {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (IslandUtils.getIsleName(event.getBlock().getLocation()).equals(IslandUtils.TUTORIAL_FARM)) {
			Player player = event.getPlayer();
			Location loc = event.getBlock().getLocation();
			String[] lines = event.getLines();
			
			Sign sign = (Sign) event.getBlock().getState();
			org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
			
			event.setCancelled(true);
			event.getBlock().breakNaturally();
			
			if (lines[0].equals("밭") || lines[1].equals("밭") || lines[2].equals("밭") || lines[3].equals("밭") ||
					lines[0].equals("farm") || lines[1].equals("farm") || lines[2].equals("farm") || lines[3].equals("farm")) {
				Location signLoc = loc.getBlock().getRelative(signData.getFacing()).getRelative(signData.getFacing()).getLocation();
				
				createFence(player, loc);
				createEntrance(player, signLoc, signData.getFacing());
				
				TutorialListener.onCreateFarm(player);
				
				player.setMetadata("tutorial_farm", new FixedMetadataValue(plugin, loc.getBlockX()+"|"+loc.getBlockY()+"|"+loc.getBlockZ()));
				player.setMetadata("tutorial_farm_sign", new FixedMetadataValue(plugin, signData.getFacing()));
				
				player.sendMessage("§a▒§r §7============================================");
				player.sendMessage("§a▒§r ");
				player.sendMessage("§a▒§r §b§l>> Your Farm Has Been Created!");
				player.sendMessage("§a▒§r ");
				player.sendMessage("§a▒§r §d괭이§r로 밭 입구의 표지판을 §e우클릭§r해서 §e작물을 심을 수§r 있습니다.");
				player.sendMessage("§a▒§r 반대로, 괭이로 §c클릭§r하면 심은 작물을 §c수확§r할 수 있습니다.");
				player.sendMessage("§a▒§r ");
				player.sendMessage("§a▒§r 이제 당신만의 작물을 키워 수확해보세요!");
				player.sendMessage("§a▒§r ");
				player.sendMessage("§a▒§r §7============================================");
			} else {
				player.sendMessage("§c▒§r 표지판에 §a밭§r이라고 적어주세요!");
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onBreakBlock(BlockBreakEvent event) {
		Location loc = event.getBlock().getLocation();
		
		if (IslandUtils.getIsleName(loc).equals(IslandUtils.TUTORIAL_FARM)) {
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void createFence(Player player, Location loc) {
		int x = loc.getBlockX() - 2;
		int y = loc.getBlockY();
		int z = loc.getBlockZ() - 2;
		
		for (int i = x; i < x + 5; i ++) {
			for (int j = z; j < z + 5; j ++) {
				if (i - x > 0 && i - x < 4 && j - z > 0 && j - z < 4) {
					player.sendBlockChange(loc.getWorld().getBlockAt(i, y - 1, j).getLocation(), Material.SOIL, (byte) 0);
					
					Block block = loc.getWorld().getBlockAt(i, y, j);
					if (!block.getType().equals(Material.AIR)) {
						player.sendBlockChange(block.getLocation(), Material.AIR, (byte) 0);
					}
					
					continue;
				}
				
				Block block = loc.getWorld().getBlockAt(i, y, j);
				if (!block.getType().equals(Material.AIR)) {
					player.sendBlockChange(block.getLocation(), Material.AIR, (byte) 0);
				}
				
				player.sendBlockChange(block.getLocation(), Material.FENCE, (byte) 0);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void createEntrance(Player player, Location loc, BlockFace face) {
		org.bukkit.material.Sign signData = new org.bukkit.material.Sign();
		signData.setFacingDirection(face);
		
		player.sendBlockChange(loc, Material.SIGN_POST, signData.getData());
		player.sendSignChange(loc, new String[]{"§2[Farm]", "", "수분: 50%", ""});
	}
	
	@SuppressWarnings("deprecation")
	public void plantCrops(Player player, Location loc) {
		int x = loc.getBlockX() - 1;
		int y = loc.getBlockY();
		int z = loc.getBlockZ() - 1;
		
		for (int i = x; i < x + 3; i ++) {
			for (int j = z; j < z + 3; j ++) {
				if (i == loc.getBlockX() && j == loc.getBlockZ()) continue;
				
				Block block = loc.getWorld().getBlockAt(i, y, j);
				
				player.sendBlockChange(block.getLocation(), Material.CARROT, (byte) 7);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void clearCrops(Player player, Location loc) {
		int x = loc.getBlockX() - 1;
		int y = loc.getBlockY();
		int z = loc.getBlockZ() - 1;
		
		for (int i = x; i < x + 3; i ++) {
			for (int j = z; j < z + 3; j ++) {
				if (i == loc.getBlockX() && j == loc.getBlockZ()) continue;
				
				Block block = loc.getWorld().getBlockAt(i, y, j);
				
				loc.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, Material.CROPS);
				player.sendBlockChange(block.getLocation(), Material.AIR, (byte) 0);
			}
		}
	}
	
	public Location getSignLoc(Location loc, BlockFace face) {
		
		return loc.getBlock().getRelative(face).getRelative(face).getLocation();
	}
}