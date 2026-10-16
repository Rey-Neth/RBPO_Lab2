package org.example.repository;

import org.example.model.SurveyParticipation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class SurveyParticipationRepository {
    private final Map<Long, SurveyParticipation> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<SurveyParticipation> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<SurveyParticipation> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public SurveyParticipation save(SurveyParticipation participation) {
        if (participation.getId() == null) {
            participation.setId(idGenerator.getAndIncrement());
        }
        storage.put(participation.getId(), participation);
        return participation;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public List<SurveyParticipation> findBySurveyId(Long surveyId) {
        return storage.values().stream()
                .filter(p -> surveyId.equals(p.getSurveyId()))
                .collect(Collectors.toList());
    }

    public List<SurveyParticipation> findByRespondentId(Long respondentId) {
        return storage.values().stream()
                .filter(p -> respondentId.equals(p.getRespondentId()))
                .collect(Collectors.toList());
    }

    public void deleteBySurveyId(Long surveyId) {
        storage.entrySet().removeIf(e -> surveyId.equals(e.getValue().getSurveyId()));
    }
}
