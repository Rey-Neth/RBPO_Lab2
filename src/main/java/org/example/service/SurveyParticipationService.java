package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.model.SurveyParticipation;
import org.example.repository.SurveyAnswerRepository;
import org.example.repository.SurveyParticipationRepository;
import org.example.repository.SurveyRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SurveyParticipationService {
    private final SurveyParticipationRepository participationRepository;
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final SurveyAnswerRepository answerRepository;

    public SurveyParticipationService(SurveyParticipationRepository participationRepository,
                                      SurveyRepository surveyRepository,
                                      UserRepository userRepository,
                                      SurveyAnswerRepository answerRepository) {
        this.participationRepository = participationRepository;
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    public List<SurveyParticipation> findAll() {
        return participationRepository.findAll();
    }

    public SurveyParticipation findById(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SurveyParticipation not found with id: " + id));
    }

    public SurveyParticipation create(SurveyParticipation participation) {
        if (participation.getSurveyId() == null) {
            throw new BadRequestException("SurveyId is required");
        }
        if (!surveyRepository.existsById(participation.getSurveyId())) {
            throw new BadRequestException("Survey with id " + participation.getSurveyId() + " does not exist");
        }
        if (participation.getRespondentId() == null) {
            throw new BadRequestException("RespondentId is required");
        }
        if (!userRepository.existsById(participation.getRespondentId())) {
            throw new BadRequestException("Respondent with id " + participation.getRespondentId() + " does not exist");
        }
        participation.setId(null);
        LocalDateTime now = LocalDateTime.now();
        participation.setCreatedAt(now);
        if (participation.getStatus() == null || participation.getStatus().isBlank()) {
            participation.setStatus("IN_PROGRESS");
        }
        // submittedAt is server-managed when status becomes SUBMITTED
        if ("SUBMITTED".equalsIgnoreCase(participation.getStatus())) {
            participation.setSubmittedAt(now);
        }
        return participationRepository.save(participation);
    }

    public SurveyParticipation update(Long id, SurveyParticipation updates) {
        SurveyParticipation existing = findById(id);
        if (updates.getSurveyId() != null) {
            if (!surveyRepository.existsById(updates.getSurveyId())) {
                throw new BadRequestException("Survey with id " + updates.getSurveyId() + " does not exist");
            }
            existing.setSurveyId(updates.getSurveyId());
        }
        if (updates.getRespondentId() != null) {
            if (!userRepository.existsById(updates.getRespondentId())) {
                throw new BadRequestException("Respondent with id " + updates.getRespondentId() + " does not exist");
            }
            existing.setRespondentId(updates.getRespondentId());
        }
        if (updates.getStatus() != null && !updates.getStatus().isBlank()) {
            existing.setStatus(updates.getStatus());
            if ("SUBMITTED".equalsIgnoreCase(updates.getStatus()) && existing.getSubmittedAt() == null) {
                existing.setSubmittedAt(LocalDateTime.now());
            }
        }
        return participationRepository.save(existing);
    }

    public void delete(Long id) {
        if (!participationRepository.existsById(id)) {
            throw new NotFoundException("SurveyParticipation not found with id: " + id);
        }
        // Cascade: remove related answers
        answerRepository.deleteByParticipationId(id);
        participationRepository.deleteById(id);
    }
}
