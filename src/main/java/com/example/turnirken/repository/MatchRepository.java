package com.example.turnirken.repository;

import com.example.turnirken.entity.Match;
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

}
