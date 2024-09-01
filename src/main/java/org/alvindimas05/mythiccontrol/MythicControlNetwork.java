package org.alvindimas05.mythiccontrol;

import org.alvindimas05.mythiccontrol.api.MythicControlPressEvent;
import org.alvindimas05.mythiccontrol.api.MythicControlReleaseEvent;
import org.alvindimas05.mythiccontrol.config.MythicControlInfo;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MythicControlNetwork {
	public static final short DEFAULT_MAX_STRING_LENGTH = Short.MAX_VALUE;

	public static void receiveKeyPress(Player player, byte[] message) {
        // Read the key press ID then call the MythicControlPress event.
        String[] messages = new String(message, StandardCharsets.UTF_8).split("\\|");
        boolean firstPress = messages[1].equals("0");

        NamespacedKey id = NamespacedKey.fromString(messages[0].trim());

        if (MythicControlPlugin.get().getConf().getKeyInfoList().containsKey(id)) {
            MythicControlInfo info = MythicControlPlugin.get().getConf().getKeyInfoList().get(id);
            boolean eventCmd = MythicControlPlugin.get().getConf().isEventOnCommand();

            if (firstPress) {
                if (!info.runCommand(player) || eventCmd) Bukkit.getPluginManager().callEvent(new MythicControlPressEvent(player, id, true));
                if (info.hasMM(true)) info.mmSkill(player, true);
                return;
            }

            if (!info.hasCommand() || eventCmd) Bukkit.getPluginManager().callEvent(new MythicControlReleaseEvent(player, id, true));
            if (info.hasMM(false)) info.mmSkill(player, false);
        } else {
            Bukkit.getPluginManager()
                    .callEvent(firstPress ? new MythicControlPressEvent(player, id, false) : new MythicControlReleaseEvent(player, id, false));
        }
    }

	public static void receiveGreeting(Player player, byte[] message) {
		/* Send this server's specified keybindings to the
		 client. This is delayed to make sure the client is properly
		 connected before attempting to send any data over. */
		for (MythicControlInfo info : MythicControlPlugin.get().getConf().getKeyInfoList().values())
			sendKeyInformation(player, info.getId(), info.getDef(), info.getVoice(), info.getName(), info.getCategory(), info.getModifiers());

		player.sendMessage(MythicControlChannels.LOAD_KEYS);
	}

	// Fk you buffer, makes my life harder
	// Seperate with uncommon symbol "|"
	public static void sendKeyInformation(Player player, NamespacedKey id, Integer def, String voice,
                                          String name, String category, Set<ModifierKey> modifiers) {
		int[] modArray = modifiers.stream().mapToInt(ModifierKey::getId).toArray();
        String type;
        if(def != null){
            type = "type:key";
        } else if(voice != null){
            type = "type:voice";
        } else {
            Bukkit.getLogger().warning("Can't send this control because type is unknown: " + id.toString());
            return;
        }

		String message = MythicControlChannels.ADD_KEY + "|" + type + "|" + id.getNamespace() + ":" + id.getKey() + "|"
				+ name + "|" + category + "|" + def + "|" + voice + "|" + IntStream.of(modArray).mapToObj(Integer::toString)
				.collect(Collectors.joining(","));
		player.sendMessage(message);
	}
}
