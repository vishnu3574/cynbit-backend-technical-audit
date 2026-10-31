# Recommended Backend Improvements

## 1. Prioritized plan

### Phase 1 — Secure the public input boundary
- Define a versioned API contract.
- Validate every field server-side.
- Apply request-size limits, rate limiting and duplicate suppression.
- Return stable, non-sensitive errors.
- Add trace IDs and structured logs.

### Phase 2 — Reliable processing
- Persist inquiries before acknowledging success.
- Use a transactional outbox and asynchronous notification worker.
- Retry transient email failures with exponential backoff and jitter.
- Add dead-letter handling and a replay procedure.

### Phase 3 — Privacy and operations
- Define retention and deletion workflows.
- Add staff RBAC and audit events.
- Monitor latency, errors, queue age and provider outcomes.
- Add automated tests and dependency/security scanning to CI.

## 2. Proposed API contract
`POST /api/v1/inquiries`
- Public endpoint; abuse controls required.
- `application/json`; suggested request limit 16 KB (confirm with product requirements).
- Optional `Idempotency-Key` stored with a short-lived request fingerprint.
- `201` with opaque inquiry ID and generic acknowledgement.
- `400` for validation, `429` with `Retry-After`, `500` generic message plus trace ID.

`GET /api/v1/inquiries` (internal only)
- Authentication and `INQUIRY_READ` permission.
- Cursor pagination; maximum page size 100.
- Allowlisted filters/sorts; audit access to sensitive records.

`PATCH /api/v1/inquiries/{id}/status` (internal only)
- Require `INQUIRY_UPDATE`.
- Validate state transitions and record actor, old state, new state and timestamp.

## 3. Data constraints
Suggested limits: name 120 characters, email 254, company 120, industry 80, message 2,000. Make optional fields nullable. Add status constraints and timestamps. Never store secrets in inquiry records. Avoid logging message bodies.

## 4. Security checklist
- [ ] HTTPS-only; redirect HTTP to HTTPS.
- [ ] HSTS after confirming subdomain readiness.
- [ ] Restrictive CSP and security headers.
- [ ] Strict CORS allowlist.
- [ ] Server-side validation and request-size limit.
- [ ] Rate limiting / bot mitigation.
- [ ] Parameterized ORM queries.
- [ ] Secrets in a deployment secret manager.
- [ ] Staff authentication and least-privilege RBAC.
- [ ] CSRF protection for cookie-authenticated state changes.
- [ ] Safe errors and trace IDs.
- [ ] PII minimization in logs and analytics.
- [ ] Retention, access, correction and deletion workflow.
- [ ] Dependency updates, SAST and secret scanning in CI.
- [ ] Backup and restore drills.

## 5. Testing strategy
- Unit: DTO constraints and normalization.
- MVC: valid request, invalid email, missing required fields, oversized text, malformed JSON.
- Integration: database transaction and outbox persistence.
- Security: unauthenticated internal route denied, role permissions, CORS policy.
- Resilience in staging: provider timeout, transient failure, duplicate delivery, retry exhaustion.
- Privacy: retention expiry and deletion workflow.

## 6. Success criteria
- Invalid requests are rejected before persistence.
- Public acknowledgement does not depend on email-provider availability.
- Each accepted inquiry has a durable record and traceable notification state.
- Sensitive values do not appear in application logs.
- Staff access is authenticated, authorized and audited.
- Deletion requests can be tracked to completion.
