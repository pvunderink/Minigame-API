package swordman.minigame.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.sign.ClickableSign;

public class SignDeleteEvent extends Event {
	
    private static final HandlerList handlers = new HandlerList();
    private Plugin plugin;
    private Player player;
    private ClickableSign sign;
    
    private boolean cancelled = false;

    public SignDeleteEvent(Plugin plugin, Player player, ClickableSign sign) {
    	this.plugin = plugin;
    	this.player = player;
    	this.sign = sign;
    	this.plugin = plugin;
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
	
	public ClickableSign getSign() {
		return sign;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

}
