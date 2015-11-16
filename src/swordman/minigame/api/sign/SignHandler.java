package swordman.minigame.api.sign;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

import swordman.minigame.api.arena.Arena;
import swordman.minigame.api.group.Group;
import swordman.minigame.api.group.GroupHandler;

public class SignHandler {

	private static List<ArenaSign> arena_signs = new ArrayList<ArenaSign>();
	private static List<GroupSign> group_signs = new ArrayList<GroupSign>();

	public static List<ArenaSign> getArenaSigns() {
		return arena_signs;
	}

	public static void addArenaSign(ArenaSign sign) {
		if (!arena_signs.contains(sign)) {
			arena_signs.add(sign);
		}
	}

	public static void removeArenaSign(ArenaSign sign) {
		arena_signs.remove(sign);
	}

	public static List<GroupSign> getGroupSigns() {
		return group_signs;
	}

	public static void addGroupSign(GroupSign sign) {
		if (!group_signs.contains(sign)) {
			group_signs.add(sign);
		}
	}

	public static void removeGroupSign(GroupSign sign) {
		group_signs.remove(sign);
	}

	public static void loadArenaSigns(Arena arena) {
		try {
			loadArenaSignsUnsafe(arena);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void loadGroupSigns(Arena arena) {
		try {
			loadGroupSignsUnsafe(arena);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void saveArenaSigns(Arena arena) {
		try {
			saveArenaSignsUnsafe(arena);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void saveGroupSigns(Arena arena) {
		try {
			saveGroupSignsUnsafe(arena);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void loadArenaSignsUnsafe(Arena arena) throws Exception {
		File file = new File(arena.getDir() + "data", "arena-signs.yml");
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists()) {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			return;
		}

		config.load(file);

		for (String s : config.getStringList("signs")) {
			String[] parts = s.split(":");

			if (parts.length > 3) {
				World w = Bukkit.getWorld(parts[0]);

				if (w != null) {
					try {
						int x = Integer.parseInt(parts[1]);
						int y = Integer.parseInt(parts[2]);
						int z = Integer.parseInt(parts[3]);

						Location loc = new Location(w, x, y, z);

						Block b = loc.getBlock();

						if (b.getState() instanceof Sign) {
							new ArenaSign(arena, (Sign) b.getState());
						}
					} catch (NumberFormatException ex) {
					}
				}
			}
		}
	}

	private static void loadGroupSignsUnsafe(Arena arena) throws Exception {
		File file = new File(arena.getDir() + "data", "group-signs.yml");
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists()) {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			return;
		}

		config.load(file);

		for (String s : config.getKeys(false)) {
			if (GroupHandler.containsGroup(arena, s)) {
				Group group = GroupHandler.getGroupByName(arena, s);

				for (String s1 : config.getStringList(s + ".signs")) {
					String[] parts = s1.split(":");

					if (parts.length > 3) {
						World w = Bukkit.getWorld(parts[0]);

						if (w != null) {
							try {
								int x = Integer.parseInt(parts[1]);
								int y = Integer.parseInt(parts[2]);
								int z = Integer.parseInt(parts[3]);

								Location loc = new Location(w, x, y, z);

								Block b = loc.getBlock();

								if (b.getState() instanceof Sign) {
									new GroupSign(arena, group, (Sign) b.getState());
								}
							} catch (NumberFormatException ex) {
							}
						}
					}
				}
			}
		}
	}

	private static void saveArenaSignsUnsafe(Arena arena) throws Exception {
		File file = new File(arena.getDir() + "data", "arena-signs.yml");
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists()) {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
		} else {
			file.delete();
		}

		file.createNewFile();

		config.load(file);

		List<String> list = new ArrayList<String>();

		for (ArenaSign as : arena_signs) {
			if (as.getArena().equals(arena)) {
				String s = as.getSign().getWorld().getName() + ":" + as.getSign().getLocation().getBlockX() + ":" + as.getSign().getLocation().getBlockY() + ":" + as.getSign().getLocation().getBlockZ();
				list.add(s);
			}
		}

		config.set("signs", list);

		config.save(file);
	}
	
	private static void saveGroupSignsUnsafe(Arena arena) throws Exception {
		File file = new File(arena.getDir() + "data", "group-signs.yml");
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists()) {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
		} else {
			file.delete();
		}

		file.createNewFile();

		config.load(file);

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		for (GroupSign gs : group_signs) {
			if (gs.getArena().equals(arena)) {
				String s = gs.getSign().getWorld().getName() + ":" + gs.getSign().getLocation().getBlockX() + ":" + gs.getSign().getLocation().getBlockY() + ":" + gs.getSign().getLocation().getBlockZ();
				
				if (map.containsKey(gs.getGroup().getName())) {
					map.get(gs.getGroup().getName()).add(s);
				} else {
					map.put(gs.getGroup().getName(), Lists.newArrayList(s));
				}
			}
		}

		for (String s : map.keySet()) {
			config.set(s + ".signs", map.get(s));
		}

		config.save(file);
	}

}
