# AI Job Agent

A production-oriented Java/Spring Boot foundation for a personal AI job-search assistant.

## What it does

1. Runs every day at 11:00 AM Asia/Kolkata by default.
2. Pulls jobs from pluggable public/authorized job sources (Greenhouse and SmartRecruiters are included).
3. Normalizes and deduplicates jobs using source + external ID.
4. Filters by target role/location.
5. Scores jobs against your master profile using deterministic matching in mock mode or an LLM in live mode.
6. Generates a tailored DOCX resume, cover letter and application answers for strong matches.
7. Writes a Markdown daily report.
8. Exposes REST endpoints so n8n can trigger runs and deliver email/Telegram notifications.
9. Uses PostgreSQL for durable state and is ready for Qdrant integration as the semantic-search layer.

## Important scope

This project deliberately does NOT scrape LinkedIn or automate unauthorized activity on job portals. Add only APIs, public feeds, company career pages, ATS endpoints, or integrations you are authorized to use.

## Stack

- Java 21 (works with Java 17+ if dependency/toolchain constraints are satisfied)
- Spring Boot 4.1.1
- Spring Data JPA + PostgreSQL
- Flyway
- LangChain4j 1.20.1
- Apache POI for DOCX
- Qdrant (Docker, reserved for semantic profile/job retrieval)
- n8n for external scheduling/notifications if desired

## Run locally

### 1. Start infrastructure

```bash
docker compose up -d postgres qdrant n8n
```

### 2. Start the API

Install Maven 3.9+ and Java 21, then:

```bash
mvn spring-boot:run
```

The app starts with `APP_MOCK_AI=true`, so an LLM key is not required for the first smoke test.

### 3. Smoke test

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/profile
curl -X POST http://localhost:8080/api/jobs/run
curl -X POST http://localhost:8080/api/jobs/report
```

The default profile is seeded with placeholders. Replace it using:

```bash
curl -X PUT http://localhost:8080/api/profile \
  -H 'Content-Type: application/json' \
  -d @your-profile.json
```

### 4. Enable a real LLM

Set:

```bash
APP_MOCK_AI=false
OPENAI_API_KEY=...
LLM_MODEL=gpt-4.1-mini
```

The LLM layer uses LangChain4j's OpenAI integration and can be pointed at an OpenAI-compatible endpoint with `LLM_BASE_URL`.

### 5. Add job sources

Create `application-local.yml` from the example and configure your target Greenhouse board tokens and SmartRecruiters company identifiers. Public Greenhouse GET endpoints do not require authentication.

## n8n at 11 AM

You can either use the Spring scheduler alone or let n8n be the external scheduler. Recommended production setup:

- n8n Cron node: 11:00 Asia/Kolkata
- HTTP Request node: `POST http://ai-job-agent:8080/api/jobs/run`
- HTTP Request node: `POST http://ai-job-agent:8080/api/jobs/report`
- Read the generated report / notify via email or Telegram.

If n8n and Spring Boot run in separate Docker services, put the API service on the same Docker network and call its service name instead of localhost.

## Production hardening before public deployment

- Put the API behind HTTPS and authentication.
- Store secrets in environment variables / a secret manager.
- Do not expose PostgreSQL or Qdrant publicly.
- Configure Qdrant API key/TLS when deployed outside localhost.
- Add rate limits and source-specific retry/backoff.
- Add structured JSON logging and tracing.
- Add a job-source circuit breaker if the number of providers grows.
- Add a persistent application-review/approval state before any submission automation.
- Never let the LLM invent experience; validate generated claims against the master profile.
- Add a separate application-submission adapter only for portals/integrations that explicitly permit it.

## Next production increments

1. Qdrant semantic matching with embeddings.
2. Lever/other ATS adapters.
3. Job deduplication using semantic similarity in addition to source IDs.
4. Human approval dashboard.
5. Email/Telegram report workflow in n8n.
6. Resume versioning and audit trail.
7. Application tracking and interview/offer states.
8. Prometheus/Grafana dashboards.
