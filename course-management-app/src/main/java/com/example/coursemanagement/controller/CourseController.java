package com.example.coursemanagement.controller;

import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {

    @Autowired
    private CourseRepository repository;

    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return repository.findAll();
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/courses/instructor/{name}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable String name) {
        List<Course> courses = repository.findByInstructor(name);
        if (courses.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/course")
    public Course addCourse(@RequestBody Course course) {
        return repository.save(course);
    }

    @PutMapping("/course/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        return repository.findById(id)
                .map(course -> {
                    course.setName(updatedCourse.getName());
                    course.setDuration(updatedCourse.getDuration());
                    course.setInstructor(updatedCourse.getInstructor());
                    return ResponseEntity.ok(repository.save(course));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}