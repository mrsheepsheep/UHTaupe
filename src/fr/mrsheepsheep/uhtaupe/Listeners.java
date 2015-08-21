package fr.mrsheepsheep.uhtaupe;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.azenet.UHPlugin.UHTeam;
import me.azenet.UHPlugin.events.UHEpisodeChangedEvent;
import me.azenet.UHPlugin.events.UHGameStartsEvent;

public class Listeners implements Listener {

	UHTaupe plugin;
	Random random;
	
	public Listeners(UHTaupe plugin){
		this.plugin = plugin;
		random = new Random();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onEpisodeStart(UHEpisodeChangedEvent e){
		if (e.getNewEpisode() == plugin.episode && plugin.episode != 1){
			spawnTaupes();
		}
	}
	
	@EventHandler
	public void onGameStart(UHGameStartsEvent e){
		if (plugin.episode <= 1){
			spawnTaupes();
		}
	}

	@EventHandler
	public void onTaupeDeath(PlayerDeathEvent e){
		Player p = e.getEntity();
		if (!plugin.uhgm.isPlayerDead(p)){
			if (plugin.taupes.contains(p.getName())){
				if (!plugin.uhtm.getTeam("Taupes").containsPlayer(p)){
					plugin.getServer().broadcastMessage(ChatColor.AQUA + p.getName() + " était une taupe!");
					plugin.taupes.remove(p.getName());
					if (plugin.taupes.isEmpty()){
						plugin.getServer().broadcastMessage(ChatColor.GREEN + "Les taupes ont été vaincues!");
					}
				}
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if (!plugin.uhgm.isPlayerDead(e.getPlayer()) && plugin.offtaupe.contains(e.getPlayer().getName())){
			plugin.offtaupe.remove(e.getPlayer().getName());
			sendTaupeMsg(e.getPlayer());
		}
	}
	
	public void spawnTaupes(){
		plugin.getServer().broadcastMessage(ChatColor.RED + "Une taupe a été désignée dans chaque équipe... Soyez sur vos gardes!");
		for (UHTeam team : plugin.uhgm.getAliveTeams()){
			Set<OfflinePlayer> sop = team.getPlayers();
			List<OfflinePlayer> lop = new LinkedList<OfflinePlayer>(sop);

			int r = randInt(0, lop.size()-1);

			OfflinePlayer op = lop.get(r);
			plugin.taupes.add(op.getName());

			if (op.isOnline()){
				sendTaupeMsg(op.getPlayer());
			}
			else
				plugin.offtaupe.add(op.getName());
		}
	}
	
	public void sendTaupeMsg(Player p){
		p.sendMessage(ChatColor.GREEN + "Vous êtes la taupe!");
		p.sendMessage(ChatColor.AQUA + "Vous pouvez taper " + ChatColor.WHITE + "/reveal " + ChatColor.AQUA + "à n'importe quel moment pour dévoiler votre identité à tout le monde et rejoindre l'équipe des taupes.");
		p.sendMessage(ChatColor.YELLOW + "Tapez /tm <message> pour envoyer un message à toutes les taupes anonymement. Seule la couleur de votre équipe sera affichée.");	
	}
	

	public int randInt(int min, int max) {
		int randomNum = random.nextInt((max - min) + 1) + min;
		return randomNum;
	}
}
