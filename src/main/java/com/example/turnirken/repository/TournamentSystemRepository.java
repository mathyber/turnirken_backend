package com.example.turnirken.repository;

import com.example.turnirken.entity.TournamentSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentSystemRepository extends JpaRepository<TournamentSystem, Long> {
    TournamentSystem findByName(String name);
    Optional<TournamentSystem> findById(Long id);

}
