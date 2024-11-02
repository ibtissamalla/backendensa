package com.example.api.services;

import com.example.api.models.Enseignant;
import com.example.api.models.Matiere;
import com.example.api.repositories.EnseignantRepository;
import com.example.api.repositories.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {
    private final EnseignantRepository enseignantRepository;
    private final MatiereRepository matiereRepository;

    @Autowired
    public EnseignantService(EnseignantRepository enseignantRepository, MatiereRepository matiereRepository) {
        this.enseignantRepository = enseignantRepository;
        this.matiereRepository = matiereRepository;
    }

    public List<Enseignant> findAll() {
        return enseignantRepository.findAll();
    }

    public Optional<Enseignant> findById(long id) {
        return enseignantRepository.findById(id);
    }

    public Enseignant save(Enseignant enseignant) {
        return enseignantRepository.save(enseignant);
    }

    public void delete(long id) {
        Optional<Enseignant> enseignantOptional = enseignantRepository.findById(id);
        if (enseignantOptional.isPresent()) {
            enseignantRepository.delete(enseignantOptional.get());
            System.out.println("Enseignant supprimé : " + id);
        } else {
            throw new RuntimeException("Enseignant non trouvé avec l'ID : " + id);
        }
    }


    public Enseignant assignMatiere(long enseignantId, int matiereId) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant not found"));

        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new RuntimeException("Matiere not found"));

        // Prevent duplicates before adding the Matiere
        if (!enseignant.getMatieres().contains(matiere)) {
            enseignant.getMatieres().add(matiere);
            matiere.setEnseignant(enseignant); // Set the enseignant in Matiere
            matiereRepository.save(matiere); // Save the updated Matiere
        }

        return enseignantRepository.save(enseignant); // Save the updated Enseignant
    }
    public boolean existsByUsername(String username) {
        return enseignantRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return enseignantRepository.existsByEmail(email);
    }

}
