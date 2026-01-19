# Order Processing System  
**Enterprise-grade Order Management Backend (Spring Boot, Java)**

A production-ready backend service for managing e-commerce orders with strong domain modeling, SOLID design, and enterprise-grade error handling.  

This service supports:

- Creating orders with multiple items  
- Retrieving and listing orders (optionally filtered by status)  
- Controlled status transitions (`PENDING → PROCESSING → SHIPPED → DELIVERED`)  
- Order cancellation with business-rule enforcement  
- Background scheduler to auto-promote `PENDING` orders  
- Domain-specific error codes (`uiCode`) for UI and BFF layers  
- Trace-ID propagation for observability and production debugging  
- Immutable DTOs and snapshot-based product modeling  

The codebase follows modern backend engineering practices used in large-scale systems:  
layered architecture, domain-driven boundaries, SOLID principles, and defensive validation.

---

## 📁 Repository Layout

order-processing/
├─ ops/ # Spring Boot application (Maven module)
│ ├─ src/main/java/... # Controllers, services, domain, persistence
│ └─ pom.xml
├─ docs/ # Architecture & API documentation
├─ .github/ # CI workflows, PR & issue templates
├─ mvnw / mvnw.cmd # Maven Wrapper
└─ README.md


Key modules inside `ops/`:

- `controller` – HTTP API layer  
- `service` – Application orchestration & business rules  
- `entity` – Domain model (Order, OrderItem, ProductSnapshot)  
- `repository` – Persistence adapters  
- `exception` – Enterprise-grade error handling & domain exceptions  
- `scheduler` – Background job for order promotion  

---

## 🚀 Quick Start

### Prerequisites

- Java **21** (recommended for Spring Boot 3.x in production)  
- Git  
- No local Maven required (Maven Wrapper included)

> Note: Java 24 may work locally, but Java 21 is the current LTS and recommended for production systems.

---

### Build & Test

```bash
./mvnw -f ops/pom.xml clean test

### Run Locally

./mvnw -f ops/pom.xml spring-boot:run

### Service starts on:
http://localhost:8080

### H2 Console (for local debugging):
http://localhost:8080/h2-console

### 🧩 API Capabilities

| Operation           | Endpoint                            |
| ------------------- | ----------------------------------- |
| Create order        | `POST   /api/orders`                |
| Get order           | `GET    /api/orders/{id}`           |
| List orders         | `GET    /api/orders?status=PENDING` |
| Update order status | `PATCH /api/orders/{id}/status`     |
| Cancel order        | `POST   /api/orders/{id}/cancel`    |
