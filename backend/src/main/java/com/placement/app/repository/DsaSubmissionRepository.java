package com.placement.app.repository;

import com.placement.app.entity.DsaSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DsaSubmissionRepository extends JpaRepository<DsaSubmission, Long> {
    List<DsaSubmission> findByStudentId(Long studentId);
    List<DsaSubmission> findByProblemId(Long problemId);
}
