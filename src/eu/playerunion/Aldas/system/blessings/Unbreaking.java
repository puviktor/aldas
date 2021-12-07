package eu.playerunion.Aldas.system.blessings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.playerunion.Aldas.system.Blessing;

public class Unbreaking extends Blessing {
	
	private static String id = "torhetetlenseg";
	private static String name = "Törhetetlenség";
	
	public Unbreaking() {
		super(id, name);
	}
	
	public void fire() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getInventory().getArmorContents() != null) {
				for(ItemStack is : p.getInventory().getArmorContents()) {
					if(is != null && is.getType() != Material.AIR) {
						is.setDurability((short) 0);
					}
				}
			}
		}
	}
	
	public void extinguish() {
	}
	
}
