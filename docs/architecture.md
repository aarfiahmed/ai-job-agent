# Architecture

## Design principles

- Spring Boot owns domain/business logic.
- LangChain4j owns LLM integration.
- PostgreSQL is the source of truth for transactional state.
- Qdrant is a semantic retrieval layer, not the system of record.
- Job sources are adapters behind `JobSource`.
- AI output is validated before becoming an application artifact.
- Application submission is intentionally separated from application preparation.
- n8n is optional and should own external notifications/orchestration rather than core business rules.

## Daily flow

`SCHEDULE -> DISCOVER -> NORMALIZE -> FILTER -> MATCH -> GENERATE -> VALIDATE -> REPORT -> NOTIFY`

## Source policy

Use public APIs, public feeds, company career pages, ATS endpoints and integrations you are authorized to use. Do not build unauthorized LinkedIn/Indeed scraping or browser automation into the core system.
