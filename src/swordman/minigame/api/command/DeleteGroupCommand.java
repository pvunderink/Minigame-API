package swordman.minigame.api.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.arena.ArenaHandler;
import swordman.minigame.api.group.Group;
import swordman.minigame.api.group.GroupHandler;
import swordman.minigame.api.util.ChatEncoder;

public class DeleteGroupCommand implements CommandExecutor {
	
	private Plugin plugin;

	public String permission = "minigame.admin";
	public String message_successfull = ChatColor.GOLD + "Deleted group %group_display%" + ChatColor.GOLD + " in arena %arena%";
	public String message_no_permission = ChatColor.RED + "You don't have the permission to perform this command";
	public String message_usage = ChatColor.RED + "Usage: /%command% <arena> <group>";
	public String message_not_exists = ChatColor.RED + "Arena %arg% does not exist";
	public String message_group_not_exists = ChatColor.RED + "Group %arg% does not exist";

	public DeleteGroupCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission(permission)) {
			if (args.length > 1) {
				if (ArenaHandler.exists(plugin, args[0])) {
					Arena a = ArenaHandler.getArenaByName(plugin, args[0]);
					
					if (GroupHandler.containsGroup(a, args[1])) {
						Group g = GroupHandler.getGroupByName(a, args[1]);
						
						GroupHandler.unregisterGroup(g, a);
						
						sender.sendMessage(ChatEncoder.arenas(ChatEncoder.groups(message_successfull, g), a));
					} else {
						sender.sendMessage(ChatEncoder.args(message_group_not_exists, args[1]));
					}
				} else {
					sender.sendMessage(ChatEncoder.args(message_not_exists, args[0]));
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
