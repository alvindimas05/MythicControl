package org.alvindimas05.mythiccontrol.api;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MythicControlReleaseEvent extends MythicControlEvent {
	@Getter
	private static final HandlerList handlerList = new HandlerList();

	public MythicControlReleaseEvent(Player player, NamespacedKey id, boolean registered) {
		super(player, id, registered);
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
}