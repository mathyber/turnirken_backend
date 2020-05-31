package com.example.turnirken.repository;

import com.example.turnirken.entity.NextType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NextTypeRepository extends JpaRepository<NextType, Long> {
    NextType findByName(String s);
}
