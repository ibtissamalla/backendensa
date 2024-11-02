package com.example.api.controllers;

import com.example.api.models.Cours;
import com.example.api.models.Etudiant;
import com.example.api.models.Matiere;
import com.example.api.services.MatiereService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class MatiereController {

    private final MatiereService matiereService;

    public MatiereController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    // Récupérer les noms et contenus des cours d'une matière
    @GetMapping("/matieres/{matiereId}/cours")
    public ResponseEntity<List<Object[]>> getCoursByMatiereId(@PathVariable int matiereId) {
        List<Object[]> cours = matiereService.findCoursNamesAndContentsByMatiereId(matiereId);
        if (cours.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        List<Object[]> result = new ArrayList<>();
        for (Object[] item : cours) {
            result.add(item); // Ajoutez le nom et le contenu du cours
        }

        return ResponseEntity.ok(result);
    }

    //    @PostMapping("/matieres/{matiereId}/cours")
//    public ResponseEntity<Cours> addCours(@PathVariable int matiereId, @RequestBody Cours cours) {
//        Cours savedCours = matiereService.addCoursToMatiere(matiereId, cours);
//        return ResponseEntity.status(201).body(savedCours); // 201 Created
//    }
    @PostMapping("/matieres/{matiereId}/cours")
    public ResponseEntity<Cours> addCours(@PathVariable int matiereId,
                                          @RequestParam String nom,
                                          @RequestParam MultipartFile contenu) {
        try {
            // Vérifiez si le fichier n'est pas vide
            if (contenu.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // ou gérer selon votre logique
            }

            // Lire le contenu du fichier en utilisant InputStream
            byte[] bytes = contenu.getBytes(); // Cela peut lever une IOException

            // Créer un objet Cours avec les données
            Cours cours = new Cours();
            cours.setNom(nom);
            cours.setContenu(bytes); // Assurez-vous que vous avez un setter pour le contenu

            // Logique d'ajout du cours
            Cours savedCours = matiereService.addCoursToMatiere(matiereId, cours);
            return ResponseEntity.status(201).body(savedCours);
        } catch (IOException e) {
            // Gérer l'exception d'entrée/sortie
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


//    @PostMapping("/{matiereId}/cours")
//    public ResponseEntity<Cours> addCours(
//            @PathVariable int matiereId,
//            @RequestParam("nom") String nom,
//            @RequestParam("contenu") MultipartFile contenu) {
//
//        // Créer un nouvel objet Cours avec le nom
//        Cours cours = new Cours();
//        cours.setNom(nom);
//
//        // Convertir le fichier en tableau de bytes
//        if (contenu != null && !contenu.isEmpty()) {
//            try {
//                byte[] pdfBytes = contenu.getBytes();
//                cours.setContenu(pdfBytes); // Stockez le tableau de bytes dans l'objet Cours
//            } catch (IOException e) {
//                // Gérer l'exception en cas de problème lors de la lecture du fichier
//                return ResponseEntity.badRequest().build(); // Retourner une réponse 400 Bad Request
//            }
//        }
//
//        Cours savedCours = matiereService.addCoursToMatiere(matiereId, cours);
//        return ResponseEntity.status(201).body(savedCours); // 201 Created
//    }
//

    @DeleteMapping("/matieres/{matiereId}/cours/{coursId}")
    public ResponseEntity<String> deleteCours(@PathVariable int matiereId, @PathVariable Long coursId) {
        try {
            matiereService.deleteCoursById(matiereId, coursId);
            return ResponseEntity.ok("Cours supprimé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Cours non trouvé.");
        }
    }
    // Récupérer les matières d'un module spécifique
    @GetMapping("/modules/{moduleId}/matieres")
    public ResponseEntity<List<Matiere>> getMatieresByModuleId(@PathVariable int moduleId) {
        List<Matiere> matieres = matiereService.getMatieresByModuleId(moduleId);
        if (matieres.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(matieres);
    }
    @DeleteMapping("/matieres/{matiereId}")
    public ResponseEntity<String> deleteMatiere(@PathVariable int matiereId) {
        try {
            matiereService.deleteMatiereById(matiereId);
            return ResponseEntity.ok("Matière supprimée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Matière non trouvée.");
        }
    }

//
//    @GetMapping("/matieres/{matiereId}/etudiants")
//    public List<Etudiant> getEtudiantsByMatiere(@PathVariable int matiereId) {
//        return matiereService.getEtudiantsByMatiereId(matiereId);
//    }
}