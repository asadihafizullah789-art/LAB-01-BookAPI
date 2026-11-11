package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class CourseControllerTest {

    private CourseController controller;

    @BeforeEach
    void setUp() {
        // Create a fresh controller before each test
        controller = new CourseController();
    }

    @Test
    void test01_findExistingCourse() {
        ResponseEntity<CourseController.Course> response = controller.getCourseById(101L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(101L, response.getBody().id());
        assertEquals("IS-401", response.getBody().code());
    }

    @Test
    void test02_findMissingCourse() {
        ResponseEntity<CourseController.Course> response = controller.getCourseById(9999L);
        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void test03_createValidCourse() {
        CourseController.CourseInput input = new CourseController.CourseInput("IS-404", "Software Testing", 3);
        ResponseEntity<?> response = controller.createCourse(input);

        assertEquals(201, response.getStatusCode().value());
        CourseController.Course createdCourse = (CourseController.Course) response.getBody();
        assertNotNull(createdCourse);
        assertEquals("IS-404", createdCourse.code());
        assertEquals(3, createdCourse.credits());
    }

    @Test
    void test04_rejectZeroCredits() {
        CourseController.CourseInput input = new CourseController.CourseInput("IS-404", "Software Testing", 0);
        ResponseEntity<?> response = controller.createCourse(input);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Credits must be positive.", response.getBody());
    }

    @Test
    void test05_deleteExistingCourse() {
        // First, verify course 103 exists
        assertEquals(200, controller.getCourseById(103L).getStatusCode().value());

        // Then delete it
        ResponseEntity<Void> deleteResponse = controller.deleteCourse(103L);
        assertEquals(204, deleteResponse.getStatusCode().value());

        // Finally, verify it is gone
        assertEquals(404, controller.getCourseById(103L).getStatusCode().value());
    }
}