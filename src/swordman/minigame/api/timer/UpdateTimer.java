package swordman.minigame.api.timer;

import swordman.minigame.api.sign.ArenaSign;
import swordman.minigame.api.sign.GroupSign;
import swordman.minigame.api.sign.SignHandler;

public class UpdateTimer implements Runnable {

	public void run() {
		for (ArenaSign s : SignHandler.getArenaSigns()) {
			s.update();
		}
		for (GroupSign s : SignHandler.getGroupSigns()) {
			s.update();
		}
	}

}
