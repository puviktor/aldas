package eu.playerunion.Aldas.system.workers;

import org.bukkit.scheduler.BukkitRunnable;

import eu.playerunion.Aldas.Main;
import eu.playerunion.Aldas.system.Blessing;

public class BlessingTask extends BukkitRunnable {
	
	private int timer;
	private Blessing blessing;
	private Main main = Main.getInstance();
	
	public BlessingTask(Blessing b, int timer){
		this.blessing = b;
		this.timer = timer;
		
		b.setStatus(BlessingStatus.ACTIVE);
	}
	
	public void run() {
		this.timer --;
		
		this.blessing.fire();
		
		if(timer == 0) {
			blessing.extinguish();
			
			this.main.getBlessingManager().stop(this.blessing);
		}
	}
	
	public int getRemainingTime() {
		return this.timer;
	}
	
	public Blessing getBlessing() {
		return this.blessing;
	}

}
