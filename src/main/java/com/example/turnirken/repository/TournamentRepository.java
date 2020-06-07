package com.example.turnirken.repository;

import com.example.turnirken.entity.AppUser;
import com.example.turnirken.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Tournament findByTournamentName_Name(String name);
    Optional<Tournament> findById(Long id);
    Set<Tournament> findByTournamentName_NameContainingIgnoreCase(String name);
    Set<Tournament> findByTournamentName_Game_NameContainingIgnoreCase(String name);
    Set<Tournament> findByOrganizer(AppUser user);
}
