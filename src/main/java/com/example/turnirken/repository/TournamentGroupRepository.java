package com.example.turnirken.repository;

import com.example.turnirken.entity.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Long> {
    Set<TournamentGroup> findByTournament_Id(Long id);
}
