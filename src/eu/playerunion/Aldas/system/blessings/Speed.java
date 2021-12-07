package eu.playerunion.Aldas.system.blessings;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.playerunion.Aldas.system.Blessing;

public class Speed extends Blessing {
	
	private static String id = "gyorsasag";
	private static String name = "Gyorsaság";
	
	public Speed() {
		super(id, name);
	}
	
	public void fire() {
		Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2010, 1)));
	}
	
	public void extinguish() {
		Bukkit.getOnlinePlayers().forEach(p -> p.removePotionEffect(PotionEffectType.SPEED));
	}

}
