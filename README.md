# SIEM-Lite

SIEM-Lite is a Java-based Security Information and Event Management (SIEM) project built with Spring Boot.

The goal of the project is to simulate the correlation engine found in enterprise SIEM platforms such as Splunk Enterprise Security, Microsoft Sentinel, IBM QRadar, and Elastic Security.

---

## Technologies

* Java 25
* Spring Boot 4.1.0
* Maven
* H2 Database
* REST API
* Postman
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
H2 Database
        ↓
DetectionService
        ↓
Alert
        ↓
AlertRepository
        ↓
H2 Database
        ↓
GET /api/events
GET /api/alerts
```

---

## Supported Event Types

* FAILED_LOGIN
* SUCCESSFUL_LOGIN

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

## Current Features

* Log ingestion
* Event storage
* Alert storage
* Time-window correlation
* Duplicate alert prevention
* Username-aware correlation
* Password spray detection
* Fully documented source code

---

## Future Roadmap

* BRUTE_FORCE_AGGRESSIVE
* PORT_SCAN detection
* PRIVILEGE_ESCALATION
* IMPOSSIBLE_TRAVEL
* DATA_EXFILTRATION
* MITRE ATT&CK mappings
* Alert status workflow
* Web dashboard
* Docker deployment
