package com.hw3.survey_app.controller;

import com.hw3.survey_app.model.Survey;
import com.hw3.survey_app.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST controller to handle HTTP requests
@RequestMapping("/api/surveys") // Base URI for all survey-related endpoints
public class SurveyController {

    @Autowired // Automatically injects the SurveyService bean
    private SurveyService surveyService;

    // Retrieve all surveys
    @GetMapping
    public List<Survey> getAllSurveys() {
        return surveyService.getAllSurveys();
    }

    // Retrieve a single survey by its ID
    @GetMapping("/{id}")
    public Survey getSurveyById(@PathVariable Long id) {
        return surveyService.getSurveyById(id);
    }

    // Create a new survey
    @PostMapping
    public Survey createSurvey(@RequestBody Survey survey) {
        return surveyService.saveSurvey(survey);
    }

    // Update an existing survey by its ID
    @PutMapping("/{id}")
    public Survey updateSurvey(@PathVariable Long id, @RequestBody Survey updatedSurvey) {
        return surveyService.updateSurvey(id, updatedSurvey);
    }

    // Delete a survey by its ID
    @DeleteMapping("/{id}")
    public void deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
    }
}
