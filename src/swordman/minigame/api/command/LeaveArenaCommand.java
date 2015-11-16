package swordman.minigame.api.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.player.PlayerHandler;

public class LeaveArenaCommand implements CommandExecutor {

	private Plugin plugin;

	public String permission = "minigame.admin";
	public String message_no_permission = ChatColor.RED + "You don't have the permission to perform this command";

	public LeaveArenaCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (sender.hasPermission(permission)) {
				PlayerHandler.leaveArena(p);
			}
		} else {
			sender.sendMessage("Player command only");
		}

		return true;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

	public Plugin getPlugin() {
		return plugin;
	}

}
