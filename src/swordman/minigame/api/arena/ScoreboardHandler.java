package swordman.minigame.api.arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * 
 * MinigameAPI
 * 
 * @author swordman407
 * 
 */
public class ScoreboardHandler {

	/**
	 * Set a player's scoreboard
	 * 
	 * @param scoreboard
	 *            The scoreboard to set
	 * @param player
	 *            The player to set the scoreboard for
	 */
	public static void setScoreboard(Scoreboard scoreboard, Player player) {
		player.setScoreboard(scoreboard);
	}

	/**
	 * Clear a player's scoreboard
	 * 
	 * @param player
	 *            The player to clear the scoreboard for
	 */
	public static void clearScoreboard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		player.setScoreboard(scoreboard);
	}

	/**
	 * Add an objective to a scoreboard
	 * 
	 * @param scoreboard
	 *            The scoreboard to add the objective to
	 * @param name
	 *            The name of the objective
	 * @param criteria
	 *            The criteria (type) of the objective
	 * @param displayName
	 *            The displayname of the objective
	 * @param slot
	 *            The slot which the objective is displayed in
	 */
	public static void addObjective(Scoreboard scoreboard, String name, String criteria, String displayName, DisplaySlot slot) {
		Objective objective = scoreboard.registerNewObjective(name, criteria);

		objective.setDisplaySlot(slot);
		objective.setDisplayName(displayName);
	}

	/**
	 * Add an objective to a scoreboard
	 * 
	 * @param scoreboard
	 *            The scoreboard to add the objective to
	 * @param name
	 *            The name of the objective
	 * @param criteria
	 *            The criteria (type) of the objective
	 * @param displayName
	 *            The displayname of the objective
	 */
	public static void addObjective(Scoreboard scoreboard, String name, String criteria, String displayName) {
		Objective objective = scoreboard.registerNewObjective(name, criteria);

		objective.setDisplayName(displayName);
	}

	/**
	 * Add an objective to a scoreboard
	 * 
	 * @param scoreboard
	 *            The scoreboard to add the objective to
	 * @param name
	 *            The name of the objective
	 * @param criteria
	 *            The criteria (type) of the objective
	 */
	public static void addObjective(Scoreboard scoreboard, String name, String criteria) {
		scoreboard.registerNewObjective(name, criteria);
	}

	/**
	 * Set a player's score for an objective
	 * 
	 * @param objective The objective
	 * @param player The player to set the score for
	 * @param score The score to set
	 */
	public static void setScore(Objective objective, Player player, int score) {
		Score s = objective.getScore(player.getName());

		s.setScore(score);
	}

	/**
	 * Set a player's score for an objective by name
	 * 
	 * @param scoreboard The scoreboard
	 * @param objName The objective's name
	 * @param player The player to set the score for
	 * @param score The score to set
	 */
	public static void setScore(Scoreboard scoreboard, String objName, Player player, int score) {
		Objective objective = scoreboard.getObjective(objName);

		Score s = objective.getScore(player.getName());

		s.setScore(score);
	}

	/**
	 * Get a player's score
	 * 
	 * @param scoreboard The scoreboard
	 * @param objName The objective's name
	 * @param player The player to get the score from
	 * @return
	 */
	public static int getScore(Scoreboard scoreboard, String objName, Player player) {
		Objective objective = scoreboard.getObjective(objName);

		return objective.getScore(player.getName()).getScore();
	}

	/**
	 * Set a custom score for an objective
	 * 
	 * @param objective The objective which custom score will be set
	 * @param name The custom score's name
	 * @param score The score to set
	 */
	public static void setCustomScore(Objective objective, String name, int score) {
		Score s = objective.getScore(name);

		s.setScore(score);
	}

	/**
	 * Set a custom score for an objective by name
	 * 
	 * @param scoreboard The scoreboard
	 * @param objName The objective's name
	 * @param name The score's name
	 * @param score The score to set
	 */
	public static void setCustomScore(Scoreboard scoreboard, String objName, String name, int score) {
		Objective objective = scoreboard.getObjective(objName);

		Score s = objective.getScore(name);

		s.setScore(score);
	}
}
