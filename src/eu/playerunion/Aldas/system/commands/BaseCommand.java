package eu.playerunion.Aldas.system.commands;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import eu.playerunion.Aldas.Main;
import eu.playerunion.Aldas.system.Blessing;
import eu.playerunion.Aldas.system.BlessingManager;
import eu.playerunion.Aldas.system.gui.BlessingGui;
import net.md_5.bungee.api.ChatColor;

public class BaseCommand implements CommandExecutor, TabExecutor {
	
	private Main main = Main.getInstance();
	private BlessingManager blessingMan = main.getBlessingManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command label, String cmd, String[] args) {
		if(!sender.hasPermission("aldas.jog")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.prefix") + main.getConfig().getString("uzenetek.nincs_jogosultsagod")));
			
			return true;
		}
		
		if(args.length == 0) {
			sender.sendMessage("§6Jelenleg elérhető Áldások:");
			
			this.blessingMan.getRegisteredBlessings().forEach(b -> sender.sendMessage("§7 - §e" + b.getName()));
			
			sender.sendMessage("\n§7» §6Aktiválj egy Áldást a §e/aldas <név> §6paranccsal!");
			
			return true;
		}
		
		if(args.length == 1) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				
				if(args[0].equalsIgnoreCase("menu")) {
					new BlessingGui().openGui(p);
					
					p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("info")) {
					sender.sendMessage("Áldás v" + this.main.getDescription().getVersion());
					sender.sendMessage("Fejlesztő: szviktor (PlayerUnion Developer Team)");
					sender.sendMessage("GitHub: https://github.com/puviktor/aldas");
					
					return true;
				}
				
				if(main.getBlessingManager().getBlessingByName(args[0]) == null) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.prefix") + main.getConfig().getString("uzenetek.ismeretlen_aldas")));
					
					return true;
				}
				
				Blessing b = main.getBlessingManager().getBlessingByName(args[0]);
				
				if(!p.hasPermission("aldas.admin")) {
					int price = main.getConfig().getInt("bellitasok.arazas." + b.getID());
					
					if(main.getEconomy().getBalance(p) < price) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.prefix") + main.getConfig().getString("üzenetek.nincs_eleg_egyenleged")));
						
						return true;
					}
					
					main.getEconomy().withdrawPlayer(p, price);
				}
			}
			
			this.blessingMan.start(sender, args[0]);
		}
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command c, String cmd, String[] args) {
		ArrayList<String> tabComplete = new ArrayList<String>();
		
		if(cmd.equalsIgnoreCase("aldas")) {
			if(args.length == 1) {
				if(main.getConfig().getBoolean("beallitasok.automatikusKitoltes")) {
					ArrayList<String> blessings = new ArrayList<String>();
					
					this.blessingMan.getRegisteredBlessings().forEach(b -> blessings.add(b.getName()));
					
					Collections.sort(blessings, Collator.getInstance());
					
					tabComplete.addAll(blessings);
					tabComplete.add("menu");
				}
			}
		}
		return tabComplete;
	}

}
