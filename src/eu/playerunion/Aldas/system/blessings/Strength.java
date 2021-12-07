package eu.playerunion.Aldas.system.blessings;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.playerunion.Aldas.system.Blessing;

public class Strength extends Blessing {
	
	private static String id = "ero";
	private static String name = "Erő";
	
	public Strength() {
		super(id, name);
	}
	
	public void fire() {
		Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2010, 0)));
	}
	
	public void extinguish() {
		Bukkit.getOnlinePlayers().forEach(p -> p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE));
	}

}
