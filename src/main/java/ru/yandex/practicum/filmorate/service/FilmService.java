package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRatingRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private FilmRepository films;
    private UserRepository users;
    private MpaRatingRepository mpaRepository;
    private GenreRepository genreRepository;

    public Collection<Film> findAll() {
        log.info("Получение списка фильмов");
        return films.findAll().values().stream().toList();
    }

    public Film getById(long filmID) {
        log.info("Получение фильма с id = {}", filmID);
        return films.getById(filmID).orElseThrow(() -> new NotFoundException("Фильм с ID - " + filmID + " не найден"));
    }

    public Film createFilm(Film film) {
        try {
            mpaRepository.isMpaExists(film.getMpa().getId());
        } catch (NotFoundException e) {
            throw new ValidationException("Такого MPA не существует");
        }
        films.createFilm(film);
        if (film.getId() == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        try {
            genreRepository.saveGenre(film);
        } catch (NotFoundException e) {
            throw new ValidationException("Такого жанра не существует");
        }
        log.info("Фильм {} добавлен в список с id = {}", film.getName(), film.getId());
        return film;
    }

    public void updateFilm(Film film) {
        films.isFilmNotExists(film.getId());
        mpaRepository.isMpaExists(film.getMpa().getId());
        films.updateFilm(film);
        genreRepository.saveGenre(film);
        log.info("Фильм с id = {} обновлен", film.getId());
    }


    public void addLike(Long filmID, Long userId) {
        films.isFilmNotExists(filmID);
        films.addLike(filmID, userId);
        log.info("Пользователь с id = {} поставил лайк фильму id = {}", userId, filmID);
    }

    public void removeLike(Long filmID, Long userId) {
        films.isFilmNotExists(filmID);
        users.isUserNotExists(userId);
        films.removeLike(filmID, userId);
        log.info("Пользователь с id = {} удалил лайк фильму id = {}", userId, filmID);
    }

    public Collection<Film> getPopular(Long count) {
        log.info("Получение списка {} популярных фильмов", count);
        return films.getPopular(count).values().stream().toList();
    }

}