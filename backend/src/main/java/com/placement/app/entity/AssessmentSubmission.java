package com.placement.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_submissions")
public class AssessmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long assessmentId;
    private Long studentId;
    private String studentName;
    private Integer score;
    private Integer totalQuestions;
    private Integer mcqScore;
    private Integer mcqTotal;
    private Integer dsaPassCount;
    private Integer dsaTotalCases;
    private String dsaStatus;
    private Double percentage;
    private String readinessLevel;

    @Column(length = 2000)
    private String aiFeedback;

    private LocalDateTime submittedAt;

    public AssessmentSubmission() {
        this.submittedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Integer getMcqScore() { return mcqScore; }
    public void setMcqScore(Integer mcqScore) { this.mcqScore = mcqScore; }

    public Integer getMcqTotal() { return mcqTotal; }
    public void setMcqTotal(Integer mcqTotal) { this.mcqTotal = mcqTotal; }

    public Integer getDsaPassCount() { return dsaPassCount; }
    public void setDsaPassCount(Integer dsaPassCount) { this.dsaPassCount = dsaPassCount; }

    public Integer getDsaTotalCases() { return dsaTotalCases; }
    public void setDsaTotalCases(Integer dsaTotalCases) { this.dsaTotalCases = dsaTotalCases; }

    public String getDsaStatus() { return dsaStatus; }
    public void setDsaStatus(String dsaStatus) { this.dsaStatus = dsaStatus; }

    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }

    public String getReadinessLevel() { return readinessLevel; }
    public void setReadinessLevel(String readinessLevel) { this.readinessLevel = readinessLevel; }

    public String getAiFeedback() { return aiFeedback; }
    public void setAiFeedback(String aiFeedback) { this.aiFeedback = aiFeedback; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
