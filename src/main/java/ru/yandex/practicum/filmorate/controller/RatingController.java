package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
@RequestMapping("/rating")
@Slf4j
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public Collection<Rating> getAllMpa() {
        log.info("Получен GET-запрос на получение всех рейтингов");
        return ratingService.getAllMpa();
    }

    @GetMapping("/{id}")
    public Rating getMpaById(@PathVariable("id") Integer id) {
        log.info("Получен GET-запрос на получение рейтинга с id = {}", id);
        return ratingService.getMpaById(id);
    }
}
