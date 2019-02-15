package edu.kstate.electionbot.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import edu.kstate.electionbot.Meta;
import reactor.core.publisher.Mono;

import java.awt.Color;
import java.util.Map;
import java.util.function.Consumer;

public class HelpCommand implements ElectionBotCommand {
    private final Consumer<EmbedCreateSpec> SPEC;

    public HelpCommand(Map<String, ElectionBotCommand> commands) {
        SPEC = spec -> {
            spec.setAuthor(Meta.NAME, Meta.REPO_URL, null);
            spec.setColor(new Color(0x512888));

            for (String name : commands.keySet()) {
                String description = commands.get(name).getDescription();
                spec.addField(name, description, true);
            }
        };
    }

    public Mono<Void> execute(MessageCreateEvent event) {
        return event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage(
                        spec -> spec.setEmbed(SPEC.andThen(embed -> {}))
                )).then();
    }

    public String getDescription() {
        return "Displays this message.";
    }
}
