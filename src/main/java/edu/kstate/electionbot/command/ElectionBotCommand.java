package edu.kstate.electionbot.command;

import edu.kstate.electionbot.command.api.Command;

public interface ElectionBotCommand extends Command {
    String getDescription();
}
