package org.example.model;

import java.time.LocalDateTime;

public class SurveyParticipation {
    private Long id;
    private Long surveyId;
    private Long respondentId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;

    public SurveyParticipation() {
    }

    public SurveyParticipation(Long id, Long surveyId, Long respondentId, String status,
                               LocalDateTime createdAt, LocalDateTime submittedAt) {
        this.id = id;
        this.surveyId = surveyId;
        this.respondentId = respondentId;
        this.status = status;
        this.createdAt = createdAt;
        this.submittedAt = submittedAt;
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

    public Long getRespondentId() {
        return respondentId;
    }

    public void setRespondentId(Long respondentId) {
        this.respondentId = respondentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
