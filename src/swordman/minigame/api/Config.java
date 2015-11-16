package swordman.minigame.api;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	
	File file = new File("plugins/MinigameAPI/config.yml");
	YamlConfiguration config = new YamlConfiguration();
	
	boolean saveArenasOnDisable;
	
	public void load()  {
		try {
			loadUnsafe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadUnsafe() throws Exception {
		if (!file.exists()) {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		
		config.load(file);

		config.set("save-arenas-on-disable", config.getBoolean("save-arenas-on-disable", true));
		
		saveArenasOnDisable = config.getBoolean("save-arenas-on-disable", true);
		
		config.save(file);
	}

	public boolean saveArenasOnDisable() {
		return saveArenasOnDisable;
	}

	public void setSaveArenasOnDisable(boolean saveArenasOnDisable) {
		this.saveArenasOnDisable = saveArenasOnDisable;
	}

}
