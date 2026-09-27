# Qdrant semantic matching

Qdrant is included in Docker Compose and reserved for semantic retrieval. The first version deliberately keeps deterministic matching and LLM matching simple so the system can run without an embedding key.

Production increment:

1. Generate embeddings for the master profile and each normalized job description.
2. Upsert vectors into a `job_profile_matches` collection.
3. Retrieve top-k semantic matches.
4. Combine semantic similarity with hard rules and LLM reasoning.
5. Store the final match explanation in PostgreSQL.

Do not use Qdrant as the transactional source of truth. Qdrant documents its REST API as a production-ready vector-search interface and its Java client is available on Maven Central.
