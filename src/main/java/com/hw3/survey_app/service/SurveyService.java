package com.hw3.survey_app.service;

import com.hw3.survey_app.model.Survey;
import com.hw3.survey_app.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    public Survey getSurveyById(Long id) {
        return surveyRepository.findById(id).orElse(null);
    }

    public Survey saveSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }

    public Survey updateSurvey(Long id, Survey updatedSurvey) {
        Survey survey = getSurveyById(id);
        if (survey != null) {
            updatedSurvey.setId(id); // Ensure the ID remains consistent
            return surveyRepository.save(updatedSurvey);
        }
        return null;
    }

    public void deleteSurvey(Long id) {
        surveyRepository.deleteById(id);
    }
}
