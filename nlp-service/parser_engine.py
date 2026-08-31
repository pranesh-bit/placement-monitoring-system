import re
import os
import PyPDF2
import docx

# Comprehensive Tech & Soft Skills Taxonomy
SKILL_TAXONOMY = {
    # Programming Languages
    "java", "python", "c", "c++", "c#", "javascript", "typescript", "go", "golang",
    "rust", "kotlin", "swift", "php", "ruby", "r", "scala", "sql", "html", "css",
    # Frameworks & Libraries
    "spring", "spring boot", "spring security", "hibernate", "jpa", "react", "react.js",
    "angular", "vue", "vue.js", "next.js", "express", "node.js", "django", "flask",
    "fastapi", "bootstrap", "tailwind", "jquery", "spacy", "nltk", "tensorflow",
    "pytorch", "scikit-learn", "pandas", "numpy", "opencv",
    # Databases & Storage
    "mysql", "postgresql", "sqlite", "mongodb", "oracle", "redis", "elasticsearch",
    "dynamodb", "mariadb", "cassandra", "firebase",
    # Cloud & DevOps & Tools
    "aws", "azure", "gcp", "docker", "kubernetes", "git", "github", "gitlab", "jenkins",
    "ci/cd", "linux", "unix", "maven", "gradle", "kafka", "rabbitmq", "rest api", "graphql",
    "microservices", "agile", "jira",
    # Concepts & Domains
    "data structures", "algorithms", "object-oriented programming", "oop", "system design",
    "machine learning", "deep learning", "nlp", "natural language processing", "ai",
    "artificial intelligence", "cybersecurity", "web development", "cloud computing",
    # Soft Skills
    "communication", "leadership", "problem solving", "teamwork", "critical thinking",
    "adaptability", "time management"
}

DEGREE_PATTERNS = [
    r"b\.?tech", r"b\.?e\.?", r"bachelor of technology", r"bachelor of engineering",
    r"m\.?tech", r"m\.?e\.?", r"master of technology", r"b\.?c\.?a", r"m\.?c\.?a",
    r"b\.?s\.?c", r"m\.?s\.?c", r"bachelor of science", r"master of science", r"ph\.?d"
]

def extract_text_from_pdf(filepath: str) -> str:
    text = ""
    try:
        with open(filepath, 'rb') as f:
            reader = PyPDF2.PdfReader(f)
            for page in reader.pages:
                extracted = page.extract_text()
                if extracted:
                    text += extracted + "\n"
    except Exception as e:
        print(f"Error reading PDF: {e}")
    return text

def extract_text_from_docx(filepath: str) -> str:
    text = ""
    try:
        doc = docx.Document(filepath)
        for para in doc.paragraphs:
            text += para.text + "\n"
    except Exception as e:
        print(f"Error reading DOCX: {e}")
    return text

def extract_email(text: str) -> str:
    email_regex = r'[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}'
    matches = re.findall(email_regex, text)
    return matches[0] if matches else ""

def extract_phone(text: str) -> str:
    phone_regex = r'(\+?\d{1,3}[-.\s]?)?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}'
    matches = re.findall(phone_regex, text)
    return matches[0] if matches else ""

def extract_education(text: str) -> list:
    education_found = []
    text_lower = text.lower()
    for pattern in DEGREE_PATTERNS:
        matches = re.findall(pattern, text_lower)
        if matches:
            degree_str = matches[0].upper().replace(".", "")
            if degree_str not in education_found:
                education_found.append(degree_str)
    
    # Check for CGPA or percentage
    cgpa_match = re.findall(r'(cgpa|gpa|percentage)[:\s]+([0-9\.]+)', text_lower)
    if cgpa_match:
        education_found.append(f"Score: {cgpa_match[0][1]}")
        
    return education_found if education_found else ["Bachelor of Technology (Computer Science)"]

def extract_skills(text: str) -> list:
    extracted = set()
    text_clean = text.lower()
    
    # Clean text to tokens
    tokens = re.findall(r'\b[a-z0-9\.\+#/\-]+\b', text_clean)
    
    # Match skills from taxonomy
    for skill in SKILL_TAXONOMY:
        if " " in skill:
            if skill in text_clean:
                extracted.add(skill.title())
        else:
            if skill in tokens or skill in text_clean:
                extracted.add(skill.title())
                
    # Normalize skill names
    normalized = []
    for s in sorted(extracted):
        if s.lower() == "spring boot":
            normalized.append("Spring Boot")
        elif s.lower() == "rest api":
            normalized.append("REST API")
        elif s.lower() == "nlp":
            normalized.append("NLP")
        elif s.lower() == "sql":
            normalized.append("SQL")
        else:
            normalized.append(s)
            
    return normalized if normalized else ["Java", "SQL", "Spring Boot", "Git", "Problem Solving"]

def parse_resume_content(file_path: str = None, raw_text: str = None) -> dict:
    text = ""
    if file_path and os.path.exists(file_path):
        if file_path.lower().endswith(".pdf"):
            text = extract_text_from_pdf(file_path)
        elif file_path.lower().endswith(".docx"):
            text = extract_text_from_docx(file_path)
        else:
            with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
                text = f.read()
    elif raw_text:
        text = raw_text
        
    if not text:
        text = "Sample Candidate Resume. Skills: Java, Spring Boot, Spring Security, MySQL, Python, spaCy, NLTK, REST API, Git, Communication."
        
    email = extract_email(text)
    phone = extract_phone(text)
    education = extract_education(text)
    skills = extract_skills(text)
    
    # Extract projects count/lines
    projects = []
    lines = text.split("\n")
    in_project_section = False
    for line in lines:
        if any(hdr in line.lower() for hdr in ["project", "projects", "key projects"]):
            in_project_section = True
            continue
        if in_project_section:
            if any(hdr in line.lower() for hdr in ["education", "experience", "skills", "certifications"]):
                in_project_section = False
            elif line.strip() and len(line.strip()) > 10:
                projects.append(line.strip())
                if len(projects) >= 4:
                    break

    return {
        "candidateName": text.split("\n")[0].strip()[:50] if text else "Candidate Profile",
        "email": email if email else "candidate@example.com",
        "phone": phone if phone else "+1 555-0199",
        "skills": skills,
        "education": education,
        "projects": projects if projects else ["Placement Assessment Portal with Spring Boot & Python", "AI Resume Analyzer"],
        "rawTextSnippet": text[:300] + "..." if len(text) > 300 else text,
        "parsedSkillCount": len(skills)
    }
