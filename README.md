# Banking Loan Origination System

**Duration:** August 2018 – July 2019

## Description

A loan processing platform that automates the complete loan lifecycle — from customer application and credit verification to underwriting, document validation, approvals, and disbursement. Reduces manual review efforts while improving compliance and audit traceability.

## Technologies

- **Backend:** Java 8, Spring Boot 2.1.7, Spring Security, Hibernate, Oracle DB
- **Messaging:** RabbitMQ (Spring AMQP) with topic exchange
- **Authentication:** JWT (Bearer tokens), Role-Based Access Control
- **Infrastructure:** Docker, Docker Compose

## Architecture

```
Banking-Loan-Origination-System/
├── src/main/java/com/banking/loan/
│   ├── config/                  # Security, JWT, RabbitMQ, CORS, DataInitializer
│   ├── model/                   # JPA Entities (8)
│   ├── repository/              # Spring Data JPA Repositories (9)
│   ├── service/                 # Business Logic (7 services)
│   ├── controller/              # REST Controllers (7)
│   ├── dto/                     # DTOs (8)
│   ├── messaging/               # RabbitMQ Publisher + Consumer + Event
│   └── exception/               # Global Exception Handler
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## Loan Lifecycle Flow

```
Customer Submits → SUBMITTED
       ↓
Credit Check (Underwriter) → CREDIT_CHECK → CREDIT_CHECKED
       ↓
Document Upload (Customer) → DOCUMENT_PENDING
       ↓
Document Verification (Underwriter) → DOCUMENTS_VERIFIED
       ↓
Underwriting Assessment → UNDERWRITING → PENDING_APPROVAL
       ↓
Manager Decision → APPROVED / REJECTED
       ↓
Disbursement (Manager) → DISBURSED → ACTIVE
```

## User Roles & Access

| Role       | Capabilities |
|------------|--------------|
| CUSTOMER   | Submit applications, upload documents, track status |
| UNDERWRITER | Initiate credit checks, verify documents, submit underwriting reports |
| MANAGER    | Process approvals/rejections, initiate disbursements |
| ADMIN      | Full access — all above + user management + audit logs |

## RabbitMQ Events

| Queue | Trigger |
|-------|---------|
| `loan.application.submitted` | Customer submits application |
| `loan.credit.check.completed` | Credit check run |
| `loan.approval.decision` | Approved or rejected |
| `loan.disbursement.completed` | Loan funds transferred |
| `loan.notification` | General status updates |

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register as customer |
| POST | /api/auth/login | Login → returns JWT |
| GET | /api/auth/me | Current user profile |

### Loan Applications (Customer)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/loans | Submit loan application |
| GET | /api/loans/my | My applications |
| GET | /api/loans/{id} | Application details |
| GET | /api/loans/track/{appNumber} | Public tracking |
| GET | /api/loans/{id}/audit | Audit trail |

### Credit Check (Underwriter+)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/credit-check/pending | Applications needing credit check |
| POST | /api/credit-check/initiate/{appId} | Run credit check |
| GET | /api/credit-check/{appId} | Get credit report |

### Documents
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/documents/upload/{appId} | Upload document |
| GET | /api/documents/application/{appId} | List documents |
| PUT | /api/documents/{docId}/verify | Verify/reject document |

### Underwriting (Underwriter+)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/underwriting/queue | Applications ready for underwriting |
| POST | /api/underwriting/assign/{appId} | Assign to underwriting |
| POST | /api/underwriting/report/{appId} | Submit underwriting report |

### Approvals (Manager+)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/approvals/pending | Pending approval queue |
| POST | /api/approvals/{appId} | Approve or reject |

### Disbursements (Manager+)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/disbursements/approved | Approved loans pending disbursal |
| POST | /api/disbursements/{appId} | Disburse loan |
| GET | /api/disbursements/{appId}/emi-schedule | EMI repayment schedule |

## Setup

### Docker
```bash
mvn clean package -DskipTests
docker-compose up --build -d
```

### Local Development
1. Start Oracle XE 21c (port 1521)
2. Start RabbitMQ (port 5672)
3. `mvn spring-boot:run`

### Demo Accounts (seeded on startup)
| Email | Password | Role |
|-------|----------|------|
| customer@bank.com | password123 | CUSTOMER |
| underwriter@bank.com | admin123 | UNDERWRITER |
| manager@bank.com | admin123 | MANAGER |
| admin@bank.com | admin123 | ADMIN |

### Authentication
```
POST /api/auth/login
{ "email": "customer@bank.com", "password": "password123" }

# Use the returned token in all subsequent requests:
Authorization: Bearer <token>
```
