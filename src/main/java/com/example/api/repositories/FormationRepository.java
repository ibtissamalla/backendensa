package com.example.api.repositories;

import com.example.api.models.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    boolean existsByNom(String nom); // Méthode pour vérifier l'existence par nom
    Optional<Formation> findByNom(String nom);
}
