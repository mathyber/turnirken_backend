package com.example.turnirken.service;

import com.example.turnirken.dto.CreateResultMatchModel;
import com.example.turnirken.dto.GetParticipantsModel;
import com.example.turnirken.dto.MatchResModel;
import com.example.turnirken.dto.TournamentModel;
import com.example.turnirken.entity.*;
import com.example.turnirken.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchService {
    private final TournamentRepository tournamentRepository;
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final MatchRepository matchRepository;
    private final MatchResultRepository matchResultRepository;
    private final UserRepository userRepository;
    private final GroupParticipantRepository groupParticipantRepository;

    public MatchService(TournamentRepository tournamentRepository, TournamentParticipantRepository tournamentParticipantRepository, MatchRepository matchRepository, MatchResultRepository matchResultRepository, UserRepository userRepository, GroupParticipantRepository groupParticipantRepository) {

        this.tournamentRepository = tournamentRepository;
        this.tournamentParticipantRepository = tournamentParticipantRepository;
        this.matchRepository = matchRepository;
        this.matchResultRepository = matchResultRepository;
        this.userRepository = userRepository;
        //  this.tournamentParticipantRepository = tournamentParticipantRepository;
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

    public void finishMatch(int id) {
        Match match = matchRepository.findById((long) id).get();

        ArrayList<MatchResult> resadm = new ArrayList<>();
        if (match.getPlayoff() != null) {
            resadm = matchResultRepository.findByMatch_IdAndResultCreator(match.getId(), match.getPlayoff().getTournament().getOrganizer());
        }
        if (match.getRound() != null) {
            resadm = matchResultRepository.findByMatch_IdAndResultCreator(match.getId(), match.getRound().getGroup().getTournament().getOrganizer() );
        }

        if (resadm.size()!=0) {

            MatchResult ma = resadm.get(0);
            Date dta = resadm.get(0).getDate();
            for (MatchResult matchResult : resadm) {
                if (dta.before(matchResult.getDate())) {
                    ma = matchResult;
                }
            }
            if(ma.getInfo().equals("FINISH_RESULT")) {
                match.setGameOverFlag(true);
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

            if (m.getResultPlayer1() == m1.getResultPlayer1() && m.getResultPlayer2() == m1.getResultPlayer2() && m.getInfo().equals("FINISH_RESULT") && m1.getInfo().equals("FINISH_RESULT"))
                match.setGameOverFlag(true);
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
        if (m.isFinish()) mr.setInfo("FINISH_RESULT");
        matchResultRepository.save(mr);

        finishMatch(m.getIdMatch().intValue());

        return true;
    }

    public void setMatchFinish(int id) {
        if (creatorResult(id)) {
            matchRepository.findById((long) id).get().setGameOverFlag(true);
        }
    }

    public Set<MatchResModel> getMatchesForUser(int id) {

        Set<MatchResModel> res = new HashSet<>();

        Set<Match> m1 = matchRepository.findByPlayer1_Id((long) id);
        Set<Match> m2 = matchRepository.findByPlayer2_Id((long) id);
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
