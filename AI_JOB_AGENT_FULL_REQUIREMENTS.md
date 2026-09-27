# AI Job Agent — Full Product Requirements & Technical Specification

**Document Version:** 1.0  
**Status:** Development Specification  
**Audience:** AI coding agents, software engineers, architects  
**Primary Goal:** Build a production-ready personal AI job-search and application-preparation platform.

---

# 1. Product Overview

## 1.1 Product Name

**AI Job Agent**

## 1.2 Objective

Build a personal AI-powered system that runs automatically every day at **11:00 AM Asia/Kolkata**, discovers relevant jobs from multiple permitted/public job sources, analyzes and deduplicates those jobs, matches them against the user's professional profile and preferences, and prepares application materials for high-confidence matches.

The system must:

1. Discover jobs from multiple job portals, public ATS/job-board APIs, company career pages, and other permitted sources.
2. Normalize all jobs into one internal model.
3. Detect duplicate jobs across sources.
4. Analyze job descriptions.
5. Match jobs against the candidate's master profile.
6. Apply deterministic hard filters before expensive LLM processing.
7. Use semantic/vector matching and LLM reasoning for deeper matching.
8. Identify strengths and gaps for every strong match.
9. Generate a job-specific resume.
10. Generate a job-specific cover letter.
11. Generate answers for application questions when applicable.
12. Validate generated documents against the master profile to prevent fabricated claims.
13. Detect jobs that were previously applied to.
14. Detect jobs that are no longer active.
15. Rank/prioritize strong opportunities.
16. Generate a daily report.
17. Deliver the report through email and optionally Telegram.
18. Store all jobs, matches, documents, and application status.
19. Provide REST APIs and a future web dashboard.
20. Require human approval before actual application submission.
21. Keep the architecture extensible so new job sources can be added without changing the core domain.

---

# 2. Important Product Boundaries

## 2.1 Application Submission

Version 1 must focus on:

**Discover → Analyze → Match → Prepare Application → Report → Human Approval**

Do NOT implement uncontrolled automatic submission to arbitrary third-party job portals.

Actual submission should only be implemented later for sources/integrations where automation is explicitly permitted and technically supported.

The system must keep:

- job discovery
- application preparation
- application submission

as separate components.

## 2.2 LinkedIn and Other Restricted Platforms

Do not build the system around unauthorized scraping, browser automation, CAPTCHA bypass, login automation, or anti-bot circumvention.

LinkedIn/Indeed/etc. may be represented as discovery sources or user-provided URLs where permitted.

Primary automated sources should favor:

- public ATS APIs
- public job-board APIs
- company career pages
- authorized integrations
- permitted job feeds

---

# 3. Target Candidate Profile

The initial profile should support:

## Professional

- 14+ years Java backend development
- Tech Lead / Lead Backend Engineer
- Java 8 / Java 17+
- Spring Boot
- Microservices
- Kafka
- Distributed Systems
- Event-Driven Architecture
- AWS
- CQRS
- SAGA
- Outbox Pattern
- API Gateway
- Circuit Breaker
- Retry
- RAG
- LangChain4j
- Spring AI
- LLMs
- Agentic Workflows
- Qdrant
- Vector Databases
- Docker
- Kubernetes
- Prometheus
- Grafana

## Target Roles

Examples:

- Senior Java Backend Engineer
- Lead Java Engineer
- Lead Backend Engineer
- Staff Backend Engineer
- Principal Backend Engineer
- Java Technical Lead
- AI Backend Engineer
- AI/LLM Backend Engineer
- Software Architect where appropriate

## Target Locations

Initial configurable locations:

- Dubai
- UAE
- Netherlands
- UK
- Remote

The system must NOT hard-code these values. They must be configurable.

## Preferences

Configurable:

- minimum experience
- target roles
- locations
- remote/hybrid/on-site
- minimum salary
- currencies
- visa/work authorization requirements
- relocation preference
- employment type
- industries
- excluded companies
- preferred companies
- technologies
- minimum match threshold

---

# 4. High-Level Architecture

