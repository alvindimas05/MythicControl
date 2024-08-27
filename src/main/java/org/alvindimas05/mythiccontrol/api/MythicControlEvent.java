package org.alvindimas05.mythiccontrol.api;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

@Getter
public abstract class MythicControlEvent extends PlayerEvent {
	private final NamespacedKey id;
	private final boolean registered;

	protected MythicControlEvent(Player player, NamespacedKey id, boolean registered) {
		super(player);
		this.id = id;
		this.registered = registered;
	}
}
