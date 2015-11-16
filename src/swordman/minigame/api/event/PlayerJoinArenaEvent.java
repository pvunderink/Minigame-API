package swordman.minigame.api.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.group.Group;

public class PlayerJoinArenaEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Plugin plugin;
	private Player player;
	private Arena arena;
	private Group group;
	private String message = ChatColor.BLACK + "[" + ChatColor.DARK_RED + "%arena%" + ChatColor.BLACK + "] " + ChatColor.GOLD + "%player% joined the arena";

	private boolean cancelled = false;

	public PlayerJoinArenaEvent(Plugin plugin, Player player, Arena arena) {
		this.plugin = plugin;
		this.player = player;
		this.arena = arena;
		this.group = arena.getDefaultGroup();
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

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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
