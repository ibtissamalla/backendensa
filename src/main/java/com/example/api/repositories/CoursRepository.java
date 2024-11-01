package com.example.api.repositories;

import com.example.api.models.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Integer> {
    // Ici, vous pouvez ajouter d'autres méthodes de requête personnalisées si nécessaire.
}