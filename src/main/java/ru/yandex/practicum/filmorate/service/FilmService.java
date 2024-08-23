package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;

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

    public void likeFilm(Integer id, Integer userId) {
        log.info("Поступил запрос на добавление лайка фильму {} от пользователя {}", id, userId);
        Film film = filmStorage.getFilmById(id);
        User user = userStorage.getUserById(userId);
        film.addLike(user.getId());
    }

    public void deleteFilmLike(Integer id, Integer userId) {
        log.info("Поступил запрос на удалеие лайка фильму {} от пользователя {}", id, userId);
        Film film = filmStorage.getFilmById(id);
        User user = userStorage.getUserById(userId);
        film.removeLike(user.getId());
    }

    public List<Film> getPopularFilms(Integer count) {
        log.info("Поступил запрос на получение 10 популярных фильмов");
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
