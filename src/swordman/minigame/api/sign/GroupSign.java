package swordman.minigame.api.sign;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.event.SignClickEvent;
import swordman.minigame.api.event.SignUpdateEvent;
import swordman.minigame.api.group.Group;
import swordman.minigame.api.player.PlayerHandler;
import swordman.minigame.api.util.ChatEncoder;

public class GroupSign implements ClickableSign {

	Arena arena;
	Group group;
	Sign sign;

	public GroupSign(Arena arena, Group group, Sign sign) {
		this.arena = arena;
		this.group = group;
		this.sign = sign;

		SignHandler.addGroupSign(this);
	}

	public void update() {
		SignUpdateEvent event = new SignUpdateEvent(arena.getPlugin(), this, new String[] { "[Join]", "%group_display%", "", "%count%/%max%" });

		Bukkit.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			for (int i = 0; i < 4; i++) {
				String s = event.getLine(i);

				if (s != null) {
					s = ChatEncoder.arenas(s, arena);
					s = ChatEncoder.groups(s, group);
					s = ChatEncoder.groupDisplays(s, group);
					s = ChatEncoder.count(s, group.getPotentialPlayers().size() + group.getPlayers().size());
					s = ChatEncoder.max(s, group.getMax());

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
			if (!group.getPotentialPlayers().contains(p)) {
				if (PlayerHandler.isInArena(p) && PlayerHandler.getArena(p).equals(arena)) {
					arena.setPotentialGroup(p, group);
				} else {
					p.sendMessage(ChatColor.RED + "Join the arena first!");
				}
			}
		}
	}

	public Sign getSign() {
		return sign;
	}

	public Group getGroup() {
		return group;
	}

	public Arena getArena() {
		return arena;
	}

}
