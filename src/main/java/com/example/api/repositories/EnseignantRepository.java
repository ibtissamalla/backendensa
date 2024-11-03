package com.example.api.repositories;

import com.example.api.models.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    Enseignant findByUsername(String username);
    Enseignant findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    long count();
}

