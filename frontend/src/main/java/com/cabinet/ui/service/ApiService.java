package com.cabinet.ui.service;

import com.cabinet.ui.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ApiService {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private static String token;

    public static void setToken(String t) { token = t; }
    public static String getToken() { return token; }

    private static HttpRequest.Builder authRequest() {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
    }

    // --- AUTH ---
    public static LoginResponse login(String email, String password) throws Exception {
        String json = mapper.writeValueAsString(new LoginRequest(email, password));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) throw new RuntimeException("Login failed: " + response.body());
        return mapper.readValue(response.body(), LoginResponse.class);
    }

    // --- PATIENTS ---
    public static List<PatientDTO> getPatients() throws Exception {
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/patients")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<PatientDTO>>() {});
    }

    public static List<PatientDTO> searchPatients(String keyword) throws Exception {
        String encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        HttpRequest request = authRequest()
                .uri(URI.create(BASE_URL + "/patients/search?keyword=" + encoded))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<PatientDTO>>() {});
    }

    public static PatientDTO createPatient(PatientDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/patients"))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), PatientDTO.class);
    }

    public static PatientDTO updatePatient(Long id, PatientDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/patients/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), PatientDTO.class);
    }

    public static void deletePatient(Long id) throws Exception {
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/patients/" + id))
                .DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // --- RENDEZ-VOUS ---
    public static List<RendezVousDTO> getRendezVous() throws Exception {
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/rendezvous")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<RendezVousDTO>>() {});
    }

    public static RendezVousDTO createRendezVous(RendezVousDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/rendezvous"))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), RendezVousDTO.class);
    }

    public static void updateRendezVousStatut(Long id, String statut) throws Exception {
        HttpRequest request = authRequest()
                .uri(URI.create(BASE_URL + "/rendezvous/" + id + "/statut?statut=" + statut))
                .PUT(HttpRequest.BodyPublishers.noBody()).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // --- CONSULTATIONS ---
    public static ConsultationDTO createConsultation(ConsultationDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/consultations"))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ConsultationDTO.class);
    }

    // --- ORDONNANCES ---
    public static OrdonnanceResultDTO createOrdonnance(OrdonnanceDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/ordonnances"))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) throw new RuntimeException("Erreur serveur: " + response.statusCode());
        return mapper.readValue(response.body(), OrdonnanceResultDTO.class);
    }

    public static byte[] getOrdonnancePdf(Long id) throws Exception {
        HttpRequest request = authRequest()
                .uri(URI.create(BASE_URL + "/ordonnances/" + id + "/pdf"))
                .GET().build();
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() != 200) throw new RuntimeException("Erreur serveur: " + response.statusCode());
        return response.body();
    }

    // --- ATTESTATIONS ---
    public static byte[] generateAttestation(String patientNom, String patientPrenom, String contenu, String date) throws Exception {
        String json = mapper.writeValueAsString(Map.of(
                "patientNom", patientNom,
                "patientPrenom", patientPrenom,
                "contenu", contenu,
                "date", date
        ));
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/attestations/generate"))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() != 200) throw new RuntimeException("Erreur serveur: " + response.statusCode());
        return response.body();
    }

    // --- FINANCE ---
    public static List<PaiementDTO> getPaiements() throws Exception {
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/finance")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<PaiementDTO>>() {});
    }

    public static FinanceSummaryDTO getFinanceSummary(int annee, int mois) throws Exception {
        HttpRequest request = authRequest()
                .uri(URI.create(BASE_URL + "/finance/summary?annee=" + annee + "&mois=" + mois))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), FinanceSummaryDTO.class);
    }

    public static PaiementDTO createPaiement(Long rendezVousId, Double montant, String modePaiement, String notes) throws Exception {
        String json = mapper.writeValueAsString(Map.of(
                "rendezVousId", rendezVousId,
                "montant", montant,
                "modePaiement", modePaiement,
                "date", java.time.LocalDate.now().toString(),
                "statut", "PAYE",
                "notes", notes != null ? notes : ""
        ));
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/finance"))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), PaiementDTO.class);
    }

    public static byte[] exportFinance(String start, String end) throws Exception {
        HttpRequest request = authRequest()
                .uri(URI.create(BASE_URL + "/export/finance?start=" + start + "&end=" + end))
                .GET().build();
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() != 200) throw new RuntimeException("Erreur serveur: " + response.statusCode());
        return response.body();
    }

    // --- USERS ---
    public static List<UserDTO> getUsers() throws Exception {
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/users")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<UserDTO>>() {});
    }

    public static UserDTO createUser(String nom, String email, String role, String password) throws Exception {
        String json = mapper.writeValueAsString(Map.of(
                "nom", nom,
                "email", email,
                "role", role,
                "password", password
        ));
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/users"))
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), UserDTO.class);
    }

    public static UserDTO updateUser(Long id, String nom, String email, String role, boolean enabled) throws Exception {
        String json = mapper.writeValueAsString(Map.of(
                "nom", nom,
                "email", email,
                "role", role,
                "enabled", enabled
        ));
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/users/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), UserDTO.class);
    }

    public static void resetUserPassword(Long id, String newPassword) throws Exception {
        String json = mapper.writeValueAsString(Map.of("newPassword", newPassword));
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/users/" + id + "/reset-password"))
                .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void deleteUser(Long id) throws Exception {
        HttpRequest request = authRequest().uri(URI.create(BASE_URL + "/users/" + id))
                .DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // --- helper classes ---
    private record LoginRequest(String email, String password) {}
    public static class UserDTO {
        private Long id;
        private String nom;
        private String email;
        private String role;
        private boolean enabled;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}
