package swordman.minigame.api.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.group.Group;

public class PlayerPotentiallyJoinGroupEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Plugin plugin;
	private Player player;
	private Group group;
	private String message = ChatColor.BLACK + "[" + "%group_display%" + ChatColor.BLACK + "] " + ChatColor.GOLD + "%player% joined the team";

	private boolean cancelled = false;

	public PlayerPotentiallyJoinGroupEvent(Plugin plugin, Player player, Group group) {
		this.plugin = plugin;
		this.player = player;
		this.group = group;
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
