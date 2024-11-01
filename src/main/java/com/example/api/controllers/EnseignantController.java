package com.example.api.controllers;

import com.example.api.models.Enseignant;
import com.example.api.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
public class EnseignantController {
    private final EnseignantService enseignantService;

    @Autowired
    public EnseignantController(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
    }

    @GetMapping
    public List<Enseignant> getAllEnseignants() {
        return enseignantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enseignant> getEnseignantById(@PathVariable int id) {
        return enseignantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEnseignant(@Valid @RequestBody Enseignant enseignant) {
        if (enseignantService.existsByUsername(enseignant.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Un enseignant avec ce nom d'utilisateur existe déjà : " + enseignant.getUsername());
        }
        if (enseignantService.existsByEmail(enseignant.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Un enseignant avec cet email existe déjà : " + enseignant.getEmail());
        }

        // Cryptage du mot de passe
        enseignant.setPassword(new BCryptPasswordEncoder().encode(enseignant.getPassword()));

        Enseignant savedEnseignant = enseignantService.save(enseignant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEnseignant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enseignant> updateEnseignant(@PathVariable int id, @RequestBody Enseignant enseignantDetails) {
        return enseignantService.findById(id)
                .map(enseignant -> {
                    enseignant.setFirstName(enseignantDetails.getFirstName());
                    enseignant.setLastName(enseignantDetails.getLastName());
                    enseignant.setEmail(enseignantDetails.getEmail());
                    enseignant.setUsername(enseignantDetails.getUsername());
                    enseignant.setPhone(enseignantDetails.getPhone());
                    enseignant.setSpecialite(enseignantDetails.getSpecialite());
                    return ResponseEntity.ok(enseignantService.save(enseignant));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable int id) {
        enseignantService.delete(id);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/{enseignantId}/matieres/{matiereId}")
//    public ResponseEntity<Enseignant> assignMatiere(@PathVariable int enseignantId, @PathVariable int matiereId) {
//        Enseignant enseignant = enseignantService.assignMatiere(enseignantId, matiereId);
//        return ResponseEntity.ok(enseignant);
//    }
}
