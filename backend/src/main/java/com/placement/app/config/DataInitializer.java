package com.placement.app.config;

import com.placement.app.entity.*;
import com.placement.app.repository.*;
import com.placement.app.service.NlpClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final CompanyDriveRepository driveRepository;
    private final AssessmentRepository assessmentRepository;
    private final DsaProblemRepository dsaProblemRepository;
    private final PasswordEncoder passwordEncoder;
    private final NlpClientService nlpClientService;

    public DataInitializer(UserRepository userRepository,
                           ResumeRepository resumeRepository,
                           CompanyDriveRepository driveRepository,
                           AssessmentRepository assessmentRepository,
                           DsaProblemRepository dsaProblemRepository,
                           PasswordEncoder passwordEncoder,
                           NlpClientService nlpClientService) {
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.driveRepository = driveRepository;
        this.assessmentRepository = assessmentRepository;
        this.dsaProblemRepository = dsaProblemRepository;
        this.passwordEncoder = passwordEncoder;
        this.nlpClientService = nlpClientService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Seed Admin
            User admin = new User("admin", "admin@placement.com", passwordEncoder.encode("admin123"), "ADMIN", "Placement Officer", "Placement Cell", "TPO", 4.0);
            userRepository.save(admin);

            // Seed Recruiters / Companies
            User recruiter1 = new User("recruiter", "recruiter@techcorp.com", passwordEncoder.encode("recruiter123"), "RECRUITER", "TechCorp HR", "TechCorp", "Talent Acquisition", null);
            User recruiter2 = new User("google_hr", "careers@google.com", passwordEncoder.encode("company123"), "RECRUITER", "Google Recruiting Team", "Google", "University Relations", null);
            userRepository.saveAll(Arrays.asList(recruiter1, recruiter2));

            // Seed Students
            User student1 = new User("student", "student@placement.com", passwordEncoder.encode("student123"), "STUDENT", "Alex Mercer", null, "Computer Science & Engineering", 8.9);
            User student2 = new User("priya_student", "priya@placement.com", passwordEncoder.encode("student123"), "STUDENT", "Priya Sharma", null, "Artificial Intelligence & ML", 9.2);
            userRepository.saveAll(Arrays.asList(student1, student2));

            // Seed Sample Resumes
            Resume resume1 = new Resume();
            resume1.setUserId(student1.getId());
            resume1.setCandidateName("Alex Mercer");
            resume1.setEmail("student@placement.com");
            resume1.setPhone("+1 555-0199");
            resume1.setSkillsJson("Java, Spring Boot, Spring Security, MySQL, Python, REST API, Git, Problem Solving, Data Structures");
            resume1.setEducation("B.Tech Computer Science (CGPA: 8.9)");
            resume1.setProjects("AI Resume Analyzer and Skill Matcher, E-Commerce Microservices");
            resume1.setRawText("Alex Mercer Resume. B.Tech Computer Science. Skills: Java, Spring Boot, Spring Security, MySQL, Python, REST API, Git, Data Structures.");
            
            Resume resume2 = new Resume();
            resume2.setUserId(student2.getId());
            resume2.setCandidateName("Priya Sharma");
            resume2.setEmail("priya@placement.com");
            resume2.setPhone("+1 555-0244");
            resume2.setSkillsJson("Python, spaCy, NLTK, Machine Learning, TensorFlow, SQL, React, Git");
            resume2.setEducation("B.Tech AI & ML (CGPA: 9.2)");
            resume2.setProjects("NLP Resume Parser, Predictive Analytics Dashboard");
            resume2.setRawText("Priya Sharma Resume. B.Tech AI & ML. Skills: Python, spaCy, NLTK, Machine Learning, TensorFlow, SQL, React.");

            resumeRepository.saveAll(Arrays.asList(resume1, resume2));

            // Seed Company Placement Drives
            CompanyDrive drive1 = new CompanyDrive("Google", "Software Engineer - Backend", "Java, Spring Boot, MySQL, REST API, Data Structures", 18.5, "Bengaluru", "2026-09-30", recruiter2.getId());
            CompanyDrive drive2 = new CompanyDrive("Microsoft", "Full Stack Engineer", "Java, React, SQL, Cloud Computing, Git", 16.0, "Hyderabad", "2026-10-15", recruiter1.getId());
            CompanyDrive drive3 = new CompanyDrive("Amazon", "SDE-1 AI/NLP", "Python, spaCy, NLTK, Machine Learning, REST API", 22.0, "Gurugram", "2026-10-31", recruiter1.getId());
            CompanyDrive drive4 = new CompanyDrive("TCS", "Systems Engineer", "C++, Java, SQL, Problem Solving", 7.5, "Pune", "2026-11-15", recruiter1.getId());

            driveRepository.saveAll(Arrays.asList(drive1, drive2, drive3, drive4));

            // Seed 8 DSA Problems with Test Cases
            DsaProblem prob1 = new DsaProblem(
                "Two Sum - Target Pair Indices",
                "Easy",
                "Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.",
                "2 <= nums.length <= 10^4, -10^9 <= nums[i] <= 10^9, -10^9 <= target <= 10^9",
                "nums = [2, 7, 11, 15], target = 9",
                "[0, 1]",
                "[{\"input\": \"nums = [2,7,11,15], target = 9\", \"expectedOutput\": \"[0, 1]\"}, {\"input\": \"nums = [3,2,4], target = 6\", \"expectedOutput\": \"[1, 2]\"}, {\"input\": \"nums = [3,3], target = 6\", \"expectedOutput\": \"[0, 1]\"}, {\"input\": \"nums = [1,5,8,3], target = 11\", \"expectedOutput\": \"[2, 3]\"}]",
                "class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        Map<Integer, Integer> map = new HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int comp = target - nums[i];\n            if (map.containsKey(comp)) return new int[]{map.get(comp), i};\n            map.put(nums[i], i);\n        }\n        return new int[]{};\n    }\n}",
                "def two_sum(nums, target):\n    seen = {}\n    for i, num in enumerate(nums):\n        comp = target - num\n        if comp in seen:\n            return [seen[comp], i]\n        seen[num] = i\n    return []"
            );

            DsaProblem prob2 = new DsaProblem(
                "Valid Parentheses Matching",
                "Easy",
                "Given a string `s` containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\n\nAn input string is valid if open brackets are closed by the same type of brackets and in correct order.",
                "1 <= s.length <= 10^4",
                "s = \"()[]{}\"",
                "true",
                "[{\"input\": \"s = '()[]{}'\", \"expectedOutput\": \"true\"}, {\"input\": \"s = '(]'\", \"expectedOutput\": \"false\"}, {\"input\": \"s = '([{}])'\", \"expectedOutput\": \"true\"}, {\"input\": \"s = '{(['\", \"expectedOutput\": \"false\"}]",
                "class Solution {\n    public boolean isValid(String s) {\n        Stack<Character> stack = new Stack<>();\n        for (char c : s.toCharArray()) {\n            if (c == '(') stack.push(')');\n            else if (c == '{') stack.push('}');\n            else if (c == '[') stack.push(']');\n            else if (stack.isEmpty() || stack.pop() != c) return false;\n        }\n        return stack.isEmpty();\n    }\n}",
                "def is_valid(s):\n    stack = []\n    mapping = {')': '(', '}': '{', ']': '['}\n    for char in s:\n        if char in mapping:\n            top = stack.pop() if stack else '#'\n            if mapping[char] != top: return False\n        else:\n            stack.append(char)\n    return not stack"
            );

            // Generate Seed 30 MCQ Assessments for Drives with Attached DSA Coding Challenge
            Map<String, Object> aiGen = nlpClientService.generateAssessment(Arrays.asList("Java", "Spring Boot", "MySQL", "SQL", "Python", "Data Structures"), "Software Engineer - Backend");
            
            String questionsStr = "[]";
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                questionsStr = mapper.writeValueAsString(aiGen.get("questions"));
            } catch (Exception e) {
                System.err.println("Questions serialization error: " + e);
            }

            Assessment assessment1 = new Assessment();
            assessment1.setDriveId(drive1.getId());
            assessment1.setRoleTitle(drive1.getRoleTitle());
            assessment1.setTargetSkills(drive1.getRequiredSkills());
            assessment1.setQuestionsJson(questionsStr);
            assessment1.setTotalQuestions(30);
            assessment1.setDurationMinutes(35);
            assessment1.setDsaTitle(prob1.getTitle());
            assessment1.setDsaDifficulty(prob1.getDifficulty());
            assessment1.setDsaDescription(prob1.getDescription());
            assessment1.setDsaConstraints(prob1.getConstraints());
            assessment1.setDsaSampleInput(prob1.getSampleInput());
            assessment1.setDsaSampleOutput(prob1.getSampleOutput());
            assessment1.setDsaTestCasesJson(prob1.getTestCasesJson());
            assessment1.setDsaStarterCodeJava(prob1.getStarterCodeJava());
            assessment1.setDsaStarterCodePython(prob1.getStarterCodePython());

            Assessment assessment2 = new Assessment();
            assessment2.setDriveId(drive3.getId());
            assessment2.setRoleTitle(drive3.getRoleTitle());
            assessment2.setTargetSkills(drive3.getRequiredSkills());
            assessment2.setQuestionsJson(questionsStr);
            assessment2.setTotalQuestions(30);
            assessment2.setDurationMinutes(35);
            assessment2.setDsaTitle(prob2.getTitle());
            assessment2.setDsaDifficulty(prob2.getDifficulty());
            assessment2.setDsaDescription(prob2.getDescription());
            assessment2.setDsaConstraints(prob2.getConstraints());
            assessment2.setDsaSampleInput(prob2.getSampleInput());
            assessment2.setDsaSampleOutput(prob2.getSampleOutput());
            assessment2.setDsaTestCasesJson(prob2.getTestCasesJson());
            assessment2.setDsaStarterCodeJava(prob2.getStarterCodeJava());
            assessment2.setDsaStarterCodePython(prob2.getStarterCodePython());

            assessmentRepository.saveAll(Arrays.asList(assessment1, assessment2));

            DsaProblem prob3 = new DsaProblem(
                "Longest Substring Without Repeating Characters",
                "Medium",
                "Given a string `s`, find the length of the longest substring without repeating characters using the Sliding Window technique.",
                "0 <= s.length <= 5 * 10^4",
                "s = \"abcabcbb\"",
                "3 (substring \"abc\")",
                "[{\"input\": \"s = 'abcabcbb'\", \"expectedOutput\": \"3\"}, {\"input\": \"s = 'bbbbb'\", \"expectedOutput\": \"1\"}, {\"input\": \"s = 'pwwkew'\", \"expectedOutput\": \"3\"}, {\"input\": \"s = ''\", \"expectedOutput\": \"0\"}]",
                "class Solution {\n    public int lengthOfLongestSubstring(String s) {\n        Set<Character> set = new HashSet<>();\n        int left = 0, maxLen = 0;\n        for (int right = 0; right < s.length(); right++) {\n            while (set.contains(s.charAt(right))) {\n                set.remove(s.charAt(left++));\n            }\n            set.add(s.charAt(right));\n            maxLen = Math.max(maxLen, right - left + 1);\n        }\n        return maxLen;\n    }\n}",
                "def length_of_longest_substring(s):\n    char_map = {}\n    left = max_len = 0\n    for right, char in enumerate(s):\n        if char in char_map and char_map[char] >= left:\n            left = char_map[char] + 1\n        char_map[char] = right\n        max_len = max(max_len, right - left + 1)\n    return max_len"
            );

            DsaProblem prob4 = new DsaProblem(
                "Container With Most Water",
                "Medium",
                "You are given an integer array `height` of length `n`. Find two lines that together with the x-axis form a container, such that the container contains the most water.\n\nReturn the maximum amount of water a container can store.",
                "n == height.length, 2 <= n <= 10^5, 0 <= height[i] <= 10^4",
                "height = [1,8,6,2,5,4,8,3,7]",
                "49",
                "[{\"input\": \"height = [1,8,6,2,5,4,8,3,7]\", \"expectedOutput\": \"49\"}, {\"input\": \"height = [1,1]\", \"expectedOutput\": \"1\"}, {\"input\": \"height = [4,3,2,1,4]\", \"expectedOutput\": \"16\"}]",
                "class Solution {\n    public int maxArea(int[] height) {\n        int left = 0, right = height.length - 1, maxArea = 0;\n        while (left < right) {\n            int currentHeight = Math.min(height[left], height[right]);\n            maxArea = Math.max(maxArea, currentHeight * (right - left));\n            if (height[left] < height[right]) left++;\n            else right--;\n        }\n        return maxArea;\n    }\n}",
                "def max_area(height):\n    left, right = 0, len(height) - 1\n    max_water = 0\n    while left < right:\n        w = right - left\n        h = min(height[left], height[right])\n        max_water = max(max_water, w * h)\n        if height[left] < height[right]:\n            left += 1\n        else:\n            right -= 1\n    return max_water"
            );

            DsaProblem prob5 = new DsaProblem(
                "Binary Tree Level Order Traversal",
                "Medium",
                "Given the root of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level using BFS queue).",
                "0 <= number of nodes <= 2000, -1000 <= Node.val <= 1000",
                "root = [3,9,20,null,null,15,7]",
                "[[3],[9,20],[15,7]]",
                "[{\"input\": \"root = [3,9,20,null,null,15,7]\", \"expectedOutput\": \"[[3],[9,20],[15,7]]\"}, {\"input\": \"root = [1]\", \"expectedOutput\": \"[[1]]\"}, {\"input\": \"root = []\", \"expectedOutput\": \"[]\"}]",
                "class Solution {\n    public List<List<Integer>> levelOrder(TreeNode root) {\n        List<List<Integer>> result = new ArrayList<>();\n        if (root == null) return result;\n        Queue<TreeNode> queue = new LinkedList<>();\n        queue.add(root);\n        while (!queue.isEmpty()) {\n            int size = queue.size();\n            List<Integer> level = new ArrayList<>();\n            for (int i = 0; i < size; i++) {\n                TreeNode node = queue.poll();\n                level.add(node.val);\n                if (node.left != null) queue.add(node.left);\n                if (node.right != null) queue.add(node.right);\n            }\n            result.add(level);\n        }\n        return result;\n    }\n}",
                "def level_order(root):\n    if not root: return []\n    res, queue = [], [root]\n    while queue:\n        val_level = [node.val for node in queue]\n        res.append(val_level)\n        queue = [child for node in queue for child in (node.left, node.right) if child]\n    return res"
            );

            DsaProblem prob6 = new DsaProblem(
                "Merge K Sorted Lists",
                "Hard",
                "You are given an array of `k` linked-lists `lists`, each linked-list is sorted in ascending order.\n\nMerge all the linked-lists into one sorted linked-list and return it using a Min-Heap / PriorityQueue.",
                "0 <= k <= 10^4, 0 <= lists[i].length <= 500, -10^4 <= lists[i][j] <= 10^4",
                "lists = [[1,4,5],[1,3,4],[2,6]]",
                "[1,1,2,3,4,4,5,6]",
                "[{\"input\": \"lists = [[1,4,5],[1,3,4],[2,6]]\", \"expectedOutput\": \"[1,1,2,3,4,4,5,6]\"}, {\"input\": \"lists = []\", \"expectedOutput\": \"[]\"}, {\"input\": \"lists = [[]]\", \"expectedOutput\": \"[]\"}]",
                "class Solution {\n    public ListNode mergeKLists(ListNode[] lists) {\n        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);\n        for (ListNode node : lists) {\n            if (node != null) pq.add(node);\n        }\n        ListNode dummy = new ListNode(0);\n        ListNode curr = dummy;\n        while (!pq.isEmpty()) {\n            ListNode node = pq.poll();\n            curr.next = node;\n            curr = curr.next;\n            if (node.next != null) pq.add(node.next);\n        }\n        return dummy.next;\n    }\n}",
                "import heapq\ndef merge_k_lists(lists):\n    heap = []\n    for i, l in enumerate(lists):\n        if l: heapq.heappush(heap, (l.val, i, l))\n    dummy = curr = ListNode(0)\n    while heap:\n        val, i, node = heapq.heappop(heap)\n        curr.next = node\n        curr = curr.next\n        if node.next: heapq.heappush(heap, (node.next.val, i, node.next))\n    return dummy.next"
            );

            DsaProblem prob7 = new DsaProblem(
                "Coin Change - Minimum Coins",
                "Medium",
                "You are given an integer array `coins` representing coins of different denominations and an integer `amount` representing a total amount of money.\n\nReturn the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return -1.",
                "1 <= coins.length <= 12, 1 <= coins[i] <= 2^31 - 1, 0 <= amount <= 10^4",
                "coins = [1, 2, 5], amount = 11",
                "3 (5 + 5 + 1 = 11)",
                "[{\"input\": \"coins = [1,2,5], amount = 11\", \"expectedOutput\": \"3\"}, {\"input\": \"coins = [2], amount = 3\", \"expectedOutput\": \"-1\"}, {\"input\": \"coins = [1], amount = 0\", \"expectedOutput\": \"0\"}]",
                "class Solution {\n    public int coinChange(int[] coins, int amount) {\n        int[] dp = new int[amount + 1];\n        Arrays.fill(dp, amount + 1);\n        dp[0] = 0;\n        for (int i = 1; i <= amount; i++) {\n            for (int coin : coins) {\n                if (i >= coin) {\n                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);\n                }\n            }\n        }\n        return dp[amount] > amount ? -1 : dp[amount];\n    }\n}",
                "def coin_change(coins, amount):\n    dp = [float('inf')] * (amount + 1)\n    dp[0] = 0\n    for i in range(1, amount + 1):\n        for c in coins:\n            if i >= c:\n                dp[i] = min(dp[i], dp[i - c] + 1)\n    return dp[amount] if dp[amount] != float('inf') else -1"
            );

            DsaProblem prob8 = new DsaProblem(
                "Find Minimum in Rotated Sorted Array",
                "Medium",
                "Suppose an array of length `n` sorted in ascending order is rotated between 1 and `n` times. Given the sorted rotated array `nums` of unique elements, return the minimum element of this array in O(log n) time complexity.",
                "n == nums.length, 1 <= n <= 5000, -5000 <= nums[i] <= 5000",
                "nums = [3,4,5,1,2]",
                "1",
                "[{\"input\": \"nums = [3,4,5,1,2]\", \"expectedOutput\": \"1\"}, {\"input\": \"nums = [4,5,6,7,0,1,2]\", \"expectedOutput\": \"0\"}, {\"input\": \"nums = [11,13,15,17]\", \"expectedOutput\": \"11\"}]",
                "class Solution {\n    public int findMin(int[] nums) {\n        int left = 0, right = nums.length - 1;\n        while (left < right) {\n            int mid = left + (right - left) / 2;\n            if (nums[mid] > nums[right]) left = mid + 1;\n            else right = mid;\n        }\n        return nums[left];\n    }\n}",
                "def find_min(nums):\n    left, right = 0, len(nums) - 1\n    while left < right:\n        mid = (left + right) // 2\n        if nums[mid] > nums[right]:\n            left = mid + 1\n        else:\n            right = mid\n    return nums[left]"
            );

            dsaProblemRepository.saveAll(Arrays.asList(prob1, prob2, prob3, prob4, prob5, prob6, prob7, prob8));

            System.out.println("DataInitializer completed: Sample users, resumes, drives, assessments, and 8 DSA problems created!");
        }
    }
}
