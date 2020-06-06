package com.example.turnirken.service;

import com.example.turnirken.entity.*;
import com.example.turnirken.repository.MatchRepository;
import com.example.turnirken.repository.RoundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

@Transactional
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
            tp.setId((long)0);
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


            if(participants1.get(Idx).getParticipant() != participants.get(0).getParticipant())
            {
                Match match = new Match();
                match.setRound(rnd);
                match.setPlayer1(participants1.get(Idx).getParticipant());
                match.setPlayer2(participants.get(0).getParticipant());
                matchRepository.save(match);
            }

            for (int idx = 1; idx < halfSize; idx++)
            {
                int pl1 = (day + idx) % participantsSize;
                int pl2 = (day  + participantsSize - idx) % participantsSize;


                if(participants1.get(pl1).getParticipant()!=participants1.get(pl2).getParticipant()) {
                    Match match1 = new Match();
                    match1.setRound(rnd);
                    match1.setPlayer1(participants1.get(pl1).getParticipant());
                    match1.setPlayer2(participants1.get(pl2).getParticipant());
                    matchRepository.save(match1);
                }
            }
        }

if((participants.size()-1) % 2 != 0){
    System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
    Set<Match> m = matchRepository.findByRound_Group_IdAndPlayer1Null(tg.getId());
    Set<Match> m1 =matchRepository.findByRound_Group_IdAndPlayer2Null(tg.getId());
    matchRepository.deleteAll(m);
    matchRepository.deleteAll(m1);

}

    }
}
