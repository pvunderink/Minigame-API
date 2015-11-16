package swordman.minigame.api.group;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import swordman.minigame.api.arena.Arena;

import com.google.common.collect.Lists;

/**
 * 
 * MinigameAPI
 * 
 * @author swordman407
 *
 */
public class GroupHandler {

	private static HashMap<Arena, List<Group>> registeredGroups = new HashMap<Arena, List<Group>>();

	/**
	 * Register a group for an arena
	 * 
	 * @param group
	 *            The group to register
	 * @param arena
	 *            The arena to register the group for
	 */
	public static void registerGroup(Group group, Arena arena) {
		if (!registeredGroups.containsKey(arena)) {
			registeredGroups.put(arena, Lists.newArrayList(group));
		} else if (!registeredGroups.get(arena).contains(group)) {
			registeredGroups.get(arena).add(group);
		}
		arena.updateGroups();
	}

	/**
	 * Unregister a group from an arena
	 * 
	 * @param group
	 *            The group to unregister
	 * @param arena
	 *            The arena to unregister the group for
	 */
	public static void unregisterGroup(Group group, Arena arena) {
		if (registeredGroups.containsKey(arena)) {
			registeredGroups.get(arena).remove(group);
			arena.updateGroups();

			File file = new File(arena.getDir() + "data/", "data.yml");

			if (file.exists()) {
				YamlConfiguration config = new YamlConfiguration();

				try {
					config.load(file);

					if (config.contains("groups." + group.getName())) {
						config.set("groups." + group.getName(), null);
					}

					config.save(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get a group by name
	 * 
	 * @param arena
	 *            The arena to get the group from
	 * @param name
	 *            The group's name
	 * @return The group, or null if not found
	 */
	public static Group getGroupByName(Arena arena, String name) {
		for (Group group : registeredGroups.get(arena)) {
			if (group.getName().equalsIgnoreCase(name)) {
				return group;
			}
		}
		return null;
	}

	/**
	 * Unregister all groups from an arena
	 * 
	 * @param arena
	 *            The arena to unregister all groups from
	 */
	public static void unregisterAll(Arena arena) {
		if (registeredGroups.containsKey(arena)) {
			for (Group g : registeredGroups.get(arena)) {
				unregisterGroup(g, arena);
			}
		}
	}

	/**
	 * Check if an arena has a certain group
	 * 
	 * @param arena
	 *            The arena to check the group from
	 * @param group
	 *            The group to check
	 */
	public static boolean containsGroup(Arena arena, String group) {
		if (registeredGroups.containsKey(arena)) {
			for (int i = 0; i < registeredGroups.get(arena).size(); i++) {
				if (registeredGroups.get(arena).get(i).getName().equalsIgnoreCase(group)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the groups from an arena
	 * 
	 * @param arena
	 *            The arena to get the groups from
	 * @return The groups, or an empty list
	 */
	public static List<Group> getGroups(Arena arena) {
		if (registeredGroups.containsKey(arena)) {
			return registeredGroups.get(arena);
		}
		return null;
	}

	/**
	 * Set the groups for an arena
	 * 
	 * @param arena
	 *            The arena to set the groups for
	 * @param groups
	 *            The new groups of the arena
	 */
	public static void setGroups(Arena arena, List<Group> groups) {
		if (registeredGroups.containsKey(arena)) {
			unregisterAll(arena);
			registeredGroups.put(arena, groups);
		} else {
			registeredGroups.put(arena, groups);
		}
	}

	/**
	 * Get the map
	 * 
	 * @return The hashmap with all registered groups
	 */
	public static HashMap<Arena, List<Group>> getMap() {
		return registeredGroups;
	}
}
