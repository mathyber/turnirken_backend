package com.example.turnirken.repository;

import com.example.turnirken.entity.GroupParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupParticipantRepository extends JpaRepository<GroupParticipant, Long> {

}
