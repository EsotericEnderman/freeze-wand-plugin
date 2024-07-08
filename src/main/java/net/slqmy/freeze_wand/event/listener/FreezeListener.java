package net.slqmy.freeze_wand.event.listener;

import net.slqmy.freeze_wand.FreezeWandPlugin;
import net.slqmy.freeze_wand.enums.Message;
import net.slqmy.freeze_wand.manager.MessageManager;
import org.bukkit.NamespacedKey;
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

		if (heldItemMeta != null &&
		    Boolean.TRUE.equals(
						    heldItemMeta
										    .getPersistentDataContainer()
										    .get(new NamespacedKey(plugin, "is_freeze_wand"), PersistentDataType.BOOLEAN)
		    )
		) {
			final String immuneToFreezePermission = plugin.getConfig().getString("immune-to-freeze-permission");
			assert immuneToFreezePermission != null;

			final List<UUID> frozenPlayers = plugin.getFrozenPlayers();
			final MessageManager messageManager = plugin.getMessageManager();

			if (rightClickedEntity.hasPermission(immuneToFreezePermission)) {
				player.sendMessage(messageManager.getMessage(Message.PLAYER_IS_IMMUNE_TO_FREEZE));
				return;
			}

			if (frozenPlayers.contains(rightClickedEntity.getUniqueId())) {
				frozenPlayers.remove(rightClickedEntity.getUniqueId());

				rightClickedEntity.sendMessage(messageManager.getMessage(Message.PLAYER_GOT_UNFROZEN));
				player.sendMessage(messageManager.getMessage(Message.UNFREEZE_PLAYER, rightClickedEntity.getName()));
			} else {
				frozenPlayers.add(rightClickedEntity.getUniqueId());

				rightClickedEntity.sendMessage(messageManager.getMessage(Message.PLAYER_GOT_FROZEN));
				player.sendMessage(messageManager.getMessage(Message.FREEZE_PLAYER, rightClickedEntity.getName()));
			}
		}
	}
}
