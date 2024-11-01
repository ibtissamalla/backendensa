package com.example.api.controllers;

import com.example.api.models.Note;
import com.example.api.models.UpdateNoteRequest;
import com.example.api.services.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // Endpoint pour récupérer les notes d'une matière donnée
    @GetMapping("/matiere/{id}")
    public List<Map<String, Object>> getNotesByMatiere(@PathVariable Long id) {
        return noteService.getNotesByMatiere(id);
    }

    // Endpoint pour mettre à jour la note d'un étudiant pour une matière donnée
    @PutMapping("/etudiant/{etudiantId}/matiere/{matiereId}")
    public ResponseEntity<Note> updateNote(@PathVariable Long etudiantId, @PathVariable Long matiereId, @RequestBody @Valid UpdateNoteRequest request) {
        Note updatedNote = noteService.updateNote(etudiantId, matiereId, request.getNewValeur());
        return ResponseEntity.ok(updatedNote);
}
}