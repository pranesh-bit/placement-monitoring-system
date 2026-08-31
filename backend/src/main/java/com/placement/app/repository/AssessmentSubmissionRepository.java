package com.placement.app.repository;

import com.placement.app.entity.AssessmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AssessmentSubmissionRepository extends JpaRepository<AssessmentSubmission, Long> {
    List<AssessmentSubmission> findByStudentId(Long studentId);
    List<AssessmentSubmission> findByAssessmentId(Long assessmentId);
    Optional<AssessmentSubmission> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);
}
