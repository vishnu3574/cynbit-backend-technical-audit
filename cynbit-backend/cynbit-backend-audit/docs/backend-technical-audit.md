# Backend Technical Audit Report

## 1. Executive summary
This is a black-box, passive assessment of Cynbit Technologies' public website. The review focused on the contact journey, careers information, privacy statement and technology landscape.

The public contact page exposes a small business inquiry form. The privacy policy explicitly names Resend for email delivery and Prisma for database management. These disclosures are useful architectural clues, but they do not reveal the actual request path, data model, hosting platform or API implementation.

The most relevant improvement opportunities are controls to verify and strengthen: server-side validation, request-size limits, abuse protection, privacy-by-design, reliable asynchronous email delivery, consistent API errors and observability.

No exploitable vulnerability is asserted.

## 2. Scope and method
**In scope:** homepage, careers, contact, privacy and services pages on `www.cynbit.in`.

**Method:** read publicly accessible page content; record visible form fields and published technology statements; compare the contact form with the privacy policy; design a proposed backend workflow.

**Out of scope:** authentication, admin panels, hidden endpoints, API discovery, source maps, credential testing, vulnerability scanning, automated crawling, form submission, payload fuzzing, load testing and personal data access.

## 3. Observable behaviour

### Contact page
The public contact page renders Full Name, Email Address, optional Company, optional Industry and Message fields, with a Send Inquiry action. The form was not submitted and its network request was not inspected.

**Implication:** a robust API should validate all fields server-side, normalize email addresses, cap message length, reject oversized payloads, mitigate automated abuse and avoid exposing internal provider errors.

### Privacy policy
The public privacy policy (last updated May 20, 2026) says the organization collects minimum information, typically name, email and professional context, for inquiry forms. It says inquiry-form and BitElevate application information is used to communicate about requests. It names Resend for email delivery and Prisma for database management, and describes rights to request a copy and deletion.

**Implication:** define the purpose of each field, retention schedule, deletion workflow, restricted staff access and separation between inquiry storage and email delivery. The policy is not independent proof of deployed configuration.

### Careers page
The careers page lists a Backend Developer Intern role and describes the role as focused on the high-performance core of the software architecture. This does not establish internal architecture or candidate-management APIs.

### Services / technology page
The services page lists languages, frameworks and data stores including Java, TypeScript, Python, Go, React, Next.js, NestJS, FastAPI, Django, PostgreSQL, MongoDB, Redis, Prisma and Supabase. A capability list does not prove each technology is used by the public site.

## 4. Architecture analysis

### What can be said
- A browser-visible contact form exists.
- The privacy statement names Resend and Prisma.
- The site publicly describes a broad software and cloud technology capability set.

### What cannot be concluded
- Whether the website is server-rendered, statically generated or hybrid.
- The actual backend framework, API routes, request/response schemas, database schema, hosting topology, queue provider or cache configuration.
- Whether submissions are stored before email delivery or sent directly to an email service.
- Whether authentication, authorization, CSRF, rate limiting, WAF rules or bot challenges are enabled.
- Whether encryption at rest, backups, retention jobs and audit logs are configured correctly.

### Proposed logical flow
1. Browser submits an inquiry to a same-origin API endpoint.
2. API validates and normalizes the request.
3. API applies abuse controls and assigns a correlation ID.
4. Inquiry and an outbox event are persisted transactionally.
5. A worker sends notification email through the configured provider.
6. Delivery status and retry metadata are recorded without logging message contents.
7. Authorized staff access inquiry records through an authenticated internal interface.

This is a recommended design, not a claim about Cynbit's current implementation.

## 5. API design proposal

### `POST /api/v1/inquiries`
Request:
```json
{
  "fullName": "Alex Rivera",
  "email": "alex@example.com",
  "company": "Example Ltd",
  "industry": "Software",
  "message": "I would like to discuss a project."
}
```

Success (`201 Created`):
```json
{
  "id": "inq_01JEXAMPLE",
  "status": "received",
  "message": "Your inquiry has been received."
}
```

Validation failure (`400 Bad Request`):
```json
{
  "title": "Request validation failed",
  "status": 400,
  "detail": "One or more fields are invalid.",
  "fieldErrors": {"email": "Enter a valid email address."},
  "traceId": "c9f0..."
}
```

Rate limit (`429 Too Many Requests`): return a generic message and `Retry-After`. These are proposed schemas, not observed Cynbit endpoints.

## 6. Data model proposal
`inquiry`:
- `id` — UUID / opaque public identifier
- `full_name` — bounded string
- `email_normalized` — bounded, normalized string
- `company` — nullable bounded string
- `industry` — nullable bounded string
- `message` — bounded text
- `status` — `RECEIVED`, `IN_PROGRESS`, `CLOSED`
- `created_at`, `updated_at`, `retention_until`
- `notification_status` — `PENDING`, `SENT`, `FAILED`

Use indexes based on real staff queries. Avoid indexing personal data without a justified use case. Store only the fields required to respond.

## 7. Conclusion
The public contact workflow and privacy disclosure create clear requirements for validation, abuse prevention, privacy controls and reliable notification handling. The public site does not expose enough information to make definitive claims about private API security or backend performance. A deeper review would require owner-authorized access to API implementation, infrastructure configuration and operational metrics.
