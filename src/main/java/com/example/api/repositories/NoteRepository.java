package com.example.api.repositories;

import com.example.api.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("SELECT n.valeur, e.id, e.firstName, e.lastName, m.nom " +
            "FROM Note n " +
            "JOIN n.etudiant e " +
            "JOIN n.matiere m " +
            "WHERE m.id = :matiereId")
    List<Object[]> findByMatiere_Id(@Param("matiereId") Long matiereId);

    Optional<Note> findByEtudiant_IdAndMatiere_Id(Long etudiantId, Long matiereId);

//    @Query("SELECT n, m.nom FROM Note n JOIN n.matiere m WHERE n.etudiant.id = :etudiantId")
//    List<Note> findNotesByEtudiantWithMatiere(@Param("etudiantId") Long etudiantId);

    @Query("SELECT n, m.nom FROM Note n JOIN n.matiere m WHERE n.etudiant.id = :etudiantId")
    List<Object[]> findNotesByEtudiantWithMatiere(@Param("etudiantId") Long etudiantId);

    // Récupérer toutes les notes d'un étudiant par matière
    List<Note> findByEtudiant_Id(Long etudiantId);

}