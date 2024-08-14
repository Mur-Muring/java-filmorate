package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 1;

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Поступил запрос на добавление фильма {}", film);
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        log.info("Отправлен ответ {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film newFilm) {
        log.info("Поступил запрос на обновление фильма {}", newFilm);
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() != null) {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null) {
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
            }
            if (newFilm.getReleaseDate() != null) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            films.put(oldFilm.getId(), oldFilm);
            log.info("Отправлен ответ {}", newFilm);
            return oldFilm;
        }
        log.warn("id {} не найден", newFilm.getId());
        throw new ValidationException("id " + newFilm.getId() + " не найден");
    }
}
