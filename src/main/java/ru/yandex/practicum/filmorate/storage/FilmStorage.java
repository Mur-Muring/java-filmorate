package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film film);

    Collection<Film> getAllFilms();

    Film updateFilm(Film newFilm);

    Film deleteFilm(Integer id);


    Film getFilmById(Integer id);
}
