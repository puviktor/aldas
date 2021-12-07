package eu.playerunion.Aldas.system.workers;

import org.bukkit.scheduler.BukkitRunnable;

import eu.playerunion.Aldas.Main;

public class BlessingCacheTask extends BukkitRunnable {
	
	private Main instance = Main.getInstance();
	
	public void run() {
		instance.getBlessingManager().saveRunningBlessings();
	}

}
