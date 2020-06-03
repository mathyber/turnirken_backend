package com.example.turnirken.repository;

import com.example.turnirken.entity.AppUser;
import com.example.turnirken.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Set;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
 //   Set<MatchResult> findByMatch_Id(Long id);
    ArrayList<MatchResult> findByMatch_Id(Long id);
    ArrayList<MatchResult> findByMatch_IdAndResultCreator(Long id, AppUser au);
}
