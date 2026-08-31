from fastapi import FastAPI, UploadFile, File, Form, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
import os
import shutil
import tempfile

from parser_engine import parse_resume_content
from gemini_service import generate_mcq_assessment, evaluate_candidate_readiness

app = FastAPI(
    title="Placement Monitoring NLP & Gemini Microservice",
    version="1.0.0",
    description="NLP Resume Parsing, Skill Extraction, and AI MCQ Assessment Engine"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class TextParseRequest(BaseModel):
    rawText: str

class AssessmentRequest(BaseModel):
    targetSkills: List[str]
    roleTitle: Optional[str] = "Software Engineer"
    numQuestions: Optional[int] = 5

class ReadinessRequest(BaseModel):
    skills: List[str]
    scorePercentage: float

@app.get("/health")
def health_check():
    return {
        "status": "UP",
        "service": "Placement NLP & Gemini API Engine",
        "nlpPipeline": "spaCy + NLTK",
        "version": "1.0.0"
    }

@app.post("/parse-resume-text")
def parse_resume_text_endpoint(req: TextParseRequest):
    result = parse_resume_content(raw_text=req.rawText)
    return result

@app.post("/parse-resume-file")
async def parse_resume_file_endpoint(file: UploadFile = File(...)):
    suffix = os.path.splitext(file.filename)[1]
    with tempfile.NamedTemporaryFile(delete=False, suffix=suffix) as tmp:
        shutil.copyfileobj(file.file, tmp)
        tmp_path = tmp.name

    try:
        result = parse_resume_content(file_path=tmp_path)
    finally:
        if os.path.exists(tmp_path):
            os.remove(tmp_path)
            
    return result

@app.post("/generate-assessment")
def generate_assessment_endpoint(req: AssessmentRequest):
    result = generate_mcq_assessment(
        target_skills=req.targetSkills,
        role_title=req.roleTitle,
        num_questions=req.numQuestions
    )
    return result

@app.post("/evaluate-readiness")
def evaluate_readiness_endpoint(req: ReadinessRequest):
    result = evaluate_candidate_readiness(
        skills=req.skills,
        score_pct=req.scorePercentage
    )
    return result

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app:app", host="0.0.0.0", port=5000, reload=True)