```text
                         +-----------------------+
                         |       Scheduler       |
                         |         n8n            |
                         |     11:00 AM IST      |
                         +-----------+-----------+
                                     |
                                     | HTTP
                                     v
                         +-----------------------+
                         |   Spring Boot API     |
                         +-----------+-----------+
                                     |
                                     v
                         +-----------------------+
                         | Daily Job Orchestrator|
                         +-----------+-----------+
                                     |
              +----------------------+----------------------+
              |                      |                      |
              v                      v                      v
      +---------------+      +---------------+      +---------------+
      | Greenhouse    |      | SmartRecruiter|      | Other Sources |
      | Adapter       |      | Adapter       |      | / ATS / APIs  |
      +-------+-------+      +-------+-------+      +-------+-------+
              |                      |                      |
              +----------------------+----------------------+
                                     |
                                     v
                         +-----------------------+
                         | Job Normalization     |
                         +-----------+-----------+
                                     |
                                     v
                         +-----------------------+
                         | Deduplication         |
                         +-----------+-----------+
                                     |
                                     v
                         +-----------------------+
                         | Hard Filter Engine    |
                         +-----------+-----------+
                                     |
                                     v
                         +-----------------------+
                         | Job Analysis           |
                         | LLM Agent               |
                         +-----------+-----------+
                                     |
                                     v
                         +-----------------------+
                         | Profile Match Engine   |
                         | Rules + Vector + LLM   |
                         +-----------+-----------+
                                     |
                         +-----------+-----------+
                         |                       |
                       Reject                 Strong Match
                         |                       |
                         v                       v
                       END              +----------------+
                                        | Resume Agent   |
                                        +-------+--------+
                                                |
                                                v
                                        +----------------+
                                        | Cover Letter   |
                                        | Agent          |
                                        +-------+--------+
                                                |
                                                v
                                        +----------------+
                                        | Application Q&A|
                                        | Agent          |
                                        +-------+--------+
                                                |
                                                v
                                        +----------------+
                                        | Truth Validator|
                                        +-------+--------+
                                                |
                                                v
                                        +----------------+
                                        | Report Builder |
                                        +-------+--------+
                                                |
                                                v
                                        +----------------+
                                        | Email/Telegram |
                                        +----------------+
```

---

# 5. Recommended Technology Stack

## Backend

- Java 21 LTS
- Spring Boot 4.x
- Spring Web
- Spring Validation
- Spring Data JPA
- Spring Actuator
- Spring Scheduling where required
- Flyway

## AI

- LangChain4j
- Configurable LLM provider
- OpenAI-compatible API support
- Gemini/OpenAI/other provider should be switchable through configuration

## Vector Database

- Qdrant

## Relational Database

- PostgreSQL

## Workflow / Scheduling

- n8n

n8n should trigger the daily workflow and handle external notification workflows.

The business logic must remain inside Spring Boot.

## Document Generation

- Apache POI / docx
- Markdown/HTML where appropriate
- PDF generation may be added later

## Infrastructure

- Docker
- Docker Compose for initial deployment
- Linux VPS
- Nginx or Caddy reverse proxy
- HTTPS

## Observability

- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana

Observability may be enabled in Phase 2.

---

# 6. Architectural Principles

## 6.1 Do Not Make Everything an AI Agent

Use deterministic software for:

- scheduling
- persistence
- validation
- deduplication
- hard filtering
- application status
- configuration
- security
- retries
- source adapters

Use AI for:

- understanding job descriptions
- semantic matching
- reasoning about fit
- resume tailoring
- cover letter generation
- application-answer generation

## 6.2 LLM Must Never Be the Source of Truth

The source of truth is:

**Candidate Master Profile**

The LLM may rewrite or reorganize factual information but must not invent:

- employers
- job titles
- years of experience
- technologies
- certifications
- projects
- achievements
- education
- responsibilities
- metrics

## 6.3 Provider Independence

LLM provider must be configurable.

Example:

```properties
ai.provider=openai
```

or:

```properties
ai.provider=gemini
```

No provider-specific logic should leak into domain services.

---

# 7. Core Components

## 7.1 Job Source Interface

Create:

```java
public interface JobSource {

    String getSourceType();

    List<JobSummary> search(JobSearchCriteria criteria);

    JobDetails getJobDetails(String externalJobId);
}
```

Each source implements this interface.

Examples:

```text
GreenhouseJobSource
SmartRecruitersJobSource
LeverJobSource
CompanyCareerSource
MockJobSource
```

Adding a new source must not require modifying the core job-processing workflow.

---

# 8. Job Discovery Service

Responsibilities:

1. Load configured search criteria.
2. Execute enabled job sources.
3. Handle pagination.
4. Respect rate limits.
5. Retry transient failures.
6. Record source-level errors.
7. Normalize results.
8. Persist raw source metadata.
9. Return discovered jobs.

## Requirements

- Source calls must have timeouts.
- Retry only transient failures.
- Use exponential backoff.
- Do not endlessly retry HTTP 4xx errors.
- Support source-specific rate limits.
- Continue processing if one source fails.
- Record partial failures.

Example:

```text
Greenhouse: SUCCESS
SmartRecruiters: SUCCESS
Company Careers: ERROR
Lever: SUCCESS

Daily workflow should continue.
```

---

# 9. Job Normalization

