package eu.playerunion.Aldas.system.blessings;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.playerunion.Aldas.system.Blessing;

public class Regeneration extends Blessing {
	
	private static String id = "regeneracio";
	private static String name = "Regeneráció";
	
	public Regeneration() {
		super(id, name);
	}
	
	public void fire() {
		Bukkit.getOnlinePlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2010, 0)));
	}
	
	public void extinguish() {
		Bukkit.getOnlinePlayers().forEach(p -> p.removePotionEffect(PotionEffectType.REGENERATION));
	}

}
