package com.example.api.controllers;

import com.example.api.models.Formation;
import com.example.api.repositories.FormationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formations")
@CrossOrigin(origins = "http://localhost:4200")
public class FormationController {

    @Autowired
    private FormationRepository formationRepository;

    // Endpoint pour ajouter une formation avec validation
    @PostMapping
    public ResponseEntity<?> addFormation(@Valid @RequestBody Formation formation) {
        // Vérifie si une formation avec le même nom existe déjà
        if (formationRepository.existsByNom(formation.getNom())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Formation déjà existante avec le nom : " + formation.getNom());
        }
        Formation savedFormation = formationRepository.save(formation);
        return ResponseEntity.ok(savedFormation);
    }

    // Endpoint pour modifier une formation
    @PutMapping("/{id}")
    public ResponseEntity<Formation> updateFormation(@PathVariable Long id, @RequestBody Formation formationDetails) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID : " + id));

        formation.setNom(formationDetails.getNom());
        formation.setDescription(formationDetails.getDescription());
        formation.setDuree(formationDetails.getDuree());

        final Formation updatedFormation = formationRepository.save(formation);
        return ResponseEntity.ok(updatedFormation);
    }


    // Endpoint pour récupérer toutes les formations
    @GetMapping
    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    // Endpoint pour calculer le nombre de formations
    @GetMapping("/count")
    public long countFormations() {
        return formationRepository.count();
    }
}
