package net.slqmy.freeze_wand_plugin.commands;

import net.kyori.adventure.text.Component;
import net.slqmy.freeze_wand_plugin.FreezeWandPlugin;
import net.slqmy.freeze_wand_plugin.language.LanguageManager;
import net.slqmy.freeze_wand_plugin.language.Message;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GiveFreezeWandCommand implements CommandExecutor {

	private final FreezeWandPlugin plugin;

	public GiveFreezeWandCommand(@NotNull final FreezeWandPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(
			@NotNull final CommandSender sender,
			@NotNull final Command command,
			@NotNull final String label,
			final @NotNull String @NotNull [] args) {
		if (args.length > 1) {
			return false;
		}

		final Player target;

		if (args.length == 1) {
			final String playerName = args[0];

			target = Bukkit.getPlayer(playerName);

			if (target == null) {
				return false;
			}
		} else {
			if (sender instanceof Player) {
				target = ((Player) sender);
			} else {
				return false;
			}
		}

		final String permission = plugin.getConfig().getString("give-freeze-wand-permission");
		assert permission != null;

		final LanguageManager languageManager = plugin.getLanguageManager();

		if (!sender.hasPermission(permission)) {
			sender.sendMessage(languageManager.getMessage(Message.NO_PERMISSION, sender));
			return true;
		}

		final ItemStack freezeWand = new ItemStack(Material.STICK);
		final ItemMeta wandMeta = freezeWand.getItemMeta();
		wandMeta.displayName(languageManager.getMessage(Message.FREEZE_WAND_ITEM_NAME, sender));
		wandMeta.lore(List.of(languageManager.getMessage(Message.FREEZE_WAND_ITEM_LORE, sender)));
		wandMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "is_freeze_wand"),
				PersistentDataType.BOOLEAN, true);
		freezeWand.setItemMeta(wandMeta);

		target.getInventory().addItem(freezeWand);

		final Component itemName = freezeWand.displayName();

		if (!target.equals(sender)) {
			sender.sendMessage(languageManager.getMessage(Message.GAVE_FREEZE_WAND, sender, itemName,
					Component.text(target.getName())));
		}

		target.sendMessage(languageManager.getMessage(Message.RECEIVE_FREEZE_WAND, target, itemName));
		return true;
	}
}
