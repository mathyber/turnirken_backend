package com.example.turnirken.repository;

import com.example.turnirken.entity.TournamentParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentParticipantsRepository extends JpaRepository<TournamentParticipant, Long> {
    Optional<TournamentParticipant> findById(Long id);
}
