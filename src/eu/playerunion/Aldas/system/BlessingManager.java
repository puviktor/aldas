package eu.playerunion.Aldas.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.json.JSONException;
import org.json.JSONObject;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;

import eu.playerunion.Aldas.Main;
import eu.playerunion.Aldas.TimeSerializer;
import eu.playerunion.Aldas.Util;
import eu.playerunion.Aldas.system.blessings.Regeneration;
import eu.playerunion.Aldas.system.blessings.Speed;
import eu.playerunion.Aldas.system.blessings.Strength;
import eu.playerunion.Aldas.system.blessings.Unbreaking;
import eu.playerunion.Aldas.system.gui.GuiEntry;
import eu.playerunion.Aldas.system.workers.BlessingStatus;
import eu.playerunion.Aldas.system.workers.BlessingTask;

public class BlessingManager {
	
	private Util util = new Util();
	private TimeSerializer timeSerialzer = new TimeSerializer();
	
	private ArrayList<Blessing> registeredBlessings = new ArrayList<Blessing>();
	private HashMap<Blessing, BlessingTask> runningBlessings = new HashMap<Blessing, BlessingTask>();
	private ArrayList<GuiEntry> guiElements = new ArrayList<GuiEntry>();
	
	public void registerBlessings() {
		this.registeredBlessings.clear();
		
		this.registeredBlessings.add(new Regeneration());
		this.registeredBlessings.add(new Strength());
		this.registeredBlessings.add(new Speed());
		this.registeredBlessings.add(new Unbreaking());
	}
	
	public void loadExternalJars() {
		Main.getInstance().getJarLoader().loadJarFiles();
		

		Main.getInstance().getJarLoader().getLoadedBlessings().forEach(b -> this.registeredBlessings.add(b));
		
		Main.getInstance().getLogger().info("Betöltött külső Áldások: " + Main.getInstance().getJarLoader().getLoadedBlessings().size());
	}
	
	public void reloadGuiElements() {
		this.guiElements.clear();
		
		int position = 0;
		Iterator<Blessing> blessingIter = this.registeredBlessings.iterator();
		
		while(blessingIter.hasNext()) {
			Blessing blessing = blessingIter.next();
			
			this.guiElements.add(new GuiEntry(position, blessing));
			
			position++;
		}
	}
	
	public void start(CommandSender starter, String name) {
		int duration = Main.getInstance().getConfig().getInt("beallitasok.ido");
		
		this.start(starter.getName(), name, duration);
	}
	
	public void start(String starter, String name, int duration) {
		Blessing b = null;
		
		for(Blessing blessing : this.registeredBlessings) {
			if(blessing.getName().equalsIgnoreCase(name)) {
				b = blessing;
				
				if(this.runningBlessings.containsKey(blessing)) {
					if(!b.getStarterName().equalsIgnoreCase(starter) || starter.equals("CONSOLE")) {
						if(!(Bukkit.getPlayer(starter) == null)) {
							Player p = Bukkit.getPlayer(starter);
							
							if(p.hasPermission("aldas.megszakit")) {
								this.stop(b);
								
								return;
							}
							
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("uzenetek.nem_megallithato")));
							
							return;
						}
					}
					
					if(!(Bukkit.getPlayer(starter) == null)) {
						Player p = Bukkit.getPlayer(starter);
						
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("uzenetek.folyamatban")));
						
						return;
					}
					
