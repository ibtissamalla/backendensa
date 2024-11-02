package com.example.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "cours")

public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom; // Nom du cours

    @Lob // Indique que ce champ est un type BLOB
    private byte[] contenu; // Contenu binaire du cours (ex: PDF, image, etc.)

    @ManyToOne(fetch = FetchType.LAZY) // Utilise le chargement paresseux pour de meilleures performances
    @JoinColumn(name = "id_matiere", nullable = false)
    private Matiere matiere; // Relation avec Matiere

    // Constructeur par défaut requis par JPA
    public Cours() {
    }

    // Constructeur avec paramètres pour nom, contenu et matière
    public Cours(String nom, byte[] contenu, Matiere matiere) {
        this.nom = nom;
        this.contenu = contenu;
        this.matiere = matiere;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public byte[] getContenu() {
        return contenu;
    }

    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", matiere=" + (matiere != null ? matiere.getNom() : "null") +
                '}';
    }
}