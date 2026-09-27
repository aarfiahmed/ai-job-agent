CREATE TABLE candidate_profile (
  id BIGINT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255), phone VARCHAR(100), location VARCHAR(255),
  target_locations TEXT, target_titles TEXT,
  master_resume TEXT NOT NULL, skills TEXT, preferences TEXT
);
CREATE TABLE jobs (
  id BIGSERIAL PRIMARY KEY,
  source VARCHAR(100) NOT NULL,
  external_id VARCHAR(255) NOT NULL,
  company VARCHAR(255) NOT NULL,
  title VARCHAR(500) NOT NULL,
  location VARCHAR(500), employment_type VARCHAR(100),
  job_url TEXT, application_url TEXT,
  posted_at TIMESTAMPTZ, updated_at TIMESTAMPTZ,
  description TEXT, normalized_skills TEXT,
  match_score INTEGER, match_category VARCHAR(50), match_reason TEXT,
  created_at TIMESTAMPTZ NOT NULL, last_seen_at TIMESTAMPTZ NOT NULL,
  CONSTRAINT uk_job_source_external UNIQUE(source, external_id)
);
CREATE INDEX idx_jobs_match ON jobs(match_score DESC);
CREATE INDEX idx_jobs_company_title ON jobs(company,title);
CREATE TABLE application_packages (
  id BIGSERIAL PRIMARY KEY,
  job_id BIGINT NOT NULL UNIQUE REFERENCES jobs(id),
  status VARCHAR(50) NOT NULL,
  resume_path TEXT, cover_letter_path TEXT, answers_path TEXT,
  summary TEXT, created_at TIMESTAMPTZ NOT NULL
);
