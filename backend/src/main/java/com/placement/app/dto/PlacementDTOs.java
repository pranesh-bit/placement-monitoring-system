package com.placement.app.dto;

import java.util.List;

public class PlacementDTOs {

    public static class ResumeUploadRequest {
        private String rawText;

        public String getRawText() { return rawText; }
        public void setRawText(String rawText) { this.rawText = rawText; }
    }

    public static class CompanyMatchResult {
        private Long driveId;
        private String companyName;
        private String roleTitle;
        private Double packageLpa;
        private String location;
        private List<String> requiredSkills;
        private List<String> matchingSkills;
        private List<String> missingSkills;
        private Double matchPercentage;
        private String matchStatus; // Highly Eligible, Eligible, Skill Gap Identified

        public Long getDriveId() { return driveId; }
        public void setDriveId(Long driveId) { this.driveId = driveId; }

        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }

        public String getRoleTitle() { return roleTitle; }
        public void setRoleTitle(String roleTitle) { this.roleTitle = roleTitle; }

        public Double getPackageLpa() { return packageLpa; }
        public void setPackageLpa(Double packageLpa) { this.packageLpa = packageLpa; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public List<String> getRequiredSkills() { return requiredSkills; }
        public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

        public List<String> getMatchingSkills() { return matchingSkills; }
        public void setMatchingSkills(List<String> matchingSkills) { this.matchingSkills = matchingSkills; }

        public List<String> getMissingSkills() { return missingSkills; }
        public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }

        public Double getMatchPercentage() { return matchPercentage; }
        public void setMatchPercentage(Double matchPercentage) { this.matchPercentage = matchPercentage; }

        public String getMatchStatus() { return matchStatus; }
        public void setMatchStatus(String matchStatus) { this.matchStatus = matchStatus; }
    }

    public static class SubmitAnswersRequest {
        private Long assessmentId;
        private List<Integer> selectedAnswers; // List of selected option indices per question
        private String dsaLanguage; // "Java" or "Python"
        private String dsaCode; // Student submitted DSA solution code

        public Long getAssessmentId() { return assessmentId; }
        public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }

        public List<Integer> getSelectedAnswers() { return selectedAnswers; }
        public void setSelectedAnswers(List<Integer> selectedAnswers) { this.selectedAnswers = selectedAnswers; }

        public String getDsaLanguage() { return dsaLanguage; }
        public void setDsaLanguage(String dsaLanguage) { this.dsaLanguage = dsaLanguage; }

        public String getDsaCode() { return dsaCode; }
        public void setDsaCode(String dsaCode) { this.dsaCode = dsaCode; }
    }

    public static class AnalyticsSummary {
        private Long totalStudents;
        private Long totalRecruiters;
        private Long totalDrives;
        private Long totalAssessmentsTaken;
        private Double averageTestScore;
        private List<String> topSkillsInDemand;
        private Double placementReadinessRate;

        public Long getTotalStudents() { return totalStudents; }
        public void setTotalStudents(Long totalStudents) { this.totalStudents = totalStudents; }

        public Long getTotalRecruiters() { return totalRecruiters; }
        public void setTotalRecruiters(Long totalRecruiters) { this.totalRecruiters = totalRecruiters; }

        public Long getTotalDrives() { return totalDrives; }
        public void setTotalDrives(Long totalDrives) { this.totalDrives = totalDrives; }

        public Long getTotalAssessmentsTaken() { return totalAssessmentsTaken; }
        public void setTotalAssessmentsTaken(Long totalAssessmentsTaken) { this.totalAssessmentsTaken = totalAssessmentsTaken; }

        public Double getAverageTestScore() { return averageTestScore; }
        public void setAverageTestScore(Double averageTestScore) { this.averageTestScore = averageTestScore; }

        public List<String> getTopSkillsInDemand() { return topSkillsInDemand; }
        public void setTopSkillsInDemand(List<String> topSkillsInDemand) { this.topSkillsInDemand = topSkillsInDemand; }

        public Double getPlacementReadinessRate() { return placementReadinessRate; }
        public void setPlacementReadinessRate(Double placementReadinessRate) { this.placementReadinessRate = placementReadinessRate; }
    }
}
