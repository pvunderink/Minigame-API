package swordman.minigame.api.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.arena.Arena;

public class PlayerQuitArenaEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Plugin plugin;
	private Player player;
	private Arena arena;
	private String message;

	private boolean cancelled = false;

	public PlayerQuitArenaEvent(Plugin plugin, Player player, Arena arena) {
		this.plugin = plugin;
		this.player = player;
		this.arena = arena;
		this.message = ChatColor.BLACK + "[" + ChatColor.DARK_RED + "%arena%" + ChatColor.BLACK + "] " + ChatColor.GOLD + "%player% left the arena";
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

}
