package fr.mrsheepsheep.uhtaupe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.azenet.UHPlugin.UHGameManager;
import me.azenet.UHPlugin.UHPlugin;
import me.azenet.UHPlugin.UHTeamManager;

public class UHTaupe extends JavaPlugin implements Listener, CommandExecutor {

	UHPlugin uhp;
	UHTeamManager uhtm;
	UHGameManager uhgm;

	List<String> taupes;
	List<String> offtaupe;

	int episode;
	
	
	public void onEnable(){
		uhp = (UHPlugin) getServer().getPluginManager().getPlugin("UHPlugin");
		uhtm = uhp.getTeamManager();
		uhgm = uhp.getGameManager();

		new Listeners(this);
		new RevealCommand(this);
		new TaupeMessageCommand(this);
		
		taupes = new ArrayList<String>();
		offtaupe = new ArrayList<String>();
		
		loadConfig();
		
	}
	
	public void loadConfig(){
		FileConfiguration c = getConfig();
		c.options().copyDefaults(true);
		saveConfig();
		episode = c.getInt("episode");
	}
}
