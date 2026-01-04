package com.rogueliver.playertag.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;

public class SoundUtil {

    public static Sound getSound(String soundName) {
        if (soundName == null || soundName.isEmpty()) {
            return null;
        }

        String lowerName = soundName.toLowerCase();
        NamespacedKey key;

        if (lowerName.contains(":")) {
            key = NamespacedKey.fromString(lowerName);
        } else {
            // Do not replace dots with underscores. Minecraft keys use dots (e.g. block.note_block.pling)
            key = NamespacedKey.minecraft(lowerName);
        }

        if (key == null) return null;

        return Registry.SOUNDS.get(key);
    }
}
