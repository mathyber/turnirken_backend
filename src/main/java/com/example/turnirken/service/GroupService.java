package com.example.turnirken.service;

import com.example.turnirken.dto.GroupPartResModel;
import com.example.turnirken.dto.GroupResModel;
import com.example.turnirken.dto.MatchResModel;
import com.example.turnirken.dto.ParticipantsModel;
import com.example.turnirken.entity.GroupParticipant;
import com.example.turnirken.entity.Match;
import com.example.turnirken.entity.TournamentGroup;
import com.example.turnirken.repository.GroupParticipantRepository;
import com.example.turnirken.repository.MatchRepository;
import com.example.turnirken.repository.TournamentGroupRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class GroupService {
    private final TournamentGroupRepository tournamentGroupRepository;
    private final MatchRepository matchRepository;
    //  private final TournamentParticipantRepository tournamentParticipantRepository;
    private final RoundRobinGenerator roundRobinGenerator;
    private final GroupParticipantRepository groupParticipantRepository;
    private final MatchService matchService;

    public GroupService(TournamentGroupRepository tournamentGroupRepository, MatchRepository matchRepository, RoundRobinGenerator roundRobinGenerator, GroupParticipantRepository groupParticipantRepository, MatchService matchService) {

        this.tournamentGroupRepository = tournamentGroupRepository;
        this.matchRepository = matchRepository;
        //  this.tournamentParticipantRepository = tournamentParticipantRepository;
        this.roundRobinGenerator = roundRobinGenerator;
        this.groupParticipantRepository = groupParticipantRepository;
        this.matchService = matchService;
    }

    //  public Game create(Game game) {
    //    return gameRepository.save(game);
    //  }

    public void createGroupMatches(TournamentGroup group) {
        ArrayList<GroupParticipant> tp = groupParticipantRepository.findByGroup(group);
        roundRobinGenerator.ListMatches(tp, group);
    }


    public GroupPartResModel getPartRes(GroupParticipant GrPart) {
        Set<Match> m1 = matchRepository.findByPlayer1AndRound_Group(GrPart.getParticipant(), GrPart.getGroup());
        Set<Match> m2 = matchRepository.findByPlayer2AndRound_Group(GrPart.getParticipant(), GrPart.getGroup());
        //   m1.addAll(matchRepository.findByPlayer2AndRound_Group(GrPart.getParticipant(), GrPart.getGroup()));
        GroupPartResModel groupPartResModel = new GroupPartResModel();

        int WinPoint = 0, LosePoint = 0, Wins = 0, Losing = 0, Draw = 0;

        for (Match match : m1) {
            MatchResModel resM = matchService.getMatchResult(match.getId().intValue());
            if (resM.isFinish()) {
                WinPoint += resM.getResPlayer1();
                LosePoint += resM.getResPlayer2();
                int wdl = resM.getResPlayer1() - resM.getResPlayer2();
                if (wdl < 0) Losing++;
                if (wdl == 0) Draw++;
                if (wdl > 0) Wins++;
            }
        }

        for (Match match : m2) {
            MatchResModel resM = matchService.getMatchResult(match.getId().intValue());
            if (resM.isFinish()) {
                WinPoint += resM.getResPlayer2();
                LosePoint += resM.getResPlayer1();
                int wdl = resM.getResPlayer2() - resM.getResPlayer1();
                if (wdl < 0) Losing++;
                if (wdl == 0) Draw++;
                if (wdl > 0) Wins++;
            }
        }

        ParticipantsModel pm = new ParticipantsModel();
        pm.setId(GrPart.getParticipant().getId());
        pm.setLogin(GrPart.getParticipant().getUser().getLogin());
        pm.setNameInTournament(GrPart.getParticipant().getNameInTournament());

        groupPartResModel.setPart(pm);
        groupPartResModel.setDraw(Draw);
        groupPartResModel.setLosing(Losing);
        groupPartResModel.setWins(Wins);
        groupPartResModel.setLosingPoints(LosePoint);
        groupPartResModel.setWinPoints(WinPoint);
        int Points = (GrPart.getGroup().getPointsWin() * Wins) + (GrPart.getGroup().getPointsDraw() * Draw);
        groupPartResModel.setPoints(Points);

        return groupPartResModel;
    }

    //вернуть рез группы
    public GroupResModel getGroup(int id) {
        GroupResModel groupResModel = new GroupResModel();

        TournamentGroup group = tournamentGroupRepository.findById((long) id).get();
        ArrayList<GroupParticipant> gps = groupParticipantRepository.findByGroup(group);


        if (gps.size() != 0) {
            ArrayList<GroupPartResModel> groupPartResModels = new ArrayList<>();
            for (GroupParticipant gp : gps) {
                groupPartResModels.add(getPartRes(gp));
            }

            Collections.sort(groupPartResModels, SortGroup.SORT_BY_POINTS_DIFFERENCE_WINS_DRAWS);

            for(int i=0; i<groupPartResModels.size(); i++){
                groupPartResModels.get(i).setPlace(i+1);
                if((i+1)<=group.getNumberOfPlayersPlayoff()) groupPartResModels.get(i).setWin(true);
            }
            groupResModel.setResults(groupPartResModels);
        }

        groupResModel.setGroupName(group.getName());
        groupResModel.setIdGroup(group.getId());
        groupResModel.setIdTour(group.getTournament().getId());

        return groupResModel;
    }

    //вернуть резы всех групп турнира
    public Set<GroupResModel> getGroupsTour(int id) {
        Set<TournamentGroup> grs = tournamentGroupRepository.findByTournament_Id((long)id);
        Set<GroupResModel> groupResModels = new HashSet<>();
        if(grs.size()!=0){
            for(TournamentGroup tournamentGroup: grs){
                groupResModels.add(getGroup(tournamentGroup.getId().intValue()));
            }
        }
        return groupResModels;
    }
}
