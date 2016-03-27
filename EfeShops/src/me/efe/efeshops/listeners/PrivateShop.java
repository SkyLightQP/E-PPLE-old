package me.efe.efeshops.listeners;

import me.efe.efeitems.listeners.VillagerGUI;
import me.efe.efeserver.util.SignGUI;
import me.efe.efeshops.EfeShops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PrivateShop implements Listener {
	public EfeShops plugin;
	public BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};
	
	public PrivateShop(EfeShops plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (isShopBlock(e.getClickedBlock()) || isShopSign(e.getClickedBlock()))) {
			e.setCancelled(true);
			
			if (isShopBlock(e.getClickedBlock()) && !hasShopSign(e.getClickedBlock())) return;
			
			BlockState state = e.getClickedBlock().getState();
			ShopData data = isShopBlock(e.getClickedBlock()) ? 
					ShopData.getShopData((Chest) state) : ShopData.getShopData(getShopBlock(e.getClickedBlock()));
			
			if (data.getOwner().equals(e.getPlayer())) {
				e.getPlayer().sendMessage("§a▒§r 현재 개인 상점 이용이 금지되어 있습니다.");
				e.getPlayer().sendMessage("§a▒§r 관리는 가능하나, 타인은 상점을 열 수 없습니다.");
				e.getPlayer().sendMessage("§a▒§r 업데이트 이후 다시 만나요!");
				
				plugin.privateShopGui.openGUI(e.getPlayer(), data);
			} else {
				e.getPlayer().sendMessage("§a▒§r 현재 개인 상점 이용이 금지되어 있습니다.");
				e.getPlayer().sendMessage("§a▒§r 업데이트 이후 다시 만나요!");
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void blockPlace(final BlockPlaceEvent e) {
		if (e.getBlock().getState() instanceof Chest) {
			for (BlockFace face : faces) {
				if (isShopBlock(e.getBlock().getRelative(face))) {
					e.setCancelled(true);
					plugin.util.updateInv(e.getPlayer());
					
					e.getPlayer().sendMessage("§c▒§r 개인 상점 근처에는 상자를 설치할 수 없습니다!");
					return;
				} else if (isShopItem(e.getItemInHand()) && e.getBlock().getRelative(face).getState() instanceof Chest) {
					e.setCancelled(true);
					plugin.util.updateInv(e.getPlayer());
					
					e.getPlayer().sendMessage("§c▒§r 상자 근처에는 개인 상점을 설치할 수 없습니다!");
					return;
				}
			}
		}
		
		if (isShopItem(e.getItemInHand())) {
			
			final BlockState state = e.getBlock().getState();
			final boolean isBuy = isShopItemForBuy(e.getItemInHand());
			
			@SuppressWarnings("deprecation")
			final DirectionalContainer dir = new DirectionalContainer(state.getType(), state.getData().getData());
			
			ProtectedRegion region = plugin.epple.efeIsland.getIsleRegion(e.getPlayer());
			
			int x = e.getBlock().getX();
			int y = e.getBlock().getY();
			int z = e.getBlock().getZ();
			
			if (!e.getBlock().getRelative(dir.getFacing()).getType().equals(Material.AIR) || region == null || !region.contains(x, y, z)) {
				e.setCancelled(true);
				plugin.util.updateInv(e.getPlayer());
				
				return;
			}
			
			
			e.getPlayer().sendMessage("§a▒§r 2~4줄에 상점 설명을 입력해주세요!");
			
			plugin.signGUI.open(e.getPlayer(), new SignGUI.SignGUIListener() {
				
				@Override
				public void onSignDone(Player p, String[] lines) {
					Chest chest = (Chest) state;
					int number = ShopData.generateNumber();
					
					chest.getInventory().setItem(0, plugin.util.createRawItem("private_shop", new ItemStack(Material.NAME_TAG), 
							new String[]{number+""}));
					
					Block block = e.getBlock().getRelative(dir.getFacing());
					
					if (!block.getType().equals(Material.AIR)) block.breakNaturally();
					block.setType(Material.WALL_SIGN);
					
					Sign sign = (Sign) block.getState();
					org.bukkit.material.Sign mSign = (org.bukkit.material.Sign) sign.getData();
					
					mSign.setFacingDirection(dir.getFacing());
					
					sign.setLine(0, "§b§l[개인 상점]");
					sign.setLine(1, lines[1]);
					sign.setLine(2, lines[2]);
					sign.setLine(3, lines[3]);
					
					sign.update();
					
					ShopData.createShopData(number, lines, e.getPlayer().getUniqueId(), isBuy);
					
					e.getPlayer().sendMessage("§a▒§r 개인 상점이 개설되었습니다!");
					e.getPlayer().sendMessage("§a▒§r 상자 혹은 표지판을 우클릭하여 품목을 등록해보세요.");
				}
			});
		}
	}
	
	public void openSignGUI(Player p, final LivingEntity entity, final boolean isBuy) {
		p.closeInventory();
		p.sendMessage("§a▒§r 2~4줄에 상점 설명을 입력해주세요!");
		
		plugin.signGUI.open(p, new SignGUI.SignGUIListener() {
			
			@Override
			public void onSignDone(Player p, String[] lines) {
				int number = ShopData.generateNumber();
				
				entity.getEquipment().setLeggings(plugin.util.createRawItem("private_shop", new ItemStack(Material.NAME_TAG), 
						new String[]{number+""}));
				
				ShopData.createShopData(number, lines, p.getUniqueId(), isBuy);
				
				if (isBuy)
					VillagerGUI.takeBuyShopItem(p);
				else
					VillagerGUI.takeSellShopItem(p);
				
				p.sendMessage("§a▒§r 개인 상점이 개설되었습니다!");
				p.sendMessage("§a▒§r 해당 주민을 우클릭하여 품목을 등록해보세요.");
			}
		});
	}
	
	public boolean isMerchant(LivingEntity entity) {
		ItemStack item = entity.getEquipment().getLeggings();
		
		return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals("private_shop");
	}
	
	public void openMerchantGUI(Player p, LivingEntity entity) {
		int number = Integer.parseInt(entity.getEquipment().getLeggings().getItemMeta().getLore().get(0));
		
		plugin.privateShopGui.openGUI(p, ShopData.getShopData(number, entity));
	}
	
	public void removeMerchant(LivingEntity entity) {
		entity.getEquipment().setLeggings(new ItemStack(Material.AIR));
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void blockBreak(BlockBreakEvent e) {
		if (isShopBlock(e.getBlock()) || isShopSign(e.getBlock())) {
			e.setCancelled(true);
			plugin.util.updateInv(e.getPlayer());
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void hopper(InventoryMoveItemEvent e) {
		if (e.getItem() != null) {
			InventoryHolder initHolder = e.getInitiator().getHolder();
			
			if (initHolder instanceof Chest) {
				Chest chest = (Chest) initHolder;
				
				if (isShopBlock(chest.getBlock())) {
					e.setCancelled(true);
				}
			}
			
			
			InventoryHolder destHolder = e.getDestination().getHolder();
			
			if (destHolder instanceof Chest) {
				Chest chest = (Chest) destHolder;
				
				if (isShopBlock(chest.getBlock())) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void explode(EntityExplodeEvent e) {
		for (Block block : e.blockList()) {
			if (block.getType() == Material.TRAPPED_CHEST && isShopBlock(block)) {
				e.setCancelled(true);
			}
			
			if (block.getType() == Material.SIGN_POST && isShopSign(block)) {
				e.setCancelled(true);
			}
		}
	}
	
	public boolean isShopItem(ItemStack item) {
		if (!item.getType().equals(Material.TRAPPED_CHEST)) return false;
		if (!item.hasItemMeta()) return false;
		if (!item.getItemMeta().hasDisplayName()) return false;
		return item.getItemMeta().getDisplayName().startsWith("§b개인 상점");
	}
	
	public boolean isShopItemForBuy(ItemStack item) {
		if (!item.getType().equals(Material.TRAPPED_CHEST)) return false;
		if (!item.hasItemMeta()) return false;
		if (!item.getItemMeta().hasDisplayName()) return false;
		return item.getItemMeta().getDisplayName().equals("§b개인 상점§3:: 구매용");
	}
	
	public boolean isShopBlock(Block block) {
		if (!block.getType().equals(Material.TRAPPED_CHEST)) return false;
		
		Chest chest = (Chest) block.getState();
		ItemStack token = chest.getInventory().getItem(0);
		
		if (token == null) return false;
		if (!token.hasItemMeta()) return false;
		if (!token.getItemMeta().hasDisplayName()) return false;
		
		return token.getItemMeta().getDisplayName().equals("private_shop");
	}
	
	public boolean isShopSign(Block block) {
		if (!(block.getState() instanceof Sign)) return false;
		
		org.bukkit.material.Sign sign = (org.bukkit.material.Sign) block.getState().getData();
		
		return isShopBlock(block.getRelative(sign.getAttachedFace()));
	}
	
	public boolean hasShopSign(Block block) {
		@SuppressWarnings("deprecation")
		DirectionalContainer dir = new DirectionalContainer(block.getType(), block.getState().getData().getData());
		
		return block.getRelative(dir.getFacing()).getState() instanceof Sign;
	}
	
	public Chest getShopBlock(Block sign) {
		org.bukkit.material.Sign mSign = (org.bukkit.material.Sign) sign.getState().getData();
		
		return (Chest) sign.getRelative(mSign.getAttachedFace()).getState();
	}
	
	public Sign getShopSign(Block chest) {
		@SuppressWarnings("deprecation")
		DirectionalContainer dir = new DirectionalContainer(chest.getType(), chest.getData());
		
		return (Sign) chest.getRelative(dir.getFacing()).getState();
	}
}