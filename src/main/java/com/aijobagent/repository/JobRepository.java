package com.aijobagent.repository;

import com.aijobagent.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findBySourceAndExternalId(String source, String externalId);
    List<Job> findTop50ByMatchScoreGreaterThanEqualOrderByMatchScoreDesc(Integer score);
}
