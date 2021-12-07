package eu.playerunion.Aldas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import eu.playerunion.Aldas.system.BlessingManager;
import eu.playerunion.Aldas.system.commands.BaseCommand;
import eu.playerunion.Aldas.system.workers.BlessingCacheTask;
import eu.playerunion.Aldas.system.workers.BlessingJarLoader;
import eu.playerunion.Aldas.system.workers.BlessingListener;
import eu.playerunion.Aldas.system.workers.PlaceholderWorker;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private File config = new File(this.getDataFolder(), "config.yml");
	private BlessingManager blessingMan;
	private BlessingJarLoader jarLoader;
	private Economy economy;
	
	public Main() {
		instance = this;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public void setupEconomy() {
		if(Bukkit.getPluginManager().getPlugin("Vault") == null) {
			this.getLogger().warning("A Vault hook nem tudott létrejönni, a plugin leállítja magát...");
			
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
		this.economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
		
		this.getLogger().info("A Vault (Economy) hook sikeresen éltre kelt!");
	}
	
	public void onEnable() {
		instance = this;
		
		this.getLogger().info("A plugin betöltése megkezdődött!");
		this.blessingMan = new BlessingManager();
		this.jarLoader = new BlessingJarLoader();
		
		this.getLogger().info("Konfiguráció betöltése...");
		this.saveDefaultConfig();
		
		this.getLogger().info("Parancsok beregisztrálása...");
		this.getCommand("aldas").setExecutor(new BaseCommand());
		
		this.getLogger().info("Áldások beregisztrálása...");
		this.blessingMan.registerBlessings();
		
		this.getLogger().info("Feladatok elindítása...");
		this.setupEconomy();
		
		Bukkit.getPluginManager().registerEvents(new BlessingListener(), this);
		
		new BlessingCacheTask().runTaskTimerAsynchronously(this, 6000L, 6000L);
		
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
			boolean bool = new PlaceholderWorker().register();
			
			this.getLogger().info("A PlaceholderAPI kapcsolat: " + bool);
		}
		
		if(this.getConfig().getBoolean("beallitasok.kulsoAldasok"))
			this.blessingMan.loadExternalJars();
		
		this.getLogger().info("Cacheben lévé Áldások indítása...");
		
		try {
			this.blessingMan.loadCachedBlessings();
		} catch (Exception e) {
			this.getLogger().severe("Nem sikerült betölteni a cachet: " + e.getMessage());
		}
		
		this.blessingMan.reloadGuiElements();
		this.blessingMan.registerPlaceholders();
		
		this.getLogger().info("A plugin betöltése kész!");
	}
	
	public void onDisable() {
		this.getLogger().info("A plugin leállítása megkezdődött...");
		
		this.blessingMan.saveRunningBlessings();
		
		this.getLogger().info("A plugin sikeresen leállt!");
	}
	
	public BlessingManager getBlessingManager() {
		return this.blessingMan;
	}
	
	public BlessingJarLoader getJarLoader() {
		return this.jarLoader;
	}
	
	public Economy getEconomy() {
		return this.economy;
	}

}
