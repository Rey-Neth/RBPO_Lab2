package org.example.repository;

import org.example.model.SurveyAnswer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class SurveyAnswerRepository {
    private final Map<Long, SurveyAnswer> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<SurveyAnswer> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<SurveyAnswer> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public SurveyAnswer save(SurveyAnswer answer) {
        if (answer.getId() == null) {
            answer.setId(idGenerator.getAndIncrement());
        }
        storage.put(answer.getId(), answer);
        return answer;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public List<SurveyAnswer> findByParticipationId(Long participationId) {
        return storage.values().stream()
                .filter(a -> participationId.equals(a.getParticipationId()))
                .collect(Collectors.toList());
    }

    public List<SurveyAnswer> findByQuestionId(Long questionId) {
        return storage.values().stream()
                .filter(a -> questionId.equals(a.getQuestionId()))
                .collect(Collectors.toList());
    }

    public void deleteByParticipationId(Long participationId) {
        storage.entrySet().removeIf(e -> participationId.equals(e.getValue().getParticipationId()));
    }

    public void deleteByQuestionId(Long questionId) {
        storage.entrySet().removeIf(e -> questionId.equals(e.getValue().getQuestionId()));
    }

    public void deleteBySurveyId(Long surveyId, SurveyParticipationRepository participationRepo,
                                 SurveyQuestionRepository questionRepo) {
        // Helper not needed here; cascade handled in service
    }
}
