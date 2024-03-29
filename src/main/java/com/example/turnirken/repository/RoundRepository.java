package com.example.turnirken.repository;

import com.example.turnirken.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
        Round findByNum(int num);
}
