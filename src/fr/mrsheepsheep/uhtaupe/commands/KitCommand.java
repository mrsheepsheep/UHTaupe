package fr.mrsheepsheep.uhtaupe.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mrsheepsheep.uhtaupe.UHTaupe;

public class KitCommand implements CommandExecutor {

	UHTaupe plugin;
	List<String> kitplayer;

	public KitCommand(UHTaupe plugin){
		this.plugin = plugin;
		plugin.getCommand("kit").setExecutor(this);
		kitplayer = new ArrayList<String>();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player){
			Player p = (Player) sender;
			if (plugin.uhgm.isGameStarted()	&& plugin.taupes.contains(p.getName()) && !kitplayer.contains(p.getName())){
				kitplayer.add(p.getName());
				dropItems(p);
				p.sendMessage(ChatColor.GREEN + "Des objets sont tomb�s � vos pieds.");
			}
			else if (kitplayer.contains(p.getName()))
				p.sendMessage(ChatColor.RED + "Vous avez d�j� l�ch� les objets.");
			else if (!plugin.taupes.contains(p.getName()))
				p.sendMessage(ChatColor.RED + "Vous n'�tes pas une taupe!");
		}
		return true;
	}

	public void dropItems(Player p){
		for (ItemStack i : plugin.kit)
			p.getWorld().dropItem(p.getLocation(), i);
	}
}