Every source must map to:

```text
Job
- id
- externalJobId
- source
- company
- title
- description
- location
- country
- city
- remoteType
- employmentType
- salaryMin
- salaryMax
- salaryCurrency
- postedAt
- updatedAt
- jobUrl
- applicationUrl
- sourceUrl
- rawPayload
- status
```

Raw provider-specific data must be retained when practical for debugging.

---

# 10. Job Deduplication

Deduplicate using multiple signals.

## Exact Signals

- external job ID
- canonical application URL
- canonical job URL

## Composite Signals

```text
company
+
normalized title
+
location
```

## Semantic Signals

Use embeddings when exact matching is insufficient.

Potential duplicate similarity:

```text
company similarity
+
title similarity
+
location
+
description embedding similarity
```

Do not use LLM calls for every duplicate check.

Prefer deterministic matching first.

---

# 11. Hard Filter Engine

Before invoking expensive AI operations, apply deterministic filters.

Examples:

## Experience

```text
Required experience > candidate maximum acceptable experience
=> reject
```

## Location

If candidate configuration says:

```text
UAE only
```

then jobs outside configured locations are rejected unless remote is allowed.

## Employment Type

Reject unsupported employment types.

## Work Authorization

If the job explicitly requires authorization unavailable to the candidate, mark:

```text
HARD_GAP
```

and optionally reject depending on profile configuration.

## Already Applied

If job/application fingerprint already exists:

```text
ALREADY_APPLIED
```

Do not generate another application package.

---

# 12. Job Analysis Agent

Input:

- normalized job

Output:

```json
{
  "title": "...",
  "company": "...",
  "location": "...",
  "seniority": "...",
  "minimumExperienceYears": 8,
  "mustHaveSkills": [],
  "niceToHaveSkills": [],
  "responsibilities": [],
  "educationRequirements": [],
  "certifications": [],
  "domainRequirements": [],
  "remoteType": "...",
  "salary": {},
  "visaRequirements": [],
  "keywords": []
}
```

The output must be validated against a strict JSON schema.

The system must reject malformed structured output and retry with constrained output.

---

# 13. Candidate Profile Model

The profile must be structured rather than storing only a PDF resume.

Example:

```json
{
  "candidate": {
    "name": "...",
    "yearsOfExperience": 14,
    "currentTitle": "Tech Lead",
    "location": "India"
  },
  "skills": [],
  "experiences": [],
  "projects": [],
  "education": [],
  "certifications": [],
  "preferences": {
    "locations": [],
    "roles": [],
    "remote": true,
    "minimumSalary": {},
    "currencies": []
  }
}
```

The master resume remains an important source document, but the structured profile is the canonical factual profile.

---

# 14. Profile Matching Engine

Use a hybrid approach.

## Layer 1 — Hard Rules

Examples:

- required years
- required location
- work authorization
- role compatibility

## Layer 2 — Skill Matching

Exact and normalized technology matching.

Example:

```text
Spring Boot
Spring
Spring Framework
```

may be related but should not automatically be treated as identical.

Maintain a technology synonym/relationship dictionary.

## Layer 3 — Vector Similarity

Embed:

- job description
- candidate experience
- candidate projects
- candidate skills

Store embeddings in Qdrant.

## Layer 4 — LLM Reasoning

Use the LLM to explain:

- strongest matching experience
- gaps
- transferable skills
- concerns

The LLM should not independently override hard rules.

---

# 15. Match Result

Store:

```json
{
  "matchStatus": "STRONG_MATCH",
  "score": 0.91,
  "technicalFit": 0.95,
  "experienceFit": 1.0,
  "locationFit": 1.0,
  "seniorityFit": 0.9,
  "domainFit": 0.8,
  "matchedSkills": [],
  "missingSkills": [],
  "hardGaps": [],
  "explanation": "...",
  "createdAt": "..."
}
```

The score is an internal prioritization metric, not a guarantee of hiring success.

Recommended configurable thresholds:

```text
>= 0.85 -> STRONG_MATCH
0.70-0.849 -> REVIEW
0.50-0.699 -> WEAK_MATCH
< 0.50 -> REJECT
```

Do not hard-code these values.

---

# 16. Resume Agent

Input:

- master structured profile
- master resume
- job analysis
- match analysis

Output:

- tailored resume
- structured change log

Rules:

1. Never invent facts.
2. Never inflate years of experience.
3. Never add unsupported technologies.
4. Never create fake metrics.
5. Preserve factual employment dates.
6. Preserve factual job titles.
7. Emphasize relevant experience.
8. Reorder skills based on job relevance.
9. Keep ATS-friendly formatting.
10. Do not keyword-stuff.

