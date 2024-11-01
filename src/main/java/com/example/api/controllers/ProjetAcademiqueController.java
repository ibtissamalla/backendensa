package com.example.api.controllers;

import com.example.api.models.ProjetAcademique;
import com.example.api.services.ProjetAcademiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projets")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjetAcademiqueController {
    @Autowired
    private ProjetAcademiqueService projetAcademiqueService;

    // Obtenir tous les projets d'un enseignant avec les informations de l'étudiant
    @GetMapping("/enseignants/{enseignantId}")
    public ResponseEntity<List<ProjetAcademique>> getProjetsByEnseignant(@PathVariable long enseignantId) {
        List<ProjetAcademique> projets = projetAcademiqueService.getProjetsByEnseignantId(enseignantId);
        return ResponseEntity.ok(projets);
    }

    // Supprimer un projet académique
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteProjet(@PathVariable Long id) {
//        projetAcademiqueService.deleteProjet(id);
//        return ResponseEntity.ok("Projet supprimé avec succès.");
//    }
}
