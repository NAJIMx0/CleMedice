# CleMedice - Gestion de Cabinet Medical

CleMedice is a full-stack desktop application for managing a medical practice. It provides tools for patient management, appointment scheduling, consultation tracking, prescription generation, medical certificates, and financial reporting. The backend is a Spring Boot REST API secured with JWT, and the frontend is a JavaFX desktop client.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Default Credentials](#default-credentials)
- [Workflow](#workflow)
- [Frontend Views](#frontend-views)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Security & Roles](#security--roles)
- [Data Model](#data-model)
- [Project Structure](#project-structure)
- [Seed Data](#seed-data)
- [Building for Production](#building-for-production)
- [Switching to MySQL](#switching-to-mysql)

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Backend Framework | Spring Boot | 3.2.5 |
| Backend Language | Java | 21 |
| Security | Spring Security + JWT (jjwt) | 0.12.5 |
| ORM | Spring Data JPA / Hibernate | — |
| Database (Development) | H2 (file-based) | — |
| Database (Production) | MySQL (via mysql-connector-j) | — |
| PDF Generation | iText Core | 8.0.4 |
| Excel Export | Apache POI (ooxml) | 5.2.5 |
| Validation | Jakarta Validation (Hibernate Validator) | — |
| Build Tool | Maven | — |
| Frontend Framework | JavaFX | 21.0.2 |
| Frontend Language | Java | 17+ |
| FXML Parsing | javafx-fxml | 21.0.2 |
| HTTP Client | `java.net.http.HttpClient` | Java 11+ |
| JSON Processing | Jackson (databind + jsr310) | 2.17.0 |

---

## Architecture

The application follows a **client-server architecture**:

```
+--------------------+          HTTP/JSON           +-------------------+
|                    |  <========================>  |                   |
|   JavaFX Client    |       JWT Bearer Auth        |  Spring Boot API  |
|   (Desktop App)    |                              |  (REST Backend)   |
|                    |                              |                   |
+--------------------+                              +--------+----------+
                                                             |
                                                     +-------v--------+
                                                     |   Database     |
                                                     |  (H2 / MySQL)  |
                                                     +----------------+
```

The frontend communicates with the backend exclusively through RESTful HTTP endpoints. Authentication is stateless using JSON Web Tokens. No sensitive logic is duplicated on the client.

---

## Prerequisites

- **Java JDK 21+** (required for the backend; Java 17 minimum for the frontend)
- **Maven 3.8+**
- A terminal or IDE (IntelliJ IDEA recommended)

Verify installations:

```bash
java -version
mvn -version
```

---

## Quick Start

### 1. Start the Backend

```bash
cd backend
mvn spring-boot:run
```

The backend starts on **`http://localhost:8080`**. The H2 database console is available at **`http://localhost:8080/h2-console`** (JDBC URL: `jdbc:h2:file:./data/clemedice`, user: `sa`, password: empty).

> **Note:** Only ONE process can hold the H2 file lock at a time. If you see "Database may be already in use", kill stale Java processes first:
> ```bash
> # Windows (PowerShell as Admin)
> Stop-Process -Name "java" -Force
> ```

### 2. Start the Frontend

Open a **second terminal** and run:

```bash
cd frontend
mvn javafx:run
```

The JavaFX application window opens at the login screen.

### 3. Log In

Use the default admin account (see below).

---

## Default Credentials

On the first startup, the backend automatically seeds data (see [Seed Data](#seed-data)):

| Email | Password | Role |
|---|---|---|
| `admin@clemedice.com` | `admin123` | `MEDECIN_PRINCIPAL` |
| `najim@clemedice.com` | `najim123` | `AUTRE_MEDECIN` |
| `adil@clemedice.com` | `adil123` | `FERMLIYAT` |
| `saad@clemedice.com` | `saad123` | `ASSISTANTE` |
| `houssam@clemedice.com` | `houssam123` | `AUTRE_MEDECIN` |

Additional users can be created through the frontend (Users section, visible only to `MEDECIN_PRINCIPAL` role).

---

## Workflow

The application enforces a strict step-by-step workflow:

```
Patient → Rendez-vous → Consultation → Ordonnance → Paiement
```

Each step unlocks the next:
1. **Patients** — Register and manage patients (no prerequisite)
2. **Rendez-vous** — Book appointments for a patient (needs a patient)
3. **Consultation** — Write consultation notes linked to a completed RDV (needs a rdv with statut EFFECTUE)
4. **Ordonnance** — Generate prescriptions from a consultation (needs a consultation)
5. **Paiement** — Record payments for appointments (any RDV)
6. **Attestation** — Generate medical certificates for any patient

Buttons for Consultations and Ordonnances are only enabled when the prerequisite step is completed. The Finance and Users sections are restricted to `MEDECIN_PRINCIPAL` role.

---

## Frontend Views

| View | FXML File | Controller | Description |
|---|---|---|---|
| **Login** | `LoginView.fxml` | `LoginController` | Email/password authentication, retrieves JWT token |
| **Dashboard** | `DashboardView.fxml` | `DashboardController` | Main navigation hub with role-based card visibility |
| **Patients** | `PatientsView.fxml` | `PatientsController` | CRUD table with search, inline dialogs for add/edit |
| **Rendez-vous** | `RendezVousView.fxml` | `RendezVousController` | Appointment management with statut badge column |
| **Consultation** | `ConsultationView.fxml` | `ConsultationController` | Consultation notes linked to a rendez-vous |
| **Ordonnance** | `OrdonnanceView.fxml` | `OrdonnanceController` | Prescription builder with medicament line items |
| **Attestation** | `AttestationView.fxml` | `AttestationController` | Medical certificate generation (PDF) |
| **Finance** | `FinanceView.fxml` | `FinanceController` | Monthly/annual financial summary with Excel export |
| **Users** | `UsersView.fxml` | `UsersController` | User management (MEDECIN_PRINCIPAL only) |

All views use a shared CSS design system (`styles.css`) with classes for top-bar, dash-card, badges, buttons, tables, form fields, and section titles. No inline `-fx-*` styles are used in FXML files.

---

## Configuration

All backend configuration is in `backend/src/main/resources/application.properties`:

```properties
# Server
server.port=8080
spring.application.name=clemedice-backend

# Database (H2 for development)
spring.datasource.url=jdbc:h2:file:./data/clemedice;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JWT
jwt.secret=CleMedice2026SecretKeyForJWTTokenGenerationMustBe256BitsLong!
jwt.expiration=86400000

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

The frontend API base URL is configured in `frontend/src/main/java/com/cabinet/ui/service/ApiService.java` (defaults to `http://localhost:8080/api`).

---

## API Endpoints

### Authentication

| Method | Path | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/login` | Authenticate user, returns JWT | No |

### Patients

| Method | Path | Description | Roles |
|---|---|---|---|
| GET | `/api/patients` | List all patients | All |
| GET | `/api/patients/{id}` | Get patient by ID | All |
| GET | `/api/patients/search?keyword=` | Search by name or CIN | All |
| POST | `/api/patients` | Create a patient | MEDECIN_PRINCIPAL, FERMLIYAT |
| PUT | `/api/patients/{id}` | Update a patient | MEDECIN_PRINCIPAL, FERMLIYAT |
| DELETE | `/api/patients/{id}` | Delete a patient | MEDECIN_PRINCIPAL |

### Rendez-vous

| Method | Path | Description | Roles |
|---|---|---|---|
| GET | `/api/rendezvous` | List all appointments | All |
| GET | `/api/rendezvous/{id}` | Get appointment by ID | All |
| GET | `/api/rendezvous/date?date=` | Get appointments by date | All |
| GET | `/api/rendezvous/period?start=&end=` | Get appointments in date range | All |
| POST | `/api/rendezvous` | Create an appointment | All |
| PUT | `/api/rendezvous/{id}/statut?statut=` | Update appointment status | All |
| DELETE | `/api/rendezvous/{id}` | Delete an appointment | MEDECIN_PRINCIPAL |

### Consultations

| Method | Path | Description | Roles |
|---|---|---|---|
| GET | `/api/consultations` | List all consultations | All |
| GET | `/api/consultations/{id}` | Get consultation by ID | All |
| GET | `/api/consultations/rendezvous/{rdvId}` | Get consultation by rendez-vous | All |
| POST | `/api/consultations` | Create a consultation | All |
| PUT | `/api/consultations/{id}` | Update a consultation | All |

### Ordonnances (Prescriptions)

| Method | Path | Description | Roles |
|---|---|---|---|
| GET | `/api/ordonnances` | List all prescriptions | All |
| GET | `/api/ordonnances/{id}` | Get prescription by ID | All |
| POST | `/api/ordonnances` | Create prescription with medicaments | All |
| DELETE | `/api/ordonnances/{id}` | Delete prescription | MEDECIN_PRINCIPAL |
| GET | `/api/ordonnances/{id}/pdf` | Download prescription as PDF | All |

### Attestations (Certificates)

| Method | Path | Description | Roles |
|---|---|---|---|
| POST | `/api/attestations/generate` | Generate medical certificate PDF | All |

### Finance / Payments

| Method | Path | Description | Roles |
|---|---|---|---|
| GET | `/api/finance` | List all payments | MEDECIN_PRINCIPAL |
| GET | `/api/finance/{id}` | Get payment by ID | MEDECIN_PRINCIPAL |
| POST | `/api/finance` | Create a payment | MEDECIN_PRINCIPAL |
| DELETE | `/api/finance/{id}` | Delete a payment | MEDECIN_PRINCIPAL |
| GET | `/api/finance/summary?annee=&mois=` | Get monthly/yearly summary | MEDECIN_PRINCIPAL |
| GET | `/api/finance/period?start=&end=` | Get payments by date range | MEDECIN_PRINCIPAL |

### Users

| Method | Path | Description | Roles |
|---|---|---|---|
| GET | `/api/users` | List all users | MEDECIN_PRINCIPAL |
| POST | `/api/users` | Create a user | MEDECIN_PRINCIPAL |
| PUT | `/api/users/{id}` | Update a user | MEDECIN_PRINCIPAL |
| PUT | `/api/users/{id}/reset-password` | Reset user password | MEDECIN_PRINCIPAL |
| DELETE | `/api/users/{id}` | Delete a user | MEDECIN_PRINCIPAL |

### Export

| Method | Path | Description | Roles |
|---|---|---|---|
| GET | `/api/export/patients` | Export patients to Excel (.xlsx) | MEDECIN_PRINCIPAL, FERMLIYAT |
| GET | `/api/export/rendezvous?start=&end=` | Export appointments to Excel | MEDECIN_PRINCIPAL, FERMLIYAT |
| GET | `/api/export/finance?start=&end=` | Export finance data to Excel | MEDECIN_PRINCIPAL, FERMLIYAT |

---

## Security & Roles

The application defines four roles with graduated permissions:

| Role | Permissions |
|---|---|
| **MEDECIN_PRINCIPAL** | Full access — all CRUD operations, user management, finance, exports |
| **FERMLIYAT** | Patient and appointment management, exports (no finance or user management) |
| **ASSISTANTE** | Read-only access to patients and appointments, can update appointment status |
| **AUTRE_MEDECIN** | Consultations, prescriptions, and medical certificates |

Authentication flow:
1. Client sends `POST /api/auth/login` with email + password
2. Server validates credentials via `BCryptPasswordEncoder`
3. Server generates a JWT containing the user's ID, email, and role (signed with HMAC-SHA256)
4. Client stores the JWT and sends it as `Authorization: Bearer <token>` on all subsequent requests
5. `JwtAuthenticationFilter` intercepts each request, validates the token, and sets the security context
6. Role-based access is enforced at the endpoint level via `.hasRole()` / `.hasAnyRole()`

---

## Data Model

### Users
| Field | Type | Notes |
|---|---|---|
| id | Long | Auto-generated |
| nom | String | User's display name |
| email | String | Unique, used as login |
| password | String | BCrypt-encoded |
| role | Enum | MEDECIN_PRINCIPAL, FERMLIYAT, ASSISTANTE, AUTRE_MEDECIN |
| enabled | Boolean | Account active/inactive |

### Patients
| Field | Type | Notes |
|---|---|---|
| id | Long | Auto-generated |
| nom | String | Last name |
| prenom | String | First name |
| cin | String | Unique national ID |
| telephone | String | Phone number |
| dateNaissance | LocalDate | Date of birth |
| adresse | String | Address |
| createdAt | LocalDateTime | Auto-set on creation |

### Rendez-vous
| Field | Type | Notes |
|---|---|---|
| id | Long | Auto-generated |
| patient | ManyToOne | Link to Patient |
| medecin | ManyToOne | Link to User |
| date | LocalDate | Appointment date |
| heure | LocalTime | Appointment time |
| topic | String | Reason for visit |
| statut | Enum | PLANIFIE, EFFECTUE, ANNULE |

### Consultation Details
| Field | Type | Notes |
|---|---|---|
| id | Long | Auto-generated |
| rendezVous | OneToOne | Linked appointment |
| description | Text | Consultation notes |
| observations | Text | Doctor's observations |
| casPatient | String | Patient case summary |
| date | LocalDate | Consultation date |

### Ordonnance / Medicament
| Field | Type | Notes |
|---|---|---|
| id (Ordonnance) | Long | Auto-generated |
| consultation | ManyToOne | Linked consultation |
| casPatient | String | Patient case |
| date | LocalDate | Prescription date |
| medicaments | OneToMany | List of prescribed medications |

Each **Medicament** has: nom, dosage, duree, instructions.

### Paiement
| Field | Type | Notes |
|---|---|---|
| id | Long | Auto-generated |
| rendezVous | OneToOne | Linked appointment |
| montant | BigDecimal | Amount |
| date | LocalDate | Payment date |
| modePaiement | Enum | ESPECES, CHEQUE, VIREMENT, CARTE_BANCAIRE |
| statut | String | Payment status |
| notes | String | Optional notes |

---

## Project Structure

```
CleMedice/
├── backend/                          # Spring Boot REST API
│   ├── pom.xml
│   └── src/main/java/com/cabinet/
│       ├── CleMediceApplication.java # Entry point
│       ├── config/
│       │   ├── SecurityConfig.java           # Spring Security + CORS + role rules
│       │   ├── JwtTokenProvider.java         # JWT generation & validation
│       │   ├── JwtAuthenticationFilter.java  # Request filter for JWT
│       │   ├── GlobalExceptionHandler.java   # @RestControllerAdvice
│       │   └── DataInitializer.java          # Seeds admin + test data on fresh DB
│       ├── controller/
│       │   ├── AuthController.java
│       │   ├── PatientController.java
│       │   ├── RendezVousController.java
│       │   ├── ConsultationController.java
│       │   ├── OrdonnanceController.java
│       │   ├── AttestationController.java
│       │   ├── PaiementController.java
│       │   ├── UserController.java
│       │   └── ExportController.java
│       ├── service/
│       │   ├── AuthService.java
│       │   ├── PatientService.java
│       │   ├── RendezVousService.java
│       │   ├── ConsultationService.java
│       │   ├── OrdonnanceService.java
│       │   ├── PaiementService.java
│       │   └── UserService.java
│       ├── repository/               # JPA repositories
│       ├── model/                    # Entities + enums
│       ├── dto/                      # Request/response DTOs
│       └── util/
│           ├── PdfGenerator.java     # iText-based PDF generation
│           └── ExcelExporter.java    # Apache POI Excel export
│
├── frontend/                         # JavaFX Desktop Client
│   ├── pom.xml
│   └── src/main/java/com/cabinet/ui/
│       ├── MainApp.java             # JavaFX entry point (global CSS, view navigation)
│       ├── controller/
│       │   ├── LoginController.java
│       │   ├── DashboardController.java
│       │   ├── PatientsController.java
│       │   ├── RendezVousController.java
│       │   ├── ConsultationController.java
│       │   ├── OrdonnanceController.java
│       │   ├── AttestationController.java
│       │   ├── FinanceController.java
│       │   └── UsersController.java
│       ├── service/
│       │   └── ApiService.java      # HTTP client + token management
│       └── model/
│           ├── LoginResponse.java
│           ├── PatientDTO.java
│           ├── RendezVousDTO.java
│           ├── ConsultationDTO.java
│           ├── MedicamentDTO.java
│           ├── OrdonnanceDTO.java
│           ├── OrdonnanceResultDTO.java
│           ├── PaiementDTO.java
│           └── FinanceSummaryDTO.java
│
├── data/                            # H2 database file (dev, gitignored)
├── TECHNICAL_CONCEPTION.md          # Architecture document
└── README.md
```

---

## Seed Data

On first startup, `DataInitializer.java` automatically seeds the following data (idempotent, runs only when tables are empty):

- **5 users**: 1 MEDECIN_PRINCIPAL, 2 FERMLIYAT, 1 ASSISTANTE, 1 AUTRE_MEDECIN
- **20 patients** with realistic Moroccan names, CINs, phone numbers, dates of birth, and addresses
- **10 rendez-vous** spread across past and future dates, various statuses (PLANIFIE, EFFECTUE, ANNULE)
- **4 paiements** linked to EFFECTUE rendez-vous

---

## Building for Production

### Backend

```bash
cd backend
mvn clean package -DskipTests
java -jar target/clemedice-backend-1.0.0.jar
```

### Frontend

```bash
cd frontend
mvn clean package
```

The built JAR with dependencies will be in `frontend/target/`. Run it with:

```bash
java -jar target/clemedice-frontend-1.0.0.jar
```

---

## Switching to MySQL

Edit `backend/src/main/resources/application.properties`:

```properties
# Comment out H2
# spring.datasource.url=jdbc:h2:file:./data/clemedice;DB_CLOSE_ON_EXIT=FALSE
# spring.datasource.driver-class-name=org.h2.Driver
# spring.h2.console.enabled=true

# Uncomment MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/clemedice?useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=yourpassword
```

Create the database manually:

```sql
CREATE DATABASE clemedice;
```

Spring Boot will create the tables automatically (`ddl-auto=update`).
