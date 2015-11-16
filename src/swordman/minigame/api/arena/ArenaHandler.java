package swordman.minigame.api.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.NameTagVisibility;

import swordman.minigame.api.group.Group;
import swordman.minigame.api.group.GroupHandler;
import swordman.minigame.api.sign.SignHandler;
import swordman.minigame.api.timer.CountdownTimer;
import swordman.minigame.api.util.GroupType;

/**
 * 
 * MinigameAPI
 * 
 * @author swordman407
 * 
 */
public class ArenaHandler {

	private static HashMap<Plugin, List<Arena>> registeredArenas = new HashMap<Plugin, List<Arena>>();

	private static List<Plugin> plugins = new ArrayList<Plugin>();

	/**
	 * Register an arena
	 * 
	 * @param plugin
	 *            The plugin which the arena is set for
	 * @param arena
	 *            The arena to register
	 */
	public static void registerArena(Plugin plugin, Arena arena) {
		if (!registeredArenas.containsKey(plugin)) {
			registeredArenas.put(plugin, new ArrayList<Arena>());
		}

		if (!registeredArenas.get(plugin).contains(arena)) {
			registeredArenas.get(plugin).add(arena);
		}

		if (!plugins.contains(plugin)) {
			plugins.add(plugin);
		}
	}

	/**
	 * Unregister an arena
	 * 
	 * @param plugin
	 *            The plugin which the arena will be unregistered for
	 * @param arena
	 *            The arena to unregister
	 */
	public static void unregisterArena(Plugin plugin, Arena arena) {
		if (registeredArenas.containsKey(plugin) && registeredArenas.get(plugin).contains(arena)) {
			GroupHandler.unregisterAll(arena);
			registeredArenas.get(plugin).remove(arena);
		}
	}

	/**
	 * Get an arena by name
	 * 
	 * @param plugin
	 *            The plugin the arena is registered for
	 * @param name
	 *            The name of the arena
	 * @return The arena, or null if not found
	 */
	public static Arena getArenaByName(Plugin plugin, String name) {
		if (registeredArenas.containsKey(plugin)) {
			for (Arena arena : registeredArenas.get(plugin)) {
				if (arena.getName().equalsIgnoreCase(name)) {
					return arena;
				}
			}
		}
		return null;
	}

	public static String getDirectory(Arena a) {
		return a.getDir();
	}

	/**
	 * Get the registered arenas
	 * 
	 * @param plugin
	 *            The plugin to get the arenas for
	 * @return The registered arenas of a plugin, or null if none are registered
	 */
	public static List<Arena> getRegisteredArenas(Plugin plugin) {
		if (registeredArenas.get(plugin) == null) {
			return new ArrayList<Arena>();
		}
		return registeredArenas.get(plugin);
	}

	public static Collection<List<Arena>> getAllArenas() {
		return registeredArenas.values();
	}

