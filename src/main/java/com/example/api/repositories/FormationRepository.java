package com.example.api.repositories;

import com.example.api.models.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    boolean existsByNom(String nom); // Méthode pour vérifier l'existence par nom
}
