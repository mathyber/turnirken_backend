package com.example.turnirken.repository;

import com.example.turnirken.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {
    Stage findByName(String name);
}
