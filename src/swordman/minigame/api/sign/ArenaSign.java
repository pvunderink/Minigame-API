package swordman.minigame.api.sign;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.event.SignClickEvent;
import swordman.minigame.api.event.SignUpdateEvent;
import swordman.minigame.api.player.PlayerHandler;
import swordman.minigame.api.util.ChatEncoder;

public class ArenaSign implements ClickableSign {

	Arena arena;
	Sign sign;

	public ArenaSign(Arena arena, Sign sign) {
		this.arena = arena;
		this.sign = sign;

		SignHandler.addArenaSign(this);
	}

	public void update() {
		SignUpdateEvent event = new SignUpdateEvent(arena.getPlugin(), this, new String[] { ChatColor.BLUE + "[Join]", "%arena%", "", "%count%/%max%" });

		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			for (int i = 0; i < 4; i++) {
				String s = event.getLine(i);

				if (s != null) {
					s = ChatEncoder.arenas(s, arena);
					s = ChatEncoder.count(s, arena.getPlayers().size());
					s = ChatEncoder.max(s, arena.getMax());
					
					sign.setLine(i, s);
				}
			}

			sign.update();
		}
	}

	public void click(Player p) {
		SignClickEvent event = new SignClickEvent(arena.getPlugin(), p, this);

		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			PlayerHandler.joinArena(p, arena);
		}
	}

	public Sign getSign() {
		return sign;
	}

	public Arena getArena() {
		return arena;
	}

}
