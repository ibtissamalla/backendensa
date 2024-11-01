package com.example.api.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@CrossOrigin(origins = "http://localhost:4200")
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Home page";
    }

    @GetMapping("/admin/home")
    public String getAdminHome() {
        return "Admin home page";
    }

    @GetMapping("/student/home")
    public String getStudentHome() {
        return "Student home page";
    }

    @GetMapping("/teacher/home")
    public String getTeacherHome() {
        return "Teacher home page";
    }

    @GetMapping("/director/home")
    public String getDirectorHome() {
        return "Director home page";
    }
}
