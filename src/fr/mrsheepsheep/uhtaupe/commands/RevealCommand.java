package fr.mrsheepsheep.uhtaupe.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mrsheepsheep.uhtaupe.UHTaupe;
import me.azenet.UHPlugin.TeamColor;
import me.azenet.UHPlugin.UHTeam;

public class RevealCommand implements CommandExecutor {
	
	UHTaupe plugin;
	
	public RevealCommand(UHTaupe plugin) {
		this.plugin = plugin;
		plugin.getCommand("reveal").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player){
			Player p = (Player) sender;
				if (plugin.uhgm.isGameStarted()){
					if (plugin.taupes.contains(p.getName()) && plugin.uhtm.getTeamForPlayer(p).getName() != "Taupes"){
						plugin.getServer().broadcastMessage(ChatColor.AQUA + p.getName() + " avoue �tre une taupe!");
						if (!plugin.uhtm.isTeamRegistered("Taupes"))
							plugin.uhtm.addTeam(new UHTeam("Taupes", TeamColor.GRAY, plugin.uhp));
						plugin.uhtm.addPlayerToTeam("Taupes", p);
					}
				}
		}
		
		return true;
	}
}
