package com.cabinet.config;

import com.cabinet.model.*;
import com.cabinet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final RendezVousRepository rendezVousRepository;
    private final PaiementRepository paiementRepository;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@clemedice.com")) {
            User admin = User.builder()
                    .nom("Administrateur")
                    .email("admin@clemedice.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.MEDECIN_PRINCIPAL)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            System.out.println("Default admin created: admin@clemedice.com / admin123");
        }

        int patientCount = 0;
        int userCount = 0;
        int rdvCount = 0;
        int paiementCount = 0;

        // --- PATIENTS ---
        if (patientRepository.count() == 0) {
            try {
                List<Patient> patients = List.of(
                        Patient.builder().nom("Badr Eddin").prenom("Najim").cin("BK247891").telephone("0661234567").dateNaissance(LocalDate.of(1995, 3, 14)).adresse("Rue Ibn Battouta 12, Tanger").build(),
                        Patient.builder().nom("Nouri").prenom("Adil").cin("BK198234").telephone("0662345678").dateNaissance(LocalDate.of(1992, 7, 22)).adresse("Avenue Mohammed V 45, Tanger").build(),
                        Patient.builder().nom("Mohsin").prenom("Amraji").cin("BK301456").telephone("0663456789").dateNaissance(LocalDate.of(1988, 11, 5)).adresse("Quartier Beni Makada 8, Tanger").build(),
                        Patient.builder().nom("Akbib").prenom("Saad").cin("BK412678").telephone("0664567890").dateNaissance(LocalDate.of(1990, 6, 18)).adresse("Rue de Fès 23, Tanger").build(),
                        Patient.builder().nom("Bazza").prenom("Houssam").cin("BK523890").telephone("0665678901").dateNaissance(LocalDate.of(1997, 9, 30)).adresse("Avenue Youssef Ibn Tachfine 67, Tanger").build(),
                        Patient.builder().nom("Alami").prenom("Youssef").cin("BK634012").telephone("0666789012").dateNaissance(LocalDate.of(1985, 1, 12)).adresse("Rue Moulay Ismail 34, Tétouan").build(),
                        Patient.builder().nom("Bensouda").prenom("Fatima").cin("BK745234").telephone("0667890123").dateNaissance(LocalDate.of(1993, 4, 25)).adresse("Quartier Al Majd 15, Tanger").build(),
                        Patient.builder().nom("Chraibi").prenom("Hamza").cin("BK856456").telephone("0668901234").dateNaissance(LocalDate.of(1987, 8, 7)).adresse("Rue Sebta 89, Tanger").build(),
                        Patient.builder().nom("Dahbi").prenom("Meriem").cin("BK967678").telephone("0669012345").dateNaissance(LocalDate.of(1996, 12, 19)).adresse("Avenue Hassan II 56, Martil").build(),
                        Patient.builder().nom("El Fassi").prenom("Karim").cin("BK078900").telephone("0660123456").dateNaissance(LocalDate.of(1991, 5, 3)).adresse("Rue Abdellah Guenoun 7, Tanger").build(),
                        Patient.builder().nom("Filali").prenom("Nadia").cin("BK189122").telephone("0661234568").dateNaissance(LocalDate.of(1989, 2, 28)).adresse("Résidence Al Amal 3, Tanger").build(),
                        Patient.builder().nom("Ghali").prenom("Omar").cin("BK290344").telephone("0662345679").dateNaissance(LocalDate.of(1994, 10, 16)).adresse("Rue Ibn Khaldoun 44, Fnideq").build(),
                        Patient.builder().nom("Hajji").prenom("Salma").cin("BK301566").telephone("0663456780").dateNaissance(LocalDate.of(1998, 7, 8)).adresse("Avenue des FAR 78, Tanger").build(),
                        Patient.builder().nom("Idrissi").prenom("Anas").cin("BK412788").telephone("0664567891").dateNaissance(LocalDate.of(1986, 3, 21)).adresse("Quartier California 22, Tanger").build(),
                        Patient.builder().nom("Jalil").prenom("Zineb").cin("BK523910").telephone("0665678902").dateNaissance(LocalDate.of(2000, 1, 14)).adresse("Rue Tariq Ibn Ziad 91, Tanger").build(),
                        Patient.builder().nom("Kettani").prenom("Rachid").cin("BK634132").telephone("0666789013").dateNaissance(LocalDate.of(1983, 9, 9)).adresse("Avenue Sidi Mohammed Ben Abdellah 33, Tanger").build(),
                        Patient.builder().nom("Lahlou").prenom("Hajar").cin("BK745354").telephone("0667890124").dateNaissance(LocalDate.of(1999, 6, 27)).adresse("Rue Al Wahda 14, Assilah").build(),
                        Patient.builder().nom("Moussaoui").prenom("Tariq").cin("BK856576").telephone("0668901235").dateNaissance(LocalDate.of(1981, 11, 11)).adresse("Quartier Mesnana 66, Tanger").build(),
                        Patient.builder().nom("Naciri").prenom("Houda").cin("BK967798").telephone("0669012346").dateNaissance(LocalDate.of(2001, 4, 4)).adresse("Rue Oued Makhazine 29, Tanger").build(),
                        Patient.builder().nom("Ouali").prenom("Khalid").cin("BK078920").telephone("0660123457").dateNaissance(LocalDate.of(1977, 8, 15)).adresse("Avenue Moulay Rachid 82, Tanger").build()
                );
                patientRepository.saveAll(patients);
                patientCount = patients.size();
                System.out.println("Seeded " + patientCount + " patients");
            } catch (Exception e) {
                System.out.println("Failed to seed patients: " + e.getMessage());
            }
        }

        // --- EXTRA USERS ---
        if (userRepository.count() <= 1) {
            try {
                List<User> extraUsers = List.of(
                        User.builder().nom("Dr. Najim Badr Eddin").email("najim@clemedice.com").password(passwordEncoder.encode("najim123")).role(Role.AUTRE_MEDECIN).enabled(true).build(),
                        User.builder().nom("Adil Nouri").email("adil@clemedice.com").password(passwordEncoder.encode("adil123")).role(Role.FERMLIYAT).enabled(true).build(),
                        User.builder().nom("Saad Akbib").email("saad@clemedice.com").password(passwordEncoder.encode("saad123")).role(Role.ASSISTANTE).enabled(true).build(),
                        User.builder().nom("Houssam Bazza").email("houssam@clemedice.com").password(passwordEncoder.encode("houssam123")).role(Role.AUTRE_MEDECIN).enabled(true).build()
                );
                userRepository.saveAll(extraUsers);
                userCount = extraUsers.size();
                System.out.println("Seeded " + userCount + " extra users");
            } catch (Exception e) {
                System.out.println("Failed to seed users: " + e.getMessage());
            }
        }

        // --- RENDEZ-VOUS ---
        Optional<User> adminOpt = userRepository.findByEmail("admin@clemedice.com");
        if (rendezVousRepository.count() == 0 && adminOpt.isPresent()) {
            try {
                User medecin = adminOpt.get();
                LocalDate today = LocalDate.now();

                List<RendezVous> rdvs = List.of(
                        buildRdv("BK247891", medecin, today.plusDays(1), LocalTime.of(9, 0), "Contrôle général", StatutRDV.PLANIFIE),
                        buildRdv("BK198234", medecin, today.plusDays(1), LocalTime.of(10, 30), "Douleurs lombaires", StatutRDV.PLANIFIE),
                        buildRdv("BK301456", medecin, today, LocalTime.of(8, 0), "Suivi tension artérielle", StatutRDV.EFFECTUE),
                        buildRdv("BK412678", medecin, today, LocalTime.of(14, 0), "Consultation diabète", StatutRDV.PLANIFIE),
                        buildRdv("BK523890", medecin, today, LocalTime.of(15, 30), "Fièvre et toux", StatutRDV.EFFECTUE),
                        buildRdv("BK634012", medecin, today.minusDays(1), LocalTime.of(9, 0), "Renouvellement ordonnance", StatutRDV.EFFECTUE),
                        buildRdv("BK745234", medecin, today.minusDays(1), LocalTime.of(11, 0), "Bilan sanguin", StatutRDV.EFFECTUE),
                        buildRdv("BK856456", medecin, today.plusDays(2), LocalTime.of(10, 0), "Douleurs abdominales", StatutRDV.PLANIFIE),
                        buildRdv("BK967678", medecin, today.plusDays(2), LocalTime.of(16, 0), "Consultation prénatale", StatutRDV.PLANIFIE),
                        buildRdv("BK078900", medecin, today.minusDays(2), LocalTime.of(8, 30), "Contrôle post-opératoire", StatutRDV.ANNULE)
                ).stream().filter(Objects::nonNull).toList();

                if (!rdvs.isEmpty()) {
                    rendezVousRepository.saveAll(rdvs);
                    rdvCount = rdvs.size();
                    System.out.println("Seeded " + rdvCount + " rendez-vous");
                }
            } catch (Exception e) {
                System.out.println("Failed to seed rendez-vous: " + e.getMessage());
            }
        }

        // --- PAIEMENTS ---
        if (paiementRepository.count() == 0) {
            try {
                List<RendezVous> effectues = rendezVousRepository.findAll().stream()
                        .filter(r -> r.getStatut() == StatutRDV.EFFECTUE)
                        .toList();

                for (RendezVous rdv : effectues) {
                    String cin = rdv.getPatient().getCin();
                    Double montant = null;
                    ModePaiement mode = null;
                    String notes = null;

                    if ("BK301456".equals(cin)) {
                        montant = 200.0;
                        mode = ModePaiement.ESPECES;
                        notes = "Consultation suivi";
                    } else if ("BK523890".equals(cin)) {
                        montant = 150.0;
                        mode = ModePaiement.CARTE_BANCAIRE;
                        notes = "Consultation urgence";
                    } else if ("BK634012".equals(cin)) {
                        montant = 100.0;
                        mode = ModePaiement.ESPECES;
                        notes = "Renouvellement simple";
                    } else if ("BK745234".equals(cin)) {
                        montant = 250.0;
                        mode = ModePaiement.CHEQUE;
                        notes = "Bilan complet";
                    }

                    if (montant != null) {
                        try {
                            Paiement paiement = Paiement.builder()
                                    .rendezVous(rdv)
                                    .montant(montant)
                                    .date(rdv.getDate())
                                    .modePaiement(mode)
                                    .statut("PAYE")
                                    .notes(notes)
                                    .build();
                            paiementRepository.save(paiement);
                            paiementCount++;
                        } catch (Exception e) {
                            System.out.println("Failed to create payment for RDV " + rdv.getId() + ": " + e.getMessage());
                        }
                    }
                }

                if (paiementCount > 0) {
                    System.out.println("Seeded " + paiementCount + " paiements");
                }
            } catch (Exception e) {
                System.out.println("Failed to seed paiements: " + e.getMessage());
            }
        }

        // --- SUMMARY ---
        int totalPatients = (int) patientRepository.count();
        int totalUsers = (int) userRepository.count();
        int totalRdvs = (int) rendezVousRepository.count();
        int totalPaiements = (int) paiementRepository.count();
        System.out.println("=== Seed complete: " + totalPatients + " patients, " + totalUsers + " users, " + totalRdvs + " rdv, " + totalPaiements + " paiements ===");
    }

    private RendezVous buildRdv(String patientCin, User medecin, LocalDate date, LocalTime heure, String topic, StatutRDV statut) {
        try {
            Optional<Patient> patientOpt = patientRepository.findByCin(patientCin);
            if (patientOpt.isEmpty()) {
                System.out.println("Patient with CIN " + patientCin + " not found, skipping RDV");
                return null;
            }
            return RendezVous.builder()
                    .patient(patientOpt.get())
                    .medecin(medecin)
                    .date(date)
                    .heure(heure)
                    .topic(topic)
                    .statut(statut)
                    .build();
        } catch (Exception e) {
            System.out.println("Error building RDV for CIN " + patientCin + ": " + e.getMessage());
            return null;
        }
    }
}
