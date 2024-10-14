package dev.enderman.minecraft.plugins.freezewand.event.listeners;

import dev.enderman.minecraft.plugins.freezewand.FreezeWandPlugin;
import dev.enderman.minecraft.plugins.freezewand.language.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerMoveListener implements Listener {

    private final FreezeWandPlugin plugin;

    public PlayerMoveListener(@NotNull final FreezeWandPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(@NotNull final PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if (plugin.getFrozenPlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);

            player.sendMessage(plugin.getLanguageManager().getMessage(Message.PLAYER_CANT_MOVE, player));
        }
    }
}
