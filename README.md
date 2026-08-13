# Internal Recruitment System (IJP — Internal Job Posting)

A microservices-based platform that lets an organization post job openings internally so existing employees get first access before the role is opened externally. Built with Spring Boot, Spring Cloud (Gateway + Eureka), MySQL (database-per-service), and an Angular single-page frontend.

---

## Table of Contents

- [Overview](#overview)
- [Objectives](#objectives)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Database Design](#database-design)
- [Features](#features)
- [API Reference](#api-reference)
- [DTO Design](#dto-design)
- [Project Structure](#project-structure)
- [Setup & Run](#setup--run)
- [Deployment View](#deployment-view)
- [Future Enhancements](#future-enhancements)

---

## Overview

| Field | Detail |
|---|---|
| Project Title | Internal Recruitment System (IJP) |
| Architecture Style | Microservices with API Gateway + Service Registry |
| Primary Users | Candidate (Employee), HR / Admin |
| Client | Angular Single Page Application (SPA) |
| Communication | REST over HTTP, JSON payloads, DTO-based contracts |

The system covers candidate registration and job application, HR (Admin) authentication, job posting management (create, list, update, close), designation master-data management, and candidate visibility for HR — all exposed through a single API Gateway.

## Objectives

- Increase workforce satisfaction and retention by giving existing employees first access to open positions.
- Give HR a single dashboard to publish, edit, and close internal job postings.
- Demonstrate a clean microservices design: independent services, own databases, gateway-routed traffic, and inter-service communication.
- Keep every module traceable to a specific service and layer.

## Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| Frontend | Angular, Bootstrap | Candidate & Admin UI, single-page application |
| API Gateway | Spring Cloud Gateway | Single entry point; routes client requests to the correct microservice |
| Service Registry | Netflix Eureka (Spring Cloud Netflix) | Service discovery — services register themselves and locate each other by name |
| Microservices | Java 8, Spring Boot, Spring MVC | Business logic exposed as independent REST services |
| Inter-service Communication | OpenFeign (declarative REST client) | Job Service calls Candidate Service to fetch applicants for a posting |
| Data Access | Spring Data JPA (Hibernate) | ORM mapping between Java entities and relational tables |
| Database | MySQL (one schema per service) | Persistent storage — Database-per-Service pattern |
| Data Contracts | DTOs (Data Transfer Objects) | Decouple internal entities from API request/response shapes |
| Build Tools | Maven (backend), npm (frontend) | Dependency management and build lifecycle |
| Containerization | Docker | Each microservice packaged as an independent container image |
| Version Control | Git & GitHub | Source control and collaboration |
| Design | UML (Class, Sequence, ER diagrams) | System design documentation |

> Kubernetes and CI/CD pipelines (Jenkins) are part of the broader curriculum this project is drawn from, but are **out of current scope** — see [Future Enhancements](#future-enhancements).

## Architecture

### Style

The system follows a **Microservices Architecture**. Each business capability (Candidate management, Job Posting management, Admin/Authentication) is an independently deployable Spring Boot service with its own database. Clients never call a microservice directly — every request goes through the API Gateway, which uses the Service Registry to locate a healthy instance of the target service.

### High-Level Components

| Component | Responsibility |
|---|---|
| Angular Client | Renders UI, calls the API Gateway over HTTP/JSON |
| API Gateway (Spring Cloud Gateway) | Single entry point; request routing, path-based forwarding to services |
| Eureka Discovery Server | Maintains a live registry of all running service instances |
| Candidate Service | Candidate registration, application to a posting, candidate listing |
| Job Posting Service | Create/list/update/close postings, designation master data |
| Admin Service | HR authentication (login) |
| MySQL | `candidate_db`, `job_db`, `admin_db` — one schema per service |

### Request Flow

```
Angular Client
     │
     ▼
API Gateway ──► Eureka (resolves service location)
     │
     ▼
Target Microservice
     │
     ▼
Service Layer (business rules, using DTOs)
     │
     ▼
Repository (Spring Data JPA)
     │
     ▼
MySQL schema for that service
```

The JSON response travels back along the same path.

### Inter-Service Communication

HR needs to see which candidates applied to a given posting. Since each service owns its own database (no cross-service foreign keys), the **Job Posting Service** calls the **Candidate Service** directly over REST using a declarative **Feign client**, resolved through Eureka. This is the one deliberate synchronous service-to-service call in the system — kept intentionally simple, and the reference example of inter-service communication in this project.

### Database-per-Service

| Service | Schema | Owns |
|---|---|---|
| Candidate Service | `candidate_db` | `candidate` table |
| Job Posting Service | `job_db` | `job_posting` table, `designation` table |
| Admin Service | `admin_db` | `admin` table |

## Database Design

Because each service owns its own schema, there are **no physical foreign keys across services**. Cross-service references (e.g., which posting a candidate applied to) are stored as plain ID values and resolved at the application layer via the Feign call described above.

### `candidate_db.candidate`

| Column | Type | Notes |
|---|---|---|
| id | BIGINT (PK) | Auto-generated |
| first_name | VARCHAR | Required |
| last_name | VARCHAR | Required |
| employee_id | VARCHAR | Required, verified/looked up |
| date_of_birth | DATE | Required |
| email | VARCHAR | Required, unique per job_id |
| job_id | BIGINT | Logical reference to `job_posting.id` in `job_db` (no physical FK) |

### `job_db.job_posting`

| Column | Type | Notes |
|---|---|---|
| id | BIGINT (PK) | Auto-generated |
| job_code | VARCHAR | Auto-populated unique job identifier |
| description | TEXT | Required |
| designation_id | BIGINT (FK → designation.id) | Required |
| location | VARCHAR | Required |
| skill_set | VARCHAR | Comma-separated / multi-select |
| experience_years | INT | Minimum required experience |
| languages_known | VARCHAR | Multi-select |
| salary_min / salary_max | DECIMAL | Salary range |
| status | VARCHAR | OPEN / CLOSED |
| posted_date | DATE | Auto-populated |

### `job_db.designation` (master data)

| Column | Type | Notes |
|---|---|---|
| id | BIGINT (PK) | Auto-generated |
| title | VARCHAR | e.g. Software Developer, Software Architect |

### `admin_db.admin`

| Column | Type | Notes |
|---|---|---|
| id | BIGINT (PK) | Auto-generated |
| email | VARCHAR | Unique, used as login ID |
| password | VARCHAR | Stored encoded (BCrypt) |

### Relationships

- **One Designation → Many Job Postings** (`job_posting.designation_id → designation.id`, same schema, physical FK).
- **One Job Posting → Many Candidates** (`job_posting.id ↔ candidate.job_id`, cross-service, logical reference resolved via Feign).
- **One Admin → Many Job Postings created** (tracked by admin email/id on `job_posting`, cross-service logical reference).

## Features

| # | Feature | Owning Service |
|---|---|---|
| 1 | Candidate Registration & Job Application | Candidate Service |
| 2 | HR (Admin) Login | Admin Service |
| 3 | Add Job Posting | Job Posting Service |
| 4 | Manage Job Designation Master Data | Job Posting Service |
| 5 | List Registered Candidates (per posting) | Job Posting Service → Candidate Service (Feign) |
| 6 | List Active Job Postings | Job Posting Service |
| 7 | Modify / Close Job Posting | Job Posting Service |
| 8 | Search & Filter Job Postings (by designation / location) | Job Posting Service |
| 9 | Application Acknowledgement (in-app notification to HR) | Job Posting Service / Candidate Service |

### 1. Candidate Registration & Job Application

- **Trigger:** Candidate clicks "Apply" on an open job posting on the home page.
- **Pre-condition:** At least one OPEN job posting is displayed.
- **Post-condition:** Candidate record persisted in `candidate_db`, linked to the selected `job_id`.
- **UI Fields:** First Name, Last Name, Employee ID, Date of Birth, Email, Apply, Cancel.
- **Business Rules:** Email + Employee ID format checked; duplicate application (same email for the same `job_id`) is rejected.
- **Exception Flow:** Validation/business-rule failure returns the candidate to the form with an inline error.
- **Alternate Flow:** Cancel returns to the home page without saving.

### 2. HR (Admin) Login

- **Trigger:** User clicks "Admin Login" on the home page.
- **Post-condition:** Admin authenticated and redirected to the dashboard.
- **UI Fields:** Email, Password, Login, Cancel.
- **Business Rules:** Email and password verified against `admin_db` (password compared using an encoded hash).
- **Exception Flow:** Invalid credentials return the user to the login screen with an error.

### 3. Add Job Posting

- **Trigger:** Admin clicks "Add Job" on the dashboard.
- **Pre-condition:** Admin is logged in.
- **Post-condition:** New job posting created in `job_db` with status `OPEN`.
- **UI Fields:** Job Id (auto), Description, Designation (dropdown), Location (dropdown), Required Skill-set (checkboxes), Years of Experience (dropdown), Languages Known (multi-select), Salary Range (min–max), Post, Cancel.
- **Business Rules:** Designation list is populated from the designation master table.

### 4. Manage Job Designation Master Data

Admin can add a general job position/designation name (e.g., Software Developer, Software Architect), which then appears in the Designation dropdown when posting a job. Prevents free-text duplication of role titles across postings.

### 5. List Registered Candidates

Admin views the list of candidates who applied to a specific posting. Implemented via the Job Posting Service calling the Candidate Service (Feign client) with the `job_id`, returning a list of `CandidateSummaryDTO` objects — the concrete example of inter-service communication in this system.

### 6. List Active Job Postings

The home page lists all postings with `status = OPEN`, fetched by the Angular client via the API Gateway. Available to any visitor, no login required.

### 7. Modify / Close Job Posting

Admin can edit posting fields or set `status = CLOSED`. A closed posting no longer appears in the public Active Job Postings list but remains queryable by Admin.

### 8. Search & Filter Job Postings

Home page allows filtering the active postings list by Designation and/or Location — reuses the same list endpoint with query parameters.

### 9. Application Acknowledgement (simplified)

Implemented as an in-application notification flag (an "unseen applications" indicator on the Admin dashboard) rather than an email/SMS integration, which would require infrastructure outside the current stack.

## API Reference

### Gateway Routing Table

| Path Prefix | Routed To | Notes |
|---|---|---|
| `/api/candidates/**` | Candidate Service | Registration, application, candidate listing |
| `/api/jobs/**` | Job Posting Service | Job posting CRUD, designation master data |
| `/api/admin/**` | Admin Service | Login / authentication |

### Candidate Service

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/candidates` | Register a candidate & apply to a job (`CandidateRequestDTO` in) |
| GET | `/api/candidates` | List all candidates |
| GET | `/api/candidates/job/{jobId}` | List candidates who applied to a given job (used by Job Service via Feign) |

### Job Posting Service

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/jobs` | Create a job posting (`JobPostingRequestDTO` in) |
| GET | `/api/jobs` | List job postings (supports `?status=`, `?designation=`, `?location=`) |
| GET | `/api/jobs/{id}` | Get a single posting |
| PUT | `/api/jobs/{id}` | Update a posting |
| PUT | `/api/jobs/{id}/close` | Close a posting (`status → CLOSED`) |
| GET | `/api/jobs/designations` | List designation master data |
| POST | `/api/jobs/designations` | Add a new designation |

### Admin Service

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/admin/login` | Authenticate HR (`AdminLoginRequestDTO` in, `AdminLoginResponseDTO` out) |

## DTO Design

DTOs decouple the JPA entities (internal persistence model) from what is actually sent over the wire, and are used for both client-facing APIs and the internal Feign call between services.

| DTO | Used By | Purpose |
|---|---|---|
| `CandidateRequestDTO` | Candidate Service (POST `/api/candidates`) | Incoming registration/application payload |
| `CandidateResponseDTO` | Candidate Service | Outgoing candidate data (excludes internal fields) |
| `CandidateSummaryDTO` | Candidate Service → Job Posting Service (Feign) | Minimal candidate fields returned for Feature 5 |
| `JobPostingRequestDTO` | Job Posting Service (POST/PUT `/api/jobs`) | Incoming job posting payload |
| `JobPostingResponseDTO` | Job Posting Service | Outgoing job posting data, including designation title |
| `DesignationDTO` | Job Posting Service | Designation master-data payload |
| `AdminLoginRequestDTO` | Admin Service | Incoming login credentials |
| `AdminLoginResponseDTO` | Admin Service | Login result (success flag, basic profile — never the password) |

## Project Structure

```
ijp-system/
├── eureka-server/
│   ├── EurekaServerApplication.java     # @EnableEurekaServer, registry entry point
│   └── application.yml                  # registry port and configuration
│
├── api-gateway/
│   ├── ApiGatewayApplication.java       # @EnableDiscoveryClient, gateway entry point
│   └── application.yml                  # route definitions
│
├── candidate-service/
│   ├── CandidateServiceApplication.java # Spring Boot entry point, @EnableDiscoveryClient
│   ├── entity/Candidate.java            # @Entity mapped to candidate table
│   ├── repository/CandidateRepository.java
│   ├── service/CandidateService.java    # duplicate check, save, list, list-by-job
│   ├── controller/CandidateController.java
│   ├── dto/CandidateRequestDTO.java
│   ├── dto/CandidateResponseDTO.java
│   ├── dto/CandidateSummaryDTO.java
│   ├── mapper/CandidateMapper.java      # Entity ↔ DTO conversion
│   └── application.yml                  # candidate_db connection, Eureka client config
│
├── job-service/
│   ├── JobServiceApplication.java       # @EnableFeignClients, @EnableDiscoveryClient
│   ├── entity/JobPosting.java
│   ├── entity/Designation.java
│   ├── repository/JobPostingRepository.java
│   ├── repository/DesignationRepository.java
│   ├── service/JobPostingService.java   # create, list (with filters), update, close
│   ├── service/DesignationService.java  # add/list designations
│   ├── client/CandidateClient.java      # @FeignClient("candidate-service") — Feature 5
│   ├── controller/JobPostingController.java
│   ├── controller/DesignationController.java
│   ├── dto/JobPostingRequestDTO.java
│   ├── dto/JobPostingResponseDTO.java
│   ├── dto/DesignationDTO.java
│   ├── mapper/JobPostingMapper.java
│   └── application.yml                  # job_db connection, Eureka client config
│
├── admin-service/
│   ├── AdminServiceApplication.java     # Spring Boot entry point, @EnableDiscoveryClient
│   ├── entity/Admin.java
│   ├── repository/AdminRepository.java
│   ├── service/AdminService.java        # login validation logic
│   ├── controller/AdminController.java
│   ├── dto/AdminLoginRequestDTO.java
│   ├── dto/AdminLoginResponseDTO.java
│   └── application.yml                  # admin_db connection, Eureka client config
│
└── ijp-frontend/ (Angular)
    ├── app.module.ts / app-routing.module.ts
    ├── home/home.component.ts               # Lists active postings; search/filter
    ├── register/register.component.ts       # Candidate application form
    ├── admin-login/admin-login.component.ts # HR login form
    ├── admin-dashboard/admin-dashboard.component.ts # Postings list, unseen-application indicator
    ├── add-job/add-job.component.ts         # Create-posting form
    ├── candidate-list/candidate-list.component.ts # Candidates for a posting
    └── services/
        ├── candidate.service.ts   # HttpClient calls to /api/candidates
        ├── job.service.ts         # HttpClient calls to /api/jobs
        └── admin.service.ts       # HttpClient call to /api/admin/login
```

## Setup & Run

> Adjust ports/credentials to match your local environment. Each service reads its own `application.yml`.

### Prerequisites

- Java 8 (or higher, per your build config)
- Maven
- Node.js + npm
- MySQL (running locally or in a container)
- Docker (optional, for containerized run)

### 1. Provision the databases

Create three schemas in MySQL:

```sql
CREATE DATABASE candidate_db;
CREATE DATABASE job_db;
CREATE DATABASE admin_db;
```

### 2. Start the services in order

Order matters because of service discovery — Eureka must be up before dependents register.

1. **Eureka Server** — `cd eureka-server && mvn spring-boot:run` (default port `8761`)
2. **Candidate Service** — `cd candidate-service && mvn spring-boot:run` (default port `8081`)
3. **Job Posting Service** — `cd job-service && mvn spring-boot:run` (default port `8082`)
4. **Admin Service** — `cd admin-service && mvn spring-boot:run` (default port `8083`)
5. **API Gateway** — `cd api-gateway && mvn spring-boot:run` (default port `8080`)

Confirm all four services show up as `UP` on the Eureka dashboard at `http://localhost:8761`.

### 3. Start the Angular client

```bash
cd ijp-frontend
npm install
ng serve
```

Client runs at `http://localhost:4200` and calls the backend through the Gateway at `http://localhost:8080`.

### 4. (Optional) Run with Docker

Build and run each service as its own container image, exposing the ports listed in [Deployment View](#deployment-view). MySQL schemas must be reachable by each container (e.g., via a shared Docker network or external MySQL host).

## Deployment View

| Component | Default Port | Container |
|---|---|---|
| Eureka Server | 8761 | eureka-server image |
| API Gateway | 8080 | api-gateway image |
| Candidate Service | 8081 | candidate-service image |
| Job Posting Service | 8082 | job-service image |
| Admin Service | 8083 | admin-service image |
| Angular Client | 4200 | served via `ng serve` / static build |

## Future Enhancements

These are explicitly **out of current scope** — documented here so the boundary of the current build is clear:

- Container orchestration with Kubernetes for scaling and self-healing.
- CI/CD pipeline (Git + Jenkins) for automated build and deployment.
- Centralized logging and monitoring (e.g., ELK stack, Grafana).
- Email/SMS integration for real application notifications (replacing the current in-app "unseen applications" flag).

---

## Conclusion

This is a microservices-based Internal Recruitment System with clear service boundaries, an API Gateway, service discovery via Eureka, database-per-service isolation, and DTO-based contracts. Every feature, endpoint, and file is scoped to be independently explainable, with future enhancements kept explicitly separate from the current deliverable.
