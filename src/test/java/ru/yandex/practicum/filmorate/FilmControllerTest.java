package ru.yandex.practicum.filmorate;

// Не могу проверить пограничные условия, есть предположение что Error...Controller их перехватывает

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    FilmController filmController;
    Film film;

    @BeforeEach
    void start() {
        filmController = new FilmController();
        film = new Film();
        film.setName("Film1");
        film.setDescription("Description1");
        film.setReleaseDate(LocalDate.of(2021, 11, 29));
        film.setDuration(180L);
    }

    @Test
    void createFilm() {
        filmController.createFilm(film);
        assertEquals(filmController.getAllFilms().toArray()[0], film);
    }

    @Test
    void updateFilm() {
        filmController.createFilm(film);
        film.setDuration(100L);
        film.setId(film.getId());
        filmController.updateFilm(film);
        assertEquals(filmController.getAllFilms().toArray()[0], film);
    }
}