package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Map;
import java.util.Optional;

public interface FilmRepository {
    Map<Long, Film> findAll();

    Optional<Film> getById(Long id);

    void createFilm(Film film);

    void updateFilm(Film newFilm);

    void addLike(Long id, Long userId);

    void removeLike(Long id, Long userId);

    Map<Long, Film> getPopular(Long count);

    void isFilmNotExists(Long id);
}
