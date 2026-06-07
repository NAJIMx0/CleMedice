package com.cabinet.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Singleton Pattern — Demonstration Test")
class SingletonPatternTest {

    @Test
    @DisplayName("DocumentFactory always returns same type map keys")
    void testFactoryOutputIsConsistent() {
        Map<String, String> doc1 = DocumentFactory.createDocument(
            DocumentFactory.DocumentType.ORDONNANCE, "A", "B", "c"
        );
        Map<String, String> doc2 = DocumentFactory.createDocument(
            DocumentFactory.DocumentType.ORDONNANCE, "A", "B", "c"
        );
        assertEquals(doc1.get("type"),  doc2.get("type"));
        assertEquals(doc1.get("titre"), doc2.get("titre"));
    }

    @Test
    @DisplayName("All four document types are creatable without exception")
    void testAllTypesCreatable() {
        assertDoesNotThrow(() -> {
            for (DocumentFactory.DocumentType t : DocumentFactory.DocumentType.values()) {
                DocumentFactory.createDocument(t, "Nom", "Prenom", "contenu");
            }
        });
    }
}
