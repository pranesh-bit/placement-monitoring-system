package com.placement.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long driveId;
    private String roleTitle;

    @Column(length = 500)
    private String targetSkills;

    @Column(columnDefinition = "TEXT")
    private String questionsJson; // JSON array of questions with options and answers

    private Integer totalQuestions;
    private Integer durationMinutes;

    private String dsaTitle;
    private String dsaDifficulty;

    @Column(columnDefinition = "TEXT")
    private String dsaDescription;

    private String dsaConstraints;

    @Column(columnDefinition = "TEXT")
    private String dsaSampleInput;

    @Column(columnDefinition = "TEXT")
    private String dsaSampleOutput;

    @Column(columnDefinition = "TEXT")
    private String dsaTestCasesJson;

    @Column(columnDefinition = "TEXT")
    private String dsaStarterCodeJava;

    @Column(columnDefinition = "TEXT")
    private String dsaStarterCodePython;

    private LocalDateTime createdDate;

    public Assessment() {
        this.createdDate = LocalDateTime.now();
        this.durationMinutes = 30;
        this.totalQuestions = 30;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDriveId() { return driveId; }
    public void setDriveId(Long driveId) { this.driveId = driveId; }

    public String getRoleTitle() { return roleTitle; }
    public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }

    public String getTargetSkills() { return targetSkills; }
    public void setTargetSkills(String targetSkills) { this.targetSkills = targetSkills; }

    public String getQuestionsJson() { return questionsJson; }
    public void setQuestionsJson(String questionsJson) { this.questionsJson = questionsJson; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getDsaTitle() { return dsaTitle; }
    public void setDsaTitle(String dsaTitle) { this.dsaTitle = dsaTitle; }

    public String getDsaDifficulty() { return dsaDifficulty; }
    public void setDsaDifficulty(String dsaDifficulty) { this.dsaDifficulty = dsaDifficulty; }

    public String getDsaDescription() { return dsaDescription; }
    public void setDsaDescription(String dsaDescription) { this.dsaDescription = dsaDescription; }

    public String getDsaConstraints() { return dsaConstraints; }
    public void setDsaConstraints(String dsaConstraints) { this.dsaConstraints = dsaConstraints; }

    public String getDsaSampleInput() { return dsaSampleInput; }
    public void setDsaSampleInput(String dsaSampleInput) { this.dsaSampleInput = dsaSampleInput; }

    public String getDsaSampleOutput() { return dsaSampleOutput; }
    public void setDsaSampleOutput(String dsaSampleOutput) { this.dsaSampleOutput = dsaSampleOutput; }

    public String getDsaTestCasesJson() { return dsaTestCasesJson; }
    public void setDsaTestCasesJson(String dsaTestCasesJson) { this.dsaTestCasesJson = dsaTestCasesJson; }

    public String getDsaStarterCodeJava() { return dsaStarterCodeJava; }
    public void setDsaStarterCodeJava(String dsaStarterCodeJava) { this.dsaStarterCodeJava = dsaStarterCodeJava; }

    public String getDsaStarterCodePython() { return dsaStarterCodePython; }
    public void setDsaStarterCodePython(String dsaStarterCodePython) { this.dsaStarterCodePython = dsaStarterCodePython; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
