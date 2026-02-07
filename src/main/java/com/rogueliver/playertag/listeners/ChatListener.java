package com.rogueliver.playertag.listeners;

import com.rogueliver.playertag.PlayerTag;
import com.rogueliver.playertag.config.ConfigManager;
import com.rogueliver.playertag.utils.SoundUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ChatListener implements Listener {

    private final PlayerTag plugin;
    private final ConfigManager configManager;
    private static final Pattern MENTION_PATTERN_WITH_AT = Pattern.compile("@(\\w+)");
    private static final Pattern MENTION_PATTERN_NO_AT = Pattern.compile("\\b(\\w+)\\b");

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        
        Pattern pattern = configManager.isTagWithoutAt() ? MENTION_PATTERN_NO_AT : MENTION_PATTERN_WITH_AT;
        Matcher matcher = pattern.matcher(message);
        
        Component newMessage = event.message();
        boolean mentioned = false;

        while (matcher.find()) {
            String potentialName = matcher.group(1);
            
            // ignore short names
            if (potentialName.length() < 3) continue;

            // getPlayerExact is thread-safe (saladedecaprium if you see this, your welcome :D)
            Player target = Bukkit.getPlayerExact(potentialName);

            if (target != null) {
                mentioned = true;
                
                String pingFormat = configManager.getPingFormat();
                String formattedPing = pingFormat.replace("<name>", target.getName());
                
                Component replacement;
                
                if (pingFormat.contains("&") || pingFormat.contains("§")) {
                     replacement = LegacyComponentSerializer.legacyAmpersand().deserialize(formattedPing);
                } else {
                     replacement = MiniMessage.miniMessage().deserialize(formattedPing);
                }

                final String match = matcher.group(0);
                
                newMessage = newMessage.replaceText(builder -> builder.matchLiteral(match).replacement(replacement));

                if (configManager.isSoundEnabled()) {
                    Sound sound = SoundUtil.getSound(configManager.getSoundType());
                    float volume = configManager.getSoundVolume();
                    float pitch = configManager.getSoundPitch();
                    
                    if (sound != null) {
                        target.playSound(target.getLocation(), sound, volume, pitch);
                    }
                }
            }
        }
        
        if (mentioned) {
            event.message(newMessage);
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        String buffer = event.getBuffer();
        int lastSpaceIndex = buffer.lastIndexOf(' ');
        String lastToken = buffer.substring(lastSpaceIndex + 1);

        if (lastToken.startsWith("@")) {
            String partialName = lastToken.substring(1).toLowerCase();
            List<String> completions = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                String playerName = player.getName();
                if (playerName.toLowerCase().startsWith(partialName)) {
                    completions.add("@" + playerName);
                }
            }

            if (!completions.isEmpty()) {
                event.setCompletions(completions);
            }
        }
    }
}
