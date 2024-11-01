package com.example.api.services;

import org.springframework.stereotype.Service;
import com.example.api.models.ProjetAcademique;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class ConventionService {

    public byte[] generateConvention(ProjetAcademique projet) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Adding the logo
        PDImageXObject pdImage = PDImageXObject.createFromFile("D:\\SGSA\\SGSA-backend\\API\\src\\main\\resources\\ENSA-LOGO-4.jpg", document);
        contentStream.drawImage(pdImage, 500, 688, 100, 100);

        // Header
        contentStream.setNonStrokingColor(0, 0, 255);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("Université Abdelmalek Essaâdi");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Ecole Nationale des Sciences Appliquées");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Tétouan");
        contentStream.endText();

        // Title "CONVENTION DE STAGE"
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        contentStream.beginText();
        contentStream.newLineAtOffset(200, 700);
        contentStream.showText("CONVENTION DE STAGE");
        contentStream.endText();

        // Detailed content for each article
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 650);  // Start position

        // Add content with controlled line breaks
        String[] lines = {
                "ENTRE",
                "L’Ecole Nationale des Sciences Appliquées, Université Abdelmalek Essaâdi - Tétouan",
                "B.P. 2222, Mhannech II, Tétouan, Maroc",
                "Tél. +212 5 39 68 80 27 ; Fax. +212 39 99 46 24. Web: https://ensa-tetouan.ac.ma",
                "Représenté par le Professeur Kamal REKLAOUI en qualité de Directeur.",

                "ET La Société : " + projet.getSociete(),

                "",
                // Article 1
                "Article 1 : Engagement",
                "L’ENTREPRISE accepte de recevoir à titre de stagiaire " + projet.getEtudiant().getFirstName() + " qui a le code appogee " + projet.getEtudiant().getCode_appogee() + ",",
                "étudiant de la filière " + projet.getEtudiant().getNiveau_etude() + " de l’ENSA de Tétouan, Université Abdelmalek Essaâdi (Tétouan)",
                " pour une période allant du " + projet.getDatedebut() + " au " + projet.getDatefin() + ".",
                "En aucun cas, cette convention ne pourra autoriser les étudiants à s’absenter durant la période des contrôles ou des"," enseignements.",
                "",
                // Article 2
                "Article 2 : Objet",
                "Le stage aura pour objet essentiel d'assurer l'application pratique de l'enseignement donné par l'Etablissement, en organisant",
                " des visites et des études proposées par L’ENTREPRISE.",
                "",
                // Article 3
                "Article 3 : Programme",
                "Le thème du stage est: " + projet.getSujet(),
                "Ce programme a été défini conjointement par l'Etablissement, L’ENTREPRISE et le Stagiaire.",
                "Le contenu de ce programme doit permettre au Stagiaire une réflexion en relation avec les enseignements ou le projet de fin ",
                "d'études qui s'inscrit dans le programme de formation de l'Etablissement.",
                "",
                // Article 4
                "Article 4 : Rapport de stage",
                "A l'issue de chaque stage, le Stagiaire rédigera un rapport de stage faisant état de ses travaux et de son vécu au sein de ",
                "L’ENTREPRISE. Ce rapport sera communiqué à L’ENTREPRISE et restera strictement confidentiel.",

                "",
                "Fait à Tétouan , le " + LocalDate.now(),
                "Nom et signature du Stagiaire : " + projet.getEtudiant().getFirstName() + " " + projet.getEtudiant().getLastName(),  // Adding student name
                "Encadrant : " + projet.getEnseignant().getLastName()+" "+projet.getEnseignant().getFirstName(),  // Adding teacher's name
                "Signature et cachet de L’Etablissement",
                "Signature et cachet de L’ENTREPRISE",
        };

        for (String line : lines) {
            contentStream.newLineAtOffset(0, -15);  // Move down for next line

            // Check if the line starts with "Article" to make it bold
            if (line.startsWith("Article")) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);  // Set bold font for article title
                contentStream.showText(line);
                contentStream.setFont(PDType1Font.HELVETICA, 10);  // Switch back to regular font
            } else {
                contentStream.showText(line);
            }
        }

        contentStream.endText();
        contentStream.close();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }
}
