package com.example.turnirken.service;

import com.example.turnirken.entity.GroupParticipant;
import com.example.turnirken.entity.TournamentGroup;
import com.example.turnirken.entity.TournamentParticipant;
import com.example.turnirken.repository.GroupParticipantRepository;
import com.example.turnirken.repository.TournamentGroupRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GroupService {
    private final TournamentGroupRepository tournamentGroupRepository;
  //  private final TournamentParticipantRepository tournamentParticipantRepository;
    private final RoundRobinGenerator roundRobinGenerator;
    private final GroupParticipantRepository groupParticipantRepository;

    public GroupService(TournamentGroupRepository tournamentGroupRepository, RoundRobinGenerator roundRobinGenerator, GroupParticipantRepository groupParticipantRepository) {

        this.tournamentGroupRepository = tournamentGroupRepository;
      //  this.tournamentParticipantRepository = tournamentParticipantRepository;
        this.roundRobinGenerator = roundRobinGenerator;
        this.groupParticipantRepository = groupParticipantRepository;
    }

  //  public Game create(Game game) {
    //    return gameRepository.save(game);
  //  }

    public void createGroupMatches(TournamentGroup group){
        ArrayList<GroupParticipant> tp = groupParticipantRepository.findByGroup(group);
        roundRobinGenerator.ListMatches(tp, group);
    }

}
