package swordman.minigame.api.util;

/**
 * 
 * MinigameAPI
 * 
 * @author swordman407
 * 
 */
public enum GroupType {

	SPECTATING, PLAYING;

	/**
	 * 
	 * @param s
	 *            The string to match
	 * @return The grouptype, or null if not found
	 */
	public static GroupType matchByString(String s) {
		if (s.equalsIgnoreCase("spectating")) {
			return SPECTATING;
		} else if (s.equalsIgnoreCase("playing")) {
			return PLAYING;
		}
		return null;
	}

}
