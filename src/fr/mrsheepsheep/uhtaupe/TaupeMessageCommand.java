package fr.mrsheepsheep.uhtaupe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TaupeMessageCommand implements CommandExecutor {

	UHTaupe plugin;

	public TaupeMessageCommand(UHTaupe plugin){
		this.plugin = plugin;
		plugin.getCommand("tm").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player){
			Player p = (Player) sender;
			if (plugin.uhgm.isGameStarted()){
				if (args.length > 0){
					if (plugin.taupes.contains(p.getName()) || (plugin.uhtm.isTeamRegistered("Taupes") && plugin.uhtm.getTeamForPlayer(p).getName().equalsIgnoreCase("Taupes"))){
						String msg = buildMessage(args, 0);
						for (String name : plugin.taupes){
							plugin.getServer().getPlayer(name).sendMessage(ChatColor.GRAY + "(Taupe) " + plugin.uhtm.getTeamForPlayer(p).getDisplayName() + " > " + msg);
						}
					}
				}
			}
		}
		return true;
	}



	public String buildMessage(String[] input, int startArg) {
		if(input.length - startArg <= 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder(input[startArg]);
		for(int i = ++startArg; i < input.length; i++) {
			sb.append(' ').append(input[i]);
		}
		return sb.toString();
	}
}