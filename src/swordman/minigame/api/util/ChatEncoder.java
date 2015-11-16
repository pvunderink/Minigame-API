package swordman.minigame.api.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.group.Group;

public class ChatEncoder {

	public static String colors(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static String players(String message, Player... players) {
		for (int i = 0; i < players.length; i++) {
			if (i == 0) {
				message = message.replace("%player%", players[i].getName());
			}
			message = message.replace("%player" + i + "%", players[i].getName());
		}

		return colors(message);
	}

	public static String arenas(String message, Arena... arenas) {
		for (int i = 0; i < arenas.length; i++) {
			if (i == 0) {
				message = message.replace("%arena%", arenas[i].getName());
			}
			message = message.replace("%arena" + i + "%", arenas[i].getName());
		}

		return colors(message);
	}

	public static String groups(String message, Group... groups) {
		for (int i = 0; i < groups.length; i++) {
			if (i == 0) {
				message = message.replace("%group%", groups[i].getName());
			}
			message = message.replace("%group" + i + "%", groups[i].getName());
		}

		return colors(groupDisplays(message, groups));
	}
	
	public static String groupDisplays(String message, Group... groups) {
		for (int i = 0; i < groups.length; i++) {
			if (i == 0) {
				message = message.replace("%group_display%", groups[i].getDisplayName());
			}
			message = message.replace("%group_display" + i + "%", groups[i].getDisplayName());
		}

		return colors(message);
	}
	
	public static String count(String message, int... count) {
		for (int i = 0; i < count.length; i++) {
			if (i == 0) {
				message = message.replace("%count%", "" + count[i]);
			}
			message = message.replace("%count" + i + "%", "" + count[i]);
		}

		return colors(message);
	}
	
	public static String max(String message, int... max) {
		for (int i = 0; i < max.length; i++) {
			if (i == 0) {
				message = message.replace("%max%", "" + max[i]);
			}
			message = message.replace("%max" + i + "%", "" + max[i]);
		}

		return colors(message);
	}
	
	public static String commands(String message, String... commands) {
		for (int i = 0; i < commands.length; i++) {
			if (i == 0) {
				message = message.replace("%command%", commands[i]);
			}
			message = message.replace("%command" + i + "%", "" + commands[i]);
		}

		return colors(message);
	}
	
	public static String args(String message, String... args) {
		for (int i = 0; i < args.length; i++) {
			if (i == 0) {
				message = message.replace("%arg%", args[i]);
			}
			message = message.replace("%arg" + i + "%", "" + args[i]);
		}

		return colors(message);
	}

}
