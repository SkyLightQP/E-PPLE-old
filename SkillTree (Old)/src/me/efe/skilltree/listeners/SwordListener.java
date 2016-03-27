package me.efe.skilltree.listeners;

import me.efe.efeisland.EfeFlag;
import me.efe.skilltree.SkillTree;
import me.efe.skilltree.SkillUtils;
import me.efe.skilltree.UserData;
import net.minecraft.server.v1_8_R3.EntityPlayer;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mewin.util.Util;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import de.slikey.effectlib.util.ParticleEffect;

public class SwordListener implements Listener {
	public SkillTree plugin;
	
	public SwordListener(SkillTree plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void weakness(EntityDamageByEntityEvent e) {
		if (Util.getFlagValue(plugin.wgp, e.getEntity().getLocation(), EfeFlag.SKILL) == State.DENY) return;
		
		if (plugin.util.getDamager(e) instanceof Player && e.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) e.getEntity();
			Player p = (Player) plugin.util.getDamager(e);
			UserData data = new UserData(p);
			
			if (p.getItemInHand() == null || !p.getItemInHand().getType().name().endsWith("_SWORD")) return;
			if (entity.getNoDamageTicks() > entity.getMaximumNoDamageTicks() / 2.0F) return;
			if (entity instanceof Player && Util.getFlagValue(plugin.wgp, p.getLocation(), DefaultFlag.PVP) != State.ALLOW) return;
			
			if (isCriticalHit(p)) {
				int bindingLevel = data.getLevel(SkillUtils.getSkill("hunt.binding"));
				if (bindingLevel == 0) return;
				
				if (bindingLevel == 5 || Math.random() <= (0.1 + 0.15 * bindingLevel)) {
					entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 127));
					ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.5F, 10, entity.getLocation().add(0, 1, 0), 32);
				}
			}
			
			if (Math.random() <= 0.3) return;
			
			int level = data.getLevel(SkillUtils.getSkill("hunt.weakness"));
			if (level == 0) return;
			
			int sec = level % 2 == 0 ? 15 : 10;
			int lv = level <= 2 ? 0 : level <= 4 ? 1 : 2;
			
			entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * sec, lv));
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean isCriticalHit(Player p) {
		EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
		
		return (p.getFallDistance() > 0.0F) && (!p.isOnGround()) && !entityPlayer.k_() && !entityPlayer.V() &&
				(!p.hasPotionEffect(PotionEffectType.BLINDNESS)) && (p.getVehicle() == null);
	}
}