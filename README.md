# SIEM-Lite

SIEM-Lite is a Java-based Security Information and Event Management (SIEM) project built with Spring Boot.

The goal of the project is to simulate the correlation engine found in enterprise SIEM platforms such as Splunk Enterprise Security, Microsoft Sentinel, IBM QRadar, and Elastic Security.

---

## Technologies

* Java 21
* Spring Boot 4.1.0
* Spring Security 7
* JWT Authentication
* Spring Data JPA
* H2 Database
* Swagger / OpenAPI
* Docker
* Maven Wrapper
* Git

---

## Architecture

```text
POST /api/logs
        ↓
LogController
        ↓
LogParserService
        ↓
SecurityEvent
        ↓
SecurityEventRepository
        ↓
DetectionService
        ↓
Alert
        ↓
AlertRepository
        ↓
Audit Logging
        ↓
H2 Database

GET /api/events
GET /api/alerts
GET /api/audit
```

---

## Authentication

JWT-based authentication with Role-Based Access Control (RBAC).

Demo users are automatically seeded at application startup.

| Username | Password    | Role    |
| -------- | ----------- | ------- |
| admin    | Password123 | ADMIN   |
| analyst  | Password123 | ANALYST |
| viewer   | Password123 | VIEWER  |

---

## Supported Event Types

* FAILED_LOGIN
* SUCCESSFUL_LOGIN
* PRIVILEGE_ESCALATION
* CONNECTION_ATTEMPT

---

## Detection Rules

### BRUTE_FORCE_ATTEMPT

Rule:

```text
5 FAILED_LOGIN
same IP
within 5 minutes
↓
HIGH alert
```

---

### ACCOUNT_COMPROMISE

Rule:

```text
5 FAILED_LOGIN
same IP
same username
within 5 minutes
+
1 SUCCESSFUL_LOGIN
same IP
same username
↓
CRITICAL alert
```

---

### PASSWORD_SPRAY

Rule:

```text
1 source IP
5 different usernames
within 5 minutes
↓
HIGH alert
```

---

## Alert Features

Every generated alert includes:

* Severity
* Risk Score
* MITRE ATT&CK Technique
* Description
* Created Timestamp

---

## Analyst Workflow

Authenticated analysts can:

* Assign alerts
* Update alert status
* Add investigation notes

Supported alert status values:

* OPEN
* INVESTIGATING
* CLOSED

---

## Audit Logging

The application records important security actions including:

* User Registration
* User Login
* Alert Assignment
* Alert Status Changes
* Alert Notes Updates

Audit logs are available through:

```text
GET /api/audit
```

(Admin only)

---

## Current Features

* JWT Authentication
* Role-Based Access Control (RBAC)
* Demo user seeding
* Log ingestion
* Log parsing
* Event storage
* Detection engine
* Alert generation
* Time-window correlation
* Duplicate alert prevention
* Username-aware correlation
* Password spray detection
* MITRE ATT&CK mapping
* Risk scoring
* Analyst workflow
* Audit logging
* Swagger / OpenAPI documentation
* Docker support

---

## API Testing

The backend has been fully tested using Swagger.

Verified functionality includes:

* User Registration
* User Login
* JWT Authentication
* Log Ingestion
* Event Creation
* Alert Generation
* Alert Assignment
* Alert Status Updates
* Alert Notes
* Audit Logging

---

## Current Project Status

**Backend Status:** ✅ Complete

The backend is feature complete for the scope of the project.

Current development is focused on:

* React frontend
* PostgreSQL migration
* Architecture diagram
* Application screenshots
* Final portfolio polish


