package com.rogueliver.playertag.commands.subcommands;

import com.rogueliver.playertag.PlayerTag;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

public class ReloadSubCommand {

    private final PlayerTag plugin;

    public ReloadSubCommand(PlayerTag plugin) {
        this.plugin = plugin;
    }

    @Command("playertag reload")
    @Permission("playertag.reload")
    public void reload(CommandSourceStack sender) {
        plugin.getConfigManager().reload();
        sender.getSender().sendMessage(Component.text("PlayerTag configuration reloaded!", NamedTextColor.GREEN));
    }
}
