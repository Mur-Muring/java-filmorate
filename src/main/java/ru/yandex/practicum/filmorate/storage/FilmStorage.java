package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

//интерфейс. методы добавления, удаления и модификации объектов.
public interface FilmStorage {
    Film createFilm(Film film);

    Collection<Film> getAllFilms();

    Film updateFilm(Film newFilm);

    void deleteFilm(Film film);

    Film getFilmById(Integer id);
}
