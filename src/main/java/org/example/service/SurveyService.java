package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.model.Survey;
import org.example.repository.SurveyAnswerRepository;
import org.example.repository.SurveyParticipationRepository;
import org.example.repository.SurveyQuestionRepository;
import org.example.repository.SurveyRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final SurveyQuestionRepository questionRepository;
    private final SurveyParticipationRepository participationRepository;
    private final SurveyAnswerRepository answerRepository;

    public SurveyService(SurveyRepository surveyRepository,
                         UserRepository userRepository,
                         SurveyQuestionRepository questionRepository,
                         SurveyParticipationRepository participationRepository,
                         SurveyAnswerRepository answerRepository) {
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.participationRepository = participationRepository;
        this.answerRepository = answerRepository;
    }

    public List<Survey> findAll() {
        return surveyRepository.findAll();
    }

    public Survey findById(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Survey not found with id: " + id));
    }

    public Survey create(Survey survey) {
        if (survey.getTitle() == null || survey.getTitle().isBlank()) {
            throw new BadRequestException("Title is required");
        }
        if (survey.getAuthorId() == null) {
            throw new BadRequestException("AuthorId is required");
        }
        if (!userRepository.existsById(survey.getAuthorId())) {
            throw new BadRequestException("Author with id " + survey.getAuthorId() + " does not exist");
        }
        survey.setId(null);
        LocalDateTime now = LocalDateTime.now();
        survey.setCreatedAt(now);
        survey.setUpdatedAt(now);
        if (survey.getStatus() == null || survey.getStatus().isBlank()) {
            survey.setStatus("DRAFT");
        }
        // startDate / endDate can be provided by client (they are deadlines, not "current time")
        return surveyRepository.save(survey);
    }

    public Survey update(Long id, Survey updates) {
        Survey existing = findById(id);
        if (updates.getTitle() != null && !updates.getTitle().isBlank()) {
            existing.setTitle(updates.getTitle());
        }
        if (updates.getAuthorId() != null) {
            if (!userRepository.existsById(updates.getAuthorId())) {
                throw new BadRequestException("Author with id " + updates.getAuthorId() + " does not exist");
            }
            existing.setAuthorId(updates.getAuthorId());
        }
        if (updates.getStartDate() != null) {
            existing.setStartDate(updates.getStartDate());
        }
        if (updates.getEndDate() != null) {
            existing.setEndDate(updates.getEndDate());
        }
        if (updates.getStatus() != null && !updates.getStatus().isBlank()) {
            existing.setStatus(updates.getStatus());
        }
        existing.setUpdatedAt(LocalDateTime.now());
        return surveyRepository.save(existing);
    }

    public void delete(Long id) {
        if (!surveyRepository.existsById(id)) {
            throw new NotFoundException("Survey not found with id: " + id);
        }
        // Cascade: remove related questions, participations and their answers
        List<Long> questionIds = questionRepository.findBySurveyId(id).stream()
                .map(q -> q.getId()).toList();
        for (Long qId : questionIds) {
            answerRepository.deleteByQuestionId(qId);
        }
        questionRepository.deleteBySurveyId(id);

        List<Long> participationIds = participationRepository.findBySurveyId(id).stream()
                .map(p -> p.getId()).toList();
        for (Long pId : participationIds) {
            answerRepository.deleteByParticipationId(pId);
        }
        participationRepository.deleteBySurveyId(id);

        surveyRepository.deleteById(id);
    }
}
