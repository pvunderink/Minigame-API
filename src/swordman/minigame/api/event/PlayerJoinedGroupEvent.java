package swordman.minigame.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.group.Group;

public class PlayerJoinedGroupEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Plugin plugin;
	private Player player;
	private Group group;

	public PlayerJoinedGroupEvent(Plugin plugin, Player player, Group group) {
		this.plugin = plugin;
		this.player = player;
		this.group = group;
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

	public Plugin getPlugin() {
		return plugin;
	}

}
