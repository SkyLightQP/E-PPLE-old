package me.efe.skilltree.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.efe.efeisland.EfeFlag;
import me.efe.efeserver.util.Scoreboarder;
import me.efe.skilltree.DelayUtils;
import me.efe.skilltree.SkillTree;
import me.efe.skilltree.SkillUtils;
import me.efe.skilltree.UserData;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mewin.util.Util;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import de.slikey.effectlib.util.ParticleEffect;

public class ArmorListener implements Listener {
	public SkillTree plugin;
	public List<HashMap<PotionEffectType, String>> angelicBlessEffects = new ArrayList<HashMap<PotionEffectType, String>>();
	
	public ArmorListener(SkillTree plugin) {
		this.plugin = plugin;
		
		HashMap<PotionEffectType, String> map = new HashMap<PotionEffectType, String>();
		map.put(PotionEffectType.WEAKNESS, "나약함");
		this.angelicBlessEffects.add(map);
		
		map = new HashMap<PotionEffectType, String>();
		map.put(PotionEffectType.WEAKNESS, "나약함");
		map.put(PotionEffectType.POISON, "독");
		this.angelicBlessEffects.add(map);

		map = new HashMap<PotionEffectType, String>();
		map.put(PotionEffectType.WEAKNESS, "나약함");
		map.put(PotionEffectType.POISON, "독");
		map.put(PotionEffectType.SLOW, "구속");
		this.angelicBlessEffects.add(map);
		
		map = new HashMap<PotionEffectType, String>();
		map.put(PotionEffectType.WEAKNESS, "나약함");
		map.put(PotionEffectType.POISON, "독");
		map.put(PotionEffectType.SLOW, "구속");
		map.put(PotionEffectType.HUNGER, "허기");
		this.angelicBlessEffects.add(map);

		map = new HashMap<PotionEffectType, String>();
		map.put(PotionEffectType.WEAKNESS, "나약함");
		map.put(PotionEffectType.POISON, "독");
		map.put(PotionEffectType.SLOW, "구속");
		map.put(PotionEffectType.HUNGER, "허기");
		map.put(PotionEffectType.BLINDNESS, "실명");
		this.angelicBlessEffects.add(map);
		
		map = new HashMap<PotionEffectType, String>();
		map.put(PotionEffectType.WEAKNESS, "나약함");
		map.put(PotionEffectType.POISON, "독");
		map.put(PotionEffectType.SLOW, "구속");
		map.put(PotionEffectType.HUNGER, "허기");
		map.put(PotionEffectType.BLINDNESS, "실명");
		map.put(PotionEffectType.CONFUSION, "멀미");
		this.angelicBlessEffects.add(map);
	}
	
	@EventHandler
	public void guard(EntityDamageByEntityEvent e) {
		if (Util.getFlagValue(plugin.wgp, e.getDamager().getLocation(), EfeFlag.SKILL) == State.DENY) return;
		
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
			Player p = (Player) e.getEntity();
			UserData data = new UserData(p);
			
			if (!p.isSneaking()) return;
			
			int level = data.getLevel(SkillUtils.getSkill("hunt.guard"));
			if (level == 0) return;
			
			double percent = 10.0D + (level - 1) * 5;
			
			e.setDamage(e.getDamage() * (1 - percent / 100.0D));
		}
	}
	
	@EventHandler
	public void angelicBless(PlayerToggleSneakEvent e) {
		if (Util.getFlagValue(plugin.wgp, e.getPlayer().getLocation(), EfeFlag.SKILL) == State.DENY) return;
		
		if (e.isSneaking() && !e.getPlayer().isFlying() && !DelayUtils.isDelayed(e.getPlayer(), "hunt.angelic-bless")) {
			UserData data = new UserData(e.getPlayer());
			
			int level = data.getLevel(SkillUtils.getSkill("hunt.angelic-bless"));
			if (level == 0) return;
			
			if (e.getPlayer().hasMetadata("angelic-bless_sneak")) {
				e.getPlayer().removeMetadata("angelic-bless_sneak", plugin);
				e.getPlayer().setMetadata("angelic-bless_sneak", new FixedMetadataValue(plugin, null));
				
				HashMap<PotionEffectType, String> map = this.angelicBlessEffects.get(level - 1);
				
				for (Player p : e.getPlayer().getWorld().getPlayers()) {
					if (p.getLocation().distance(e.getPlayer().getLocation()) > 4.0D) continue;
					
					String message = "§a▒§r " + (p.equals(e.getPlayer()) ? "자신의 " : p.getName()+"님의 ");
					List<PotionEffectType> effects = new ArrayList<PotionEffectType>();
					
					for (PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
						if (map.containsKey(effect.getType())) {
							message += map.get(effect.getType()) + ", ";
							effects.add(effect.getType());
						}
					}
					
					if (!effects.isEmpty()) {
						for (PotionEffectType effect : effects) {
							e.getPlayer().removePotionEffect(effect);
						}
						
						message = message.substring(0, message.length() - 2) + " 효과를 제거했습니다!";
						e.getPlayer().sendMessage(message);
						
						plugin.util.playSoundAll(e.getPlayer(), Sound.ORB_PICKUP, 1.0F);
						ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.5F, 10, p.getLocation().add(0, 1, 0), 32);
					}
				}
				
				
				plugin.util.playSoundAll(e.getPlayer(), Sound.GHAST_FIREBALL, 1.5F);
				plugin.util.playSoundAll(e.getPlayer(), Sound.LEVEL_UP, 0.75F);
				ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.5F, 10, e.getPlayer().getLocation().add(0, 1, 0), 32);
				ParticleEffect.ENCHANTMENT_TABLE.display(0.0F, 0.0F, 0.0F, 0.5F, 30, e.getPlayer().getLocation().add(0, 1, 0), 32);
				
				Scoreboarder.message(e.getPlayer(), new String[]{"§a§l>>§a 엔젤릭 블레스", "§c딜레이 3분"}, 2);
				DelayUtils.delay(e.getPlayer(), "hunt.angelic-bless", "엔젤릭 블레스", 180);
			} else {
				e.getPlayer().setMetadata("angelic-bless_sneak", new FixedMetadataValue(plugin, null));
				
				delay(e.getPlayer());
			}
		}
	}
	
	public void delay(final Player p) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (p == null || !p.hasMetadata("angelic-bless_sneak")) return;
				
				p.removeMetadata("angelic-bless_sneak", plugin);
			}
		}, 10L);
	}
}