package me.efe.efemobs.rudish.mobs.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.efe.efemobs.Expedition;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobSpawner;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Rudish1FZombie extends RudishBoss {
	private Random random = new Random();
	
	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(new ItemStack(Material.ROTTEN_FLESH, plugin.util.rand(4, 13)));
		
		if (Math.random() <= 0.3)
			list.add(new ItemStack(Material.IRON_INGOT, plugin.util.rand(2, 7)));
		if (Math.random() <= 0.2)
			list.add(new ItemStack(Material.CARROT_ITEM, plugin.util.rand(2, 5)));
		if (Math.random() <= 0.2)
			list.add(new ItemStack(Material.POTATO_ITEM, plugin.util.rand(2, 5)));
		
		return list;
	}
	
	@Override
	public long getSkillDelay(ActiveMob mob, Expedition exp) {
		
		return 20 * plugin.util.rand(15, 20);
	}
	
	@Override
	public void onSkill(ActiveMob mob, Expedition exp) {
		for (int i = 0; i < 5; i ++) {
			Entity entity1 = MobSpawner.SpawnMythicMob("Minion_1F_BabyZombie", mob.getEntity().getBukkitEntity().getLocation());
			
			if (mob.getLivingEntity().getHealth() <= 200) {
				Entity entity2 = MobSpawner.SpawnMythicMob("Minion_1F_BabyZombie", mob.getEntity().getBukkitEntity().getLocation());
				
				entity1.setPassenger(entity2);
				
				if (mob.getLivingEntity().getHealth() <= 100) {
					Entity entity3 = MobSpawner.SpawnMythicMob("Minion_1F_BabyZombie", mob.getEntity().getBukkitEntity().getLocation());
					
					entity2.setPassenger(entity3);
				}
			}
		}
	}
	
	private ItemStack color(ItemStack item) {
		
		return plugin.util.color(item, Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
	}
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e, Expedition exp) {
		e.getLivingEntity().getEquipment().setArmorContents(new ItemStack[]{null, null, null, null});
		e.getLivingEntity().getEquipment().setChestplate(color(new ItemStack(Material.LEATHER_CHESTPLATE)));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim, Expedition exp) {
		
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager, Expedition exp) {
		victim.getLivingEntity().getEquipment().setChestplate(color(new ItemStack(Material.LEATHER_CHESTPLATE)));
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e, Expedition exp) {
		
	}
}