package swordman.minigame.api.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import swordman.minigame.api.event.ArenaStartEvent;
import swordman.minigame.api.event.ArenaStopEvent;
import swordman.minigame.api.event.PlayerJoinGroupEvent;
import swordman.minigame.api.event.PlayerJoinedGroupEvent;
import swordman.minigame.api.event.PlayerPotentiallyJoinGroupEvent;
import swordman.minigame.api.event.PlayerPotentiallyJoinedGroupEvent;
import swordman.minigame.api.group.Group;
import swordman.minigame.api.group.GroupHandler;
import swordman.minigame.api.player.PlayerHandler;
import swordman.minigame.api.util.ChatEncoder;
import swordman.minigame.api.util.GroupType;

/**
 * 
 * MinigameAPI
 * 
 * @author swordman407
 * 
 * TODO
 * - Add boundaries and boundary rules.
 */
public class Arena {

	Plugin plugin;

	String name;
	int max;
	int min;

	String path;

	Scoreboard scoreboard;
	ScoreboardManager scoreboardManager;

	Location lobby;

	List<Group> groups = new ArrayList<Group>();
	List<Player> players = new ArrayList<Player>();

	Map<Player, Group> group = new HashMap<Player, Group>();
	Map<Player, Group> potential_group = new HashMap<Player, Group>();

	Group defaultGroup;

	boolean started = false;
	boolean starting = false;

	/**
	 * Arena constructor
	 * 
	 * NOTE: Automatically registers in ArenaHandler
	 * 
	 * @param plugin
	 *            The plugin which the arena is made for
	 * @param name
	 *            The arena's name
	 * @param max
	 *            The arena's maximum players
	 */
	public Arena(Plugin plugin, String name, int max) {
		this.plugin = plugin;
		this.name = name;
		this.max = max;

		this.scoreboardManager = Bukkit.getScoreboardManager();
		this.scoreboard = scoreboardManager.getNewScoreboard();
		this.groups = new ArrayList<Group>();

		defaultGroup = new Group(GroupType.SPECTATING, "default", ChatColor.BLACK, this, 8);

		GroupHandler.registerGroup(defaultGroup, this);

		path = plugin.getDataFolder().getPath() + "/arenas/" + name + "/";

		ArenaHandler.registerArena(plugin, this);
	}

	/**
	 * Broadcast a message to all players in the arena
	 * 
	 * @param message
	 *            Message to broadcast
	 */
	public void broadcastMessage(String message) {
		for (int i = 0; i < players.size(); i++) {
			players.get(i).sendMessage(message);
		}
	}

	/**
	 * Get the name of the arena
	 * 
	 * @return The arena's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the arena
	 * 
	 * @param name
	 *            The new name of the arena
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the groups of the arena
	 * 
	 * @return The groups of the arena
	 */
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * Set the groups of the arena
	 * 
	 * @param groups
	 *            The new groups of the arena
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	/**
	 * Update the groups of this arena
	 */
	public void updateGroups() {
		this.groups = GroupHandler.getGroups(this);
	}

	/**
	 * Get the players in the arena
	 * 
	 * @return The players in the arena
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Set the players in the arena
	 * 
	 * @param players
	 *            The new players in the arena
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	/**
	 * Add a player to the arena in the default group
	 * 
	 * @param player
	 * @return true if defaultGroup is not null
	 */
	public void addPlayer(Player player) {
		if (this.defaultGroup != null) {
			addPlayerInGroup(player, defaultGroup);
		}
	}

	/**
	 * Add a player to the arena in a specific group
	 * 
	 * @param player
	 *            Player to add
	 * @param group
	 *            Group to add the player in
	 * @return true if the arena is not full
	 */
	public void addPlayerInGroup(Player player, Group group) {
		if (!this.players.contains(player)) {
			this.players.add(player);

			player.setScoreboard(scoreboard);

			setGroup(player, group);
		}
	}

	/**
	 * Remove a player from the arena
	 * 
	 * @param player
	 *            The player to remove from the arena
	 */
	public void removePlayer(Player player) {
		players.remove(player);
		group.remove(player);
		potential_group.remove(player);

		if (getGroup(player) != null) {
			getGroup(player).removePlayer(player);
		}
	}

	public void movePotentialToGroup(Player p) {
		if (potential_group.containsKey(p)) {
			potential_group.get(p).addPlayer(p);
			potential_group.get(p).removePotentialPlayer(p);
			potential_group.remove(p);
		}
	}

	public void moveAllPotentialToGroup() {
		for (Player p : players) {
			movePotentialToGroup(p);
		}
	}

