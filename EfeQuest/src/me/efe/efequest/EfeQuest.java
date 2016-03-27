package me.efe.efequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.efe.efegear.EfeUtil;
import me.efe.efequest.gui.QuestGUI;
import me.efe.efequest.gui.QuesterGUI;
import me.efe.efequest.quest.QuestLoader;
import me.efe.efequest.quest.UserData;
import me.efe.fishkg.events.PlayerFishkgEvent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.ParticleEffect;

public class EfeQuest extends JavaPlugin implements Listener {
	public EfeUtil util;
	public HashMap<UUID, List<NPC>> particlesPos = new HashMap<UUID, List<NPC>>();
	public HashMap<UUID, List<NPC>> particlesEnd = new HashMap<UUID, List<NPC>>();
	
	private static QuesterGUI questerGui;
	public QuestGUI questGui;
	
	public EffectManager em;
	
	@Override
	public void onDisable() {
		util.logDisable();
	}
	
	@Override
	public void onEnable() {
		util = new EfeUtil(this);
		util.logEnable();
		
		questerGui = new QuesterGUI(this);
		questGui = new QuestGUI(this);
		util.register(this);
		util.register(questerGui);
		util.register(questGui);
		
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(QuesterTrait.class).withName("EfeQuester"));
		
		QuestLoader.init(this);
		
		em = new EffectManager(EffectLib.instance());
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (UUID id : particlesPos.keySet()) {
					Player p = getServer().getPlayer(id);
					
					if (p == null) continue;
					
					for (NPC npc : particlesPos.get(p.getUniqueId())) {
						Location loc = npc.getStoredLocation().clone().add(0, 1, 0);
						
						if (p.getWorld() != loc.getWorld() || loc.distance(p.getLocation()) > 32) continue;
						
						ParticleEffect.VILLAGER_HAPPY.display(0.3F, 0.3F, 0.3F, 0.0F, 10, loc, p);
					}
				}
				
				for (UUID id : particlesEnd.keySet()) {
					Player p = getServer().getPlayer(id);
					
					if (p == null) continue;
					
					for (NPC npc : particlesEnd.get(p.getUniqueId())) {
						Location loc = npc.getStoredLocation().clone().add(0, 1, 0);
						
						if (p.getWorld() != loc.getWorld() || loc.distance(p.getLocation()) > 32) continue;
						
						ParticleEffect.FIREWORKS_SPARK.display(0.05F, 0.05F, 0.05F, 0.075F, 20, loc, p);
					}
				}
			}
		}, 100L, 20L);
	}
	
