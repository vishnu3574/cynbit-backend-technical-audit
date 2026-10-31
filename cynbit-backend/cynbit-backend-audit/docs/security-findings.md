# Security & Privacy Findings

## Rating approach
P1 = high-priority control to verify; P2 = medium-priority defense-in-depth. These are improvement opportunities and verification tasks, not confirmed vulnerabilities.

| ID | Area | Evidence / observation | Risk if control is missing | Priority | Recommendation |
|---|---|---|---|---|---|
| S-01 | Data minimization | Contact form asks for name, email, optional company, optional industry and message; policy says minimum information is collected. | Free text can contain unnecessary sensitive information. | P1 | Keep fields purpose-limited; add privacy notice; warn users not to submit secrets or sensitive IDs; define retention. |
| S-02 | Input validation | Browser-visible page does not establish server-side validation behaviour. | Malformed or oversized input can cause downstream errors or abuse if unconstrained. | P1 verify | Enforce DTO validation, normalization, max lengths, content-type and request-size checks. |
| S-03 | Abuse protection | Public submission action exists; rate limiting and bot controls are not verifiable from page text. | Automated spam, mail-provider cost and operational noise if absent. | P1 verify | Add per-IP and global limits, duplicate suppression and risk-based bot challenge. |
| S-04 | Email reliability | Privacy policy names Resend; queueing, retries and secret handling are not visible. | Provider failures may delay/loss notifications or secrets may be mishandled. | P2 verify | Use server-side secrets, transactional outbox, bounded retries with backoff, dead-letter handling and metrics. |
| S-05 | Staff access | Policy mentions Prisma/database management; staff authorization is not publicly visible. | Inquiry records could be exposed if access controls are weak. | P1 verify | Authenticated staff access, least-privilege RBAC, audit logs and access reviews. |
| S-06 | Error handling | Public API error schema was not established. | Verbose errors may disclose internals; inconsistent errors complicate clients. | P2 verify | Return stable problem-details responses; log stack traces only server-side with trace IDs. |
| S-07 | Browser/transport controls | TLS, HSTS, CSP, CORS, cookie flags and CSRF were not independently validated. | Misconfiguration can weaken browser/API protections. | P2 verify | Verify HTTPS, HSTS, CSP, restrictive CORS, secure cookie attributes and CSRF for cookie-authenticated actions. |
| S-08 | Retention/deletion | Policy describes data rights; actual workflow and retention automation are not publicly verifiable. | Data may be retained longer than necessary if no process exists. | P1 verify | Define retention, deletion verification, completion tracking and backup/provider handling. |

## Remediation detail

### S-01: Minimize data
Make optional fields truly optional in the API. Set a bounded message length (e.g. 2,000 characters), explain the purpose of collection and warn against including passwords, payment data or government IDs. Retention periods must be approved by the data owner.

### S-02: Validate at the trust boundary
Browser validation is only a usability feature. Validate on the server, reject oversized or malformed payloads, normalize email addresses and use parameterized ORM access.

### S-03: Prevent abuse
Use layered controls: configurable token bucket per IP, global emergency limit, duplicate request detection and a challenge only when risk signals justify it. Consider shared networks to avoid false positives.

### S-04: Reliable notification
Persist the inquiry and outbox event in one transaction. A worker sends the email asynchronously. Retry transient failures with exponential backoff and jitter; do not retry permanent validation failures.

### S-05: Protect staff workflows
Use permissions such as `INQUIRY_READ`, `INQUIRY_UPDATE` and `PRIVACY_DELETE`. Enforce authorization on every server request, not only by hiding UI controls.

### S-06: Safe errors
Do not return stack traces, SQL, provider credentials, internal hostnames or raw exception messages. Include a trace ID for support.

### S-07: Browser and transport
Review deployed headers with the engineering owner. CORS is not authentication; do not use wildcard origins with credentials.

### S-08: Privacy lifecycle
Define a data owner, retention clock, deletion request verification process and auditable completion status. Minimize personal data in logs and analytics.
