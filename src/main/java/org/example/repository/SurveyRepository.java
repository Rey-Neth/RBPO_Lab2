package org.example.repository;

import org.example.model.Survey;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class SurveyRepository {
    private final Map<Long, Survey> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Survey> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Survey> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Survey save(Survey survey) {
        if (survey.getId() == null) {
            survey.setId(idGenerator.getAndIncrement());
        }
        storage.put(survey.getId(), survey);
        return survey;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public List<Survey> findByAuthorId(Long authorId) {
        return storage.values().stream()
                .filter(s -> authorId.equals(s.getAuthorId()))
                .collect(Collectors.toList());
    }
}
