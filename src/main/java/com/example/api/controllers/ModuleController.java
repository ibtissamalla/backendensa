package com.example.api.controllers;

import com.example.api.models.Module;
import com.example.api.models.Matiere;
import com.example.api.models.Enseignant;
import com.example.api.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    private final ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    // Endpoint pour ajouter un module
    // ModuleController.java
    @PostMapping("/add")
    public ResponseEntity<?> addModule(@RequestBody Module module) {
        try {
            Module addedModule = moduleService.addModule(module);
            return ResponseEntity.ok(addedModule); // Renvoie le module ajouté en cas de succès
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Renvoie le message d'erreur
}
    }



    // Endpoint pour ajouter une matière à un module et affecter un enseignant
    @PostMapping("/{moduleId}/addMatiere")
    public ResponseEntity<?> addMatiereToModule(@PathVariable int moduleId,
                                                @RequestParam String nomMatiere,
                                                @RequestParam long enseignantId,
                                                @RequestParam long formationId) {
        try {
            Matiere matiere = moduleService.addMatiereToModule(moduleId, nomMatiere, enseignantId, formationId);
            return ResponseEntity.ok(matiere); // Renvoie la matière ajoutée en cas de succès
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Renvoie le message d'erreur
        }
    }

    // Endpoint pour récupérer tous les modules
    @GetMapping
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = moduleService.getAllModules();
        return ResponseEntity.ok(modules); // Renvoie la liste des modules
    }

}
