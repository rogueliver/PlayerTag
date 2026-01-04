package com.rogueliver.playertag;

import com.rogueliver.playertag.config.ConfigManager;
import com.rogueliver.playertag.listeners.ChatListener;
import com.rogueliver.playertag.commands.PlayerTagCommand;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class PlayerTag extends JavaPlugin {

    private ConfigManager configManager;
    private PlayerTagCommand playerTagCommand;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.playerTagCommand = new PlayerTagCommand(this);
        getServer().getPluginManager().registerEvents(new ChatListener(this, configManager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
