package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {

    public record Course(long id, String code, String title, int credits) {}
    public record CourseInput(String code, String title, int credits) {}

    // Initial data required by the lab
    private final List<Course> courses = new ArrayList<>(List.of(
            new Course(101L, "IS-401", "Advanced Enterprise Web Development", 3),
            new Course(102L, "DB201", "Database Systems", 3),
            new Course(103L, "SE202", "Software Engineering", 3)
    ));

    @GetMapping
    public List<Course> getAllCourses() {
        return courses;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable long id) {
        return courses.stream()
                .filter(c -> c.id() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseInput input) {
        if (input.credits() <= 0) {
            return ResponseEntity.badRequest().body("Credits must be positive.");
        }
        long newId = courses.stream().mapToLong(Course::id).max().orElse(0) + 1;
        Course newCourse = new Course(newId, input.code(), input.title(), input.credits());
        courses.add(newCourse);
        return ResponseEntity.status(201).body(newCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable long id) {
        boolean removed = courses.removeIf(c -> c.id() == id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}