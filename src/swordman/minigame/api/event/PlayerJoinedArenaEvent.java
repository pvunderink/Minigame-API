package swordman.minigame.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.arena.Arena;

public class PlayerJoinedArenaEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Plugin plugin;
	private Player player;
	private Arena arena;

	public PlayerJoinedArenaEvent(Plugin plugin, Player player, Arena arena) {
		this.plugin = plugin;
		this.player = player;
		this.arena = arena;
	}

	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public Arena getArena() {
		return arena;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

}