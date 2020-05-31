package com.example.turnirken.service;

import com.example.turnirken.dto.CreateEntityModel;
import com.example.turnirken.dto.CreateTournamentModel;
import com.example.turnirken.dto.SaveTourGridModel;
import com.example.turnirken.entity.*;
import com.example.turnirken.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public TournamentService(TournamentRepository tournamentRepository, GameRepository gameRepository, UserRepository userRepository, TournamentSystemRepository tournamentSystemRepository, TournamentParticipantRepository tournamentParticipantRepository, NextTypeRepository nextTypeRepository, TournamentGroupRepository tournamentGroupRepository, MatchRepository matchRepository, NextRepository nextRepository, StageRepository stageRepository, PlayoffRepository playoffRepository, GroupService groupService){
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
    }

    public Tournament create(CreateTournamentModel model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);

        TournamentName name = new TournamentName();
        name.setName(model.getName());
        name.setCreator(user);

        Game game = gameRepository.findByName(model.getGame());
        if(game == null) {
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

    public void gridSave(SaveTourGridModel model){

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
            if (stageRepository.findByName(gridElemementModel.getStage())==null){
                System.out.println("AAAAAAAAAAAAAAAAAA A");
                stage.setName(gridElemementModel.getStage());
                stageRepository.save(stage);
            }
            else{
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

            int i=0;
            if(!gridElemementModel.getPlace().equals("Победитель турнира")) i = Integer.parseInt(gridElemementModel.getPlace());
            else i=1;

            crmod.setId((long)i);
            crmod.setType(nextTypeRepository.findByName("result"));
            crmod.setIdFromModel(gridElemementModel.getId());
            cem.add(crmod);
        });

        model.getGroups().forEach(gridElemementModel -> {

            final int[] i = new int[1];
            cem.forEach(createEntityModel -> {
                if(createEntityModel.getIdFromModel()==gridElemementModel.getId())
                //   next.setIdThis(tournamentGroupRepository.findById(createEntityModel.getId()));
                {
                    i[0] = createEntityModel.getId().intValue();
                }
            });

            if(gridElemementModel.getLinksout().size()>0) gridElemementModel.getLinksout().forEach(gridElementLinkModel -> {
                Next next = new Next();
                next.setIdThis(i[0]);
                next.setThisType(nextTypeRepository.findByName("group"));
                next.setNextType(nextTypeRepository.findByName(gridElementLinkModel.getType()));

                cem.forEach(createEntityModel -> {
                    if(createEntityModel.getIdFromModel()==gridElementLinkModel.getId())
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
                if(createEntityModel.getIdFromModel()==gridElemementModel.getId())
                //   next.setIdThis(tournamentGroupRepository.findById(createEntityModel.getId()));
                {
                    i[0] = createEntityModel.getId().intValue();
                }
            });

            if(gridElemementModel.getLinksout().size()>0) gridElemementModel.getLinksout().forEach(gridElementLinkModel -> {
                Next next = new Next();
                next.setIdThis(i[0]);
                next.setThisType(nextTypeRepository.findByName("match"));
                next.setNextType(nextTypeRepository.findByName(gridElementLinkModel.getType()));

                cem.forEach(createEntityModel -> {
                    if(createEntityModel.getIdFromModel()==gridElementLinkModel.getId())
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
                if(createEntityModel.getIdFromModel()==gridElemementModel.getId())
                //   next.setIdThis(tournamentGroupRepository.findById(createEntityModel.getId()));
                {
                    i[0] = createEntityModel.getId().intValue();
                }
            });

            if(gridElemementModel.getLinksout().size()>0) gridElemementModel.getLinksout().forEach(gridElementLinkModel -> {
                Next next = new Next();
                next.setIdThis(i[0]);
                next.setThisType(nextTypeRepository.findByName("user"));
                next.setNextType(nextTypeRepository.findByName(gridElementLinkModel.getType()));

                cem.forEach(createEntityModel -> {
                    if(createEntityModel.getIdFromModel()==gridElementLinkModel.getId())
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
}
