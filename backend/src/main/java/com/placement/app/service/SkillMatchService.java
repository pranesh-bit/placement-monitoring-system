package com.placement.app.service;

import com.placement.app.dto.PlacementDTOs.CompanyMatchResult;
import com.placement.app.entity.CompanyDrive;
import com.placement.app.entity.Resume;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillMatchService {

    public CompanyMatchResult calculateMatch(Resume resume, CompanyDrive drive) {
        CompanyMatchResult result = new CompanyMatchResult();
        result.setDriveId(drive.getId());
        result.setCompanyName(drive.getCompanyName());
        result.setRoleTitle(drive.getRoleTitle());
        result.setPackageLpa(drive.getPackageLpa());
        result.setLocation(drive.getLocation());

        // Parse candidate skills
        List<String> candidateSkills = parseSkills(resume != null ? resume.getSkillsJson() : "");
        // Parse required skills
        List<String> requiredSkills = parseSkills(drive.getRequiredSkills());

        result.setRequiredSkills(requiredSkills);

        Set<String> candidateSkillsLower = candidateSkills.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<String> matchingSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String req : requiredSkills) {
            boolean matched = false;
            String reqLower = req.toLowerCase();
            for (String cand : candidateSkillsLower) {
                if (cand.equals(reqLower) || cand.contains(reqLower) || reqLower.contains(cand)) {
                    matchingSkills.add(req);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                missingSkills.add(req);
            }
        }

        result.setMatchingSkills(matchingSkills);
        result.setMissingSkills(missingSkills);

        double percentage = requiredSkills.isEmpty() ? 100.0 : ((double) matchingSkills.size() / requiredSkills.size()) * 100.0;
        percentage = Math.round(percentage * 10.0) / 10.0;
        result.setMatchPercentage(percentage);

        if (percentage >= 75.0) {
            result.setMatchStatus("Highly Eligible");
        } else if (percentage >= 45.0) {
            result.setMatchStatus("Eligible");
        } else {
            result.setMatchStatus("Skill Gap Identified");
        }

        return result;
    }

    private List<String> parseSkills(String str) {
        if (str == null || str.trim().isEmpty()) return new ArrayList<>();
        // Remove json brackets if any
        String clean = str.replaceAll("[\\[\\]\"]", "");
        String[] parts = clean.split(",");
        List<String> list = new ArrayList<>();
        for (String p : parts) {
            if (!p.trim().isEmpty()) {
                list.add(p.trim());
            }
        }
        return list;
    }
}
