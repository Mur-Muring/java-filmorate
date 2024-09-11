package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private Film film;
    private final String errorUser = "Пользователь не найден";
    private final String errorFilm = "Фильм не найден";

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public Collection<Film> getAllFilms() {
        log.info("Поступил запрос на получение списка всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        log.info("Поступил запрос на создание фильма {}", film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        log.info("Поступил запрос на обновление фильма {}", newFilm);
        return filmStorage.updateFilm(newFilm);
    }

    public void likeFilm(Integer userId, Integer filmId) {
        log.info("Поступил запрос на добавление лайка фильму {} от пользователя {}", filmId, userId);
        film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (userStorage.getUserById(userId) != null) {
                likeStorage.addLike(filmId, userId);
            } else {
                throw new NotFoundException(errorUser);
            }
        } else {
            throw new NotFoundException(errorFilm);
        }

    }

    public void deleteFilmLike(Integer userId, Integer filmId) {
        log.info("Поступил запрос на удалеие лайка фильму {} от пользователя {}", filmId, userId);
        film = filmStorage.getFilmById(filmId);
        if (film != null) {
            if (film.getLikes().contains(userId)) {
                likeStorage.deleteLike(filmId, userId);
            } else {
                throw new NotFoundException(errorUser);
            }
        } else {
            throw new NotFoundException(errorFilm);
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Поступил запрос на получение {} популярных фильмов", count);
        return likeStorage.getPopular(count);
    }
}
