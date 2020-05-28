package com.example.turnirken.repository;

import com.example.turnirken.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Tournament findByTournamentName_Name(String name);
}
