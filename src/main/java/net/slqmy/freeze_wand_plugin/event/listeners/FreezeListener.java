package net.slqmy.freeze_wand_plugin.event.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import net.slqmy.freeze_wand_plugin.FreezeWandPlugin;
import net.slqmy.freeze_wand_plugin.language.LanguageManager;
import net.slqmy.freeze_wand_plugin.language.Message;

import java.util.List;
import java.util.UUID;

public final class FreezeListener implements Listener {

	private final FreezeWandPlugin plugin;

	public FreezeListener(@NotNull final FreezeWandPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onFreeze(@NotNull final PlayerInteractAtEntityEvent event) {
		if (event.getHand() == EquipmentSlot.OFF_HAND) {
			return;
		}

		final Entity rightClickedEntity = event.getRightClicked();

		if (!(rightClickedEntity instanceof Player)) {
			return;
		}

		final Player player = event.getPlayer();

		final ItemStack heldItem = player.getInventory().getItemInMainHand();
		final ItemMeta heldItemMeta = heldItem.getItemMeta();

		if (heldItemMeta != null && Boolean.TRUE.equals(heldItemMeta.getPersistentDataContainer().get(plugin.getIsItemFreezeWandKey(), PersistentDataType.BOOLEAN))) {
			final String immuneToFreezePermission = plugin.getConfig().getString("immune-to-freeze-permission");
			assert immuneToFreezePermission != null;

			final List<UUID> frozenPlayers = plugin.getFrozenPlayers();
			final LanguageManager languageManager = plugin.getLanguageManager();

			if (rightClickedEntity.hasPermission(immuneToFreezePermission)) {
				player.sendMessage(languageManager.getMessage(Message.PLAYER_IS_IMMUNE_TO_FREEZE, player));
				return;
			}

			UUID clickedEntityUuid = rightClickedEntity.getUniqueId();

			if (frozenPlayers.contains(clickedEntityUuid)) {
				frozenPlayers.remove(clickedEntityUuid);

				rightClickedEntity.sendMessage(languageManager.getMessage(Message.PLAYER_GOT_UNFROZEN, rightClickedEntity));
				player.sendMessage(languageManager.getMessage(Message.UNFREEZE_PLAYER, rightClickedEntity, rightClickedEntity.name()));
			} else {
				frozenPlayers.add(clickedEntityUuid);

				rightClickedEntity.sendMessage(languageManager.getMessage(Message.PLAYER_GOT_FROZEN, rightClickedEntity));
				player.sendMessage(languageManager.getMessage(Message.FREEZE_PLAYER, player, rightClickedEntity.name()));
			}
		}
	}
}
