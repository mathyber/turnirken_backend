package com.example.turnirken.repository;

import com.example.turnirken.entity.Playoff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayoffRepository extends JpaRepository<Playoff, Long> {

}
