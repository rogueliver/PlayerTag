package com.rogueliver.playertag.commands;

import com.rogueliver.playertag.PlayerTag;
import com.rogueliver.playertag.commands.subcommands.ReloadSubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

public class PlayerTagCommand {

    private final PaperCommandManager<CommandSourceStack> commandManager;
    private final AnnotationParser<CommandSourceStack> annotationParser;

    public PlayerTagCommand(PlayerTag plugin) {
        this.commandManager = PaperCommandManager.builder(
                        org.incendo.cloud.SenderMapper.identity()
                )
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(plugin);

        this.annotationParser = new AnnotationParser<>(
                commandManager,
                CommandSourceStack.class
        );

        this.annotationParser.parse(new ReloadSubCommand(plugin));
    }
}
