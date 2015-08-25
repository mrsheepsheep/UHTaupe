package fr.mrsheepsheep.uhtaupe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mrsheepsheep.uhtaupe.commands.KitCommand;
import fr.mrsheepsheep.uhtaupe.commands.RevealCommand;
import fr.mrsheepsheep.uhtaupe.commands.TaupeMessageCommand;
import me.azenet.UHPlugin.UHGameManager;
import me.azenet.UHPlugin.UHPlugin;
import me.azenet.UHPlugin.UHTeamManager;

public class UHTaupe extends JavaPlugin implements Listener, CommandExecutor {

	public UHPlugin uhp;
	public UHTeamManager uhtm;
	public UHGameManager uhgm;

	public List<String> taupes; // Liste des taupes
	public List<String> offtaupe; // Liste des taupes qui ne le savent pas encore et qui sont déconnectées
	public List<String> offautoreveal; // Liste des taupes déconnectées qui n'ont pas encore autoreveal

	public int episode;
	public int autoreveal; 
	
	public List<ItemStack> kit;
	
	public void onEnable(){
		uhp = (UHPlugin) getServer().getPluginManager().getPlugin("UHPlugin");
		uhtm = uhp.getTeamManager();
		uhgm = uhp.getGameManager();

		new Listeners(this);
		new RevealCommand(this);
		new TaupeMessageCommand(this);
		new KitCommand(this);
		
		taupes = new ArrayList<String>();
		offtaupe = new ArrayList<String>();
		offautoreveal = new ArrayList<String>();
		kit = new ArrayList<ItemStack>();
		
		loadConfig();
		
	}
	
	public void loadConfig(){
		FileConfiguration c = getConfig();
		c.options().copyDefaults(true);
		saveConfig();
		episode = c.getInt("episode");
		autoreveal = c.getInt("autoreveal");
		
		List<String> items = c.getStringList("kit");
		for (String i : items){
			String[] item = i.split(" ");
			String nameid = item[0].toUpperCase();
			String data = item[1];
			String amount = item[2];
			
			ItemStack is = new ItemStack(Material.valueOf(nameid), Integer.valueOf(amount), Short.valueOf(data));
			kit.add(is);
		}
	}
}
