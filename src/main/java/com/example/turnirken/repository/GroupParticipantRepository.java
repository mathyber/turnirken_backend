package com.example.turnirken.repository;

import com.example.turnirken.entity.GroupParticipant;
import com.example.turnirken.entity.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface GroupParticipantRepository extends JpaRepository<GroupParticipant, Long> {
    ArrayList<GroupParticipant> findByGroup(TournamentGroup g);
}
