package com.example.turnirken.security;

class SecurityConstants {
    static final String SECRET = "vkodsdvjsfnjvdjkvnvgwurigh4hg49gh849hg94whgrhw4g459hw4g9ehgw49hg74whg84h5g8w4hgwog8wh749gohw4g95h4ow9egho94w8hg59w4hg95";
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/api/user/registration",
            "/api/user/registration/testLogin",
            "/api/user/registration/testEmail",
            "/api/login",
            "/api/tournaments/getTournaments",
            "/api/tournaments/getGroups",
            "/api/tournaments/getMatches",
            "/api/tournaments/getParticipants",
            "/api/tournaments/getTournamentId",
            "/api/tournaments/searchTournaments",
            "/api/tournaments/searchTournamentsNameGame",
            "/api/matches/getMatchById",
            "/api/matches/getMatchesGroup",
            "/api/matches/getAllMatchesTournament",
            // other public endpoints of your API may be appended to this array
    };

    static final String[] MODERATOR_LIST = {
            "/api/games/moderator/createOnDisplayGame",
            "/api/games/moderator/setOnDisplayGame",
    };

    static final String[] ADMIN_LIST = {
            "/api/games/moderator/createOnDisplayGame",
            "/api/games/moderator/setOnDisplayGame",
            "/api/user/setRole",
    };
}
