package com.example.api.services;


import com.example.api.models.Etudiant;
import com.example.api.models.Formation;
import com.example.api.models.Matiere;
import com.example.api.repositories.EtudiantRepository;
import com.example.api.repositories.FormationRepository;
import com.example.api.repositories.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Etudiantservices {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    public Formation getFormationParNiveauEtude(Long etudiantId) {
        // Récupérer l'étudiant par son ID
        Optional<Etudiant> etudiantOpt = etudiantRepository.findById(etudiantId);

        if (etudiantOpt.isPresent()) {
            Etudiant etudiant = etudiantOpt.get();
            String niveauEtude = etudiant.getNiveau_etude();

            // Récupérer la formation ayant le même nom que le niveau d'étude
            Optional<Formation> formationOpt = formationRepository.findByNom(niveauEtude);

            if (formationOpt.isPresent()) {
                return formationOpt.get();
            } else {
                throw new NoSuchElementException("Aucune formation trouvée avec le nom " + niveauEtude);
            }
        } else {
            throw new NoSuchElementException("L'étudiant avec l'ID " + etudiantId + " n'existe pas.");
        }
    }
    public List<Matiere> getMatieresParFormationId(Long formationId) {
        return matiereRepository.findByFormationId(formationId);
    }



//    public String getNiveauEtude(Long etudiantId) {
//        // Supposons que etudiantRepository soit un repository de type JpaRepository<Etudiant, Long>
//        Optional<Etudiant> etudiantOpt = etudiantRepository.findById(etudiantId);
//
//        if (etudiantOpt.isPresent()) {
//            return etudiantOpt.get().getNiveau_etude();
//        } else {
//            throw new NoSuchElementException("L'étudiant avec l'ID " + etudiantId + " n'existe pas.");
//        }
//    }


//    public List<Matiere> getMatieresByEtudiantId(Long etudiantId) {
//        Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
//        if (etudiant != null && etudiant.getFormations() != null) {
//            // Parcourir les formations de l'étudiant et récupérer les matières associées
//            return etudiant.getFormations().stream()
//                    .flatMap(formation -> formation.niveau_etude().stream())
//                    .distinct() // Pour éviter les doublons
//                    .collect(Collectors.toList());
//        }
//        return List.of(); // Retourne une liste vide si aucune formation n'est trouvée
//    }
}
