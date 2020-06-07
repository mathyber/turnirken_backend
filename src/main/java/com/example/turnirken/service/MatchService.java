package com.example.turnirken.service;

import com.example.turnirken.dto.*;
import com.example.turnirken.entity.*;
import com.example.turnirken.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchService {
  //  private final TournamentService tournamentService;
    private final MatchRepository matchRepository;
    private final MatchResultRepository matchResultRepository;
    private final UserRepository userRepository;
    private final NextRepository nextRepository;
    private final NextTypeRepository nextTypeRepository;
    private final TournamentRepository tournamentRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final TournamentGroupRepository tournamentGroupRepository;
    private final GroupParticipantRepository groupParticipantRepository;

    public MatchService(MatchRepository matchRepository, MatchResultRepository matchResultRepository, UserRepository userRepository, NextRepository nextRepository, NextTypeRepository nextTypeRepository, TournamentRepository tournamentRepository, TournamentParticipantRepository tournamentParticipantRepository, TournamentGroupRepository tournamentGroupRepository, GroupParticipantRepository groupParticipantRepository) {

     //   this.tournamentService = tournamentService;
        this.matchRepository = matchRepository;
        this.matchResultRepository = matchResultRepository;
        this.userRepository = userRepository;
        this.nextRepository = nextRepository;
        this.nextTypeRepository = nextTypeRepository;
        this.tournamentRepository = tournamentRepository;
        this.tournamentParticipantRepository = tournamentParticipantRepository;
        this.tournamentGroupRepository = tournamentGroupRepository;
        this.groupParticipantRepository = groupParticipantRepository;
    }

    public MatchResModel getMatchResult(int id) {

        Match match = matchRepository.findById((long) id).get();
        MatchResModel matchResModel = new MatchResModel();
        if (match.getRound() != null) {
            matchResModel.setGroupName(match.getRound().getGroup().getName());
            matchResModel.setGroupId(match.getRound().getGroup().getId().intValue());
            matchResModel.setRound(match.getRound().getNum());
            Tournament t = match.getRound().getGroup().getTournament();
            TournamentModel tm = new TournamentModel();
            tm.setId(t.getId().intValue());
            tm.setName(t.getTournamentName().getName());
            tm.setGame(t.getTournamentName().getGame().getName());
            tm.setSeason(t.getSeason());
            tm.setIdOrg(t.getOrganizer().getId().intValue());
            matchResModel.setTournament(tm);
        }
        if (match.getPlayoff() != null) {
            matchResModel.setPlayoffStage(match.getPlayoff().getStage().getName());
            Tournament t = match.getPlayoff().getTournament();
            TournamentModel tm = new TournamentModel();
            tm.setId(t.getId().intValue());
            tm.setName(t.getTournamentName().getName());
            tm.setGame(t.getTournamentName().getGame().getName());
            tm.setSeason(t.getSeason());
            tm.setIdOrg(t.getOrganizer().getId().intValue());
            matchResModel.setTournament(tm);
        }

        if (match.getPlayer2() != null) {
            GetParticipantsModel getParticipantsModel = new GetParticipantsModel();
            getParticipantsModel.setUser_id(match.getPlayer2().getUser().getId());
            getParticipantsModel.setLogin(match.getPlayer2().getUser().getLogin());
            getParticipantsModel.setId(match.getPlayer2().getId());
            matchResModel.setPlayer2(getParticipantsModel);
        }

        if (match.getPlayer1() != null) {
            GetParticipantsModel getParticipantsModel = new GetParticipantsModel();
            getParticipantsModel.setUser_id(match.getPlayer1().getUser().getId());
            getParticipantsModel.setLogin(match.getPlayer1().getUser().getLogin());
            getParticipantsModel.setId(match.getPlayer1().getId());
            matchResModel.setPlayer1(getParticipantsModel);
        }

        matchResModel.setId(match.getId());

        ArrayList<MatchResult> resm = matchResultRepository.findByMatch_Id(match.getId());

        if (resm.size() != 0) {
            List<MatchResForPageModel> matchResForPageModels = new ArrayList<>();
            for(MatchResult matchResult: resm){
                MatchResForPageModel matchResForPageModel = new MatchResForPageModel();
                matchResForPageModel.setId(matchResult.getId());
                matchResForPageModel.setFinish(matchResult.isFinish());
                matchResForPageModel.setInfo(matchResult.getInfo());
                matchResForPageModel.setResPlayer1(matchResult.getResultPlayer1());
                matchResForPageModel.setResPlayer2(matchResult.getResultPlayer2());
                matchResForPageModel.setDate(matchResult.getDate());
                if(matchResult.getResultCreator().getId().equals(matchResult.getMatch().getPlayer1().getUser().getId())) matchResForPageModel.setResCreator(1);
                else {
                    if (matchResult.getResultCreator().getId().equals(matchResult.getMatch().getPlayer2().getUser().getId()))
                        matchResForPageModel.setResCreator(2);
                    else matchResForPageModel.setResCreator(0);
                }
                matchResForPageModels.add(matchResForPageModel);
            }

            Collections.sort(matchResForPageModels, SortResultStory.SORT_BY_DATE);
            matchResModel.setStory(matchResForPageModels);
        }

        if (resm.size() != 0) {
            int r1p1 = 0, r2p1 = 0, r1p2 = 0, r2p2 = 0;
            Date dt = resm.get(0).getDate();
            for (MatchResult matchResult : resm) {
                if (dt.before(matchResult.getDate()) || dt.equals(matchResult.getDate())) {
                    if (matchResult.getResultCreator() != matchResult.getMatch().getPlayer1().getUser() && matchResult.getResultCreator() != matchResult.getMatch().getPlayer2().getUser()) {
                        matchResModel.setResPlayer1(matchResult.getResultPlayer1());
                        matchResModel.setResPlayer2(matchResult.getResultPlayer2());
                    }
                    if (matchResult.getResultCreator() == matchResult.getMatch().getPlayer1().getUser() || matchResult.getResultCreator() == matchResult.getMatch().getPlayer2().getUser()) {

                        if (matchResult.getResultCreator() == matchResult.getMatch().getPlayer1().getUser()) {
                            r1p1 = matchResult.getResultPlayer1();
                            r2p1 = matchResult.getResultPlayer2();
                        }

                        if (matchResult.getResultCreator() == matchResult.getMatch().getPlayer2().getUser()) {
                            r1p2 = matchResult.getResultPlayer1();
                            r2p2 = matchResult.getResultPlayer2();
                        }

                        if (r1p1 == r1p2 && r2p1 == r2p2) {
                            matchResModel.setResPlayer1(r1p1);
                            matchResModel.setResPlayer2(r2p1);
                        }
                    }
                }
            }
        } else {
            matchResModel.setResPlayer1(0);
            matchResModel.setResPlayer2(0);
        }

        matchResModel.setFinish(match.isGameOverFlag());

        return matchResModel;
    }



    public void nextStageForMatch(Match match) {
        if (match.isGameOverFlag()) {
            TournamentParticipant winner;
            TournamentParticipant loser;
            MatchResModel mrm = getMatchResult(match.getId().intValue());
            if(mrm.getResPlayer1()-mrm.getResPlayer2()>0){
                winner = tournamentParticipantRepository.findById(mrm.getPlayer1().getId()).get();
                loser = tournamentParticipantRepository.findById(mrm.getPlayer2().getId()).get();
            } else {
                winner = tournamentParticipantRepository.findById(mrm.getPlayer2().getId()).get();
                loser = tournamentParticipantRepository.findById(mrm.getPlayer1().getId()).get();
            }

            Set<Next> nr = nextRepository.findByThisTypeAndIdThis(nextTypeRepository.findByName("match"), match.getId().intValue());
            for (Next next : nr) {
                if (next.getPlace() == 1) {
                    if (next.getNextType().equals(nextTypeRepository.findByName("group"))) {
                        TournamentGroup gr = tournamentGroupRepository.findById((long) next.getIdNext()).get();
                        GroupParticipant gp = new GroupParticipant();
                        gp.setParticipant(winner);
                        gp.setGroup(gr);
                        groupParticipantRepository.save(gp);
                    }
                    if (next.getNextType().equals(nextTypeRepository.findByName("match"))) {
                        Match m = matchRepository.findById((long) next.getIdNext()).get();
                        if (m.getPlayer1() == null) {
                            m.setPlayer1(winner);
                        } else if (m.getPlayer2() == null) {
                            m.setPlayer2(winner);
                        }
                        matchRepository.save(m);
                    }
                    if (next.getNextType().equals(nextTypeRepository.findByName("result"))) {
                        if (next.getIdNext() == 1) {
                            winner.setNameInTournament("Победитель");
                            tournamentParticipantRepository.save(winner);
                            Tournament t = tournamentRepository.findById(match.getPlayer1().getTournament().getId()).get();
                            t.setDateFinish(new Date());
                            tournamentRepository.save(t);
                        } else {
                            winner.setNameInTournament(next.getIdNext()+" место");
                         //   System.out.println("DDDDDDDDDDDDDD"+next.getIdNext());
                            tournamentParticipantRepository.save(winner);
                        }
                    }
                } else {
                    if (next.getNextType().equals(nextTypeRepository.findByName("group"))) {
                        TournamentGroup gr = tournamentGroupRepository.findById((long) next.getIdNext()).get();
                        GroupParticipant gp = new GroupParticipant();
                        gp.setParticipant(loser);
                        gp.setGroup(gr);
                        groupParticipantRepository.save(gp);
                    }
                    if (next.getNextType().equals(nextTypeRepository.findByName("match"))) {
                        Match m = matchRepository.findById((long) next.getIdNext()).get();
                        if (m.getPlayer1() == null) {
                            m.setPlayer1(loser);
                        } else if (m.getPlayer2() == null) {
                            m.setPlayer2(loser);
                        }
                        matchRepository.save(m);
                    }
                    if (next.getNextType().equals(nextTypeRepository.findByName("result"))) {

                            loser.setNameInTournament(next.getIdNext()+" место");
                            //   System.out.println("DDDDDDDDDDDDDD"+next.getIdNext());
                            tournamentParticipantRepository.save(loser);

                    }
                }
            }
        }
    }

    public void finishMatch(int id) {
        Match match = matchRepository.findById((long) id).get();

        ArrayList<MatchResult> resadm = new ArrayList<>();
        if (match.getPlayoff() != null) {
            resadm = matchResultRepository.findByMatch_IdAndResultCreator(match.getId(), match.getPlayoff().getTournament().getOrganizer());
        }
        if (match.getRound() != null) {
            resadm = matchResultRepository.findByMatch_IdAndResultCreator(match.getId(), match.getRound().getGroup().getTournament().getOrganizer());
        }

        if (resadm.size() != 0) {

            MatchResult ma = resadm.get(0);
            Date dta = resadm.get(0).getDate();
            for (MatchResult matchResult : resadm) {
                if (dta.before(matchResult.getDate())) {
                    ma = matchResult;
                }
            }
            if (ma.isFinish()) {
                {
                    match.setGameOverFlag(true);
                    nextStageForMatch(match);
                }
                matchRepository.save(match);
                return;
            }
        }

        ArrayList<MatchResult> resm = matchResultRepository.findByMatch_IdAndResultCreator(match.getId(), match.getPlayer1().getUser());
        ArrayList<MatchResult> resm1 = matchResultRepository.findByMatch_IdAndResultCreator(match.getId(), match.getPlayer2().getUser());

        if (resm.size() != 0 && resm1.size() != 0) {

            MatchResult m = resm.get(0);
            Date dt = resm.get(0).getDate();
            for (MatchResult matchResult : resm) {
                if (dt.before(matchResult.getDate())) {
                    m = matchResult;
                }
            }

            MatchResult m1 = resm1.get(0);
            Date dt1 = resm1.get(0).getDate();
            for (MatchResult matchResult : resm1) {
                if (dt1.before(matchResult.getDate())) {
                    m1 = matchResult;
                }
            }

            if (m.getResultPlayer1() == m1.getResultPlayer1() && m.getResultPlayer2() == m1.getResultPlayer2() && m.isFinish() && m1.isFinish()) {
                match.setGameOverFlag(true);
              //  if (match.getRound() != null) tournamentService.nextStageForGroup(match.getRound().getGroup());
                nextStageForMatch(match);
            }
            matchRepository.save(match);
        }
    }


    public boolean creatorResult(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);

        Match match = matchRepository.findById((long) id).get();
        if (match.isGameOverFlag()) return false;

        if (match.getPlayoff() != null) {
            if (match.getPlayoff().getTournament().getOrganizer() == user) return true;
            else if (match.getPlayoff().getTournament().isOnlyAdminResult()) return false;
        }
        if (match.getRound() != null) {
            if (match.getRound().getGroup().getTournament().getOrganizer() == user) return true;
            else if (match.getRound().getGroup().getTournament().isOnlyAdminResult()) return false;
        }
        if (match.getPlayer1().getUser() == user || match.getPlayer2().getUser() == user) {
            return true;
        }

        return false;

    }

    public boolean setResultMatch(CreateResultMatchModel m) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);

        MatchResult mr = new MatchResult();
        mr.setMatch(matchRepository.findById(m.getIdMatch()).get());
        mr.setDate(new Date());
        mr.setInfo(m.getInfo());

        if (matchRepository.findById(m.getIdMatch()).get().getPlayoff() != null) {
            mr.setResultCreator(user);
        }
        if (matchRepository.findById(m.getIdMatch()).get().getRound() != null) {
            mr.setResultCreator(user);
        }

        mr.setResultPlayer1(m.getMeRes());
        mr.setResultPlayer2(m.getPlayer2Res());
        if (m.isFinish()) {
            if(matchRepository.findById(m.getIdMatch()).get().getPlayoff() != null && m.getMeRes()==m.getPlayer2Res()) return false;
            mr.setFinish(true);
        }
        matchResultRepository.save(mr);

        finishMatch(m.getIdMatch().intValue());

        return true;
    }

    public void setMatchFinish(int id) {
        if (creatorResult(id)) {
            matchRepository.findById((long) id).get().setGameOverFlag(true);
        }
    }

    public List<MatchResModel> getMatchesForUser(AppUser ap) {

        List<MatchResModel> res = new ArrayList<>();

        Set<Match> m1 = matchRepository.findByPlayer1_User(ap);
        Set<Match> m2 = matchRepository.findByPlayer2_User(ap);
        Set<Match> ms = new HashSet<>();
        ms.addAll(m1);
        ms.addAll(m2);

        for (Match match : ms) {
            MatchResModel mr = getMatchResult(match.getId().intValue());
            res.add(mr);
        }

        return res;

    }

    public Set<MatchResModel> getAllMatchesTournament(int id) {

        Set<MatchResModel> res = new HashSet<>();

        Set<Match> m1 = matchRepository.findByPlayoff_Tournament_Id((long) id);
        Set<Match> m2 = matchRepository.findByRound_Group_Tournament_Id((long) id);

        Set<Match> ms = new HashSet<>();
        ms.addAll(m1);
        ms.addAll(m2);

        for (Match match : ms) {
            MatchResModel mr = getMatchResult(match.getId().intValue());
            res.add(mr);
        }

        return res;

    }

    public ArrayList<MatchResModel> getMatchesGroup(int id) {
        ArrayList<MatchResModel> res = new ArrayList<>();
        Set<Match> m2 = matchRepository.findByRound_Group_Id((long) id);

        for (Match match : m2) {
            MatchResModel mr = getMatchResult(match.getId().intValue());
            res.add(mr);
        }
        Collections.sort(res, SortGroup.SORT_BY_ROUND);

        return res;
    }
}
