package edu.kstate.electionbot.command.obj;

public class Candidate {
    private String first;
    private String last;
    private String tag;

    public Candidate(String first, String last, String tag) {
        this.first = first;
        this.last = last;
        this.tag = tag;
    }

    public String formatName() {
        return first + " \"" + tag + "\" " + last;
    }
}
