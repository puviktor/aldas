package eu.playerunion.Aldas.system.workers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;

import eu.playerunion.Aldas.Main;
import eu.playerunion.Aldas.TimeSerializer;
import eu.playerunion.Aldas.system.Blessing;
import eu.playerunion.Aldas.system.BlessingManager;

public class PlaceholderWorker extends PlaceholderExpansion {
	
	private Main main = Main.getInstance();
	private BlessingManager blessingMan = main.getBlessingManager();
	private TimeSerializer ts = new TimeSerializer();
	
	@Override
	public String getAuthor() {
		return "szviktor";
	}

	@Override
	public String getIdentifier() {
		return "aldas";
	}

	@Override
	public String getVersion() {
		return "v1.0";
	}
	
	public String onPlaceholderRequest(Player p, String id) {
		String response = "";
		String blessingId = id.split("_")[0];
		String mode = id.split("_")[1];
		
		Blessing b = this.blessingMan.getBlessingByID(blessingId);
		
		if(mode.equalsIgnoreCase("ido")) {
			if(!this.main.getBlessingManager().getRunningBlessings().containsKey(b)) {
				response = "Hátralévő idő: --:--:--";
				
				return response;
			}
			
			BlessingTask task = this.blessingMan.getRunningBlessings().get(b);
			
			b = task.getBlessing();
			
			response = "Hátralévő idő: " + this.ts.serialize(task.getRemainingTime());
			
			return response;
		}
		
		if(mode.equalsIgnoreCase("status")) {
			if(!this.main.getBlessingManager().getRunningBlessings().containsKey(b)) {
				response = "§fStátusz: " + BlessingStatus.INACTIVE.type;
				
				return response;
			}
			
			BlessingTask task = this.blessingMan.getRunningBlessings().get(b);
			
			b = task.getBlessing();
			
			response = "§fStátusz: " + b.getStatus().type;
			
			return response;
		}
		
		return response;
	}

}
