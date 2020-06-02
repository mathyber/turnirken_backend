package com.example.turnirken.repository;

import com.example.turnirken.entity.GroupParticipant;
import com.example.turnirken.entity.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GroupParticipantRepository extends JpaRepository<GroupParticipant, Long> {
    ArrayList<GroupParticipant> findByGroup(TournamentGroup g);
    Optional<GroupParticipant> findById (Long id);
    GroupParticipant findByGroup_IdAndParticipant_Id (Long gid, Long pid);
}
