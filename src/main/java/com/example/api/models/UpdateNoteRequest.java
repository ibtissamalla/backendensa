package com.example.api.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdateNoteRequest {
    @Min(0)
    @Max(20)
    private double newValeur;

    // Constructeur par défaut
    public UpdateNoteRequest() {}

    public UpdateNoteRequest(double newValeur) {
        this.newValeur = newValeur;
    }

    public double getNewValeur() {
        return newValeur;
    }

    public void setNewValeur(double newValeur) {
        this.newValeur = newValeur;
    }
}
