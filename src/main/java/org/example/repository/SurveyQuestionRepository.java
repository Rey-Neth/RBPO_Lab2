package org.example.repository;

import org.example.model.SurveyQuestion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class SurveyQuestionRepository {
    private final Map<Long, SurveyQuestion> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<SurveyQuestion> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<SurveyQuestion> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public SurveyQuestion save(SurveyQuestion question) {
        if (question.getId() == null) {
            question.setId(idGenerator.getAndIncrement());
        }
        storage.put(question.getId(), question);
        return question;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public List<SurveyQuestion> findBySurveyId(Long surveyId) {
        return storage.values().stream()
                .filter(q -> surveyId.equals(q.getSurveyId()))
                .collect(Collectors.toList());
    }

    public void deleteBySurveyId(Long surveyId) {
        storage.entrySet().removeIf(e -> surveyId.equals(e.getValue().getSurveyId()));
    }
}
