package edu.kstate.electionbot;

import ch.qos.logback.classic.Logger;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.guild.EmojisUpdateEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.store.jdk.JdkStoreService;

import edu.kstate.electionbot.command.ElectCommand;
import edu.kstate.electionbot.command.ElectionBotCommand;
import edu.kstate.electionbot.command.ElectionBotCommandHandler;
import edu.kstate.electionbot.command.HelpCommand;

import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

public class ElectionBot {
    private final BotConfig config;

    public static void main(String[] args) {
        new ElectionBot(new BotConfig()).start().block();
    }

    private ElectionBot(BotConfig config) {
        this.config = config;
    }

    private Mono<Void> start() {
        DiscordClient discord = new DiscordClientBuilder(config.getDiscordToken())
                .setStoreService(new JdkStoreService())
                .build();

        Map<String, ElectionBotCommand> commands = new HashMap<>(3);
        commands.put("elect", new ElectCommand());
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
