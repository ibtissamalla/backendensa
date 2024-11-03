package com.example.api.controllers;


import com.example.api.models.Etudiant;
import com.example.api.repositories.EnseignantRepository;
import com.example.api.repositories.EtudiantRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/directeurs")
public class DirecteurController {

    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;

    @GetMapping("/GetAllEtudiants")
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAllByStatutActif();
    }

    //ajouter etudiant
    @PostMapping("/register/etudiant")
    public ResponseEntity<Object> registerEtudiant(@Valid @RequestBody Etudiant etudiant) {
        System.out.println(etudiant); // Log the incoming Etudiant object
        etudiant.setPassword(new BCryptPasswordEncoder().encode(etudiant.getPassword()));
        etudiant.setStatut("actif");
        etudiantRepository.save(etudiant);
        return ResponseEntity.ok(Map.of("message", "Etudiant inscrit avec succès"));
    }

    @PutMapping("/delete/etudiant/{id}")
    public ResponseEntity<Object> inactivateEtudiant(@PathVariable Long id) {
        Optional<Etudiant> optionalEtudiant = etudiantRepository.findById(id);

        if (optionalEtudiant.isPresent()) {
            try {
                Etudiant etudiant = optionalEtudiant.get();
                etudiant.setStatut("inactif");  // Assuming there's a 'statut' field in the Etudiant entity
                etudiantRepository.save(etudiant);
                return ResponseEntity.ok(Map.of("message", "L'étudiant est supprimé avec succès"));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erreur lors de la suppression de l'étudiant"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Etudiant non trouvé"));
        }
    }

    @PutMapping("/update/etudiant/{id}")
    public ResponseEntity<Object> updateEtudiant(@PathVariable Long id, @Valid @RequestBody Etudiant etudiantDetails) {
        Optional<Etudiant> optionalEtudiant = etudiantRepository.findById(id);

        if (optionalEtudiant.isPresent()) {
            Etudiant etudiant = optionalEtudiant.get();

            // Met à jour les champs, en conservant l'ancienne valeur si la nouvelle est null ou vide
            etudiant.setFirstName(etudiantDetails.getFirstName() != null ? etudiantDetails.getFirstName() : etudiant.getFirstName());
            etudiant.setLastName(etudiantDetails.getLastName() != null ? etudiantDetails.getLastName() : etudiant.getLastName());
            etudiant.setEmail(etudiantDetails.getEmail() != null && !etudiantDetails.getEmail().isEmpty() ? etudiantDetails.getEmail() : etudiant.getEmail());
            etudiant.setUsername(etudiantDetails.getUsername() != null ? etudiantDetails.getUsername() : etudiant.getUsername());
            etudiant.setPhone(etudiantDetails.getPhone() != null ? etudiantDetails.getPhone() : etudiant.getPhone());
            etudiant.setNiveau_etude(etudiantDetails.getNiveau_etude() != null ? etudiantDetails.getNiveau_etude() : etudiant.getNiveau_etude());
            etudiant.setCode_appogee(etudiantDetails.getCode_appogee() != null ? etudiantDetails.getCode_appogee() : etudiant.getCode_appogee());
            etudiant.setDate_inscription(etudiantDetails.getDate_inscription() != null ? etudiantDetails.getDate_inscription() : etudiant.getDate_inscription());

            // Vérifiez si le mot de passe n'est pas vide avant de l'encoder et de le mettre à jour
            if (etudiantDetails.getPassword() != null && !etudiantDetails.getPassword().isEmpty()) {
                etudiant.setPassword(new BCryptPasswordEncoder().encode(etudiantDetails.getPassword()));
            }

            System.out.println("Updating student: " + etudiant);
            etudiantRepository.save(etudiant);

            return ResponseEntity.ok(Map.of("message", "Étudiant mis à jour avec succès"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Etudiant non trouvé"));
        }
    }

    @GetMapping("/nombre/etudiants")
    public long getEtudiantNumber() {
        return etudiantRepository.count();
    }

    @GetMapping("/nombre/enseignants")
    public long getEnseignantNumber() {
        return enseignantRepository.count();
    }




}
