package edu.kstate.electionbot;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BotConfig {
    private static final ObjectMapper mapper = new ObjectMapper();

    private String discordToken;
    private Set<Long> allowedGuilds;
    private String prefix;
    private int resetTime;

    static BotConfig fromJson(Path json) throws IOException {
        try (Stream<String> lines = Files.lines(json)) {
            return fromJson(lines.collect(Collectors.joining()));
        }
    }

    static BotConfig fromJson(String json) throws IOException {
        return mapper.readValue(json, BotConfig.class);
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public Set<Long> getAllowedGuilds() {
        return allowedGuilds;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getResetTime() {
        return resetTime;
    }
}
