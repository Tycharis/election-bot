package edu.kstate.electionbot;

import ch.qos.logback.classic.Logger;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.store.jdk.JdkStoreService;

import edu.kstate.electionbot.command.ElectionBotCommand;
import edu.kstate.electionbot.command.ElectionBotCommandHandler;

import edu.kstate.electionbot.command.HelpCommand;
import reactor.core.publisher.Mono;
import reactor.util.Loggers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ElectionBot {
    private final BotConfig config;

    public static void main(String[] args) {
        try {
            new ElectionBot(BotConfig.fromJson(Paths.get(args[0]))).start().block();
        } catch (IOException e) {
            Loggers.getLogger(Logger.class).error("Error starting bot", e);
        }
    }

    private ElectionBot(BotConfig config) {
        this.config = config;
    }

    private Mono<Void> start() {
        DiscordClient discord = new DiscordClientBuilder(config.getDiscordToken())
                .setStoreService(new JdkStoreService())
                .build();

        Map<String, ElectionBotCommand> commands = new HashMap<>(3);
        //TODO other commands go BEFORE help
        commands.put("help", new HelpCommand(commands));

        ElectionBotCommandHandler commandHandler = new ElectionBotCommandHandler(commands, config.getPrefix(),
                config.getAllowedGuilds());

        Mono<Void> handleCommands = discord.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(commandHandler::handle)
                .then();

        return discord.login().and(handleCommands);
    }
}
