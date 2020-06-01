package com.example.turnirken.repository;

import com.example.turnirken.entity.Next;
import com.example.turnirken.entity.NextType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface NextRepository extends JpaRepository<Next, Long> {
    Set<Next> findByThisTypeAndIdThis(NextType nt, int id);
    Set<Next> findByNextTypeAndIdNext(NextType nt, int id);
}