	/**
	 * Check if an arena exists
	 * 
	 * @param plugin
	 *            The plugin to check the arena for
	 * @param arena
	 *            The arena's name to check
	 * @return
	 */
	public static boolean exists(Plugin plugin, String arena) {
		if (registeredArenas.containsKey(plugin)) {
			for (Arena a : registeredArenas.get(plugin)) {
				if (a.getName().equalsIgnoreCase(arena)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Destroy an arena
	 * 
	 * @param plugin
	 *            The plugin which arena is registered for
	 * @param arena
	 *            The arena
	 */
	public static void destroyArena(Plugin plugin, String arena) {
		if (exists(plugin, arena)) {
			Arena a = getArenaByName(plugin, arena);

			File file = new File(a.getDir());

			for (File f : file.listFiles()) {
				if (!f.delete()) {
					a.getPlugin().getLogger().log(Level.INFO, "File " + f.getPath() + " could not be deleted");
				}
			}

			if (!file.delete()) {
				a.getPlugin().getLogger().log(Level.INFO, "File " + file.getPath() + " could not be deleted");
			}

			unregisterArena(plugin, a);
		}
	}

	public static void startCountdownTimer(Arena arena, int seconds) {
		if (!arena.isStarting()) {
			CountdownTimer timer = new CountdownTimer(arena, seconds);
			BukkitTask task = Bukkit.getScheduler().runTaskTimer(arena.getPlugin(), timer, 0L, 20L);

			timer.setTask(task);
		}
	}

	public static void startCountdownTimer(Arena arena, int seconds, String message) {
		if (!arena.isStarting()) {
			CountdownTimer timer = new CountdownTimer(arena, seconds, message);
			BukkitTask task = Bukkit.getScheduler().runTaskTimer(arena.getPlugin(), timer, 0L, 20L);

			timer.setTask(task);
		}
	}

	/**
	 * Save an arena to the config
	 * 
	 * @param plugin
	 *            The plugin which the arena is saved for
	 * @param arena
	 *            The arena to save
	 */
	public static void saveToConfig(Arena arena) {
		try {
			saveToConfigUnsafe(arena);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Load an arena from the config
	 * 
	 * @param plugin
	 *            The plugin which the arena is loaded for
	 * @param arena
	 *            The arena to load
	 */
	public static void loadFromConfig(Plugin plugin, String arena) {
		try {
			loadFromConfigUnsafe(plugin, arena);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save all arena's to the config
	 */
	public static void saveAllToConfig() {
		Iterator<Plugin> it = registeredArenas.keySet().iterator();

		while (it.hasNext()) {
			Plugin plugin = it.next();

			for (Arena arena : registeredArenas.get(plugin)) {
				saveToConfig(arena);
			}
		}
	}

	/**
	 * Load all arena's from the config
	 */
	public static void loadAllFromConfig() {
		try {
			loadAllFromConfigUnsafe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load all arena's from the config
	 * 
	 * @throws Exception
	 */
	private static void loadAllFromConfigUnsafe() throws Exception {
		File file = new File("plugins/MinigameAPI/plugins.yml");

		if (!file.exists()) {
			if (file.getParentFile().mkdirs()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			return;
		}

		YamlConfiguration config = new YamlConfiguration();

		config.load(file);

		List<String> plugins = config.getStringList("plugins");

		config.save(file);

		for (String p : plugins) {
			if (Bukkit.getPluginManager().getPlugin(p) != null) {
				Plugin plugin = Bukkit.getPluginManager().getPlugin(p);
				File f = new File(plugin.getDataFolder().getPath() + "/arenas/");

				for (File arena : f.listFiles()) {
					if (arena.isDirectory()) {
						String a = arena.getName();

						File arenaConfig = new File(arena.getPath() + "/data/data.yml");

						if (arenaConfig.exists()) {
							loadFromConfig(plugin, a);
						}
					}
				}
			}

		}
	}

	/**
	 * Save the data of an arena to the config
	 * 
	 * @param plugin
	 *            The plugin which the arena is saved for
	 * @param arena
	 *            The arena
	 * @throws Exception
	 */
	private static void saveToConfigUnsafe(Arena arena) throws Exception {
		File file = new File(arena.getDir() + "data", "data.yml");
		YamlConfiguration config = new YamlConfiguration();

		File pFile = new File("plugins/MinigameAPI/plugins.yml");

		YamlConfiguration pConfig = new YamlConfiguration();

		if (!pFile.exists()) {
			if (pFile.getParentFile() != null) {
				pFile.getParentFile().mkdirs();
			}
			pFile.createNewFile();
		}

		pConfig.load(pFile);

		List<String> list = pConfig.getStringList("plugins");

		for (Plugin p : plugins) {
			if (!list.contains(p.getName()))
				list.add(p.getName());
		}

		pConfig.set("plugins", list);

		pConfig.save(pFile);

		if (!file.exists()) {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}

		config.load(file);

		config.set("max", arena.getMax());
		config.set("min", arena.getMin());

		if (arena.getLobby() != null) {
			String w = arena.getLobby().getWorld().getName();

			double x = arena.getLobby().getX();
			double y = arena.getLobby().getY();
			double z = arena.getLobby().getZ();
			float yaw = arena.getLobby().getYaw();
			float pitch = arena.getLobby().getPitch();

			config.set("lobby", w + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch);
		}

		if (arena.getDefaultGroup() != null) {
			config.set("default-group", arena.getDefaultGroup().getName());
		}

		for (Group g : arena.getGroups()) {
			if (g.getName() != null) {
				config.set("groups." + g.getName() + ".name", g.getName());

				if (g.getType() != null) {
					config.set("groups." + g.getName() + ".type", g.getType().toString());
				}

				if (g.getColor() != null) {
					config.set("groups." + g.getName() + ".color", g.getColor().getChar());
				}

				if (g.getMax() > 0) {
					config.set("groups." + g.getName() + ".max", g.getMax());
				}

				if (g.getTeam() != null) {
					config.set("groups." + g.getName() + ".team", g.getTeam().getName());

					config.set("groups." + g.getName() + ".friendly-fire", g.getAllowFriendlyFire());
					config.set("groups." + g.getName() + ".see-invisibles", g.getCanSeeFriendlyInvisibles());

					if (g.getDisplayName() != null) {
						config.set("groups." + g.getName() + ".display-name", g.getDisplayName());
					}

					if (g.getPrefix() != null) {
						config.set("groups." + g.getName() + ".prefix", g.getPrefix());
					}

					if (g.getSuffix() != null) {
						config.set("groups." + g.getName() + ".suffix", g.getSuffix());
					}

					if (g.getTeam().getNameTagVisibility() != null) {
						config.set("groups." + g.getName() + ".nametag-visibility", g.getTeam().getNameTagVisibility().toString());
					}
				}

				if (g.getSpawn() != null) {
					String w = g.getSpawn().getWorld().getName();

					double x = g.getSpawn().getX();
					double y = g.getSpawn().getY();
					double z = g.getSpawn().getZ();
					float yaw = g.getSpawn().getYaw();
					float pitch = g.getSpawn().getPitch();

					config.set("groups." + g.getName() + ".spawn", w + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch);
				}

				if (!g.getSpawnpoints().isEmpty()) {
					List<String> locations = new ArrayList<String>();

					for (int i = 0; i < g.getSpawnpoints().size(); i++) {
						Location l = g.getSpawnpoints().get(i);

						String w = l.getWorld().getName();

						double x = l.getX();
						double y = l.getY();
						double z = l.getZ();

						float yaw = l.getYaw();
						float pitch = l.getPitch();

						locations.add(w + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch);
					}

					config.set("groups." + g.getName() + ".spawnpoints", locations);
				}

			}
		}

		SignHandler.saveArenaSigns(arena);
		SignHandler.saveGroupSigns(arena);

		config.save(file);
	}

	/**
	 * Load arena data from the config
	 * 
	 * @param plugin
	 *            The plugin which the arena is loaded for
	 * @param arena
	 *            The arena to load
	 * @throws Exception
	 */
	private static void loadFromConfigUnsafe(Plugin plugin, String arena) throws Exception {
		File file = new File(plugin.getDataFolder().getPath() + "/arenas/" + arena + "/data", "data.yml");
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists()) {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
			return;
		}

		config.load(file);

		int max;
		if (config.contains("max")) {
			max = config.getInt("max");
		} else {
			config.save(file);
			return;
		}

		Arena a = new Arena(plugin, arena, max);

		if (config.contains("min")) {
			int min = config.getInt("min");
			a.setMin(min);
		}

		if (config.contains("lobby")) {
			String[] parts = config.getString("lobby").split(":");

			World w = Bukkit.getWorld(parts[0]);

			double x = Double.parseDouble(parts[1]);
			double y = Double.parseDouble(parts[2]);
			double z = Double.parseDouble(parts[3]);

			float yaw = Float.parseFloat(parts[4]);
			float pitch = Float.parseFloat(parts[5]);

			Location l = new Location(w, x, y, z, yaw, pitch);
			a.setLobby(l);
		}

		if (config.contains("groups")) {
			for (String group : config.getConfigurationSection("groups").getKeys(false)) {
				if (config.contains("groups." + group + ".name") && config.contains("groups." + group + ".type") && config.contains("groups." + group + ".max")) {
					String name = config.getString("groups." + group + ".name");

					GroupType type = GroupType.matchByString(config.getString("groups." + group + ".type"));

					ChatColor color = ChatColor.getByChar(config.getString("groups." + group + ".color"));

					int max1 = config.getInt("groups." + group + ".max");

					Group g = new Group(type, name, color, a, max1);

					if (config.contains("groups." + group + ".team")) {
						if (config.contains("groups." + group + ".see-invisibles")) {
							g.setCanSeeFriendlyInvisibles(config.getBoolean("groups." + group + ".see-invisibles"));
						}

						if (config.contains("groups." + group + ".friendly-fire")) {
							g.setAllowFriendlyFire(config.getBoolean("groups." + group + ".friendly-fire"));
						}

						if (config.contains("groups." + group + ".display-name")) {
							g.setDisplayName(config.getString("groups." + group + ".display-name"));
						}

						if (config.contains("groups." + group + ".prefix")) {
							g.setPrefix(config.getString("groups." + group + ".prefix"));
						}

						if (config.contains("groups." + group + ".suffix")) {
							g.setSuffix(config.getString("groups." + group + ".suffix"));
						}

						if (config.contains("groups." + group + ".nametag-visibility")) {
							g.getTeam().setNameTagVisibility(NameTagVisibility.valueOf(config.getString("groups." + group + ".nametag-visibility")));
						}
					}

					if (config.contains("groups." + group + ".spawn")) {
						String[] parts = config.getString("groups." + group + ".spawn").split(":");

						World w = Bukkit.getWorld(parts[0]);

						double x = Double.parseDouble(parts[1]);
						double y = Double.parseDouble(parts[2]);
						double z = Double.parseDouble(parts[3]);

						float yaw = Float.parseFloat(parts[4]);
						float pitch = Float.parseFloat(parts[5]);

						Location l = new Location(w, x, y, z, yaw, pitch);

						g.setSpawn(l);
					}

					if (config.contains("groups." + group + ".spawnpoints")) {
						List<String> locs = config.getStringList("groups." + group + ".spawnpoints");

						for (String s1 : locs) {
							String[] parts = s1.split(":");

							World w = Bukkit.getWorld(parts[0]);

							double x = Double.parseDouble(parts[1]);
							double y = Double.parseDouble(parts[2]);
							double z = Double.parseDouble(parts[3]);

							float yaw = Float.parseFloat(parts[4]);
							float pitch = Float.parseFloat(parts[5]);

							Location l = new Location(w, x, y, z, yaw, pitch);

							g.addSpawnpoint(l);
						}
					}
				} else {
					config.save(file);
					return;
				}
			}
		}

		if (config.contains("default-group")) {
			String defaultGroup = config.getString("default-group");

			for (Group g : a.getGroups()) {
				if (g.getName().equalsIgnoreCase(defaultGroup)) {
					a.setDefaultGroup(g);
					break;
				}
			}
		}

		SignHandler.loadArenaSigns(a);
		SignHandler.loadGroupSigns(a);
	}
}
