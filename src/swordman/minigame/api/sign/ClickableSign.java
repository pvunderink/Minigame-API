package swordman.minigame.api.sign;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface ClickableSign {
	
	public Sign getSign();
	
	public void update();
	
	public void click(Player player);

}
