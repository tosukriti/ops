# Order Processing System (Spring Boot) - Using AI

A production-style backend for managing e-commerce orders: create, retrieve, list, update status, cancel, and auto-promote PENDING orders to PROCESSING via a scheduler.

## Project layout

- `ops/` — Spring Boot application (Maven module)
- `.github/` — CI workflows, issue templates, PR template
- `docs/` — Architecture & API examples

## Quick start

### Prerequisites
- Java **24**

### Build & test
```bash
./mvnw -f ops/pom.xml clean test
