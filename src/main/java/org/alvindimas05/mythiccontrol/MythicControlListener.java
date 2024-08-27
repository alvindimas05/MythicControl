package org.alvindimas05.mythiccontrol;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class MythicControlListener implements PluginMessageListener {
	@Override
	public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
		// When receiving a handshake or key press, forward to their respective methods.
		if (channel.equalsIgnoreCase(MythicControlChannels.HANDSHAKE)) MythicControlNetwork.receiveGreeting(player, message);
		else if (channel.equalsIgnoreCase(MythicControlChannels.KEY_PRESS)) MythicControlNetwork.receiveKeyPress(player, message);
	}
}
