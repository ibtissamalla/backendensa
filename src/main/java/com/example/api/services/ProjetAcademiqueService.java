package com.example.api.services;

import com.example.api.models.Enseignant;
import com.example.api.models.Etudiant;
import com.example.api.models.ProjetAcademique;
import com.example.api.repositories.EnseignantRepository;
import com.example.api.repositories.EtudiantRepository;
import com.example.api.repositories.ProjetAcademiqueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.api.models.Etudiant;

import java.util.List;

@Service
public class ProjetAcademiqueService {
    @Autowired
    private ProjetAcademiqueRepository projetAcademiqueRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    // Récupérer les projets académiques par ID d'enseignant
    public List<ProjetAcademique> getProjetsByEnseignantId(Long enseignantId) {
        List<ProjetAcademique> projets = projetAcademiqueRepository.findByEnseignantId(enseignantId);

        for (ProjetAcademique projet : projets) {
            if (projet.getEtudiant() != null) {
                Etudiant etudiant = etudiantRepository.findById(projet.getEtudiant().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Etudiant non trouvé pour le projet ID : " + projet.getId()));
                projet.setEtudiant(etudiant);
            }
        }

        return projets;
    }


//    public void deleteProjet(Long id) {
//        projetAcademiqueRepository.deleteById(id);
//    }

    public ProjetAcademique addProjet(ProjetAcademique projetAcademique) {
        if (projetAcademique.getEnseignant() != null && projetAcademique.getEnseignant().getId() > 0) {
            Enseignant enseignant = enseignantRepository.findById(Long.valueOf(projetAcademique.getEnseignant().getId()))
                    .orElseThrow(() -> new RuntimeException("Enseignant not found"));
            projetAcademique.setEnseignant(enseignant);
        } else {
            throw new RuntimeException("Enseignant ID is required and must be greater than 0");
        }

        if (projetAcademique.getEtudiant() != null && projetAcademique.getEtudiant().getId() > 0) {
            Etudiant etudiant = etudiantRepository.findById(Long.valueOf(projetAcademique.getEtudiant().getId()))
                    .orElseThrow(() -> new RuntimeException("Etudiant not found"));
            projetAcademique.setEtudiant(etudiant);
        } else {
            throw new RuntimeException("Etudiant ID is required and must be greater than 0");
        }

        return projetAcademiqueRepository.save(projetAcademique);
    }

//    public ProjetAcademique addProjet(ProjetAcademique projetAcademique) {
//        if (projetAcademique.getEnseignant() == null || projetAcademique.getEnseignant().getId() == null) {
//            throw new IllegalArgumentException("Enseignant is missing or invalid in the request.");
//        }
//
//        Enseignant enseignant = enseignantRepository.findById(Long.valueOf(projetAcademique.getEnseignant().getId()))
//                .orElseThrow(() -> new EntityNotFoundException("Enseignant not found"));
//
//        projetAcademique.setEnseignant(enseignant);
//        return projetAcademiqueRepository.save(projetAcademique);
//    }

}