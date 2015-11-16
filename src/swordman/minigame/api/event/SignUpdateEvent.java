package swordman.minigame.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.sign.ClickableSign;

public class SignUpdateEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Plugin plugin;
	private ClickableSign sign;
	private String[] lines;

	private boolean cancelled = false;

	public SignUpdateEvent(Plugin plugin, ClickableSign sign, String... lines) {
		this.plugin = plugin;
		this.sign = sign;
		this.lines = new String[4];
		
		for (int i = 0; i < lines.length; i++) {
			if (i < this.lines.length) {
				this.lines[i] = lines[i];
			}
		}
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

	public ClickableSign getSign() {
		return sign;
	}
	
	public void setLine(int n, String line) {
		if (n < lines.length) {
			lines[n] = line;
		}
	}
	
	public void setLines(String[] lines) {
		this.lines = lines;
	}

	public String getLine(int n) {
		if (n < lines.length) {
			return lines[n];
		} else {
			return "";
		}
	}
	
	public String[] getLines() {
		return lines;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

}
