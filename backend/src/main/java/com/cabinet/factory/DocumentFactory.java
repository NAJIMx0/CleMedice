package com.cabinet.factory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DocumentFactory {

    public enum DocumentType {
        ORDONNANCE,
        ATTESTATION,
        CERTIFICAT_ARRET_TRAVAIL,
        CERTIFICAT_APTITUDE
    }

    public static Map<String, String> createDocument(
            DocumentType type,
            String patientNom,
            String patientPrenom,
            String contenu) {

        Map<String, String> doc = new HashMap<>();
        doc.put("patientNom",    patientNom);
        doc.put("patientPrenom", patientPrenom);
        doc.put("date",          LocalDate.now().toString());
        doc.put("contenu",       contenu);

        switch (type) {
            case ORDONNANCE -> {
                doc.put("type",  "ORDONNANCE");
                doc.put("titre", "Ordonnance Médicale");
                doc.put("entete","Je soussigné, prescris au patient " +
                                  patientNom + " " + patientPrenom + " :");
            }
            case ATTESTATION -> {
                doc.put("type",  "ATTESTATION");
                doc.put("titre", "Attestation Médicale");
                doc.put("entete","Je soussigné, médecin au Cabinet CleMedice, " +
                                  "certifie que le patient " +
                                  patientNom + " " + patientPrenom);
            }
            case CERTIFICAT_ARRET_TRAVAIL -> {
                doc.put("type",  "ARRET_TRAVAIL");
                doc.put("titre", "Certificat d'Arrêt de Travail");
                doc.put("entete","Le patient " + patientNom + " " + patientPrenom +
                                  " est en arrêt de travail pour :");
            }
            case CERTIFICAT_APTITUDE -> {
                doc.put("type",  "APTITUDE");
                doc.put("titre", "Certificat d'Aptitude");
                doc.put("entete","Je certifie que le patient " +
                                  patientNom + " " + patientPrenom +
                                  " est apte à :");
            }
            default -> throw new IllegalArgumentException("Type de document inconnu: " + type);
        }

        return doc;
    }
}
