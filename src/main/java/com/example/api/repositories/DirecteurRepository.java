package com.example.api.repositories;

import com.example.api.models.Directeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirecteurRepository extends JpaRepository<Directeur, Long> {
    Directeur findByUsername(String username);
}
