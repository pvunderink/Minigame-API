package swordman.minigame.api.group;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.util.GroupType;

/**
 * 
 * MinigameAPI
 * 
 * @author swordman407
 * 
 */
public class Group {

	GroupType type;
	String name;
	ChatColor color;
	Arena arena;
	int max;

	String prefix;
	String suffix;
	
	Team team;

	Location spawn;

	List<Location> spawnpoints = new ArrayList<Location>();

	List<Player> players;
	List<Player> potential_players;

	public Group(GroupType type, String name, ChatColor color, Arena arena, int max, boolean friendlyFire, boolean seeInvisibles, String prefix, String suffix, NameTagVisibility nametagVisibility) {
		this.type = type;
		this.name = name;
		this.color = color;
		this.arena = arena;
		this.max = max;
		this.prefix = prefix;
		this.suffix = suffix;

		if (arena.getScoreboard().getTeam(name) == null)
			this.team = arena.getScoreboard().registerNewTeam(name);
		else
			this.team = arena.getScoreboard().getTeam(name);
		team.setDisplayName(color + name);
		team.setAllowFriendlyFire(friendlyFire);
		team.setCanSeeFriendlyInvisibles(seeInvisibles);
		team.setPrefix(prefix + (prefix.endsWith(color.toString()) ? "" : color));
		team.setSuffix((suffix.startsWith(ChatColor.RESET.toString()) ? "" : ChatColor.RESET) + suffix);
		team.setNameTagVisibility(nametagVisibility);

		this.players = new ArrayList<Player>();
		this.potential_players = new ArrayList<Player>();

		GroupHandler.registerGroup(this, this.arena);
	}

	/**
	 * Group constructor
	 * 
	 * @param type
	 *            The group type
	 * @param name
	 *            The group name
	 * @param color
	 *            The group color
	 * @param arena
	 *            The arena this group is made for
	 * @param max
	 *            The maximum amount of players
	 * @param friendlyFire
	 *            Friendly fire
	 * @param seeInvisibles
	 *            See own invisibles
	 */
	public Group(GroupType type, String name, ChatColor color, Arena arena, int max, boolean friendlyFire, boolean seeInvisibles) {
		this(type, name, color, arena, max, friendlyFire, seeInvisibles, "", "", NameTagVisibility.ALWAYS);
	}

	/**
	 * Group constructor
	 * 
	 * @param type
	 *            The group type
	 * @param name
	 *            The group name
	 * @param color
	 *            The group color
	 * @param arena
	 *            The arena this group is made for
	 * @param max
	 *            The maximum amount of players
	 */
	public Group(GroupType type, String name, ChatColor color, Arena arena, int max) {
		this(type, name, color, arena, max, false, true);
	}

	/**
	 * Add a player to this group
	 * 
	 * @param player
	 *            The player to add
	 */
	public void addPlayer(Player player) {
		players.add(player);
		team.addEntry(player.getName());
	}

	/**
	 * Remove a player from this group
	 * 
	 * @param player
	 *            The player to remove
	 */
	public void removePlayer(Player player) {
		players.remove(player);
		team.removeEntry(player.getName());
	}

	/**
	 * Get the players in this group
	 * 
	 * @return The players in this group
	 */
	public List<Player> getPlayers() {
		return this.players;
	}

	/**
	 * Set the players in this group
	 * 
	 * @param players
	 *            The new players in this group
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public void addPotentialPlayer(Player p) {
		potential_players.add(p);
	}
	
	public void removePotentialPlayer(Player p) {
		potential_players.remove(p);
	}
	
	public List<Player> getPotentialPlayers() {
		return potential_players;
	}

	/**
	 * Get the spawn location of this group
	 * 
	 * @return The spawn of this group
	 */
	public Location getSpawn() {
		return this.spawn;
	}

	/**
	 * Set the spawn for this group
	 * 
	 * @param location
	 *            The new spawn of this group
	 */
	public void setSpawn(Location location) {
		this.spawn = location;
	}

	/**
	 * Get the group type
	 * 
	 * @return This group's type
	 */
	public GroupType getType() {
		return type;
	}

	/**
	 * Set the group type
	 * 
	 * @param type
	 *            The new group type
	 */
	public void setType(GroupType type) {
		this.type = type;
	}

