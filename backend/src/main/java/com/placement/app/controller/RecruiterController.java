package com.placement.app.controller;

import com.placement.app.dto.PlacementDTOs.*;
import com.placement.app.entity.*;
import com.placement.app.repository.*;
import com.placement.app.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterController {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final CompanyDriveRepository driveRepository;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentSubmissionRepository submissionRepository;
    private final SkillMatchService skillMatchService;
    private final NlpClientService nlpClientService;
    private final EmailNotificationService notificationService;

    public RecruiterController(UserRepository userRepository,
                               ResumeRepository resumeRepository,
                               CompanyDriveRepository driveRepository,
                               AssessmentRepository assessmentRepository,
                               AssessmentSubmissionRepository submissionRepository,
                               SkillMatchService skillMatchService,
                               NlpClientService nlpClientService,
                               EmailNotificationService notificationService) {
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.driveRepository = driveRepository;
        this.assessmentRepository = assessmentRepository;
        this.submissionRepository = submissionRepository;
        this.skillMatchService = skillMatchService;
        this.nlpClientService = nlpClientService;
        this.notificationService = notificationService;
    }

    @PostMapping("/drives/create")
    public ResponseEntity<?> createDrive(@RequestBody CompanyDrive drive, Principal principal) {
        User recruiter = userRepository.findByUsername(principal.getName()).orElseThrow();
        drive.setCreatedById(recruiter.getId());
        driveRepository.save(drive);

        // Auto-generate AI Assessment for Drive
        List<String> targetSkills = Arrays.asList(drive.getRequiredSkills().split(","));
        Map<String, Object> aiGen = nlpClientService.generateAssessment(targetSkills, drive.getRoleTitle());

        Assessment assessment = new Assessment();
        assessment.setDriveId(drive.getId());
        assessment.setRoleTitle(drive.getRoleTitle());
        assessment.setTargetSkills(drive.getRequiredSkills());

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            assessment.setQuestionsJson(mapper.writeValueAsString(aiGen.get("questions")));
        } catch (Exception e) {
            assessment.setQuestionsJson("[]");
        }

        assessment.setTotalQuestions(30);
        assessment.setDurationMinutes(35);
        assessment.setDsaTitle("Two Sum - Target Pair Indices");
        assessment.setDsaDifficulty("Easy");
        assessment.setDsaDescription("Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.");
        assessment.setDsaSampleInput("nums = [2, 7, 11, 15], target = 9");
        assessment.setDsaSampleOutput("[0, 1]");
        assessment.setDsaTestCasesJson("[{\"input\": \"nums = [2,7,11,15], target = 9\", \"expectedOutput\": \"[0, 1]\"}, {\"input\": \"nums = [3,2,4], target = 6\", \"expectedOutput\": \"[1, 2]\"}, {\"input\": \"nums = [3,3], target = 6\", \"expectedOutput\": \"[0, 1]\"}]");
        assessment.setDsaStarterCodeJava("class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Write Java solution\n        return new int[]{};\n    }\n}");
        assessment.setDsaStarterCodePython("def two_sum(nums, target):\n    # Write Python solution\n    pass");

        assessmentRepository.save(assessment);

        Map<String, Object> resp = new HashMap<>();
        resp.put("drive", drive);
        resp.put("assessmentId", assessment.getId());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/drives")
    public ResponseEntity<?> getMyDrives(Principal principal) {
        User recruiter = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<CompanyDrive> drives = driveRepository.findByCreatedById(recruiter.getId());
        if (drives.isEmpty()) {
            drives = driveRepository.findAll();
        }
        return ResponseEntity.ok(drives);
    }

    @GetMapping("/candidates/match/{driveId}")
    public ResponseEntity<?> getMatchingCandidatesForDrive(@PathVariable Long driveId) {
        CompanyDrive drive = driveRepository.findById(driveId).orElseThrow();
        List<Resume> resumes = resumeRepository.findAll();
        List<Map<String, Object>> candidatesList = new ArrayList<>();

        for (Resume r : resumes) {
            CompanyMatchResult match = skillMatchService.calculateMatch(r, drive);
            Map<String, Object> item = new HashMap<>();
            item.put("studentId", r.getUserId());
            item.put("candidateName", r.getCandidateName());
            item.put("email", r.getEmail());
            item.put("phone", r.getPhone());
            item.put("skills", r.getSkillsJson());
            item.put("matchPercentage", match.getMatchPercentage());
            item.put("matchStatus", match.getMatchStatus());
            item.put("matchingSkills", match.getMatchingSkills());
            item.put("missingSkills", match.getMissingSkills());

            // Check if student attempted assessment
            Optional<Assessment> assOpt = assessmentRepository.findByDriveId(driveId);
            if (assOpt.isPresent()) {
                Optional<AssessmentSubmission> subOpt = submissionRepository.findByAssessmentIdAndStudentId(assOpt.get().getId(), r.getUserId());
                if (subOpt.isPresent()) {
                    item.put("testScore", subOpt.get().getScore());
                    item.put("testPercentage", subOpt.get().getPercentage());
                    item.put("testStatus", "Completed");
                } else {
                    item.put("testStatus", "Pending");
                }
            } else {
                item.put("testStatus", "N/A");
            }

            candidatesList.add(item);
        }

        candidatesList.sort((a, b) -> Double.compare((Double) b.get("matchPercentage"), (Double) a.get("matchPercentage")));
        return ResponseEntity.ok(candidatesList);
    }

    @PostMapping("/notify-candidate")
    public ResponseEntity<?> notifyCandidate(@RequestParam String email, @RequestParam String candidateName, @RequestParam Long driveId) {
        CompanyDrive drive = driveRepository.findById(driveId).orElseThrow();
        Optional<Assessment> assOpt = assessmentRepository.findByDriveId(driveId);
        Long assId = assOpt.map(Assessment::getId).orElse(1L);

        notificationService.sendAssessmentInvite(email, candidateName, drive.getCompanyName(), drive.getRoleTitle(), assId);
        return ResponseEntity.ok(Map.of("message", "Email assessment invitation dispatched to " + email));
    }
}
