package com.example.turnirken.repository;

import com.example.turnirken.entity.Next;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NextRepository extends JpaRepository<Next, Long> {

}
