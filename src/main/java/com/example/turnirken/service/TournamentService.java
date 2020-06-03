package com.example.turnirken.service;

import com.example.turnirken.dto.*;
import com.example.turnirken.entity.*;
import com.example.turnirken.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TournamentService {
    private TournamentRepository tournamentRepository;
    private GameRepository gameRepository;
    private UserRepository userRepository;
    private TournamentSystemRepository tournamentSystemRepository;
    private TournamentParticipantRepository tournamentParticipantRepository;
    private NextTypeRepository nextTypeRepository;
    private TournamentGroupRepository tournamentGroupRepository;
    private MatchRepository matchRepository;
    private NextRepository nextRepository;
    private StageRepository stageRepository;
    private PlayoffRepository playoffRepository;
    private GroupService groupService;
    private GroupParticipantRepository groupParticipantRepository;
    private RoundRobinGenerator roundRobinGenerator;

    public TournamentService(TournamentRepository tournamentRepository, GameRepository gameRepository, UserRepository userRepository, TournamentSystemRepository tournamentSystemRepository, TournamentParticipantRepository tournamentParticipantRepository, NextTypeRepository nextTypeRepository, TournamentGroupRepository tournamentGroupRepository, MatchRepository matchRepository, NextRepository nextRepository, StageRepository stageRepository, PlayoffRepository playoffRepository, GroupService groupService, GroupParticipantRepository groupParticipantRepository, RoundRobinGenerator roundRobinGenerator) {
        this.tournamentRepository = tournamentRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.tournamentSystemRepository = tournamentSystemRepository;
        this.tournamentParticipantRepository = tournamentParticipantRepository;
        this.nextTypeRepository = nextTypeRepository;
        this.tournamentGroupRepository = tournamentGroupRepository;
        this.matchRepository = matchRepository;
        this.nextRepository = nextRepository;
        this.stageRepository = stageRepository;
        this.playoffRepository = playoffRepository;
        this.groupService = groupService;
        this.groupParticipantRepository = groupParticipantRepository;
        this.roundRobinGenerator = roundRobinGenerator;
    }

    public Tournament create(CreateTournamentModel model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);

        TournamentName name = new TournamentName();
        name.setName(model.getName());
        name.setCreator(user);

        Game game = gameRepository.findByName(model.getGame());
        if (game == null) {
            Game game1 = new Game();
            game1.setName(model.getGame());
            game1.setOnDisplay(false);
            game1.setInfo(model.getGame());
            gameRepository.save(game1);
            name.setGame(game1);
        } else
            name.setGame(game);

        Tournament t = new Tournament();
        t.setTournamentName(name);
        t.setSeason(model.getSeason());
        t.setOrganizer(user);
        t.setLogo(model.getLogo());
        t.setMaxParticipants(model.getMaxParticipants());
        t.setDateFinish(model.getDateFinish());
        t.setDateStart(model.getDateStart());
        t.setDateFinishReg(model.getDateFinishReg());
        t.setDateStartReg(model.getDateStartReg());
        t.setNumToWin(model.getNumToWin());
        t.setOnlyAdminResult(model.isOnlyAdminResult());
        t.setInfo(model.getInfo());
        //zaglushki
        TournamentSystem ts = tournamentSystemRepository.findById(1L).get();
        t.setTournamentSystem(ts);

        return tournamentRepository.save(t);
    }

    public List<Tournament> getTours() {
        return tournamentRepository.findAll();
    }

    public boolean tournamentGrid(int id){
        Tournament tournament = tournamentRepository.findById((long)id ).get();
        if (tournament.getGrid()==null) return true;
        else return false;
    }

    public boolean tournamentParticipant(int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);

      //  Tournament tournament = tournamentRepository.findById((long)id ).get();

            Set<TournamentParticipant> tp = tournamentParticipantRepository.findByTournament_Id((long)id);
            AtomicInteger i= new AtomicInteger();

        for(TournamentParticipant tournamentParticipant : tp){
            if(tournamentParticipant.getUser()!=null)
                if (tournamentParticipant.getUser().getId().equals(user.getId())) i.set(1);
        }

        return i.get() == 0;
        }


    public void gridSave(SaveTourGridModel model) {

        //  try {
        //  JSONObject json =
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);

        Tournament tournament = tournamentRepository.findById((long) model.getId()).get();
        if (tournament.getOrganizer().getId() != user.getId()) return;

        tournament.setGrid(model.getGrid());

        Set<CreateEntityModel> cem = new HashSet<>();

        model.getUsers().forEach(gridElemementModel -> {
            TournamentParticipant tp = new TournamentParticipant();
            tp.setTournament(tournament);
            tp.setNameInTournament(gridElemementModel.getName());
            CreateEntityModel crmod = new CreateEntityModel();
            crmod.setId(tournamentParticipantRepository.save(tp).getId());
            crmod.setType(nextTypeRepository.findByName("user"));
            crmod.setIdFromModel(gridElemementModel.getId());
            cem.add(crmod);
        });

        model.getGroups().forEach(gridElemementModel -> {
            TournamentGroup tg = new TournamentGroup();
            tg.setTournament(tournament);
            tg.setName(gridElemementModel.getName());// setNameInTournament(gridElemementModel.getName());
            tg.setNumberOfPlayers(gridElemementModel.getLinksin().size());
            tg.setNumberOfPlayersPlayoff(gridElemementModel.getLinksout().size());
            CreateEntityModel crmod = new CreateEntityModel();
            crmod.setId(tournamentGroupRepository.save(tg).getId());
            crmod.setType(nextTypeRepository.findByName("group"));
            crmod.setIdFromModel(gridElemementModel.getId());
            cem.add(crmod);
        });

        model.getMatches().forEach(gridElemementModel -> {
            Match m = new Match();
            Playoff playoff = new Playoff();
            playoff.setTournament(tournament);

            Stage stage = new Stage();
            if (stageRepository.findByName(gridElemementModel.getStage()) == null) {
                System.out.println("AAAAAAAAAAAAAAAAAA A");
                stage.setName(gridElemementModel.getStage());
                stageRepository.save(stage);
            } else {
                stage = stageRepository.findByName(gridElemementModel.getStage());
            }
            playoff.setStage(stage);
            playoffRepository.save(playoff);
            m.setPlayoff(playoff);
            //  m.setTournament(tournament);
            CreateEntityModel crmod = new CreateEntityModel();
            crmod.setId(matchRepository.save(m).getId());
            crmod.setType(nextTypeRepository.findByName("match"));
            crmod.setIdFromModel(gridElemementModel.getId());
            cem.add(crmod);
        });

        model.getResults().forEach(gridElemementModel -> {

            CreateEntityModel crmod = new CreateEntityModel();

            int i = 0;
            if (!gridElemementModel.getPlace().equals("Победитель турнира"))
                i = Integer.parseInt(gridElemementModel.getPlace());
            else i = 1;

            crmod.setId((long) i);
            crmod.setType(nextTypeRepository.findByName("result"));
            crmod.setIdFromModel(gridElemementModel.getId());
            cem.add(crmod);
        });

        model.getGroups().forEach(gridElemementModel -> {

            final int[] i = new int[1];
            cem.forEach(createEntityModel -> {
                if (createEntityModel.getIdFromModel().equals(gridElemementModel.getId()))
                //   next.setIdThis(tournamentGroupRepository.findById(createEntityModel.getId()));
                {
                    i[0] = createEntityModel.getId().intValue();
                }
            });

            if (gridElemementModel.getLinksout().size() > 0)
                gridElemementModel.getLinksout().forEach(gridElementLinkModel -> {
                    Next next = new Next();
                    next.setIdThis(i[0]);
                    next.setThisType(nextTypeRepository.findByName("group"));
                    next.setNextType(nextTypeRepository.findByName(gridElementLinkModel.getType()));
                     

                    cem.forEach(createEntityModel -> {
                        if (createEntityModel.getIdFromModel().equals(gridElementLinkModel.getId()))
                        //   next.setIdThis(tournamentGroupRepository.findById(createEntityModel.getId()));
                        {
                            next.setIdNext(createEntityModel.getId().intValue());
                        }
                    });
                    next.setPlace(gridElementLinkModel.getPlace());
                    nextRepository.save(next);
                });
        });

        model.getMatches().forEach(gridElemementModel -> {

            final int[] i = new int[1];
            cem.forEach(createEntityModel -> {
                if (createEntityModel.getIdFromModel().equals(gridElemementModel.getId()))
                //   next.setIdThis(tournamentGroupRepository.findById(createEntityModel.getId()));
                {
                    i[0] = createEntityModel.getId().intValue();
                }
            });

            if (gridElemementModel.getLinksout().size() > 0)
                gridElemementModel.getLinksout().forEach(gridElementLinkModel -> {
                    Next next = new Next();
                    next.setIdThis(i[0]);
                    next.setThisType(nextTypeRepository.findByName("match"));
                    next.setNextType(nextTypeRepository.findByName(gridElementLinkModel.getType()));

                    cem.forEach(createEntityModel -> {
                        if (createEntityModel.getIdFromModel().equals(gridElementLinkModel.getId()))
                        //   next.setIdThis(tournamentGroupRepository.findById(createEntityModel.getId()));
                        {
                            next.setIdNext(createEntityModel.getId().intValue());
                        }
                    });
                    next.setPlace(gridElementLinkModel.getPlace());
                    nextRepository.save(next);
                });
        });


        model.getUsers().forEach(gridElemementModel -> {

            final int[] i = new int[1];
            cem.forEach(createEntityModel -> {
                if (createEntityModel.getIdFromModel().equals(gridElemementModel.getId()))
                //   next.setIdThis(tournamentGroupRepository.findById(createEntityModel.getId()));
                {
                    i[0] = createEntityModel.getId().intValue();
                }
            });

            if (gridElemementModel.getLinksout().size() > 0)
                gridElemementModel.getLinksout().forEach(gridElementLinkModel -> {
                    Next next = new Next();
                    next.setIdThis(i[0]);
                    next.setThisType(nextTypeRepository.findByName("user"));
                    next.setNextType(nextTypeRepository.findByName(gridElementLinkModel.getType()));

                    cem.forEach(createEntityModel -> {
                        if (createEntityModel.getIdFromModel().equals(gridElementLinkModel.getId()))
                        //   next.setIdThis(tournamentGroupRepository.findById(createEntityModel.getId()));
                        {
                            next.setIdNext(createEntityModel.getId().intValue());
                        }
                    });
                    // next.setPlace(gridElementLinkModel.getPlace());
                    nextRepository.save(next);
                });
        });


        /*
        tournament.getGroups().forEach(tournamentGroup -> {
            groupService.createGroupMatches(tournamentGroup);
        });*/

        System.out.println("Creating DONE");

    }

    public Optional<Tournament> getTourId(Long Id) {
        return tournamentRepository.findById(Id);
    }

    public void createParticipate(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);

        //  Tournament tour = tournamentRepository.findById((long) id).get();

        Set<TournamentParticipant> tps = tournamentParticipantRepository.findByTournament_Id((long) id);

        for(TournamentParticipant tournamentParticipant : tps){
            if (tournamentParticipant.getUser() == null) {
                tournamentParticipant.setUser(user);
                tournamentParticipantRepository.save(tournamentParticipant);
                return;
            }
        }
    }

    public Set<GetParticipantsModel> getParticipants(int id) {
        Set<TournamentParticipant> tps = tournamentParticipantRepository.findByTournament_Id((long) id);
        Set<GetParticipantsModel> models = new HashSet<>();

        for(TournamentParticipant tournamentParticipant : tps){
            GetParticipantsModel model = new GetParticipantsModel();
            model.setId(tournamentParticipant.getId());
            if (tournamentParticipant.getUser() != null) {
                model.setUser_id(tournamentParticipant.getUser().getId());
                model.setLogin(tournamentParticipant.getUser().getLogin());
            }
            models.add(model);
        }

        return models;
    }

    public Set<GroupModel> getGroups(int id) {
        Set<TournamentGroup> grs = tournamentGroupRepository.findByTournament_Id((long) id);
        Set<GroupModel> gms = new HashSet<>();

        grs.forEach(tournamentGroup -> {
            GroupModel gm = new GroupModel();
            gm.setId(tournamentGroup.getId());
            gm.setName(tournamentGroup.getName());
            gm.setNumberOfPlayers(tournamentGroup.getNumberOfPlayers());
            gm.setNumberOfPlayersPlayoff(tournamentGroup.getNumberOfPlayersPlayoff());
            gm.setPointsDraw(tournamentGroup.getPointsDraw());
            gm.setPointsWin(tournamentGroup.getPointsWin());

            Set<GetParticipantsModel> gpm = new HashSet<>();
            ArrayList<GroupParticipant> gp =  groupParticipantRepository.findByGroup(tournamentGroup);

            gp.forEach(groupParticipant -> {
                GetParticipantsModel getParticipantsModel = new GetParticipantsModel();
                getParticipantsModel.setId(groupParticipant.getId());
                getParticipantsModel.setLogin(groupParticipant.getParticipant().getUser().getLogin());
                getParticipantsModel.setUser_id(groupParticipant.getParticipant().getUser().getId());

                gpm.add(getParticipantsModel);
            });

            gm.setParticipants(gpm);

            gm.setNexts(nextRepository.findByThisTypeAndIdThis(nextTypeRepository.findByName("group"),tournamentGroup.getId().intValue()));

            gm.setLasts(nextRepository.findByNextTypeAndIdNext(nextTypeRepository.findByName("group"),tournamentGroup.getId().intValue()));

            gms.add(gm);

        });

        return gms;

    }

    public Set<MatchModel> getMatches(int id, boolean playoffs) {
        Set<Match> ms;
        if(playoffs) ms = matchRepository.findByPlayoff_Tournament_Id((long) id);
        else ms = matchRepository.findByRound_Group_Tournament_Id((long) id);
        Set<MatchModel> mms = new HashSet<>();

        ms.forEach(match -> {
            MatchModel m = new MatchModel();
            m.setId(match.getId());

            if (match.getRound()!=null) m.setId_group(match.getRound().getGroup().getId());
            if (match.getPlayoff()!=null) m.setId_playoff(match.getPlayoff().getId());

            Set<GetParticipantsModel> pm = new HashSet<>();
         //   ArrayList<GroupParticipant> gp =  groupParticipantRepository.findByGroup(tournamentGroup);
            if (match.getPlayer1()!=null) {
                GetParticipantsModel getParticipantsModel1 = new GetParticipantsModel();
                getParticipantsModel1.setId(match.getPlayer1().getId());
                getParticipantsModel1.setLogin(match.getPlayer1().getUser().getLogin());
                getParticipantsModel1.setUser_id(match.getPlayer1().getUser().getId());
                pm.add(getParticipantsModel1);
            }
            if (match.getPlayer2()!=null) {
                GetParticipantsModel getParticipantsModel2 = new GetParticipantsModel();
                getParticipantsModel2.setId(match.getPlayer2().getId());
                getParticipantsModel2.setLogin(match.getPlayer2().getUser().getLogin());
                getParticipantsModel2.setUser_id(match.getPlayer2().getUser().getId());
                pm.add(getParticipantsModel2);
            }

            m.setParticipants(pm);

            m.setNexts(nextRepository.findByThisTypeAndIdThis(nextTypeRepository.findByName("match"),match.getId().intValue()));

            m.setLasts(nextRepository.findByNextTypeAndIdNext(nextTypeRepository.findByName("match"),match.getId().intValue()));

            mms.add(m);

        });

        return mms;
    }

    public void saveGroups(Set<SaveGroupAndMatchesModel> groups) {
     //   System.out.println("HELLOOOOOOOOOOOOOOO SAVE GROUP");

        Set<Integer> ss = new HashSet<>();
        for( SaveGroupAndMatchesModel saveGroupAndMatchesModel : groups ) {
            ss.add(saveGroupAndMatchesModel.getId());
            if (groupParticipantRepository.findByGroup(tournamentGroupRepository.findById((long)saveGroupAndMatchesModel.getId()).get()).size() < tournamentGroupRepository.findById((long) saveGroupAndMatchesModel.getId()).get().getNumberOfPlayers()) {
                GroupParticipant groupParticipant = new GroupParticipant();
                groupParticipant.setGroup(tournamentGroupRepository.findById((long) saveGroupAndMatchesModel.getId()).get());
                groupParticipant.setParticipant(tournamentParticipantRepository.findById((long) saveGroupAndMatchesModel.getIdPart()).get());
                if (groupParticipantRepository.findByGroup_IdAndParticipant_Id(groupParticipant.getId(), groupParticipant.getParticipant().getId())==null)
                    groupParticipantRepository.save(groupParticipant);
            }
         /*   for ( GroupParticipant groupParticipant : groupParticipantRepository.findByGroup(tournamentGroupRepository.findById((long)saveGroupAndMatchesModel.getId()).get())){
                if(groupParticipant.getParticipant()!=null) {
                    groupParticipant.setGroup(tournamentGroupRepository.findById((long)saveGroupAndMatchesModel.getId()).get());
                    groupParticipant.setParticipant(tournamentParticipantRepository.findById((long)saveGroupAndMatchesModel.getIdPart()).get());
                    groupParticipantRepository.save(groupParticipant);
                    return;*/
             }

        for( Integer intr : ss ) {
            if (groupParticipantRepository.findByGroup(tournamentGroupRepository.findById((long)intr).get()).size() == tournamentGroupRepository.findById((long) intr).get().getNumberOfPlayers()){
                ArrayList<GroupParticipant> gps = groupParticipantRepository.findByGroup(tournamentGroupRepository.findById((long) intr).get());
                roundRobinGenerator.ListMatches(gps, tournamentGroupRepository.findById((long) intr).get());}
        }

    }

    public void saveMatchesOfGroup(ArrayList<GroupParticipant> gps, TournamentGroup tg){
        roundRobinGenerator.ListMatches(gps,tg);
    }

    public void saveMatches(Set<SaveGroupAndMatchesModel> matches) {
        for( SaveGroupAndMatchesModel saveGroupAndMatchesModel : matches ){
            Match m = matchRepository.findById((long)saveGroupAndMatchesModel.getId()).get();
            if(m.getPlayer1()==null && m.getPlayer2()==null){
                m.setPlayer1(tournamentParticipantRepository.findById((long)saveGroupAndMatchesModel.getIdPart()).get());
            } else if(m.getPlayer1()==null || m.getPlayer2()==null){
                if (m.getPlayer1()==null) {
                    if(m.getPlayer2().getUser().getId().intValue() != saveGroupAndMatchesModel.getIdPart())
                        m.setPlayer1(tournamentParticipantRepository.findById((long)saveGroupAndMatchesModel.getIdPart()).get());
                }
                if (m.getPlayer2()==null) {
                    if(m.getPlayer1().getUser().getId().intValue() != saveGroupAndMatchesModel.getIdPart())
                        m.setPlayer2(tournamentParticipantRepository.findById((long)saveGroupAndMatchesModel.getIdPart()).get());
                }
            }
            matchRepository.save(m);
        }
    }
}