The generated resume must include a provenance record.

Example:

```text
Resume bullet
   ↓
source = candidate.experience[3]
```

---

# 17. Resume Truth Validator

Every generated factual claim should be checked against:

- candidate profile
- master resume
- verified project records

Classify claims:

```text
SUPPORTED
UNSUPPORTED
AMBIGUOUS
```

Rules:

```text
UNSUPPORTED -> remove or regenerate
AMBIGUOUS -> regenerate conservatively
SUPPORTED -> allow
```

The validator must run before report generation.

---

# 18. Cover Letter Agent

Input:

- job analysis
- company
- candidate profile
- strongest matching experiences

Output:

- concise tailored cover letter

Rules:

- no generic filler
- no fake company claims
- no fabricated achievements
- no unsupported technology claims
- maximum configurable length
- professional tone

---

# 19. Application Q&A Agent

Generate answers for common questions:

- Why are you interested?
- Why this company?
- Why this role?
- Relevant experience?
- Kafka experience?
- Java experience?
- AWS experience?
- Notice period?
- Salary expectation?
- Relocation?
- Work authorization?

Answers must use verified profile data.

Sensitive personal questions should not be fabricated.

---

# 20. Job Priority Engine

Do not generate documents for every discovered job.

Pipeline:

```text
100 jobs
   ↓
Hard filters
   ↓
50 jobs
   ↓
Matching
   ↓
15 strong/review matches
   ↓
Priority
   ↓
Top N document generation
```

Configurable:

```properties
job-agent.document-generation.max-jobs=10
```

This controls LLM cost.

---

# 21. Application Tracker

Application states:

```text
DISCOVERED
ANALYZED
MATCHED
DOCUMENTS_GENERATED
READY_FOR_REVIEW
APPROVED
APPLIED
SCREENING
INTERVIEW
OFFER
REJECTED
WITHDRAWN
CLOSED
```

Every transition should be recorded in an audit/history table.

---

# 22. Already Applied Detection

Fingerprint:

```text
company
+
externalJobId
+
canonicalApplicationUrl
```

If fingerprint exists:

```text
skip application generation
```

The system should still update the job if the source posting changes.

---

# 23. Closed Job Detection

On subsequent runs:

1. Check whether the job still exists.
2. Check source status.
3. Mark closed jobs as `CLOSED`.
4. Do not generate documents for closed jobs.

---

# 24. Daily Workflow

Daily workflow:

```text
START
 |
 +--> Load candidate profile
 |
 +--> Load job-source configuration
 |
 +--> Search all enabled sources
 |
 +--> Normalize
 |
 +--> Deduplicate
 |
 +--> Hard filters
 |
 +--> Job analysis
 |
 +--> Semantic matching
 |
 +--> LLM match reasoning
 |
 +--> Select strong matches
 |
 +--> Generate tailored resumes
 |
 +--> Generate cover letters
 |
 +--> Generate application Q&A
 |
 +--> Validate documents
 |
 +--> Persist documents
 |
 +--> Build report
 |
 +--> Send report
 |
 +--> END
```

The workflow must be idempotent.

Running it twice should not create duplicate jobs or duplicate application packages.

---

# 25. Scheduling

n8n should trigger:

```text
POST /api/v1/runs/daily
```

at:

```text
11:00
Asia/Kolkata
```

The Spring Boot service may also have a scheduler as a fallback, but only one scheduler should be active in production to avoid duplicate runs.

Preferred:

```text
n8n = external scheduler
Spring Boot = workflow executor
```

---

# 26. n8n Workflow

Minimal workflow:

```text
Schedule Trigger
       |
       v
HTTP Request
POST /api/v1/runs/daily
       |
       v
Wait / Poll
       |
       v
GET /api/v1/reports/latest
       |
       v
Email
       |
       v
Telegram (optional)
```

n8n must not contain the core job matching logic.

---

# 27. Daily Report

Report must contain:

## Summary

```text
Date
Sources searched
Jobs discovered
Jobs after filtering
Strong matches
Documents generated
Errors
```

## Each Strong Match

```text
Company
Job title
Location
Source
Job URL
Application URL
Match score/status
Matched skills
Missing skills
Hard gaps
Why it matches
Job posted date
Salary if available
```

## Documents

```text
Tailored Resume
Cover Letter
Application Answers
```

## Suggested Next Action

```text
READY_FOR_REVIEW
```

The system must not automatically claim that the candidate should apply; it should present the information and let the user decide.

---

# 28. Notification Channels

Version 1:

- Email

Optional:

- Telegram
- Slack

Future:

- Web push
- Mobile notification

---

# 29. REST API

Base:

```text
/api/v1
```

## Profile

