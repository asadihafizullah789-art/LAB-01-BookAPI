package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {

    public record Course(long id, String code, String title, int credits) {}

    private final List<Course> courses = new ArrayList<>(List.of(
            new Course(1, "EWA301", "Enterprise Web App", 3),
            new Course(2, "DB201", "Database Systems", 3),
            new Course(3, "SE202", "Software Engineering", 3)
    ));

    @GetMapping
    public List<Course> getCourses() {
        return courses;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable long id, @RequestBody Course updatedCourse) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).id() == id) {
                courses.set(i, new Course(id, updatedCourse.code(), updatedCourse.title(), updatedCourse.credits()));
                return ResponseEntity.ok(courses.get(i));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable long id) {
        boolean removed = courses.removeIf(c -> c.id() == id);
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}