					return;
				}
				
				break;
			}
		}
		
		b.setStarter(starter);

		BlessingTask task = new BlessingTask(b, duration);
		task.runTaskTimer(Main.getInstance(), 0, 20L);
		
		this.runningBlessings.put(b, task);
		
		saveRunningBlessings();
		
		Player p = Bukkit.getPlayer(starter);
		
		if(Main.getInstance().getConfig().getBoolean("beallitasok.tuzijatek.allapot")) {
			Firework fw = (Firework) p.getLocation().getWorld().spawn(p.getLocation(), Firework.class);
		    FireworkMeta fm = fw.getFireworkMeta();
	        List<Color> c = new ArrayList<Color>();
	        
	        c.add(Color.NAVY);
	        c.add(Color.FUCHSIA);
	        c.add(Color.GREEN);
	        c.add(Color.BLUE);
	        c.add(Color.AQUA);
	        c.add(Color.RED);
	        
	        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(c).withFade(c).with(Type.BALL_LARGE).trail(true).build();
	        
	        fm.addEffect(effect);
	        fm.setPower(0);
	        fw.setFireworkMeta(fm);
		}
		
		Bukkit.getOnlinePlayers().forEach(ps -> ps.playSound(ps.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 2.0F));
		
		String startMsg = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("uzenetek.prefix") + Main.getInstance().getConfig().getString("uzenetek.start").replaceAll("%jatekos%", starter).replaceAll("%aldas%", b.getName()));
		String comment = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("uzenetek.prefix") + Main.getInstance().getConfig().getString("uzenetek.start_megjegyzes"));
		
		Bukkit.broadcastMessage(startMsg);
		Bukkit.broadcastMessage(comment);
	}
	
	public void resume(String starter, String name, int duration) {
		Blessing b = null;
		
		for(Blessing blessing : this.registeredBlessings) {
			if(blessing.getName().equalsIgnoreCase(name)) {
				b = blessing;
				
				break;
			}
		}
		
		b.setStarter(starter);

		BlessingTask task = new BlessingTask(b, duration);
		task.runTaskTimer(Main.getInstance(), 0, 20L);
		
		runningBlessings.put(b, task);
		
		String time = timeSerialzer.serialize(duration);
		
		Main.getInstance().getLogger().info(name + " Áldás folytatása... Hátralévő idő: " + time);
		
		String resume = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("uzenetek.prefix") + Main.getInstance().getConfig().getString("uzenetek.folytatas").replaceAll("%aldas%", b.getName()).replaceAll("%jatekos%", starter));
		String comment = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("uzenetek.prefix") + Main.getInstance().getConfig().getString("uzenetek.folytatas_megjegyzes").replaceAll("%ido%", time));
		
		Bukkit.broadcastMessage(resume);
		Bukkit.broadcastMessage(comment);
	}
	
	public void stop(Blessing b) {
		b.setStatus(BlessingStatus.INACTIVE);
		
		BlessingManager.this.runningBlessings.get(b).cancel();
		
		runningBlessings.remove(b);
		
		saveRunningBlessings();
		
		String expired = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("uzenetek.prefix") + Main.getInstance().getConfig().getString("uzenetek.lejart").replaceAll("%aldas%", b.getName()));
		
		Bukkit.broadcastMessage(expired);
	}
	
	public void saveRunningBlessings() {
		File file = new File(Main.getInstance().getDataFolder(), "blessingCache.json");
		JSONObject blessings = new JSONObject();
		
		Main.getInstance().getLogger().info("Cache mentése folyamatban...");
		
		try {
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					Main.getInstance().getLogger().severe("Nem sikerült az Áldás cache fájl létrehozása: " + e.getMessage());
				}
			}
			
			if(!this.runningBlessings.isEmpty()) {		
				for(Blessing b : this.runningBlessings.keySet()) {
					BlessingTask task = this.runningBlessings.get(b);
					JSONObject details = new JSONObject();
					
					details.put("taskId", task.getTaskId());
					details.put("starter", b.getStarterName());
					details.put("remainingTime", task.getRemainingTime());
					
					blessings.put(b.getName(), details);
				}
			}
			
			try {
				this.util.writeJSONFile(blessings, file);
				
				Main.getInstance().getLogger().info("Cache mentése sikeres volt!");
			} catch (Exception e) {
				Main.getInstance().getLogger().severe("Nem sikerült az Áldás cache kimentése: " + e.getMessage());
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void loadCachedBlessings() throws Exception {
		File file = new File(Main.getInstance().getDataFolder(), "blessingCache.json");
		
		if(!file.exists())
			this.util.writeJSONFile(new JSONObject(), file);
		
		JSONObject obj = this.util.readJSONFile(file);
		
		if(obj.length() == 0) {
			Main.getInstance().getLogger().info("Nem találtam egy Áldást sem a cacheben!");
			
			return;
		}
		
		Iterator<String> it = obj.keys();
		
		while(it.hasNext()) {
			String name = it.next();
			if(getBlessingByName(name) != null) {
				Blessing blessing = getBlessingByName(name);
				JSONObject details = obj.getJSONObject(name);
				String starter = details.getString("starter");
				int remaining = details.getInt("remainingTime");
				
				this.resume(starter, blessing.getName(), remaining);
				
				Main.getInstance().getLogger().info("Betöltött Áldás: " + blessing.getName());
			}
		}
	}
	
	public ArrayList<Blessing> getRegisteredBlessings() {
		return this.registeredBlessings;
	}
	
	public ArrayList<GuiEntry> getGuiElements() {
		return this.guiElements;
	}
	
	public HashMap<Blessing, BlessingTask> getRunningBlessings() {
		return this.runningBlessings;
	}
	
	public Blessing getBlessingByName(String name) {
		for(Blessing blessing : this.registeredBlessings)
			if(blessing.getName().equalsIgnoreCase(name))
				return blessing;
		
		return null;
	}
	
	public Blessing getBlessingByID(String id) {
		for(Blessing blessing : this.registeredBlessings)
			if(blessing.getID().equalsIgnoreCase(id))
				return blessing;
		
		return null;
	}
	
	public void registerPlaceholders() {
		for(Blessing b : this.registeredBlessings) {
			HologramsAPI.registerPlaceholder(Main.getInstance(), "{aldas_status_" + b.getID() + "}", 1, new PlaceholderReplacer() {
				
				@Override
				public String update() {
					return BlessingManager.this.runningBlessings.containsKey(b) ? b.getStatus().type : BlessingStatus.INACTIVE.type;
				}
				
			});
			
			HologramsAPI.registerPlaceholder(Main.getInstance(), "{aldas_ido_" + b.getID() + "}", 1, new PlaceholderReplacer() {
				
				@Override
				public String update() {
					TimeSerializer ts = new TimeSerializer();
					
					return BlessingManager.this.runningBlessings.containsKey(b) ? ts.serialize(BlessingManager.this.runningBlessings.get(b).getRemainingTime()) : "--:--:--";
				}
				
			});
		}
	}

}