```http
GET /profile
PUT /profile
POST /profile/resume
GET /profile/resume
```

## Jobs

```http
GET /jobs
GET /jobs/{id}
POST /jobs/search
```

## Matching

```http
GET /jobs/{id}/match
POST /jobs/{id}/match
```

## Documents

```http
GET /jobs/{id}/documents
POST /jobs/{id}/resume
POST /jobs/{id}/cover-letter
POST /jobs/{id}/application-answers
```

## Daily Run

```http
POST /runs/daily
GET /runs/{id}
GET /runs/latest
```

## Reports

```http
GET /reports/latest
GET /reports/{id}
```

## Applications

```http
GET /applications
GET /applications/{id}
PATCH /applications/{id}/status
```

## Sources

```http
GET /sources
POST /sources
PATCH /sources/{id}
```

---

# 30. Database Schema

Recommended tables:

```text
candidate_profile
candidate_skill
candidate_experience
candidate_project
candidate_education
candidate_certification
candidate_preference

job_source
job
job_raw_payload
job_skill
job_analysis

job_match
job_match_skill
job_match_reason

application
application_status_history

generated_document
document_claim
document_validation

daily_run
daily_run_source
daily_report

audit_event
system_setting
```

---

# 31. Important Database Constraints

Unique constraints:

```text
job_source + external_job_id
```

and where applicable:

```text
canonical_application_url
```

Application:

```text
candidate + job
```

Document:

```text
application + document_type + version
```

All timestamps must be stored in UTC.

Display timezone:

```text
Asia/Kolkata
```

---

# 32. Qdrant Collections

Recommended collections:

```text
candidate_experience
candidate_skills
candidate_projects
job_descriptions
job_requirements
```

Payload should include:

```json
{
  "candidateId": "...",
  "jobId": "...",
  "source": "...",
  "type": "...",
  "text": "..."
}
```

Do not put sensitive information into vectors unless necessary.

---

# 33. LLM Abstraction

Create:

```java
interface AiProvider {
    JobAnalysis analyzeJob(Job job);
    MatchAnalysis analyzeMatch(...);
    ResumeDraft generateResume(...);
    CoverLetter generateCoverLetter(...);
    ApplicationAnswers generateAnswers(...);
}
```

Implementation examples:

```text
LangChain4jAiProvider
MockAiProvider
```

The application must support:

```text
APP_MOCK_AI=true
```

for local testing.

---

# 34. Prompt Management

Do not hard-code large prompts inside Java methods.

Store prompts as resources:

```text
src/main/resources/prompts/
    job-analysis.txt
    profile-match.txt
    resume-generation.txt
    cover-letter.txt
    application-answers.txt
    truth-validation.txt
```

Prompts should be versioned.

Store prompt version in generated-document metadata.

---

# 35. AI Cost Controls

Implement:

- deterministic filtering first
- deduplication before LLM calls
- caching
- batch processing where supported
- maximum jobs per run
- configurable model
- configurable token limits
- retry limits
- fallback model

Example:

```properties
ai.max-jobs-for-analysis=50
ai.max-jobs-for-documents=10
ai.max-retries=2
```

---

# 36. Retry Strategy

For AI calls:

```text
attempt 1
   ↓
failure
   ↓
backoff
   ↓
attempt 2
   ↓
failure
   ↓
fallback model/provider
```

Do not retry:

- invalid business input
- permanent HTTP 4xx
- validation failures without prompt correction

---

# 37. Resilience

Every external dependency must have:

- timeout
- retry
- circuit breaker where appropriate
- structured error logging
- fallback/partial processing

A failure from one job source must not fail the entire daily run.

---

# 38. Security

Never commit:

```text
API keys
database passwords
SMTP passwords
Telegram tokens
LLM keys
```

Use:

```text
.env
Docker secrets
environment variables
```

Production should use a secret manager where practical.

API should require authentication before exposing sensitive profile/document endpoints.

---

# 39. Privacy

The candidate's:

- resume
- contact details
- employment history
- application information

are private data.

Do not log full resumes or application documents.

Logs should contain IDs, not complete personal data.

---

# 40. Observability

Every daily run gets:

```text
runId
```

Every operation logs:

```text
runId
jobId
source
operation
duration
status
errorCode
```

Metrics:

```text
jobs.discovered
jobs.normalized
jobs.duplicates
jobs.filtered
jobs.analyzed
jobs.strong_matches
documents.generated
documents.validation_failures
source.errors
ai.requests
ai.tokens
ai.errors
run.duration
```

---

# 41. Health Endpoints

Required:

```http
GET /actuator/health
GET /actuator/info
```

Health checks:

- PostgreSQL
- Qdrant
- AI provider where practical