	/**
	 * Change the players group
	 * 
	 * @param player
	 *            The player to change the group for
	 * @param group
	 *            The new group of the player
	 */
	public void setGroup(Player player, Group group) {
		PlayerJoinGroupEvent event = new PlayerJoinGroupEvent(plugin, player, group);

		if (event.getGroup().equals(event.getGroup().getArena().getDefaultGroup())) {
			event.setMessage(null);
		}

		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			if (PlayerHandler.isInArena(player)) {
				Arena a = PlayerHandler.getArena(player);
				if (a.getGroup(player) != null) {
					a.getGroup(player).removePlayer(player);
					a.group.remove(player);
				}
				if (a.getPotentialGroup(player) != null) {
					a.getPotentialGroup(player).removePotentialPlayer(player);
					a.potential_group.remove(player);
				}
			}

			Group g = event.getGroup();
			g.getArena().group.put(player, g);
			g.addPlayer(player);

			PlayerJoinedGroupEvent event2 = new PlayerJoinedGroupEvent(event.getPlugin(), event.getPlayer(), event.getGroup());
			Bukkit.getPluginManager().callEvent(event2);

			String message = event.getMessage();
			if (message != null) {
				message = ChatEncoder.arenas(message, g.getArena());
				message = ChatEncoder.groups(message, g);
				message = ChatEncoder.groupDisplays(message, g);
				message = ChatEncoder.players(message, player);
				g.broadcastMessage(message);
			}
		}
	}

	public void setPotentialGroup(Player player, Group group) {
		PlayerPotentiallyJoinGroupEvent event = new PlayerPotentiallyJoinGroupEvent(plugin, player, group);

		if (event.getGroup().equals(event.getGroup().getArena().getDefaultGroup())) {
			event.setMessage(null);
		}

		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			if (PlayerHandler.isInArena(player)) {
				Arena a = PlayerHandler.getArena(player);
				if (a.getPotentialGroup(player) != null) {
					a.getPotentialGroup(player).removePlayer(player);
					a.potential_group.remove(player);
				}
			}

			Group g = event.getGroup();
			g.getArena().potential_group.put(player, g);
			g.addPotentialPlayer(player);

			PlayerPotentiallyJoinedGroupEvent event2 = new PlayerPotentiallyJoinedGroupEvent(event.getPlugin(), event.getPlayer(), event.getGroup());
			Bukkit.getPluginManager().callEvent(event2);

			String message = event.getMessage();
			if (message != null) {
				message = ChatEncoder.arenas(message, g.getArena());
				message = ChatEncoder.groups(message, g);
				message = ChatEncoder.groupDisplays(message, g);
				message = ChatEncoder.players(message, player);
				g.broadcastMessage(message);
			}
		}
	}

	public Group getPotentialGroup(Player player) {
		return potential_group.get(player);
	}

	/**
	 * Get the players group
	 * 
	 * @param player
	 *            The player to get the group for
	 * @return group The player's group
	 */
	public Group getGroup(Player player) {
		return this.group.get(player);
	}

	/**
	 * Get the scoreboard of the arena
	 * 
	 * @return The arena's scoreboard
	 */
	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	/**
	 * Set the scoreboard of the arena
	 * 
	 * @param scoreboard
	 *            The new scoreboard of the arena
	 */
	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	/**
	 * Set the scoreboard manager
	 * 
	 * @return The arena's scoreboard manager
	 */
	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}

	/**
	 * Get the scoreboard manager
	 * 
	 * @param scoreboardManager
	 *            The new scoreboard manager of the arena
	 */
	public void setScoreboardManager(ScoreboardManager scoreboardManager) {
		this.scoreboardManager = scoreboardManager;
	}

	/**
	 * Get the lobby
	 * 
	 * @return The arena's lobby
	 */
	public Location getLobby() {
		return lobby;
	}

	/**
	 * Set the lobby
	 * 
	 * @param lobby
	 *            The new lobby of the arena
	 */
	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	/**
	 * Get the maximum amount of players
	 * 
	 * @return The maximum amount of players in the arena
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Set the maximum amount of players
	 * 
	 * @param max
	 *            The new maximum players
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Set the minimum amount of players
	 * 
	 * @return The minimum amount of players
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Get the minimum amount of players
	 * 
	 * @param min
	 *            The new minimum amount of players
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * Check if the arena has started
	 * 
	 * @return True if the arena started
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * Set if the arena has started
	 * 
	 * @param started
	 */
	public void start() {
		ArenaStartEvent event = new ArenaStartEvent(plugin, this);

		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			this.started = true;

			String message = event.getMessage();
			if (message != null) {
				message = ChatEncoder.arenas(message, this);

				broadcastMessage(message);
			}
		}
	}

	public void stop() {
		ArenaStopEvent event = new ArenaStopEvent(plugin, this);

		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			this.started = false;

			String message = event.getMessage();
			if (message != null) {
				message = ChatEncoder.arenas(message, this);

				broadcastMessage(message);
			}
		}
	}

	/**
	 * Get the default group of this arena
	 * 
	 * @return the default group, or null
	 */
	public Group getDefaultGroup() {
		return defaultGroup;
	}

	/**
	 * Set the default group of this arena
	 * 
	 * @param defaultGroup
	 *            The new default group
	 */
	public void setDefaultGroup(Group defaultGroup) {
		this.defaultGroup = defaultGroup;
	}

	public boolean isFull() {
		return players.size() >= max;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public String getDir() {
		return path;
	}

	public void setDir(String path) {
		this.path = path;
	}

	public void setStarting(boolean starting) {
		this.starting = starting;
	}

	public boolean isStarting() {
		return starting;
	}

}
