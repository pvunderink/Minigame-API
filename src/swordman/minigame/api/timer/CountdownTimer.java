package swordman.minigame.api.timer;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import swordman.minigame.api.arena.Arena;

public class CountdownTimer implements Runnable {

	BukkitTask task;

	Arena arena;
	int seconds;
	String message;

	public CountdownTimer(Arena arena, int seconds) {
		this.arena = arena;
		this.seconds = seconds;
		this.message = ChatColor.BLACK + "[" + ChatColor.DARK_RED + "%arena%" + "]" + ChatColor.GOLD + " Game starting in %time% seconds";
	}

	public CountdownTimer(Arena arena, int seconds, String message) {
		this.arena = arena;
		this.seconds = seconds;
		this.message = message;
	}

	public void run() {
		if (seconds > 0) {
			arena.broadcastMessage(message.replace("%time%", "" + seconds).replace("%arena%", arena.getName()));
		} else {
			if (task != null) {
				task.cancel();
			}

			if (!arena.isStarted()) {
				arena.start();
			}
		}
		seconds--;
	}

	public void setTask(BukkitTask task) {
		this.task = task;
	}

}
