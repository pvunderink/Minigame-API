package swordman.minigame.api.event;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.group.Group;

public class SignCreateEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Plugin plugin;
	private Player player;
	private Sign sign;
	private Arena arena;
	private Group group;

	private boolean cancelled = false;

	public SignCreateEvent(Plugin plugin, Player player, Sign sign, Arena arena) {
		this.plugin = plugin;
		this.player = player;
		this.sign = sign;
		this.arena = arena;
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

	public Sign getSign() {
		return sign;
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
	
	public Plugin getPlugin() {
		return plugin;
	}

}
