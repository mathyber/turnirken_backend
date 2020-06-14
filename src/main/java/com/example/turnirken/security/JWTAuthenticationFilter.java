package com.example.turnirken.security;

import com.auth0.jwt.JWT;
import com.example.turnirken.entity.AppUser;
import com.example.turnirken.entity.Role;
import com.example.turnirken.entity.UserRole;
import com.example.turnirken.repository.UserRepository;
import com.example.turnirken.repository.UserRoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.turnirken.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    UserRepository rep;
    UserRoleRepository urr;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository rep, UserRoleRepository urr) {
        this.authenticationManager = authenticationManager;
        this.rep = rep;
        this.urr = urr;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            AppUser creds = new ObjectMapper()
                    .readValue(req.getInputStream(), AppUser.class);

            List<GrantedAuthority> roles = new ArrayList<>();
            List<UserRole> r = urr.findByAppUser_Login(creds.getLogin());
            r.forEach(userrole -> roles.add(new SimpleGrantedAuthority(userrole.getRole().getName().name())));
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getLogin(),
                            creds.getPassword(),
                            roles)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.setContentType("application/json");
        JSONObject jwtJson = new JSONObject();
        jwtJson.put("jwt", token);
        PrintWriter jwt = res.getWriter();
        jwt.println(jwtJson);
    }
}