	/**
	 * Get the group color
	 * 
	 * @return The group's color
	 */
	public ChatColor getColor() {
		return color;
	}

	/**
	 * Set the group color
	 * 
	 * @param color
	 *            The new group color
	 */
	public void setColor(ChatColor color) {
		team.setDisplayName(color + getName());
		team.setPrefix(prefix + (prefix.endsWith(color.toString()) ? "" : color));
		this.color = color;
	}

	/**
	 * Get the group's name
	 * 
	 * @return The group's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the group's name
	 * 
	 * @param name
	 *            The new group name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the arena
	 * 
	 * @return The arena this group is registered for
	 */
	public Arena getArena() {
		return arena;
	}

	/**
	 * Set the arena
	 * 
	 * @param arena
	 *            The new arena this group is registered for
	 */
	public void setArena(Arena arena) {
		this.arena = arena;
	}

	/**
	 * Get the team on scoreboard
	 * 
	 * @return The group's scoreboard team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Set the team on scoreboard
	 * 
	 * @param team
	 *            Set the new group scoreboard team
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Get the display name (scoreboard)
	 * 
	 * @return The name of this group's team on the scoreboard
	 */
	public String getDisplayName() {
		return team.getDisplayName();
	}

	/**
	 * Set the display name (scoreboard)
	 * 
	 * @param name
	 *            The new name of this group's team on the scoreboard
	 */
	public void setDisplayName(String name) {
		team.setDisplayName(name);
	}

	/**
	 * Check if friendly fire is allowed
	 * 
	 * @return True if allowed
	 */
	public boolean getAllowFriendlyFire() {
		return team.allowFriendlyFire();
	}

	/**
	 * Set allow friendly fire
	 * 
	 * @param bool
	 */
	public void setAllowFriendlyFire(boolean bool) {
		team.setAllowFriendlyFire(bool);
	}

	/**
	 * Check can see invisibles
	 * 
	 * @return True if canSeeFriendlyInvisibles is set to true
	 */
	public boolean getCanSeeFriendlyInvisibles() {
		return team.canSeeFriendlyInvisibles();
	}

	/**
	 * Set can see invisibles
	 * 
	 * @param bool
	 */
	public void setCanSeeFriendlyInvisibles(boolean bool) {
		team.setCanSeeFriendlyInvisibles(bool);
	}

	/**
	 * Get the team's prefix
	 * 
	 * @return The team's prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Set the team's prefix
	 * 
	 * @param prefix
	 *            The new team's prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		team.setPrefix(prefix + (prefix.endsWith(color.toString()) ? "" : color));
	}

	/**
	 * Get the team's suffix
	 * 
	 * @return The team's suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Set the team's suffix
	 * 
	 * @param suffix
	 *            The new team's suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
		team.setSuffix((suffix.startsWith(ChatColor.RESET.toString()) ? "" : ChatColor.RESET) + suffix);
	}

	/**
	 * Get the maximum amount of players
	 * 
	 * @return The maximum amount of players
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Set the maximum amount of players
	 * 
	 * @param max
	 *            The new maximum amount of players
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Get the spawnpoints of this arena
	 * 
	 * @return The spawnpoints of this arena
	 */
	public List<Location> getSpawnpoints() {
		return spawnpoints;
	}

	/**
	 * Set the spawnpoints of this arena
	 * 
	 * @param spawnpoints
	 *            The new spawnpoints
	 */
	public void setSpawnpoints(List<Location> spawnpoints) {
		this.spawnpoints = spawnpoints;
	}

	/**
	 * Add a spawnpoint to the arena
	 * 
	 * @param loc
	 *            The new spawnpoint
	 */
	public void addSpawnpoint(Location loc) {
		this.spawnpoints.add(loc);
	}

	public void broadcastMessage(String message) {
		for (Player p : players) {
			p.sendMessage(message);
		}
	}

	public boolean isFull() {
		return players.size() + potential_players.size() >= max;
	}
	
	public void setNameTagVisibility(NameTagVisibility ntv) {
		team.setNameTagVisibility(ntv);
	}
	
	public NameTagVisibility getNameTagVisibility() {
		return team.getNameTagVisibility();
	}

}
