# Cynbit Backend Technical Audit

**Prepared by:** Vishnu Vardhan  
**Role:** Backend Developer Intern  
**Review date:** 31 October 2026  
**Target:** https://www.cynbit.in/

## Purpose and scope
This repository documents a passive review of publicly accessible Cynbit pages and proposes backend improvements. It does not claim access to source code, private APIs, databases, infrastructure, logs, or administrative systems.

No vulnerability exploitation, authenticated probing, endpoint enumeration, load testing, form submission, or disruptive testing was performed.

## Key public observations
1. The contact page visibly presents Full Name, Email Address, optional Company, optional Industry, Message, and a Send Inquiry action.
2. The privacy policy says inquiry-form and BitElevate application information is used to communicate about requests. It names Resend for email delivery and Prisma for database management.
3. The careers page lists a Backend Developer Intern role.
4. The services page lists technologies including Java, TypeScript, Python, Go, React, Next.js, NestJS, FastAPI, Django, PostgreSQL, MongoDB, Redis, Prisma and Supabase.

These are public statements only. They do not prove the actual API route, backend framework, database schema, security configuration, or deployment topology.

## Repository contents
- `docs/backend-technical-audit.md` — audit narrative and architecture limits.
- `docs/security-findings.md` — prioritized security and privacy findings.
- `docs/performance-analysis.md` — performance risks and measurement plan.
- `docs/recommendations.md` — proposed API contract and remediation plan.
- `docs/evidence-log.md` — sources and evidence boundaries.
- `diagrams/proposed-architecture.md` — proposed Mermaid architecture.
- `code-examples/` — illustrative Java 17 / Spring Boot validation and error-handling API.

## Run the reference API
Requirements: Java 17+ and Maven 3.9+.

```bash
cd code-examples
mvn spring-boot:run
```

Example request:
```json
{
  "fullName": "Alex Rivera",
  "email": "alex@example.com",
  "company": "Example Ltd",
  "industry": "Software",
  "message": "I would like to discuss a project."
}
```

The local example exposes `POST /api/v1/inquiries` and `/actuator/health`. It runs independently and does not connect to Cynbit systems.

## Limitations
No private API contract, TLS configuration, security headers, cookie attributes, rate-limit behaviour, CORS policy, database schema, retention job, authentication flow or performance metrics were independently verified. Browser-visible content cannot prove whether controls are present or absent.

## Public sources reviewed
- https://www.cynbit.in/
- https://www.cynbit.in/careers
- https://www.cynbit.in/contact
- https://www.cynbit.in/privacy
- https://www.cynbit.in/services
