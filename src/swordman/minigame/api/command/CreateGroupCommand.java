package swordman.minigame.api.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.NameTagVisibility;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.arena.ArenaHandler;
import swordman.minigame.api.group.Group;
import swordman.minigame.api.group.GroupHandler;
import swordman.minigame.api.util.ChatEncoder;
import swordman.minigame.api.util.GroupType;

public class CreateGroupCommand implements CommandExecutor {

	private Plugin plugin;

	public String permission = "minigame.admin";
	public String message_successfull = ChatColor.GOLD + "Created group %group_display%" + ChatColor.GOLD + " in arena %arena%";
	public String message_no_permission = ChatColor.RED + "You don't have the permission to perform this command";
	public String message_usage = ChatColor.RED + "Usage: /%command% <arena> <name> <type> <max players> [color] [prefix] [suffix] [friendly fire] [see invisibles] [nametag visibility]";
	public String message_not_number = ChatColor.RED + "%arg% has to be a number";
	public String message_not_boolean = ChatColor.RED + "%arg% has to be a boolean (true or false)";
	public String message_already_exists = ChatColor.RED + "Group %arg% in %arena% already exists";
	public String message_not_exists = ChatColor.RED + "Arena %arg% does not exist";
	public String message_invalid_type = ChatColor.RED + "Invalid grouptype (Choose from: PLAYING, SPECTATING)";
	public String message_invalid_color = ChatColor.RED + "Invalid color";
	public String message_invalid_nametag_visibility = ChatColor.RED + "Invalid nametag visibility (Choose from: ALWAYS, HIDE_FOR_OTHER_TEAMS, HIDE_FOR_OWN_TEAM, NEVER)";

	public CreateGroupCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission(permission)) {
			if (args.length > 3) {
				if (ArenaHandler.exists(plugin, args[0])) {
					Arena a = ArenaHandler.getArenaByName(plugin, args[0]);
					if (!GroupHandler.containsGroup(a, args[1])) {
						if (GroupType.matchByString(args[2]) != null) {
							GroupType type = GroupType.matchByString(args[2]);
							try {
								int max = Integer.parseInt(args[3]);

								Group g = new Group(type, args[1], ChatColor.WHITE, a, max);

								if (args.length > 4) {
									if (ChatColor.valueOf(args[4].toUpperCase()) != null) {
										ChatColor color = ChatColor.valueOf(args[4].toUpperCase());
										g.setColor(color);

										if (args.length > 5) {
											g.setPrefix(args[5]);

											if (args.length > 6) {
												g.setSuffix(args[6]);

												if (args.length > 7) {
													if (args[7].equalsIgnoreCase("true") || args[7].equalsIgnoreCase("false")) {
														boolean ff = Boolean.parseBoolean(args[7]);

														g.setAllowFriendlyFire(ff);

														if (args.length > 8) {
															if (args[8].equalsIgnoreCase("true") || args[8].equalsIgnoreCase("false")) {
																boolean si = Boolean.parseBoolean(args[8]);

																g.setCanSeeFriendlyInvisibles(si);
															} else {
																sender.sendMessage(ChatEncoder.args(message_not_boolean, "argument 9 (see invisibles)"));
																return true;
															}

															if (args.length > 9) {
																if (NameTagVisibility.valueOf(args[9]) != null) {
																	NameTagVisibility ntv = NameTagVisibility.valueOf(args[9]);

																	g.setNameTagVisibility(ntv);
																} else {
																	sender.sendMessage(ChatEncoder.colors(message_invalid_nametag_visibility));
																	return true;
																}
															}
														}
													} else {
														sender.sendMessage(ChatEncoder.args(message_not_boolean, "argument 8 (friendly fire)"));
														return true;
													}
												}
											}
										}
									} else {
										sender.sendMessage(ChatEncoder.colors(message_invalid_color));
										return true;
									}
								}
								
								System.out.println(g.getName());
								System.out.println(g.getDisplayName());

								sender.sendMessage(ChatEncoder.arenas(ChatEncoder.groups(message_successfull, g), a));
							} catch (NumberFormatException ex) {
								sender.sendMessage(ChatEncoder.args(message_not_number, "argument 4 (max players)"));
							}
						} else {
							sender.sendMessage(ChatEncoder.colors(message_invalid_type));
						}
					} else {
						sender.sendMessage(ChatEncoder.arenas(ChatEncoder.args(message_already_exists, args[1]), a));
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
