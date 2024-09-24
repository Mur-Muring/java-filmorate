package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    List<Film> findAll();

    Optional<Film> getById(Long id);

    void createFilm(Film film);

    void updateFilm(Film newFilm);

    void addLike(Long id, Long userId);

    void removeLike(Long id, Long userId);

    List<Film> getPopular(Long count);

    Long isFilmExists(Long id);
}
