package com.example.turnirken.service;

import com.example.turnirken.dto.*;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.entity.Role;
import com.example.turnirken.entity.Tournament;
import com.example.turnirken.entity.TournamentParticipant;
import com.example.turnirken.entity.enums.UserRoleEnum;
import com.example.turnirken.repository.*;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TournamentParticipantRepository tournamentParticipantRepository;
    private final TournamentRepository tournamentRepository;
    private final RoleRepository roleRepository;
    private final MatchRepository matchRepository;
    private final MatchService matchService;
    private final TournamentService tournamentService;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TournamentParticipantRepository tournamentParticipantRepository, TournamentRepository tournamentRepository, RoleRepository roleRepository, MatchRepository matchRepository, MatchService matchService, TournamentService tournamentService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tournamentParticipantRepository = tournamentParticipantRepository;
        this.tournamentRepository = tournamentRepository;
        this.roleRepository = roleRepository;
        this.matchRepository = matchRepository;
        this.matchService = matchService;
        this.tournamentService = tournamentService;
    }

    public AppUser create(CreateUserModel userModel) {
        AppUser appUser = new AppUser();
        appUser.setLogin(userModel.getLogin());
        appUser.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        appUser.setEmail(userModel.getEmail());
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(UserRoleEnum.ROLE_USER).get());
        appUser.setRoles(roles);
        return userRepository.save(appUser);
    }

    public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();//get logged in username
    }

    public JSONObject getUserinfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);
        JSONObject userInfo = new JSONObject();
        userInfo.put("id", user.getId());
        userInfo.put("login", user.getLogin());
        userInfo.put("email", user.getEmail());
        userInfo.put("roles", user.getRoles());
        return userInfo;
    }

    public boolean testLogin(TestRegModel userModel) {
        return userRepository.findByLogin(userModel.getStr()) != null;
    }

    public boolean testEmail(TestRegModel userModel) {
        return userRepository.findByEmail(userModel.getStr()) != null;
    }

    public Collection<? extends GrantedAuthority> getUserrole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities();
    }

    public AppUser setRole(RoleModel model) {
        AppUser user = userRepository.findById(model.getId()).get();
        Set<Role> roles = user.getRoles();
        Optional<Role> role = roleRepository.findByName(UserRoleEnum.valueOf(model.getRole()));
        int i = 0;
        for (Role rol : roles) {
            if (rol.getName().name().equals(model.getRole())) i++;
        }
        if (i != 0)
            for (Role rol : roles) {
                if (rol.getName().name().equals(model.getRole())) {
                    roles.remove(rol);
                    break;
                };
            }
        else roles.add(role.get());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public ProfileModel getProfile(GetTourIdModel model) {
        ProfileModel profileModel = new ProfileModel();

        AppUser appUser = userRepository.findById((long) model.getId()).get();
        profileModel.setId(appUser.getId().intValue());
        profileModel.setLogin(appUser.getLogin());
        profileModel.setEmail(appUser.getEmail());

        Set<TournamentParticipant> tp = tournamentParticipantRepository.findByUser_Id((long) model.getId());
        profileModel.setNumPs(tp.size());

        Set<TournamentParticipant> tpw = tournamentParticipantRepository.findByUser_IdAndNameInTournament((long) model.getId(), "Победитель");
        profileModel.setNumW(tpw.size());

        Set<TournamentParticipant> tp2 = tournamentParticipantRepository.findByUser_IdAndNameInTournament((long) model.getId(), "2 место");
        profileModel.setNum2(tp2.size());

        Set<TournamentParticipant> tp3 = tournamentParticipantRepository.findByUser_IdAndNameInTournament((long) model.getId(), "3 место");
        profileModel.setNum3(tp3.size());

        Set<Tournament> org = tournamentRepository.findByOrganizer(appUser);
        profileModel.setNumOrg(org.size());

        List<MatchResModel> m = matchService.getMatchesForUser(appUser);
        Collections.sort(m, SortMatches.SORT_BY_ID);

        int i;
        if (m.size() < 22) i = m.size();
        else i = 21;
        profileModel.setMatches(m.subList(0, i));

        profileModel.setRoles(appUser.getRoles());

        List<TournamentForPageModel> tournamentForPageModels = new ArrayList<>();
        if (tp.size() != 0)
            for (TournamentParticipant tournamentParticipant : tp) {
                tournamentForPageModels.add(tournamentService.toModel(tournamentRepository.findById(tournamentParticipant.getTournament().getId()).get()));
            }
        Collections.sort(tournamentForPageModels, SortTournaments.SORT_BY_DATE);

        if (tournamentForPageModels.size() < 22) i = tournamentForPageModels.size();
        else i = 21;
        profileModel.setTournaments(tournamentForPageModels.subList(0, i));

        return profileModel;
    }
}
