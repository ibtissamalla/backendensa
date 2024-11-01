package com.example.api.services;

import com.example.api.models.Etudiant;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class AttestationService {

    public byte[] generateAttestation(Etudiant etudiant) throws IOException {
        // Création du ByteArrayOutputStream pour contenir les données PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Création du document PDF
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Création du flux de contenu pour la page
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDImageXObject pdImage = PDImageXObject.createFromFile("D:\\SGSA\\SGSA-backend\\API\\src\\main\\resources\\ENSA-LOGO-4.jpg", document);
        contentStream.drawImage(pdImage, 500, 688, 100, 100);

        // En-tête
        contentStream.setNonStrokingColor(0, 0, 255);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("École Nationale des Sciences Appliquées de Tétouan");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Quartier M’haneche II, avenue 9 Avril, B.P. 2117 Tétouan");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Tel : 06123456789");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Email : presidencee@uae.ac.ma");
        contentStream.endText();

        // Titre "ATTESTATION DE SCOLARITÉ"
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        contentStream.beginText();
        contentStream.newLineAtOffset(200, 620);
        contentStream.showText("ATTESTATION DE SCOLARITÉ");
        contentStream.endText();

        // Détails de l'étudiant
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 570);
        contentStream.showText("La direction des services de scolarité de l'École Nationale des Sciences Appliquées de Tétouan");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("certifie que :");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Nom : " + etudiant.getLastName());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Prénom : " + etudiant.getFirstName());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Code Apogée : " + etudiant.getCode_appogee());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Est régulièrement inscrit(e) en " + etudiant.getNiveau_etude() + " pour l'année académique 2024/2025.");
        contentStream.endText();

        // Signature et date
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 450);
        contentStream.showText("Fait à Tétouan, le " + java.time.LocalDate.now());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Mohammed Youssef");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Responsable du Service de Scolarité");
        contentStream.endText();

        // Fermeture du flux de contenu et du document
        contentStream.close();
        document.save(outputStream);
        document.close();

        // Retourne le tableau d'octets contenant les données PDF
        return outputStream.toByteArray();
    }
}
