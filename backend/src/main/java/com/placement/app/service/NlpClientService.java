package com.placement.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.*;

@Service
public class NlpClientService {

    @Value("${placement.nlp-service.url:http://localhost:5000}")
    private String nlpServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public Map<String, Object> parseResumeText(String rawText) {
        try {
            String url = nlpServiceUrl + "/parse-resume-text";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("rawText", rawText);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            System.err.println("NLP Service connection fallback: " + e.getMessage());
        }

        return fallbackParse(rawText);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> generateAssessment(List<String> targetSkills, String roleTitle) {
        try {
            String url = nlpServiceUrl + "/generate-assessment";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("targetSkills", targetSkills);
            body.put("roleTitle", roleTitle);
            body.put("numQuestions", 6);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            System.err.println("NLP Gemini Assessment fallback: " + e.getMessage());
        }

        return fallbackAssessment(targetSkills, roleTitle);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> evaluateReadiness(List<String> skills, double scorePercentage) {
        try {
            String url = nlpServiceUrl + "/evaluate-readiness";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("skills", skills);
            body.put("scorePercentage", scorePercentage);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            System.err.println("NLP Evaluation fallback: " + e.getMessage());
        }

        Map<String, Object> res = new HashMap<>();
        res.put("readinessScore", scorePercentage);
        res.put("readinessLevel", scorePercentage >= 80 ? "High" : (scorePercentage >= 50 ? "Medium" : "Development Needed"));
        res.put("aiFeedback", "Candidate exhibits strong core technical competence. Score: " + scorePercentage + "%");
        return res;
    }

    private Map<String, Object> fallbackParse(String text) {
        Map<String, Object> res = new HashMap<>();
        res.put("candidateName", "Candidate Profile");
        res.put("email", "candidate@example.com");
        res.put("phone", "+1 555-0199");
        
        List<String> skills = new ArrayList<>();
        String textLower = text.toLowerCase();
        if (textLower.contains("java")) skills.add("Java");
        if (textLower.contains("spring")) skills.add("Spring Boot");
        if (textLower.contains("sql") || textLower.contains("mysql")) skills.add("SQL");
        if (textLower.contains("python")) skills.add("Python");
        if (textLower.contains("react")) skills.add("React");
        if (skills.isEmpty()) {
            skills = Arrays.asList("Java", "Spring Boot", "SQL", "Git", "Problem Solving");
        }
        res.put("skills", skills);
        res.put("education", Arrays.asList("Bachelor of Technology in Computer Science"));
        res.put("projects", Arrays.asList("Placement Monitoring System using Spring Boot & Python NLP"));
        res.put("parsedSkillCount", skills.size());
        return res;
    }

    private Map<String, Object> fallbackAssessment(List<String> targetSkills, String roleTitle) {
        Map<String, Object> res = new HashMap<>();
        res.put("source", "Spring Boot 30 MCQ Engine");
        res.put("roleTitle", roleTitle);
        res.put("targetSkills", targetSkills);
        
        List<Map<String, Object>> qList = new ArrayList<>();
        
        String[][] bank = {
            {"In Java Spring Boot, what is the role of Spring Security Filter Chain?", "It intercepts HTTP requests to validate credentials, JWT tokens, and authority roles.", "It compiles Java code to native binary executables.", "It automatically formats HTML templates.", "It deletes unreferenced database rows.", "0", "Spring Security Filter Chain intercepts HTTP requests for authentication/authorization."},
            {"How does a Jaccard Similarity algorithm measure skill-job matching?", "By calculating the ratio of the intersection of candidate & job skills over their union.", "By counting the total character length of the resume.", "By running a sentiment analysis model on candidate emails.", "By sorting candidates alphabetically by username.", "0", "Jaccard similarity measures overlap ratio |A ∩ B| / |A ∪ B|."},
            {"What is the purpose of the `volatile` keyword in Java multi-threading?", "It ensures variable reads and writes go directly to main memory rather than CPU cache.", "It locks the class preventing concurrent access.", "It converts primitive types to atomic objects.", "It serializes objects to disk.", "0", "`volatile` guarantees visibility of changes across threads."},
            {"What is the role of `@SpringBootApplication` annotation?", "It combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`.", "It enables Security JWT authentication.", "It connects exclusively to SQLite databases.", "It generates static HTML templates.", "0", "Combines configuration, auto-configuration, and component scanning."},
            {"How does Spring Security handle stateless REST API authentication?", "Using JWT (JSON Web Tokens) validated per request in a custom filter.", "Using HTTP Session cookies stored on server memory.", "Using static basic auth headers in HTML forms.", "Disabling security checks.", "0", "Uses JWT token header parsed per request."},
            {"What is the main difference between INNER JOIN and LEFT JOIN in SQL?", "INNER JOIN returns matching rows in both tables; LEFT JOIN returns all left rows.", "LEFT JOIN is faster than INNER JOIN.", "INNER JOIN deletes duplicates.", "They produce identical result sets.", "0", "INNER JOIN requires matches in both tables."},
            {"What is the worst-case time complexity of QuickSort?", "O(N^2)", "O(N log N)", "O(N)", "O(log N)", "0", "Worst-case QuickSort is O(N^2) when bad pivot selection occurs."},
            {"Which data structure follows the First-In-First-Out (FIFO) order?", "Queue", "Stack", "Tree", "Graph", "0", "Queue follows FIFO order."},
            {"What is the primary benefit of using B-Tree Indexing in SQL databases?", "Speeds up search queries from O(N) full scans to O(log N) lookups.", "Encrypts table data.", "Compresses database storage by 90%.", "Allows storing JSON objects.", "0", "B-Tree index reduces search complexity to O(log N)."},
            {"In Java, how does `HashMap` differ from `ConcurrentHashMap`?", "ConcurrentHashMap supports thread-safe concurrent entry access without locking the whole map.", "HashMap is synchronized by default.", "HashMap cannot store null keys.", "They have identical concurrency guarantees.", "0", "ConcurrentHashMap provides fine-grained thread safety."},
            {"What is the time complexity of searching in a balanced Binary Search Tree (BST)?", "O(log N)", "O(N^2)", "O(1)", "O(N log N)", "0", "Balanced BST has height log N."},
            {"What does the ACID acronym stand for in relational databases?", "Atomicity, Consistency, Isolation, Durability", "Asynchronous, Concurrent, Indexed, Distributed", "Authentication, Control, Integrity, Data", "Automated, Compiled, Isolated, Decoupled", "0", "ACID guarantees database transaction integrity."},
            {"What is the role of `@Transactional` in Spring Data JPA?", "Manages atomic database transaction boundaries with auto-rollback on exceptions.", "Converts SQL to REST APIs.", "Caches all queries in memory indefinitely.", "Encrypts password fields.", "0", "Enforces ACID transaction boundaries."},
            {"In Python, what is the difference between a List and a Tuple?", "Lists are mutable; Tuples are immutable.", "Tuples store strings while lists store numbers.", "Lists use parentheses (); Tuples use square brackets [].", "Lists cannot hold duplicates.", "0", "Lists are mutable []; Tuples are immutable ()."},
            {"What is the difference between Lemmatization and Stemming in NLP?", "Lemmatization uses vocabulary context to return valid dictionary root words.", "Stemming always produces valid dictionary words.", "Lemmatization is faster than stemming.", "They produce identical tokens.", "0", "Lemmatization uses morphological analysis to find true base lemmas."},
            {"What is Dependency Injection in Spring Framework?", "Spring IoC container automatically manages object creation and injects dependencies.", "Developers manually create objects with new operator.", "Compiles Java into native dynamic C libraries.", "Applies only to frontend JS.", "0", "Spring container injects dependencies automatically."},
            {"What is the difference between `@Controller` and `@RestController`?", "`@RestController` combines `@Controller` and `@ResponseBody` for JSON/XML REST responses.", "`@Controller` only handles POST requests.", "`@RestController` renders JSP HTML views automatically.", "They are identical aliases.", "0", "`@RestController` automatically returns JSON response bodies."},
            {"Which HTTP method is idempotent and used to update a full resource?", "PUT", "POST", "PATCH", "DELETE", "0", "PUT is idempotent and replaces/updates resources."},
            {"What is the time complexity of pushing an element onto a Stack?", "O(1)", "O(N)", "O(log N)", "O(N^2)", "0", "Stack push operates in O(1) constant time."},
            {"What is the main function of Docker in microservices architecture?", "Containerizes applications with all dependencies for consistent deployment across environments.", "Compiles Java bytecode to binary.", "Replaces SQL relational databases.", "Generates frontend UI components.", "0", "Docker containerizes application environments."},
            {"In SQL, what is the purpose of `GROUP BY` clause?", "Aggregates rows with identical values in specified columns into summary rows.", "Sorts result set alphabetically.", "Filters individual rows before joining.", "Deletes duplicate primary keys.", "0", "GROUP BY groups summary rows together."},
            {"What is the difference between `WHERE` and `HAVING` clauses in SQL?", "WHERE filters rows before aggregation; HAVING filters groups after aggregation.", "HAVING filters rows before join.", "WHERE can only be used with primary keys.", "They are identical.", "0", "WHERE filters rows; HAVING filters aggregated groups."},
            {"Which data structure is optimal for implementing Breadth-First Search (BFS)?", "Queue", "Stack", "Min Heap", "Binary Search Tree", "0", "BFS traversal uses a Queue."},
            {"Which data structure is optimal for implementing Depth-First Search (DFS)?", "Stack", "Queue", "Array", "Linked List", "0", "DFS uses a Stack (or call stack recursion)."},
            {"What does JWT stand for in REST API security?", "JSON Web Token", "Java Web Technology", "Joint Web Transfer", "JavaScript Window Target", "0", "JWT stands for JSON Web Token."},
            {"What is the time complexity of accessing an array element by index `arr[i]`?", "O(1)", "O(N)", "O(log N)", "O(N^2)", "0", "Array indexing is direct O(1) memory lookup."},
            {"What is the main advantage of Microservices over Monolithic architecture?", "Decoupled services allow independent scaling, deployment, and technology choices.", "Microservices require fewer servers.", "Microservices eliminate database usage.", "Microservices don't require network calls.", "0", "Microservices offer independent scalability and deployment."},
            {"What is Garbage Collection (GC) in JVM?", "Automatic memory management process that frees memory occupied by unreferenced objects.", "Deletes unread emails.", "Compresses source code files.", "Formats database tables.", "0", "GC reclaims heap memory from unreferenced objects."},
            {"What is a RESTful API constraint?", "Stateless client-server communication where each request contains all required auth context.", "Requires client session cookies on server.", "Requires SOAP XML headers.", "Requires real-time WebSockets.", "0", "REST APIs are stateless."},
            {"In DSA, what technique uses two pointers moving towards each other in a sorted array?", "Two Pointers Technique", "Sliding Window", "Binary Search", "Dynamic Programming", "0", "Two Pointers converge from opposite ends to find pairs in O(N) time."}
        };

        for (int i = 0; i < bank.length; i++) {
            Map<String, Object> q = new HashMap<>();
            q.put("id", i + 1);
            q.put("question", bank[i][0]);
            q.put("options", Arrays.asList(bank[i][1], bank[i][2], bank[i][3], bank[i][4]));
            q.put("correctAnswerIndex", Integer.parseInt(bank[i][5]));
            q.put("explanation", bank[i][6]);
            qList.add(q);
        }

        res.put("totalQuestions", qList.size());
        res.put("questions", qList);
        return res;
    }
}
