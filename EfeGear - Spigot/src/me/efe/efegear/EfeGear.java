package me.efe.efegear;

import me.efe.efegear.util.UpdateChecker;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 이 플러그인을 무단 수정하여 사용한다면
 * 크나큰 불이익이 생길 수 있습니다.
 * 
 * 만약 그것이 적발되면 당신의 서버는 더이상 제 플러그인을 사용할 수 없을 것입니다.
 * 
 */

public class EfeGear extends JavaPlugin {
	public EfeUtil util;
	
	@Override
	public void onEnable() {
		EfeUtil.initalize(this);
		
		util = new EfeUtil(this);
		util.register(new EfeListener(this));
		
		if (!getServer().getBukkitVersion().startsWith("1.7.10") && !getServer().getBukkitVersion().startsWith("1.8")) {
			for (int i = 0; i < 10; i ++) {
				util.console("§cEfeGear§r: Not Correct Version!");
				util.console("§cEfeGear§r: 1.5.2 Bukkit needs 'EfeGear v"+EfeUtil.getGearVersion()+" for 1.5");
				util.console("§cEfeGear§r: 1.6.2 ~ 1.6.4 Bukkit needs 'EfeGear v"+EfeUtil.getGearVersion()+" for 1.6");
				util.console("§cEfeGear§r: 1.7.2 Bukkit needs 'EfeGear v"+EfeUtil.getGearVersion()+" for 1.7.2");
				util.console("§cEfeGear§r: 1.7.9 Bukkit needs 'EfeGear v"+EfeUtil.getGearVersion()+" for 1.7.9");
				util.console("§cEfeGear§r: 1.7.10 ~ 1.8 Bukkit needs 'EfeGear v"+EfeUtil.getGearVersion()+" for Spigot");
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			getServer().shutdown();
			return;
		}
		
		double now = EfeUtil.getGearVersion();
		double newest = UpdateChecker.getVersion("EfeGear");
		if (newest > now) {
			for (int i = 0; i < 10; i ++) {
				util.console("§cEfeGear§r: §aNew Version Detected! v"+now+" → §lv"+newest);
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
	}
}