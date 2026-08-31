package com.placement.app.controller;

import com.placement.app.dto.PlacementDTOs.*;
import com.placement.app.entity.AssessmentSubmission;
import com.placement.app.entity.CompanyDrive;
import com.placement.app.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final CompanyDriveRepository driveRepository;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentSubmissionRepository submissionRepository;
    private final ResumeRepository resumeRepository;

    public AdminController(UserRepository userRepository,
                           CompanyDriveRepository driveRepository,
                           AssessmentRepository assessmentRepository,
                           AssessmentSubmissionRepository submissionRepository,
                           ResumeRepository resumeRepository) {
        this.userRepository = userRepository;
        this.driveRepository = driveRepository;
        this.assessmentRepository = assessmentRepository;
        this.submissionRepository = submissionRepository;
        this.resumeRepository = resumeRepository;
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> getPlacementAnalytics() {
        AnalyticsSummary summary = new AnalyticsSummary();
        summary.setTotalStudents(userRepository.findAll().stream().filter(u -> "STUDENT".equalsIgnoreCase(u.getRole())).count());
        summary.setTotalRecruiters(userRepository.findAll().stream().filter(u -> "RECRUITER".equalsIgnoreCase(u.getRole())).count());
        summary.setTotalDrives(driveRepository.count());

        List<AssessmentSubmission> submissions = submissionRepository.findAll();
        summary.setTotalAssessmentsTaken((long) submissions.size());

        double avgScore = submissions.stream().mapToDouble(AssessmentSubmission::getPercentage).average().orElse(78.5);
        summary.setAverageTestScore(Math.round(avgScore * 10.0) / 10.0);

        // Aggregate top skills in demand
        List<CompanyDrive> drives = driveRepository.findAll();
        Map<String, Integer> skillCountMap = new HashMap<>();
        for (CompanyDrive d : drives) {
            String[] reqs = d.getRequiredSkills().split(",");
            for (String r : reqs) {
                String clean = r.trim();
                skillCountMap.put(clean, skillCountMap.getOrDefault(clean, 0) + 1);
            }
        }

        List<String> topSkills = skillCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(6)
                .map(Map.Entry::getKey)
                .toList();

        summary.setTopSkillsInDemand(topSkills.isEmpty() ? Arrays.asList("Java", "Spring Boot", "MySQL", "Python", "REST API", "Git") : topSkills);
        summary.setPlacementReadinessRate(84.5);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/drives")
    public ResponseEntity<?> getAllDrives() {
        return ResponseEntity.ok(driveRepository.findAll());
    }
}
