package com.example.api.repositories;

import com.example.api.models.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Etudiant findByUsername(String username);

    @Query("SELECT e FROM Etudiant e WHERE e.statut = 'actif'")
    List<Etudiant> findAllByStatutActif();

    long count();
}