Do not make external job sources mandatory for application startup.

---

# 42. Testing Strategy

## Unit Tests

Test:

- job normalization
- canonical URL generation
- deduplication
- hard filters
- matching rules
- score calculation
- application-state transitions
- document validation

## Integration Tests

Use:

- Testcontainers PostgreSQL
- Testcontainers Qdrant if practical
- Mock HTTP job sources
- Mock AI provider

## End-to-End Test

Scenario:

```text
Mock jobs
   ↓
Discovery
   ↓
Normalization
   ↓
Deduplication
   ↓
Matching
   ↓
Resume
   ↓
Cover letter
   ↓
Validation
   ↓
Report
```

Expected:

```text
daily run SUCCESS
strong match > 0
documents generated
report generated
```

---

# 43. Local Development

Required:

```text
Java 21
Maven
Docker
Docker Compose
```

Start:

```bash
docker compose up -d postgres qdrant n8n
```

Run:

```bash
./mvnw spring-boot:run
```

Health:

```bash
curl http://localhost:8080/actuator/health
```

Daily run:

```bash
curl -X POST http://localhost:8080/api/v1/runs/daily
```

---

# 44. Mock Mode

The application must work without external credentials.

Configuration:

```properties
app.mock-ai=true
app.mock-job-source=true
```

Mock mode must create:

- at least 3 jobs
- at least 1 strong match
- at least 1 weak match
- at least 1 duplicate
- generated mock resume
- generated mock cover letter
- report

This is mandatory for CI.

---

# 45. CI/CD

Recommended GitHub Actions pipeline:

```text
checkout
   ↓
setup Java 21
   ↓
Maven test
   ↓
integration tests
   ↓
build
   ↓
Docker image
   ↓
security scan
   ↓
publish image
```

Deployment should be separate from build.

---

# 46. Docker

Services:

```yaml
services:
  app:
  postgres:
  qdrant:
  n8n:
```

Do not expose PostgreSQL publicly.

Qdrant should be restricted to private network where possible.

n8n should be protected with authentication and HTTPS in production.

---

# 47. Production Deployment

Target:

```text
Linux VPS
```

Recommended:

```text
Internet
   |
   v
Nginx/Caddy
   |
   +--> Spring Boot
   |
   +--> n8n
```

Internal:

```text
Spring Boot
   |
   +--> PostgreSQL
   +--> Qdrant
```

Do not expose PostgreSQL/Qdrant directly to the Internet.

---

# 48. Backup

Daily:

- PostgreSQL backup
- application configuration backup
- generated document backup

Do not back up secrets into the same location.

Retention:

```text
7 daily
4 weekly
3 monthly
```

Make retention configurable.

---

# 49. Idempotency

Every daily run must have an idempotency key:

```text
DAILY-YYYY-MM-DD
```

If the same run is triggered twice:

```text
do not duplicate jobs
do not duplicate documents
do not duplicate notifications
```

---

# 50. Report Generation

Create both:

```text
HTML report
Markdown report
```

Optional:

```text
PDF
```

Documents should be stored with:

```text
candidate/
  YYYY/
    MM/
      DD/
        company-job-slug/
          resume.docx
          cover-letter.md
          application-answers.md
          match-analysis.json
```

---

# 51. Configuration

Example:

```properties
app.timezone=Asia/Kolkata

job-agent.match.strong-threshold=0.85
job-agent.match.review-threshold=0.70

job-agent.document-generation.max-jobs=10

job-agent.sources.greenhouse.enabled=true
job-agent.sources.smartrecruiters.enabled=true

ai.provider=openai
ai.model=...
ai.temperature=0.2

qdrant.url=http://qdrant:6333

spring.datasource.url=jdbc:postgresql://postgres:5432/ai_job_agent
```

All values must be environment-variable overridable.

---

# 52. Job Source Configuration

Do not hard-code companies.

Example:

```yaml
sources:
  greenhouse:
    enabled: true
    boards:
      - companySlugA
      - companySlugB

  smartrecruiters:
    enabled: true
    companies:
      - companyA
      - companyB
```

Later these should be configurable through the database/admin API.

---

# 53. Candidate Profile Management

Provide an API/UI later to manage:

```text
Skills
Experience
Projects
Education
Certifications
Preferred locations
Preferred roles
Salary
Remote preference
Excluded companies
Preferred companies
Visa/work authorization
Notice period
```

The user must be able to edit profile facts without changing source code.

---

# 54. Human Review

Each strong match should have:

```text
VIEW JOB
VIEW MATCH
VIEW RESUME
VIEW COVER LETTER
VIEW APPLICATION ANSWERS

[APPROVE]
[REJECT]
[EDIT]
```

