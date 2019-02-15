package edu.kstate.electionbot.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.util.Snowflake;

import edu.kstate.electionbot.command.api.CommandHandler;

import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class ElectionBotCommandHandler extends CommandHandler<ElectionBotCommand> {
    private final String prefix;
    private final ArrayList<Long> allowedGuilds;

    public ElectionBotCommandHandler(Map<String, ElectionBotCommand> commands, String prefix, ArrayList<Long> allowedGuilds) {
        super(commands);
        this.prefix = prefix;
        this.allowedGuilds = allowedGuilds;
    }

    @Override
    public Mono<Void> handle(MessageCreateEvent messageCreateEvent) {
        return Mono.just(messageCreateEvent)
                .filter(this::shouldHandle)
                .flatMap(event -> {
                    Optional<ElectionBotCommand> command = event.getMessage().getContent()
                            .map(this::getCommandName)
                            .flatMap(this::getCommand);

                    return Mono.justOrEmpty(command).flatMap(it -> it.execute(event));
                });
    }

    @Override
    public String getPrefix(MessageCreateEvent messageCreateEvent) {
        return prefix;
    }

    private boolean shouldHandle(MessageCreateEvent event) {
        return event.getGuildId().map(Snowflake::asLong).map(allowedGuilds::contains).orElse(false)
                && event.getMessage().getContent().map(it -> it.startsWith(prefix)).orElse(false);
    }

    private String getCommandName(String content) {
        int end = content.contains(" ") ? content.indexOf(" ", prefix.length()) : content.length();
        return content.substring(prefix.length(), end);
    }
}
