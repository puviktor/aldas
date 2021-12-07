package eu.playerunion.Aldas.system.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.playerunion.Aldas.Main;
import eu.playerunion.Aldas.system.Blessing;
import me.clip.placeholderapi.PlaceholderAPI;

public class BlessingGui {
	
	private Main main = Main.getInstance();
	private int updateTaskId = 0;
	
	public void openGui(Player holder) {
		Inventory inv = Bukkit.createInventory(holder, 9, "§bAktiválj Áldásokat!");
		
		this.update(holder, inv);
		
		holder.openInventory(inv);
		
		Runnable updateTask = () -> {
			if(inv.getViewers().size() == 0)
				Bukkit.getScheduler().cancelTask(this.updateTaskId);
			
			BlessingGui.this.update(holder, inv);
		};
		
		this.updateTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(this.main, updateTask, 0L, 100L).getTaskId();
	}
	
	private void update(Player holder, Inventory inv) {
		inv.clear();
		
		Material fillMaterial = Material.GRAY_STAINED_GLASS_PANE;
		Material blessingMaterial = Material.GRAY_STAINED_GLASS_PANE;
		
		for(GuiEntry element : this.main.getBlessingManager().getGuiElements()) {
			int price = this.main.getConfig().getInt("aldasok.arazas." + element.getBlessing().getID());
			blessingMaterial = this.main.getBlessingManager().getRunningBlessings().containsKey(element.getBlessing()) ? Material.RED_STAINED_GLASS_PANE : Material.LIGHT_BLUE_STAINED_GLASS_PANE;
			ItemStack item = new ItemStack(blessingMaterial, 1);
			ItemMeta meta = item.getItemMeta();
			
			meta.setDisplayName("§b" + element.getBlessing().getName());
			meta.setLore(Arrays.asList("§7Aktiválj egy §b" + element.getBlessing().getName() + " §7Áldást!",
					"§7Az Áldás §b1 óráig §7tart!",
					"§0",
					"§7Az Áldás aktiválásához §b" + price + " §7szükséges."));
			
			if(this.main.getBlessingManager().getRunningBlessings().containsKey(element.getBlessing())) {
				meta.setLore(Arrays.asList("§cEz az Áldás jelenleg is aktív!",
						"§7Amennyiben aktiválni szeretnéd,",
						"§7várd meg, míg lejárt!",
						"§0",
						"§c" + PlaceholderAPI.setPlaceholders(holder, "%aldas_" + element.getBlessing().getID() + "_ido%")));
			}
			
			item.setItemMeta(meta);
			
			inv.setItem(element.getPosition(), item);
		}
		
		for(int i = 0; i < inv.getSize(); i++)
			if(inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR)
				inv.setItem(i, new ItemStack(fillMaterial));
	}

}
