package edu.kstate.electionbot.command.obj;

import java.util.HashMap;

public class ElectionRegistry {
    public static HashMap<Long, Election> elections = new HashMap<>();

    public static void createNew(Long messageId, Election election) {
        elections.put(messageId, election);
    }
}
