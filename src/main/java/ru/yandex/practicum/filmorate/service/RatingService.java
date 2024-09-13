package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingStorage ratingStorage;

    public Collection<Rating> getAllMpa() {
        return ratingStorage.getAllMpa().stream()
                .sorted(Comparator.comparing(Rating::getId))
                .collect(Collectors.toList());
    }

    public Rating getMpaById(Integer id) {
        return ratingStorage.getMpaById(id);
    }
}
