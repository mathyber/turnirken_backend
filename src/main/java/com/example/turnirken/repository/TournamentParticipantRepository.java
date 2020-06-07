package com.example.turnirken.repository;

import com.example.turnirken.entity.Tournament;
import com.example.turnirken.entity.TournamentParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TournamentParticipantRepository extends JpaRepository<TournamentParticipant, Long> {
    Optional<TournamentParticipant> findById(Long id);
    Set<TournamentParticipant> findByTournament_Id(Long tourId);
    ArrayList<TournamentParticipant> findByTournament(Tournament tour);
    Set<TournamentParticipant> findByUser_Id(Long id);
    Set<TournamentParticipant> findByUser_IdAndNameInTournament(Long id, String name);
    TournamentParticipant findByUser_IdAndTournament_Id(Long id, Long t);
}