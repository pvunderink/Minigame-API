package swordman.minigame.api.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.arena.ArenaHandler;
import swordman.minigame.api.group.Group;
import swordman.minigame.api.group.GroupHandler;
import swordman.minigame.api.sign.ArenaSign;
import swordman.minigame.api.sign.GroupSign;
import swordman.minigame.api.sign.SignHandler;

public class BukkitEvents implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent ev) {
		Player p = ev.getPlayer();

		if (ev.getAction() == Action.RIGHT_CLICK_BLOCK && ev.getClickedBlock().getState() instanceof Sign) {
			for (ArenaSign as : SignHandler.getArenaSigns()) {
				if (as.getSign().getLocation().equals(ev.getClickedBlock().getLocation())) {
					as.click(p);
				}
			}
			for (GroupSign gs : SignHandler.getGroupSigns()) {
				if (gs.getSign().getLocation().equals(ev.getClickedBlock().getLocation())) {
					gs.click(p);
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent ev) {
		Player p = ev.getPlayer();

		if (ev.getBlock().getState() instanceof Sign) {
			for (int i = 0; i < SignHandler.getArenaSigns().size(); i++) {
				ArenaSign as = SignHandler.getArenaSigns().get(i);

				if (as.getSign().getLocation().equals(ev.getBlock().getLocation())) {
					SignDeleteEvent event = new SignDeleteEvent(as.getArena().getPlugin(), p, as);

					Bukkit.getPluginManager().callEvent(event);

					if (!event.isCancelled()) {
						SignHandler.removeArenaSign((ArenaSign) event.getSign());

						p.sendMessage(ChatColor.GOLD + "Removed a sign from arena " + as.getArena().getName());
					}
				}
			}
			
			for (int i = 0; i < SignHandler.getGroupSigns().size(); i++) {
				GroupSign gs = SignHandler.getGroupSigns().get(i);

				if (gs.getSign().getLocation().equals(ev.getBlock().getLocation())) {
					SignDeleteEvent event = new SignDeleteEvent(gs.getArena().getPlugin(), p, gs);

					Bukkit.getPluginManager().callEvent(event);

					if (!event.isCancelled()) {
						SignHandler.removeGroupSign((GroupSign) event.getSign());

						p.sendMessage(ChatColor.GOLD + "Removed a group sign from arena " + gs.getArena().getName());
					}
				}
			}
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent ev) {
		Player p = ev.getPlayer();
		Sign s = (Sign) ev.getBlock().getState();

		if (p.hasPermission("minigame.admin")) {
			if (ev.getLine(0).equalsIgnoreCase("[Arena]")) {
				if (Bukkit.getServer().getPluginManager().getPlugin(ev.getLine(1)) != null) {
					Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(ev.getLine(1));

					if (ArenaHandler.exists(plugin, ev.getLine(2))) {
						Arena a = ArenaHandler.getArenaByName(plugin, ev.getLine(2));
						SignCreateEvent event = new SignCreateEvent(a.getPlugin(), p, s, a);

						Bukkit.getPluginManager().callEvent(event);

						if (!event.isCancelled()) {
							new ArenaSign(event.getArena(), event.getSign());

							p.sendMessage(ChatColor.GOLD + "Created a new sign for arena " + event.getArena().getName() + "!");
						}
					} else {
						p.sendMessage(ChatColor.RED + "The arena specified on line 3 does not exist!");
					}
				} else {
					p.sendMessage(ChatColor.RED + "The minigame plugin specified on line 2 does not exist!");
				}
			} else if (ev.getLine(0).equalsIgnoreCase("[Group]")) {
				if (Bukkit.getServer().getPluginManager().getPlugin(ev.getLine(1)) != null) {
					Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(ev.getLine(1));

					if (ArenaHandler.exists(plugin, ev.getLine(2))) {
						Arena a = ArenaHandler.getArenaByName(plugin, ev.getLine(2));
						SignCreateEvent event = new SignCreateEvent(a.getPlugin(), p, s, a);

						if (GroupHandler.containsGroup(a, ev.getLine(3))) {
							Group g = GroupHandler.getGroupByName(a, ev.getLine(3));
							event.setGroup(g);
						}
						Bukkit.getPluginManager().callEvent(event);
						
						if (!event.isCancelled()) {
							Group g = event.getGroup();
							
							if (g == null) {
								g = event.getArena().getDefaultGroup();
							}
							
							new GroupSign(event.getArena(), g, event.getSign());
							
							p.sendMessage(ChatColor.GOLD + "Created a new sign for group " + g.getDisplayName() + ChatColor.GOLD + " in arena " + event.getArena().getName() + "!");
						}
					} else {
						p.sendMessage(ChatColor.RED + "The arena specified on line 3 does not exist!");
					}
				} else {
					p.sendMessage(ChatColor.RED + "The minigame plugin specified on line 2 does not exist!");
				}
			}
		}
	}

}
