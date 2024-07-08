package net.slqmy.freeze_wand.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.slqmy.freeze_wand.FreezeWandPlugin;
import net.slqmy.freeze_wand.enums.Message;
import net.slqmy.freeze_wand.util.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class MessageManager {
	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\d+}");


	private final YamlConfiguration messages;

	private final Cache<Message, TextComponent> cachedMessages = CacheBuilder.newBuilder()
	                                                                     .expireAfterWrite(45, TimeUnit.SECONDS)
	                                                                     .build();

	public MessageManager(@NotNull final FreezeWandPlugin plugin) {
		final File messagesFile = new File(plugin.getDataFolder(), "lang/" + plugin.getConfig().getString("language") + ".yml");

		messages = YamlConfiguration.loadConfiguration(messagesFile);
	}

	public @NotNull TextComponent getMessage(@NotNull final Message key, @NotNull final Component @NotNull ... placeHolderValues) {
		if (cachedMessages.asMap().containsKey(key)) {
			return cachedMessages.asMap().get(key);
		}

		final String message = messages.getString(key.toString());
		assert message != null;

		final String translatedMessage = MessageUtil.translateChatColours(message);

		final String[] messageArray = PLACEHOLDER_PATTERN.split(translatedMessage, placeHolderValues.length + 1);

		TextComponent newMessage = Component.text(messageArray[0]);

		for (int i = 0; i < placeHolderValues.length; i++) {
			newMessage = newMessage.append(placeHolderValues[i]).append(Component.text(messageArray[i + 1]));
		}

		cachedMessages.put(key, newMessage);
		return newMessage;
	}

	public @NotNull TextComponent getMessage(@NotNull final Message key, @NotNull final String @NotNull ... placeHolderValues) {
		return getMessage(key,Arrays.stream(placeHolderValues).map(Component::text).toArray(
						TextComponent[]::new));
	}

	public @NotNull TextComponent getMessage(@NotNull final Message key) {
		return getMessage(key, new TextComponent[]{});
	}
}
