package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.efe.efemobs.Expedition;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;

public class Rudish6FCreeper extends RudishBoss {
	private Material[] records = new Material[]{
			Material.GOLD_RECORD, Material.GREEN_RECORD,Material.RECORD_3, Material.RECORD_4, Material.RECORD_5, Material.RECORD_6, Material.RECORD_7, Material.RECORD_8,
			Material.RECORD_9, Material.RECORD_10, Material.RECORD_11, Material.RECORD_12};
	
	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.SULPHUR, plugin.util.rand(4, 7)));
		
		if (Math.random() <= 0.3)
			list.add(new ItemStack(Material.TNT, plugin.util.rand(1, 2)));
		if (Math.random() <= 0.03)
			list.add(new ItemStack(records[new Random().nextInt(records.length)]));
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		
		return 20 * plugin.util.rand(10, 15);
	}
	
	@Override
	public void onSkill(ActiveMob mob, Expedition exp) {
		switch (plugin.util.random.nextInt(3)) {
		case 0:
			for (Player p : exp.getMembers()) {
				TNTPrimed tnt = mob.getEntity().getBukkitEntity().getWorld().spawn(p.getLocation(), TNTPrimed.class);
				
				tnt.setFuseTicks(40);
				
				for (int i = 0; i < 14; i ++) {
					TNTPrimed newTnt = mob.getEntity().getBukkitEntity().getWorld().spawn(p.getLocation(), TNTPrimed.class);
					
					newTnt.setFuseTicks(40);
					tnt.setPassenger(newTnt);
					
					tnt = newTnt;
				}
			}
			
			break;
		case 1:
			for (Player p : exp.getMembers()) {
				TNTPrimed tnt = mob.getEntity().getBukkitEntity().getWorld().spawn(p.getLocation().add(0, 1, 0), TNTPrimed.class);
				
				tnt.setFuseTicks(30);
				tnt.setPassenger(p);
			}
			
			break;
		case 2:
			launchTNT(exp.getChannel(), 30);
			
			break;
		}
	}
	
	private void launchTNT(final int channel, final int amount) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				ActiveMob mob = plugin.bossListener.getBoss(channel);
				
				if (mob == null || mob.getEntity().getBukkitEntity() == null || mob.getEntity().getBukkitEntity().isDead()) return;
				
				
				TNTPrimed tnt = mob.getEntity().getBukkitEntity().getWorld().spawn(mob.getEntity().getBukkitEntity().getLocation(), TNTPrimed.class);
				
				tnt.setFuseTicks(80);
				tnt.setVelocity(new Vector(Math.random() * 2 - 1, 0.3, Math.random() * 2 - 1).multiply(1.5D));
				
				ParticleEffect.SMOKE_NORMAL.display(0.0F, 0.0F, 0.0F, 0.2F, 200, mob.getEntity().getBukkitEntity().getLocation(), 32);
				
				
				if (amount == 0) return;
				
				launchTNT(channel, amount - 1);
			}
		}, 5L);
	}
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e, Expedition exp) {
		Creeper creeper = (Creeper) e.getEntity();
		
		creeper.setPowered(true);
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim, Expedition exp) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp) {
		if (victim.getLivingEntity().getHealth() - e.getFinalDamage() <= 0) return;
		
		MobSpawner.SpawnMythicMob("Minion_6F_LightCreeper", victim.getEntity().getBukkitEntity().getLocation());
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		exp.setData(false);
	}
}