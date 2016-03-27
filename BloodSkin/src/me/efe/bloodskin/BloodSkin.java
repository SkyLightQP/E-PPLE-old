package me.efe.bloodskin;

import java.util.HashMap;

import me.efe.bloodskin.skins.Skin;
import me.efe.bloodskin.skins.SkinAngry;
import me.efe.bloodskin.skins.SkinApple;
import me.efe.bloodskin.skins.SkinAppleParty;
import me.efe.bloodskin.skins.SkinApplePartyGold;
import me.efe.bloodskin.skins.SkinBleed;
import me.efe.bloodskin.skins.SkinChicken;
import me.efe.bloodskin.skins.SkinColorful;
import me.efe.bloodskin.skins.SkinFish;
import me.efe.bloodskin.skins.SkinFlame;
import me.efe.bloodskin.skins.SkinFridge;
import me.efe.bloodskin.skins.SkinHeart;
import me.efe.bloodskin.skins.SkinMelon;
import me.efe.bloodskin.skins.SkinMineZ;
import me.efe.bloodskin.skins.SkinHead;
import me.efe.bloodskin.skins.SkinNote;
import me.efe.bloodskin.skins.SkinRose;
import me.efe.bloodskin.skins.SkinSnow;
import me.efe.bloodskin.skins.SkinTNT;
import me.efe.bloodskin.skins.SkinWool;
import me.efe.efegear.EfeUtil;
import me.efe.efeserver.PlayerData;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BloodSkin extends JavaPlugin {
	public EfeUtil util;
	public HashMap<String, Skin> skins = new HashMap<String, Skin>();
	
	public BloodListener bloodListener;
	public BloodSkinGUI bloodSkinGui;
	
	@Override
	public void onDisable() {
		util.logDisable();
	}
	
	@Override
	public void onEnable() {
		util = new EfeUtil(this);
		util.logEnable();
		
		this.bloodListener = new BloodListener(this);
		this.bloodSkinGui = new BloodSkinGUI(this);
		
		util.register(bloodListener);
		util.register(bloodSkinGui);
		
		initSkins();
	}
	
	public void initSkins() {
		Skin.init(this);
		
		skins.put("bleed", new SkinBleed());
		skins.put("minez", new SkinMineZ());
		skins.put("angry", new SkinAngry());
		skins.put("note", new SkinNote());
		skins.put("heart", new SkinHeart());
		skins.put("flame", new SkinFlame());
		skins.put("colorful", new SkinColorful());
		skins.put("snow", new SkinSnow());
		skins.put("apple", new SkinApple());
		skins.put("apple_party", new SkinAppleParty());
		skins.put("apple_party_gold", new SkinApplePartyGold());
		skins.put("rose", new SkinRose());
		skins.put("chicken", new SkinChicken());
		skins.put("tnt", new SkinTNT());
		skins.put("melon", new SkinMelon());
		skins.put("fish", new SkinFish());
		skins.put("head", new SkinHead());
		skins.put("wool", new SkinWool());
		skins.put("fridge", new SkinFridge());
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (l.equalsIgnoreCase("블러드스킨") || l.equalsIgnoreCase("블러드") || l.equalsIgnoreCase("스킨") || l.equalsIgnoreCase("bloodskin")) {
			try {
				if (a.length == 0) {
					Player p = (Player) s;
					
					this.bloodSkinGui.openGUI(p);
					return false;
				}
				
				
				if (!s.isOp()) return false;
				
				if (a[0].equals("추가")) {
					PlayerData data = PlayerData.get(util.getOfflinePlayer(a[1]).getUniqueId());
					
					data.addBloodSkin(skins.get(a[2]).getName());
					
					s.sendMessage("§a▒§r 추가 완료");
					return false;
				}
				
				if (a[0].equals("제거")) {
					PlayerData data = PlayerData.get(util.getOfflinePlayer(a[1]).getUniqueId());
					
					data.removeBloodSkin(skins.get(a[2]).getName());
					
					s.sendMessage("§a▒§r 제거 완료");
					return false;
				}
				
				if (a[0].equals("목록")) {
					if (a.length == 1) {
						s.sendMessage("------");
						
						for (String skin : skins.keySet()) {
							s.sendMessage(skin);
						}
						
						s.sendMessage("------");
						return false;
					}
					
					
					PlayerData data = PlayerData.get(util.getOfflinePlayer(a[1]).getUniqueId());
					
					s.sendMessage("------");
					
					for (int i = 0; i < data.getBloodSkins().size(); i ++) {
						s.sendMessage("§a▒§r ["+i+"] "+data.getBloodSkins().get(i));
					}
					
					s.sendMessage("------");
					s.sendMessage("§a▒§r Selected: "+data.getBloodSkin());
					return false;
				}
				
			} catch (Exception e) {
				s.sendMessage("§c▒§r Exception Occured: "+e.getMessage());
				e.printStackTrace();
			}
		}
		
		return false;
	}
}