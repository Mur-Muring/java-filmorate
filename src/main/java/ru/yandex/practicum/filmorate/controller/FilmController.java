package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import utils.WorkInterface;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        return filmService.getById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    @Validated(WorkInterface.Update.class)
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        filmService.updateFilm(newFilm);
        return newFilm;
    }

    @PutMapping("{filmID}/like/{userID}")
    public void like(@PathVariable Long filmID, @PathVariable Long userID) {
        filmService.addLike(filmID, userID);
    }

    @DeleteMapping("{filmID}/like/{userID}")
    public void deleteLike(@PathVariable Long filmID, @PathVariable Long userID) {
        filmService.removeLike(filmID, userID);
    }

    @GetMapping("popular")
    public Collection<Film> getPopular(@Min(0) @RequestParam(defaultValue = "10", name = "count") Long count) {
        return filmService.getPopular(count);
    }
}