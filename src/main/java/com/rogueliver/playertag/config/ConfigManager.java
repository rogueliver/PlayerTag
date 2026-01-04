package com.rogueliver.playertag.config;

import com.rogueliver.playertag.PlayerTag;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

@Getter
public class ConfigManager {

    private final PlayerTag plugin;
    private YamlDocument config;

    private String pingFormat;
    private boolean soundEnabled;
    private String soundType;
    private float soundVolume;
    private float soundPitch;

    public ConfigManager(PlayerTag plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        try {
            config = YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"),
                    plugin.getResource("config.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
            );
            
            config.update();
            config.save();
            
            reloadValues();
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load config.yml!");
            e.printStackTrace();
        }
    }

    public void reloadValues() {
        this.pingFormat = config.getString("ping-format", "<gradient:aqua:blue>@<name></gradient>");
        this.soundEnabled = config.getBoolean("sound.enabled", true);
        this.soundType = config.getString("sound.type", "block.note_block.pling");
        this.soundVolume = config.getFloat("sound.volume", 1.0f);
        this.soundPitch = config.getFloat("sound.pitch", 2.0f);
    }
    
    public void reload() {
        try {
            config.reload();
            reloadValues();
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to reload config.yml!");
            e.printStackTrace();
        }
    }
}
