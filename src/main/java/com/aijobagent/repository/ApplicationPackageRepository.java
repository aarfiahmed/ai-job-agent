package com.aijobagent.repository;

import com.aijobagent.domain.ApplicationPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ApplicationPackageRepository extends JpaRepository<ApplicationPackage, Long> {
    Optional<ApplicationPackage> findByJobId(Long jobId);
}
