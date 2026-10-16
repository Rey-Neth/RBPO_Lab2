package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class SurveyQuestion {
    private Long id;
    private Long surveyId;
    private String text;
    private boolean required;
    private List<String> options;

    public SurveyQuestion() {
        this.options = new ArrayList<>();
    }

    public SurveyQuestion(Long id, Long surveyId, String text, boolean required, List<String> options) {
        this.id = id;
        this.surveyId = surveyId;
        this.text = text;
        this.required = required;
        this.options = options != null ? options : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options != null ? options : new ArrayList<>();
    }
}
