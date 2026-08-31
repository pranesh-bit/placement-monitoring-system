package com.placement.app.dto;

public class AuthDTOs {

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private String role; // STUDENT, RECRUITER, ADMIN
        private String fullName;
        private String companyName;
        private String department;
        private Double gpa;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }

        public Double getGpa() { return gpa; }
        public void setGpa(Double gpa) { this.gpa = gpa; }
    }

    public static class AuthResponse {
        private String token;
        private String username;
        private String email;
        private String role;
        private String fullName;
        private Long userId;
        private String companyName;
        private String department;
        private Double gpa;

        public AuthResponse(String token, String username, String email, String role, String fullName, Long userId) {
            this.token = token;
            this.username = username;
            this.email = email;
            this.role = role;
            this.fullName = fullName;
            this.userId = userId;
        }

        public AuthResponse(String token, String username, String email, String role, String fullName, Long userId, String companyName, String department, Double gpa) {
            this.token = token;
            this.username = username;
            this.email = email;
            this.role = role;
            this.fullName = fullName;
            this.userId = userId;
            this.companyName = companyName;
            this.department = department;
            this.gpa = gpa;
        }

        public String getToken() { return token; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getFullName() { return fullName; }
        public Long getUserId() { return userId; }
        public String getCompanyName() { return companyName; }
        public String getDepartment() { return department; }
        public Double getGpa() { return gpa; }
    }
}
