package dev.esoteric_enderman.freeze_wand_plugin.commands;

import dev.esoteric_enderman.freeze_wand_plugin.FreezeWandPlugin;
import dev.esoteric_enderman.freeze_wand_plugin.language.LanguageManager;
import dev.esoteric_enderman.freeze_wand_plugin.language.Message;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GiveFreezeWandCommand extends CommandAPICommand {


    public GiveFreezeWandCommand(@NotNull final FreezeWandPlugin plugin) {
        super("give-freeze-wand");

        withAliases("gfw", "fw", "give-freeze", "give-wand", "freeze-wand", "freeze");
        withPermission(plugin.getConfig().getString("give-freeze-wand-permission"));

        final LanguageManager languageManager = plugin.getLanguageManager();

        final String playerArgumentNodeName = "player";

        withArguments(new PlayerArgument(playerArgumentNodeName).setOptional(true));

        executesPlayer((executor) -> {
            final Player sender = executor.sender();
            Player target = (Player) executor.args().get(playerArgumentNodeName);

            if (target == null) {
                target = executor.sender();
            }

            final ItemStack freezeWand = new ItemStack(Material.STICK);
            final ItemMeta wandMeta = freezeWand.getItemMeta();
            wandMeta.displayName(languageManager.getMessage(Message.FREEZE_WAND_ITEM_NAME, sender));
            wandMeta.lore(List.of(languageManager.getMessage(Message.FREEZE_WAND_ITEM_LORE, sender)));
            wandMeta.getPersistentDataContainer().set(plugin.getIsItemFreezeWandKey(), PersistentDataType.BOOLEAN, true);
            freezeWand.setItemMeta(wandMeta);

            target.getInventory().addItem(freezeWand);

            final Component itemName = freezeWand.displayName();

            if (!target.equals(sender)) {
                sender.sendMessage(languageManager.getMessage(Message.GAVE_FREEZE_WAND, sender, itemName, target.name()));
            }

            target.sendMessage(languageManager.getMessage(Message.RECEIVE_FREEZE_WAND, target, itemName));
        });

        register();
    }
}
