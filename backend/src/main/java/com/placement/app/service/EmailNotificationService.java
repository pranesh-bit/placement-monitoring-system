package com.placement.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    // ─── Student: Assessment Invitation ─────────────────────────────────────────
    public void sendAssessmentInvite(String toEmail, String studentName, String companyName,
                                      String roleTitle, Long assessmentId) {
        String subject = "🎯 Placement Assessment Invitation: " + companyName + " – " + roleTitle;
        String html = buildInviteHtml(studentName, companyName, roleTitle, assessmentId);
        logAndSend(toEmail, subject, html);
    }

    // ─── Student: Assessment Result ──────────────────────────────────────────────
    public void sendAssessmentResultNotification(String toEmail, String studentName, String roleTitle,
                                                  double overall, double mcqPct, int mcqScore, int mcqTotal,
                                                  double dsaPct, int dsaPass, int dsaTotal, String dsaStatus,
                                                  String readinessLevel, String aiFeedback) {
        String subject = "📊 Your Assessment Result – " + roleTitle + " | Score: " + String.format("%.1f", overall) + "%";
        String html = buildStudentResultHtml(studentName, roleTitle, overall, mcqPct, mcqScore, mcqTotal,
                dsaPct, dsaPass, dsaTotal, dsaStatus, readinessLevel, aiFeedback);
        logAndSend(toEmail, subject, html);
    }

    // ─── Company/Recruiter: Candidate Score Report ────────────────────────────────
    public void sendCompanyScoreReport(String recruiterEmail, String recruiterName, String companyName,
                                        String studentName, String studentEmail, String roleTitle,
                                        double overall, double mcqPct, int mcqScore, int mcqTotal,
                                        double dsaPct, int dsaPass, int dsaTotal, String dsaStatus,
                                        String readinessLevel, String aiFeedback) {
        String subject = "📋 Candidate Report: " + studentName + " – " + roleTitle + " (" + String.format("%.1f", overall) + "%)";
        String html = buildCompanyReportHtml(recruiterName, companyName, studentName, studentEmail, roleTitle,
                overall, mcqPct, mcqScore, mcqTotal, dsaPct, dsaPass, dsaTotal, dsaStatus, readinessLevel, aiFeedback);
        logAndSend(recruiterEmail, subject, html);
    }

    // ─── Core Send Logic ─────────────────────────────────────────────────────────
    private void logAndSend(String to, String subject, String htmlBody) {
        System.out.println("\n==================================================");
        System.out.println("📧 AUTOMATED EMAIL NOTIFICATION");
        System.out.println("   To      : " + to);
        System.out.println("   Subject : " + subject);
        System.out.println("==================================================\n");

        if (mailSender != null) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setFrom("noreply@placementportal.ai");
                helper.setText(htmlBody, true);
                mailSender.send(message);
                System.out.println("✅ Email sent successfully to: " + to);
            } catch (Exception e) {
                System.err.println("⚠️  SMTP send failed: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️  JavaMailSender not configured – email logged only.");
        }
    }

    // ─── HTML: Student Invite ────────────────────────────────────────────────────
    private String buildInviteHtml(String name, String company, String role, Long id) {
        return "<!DOCTYPE html><html><body style='font-family:Arial,sans-serif;background:#0f172a;color:#e2e8f0;margin:0;padding:20px'>" +
            "<div style='max-width:580px;margin:auto;background:#1e293b;border-radius:16px;overflow:hidden'>" +
            "<div style='background:linear-gradient(135deg,#6366f1,#8b5cf6);padding:30px;text-align:center'>" +
            "<h1 style='margin:0;color:#fff;font-size:22px'>🎯 Assessment Invitation</h1>" +
            "<p style='margin:8px 0 0;color:rgba(255,255,255,0.8)'>" + company + " &bull; " + role + "</p></div>" +
            "<div style='padding:30px'><p>Dear <strong>" + name + "</strong>,</p>" +
            "<p>You have been <strong>shortlisted</strong> for the <strong>" + company + "</strong> placement drive for the role of <strong>" + role + "</strong>.</p>" +
            "<p>Please log into the <strong>Placement Portal</strong> and complete your AI-Powered Technical Assessment (Test ID: <strong>#" + id + "</strong>).</p>" +
            "<div style='background:#0f172a;border-radius:10px;padding:16px;margin:20px 0;border-left:4px solid #6366f1'>" +
            "<p style='margin:0;color:#94a3b8;font-size:13px'>&#128204; Assessment includes 30 Technical MCQs + 1 DSA Coding Challenge</p></div>" +
            "<p style='color:#94a3b8;font-size:13px'>Best of luck!<br/><strong>Placement &amp; Training Cell</strong></p></div></div></body></html>";
    }

    // ─── HTML: Student Result ────────────────────────────────────────────────────
    private String buildStudentResultHtml(String name, String role, double overall,
                                           double mcqPct, int mcqScore, int mcqTotal,
                                           double dsaPct, int dsaPass, int dsaTotal,
                                           String dsaStatus, String readiness, String feedback) {
        String oc = overall >= 75 ? "#22c55e" : overall >= 50 ? "#f59e0b" : "#ef4444";
        String dc = "Accepted".equals(dsaStatus) ? "#22c55e" : "Partially Accepted".equals(dsaStatus) ? "#f59e0b" : "#ef4444";
        return "<!DOCTYPE html><html><body style='font-family:Arial,sans-serif;background:#0f172a;color:#e2e8f0;margin:0;padding:20px'>" +
            "<div style='max-width:580px;margin:auto;background:#1e293b;border-radius:16px;overflow:hidden'>" +
            "<div style='background:linear-gradient(135deg,#6366f1,#8b5cf6);padding:30px;text-align:center'>" +
            "<h1 style='margin:0;color:#fff;font-size:22px'>&#128202; Assessment Result</h1>" +
            "<p style='margin:8px 0 0;color:rgba(255,255,255,0.8)'>" + role + "</p></div>" +
            "<div style='padding:30px'><p>Dear <strong>" + name + "</strong>,</p>" +
            "<p>Your technical assessment has been evaluated. Here are your results:</p>" +
            "<div style='text-align:center;background:#0f172a;border-radius:14px;padding:24px;margin:20px 0'>" +
            "<div style='font-size:52px;font-weight:900;color:" + oc + "'>" + String.format("%.1f", overall) + "%</div>" +
            "<div style='color:#94a3b8;margin-top:4px'>Overall Composite Score</div>" +
            "<div style='display:inline-block;background:" + oc + "33;color:" + oc + ";padding:5px 14px;border-radius:20px;font-weight:700;margin-top:10px;font-size:13px'>" + readiness + "</div></div>" +
            "<table style='width:100%;border-collapse:collapse;margin:16px 0'>" +
            "<tr style='background:#0f172a'>" +
            "<th style='padding:12px;text-align:left;color:#94a3b8;font-size:11px;text-transform:uppercase'>Section</th>" +
            "<th style='padding:12px;text-align:center;color:#94a3b8;font-size:11px;text-transform:uppercase'>Score</th>" +
            "<th style='padding:12px;text-align:center;color:#94a3b8;font-size:11px;text-transform:uppercase'>%</th>" +
            "<th style='padding:12px;text-align:center;color:#94a3b8;font-size:11px;text-transform:uppercase'>Weight</th></tr>" +
            "<tr style='border-bottom:1px solid #334155'>" +
            "<td style='padding:12px'>&#128221; 30 MCQs</td>" +
            "<td style='padding:12px;text-align:center'>" + mcqScore + "/" + mcqTotal + "</td>" +
            "<td style='padding:12px;text-align:center;color:#6366f1;font-weight:700'>" + String.format("%.1f", mcqPct) + "%</td>" +
            "<td style='padding:12px;text-align:center;color:#94a3b8'>70%</td></tr>" +
            "<tr><td style='padding:12px'>&#128187; DSA Coding</td>" +
            "<td style='padding:12px;text-align:center'>" + dsaPass + "/" + dsaTotal + " tests</td>" +
            "<td style='padding:12px;text-align:center;color:" + dc + ";font-weight:700'>" + String.format("%.1f", dsaPct) + "% &bull; " + dsaStatus + "</td>" +
            "<td style='padding:12px;text-align:center;color:#94a3b8'>30%</td></tr></table>" +
            "<div style='background:#0f172a;border-radius:10px;padding:16px;border-left:4px solid #6366f1;margin:20px 0'>" +
            "<p style='margin:0 0 6px;color:#6366f1;font-weight:700;font-size:12px'>&#129302; AI PERFORMANCE FEEDBACK</p>" +
            "<p style='margin:0;color:#cbd5e1;font-size:13px;line-height:1.6'>" + feedback + "</p></div>" +
            "<p style='color:#94a3b8;font-size:13px'>Log in to view your detailed breakdown.<br/><strong>Placement Cell &ndash; AI Assessment Engine</strong></p>" +
            "</div></div></body></html>";
    }

    // ─── HTML: Company Recruiter Report ──────────────────────────────────────────
    private String buildCompanyReportHtml(String recruiterName, String company, String studentName,
                                           String studentEmail, String role, double overall,
                                           double mcqPct, int mcqScore, int mcqTotal,
                                           double dsaPct, int dsaPass, int dsaTotal,
                                           String dsaStatus, String readiness, String feedback) {
        String oc = overall >= 75 ? "#22c55e" : overall >= 50 ? "#f59e0b" : "#ef4444";
        String dc = "Accepted".equals(dsaStatus) ? "#22c55e" : "Partially Accepted".equals(dsaStatus) ? "#f59e0b" : "#ef4444";
        String rec = overall >= 80 ? "&#11088; Strongly Recommended for Interview"
                   : overall >= 60 ? "&#9989; Recommended for Next Round"
                   : overall >= 40 ? "&#128310; Borderline – Review Manually" : "&#10060; Not Recommended";
        return "<!DOCTYPE html><html><body style='font-family:Arial,sans-serif;background:#0f172a;color:#e2e8f0;margin:0;padding:20px'>" +
            "<div style='max-width:620px;margin:auto;background:#1e293b;border-radius:16px;overflow:hidden'>" +
            "<div style='background:linear-gradient(135deg,#0f172a,#1e3a5f);padding:30px;border-bottom:2px solid #334155'>" +
            "<h1 style='margin:0;color:#fff;font-size:20px'>&#128203; Candidate Assessment Report</h1>" +
            "<p style='margin:8px 0 0;color:#94a3b8'>" + company + " &bull; " + role + "</p></div>" +
            "<div style='padding:30px'><p>Dear <strong>" + recruiterName + "</strong>,</p>" +
            "<p>A candidate has completed the technical assessment for the <strong>" + role + "</strong> position:</p>" +
            "<div style='background:#0f172a;border-radius:12px;padding:18px;margin:16px 0'>" +
            "<div style='font-size:17px;font-weight:700;color:#fff'>&#128100; " + studentName + "</div>" +
            "<div style='color:#94a3b8;font-size:13px;margin-top:4px'>&#9993; " + studentEmail + "</div></div>" +
            "<div style='text-align:center;background:#0f172a;border-radius:14px;padding:24px;margin:16px 0'>" +
            "<div style='font-size:48px;font-weight:900;color:" + oc + "'>" + String.format("%.1f", overall) + "%</div>" +
            "<div style='color:#94a3b8;margin-top:4px'>Overall Composite Score</div>" +
            "<div style='margin-top:12px;font-size:15px;font-weight:700;color:" + oc + "'>" + rec + "</div></div>" +
            "<table style='width:100%;border-collapse:collapse;margin:16px 0'>" +
            "<tr style='background:#0f172a'>" +
            "<th style='padding:12px;text-align:left;color:#94a3b8;font-size:11px;text-transform:uppercase'>Section</th>" +
            "<th style='padding:12px;text-align:center;color:#94a3b8;font-size:11px;text-transform:uppercase'>Score</th>" +
            "<th style='padding:12px;text-align:center;color:#94a3b8;font-size:11px;text-transform:uppercase'>Result</th></tr>" +
            "<tr style='border-bottom:1px solid #334155'>" +
            "<td style='padding:12px'>&#128221; 30 Technical MCQs</td>" +
            "<td style='padding:12px;text-align:center'>" + mcqScore + "/" + mcqTotal + "</td>" +
            "<td style='padding:12px;text-align:center;color:#6366f1;font-weight:700'>" + String.format("%.1f", mcqPct) + "%</td></tr>" +
            "<tr><td style='padding:12px'>&#128187; DSA Coding Challenge</td>" +
            "<td style='padding:12px;text-align:center'>" + dsaPass + "/" + dsaTotal + " test cases</td>" +
            "<td style='padding:12px;text-align:center;color:" + dc + ";font-weight:700'>" + String.format("%.1f", dsaPct) + "% &bull; " + dsaStatus + "</td></tr></table>" +
            "<div style='display:flex;gap:10px;margin:16px 0'>" +
            "<div style='flex:1;background:#6366f122;border:1px solid #6366f155;border-radius:10px;padding:14px;text-align:center'>" +
            "<div style='color:#94a3b8;font-size:11px;text-transform:uppercase;margin-bottom:4px'>Readiness Level</div>" +
            "<div style='color:#6366f1;font-weight:700'>" + readiness + "</div></div></div>" +
            "<div style='background:#0f172a;border-radius:10px;padding:16px;border-left:4px solid #6366f1;margin:16px 0'>" +
            "<p style='margin:0 0 6px;color:#6366f1;font-weight:700;font-size:12px'>&#129302; AI EVALUATION SUMMARY</p>" +
            "<p style='margin:0;color:#cbd5e1;font-size:13px;line-height:1.6'>" + feedback + "</p></div>" +
            "<p style='color:#94a3b8;font-size:13px'>Log into the Recruiter Dashboard to view all submissions.<br/><strong>AI Placement Monitoring System</strong></p>" +
            "</div></div></body></html>";
    }
}

