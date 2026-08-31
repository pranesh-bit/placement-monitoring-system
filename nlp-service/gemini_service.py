import os
import json
import requests

GEMINI_API_KEY = os.environ.get("GEMINI_API_KEY", "")

HEURISTIC_MCQ_BANK = {
    "Java": [
        {
            "id": 1,
            "question": "What is the primary difference between JVM, JRE, and JDK in Java?",
            "options": [
                "JDK includes JRE and development tools; JRE contains JVM and runtime libraries.",
                "JVM compiles Java code to bytecode; JDK executes bytecode.",
                "JRE is used only for web applications; JVM is for desktop apps.",
                "JDK is purely a compiler without any runtime environment."
            ],
            "correctAnswerIndex": 0,
            "explanation": "JDK (Java Development Kit) is a full feature SDK containing JRE and development tools."
        },
        {
            "id": 2,
            "question": "In Java 8+, how does stream().filter() differ from stream().map()?",
            "options": [
                "filter() transforms elements while map() selects matching elements.",
                "filter() evaluates a predicate to retain elements; map() applies a mapper function to transform elements.",
                "filter() runs asynchronously while map() runs synchronously.",
                "Both functions produce identical side effects on collection items."
            ],
            "correctAnswerIndex": 1,
            "explanation": "filter() filters elements based on a boolean predicate. map() transforms each element to another value."
        },
        {
            "id": 3,
            "question": "What is the purpose of the `volatile` keyword in Java multi-threading?",
            "options": [
                "It ensures variable reads and writes go directly to main memory rather than CPU cache.",
                "It locks the entire class preventing concurrent access.",
                "It automatically converts standard types to AtomicInteger.",
                "It serializes object state to disk."
            ],
            "correctAnswerIndex": 0,
            "explanation": "`volatile` guarantees visibility of changes to variables across threads."
        },
        {
            "id": 4,
            "question": "What is the difference between HashMap and ConcurrentHashMap in Java?",
            "options": [
                "HashMap is synchronized per entry; ConcurrentHashMap locks the entire map.",
                "ConcurrentHashMap uses bucket/segment locking and thread-safe lock-free reads, whereas HashMap is not thread-safe.",
                "HashMap allows concurrent modifications without throwing ConcurrentModificationException.",
                "They have identical concurrency guarantees."
            ],
            "correctAnswerIndex": 1,
            "explanation": "ConcurrentHashMap offers thread safety with concurrent entry-level access, unlike standard non-thread-safe HashMap."
        },
        {
            "id": 5,
            "question": "What happens when an exception is thrown inside a `try-with-resources` block in Java?",
            "options": [
                "Resources are closed automatically in reverse order before the exception bubbles up.",
                "Resources remain open until garbage collection.",
                "The application terminates immediately with a Segmentation Fault.",
                "The exception is silently swallowed."
            ],
            "correctAnswerIndex": 0,
            "explanation": "`AutoCloseable` resources declared in try-with-resources are automatically closed in reverse order."
        }
    ],
    "Spring Boot": [
        {
            "id": 6,
            "question": "What is the role of `@SpringBootApplication` annotation?",
            "options": [
                "It enables Security JWT authentication across controllers.",
                "It combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`.",
                "It forces Spring Boot to connect exclusively to SQLite databases.",
                "It compiles HTML templates to static web pages."
            ],
            "correctAnswerIndex": 1,
            "explanation": "`@SpringBootApplication` combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`."
        },
        {
            "id": 7,
            "question": "How does Spring Security handle stateless REST API authentication?",
            "options": [
                "Using HTTP Session cookies stored on server memory.",
                "Using JWT (JSON Web Tokens) validated per request in a custom filter.",
                "Using static hardcoded basic auth header strings in HTML forms.",
                "Disabling security checks for all non-GET requests."
            ],
            "correctAnswerIndex": 1,
            "explanation": "Stateless REST APIs use JWT headers verified by a Security Filter without creating HTTP Sessions."
        },
        {
            "id": 8,
            "question": "In Spring Data JPA, what does the `@Transactional` annotation guarantee?",
            "options": [
                "ACID transaction properties across database operations with automatic rollback on runtime exceptions.",
                "It converts all SQL queries to REST API calls.",
                "It forces in-memory caching of all database rows indefinitely.",
                "It encrypts database passwords."
            ],
            "correctAnswerIndex": 0,
            "explanation": "`@Transactional` manages transaction boundaries ensuring atomic DB commits or rollbacks."
        },
        {
            "id": 9,
            "question": "What is Dependency Injection (DI) and Inversion of Control (IoC) in Spring Framework?",
            "options": [
                "Spring IoC container instantiates, configures, and manages object lifecycles and injects dependencies.",
                "The developer manually instantiates objects using `new` operator inside controllers.",
                "DI compiles Java code into C dynamic libraries.",
                "DI is only applicable to frontend Javascript code."
            ],
            "correctAnswerIndex": 0,
            "explanation": "Spring IoC container controls bean lifecycles and injects dependencies automatically."
        },
        {
            "id": 10,
            "question": "What is the difference between `@Controller` and `@RestController` in Spring Boot?",
            "options": [
                " `@RestController` combines `@Controller` and `@ResponseBody`, serializing return values directly to JSON/XML.",
                "`@Controller` can only handle POST requests while `@RestController` handles GET requests.",
                "`@RestController` renders JSP view templates automatically.",
                "They are identical alias annotations with no difference."
            ],
            "correctAnswerIndex": 0,
            "explanation": "`@RestController` automatically adds `@ResponseBody` to all handler methods for REST endpoints."
        }
    ],
    "SQL": [
        {
            "id": 11,
            "question": "What is the difference between `INNER JOIN` and `LEFT JOIN` in SQL?",
            "options": [
                "INNER JOIN returns matching rows in both tables; LEFT JOIN returns all rows from the left table and matched rows from the right.",
                "LEFT JOIN is faster than INNER JOIN in all databases.",
                "INNER JOIN creates new tables while LEFT JOIN deletes duplicates.",
                "They are syntactically identical and produce identical result sets."
            ],
            "correctAnswerIndex": 0,
            "explanation": "INNER JOIN requires matches in both tables. LEFT JOIN keeps all left rows."
        },
        {
            "id": 12,
            "question": "Why are B-Tree indexes used in SQL databases?",
            "options": [
                "To reduce lookup time complexity from O(N) full table scans to O(log N).",
                "To automatically encrypt sensitive credit card columns.",
                "To allow storing JSON strings in relational tables.",
                "To compress disk storage size by 90%."
            ],
            "correctAnswerIndex": 0,
            "explanation": "Indexes speed up search queries from linear O(N) scans to logarithmic O(log N) lookups."
        },
        {
            "id": 13,
            "question": "What does the ACID acronym stand for in database transactions?",
            "options": [
                "Atomicity, Consistency, Isolation, Durability.",
                "Asynchronous, Concurrent, Indexed, Distributed.",
                "Authentication, Control, Integrity, Data.",
                "Automated, Compiled, Isolated, Decoupled."
            ],
            "correctAnswerIndex": 0,
            "explanation": "ACID ensures database transaction reliability: Atomicity, Consistency, Isolation, Durability."
        },
        {
            "id": 14,
            "question": "What is the difference between `WHERE` and `HAVING` clauses in SQL?",
            "options": [
                "WHERE filters rows before aggregation; HAVING filters groups after GROUP BY aggregation.",
                "HAVING is used for sorting while WHERE is used for joins.",
                "WHERE can only be used with primary keys.",
                "They are completely interchangeable."
            ],
            "correctAnswerIndex": 0,
            "explanation": "WHERE filters individual rows before grouping. HAVING filters aggregated group results."
        }
    ],
    "Data Structures & Algorithms": [
        {
            "id": 15,
            "question": "What is the time complexity of searching for an element in a balanced Binary Search Tree (BST)?",
            "options": [
                "O(log N)",
                "O(N^2)",
                "O(1)",
                "O(N log N)"
            ],
            "correctAnswerIndex": 0,
            "explanation": "In a balanced BST, tree height is log N, yielding O(log N) lookup time."
        },
        {
            "id": 16,
            "question": "Which data structure is ideal for implementing Breadth-First Search (BFS) graph traversal?",
            "options": [
                "Queue (FIFO)",
                "Stack (LIFO)",
                "PriorityQueue (Min Heap)",
                "Binary Max Tree"
            ],
            "correctAnswerIndex": 0,
            "explanation": "BFS uses a Queue (First-In, First-Out) to process nodes level by level."
        },
        {
            "id": 17,
            "question": "What is the worst-case time complexity of QuickSort when bad pivot selection occurs?",
            "options": [
                "O(N^2)",
                "O(N log N)",
                "O(N)",
                "O(log N)"
            ],
            "correctAnswerIndex": 0,
            "explanation": "Worst case QuickSort (e.g., sorted array with first element as pivot) degrades to O(N^2)."
        },
        {
            "id": 18,
            "question": "What technique uses two pointers moving from opposite ends of a sorted array to find a target sum?",
            "options": [
                "Two Pointers Pattern",
                "Sliding Window Pattern",
                "Monotonic Stack Pattern",
                "Floyd's Cycle Detection"
            ],
            "correctAnswerIndex": 0,
            "explanation": "Two Pointers starting at left and right ends converge to target sum in O(N) time."
        }
    ],
    "Python & Machine Learning": [
        {
            "id": 19,
            "question": "In Python, what is the key difference between a List and a Tuple?",
            "options": [
                "Lists are mutable (modifiable); Tuples are immutable (read-only after creation).",
                "Tuples store strings while lists store numbers.",
                "Lists use parentheses `()` while tuples use square brackets `[]`.",
                "Lists cannot contain duplicate values."
            ],
            "correctAnswerIndex": 0,
            "explanation": "Lists are mutable sequences `[]`; Tuples are immutable sequences `()`."
        },
        {
            "id": 20,
            "question": "In NLP, what is the role of Lemmatization compared to Stemming?",
            "options": [
                "Lemmatization reduces words to valid dictionary base forms (lemmas) using morphological analysis, while Stemming chops suffixes heuristically.",
                "Stemming produces real dictionary words while Lemmatization creates arbitrary prefixes.",
                "Lemmatization counts word frequencies while Stemming extracts sentiment.",
                "Both algorithms produce identical tokens."
            ],
            "correctAnswerIndex": 0,
            "explanation": "Lemmatization uses vocabulary and context analysis to return true dictionary roots."
        }
    ]
}

