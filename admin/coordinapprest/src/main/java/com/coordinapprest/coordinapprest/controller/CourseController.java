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

    @PostMapping("/postCourse")
    public String postCourse(@RequestBody Course course) throws InterruptedException, ExecutionException{ 
        return firebaseService.postCourse(course);
    }

}