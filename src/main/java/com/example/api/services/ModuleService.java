package com.example.api.services;

import com.example.api.models.Module;
import com.example.api.models.Matiere;
import com.example.api.models.Enseignant;
import com.example.api.models.Formation; // Import Formation
import com.example.api.repositories.ModuleRepository;
import com.example.api.repositories.MatiereRepository;
import com.example.api.repositories.EnseignantRepository;
import com.example.api.repositories.FormationRepository; // Import FormationRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final MatiereRepository matiereRepository;
    private final EnseignantRepository enseignantRepository;
    private final FormationRepository formationRepository; // Add FormationRepository

    @Autowired
    public ModuleService(ModuleRepository moduleRepository,
                         MatiereRepository matiereRepository,
                         EnseignantRepository enseignantRepository,
                         FormationRepository formationRepository) { // Add FormationRepository to constructor
        this.moduleRepository = moduleRepository;
        this.matiereRepository = matiereRepository;
        this.enseignantRepository = enseignantRepository;
        this.formationRepository = formationRepository; // Initialize FormationRepository
    }

    // Ajouter un module
    public Module addModule(Module module) {
        // Vérifier si un module avec le même nom existe déjà
        if (moduleRepository.findByNom(module.getNom()).isPresent()) {
            throw new RuntimeException("Le module \"" + module.getNom() + "\" existe déjà.");
        }
        return moduleRepository.save(module); // Enregistre le module
    }




    // Ajouter une matière à un module et affecter un enseignant
    public Matiere addMatiereToModule(int moduleId, String nomMatiere, long enseignantId, long formationId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module non trouvé"));

        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée")); // Fetch Formation

        // Vérifier si la matière est déjà affectée à cet enseignant
        List<Matiere> matieresExistantes = matiereRepository.findByModule_Id(moduleId);
        for (Matiere matiereExistante : matieresExistantes) {
            if (matiereExistante.getNom().equalsIgnoreCase(nomMatiere) &&
                    matiereExistante.getEnseignant().getId() == enseignantId) {
                throw new RuntimeException("La matière \"" + nomMatiere + "\" est déjà affectée à cet enseignant.");
            }
        }

        Matiere matiere = new Matiere();
        matiere.setNom(nomMatiere);
        matiere.setModule(module);
        matiere.setEnseignant(enseignant);
        matiere.setFormation(formation); // Set Formation

        return matiereRepository.save(matiere);
    }

    public List<Module> getAllModules() {
        return moduleRepository.findAll(); // Assumes you have a findAll method in your repository
    }

}
