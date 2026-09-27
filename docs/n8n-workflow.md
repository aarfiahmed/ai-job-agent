# n8n workflow

Create a workflow:

1. Cron: every day at 11:00, timezone Asia/Kolkata.
2. HTTP Request: POST `http://ai-job-agent:8080/api/jobs/run`.
3. HTTP Request: POST `http://ai-job-agent:8080/api/jobs/report`.
4. Read the returned report path through a future authenticated report endpoint, or mount the shared report volume.
5. Send Email and/or Telegram.

Keep the application workflow inside Spring Boot. n8n is the scheduler and notification boundary.
