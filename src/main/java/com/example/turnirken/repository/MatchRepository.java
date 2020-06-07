package com.example.turnirken.repository;

import com.example.turnirken.entity.AppUser;
import com.example.turnirken.entity.Match;
import com.example.turnirken.entity.TournamentGroup;
import com.example.turnirken.entity.TournamentParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Set<Match> findByPlayoff_Tournament_Id(Long id);
    Set<Match> findByRound_Group_Tournament_Id(Long id);
    Set<Match> findByRound_Group_Id(Long id);
    Set<Match> findByPlayer1_Id(Long id);
    Set<Match> findByPlayer2_Id(Long id);
    Set<Match> findByPlayer1AndRound_Group(TournamentParticipant tp, TournamentGroup tg);
    Set<Match> findByPlayer2AndRound_Group(TournamentParticipant tp, TournamentGroup tg);
    Set<Match> findByRound_Group_IdAndPlayer1Null(Long id);
    Set<Match> findByRound_Group_IdAndPlayer2Null(Long id);
    Set<Match> findByPlayer1_User(AppUser au);
    Set<Match> findByPlayer2_User(AppUser au);

}
