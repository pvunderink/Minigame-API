package swordman.minigame.api.player;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.event.PlayerJoinArenaEvent;
import swordman.minigame.api.event.PlayerJoinedArenaEvent;
import swordman.minigame.api.event.PlayerQuitArenaEvent;
import swordman.minigame.api.group.Group;
import swordman.minigame.api.util.ChatEncoder;

/**
 * 
 * MinigameAPI
 * 
 * @author swordman407
 * 
 */
public class PlayerHandler {

	private static HashMap<Player, Arena> players = new HashMap<Player, Arena>();

	/**
	 * Let a player join an arena
	 * 
	 * @param player
	 *            The player which will join the arena
	 * @param arena
	 *            The arena to join
	 * @return True if successfully joined (false if the arena is full)
	 */
	public static boolean joinArena(Player player, Arena arena) {
		return joinArena(player, arena, null);
	}

	/**
	 * Let a player join an arena
	 * 
	 * @param player
	 *            The player which will join the arena
	 * @param arena
	 *            The arena to join
	 * @param group
	 *            The group to add the player to
	 * @return
	 */
	public static boolean joinArena(Player player, Arena arena, Group group) {
		PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(arena.getPlugin(), player, arena);

		if (group != null)
			event.setGroup(group);

		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			if (isInArena(player)) {
				getArena(player).removePlayer(player);
			}

			arena.addPlayerInGroup(event.getPlayer(), event.getGroup());
			players.remove(event.getPlayer());
			players.put(event.getPlayer(), event.getArena());

			String message = event.getMessage();
			if (message != null) {
				message = ChatEncoder.arenas(message, arena);
				message = ChatEncoder.players(message, player);
				arena.broadcastMessage(message);
			}

			PlayerJoinedArenaEvent event2 = new PlayerJoinedArenaEvent(arena.getPlugin(), player, arena);
			Bukkit.getPluginManager().callEvent(event2);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Let a player leave its arena
	 * 
	 * @param player
	 *            The player that will leave its arena
	 * @return
	 */
	public static boolean leaveArena(Player player) {
		if (players.containsKey(player)) {
			Arena arena = players.get(player);

			PlayerQuitArenaEvent event = new PlayerQuitArenaEvent(arena.getPlugin(), player, arena);

			Bukkit.getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				arena.removePlayer(event.getPlayer());
				players.remove(event.getPlayer());

				String message = event.getMessage();
				if (message != null) {
					message = ChatEncoder.arenas(message, arena);
					message = ChatEncoder.players(message, player);
					arena.broadcastMessage(message);
				}

				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Get the arena a player is in
	 * 
	 * @param player
	 * @return The arena, or null if the player isn't in an arena
	 */
	public static Arena getArena(Player player) {
		return players.get(player);
	}

	/**
	 * Check if a player is in an arena
	 * 
	 * @param player
	 * @return true if the player is in an arena
	 */
	public static boolean isInArena(Player player) {
		return players.containsKey(player);
	}

}