def generate_mcq_assessment(target_skills: list, role_title: str = "Software Development Engineer", num_questions: int = 6) -> dict:
    if GEMINI_API_KEY:
        try:
            url = f"https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key={GEMINI_API_KEY}"
            prompt = f"""
            Generate a JSON list of {num_questions} multiple choice questions (MCQs) evaluating candidate proficiency in: {', '.join(target_skills)} for role: {role_title}.
            Return JSON array only:
            [
              {{
                "id": 1,
                "question": "...",
                "options": ["A", "B", "C", "D"],
                "correctAnswerIndex": 0,
                "explanation": "..."
              }}
            ]
            """
            payload = {"contents": [{"parts": [{"text": prompt}]}]}
            resp = requests.post(url, json=payload, timeout=8)
            if resp.status_code == 200:
                raw_text = resp.json()['candidates'][0]['content']['parts'][0]['text']
                json_str = raw_text[raw_text.find('['):raw_text.rfind(']')+1]
                questions = json.loads(json_str)
                return {
                    "source": "Gemini 1.5 Flash AI Engine",
                    "roleTitle": role_title,
                    "targetSkills": target_skills,
                    "totalQuestions": len(questions),
                    "questions": questions
                }
        except Exception as e:
            print(f"Gemini API fallback: {e}")

    # Heuristic Fallback Generator
    selected_questions = []
    qid = 1
    for bank_skill, q_list in HEURISTIC_MCQ_BANK.items():
        for q in q_list:
            q_copy = dict(q)
            q_copy["id"] = qid
            selected_questions.append(q_copy)
            qid += 1

    return {
        "source": "Placement AI Assessment Generator Engine",
        "roleTitle": role_title,
        "targetSkills": target_skills,
        "totalQuestions": len(selected_questions[:num_questions]),
        "questions": selected_questions[:num_questions]
    }

def evaluate_candidate_readiness(skills: list, score_pct: float) -> dict:
    readiness_level = "High" if score_pct >= 80 else ("Medium" if score_pct >= 50 else "Development Needed")
    strengths = [s for s in skills if s in ["Java", "Spring Boot", "SQL", "Python"]]
    recommendations = []
    
    if score_pct < 80:
        recommendations.append("Practice system design & advanced Spring Security filter chains.")
        recommendations.append("Review REST API status codes and JPA query performance optimization.")
    else:
        recommendations.append("Ready for senior technical interviews and live coding rounds.")
        recommendations.append("Focus on cloud deployment (Docker, Kubernetes) and microservice architecture.")
        
    return {
        "readinessScore": score_pct,
        "readinessLevel": readiness_level,
        "strengths": strengths if strengths else skills[:3],
        "improvementAreas": recommendations,
        "aiFeedback": f"Candidate demonstrates {readiness_level.lower()} competency across target domain skills ({', '.join(skills[:4])}). Overall technical assessment accuracy: {score_pct:.1f}%."
    }