//	public void refreshParticles() {
//		particlesPos.clear();
//		particlesEnd.clear();
//		
//		for (NPC npc : CitizensAPI.getNPCRegistry()) {
//			if (!npc.hasTrait(QuesterTrait.class)) continue;
//			
//			for (Player p : getServer().getOnlinePlayers()) {
//				UserData data = QuestLoader.getUserData(p);
//				
//				if (!particlesPos.containsKey(p.getUniqueId()))
//					particlesPos.put(p.getUniqueId(), new ArrayList<NPC>());
//				if (!pSarticlesEnd.containsKey(p.getUniqueId()))
//					particlesEnd.put(p.getUniqueId(), new ArrayList<NPC>());
//				
//				if (containsNPCPossible(p, data, npc)) {
//					particlesPos.get(p.getUniqueId()).add(npc);
//				} else if (containsNPCEndable(p, data, npc)) {
//					particlesEnd.get(p.getUniqueId()).add(npc);
//				}
//			}
//		}
//	}
	
	public void refreshParticles(final Player p) {
		if (p.hasMetadata("async_questparticles"))
			return;
		
		p.setMetadata("async_questparticles", new FixedMetadataValue(this, ""));
		
		getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				UUID id = p.getUniqueId();
				UserData data = QuestLoader.getUserData(p);
				
				if (particlesPos.containsKey(id))
					particlesPos.remove(id);
				if (particlesEnd.containsKey(id))
					particlesEnd.remove(id);
				
				for (NPC npc : CitizensAPI.getNPCRegistry()) {
					if (!npc.hasTrait(QuesterTrait.class)) continue;
					
					if (!particlesPos.containsKey(id))
						particlesPos.put(id, new ArrayList<NPC>());
					if (!particlesEnd.containsKey(id))
						particlesEnd.put(id, new ArrayList<NPC>());
					
					if (containsNPCPossible(p, data, npc)) {
						particlesPos.get(id).add(npc);
					} else if (containsNPCEndable(p, data, npc)) {
						particlesEnd.get(id).add(npc);
					}
				}
				
				if (p == null || !p.isOnline()) {
					if (particlesPos.containsKey(id))
						particlesPos.remove(id);
					if (particlesEnd.containsKey(id))
						particlesEnd.remove(id);
				}
				
				p.removeMetadata("async_questparticles", EfeQuest.this);
			}
		});
	}
	
	private boolean containsNPCPossible(Player p, UserData data, NPC npc) {
		for (int id : data.getPossibleQuests()) {
			if (QuestLoader.canStart(p, QuestLoader.getQuest(id)) && QuestLoader.getQuest(id).getStartNPC() == npc.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean containsNPCEndable(Player p, UserData data, NPC npc) {
		for (int id : data.getAcceptedQuests()) {
			if (QuestLoader.isEndable(p, QuestLoader.getQuest(id)) && QuestLoader.getQuest(id).getQuitNPC() == npc.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		refreshParticles(e.getPlayer());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		if (particlesPos.containsKey(e.getPlayer().getUniqueId()))
			particlesPos.remove(e.getPlayer().getUniqueId());
		if (particlesEnd.containsKey(e.getPlayer().getUniqueId()))
			particlesEnd.remove(e.getPlayer().getUniqueId());
	}
	
	public static void openQuesterGUI(Player p, NPC npc) {
		questerGui.openGUI(p, npc);
	}
	
	@EventHandler
	public void fish(PlayerFishkgEvent e) {
		QuestLoader.achieveGoal(e.getPlayer(), "FISH");
		QuestLoader.achieveGoal(e.getPlayer(), "FISH_TYPED", e.getFish().getName());
		QuestLoader.achieveGoal(e.getPlayer(), "FISH_LENGTH", e.getFish().getLength());
	}
	
//	@EventHandler
//	public void regionEnter(RegionEnterEvent e) {
//		QuestLoader.achieveGoal(e.getPlayer(), "REGION_ENTER", e.getRegion().getId());
//	}
	
	@EventHandler
	public void kill(EntityDeathEvent e) {
		if (util.getKiller(e) instanceof Player) {
			Player p = (Player) util.getKiller(e);
			
			QuestLoader.achieveGoal(p, "KILL", e.getEntityType().name());
		}
	}
	
	@EventHandler
	public void kill(MythicMobDeathEvent e) {
		if (getKiller(e) instanceof Player) {
			Player p = (Player) getKiller(e);
			
			QuestLoader.achieveGoal(p, "KILL", e.getMobType().getInternalName());
		}
	}
	
	@EventHandler
	public void killedBy(PlayerDeathEvent e) {
		LivingEntity entity = util.getKiller(e);
		
		if (entity == null)
			return;
		
		QuestLoader.achieveGoal(e.getEntity(), "KILLED_BY", entity.getType().name());
	}
	
	private LivingEntity getKiller(MythicMobDeathEvent e) {
		if (!(e.getLivingEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)) return null;
		
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e.getLivingEntity().getLastDamageCause();
		Entity damager = event.getDamager();
		
		if (damager instanceof LivingEntity) return (LivingEntity) damager;
		if (damager instanceof Projectile) {
			Projectile proj = (Projectile) damager;
			
			if (proj.getShooter() != null) {
				return (LivingEntity) proj.getShooter();
			}
		}
		
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("퀘스트")) {
			try {
				questGui.openGUI((Player) s);
			} catch (Exception e) {
				s.sendMessage("§c▒§r 잘못된 명령어입니다.");
				e.printStackTrace();
			}
		} else if (l.equalsIgnoreCase("efequest")) {
			if (!s.isOp()) return false;
			
			if (a.length == 0) {
				s.sendMessage("§a▒§r /efequest <name>");
			} else {
				Player p = (Player) s;
				
				if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR)
					QuestLoader.setItem(a[0], null);
				else
					QuestLoader.setItem(a[0], p.getItemInHand().clone());
				
				s.sendMessage("§a▒§r 아이템 등록이 완료되었습니다.");
			}
		}
		return false;
	}
}