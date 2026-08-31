# 🎓 AI-Powered Placement Monitoring & Assessment System

A full-stack Spring Boot application for campus placement management with AI-powered skill matching, technical assessments (30 MCQs + DSA coding challenges), and automated email notifications.

## ✨ Features

- **Multi-role Authentication** – Student, Recruiter (Company), Admin accounts with JWT
- **AI Resume Parsing** – NLP-powered skill extraction from resume text
- **Smart Job Matching** – Jaccard similarity-based skill-to-job matching with % scores
- **Unified Assessment Engine** – 30 technical MCQs + integrated DSA coding challenge
- **Composite Scoring** – 70% MCQ + 30% DSA weighted overall score
- **DSA Practice Module** – 8 problems with automated test case evaluation
- **Email Notifications** – HTML emails sent to both student and company recruiter after assessment
- **Recruiter Dashboard** – Post drives, view applicants and their scores
- **Admin Analytics** – Placement statistics and overview

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2, Spring Security, JWT |
| Database | H2 (in-memory), Spring Data JPA / Hibernate |
| Email | Spring Mail (JavaMailSender), Gmail SMTP |
| Frontend | Vanilla HTML/CSS/JS (served as static resources) |
| Build | Maven (mvnw wrapper) |

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven (or use the included `mvnw` wrapper)

### Run the App

```bash
cd backend
./mvnw spring-boot:run      # Linux/Mac
.\mvnw.cmd spring-boot:run  # Windows
```

App runs at: **http://localhost:8080**

### Default Demo Accounts

| Role | Username | Password |
|---|---|---|
| Student | `student` | `student123` |
| Student 2 | `priya_student` | `student123` |
| Recruiter (TechCorp) | `recruiter` | `recruiter123` |
| Recruiter (Google) | `google_hr` | `company123` |
| Admin | `admin` | `admin123` |

## 📧 Email Configuration

To enable real email delivery, update `backend/src/main/resources/application.properties`:

```properties
spring.mail.username=YOUR_GMAIL@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
```

> Get a Gmail App Password at: https://myaccount.google.com/apppasswords  
> (Requires 2-Step Verification to be enabled)

After a student submits an assessment:
- 📩 **Student** receives their score report with MCQ + DSA breakdown
- 📩 **Company Recruiter** receives the full candidate assessment report with hire recommendation

## 📁 Project Structure

```
backend/
├── src/main/java/com/placement/app/
│   ├── config/          # Security, JWT, DataInitializer
│   ├── controller/      # AuthController, StudentController, RecruiterController
│   ├── entity/          # JPA Entities (User, Assessment, Resume, etc.)
│   ├── repository/      # Spring Data JPA repositories
│   ├── service/         # EmailNotificationService, NlpClientService, SkillMatchService
│   └── dto/             # PlacementDTOs
├── src/main/resources/
│   ├── static/          # index.html, app.js, styles.css (Frontend)
│   └── application.properties
└── pom.xml
```

## 📊 Assessment Scoring

| Component | Weight | Details |
|---|---|---|
| 30 Technical MCQs | 70% | Java, Spring Boot, SQL, DSA, Python |
| DSA Coding Challenge | 30% | Automated test case evaluation |
| **Overall Score** | **100%** | Composite weighted score |
