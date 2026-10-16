package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.model.SurveyQuestion;
import org.example.repository.SurveyAnswerRepository;
import org.example.repository.SurveyQuestionRepository;
import org.example.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyQuestionService {
    private final SurveyQuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyAnswerRepository answerRepository;

    public SurveyQuestionService(SurveyQuestionRepository questionRepository,
                                 SurveyRepository surveyRepository,
                                 SurveyAnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
        this.answerRepository = answerRepository;
    }

    public List<SurveyQuestion> findAll() {
        return questionRepository.findAll();
    }

    public SurveyQuestion findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SurveyQuestion not found with id: " + id));
    }

    public SurveyQuestion create(SurveyQuestion question) {
        if (question.getSurveyId() == null) {
            throw new BadRequestException("SurveyId is required");
        }
        if (!surveyRepository.existsById(question.getSurveyId())) {
            throw new BadRequestException("Survey with id " + question.getSurveyId() + " does not exist");
        }
        if (question.getText() == null || question.getText().isBlank()) {
            throw new BadRequestException("Text is required");
        }
        question.setId(null);
        if (question.getOptions() == null) {
            question.setOptions(List.of());
        }
        return questionRepository.save(question);
    }

    public SurveyQuestion update(Long id, SurveyQuestion updates) {
        SurveyQuestion existing = findById(id);
        if (updates.getSurveyId() != null) {
            if (!surveyRepository.existsById(updates.getSurveyId())) {
                throw new BadRequestException("Survey with id " + updates.getSurveyId() + " does not exist");
            }
            existing.setSurveyId(updates.getSurveyId());
        }
        if (updates.getText() != null && !updates.getText().isBlank()) {
            existing.setText(updates.getText());
        }
        existing.setRequired(updates.isRequired());
        if (updates.getOptions() != null) {
            existing.setOptions(updates.getOptions());
        }
        return questionRepository.save(existing);
    }

    public void delete(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new NotFoundException("SurveyQuestion not found with id: " + id);
        }
        // Cascade: remove related answers
        answerRepository.deleteByQuestionId(id);
        questionRepository.deleteById(id);
    }
}
