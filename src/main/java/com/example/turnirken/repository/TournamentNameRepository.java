package com.example.turnirken.repository;

import com.example.turnirken.entity.TournamentName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentNameRepository extends JpaRepository<TournamentName, Long> {
    TournamentName findByName(String name);
}
