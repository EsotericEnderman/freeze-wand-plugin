package net.slqmy.freeze_wand;

import net.slqmy.freeze_wand.command.GiveFreezeWandCommand;
import net.slqmy.freeze_wand.event.listener.FreezeListener;
import net.slqmy.freeze_wand.event.listener.PlayerMoveListener;
import net.slqmy.freeze_wand.manager.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public final class FreezeWandPlugin extends JavaPlugin {

	private MessageManager messageManager;

	private final ArrayList<UUID> frozenPlayers = new ArrayList<>();

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public ArrayList<UUID> getFrozenPlayers() {
		return frozenPlayers;
	}

	@Override
	public void onEnable() {
		getDataFolder().mkdir();

		getConfig().options().copyDefaults();
		saveDefaultConfig();

		final File enUkLangFile = new File(getDataFolder() + "/lang/en-uk.yml");

		if (!enUkLangFile.exists()) {
			saveResource("lang/en-uk.yml", false);
		}

		messageManager = new MessageManager(this);

		final PluginManager pluginManager = Bukkit.getPluginManager();

		pluginManager.registerEvents(new FreezeListener(this), this);
		pluginManager.registerEvents(new PlayerMoveListener(this), this);

		getCommand("give-freeze-wand").setExecutor(new GiveFreezeWandCommand(this));
	}
}
