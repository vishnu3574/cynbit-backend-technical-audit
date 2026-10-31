# Performance Analysis & Verification Plan

## 1. Observable limits
The public pages contain marketing content, careers listings and a contact form. No private API, backend latency dashboard, server timing header, database metrics or authorized load test was available. This report does not claim that Cynbit currently has slow APIs or poor uptime.

## 2. Areas worth measuring

| Area | Why it matters | Measurement |
|---|---|---|
| Page delivery | Images, fonts and scripts affect first render. | Lighthouse / Web Vitals on representative public pages. |
| Contact submission | User acknowledgement should not wait on email delivery. | API p50/p95/p99; database commit time; queue delay; provider latency. |
| Database | Staff filters may grow over time. | Query plans, slow-query logs, pool saturation and index usage. |
| Email provider | Provider latency/outage should not block the browser. | Success rate, retry count, queue age and dead-letter count. |
| Traffic spikes | Public forms can receive automated bursts. | 429 rate, rejected requests, CPU, memory, connections and queue depth. |

## 3. Recommended design
1. Return success after durable database persistence, not after waiting synchronously for email.
2. Use a transactional outbox for reliable notification scheduling.
3. Use cursor pagination for staff inquiry lists and cap page size.
4. Add database indexes only after reviewing real query plans.
5. Cache public non-personal static content at the CDN; never publicly cache inquiry records.
6. Set explicit timeouts for database and provider calls.
7. Bound thread pools and connection pools; apply backpressure.
8. Monitor p95 latency, error rate, saturation and queue age.

## 4. Suggested indicators
Agree targets with the service owner after baseline measurement:
- Contact API availability and 5xx rate.
- Contact API p95 response time.
- Accepted inquiries vs notification outcomes.
- Oldest pending outbox event age.
- Database connection pool utilization.
- Rate-limit rejection ratio.
- Privacy deletion request completion time.

Do not publish an SLA until the owner approves it and measurements support it.

## 5. Safe validation plan
With written authorization, use staging, test accounts and synthetic data. Agree request rate, duration and stop conditions in writing. Start with functional tests. Never stress-test production without explicit approval.
