package com.example.api.controllers;

import com.example.api.models.*;

import com.example.api.models.Module;
import com.example.api.repositories.EtudiantRepository;
import com.example.api.repositories.NoteRepository;
import com.example.api.repositories.ModuleRepository;
import com.example.api.repositories.MatiereRepository;
import com.example.api.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.api.models.Etudiant;
import com.example.api.repositories.EtudiantRepository;
import com.example.api.services.AttestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


import java.util.*;

//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

@RestController
@RequestMapping("/etudiants")
//@CrossOrigin(origins = "*")

public class EtudiantController {

//    @RequestMapping(method = RequestMethod.OPTIONS)
//    public ResponseEntity<?> handleOptionsRequest() {
//        return ResponseEntity.ok().build();
//    }

    @Autowired
    private NoteRepository noteRepository;


    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private MatiereRepository matiereRepository;
    private final MatiereService matiereService;
    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private AttestationService attestationService;

    @Autowired
    private ProjetAcademiqueService projetAcademiqueService;
    @Autowired
    private ConventionService conventionService;

    @Autowired
    private EnseignantService enseignantService;



    // Constructor injection
    public EtudiantController(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    // Méthode pour obtenir les modules et les notes d'un étudiant
    @GetMapping("/{etudiantId}/notes/modules")
    public ResponseEntity<List<Map<String, Object>>> getModulesWithNotes(@PathVariable Long etudiantId) {
        List<Module> modules = moduleRepository.findAll(); // Cela devrait maintenant fonctionner correctement
        List<Map<String, Object>> result = new ArrayList<>();

        for (Module module : modules) {
            Map<String, Object> moduleData = new HashMap<>();
            moduleData.put("moduleId", module.getId());
            moduleData.put("moduleNom", module.getNom());

            // Récupérer les matières du module
            List<Matiere> matieres = matiereRepository.findByModule_Id(module.getId());
            List<Double> notes = new ArrayList<>();

            for (Matiere matiere : matieres) {
                // Récupérer la note de l'étudiant pour chaque matière
                Optional<Note> matiereNote = noteRepository.findByEtudiant_IdAndMatiere_Id(etudiantId, matiere.getId());
                matiereNote.ifPresent(note -> notes.add(note.getValeur())); // Ajoute la valeur de la note si elle est présente
            }


            // Calculer la note moyenne pour le module
            if (!notes.isEmpty()) {
                // Calculer la note moyenne pour le module
                double moyenne = notes.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                moduleData.put("moyenneNote", moyenne);
                result.add(moduleData);
            }
        }
        return ResponseEntity.ok(result);
    }

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
    @GetMapping("/attestation/{id}")
    public ResponseEntity<byte[]> getAttestation(@PathVariable Long id, Authentication authentication) {
        // Vérifier si l'utilisateur est authentifié
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(null);
        }

        // Récupérer l'étudiant par son ID
        Etudiant etudiant = etudiantRepository.findById(id).orElse(null);
        if (etudiant == null) {
            return ResponseEntity.status(404).body(null);
        }

        try {
            // Générer le PDF d'attestation
            byte[] pdfBytes = attestationService.generateAttestation(etudiant);

            // Configurer les en-têtes de la réponse
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=attestation.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @PostMapping("/convention")
    public ResponseEntity<?> generateConvention(@RequestBody ProjetAcademique projetAcademique) {
        System.out.println("Incoming ProjetAcademique: " + projetAcademique);

        if (projetAcademique.getEnseignant() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Enseignant is missing in the request.");
        }
        try {
            // Validate and save the data if needed
            System.out.println(projetAcademique.getEnseignant().getId());
            ProjetAcademique savedProjet = projetAcademiqueService.addProjet(projetAcademique);

            // Generate the internship agreement (e.g., PDF)
            byte[] conventionPdf = conventionService.generateConvention(savedProjet);

            // Return the PDF file as a response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"convention_stage.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(conventionPdf);
        } catch (Exception e) {
            e.printStackTrace(); // Log the actual exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating the convention: " + e.getMessage());
        }
    }

    @GetMapping("/{etudiantId}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long etudiantId) {

        Optional<Etudiant> etudiantOptional = etudiantRepository.findById(etudiantId);

        if (etudiantOptional.isPresent()) {
            return ResponseEntity.ok(etudiantOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/projet/{etudiantId}")
    public List<ProjetAcademique> getProjetsByEtudiantId(@PathVariable Long etudiantId) {
        return projetAcademiqueService.getProjetsByEtudiantId(etudiantId);
    }
    @GetMapping("/enseignants")
    public List<Enseignant> getAllEnseignants() {
        return enseignantService.findAll();
    }

//    @GetMapping("/{etudiantId}/notes")
//    public ResponseEntity<List<Note>> getNotesByEtudiant(@PathVariable int etudiantId) {
//        // Récupérer l'étudiant par ID
//        Etudiant etudiant = etudiantRepository.findById(etudiantId)
//                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé avec id : " + etudiantId));
//
//        // Récupérer la liste des notes associées à l'étudiant
//        List<Note> notes = etudiant.getNotes();
//
//        return ResponseEntity.ok(notes);
//    }



//    @GetMapping("/{etudiantId}/notes")
//    public ResponseEntity<List<Note>> getNotesByEtudiantWithMatiere(@PathVariable Long etudiantId) {
//        // Utilisation de la méthode personnalisée pour récupérer les notes avec les matières
//        List<Note> notes = noteRepository.findNotesByEtudiantWithMatiere(etudiantId);
//
//        // Retourne les notes sous forme de réponse HTTP
//        return ResponseEntity.ok(notes);
//    }
//    public ResponseEntity<List<Object[]>> getNotesByEtudiantWithMatiere(@PathVariable Long etudiantId) {
//        // Utilisation de la méthode personnalisée pour récupérer les notes avec les matières
//        List<Object[]> notes = noteRepository.findNotesByEtudiantWithMatiere(etudiantId);
//
//        // Retourne les notes sous forme de réponse HTTP
//        return ResponseEntity.ok(notes);
//    }


}
