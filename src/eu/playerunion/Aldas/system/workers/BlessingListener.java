package eu.playerunion.Aldas.system.workers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import eu.playerunion.Aldas.Main;
import eu.playerunion.Aldas.system.gui.GuiEntry;

public class BlessingListener implements Listener {
	
	private Main main = Main.getInstance();
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		if(e.getView().getTitle().equals("§bAktiválj Áldásokat!")) {
			e.setCancelled(true);
			
			for(GuiEntry element : this.main.getBlessingManager().getGuiElements()) {
				if(element.getPosition() == e.getSlot()) {
					p.chat("/aldas " + element.getBlessing().getName());
					
					return;
				}
			}
		}
	}

}
