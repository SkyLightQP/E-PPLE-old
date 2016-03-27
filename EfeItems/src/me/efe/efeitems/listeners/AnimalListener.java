package me.efe.efeitems.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.efe.efecommunity.Post;
import me.efe.efecore.util.EfeUtils;
import me.efe.efeisland.EfeIsland;
import me.efe.efeitems.EfeItems;
import me.efe.efeitems.ItemStorage;
import me.efe.efeserver.EfeServer;
import me.efe.efeserver.util.AnvilGUI;
import me.efe.efeserver.util.AnvilGUI.AnvilClickEvent;
import me.efe.efeserver.util.AnvilGUI.AnvilSlot;
import me.efe.efeshops.EfeShops;
import me.efe.unlimitedrpg.unlimitedtag.TagType;
import me.efe.unlimitedrpg.unlimitedtag.UnlimitedTagAPI;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class AnimalListener implements Listener {
	public EfeItems plugin;
	public Random random = new Random();
	public EfeShops efeShops;
	
	public AnimalListener(EfeItems plugin) {
		this.plugin = plugin;
		this.efeShops = (EfeShops) plugin.getServer().getPluginManager().getPlugin("EfeShops");
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if (event.getItem() != null && event.getItem().getType() == Material.MONSTER_EGG &&
				EfeUtils.event.isRightClick(event) && event.getPlayer().getWorld() == EfeServer.getInstance().worldIsl) {
			
			EfeIsland efeIsland = EfeServer.getInstance().efeIsland;
			ProtectedRegion region = efeIsland.getIsleRegion(event.getPlayer());
			Block block = EfeUtils.player.getTargetBlock(event.getPlayer(), 4, true);
			Location loc = block.getLocation().add(0.5, 1, 0.5);
			
			if (!region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("��c�ơ�r �ڽ��� �������� ��ȯ�� �� �ֽ��ϴ�.");
				return;
			}
			
			ActionBarAPI.sendActionBar(event.getPlayer(), "��e����: ���� �н��� å���� ���ο��� �ֽ��ϴ�.");
		}
		
		if (event.getItem() != null && ItemStorage.getName(event.getItem()).equals(ItemStorage.SPAWN_EGG_VILLAGER.getItemMeta().getDisplayName()) &&
				EfeUtils.event.isRightClick(event) && event.getPlayer().getWorld() == EfeServer.getInstance().worldIsl) {
			event.setCancelled(true);
			
			EfeIsland efeIsland = EfeServer.getInstance().efeIsland;
			ProtectedRegion region = efeIsland.getIsleRegion(event.getPlayer());
			Block block = EfeUtils.player.getTargetBlock(event.getPlayer(), 4, true);
			
			if (block == null)
				return;
			
			Location loc = block.getLocation().add(0.5, 1, 0.5);
			
			if (!region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
				event.getPlayer().sendMessage("��c�ơ�r �ڽ��� �������� ��ȯ�� �� �ֽ��ϴ�.");
				return;
			}
			
			if (block.getType().isSolid())
				loc.add(0, 1, 0);
			
			Villager villager = loc.getWorld().spawn(loc, Villager.class);
			
			villager.setProfession(Villager.Profession.values()[random.nextInt(Villager.Profession.values().length)]);
			villager.getEquipment().setBoots(EfeUtils.item.createItem(event.getPlayer().getUniqueId().toString(), new ItemStack(Material.NAME_TAG), null));
			
			event.getPlayer().setItemInHand(EfeUtils.item.getUsed(event.getItem().clone(), event.getPlayer()));
			
			ActionBarAPI.sendActionBar(event.getPlayer(), "��eä��/���� Ȱ���� �Ұ����ϸ� �н��� å���� ���ο��� �ֽ��ϴ�.");
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void interactEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getWorld() == EfeServer.getInstance().worldIsl &&
				event.getRightClicked() instanceof Villager && !(event.getRightClicked() instanceof Player)) {
			LivingEntity entity = (LivingEntity) event.getRightClicked();
			OfflinePlayer owner = getOwner(entity);
			
			if (owner == null) {
				event.setCancelled(true);
				entity.remove();
				return;
			}
			
			EfeIsland efeIsland = EfeServer.getInstance().efeIsland;
			ProtectedRegion region = efeIsland.getIsleRegion(owner);
			Location loc = event.getRightClicked().getLocation();
			
			if (!region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
				event.setCancelled(true);
				entity.teleport(efeIsland.getIsleSpawnLoc(owner));
				
				event.getPlayer().sendMessage("��a�ơ�r ������ ������ ��ȯ�߽��ϴ�.");
				return;
			}
			
			if (!owner.equals(event.getPlayer())) {
				event.setCancelled(true);
				
				ActionBarAPI.sendActionBar(event.getPlayer(), "��cŸ���� ������ �ǵ帱 �� �����ϴ�.");
				return;
			}
			
			ItemStack item = event.getPlayer().getItemInHand();
			
			if (item == null || item.getType() == Material.AIR) {
				
				if (entity.getType() == EntityType.VILLAGER) {
					plugin.villagerGui.openGUI(event.getPlayer(), entity);
				}
				
				return;
			}
			
			if (item.getType() == Material.BUCKET || item.getType() == Material.SHEARS) {
				event.setCancelled(true);
				
				EfeUtils.player.updateInv(event.getPlayer());
				return;
			}
			
			String name = ItemStorage.getName(item);
			
			if (name.equals(ItemStorage.NAME_CHANGER.getItemMeta().getDisplayName())) {
				event.setCancelled(true);
				
				event.getPlayer().sendMessage("��a�ơ�r ���ο� �̸��� �����ּ���. (�ѱ� 10��, ���� 20��)");
				
				AnvilGUI gui = new AnvilGUI(plugin, event.getPlayer(), new AnvilGUI.AnvilClickEventHandler() {
					
					@Override
					public void onAnvilClick(AnvilClickEvent event) {
						if (event.getSlot() == AnvilSlot.INPUT_LEFT || event.getSlot() == AnvilSlot.INPUT_RIGHT) {
							event.getPlayer().sendMessage("��c�ơ�r ������ ��ư�� ������ּ���.");
							return;
						}
						
						if (event.getName().isEmpty()) {
							event.getPlayer().sendMessage("��c�ơ�r �̸��� �Էµ��� �ʾҽ��ϴ�!");
							return;
						}
						
						if (event.getName().equalsIgnoreCase("THE KILLER BUNNY")) {
							event.getPlayer().sendMessage("��c�ơ�r �Ұ����� �̸��Դϴ�!");
							return;
						}
						
						if (event.getName().getBytes().length > 20) {
							event.getPlayer().sendMessage("��c�ơ�r �̸��� �ʹ� ��ϴ�!");
							return;
						}
						
						String itemName = ItemStorage.getName(ItemStorage.NAME_CHANGER);
						
						if (!ItemStorage.hasItem(event.getPlayer(), itemName)) {
							event.getPlayer().sendMessage("��c�ơ�r �������� �����ϴ�.");
							
							event.setWillClose(true);
							event.setWillDestroy(true);
							return;
						}
						
						ItemStorage.takeItem(event.getPlayer(), itemName, 1);
						
						entity.setCustomNameVisible(true);
						entity.setCustomName(event.getName());
						
						event.getPlayer().sendMessage("��a�ơ�r �̸��� ����Ǿ����ϴ�.");
						
						event.setWillClose(true);
						event.setWillDestroy(true);
					}
				});
				
				gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, EfeUtils.item.createDisplayItem("�̸�", new ItemStack(Material.NAME_TAG), 
						new String[]{}));
				gui.open();
			}
		}
	}
	
	private OfflinePlayer getOwner(LivingEntity entity) {
		ItemStack item = entity.getEquipment().getBoots();
		
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
			return null;
		
		String tag = item.getItemMeta().getDisplayName();
		UUID id = UUID.fromString(tag);
		
		return plugin.getServer().getOfflinePlayer(id);
	}
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent event) {
		if (event.getEntity().getWorld() == EfeServer.getInstance().worldIsl && event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof Player)) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			OfflinePlayer owner = getOwner(entity);
			
			if (owner == null) return;
			
			EfeIsland efeIsland = EfeServer.getInstance().efeIsland;
			ProtectedRegion region = efeIsland.getIsleRegion(owner);
			Location loc = entity.getLocation();
			
			if (!region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
				event.setCancelled(true);
				entity.teleport(efeIsland.getIsleSpawnLoc(owner));
				return;
			}
			
			if (!owner.equals(event.getDamager())) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void death(EntityDeathEvent event) {
		if (event.getEntity().getWorld() == EfeServer.getInstance().worldIsl) {
			OfflinePlayer owner = getOwner(event.getEntity());
			
			if (owner == null)
				return;
			
			event.setDroppedExp(0);
			event.getDrops().clear();
			
			if (efeShops.privateShop.isMerchant(event.getEntity())) {
				efeShops.privateShop.removeMerchant(event.getEntity());
			}
			
			List<ItemStack> list = new ArrayList<ItemStack>();
			
			ItemStack spawnEgg = ItemStorage.SPAWN_EGG_VILLAGER;
			
			if (spawnEgg == null)
				return;
			
			UnlimitedTagAPI.addTag(spawnEgg, TagType.VESTED, owner.getUniqueId().toString());
			
			list.add(spawnEgg);
			
			if (event.getEntity().getCustomName() != null) {
				ItemStack item = ItemStorage.NAME_CHANGER.clone();
				
				list.add(item);
			}
			
			if (owner.isOnline() && owner.getPlayer().getInventory().firstEmpty() != -1) {
				for (ItemStack item : list)
					owner.getPlayer().getInventory().addItem(item);
				
				if (owner.getPlayer().getWorld().equals(EfeServer.getInstance().world)) {
					owner.getPlayer().sendMessage("��a�ơ�r ����� ������ �������� �κ��丮�� ���޵Ǿ����ϴ�.");
				}
			} else {
				Post post = Post.getBuilder()
						.setSender("��a������")
						.setMessage("����� ������ ������", "������ ���� ����� ������ �������� ��޵Ǿ����ϴ�.")
						.setItems(list.toArray(new ItemStack[list.size()]))
						.build();
				Post.sendPost(owner, post);
				
				if (owner.isOnline()) {
					owner.getPlayer().sendMessage("��a�ơ�r ����� ������ �������� ���������� ���۵Ǿ����ϴ�.");
				}
			}
		}
	}
	
	@EventHandler
	public void noBreed(CreatureSpawnEvent event) {
		if (event.getEntityType() == EntityType.VILLAGER && event.getSpawnReason() == SpawnReason.BREEDING) {
			event.setCancelled(true);
		}
	}
}