package com.example.turnirken.service;

import com.example.turnirken.dto.*;
import com.example.turnirken.entity.*;
import com.example.turnirken.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {
    private final TournamentGroupRepository tournamentGroupRepository;
    private final MatchRepository matchRepository;
    private final RoundRobinGenerator roundRobinGenerator;
    private final GroupParticipantRepository groupParticipantRepository;
    private final MatchService matchService;
    private final NextRepository nextRepository;
    private final NextTypeRepository nextTypeRepository;
    private final TournamentRepository tournamentRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;

    public GroupService(TournamentGroupRepository tournamentGroupRepository, MatchRepository matchRepository, RoundRobinGenerator roundRobinGenerator, GroupParticipantRepository groupParticipantRepository, MatchService matchService, NextRepository nextRepository, NextTypeRepository nextTypeRepository, TournamentRepository tournamentRepository, TournamentParticipantRepository tournamentParticipantRepository) {

        this.tournamentGroupRepository = tournamentGroupRepository;
        this.matchRepository = matchRepository;
        this.roundRobinGenerator = roundRobinGenerator;
        this.groupParticipantRepository = groupParticipantRepository;
        this.matchService = matchService;
        this.nextRepository = nextRepository;
        this.nextTypeRepository = nextTypeRepository;
        this.tournamentRepository = tournamentRepository;
        this.tournamentParticipantRepository = tournamentParticipantRepository;
    }

    //  public Game create(Game game) {
    //    return gameRepository.save(game);
    //  }

    public void createGroupMatches(TournamentGroup group) {
        ArrayList<GroupParticipant> tp = groupParticipantRepository.findByGroup(group);
        roundRobinGenerator.ListMatches(tp, group);
    }


    public void nextStageForGroup(TournamentGroup group) {
        if (finishGroup(group)) {
            Set<Next> nr = nextRepository.findByThisTypeAndIdThis(nextTypeRepository.findByName("group"), group.getId().intValue());
            GroupResModel grm = getGroup(group.getId().intValue());
            for (Next next : nr) {
                GroupPartResModel gprm = grm.getResults().get(next.getPlace() - 1);
                if (next.getNextType().equals(nextTypeRepository.findByName("group"))) {
                    TournamentGroup gr = tournamentGroupRepository.findById((long) next.getIdNext()).get();
                    GroupParticipant gp = new GroupParticipant();
                    TournamentParticipant tp = tournamentParticipantRepository.findById(gprm.getPart().getId()).get();
                    gp.setParticipant(tp);
                    gp.setGroup(gr);
                    groupParticipantRepository.save(gp);
                }
                if (next.getNextType().equals(nextTypeRepository.findByName("match"))) {
                    Match m = matchRepository.findById((long) next.getIdNext()).get();
                    if (m.getPlayer1() == null) {
                        m.setPlayer1(tournamentParticipantRepository.findById(gprm.getPart().getId()).get());
                    } else if (m.getPlayer2() == null) {
                        m.setPlayer2(tournamentParticipantRepository.findById(gprm.getPart().getId()).get());
                    }
                    matchRepository.save(m);
                }
                if (next.getNextType().equals(nextTypeRepository.findByName("result"))) {
                    if (next.getIdNext() == 1) {
                        Tournament t = group.getTournament();
                        t.setDateFinish(new Date());
                        tournamentRepository.save(t);
                    }
                }//  gprm.getPart();
            }
        }
    }


    public boolean finishGroup(TournamentGroup group) {
        if (group.isFinish()) return true;
        Set<Match> m2 = matchRepository.findByRound_Group_Id(group.getId());
        int i = 0;
        if (m2.size() != 0) {
            for (Match match : m2) {
                if (match.isGameOverFlag()) i++;
            }
            if (i == m2.size()) {
                group.setFinish(true);
                tournamentGroupRepository.save(group);
                nextStageForGroup(group);
                return true;
            }
        }
        return false;
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

            for (int i = 0; i < groupPartResModels.size(); i++) {
                groupPartResModels.get(i).setPlace(i + 1);
                if ((i + 1) <= group.getNumberOfPlayersPlayoff()) groupPartResModels.get(i).setWin(true);
            }
            groupResModel.setResults(groupPartResModels);
        }

        groupResModel.setGroupName(group.getName());
        groupResModel.setIdGroup(group.getId());
        groupResModel.setIdTour(group.getTournament().getId());
        if (finishGroup(group)) groupResModel.setFinish(true);
        return groupResModel;
    }

    //вернуть резы всех групп турнира
    public Set<GroupResModel> getGroupsTour(int id) {
        Set<TournamentGroup> grs = tournamentGroupRepository.findByTournament_Id((long) id);
        Set<GroupResModel> groupResModels = new HashSet<>();
        if (grs.size() != 0) {
            for (TournamentGroup tournamentGroup : grs) {
                groupResModels.add(getGroup(tournamentGroup.getId().intValue()));
            }
        }
        return groupResModels;
    }

    public void getGroupsPoints(GroupPointsModel model) {

        if(model.isAllGroups()){
            Set<TournamentGroup> groups = tournamentGroupRepository.findByTournament_Id(tournamentGroupRepository.findById((long)model.getIdGroup().intValue()).get().getTournament().getId());
            groups.forEach(tournamentGroup -> {
                tournamentGroup.setPointsDraw(model.getNumDraw());
                tournamentGroup.setPointsWin(model.getNumWin());
                tournamentGroupRepository.save(tournamentGroup);
            });
        }

        TournamentGroup group = tournamentGroupRepository.findById(model.getIdGroup()).get();
        if (!group.isFinish()) {
            group.setPointsWin(model.getNumWin());
            group.setPointsDraw(model.getNumDraw());
            tournamentGroupRepository.save(group);
        }
    }
}
