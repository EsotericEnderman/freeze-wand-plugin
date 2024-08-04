package net.slqmy.freeze_wand_plugin;

import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import net.slqmy.freeze_wand_plugin.commands.SetLanguageCommand;
import net.slqmy.freeze_wand_plugin.data.player.PlayerDataManager;
import net.slqmy.freeze_wand_plugin.language.LanguageManager;

import java.util.ArrayList;
import java.util.UUID;

public final class FreezeWandPlugin extends JavaPlugin {

	private PlayerDataManager playerDataManager;
	private LanguageManager languageManager;

	public PlayerDataManager getPlayerDataManager() {
		return playerDataManager;
	}

	public LanguageManager getLanguageManager() {
		return languageManager;
	}

	private final ArrayList<UUID> frozenPlayers = new ArrayList<>();

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

		new SetLanguageCommand(this);
	}
}
