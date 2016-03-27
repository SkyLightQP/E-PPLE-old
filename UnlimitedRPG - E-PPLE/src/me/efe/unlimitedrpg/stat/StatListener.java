package me.efe.unlimitedrpg.stat;

import java.util.ArrayList;
import java.util.List;

import me.efe.unlimitedrpg.UnlimitedRPG;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class StatListener implements Listener{
	public UnlimitedRPG plugin;
	public ItemStack SPpaper;
	public ItemStack SPreset;
	public List<EntityType> headshots = new ArrayList<EntityType>();
	
	public StatListener(UnlimitedRPG plugin) {
		this.plugin = plugin;
		SPpaper = plugin.util.createRawItem("§dSP 주문서", new ItemStack(Material.PAPER), new String[]{"§7우클릭시 SP를 획득합니다."});
		SPreset = plugin.util.createRawItem("§dSP 초기화 주문서", new ItemStack(Material.PAPER), 
				new String[]{"§7우클릭시 스텟에 소모한 SP를", "§7모두 되돌려받습니다."});
		SPreset.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
		
		headshots.add(EntityType.CREEPER);
		headshots.add(EntityType.IRON_GOLEM);
		headshots.add(EntityType.PIG_ZOMBIE);
		headshots.add(EntityType.PLAYER);
		headshots.add(EntityType.SKELETON);
		headshots.add(EntityType.SNOWMAN);
		headshots.add(EntityType.VILLAGER);
		headshots.add(EntityType.WITCH);
		headshots.add(EntityType.WITHER);
		headshots.add(EntityType.ZOMBIE);
	}
	
	@EventHandler
	public void usePaper(PlayerInteractEvent e) {
		if (e.getItem() == null) return;
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (plugin.util.containsLore(e.getItem(), "§7우클릭시 SP를 획득합니다.")) {
				e.setCancelled(true);
				e.getPlayer().setItemInHand(plugin.util.getUsed(e.getItem(), e.getPlayer()));
				
				StatAPI.editSP(e.getPlayer(), 1);
				
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.PORTAL_TRIGGER, 1.0F, 2.0F);
				e.getPlayer().sendMessage(plugin.main+"SP를 획득했습니다. §a[SP +1]");
			} else if (plugin.util.containsLore(e.getItem(), "§7우클릭시 스텟에 소모한 SP를")) {
				e.setCancelled(true);
				e.getPlayer().setItemInHand(plugin.util.getUsed(e.getItem(), e.getPlayer()));
				
				int[] stat = StatAPI.getStat(e.getPlayer());
				int sp = stat[0] + stat[1] + stat[2] + stat[3] + stat[4];
				
				stat[0] = sp;
				
				for (int i = 1; i < 5; i ++) {
					stat[i] = 0;
				}
				
				StatAPI.setStat(e.getPlayer(), stat);
				
				e.getPlayer().sendMessage(plugin.main+"SP를 초기화했습니다. §a[SP +"+sp+"]");
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.PORTAL_TRIGGER, 1.0F, 2.0F);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void levelup(PlayerLevelChangeEvent e) {
		if (e.getOldLevel() > e.getNewLevel()) return;
		
		int amount = plugin.getConfig().getInt("stat.levelup-sp");
		if (amount == 0) return;
		
		for (int i = 0; i < (e.getNewLevel() - e.getOldLevel()); i ++) {
			StatAPI.editSP(e.getPlayer(), amount);
			
			e.getPlayer().sendMessage(plugin.main+"레벨업하여 SP를 획득했습니다! §a[+ "+amount+"]");
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void strength(EntityDamageByEntityEvent e) {
		if (e.getDamager() != null && e.getDamager() instanceof Player) {
			
			Player p = (Player) e.getDamager();
			int str = StatAPI.getSTR(p);
			
			e.setDamage(plugin.statGUI.getValue("str", e.getDamage(), p));
			
			if (str >= 30 && plugin.util.rand(0.3)) {
				e.getEntity().setFireTicks(60);
				p.playSound(p.getLocation(), Sound.GHAST_FIREBALL, 1.0F, 2.0F);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void accuracy(EntityDamageByEntityEvent e) {
		if (e.getDamager() != null && e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			
			if (proj.getShooter() instanceof Player) {
				
				Player p = (Player) proj.getShooter();
				int acc = StatAPI.getACC(p);
				
				e.setDamage(plugin.statGUI.getValue("acc", e.getDamage(), p));
				
				if (acc >= 30 && headshots.contains(e.getEntityType()) && e.getDamager().getLocation().clone().subtract(e.getEntity().getLocation()).getY() >= 1.5) {
					e.setDamage(e.getDamage() * 2);
					p.playSound(p.getLocation(), Sound.IRONGOLEM_THROW, 1.0F, 2.0F);
					
					for (int i = 0; i < 10; i ++) {
						Snowball snowball = (Snowball) e.getEntity().getLocation().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.SNOWBALL);
						snowball.setVelocity(new Vector(Math.random() * 2 - 1, 0.3, Math.random() * 2 - 1));
						snowball.setMetadata("urpg_acc_effect", new FixedMetadataValue(plugin, "made_by_efe"));
					}
				}
			} else if (proj.hasMetadata("urpg_acc_effect")) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void defensive(EntityDamageByEntityEvent e) {
		if (e.getEntity() != null && e.getEntity() instanceof Player) {
			
			Player p = (Player) e.getEntity();
			
			int def = StatAPI.getDEF(p);
			double damage = plugin.statGUI.getValue("def", e.getDamage(), p);
			
			if (damage < 0) damage = 0.0D;
			e.setDamage(damage);
			
			if (p.isBlocking() && def >= 30 && plugin.util.rand(0.1)) {
				e.setDamage(0.0D);
				
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 2.0F);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void dexterity(final EntityDamageByEntityEvent e) {
		if (e.getDamager() != null && e.getDamager() instanceof Player) {
			
			final Player p = (Player) e.getDamager();
			int dex = StatAPI.getDEX(p);
			
			if (plugin.util.rand(plugin.statGUI.getValue("dex", e.getDamage(), p) / 100)) {
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						if (e.getEntity() == null || e.getEntity().isDead() || p == null) return;
						
						LivingEntity entity = (LivingEntity) e.getEntity();
						entity.damage(e.getDamage());
						entity.getWorld().playEffect(entity.getLocation(), Effect.SMOKE, 3);
					}
				}, 10L);
			}
			
			if (dex >= 30 && plugin.util.rand(0.1)) {
				Vector direction = e.getEntity().getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
				direction.setX(direction.getX() * 2);
                direction.setY(direction.getY() * 2);
                direction.setZ(direction.getZ() * 2);
                
                e.getEntity().setVelocity(direction.multiply(3.0D));
				
				p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.5F);
			}
		}
	}
}