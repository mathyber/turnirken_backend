package com.example.turnirken.repository;

import com.example.turnirken.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByName(String name);
}
