package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    final JdbcTemplate jdbcTemplate;
    final RatingService ratingService;
    final LikeStorage likeStorage;
    final GenreService genreService;
    Film film;
    String sql;


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, RatingService ratingService, LikeStorage likeStorage,
                         GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingService = ratingService;
        this.likeStorage = likeStorage;
        this.genreService = genreService;
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("id");

        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        film.setRating_mpa(ratingService.getMpaById(film.getRating_mpa().getId()));

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                genre.setName(genreService.getGenreById(genre.getId()).getName());
            }
            genreService.putGenres(film);
        }
        return film;
    }

    @Override
    public Film deleteFilm(Integer id) {
        film = getFilmById(id);
        sql = "DELETE FROM film WHERE id = ?";
        if (jdbcTemplate.update(sql, id) == 0) {
            throw new NotFoundException("Фильм с id = " + id + " не найден!");
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        int updatedRows = jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating_mpa().getId(),
                film.getId()
        );
        if (updatedRows == 0) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден для обновления!");
        }
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE id = ?", id);
        if (filmRows.first()) {
            Rating rating_mpa = ratingService.getMpaById(filmRows.getInt("rating_id"));
            Set<Genre> genres = genreService.getFilmGenres(id);
            film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                    filmRows.getLong("duration"),
                    new HashSet<>(likeStorage.getLikes(filmRows.getInt("id"))),
                    rating_mpa,
                    genres);
        } else {
            throw new NotFoundException("Фильм с id = " + id + " не найден!");
        }
        if (film.getGenres() != null && film.getGenres().isEmpty()) {
            film.setGenres(null);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        sql = "SELECT * FROM film";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getLong("duration"),
                new HashSet<>(likeStorage.getLikes(rs.getInt("id"))),
                ratingService.getMpaById(rs.getInt("rating_id")),
                genreService.getFilmGenres(rs.getInt("id")))
        );
    }
}