Approval must be persisted.

Actual submission is a separate action.

---

# 55. Future Browser/Application Module

Future component:

```text
ApplicationExecutor
```

Interface:

```java
public interface ApplicationExecutor {

    ApplicationResult prepare(Application application);

    ApplicationResult submit(Application application);
}
```

Do not implement arbitrary website automation in the initial version.

Implement provider-specific executors only where the provider explicitly permits automation/integration.

---

# 56. Recommended Package Structure

```text
com.example.aijobagent

├── config
├── common
│   ├── exception
│   ├── validation
│   └── security
│
├── profile
│   ├── controller
│   ├── service
│   ├── domain
│   └── repository
│
├── job
│   ├── controller
│   ├── domain
│   ├── service
│   ├── repository
│   ├── source
│   │   ├── greenhouse
│   │   ├── smartrecruiters
│   │   └── mock
│   ├── normalization
│   └── deduplication
│
├── matching
│   ├── rule
│   ├── semantic
│   ├── ai
│   └── service
│
├── document
│   ├── resume
│   ├── coverletter
│   ├── applicationqa
│   └── validation
│
├── ai
│   ├── provider
│   ├── prompt
│   ├── model
│   └── tool
│
├── application
│   ├── domain
│   ├── service
│   ├── controller
│   └── repository
│
├── workflow
│   ├── daily
│   └── report
│
├── notification
│   ├── email
│   └── telegram
│
└── infrastructure
    ├── qdrant
    └── persistence
```

---

# 57. Agent Responsibilities

## Job Analyzer Agent

Only understand the job.

Do not generate resumes.

## Match Agent

Only evaluate candidate/job fit.

Do not generate documents.

## Resume Agent

Only tailor resume.

Must use verified profile data.

## Cover Letter Agent

Only generate cover letter.

## Application Q&A Agent

Only generate answers.

## Truth Validator

Only validate claims.

## Report Agent

Only assemble results.

This separation improves testability and observability.

---

# 58. Agent State

Each workflow should maintain structured state:

```java
DailyJobRunState {

    UUID runId;

    CandidateProfile profile;

    List<Job> discoveredJobs;

    List<Job> filteredJobs;

    List<JobMatch> matches;

    List<GeneratedDocument> documents;

    Report report;

    RunStatus status;
}
```

Avoid storing huge raw documents inside workflow state.

Use IDs/references to persisted data.

---

# 59. Error Handling

Errors should be categorized:

```text
SOURCE_UNAVAILABLE
SOURCE_RATE_LIMIT
INVALID_JOB
AI_TIMEOUT
AI_RATE_LIMIT
AI_INVALID_OUTPUT
DOCUMENT_VALIDATION_FAILED
DATABASE_ERROR
VECTOR_DB_ERROR
NOTIFICATION_ERROR
```

A notification failure must not mark the entire job-processing workflow as failed if the report itself was successfully generated.

---

# 60. Security Rules for AI Tools

LLM-generated tool calls must not be allowed to:

- execute arbitrary shell commands
- access filesystem outside permitted directories
- access secrets
- modify database schema
- send arbitrary emails
- submit arbitrary applications

Tool access must be allowlisted.

---

# 61. Performance Targets

Initial target:

```text
100–500 discovered jobs/day
```

Processing:

```text
< 15 minutes
```

for normal source availability and configurable LLM limits.

The system should support asynchronous processing later.

---

# 62. Cost Targets

Minimize LLM usage.

Preferred pipeline:

```text
Cheap deterministic filters
       ↓
Cheap normalization
       ↓
Vector similarity
       ↓
LLM analysis only for candidates
       ↓
LLM document generation only for top matches
```

Never send every discovered job to the expensive LLM.

---

# 63. Security / Privacy Requirements

The application must:

- use HTTPS in production
- protect APIs
- encrypt credentials/secrets
- avoid sensitive logging
- support deletion of candidate data
- support export of candidate data
- avoid sending unnecessary personal information to LLM providers
- document what data is sent to external AI providers

---

# 64. Acceptance Criteria

The system is considered MVP-complete when:

### Discovery

- [ ] At least two real job sources work.
- [ ] Mock source works.
- [ ] Pagination works.
- [ ] Source failure does not terminate entire run.

### Processing

- [ ] Jobs normalize correctly.
- [ ] Duplicates are removed.
- [ ] Hard filters work.
- [ ] Job analysis produces valid structured output.

### Matching

- [ ] Candidate profile is configurable.
- [ ] Deterministic matching works.
- [ ] Semantic matching works.
- [ ] Match explanation is stored.

### Documents

