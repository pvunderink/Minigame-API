package swordman.minigame.api;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import swordman.minigame.api.arena.ArenaHandler;
import swordman.minigame.api.event.BukkitEvents;
import swordman.minigame.api.player.PlayerHandler;
import swordman.minigame.api.timer.UpdateTimer;

/**
 * 
 * MinigameAPI
 * 
 * @author swordman407
 * 
 */
public class MinigameAPI extends JavaPlugin {

	Config config;

	/**
	 * Fired when the plugin enables
	 */
	public void onEnable() {
		config = new Config();

		// Read config
		this.getLogger().log(Level.INFO, "Reading config...");
		config.load();
		
		// Load arenas and groups
		this.getLogger().log(Level.INFO, "Loading arenas...");
		ArenaHandler.loadAllFromConfig();
		
		// Register events
		getServer().getPluginManager().registerEvents(new BukkitEvents(), this);
		
		// Start tasks
		getServer().getScheduler().runTaskTimer(this, new UpdateTimer(), 2L, 10L);
		
		this.getLogger().log(Level.INFO, "Has been enabled");
	}

	/**
	 * Fired when the plugin disables
	 */
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayerHandler.leaveArena(p);
		}
		
		if (config.saveArenasOnDisable) {
			this.getLogger().log(Level.INFO, "Saving arenas...");
			ArenaHandler.saveAllToConfig();
		}
		this.getLogger().log(Level.INFO, "Has been disabled");
	}

}
