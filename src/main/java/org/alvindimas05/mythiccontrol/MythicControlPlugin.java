package org.alvindimas05.mythiccontrol;

import org.alvindimas05.mythiccontrol.cmd.MythicControlCommand;
import org.alvindimas05.mythiccontrol.config.MythicControlConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class MythicControlPlugin extends JavaPlugin {
	private static MythicControlPlugin plugin;

	@Getter
	private final MythicControlConfig conf = new MythicControlConfig();
	private final MythicControlListener mkl = new MythicControlListener();

	public boolean papi, mm;

	@Override
	public void onEnable() {
		plugin = this;
		// Register all the channels needed for incoming/outgoing packets.
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, MythicControlChannels.HANDSHAKE, mkl);
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, MythicControlChannels.KEY_PRESS, mkl);
//		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, MythicControlChannels.ADD_KEY);
//		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, MythicControlChannels.LOAD_KEYS);

		// Set up command
		MythicControlCommand cmd = new MythicControlCommand();
		PluginCommand command = getCommand("mythiccontrol");
		if (command != null) {
			command.setExecutor(cmd);
			command.setTabCompleter(cmd);
		}

		papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
		mm = Bukkit.getPluginManager().getPlugin("MythicMobs") != null;

		saveDefaultConfig();
		reload();
	}

	@Override
	public void onDisable() {
		// Unregister the channels
		Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin);
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin);
		plugin = null;
	}

	// mythiccontrol reload
	public void reload() {
		reloadConfig();
		conf.reload(getConfig());
	}

	public static MythicControlPlugin get() {
		return plugin;
	}
}
