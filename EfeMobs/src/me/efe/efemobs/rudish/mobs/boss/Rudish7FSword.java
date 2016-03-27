package me.efe.efemobs.rudish.mobs.boss;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.slikey.effectlib.util.ParticleEffect;
import me.efe.efemobs.rudish.mobs.RudishMob;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

public class Rudish7FSword implements RudishMob {
	
	@Override
	public void onSpawn(MythicMobSpawnEvent e) {
		e.getLivingEntity().getEquipment().setItemInHand(plugin.util.enchant(new ItemStack(Material.IRON_SWORD), Enchantment.DAMAGE_ALL, 1));
		
		e.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
	}
	
	@Override
	public void onAttack(EntityDamageByEntityEvent e, ActiveMob damager, Player victim) {
		e.setDamage(0.0D);
		
		victim.setHealth(2.0D);
	}
	
	@Override
	public void onDamaged(EntityDamageByEntityEvent e, ActiveMob victim, Player damager) {
		ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.IRON_SWORD, (byte) 0), 0.0F, 0.0F, 0.0F, 0.1F, 30, victim.getEntity().getBukkitEntity().getLocation().add(0, 1.7, 0), 32);
		
		victim.getEntity().getBukkitEntity().remove();
	}
	
	@Override
	public void onDeath(MythicMobDeathEvent e) {
		
	}
}