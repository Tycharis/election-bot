package edu.kstate.electionbot.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateSpec;
import edu.kstate.electionbot.Meta;
import edu.kstate.electionbot.command.obj.Candidate;
import edu.kstate.electionbot.command.obj.Election;
import edu.kstate.electionbot.command.obj.ElectionRegistry;
import reactor.core.publisher.Mono;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

public class ElectCommand implements ElectionBotCommand {
    public Mono<Void> execute(MessageCreateEvent event) {
        Consumer<EmbedCreateSpec> eventSpec = spec -> {
            Optional<Member> memberOptional = event.getMember();

            if (memberOptional.isEmpty()) {
                spec.setAuthor(Meta.NAME, Meta.REPO_URL, null);
            } else {
                spec.setAuthor(event.getMember().get().getDisplayName(), Meta.REPO_URL, null);
            }

            String[] message = event.getMessage().toString().split(" ");

            spec.setColor(new Color(0x512888));
            spec.setTitle(message[3]);

            ArrayList<Candidate> candidates = decodeList(message[4]);

            //TODO set message ID - Blocking issue
            ElectionRegistry.createNew(0L, new Election(message[1], parseDate(message[2], message[3]), event, candidates));

            for (int i = 0; i < candidates.size(); i++) {
                spec.addField(Integer.toString(i), candidates.get(i).formatName(), true);
            }
        };

        return event.getMessage().getChannel().flatMap(channel ->
                channel.createMessage(spec ->
                        spec.setEmbed(eventSpec.andThen(x -> {}))
                )).then();
    }

    private ArrayList<Candidate> decodeList(String input) {
        // &elect President 2019-01-01 23:59:00 Braedon,Smith,smithb99|Noah,Kelly,Fate

        String[] data = input.split("\\|");
        ArrayList<Candidate> candidates = new ArrayList<>();

        for (String name : data) {
            String[] decoded = name.split(",");
            candidates.add(new Candidate(decoded[0], decoded[1], decoded[2]));
        }

        return candidates;
    }

    private Date parseDate(String date, String time) {
        String[] dateString = date.split("-");
        String[] timeString = time.split(":");

        int[] dateSplit = new int[dateString.length];
        int[] timeSplit = new int[timeString.length];

        for (int i = 0; i < dateString.length; i++) {
            dateSplit[i] = Integer.parseInt(dateString[i]);
        }

        for (int i = 0; i < timeString.length; i++) {
            timeSplit[i] = Integer.parseInt(timeString[i]);
        }

        Calendar calendar = GregorianCalendar.getInstance(Locale.US);

        calendar.set(Calendar.YEAR, dateSplit[0]);
        calendar.set(Calendar.MONTH, dateSplit[1]);
        calendar.set(Calendar.DATE, dateSplit[2]);
        calendar.set(Calendar.HOUR_OF_DAY, timeSplit[0]);
        calendar.set(Calendar.MINUTE, timeSplit[1]);
        calendar.set(Calendar.SECOND, timeSplit[2]);

        return calendar.getTime();
    }

    public String getDescription() {
        return "Runs an election.\nUsage:  " + "" + "elect <Title> <YYYY-DD-MM> <HH:MM> <First>,<Last>,<Tag>|<First>,<Last>,<Tag>|...";
    }
}
