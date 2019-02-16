package edu.kstate.electionbot.command.obj;

import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Election {
    private String name;
    private Date expires;
    private MessageCreateEvent event;
    private ArrayList<Tuple<Candidate, Integer>> candidates = new ArrayList<>();

    public Election(String name, Date expires, MessageCreateEvent event, ArrayList<Candidate> candidates) {
        this.name = name;
        this.expires = expires;
        this.event = event;

        for (Candidate candidate : candidates) {
            this.candidates.add(new Tuple<>(candidate, 0));
        }
    }

    public String getName() {
        return name;
    }

    public Date getExpires() {
        return expires;
    }

    public ArrayList<Tuple<Candidate, Integer>> getCandidates() {
        return candidates;
    }

    public void countVote(int index) {
        Tuple<Candidate, Integer> tuple = candidates.remove(index);
        candidates.add(new Tuple<>(tuple.getLeft(), tuple.getRight() + 1));
    }

    public String declareWinner() {
        Collections.sort(candidates);

        ArrayList<Candidate> winners = new ArrayList<>();

        int i = 0;
        while (candidates.get(i).getRight() >= candidates.get(i + 1).getRight()) {
            winners.add(candidates.get(i).getLeft());
            i ++;
        }

        boolean plural = winners.size() > 1;

        StringBuilder winnerNames = new StringBuilder();

        for (Candidate candidate : winners) {
            winnerNames.append(candidate.formatName());
        }

        return "The winner" + (plural ? "s" : "") + " of the election for " +
                name + (plural ? " are" : " is") + winnerNames.toString() + ".";
    }
}