- [ ] Tailored resume generated.
- [ ] Cover letter generated.
- [ ] Application answers generated.
- [ ] Truth validation prevents unsupported claims.

### Tracking

- [ ] Previously applied jobs detected.
- [ ] Closed jobs detected.
- [ ] Application states tracked.

### Reporting

- [ ] Daily report generated.
- [ ] Job URLs included.
- [ ] Application URLs included.
- [ ] Resume links included.
- [ ] Cover letter included.
- [ ] Match analysis included.

### Automation

- [ ] n8n triggers at 11 AM Asia/Kolkata.
- [ ] Daily workflow is idempotent.
- [ ] Notification delivered.

### Quality

- [ ] Unit tests.
- [ ] Integration tests.
- [ ] End-to-end mock test.
- [ ] Docker Compose startup works.
- [ ] Health endpoint works.

---

# 65. Development Phases

## Phase 0 — Skeleton

- Spring Boot
- PostgreSQL
- Flyway
- Docker Compose
- Actuator
- basic REST API

## Phase 1 — Profile

- Candidate profile
- Master resume upload
- structured profile

## Phase 2 — Job Discovery

- JobSource interface
- Greenhouse
- SmartRecruiters
- Mock source
- normalization

## Phase 3 — Deduplication and Filtering

- canonical URLs
- duplicate detection
- hard filters
- already-applied detection

## Phase 4 — AI Matching

- LangChain4j
- Job Analysis Agent
- Match Agent
- structured outputs

## Phase 5 — Qdrant

- embeddings
- candidate vectors
- job vectors
- semantic similarity

## Phase 6 — Documents

- Resume Agent
- Cover Letter Agent
- Application Q&A Agent
- Truth Validator

## Phase 7 — Reports

- HTML
- Markdown
- email
- Telegram

## Phase 8 — n8n

- 11 AM trigger
- daily API invocation
- report delivery

## Phase 9 — Dashboard

- jobs
- matches
- documents
- applications
- analytics

## Phase 10 — Production

- HTTPS
- authentication
- monitoring
- backups
- CI/CD
- VPS deployment

---

# 66. AI Coding Agent Instructions

The AI coding agent implementing this specification must follow these rules:

1. Do not implement everything in one commit.
2. Build phase by phase.
3. Keep the application compiling after every phase.
4. Add tests with each feature.
5. Never use TODO placeholders for core functionality.
6. Do not silently remove requirements.
7. Ask before making major architecture changes.
8. Prefer interfaces for external integrations.
9. Keep provider-specific code isolated.
10. Never hard-code API keys.
11. Never hard-code candidate facts outside the profile.
12. Never fabricate resume information.
13. Use database migrations for schema changes.
14. Use structured logging.
15. Make workflows idempotent.
16. Handle partial source failures.
17. Add timeout/retry policies.
18. Keep LLM prompts versioned.
19. Validate LLM structured outputs.
20. Add integration tests for external adapters.
21. Use mock providers in CI.
22. Do not implement unauthorized website scraping or anti-bot bypasses.
23. Do not automatically submit applications without an explicit supported integration.
24. Document every external API integration.
25. Update README after every major phase.

---

# 67. Definition of Done

A feature is DONE only when:

```text
Code
+
Unit tests
+
Integration test where appropriate
+
Configuration
+
Error handling
+
Logging
+
Documentation
+
Docker compatibility
```

are complete.

A feature is NOT done merely because the happy path works.

---

# 68. Recommended Initial Repository

```text
ai-job-agent/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       ├── prompts/
│   │       └── db/migration/
│   │
│   └── test/
│
├── docker/
├── n8n/
├── docs/
├── scripts/
├── .env.example
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

# 69. Final Product Vision

The final system should behave like:

```text
Every day at 11 AM
        |
        v
Find new jobs
        |
        v
Remove duplicates
        |
        v
Remove irrelevant jobs
        |
        v
Understand remaining jobs
        |
        v
Compare with candidate profile
        |
        v
Identify strong matches
        |
        v
Create job-specific resume
        |
        v
Create cover letter
        |
        v
Prepare application answers
        |
        v
Validate every generated claim
        |
        v
Store everything
        |
        v
Generate daily report
        |
        v
Send report to candidate
        |
        v
Candidate reviews and decides
        |
        v
Optional permitted application submission
```

The system should ultimately reduce the candidate's daily work to:

**Open report → review strong opportunities → review tailored documents → decide which applications to submit.**

---

# 70. Primary Engineering Principle

The most important design rule is:

> **Use deterministic software for control, rules, persistence, security and orchestration; use AI for interpretation, semantic reasoning and content generation; use human approval for consequential application decisions.**

This keeps the system reliable, auditable, cost-controlled, and extensible.
