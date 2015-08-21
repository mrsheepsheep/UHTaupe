package fr.mrsheepsheep.uhtaupe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.azenet.UHPlugin.TeamColor;
import me.azenet.UHPlugin.UHGameManager;
import me.azenet.UHPlugin.UHPlugin;
import me.azenet.UHPlugin.UHTeam;
import me.azenet.UHPlugin.UHTeamManager;
import me.azenet.UHPlugin.events.UHEpisodeChangedEvent;
import me.azenet.UHPlugin.events.UHGameStartsEvent;

public class UHTaupe extends JavaPlugin implements Listener, CommandExecutor {

	UHPlugin uhp;
	UHTeamManager uhtm;
	UHGameManager uhgm;

	Random random;
	List<String> taupes;
	List<String> offtaupe;

	int episode;
	
	
	public void onEnable(){
		uhp = (UHPlugin) getServer().getPluginManager().getPlugin("UHPlugin");
		uhtm = uhp.getTeamManager();
		uhgm = uhp.getGameManager();

		getServer().getPluginManager().registerEvents(this, this);
		getCommand("reveal").setExecutor(this);
		random = new Random();
		taupes = new ArrayList<String>();
		offtaupe = new ArrayList<String>();
		
		loadConfig();
		
	}

	@EventHandler
	public void onEpisodeStart(UHEpisodeChangedEvent e){
		if (e.getNewEpisode() == episode && episode != 1){
			spawnTaupes();
		}
	}
	
	@EventHandler
	public void onGameStart(UHGameStartsEvent e){
		if (episode <= 1){
			spawnTaupes();
		}
	}

	@EventHandler
	public void onTaupeDeath(PlayerDeathEvent e){
		Player p = e.getEntity();
		if (!uhgm.isPlayerDead(p)){
			if (taupes.contains(p.getName())){
				if (!uhtm.getTeam("Taupes").containsPlayer(p)){
					getServer().broadcastMessage(ChatColor.AQUA + p.getName() + " était une taupe!");
					taupes.remove(p.getName());
					if (taupes.isEmpty()){
						getServer().broadcastMessage(ChatColor.GREEN + "Les taupes ont été vaincues!");
					}
				}
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if (!uhgm.isPlayerDead(e.getPlayer()) && offtaupe.contains(e.getPlayer().getName())){
			offtaupe.remove(e.getPlayer().getName());
			sendTaupeMsg(e.getPlayer());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player){
			Player p = (Player) sender;
			switch (cmd.getName()){
			default: break;
			case "reveal":
				if (uhgm.isGameStarted()){
					if (taupes.contains(p.getName()) && uhtm.getTeamForPlayer(p).getName() != "Taupes"){
						getServer().broadcastMessage(ChatColor.AQUA + p.getName() + " avoue être une taupe!");
						if (!uhtm.isTeamRegistered("Taupes")){
							uhtm.addTeam(new UHTeam("Taupes", TeamColor.GRAY, uhp));
						}
						uhtm.addPlayerToTeam("Taupes", p);
					}
				}
				break;
			case "tm":
				if (uhgm.isGameStarted()){
					if (args.length > 0){
						if (taupes.contains(p.getName()) || (uhtm.isTeamRegistered("Taupes") && uhtm.getTeamForPlayer(p).getName().equalsIgnoreCase("Taupes"))){
							String msg = buildMessage(args, 0);
							for (String name : taupes){
								getServer().getPlayer(name).sendMessage(ChatColor.GRAY + "(Taupe) " + uhtm.getTeamForPlayer(p).getDisplayName() + " > " + msg);
							}
						}
					}
				}
				break;
			}
		}
		return true;
	}

	public void sendTaupeMsg(Player p){
		p.sendMessage(ChatColor.GREEN + "Vous êtes la taupe!");
		p.sendMessage(ChatColor.AQUA + "Vous pouvez taper " + ChatColor.WHITE + "/reveal " + ChatColor.AQUA + "à n'importe quel moment pour dévoiler votre identité à tout le monde et rejoindre l'équipe des taupes.");
		p.sendMessage(ChatColor.YELLOW + "Tapez /tm <message> pour envoyer un message à toutes les taupes anonymement. Seule la couleur de votre équipe sera affichée.");	
	}
	
	public void spawnTaupes(){
		getServer().broadcastMessage(ChatColor.RED + "Une taupe a été désignée dans chaque équipe... Soyez sur vos gardes!");
		for (UHTeam team : uhgm.getAliveTeams()){
			Set<OfflinePlayer> sop = team.getPlayers();
			List<OfflinePlayer> lop = new LinkedList<OfflinePlayer>(sop);

			int r = randInt(0, lop.size()-1);

			OfflinePlayer op = lop.get(r);
			taupes.add(op.getName());

			if (op.isOnline()){
				sendTaupeMsg(op.getPlayer());
			}
			else
				offtaupe.add(op.getName());
		}
	}


	public int randInt(int min, int max) {
		int randomNum = random.nextInt((max - min) + 1) + min;
		return randomNum;
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
	
	public void loadConfig(){
		FileConfiguration c = getConfig();
		c.options().copyDefaults(true);
		saveConfig();
		episode = c.getInt("episode");
	}
}
