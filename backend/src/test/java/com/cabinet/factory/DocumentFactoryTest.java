package com.cabinet.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DocumentFactory — Unit Tests")
class DocumentFactoryTest {

    @Test
    @DisplayName("Factory creates ORDONNANCE document with correct keys")
    void testCreateOrdonnance() {
        Map<String, String> doc = DocumentFactory.createDocument(
            DocumentFactory.DocumentType.ORDONNANCE,
            "Akbib", "Saad", "Paracetamol 500mg"
        );

        assertEquals("ORDONNANCE",        doc.get("type"));
        assertEquals("Ordonnance Médicale", doc.get("titre"));
        assertEquals("Akbib",              doc.get("patientNom"));
        assertEquals("Saad",               doc.get("patientPrenom"));
        assertNotNull(doc.get("date"));
        assertTrue(doc.get("entete").contains("Akbib"));
    }

    @Test
    @DisplayName("Factory creates ATTESTATION document with correct keys")
    void testCreateAttestation() {
        Map<String, String> doc = DocumentFactory.createDocument(
            DocumentFactory.DocumentType.ATTESTATION,
            "Bazza", "Houssam", "est apte au travail"
        );

        assertEquals("ATTESTATION",       doc.get("type"));
        assertEquals("Attestation Médicale", doc.get("titre"));
        assertTrue(doc.get("entete").contains("Bazza"));
        assertTrue(doc.get("entete").contains("Houssam"));
    }

    @Test
    @DisplayName("Factory creates CERTIFICAT_ARRET_TRAVAIL with correct type")
    void testCreateArretTravail() {
        Map<String, String> doc = DocumentFactory.createDocument(
            DocumentFactory.DocumentType.CERTIFICAT_ARRET_TRAVAIL,
            "Alami", "Youssef", "grippe saisonnière"
        );

        assertEquals("ARRET_TRAVAIL",                   doc.get("type"));
        assertEquals("Certificat d'Arrêt de Travail", doc.get("titre"));
    }

    @Test
    @DisplayName("Factory creates CERTIFICAT_APTITUDE with correct type")
    void testCreateCertificatAptitude() {
        Map<String, String> doc = DocumentFactory.createDocument(
            DocumentFactory.DocumentType.CERTIFICAT_APTITUDE,
            "Naciri", "Houda", "pratiquer une activité sportive"
        );

        assertEquals("APTITUDE",               doc.get("type"));
        assertEquals("Certificat d'Aptitude", doc.get("titre"));
        assertTrue(doc.get("entete").contains("Naciri"));
    }

    @Test
    @DisplayName("Document always contains date field")
    void testDocumentAlwaysHasDate() {
        for (DocumentFactory.DocumentType type : DocumentFactory.DocumentType.values()) {
            Map<String, String> doc = DocumentFactory.createDocument(
                type, "Test", "Patient", "contenu"
            );
            assertNotNull(doc.get("date"), "Date must not be null for type: " + type);
            assertFalse(doc.get("date").isEmpty(), "Date must not be empty for type: " + type);
        }
    }
}
