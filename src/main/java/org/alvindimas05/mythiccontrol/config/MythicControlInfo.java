package org.alvindimas05.mythiccontrol.config;

import org.alvindimas05.mythiccontrol.MythicControlPlugin;
import org.alvindimas05.mythiccontrol.ModifierKey;
import org.alvindimas05.mythiccontrol.compat.MythicMobsCompat;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Getter
public class MythicControlInfo {
	private MythicControlInfo(NamespacedKey id, String name, String category, @Nullable Integer def, @Nullable String voice,
							  Set<ModifierKey> modifiers, String command, String mythicPress, String mythicRelease) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.def = def;
		this.voice = voice;
		this.modifiers = modifiers;
		this.command = command;
		this.mythicPress = mythicPress;
		this.mythicRelease = mythicRelease;
	}

	private final NamespacedKey id;
	private final String name, category, command, mythicPress, mythicRelease;
	@Nullable
	private final Integer def;
	@Nullable
	private final String voice;
	private final Set<ModifierKey> modifiers;

	// Using a static method to insert KeyInfo verification code.
	public static @Nullable MythicControlInfo from(ConfigurationSection config) {
		if (config.contains("Id")) {
			NamespacedKey key = NamespacedKey.fromString(config.getString("Id", ""), MythicControlPlugin.get());
			if (key == null) return null;

			String name;
			String category;
			Integer defaultKey = null;
			String voice = null;

			if (config.contains("Name") && config.contains("Category")) {
				name = config.getString("Name");
				category = config.getString("Category");
				if(config.contains("DefaultKey")){
					defaultKey = config.getInt("DefaultKey");
				} else if(config.contains("Voice")){
					voice = config.getString("Voice");
				}
			} else if (key.getNamespace().equals("minecraft")) {
				name = "name";
				category = "category";
				defaultKey = 0;
			} else {
				return null;
			}

			Set<ModifierKey> modifiers = new HashSet<>();
			for (String modifier : config.getStringList("Modifiers")) {
				ModifierKey modKey = ModifierKey.fromString(modifier);
				if (modKey != ModifierKey.NONE) modifiers.add(modKey);
			}

			return new MythicControlInfo(key, name, category, defaultKey, voice, modifiers, config.getString("RunCommand", ""),
					config.getString("SkillPress", ""), config.getString("SkillRelease", ""));
		}

		return null;
	}

	public boolean runCommand(Player player) {
		if (command == null || command.isEmpty()) return false;

		final boolean isAdmin = command.startsWith("!");
		String cmd = (isAdmin ? command.substring(1) : command).replace("%player%", player.getName());
		if (MythicControlPlugin.get().papi) cmd = PlaceholderAPI.setPlaceholders(player, cmd);

		if (isAdmin) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
		else Bukkit.dispatchCommand(player, cmd);
		return true;
	}

	public boolean hasCommand() {
		return checkStr(command);
	}

	public boolean hasMM(boolean press) {
		return press ? checkStr(mythicPress) : checkStr(mythicRelease);
	}

	private boolean checkStr(String str) {
		return str != null && !str.isEmpty();
	}

	public void mmSkill(Player player, boolean press) {
		if (!MythicControlPlugin.get().mm) return;
		MythicMobsCompat.runSkill(press ? mythicPress : mythicRelease, player);
	}
}
