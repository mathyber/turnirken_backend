package com.example.turnirken.service;

import com.example.turnirken.entity.*;
import com.example.turnirken.repository.MatchRepository;
import com.example.turnirken.repository.RoundRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RoundRobinGenerator {

    private RoundRepository roundRepository;
    private MatchRepository matchRepository;

    public RoundRobinGenerator(RoundRepository roundRepository, MatchRepository matchRepository) {
        this.roundRepository = roundRepository;
        this.matchRepository = matchRepository;
    }

    void ListMatches(ArrayList<GroupParticipant> participants, TournamentGroup tg)
    {

        if (participants.size() % 2 != 0)
        {
            TournamentParticipant tp = new TournamentParticipant();
            tp.setNameInTournament("Fake");
            GroupParticipant gp = new GroupParticipant();
            participants.add(gp); // If odd number of teams add a dummy
        }
        int numUsers = participants.size();
        int numDays = (numUsers - 1); // Days needed to complete tournament
        int halfSize = numUsers / 2;

        ArrayList<GroupParticipant> participants1 = new ArrayList<>();

        participants1.addAll(participants);
        participants1.remove(0);

        int participantsSize = participants1.size();

        for (int day = 0; day < numDays; day++)
        {

            Round rnd = new Round();
            rnd.setNum(day+1);
            rnd.setGroup(tg);
            roundRepository.save(rnd);

            int Idx = day % participantsSize;

            Match match = new Match();
            match.setRound(rnd);
            match.setPlayer1(participants1.get(Idx).getParticipant());
            match.setPlayer2(participants.get(0).getParticipant());
            if(!match.getPlayer1().getNameInTournament().equals("Fake") && !match.getPlayer2().getNameInTournament().equals("Fake") && match.getPlayer2() != match.getPlayer1())
                matchRepository.save(match);

            for (int idx = 1; idx < halfSize; idx++)
            {
                int pl1 = (day + idx) % participantsSize;
                int pl2 = (day  + participantsSize - idx) % participantsSize;

                Match match1 = new Match();
                match1.setRound(rnd);
                match1.setPlayer1(participants1.get(pl1).getParticipant());
                match1.setPlayer2(participants1.get(pl2).getParticipant());
                if(!match.getPlayer1().getNameInTournament().equals("Fake") && !match.getPlayer2().getNameInTournament().equals("Fake") && match.getPlayer2() != match.getPlayer1())
                    matchRepository.save(match1);
            }
        }
    }
}
