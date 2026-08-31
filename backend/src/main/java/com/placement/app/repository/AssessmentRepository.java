package com.placement.app.repository;

import com.placement.app.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    Optional<Assessment> findByDriveId(Long driveId);
}
