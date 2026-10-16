package org.example.model;

public class SurveyAnswer {
    private Long id;
    private Long participationId;
    private Long questionId;
    private String selectedValue;

    public SurveyAnswer() {
    }

    public SurveyAnswer(Long id, Long participationId, Long questionId, String selectedValue) {
        this.id = id;
        this.participationId = participationId;
        this.questionId = questionId;
        this.selectedValue = selectedValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParticipationId() {
        return participationId;
    }

    public void setParticipationId(Long participationId) {
        this.participationId = participationId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
}
