package com.digitalprimaryschool.digitalprimaryschool.service;

import com.digitalprimaryschool.digitalprimaryschool.model.Eleve;
import com.digitalprimaryschool.digitalprimaryschool.model.Classe;
import com.digitalprimaryschool.digitalprimaryschool.model.Paiement;
import com.digitalprimaryschool.digitalprimaryschool.model.recue_paiement;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class PDFService {

    /**
     * Génère et ouvre le reçu d'inscription au format compact (A6)
     */
    public static void genererRecuInscription(Eleve eleve, Classe classe, Paiement paiement, String dateInscription) {
        String dossierRecus = "recus_pdf";
        File dir = new File(dossierRecus);
        if (!dir.exists()) dir.mkdir();

        String cheminFichier = dossierRecus + "/Recu_" + paiement.getReference() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(cheminFichier);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A6);
            document.setMargins(20, 20, 20, 20);

            document.add(new Paragraph("DIGITAL PRIMARY SCHOOL")
                    .setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("REÇU DE PAIEMENT D'INSCRIPTION\n")
                    .setBold().setFontSize(9).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Référence : " + paiement.getReference()).setFontSize(8));
            document.add(new Paragraph("Date : " + dateInscription).setFontSize(8));
            document.add(new Paragraph("-------------------------------------------------------------------------").setFontSize(8));
            document.add(new Paragraph("Matricule : " + eleve.getMatricule()).setFontSize(8));
            document.add(new Paragraph("Élève : " + eleve.getNom() + " " + eleve.getPrenom()).setFontSize(8));
            document.add(new Paragraph("Classe : " + classe.getNom()).setFontSize(8));
            document.add(new Paragraph("-------------------------------------------------------------------------").setFontSize(8));
            document.add(new Paragraph("Mode de règlement : " + paiement.getModePaiementLibelle()).setFontSize(8));

            document.add(new Paragraph("\nTOTAL PAYÉ : " + paiement.getMontant() + " FCFA")
                    .setBold().setFontSize(10).setTextAlignment(TextAlignment.RIGHT));

            document.close();

            File pdfFile = new File(cheminFichier);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * VRAIE MÉTHODE DEMANDÉE : Génère et imprime la fiche de renseignement au format A4
     */
    public void genererFicheEleve(Eleve eleve, String classeLibelle) {
        String dossier = "fiches_eleves";
        File dir = new File(dossier);
        if (!dir.exists()) dir.mkdir();

        String cheminFichier = dossier + "/Fiche_" + eleve.getMatricule() + ".pdf";
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

        try {
            PdfWriter writer = new PdfWriter(cheminFichier);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(36, 36, 36, 36);

            // Titre Principal
            document.add(new Paragraph("DIGITAL PRIMARY SCHOOL")
                    .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("FICHE DE RENSEIGNEMENT DE L'ÉLÈVE\n\n")
                    .setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER));

            // Section 1 : Éléve
            document.add(new Paragraph("1. IDENTITÉ DE L'ÉLÈVE").setBold().setFontSize(11));
            document.add(new Paragraph("---------------------------------------------------------------------------------------------------------").setFontSize(10));
            document.add(new Paragraph("Matricule : " + eleve.getMatricule()).setFontSize(10));
            document.add(new Paragraph("Nom Complet : " + eleve.getNom() + " " + eleve.getPrenom()).setFontSize(10));
            document.add(new Paragraph("Classe Actuelle : " + classeLibelle).setFontSize(10));
            document.add(new Paragraph("Sexe : " + (eleve.getSexe() != null ? eleve.getSexe().name() : "Non spécifié")).setFontSize(10));

            String dateNaiss = (eleve.getDateNaissance() != null) ? fmt.format(eleve.getDateNaissance()) : "-";
            document.add(new Paragraph("Date et Lieu de Naissance : " + dateNaiss + " à " + eleve.getLieuNaissanceLibelle()).setFontSize(10));
            document.add(new Paragraph("Nationalité : " + eleve.getNationnalite()).setFontSize(10));
            document.add(new Paragraph("Région d'Origine : " + eleve.getRegionOrigineLibelle()).setFontSize(10));
            document.add(new Paragraph("Antécédents Médicaux : " + (eleve.getAntecedentsMedicaux() != null ? eleve.getAntecedentsMedicaux() : "Aucun")).setFontSize(10));
            document.add(new Paragraph("\n"));

            // Section 2 : Responsables
            document.add(new Paragraph("2. COORDONNÉES DES RESPONSABLES").setBold().setFontSize(11));
            document.add(new Paragraph("---------------------------------------------------------------------------------------------------------").setFontSize(10));
            if (eleve.parent != null) {
                document.add(new Paragraph("Nom du Tuteur / Parent : " + eleve.getNomParent()).setFontSize(10));
            } else {
                document.add(new Paragraph("Parent : Aucun parent rattaché").setFontSize(10));
            }
            document.add(new Paragraph("\n\n"));

            // Signature de clôture
            document.add(new Paragraph("Fait pour servir et valoir ce que de droit.\n\n").setFontSize(10));
            document.add(new Paragraph("La Direction").setBold().setFontSize(10).setTextAlignment(TextAlignment.RIGHT));

            document.close();

            // Ouverture instantanée du PDF pour impression directe
            File pdfFile = new File(cheminFichier);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}