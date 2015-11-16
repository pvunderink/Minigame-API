package swordman.minigame.api.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.arena.ArenaHandler;
import swordman.minigame.api.util.ChatEncoder;

public class CreateArenaCommand implements CommandExecutor {

	private Plugin plugin;

	public String permission = "minigame.admin";
	public String message_succesfull = ChatColor.GOLD + "Created arena %arena%";
	public String message_no_permission = ChatColor.RED + "You don't have the permission to perform this command";
	public String message_usage = ChatColor.RED + "Usage: /%command% <name> <max players>";
	public String message_number_format = ChatColor.RED + "%arg% has to be a number";
	public String message_already_exists = ChatColor.RED + "Arena %arg% already exists";

	public CreateArenaCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission(permission)) {
			if (args.length > 1) {
				if (!ArenaHandler.exists(plugin, args[0])) {
					try {
						Arena a = new Arena(plugin, args[0], Integer.parseInt(args[1]));

						sender.sendMessage(ChatEncoder.arenas(message_succesfull, a));
					} catch (NumberFormatException ex) {
						sender.sendMessage(ChatEncoder.args(message_number_format, "argument 2 (max players)"));
					}
				} else {
					sender.sendMessage(ChatEncoder.args(message_already_exists, args[0]));
				}
			} else {
				sender.sendMessage(ChatEncoder.commands(message_usage, label));
			}
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
