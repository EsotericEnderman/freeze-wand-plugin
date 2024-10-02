package dev.esoteric_enderman.freeze_wand_plugin;

import dev.esoteric_enderman.freeze_wand_plugin.commands.GiveFreezeWandCommand;
import dev.esoteric_enderman.freeze_wand_plugin.commands.SetLanguageCommand;
import dev.esoteric_enderman.freeze_wand_plugin.data.player.PlayerDataManager;
import dev.esoteric_enderman.freeze_wand_plugin.event.listeners.FreezeListener;
import dev.esoteric_enderman.freeze_wand_plugin.event.listeners.PlayerMoveListener;
import dev.esoteric_enderman.freeze_wand_plugin.language.LanguageManager;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public final class FreezeWandPlugin extends JavaPlugin {

    private final NamespacedKey isItemFreezeWandKey = new NamespacedKey(this, "is_freeze_wand");
    private final ArrayList<UUID> frozenPlayers = new ArrayList<>();
    private PlayerDataManager playerDataManager;
    private LanguageManager languageManager;

    public NamespacedKey getIsItemFreezeWandKey() {
        return isItemFreezeWandKey;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public ArrayList<UUID> getFrozenPlayers() {
        return frozenPlayers;
    }

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        saveDefaultConfig();

        CommandAPIBukkitConfig commandAPIConfig = new CommandAPIBukkitConfig(this);

        CommandAPI.onLoad(commandAPIConfig);
        CommandAPI.onEnable();

        playerDataManager = new PlayerDataManager(this);
        languageManager = new LanguageManager(this);

        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new FreezeListener(this), this);
        pluginManager.registerEvents(new PlayerMoveListener(this), this);

        new SetLanguageCommand(this);

        new GiveFreezeWandCommand(this);
    }
}
