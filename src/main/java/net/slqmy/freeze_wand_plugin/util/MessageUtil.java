package net.slqmy.freeze_wand_plugin.util;

import org.jetbrains.annotations.NotNull;

public final class MessageUtil {
	public static @NotNull String translateChatColours(@NotNull final String message) {
		return message.replace('&', '\u00A7');
	}
}
