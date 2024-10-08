package org.alvindimas05.mythiccontrol.config;

import org.alvindimas05.mythiccontrol.MythicControlPlugin;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MythicControlConfig {
	private final Map<NamespacedKey, MythicControlInfo> keyInfoList = new HashMap<>();

	private boolean eventOnCommand;

	// Loads all the keys specified in config.yml and puts them in keyInfoList.
	public void reload(FileConfiguration config) {
		eventOnCommand = config.getBoolean("run_event_on_command");
		keyInfoList.clear();
		ConfigurationSection keys = config.getConfigurationSection("Controls");
		if (keys == null) return;
		for (String key : keys.getKeys(false)) {
			if (keys.contains(key, true)) {
				ConfigurationSection section = keys.getConfigurationSection(key);
				if (section == null) {
					error(key);
					return;
				}

				MythicControlInfo info = MythicControlInfo.from(section);
				if (info == null) {
					error(key);
					return;
				}

				keyInfoList.put(info.getId(), info);
			}
		}
	}

	private void error(String name) {
		MythicControlPlugin.get().getLogger().severe("Unable to add MythicKey: '" + name + "' - Check your syntax!");
	}
}
