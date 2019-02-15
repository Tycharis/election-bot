package edu.kstate.electionbot;

import ch.qos.logback.classic.Logger;

import reactor.util.Loggers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

@SuppressWarnings({"unused", "WeakerAccess"})
public class BotConfig {
    private String discordToken;
    private ArrayList<Long> allowedGuilds = new ArrayList<>();
    private String prefix;

    public BotConfig() {
        try {
            // create and load default properties
            Properties defaultProperties = new Properties();
            FileInputStream in = new FileInputStream("default.properties");
            defaultProperties.load(in);
            in.close();

            // create application properties with default
            Properties applicationProperties = new Properties(defaultProperties);

            // now load properties from last invocation
            in = new FileInputStream("electionbot.properties");
            applicationProperties.load(in);
            in.close();

            //Set properties values
            discordToken = applicationProperties.getProperty("token");
            prefix = applicationProperties.getProperty("prefix");

            String guilds = applicationProperties.getProperty("allowed-guilds");
            String[] guildArray = guilds.split(",");
            Long[] guildIds = new Long[guildArray.length];

            for (int i = 0; i < guildArray.length; i++) {
                guildIds[i] = Long.parseLong(guildArray[i]);
            }

            Collections.addAll(allowedGuilds, guildIds);
        } catch (IOException e) {
            Loggers.getLogger(Logger.class).error("Error loading properties.", e);
        }
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public ArrayList<Long> getAllowedGuilds() {
        return allowedGuilds;
    }

    public String getPrefix() {
        return prefix;
    }
}
