package com.example.turnirken.service;

import com.example.turnirken.dto.CreateUserModel;
import com.example.turnirken.dto.RoleModel;
import com.example.turnirken.dto.TestRegModel;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.entity.Role;
import com.example.turnirken.entity.enums.UserRoleEnum;
import com.example.turnirken.repository.RoleRepository;
import com.example.turnirken.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    public AppUser create(CreateUserModel userModel) {

        AppUser appUser = new AppUser();

        appUser.setLogin(userModel.getLogin());
        appUser.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        appUser.setEmail(userModel.getEmail());
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(UserRoleEnum.ROLE_USER).get());
        appUser.setRoles(roles);
      //  appUser.setRole("ROLE_USER");
        return userRepository.save(appUser);
    }

    public String getUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();//get logged in username
    }

    public JSONObject getUserinfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        AppUser user = userRepository.findByLogin(login);
        JSONObject userInfo = new JSONObject();
        userInfo.put("id",user.getId());
        userInfo.put("login",user.getLogin());
        userInfo.put("email",user.getEmail());
        userInfo.put("roles",user.getRoles());
        return userInfo;
    }

    public boolean testLogin(TestRegModel userModel) {
        return userRepository.findByLogin(userModel.getStr())!=null;
    }

    public boolean testEmail(TestRegModel userModel) {
        return userRepository.findByEmail(userModel.getStr())!=null;
    }

    public Collection<? extends GrantedAuthority> getUserrole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities();
    }

    public AppUser setRole(RoleModel model) {
        AppUser user = userRepository.findByLogin(model.getLogin());
        Set<Role> roles = user.getRoles();
        Optional<Role> role = roleRepository.findByName(UserRoleEnum.valueOf(model.getRole()));
        roles.add(role.get());
        user.setRoles(roles);
        return userRepository.save(user);
    }
}
