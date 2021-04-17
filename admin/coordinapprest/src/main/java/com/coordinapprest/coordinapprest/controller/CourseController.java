package com.coordinapprest.coordinapprest.controller;

import java.util.concurrent.ExecutionException;

import com.coordinapprest.coordinapprest.models.Course;
import com.coordinapprest.coordinapprest.service.FirebaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    @Autowired
    FirebaseService firebaseService;

    @GetMapping("/getCourse") // Get course
    public String getCourse(@RequestHeader()String courseName){ 
        return "Hello from get";
    }

    @PostMapping("/postCourse") // Create course
    public String postCourse(@RequestBody Course course) throws InterruptedException, ExecutionException{ 
        return firebaseService.postCourse(course);
    } 

    @PutMapping("/putCourse") // Update course
    public String putCourse(){ 
        return "Hello from get";
    } 

    @DeleteMapping("/deleteCourse") // Delete course
    public String deleteCourse(){ 
        return "Hello from get";
    } 

}