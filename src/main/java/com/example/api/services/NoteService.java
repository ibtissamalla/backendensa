package com.example.api.services;

import com.example.api.models.Note;
import com.example.api.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Map<String, Object>> getNotesByMatiere(Long matiereId) {
        List<Object[]> results = noteRepository.findByMatiere_Id(matiereId);
        List<Map<String, Object>> formattedNotes = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("valeur", row[0]);  // Note value
            map.put("id_etudiant", row[1]);
            map.put("etudiantFirstName", row[2]);  // Student's first name
            map.put("etudiantLastName", row[3]);  // Student's last name
//            map.put("matiereNom", row[4]);  // Subject name
            formattedNotes.add(map);
        }

        return formattedNotes;
    }

    public Note updateNote(Long etudiantId, Long matiereId, double newValeur) {
        Note note = noteRepository.findByEtudiant_IdAndMatiere_Id(etudiantId, matiereId)
                .orElseThrow(() -> new RuntimeException("Note not found for student ID " + etudiantId + " and subject ID " + matiereId));
        note.setValeur(newValeur);
        return noteRepository.save(note); // Enregistrer les modifications
}
}