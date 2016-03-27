package me.efe.efemobs.rudish.mobs.boss;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMobHandler;
import me.confuser.barapi.BarAPI;
import me.efe.efeitems.ItemStorage;
import me.efe.efemobs.Expedition;
import me.efe.efemobs.rudish.mobs.RudishMob;

public abstract class RudishBoss implements RudishMob {
	
	public abstract List<ItemStack> getDrops();
	
	public abstract long getSkillDelay(ActiveMob mob, Expedition exp);
	
	public abstract void onSkill(ActiveMob mob, Expedition exp);
	
	public abstract void onSpawn(MythicMobSpawnEvent e, Expedition exp);
	
	public abstract void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim, Expedition exp);
	
	public abstract void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp);
	
	public abstract void onDeath(MythicMobDeathEvent e, Expedition exp);
	
	public List<ItemStack> getRewards(int floor) {
		List<ItemStack> list = getDrops();
		
		if (floor <= 3) {
			if (Math.random() <= 0.5)
				list.add(new ItemStack(Material.ENCHANTED_BOOK, plugin.util.rand(2, 6)));
		} else if (floor <= 6) {
			if (Math.random() <= 0.5)
				list.add(new ItemStack(Material.ENCHANTED_BOOK, plugin.util.rand(2, 6)));
			if (Math.random() <= 0.1)
				list.add(ItemStorage.ANVIL_ABANDONED.clone());
			if (Math.random() <= 0.1)
				list.add(ItemStorage.PICK_ME_UP_1.clone());
			if (Math.random() <= 0.1)
				list.add(ItemStorage.PICK_ME_UP_2.clone());
			if (Math.random() <= 0.1) {
				ItemStack item = ItemStorage.NAME_CHANGER.clone();
				item.setAmount(plugin.util.rand(6, 13));
				list.add(item);
			}
			if (Math.random() <= 0.25)
				list.add(ItemStorage.DECORATION_HEAD_BOX.clone());
		} else {
			if (Math.random() <= 0.5)
				list.add(new ItemStack(Material.ENCHANTED_BOOK, plugin.util.rand(6, 10)));
			if (Math.random() <= 0.1)
				list.add(ItemStorage.ANVIL_OLD.clone());
			if (Math.random() <= 0.1)
				list.add(ItemStorage.PICK_ME_UP_1.clone());
			if (Math.random() <= 0.1)
				list.add(ItemStorage.PICK_ME_UP_2.clone());
			if (Math.random() <= 0.1) {
				ItemStack item = ItemStorage.NAME_CHANGER.clone();
				item.setAmount(plugin.util.rand(6, 13));
				list.add(item);
			}
			if (Math.random() <= 0.5)
				list.add(ItemStorage.DECORATION_HEAD_BOX.clone());
		}
		
		return list;
	}
	
	public void onRegainHealth(ActiveMob mob) {
		int channel = plugin.bossListener.getChannel(mob.getEntity().getBukkitEntity().getLocation());
		Expedition exp = plugin.bossListener.expMap.get(channel);
		
		for (Player p : exp.getMembers()) {
			float percent = (float) (mob.getLivingEntity().getHealth() / mob.getLivingEntity().getMaxHealth() * 100.0F);
			
			BarAPI.setHealth(p, percent);
		}
	}
	
	@Override
	public void onSpawn(final MythicMobSpawnEvent e) {
		int channel = plugin.bossListener.getChannel(e.getLocation());
		Expedition exp = plugin.bossListener.expMap.get(channel);
		
		this.onSpawn(e, exp);
		
		if (this instanceof Rudish6FCreeper) {
			if (exp.getData() != null && (boolean) exp.getData()) return;
			
			exp.setData(true);
		}
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				skill(e.getEntity());
			}
		}, 1L);
	}
	
	private void skill(final Entity entity) {
		final int channel = plugin.bossListener.getChannel(entity.getLocation());
		final Expedition exp = plugin.bossListener.expMap.get(channel);
		
		final ActiveMob mob = ActiveMobHandler.getMythicMobInstance(entity);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if (RudishBoss.this instanceof Rudish6FCreeper) {
					ActiveMob creeper = plugin.bossListener.getBoss(channel);
					
					if (creeper == null || (!(boolean) exp.getData())) return;
					
					onSkill(creeper, exp);
					
					skill(creeper.getEntity().getBukkitEntity());
				} else {
					if (entity == null || entity.isDead()) return;
					
					onSkill(mob, exp);
					
					skill(entity);
				}
			}
		}, getSkillDelay(mob, exp));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		Expedition exp = plugin.bossListener.getExpedition(victim);
		
		if (exp == null) return;
		
		this.onAttack(e, damager, victim, exp);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, final ActiveMob victim, Player damager) {
		if (!(e.getDamager() instanceof Projectile) && !damager.hasMetadata("arrow")) {
			Vector from = victim.getLivingEntity().getLocation().toVector();
			Vector to = damager.getLocation().toVector();
			Vector vector = to.subtract(from).multiply(0.5D);
			
			damager.setVelocity(vector.setY(0.5D));
		}
		
		if (damager.hasMetadata("arrow")) {
			damager.removeMetadata("arrow", plugin);
		} else if (victim.getLivingEntity() instanceof Giant) {
			e.setCancelled(true);
			ActionBarAPI.sendActionBar(damager, "§c몬스터가 공격을 튕겨냈습니다!");
		}
		
		
		Expedition exp = plugin.bossListener.getExpedition(damager);
		
		if (exp == null) return;
		
		if (exp.getMembers().size() > 1)
			e.setDamage(e.getDamage() / exp.getMembers().size() * 1.25);
		
		this.onDamaged(e, victim, damager, exp);
		
		
		if (victim.getLivingEntity().getHealth() - e.getFinalDamage() > 0) {
			for (Player p : exp.getMembers()) {
				float percent = (float) (victim.getLivingEntity().getHealth() / victim.getLivingEntity().getMaxHealth() * 100.0F);
				
				BarAPI.setHealth(p, percent);
			}
		}
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		int channel = plugin.bossListener.getChannel(e.getLivingEntity().getLocation());
		Expedition exp = plugin.bossListener.expMap.get(channel);
		
		if (exp == null) return;
		
		this.onDeath(e, exp);
	}
}