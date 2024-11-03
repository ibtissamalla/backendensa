package com.example.api.repositories;

import com.example.api.models.ProjetAcademique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetAcademiqueRepository extends JpaRepository<ProjetAcademique, Long> {
    List<ProjetAcademique> findByEnseignantId(Long enseignantId); // Changer int en Long
}