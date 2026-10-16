package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.model.SurveyAnswer;
import org.example.model.SurveyQuestion;
import org.example.repository.SurveyAnswerRepository;
import org.example.repository.SurveyParticipationRepository;
import org.example.repository.SurveyQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyAnswerService {
    private final SurveyAnswerRepository answerRepository;
    private final SurveyParticipationRepository participationRepository;
    private final SurveyQuestionRepository questionRepository;

    public SurveyAnswerService(SurveyAnswerRepository answerRepository,
                               SurveyParticipationRepository participationRepository,
                               SurveyQuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.participationRepository = participationRepository;
        this.questionRepository = questionRepository;
    }

    public List<SurveyAnswer> findAll() {
        return answerRepository.findAll();
    }

    public SurveyAnswer findById(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SurveyAnswer not found with id: " + id));
    }

    public SurveyAnswer create(SurveyAnswer answer) {
        if (answer.getParticipationId() == null) {
            throw new BadRequestException("ParticipationId is required");
        }
        if (!participationRepository.existsById(answer.getParticipationId())) {
            throw new BadRequestException("Participation with id " + answer.getParticipationId() + " does not exist");
        }
        if (answer.getQuestionId() == null) {
            throw new BadRequestException("QuestionId is required");
        }
        SurveyQuestion question = questionRepository.findById(answer.getQuestionId())
                .orElseThrow(() -> new BadRequestException("Question with id " + answer.getQuestionId() + " does not exist"));

        // Optional: validate selected value against options if options are defined
        if (question.getOptions() != null && !question.getOptions().isEmpty()
                && answer.getSelectedValue() != null
                && !question.getOptions().contains(answer.getSelectedValue())) {
            throw new BadRequestException("Selected value is not in allowed options");
        }

        answer.setId(null);
        return answerRepository.save(answer);
    }

    public SurveyAnswer update(Long id, SurveyAnswer updates) {
        SurveyAnswer existing = findById(id);
        if (updates.getParticipationId() != null) {
            if (!participationRepository.existsById(updates.getParticipationId())) {
                throw new BadRequestException("Participation with id " + updates.getParticipationId() + " does not exist");
            }
            existing.setParticipationId(updates.getParticipationId());
        }
        if (updates.getQuestionId() != null) {
            if (!questionRepository.existsById(updates.getQuestionId())) {
                throw new BadRequestException("Question with id " + updates.getQuestionId() + " does not exist");
            }
            existing.setQuestionId(updates.getQuestionId());
        }
        if (updates.getSelectedValue() != null) {
            // re-validate against current question options
            Long qId = updates.getQuestionId() != null ? updates.getQuestionId() : existing.getQuestionId();
            SurveyQuestion question = questionRepository.findById(qId).orElse(null);
            if (question != null && question.getOptions() != null && !question.getOptions().isEmpty()
                    && !question.getOptions().contains(updates.getSelectedValue())) {
                throw new BadRequestException("Selected value is not in allowed options");
            }
            existing.setSelectedValue(updates.getSelectedValue());
        }
        return answerRepository.save(existing);
    }

    public void delete(Long id) {
        if (!answerRepository.existsById(id)) {
            throw new NotFoundException("SurveyAnswer not found with id: " + id);
        }
        answerRepository.deleteById(id);
    }
}
